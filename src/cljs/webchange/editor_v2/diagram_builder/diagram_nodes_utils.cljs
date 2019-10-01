(ns webchange.editor-v2.diagram-builder.diagram-nodes-utils
  (:require
    [webchange.editor-v2.diagram-builder.custom-nodes.action-node.action-model :refer [get-action-model]]
    [webchange.editor-v2.diagram-builder.custom-nodes.object-node.object-model :refer [get-object-model]]
    [webchange.editor-v2.diagram-builder.colors :refer [get-object-color get-action-color]]
    ["@projectstorm/react-diagrams" :refer [DefaultNodeModel]]))

(defn get-object-node
  [name {:keys [outs]}]
  (let [node (get-object-model {:name  (clojure.core/name name)
                                :color (get-object-color name)})]
    (doseq [{:keys [event _]} outs]
      (.addOutPort node (clojure.core/name event)))
    node))

(defn get-action-node
  [name {:keys [type outs]}]
  (let [node (get-action-model {:name  (clojure.core/name name)
                                :color (get-action-color (keyword type))})]
    (.addInPort node "in")
    (doseq [{:keys [event _]} outs]
      (.addOutPort node (clojure.core/name event)))
    node))

(defn set-node-position
  [node x y]
  (.setPosition node x y)
  node)

(defn connect-nodes
  [node-1 out-port-name node-2 in-port-name]
  (let [out-port (some

                   #(when (= (.-label %) out-port-name) %)
                   (.getOutPorts node-1))
        in-port (some

                  #(when (= (.-label %) in-port-name) %)
                  (.getInPorts node-2))]
    (.link out-port in-port)))
