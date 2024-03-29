(ns webchange.admin.pages.class-students.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.progress.activity :as activity]
    [webchange.utils.date :refer [date-str->locale-date ms->duration]]))

(def path-to-db :page/class-students)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id class-id]}]]
    {:db         (-> db
                     (assoc :school-id school-id)
                     (assoc :class-id class-id)
                     (assoc :loading-class true)
                     (assoc :loading-course true)
                     (assoc :loading-progress true))
     :dispatch-n [[::warehouse/load-class {:class-id class-id} {:on-success [::load-class-success]}]
                  [::warehouse/load-class-course {:class-id class-id} {:on-success [::load-course-success]}]
                  [::warehouse/load-class-students-progress {:class-id class-id} {:on-success [::load-progress-success]}]
                  [::warehouse/load-school {:school-id school-id}
                   {:on-success [::load-school-success]}]]}))

(re-frame/reg-event-fx
  ::load-class-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class]}]]
    {:db (-> db
             (assoc :loading-class false)
             (assoc :class class))}))

(re-frame/reg-event-fx
  ::load-school-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school]}]]
    {:db (assoc db :school-data school)}))

(re-frame/reg-sub
  ::readonly?
  :<- [path-to-db]
  #(get-in % [:school-data :readonly] false))

;; course data

(def course-data-key :course-data)

(defn- set-course-data
  [db value]
  (assoc db course-data-key value))

(re-frame/reg-sub
  ::course-data
  :<- [path-to-db]
  #(get % course-data-key))

(re-frame/reg-event-fx
  ::load-course-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ course-data]]
    {:db (set-course-data db course-data)}))

;; progress

(def students-progress-key :students-progress)

(defn- set-students-progress
  [db value]
  (assoc db students-progress-key value))

(re-frame/reg-sub
  ::students-progress
  :<- [path-to-db]
  #(get % students-progress-key {:progress []
                                 :students []}))

(re-frame/reg-event-fx
  ::load-progress-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (set-students-progress db data)}))

;; prepared progress data

(defn- user->user-name
  [{:keys [first-name last-name]}]
  (str first-name " " last-name))

(defn- progress->progress-data
  [{:keys [data unique-id]}]
  (let [{:keys [last-played time-spent score]} data]
    {:last-played (date-str->locale-date last-played)
     :score-value 100
     :score       score
     :time-spent  (ms->duration time-spent)
     :unique-id   unique-id}))

(re-frame/reg-sub
  ::progress-data
  :<- [::students-progress]
  :<- [::course-data]
  :<- [::current-activities]
  (fn [[{:keys [progress students]} course-data activities]]
    (let [activities-data (->> activities
                               (map (fn [{:keys [scene-id unique-id]}]
                                      [unique-id (merge {:unique-id unique-id
                                                         :category  "Foundational Literacy"}
                                                        (get-in course-data [:data :scene-list (-> scene-id str keyword)]))]))
                               (into {}))
          activities-list (map :unique-id activities)
          students-data (->> students
                             (map (fn [{:keys [id access-code user user-id]}]
                                    [user-id {:id          id
                                              :access-code access-code
                                              :name        (user->user-name user)
                                              :progress    (->> activities-list
                                                                (map (fn [unique-id]
                                                                       [unique-id {:score-value 0
                                                                                   :unique-id unique-id}]))
                                                                (into {}))}]))
                             (into {}))
          students-data (reduce (fn [students-data {:keys [unique-id user-id] :as progress}]
                                  (->> (progress->progress-data progress)
                                       (assoc-in students-data [user-id :progress unique-id])))
                                students-data
                                progress)
          students-list (map :user-id students)]
      {:activities (map (fn [unique-id]
                          (get activities-data unique-id))
                        activities-list)
       :students   (map (fn [user-id]
                          (-> (get students-data user-id)
                              (assoc :user-id user-id
                                     :progress (map (fn [unique-id]
                                                      (get-in students-data [user-id :progress unique-id]))
                                                    activities-list))))
                        students-list)})))

(re-frame/reg-sub
  ::class
  :<- [path-to-db]
  #(get % :class))

(re-frame/reg-sub
  ::current-level
  :<- [path-to-db]
  #(get % :current-level 0))

(re-frame/reg-sub
  ::current-lesson
  :<- [path-to-db]
  #(get % :current-lesson 0))

(re-frame/reg-sub
  ::level-options
  :<- [::course-data]
  (fn [course]
    (->> course
         :data
         :levels
         (count)
         (range)
         (map (fn [idx] {:value idx
                         :text  (str "Level " (inc idx))})))))

(re-frame/reg-sub
  ::lesson-options
  :<- [::course-data]
  :<- [::current-level]
  (fn [[course current-level]]
    (let [level (-> course
                    :data
                    :levels
                    (get current-level))]
      (->> level
           :lessons
           (count)
           (range)
           (map (fn [idx] {:value idx
                           :text  (str "Lesson " (inc idx))}))))))

(re-frame/reg-event-fx
  ::select-level
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ idx]]
    {:db (assoc db :current-level idx)}))

(re-frame/reg-event-fx
  ::select-lesson
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ idx]]
    {:db (assoc db :current-lesson idx)}))

(re-frame/reg-sub
  ::current-activities
  :<- [::course-data]
  :<- [::current-level]
  :<- [::current-lesson]
  :<- [::show-only-assessments?]
  (fn [[course current-level current-lesson only-assessments?]]
    (let [levels (-> course :data :levels)
          all-activities (activity/flatten-active-activities levels)
          with-assessment-data (fn [{:keys [scene-id] :as activity}]
                                 (assoc activity :assessment (get-in course [:data :scene-list (-> scene-id str keyword) :assessment])))]
      (if only-assessments?
        (->> all-activities
             (map with-assessment-data)
             (filter #(:assessment %)))
        (->> all-activities
             (filter #(and (= current-level (:level %)) (= current-lesson (:lesson %)))))))))

(re-frame/reg-event-fx
  ::open-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    (let [school-id (:school-id db)]
      {:dispatch [::routes/redirect :student-profile
                  :school-id school-id
                  :student-id student-id]})))

(re-frame/reg-event-fx
  ::open-add-students-page
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [school-id (:school-id db)
          class-id (:class-id db)]
      {:dispatch [::routes/redirect :class-students-add :school-id school-id :class-id class-id]})))

(re-frame/reg-event-fx
  ::open-student-profile-page
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    (let [school-id (:school-id db)]
      {:dispatch [::routes/redirect :student-profile :school-id school-id :student-id student-id]})))

(re-frame/reg-sub
  ::show-only-assessments?
  :<- [path-to-db]
  (fn [db]
    (get db :show-only-assessments? false)))

(re-frame/reg-event-fx
  ::set-show-only-assessments
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db :show-only-assessments? value)}))

(comment
  (-> @(re-frame/subscribe [::course-data])
      :data
      :scene-list)
  (-> @(re-frame/subscribe [::students-progress])))
