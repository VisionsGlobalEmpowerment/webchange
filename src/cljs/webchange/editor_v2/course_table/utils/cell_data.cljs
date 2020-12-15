(ns webchange.editor-v2.course-table.utils.cell-data)

(defn- ->int
  [str]
  (.parseInt js/Number str))

(defn activity->cell-data
  [activity field]
  (merge (select-keys activity [:level :lesson :lesson-idx])
         {:field field}))

(defn cell->cell-data
  [cell-el]
  {:level      (->int (.getAttribute cell-el "data-level"))
   :lesson     (->int (.getAttribute cell-el "data-lesson"))
   :lesson-idx (->int (.getAttribute cell-el "data-lesson-idx"))
   :field      (keyword (.getAttribute cell-el "data-field"))})

(defn- stringify-cell-data
  [activity]
  {:level      (str (:level activity))
   :lesson     (str (:lesson activity))
   :lesson-idx (str (:lesson-idx activity))
   :field      (clojure.core/name (:field activity))})

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
  [{:keys [level lesson lesson-idx]}]
  (str level "-" lesson "-" lesson-idx))
