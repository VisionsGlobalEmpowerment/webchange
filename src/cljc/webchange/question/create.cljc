(ns webchange.question.create
  (:require
    [webchange.question.create-multiple-choice-image :as multiple-choice-image]
    [webchange.question.create-multiple-choice-text :as multiple-choice-text]
    [webchange.question.create-thumbs-up-n-down :as thumbs-up-n-down]
    [webchange.question.get-question-data :refer [param-name->object-name]]
    [webchange.question.utils :refer [get-voice-over-tag merge-data]]))

(def question-types {"multiple-choice-image" multiple-choice-image/create
                     "multiple-choice-text"  multiple-choice-text/create
                     "thumbs-up-n-down"      thumbs-up-n-down/create})

(defn- create-voice-over-handlers
  [{:keys [options-number] :as form-data}
   {:keys [action-name question-id]}]
  (let [{activate-tag-template   :activate-template
         inactivate-tag-template :inactivate-template} (get-voice-over-tag {:question-id question-id})]
    (->> (range 1 (inc options-number))
         (map (fn [option-number]
                (let [option-text-name (-> (str "option-" option-number "-text")
                                           (keyword))
                      option-text-data (get form-data option-text-name)]
                  {:text  (:text option-text-data)
                   :value (str "option-" option-number)})))
         (reduce (fn [result {:keys [text value]}]
                   (let [option-dialog-name (str action-name "-voice-over-" value)]
                     (-> result
                         (assoc-in [:actions (keyword option-dialog-name)] {:type               "sequence-data",
                                                                            :data               [{:type "sequence-data"
                                                                                                  :data [{:type "empty" :duration 0}
                                                                                                         {:type        "animation-sequence"
                                                                                                          :phrase-text text
                                                                                                          :audio       nil}]}]
                                                                            :phrase-description (str "Option \"" text "\" voice-over")
                                                                            :editor-type        "dialog"})
                         (assoc-in [:actions (keyword action-name) :data 1 :options (keyword value)] {:type "action" :id option-dialog-name})
                         (update-in [:track :nodes] conj {:type      "dialog"
                                                          :action-id (keyword option-dialog-name)}))))
                 {:actions {(keyword action-name) {:type "sequence-data"
                                                   :data [{:type        "parallel-by-tag"
                                                           :from-params [{:template        activate-tag-template
                                                                          :action-property "tag"
                                                                          :param-property  "value"}]}
                                                          {:type        "case"
                                                           :from-params [{:action-property "value" :param-property "value"}]
                                                           :options     {}}
                                                          {:type        "parallel-by-tag"
                                                           :from-params [{:template        inactivate-tag-template
                                                                          :action-property "tag"
                                                                          :param-property  "value"}]}]}}
                  :track   {:nodes []}}))))

(defn- add-check-correct-answer
  [{:keys [action-name correct-answers hide-question-name question-id]}]
  (let [on-correct (str action-name "-correct-answer")
        on-correct-dialog (str action-name "-correct-answer-dialog")
        on-wrong (str action-name "-wrong-answer")
        on-wrong-dialog (str action-name "-wrong-answer-dialog")]
    {:actions {(keyword action-name)       {:type "sequence-data"
                                            :data [{:type    "question-check"
                                                    :id      question-id
                                                    :answer  correct-answers
                                                    :success on-correct
                                                    :fail    on-wrong}
                                                   {:type "question-reset"
                                                    :id   question-id}
                                                   {:type "parallel-by-tag"
                                                    :tag  (str "inactivate-options-" question-id)}]}

               (keyword on-correct)        {:type "sequence-data"
                                            :data [{:type "action" :id on-correct-dialog}
                                                   {:type "action" :id hide-question-name}
                                                   {:type "finish-flows" :tag question-id}]}

               (keyword on-correct-dialog) {:type               "sequence-data"
                                            :data               [{:type "sequence-data"
                                                                  :data [{:type "empty" :duration 0}
                                                                         {:type        "animation-sequence"
                                                                          :phrase-text ""
                                                                          :audio       nil}]}]
                                            :phrase-description "Correct answer"
                                            :editor-type        "dialog"}

               (keyword on-wrong)          {:type "sequence-data",
                                            :data [{:type "empty" :duration 500}
                                                   {:type "action" :id on-wrong-dialog}]}

               (keyword on-wrong-dialog)   {:type               "sequence-data"
                                            :data               [{:type "sequence-data"
                                                                  :data [{:type "empty" :duration 0}
                                                                         {:type        "animation-sequence"
                                                                          :phrase-text ""
                                                                          :audio       nil}]}]
                                            :phrase-description "Wrong answer"
                                            :editor-type        "dialog"}}
     :track   {:nodes [{:type      "dialog"
                        :action-id (keyword on-correct-dialog)}
                       {:type      "dialog"
                        :action-id (keyword on-wrong-dialog)}]}}))

(defn- add-finish-dialog
  [{:keys [action-name hide-question-name question-id]}]
  (let [dialog-name (str action-name "-dialog")]
    {:actions {(keyword action-name) {:type "sequence-data"
                                      :data [{:type "empty" :duration 500}
                                             {:type "action" :id dialog-name}
                                             {:type "action" :id hide-question-name}
                                             {:type "finish-flows" :tag question-id}]}
               (keyword dialog-name) {:type               "sequence-data"
                                      :data               [{:type "sequence-data"
                                                            :data [{:type "empty" :duration 0}
                                                                   {:type        "animation-sequence"
                                                                    :phrase-text ""
                                                                    :audio       nil}]}]
                                      :phrase-description "Finish dialog"
                                      :editor-type        "dialog"}}
     :track   {:nodes [{:type      "dialog"
                        :action-id (keyword dialog-name)}]}}))

