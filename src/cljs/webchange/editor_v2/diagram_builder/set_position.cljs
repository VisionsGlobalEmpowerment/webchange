(ns webchange.editor-v2.diagram-builder.set-position
  (:require
    [webchange.editor-v2.diagram-builder.diagram-nodes-utils :refer [set-node-position]]))

(defn set-position
  [{:keys [nodes links]}]
  (doseq [node nodes]
    (set-node-position node (* (rand-int 100) 10) (* (rand-int 60) 10)))
  {:nodes nodes
   :links links})
