(ns webchange.editor-v2.graph-builder.duplicates-replicator.duplicates-replicator
  (:require
    [webchange.editor-v2.graph-builder.duplicates-replicator.usages-counter :refer [get-reuse-map]]
    [webchange.editor-v2.graph-builder.utils.node-siblings :refer [get-node-outs]]
    [webchange.editor-v2.graph-builder.utils.counter-map :refer [add-to-map
                                                                 map-has-name?]]
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [add-root-node
                                                                get-root-nodes
                                                                remove-root-node]]))

(defn get-copy-name
  [origin-name number]
  (-> origin-name
      (name)
      (str "-copy-" number)))

(defn change-connection-name
  [graph node-name prev-parent-name new-parent-name]
  (let [node-data (get graph node-name)
        new-node-connections (->> (:connections node-data)
                                  (map (fn [{:keys [previous] :as connection}]
                                         (if (= previous prev-parent-name)
                                           (assoc connection :previous new-parent-name)
                                           connection)))
                                  (set))]
    (assoc-in graph [node-name :connections] new-node-connections)))

(defn change-children-connection-name
  [graph children node-name new-node-name]
  (reduce
    (fn [graph child]
      (change-connection-name graph child node-name new-node-name))
    graph
    children))

(defn change-handlers-name
  [graph node-name prev-child-name new-child-name]
  (let [node-data (get graph node-name)
        new-node-connections (->> (:connections node-data)
                                  (map (fn [{:keys [handler] :as connection}]
                                         (if (= handler prev-child-name)
                                           (assoc connection :handler new-child-name)
                                           connection)))
                                  (set))]
    (assoc-in graph [node-name :connections] new-node-connections)))

(defn filter-node-connections
  [node-data connection-name]
  (assoc node-data :connections (->> (:connections node-data)
                                     (filter (fn [{:keys [previous]}]
                                               (= previous connection-name)))
                                     (set))))

(defn rename-node-connection
  [node-data connection-name new-connection-name]
  (if-not (= connection-name new-connection-name)
    (assoc node-data :connections (->> (:connections node-data)
                                       (map (fn [{:keys [previous] :as connection}]
                                              (if (= previous connection-name)
                                                (assoc connection :previous new-connection-name)
                                                connection)))
                                       (set)))
    node-data))

(defn change-node-connection-parent
  [node-data connection-name new-parent-name]
  (assoc node-data :connections (->> (:connections node-data)
                                     (map (fn [{:keys [previous] :as connection}]
                                            (if (= previous connection-name)
                                              (assoc connection :sequence new-parent-name)
                                              connection)))
                                     (set))))

(defn add-origin
  [node-data origin-name counter]
  (-> node-data
      (assoc :origin origin-name)
      (assoc :copy-counter counter)))

(defn process-node
  [node-name node-data prev-node-name origin-prev-node-name reused-nodes graph]
  (let [replicate? (map-has-name? reused-nodes node-name)]
    (if replicate?
      (let [[reused-nodes counter] (add-to-map reused-nodes node-name)
            new-node-name (-> node-name (get-copy-name counter) (keyword))
            new-node-data (-> node-data
                              (filter-node-connections origin-prev-node-name)
                              (rename-node-connection origin-prev-node-name prev-node-name)
                              (add-origin node-name counter))]
        {:new-node-name new-node-name
         :reused-nodes  reused-nodes
         :graph         (-> graph
                            (assoc new-node-name new-node-data)
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
       (map :handler (get-children node-name node-data origin-prev-node-name))))))

(defn update-replicated-children
  [graph]
  (reduce
    (fn [graph [node-name node-data]]
      (if (contains? node-data :origin)
        (change-children-connection-name graph
                                         (->> node-data get-node-outs keys)
                                         (:origin node-data)
                                         node-name)
        graph))
    graph
    graph))

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
      (update-replicated-children)
      (remove-reused-nodes reused-nodes)
      (remove-root-node)))

(defn untangle-reuses
  [parsed-data]
  (let [root-nodes (get-root-nodes parsed-data)
        reused-nodes (get-reuse-map parsed-data root-nodes)]
    (replicate-reused-nodes parsed-data root-nodes reused-nodes)))