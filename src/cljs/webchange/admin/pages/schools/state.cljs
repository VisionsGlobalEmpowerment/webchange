(ns webchange.admin.pages.schools.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :schools)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} _]
    {:dispatch [::warehouse/load-schools
                {:on-success [::load-schools-success]}]}))

(re-frame/reg-event-fx
  ::load-schools-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [schools]}]]
    {:db (assoc db :schools schools)}))

(re-frame/reg-event-fx
  ::add-school
  (fn [{:keys [db]} [_]]
    {:dispatch [::routes/redirect :add-school]}))

(re-frame/reg-event-fx
  ::edit-school
  (fn [{:keys [db]} [_ school-id]]
    {:dispatch [::routes/redirect :school-profile :school-id school-id]}))

(re-frame/reg-event-fx
  ::remove-school
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ school-id]]
    (print "::remove-school" school-id)))

(re-frame/reg-sub
  ::schools-list
  :<- [path-to-db]
  (fn [data]
    (get data :schools [])))
