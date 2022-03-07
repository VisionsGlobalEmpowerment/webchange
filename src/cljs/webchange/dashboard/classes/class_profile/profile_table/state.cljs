(ns webchange.dashboard.classes.class-profile.profile-table.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.core :as common]
    [webchange.dashboard.classes.class-profile.state :as parent-state]
    [webchange.logger.index :as logger]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:profile-table])
       (parent-state/path-to-db)))

;; Init

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ class-id course-slug]]
    (logger/trace-folded-defs "Init profile table" "class-id" class-id "course-slug" course-slug)
    {:dispatch [::warehouse/load-class-profile class-id course-slug
                {:on-success [::load-class-profile-success]}]}))

(re-frame/reg-event-fx
  ::load-class-profile-success
  (fn [{:keys [_]} [_ response]]
    {:dispatch [::set-class-profile response]}))

;; Class profile

(def class-profile-path (path-to-db [:class-profile]))

(re-frame/reg-event-fx
  ::set-class-profile
  (fn [{:keys [db]} [_ data]]
    (logger/trace-folded "Class profile" data)
    {:db (assoc-in db class-profile-path data)}))

(defn get-class-profile
  [db]
  (get-in db class-profile-path))

(re-frame/reg-sub
  ::class-profile
  get-class-profile)

(defn get-class-id
  [db]
  (-> (get-class-profile db)
      (get :class-id)))

;; Students

(re-frame/reg-event-fx
  ::open-student-profile
  (fn [{:keys [db]} [_ student-id]]
    (let [class-id (get-class-id db)]
      {:redirect [:dashboard-student-profile
                  :class-id class-id
                  :student-id student-id]})))

(defn- time->value
  [time]
  (let [elapsed (-> time (/ 1000) js/Math.round)
        minutes (int (/ elapsed 60))
        seconds (int (- elapsed (* minutes 60)))]
    (str minutes "m " seconds "s")))

(re-frame/reg-sub
  ::students
  (fn []
    [(re-frame/subscribe [::class-profile])])
  (fn [[class-profile]]
    (let [float->percentage (fn [float] (-> float (* 100) js/Math.round (str "%")))
          ->latest-activity (fn [data]
                              (str (:lesson data) " - " (:id data)))
          ->cumulative-score (fn [data]
                               (if data
                                 (let [correct (:correct data)
                                       incorrect (:incorrect data)
                                       overall (+ correct incorrect)]
                                   (if (not= overall 0)
                                     (-> correct (/ overall) float->percentage)
                                     "-"))
                                 "-"))
          ->activity-progress (fn [data]
                                (if data
                                  (let [overall (:course-activities-number class-profile)]
                                    (str data " / " overall))
                                  "-"))
          ->cumulative-time (fn [data]
                              (if data
                                (time->value data)
                                "-"))]
      (->> (get class-profile :stats [])
           (map (fn [profile-record]
                  {:id                (-> profile-record :student :id)
                   :first-name        (-> profile-record :user :first-name)
                   :last-name         (-> profile-record :user :last-name)
                   :started-at        (-> profile-record :data :started-at common/format-date-string)
                   :latest-activity   (-> profile-record :data :latest-activity ->latest-activity)
                   :last-login        (-> profile-record :data :last-login common/format-date-string)
                   :cumulative-score  (-> profile-record :data :cumulative-score ->cumulative-score)
                   :activity-progress (-> profile-record :data :activity-progress ->activity-progress)
                   :cumulative-time   (-> profile-record :data :cumulative-time ->cumulative-time)}))))))
