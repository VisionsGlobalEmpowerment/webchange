(ns webchange.editor-v2.graph-builder.graph-normalizer.clone-repetitive-nodes.process-repetitive-nodes
  (:require
    [webchange.editor-v2.graph-builder.graph-normalizer.utils :refer [get-copy-name]]
    [webchange.editor-v2.graph-builder.utils.counter-map :refer [add-to-map]]
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]
    [webchange.editor-v2.graph-builder.utils.merge-actions :refer [merge-actions]]))

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
           {:connections  connections
            :origin-name  old-node-name
            :copy-counter copy-counter})))

(defn get-repetitive-data-connection
  [repetitive-node-data connection]
  (some (fn [data]
          (and (= (second data) connection)
               data))
        repetitive-node-data))

(defn update-copy-node
  [graph node-name children]
  (merge graph (merge-actions (assoc {} node-name (get graph node-name))
                              (assoc {} node-name {:connections children}))))

(defn clone-node
  [graph node-name node-data prev-node-name prev-node-connection clones-counter seq-path repetitive-node-data]
  (let [ready-copy-name (-> repetitive-node-data
                            (get-repetitive-data-connection prev-node-connection)
                            (first))]
    (if (nil? ready-copy-name)
      (let [[clones-counter current-number] (add-to-map clones-counter node-name)
            new-node-name (get-copy-name node-name current-number)
            children (get-children node-name node-data prev-node-name seq-path)
            graph (-> graph
                      (update-parent-connection prev-node-name prev-node-connection new-node-name)
                      (add-new-node node-name new-node-name children current-number)
                      (update-children-connections node-name new-node-name children seq-path))]
        [graph new-node-name clones-counter])
      (let [new-node-name ready-copy-name
            children (get-children node-name node-data prev-node-name seq-path)
            graph (-> graph
                      (update-parent-connection prev-node-name prev-node-connection new-node-name)
                      (update-copy-node new-node-name children)
                      (update-children-connections node-name new-node-name children seq-path))]
        [graph new-node-name clones-counter]))))

(defn update-repetitive-nodes
  [repetitive-nodes node-origin-name node-connection node-new-name]
  (let [new-value (assoc @repetitive-nodes node-origin-name (map (fn [[copy-name connection]]
                                                                   (if (= connection node-connection)
                                                                     [node-new-name connection]
                                                                     [copy-name connection]))
                                                                 (get @repetitive-nodes node-origin-name)))]
    (reset! repetitive-nodes new-value)
    repetitive-nodes))

(defn process-repetitive-nodes-dfs
  [graph [prev-node-name prev-node-connection node-name] seq-path clones-counter repetitive-nodes]
  (let [node-data (get graph node-name)
        clone-node? (contains? @repetitive-nodes node-name)
        [graph node-name clones-counter] (if clone-node?
                                           (clone-node graph
                                                       node-name
                                                       node-data
                                                       prev-node-name
                                                       prev-node-connection
                                                       clones-counter
                                                       seq-path
                                                       (get @repetitive-nodes node-name))
                                           [graph node-name clones-counter])
        node-origin-name (get-in graph [node-name :origin-name])
        children (get-children (or node-origin-name node-name) node-data prev-node-name seq-path)
        repetitive-nodes (if clone-node?
                           (update-repetitive-nodes repetitive-nodes node-origin-name prev-node-connection node-name)
                           repetitive-nodes)]
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
  (println "repetitive-nodes" repetitive-nodes)
  (let [prepared-repetitive-nodes (atom (reduce (fn [result [node-name node-connections]]
                                                  (assoc result node-name (map (fn [connection] [nil connection]) node-connections)))
                                                {}
                                                repetitive-nodes))]
    (-> graph
        (process-repetitive-nodes-dfs [:root nil :root] [] {} prepared-repetitive-nodes)
        (first))))
