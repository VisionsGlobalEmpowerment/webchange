(ns webchange.editor-v2.diagram.scene-parser.duplicates-replicator.utils)

(defn add-to-map
  [map name]
  (let [key (keyword name)
        current-value (get map key)
        new-value (inc current-value)]
    [(assoc map key new-value) new-value]))

(defn map-has-name?
  [map name]
  (->> (keyword name)
       (contains? map)))

(defn get-next-children
  ([node-data]
   (get-next-children node-data :root))
  ([node-data prev-node]
   (reduce
     (fn [result [connection-name connection-data]]
       (concat result (if (= connection-name prev-node)
                        (reduce
                          (fn [result [_ event-handlers]]
                            (concat result event-handlers))
                          []
                          (:handlers connection-data))
                        [])))
     []
     (:connections node-data))))

(defn add-root-node
  [parsed-data start-nodes]
  (assoc parsed-data :root {:type        "root"
                            :connections {:root {:handlers {:next start-nodes}}}}))

(defn remove-root-node
  [parsed-data]
  (dissoc parsed-data :root))