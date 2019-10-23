(ns webchange.editor-v2.diagram.diagram-items-factory.utils)

(defn get-node-connections
  [node-name node-data]
  (reduce
    (fn [result [_ connection-data]]
      (concat result (reduce
                       (fn [result [event handlers]]
                         (concat result (map
                                          (fn [handler] [node-name event handler])
                                          handlers)))
                       []
                       (:handlers connection-data))))
    []
    (:connections node-data)))

(defn get-node-outs
  [node-data]
  (->> node-data
       (get-node-connections nil)
       (map (fn [[_ out _]] out))
       (distinct)))
