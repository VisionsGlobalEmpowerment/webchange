(ns webchange.editor-v2.diagram.scene-data-parser.parser-globals)

(defn parse-triggers
  [triggers-data]
  {:type        "trigger"
   :data        triggers-data
   :connections {:root {:next (map (fn [[_ trigger-data]]
                                     (->> trigger-data :action keyword))
                                   triggers-data)}}})

(defn parse-globals
  [scene-data]
  {:triggers (parse-triggers (:triggers scene-data))})
