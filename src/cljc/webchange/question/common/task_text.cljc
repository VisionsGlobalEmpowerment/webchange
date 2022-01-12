(ns webchange.question.common.task-text
  (:require
    [webchange.question.common.params :as common-params]
    [webchange.question.get-question-data :refer [param-name->object-name]]
    [webchange.question.utils :refer [merge-data]]))

(defn create
  [form-data layout {:keys [object-name question-id param-name]}]
  (let [{:keys [x y width height]} (:text layout)
        params (->> (keyword param-name)
                    (get form-data))
        text-object-name (param-name->object-name param-name question-id)]
    {:objects {(keyword object-name)      {:type     "group"
                                           :x        x
                                           :y        y
                                           :children [text-object-name]}
               (keyword text-object-name) (merge {:type           "text"
                                                  :x              0
                                                  :y              0
                                                  :width          width
                                                  :height         height
                                                  :word-wrap      true
                                                  :align          "center"
                                                  :vertical-align "middle"
                                                  :fill           0xFFFFFF
                                                  :font-size      common-params/font-size--task
                                                  :font-family    common-params/font-family
                                                  :editable?      {:select true}
                                                  :metadata       {:question-form-param param-name}}
                                                 params)}}))
