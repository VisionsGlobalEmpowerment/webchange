(ns webchange.editor-v2.diagram.graph-builder.utils.change-node)

(defn change-parent-node-connections
  [graph parent-node-name removing-connection-name new-connection-names]
  (let [prev-node-connections (get-in graph [parent-node-name :connections])
        prev-node-fixed-connections (reduce
                                      (fn [result [prev-node connection]]
                                        (let [new-handlers (reduce
                                                             (fn [result [event handlers]]
                                                               (let [updated-handlers (->> handlers
                                                                                           (map (fn [handler]
                                                                                                  (if (= handler removing-connection-name)
                                                                                                    new-connection-names
                                                                                                    handler)))
                                                                                           (flatten))]
                                                                 (if-not (empty? updated-handlers)
                                                                   (assoc result event updated-handlers)
                                                                   result)))
                                                             {}
                                                             (:handlers connection))]
                                          (assoc result prev-node (assoc connection :handlers new-handlers))))
                                      {}
                                      prev-node-connections)]
    (assoc-in graph [parent-node-name :connections] prev-node-fixed-connections)))

(defn change-child-node-connections
  [graph child-node-name removing-connection-name new-connection-names]
  (let [node-connections (get-in graph [child-node-name :connections])
        removing-connection-data (get node-connections removing-connection-name)
        next-node-fixed-connections (-> (reduce
                                          (fn [result connection-name]
                                            (assoc result connection-name removing-connection-data))
                                          node-connections
                                          new-connection-names)
                                        (dissoc removing-connection-name))]
    (assoc-in graph [child-node-name :connections] next-node-fixed-connections)))

(defn get-connection-handlers
  [connection-data]
  (reduce
    (fn [result [_ event-handlers]]
      (concat result event-handlers))
    []
    (:handlers connection-data)))

(defn get-node-ins
  [node-data]
  (reduce
    (fn [result [connection-name connection-data]]
      (assoc result connection-name (get-connection-handlers connection-data)))
    {}
    (:connections node-data)))

(defn get-node-outs
  [node-data]
  (reduce
    (fn [result [connection-name connection-data]]
      (let [connections (get-connection-handlers connection-data)]
        (reduce
          (fn [result connection]
            (assoc result connection (conj (get result connection)
                                           connection-name)))
          result
          connections)))
    {}
    (:connections node-data)))

(defn update-parents-nodes
  [graph removing-node-name]
  (reduce
    (fn [graph [parent-node-name new-outs]]
      (change-parent-node-connections graph parent-node-name removing-node-name new-outs))
    graph
    (->> removing-node-name
         (get graph)
         (get-node-ins))))

(defn update-children-nodes
  [graph removing-node-name]
  (reduce
    (fn [graph [child-node-name new-ins]]
      (change-child-node-connections graph child-node-name removing-node-name new-ins))
    graph
    (->> removing-node-name
         (get graph)
         (get-node-outs))))

(defn remove-node
  [graph node-name]
  (-> graph
      (update-parents-nodes node-name)
      (update-children-nodes node-name)
      (dissoc node-name)))

(defn rename-parents-connections
  [graph old-name new-name]
  (reduce
    (fn [graph [parent-node-name]]
      (change-parent-node-connections graph parent-node-name old-name [new-name]))
    graph
    (->> old-name
         (get graph)
         (get-node-ins))))

(defn rename-children-connections
  [graph old-name new-name]
  (reduce
    (fn [graph [child-node-name]]
      (change-child-node-connections graph child-node-name old-name [new-name]))
    graph
    (->> old-name
         (get graph)
         (get-node-outs))))

(defn rename-current-node
  [graph old-name new-name]
  (let [data (get graph old-name)]
    (-> graph
        (assoc new-name data)
        (dissoc old-name))))

(defn rename-node
  [graph old-name new-name]
  (-> graph
      (rename-parents-connections old-name new-name)
      (rename-children-connections old-name new-name)
      (rename-current-node old-name new-name)))

(defn get-nodes-by-prev-node-name
  [graph prev-node-name]
  (reduce
    (fn [result [node-name node-data]]
      (let [node-ins (-> node-data get-node-ins keys)]
        (if (some #{prev-node-name} node-ins)
          (conj result node-name)
          result)))
    []
    graph))

(defn get-nodes-by-next-node-name
  [graph next-node-name]
  (reduce
    (fn [result [node-name node-data]]
      (let [node-outs (-> node-data get-node-outs keys)]
        (if (some #{next-node-name} node-outs)
          (conj result node-name)
          result)))
    []
    graph))
