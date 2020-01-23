(ns webchange.editor-v2.graph-builder.graph-normalizer.clone-repetitive-nodes.get-repetitive-nodes
  (:require
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]))

(defn merge-equal-connections
  [data]
  (map (fn [[node-name node-connections]]
         [node-name (->> node-connections
                         (reduce (fn [[result clean-connections] connection]
                                   (let [clean-connection (dissoc connection :info)]
                                     (if-not (some #{clean-connection} clean-connections)
                                       [(conj result connection)
                                        (conj clean-connections clean-connection)]
                                       [result clean-connections])))
                                 [[] []])
                         (first))])
       data))

(defn filter-single-connection-nodes
  [data]
  (filter (fn [[_ node-connections]]
            (<= 2 (count node-connections)))
          data))

(defn remove-extra-info
  [data]
  (map (fn [[node-name node-connections-groups]]
         [node-name (map (fn [node-connections]
                           (map (fn [connection]
                                  (dissoc connection :info))
                                node-connections))
                         node-connections-groups)])
       data))

(defn used-connection?
  [used-map node-name connection]
  (some #{[node-name connection]} used-map))

(defn add-to-used-map
  [used-map node-name connection]
  (conj used-map [node-name connection]))

(defn get-new-seq-path
  [current-seq-path connection-sequence-name]
  (let [seq-path (if (some #{connection-sequence-name} current-seq-path)
                   (vec (drop-last current-seq-path))
                   current-seq-path)]
    (if-not (= connection-sequence-name (last seq-path))
      (conj seq-path connection-sequence-name)
      seq-path)))

(defn get-connections-to-node
  ([graph]
   (first (get-connections-to-node graph [:root nil :root] [] [] {})))
  ([graph [prev-node-name prev-node-connection node-name] used-map seq-path result]
   (let [node-data (get graph node-name node-name)
         new-used-map (add-to-used-map used-map prev-node-name prev-node-connection)]
     (reduce
       (fn [[result used-map seq-path] {:keys [handler sequence] :as connection}]
         (let [new-result (update-in result [handler] conj (merge connection
                                                                  {:info {:prev-parent      (:previous prev-node-connection)
                                                                          :prev-parent-type (get-in graph [(:previous prev-node-connection) :data :type])}}))]
           (if-not (used-connection? used-map node-name connection)
             (get-connections-to-node graph
                                      [node-name connection handler]
                                      used-map
                                      (get-new-seq-path seq-path sequence)
                                      new-result)
             [new-result used-map seq-path])))
       [result new-used-map seq-path]
       (get-children node-name node-data prev-node-name seq-path new-used-map)))))

(defn group-parallel-children-connections
  [data]
  (map (fn [[node-name connections-groups]]
         (let [[parallel-children rest-children] (reduce (fn [[parallel-children rest-children] connections-group]
                                                           (if (and (= 1 (count connections-group))
                                                                    (= "parallel" (get-in (first connections-group) [:info :prev-parent-type])))
                                                             [(conj parallel-children connections-group) rest-children]
                                                             [parallel-children (conj rest-children connections-group)]))
                                                         [[] []]
                                                         connections-groups)
               grouped-parallel-children (->> parallel-children
                                              (reduce (fn [result connections]
                                                        (let [group-id (str (count connections) "-"
                                                                            (-> connections first (get-in [:info :prev-parent]) name) "-"
                                                                            (-> connections first (get-in [:info :prev-parent-type])))]
                                                          (update result group-id conj connections)))
                                                      {})
                                              (reduce (fn [result [_ group-items]]
                                                        (conj result (flatten group-items)))
                                                      []))]
           [node-name (concat grouped-parallel-children rest-children)]))
       data))

;; ToDo: remove when clone repetitive nodes is fixed
(defn filter-extra-nodes
  [extra-nodes-map]
  (let [actions-to-clone [; tests
                          :a
                          :b
                          :c
                          :d
                          :e
                          :f
                          ;; common
                          :empty-big
                          :empty-small
                          :set-reminder-on
                          :set-reminder-off
                          ;; home
                          :group-3-times-var
                          ;; sandbox
                          :mari-short-letter-var
                          :mari-long-letter-var
                          :word-1-state-var
                          :word-2-state-var
                          :word-3-state-var
                          :word-4-state-var
                          ]]
    (select-keys extra-nodes-map actions-to-clone)))

(defn get-repetitive-nodes
  [graph]
  (let [repetitive-nodes (->> graph
                              (get-connections-to-node)
                              (merge-equal-connections)
                              (filter-single-connection-nodes)
                              (reduce (fn [result [node-name node-connections]]
                                        (assoc result node-name (->> node-connections
                                                                     (map (fn [connection] [connection]))
                                                                     (vec))))
                                      {})
                              (group-parallel-children-connections)
                              (remove-extra-info)
                              (into (hash-map))
                              (filter-extra-nodes))]
    repetitive-nodes))
