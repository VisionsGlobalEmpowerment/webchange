(ns webchange.editor-v2.diagram.scene-data-parser.actions-parser)

(defn get-action-outs
  [action-data]
  (let [type (:type action-data)]
    (case type
      "audio" {}
      "sequence" (->> (:data action-data)
                      (map-indexed (fn [index item]
                                     [(str index) [(keyword item)] ]))
                      (into (sorted-map)))
      "test-var-scalar" {:success [(keyword (:success action-data))]
                         :fail    [(keyword (:fail action-data))]}
      (do (.warn js/console (str "Unhandled action type: " type))
          {}))))

(defn parse-actions
  [scene-data]
  (->> (:actions scene-data)
       (seq)
       (reduce
         (fn [result [action-name action-data]]
           (assoc result action-name {:name   (name action-name)
                                      :entity :action
                                      :type   (:type action-data)
                                      :outs   (get-action-outs action-data)}))
         {})))
