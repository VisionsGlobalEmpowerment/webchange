(ns webchange.editor-v2.diagram.graph-builder.utils.root-nodes)

(defn get-root-nodes
  [graph]
  (reduce
    (fn [result [node-name node-data]]
      (if (contains? (:connections node-data) :root)
        (conj result node-name)
        result))
    []
    graph))

(defn add-root-node
  [graph start-nodes]
  (assoc graph :root {:type        "root"
                      :connections {:root {:handlers {:next start-nodes}}}}))

(defn remove-root-node
  [graph]
  (dissoc graph :root))
