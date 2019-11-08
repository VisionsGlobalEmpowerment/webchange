(ns webchange.editor-v2.diagram.diagram-model.items-factory.utils)

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
  (reduce
    (fn [result [_ connection-data]]
      (concat result (keys (:handlers connection-data))))
    []
    (:connections node-data)))
