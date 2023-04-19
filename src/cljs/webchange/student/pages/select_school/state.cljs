(ns webchange.student.pages.select-school.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]
    [webchange.events :as events]))

(def path-to-db :student-select-school/index)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn- save-school-id!
  [school-id]
  (js/console.log "save school id?")
  (let [local-storage (.-localStorage js/window)]
    (.setItem local-storage "saved-school-id" school-id)))

(defn- get-saved-school-id
  []
  (let [local-storage (.-localStorage js/window)]
    (.getItem local-storage "saved-school-id")))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [_db]} [_]]
    (if-let [saved-school-id (get-saved-school-id)]
      {:dispatch [::events/redirect :school-student-login :school-id saved-school-id]}
      {:dispatch [::warehouse/load-schools {:on-success [::load-schools-success]}]})))

(re-frame/reg-event-fx
  ::load-schools-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [schools]}]]
    {:db (assoc db :schools schools)}))

(re-frame/reg-event-fx
  ::select-school
  [(i/path path-to-db)]
  (fn [{:keys [_db]} [_ school-id]]
    (save-school-id! school-id)
    {:dispatch [::events/redirect :school-student-login :school-id school-id]}))

(re-frame/reg-sub
  ::schools
  :<- [path-to-db]
  (fn [db]
    (get db :schools)))
