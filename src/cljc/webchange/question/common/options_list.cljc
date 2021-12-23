(ns webchange.question.common.options-list
  (:require
    [webchange.question.common.check-button :as check-button]
    [webchange.question.common.option-item :as option-item]
    [webchange.question.common.params :as params]
    [webchange.question.utils :refer [merge-data]]))

(defn create
  [options
   {:keys [answers-number question-type] :as form-data}
   {:keys [object-name x y width height frame on-check-click on-option-click on-option-voice-over-click question-id]}]
  (let [show-check-button? (= answers-number "many")

        check-button-name (str object-name "-check-button")
        {check-button-size :size} params/check-button
        check-panel-height (+ check-button-size params/block-padding)
        check-button-x (- (/ width 2) (/ check-button-size 2))
        check-button-y (+ (- height check-panel-height) (/ params/block-padding 2))

        {:keys [list-width
                list-height
                option-width
                option-height
                positions]} frame

        list-placeholder-width width
        list-placeholder-height (if show-check-button?
                                  (- height check-panel-height)
                                  height)
        list-offset-x (-> (- list-placeholder-width list-width) (/ 2))
        list-offset-y (-> (- list-placeholder-height list-height) (/ 2))]
    (->> options
         (map-indexed (fn [idx option] [(inc idx) option]))
         (reduce (fn [result [idx option]]
                   (let [option-name (str object-name "-option-" idx)]
                     (-> result
                         (update-in [:objects (keyword object-name) :children] conj option-name)
                         (merge-data (option-item/create option
                                                         form-data
                                                         {:idx                        idx
                                                          :object-name                option-name
                                                          :x                          (-> (get-in positions [(dec idx) :x])
                                                                                          (+ list-offset-x))
                                                          :y                          (-> (get-in positions [(dec idx) :y])
                                                                                          (+ list-offset-y))
                                                          :width                      option-width
                                                          :height                     option-height
                                                          :on-option-click            on-option-click
                                                          :on-option-voice-over-click on-option-voice-over-click
                                                          :value                      (str "option-" idx)
                                                          :question-id                question-id
                                                          :question-type              question-type})))))
                 (cond-> {:objects {(keyword object-name) {:type     "group"
                                                           :x        x
                                                           :y        y
                                                           :children (cond-> []
                                                                             show-check-button? (conj check-button-name))}}}
                         show-check-button? (merge-data (check-button/create {:object-name check-button-name
                                                                              :x           check-button-x
                                                                              :y           check-button-y
                                                                              :on-click    on-check-click})))))))
