(ns webchange.admin.widgets.class-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.state :as parent-state]
    [webchange.validation.specs.class-spec :as spec]
    [webchange.validation.validate :refer [validate]]))

(defn path-to-db
  [id relative-path]
  {:pre [(some? id) (sequential? relative-path)]}
  (->> relative-path
       (concat [:class-form id])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ id {:keys [data on-change]}]]
    {:dispatch-n [[::set-callbacks id {:on-change on-change}]
                  [::set-current-data id data]]}))

;; Current Data

(def current-data-path [:current-data])

(defn- get-current-data
  [db id]
  (->> (path-to-db id current-data-path)
       (get-in db)))

(re-frame/reg-sub
  ::current-data
  (fn [db [_ id]]
    (get-current-data db id)))

(re-frame/reg-event-fx
  ::set-current-data
  (fn [{:keys [db]} [_ id value]]
    {:db (assoc-in db (path-to-db id current-data-path) value)}))

(re-frame/reg-event-fx
  ::update-current-data
  (fn [{:keys [db]} [_ id data-patch]]
    {:db       (update-in db (path-to-db id current-data-path) merge data-patch)
     :dispatch [::handle-change id]}))

;; Callbacks

(def callbacks-path [:callbacks])

(defn- get-callbacks
  [db id]
  (->> (path-to-db id callbacks-path)
       (get-in db)))

(re-frame/reg-event-fx
  ::set-callbacks
  (fn [{:keys [db]} [_ id value]]
    {:db (assoc-in db (path-to-db id callbacks-path) value)}))

(re-frame/reg-event-fx
  ::handle-change
  (fn [{:keys [db]} [_ id]]
    (let [{:keys [on-change]} (get-callbacks db id)
          current-data (get-current-data db id)]
      (cond-> {}
              (some? on-change) (assoc :dispatch (conj on-change current-data))))))

;; Class Name

(def name-key :name)

(re-frame/reg-sub
  ::class-name
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::current-data id])])
  (fn [[current-data]]
    (get current-data name-key "")))

(re-frame/reg-event-fx
  ::set-class-name
  (fn [{:keys [_]} [_ id value]]
    {:dispatch [::update-current-data id {name-key value}]}))

(re-frame/reg-sub
  ::class-name-validation-error
  (fn [[_ id]]
    (re-frame/subscribe [::class-name id]))
  (fn [class-name]
    (validate ::spec/name class-name)))
