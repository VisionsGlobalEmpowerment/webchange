(ns webchange.admin.widgets.student-progress-complete.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.ui.components.form.data :refer [init]]
    [webchange.admin.widgets.state :as widgets]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :widget/student-complete-progress)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Flags

(def flags-data (init :flags))
(def get-flags-data (:get-data flags-data))
(def reset-flags-data (:reset-data flags-data))
(def update-flags-data (:update-data flags-data))

(re-frame/reg-sub
  ::flags-data
  :<- [path-to-db]
  #(get-flags-data %))

;; -- saving?

(def saving-key :saving?)

(defn- set-saving
  [db value]
  (update-flags-data db {saving-key value}))

(re-frame/reg-sub
  ::saving?
  :<- [::flags-data]
  #(get % saving-key false))

;; -- progress-loading?

(def progress-loading-key :progress-loading?)

(defn- set-progress-loading
  [db value]
  (update-flags-data db {progress-loading-key value}))

(re-frame/reg-sub
  ::progress-loading?
  :<- [::flags-data]
  #(get % progress-loading-key false))

(re-frame/reg-sub
  ::data-loading?
  :<- [::progress-loading?]
  (fn [progress-loading?]
    progress-loading?))

;; Course Data

(def course-data-key :course-data)

(defn- get-course-data
  [db]
  (get db course-data-key))

(defn- set-course-data
  [db data]
  (assoc db course-data-key data))

(re-frame/reg-sub
  ::course-data
  :<- [path-to-db]
  #(get-course-data %))

;; Form Data

(def form-data (init :form-data))
(def get-form-data (:get-data form-data))
(def set-form-data (:set-data form-data))
(def update-form-data (:update-data form-data))

(re-frame/reg-sub
  ::form-data
  :<- [path-to-db]
  #(get-form-data %))

(re-frame/reg-event-fx
  ::update-form-data
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ form-data]]
    {:db (update-form-data db form-data)}))

;; Level

(re-frame/reg-sub
  ::level-options
  :<- [::course-data]
  (fn [course-data]
    (->> (get-in course-data [:data :levels] [])
         (map-indexed (fn [idx {:keys [_]}]
                        (let [number (inc idx)]
                          {:text  (str "Level " number)
                           :value number}))))))

(re-frame/reg-sub
  ::lesson-options
  :<- [::course-data]
  :<- [::form-data]
  (fn [[course-data {level-number :level}]]
    (if (and (some? course-data)
             (number? level-number))
      (let [levels (get-in course-data [:data :levels] [])
            current-level-data (->> (dec level-number)
                                    (nth levels))]
        (->> (get current-level-data :lessons [])
             (map-indexed (fn [idx {:keys [type]}]
                            (let [number (inc idx)]
                              {:text  (cond-> (str "Lesson " number)
                                              (= type "assessment") (str " (assessment)"))
                               :value number})))))
      [])))

(re-frame/reg-sub
  ::activity-options
  :<- [::course-data]
  :<- [::form-data]
  (fn [[course-data {level-number :level lesson-number :lesson}]]
    (if (and (some? course-data)
             (number? level-number)
             (number? lesson-number))
      (let [levels (get-in course-data [:data :levels] [])
            current-level-data (->> (dec level-number)
                                    (nth levels))
            lessons (get current-level-data :lessons [])
            current-lesson-data (->> (dec lesson-number)
                                     (nth lessons))

            activities (get-in course-data [:data :scene-list])]
        (->> (get current-lesson-data :activities [])
             (map-indexed (fn [idx {:keys [scene-id]}]
                            (let [number (inc idx)]
                              {:text  (str number ". " (or (->> scene-id
                                                                (str)
                                                                (keyword)
                                                                (get activities)
                                                                (:name))
                                                           scene-id))
                               :value number})))))
      [])))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [student-id]}]]
    {:db       (-> db
                   (assoc :student-id student-id)
                   (set-form-data {:level    1
                                   :lesson   1
                                   :activity 1})
                   (set-progress-loading true))
     :dispatch [::warehouse/load-class-student-progress
                {:student-id student-id}
                {:on-success [::load-student-success]}]}))

(re-frame/reg-event-fx
  ::load-student-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [course]}]]
    {:db (-> db
             (set-progress-loading false)
             (set-course-data course))}))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [on-success]}]]
    (let [student-id (:student-id db)
          course-slug (-> (get-course-data db)
                          (:slug))
          form-data (get-form-data db)]

      {:db       (-> db (set-saving true))
       :dispatch [::warehouse/complete-student-progress
                  {:student-id  student-id
                   :course-name course-slug
                   :data        form-data}
                  {:on-success [::save-success on-success]
                   :on-failure [::save-failure]}]})))

(re-frame/reg-event-fx
  ::save-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-success]]
    {:db                (-> db (set-saving false))
     ::widgets/callback [on-success]}))

(re-frame/reg-event-fx
  ::save-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-saving false))}))
