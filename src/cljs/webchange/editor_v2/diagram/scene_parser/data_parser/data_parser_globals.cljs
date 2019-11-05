(ns webchange.editor-v2.diagram.scene-parser.data-parser.data-parser-globals)

(def available-triggers [:start
                         :back])

(defn add-handler
  [data event handler]
  (let [handlers (get data event)]
    (if (nil? handlers)
      (assoc data event [handler])
      (assoc data event (conj handlers handler)))))

(defn parse-triggers
  [scene-data]
  (if (contains? scene-data :triggers)
    (let [triggers-data (:triggers scene-data)]
      {:triggers {:type        "trigger"
                  :data        triggers-data
                  :connections {:root {:handlers (merge (reduce
                                                          (fn [result trigger-name]
                                                            (assoc result trigger-name []))
                                                          {}
                                                          available-triggers)
                                                        (reduce
                                                          (fn [result [_ trigger-data]]
                                                            (add-handler result
                                                                         (->> trigger-data :on keyword)
                                                                         (->> trigger-data :action keyword)))
                                                          {}
                                                          triggers-data))}}}})
    {}))

(defn parse-globals
  [scene-data]
  (merge (parse-triggers scene-data)))
