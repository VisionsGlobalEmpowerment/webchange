(ns webchange.templates.library.flipbook.utils)

(defn generate-name
  ([]
   (generate-name nil))
  ([parent-name]
   (let [uid (-> (java.util.UUID/randomUUID)
                 (.toString)
                 (subs 0 8))]
     (if (some? parent-name)
       (str parent-name "-" uid)
       uid))))

(defn- process-object-data
  [object-data target-name]
  (if (= (:type object-data))
    (let [children (:children object-data)
          updated-children (map (fn [_] (generate-name target-name)) children)]
      {:data      (cond-> (assoc object-data :children updated-children)
                          (contains? object-data :transition) (assoc :transition target-name))
       :to-rename (map vector children updated-children)})
    {:data      object-data
     :to-rename []}))

(defn rename-object
  ([template object-name]
   (rename-object template object-name (generate-name object-name)))
  ([template object-name new-object-name]
   (loop [result-template template
          [current-rename & rest-que] [[object-name new-object-name]]]
     (if (some? current-rename)
       (let [[source-name target-name] current-rename
             {:keys [data to-rename]} (process-object-data
                                        (get result-template (keyword source-name))
                                        target-name)]
         (recur
           (-> result-template
               (dissoc (keyword source-name))
               (assoc (keyword target-name) data))
           (concat rest-que to-rename)))
       result-template))))

(defn get-text-name
  [template]
  (->> template
       (filter (fn [[_ {:keys [type]}]] (= type "text")))
       (map first)
       (first)
       (clojure.core/name)))
