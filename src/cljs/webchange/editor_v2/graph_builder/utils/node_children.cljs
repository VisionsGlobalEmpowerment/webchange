(ns webchange.editor-v2.graph-builder.utils.node-children)

(defn used-connection?
  [used-map node-name handler connection]
  (some #{[node-name handler connection]} used-map))

(defn get-children
  ([node-name node-data]
   (get-children node-name node-data :root))
  ([node-name node-data prev-node]
   (get-children node-name node-data prev-node nil nil))
  ([node-name node-data prev-node searched-sequence ]
   (get-children node-name node-data prev-node searched-sequence nil))
  ([node-name node-data prev-node searched-sequence used-map]
   (when (and (sequential? searched-sequence)
              (not (vector? searched-sequence)))
     (-> (str "Sequence must be a vector") js/Error. throw))
   (let [searched-sequence (if (and (not (nil? searched-sequence))
                                    (not (vector? searched-sequence)))
                             [searched-sequence]
                             searched-sequence)
         result (reduce
                  (fn [result {:keys [handler previous sequence] :as connection}]
                    (if (and (or (nil? prev-node)
                                 (= previous prev-node))
                             (or (nil? searched-sequence)
                                 (nil? sequence)
                                 (= sequence node-name)
                                 (= sequence (last searched-sequence)))
                             (or (nil? used-map)
                                 (not (used-connection? used-map node-name handler connection))))
                      (conj result connection)
                      result))
                  []
                  (:connections node-data))]
     (if (and (empty? result)
              (not (empty? (drop-last searched-sequence))))
       (get-children node-name node-data prev-node (-> searched-sequence drop-last vec) used-map)
       (set result)))))
