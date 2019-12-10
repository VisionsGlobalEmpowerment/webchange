(ns webchange.editor-v2.graph-builder.graph
  (:require
    [webchange.editor-v2.graph-builder.filters.phrases :refer [get-phrases-graph]]
    [webchange.editor-v2.graph-builder.scene-parser.concepts-replacer.replacer :refer [override-concept-actions]]
    [webchange.editor-v2.graph-builder.scene-parser.scene-parser :refer [parse-data]]
    [webchange.editor-v2.graph-builder.duplicates-replicator.duplicates-replicator :refer [untangle-reuses]]
    [webchange.editor-v2.graph-builder.graph-normalizer.graph-normalizer :refer [normalize-graph]]))

(defn parse-scene
  ([scene-data]
   (parse-scene scene-data {}))
  ([scene-data {:keys [start-node concept-data]}]
   (-> scene-data
       (parse-data start-node)
       ;(normalize-graph)
       (untangle-reuses)
       (override-concept-actions concept-data)
       )))

(defn get-diagram-graph
  ([scene-data diagram-mode]
    (get-diagram-graph scene-data diagram-mode {}))
  ([scene-data diagram-mode params]
   (cond
     (= diagram-mode :phrases) (-> scene-data
                                   (parse-scene)
                                   (get-phrases-graph))
     (= diagram-mode :translation) (-> scene-data
                                       (parse-scene (select-keys params [:start-node :concept-data])))
     :default (-> scene-data
                  (parse-scene)))))
