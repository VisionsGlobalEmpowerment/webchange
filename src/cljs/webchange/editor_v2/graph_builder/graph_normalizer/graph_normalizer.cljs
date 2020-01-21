(ns webchange.editor-v2.graph-builder.graph-normalizer.graph-normalizer
  (:require
    [webchange.editor-v2.graph-builder.graph-normalizer.clone-repetitive-nodes.clone-repetitive-nodes :refer [clone-repetitive-nodes]]
    [webchange.editor-v2.graph-builder.graph-normalizer.open-cycles :refer [open-cycles]]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [add-root-node
                                                                get-root-nodes
                                                                remove-root-node]]))

(defn normalize-graph
  [graph]
  (->> graph
       (get-root-nodes)
       (add-root-node graph)
       (open-cycles)
       (clone-repetitive-nodes)
       (remove-root-node)))
