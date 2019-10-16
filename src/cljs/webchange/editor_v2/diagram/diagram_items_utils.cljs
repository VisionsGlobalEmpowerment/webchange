(ns webchange.editor-v2.diagram.diagram-items-utils
  (:require
    [webchange.editor-v2.diagram.custom-diagram-items.custom-model :refer [get-custom-model]]))

(declare add-out-ports!)
(declare add-in-ports!)
(declare create-node)

(defmulti get-diagram-node
          (fn [node-data]
            (:entity node-data)))

(defmethod get-diagram-node :default
  [node-name node-data]
  (-> (create-node node-name node-data)
      (add-in-ports! [:in])
      (add-out-ports! [:next])))

;; ---

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

(defn create-node
  [name node-data]
  (get-custom-model (merge
                      node-data
                      {:name  (clojure.core/name name)})))

(defn connect-nodes
  [node-1 out-port-name node-2 in-port-name]
  (let [out-port (some
                   #(when (= (.-label %) out-port-name) %)
                   (.getOutPorts node-1))
        in-port (some
                  #(when (= (.-label %) in-port-name) %)
                  (.getInPorts node-2))]
    (.link out-port in-port)))
