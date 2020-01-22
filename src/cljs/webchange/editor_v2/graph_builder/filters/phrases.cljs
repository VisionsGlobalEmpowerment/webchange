(ns webchange.editor-v2.graph-builder.filters.phrases
  (:require
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]
    [webchange.editor-v2.graph-builder.utils.change-node :refer [remove-node]]
    [webchange.editor-v2.graph-builder.utils.remove-sub-graph :refer [remove-sub-graph]]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [add-root-node
                                                                get-root-nodes
                                                                remove-root-node]]

    [webchange.editor-v2.graph-builder.utils.count-nodes-weights :refer [count-nodes-weights
                                                                         weight-changer?]]
    [webchange.editor-v2.graph-builder.utils.remove-extra-nodes :refer [remove-extra-nodes]]
    [webchange.editor-v2.graph-builder.utils.node-data :refer [phrase-node?]]))

(defn remove-node?
  [nodes-weights node-name node-data]
  (let [significant? (-> (get nodes-weights node-name)
                         (weight-changer?))
        action? (= :action (:entity node-data))]
    (and action?
         (not significant?))))

(defn remove-detached-nodes
  [graph nodes-weights]
  (reduce (fn [graph [node-name _]]
            (let [counted? (contains? nodes-weights node-name)
                  zero-weight? (= 0 (-> nodes-weights (get node-name) (first)))]
              (if (or zero-weight?
                      (not counted?))
                (dissoc graph node-name)
                graph)))
          graph
          graph))

(defn get-phrases-graph
  [scene-graph]
  (let [graph (->> scene-graph
                   (get-root-nodes)
                   (add-root-node scene-graph))
        subtree-phrase-weights (count-nodes-weights graph phrase-node?)]
    (-> graph
        (remove-extra-nodes (partial remove-node? subtree-phrase-weights))
        (remove-detached-nodes subtree-phrase-weights)
        (remove-root-node))))
