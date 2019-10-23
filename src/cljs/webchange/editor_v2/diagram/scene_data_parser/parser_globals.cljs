(ns webchange.editor-v2.diagram.scene-data-parser.parser-globals)

(defn add-handler
  [data event handler]
  (let [handlers (get data event)]
    (if (nil? handlers)
      (assoc data event [handler])
      (assoc data event (conj handlers handler)))))

(defn parse-triggers
  [triggers-data]
  {:type        "trigger"
   :data        triggers-data
   :connections {:root {:handlers (reduce
                                    (fn [result [_ trigger-data]]
                                      (add-handler result
                                                   (->> trigger-data :on keyword)
                                                   (->> trigger-data :action keyword)))
                                    {}
                                    triggers-data)}}})

(defn parse-globals
  [scene-data]
  {:triggers (parse-triggers (:triggers scene-data))})
