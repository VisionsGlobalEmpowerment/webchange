(ns webchange.editor-v2.graph-builder.utils.remove-sub-graph
  (:require
    [webchange.editor-v2.graph-builder.utils.change-node :refer [remove-handler]]
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]))

(defn remove-extra-nodes-dfs
  [graph [prev-node-name node-name]]
  (let [children (get-children node-name (get graph node-name) prev-node-name)]
    (reduce
      (fn [graph {:keys [handler]}]
        (remove-extra-nodes-dfs graph [node-name handler]))
      (dissoc graph node-name)
      children)))

(defn update-parents
  [graph removed-node-name]
  (reduce
    (fn [graph [node-name]]
      (remove-handler graph node-name removed-node-name))
    graph
    graph))

(defn remove-sub-graph
  ([graph start-node]
   (remove-sub-graph graph start-node nil))
  ([graph start-node prev-node]
   (-> graph
       (remove-extra-nodes-dfs [prev-node start-node])
       (update-parents start-node))))
