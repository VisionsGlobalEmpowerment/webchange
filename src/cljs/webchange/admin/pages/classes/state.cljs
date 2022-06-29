(ns webchange.admin.pages.classes.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :school-classes)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn- get-school-data
  [db]
  (get db :school-data))

(defn- set-school-data
  [db school-data]
  (assoc db :school-data school-data))

(defn- set-school-classes
  [db classes]
  (assoc db :classes classes))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ {:keys [school-id]}]]
    {:dispatch-n [[::warehouse/load-school {:school-id school-id} {:on-success [::load-school-success]}]
                  [::warehouse/load-school-classes {:school-id school-id} {:on-success [::load-school-classes-success]}]]}))

(re-frame/reg-event-fx
  ::load-school-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school]}]]
    {:db (set-school-data db school)}))

(re-frame/reg-event-fx
  ::load-school-classes-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [classes]}]]
    {:db (set-school-classes db classes)}))

(re-frame/reg-sub
  ::school-data
  :<- [path-to-db]
  get-school-data)

(re-frame/reg-sub
  ::school-name
  :<- [::school-data]
  #(get % :name ""))

(re-frame/reg-sub
  ::classes
  :<- [path-to-db]
  (fn [data]
    (get data :classes [])))

(re-frame/reg-sub
  ::classes-number
  :<- [::classes]
  #(count %))

(re-frame/reg-sub
  ::classes-list-data
  :<- [::classes]
  (fn [classes]
    (map (fn [{:keys [created-at] :as class-data}]
           (-> class-data
               (select-keys [:id :name :stats])
               (update :stats #(merge {:students 0 :teachers 0} %))
               (merge {:name-description [["Created" created-at]]})))
         classes)))

(re-frame/reg-event-fx
  ::add-class
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:id (get-school-data db))]
      {:dispatch [::routes/redirect :class-add :school-id school-id]})))

(re-frame/reg-event-fx
  ::edit-class
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ class-id]]
    (let [school-id (:id (get-school-data db))]
      {:dispatch [::routes/redirect :class-profile :school-id school-id :class-id class-id]})))

(re-frame/reg-event-fx
  ::open-class-students
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ class-id]]
    (let [school-id (:id (get-school-data db))]
      {:dispatch [::routes/redirect :class-students :school-id school-id :class-id class-id]})))

(re-frame/reg-event-fx
  ::open-class-teachers
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ class-id]]
    (let [school-id (:id (get-school-data db))]
      {:dispatch [::routes/redirect :class-profile :school-id school-id :class-id class-id]})))
