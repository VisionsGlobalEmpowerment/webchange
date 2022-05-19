(ns webchange.admin.pages.student-profile.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :student-profile)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class-id student-id]}]]
    {:db (-> db
             (assoc :class-id class-id)
             (assoc :student-id student-id)
             (assoc :loading-student true))
     :dispatch-n [[::warehouse/load-class-student-progress {:student-id student-id} {:on-success [::load-student-success]}]]}))

(defn- prepare-student
  [{:keys [user]}]
  {:name (str (:first-name user) " " (:last-name user))})

(defn- prepare-progress
  "Prepare progress for quick access. Expects a list of activity-stats.
  uses activity unique-id as a key"
  [progress]
  (->> progress
       (map (juxt :unique-id identity))
       (into {})))

(re-frame/reg-event-fx
  ::load-student-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [student class course course-stats activity-stats]}]]
    {:db (-> db
             (assoc :loading-student false)
             (assoc :student (prepare-student student))
             (assoc :class class)
             (assoc :course course)
             (assoc :course-stats course-stats)
             (assoc :activity-stats (prepare-progress activity-stats)))}))

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
                         :text (str "Level " (inc idx))})))))

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
    {:id unique-id
     :name activity-name
     :completed? (some? (:score stat))
     :last-played (:last-played stat)
     :total-time (:total-time stat)}))

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
    (let [activities (-> course :data :scene-list)
          lessons (->> current-lessons
                       (map-indexed (fn [idx lesson]
                                      {:id idx
                                       :name (str "Lesson " (inc idx))
                                       :activities (->lesson-stats activities lesson activity-stats)})))]
      lessons)))