(defn create
  ([question-params activity-params]
   (create question-params activity-params {}))
  ([{:keys [alias answers-number correct-answers task-text question-type] :as form-data}
    {:keys [action-name object-name index]}
    {:keys [visible?] :or {visible? false}}]
   (let [show-question-name (str action-name "-show")
         hide-question-name (str action-name "-hide")

         question-id action-name
         check-answers (str action-name "-check-answers")
         finish-dialog (str action-name "-finish-dialog")

         task-voice-over-click (str action-name "-task-voice-over-click")
         task-dialog-name (str action-name "-task-dialog")
         {activate-tag-task   :activate
          inactivate-tag-task :inactivate} (get-voice-over-tag {:question-id question-id})

         option-voice-over-name (str action-name "-option-voice-over")
         option-click-name (str action-name "-option-click-handler")

         options-have-voice-over? (not= question-type "thumbs-up-n-down")
         has-correct-answer? (not= answers-number "any")
         create-question (get question-types question-type)
         question-params {:task-type-param-name  "task-type"
                          :task-image-param-name "task-image"}

         question-data (merge {:alias       alias
                               :action-name action-name
                               :object-name object-name
                               :params      form-data
                               :index       index}
                              (cond-> {:actions {(keyword action-name)           {:type                "sequence-data"
                                                                                  :description         "-- Description --"
                                                                                  :workflow-user-input true
                                                                                  :tags                [question-id]
                                                                                  :data                [{:type "action" :id show-question-name}
                                                                                                        {:type "action" :id task-dialog-name}]}

                                                 (keyword show-question-name)    {:type       "set-attribute"
                                                                                  :target     object-name
                                                                                  :attr-name  "visible"
                                                                                  :attr-value true}
                                                 (keyword hide-question-name)    {:type       "set-attribute"
                                                                                  :target     object-name
                                                                                  :attr-name  "visible"
                                                                                  :attr-value false}

                                                 (keyword task-voice-over-click) {:type "sequence-data"
                                                                                  :data [{:type "parallel-by-tag"
                                                                                          :tag  activate-tag-task}
                                                                                         {:type "action"
                                                                                          :id   task-dialog-name}
                                                                                         {:type "parallel-by-tag"
                                                                                          :tag  inactivate-tag-task}]}

                                                 (keyword task-dialog-name)      {:type               "sequence-data",
                                                                                  :tags               ["question-action"]
                                                                                  :data               [{:type "sequence-data"
                                                                                                        :data [{:type "empty" :duration 0}
                                                                                                               {:type        "text-animation"
                                                                                                                :phrase-text (:text task-text)
                                                                                                                :target      (param-name->object-name "task-text" question-id)
                                                                                                                :audio       nil
                                                                                                                :animation   "color"
                                                                                                                :fill        45823
                                                                                                                :data        []}]}]
                                                                                  :phrase-description "Question text"
                                                                                  :editor-type        "dialog"}

                                                 (keyword option-click-name)     {:type "sequence-data"
                                                                                  :data (cond-> [{:type        "question-pick"
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
                                                                                                                               :param-property  "value"}]}}]
                                                                                                has-correct-answer? (conj {:type    "test-value"
                                                                                                                           :value1  answers-number
                                                                                                                           :value2  "one"
                                                                                                                           :success check-answers})
                                                                                                (not has-correct-answer?) (conj {:type "action" :id finish-dialog}))}}
                                       :track   {:title alias
                                                 :nodes [{:type      "dialog"
                                                          :action-id (keyword task-dialog-name)}]}
                                       :objects {}
                                       :assets  []}
                                      has-correct-answer? (merge-data (add-check-correct-answer {:action-name        check-answers
                                                                                                 :correct-answers    correct-answers
                                                                                                 :hide-question-name hide-question-name
                                                                                                 :question-id        question-id}))
                                      (not has-correct-answer?) (merge-data (add-finish-dialog {:action-name        finish-dialog
                                                                                                :hide-question-name hide-question-name
                                                                                                :question-id        question-id}))
                                      options-have-voice-over? (merge-data (create-voice-over-handlers form-data
                                                                                                       {:action-name option-voice-over-name
                                                                                                        :question-id question-id}))
                                      :always (merge-data (create-question form-data
                                                                           (cond-> (merge {:question-id              question-id
                                                                                           :object-name              object-name
                                                                                           :on-option-click          option-click-name
                                                                                           :on-task-voice-over-click task-voice-over-click
                                                                                           :visible?                 visible?}
                                                                                          question-params)
                                                                                   options-have-voice-over? (assoc :on-option-voice-over-click option-voice-over-name)
                                                                                   has-correct-answer? (assoc :on-check-click check-answers))))))]
     (assoc-in question-data [:objects (keyword object-name) :metadata] {:question? true
                                                                         :assets    (map :url (:assets question-data))
                                                                         :actions   (->> (keys (:actions question-data)) (map clojure.core/name))
                                                                         :objects   (->> (keys (:objects question-data)) (map clojure.core/name))
                                                                         :params    form-data
                                                                         :index     index}))))

(defn add-to-scene
  [activity-data {:keys [alias action-name actions object-name objects assets track]}]
  (let [conj-vec (fn [list element] (conj (if (vector? list) list (vec list)) element))]
    (-> activity-data
        (update :actions merge actions)
        (update :objects merge objects)
        (update :assets concat assets)
        (update-in [:scene-objects] conj-vec [object-name])
        (update-in [:metadata :tracks] conj (merge track
                                                   {:question-id object-name}))
        (update-in [:metadata :available-actions] concat [{:action action-name
                                                           :type   "question"
                                                           :name   (str "Ask " alias)}]))))
