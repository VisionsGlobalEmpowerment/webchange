(ns webchange.editor-v2.graph-builder.graph-normalizer.clone-repetitive-nodes.process-repetitive-nodes
  (:require
    [webchange.editor-v2.graph-builder.graph-normalizer.utils :refer [get-copy-name]]
    [webchange.editor-v2.graph-builder.utils.counter-map :refer [add-to-map]]
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]
    [webchange.editor-v2.graph-builder.utils.merge-actions :refer [merge-actions]]))

(defn create-node
  [graph update-data]
  (let [{:keys [name origin-name copy-counter connections]} update-data
        new-node-data (merge (get graph origin-name)
                             {:connections  (set connections)
                              :origin-name  origin-name
                              :copy-counter copy-counter})
        exist-node-data (get graph name)
        updated-graph (merge graph (merge-actions (assoc {} name exist-node-data)
                                                  (assoc {} name new-node-data)))]
    updated-graph))

(defn update-node-connection
  [graph update-data]
  (let [{:keys [name connection patch]} update-data
        updated-graph (assoc-in graph [name :connections] (->> (get-in graph [name :connections])
                                                               (map (fn [current-connection]
                                                                      (if (= current-connection connection)
                                                                        (merge current-connection patch)
                                                                        current-connection)))
                                                               (set)))]
    updated-graph))

(defn update-graph
  [graph clone-update parent-update children-updates]
  (reduce (fn [graph child-update]
            (update-node-connection graph child-update))
          (-> graph
              (create-node clone-update)
              (update-node-connection parent-update))
          children-updates))

(defn get-new-seq-path
  [current-seq-path connection-sequence-name]
  (let [seq-path (if (some #{connection-sequence-name} current-seq-path)
                   (vec (drop-last current-seq-path))
                   current-seq-path)]
    (if-not (= connection-sequence-name (last seq-path))
      (conj seq-path connection-sequence-name)
      seq-path)))

(defn get-repetitive-node-connection-number
  [repetitive-nodes node-name node-connection]
  (let [fixed-connection (->> node-connection
                              (map (fn [[field-name field-value]]
                                     [field-name (if (keyword? field-value)
                                                   (-> field-value
                                                       (name)
                                                       (clojure.string/replace #"-copy-\d+" "")
                                                       (keyword))
                                                   field-value)]
                                     ))
                              (into (hash-map)))]
    (->> (get repetitive-nodes node-name)
         (map-indexed (fn [idx connections-group]
                        (map (fn [connection] [(inc idx) connection]) connections-group)))
         (reduce (fn [result data] (concat result data)))
         (some (fn [[idx connection]]
                 (and (= connection fixed-connection)
                      idx))))))

(defn clone-node
  [graph seq-path node-name prev-node-name prev-node-connection next-nodes-connections repetitive-nodes used-map]
  (let [copy-counter (get-repetitive-node-connection-number repetitive-nodes node-name prev-node-connection)
        new-node-name (get-copy-name node-name copy-counter)]
    (let [clone-update {:type         :create
                        :name         new-node-name
                        :origin-name  node-name
                        :copy-counter copy-counter
                        :connections  next-nodes-connections}
          children-updates (->> next-nodes-connections
                                (map (fn [{:keys [handler sequence]}]
                                       (let [node-data (get graph handler)
                                             next-seq-path (get-new-seq-path seq-path sequence)
                                             next-connections (get-children handler node-data node-name next-seq-path used-map)]
                                         (map (fn [connection]
                                                {:type       :update
                                                 :name       handler
                                                 :connection connection
                                                 :patch      {:previous new-node-name}})
                                              next-connections))))
                                (flatten))
          parent-update {:type       :update
                         :name       prev-node-name
                         :connection prev-node-connection
                         :patch      {:handler new-node-name}}]
      [(update-graph graph clone-update parent-update children-updates) new-node-name])))

(defn repetitive-node?
  [repetitive-nodes node-name]
  (contains? repetitive-nodes node-name))

(defn used-connection?
  [used-map node-name handler connection]
  (some #{[node-name handler connection]} used-map))

(defn add-to-used-map
  [used-map node-name handler connection]
  (conj used-map [node-name handler connection]))

(defn clone-repetitive-nodes
  ([graph repetitive-nodes]
   (first (clone-repetitive-nodes graph [:root nil :root] [] [] repetitive-nodes)))
  ([graph [prev-node-name prev-node-connection node-name] used-map seq-path repetitive-nodes]
   (let [node-data (get graph node-name node-name)
         new-used-map (add-to-used-map used-map prev-node-name node-name prev-node-connection)
         children (get-children node-name node-data prev-node-name seq-path new-used-map)
         [updated-graph new-node-name] (if (repetitive-node? repetitive-nodes node-name)
                                         (clone-node graph
                                                     seq-path
                                                     node-name
                                                     prev-node-name
                                                     prev-node-connection
                                                     children
                                                     repetitive-nodes
                                                     used-map)
                                         [graph node-name])]
     (reduce
       (fn [[graph used-map seq-path] {:keys [handler sequence] :as connection}]
         (if-not (used-connection? used-map new-node-name handler connection)
           (clone-repetitive-nodes graph
                                   [new-node-name connection handler]
                                   used-map
                                   (get-new-seq-path seq-path sequence)
                                   repetitive-nodes)
           [graph used-map seq-path]))
       [updated-graph new-used-map seq-path]
       (get-children new-node-name node-data prev-node-name seq-path new-used-map)))))

(defn remove-origin-repetitive-nodes
  [graph repetitive-nodes]
  (reduce (fn [graph [node-name]]
            (dissoc graph node-name))
          graph
          repetitive-nodes))

(defn remove-connections-with-repetitive-nodes
  [graph repetitive-nodes]
  (reduce (fn [graph [node-name]]
            (assoc-in graph [node-name :connections] (->> (get-in graph [node-name :connections])
                                                          (filter (fn [{:keys [previous handler]}]
                                                                    (not (or (some #{previous} (keys repetitive-nodes))
                                                                             (some #{handler} (keys repetitive-nodes))))))
                                                          (set))))
          graph
          graph))

(defn process-repetitive-nodes
  [graph repetitive-nodes]
  (-> graph
      (clone-repetitive-nodes repetitive-nodes)
      (remove-origin-repetitive-nodes repetitive-nodes)
      (remove-connections-with-repetitive-nodes repetitive-nodes)))
