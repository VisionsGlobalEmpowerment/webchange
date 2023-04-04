(ns webchange.state.state-progress
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.core :as core]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  ([]
   (path-to-db []))
  ([relative-path]
   (->> relative-path
        (concat [:progress-data]))))

(def progress-data-path (path-to-db))

(defn get-progress-data
  [db]
  (get-in db progress-data-path))

(def current-activity-key :current-activity)
(def current-activity-path (path-to-db [current-activity-key]))

(defn get-current-activity
  "In case student refreshes the page we want to show them previously played game"
  [db]
  (get-in db current-activity-path))

(re-frame/reg-event-fx
  ::set-current-activity
  (fn [{:keys [db]} [_ activity-data]]
    {:db       (assoc-in db current-activity-path activity-data)
     :dispatch [::progress-data-changed]}))

(re-frame/reg-event-fx
  ::reset-current-activity
  (fn [{:keys [db]} [_]]
    {:db       (update-in db progress-data-path dissoc current-activity-key)
     :dispatch [::progress-data-changed]}))

(defn set-course-in-progress
  [db value]
  (assoc db :course-in-progress value))

(defn course-in-progress?
  [db]
  (get db :course-in-progress))

(re-frame/reg-event-fx
  ::progress-data-changed
  (fn [{:keys [db]} [_ {:keys [on-failure]}]]
    (let [course-id (core/current-course-id db)
          scene-id (core/current-scene-id db)
          school-id (core/current-school-id db)
          progress (get-progress-data db)
          events (:pending-events db)
          loading? (warehouse/request-in-progress? db :save-progress-data)
          sandbox? (-> db :sandbox :enabled)
          course-in-progress (course-in-progress? db)]
      (when-not sandbox?
        (cond
          loading? {:db (assoc db :schedule-save-progress true)}
          (some? progress) {:db       (dissoc db :pending-events)
                            :dispatch [::warehouse/save-progress-data
                                       {:course-slug   course-id                                        
                                        :progress-data {:scene-id scene-id
                                                        :school-id school-id
                                                        :progress progress
                                                        :events events
                                                        :course-in-progress course-in-progress}}
                                       (cond-> {:on-success [::save-progress-success]}
                                               (some? on-failure) (assoc :on-failure on-failure))]}
          :else {})))))

(re-frame/reg-event-fx
  ::save-progress-success
  (fn [{:keys [db]} _]
    (let [scheduled? (:schedule-save-progress db)]
      (if scheduled?
        {:db       (assoc db :schedule-save-progress false)
         :dispatch [::progress-data-changed]}
        {}))))
