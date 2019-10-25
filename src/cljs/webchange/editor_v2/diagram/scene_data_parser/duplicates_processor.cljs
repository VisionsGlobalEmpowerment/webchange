(ns webchange.editor-v2.diagram.scene-data-parser.duplicates-processor)

(defn get-copy-name
  [origin-name number]
  (-> (name origin-name)
      (str "-copy-" number)
      (keyword)))

(defn get-duplicated-nodes-map
  [data]
  (reduce
    (fn [result [node-name node-data]]
      (let [connections-number (->> node-data :connections keys count)]
        (if (> connections-number 1)
          (assoc result node-name 0)
          result)))
    {}
    data))

(defn get-fixed-prev-node
  [nodes duplicate-name new-duplicate-name]
  (reduce
    (fn [result [node-name node-data]]
      (assoc
        result
        node-name
        (assoc
          node-data
          :connections
          (reduce
            (fn [result [connection-name connection-data]]
              (assoc
                result
                connection-name
                (assoc
                  connection-data
                  :handlers
                  (reduce
                    (fn [result [event-name event-handlers]]
                      (assoc result
                        event-name
                        (map
                          (fn [handler]
                            (if (= handler duplicate-name)
                              new-duplicate-name
                              handler))
                          event-handlers)))
                    {}
                    (:handlers connection-data)))))
            {}
            (:connections node-data)))))
    {}
    nodes))

(defn get-fixed-next-nodes
  [nodes duplicate-name new-duplicate-name]
  (reduce
    (fn [result [node-name node-data]]
      (let [connections (:connections node-data)]
        (assoc
          result
          node-name
          (merge
            node-data
            {:connections (merge
                            connections
                            (if (contains? connections duplicate-name)
                              (assoc {} new-duplicate-name (get connections duplicate-name))
                              {}))}))))
    {}
    nodes))

(defn get-node-copy
  [node-name node-data duplicate-counter prev-node-name]
  (let [copy-name (get-copy-name node-name duplicate-counter)
        copy-connections (get-in node-data [:connections prev-node-name])]
    [copy-name (merge node-data
                      {:origin      node-name
                       :connections (assoc
                                      {}
                                      (if (= prev-node-name node-name)
                                        (get-copy-name node-name (dec duplicate-counter))
                                        prev-node-name)
                                      copy-connections)})]))

(defn get-prev-siblings
  [node-data]
  (map
    (fn [[connection-name]]
      connection-name)
    (:connections node-data)))

(defn get-next-siblings
  ([node-data]
   (get-next-siblings node-data nil))
  ([node-data connection]
   (reduce
     (fn [result [connection-name connection-data]]
       (concat result (if (or (nil? connection) (= connection-name connection))
                        (reduce
                          (fn [result [_ event-handlers]]
                            (concat result event-handlers))
                          []
                          (:handlers connection-data))
                        [])))
     []
     (:connections node-data))))

(defn check-duplicate
  [data duplicates node-name prev-node-name]
  (if (contains? duplicates node-name)
    (let [duplicate-counter (get duplicates node-name)
          duplicates (assoc duplicates node-name (inc duplicate-counter))
          node-data (get data node-name)
          [new-node-name new-node-data] (get-node-copy node-name node-data duplicate-counter prev-node-name)
          prev-siblings (get-prev-siblings new-node-data)
          next-siblings (get-next-siblings new-node-data)
          data (merge data
                      (assoc {} new-node-name new-node-data)
                      (get-fixed-prev-node (select-keys data prev-siblings) node-name new-node-name)
                      (get-fixed-next-nodes (select-keys data next-siblings) node-name new-node-name))]
      [data duplicates])
    [data duplicates]))

(defn dfs
  "Depth-first-search"
  [data prev-node node-name duplicates]
  (let [[data duplicates] (check-duplicate data duplicates node-name prev-node)]
    (reduce
      (fn [[data duplicates] next-node]
        (let [[updated-data updated-duplicates] (dfs data node-name next-node duplicates)]
          [(merge data updated-data) updated-duplicates]))
      [data duplicates]
      (get-next-siblings (get data node-name) prev-node))))

(defn remove-duplicate-origins
  [data duplicates]
  (reduce
    (fn [data duplicate-name]
      (let [duplicate-data (get data duplicate-name)
            fixed-siblings (reduce
                             (fn [result [node-name node-data]]
                               (if-not (= node-name duplicate-name)
                                 (assoc
                                   result
                                   node-name
                                   (merge
                                     node-data
                                     {:connections (dissoc (:connections node-data) duplicate-name)}))
                                 result)
                               )
                             {}
                             (select-keys data (get-next-siblings duplicate-data)))]
        (-> data
            (dissoc duplicate-name)
            (merge fixed-siblings))))
    data
    duplicates))

(defn remove-duplicates
  [entries data]
  (let [duplicates (get-duplicated-nodes-map data)
        data (first (reduce
                      (fn [[data duplicates] [entry-name]]
                        (dfs data :root entry-name duplicates))
                      [data duplicates]
                      entries))]
    (remove-duplicate-origins data (keys duplicates))))
