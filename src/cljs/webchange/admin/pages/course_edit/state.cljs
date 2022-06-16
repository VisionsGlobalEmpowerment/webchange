(ns webchange.admin.pages.course-edit.state
  (:require
    [clojure.string :as s]
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.course-data :as utils]))

(def path-to-db :page/edit-course)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Course Info

(def course-info-key :course-info)

(defn- set-course-info
  [db value]
  (assoc db course-info-key value))

(re-frame/reg-sub
  ::course-info
  :<- [path-to-db]
  #(get % course-info-key))

;; Course Data

(def course-data-key :course-data)

(defn- get-course-data
  [db]
  (get db course-data-key))

(defn- set-course-data
  [db value]
  (assoc db course-data-key value))

(re-frame/reg-sub
  ::course-data
  :<- [path-to-db]
  #(get-course-data %))

(re-frame/reg-sub
  ::course-statistic
  :<- [::course-data]
  :<- [::course-info]
  (fn [[course-data course-info]]
    {:name       (:name course-info)
     :levels     (utils/get-levels-count course-data)
     :lessons    (utils/get-lessons-count course-data)
     :activities (utils/get-activities-count course-data)}))

(re-frame/reg-sub
  ::course-levels
  :<- [::course-data]
  (fn [course-data]
    (->> (get course-data :levels [])
         (map-indexed (fn [idx level-data]
                        (let [level-index (inc idx)]
                          {:idx            level-index
                           :name           (str "Level " level-index)
                           :lessons-number (-> (utils/get-lessons-data level-data)
                                               (count))}))))))

(re-frame/reg-sub
  ::level-lessons
  :<- [::course-data]
  (fn [course-data [_ level-idx]]
    (->> (dec level-idx)
         (utils/get-lessons-data course-data)
         (map-indexed (fn [idx lesson-data]
                        (let [lesson-index (inc idx)]
                          {:idx               lesson-index
                           :name              (str "Lesson " lesson-index)
                           :activities-number (-> (utils/get-activities-data lesson-data)
                                                  (count))}))))))

(re-frame/reg-sub
  ::lesson-activities
  :<- [::course-data]
  :<- [::activities-library]
  (fn [[course-data activities-library] [_ level-idx lesson-idx]]
    (let [activities-library (->> activities-library
                                  (map (fn [{:keys [slug] :as activity}]
                                         [slug activity]))
                                  (into {}))]
      (->> (utils/get-activities-data course-data (dec level-idx) (dec lesson-idx))
           (map-indexed (fn [idx {:keys [activity unique-id]}]
                          (let [activity-index (inc idx)]
                            (->> (get activities-library activity)
                                 (merge {:id  unique-id
                                         :idx activity-index})))))))))

;; Available Activities

(def available-activities-filter-key :available-activities-filter)

(defn- set-available-activities-filter
  [db value]
  (assoc db available-activities-filter-key value))

(re-frame/reg-event-fx
  ::set-available-activities-filter
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (set-available-activities-filter db value)}))

(re-frame/reg-sub
  ::available-activities-filter
  :<- [path-to-db]
  #(get % available-activities-filter-key))

(re-frame/reg-sub
  ::activities-library
  :<- [path-to-db]
  #(get % :activities-library))

(re-frame/reg-sub
  ::available-activities
  :<- [::course-info]
  :<- [::activities-library]
  :<- [::available-activities-filter]
  (fn [[{course-lang :lang} activities-library filter-str]]
    (cond->> activities-library
             :always (filter #(= course-lang (:lang %)))
             (seq filter-str) (filter (fn [{:keys [name]}]
                                        (and (string? name)
                                             (s/includes? (s/lower-case name)
                                                          (s/lower-case filter-str)))))
             :always (sort-by :name))))

;; Side Bar

(def side-bar-content-key :side-bar-content)

(defn- set-side-bar-content
  [db value]
  (assoc db side-bar-content-key value))

(re-frame/reg-event-fx
  ::open-course-info
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-side-bar-content db :course-info)}))

(re-frame/reg-event-fx
  ::open-available-activities
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-side-bar-content db :available-activities)}))

