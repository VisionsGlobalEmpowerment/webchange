(ns webchange.editor-v2.graph-builder.utils.root-nodes
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.utils.create-graph-node :refer [create-graph-node-connection]]))

(defn get-root-nodes
  [graph]
  (if (= 1 (-> graph keys count))
    (-> graph keys)
    (reduce
      (fn [result [node-name node-data]]
        (let [node-connections (:connections node-data)
              node-parents (->> node-connections
                                (map :previous)
                                (distinct))]
          (if (some #{:root} node-parents)
            (conj result node-name)
            result)))
      []
      graph)))

(defn add-root-node
  [graph start-nodes]
  (assoc graph :root {:connections (->> start-nodes
                                        (map (fn [start-node]
                                               (create-graph-node-connection {:previous :root
                                                                              :name     "next"
                                                                              :handler  start-node})))
                                        (set))}))

(defn init-root-node
  [graph]
  (->> graph
       (get-root-nodes)
       (add-root-node graph)))

(defn remove-root-node
  [graph]
  (dissoc graph :root))
