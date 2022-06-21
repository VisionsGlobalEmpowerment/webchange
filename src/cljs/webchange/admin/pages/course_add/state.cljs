(ns webchange.admin.pages.course-add.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :course-add)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-sub
  ::data-saving?
  :<- [path-to-db]
  (fn [db]
    (get db :saving)))

(re-frame/reg-event-fx
  ::open-course-list
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :courses]}))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [_]}]]
    {}))

(re-frame/reg-event-fx
  ::create-course
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ course-data]]
    {:db       (assoc db :saving true)
     :dispatch [::warehouse/create-course
                {:course-data course-data}
                {:on-success [::save-success]
                 :on-failure [::save-failure]}]}))

(re-frame/reg-event-fx
  ::save-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {id :id}]]
    {:db        (-> db
                    (assoc :saving false))
     :dispatch [::routes/redirect :course-view :course-id id]}))

(re-frame/reg-event-fx
  ::save-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :saving false)}))
