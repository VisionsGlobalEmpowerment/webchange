(ns webchange.editor-v2.graph-builder.graph-normalizer.clone-repetitive-nodes
  (:require
    [webchange.editor-v2.graph-builder.graph-normalizer.utils :refer [get-copy-name]]
    [webchange.editor-v2.graph-builder.utils.counter-map :refer [add-to-map]]
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]))

(defn get-node-as-handler-counts
  [graph]
  (reduce
    (fn [result [_ node-data]]
      (reduce
        (fn [result {:keys [handler]}]
          (update-in result [handler] inc))
        result
        (:connections node-data)))
    {}
    graph))

(defn get-repetitive-nodes
  [graph]
  (->> graph
       (get-node-as-handler-counts)
       (seq)
       (filter (fn [[_ node-links-count]] (<= 2 node-links-count)))
       (map first)))

(defn update-child-connection
  [graph child-name new-previous-node-name connections-to-update]
  (assoc-in
    graph
    [child-name :connections]
    (->> (get-in graph [child-name :connections])
         (map (fn [connection]
                (if (some #{connection} connections-to-update)
                  (assoc connection :previous new-previous-node-name)
                  connection)))
         (set))))

(defn update-children-connections
  [graph old-node-name new-node-name connections seq-path]
  (reduce
    (fn [graph {:keys [handler sequence]}]
      (if-not (nil? handler)
        (let [child-data (get graph handler)
              new-seq-path (-> seq-path (conj sequence) dedupe vec)
              next-connections (get-children handler child-data old-node-name new-seq-path)]
          (update-child-connection graph handler new-node-name next-connections))
        graph))
    graph
    connections))

(defn update-parent-connection
  [graph parent-name parent-connection new-node-name]
  (assoc-in graph
            [parent-name :connections]
            (->> (get-in graph [parent-name :connections])
                 (map (fn [connection]
                        (if (= connection parent-connection)
                          (assoc connection :handler new-node-name)
                          connection)))
                 (set))))

(defn add-new-node
  [graph old-node-name new-node-name connections copy-counter]
  (assoc
    graph
    new-node-name
    (merge (get graph old-node-name)
           {:connections connections
            :origin-name old-node-name
            :copy-counter copy-counter})))

(defn clone-node
  [graph node-name node-data prev-node-name prev-node-connection clones-counter seq-path]
  (let [[clones-counter current-number] (add-to-map clones-counter node-name)
        new-node-name (get-copy-name node-name current-number)
        children (get-children node-name node-data prev-node-name seq-path)
        graph (-> graph
                  (update-parent-connection prev-node-name prev-node-connection new-node-name)
                  (add-new-node node-name new-node-name children current-number)
                  (update-children-connections node-name new-node-name children seq-path))]
    [graph new-node-name clones-counter]))

(defn process-repetitive-nodes-dfs
  [graph [prev-node-name prev-node-connection node-name] seq-path clones-counter repetitive-nodes]
  (let [node-data (get graph node-name)
        clone-node? (->> repetitive-nodes (some #{node-name}) (boolean))
        [graph node-name clones-counter] (if clone-node?
                                           (clone-node graph
                                                       node-name
                                                       node-data
                                                       prev-node-name
                                                       prev-node-connection
                                                       clones-counter
                                                       seq-path)
                                           [graph node-name clones-counter])
        node-origin-name (get-in graph [node-name :origin-name])
        children (get-children (or node-origin-name node-name) node-data prev-node-name seq-path)]
    (reduce
      (fn [[graph seq-path clones-counter] {:keys [handler sequence] :as connection}]
        (let [seq-path (if (some #{sequence} seq-path)
                        (vec (drop-last seq-path))
                        seq-path)
              new-seq-path (if-not (= sequence (last seq-path))
                             (conj seq-path sequence)
                             seq-path)]
          (process-repetitive-nodes-dfs graph [node-name connection handler] new-seq-path clones-counter repetitive-nodes)))
      [graph seq-path clones-counter]
      children)))

(defn process-repetitive-nodes
  [graph repetitive-nodes]
  (-> graph
      (process-repetitive-nodes-dfs [:root nil :root] [] {} repetitive-nodes)
      (first)))

(defn remove-nodes
  [graph nodes]
  (reduce
    (fn [graph node]
      (dissoc graph node))
    graph
    nodes))

(defn clone-repetitive-nodes
  [graph]
  (let [repetitive-nodes (get-repetitive-nodes graph)]
    (-> graph
        (process-repetitive-nodes repetitive-nodes)
        (remove-nodes repetitive-nodes))))
