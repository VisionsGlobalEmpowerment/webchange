(ns webchange.editor-v2.diagram.scene-parser.duplicates-replicator.duplicates-replicator
  (:require
    [webchange.editor-v2.diagram.scene-parser.duplicates-replicator.usages-counter :refer [get-reuse-map]]
    [webchange.editor-v2.diagram.scene-parser.graph-utils.root-nodes :refer [add-root-node
                                                                             get-root-nodes
                                                                             remove-root-node]]
    [webchange.editor-v2.diagram.scene-parser.graph-utils.node-children :refer [get-children]]
    [webchange.editor-v2.diagram.scene-parser.utils :refer [add-to-map
                                                            map-has-name?]]))
(defn get-copy-name
  [origin-name number]
  (-> origin-name
      (name)
      (str "-copy-" number)))

(defn change-connection-name
  [graph node-name prev-parent-name new-parent-name]
  (let [node-data (get graph node-name)
        new-node-connections (reduce
                               (fn [result [connection-name connection-data]]
                                 (let [new-connection-name (if (= connection-name prev-parent-name)
                                                             new-parent-name
                                                             connection-name)]
                                   (assoc result new-connection-name connection-data)))
                               {}
                               (:connections node-data))]
    (assoc-in graph [node-name :connections] new-node-connections)))

(defn change-handlers-name
  [graph node-name prev-child-name new-child-name]
  (let [node-data (get graph node-name)
        new-node-connections (reduce
                               (fn [result [connection-name connection-data]]
                                 (let [new-connection-handlers (reduce
                                                                 (fn [result [event-name event-handlers]]
                                                                   (let [new-event-handlers (map
                                                                                              (fn [event-handler]
                                                                                                (if (= event-handler prev-child-name)
                                                                                                  new-child-name
                                                                                                  event-handler))
                                                                                              event-handlers)]
                                                                     (assoc result event-name new-event-handlers)))
                                                                 {}
                                                                 (:handlers connection-data))]
                                   (assoc result connection-name (assoc connection-data :handlers new-connection-handlers))))
                               {}
                               (:connections node-data))]
    (assoc-in graph [node-name :connections] new-node-connections)))

(defn filter-node-connections
  [node-data connection-name]
  (assoc node-data :connections (select-keys (:connections node-data) [connection-name])))

(defn rename-node-connection
  [node-data connection-name new-connection-name]
  (if-not (= connection-name new-connection-name)
    (let [connections (:connections node-data)]
      (assoc node-data :connections (-> connections
                                        (assoc new-connection-name (get connections connection-name))
                                        (dissoc connection-name))))
    node-data))

(defn change-node-connection-parent
  [node-data connection-name new-parent-name]
  (let [new-node-connections (reduce
                               (fn [result [current-connection-name current-connection-data]]
                                 (let [new-connection-data (if (and (= connection-name current-connection-name)
                                                                    (contains? current-connection-data :parent))
                                                             (assoc current-connection-data :parent new-parent-name)
                                                             current-connection-data)]
                                   (assoc result current-connection-name new-connection-data)))
                               {}
                               (:connections node-data))]
    (assoc node-data :connections new-node-connections)))

(defn add-origin
  [node-data origin-name]
  (assoc node-data :origin origin-name))

(defn process-node
  [node-name node-data prev-node-name origin-prev-node-name reused-nodes graph]
  (let [replicate? (map-has-name? reused-nodes node-name)]
    (if replicate?
      (let [[reused-nodes counter] (add-to-map reused-nodes node-name)
            new-node-name (-> node-name (get-copy-name counter) (keyword))]
        {:new-node-name new-node-name
         :reused-nodes  reused-nodes
         :graph         (-> graph
                            (assoc new-node-name (-> node-data
                                                     (filter-node-connections origin-prev-node-name)
                                                     (rename-node-connection origin-prev-node-name prev-node-name)
                                                     (add-origin node-name)))
                            (change-handlers-name prev-node-name node-name new-node-name))})
      {:new-node-name node-name
       :reused-nodes  reused-nodes
       :graph         graph})))

(defn replicate-dfs
  "Depth-first-search"
  ([graph reused-nodes]
   (replicate-dfs graph [:root :root :root] reused-nodes))
  ([graph [origin-prev-node-name prev-node-name node-name] reused-nodes]
   (let [node-data (get graph node-name)
         {:keys [graph new-node-name reused-nodes]} (process-node node-name node-data prev-node-name origin-prev-node-name reused-nodes graph)]
     (reduce
       (fn [[graph reused-nodes] next-node-name]
         (replicate-dfs graph [node-name new-node-name next-node-name] reused-nodes))
       [graph reused-nodes]
       (get-children node-data origin-prev-node-name)))))

(defn remove-reused-nodes
  [graph reused-nodes]
  (reduce
    (fn [graph [reused-node-name]]
      (dissoc graph reused-node-name))
    graph
    reused-nodes))

(defn replicate-reused-nodes
  [parsed-data start-nodes reused-nodes]
  (-> parsed-data
      (add-root-node start-nodes)
      (replicate-dfs reused-nodes)
      (first)
      (remove-reused-nodes reused-nodes)
      (remove-root-node)))

(defn untangle-reuses
  [parsed-data]
  (let [root-nodes (get-root-nodes parsed-data)
        reused-nodes (get-reuse-map parsed-data root-nodes)]
    (replicate-reused-nodes parsed-data root-nodes reused-nodes)))
