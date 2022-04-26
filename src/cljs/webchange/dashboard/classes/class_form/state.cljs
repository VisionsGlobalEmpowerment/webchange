(ns webchange.dashboard.classes.class-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.validation.state :as validation-state]
    [webchange.dashboard.classes.state :as parent-state]
    [webchange.state.warehouse :as warehouse]
    [webchange.validation.specs.class-spec :as class-spec]
    [webchange.validation.validate :refer [validate]]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:class-form])
       (parent-state/path-to-db)))

(def validation-state-id :dashboard-class-form)

(re-frame/reg-event-fx
  ::open-edit-class-window
  (fn [{:keys [_]} [_ class-id handlers]]
    {:dispatch-n [[::load-class class-id]
                  [::set-form-state {:action       "edit"
                                     :class-id     class-id
                                     :handlers     handlers
                                     :window-open? true
                                     :window-title "Edit class"}]]}))

(re-frame/reg-event-fx
  ::open-add-class-window
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::set-form-state {:action       "add"
                                     :window-open? true
                                     :window-title "Add class"}]]}))

;; Form state

(def form-state-path (path-to-db [:form-state]))

(re-frame/reg-event-fx
  ::set-form-state
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db form-state-path data)}))

(re-frame/reg-event-fx
  ::reset-form-state
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-form-state {:window-open? false}]}))

(defn- get-form-state
  [db]
  (get-in db form-state-path))

(re-frame/reg-sub
  ::form-state
  get-form-state)

(re-frame/reg-sub
  ::window-open?
  (fn []
    [(re-frame/subscribe [::form-state])])
  (fn [[form-state]]
    (get form-state :window-open? false)))

(re-frame/reg-sub
  ::window-title
  (fn []
    [(re-frame/subscribe [::form-state])])
  (fn [[form-state]]
    (get form-state :window-title "Class form")))

(defn get-class-id
  [db]
  (-> (get-form-state db)
      (get :class-id)))

(defn get-action
  [db]
  (-> (get-form-state db)
      (get :action)))

(defn get-handlers
  [db]
  (-> (get-form-state db)
      (get :handlers)))

;; Form data

(def form-data-path (path-to-db [:form-data]))

(re-frame/reg-event-fx
  ::set-form-data
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db form-data-path data)}))

(re-frame/reg-event-fx
  ::update-form-data
  (fn [{:keys [db]} [_ field-name value]]
    {:db (update-in db form-data-path assoc field-name value)}))

(re-frame/reg-event-fx
  ::reset-form-data
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-form-data {}]}))

(defn get-form-data
  [db]
  (get-in db form-data-path))

(re-frame/reg-sub
  ::form-data
  get-form-data)

;; Course

(def course-id-key :course-id)

(re-frame/reg-sub
  ::course-id
  (fn []
    [(re-frame/subscribe [::form-data])])
  (fn [[form-data]]
    (get form-data course-id-key)))

(re-frame/reg-event-fx
  ::set-course-id
  (fn [{:keys [_]} [_ course-id]]
    {:dispatch [::update-form-data course-id-key course-id]}))

(re-frame/reg-sub
  ::course-id-error
  (fn []
    [(re-frame/subscribe [::validation-state/error validation-state-id course-id-key])])
  (fn [[error]]
    error))

(re-frame/reg-sub
  ::course-options
  (fn [_]
    (->> [{:id 4 :name "english"}
          {:id 2 :name "spanish"}
          {:id 1 :name "demo"}]
         (map (fn [{:keys [id name]}]
                {:text  name
                 :value id})))))

;; Name

(def name-key :name)

(re-frame/reg-sub
  ::name
  (fn []
    [(re-frame/subscribe [::form-data])])
  (fn [[form-data]]
    (get form-data name-key)))

(re-frame/reg-event-fx
  ::set-name
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-form-data name-key value]}))

(re-frame/reg-sub
  ::name-error
  (fn []
    [(re-frame/subscribe [::validation-state/error validation-state-id name-key])])
  (fn [[error]]
    error))

;; Load class

(re-frame/reg-event-fx
  ::load-class
  (fn [{:keys [_]} [_ class-id]]
    {:dispatch [::warehouse/load-class {:class-id class-id} {:on-success [::load-class-success]}]}))

(re-frame/reg-event-fx
  ::load-class-success
  (fn [{:keys [_]} [_ response]]
    (let [class-data (:class response)]
      {:dispatch [::set-form-data (select-keys class-data [name-key course-id-key])]})))

;; Save

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_]]
    (let [action (get-action db)
          form-data (get-form-data db)
          validation-errors (validate ::class-spec/class form-data)]
      (if (nil? validation-errors)
        {:dispatch-n [[::validation-state/reset-errors validation-state-id]
                      [(case action
                         "add" ::add-class
                         "edit" ::edit-class)
                       {:on-success [::save-success]}]]}
        {:dispatch [::validation-state/set-errors validation-state-id validation-errors]}))))

(re-frame/reg-event-fx
  ::save-success
  (fn [{:keys [db]} [_]]
    (let [{:keys [on-success]} (get-handlers db)]
      {:dispatch-n (cond-> [[::reset]]
                           (some? on-success) (conj on-success))})))

(re-frame/reg-event-fx
  ::add-class
  (fn [{:keys [db]} [_ handlers]]
    (let [form-data (get-form-data db)]
      {:dispatch [::parent-state/add-class
                  {:data form-data}
                  handlers]})))

(re-frame/reg-event-fx
  ::edit-class
  (fn [{:keys [db]} [_ handlers]]
    (let [class-id (get-class-id db)
          form-data (get-form-data db)]
      {:dispatch [::parent-state/edit-class
                  {:id   class-id
                   :data form-data}
                  handlers]})))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::reset-form-data]
                  [::reset-form-state]]}))
