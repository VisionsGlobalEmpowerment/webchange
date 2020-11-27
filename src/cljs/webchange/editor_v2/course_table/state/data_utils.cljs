(ns webchange.editor-v2.course-table.state.data-utils)

(defn prepare-course-data
  [course scenes-data]
  (->> (:levels course)
       (map (fn [{:keys [level lessons]}]
              (map (fn [{:keys [lesson activities]}]
                     (map (fn [{:keys [activity]}]
                            {:level    level
                             :lesson   lesson
                             :activity activity
                             :skills   (get-in scenes-data [activity :skills] [])})
                          activities))
                   lessons)))
       (flatten)
       (map-indexed (fn [idx activity]
                      (assoc activity :idx (inc idx))))))

