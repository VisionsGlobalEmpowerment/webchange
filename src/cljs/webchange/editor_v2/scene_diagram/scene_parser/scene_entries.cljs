(ns webchange.editor-v2.scene-diagram.scene-parser.scene-entries)

(defn- parse-object
  [object-name object-data]
  (let [actions-data (map (fn [[_ {:keys [id]}]]
                            (keyword id))
                          (:actions object-data))
        object-root-info (if (= "text" (:type object-data)) [:default] [])]
    (assoc {} object-name {:connections (concat actions-data object-root-info)})))

(defn- parse-objects
  [scene-data]
  (->> (:objects scene-data)
       (reduce
         (fn [result [object-name object-data]]
           (merge result (parse-object object-name object-data)))
         {})))

(defn- parse-triggers
  [scene-data]
  (reduce
    (fn [result [trigger-name {:keys [action]}]]
      (assoc result trigger-name {:connections [(keyword action)]}))
    {}
    (:triggers scene-data)))

(defn get-entry-actions
  [scene-data]
  (->> (merge (parse-objects scene-data)
              (parse-triggers scene-data))
       (reduce
         (fn [result [_ {:keys [connections]}]]
           (concat result connections))
         [])
       (concat (-> scene-data :actions keys))
       (map (fn [action] [action]))))
