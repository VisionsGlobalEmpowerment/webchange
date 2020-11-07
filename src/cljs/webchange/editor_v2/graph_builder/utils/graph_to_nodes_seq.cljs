(ns webchange.editor-v2.graph-builder.utils.graph-to-nodes-seq
  (:require
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [add-root-node
                                                                get-root-nodes
                                                                remove-root-node]]))
(defn get-nodes-seq
  ([graph]
   (first (get-nodes-seq graph [:root :root] {} [])))
  ([graph [prev-node-name node-name] used-map result]
   (let [node-data (get graph node-name)]
     (reduce
       (fn [[result used-map] {:keys [handler]}]
         (if-not (contains? used-map handler)
           (get-nodes-seq graph [node-name handler] (assoc used-map handler true) result)
           [result used-map]))
       [(conj result (assoc {} node-name node-data))
        used-map]
       (get-children node-name node-data prev-node-name)))))

(defn graph-to-nodes-seq
  [graph]
  (->> graph
       (get-root-nodes)
       (add-root-node graph)
       (get-nodes-seq)))
