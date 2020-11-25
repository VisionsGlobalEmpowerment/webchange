(ns webchange.editor-v2.course-table.state.data-utils)

(defn prepare-course-data
  [data]
  (->> (:levels data)
       (map (fn [{:keys [level lessons]}]
              (map (fn [{:keys [lesson activities]}]
                     (map (fn [activity]
                            (merge (select-keys activity [:activity])
                                   {:lesson lesson
                                    :level  level}))
                          activities))
                   lessons)))
       (flatten)
       (map-indexed (fn [idx activity]
                      (assoc activity :idx (inc idx))))))

