(ns webchange.admin.pages.classes.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :school-classes)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn- set-school-data
  [db class-data]
  (assoc db :school-data class-data))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ {:keys [school-id]}]]
    {:dispatch [::warehouse/load-school {:school-id school-id}
                {:on-success [::load-school-success]}]}))

(re-frame/reg-event-fx
  ::load-school-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school]}]]
    {:db (set-school-data db school)}))

(re-frame/reg-sub
  ::school-data
  :<- [path-to-db]
  :school-data)

(re-frame/reg-sub
  ::school-name
  :<- [::school-data]
  :name)
