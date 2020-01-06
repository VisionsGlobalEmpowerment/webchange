(ns webchange.editor-v2.graph-builder.scene-parser.utils.create-graph-node)

(defn get-sequence-item-name
  [parent-sequence-name index]
  (-> (clojure.core/name parent-sequence-name)
      (str "-" index)
      (keyword)))

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

(defn normalize-connection-previous-data
  [{:keys [previous] :as connection}]
  (if (sequential? previous)
    (reduce
      (fn [result prev]
        (conj result (assoc connection :previous prev)))
      []
      previous)
    connection))

(defn normalize-connections-data
  [connections]
  (->> connections
       (filter (fn [{:keys [handler]}] (-> handler nil? not)))
       (map normalize-connection-previous-data)
       (flatten)))

(defn create-graph-node
  [{:keys [data path connections]}]
  {:data        data
   :path        path
   :connections (if (sequential? connections)
                  (->> connections
                       (normalize-connections-data)
                       (map create-graph-node-connection)
                       (set))
                  #{})})
