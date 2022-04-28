(ns webchange.admin.pages.students.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.state :as parent-state]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:students])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ {:keys [school-id]}]]
    {:dispatch [::warehouse/load-school {:school-id school-id}
                {:on-success [::load-school-success]}]}))

(re-frame/reg-event-fx
  ::load-school-success
  (fn [{:keys [_]} [_ {:keys [school]}]]
    {:dispatch [::set-school-data school]}))

(def school-data-path (path-to-db [:school-data]))

(defn- get-school-data
  [db]
  (get-in db school-data-path))

(defn- get-school-id
  [db]
  (-> (get-school-data db)
      (get :id)))

(re-frame/reg-sub
  ::school-data
  get-school-data)

(re-frame/reg-event-fx
  ::set-school-data
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db school-data-path value)}))

(re-frame/reg-sub
  ::school-name
  (fn []
    (re-frame/subscribe [::school-data]))
  (fn [school-data]
    (get school-data :name "No Name")))

(re-frame/reg-sub
  ::students-number
  (fn []
    (re-frame/subscribe [::school-data]))
  (fn [school-data]
    (get-in school-data [:students :number] 0)))

(re-frame/reg-event-fx
  ::add-student
  (fn [{:keys [db]} [_]]
    (let [school-id (get-school-id db)]
      (print "Add student" school-id)
      {:dispatch [::routes/redirect :add-student :school-id school-id]})))