(ns webchange.editor-v2.diagram.scene-data-parser.globals-parser)

(defn parse-triggers
  [scene-data]
  (->> (:triggers scene-data)
       (seq)
       (map second)
       (reduce (fn [result data]
                 (let [event (keyword (:on data))
                       handler (keyword (:action data))
                       current-handlers (get result event)]
                   (assoc result event (if (nil? current-handlers)
                                         [handler]
                                         (conj current-handlers handler)))))
               {})))

(defn parse-globals
  [scene-data]
  {:triggers {:name   "Triggers"
              :entity :global-object
              :outs   (parse-triggers scene-data)}})
