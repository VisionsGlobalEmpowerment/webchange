(ns webchange.admin.pages.student-profile.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.date :refer [date-str->locale-date ms->duration]]))

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
  (fn [{:keys [db]} [_ {:keys [school-id class-id student-id]}]]
    {:db       (-> db
                   (assoc :class-id class-id)
                   (assoc :school-id school-id)
                   (assoc :student-id student-id)
                   (assoc :loading-student true))
     :dispatch [::load-student-progress]}))

(re-frame/reg-event-fx
  ::load-student-progress
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [student-id]} db]
      {:dispatch [::warehouse/load-class-student-progress
                  {:student-id student-id}
                  {:on-success [::load-student-success]}]})))

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
       :cumulative-time   (ms->duration cumulative-time)})))

(re-frame/reg-sub
  ::student-data
  :<- [::student-progress]
  (fn [{:keys [student]}]
    (let [{:keys [first-name last-name]} (:user student)]
      {:name (str first-name " " last-name)})))

(re-frame/reg-sub
  ::class-data
  :<- [::student-progress]
  (fn [{:keys [class]}]
    (let [{:keys [name]} class]
      {:name name})))

(re-frame/reg-sub
  ::course-data
  :<- [::student-progress]
  (fn [{:keys [course]}]
    (let [{:keys [name]} course]
      {:name name})))

(re-frame/reg-event-fx
  ::open-class-profile-page
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [school-id class-id]} db]
      {:dispatch [::routes/redirect :class-students :school-id school-id :class-id class-id]})))

;; progress data

(defn- ->progress-data
  [activity-data activity-progress course]
  (let [{:keys [activity unique-id]} activity-data
        {:keys [name]} (get-in course [:data :scene-list (keyword activity)])]
    (merge (get activity-progress unique-id {:score 0})
           {:name      name
            :unique-id unique-id})))

(re-frame/reg-sub
  ::progress-data
  :<- [::student-progress]
  :<- [::current-level]
  (fn [[{:keys [activity-stats course]} current-level-idx]]
    (let [activity-progress (reduce (fn [result {:keys [unique-id]}]
                                      (assoc result unique-id {:score 100}))
                                    {}
                                    activity-stats)

          current-level-data (-> (get-in course [:data :levels]) (nth current-level-idx nil))
          lessons (get current-level-data :lessons [])
          lessons-data (->> lessons
                            (map-indexed (fn [idx {:keys [activities]}]
                                           [idx {:name     (str "Lesson " (when (< idx 9) "0") (inc idx))
                                                 :progress (map #(->progress-data % activity-progress course) activities)}]))
                            (into {}))]
      {:lessons (->> (count lessons)
                     (range)
                     (map #(get lessons-data %)))})))

(re-frame/reg-event-fx
  ::update-student-data
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_ student-id]]
    {:dispatch [::warehouse/load-class-student-progress {:student-id student-id} {:on-success [::load-student-success]}]}))

(re-frame/reg-sub
  ::current-level
  :<- [path-to-db]
  #(get % :current-level 0))

(re-frame/reg-sub
  ::level-options
  :<- [::student-progress]
  (fn [{:keys [course]}]
    (->> (get-in course [:data :levels])
         (map-indexed (fn [idx]
                        {:value idx
                         :text  (str "Level " (when (< idx 9) "0") (inc idx))})))))

(re-frame/reg-event-fx
  ::select-level
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ idx]]
    {:db (assoc db :current-level idx)}))

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
