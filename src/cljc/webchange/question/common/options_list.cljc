(ns webchange.question.common.options-list
  (:require
    [webchange.question.common.check-button :as check-button]
    [webchange.question.common.option-item :as option-item]
    [webchange.question.common.params :as params]
    [webchange.question.utils :refer [merge-data]]))

(defn create
  [{:keys [object-name x y width height options frame on-check-click on-option-click on-option-voice-over-click question-id]}
   {:keys [answers-number question-type] :as form-data}]
  (let [{label-type :label} (:options form-data)
        show-check-button? (= answers-number "many")

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

    (->> (map-indexed vector options)
         (reduce (fn [result [idx {:keys [img text value]}]]
                   (let [option-name (str object-name "-option-" idx)]
                     (-> result
                         (update-in [:objects (keyword object-name) :children] conj option-name)
                         (merge-data (option-item/create {:object-name                option-name
                                                          :x                          (-> (get-in positions [idx :x])
                                                                                          (+ list-offset-x))
                                                          :y                          (-> (get-in positions [idx :y])
                                                                                          (+ list-offset-y))
                                                          :width                      option-width
                                                          :height                     option-height
                                                          :img                        img
                                                          :text                       text
                                                          :label-type                 label-type
                                                          :on-option-click            on-option-click
                                                          :on-option-voice-over-click on-option-voice-over-click
                                                          :value                      value
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
