(ns webchange.editor-v2.diagram.scene-data-parser.parser-objects)

(defn parse-object
  [object-name object-data]
  (assoc {} object-name {:type        "object"
                         :data        object-data
                         :connections {:root {:next (->> (:actions object-data)
                                                         (map #(->> % second :id keyword))
                                                         (vec))}}}))

(defn parse-objects
  [scene-data]
  (->> (:objects scene-data)
       (reduce
         (fn [result [object-name object-data]]
           (merge result (parse-object object-name object-data)))
         {})))
