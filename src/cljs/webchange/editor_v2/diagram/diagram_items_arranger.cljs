(ns webchange.editor-v2.diagram.diagram-items-arranger
  (:require
    [webchange.editor-v2.diagram.diagram-items-utils :refer [set-node-position]]
    ))

(defn get-entries
  [nodes-data]
  (->> nodes-data
       (seq)
       (filter (fn [[_ node]] (contains? #{:object :global-object} (:entity node))))
       (map (fn [[key _]] key))
       (vec)))

(defn get-simple-graph
  [nodes-data]
  (reduce
    (fn [result [key node]]
      (assoc result key (reduce
                          (fn [result out]
                            (->> out (concat result) (vec)))
                          []
                          (->> node :outs vals))))
    {}
    (seq nodes-data)))

(defn get-nodes-distances
  [graph start-nodes]
  (let [distances (loop [que start-nodes
                         distances {}
                         current 0]
                    (let [updated-distance (reduce (fn [distances node]
                                                     (if (nil? (get distances node))
                                                       (assoc distances node current)
                                                       distances))
                                                   distances que)
                          next-que (reduce (fn [que node]
                                             (concat que (get graph node)))
                                           [] que)]
                      (if-not (= 0 (count next-que))
                        (recur next-que updated-distance (inc current))
                        updated-distance)))]
    (reduce
      (fn [distances [node]]
        (if (nil? (get distances node))
          (assoc distances node -1)
          distances))
      distances
      (seq graph))))

(defn arrange-items
  [{:keys [scene-data diagram-items]}]
  (let [[nodes links] diagram-items
        distances (get-nodes-distances
                    (get-simple-graph scene-data)
                    (get-entries scene-data))]
    (loop [[node & rest-nodes] nodes
           y-positions {}]
      (if (nil? node)
        [nodes links]
        (let [node-name (->> node .-props .-name keyword)
              x-position (get distances node-name)
              y-position (or (get y-positions x-position) 0)]
          (set-node-position node (* 350 x-position) (* y-position 150))
          (recur rest-nodes (assoc y-positions x-position (inc y-position)))))))
  diagram-items)
