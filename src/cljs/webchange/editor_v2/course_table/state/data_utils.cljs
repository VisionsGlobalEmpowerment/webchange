(ns webchange.editor-v2.course-table.state.data-utils)

(defn- get-lesson-sets
  [level lesson lesson-sets-data]
  (let [lesson-type (->> lesson :type keyword)
        level-scheme (:scheme level)
        lesson-sets-keys (->> (get-in level-scheme [lesson-type :lesson-sets])
                              (map keyword))
        lesson-sets (:lesson-sets lesson)]

    (->> lesson-sets-keys
         (map (fn [lesson-set-key]
                [lesson-set-key (get lesson-sets lesson-set-key)]))
         (map (fn [[activity-set-name lesson-set-name]]
                (let [lesson-set-data (-> lesson-sets-data
                                          (get lesson-set-name)
                                          (select-keys [:id :name :dataset-id :items]))]
                  [activity-set-name (if-not (empty? lesson-set-data) lesson-set-data nil)])))
         (into {}))))

(defn- get-activity-tags
  [{:keys [only tags-by-score]}]
  (cond-> {}
          (some? only) (assoc :for-tags only)
          (some? tags-by-score) (assoc :set-tags tags-by-score)))

(defn prepare-course-data
  [course scenes-data lesson-sets-data]
  (->> (:levels course)
       (map-indexed (fn [level-index {:keys [lessons] :as level}]
                      (map-indexed (fn [lesson-index {:keys [activities comment] :as lesson}]
                                     (map-indexed (fn [activity-index {:keys [activity] :as activity-data}]
                                                    {:level-idx    level-index
                                                     :lesson-idx   lesson-index
                                                     :activity-idx activity-index
                                                     :activity     activity
                                                     :skills       (get-in scenes-data [activity :skills] [])
                                                     :lesson-sets  (get-lesson-sets level lesson lesson-sets-data)
                                                     :tags         (get-activity-tags activity-data)
                                                     :comment      (or comment "")})
                                                  activities))
                                   lessons)))
       (flatten)
       (map-indexed (fn [idx activity]
                      (assoc activity :idx (inc idx))))))
