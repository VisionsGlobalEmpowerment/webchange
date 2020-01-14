(ns webchange.editor-v2.graph-builder.graph-normalizer.clone-repetitive-nodes.clone-repetitive-nodes
  (:require
    [webchange.editor-v2.graph-builder.graph-normalizer.clone-repetitive-nodes.get-repetitive-nodes :refer [get-repetitive-nodes]]
    [webchange.editor-v2.graph-builder.graph-normalizer.clone-repetitive-nodes.process-repetitive-nodes :refer [process-repetitive-nodes]]))

(defn remove-nodes
  [graph nodes]
  (reduce
    (fn [graph node]
      (dissoc graph node))
    graph
    nodes))

(defn clone-repetitive-nodes
  [graph]
  (let [repetitive-nodes (get-repetitive-nodes graph)]
    (-> graph
        (process-repetitive-nodes repetitive-nodes)
        (remove-nodes repetitive-nodes))))
