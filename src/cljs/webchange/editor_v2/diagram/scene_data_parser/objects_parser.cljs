(ns webchange.editor-v2.diagram.scene-data-parser.objects-parser)

(defn get-object-outs
  [object-data]
  (->> object-data
       (:actions)
       (seq)
       (reduce
         (fn [result [_ action-data]]
           (let [event (keyword (:on action-data))
                 handler (case (:type action-data)
                           "action" (->> action-data :id keyword)
                           nil)]
             (if-not (nil? handler)
               (assoc result event [handler])
               (do (.warn js/console (str "Unhandled object event handler: " (:type action-data)))
                   result))
             ))
         {})))

(defn parse-objects
  [scene-data]
  (->> (:objects scene-data)
       (seq)
       (reduce
         (fn [result [object-name object-data]]
           (assoc result object-name {:name   (name object-name)
                                      :entity :object
                                      :outs   (get-object-outs object-data)}))
         {})))