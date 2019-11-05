(ns webchange.editor-v2.diagram.scene-parser.data-parser.data-parser-objects)

(defn parse-object
  [object-name object-data]
  (assoc {} object-name {:type        "object"
                         :data        object-data
                         :connections {:root {:handlers (reduce
                                                          (fn [result [_ action-data]]
                                                            (assoc
                                                              result
                                                              (->> action-data :on keyword)
                                                              [(->> action-data :id keyword)]))
                                                          {}
                                                          (:actions object-data))}}}))

(defn parse-objects
  [scene-data]
  (->> (:objects scene-data)
       (reduce
         (fn [result [object-name object-data]]
           (merge result (parse-object object-name object-data)))
         {})))
