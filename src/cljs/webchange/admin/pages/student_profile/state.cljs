(ns webchange.admin.pages.student-profile.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.date :refer [date-str->locale-date ms->time]]))

(def path-to-db :page/student-profile)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; student progress

(def student-progress-key :student-progress)

(defn- set-student-progress
  [db value]
  (assoc db student-progress-key value))

(re-frame/reg-sub
  ::student-progress
  :<- [path-to-db]
  #(get % student-progress-key))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class-id student-id]}]]
    {:db       (-> db
                   (assoc :class-id class-id)
                   (assoc :student-id student-id)
                   (assoc :loading-student true))
     :dispatch [::warehouse/load-class-student-progress
                {:student-id student-id}
                {:on-success [::load-student-success]}]}))

(re-frame/reg-event-fx
  ::load-student-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (set-student-progress db data)}))

(re-frame/reg-sub
  ::course-statistics
  :<- [::student-progress]
  (fn [{:keys [course-stats]}]
    (let [{:keys [activity-progress cumulative-time started-at last-login]} (:data course-stats)]
      {:started-at        (date-str->locale-date started-at)
       :last-login        (date-str->locale-date last-login)
       :activity-progress activity-progress
       :cumulative-time   (ms->time cumulative-time)})))

(re-frame/reg-sub
  ::student-data
  :<- [::student-progress]
  (fn [{:keys [student]}]
    (let [{:keys [first-name last-name]} (:user student)]
      {:name (str first-name " " last-name)})))

;; ---------------------------

(re-frame/reg-event-fx
  ::update-student-data
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    {:dispatch [::warehouse/load-class-student-progress {:student-id student-id} {:on-success [::load-student-success]}]}))

(re-frame/reg-sub
  ::student
  :<- [path-to-db]
  #(get % :student))

(re-frame/reg-sub
  ::course-stats
  :<- [path-to-db]
  #(get % :course-stats))

(re-frame/reg-sub
  ::activity-stats
  :<- [path-to-db]
  #(get % :activity-stats))

(re-frame/reg-sub
  ::class
  :<- [path-to-db]
  #(get % :class))

(re-frame/reg-sub
  ::course
  :<- [path-to-db]
  #(get % :course))

(re-frame/reg-sub
  ::current-level
  :<- [path-to-db]
  #(get % :current-level 0))

(re-frame/reg-sub
  ::level-options
  :<- [::course]
  (fn [course]
    (->> course
         :data
         :levels
         (count)
         (range)
         (map (fn [idx] {:value idx
                         :text  (str "Level " (inc idx))})))))

(re-frame/reg-event-fx
  ::select-level
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ idx]]
    {:db (assoc db :current-level idx)}))

(re-frame/reg-sub
  ::current-lessons
  :<- [::course]
  :<- [::current-level]
  (fn [[course current-level]]
    (-> course
        :data
        :levels
        (get current-level)
        :lessons)))

(defn- ->activity-stats
  [activities {unique-id :unique-id activity-key :activity :as course-activity} stats]
  (let [activity-name (->> activity-key keyword (get activities) :name)
        stat (get stats unique-id)]
    {:id          unique-id
     :name        activity-name
     :completed?  (some? (:score stat))
     :last-played (:last-played stat)
     :total-time  (:total-time stat)}))

(defn- ->lesson-stats
  [activities lesson activity-stats]
  (->> lesson
       :activities
       (map #(->activity-stats activities % activity-stats))))

(re-frame/reg-sub
  ::lessons-data
  :<- [::course]
  :<- [::current-lessons]
  :<- [::activity-stats]
  (fn [[course current-lessons activity-stats]]
    (let [activities (-> course :data :scene-list)]
      (->> current-lessons
           (map-indexed vector)
           (reduce (fn [result [idx lesson]]
                     (let [lesson-activities (->lesson-stats activities lesson activity-stats)]
                       (-> result
                           (update :data conj {:id         idx
                                               :name       (str "Lesson " (inc idx))
                                               :activities lesson-activities})
                           (update :max-activities max (count lesson-activities)))))
                   {:data           []
                    :max-activities 0})))))

;; Side Bar Content

(def side-bar-key :side-bar)

(defn- set-side-bar
  [db value]
  (assoc db side-bar-key value))

(re-frame/reg-sub
  ::side-bar
  :<- [path-to-db]
  #(get % side-bar-key :student-profile))

(re-frame/reg-event-fx
  ::open-student-profile
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-side-bar db :student-profile)}))

(re-frame/reg-event-fx
  ::open-complete-class
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-side-bar db :complete-class)}))
