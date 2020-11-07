(ns webchange.editor-v2.graph-builder.utils.remove-extra-nodes
  (:require
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]
    [webchange.editor-v2.graph-builder.utils.change-node :refer [remove-node]]))

(defn remove-extra-nodes
  ([graph remove-node?]
   (first (remove-extra-nodes graph [:root :root] {} remove-node?)))
  ([graph [_ node-name] used-map remove-node?]
   (let [[graph used-map] (reduce
                            (fn [[graph used-map] {:keys [handler]}]
                              (if-not (contains? used-map handler)
                                (remove-extra-nodes graph [node-name handler] used-map remove-node?)
                                [graph used-map]))
                            [graph (assoc used-map node-name true)]
                            (get-children node-name (get graph node-name) nil))]
     (let [node-data (get graph node-name)
           remove? (remove-node? node-name node-data)]
       [(if remove?
          (remove-node graph node-name)
          graph)
        used-map]))))

; ToDo: possible variant:
;(reduce (fn [graph [node-name node-data]]
;             (if (remove-node? node-name node-data)
;               (remove-node graph node-name)
;               graph))
;           graph
;           graph)
