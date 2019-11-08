(ns webchange.editor-v2.diagram.graph-builder.graph
  (:require
    [webchange.editor-v2.diagram.graph-builder.filters.phrases :refer [get-phrases-graph]]
    [webchange.editor-v2.diagram.graph-builder.scene-parser.parser :refer [parse-scene]]))

(defn get-diagram-graph
  [scene-data diagram-mode]
  (let [scene-graph (parse-scene scene-data)]
    (cond
      (= diagram-mode :translation) (get-phrases-graph scene-graph)
      :default scene-graph)))
