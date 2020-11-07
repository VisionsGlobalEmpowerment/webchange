(ns webchange.editor-v2.graph-builder.filters.translation
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.utils.create-graph-node :refer [create-graph-node-connection]]
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

(defn add-parent-to-disconnected
  [graph]
  (let [nodes-connections-count (reduce (fn [result [_ {:keys [connections]}]]
                                          (reduce (fn [result {:keys [handler]}]
                                                    (update result handler inc))
                                                  result
                                                  connections))
                                        {}
                                        graph)
        disconnected-nodes (reduce (fn [result [node-name _]]
                                     (if-not (contains? nodes-connections-count node-name)
                                       (conj result node-name)
                                       result))
                                   []
                                   graph)]
    (if (< 1 (count disconnected-nodes))
      (assoc graph :parent {:connections (->> disconnected-nodes
                                              (map (fn [disconnected-node]
                                                     (create-graph-node-connection {:handler disconnected-node})))
                                              (set))})
      graph)))

(defn get-translation-graph
  [scene-graph]
  (let [graph (init-root-node scene-graph)
        audio-weights (count-nodes-weights graph speech-node?)
        result (-> graph
                   (remove-extra-nodes (partial remove-node? audio-weights))
                   (remove-root-node)
                   (add-parent-to-disconnected))]
    result))
