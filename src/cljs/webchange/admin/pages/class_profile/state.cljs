(ns webchange.admin.pages.class-profile.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.state :as parent-state]
    [webchange.state.warehouse :as warehouse]
    [webchange.validation.specs.class-spec :as class-spec]
    [webchange.validation.validate :refer [validate]]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:class-profile])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ {:keys [class-id]}]]
    {:dispatch-n [[::set-class-id {:class-id class-id}]
                  [::warehouse/load-class {:class-id class-id}
                   {:on-success [::load-class-success]}]]}))

(re-frame/reg-event-fx
  ::load-class-success
  (fn [{:keys [_]} [_ {:keys [class]}]]
    (let [class-data (select-keys class [:course-id :name])]
      {:dispatch [::reset-class-data class-data]})))

(re-frame/reg-event-fx
  ::reset-class-data
  (fn [{:keys [_]} [_ class-data]]
    {:dispatch-n [[::set-initial-class-data class-data]
                  [::set-class-data class-data]]}))

;; Class id

(def class-id-path (path-to-db [:class-id]))

(defn- get-class-id
  [db]
  (get-in db class-id-path))

(re-frame/reg-event-fx
  ::set-class-id
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db class-id-path value)}))

;; Initial Class Data

(def initial-class-data-path (path-to-db [:initial-class-data]))

(re-frame/reg-sub
  ::initial-class-data
  (fn [db]
    (get-in db initial-class-data-path)))

(re-frame/reg-event-fx
  ::set-initial-class-data
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db initial-class-data-path value)}))

;; Class Data

(def class-data-path (path-to-db [:class-data]))

(re-frame/reg-event-fx
  ::set-class-data
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db class-data-path value)}))

(re-frame/reg-event-fx
  ::update-class-data
  (fn [{:keys [db]} [_ data-patch]]
    {:db (update-in db class-data-path merge data-patch)}))

(defn- get-class-data
  [db]
  (get-in db class-data-path))

(re-frame/reg-sub
  ::class-data
  get-class-data)

(re-frame/reg-sub
  ::has-changes?
  (fn []
    [(re-frame/subscribe [::class-data])
     (re-frame/subscribe [::initial-class-data])])
  (fn [[current-data initial-data]]
    (not= current-data initial-data)))

;; Class Name

(def name-key :name)

(re-frame/reg-sub
  ::class-name
  (fn []
    (re-frame/subscribe [::class-data]))
  (fn [class-data]
    (get class-data name-key "")))

(re-frame/reg-event-fx
  ::set-class-name
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-class-data {name-key value}]}))

(re-frame/reg-sub
  ::name-validation-error
  (fn []
    (re-frame/subscribe [::validation-error name-key]))
  (fn [error]
    error))

;; Validation

(def validation-data-path (path-to-db [:validation-data]))

(re-frame/reg-event-fx
  ::set-validation-errors
  (fn [{:keys [db]} [_ errors]]
    {:db (assoc-in db validation-data-path errors)}))

(re-frame/reg-event-fx
  ::reset-validation-errors
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-validation-errors nil]}))

(re-frame/reg-sub
  ::validation-errors
  (fn [db]
    (get-in db validation-data-path)))

(re-frame/reg-sub
  ::validation-error
  (fn []
    (re-frame/subscribe [::validation-errors]))
  (fn [errors [_ field]]
    (get errors field)))

;; Save

(re-frame/reg-sub
  ::save-button-enabled?
  (fn []
    [(re-frame/subscribe [::has-changes?])])
  (fn [[has-changes?]]
    has-changes?))

(re-frame/reg-event-fx
  ::save-class
  (fn [{:keys [db]} [_]]
    (let [{:keys [class-id]} (get-class-id db)
          class-data (get-class-data db)
          validation-errors (validate ::class-spec/class class-data)]
      (if (nil? validation-errors)
        {:dispatch [::warehouse/save-class {:class-id class-id
                                            :data     class-data}
                    {:on-success [::save-class-success]}]}
        {:dispatch [::set-validation-errors validation-errors]}))))

(re-frame/reg-event-fx
  ::save-class-success
  (fn [{:keys [_]} [_ {:keys [data]}]]
    {:dispatch [::reset-class-data data]}))
