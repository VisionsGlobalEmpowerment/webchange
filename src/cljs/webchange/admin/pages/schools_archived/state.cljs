(ns webchange.admin.pages.schools-archived.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :schools-archived)

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

(re-frame/reg-sub
  ::schools-list
  :<- [path-to-db]
  (fn [data]
    (get data :schools [])))

(re-frame/reg-event-fx
  ::restore-school
  (fn [{:keys [db]} [_ school-id]]
    (print "Restore school:" school-id)
    {}))
