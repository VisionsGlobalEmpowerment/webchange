(ns webchange.editor-v2.diagram.diagram-model.items-factory.links-factory
  (:require
    [webchange.editor-v2.diagram.diagram-model.items-factory.utils :refer [get-node-connections]]))

(defn- show-undefined-node-error!
  [node-1 node-2 node-1-name node-2-name]
  (let [common-message (str "Can not connect "
                            node-1-name " and "
                            node-2-name ". ")
        undefined-node (cond (nil? node-1) node-1-name
                             (nil? node-2) node-2-name)
        detailed-message (str "Node '" undefined-node "' is not defined.")]
    (.error js/console (str common-message detailed-message))))

(defn- show-undefined-port-error!
  [node-1 out-port out-port-name node-2 in-port in-port-name]
  (let [[undefined-port node-name] (cond (nil? out-port) [out-port-name (.-name node-1)]
                                         (nil? in-port) [in-port-name (.-name node-2)])]
    (.error js/console (str "Port '" undefined-port "' of node '" node-name "' is not defined"))))

(defn- connect-nodes
  [node-1 out-port-name node-2 in-port-name]
  (let [out-port (some
                   #(when (= (.-label %) out-port-name) %)
                   (.getOutPorts node-1))
        in-port (some
                  #(when (= (.-label %) in-port-name) %)
                  (.getInPorts node-2))]
    (if (or (nil? out-port) (nil? in-port))
      (do (show-undefined-port-error! node-1 out-port out-port-name node-2 in-port in-port-name)
          nil)
      (.link out-port in-port))))

(defn- get-diagram-link
  ([diagram-nodes node-1-name node-1-out node-2-name]
   (get-diagram-link diagram-nodes node-1-name node-1-out node-2-name :in))
  ([diagram-nodes node-1-name node-1-out node-2-name node-2-in]
   (let [node-1 (get diagram-nodes node-1-name)
         node-2 (get diagram-nodes node-2-name)]
     (if (or (nil? node-1) (nil? node-2))
       (do (show-undefined-node-error! node-1 node-2 node-1-name node-2-name)
           nil)
       (connect-nodes
         node-1 (name node-1-out)
         node-2 (name node-2-in))))))

(defn create-link
  [nodes node-name node-data]
  (->> (get-node-connections node-name node-data)
       (map (fn [[node-name node-out target-name]]
              (get-diagram-link nodes node-name node-out target-name)))
       (filter #(not (nil? %)))))

(defn create-links
  [graph nodes]
  (reduce
    (fn [result [node-name node-data]]
      (concat result (create-link nodes node-name node-data)))
    []
    graph))
