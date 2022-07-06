(ns webchange.admin.pages.course-view.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :course-view)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-sub
  ::data-saving?
  :<- [path-to-db]
  (fn [db]
    (get db :saving)))

(re-frame/reg-sub
  ::data-loading?
  :<- [path-to-db]
  (fn [db]
    (get db :loading)))

(re-frame/reg-sub
  ::course-data
  :<- [path-to-db]
  (fn [db]
    (get db :course-data)))

(re-frame/reg-event-fx
  ::open-course-list
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :courses]}))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [course-slug]}]]
    {:db (-> db
             (assoc :course-slug course-slug)
             (assoc :loading true))
     :dispatch [::warehouse/load-course-info
                course-slug
                {:on-success [::load-success]}]}))

(re-frame/reg-event-fx
  ::load-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {course-id :id :as course-info}]]
    {:db (-> db
             (assoc :loading false)
             (assoc :course-id course-id)
             (assoc :course-data course-info))}))

(re-frame/reg-event-fx
  ::save-course
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ course-data]]
    {:db       (assoc db :saving true)
     :dispatch [::warehouse/save-course-info
                {:course-id (:course-id db)
                 :data course-data}
                {:on-success [::save-success]
                 :on-failure [::save-failure]}]}))

(re-frame/reg-event-fx
  ::save-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {id :id}]]
    {:db (-> db
             (assoc :saving false))}))

(re-frame/reg-event-fx
  ::save-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :saving false)}))

(re-frame/reg-event-fx
  ::reset-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (dissoc db :course-data)}))

;; Remove

(def remove-window-state-key :remove-window-state)

(def remove-window-default-state {:open?        false
                                  :in-progress? false
                                  :done?        false})

(defn- update-remove-window-state
  [db data-patch]
  (update db remove-window-state-key merge data-patch))

(re-frame/reg-sub
  ::remove-window-state
  :<- [path-to-db]
  #(get % remove-window-state-key remove-window-default-state))

(re-frame/reg-event-fx
  ::open-remove-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-window-state db {:open? true})}))

(re-frame/reg-event-fx
  ::close-remove-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-window-state db remove-window-default-state)}))

(re-frame/reg-event-fx
  ::handle-removed
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:dispatch [::routes/redirect :courses]}))

;; Archive

(re-frame/reg-event-fx
  ::archive-course
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (assoc db :saving true)
     :dispatch [::warehouse/archive-course
                (:course-slug db)
                {:on-success [::archive-success]
                 :on-failure [::archive-failure]}]}))

(re-frame/reg-event-fx
  ::archive-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (assoc :saving false)
             (update-remove-window-state {:done? true}))}))

(re-frame/reg-event-fx
  ::archive-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (assoc :saving false))}))
