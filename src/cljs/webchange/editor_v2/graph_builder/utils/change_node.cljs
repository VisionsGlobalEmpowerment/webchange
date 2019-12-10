(ns webchange.editor-v2.graph-builder.utils.change-node
  (:require
    [webchange.editor-v2.graph-builder.utils.node-siblings :refer [get-node-ins
                                                                   get-node-outs]]))

(defn change-parent-node-connections
  [graph parent-node-name removing-connection-name new-connection-names]
  (let [prev-node-fixed-connections (->> (get-in graph [parent-node-name :connections])
                                         (reduce
                                           (fn [result {:keys [handler] :as connection}]
                                             (concat result (if (= handler removing-connection-name)
                                                              (map (fn [new-connection-name]
                                                                     (assoc connection :handler new-connection-name))
                                                                   new-connection-names)
                                                              [connection])))
                                           [])
                                         (set))]
    (assoc-in graph [parent-node-name :connections] prev-node-fixed-connections)))

(defn change-child-node-connections
  [graph child-node-name removing-connection-name new-connection-names]
  (let [next-node-fixed-connections (->> (get-in graph [child-node-name :connections])
                                         (reduce
                                           (fn [result {:keys [previous] :as connection}]
                                             (concat result (if (= previous removing-connection-name)
                                                              (map (fn [new-connection-name]
                                                                     (assoc connection :previous new-connection-name)) new-connection-names)
                                                              [connection])))
                                           [])
                                         (set))]
    (assoc-in graph [child-node-name :connections] next-node-fixed-connections)))

(defn get-connection-handlers
  [connection-data]
  (reduce
    (fn [result [_ event-handlers]]
      (concat result event-handlers))
    []
    (:handlers connection-data)))

(defn update-parents-nodes
  [graph removing-node-name]
  (reduce
    (fn [graph [parent-node-name new-outs]]
      (change-parent-node-connections graph parent-node-name removing-node-name new-outs))
    graph
    (get-node-ins graph removing-node-name)))

(defn update-children-nodes
  [graph removing-node-name]
  (reduce
    (fn [graph [child-node-name new-ins]]
      (change-child-node-connections graph child-node-name removing-node-name new-ins))
    graph
    (get-node-outs graph removing-node-name)))

(defn remove-node
  [graph node-name]
  (-> graph
      (update-parents-nodes node-name)
      (update-children-nodes node-name)
      (dissoc node-name)))

(defn remove-handler
  [graph node-name removing-connection-name]
  (change-parent-node-connections graph node-name removing-connection-name []))

(defn remove-connection
  [graph node-name connection-data]
  (let [new-connections (->> (get-in graph [node-name :connections])
                             (filter
                               (fn [connection]
                                 (->> connection-data
                                      (reduce
                                        (fn [result [connection-data-key connection-data-value]]
                                          (and result
                                               (= connection-data-value
                                                  (get connection connection-data-key))))
                                        true)
                                      (not))))
                             (set))]
    (assoc-in graph [node-name :connections] new-connections)))

(defn rename-parents-connections
  [graph old-name new-name]
  (reduce
    (fn [graph [parent-node-name]]
      (change-parent-node-connections graph parent-node-name old-name [new-name]))
    graph
    (get-node-ins graph old-name)))

(defn rename-children-connections
  [graph old-name new-name]
  (reduce
    (fn [graph [child-node-name]]
      (change-child-node-connections graph child-node-name old-name [new-name]))
    graph
    (get-node-outs graph old-name)))

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
