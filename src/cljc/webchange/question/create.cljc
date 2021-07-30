(ns webchange.question.create
  (:require
    [webchange.question.create-multiple-choice-image :as multiple-choice-image]
    [webchange.question.create-multiple-choice-text :as multiple-choice-text]
    [webchange.question.utils :refer [merge-data]]))

(def question-types {"multiple-choice-image" multiple-choice-image/create
                     "multiple-choice-text"  multiple-choice-text/create})

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
                  (update-in [:track :nodes] conj {:type      "dialog"
                                                   :action-id (keyword option-dialog-name)}))))
          {:actions {(keyword action-name) {:type        "case",
                                            :from-params [{:action-property "value" :param-property "value"}]
                                            :options     {}}}
           :track   {:nodes []}}
          options))

(defn create
  ([question-params activity-params]
   (create question-params activity-params {}))
  ([{:keys [alias answers-number correct-answers options task question-type] :as args}
    {:keys [action-name object-name]}
    {:keys [visible?] :or {visible? false}}]
   (let [show-question-name (str action-name "-show")
         hide-question-name (str action-name "-hide")

         task-dialog-name (str action-name "-task-dialog")

         option-voice-over-name (str action-name "-option-voice-over")
         option-click-name (str action-name "-option-click-handler")

         question-id (str action-name "-question-id")
         check-answers (str action-name "-check-answers")

         on-correct (str action-name "-correct-answer")
         on-correct-dialog (str action-name "-correct-answer-dialog")
         on-wrong (str action-name "-wrong-answer")
         on-wrong-dialog (str action-name "-wrong-answer-dialog")

         create-question (get question-types question-type)]
     (merge {:alias       alias
             :action-name action-name
             :object-name object-name}
            (merge-data {:actions {(keyword action-name)        {:type                "sequence-data"
                                                                 :description         "-- Description --"
                                                                 :workflow-user-input true
                                                                 :tags                [question-id]
                                                                 :data                [{:type "action" :id show-question-name}
                                                                                       {:type "action" :id task-dialog-name}]}

                                   (keyword show-question-name) {:type       "set-attribute"
                                                                 :target     object-name
                                                                 :attr-name  "visible"
                                                                 :attr-value true}
                                   (keyword hide-question-name) {:type       "set-attribute"
                                                                 :target     object-name
                                                                 :attr-name  "visible"
                                                                 :attr-value false}

                                   (keyword task-dialog-name)   {:type               "sequence-data",
                                                                 :tags ["question-action"]
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
                                                                        {:type        "question-test"
                                                                         :id          question-id
                                                                         :from-params [{:action-property "value"
                                                                                        :param-property  "value"}]
                                                                         :success     {:type        "parallel-by-tag"
                                                                                       :from-params [{:template        (str "activate-option-%-" question-id)
                                                                                                      :action-property "tag"
                                                                                                      :param-property  "value"}]}
                                                                         :fail        {:type        "parallel-by-tag"
                                                                                       :from-params [{:template        (str "inactivate-option-%-" question-id)
                                                                                                      :action-property "tag"
                                                                                                      :param-property  "value"}]}}

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
                                                                         :id   question-id}
                                                                        {:type "parallel-by-tag"
                                                                         :tag  (str "inactivate-options-" question-id)}]}

                                   (keyword on-correct)         {:type "sequence-data"
                                                                 :data [{:type "action" :id on-correct-dialog}
                                                                        {:type "action" :id hide-question-name}
                                                                        {:type "finish-flows" :tag question-id}]}

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
                         :track   {:title alias
                                   :nodes [{:type      "dialog"
                                            :action-id (keyword task-dialog-name)}
                                           {:type      "dialog"
                                            :action-id (keyword on-correct-dialog)}
                                           {:type      "dialog"
                                            :action-id (keyword on-wrong-dialog)}]}
                         :objects {}}
                        (create-voice-over-handlers {:action-name option-voice-over-name
                                                     :options     (:data options)})
                        (create-question {:question-id                question-id
                                          :object-name                object-name
                                          :on-check-click             check-answers
                                          :on-option-click            option-click-name
                                          :on-option-voice-over-click option-voice-over-name
                                          :on-task-voice-over-click   task-dialog-name
                                          :visible?                   visible?}
                                         args))))))

(defn add-to-scene
  [activity-data {:keys [alias action-name actions object-name objects track]}]
  (let [conj-vec (fn [list element] (conj (if (vector? list) list (vec list)) element))]
    (-> activity-data
        (update :actions merge actions)
        (update :objects merge objects)
        (update-in [:scene-objects] conj-vec [object-name])
        (update-in [:metadata :tracks] conj track)
        (update-in [:metadata :available-actions] concat [{:action action-name
                                                           :name   (str "Ask " alias)}]))))
