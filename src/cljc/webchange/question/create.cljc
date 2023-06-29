(ns webchange.question.create
  (:require
    [webchange.question.create-multiple-choice-image :as multiple-choice-image]
    [webchange.question.create-multiple-choice-text :as multiple-choice-text]
    [webchange.question.create-arrange-images :as arrange-images]
    [webchange.question.create-thumbs-up-n-down :as thumbs-up-n-down]
    [webchange.question.get-question-data :refer [param-name->object-name]]
    [webchange.question.utils :refer [get-voice-over-tag merge-data] :as utils]))

(def question-types {"multiple-choice-image" multiple-choice-image/create
                     "multiple-choice-text"  multiple-choice-text/create
                     "arrange-images"        arrange-images/create
                     "thumbs-up-n-down"      thumbs-up-n-down/create})

(defn- option-number->option
  [option-number {:keys [question-type] :as form-data}]
  (if (= question-type "thumbs-up-n-down")
    (let [option-value (-> (get form-data :mark-options)
                           (get option-number))
          option-text-name (-> option-value
                               (str "-text")
                               (keyword))
          option-text-data (get form-data option-text-name)]
      {:idx   (inc option-number)
       :text  (:text option-text-data)
       :value option-value})
    (let [option-number (inc option-number)
          option-text-name (-> (str "option-" option-number "-text")
                               (keyword))
          option-text-data (get form-data option-text-name)]
      {:idx   option-number
       :text  (:text option-text-data)
       :value (str "option-" option-number)})))

