(ns webchange.editor-v2.graph-builder.utils.remove-extra-nodes
  (:require
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]
    [webchange.editor-v2.graph-builder.utils.change-node :refer [remove-node]]))

(defn remove-extra-nodes
  ([graph remove-node?]
   (remove-extra-nodes graph [:root :root] remove-node?))
  ([graph [prev-node-name node-name] remove-node?]
   (let [node-data (get graph node-name)
         remove? (remove-node? node-name node-data)]
     (reduce
       (fn [graph next-node-name]
         (remove-extra-nodes graph [(if remove? prev-node-name node-name) next-node-name] remove-node?))
       (if remove?
         (remove-node graph node-name)
         graph)
       (map :handler (get-children node-name node-data prev-node-name))))))
