(ns webchange.editor-v2.graph-builder.graph-normalizer.clone-repetitive-nodes.get-repetitive-nodes
  (:require
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]))

(defn get-nodes-in-connections
  ([graph]
   (first (get-nodes-in-connections graph [:root :root] {} {})))
  ([graph [prev-node-name node-name] result visited-map]
   (let [node-data (get graph node-name)]
     (reduce
       (fn [[result visited-map] {:keys [handler] :as connection}]
         (println ">>" handler)
         (println "connection" connection)
         (println "map" (get visited-map handler))
         (if-not (some #{connection} (get visited-map handler))
           (get-nodes-in-connections graph
                                     [node-name handler]
                                     (update result handler conj [node-name connection])
                                     (update visited-map handler conj connection))
           [(update result handler conj [node-name connection]) visited-map])
         )
       [result visited-map]
       (get-children node-name node-data prev-node-name)))))

(defn get-repetitive-nodes
  [graph]
  (->> graph
       (get-nodes-in-connections)
       ;; for each node reduce connections with same previous node
       ;; (when parent of current node has many parents)
       (map (fn [[node-name node-connections]]
              [node-name (first (reduce (fn [[result used-nodes] [prev-node connection]]
                                          (if-not (contains? used-nodes prev-node)
                                            [(conj result connection)
                                             (assoc used-nodes prev-node true)]
                                            [result used-nodes]))
                                        [[] {}]
                                        node-connections))]))
       ;; for each node reduce connections with same connection data
       ;; (when parents of current node are children of one parallel action)
       ;(map (fn [[node-name node-connections]]
       ;       [node-name (->> node-connections distinct vec)]))
       ;; clone node with many connections
       ;(filter (fn [[_ node-connections]]
       ;          (< 1 (count node-connections))))
       (into (sorted-map))))
