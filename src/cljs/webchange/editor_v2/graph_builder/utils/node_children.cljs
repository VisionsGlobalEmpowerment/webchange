(ns webchange.editor-v2.graph-builder.utils.node-children)

(defn get-children
  ([node-name node-data]
   (get-children node-name node-data :root))
  ([node-name node-data prev-node]
   (get-children node-name node-data prev-node nil))
  ([node-name node-data prev-node searched-sequence]
   (when (and (sequential? searched-sequence)
              (not (vector? searched-sequence)))
     (-> (str "Sequence must be a vector") js/Error. throw))
   (let [searched-sequence (if (and (not (nil? searched-sequence))
                                    (not (vector? searched-sequence)))
                             [searched-sequence]
                             searched-sequence)
         result (reduce
                  (fn [result {:keys [previous sequence] :as connection}]
                    (if (and (or (nil? prev-node)
                                 (= previous prev-node))
                             (or (nil? searched-sequence)
                                 (= sequence node-name)
                                 (= sequence (last searched-sequence))))
                      (conj result connection)
                      result))
                  []
                  (:connections node-data))]
     (if (and (empty? result)
              (not (empty? (drop-last searched-sequence))))
       (get-children node-name node-data prev-node (-> searched-sequence drop-last vec))
       (set result)))))
