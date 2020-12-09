(ns webchange.editor-v2.course-table.utils.move-selection-horizontally)

(defn- get-item-index
  [item list]
  (->> list
       (map-indexed vector)
       (some (fn [[idx list-item]]
               (and (= list-item item)
                    idx)))))

(defn- get-current-column-idx
  [columns selection]
  (let [columns-list (map :id columns)
        current-column (:field selection)]
    (get-item-index current-column columns-list)))

(defn- column-idx->field
  [columns idx]
  (->> idx
       (nth columns)
       (:id)))

(defn move-selection-left
  [{:keys [selection columns]}]
  (let [current-column-idx (get-current-column-idx columns selection)
        min-idx 0]
    (if (> current-column-idx min-idx)
      (->> (dec current-column-idx)
           (column-idx->field columns)
           (assoc selection :field))
      selection)))

(defn move-selection-right
  [{:keys [selection columns]}]
  (let [current-column-idx (get-current-column-idx columns selection)
        max-idx (-> columns count dec)]
    (if (< current-column-idx max-idx)
      (->> (inc current-column-idx)
           (column-idx->field columns)
           (assoc selection :field))
      selection)))
