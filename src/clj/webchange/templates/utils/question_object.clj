(ns webchange.templates.utils.question-object
  (:require
    [clojure.tools.logging :as log]
    [webchange.templates.utils.question-object.multiple-choice-image :as multiple-choice-image]))

(defn- create-voice-over-handlers
  [{:keys [action-name options]}]
  (reduce (fn [result option]
            (let [option-dialog-name (str action-name "-voice-over-" (:value option))]
              (-> result
                  (assoc-in [:actions (keyword option-dialog-name)] {:type               "sequence-data",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type        "animation-sequence"
                                                                                                   :phrase-text (:text option)
                                                                                                   :audio       nil}]}]
                                                                     :phrase-description (str "Option \"" (:text option) "\" voice-over")})
                  (assoc-in [:actions (keyword action-name) :options (keyword (:value option))] {:type "action" :id option-dialog-name})
                  (update-in [:track] conj {:type      "dialog"
                                            :action-id (keyword option-dialog-name)}))))
          {:actions {(keyword action-name) {:type        "case",
                                            :from-params [{:action-property "value" :param-property "value"}]
                                            :options     {}}}
           :track   []}
          options))

(defn create
  [{:keys [alias options task] :as args}
   {:keys [action-name object-name next-action-name]}]
  (let [show-question-name (str action-name "-show")
        hide-question-name (str action-name "-hide")

        task-dialog-name (str action-name "-task-dialog")

        option-voice-over-name (str action-name "-option-voice-over")
        option-voice-over-data (create-voice-over-handlers {:action-name option-voice-over-name
                                                            :options     (:data options)})
        correct-value (->> (:data options)
                           (some (fn [{:keys [correct value]}]
                                   (and correct value))))
        option-click-name (str action-name "-option-click-handler")

        on-correct (str action-name "-correct-answer")
        on-correct-dialog (str action-name "-correct-answer-dialog")
        on-wrong (str action-name "-wrong-answer")
        on-wrong-dialog (str action-name "-wrong-answer-dialog")]
    {:action-name action-name
     :object-name object-name
     :actions     (merge {(keyword action-name)        {:type          "sequence-data"
                                                        :description   "-- Description --"
                                                        :continue-flow (nil? next-action-name)
                                                        :data          [{:type "action" :id show-question-name}
                                                                        {:type "action" :id task-dialog-name}]}

                          (keyword show-question-name) {:type "state" :target object-name :id "visible"}
                          (keyword hide-question-name) {:type "state" :target object-name :id "invisible"}

                          (keyword task-dialog-name)   {:type               "sequence-data",
                                                        :data               [{:type "sequence-data"
                                                                              :data [{:type "empty" :duration 0}
                                                                                     {:type        "animation-sequence"
                                                                                      :phrase-text (:text task)
                                                                                      :audio       nil}]}]
                                                        :phrase-description "Question text"}

                          (keyword option-click-name)  {:type        "test-value"
                                                        :value1      correct-value
                                                        :from-params [{:action-property "value2" :param-property "value"}]
                                                        :success     on-correct
                                                        :fail        on-wrong}

                          (keyword on-correct)         {:type "sequence-data",
                                                        :data [{:type "empty" :duration 500}
                                                               {:type "action" :id on-correct-dialog}
                                                               {:type "action" :id hide-question-name}
                                                               {:type "empty" :duration 2000}
                                                               (if next-action-name
                                                                 {:type "action" :id next-action-name}
                                                                 {:type "empty" :duration 100})]}

                          (keyword on-correct-dialog)  {:type               "sequence-data"
                                                        :data               [{:type "sequence-data"
                                                                              :data [{:type "empty" :duration 0}
                                                                                     {:type        "animation-sequence"
                                                                                      :phrase-text ""
                                                                                      :audio       nil}]}]
                                                        :phrase-description "Correct answer"}

                          (keyword on-wrong)           {:type "sequence-data",
                                                        :data [{:type "empty" :duration 500}
                                                               {:type "action" :id on-wrong-dialog}]}

                          (keyword on-wrong-dialog)    {:type               "sequence-data"
                                                        :data               [{:type "sequence-data"
                                                                              :data [{:type "empty" :duration 0}
                                                                                     {:type        "animation-sequence"
                                                                                      :phrase-text ""
                                                                                      :audio       nil}]}]
                                                        :phrase-description "Wrong answer"}}
                         (:actions option-voice-over-data))
     :track       {:title alias
                   :nodes (concat [{:type      "dialog"
                                    :action-id (keyword task-dialog-name)}
                                   {:type      "dialog"
                                    :action-id (keyword on-correct-dialog)}
                                   {:type      "dialog"
                                    :action-id (keyword on-wrong-dialog)}]
                                  (:track option-voice-over-data))}
     :objects     (multiple-choice-image/create {:object-name                object-name
                                                 :on-option-click            option-click-name
                                                 :on-option-voice-over-click option-voice-over-name
                                                 :on-task-voice-over-click   task-dialog-name}
                                                args)}))
