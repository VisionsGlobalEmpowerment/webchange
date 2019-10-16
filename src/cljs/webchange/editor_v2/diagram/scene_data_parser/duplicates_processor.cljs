(ns webchange.editor-v2.diagram.scene-data-parser.duplicates-processor)

(defn get-copy-name
  [origin-name number]
  (-> (name origin-name)
      (str "-copy-" number)
      (keyword)))

(defn fix-prev-usage
  [data usage-name duplicate-name new-duplicate-name]
  (let [usage-data (get data usage-name)]
    (assoc
      {}
      usage-name
      (merge usage-data
             {:connections (->> (:connections usage-data)
                                (reduce
                                  (fn [result [connection-name connection-data]]
                                    (assoc
                                      result
                                      connection-name
                                      (merge connection-data
                                             {:next (->> (:next connection-data)
                                                         (map (fn [item] (if (= item duplicate-name)
                                                                           new-duplicate-name
                                                                           item)))
                                                         (vec))})))
                                  {}))}))))

(defn fix-next-usage
  [usage-data duplicate-name new-duplicate-name]
  (merge usage-data
         {:connections (-> (:connections usage-data)
                           (assoc
                             new-duplicate-name
                             (get (:connections usage-data) duplicate-name))
                           (dissoc duplicate-name))}))

(defn fix-next-usages
  [data usages-names duplicate-name new-duplicate-name]
  (reduce
    (fn [result [usage-name usage-data]]
      (assoc result usage-name (fix-next-usage usage-data duplicate-name new-duplicate-name)))
    {}
    (select-keys data usages-names)))

(defn eliminate-duplicate-usage
  [data duplicate-name duplicate-data duplicate-number usage-prev usages-next]
  (let [new-name (get-copy-name duplicate-name duplicate-number)
        fixed-prev (fix-prev-usage data usage-prev duplicate-name new-name)
        fixed-next (fix-next-usages data usages-next duplicate-name new-name)]
    (-> data
        (dissoc duplicate-name)
        (assoc new-name (merge duplicate-data
                               {:original    duplicate-name
                                :connections (select-keys
                                               (:connections duplicate-data)
                                               [usage-prev])}))
        (merge fixed-prev)
        (merge fixed-next))))

(defn eliminate-duplicates
  [data [duplicate-name duplicate-data]]
  (let [connections (->> duplicate-data :connections seq)
        connections-indexed (map-indexed (fn [idx itm] [idx itm]) connections)]
    (reduce
      (fn [result [index [connection-name connection-data]]]
        (eliminate-duplicate-usage result
                                   duplicate-name
                                   duplicate-data
                                   index
                                   connection-name
                                   (:next connection-data)))
      data
      connections-indexed)))

(defn get-duplicated-nodes
  [data]
  (->> (seq data)
       (reduce
         (fn [result [node-name node-data]]
           (let [connections-number (->> node-data :connections keys count)]
             (if (> connections-number 1)
               (conj result [node-name node-data])
               result)))
         [])))

(defn process-duplicates
  [data]
  (->> (get-duplicated-nodes data)
       (reduce eliminate-duplicates data)))
