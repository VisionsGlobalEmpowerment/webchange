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
      (-> (str "Can not connect " node-1-name " and " node-2-name) js/Error. throw)
      [(connect-nodes
         node-1 (name node-1-out)
         node-2 (name node-2-in))])
   ))

(defn get-node-links
  [diagram-nodes node-name node-data]
  (reduce
    (fn [res target]
      (concat res (get-diagram-link diagram-nodes node-name target "next")))
    []
    (->> node-data :next)))

(defn create-diagram-items
  [parsed-scene-data]
  (let [nodes (->> parsed-scene-data
                   (seq)
                   (reduce
                     (fn [result [name data]]
                       (assoc result name (get-diagram-node {:entity :action
                                                             :type (:type data)
                                                             :name name})))
                     {}))
        links (->> parsed-scene-data
                   (seq)
                   (reduce
                     (fn [result [node-name node-data]]
                       (concat result (get-node-links nodes node-name node-data)))
                     []))
        ]
    [(vals nodes) links]))
