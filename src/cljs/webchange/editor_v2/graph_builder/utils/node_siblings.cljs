(ns webchange.editor-v2.graph-builder.utils.node-siblings)

(defn get-node-ins
  [graph node-name]
  (let [parents (->> graph
                     (filter (fn [[_ node-data]]
                               (some (fn [{:keys [handler]}]
                                       (= node-name handler))
                                     (:connections node-data))))
                     (map first))]
    (reduce
      (fn [result parent]
        (assoc result parent (->> (get-in graph [node-name :connections])
                                  (filter (fn [{:keys [previous handler]}]
                                            (and (= previous parent)
                                                 (not= handler node-name))))
                                  (map :handler))))
      {}
      parents)))

(defn get-node-outs
  ([graph node-name]
   (->> (get graph node-name)
        (get-node-outs)))
  ([node-data]
   (->> (:connections node-data)
        (reduce (fn [result {:keys [previous handler]}]
                  (update result handler conj previous))
                {}))))
