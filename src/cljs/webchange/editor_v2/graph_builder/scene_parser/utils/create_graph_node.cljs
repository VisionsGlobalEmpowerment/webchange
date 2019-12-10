(ns webchange.editor-v2.graph-builder.scene-parser.utils.create-graph-node)

(defn create-graph-node-connection
  [{:keys [previous name handler sequence]}]
  (let [default-previous :root
        default-name "next"
        connection (-> {}
                       (assoc :previous (if-not (nil? previous) previous default-previous))
                       (assoc :name (if-not (nil? name) name default-name)))]
    (cond-> connection
            sequence (assoc :sequence sequence)
            (string? handler) (assoc :handler (keyword handler))
            (keyword? handler) (assoc :handler handler))))

(defn create-graph-node
  [{:keys [data path connections]}]
  {:data        data
   :path        path
   :connections (if (sequential? connections)
                  (->> connections
                       (filter (fn [{:keys [handler]}] (-> handler nil? not)))
                       (map create-graph-node-connection)
                       (set))
                  #{})})
