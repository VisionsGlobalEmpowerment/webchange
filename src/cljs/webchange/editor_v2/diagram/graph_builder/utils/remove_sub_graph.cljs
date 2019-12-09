(ns webchange.editor-v2.diagram.graph-builder.utils.remove-sub-graph
  (:require
    [webchange.editor-v2.diagram.graph-builder.utils.change-node :refer [get-node-ins
                                                                         get-node-outs
                                                                         remove-handler]]
    [webchange.editor-v2.diagram.graph-builder.utils.node-children :refer [get-children]]))

(defn remove-extra-nodes-dfs
  [graph [prev-node-name node-name]]
  (let [children (-> graph
                     (get node-name)
                     (get-children prev-node-name))]

    (reduce
      (fn [graph next-node-name]
        (remove-extra-nodes-dfs graph [node-name next-node-name]))
      (dissoc graph node-name)
      children)))

(defn update-parents
  [graph parents node-name]
  (reduce
    (fn [graph parent]
      (if (contains? graph parent)
        (remove-handler graph parent node-name)
        graph))
    graph
    parents))

(defn remove-sub-graph
  ([graph start-node]
   (remove-sub-graph graph start-node nil))
  ([graph start-node prev-node]
   (let [parents-to-update (-> graph (get start-node) (get-node-ins) (keys))]
     (-> graph
         (remove-extra-nodes-dfs [prev-node start-node])
         (update-parents parents-to-update start-node)))))
