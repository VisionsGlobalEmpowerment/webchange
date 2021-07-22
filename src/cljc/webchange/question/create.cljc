(ns webchange.question.create
  (:require
    [webchange.question.create-multiple-choice-image :as multiple-choice-image]))

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
                                                                     :phrase-description (str "Option \"" (:text option) "\" voice-over")
                                                                     :editor-type        "dialog"})
                  (assoc-in [:actions (keyword action-name) :options (keyword (:value option))] {:type "action" :id option-dialog-name})
                  (update-in [:track] conj {:type      "dialog"
                                            :action-id (keyword option-dialog-name)}))))
          {:actions {(keyword action-name) {:type        "case",
                                            :from-params [{:action-property "value" :param-property "value"}]
                                            :options     {}}}
           :track   []}
          options))

(defn create
  ([question-params activity-params]
   (create question-params activity-params {}))
  ([{:keys [alias answers-number correct-answers options task] :as args}
    {:keys [action-name object-name next-action-name]}
    {:keys [visible?] :or {visible? false}}]
   (let [show-question-name (str action-name "-show")
         hide-question-name (str action-name "-hide")

         task-dialog-name (str action-name "-task-dialog")

         option-voice-over-name (str action-name "-option-voice-over")
         option-voice-over-data (create-voice-over-handlers {:action-name option-voice-over-name
                                                             :options     (:data options)})

         option-click-name (str action-name "-option-click-handler")

         question-id (str action-name "-question-id")
         check-answers (str action-name "-check-answers")

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
                                                         :phrase-description "Question text"
                                                         :editor-type        "dialog"}

                           (keyword option-click-name)  {:type "sequence-data"
                                                         :data [{:type        "question-pick"
                                                                 :id          question-id
                                                                 :from-params [{:action-property "value" :param-property "value"}]}
                                                                {:type    "test-value"
                                                                 :value1  answers-number
                                                                 :value2  "one"
                                                                 :success check-answers}]}

                           (keyword check-answers)      {:type "sequence-data"
                                                         :data [{:type    "question-check"
                                                                 :id      question-id
                                                                 :answer  correct-answers
                                                                 :success on-correct
                                                                 :fail    on-wrong}
                                                                {:type "question-reset"
                                                                 :id   question-id}]}

                           (keyword on-correct)         {:type "sequence-data"
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
                                                         :phrase-description "Correct answer"
                                                         :editor-type        "dialog"}

                           (keyword on-wrong)           {:type "sequence-data",
                                                         :data [{:type "empty" :duration 500}
                                                                {:type "action" :id on-wrong-dialog}]}

                           (keyword on-wrong-dialog)    {:type               "sequence-data"
                                                         :data               [{:type "sequence-data"
                                                                               :data [{:type "empty" :duration 0}
                                                                                      {:type        "animation-sequence"
                                                                                       :phrase-text ""
                                                                                       :audio       nil}]}]
                                                         :phrase-description "Wrong answer"
                                                         :editor-type        "dialog"}}
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
                                                  :on-task-voice-over-click   task-dialog-name
                                                  :visible?                   visible?}
                                                 args)})))

(defn add-to-scene
  [activity-data {:keys [action-name actions object-name objects track]}]
  (let [conj-vec (fn [list element] (conj (if (vector? list) list (vec list)) element))]
    (-> activity-data
        (update :actions merge actions)
        (update :objects merge objects)
        (update-in [:scene-objects] conj-vec [object-name])
        (update-in [:actions :script :data] conj {:type "action" :id action-name :workflow-user-input true})
        (update-in [:metadata :tracks] conj track))))
