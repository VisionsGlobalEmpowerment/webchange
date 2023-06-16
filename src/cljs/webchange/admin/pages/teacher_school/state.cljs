(ns webchange.admin.pages.teacher-school.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.auth.state :as auth]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/teacher-school)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {}))

(re-frame/reg-event-fx
  ::open-school-profile
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [id]}]]
    {:db (assoc db :current-school id)}))

(re-frame/reg-sub
  ::current-school
  :<- [path-to-db]
  :<- [::auth/current-user]
  (fn [[db user]]
    (or
     (get user :school-id)
     (get db :current-school))))