(defn- create-voice-over-handlers
  [{:keys [options-number] :as form-data}
   {:keys [action-name text-animation? text-objects-names question-id]}]
  (let [{activate-tag-template   :activate-template
         inactivate-tag-template :inactivate-template} (get-voice-over-tag {:question-id question-id})]
    (->> (range 0 options-number)
         (map #(option-number->option % form-data))
         (reduce (fn [result {:keys [idx text value]}]
                   (let [option-dialog-name (str action-name "-voice-over-" value)
                         option-correct-dialog-name (str action-name "-correct-" value)
                         with-on-correct (fn [result]
                                           (if (utils/has-correct-answer? form-data)
                                             result
                                             (-> result
                                                 (assoc-in [:actions (keyword option-correct-dialog-name)] {:type               "sequence-data",
                                                                                                            :data               [{:type "sequence-data"
                                                                                                                                  :data [{:type "empty" :duration 0}
                                                                                                                                         {:phrase-text text
                                                                                                                                          :audio       nil
                                                                                                                                          :type "animation-sequence"}]}]
                                                                                                            :phrase-description (str "Response to \"" text "\"")
                                                                                                            :editor-type        "dialog"})
                                                 (update-in [:track :nodes] conj {:type      "dialog"
                                                                                  :action-id option-correct-dialog-name}))))]
                     (-> result
                         (assoc-in [:actions (keyword option-dialog-name)] {:type               "sequence-data",
                                                                            :data               [{:type "sequence-data"
                                                                                                  :data [{:type "empty" :duration 0}
                                                                                                         (merge {:phrase-text text
                                                                                                                 :audio       nil}
                                                                                                                (if text-animation?
                                                                                                                  {:type      "text-animation"
                                                                                                                   :target    (get text-objects-names idx)
                                                                                                                   :animation "color"
                                                                                                                   :fill      45823
                                                                                                                   :data      []}
                                                                                                                  {:type "animation-sequence"
                                                                                                                   :phrase-placeholder "Enter phrase text"}))]}]
                                                                            :phrase-description (str "Option \"" text "\" voice-over")
                                                                            :editor-type        "dialog"})
                         (assoc-in [:actions (keyword action-name) :data 1 :options (keyword value)] {:type "action" :id option-dialog-name})
                         (update-in [:track :nodes] conj {:type      "dialog"
                                                          :action-id (keyword option-dialog-name)})
                         (with-on-correct))))
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
  [{:keys [action-name correct-answers hide-question-name question-id option-voice-over-name]} form-data data-names]
  (let [submit (str action-name "-submit")
        on-correct (str action-name "-correct-answer")
        on-correct-dialog (str action-name "-correct-answer-dialog")
        on-correct-sound (str action-name "-correct-answer-sound")
        on-wrong (str action-name "-wrong-answer")
        on-wrong-dialog (str action-name "-wrong-answer-dialog")
        on-wrong-sound (str action-name "-wrong-answer-sound")
        try-again-dialog (str action-name "-tray-again-dialog")
        highlight-correct (str action-name "-highlight-correct")
        highlight-incorrect (str action-name "-highlight-incorrect")
        highlight-default (str action-name "-highlight-default")
        on-finish (str action-name "-finish-question")]
    {:actions {(keyword action-name)         {:type     "test-var-scalar"
                                              :var-name "check-button-enabled"
                                              :value    true
                                              :success  submit}
               (keyword submit)              {:type "sequence-data"
                                              :data (cond-> [{:type "action"
                                                              :id   (get-in data-names [:check-button :actions :set-submitted])}]

                                                            (-> form-data utils/has-correct-answer?)
                                                            (concat [{:type    "question-highlight"
                                                                      :id      question-id
                                                                      :answer  correct-answers
                                                                      :success highlight-correct
                                                                      :fail    highlight-incorrect}
                                                                     {:type    "question-check"
                                                                      :id      question-id
                                                                      :answer  correct-answers
                                                                      :finish  on-finish
                                                                      :success on-correct
                                                                      :fail    on-wrong}])

                                                            (-> form-data utils/has-correct-answer? not)
                                                            (concat [{:type "action"
                                                                      :from-expression [{:expression ["str" option-voice-over-name "-correct-" [:first (str "@" question-id)]]
                                                                                         :action-property "id"}]}
                                                                     {:type "action"
                                                                      :id   on-correct}])

                                                            (-> form-data utils/sequence-correct-answer? not)
                                                            (concat [{:type    "question-highlight"
                                                                      :id      question-id
                                                                      :answer  correct-answers
                                                                      :success highlight-correct
                                                                      :fail    highlight-default}
                                                                     {:type            "question-reset"
                                                                      :id              question-id
                                                                      :incorrect-only? true
                                                                      :answer          correct-answers}])

                                                            (-> form-data utils/sequence-correct-answer?)
                                                            (concat [{:type    "question-highlight"
                                                                      :id      question-id
                                                                      :answer  correct-answers
                                                                      :success highlight-default
                                                                      :fail    highlight-default}]))}

               (keyword on-correct)          {:type "sequence-data"
                                              :data [{:type "action" :id on-correct-sound}
                                                     {:type "action" :id on-correct-dialog}
                                                     {:type "action" :id on-finish}]}
               
               (keyword on-finish)           {:type "sequence-data"
                                              :data [{:type "action" :id hide-question-name}
                                                     {:type "finish-flows" :tag question-id}]}

               (keyword on-correct-dialog)   {:type               "sequence-data"
                                              :data               [{:type "sequence-data"
                                                                    :data [{:type "empty" :duration 0}
                                                                           {:type        "animation-sequence"
                                                                            :phrase-text ""
                                                                            :audio       nil}]}]
                                              :phrase-description "Correct answer"
                                              :editor-type        "dialog"}

               (keyword on-correct-sound)    {:type               "sequence-data"
                                              :data               [{:type "sequence-data"
                                                                    :data [{:type "empty" :duration 0}
                                                                           {:type        "animation-sequence"
                                                                            :phrase-text ""
                                                                            :audio       nil}]}]
                                              :phrase-description "Correct answer sound effect"
                                              :editor-type        "dialog"}

               (keyword on-wrong)            {:type "sequence-data",
                                              :data [{:type "action" :id on-wrong-sound}
                                                     {:type "action" :id (get-in data-names [:check-button :actions :set-default])}
                                                     {:type        "parallel-by-tag"
                                                      :from-params [{:template        (str "inactivate-option-%-" question-id)
                                                                     :action-property "tag"
                                                                     :param-property  "value"}]}
                                                     {:type "action" :id on-wrong-dialog}
                                                     {:type "action" :id try-again-dialog}]}

               (keyword highlight-correct)   {:type        "parallel-by-tag"
                                              :from-params [{:template        (utils/get-option-tag :set-correct {:question-id question-id :template? true})
                                                             :action-property "tag"
                                                             :param-property  "value"}]}
               (keyword highlight-incorrect) {:type        "parallel-by-tag"
                                              :from-params [{:template        (utils/get-option-tag :set-incorrect {:question-id question-id :template? true})
                                                             :action-property "tag"
                                                             :param-property  "value"}]}
               (keyword highlight-default)   {:type        "parallel-by-tag"
                                              :from-params [{:template        (utils/get-option-tag :set-unselected {:question-id question-id :template? true})
                                                             :action-property "tag"
                                                             :param-property  "value"}]}

               (keyword on-wrong-dialog)     {:type               "sequence-data"
                                              :data               [{:type "sequence-data"
                                                                    :data [{:type "empty" :duration 0}
                                                                           {:type        "animation-sequence"
                                                                            :phrase-text ""
                                                                            :audio       nil}]}]
                                              :phrase-description "Wrong answer"
                                              :editor-type        "dialog"}
               (keyword on-wrong-sound)      {:type               "sequence-data"
                                              :data               [{:type "sequence-data"
                                                                    :data [{:type "empty" :duration 0}
                                                                           {:type        "animation-sequence"
                                                                            :phrase-text ""
                                                                            :audio       nil}]}]
                                              :phrase-description "Wrong answer sound effect"
                                              :editor-type        "dialog"}
               (keyword try-again-dialog)    {:type               "sequence-data"
                                              :data               [{:type "sequence-data"
                                                                    :data [{:type "empty" :duration 0}
                                                                           {:type        "animation-sequence"
                                                                            :phrase-text ""
                                                                            :audio       nil}]}]
                                              :phrase-description "Try again"
                                              :editor-type        "dialog"}}
     :track   {:nodes [{:type      "dialog"
                        :action-id (keyword on-correct-sound)}
                       {:type      "dialog"
                        :action-id (keyword on-wrong-sound)}

                       {:type      "dialog"
                        :action-id (keyword on-correct-dialog)}
                       {:type      "dialog"
                        :action-id (keyword on-wrong-dialog)}

                       {:type      "dialog"
                        :action-id (keyword try-again-dialog)}]}}))

(defn- option-names->values
  [options]
  (->> options 
       (map (fn [[idx name]]
              [(str "option-" idx) name]))
       (into {})))

(defn- create-option-click-handler
  [question-id {:keys [correct-answers] :as form-data} data-names option-objects-names]
  (let [{:keys [click-handler drop-handler voice-over]} (get-in data-names [:options :actions])

        pick-one-option (str click-handler "--pick-one-option")
        pick-several-option (str click-handler "--pick-several-option")
        update-check-button (str click-handler "--update-check-button")
        set-check-button-ready (str click-handler "--set-check-button-ready")

        highlight-option (str click-handler "--highlight-option")
        unhighlight-option (str click-handler "--unhighlight-option")]
    {:actions {(keyword click-handler)          {:type "sequence-data"
                                                 :data (cond-> []
                                                               (utils/one-correct-answer? form-data) (conj {:type "action" :id pick-one-option})
                                                               (utils/many-correct-answers? form-data) (conj {:type "action" :id pick-several-option})
                                                               (utils/show-check-button? form-data) (conj {:type "action" :id update-check-button})
                                                               :always (conj {:type "action" :id voice-over}))}
               (keyword pick-one-option)        {:type "sequence-data"
                                                 :data [{:type "parallel-by-tag"
                                                         :tag  (str "inactivate-options-" question-id)}
                                                        {:type "question-reset"
                                                         :id   question-id}
                                                        {:type        "question-pick"
                                                         :id          question-id
                                                         :from-params [{:action-property "value" :param-property "value"}]
                                                         :on-check    highlight-option}]}
               (keyword pick-several-option)    {:type "sequence-data"
                                                 :data [{:type           "question-pick"
                                                         :id             question-id
                                                         :restrict-count (count correct-answers)
                                                         :from-params    [{:action-property "value" :param-property "value"}]
                                                         :on-check       highlight-option
                                                         :on-uncheck     unhighlight-option}]}
               (keyword drop-handler)            {:type "sequence-data"
                                                  :data [{:type "question-pick"
                                                          :id question-id
                                                          :from-expression [{:expression ["." "#placeholder" ":position"]
                                                                             :action-property "position"}
                                                                            {:expression "#value"
                                                                             :action-property "value"}]}
                                                         {:type "question-arrange"
                                                          :id question-id
                                                          :options (option-names->values option-objects-names)
                                                          :from-expression [{:expression "#placeholders" :action-property "placeholders"}]}
                                                         {:type "action" :id update-check-button}]}
               (keyword update-check-button)    {:type "sequence-data"
                                                 :data [{:type "set-variable" :var-name "check-button-enabled" :var-value false}
                                                        (if (utils/has-correct-answer? form-data)
                                                          {:type     "question-count-answers"
                                                           :id       question-id
                                                           :answer   correct-answers
                                                           :empty    (get-in data-names [:check-button :actions :set-default])
                                                           :partial  (get-in data-names [:check-button :actions :set-touched])
                                                           :full     set-check-button-ready
                                                           :overfull (get-in data-names [:check-button :actions :set-touched])}
                                                          {:type     "question-count-answers"
                                                           :id       question-id
                                                           :answer   correct-answers
                                                           :empty    (get-in data-names [:check-button :actions :set-default])
                                                           :partial  set-check-button-ready
                                                           :full     set-check-button-ready
                                                           :overfull set-check-button-ready})]}

               (keyword set-check-button-ready) {:type "sequence-data"
                                                 :data [{:type "set-variable" :var-name "check-button-enabled" :var-value true}
                                                        {:type "action" :id (get-in data-names [:check-button :actions :set-ready])}]}

               (keyword highlight-option)       {:type        "parallel-by-tag"
                                                 :from-params [{:template        (str "activate-option-%-" question-id)
                                                                :action-property "tag"
                                                                :param-property  "value"}]}
               (keyword unhighlight-option)     {:type        "parallel-by-tag"
                                                 :from-params [{:template        (str "inactivate-option-%-" question-id)
                                                                :action-property "tag"
                                                                :param-property  "value"}]}}}))

(defn create
  ([question-params activity-params]
   (create question-params activity-params {}))
  ([{:keys [alias correct-answers options-number task-text question-type] :as form-data}
    {:keys [action-name object-name index]}
    creation-options]
   (let [show-question-name (str action-name "-show")
         hide-question-name (str action-name "-hide")

         question-id action-name
         check-answers (str action-name "-check-answers")
         finish-dialog (str action-name "-finish-dialog")
         task-dialog-name (str action-name "-task-dialog")
         option-voice-over-name (str action-name "-option-voice-over")

         data-names {:check-button {:objects {:main (str object-name "-check-button")}
                                    :actions {:on-click      check-answers
                                              :set-default   (str object-name "-check-button-set-default")
                                              :set-touched   (str object-name "-check-button-set-touched")
                                              :set-ready     (str object-name "-check-button-set-ready")
                                              :set-submitted (str object-name "-check-button-set-submitted")}}
                     :options      {:actions {:click-handler (str action-name "-option-click-handler")
                                              :drop-handler  (str action-name "-option-drop-handler")
                                              :voice-over    option-voice-over-name}}
                     :dialogs      {:finish finish-dialog}}

         create-question (get question-types question-type)
         question-params {:task-type-param-name  "task-type"
                          :task-image-param-name "task-image"}

         text-objects-names (->> (range options-number)
                                 (map inc)
                                 (map (fn [option-idx]
                                        [option-idx (-> (str "options-option-" (dec option-idx) "-text")
                                                        (param-name->object-name question-id))]))
                                 (into {}))

         option-objects-names (->> (range options-number)
                                   (map inc)
                                   (map (fn [option-idx]
                                          [option-idx (-> (str "options-option-" option-idx)
                                                          (param-name->object-name question-id))]))
                                   (into {}))

         question-data (merge {:alias       alias
                               :action-name action-name
                               :object-name object-name
                               :params      form-data
                               :index       index}
                              (cond-> {:actions {(keyword action-name)        {:type                "sequence-data"
                                                                               :description         "-- Description --"
                                                                               :workflow-user-input true
                                                                               :tags                [question-id]
                                                                               :data                [{:type "action" :id show-question-name}
                                                                                                     {:type "action" :id task-dialog-name}]}

                                                 (keyword show-question-name) {:type "sequence-data"
                                                                               :data [{:type       "set-attribute"
                                                                                       :target     object-name
                                                                                       :attr-name  "visible"
                                                                                       :attr-value true}
                                                                                      {:type "show-guide"}
                                                                                      {:type     "set-variable" :var-name "tap-instructions-prev"
                                                                                       :from-var [{:var-name "tap-instructions-action", :action-property "var-value"}]}
                                                                                      {:type "set-variable" :var-name "tap-instructions-action" :var-value task-dialog-name}
                                                                                      {:type "reset-question-attempt"}]}
                                                 (keyword hide-question-name) {:type "sequence-data"
                                                                               :data [{:type       "set-attribute"
                                                                                       :target     object-name
                                                                                       :attr-name  "visible"
                                                                                       :attr-value false}
                                                                                      {:type     "set-variable" :var-name "tap-instructions-action"
                                                                                       :from-var [{:var-name "tap-instractuins-prev", :action-property "var-value"}]}
                                                                                      {:type "hide-guide"}]}
                                                 (keyword task-dialog-name)   {:type               "sequence-data",
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
                                                                               :editor-type        "dialog"}}
                                       :track   {:title alias
                                                 :nodes [{:type      "dialog"
                                                          :action-id (keyword task-dialog-name)}]}
                                       :objects {}
                                       :assets  []}
                                      :always (merge-data (create-option-click-handler question-id form-data data-names option-objects-names))
                                      :always (merge-data (add-check-correct-answer {:action-name        check-answers
                                                                                     :correct-answers    correct-answers
                                                                                     :hide-question-name hide-question-name
                                                                                     :question-id        question-id
                                                                                     :option-voice-over-name option-voice-over-name}
                                                                                    form-data
                                                                                    data-names))
                                      :always (merge-data (create-voice-over-handlers form-data
                                                                                      {:action-name        option-voice-over-name
                                                                                       :text-objects-names text-objects-names
                                                                                       :text-animation?    (= question-type "multiple-choice-text")
                                                                                       :question-id        question-id}))
                                      :always (merge-data (create-question form-data
                                                                           data-names
                                                                           (merge {:question-id        question-id
                                                                                   :object-name        object-name
                                                                                   :text-objects-names text-objects-names}
                                                                                  question-params)
                                                                           creation-options))))]
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
                                                           :name   alias}]))))
