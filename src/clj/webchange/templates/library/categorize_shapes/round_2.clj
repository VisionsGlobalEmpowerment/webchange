(ns webchange.templates.library.categorize-shapes.round-2)

(def template-round-2 {:assets        [
                                       {:url "/raw/img/categorize-shapes/background.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/circle.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/oval.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/rectangle.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/square.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/star.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/triangle.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/circle-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/oval-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/rectangle-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/square-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/star-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/triangle-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/question.png", :size 10, :type "image"}
                                       ],
                       :objects       {:background    {:type "background", :src "/raw/img/categorize/background.png"},
                                       :circle-box
                                                      {:type       "image",
                                                       :x          15,
                                                       :y          800,
                                                       :src        "/raw/img/categorize-shapes/circle-box.png",
                                                       :transition "circle-box",
                                                       :states     {:highlighted     {:highlight true}
                                                                    :not-highlighted {:highlight false}},
                                                       },
                                       :oval-box      {:type       "image",
                                                       :x          250,
                                                       :y          800,
                                                       :transition "oval-box",
                                                       :src        "/raw/img/categorize-shapes/oval-box.png",
                                                       :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                       }
                                       :rectangle-box {:type       "image",
                                                       :x          500,
                                                       :y          800,
                                                       :transition "rectangle-box",
                                                       :src        "/raw/img/categorize-shapes/rectangle-box.png",
                                                       :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                       }
                                       :square-box    {:type       "image",
                                                       :x          800,
                                                       :y          800,
                                                       :transition "square-box",
                                                       :src        "/raw/img/categorize-shapes/square-box.png",
                                                       :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                       }
                                       :star-box      {:type       "image",
                                                       :x          1100,
                                                       :y          800,
                                                       :transition "star-box",
                                                       :src        "/raw/img/categorize-shapes/star-box.png",
                                                       :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                       }
                                       :triangle-box  {:type       "image",
                                                       :x          1400,
                                                       :y          800,
                                                       :transition "triangle-box",
                                                       :src        "/raw/img/categorize-shapes/triangle-box.png",
                                                       :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                       }
                                       :circle-1      {
                                                       :type       "image",
                                                       :x          46,
                                                       :y          700,
                                                       :scale      0.35,
                                                       :src        "/raw/img/categorize-shapes/circle.png",
                                                       :transition "circle-1",
                                                       :draggable  true,
                                                       :actions    {:drag-start {:type "action",
                                                                                 :on   "drag-start",
                                                                                 :id   "start-drag"}
                                                                    :drag-end
                                                                                {:id     "dragged",
                                                                                 :on     "drag-end",
                                                                                 :type   "action",
                                                                                 :params {:box            "circle-box"
                                                                                          :target         "circle-1"
                                                                                          :init-position  {:x 46, :y 700, :duration 1}
                                                                                          :check-variable "circle-box-selected"
                                                                                          }}},
                                                       :states     {:hide {:visible false}}},
                                       :oval-1        {
                                                       :type       "image",
                                                       :x          592,
                                                       :y          500,
                                                       :rotation   -90,
                                                       :scale      0.35,
                                                       :src        "/raw/img/categorize-shapes/oval.png",
                                                       :transition "oval-1",
                                                       :draggable  true,
                                                       :actions    {:drag-start {
                                                                                 :type "action",
                                                                                 :on   "drag-start",
                                                                                 :id   "start-drag"}
                                                                    :drag-end
                                                                                {:id     "dragged",
                                                                                 :on     "drag-end",
                                                                                 :type   "action",
                                                                                 :params {:box            "oval-box"
                                                                                          :target         "oval-1"
                                                                                          :init-position  {:x 592, :y 500, :duration 1}
                                                                                          :check-variable "oval-box-selected"
                                                                                          }}},
                                                       :states     {:hide {:visible false}}},
                                       :rectangle-1   {
                                                       :type       "image",
                                                       :x          392,
                                                       :y          700,
                                                       :rotation   -90,
                                                       :scale      0.35,
                                                       :src        "/raw/img/categorize-shapes/rectangle.png",
                                                       :transition "rectangle-1",
                                                       :draggable  true,
                                                       :actions    {:drag-start {
                                                                                 :type "action",
                                                                                 :on   "drag-start",
                                                                                 :id   "start-drag"}
                                                                    :drag-end
                                                                                {:id     "dragged",
                                                                                 :on     "drag-end",
                                                                                 :type   "action",
                                                                                 :params {:box            "rectangle-box"
                                                                                          :target         "rectangle-1"
                                                                                          :init-position  {:x 392, :y 700, :duration 1}
                                                                                          :check-variable "rectangle-box-selected"
                                                                                          }}},
                                                       :states     {:hide {:visible false}}}
                                       :square-1      {
                                                       :type       "image",
                                                       :x          1192,
                                                       :y          700,
                                                       :rotation   -90,
                                                       :scale      0.35,
                                                       :src        "/raw/img/categorize-shapes/square.png",
                                                       :transition "square-1",
                                                       :draggable  true,
                                                       :actions    {:drag-start {
                                                                                 :type "action",
                                                                                 :on   "drag-start",
                                                                                 :id   "start-drag"}
                                                                    :drag-end
                                                                                {:id     "dragged",
                                                                                 :on     "drag-end",
                                                                                 :type   "action",
                                                                                 :params {:box            "square-box"
                                                                                          :target         "square-1"
                                                                                          :init-position  {:x 1192, :y 700, :duration 1}
                                                                                          :check-variable "square-box-selected"
                                                                                          }}},
                                                       :states     {:hide {:visible false}}}
                                       :star-1        {
                                                       :type       "image",
                                                       :x          992,
                                                       :y          200,
                                                       :rotation   -90,
                                                       :scale      0.35,
                                                       :src        "/raw/img/categorize-shapes/star.png",
                                                       :transition "star-1",
                                                       :draggable  true,
                                                       :actions    {:drag-start {
                                                                                 :type "action",
                                                                                 :on   "drag-start",
                                                                                 :id   "start-drag"}
                                                                    :drag-end
                                                                                {:id     "dragged",
                                                                                 :on     "drag-end",
                                                                                 :type   "action",
                                                                                 :params {:box            "star-box"
                                                                                          :target         "star-1"
                                                                                          :init-position  {:x 992, :y 200, :duration 1}
                                                                                          :check-variable "star-box-selected"
                                                                                          }}},
                                                       :states     {:hide {:visible false}}}
                                       :triangle-1    {
                                                       :type       "image",
                                                       :x          392,
                                                       :y          200,
                                                       :rotation   -90,
                                                       :scale      0.35,
                                                       :src        "/raw/img/categorize-shapes/triangle.png",
                                                       :transition "triangle-1",
                                                       :draggable  true,
                                                       :actions    {:drag-start {
                                                                                 :type "action",
                                                                                 :on   "drag-start",
                                                                                 :id   "start-drag"}
                                                                    :drag-end
                                                                                {:id     "dragged",
                                                                                 :on     "drag-end",
                                                                                 :type   "action",
                                                                                 :params {:box            "triangle-box"
                                                                                          :target         "triangle-1"
                                                                                          :init-position  {:x 392, :y 200, :duration 1}
                                                                                          :check-variable "triangle-box-selected"
                                                                                          }}},
                                                       :states     {:hide {:visible false}}}

                                       },
                       :scene-objects [["background"]
                                       ["circle-box" "oval-box" "rectangle-box"]
                                       ["square-box" "star-box" "triangle-box"]
                                       ["circle-1" "oval-1" "rectangle-1"]
                                       ["square-1" "star-1" "triangle-1"]],
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
                                                                        :var-names ["front-box-selected" "right-box-selected" "down-box-selected"
                                                                                    "quiet-box-selected" "day-box-selected" "in-box-selected"]
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
                                                                               {:type        "set-variable",
                                                                                :var-value   true
                                                                                :from-params [{:action-property "var-name" :param-property "variable"}]
                                                                                }
                                                                               {:type        "state"
                                                                                :id          "highlighted"
                                                                                :from-params [{:action-property "target" :param-property "target"}]
                                                                                }]
                                                                        }
                                       :unhighlight                    {:type "sequence-data"
                                                                        :data [{:type        "state"
                                                                                :id          "not-highlighted"
                                                                                :from-params [{:action-property "target" :param-property "target"}]}]
                                                                        }
                                       :check-collide                  {:type "sequence-data"
                                                                        :data [
                                                                               {:type       "test-transition-and-pointer-collide",
                                                                                :success    "highlight",
                                                                                :fail       "unhighlight",
                                                                                :transition "oval-box",
                                                                                :params     {:target   "oval-box"
                                                                                             :variable "oval-box-selected"}
                                                                                }
                                                                               {:type       "test-transition-and-pointer-collide",
                                                                                :success    "highlight",
                                                                                :fail       "unhighlight",
                                                                                :transition "circle-box"
                                                                                :params     {:target   "circle-box"
                                                                                             :variable "circle-box-selected"
                                                                                             }
                                                                                }
                                                                               {:type       "test-transition-and-pointer-collide",
                                                                                :success    "highlight",
                                                                                :fail       "unhighlight",
                                                                                :transition "rectangle-box"
                                                                                :params     {:target   "rectangle-box"
                                                                                             :variable "rectangle-box-selected"
                                                                                             }
                                                                                }
                                                                               {:type       "test-transition-and-pointer-collide",
                                                                                :success    "highlight",
                                                                                :fail       "unhighlight",
                                                                                :transition "square-box"
                                                                                :params     {:target   "square-box"
                                                                                             :variable "square-box-selected"
                                                                                             }
                                                                                }
                                                                               {:type       "test-transition-and-pointer-collide",
                                                                                :success    "highlight",
                                                                                :fail       "unhighlight",
                                                                                :transition "star-box"
                                                                                :params     {:target   "star-box"
                                                                                             :variable "star-box-selected"
                                                                                             }
                                                                                }
                                                                               {:type       "test-transition-and-pointer-collide",
                                                                                :success    "highlight",
                                                                                :fail       "unhighlight",
                                                                                :transition "triangle-box"
                                                                                :params     {:target   "triangle-box"
                                                                                             :variable "triangle-box-selected"
                                                                                             }
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

                                                                                      :image      "/raw/img/categorize-shapes/question.png"
                                                                                      :background "/raw/img/bg.png"
                                                                                      :answers    {:data
                                                                                                   [
                                                                                                    {:text       "A. They are both blue"
                                                                                                     :correct    false
                                                                                                     :chunks     [{:start 0, :end 2},
                                                                                                                  {:start 3, :end 7},
                                                                                                                  {:start 8, :end 11},
                                                                                                                  {:start 12, :end 16},
                                                                                                                  {:start 17, :end 20}]
                                                                                                     :audio-data {
                                                                                                                  :audio     ""
                                                                                                                  :start     0,
                                                                                                                  :duration  0,
                                                                                                                  :animation "color",
                                                                                                                  :fill      0x00B2FF
                                                                                                                  :data      []}
                                                                                                     }
                                                                                                    {:text       "B. They are both squares"
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
                                                                                                    {:text       "C. They are both shapes that are round"
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
                                                                                                     :correct    true}
                                                                                                    {:text       "D. They are both foods"
                                                                                                     :chunks     [{:start 0, :end 2},
                                                                                                                  {:start 3, :end 7},
                                                                                                                  {:start 8, :end 11},
                                                                                                                  {:start 12, :end 16},
                                                                                                                  {:start 17, :end 22}],
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
                                                                        :phrase             "finish-dialog",
                                                                        :phrase-description "finish dialog"}
                                       :fail-answer-dialog             {:type               "sequence-data",
                                                                        :editor-type        "dialog",
                                                                        :data               [{:type "sequence-data"
                                                                                              :data [{:type "empty" :duration 0}
                                                                                                     {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                        :phrase             "finish-dialog",
                                                                        :phrase-description "finish dialog"}
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