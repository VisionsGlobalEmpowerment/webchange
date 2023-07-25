(ns webchange.utils.get-phrases-sequence
  (:require
    [webchange.utils.list :refer [update-first->> update-last->>]]))

(defn- call-concept-action?
  [action-data]
  (= (:type action-data)
     "action"))

(defn- get-concept-action-data
  [field-name concept-data]
  (->> (concat [:data field-name])
       (get-in concept-data)))

(defn- get-concept-field-name
  [scene-phrase-data]
  (-> (:from-var scene-phrase-data)
      (first)
      (:var-property)
      (keyword)))

(defn- action-data->phrases-sequence
  [{:keys [action-path action-data scene-data concept-data]}]
  (->> (get action-data :data)
       (map-indexed (fn [idx {:keys [type] :as phrase-action-data}]
                      (let [phrase-action-path (concat action-path [:data idx])]
                        (cond
                          (= type "sequence-data")
                          {:scene-action-path phrase-action-path
                           :parallel-mark     :none}

                          (= type "parallel")
                          (->> (action-data->phrases-sequence {:action-path  phrase-action-path
                                                               :action-data  phrase-action-data
                                                               :scene-data   scene-data
                                                               :concept-data concept-data})

                               (map #(assoc % :parallel-mark :middle))
                               (vec)
                               (update-first->> assoc :parallel-mark :start)
                               (update-last->> assoc :parallel-mark :end))

                          (call-concept-action? phrase-action-data)
                          (let [concept-action-name (get-concept-field-name phrase-action-data)
                                concept-action-data (get-concept-action-data concept-action-name concept-data)]
                            (->> (action-data->phrases-sequence {:action-path  [concept-action-name]
                                                                 :action-data  concept-action-data
                                                                 :scene-data   scene-data
                                                                 :concept-data concept-data})
                                 (map (fn [{:keys [scene-action-path] :as phrase-data}]
                                        (-> phrase-data
                                            (assoc :scene-action-path phrase-action-path)
                                            (assoc :concept-action-path scene-action-path))))))

                          :else
                          {:scene-action-path phrase-action-path
                           :parallel-mark     :none}))))
       (flatten)
       (vec)))

(defn get-phrases-sequence
  [{:keys [action-path scene-data concept-data]}]
  (let [action-data (->> (concat [:actions] action-path)
                         (get-in scene-data))]
    (action-data->phrases-sequence {:action-path  action-path
                                    :action-data  action-data
                                    :scene-data   scene-data
                                    :concept-data concept-data})))
