(ns webchange.templates.library.categorize-synonyms.round-2)

(def template-round-2 {:assets        [
                                       {:url "/raw/img/categorize-synonyms/background-class.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/surface.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/decoration.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/afraid.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/scared.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/large.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/big.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/child.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/kid.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/glad.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/happy.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/cold.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/chilly.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/trash.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/garbage.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/question.png", :size 10, :type "image"}
                                       ],
                       :objects       {:layered-background {:type       "layered-background",
                                                            :background {:src "/raw/img/categorize-shapes/background-class.png"},
                                                            :decoration {:src "/raw/img/categorize-shapes/decoration.png"},
                                                            :surface    {:src "/raw/img/categorize-shapes/surface.png"}},
                                       :chilly-box
                                                           {:type       "image",
                                                            :width      253,
                                                            :height     253,
                                                            :x          980,
                                                            :y          763,
                                                            :src        "/raw/img/categorize-synonyms/chilly.png",
                                                            :transition "chilly-box",
                                                            :states     {:highlighted     {:highlight true}
                                                                         :not-highlighted {:highlight false}},
                                                            },
                                       :kid-box            {:type       "image",
                                                            :width      253,
                                                            :height     252,
                                                            :x          1274,
                                                            :y          763,
                                                            :transition "kid-box",
                                                            :src        "/raw/img/categorize-synonyms/kid.png",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                            }
                                       :large-box          {:type       "image",
                                                            :width      253,
                                                            :height     253,
                                                            :x          392,
                                                            :y          762,
                                                            :transition "large-box",
                                                            :src        "/raw/img/categorize-synonyms/large.png",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                            }
                                       :garbage-box        {:type       "image",
                                                            :width      253,
                                                            :height     253,
                                                            :x          1568,
                                                            :y          762,
                                                            :transition "garbage-box",
                                                            :src        "/raw/img/categorize-synonyms/garbage.png",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                            }
                                       :afraid-box         {:type       "image",
                                                            :width      253,
                                                            :height     253,
                                                            :x          686,
                                                            :y          763,
                                                            :transition "afraid-box",
                                                            :src        "/raw/img/categorize-synonyms/afraid.png",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                            }
                                       :glad-box           {:type       "image",
                                                            :width      253,
                                                            :height     253,
                                                            :x          99,
                                                            :y          763,
                                                            :transition "glad-box",
                                                            :src        "/raw/img/categorize-synonyms/glad.png",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                            }
                                       :cold-1             {
                                                            :type       "image",
                                                            :width      159,
                                                            :height     160,
                                                            :x          768,
                                                            :y          481,
                                                            :src        "/raw/img/categorize-synonyms/cold.png",
                                                            :transition "cold-1",
                                                            :draggable  true,
                                                            :actions    {:drag-start {:type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"}
                                                                         :drag-end
                                                                                     {:id     "dragged",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {:box            "chilly-box"
                                                                                               :target         "cold-1"
                                                                                               :init-position  {:x 768,
                                                                                                                :y 481, :duration 1}
                                                                                               :check-variable "chilly-box-selected"
                                                                                               }}},
                                                            :states     {:hide {:visible false}}},
                                       :child-1            {
                                                            :type       "image",
                                                            :width      161,
                                                            :height     160,
                                                            :x          1094,
                                                            :y          223,
                                                            :src        "/raw/img/categorize-synonyms/child.png",
                                                            :transition "child-1",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"}
                                                                         :drag-end
                                                                                     {:id     "dragged",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {:box            "kid-box"
                                                                                               :target         "child-1"
                                                                                               :init-position  {:x 1094,
                                                                                                                :y 223, :duration 1}
                                                                                               :check-variable "kid-box-selected"
                                                                                               }}},
                                                            :states     {:hide {:visible false}}},
                                       :big-1              {
                                                            :type       "image",
                                                            :width      160,
                                                            :height     159,
                                                            :x          415,
                                                            :y          355,
                                                            :src        "/raw/img/categorize-synonyms/big.png",
                                                            :transition "big-1",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"}
                                                                         :drag-end
                                                                                     {:id     "dragged",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {:box            "large-box"
                                                                                               :target         "big-1"
                                                                                               :init-position  {:x 415,
                                                                                                                :y 355, :duration 1}
                                                                                               :check-variable "large-box-selected"
                                                                                               }}},
                                                            :states     {:hide {:visible false}}}
                                       :trash-1            {
                                                            :type       "image",
                                                            :width      160,
                                                            :height     160,
                                                            :x          1388,
                                                            :y          561,
                                                            :src        "/raw/img/categorize-synonyms/trash.png",
                                                            :transition "trash-1",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"}
                                                                         :drag-end
                                                                                     {:id     "dragged",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {:box            "garbage-box"
                                                                                               :target         "trash-1"
                                                                                               :init-position  {:x 1388,
                                                                                                                :y 561, :duration 1}
                                                                                               :check-variable "garbage-box-selected"
                                                                                               }}},
                                                            :states     {:hide {:visible false}}}
                                       :scared-1           {
                                                            :type       "image",
                                                            :width      160,
                                                            :height     160,
                                                            :x          1636,
                                                            :y          107,
                                                            :src        "/raw/img/categorize-synonyms/scared.png",
                                                            :transition "scared-1",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"}
                                                                         :drag-end
                                                                                     {:id     "dragged",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {:box            "afraid-box"
                                                                                               :target         "scared-1"
                                                                                               :init-position  {:x 1636,
                                                                                                                :y 107, :duration 1}
                                                                                               :check-variable "afraid-box-selected"
                                                                                               }}},
                                                            :states     {:hide {:visible false}}}
                                       :happy-1            {
                                                            :type       "image",
                                                            :width      160,
                                                            :height     161,
                                                            :x          1674,
                                                            :y          434,
                                                            :src        "/raw/img/categorize-synonyms/happy.png",
                                                            :transition "happy-1",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"}
                                                                         :drag-end
                                                                                     {:id     "dragged",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {:box            "glad-box"
                                                                                               :target         "happy-1"
                                                                                               :init-position  {:x 1674,
                                                                                                                :y 434, :duration 1}
                                                                                               :check-variable "glad-box-selected"
                                                                                               }}},
                                                            :states     {:hide {:visible false}}}

                                       },
                       :scene-objects [["layered-background"]
                                       ["chilly-box" "kid-box" "large-box"]
                                       ["garbage-box" "afraid-box" "glad-box"]
                                       ["cold-1" "child-1" "big-1"]
                                       ["trash-1" "scared-1" "happy-1"]],
                       :actions       {

                                       :object-in-right-box            {:id          "hide",
                                                                        :type        "state",
                                                                        :from-params [{:action-property "target" :param-property "target"}]},
                                       :object-revert                  {:type        "transition",
                                                                        :from-params [{:action-property "transition-id" :param-property "target"}
                                                                                      {:action-property "to" :param-property "init-position"}]
                                                                        }
                                       :wrong-option                   {:type "sequence-data",
                                                                        :data [{:id "object-revert", :type "action"}
                                                                               {:id "wrong-answer", :type "action"}],}

                                       :correct-option                 {:type "sequence-data",
                                                                        :data [{:type "remove-interval"
                                                                                :id   "check-collide-2"}
                                                                               {:type "counter" :counter-action "increase" :counter-id "sorted-objects"}
                                                                               {:id "object-in-right-box", :type "action"}
                                                                               {:id "correct-answer", :type "action"}
                                                                               {:type       "test-var-inequality"
                                                                                :var-name   "sorted-objects",
                                                                                :value      6,
                                                                                :inequality ">=",
                                                                                :success    "scene-question",
                                                                                :fail       "continue-sorting"
                                                                                }
                                                                               ]}
                                       :check-option                   {:type      "test-var-list-at-least-one-true"
                                                                        :var-names ["kid-box-selected" "chilly-box-selected" "large-box-selected"
                                                                                    "garbage-box-selected" "afraid-box-selected" "glad-box-selected"]
                                                                        :success   "wrong-option",
                                                                        :fail      "object-revert"
                                                                        }
                                       :dragged                        {:type "sequence-data"
                                                                        :data [
                                                                               {:type        "test-var-scalar",
                                                                                :success     "correct-option",
                                                                                :fail        "check-option",
                                                                                :value       true,
                                                                                :from-params [{:param-property "check-variable", :action-property "var-name"}]
                                                                                }
                                                                               {:type "remove-interval"
                                                                                :id   "check-collide-2"}
                                                                               {:type        "state"
                                                                                :id          "not-highlighted"
                                                                                :from-params [{:action-property "target" :param-property "box"}]}
                                                                               ]
                                                                        }
                                       :highlight                      {:type "sequence-data"
                                                                        :data [{:type "action", :id "reset-selected-vars"}
                                                                               ;{:type        "set-variable",
                                                                               ; :var-value   true
                                                                               ; :from-params [{:action-property "var-name" :param-property "variable"}]
                                                                               ; }
                                                                               {:type        "set-variable",
                                                                                :var-value   true
                                                                                :from-params [{:action-property "var-name",
                                                                                               :template        "%-selected",
                                                                                               :param-property  "var-name"}]
                                                                                }
                                                                               {:type        "state"
                                                                                :id          "highlighted"
                                                                                :from-params [{:action-property "target" :param-property "transition"}]
                                                                                }]
                                                                        }
                                       :unhighlight                    {:type "sequence-data"
                                                                        :data [{:type        "state"
                                                                                :id          "not-highlighted"
                                                                                :from-params [{:action-property "target" :param-property "transition"}]}]
                                                                        }
                                       :check-collide                  {:type "sequence-data"
                                                                        :data [
                                                                               {:type          "test-transitions-and-pointer-collide",
                                                                                :success       "highlight",
                                                                                :fail          "unhighlight",
                                                                                :transitions   ["kid-box" "chilly-box" "large-box" "garbage-box" "afraid-box" "glad-box"]
                                                                                :action-params [{:var-name "kid-box"}
                                                                                                {:var-name "chilly-box"}
                                                                                                {:var-name "large-box"}
                                                                                                {:var-name "garbage-box"}
                                                                                                {:var-name "afraid-box"}
                                                                                                {:var-name "glad-box"}]
                                                                                }]}

                                       :init-activity                  {:type "sequence-data"
                                                                        :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-objects"}
                                                                               {:type "action" :id "intro"}]
                                                                        }
                                       :reset-selected-vars            {:type "sequence-data"
                                                                        :data [
                                                                               {:type "set-variable", :var-name "oval-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "circle-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "rectangle-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "square-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "star-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "triangle-box-selected", :var-value false}
                                                                               ]
                                                                        }
                                       :start-drag                     {:type "sequence-data"
                                                                        :data [
                                                                               {:type "action", :id "reset-selected-vars"}
                                                                               {:type     "set-interval"
                                                                                :id       "check-collide-2"
                                                                                :interval 100
                                                                                :action   "check-collide"}
                                                                               ]
                                                                        }
                                       :intro                          {:type               "sequence-data",
                                                                        :editor-type        "dialog",
                                                                        :data               [{:type "sequence-data"
                                                                                              :data [{:type "empty" :duration 0}
                                                                                                     {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                        :phrase             "intro",
                                                                        :phrase-description "Introduce task"}
                                       :correct-answer                 {:type               "sequence-data",
                                                                        :editor-type        "dialog",
                                                                        :data               [{:type "sequence-data"
                                                                                              :data [{:type "empty" :duration 0}
                                                                                                     {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                        :phrase             "correct-answer",
                                                                        :phrase-description "correct answer"}
                                       :wrong-answer                   {:type               "sequence-data",
                                                                        :editor-type        "dialog",
                                                                        :data               [{:type "sequence-data"
                                                                                              :data [{:type "empty" :duration 0}
                                                                                                     {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                        :phrase             "wrong-answer",
                                                                        :phrase-description "wrong answer"}
                                       :scene-question                 {
                                                                        :type        "show-question"
                                                                        :description "What is the same about these two groups?"
                                                                        :data        {
                                                                                      :type       "type-1"
                                                                                      :text       "What is the same about these two groups?\n"
                                                                                      :chunks     [{:start 0, :end 4},
                                                                                                   {:start 5, :end 7},
                                                                                                   {:start 8, :end 11},
                                                                                                   {:start 12, :end 16},
                                                                                                   {:start 17, :end 22},
                                                                                                   {:start 23, :end 28},
                                                                                                   {:start 29, :end 32},
                                                                                                   {:start 33, :end 40}
                                                                                                   ]
                                                                                      :success    "correct-answer-question",
                                                                                      :fail       "fail-answer-question"
                                                                                      :skip       "skip-question"
                                                                                      :audio-data {
                                                                                                   :audio     ""
                                                                                                   :start     0,
                                                                                                   :duration  0,
                                                                                                   :animation "color",
                                                                                                   :fill      0x00B2FF
                                                                                                   :data      []}

                                                                                      :image      "/raw/img/categorize-synonyms/question.png"
                                                                                      :background "/raw/img/bg.png"
                                                                                      :answers    {:data
                                                                                                   [
                                                                                                    {:text       "A.  They are both faces that are smiling."
                                                                                                     :correct    true
                                                                                                     :chunks     [{:start 0, :end 2},
                                                                                                                  {:start 3, :end 7},
                                                                                                                  {:start 8, :end 11},
                                                                                                                  {:start 12, :end 16},
                                                                                                                  {:start 17, :end 22}]
                                                                                                     :audio-data {
                                                                                                                  :audio     ""
                                                                                                                  :start     0,
                                                                                                                  :duration  0,
                                                                                                                  :animation "color",
                                                                                                                  :fill      0x00B2FF
                                                                                                                  :data      []}
                                                                                                     }
                                                                                                    {:text       "B. They are both opposites."
                                                                                                     :chunks     [{:start 0, :end 2},
                                                                                                                  {:start 3, :end 7},
                                                                                                                  {:start 8, :end 11},
                                                                                                                  {:start 12, :end 16},
                                                                                                                  {:start 17, :end 24},
                                                                                                                  {:start 25, :end 28},
                                                                                                                  {:start 29, :end 32},
                                                                                                                  {:start 33, :end 36},
                                                                                                                  {:start 37, :end 39},
                                                                                                                  {:start 40, :end 45}
                                                                                                                  ],
                                                                                                     :audio-data {
                                                                                                                  :audio     ""
                                                                                                                  :start     0,
                                                                                                                  :duration  0,
                                                                                                                  :animation "color",
                                                                                                                  :fill      0x00B2FF
                                                                                                                  :data      []}
                                                                                                     :correct    false}
                                                                                                    {:text       "C. They are both green."
                                                                                                     :chunks     [{:start 0, :end 2},
                                                                                                                  {:start 3, :end 7},
                                                                                                                  {:start 8, :end 11},
                                                                                                                  {:start 12, :end 16},
                                                                                                                  {:start 17, :end 19},
                                                                                                                  {:start 20, :end 25},
                                                                                                                  {:start 26, :end 32},
                                                                                                                  {:start 33, :end 38}]
                                                                                                     :audio-data {
                                                                                                                  :audio     ""
                                                                                                                  :start     0,
                                                                                                                  :duration  0,
                                                                                                                  :animation "color",
                                                                                                                  :fill      0x00B2FF
                                                                                                                  :data      []}
                                                                                                     :correct    false}
                                                                                                    {:text       "D. They are both stars."
                                                                                                     :chunks     [{:start 0, :end 2},
                                                                                                                  {:start 3, :end 7},
                                                                                                                  {:start 8, :end 11},
                                                                                                                  {:start 12, :end 16},
                                                                                                                  {:start 17, :end 22}
                                                                                                                  {:start 23, :end 40}],
                                                                                                     :audio-data {
                                                                                                                  :audio     ""
                                                                                                                  :start     0,
                                                                                                                  :duration  0,
                                                                                                                  :animation "color",
                                                                                                                  :fill      0x00B2FF
                                                                                                                  :data      []}
                                                                                                     :correct    false}
                                                                                                    ]
                                                                                                   }
                                                                                      }
                                                                        }
                                       :fail-answer-question           {:type "sequence-data",
                                                                        :data [{:type "action" :id "fail-answer-dialog"}],}
                                       :correct-answer-question        {:type "sequence-data",
                                                                        :data [
                                                                               {:type "empty" :duration 500}
                                                                               {:type "hide-question"}
                                                                               {:type "action" :id "correct-answer-dialog"}
                                                                               {:type "action" :id "technical-question-placeholder"}
                                                                               ],
                                                                        }
                                       :skip-question                  {:type "sequence-data",
                                                                        :data [
                                                                               {:type "hide-question"}
                                                                               {:type "action" :id "technical-question-placeholder"}
                                                                               ],
                                                                        }
                                       :technical-question-placeholder {:type "action" :id "finish-scene"}
                                       :correct-answer-dialog          {:type               "sequence-data",
                                                                        :editor-type        "dialog",
                                                                        :data               [{:type "sequence-data"
                                                                                              :data [{:type "empty" :duration 0}
                                                                                                     {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                        :phrase             "correct-answer-dialog ",
                                                                        :phrase-description "Correct answer question dialog"}
                                       :fail-answer-dialog             {:type               "sequence-data",
                                                                        :editor-type        "dialog",
                                                                        :data               [{:type "sequence-data"
                                                                                              :data [{:type "empty" :duration 0}
                                                                                                     {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                        :phrase             "fail-answer-dialog",
                                                                        :phrase-description "Fail answer question dialog"}
                                       :finish-scene                   {:type "sequence-data",
                                                                        :data [
                                                                               {:type "action" :id "finish-dialog"}
                                                                               {:type "remove-interval"
                                                                                :id   "check-collide-2"}
                                                                               {:type "action", :id "stop-activity"}
                                                                               ],
                                                                        }
                                       :finish-dialog                  {:type               "sequence-data",
                                                                        :editor-type        "dialog",
                                                                        :data               [{:type "sequence-data"
                                                                                              :data [{:type "empty" :duration 0}
                                                                                                     {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                        :phrase             "finish-dialog",
                                                                        :phrase-description "finish dialog"}
                                       :continue-sorting               {:type               "sequence-data",
                                                                        :editor-type        "dialog",
                                                                        :data               [{:type "sequence-data"
                                                                                              :data [{:type "empty" :duration 0}
                                                                                                     {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                        :phrase             "continue-sorting",
                                                                        :phrase-description "Continue sorting"}
                                       :stop-activity                  {:type "stop-activity", :id "categorize"},

                                       },

                       :triggers
                                      {:back  {:on "back", :action "stop-activity"},
                                       :start {:on "start", :action "init-activity"}},
                       :metadata      {:autostart   true
                                       :last-insert "technical-question-placeholder"
                                       :tracks      [{:title "Round 2"
                                                      :nodes [{:type      "dialog"
                                                               :action-id :intro}
                                                              {:type "prompt"
                                                               :text "Correct answer"}
                                                              {:type      "dialog"
                                                               :action-id :correct-answer}
                                                              {:type "prompt"
                                                               :text "Wrong answer"}
                                                              {:type      "dialog"
                                                               :action-id :wrong-answer}
                                                              {:type "prompt"
                                                               :text "Continue sorting"}
                                                              {:type      "dialog"
                                                               :action-id :continue-sorting}
                                                              {:type "prompt"
                                                               :text "Finish dialog"}
                                                              {:type      "dialog"
                                                               :action-id :finish-dialog}
                                                              ]}
                                                     ]
                                       },
                       })