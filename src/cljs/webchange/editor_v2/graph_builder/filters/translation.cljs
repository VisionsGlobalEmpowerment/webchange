(ns webchange.editor-v2.graph-builder.filters.translation
  (:require
    [webchange.editor-v2.graph-builder.utils.count-nodes-weights :refer [count-nodes-weights
                                                                         weight-changer?]]
    [webchange.editor-v2.graph-builder.utils.remove-extra-nodes :refer [remove-extra-nodes]]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [init-root-node
                                                                remove-root-node]]
    [webchange.editor-v2.graph-builder.utils.node-data :refer [speech-node?]]))

(defn remove-node?
  [nodes-weights node-name _]
  (-> (get nodes-weights node-name)
      (weight-changer?)
      (not)))

(defn get-translation-graph
  [scene-graph]
  (let [graph (init-root-node scene-graph)
        audio-weights (count-nodes-weights graph speech-node?)]
    (-> graph
        (remove-extra-nodes (partial remove-node? audio-weights))
        (remove-root-node))))
