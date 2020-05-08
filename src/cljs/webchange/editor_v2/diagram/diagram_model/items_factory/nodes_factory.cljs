(ns webchange.editor-v2.diagram.diagram-model.items-factory.nodes-factory
  (:require
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-model :refer [get-custom-model]]
    [webchange.editor-v2.diagram.diagram-model.items-factory.utils :refer [get-node-outs]]))

(defn add-in-ports!
  [node ins]
  (doseq [event ins]
    (.addInPort node (clojure.core/name event)))
  node)

(defn add-out-ports!
  [node outs]
  (doseq [event outs]
    (.addOutPort node (clojure.core/name event)))
  node)

(defn new-model
  [name node-data]
  (get-custom-model (merge
                      node-data
                      {:name  (clojure.core/name name)})))

(defn create-node
  [node-name node-data]
  (-> (new-model node-name node-data)
      (add-in-ports! [:in])
      (add-out-ports! (get-node-outs node-data))))

(defn create-nodes
  [graph]
  (reduce
    (fn [result [name data]]
      (assoc result name (create-node name data)))
    {}
    graph))
