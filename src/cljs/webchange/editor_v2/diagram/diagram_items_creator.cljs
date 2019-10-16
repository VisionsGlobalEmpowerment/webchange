(ns webchange.editor-v2.diagram.diagram-items-creator
  (:require
    [webchange.editor-v2.diagram.diagram-items-utils :refer [connect-nodes
                                                             get-diagram-node]]))

(defn get-diagram-link
  [diagram-nodes node-1-name node-2-name node-1-out]
  (let [node-2-in :in
        node-1 (get diagram-nodes node-1-name)
        node-2 (get diagram-nodes node-2-name)]
    (if (or (nil? node-1) (nil? node-2))
      (-> (str "Can not connect "
               node-1-name " (" node-1-out ") and "
               node-2-name " (" node-2-in ")")
          js/Error. throw)
      [(connect-nodes
         node-1 (name node-1-out)
         node-2 (name node-2-in))])))

(defmulti get-node-links
          (fn [_ _ node-data]
            (:type node-data)))

(defmethod get-node-links :default
  [diagram-nodes node-name node-data]
  (reduce
    (fn [result [_ connection-data]]
      (concat result (reduce
                       (fn [connections next-name]
                         (concat connections (get-diagram-link diagram-nodes node-name next-name :next)))
                       []
                       (:next connection-data))))
    []
    (->> node-data :connections)))

(defn create-diagram-items
  [parsed-scene-data]
  (let [nodes (->> parsed-scene-data
                   (reduce
                     (fn [result [name data]]
                       (assoc result name (get-diagram-node name data)))
                     {}))
        links (->> parsed-scene-data
                   (reduce
                     (fn [result [node-name node-data]]
                       (concat result (get-node-links nodes node-name node-data)))
                     []))]
    [(vals nodes) links]))
