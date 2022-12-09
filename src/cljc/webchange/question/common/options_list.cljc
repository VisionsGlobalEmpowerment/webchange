(ns webchange.question.common.options-list
  (:require
    [webchange.question.common.option-item :as option-item]
    [webchange.question.utils :refer [merge-data round]]))

(defn- create-placeholder
  [object-name {:keys [x y width height]}]
  {:type "rectangle"
   :transition object-name
   :x x
   :y y
   :width width
   :height height
   :border-radius 20
   :border-color 0xFFFFFF
   :border-width 2
   :fill 0xF1F1F1})

(defn- create-placeholder-params
  [idx {:keys [transition x y width height]}]
  {:id transition
   :position idx
   :offset {:x (-> width (* 0.75) (+ 4) (+ x)) ;;some issue with pivot
            :y (-> height (/ 2) (round) (+ y))}})

(defn- create-placeholders
  [items object-name]
  (->> items
       (map (fn [[idx placeholder]]
              (let [placeholder-name (str object-name "-placeholder-" idx)]
                (create-placeholder placeholder-name placeholder))))))

(defn create
  [options
   {:keys [correct-answers question-type]}
   {:keys [object-name question-id]}
   {:keys [options-container options-items placeholder-items]}
   data-names
   creation-options]
  (let [add-placeholders? (= question-type "arrange-images")
        placeholders (create-placeholders placeholder-items object-name)
        created-options (->> options
                             (map-indexed (fn [idx option] [(inc idx) option]))
                             (map (fn [[idx {:keys [value] :as option}]]
                                    (let [option-name (str object-name "-option-" idx)]
                                      [option-name (option-item/create option
                                                                       (merge {:idx             idx
                                                                               :object-name     option-name
                                                                               :question-id     question-id
                                                                               :question-type   question-type
                                                                               :correct-option? (some #{value} correct-answers)
                                                                               :placeholders    (when add-placeholders? (map-indexed create-placeholder-params placeholders))}
                                                                              (get options-items idx))
                                                                       data-names
                                                                       creation-options)]))))
        children (concat (when add-placeholders? (map :transition placeholders)) (map first created-options))
        options-data (apply merge-data (map second created-options))]
    (merge-data {:objects {(keyword object-name) {:type     "group"
                                                  :x        (:x options-container)
                                                  :y        (:y options-container)
                                                  :children children}}}
                options-data
                (when add-placeholders? {:objects (->> placeholders
                                                       (map (fn [p] [(-> p :transition keyword) p]))
                                                       (into {}))}))))

(comment
  (concat [1 2]))
