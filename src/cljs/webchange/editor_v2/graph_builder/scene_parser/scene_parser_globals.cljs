(ns webchange.editor-v2.graph-builder.scene-parser.scene-parser-globals
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.utils.create-graph-node :refer [create-graph-node]]))

(defn parse-triggers
  [triggers-data]
  (reduce
    (fn [result [trigger-name {:keys [on action] :as trigger-data}]]
      (assoc result trigger-name (create-graph-node {:entity      :trigger
                                                     :data        trigger-data
                                                     :path        [trigger-name]
                                                     :connections [{:name on
                                                                    :handler (keyword action)}]})))
    {}
    triggers-data))

(defn parse-globals
  [scene-data]
  (parse-triggers (:triggers scene-data)))
