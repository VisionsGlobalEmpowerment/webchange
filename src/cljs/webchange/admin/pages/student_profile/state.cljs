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

(defn- get-student-progress
  [db]
  (get db student-progress-key))

(defn- set-student-progress
  [db value]
  (assoc db student-progress-key value))

(re-frame/reg-sub
  ::student-progress
  :<- [path-to-db]
  #(get-student-progress %))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id student-id params]}]]
    (let [{:keys [action]} params]
      {:db         (-> db
                       (assoc :school-id school-id)
                       (assoc :student-id student-id)
                       (assoc :params params))
       :dispatch-n (cond-> [[::load-student-progress]
                            [::warehouse/load-school {:school-id school-id} {:on-success [::load-school-success]}]]
                           (= action "edit") (conj [::set-student-form-editable true]))})))

;; load progress

(def progress-loading-key :progress-loading?)

(defn- set-progress-loading
  [db value]
  (assoc db progress-loading-key value))

(re-frame/reg-sub
  ::progress-loading?
  :<- [path-to-db]
  #(get % progress-loading-key true))

(re-frame/reg-event-fx
  ::load-student-progress
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [student-id]} db]
      {:db       (-> db (set-progress-loading true))
       :dispatch [::warehouse/load-class-student-progress
                  {:student-id student-id}
                  {:on-success [::load-student-success]
                   :on-failure [::load-student-failure]}]})))

(re-frame/reg-event-fx
  ::load-student-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class] :as data}]]
    (if (some? class)
      {:db (-> db
               (set-progress-loading false)
               (set-student-progress data))}
      {:dispatch [::redirect-to-student-view]})))

(re-frame/reg-event-fx
  ::load-student-failure
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::redirect-to-student-view]}))

(re-frame/reg-event-fx
  ::load-school-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school]}]]
    {:db (assoc db :school-data school)}))

(re-frame/reg-sub
  ::readonly?
  :<- [path-to-db]
  #(get-in % [:school-data :readonly] false))

(re-frame/reg-event-fx
  ::redirect-to-student-view
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [school-id student-id params]} db]
      {:dispatch [::routes/redirect :student-view :school-id school-id :student-id student-id
                  :storage-params params]})))

(re-frame/reg-sub
  ::course-statistics
  :<- [::student-progress]
  (fn [{:keys [course-stats]}]
    (let [{:keys [activity-progress books-read cumulative-time started-at last-login]} (:data course-stats)]
      {:started-at        (date-str->locale-date started-at "no data")
       :last-login        (date-str->locale-date last-login "no data")
       :activity-progress (or activity-progress "no data")
       :books-read        (or books-read "no data")
       :cumulative-time   (or (ms->duration cumulative-time) "no data")})))

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
    (let [{:keys [school-id]} db
          class-id (-> (get-student-progress db)
                       (get-in [:class :id]))]
      {:dispatch [::routes/redirect :class-profile :school-id school-id :class-id class-id]})))

;; progress data

(defn- ->progress-data
  [activity-data activity-progress course]
  (let [{:keys [scene-id unique-id]} activity-data
        {:keys [name]} (get-in course [:data :scene-list (-> scene-id str keyword)])]
    (merge (get activity-progress unique-id {:score-value 0})
           {:name      name
            :unique-id unique-id})))

(re-frame/reg-sub
  ::progress-data
  :<- [::student-progress]
  :<- [::current-level]
  (fn [[{:keys [activity-stats course]} current-level-idx]]
    (let [activity-progress (reduce (fn [result {:keys [data unique-id]}]
                                      (assoc result unique-id {:score-value 100
                                                               :score (:score data)
                                                               :time-spent (:time-spent data)}))
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

;; student form

(def student-form-editable-key :student-form-editable?)

(defn- set-student-form-editable
  [db value]
  (assoc db student-form-editable-key value))

(re-frame/reg-sub
  ::student-form-editable?
  :<- [path-to-db]
  #(get % student-form-editable-key false))

(re-frame/reg-event-fx
  ::set-student-form-editable
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (set-student-form-editable db value)}))

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

(re-frame/reg-event-fx
  ::handle-edit-finished
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    (let [{:keys [on-edit-finished]} (:params db)]
      {:dispatch-n (cond-> [[::set-student-form-editable false]
                            [::update-student-data student-id]]
                           (some? on-edit-finished) (conj on-edit-finished))})))

(re-frame/reg-event-fx
  ::handle-edit-canceled
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [on-edit-finished]} (:params db)]
      {:dispatch-n (cond-> [[::set-student-form-editable false]]
                           (some? on-edit-finished) (conj on-edit-finished))})))

(comment
  (-> @(re-frame/subscribe [::progress-data]))
  (-> @(re-frame/subscribe [::student-progress])
      :activity-stats))
