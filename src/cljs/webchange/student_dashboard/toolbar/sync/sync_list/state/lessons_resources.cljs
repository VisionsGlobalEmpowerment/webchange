(ns webchange.student-dashboard.toolbar.sync.sync-list.state.lessons-resources
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.core :as i]
    [webchange.resources.scene-parser :refer [get-lesson-resources get-lesson-endpoints]]
    [webchange.student-dashboard.toolbar.sync.sync-list.state.db :as db]))

;; Initialize data with parsed lessons resources

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:lessons-resources])
       (db/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_]]
    (let [should-parse? (->> (path-to-db [:ready?]) (get-in db) (not))]
      (if should-parse?
        (let [scenes (get-in db [:course-data :scenes])]
          {:db         (assoc-in db (path-to-db [:scenes]) {:done 0 :total (count scenes)})
           :dispatch-n (map (fn [scene] [::load-scene-resources scene])
                            scenes)})
        {}))))

;; Loading

(re-frame/reg-event-fx
  ::load-scene-resources
  (fn [{:keys [db]} [_ scene-id]]
    (let [course-id (:current-course db)]
      {:load-scene-resources {:course-id course-id
                              :scene-id  scene-id}})))

(re-frame/reg-fx
  :load-scene-resources
  (fn [{:keys [course-id scene-id]}]
    (i/load-scene {:course-id course-id
                   :scene-id  scene-id}
                  (fn [scene-data]
                    (re-frame/dispatch [::store-scene-data scene-id scene-data])))))

(re-frame/reg-event-fx
  ::store-scene-data
  (fn [{:keys [db]} [_ scene-id scene-data]]
    (let [loaded-scenes (->> (path-to-db [:scenes :done]) (get-in db) (inc))
          total-scenes (->> (path-to-db [:scenes :total]) (get-in db))]
      (cond-> {:db (-> db
                       (assoc-in (path-to-db [:scenes :data scene-id]) scene-data)
                       (assoc-in (path-to-db [:scenes :done]) loaded-scenes))}
              (= loaded-scenes total-scenes) (assoc :dispatch [::parse-lessons-resources])))))

;; Parsing

(defn- get-lesson-name
  [{:keys [level level-idx lesson lesson-idx]}]
  (let [level-name "Level "
        lesson-type (-> lesson :type keyword)
        lesson-name (get-in level [:scheme lesson-type :name] "Lesson ")]
    (str level-name (inc level-idx) " - "  lesson-name (inc lesson-idx))))

(defn- get-lessons-data
  [levels navigation-activities]
  (->> levels
       (map-indexed (fn [level-idx level]
                      (map-indexed (fn [lesson-idx lesson]
                                     {:name        (get-lesson-name {:level      level
                                                                     :level-idx  level-idx
                                                                     :lesson     lesson
                                                                     :lesson-idx lesson-idx})
                                      :level-id    level-idx
                                      :lesson-id   lesson-idx
                                      :activities  (->> (:activities lesson) (map :activity) (concat navigation-activities))
                                      :lesson-sets (->> (:lesson-sets lesson) (vals))})
                                   (:lessons level))))
       (flatten)))

(defn- get-lessons-list
  [levels navigation-activities scenes-data current-course]
  (->> (get-lessons-data levels navigation-activities)
       (map (fn [{:keys [level-id lesson-id] :as lesson}]
              (let [resources (get-lesson-resources lesson scenes-data)
                    endpoints (get-lesson-endpoints current-course lesson)]
                (merge lesson
                       {:id        (+ lesson-id (* level-id 1000))
                        :resources resources
                        :endpoints endpoints}))))))

(defn- get-navigation-activities
  [course-data]
  (->> (:scene-list course-data)
       (filter (fn [[_ scene-data]] (< 1 (->> scene-data :outs count))))
       (map (fn [[scene-name _]] (name scene-name)))))

(re-frame/reg-event-fx
  ::parse-lessons-resources
  (fn [{:keys [db]} [_]]
    (let [current-course (get-in db [:current-course])
          course-data (get-in db [:course-data])
          scenes-data (get-in db (path-to-db [:scenes :data]))

          lessons (get-lessons-list (:levels course-data)
                                    (get-navigation-activities course-data)
                                    scenes-data
                                    current-course)]
      {:db (-> db
               (assoc-in (path-to-db [:ready?]) true)
               (assoc-in (path-to-db [:list]) (map :id lessons))
               (assoc-in (path-to-db [:data]) (->> lessons
                                                   (map (fn [{:keys [id] :as lesson}] [id lesson]))
                                                   (into {})))
               (update-in (path-to-db []) dissoc :scenes))})))

;; Subscriptions

(re-frame/reg-sub
  ::loading
  (fn [db]
    (not (get-in db (path-to-db [:ready?]) false))))

(defn lessons-data
  [db]
  (get-in db (path-to-db [:data])))

(defn lessons-list
  [db]
  (let [ids (get-in db (path-to-db [:list]))
        data (lessons-data db)]
    (map #(get data %) ids)))

(re-frame/reg-sub ::lessons-list lessons-list)

