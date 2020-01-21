(ns webchange.editor-v2.graph-builder.utils.graph-to-nodes-seq
  (:require
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [add-root-node
                                                                get-root-nodes
                                                                remove-root-node]]))
(defn get-nodes-seq
  ([graph]
   (get-nodes-seq graph [:root :root] []))
  ([graph [prev-node-name node-name] result]
   (let [node-data (get graph node-name)]
     (reduce
       (fn [result next-node-name]
         (get-nodes-seq graph [node-name next-node-name] result))
       (conj result (assoc {} node-name node-data))
       (map :handler (get-children node-name node-data prev-node-name))))))

(defn graph-to-nodes-seq
  [graph]
  (->> graph
       (get-root-nodes)
       (add-root-node graph)
       (get-nodes-seq)))