(re-frame/reg-sub
  ::side-bar-content
  :<- [path-to-db]
  #(get % side-bar-content-key :course-info))

;; Course Slug

(def course-slug-key :course-slug)

(defn- get-course-slug
  [db]
  (get db course-slug-key))

(defn- set-course-slug
  [db value]
  (assoc db course-slug-key value))

;; Course Fetching?

(def course-fetching-key :course-fetching?)

(defn- set-course-fetching
  [db value]
  (assoc db course-fetching-key value))

(re-frame/reg-sub
  ::course-fetching?
  :<- [path-to-db]
  #(get % course-fetching-key false))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [course-slug]}]]
    {:db         (-> db
                     (set-course-slug course-slug)
                     (set-course-fetching true))
     :dispatch-n [[::warehouse/load-course course-slug
                   {:on-success [::load-course-success]}]
                  [::warehouse/load-course-info course-slug
                   {:on-success [::load-course-info-success]}]
                  [::warehouse/load-visible-activities
                   {:on-success [::load-available-activities-success]}]]}))

(re-frame/reg-event-fx
  ::load-course-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ course-data]]
    {:db (-> db
             (set-course-fetching false)
             (set-course-data course-data))}))

(re-frame/reg-event-fx
  ::load-course-info-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ course-info]]
    {:db (-> db (set-course-info course-info))}))

(re-frame/reg-event-fx
  ::load-available-activities-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ activities]]
    {:db (assoc db :activities-library activities)}))

(re-frame/reg-event-fx
  ::set-course-info
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ course-info]]
    {:db (-> db (set-course-info course-info))}))

(re-frame/reg-event-fx
  ::add-level
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ props]]
    (let [new-course-data (-> (get-course-data db)
                              (utils/add-level props))]
      {:db       (set-course-data db new-course-data)
       :dispatch [::save-course]})))

(re-frame/reg-event-fx
  ::move-level
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ props]]
    (let [new-course-data (-> (get-course-data db)
                              (utils/move-level props))]
      {:db       (set-course-data db new-course-data)
       :dispatch [::save-course]})))

(re-frame/reg-event-fx
  ::remove-level
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ level-idx]]
    (let [new-course-data (-> (get-course-data db)
                              (utils/remove-level level-idx))]
      {:db       (set-course-data db new-course-data)
       :dispatch [::save-course]})))

(re-frame/reg-event-fx
  ::add-lesson
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ props]]
    (let [new-course-data (-> (get-course-data db)
                              (utils/add-lesson props))]
      {:db       (set-course-data db new-course-data)
       :dispatch [::save-course]})))

(re-frame/reg-event-fx
  ::move-lesson
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ props]]
    (let [new-course-data (-> (get-course-data db)
                              (utils/move-lesson props))]
      {:db       (set-course-data db new-course-data)
       :dispatch [::save-course]})))

(re-frame/reg-event-fx
  ::remove-lesson
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ level-idx lesson-idx]]
    (let [new-course-data (-> (get-course-data db)
                              (utils/remove-lesson level-idx lesson-idx))]
      {:db       (set-course-data db new-course-data)
       :dispatch [::save-course]})))

(re-frame/reg-event-fx
  ::add-activity
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ props activity-data]]
    (let [new-course-data (-> (get-course-data db)
                              (utils/add-activity props activity-data))]
      {:db       (set-course-data db new-course-data)
       :dispatch [::save-course]})))

(re-frame/reg-event-fx
  ::move-activity
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ props]]
    (let [new-course-data (-> (get-course-data db)
                              (utils/move-activity props))]
      {:db       (set-course-data db new-course-data)
       :dispatch [::save-course]})))

(re-frame/reg-event-fx
  ::remove-activity
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ level-idx lesson-idx activity-idx]]
    (let [new-course-data (-> (get-course-data db)
                              (utils/remove-activity level-idx lesson-idx activity-idx))]
      {:db       (set-course-data db new-course-data)
       :dispatch [::save-course]})))

(re-frame/reg-event-fx
  ::save-course
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [course-slug (get-course-slug db)
          course-data (get-course-data db)]
      {:db       (set-course-fetching db true)
       :dispatch [::warehouse/save-course
                  {:course-slug course-slug
                   :course-data course-data}
                  {:on-success [::save-course-success]
                   :on-failure [::save-course-failure]}]})))

(re-frame/reg-event-fx
  ::save-course-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [data]}]]
    {:db (-> db
             (set-course-fetching false)
             (set-course-data data))}))

(re-frame/reg-event-fx
  ::save-course-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-course-fetching db false)}))
