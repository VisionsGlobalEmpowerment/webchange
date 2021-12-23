(ns webchange.question.common.task-text
  (:require
    [webchange.question.common.params :as common-params]
    [webchange.question.common.voice-over-button :as voice-over]
    [webchange.question.get-question-data :refer [param-name->object-name]]
    [webchange.question.utils :refer [merge-data]]))

(defn create
  [form-data {:keys [object-name x y width question-id on-task-voice-over-click param-name]}]
  (let [params (->> (keyword param-name)
                    (get form-data))

        text-object-name (param-name->object-name param-name question-id)
        button-name (str object-name "-button")]
    (merge-data {:objects {(keyword object-name)      {:type     "group"
                                                       :x        (+ x common-params/block-padding)
                                                       :y        (+ y common-params/block-padding)
                                                       :children [button-name text-object-name]}
                           (keyword text-object-name) (merge {:type           "text"
                                                              :x              (+ voice-over/default-size common-params/voice-over-button-margin)
                                                              :y              0
                                                              :width          (- width
                                                                                 voice-over/default-size
                                                                                 common-params/voice-over-button-margin
                                                                                 (* 2 common-params/block-padding))
                                                              :word-wrap      true
                                                              :vertical-align "top"
                                                              :editable?      {:select true}}
                                                             params)}}
                (voice-over/create {:object-name button-name
                                    :x           0
                                    :y           0
                                    :on-click    on-task-voice-over-click
                                    :question-id question-id}))))
