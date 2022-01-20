(ns webchange.question.common.options-list
  (:require
    [webchange.question.common.option-item :as option-item]
    [webchange.question.utils :refer [merge-data]]))

(defn create
  [options
   {:keys [correct-answers question-type]}
   {:keys [object-name question-id]}
   {:keys [options-container options-items]}
   data-names
   creation-options]
  (->> options
       (map-indexed (fn [idx option] [(inc idx) option]))
       (reduce (fn [result [idx {:keys [value] :as option}]]
                 (let [option-name (str object-name "-option-" idx)]
                   (-> result
                       (update-in [:objects (keyword object-name) :children] conj option-name)
                       (merge-data (option-item/create option
                                                       (merge {:idx             idx
                                                               :object-name     option-name
                                                               :question-id     question-id
                                                               :question-type   question-type
                                                               :correct-option? (some #{value} correct-answers)}
                                                              (get options-items idx))
                                                       data-names
                                                       creation-options)))))
               {:objects {(keyword object-name) {:type     "group"
                                                 :x        (:x options-container)
                                                 :y        (:y options-container)
                                                 :children []}}})))
