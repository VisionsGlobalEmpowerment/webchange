(ns webchange.editor-v2.course-table.utils.cell-data)

(defn- ->int
  [str]
  (.parseInt js/Number str))

(defn activity->cell-data
  [activity field]
  (merge (select-keys activity [:level-idx :lesson-idx :activity-idx])
         {:field field}))

(defn cell->cell-data
  [cell-el]
  {:level-idx    (->int (.getAttribute cell-el "data-level-idx"))
   :lesson-idx   (->int (.getAttribute cell-el "data-lesson-idx"))
   :activity-idx (->int (.getAttribute cell-el "data-activity-idx"))
   :field        (keyword (.getAttribute cell-el "data-field"))})

(defn- stringify-cell-data
  [activity]
  {:level-idx    (str (:level-idx activity))
   :lesson-idx   (str (:lesson-idx activity))
   :activity-idx (str (:activity-idx activity))
   :field        (clojure.core/name (:field activity))})

(defn cell-data->cell-attributes
  [data]
  (let [data-prefix (fn [field] (->> field
                                     (clojure.core/name)
                                     (str "data-")
                                     (keyword)))]
    (->> (stringify-cell-data data)
         (map (fn [[field value]]
                [(data-prefix field) value]))
         (into {}))))

(defn get-row-id
  [{:keys [level-idx lesson-idx activity-idx]}]
  (str level-idx "-" lesson-idx "-" activity-idx))
