(ns webchange.utils.scene-data)

(defn- gen-uuid []
  #?(:clj  (java.util.UUID/randomUUID)
     :cljs (random-uuid)))

(defn generate-name
  ([]
   (generate-name nil))
  ([parent-name]
   (let [uid (-> (gen-uuid)
                 (.toString)
                 (subs 0 8))]
     (if (some? parent-name)
       (str parent-name "-" uid)
       uid))))

(defn- process-object-data
  [object-data target-name]
  (if (= (:type object-data) "group")
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

;; Scene Data

(defn- get-scene-objects
  [scene-data]
  (get scene-data :objects {}))

(defn get-scene-background
  [scene-data]
  (->> (get-scene-objects scene-data)
       (some (fn [[object-name {:keys [type] :as object-data}]]
               (and (some #{type} ["background" "layered-background"])
                    [object-name object-data])))))
