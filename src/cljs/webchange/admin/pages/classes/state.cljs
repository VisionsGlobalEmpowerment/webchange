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
  :school-data)

(re-frame/reg-sub
  ::school-name
  :<- [::school-data]
  :name)

(re-frame/reg-sub
  ::classes
  :<- [path-to-db]
  :classes)

(re-frame/reg-sub
  ::classes-list-data
  :<- [::classes]
  (fn [classes]
    (map (fn [{:keys [id img name stats]}]
           {:id          id
            :name        name
            :description [["Created" "16 / 03 / 2022"]]
            :img         img
            :stats       stats})
         classes)))

(re-frame/reg-event-fx
  ::add-class
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    (print "::add-class")))

(re-frame/reg-event-fx
  ::edit-class
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ class-id]]
    (print "::edit-class" class-id)))

(re-frame/reg-event-fx
  ::remove-class
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ class-id]]
    (print "::remove-class" class-id)))
