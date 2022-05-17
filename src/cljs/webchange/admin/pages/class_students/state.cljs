(ns webchange.admin.pages.class-students.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :class-students)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class-id]}]]
    {:db (-> db
             (assoc :class-id class-id)
             (assoc :loading-class true)
             (assoc :loading-course true)
             (assoc :loading-progress true))
     :dispatch-n [[::warehouse/load-class {:class-id class-id} {:on-success [::load-class-success]}]
                  [::warehouse/load-class-course {:class-id class-id} {:on-success [::load-course-success]}]
                  [::warehouse/load-class-students-progress {:class-id class-id} {:on-success [::load-progress-success]}]]}))

(re-frame/reg-event-fx
  ::load-class-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class]}]]
    {:db (-> db
             (assoc :loading-class false)
             (assoc :class class))}))

(re-frame/reg-event-fx
  ::load-course-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ course]]
    {:db (-> db
             (assoc :loading-course false)
             (assoc :course course))}))

(defn- prepare-progress
  "Prepare progress for quick access. Expects a list of activity-stats.
  Returns nested map:
  outer - grouped by user id
  inner - uses activity unique-id as a key"
  [progress]
  (let [grouped (group-by :user-id progress)
        ->by-unique-id #(->> % (map (juxt :unique-id identity)) (into {}))]
    (->> grouped
         (map ->by-unique-id)
         (into {}))))

(defn- prepare-student
  [{:keys [access-code user]}]
  {:name (str (:first-name user) " " (:last-name user))
   :code access-code})

(re-frame/reg-event-fx
  ::load-progress-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [students progress]}]]
    {:db (-> db
             (assoc :loading-progress false)
             (assoc :students (map prepare-student students))
             (assoc :progress (prepare-progress progress)))}))

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
  ::current-lesson
  :<- [path-to-db]
  #(get % :current-lesson 0))

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

(re-frame/reg-sub
  ::lesson-options
  :<- [::course]
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
                           :text (str "Lesson " (inc idx))}))))))

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
  :<- [::course]
  :<- [::current-level]
  :<- [::current-lesson]
  (fn [[course current-level current-lesson]]
    (-> course
        :data
        :levels
        (get current-level)
        :lessons
        (get current-lesson)
        :activities)))

(re-frame/reg-sub
  ::lesson-data
  :<- [::course]
  :<- [::current-activities]
  (fn [[course current-activities]]
    (let [activities (->> current-activities
                          (map (fn [{:keys [activity unique-id]}]
                                 (let [{:keys [name preview]} (-> course :data :scene-list
                                                                  (get (keyword activity)))]
                                   {:id unique-id
                                    :name name
                                    :preview preview}))))]
      {:name (:name course)
       :activities activities})))

(re-frame/reg-sub
  ::students-progress
  :<- [path-to-db]
  (fn [data]
    (select-keys data [:students :progress])))

(re-frame/reg-sub
  ::students-data
  :<- [::current-activities]
  :<- [::students-progress]
  (fn [[activities {:keys [students progress]}]]
    (let [user-progress (fn [user-id]
                          (let [p (get progress user-id)]
                            (map (fn [activity]
                                   (let [stat-data (-> p (get (:unique-id activity)) :data)]
                                     {:id (:unique-id activity)
                                      :completed (some? (:score stat-data))
                                      :last-played (:last-played stat-data)
                                      :total-time (:time-played stat-data)})) activities)))
          with-progress #(assoc % :activities (user-progress (:user-id %)))]
      (->> students
           (map with-progress)))))
