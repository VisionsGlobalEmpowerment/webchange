(ns webchange.editor-v2.diagram-builder.index
  (:require
    [webchange.editor-v2.diagram-builder.diagram-nodes :refer [get-diagram-nodes]]
    [webchange.editor-v2.diagram-builder.set-position :refer [set-position]]))

(defn get-diagram-data
  [scene-data]
  (->> scene-data
       (get-diagram-nodes)
       (set-position)))
