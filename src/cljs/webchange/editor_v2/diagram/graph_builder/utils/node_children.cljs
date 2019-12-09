(ns webchange.editor-v2.diagram.graph-builder.utils.node-children)

(defn get-children
  ([node-data]
   (get-children node-data :root))
  ([node-data prev-node]
   (reduce
     (fn [result [connection-name connection-data]]
       (concat result (if (or (nil? prev-node)
                              (= connection-name prev-node))
                        (reduce
                          (fn [result [_ event-handlers]]
                            (concat result event-handlers))
                          []
                          (:handlers connection-data))
                        [])))
     []
     (:connections node-data))))
