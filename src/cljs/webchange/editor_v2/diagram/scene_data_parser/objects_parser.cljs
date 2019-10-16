(ns webchange.editor-v2.diagram.scene-data-parser.objects-parser)

(defn get-object-handlers
  [object-data]
  (->> (:actions object-data)
       (seq)
       (reduce
         (fn [result [_ action-data]]
           (conj result (->> action-data :id keyword)))
         [])))

(defn parse-objects
  [scene-data]
  (->> (:objects scene-data)
       (seq)
       (reduce
         (fn [result [object-name object-data]]
           (assoc result object-name {:type "object"
                                      :next (get-object-handlers object-data)}))
         {})))

