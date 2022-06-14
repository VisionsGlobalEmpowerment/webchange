(ns webchange.admin.pages.student-add.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]))

(def path-to-db :page/student-add)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id]}]]
    {:db (-> db (assoc :school-id school-id))}))

(re-frame/reg-event-fx
  ::open-students-list
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)]
      {:dispatch [::routes/redirect :students :school-id school-id]})))
