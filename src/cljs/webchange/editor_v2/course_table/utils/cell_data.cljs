(ns webchange.editor-v2.course-table.utils.cell-data)

(defn activity->cell-data
  [activity field]
  {:level    (str (:level activity))
   :lesson   (str (:lesson activity))
   :activity (str (:activity activity))
   :field    (clojure.core/name field)})

(defn cell->cell-data
  [cell-el]
  (->> ["level" "lesson" "activity" "field"]
       (map (fn [field]
              [(keyword field)
               (.getAttribute cell-el (str "data-" field))]))
       (into {})))

(defn cell-data->cell-attributes
  [data]
  (let [data-prefix (fn [field] (->> field
                                     (clojure.core/name)
                                     (str "data-")
                                     (keyword)))]
    (->> data
         (map (fn [[field value]]
                [(data-prefix field) value]))
         (into {}))))

(defn get-row-id
  [{:keys [level lesson activity]}]
  (str level "-" lesson "-" activity))
