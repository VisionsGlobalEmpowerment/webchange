(ns webchange.templates.library.categorize-antonims.round-2)

(def template-round-2 {:assets        [
                                       {:url "/raw/img/categorize-antonims/background.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/day.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/night.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/down.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/up.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/back.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/front.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/in.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/out.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/left.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/right.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/loud.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/quiet.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/question.png", :size 10, :type "image"}
                                       ],
                       :objects       {:background {:type "background", :src "/raw/img/categorize-antonims/background.png"},
                                       :right-box
                                                   {:type       "image",
                                                    :x          15,
                                                    :y          800,
                                                    :src        "/raw/img/categorize-antonims/right.png",
                                                    :transition "right-box",
                                                    :states     {:highlighted     {:highlight true}
                                                                 :not-highlighted {:highlight false}},
                                                    },
                                       :front-box  {:type       "image",
                                                    :x          250,
                                                    :y          800,
                                                    :transition "front-box",
                                                    :src        "/raw/img/categorize-antonims/front.png",
                                                    :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                    }
                                       :down-box   {:type       "image",
                                                    :x          500,
                                                    :y          800,
                                                    :transition "down-box",
                                                    :src        "/raw/img/categorize-antonims/down.png",
                                                    :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                    }
                                       :quiet-box  {:type       "image",
                                                    :x          800,
                                                    :y          800,
                                                    :transition "quiet-box",
                                                    :src        "/raw/img/categorize-antonims/quiet.png",
                                                    :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                    }
                                       :day-box    {:type       "image",
                                                    :x          1100,
                                                    :y          800,
                                                    :transition "day-box",
                                                    :src        "/raw/img/categorize-antonims/day.png",
                                                    :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                    }
                                       :in-box     {:type       "image",
                                                    :x          1400,
                                                    :y          800,
                                                    :transition "in-box",
                                                    :src        "/raw/img/categorize-antonims/in.png",
                                                    :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                    }
                                       :left-1     {
                                                    :type       "image",
                                                    :x          46,
                                                    :y          700,
                                                    :scale      0.35,
                                                    :src        "/raw/img/categorize-antonims/left.png",
                                                    :transition "left-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {:type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"}
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "right-box"
                                                                                       :target         "left-1"
                                                                                       :init-position  {:x 46, :y 700, :duration 1}
                                                                                       :check-variable "right-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}},
                                       :back-1     {
                                                    :type       "image",
                                                    :x          592,
                                                    :y          500,
                                                    :rotation   -90,
                                                    :scale      0.35,
                                                    :src        "/raw/img/categorize-antonims/back.png",
                                                    :transition "back-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"}
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "front-box"
                                                                                       :target         "back-1"
                                                                                       :init-position  {:x 592, :y 500, :duration 1}
                                                                                       :check-variable "front-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}},
                                       :up-1       {
                                                    :type       "image",
                                                    :x          392,
                                                    :y          700,
                                                    :rotation   -90,
                                                    :scale      0.35,
                                                    :src        "/raw/img/categorize-antonims/up.png",
                                                    :transition "up-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"}
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "down-box"
                                                                                       :target         "up-1"
                                                                                       :init-position  {:x 392, :y 700, :duration 1}
                                                                                       :check-variable "down-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}}
                                       :loud-1     {
                                                    :type       "image",
                                                    :x          1192,
                                                    :y          700,
                                                    :rotation   -90,
                                                    :scale      0.35,
                                                    :src        "/raw/img/categorize-antonims/loud.png",
                                                    :transition "loud-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"}
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "quiet-box"
                                                                                       :target         "loud-1"
                                                                                       :init-position  {:x 1192, :y 700, :duration 1}
                                                                                       :check-variable "quiet-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}}
                                       :night-1    {
                                                    :type       "image",
                                                    :x          992,
                                                    :y          200,
                                                    :rotation   -90,
                                                    :scale      0.35,
                                                    :src        "/raw/img/categorize-antonims/night.png",
                                                    :transition "night-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"}
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "day-box"
                                                                                       :target         "night-1"
                                                                                       :init-position  {:x 992, :y 200, :duration 1}
                                                                                       :check-variable "day-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}}
                                       :out-1      {
                                                    :type       "image",
                                                    :x          392,
                                                    :y          200,
                                                    :rotation   -90,
                                                    :scale      0.35,
                                                    :src        "/raw/img/categorize-antonims/out.png",
                                                    :transition "out-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"}
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "in-box"
                                                                                       :target         "out-1"
                                                                                       :init-position  {:x 392, :y 200, :duration 1}
                                                                                       :check-variable "in-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}}

                                       },
                       :scene-objects [["background"]
                                       ["right-box" "front-box" "down-box"]
                                       ["quiet-box" "day-box" "in-box"]
                                       ["left-1" "back-1" "up-1"]
                                       ["loud-1" "night-1" "out-1"]],
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
                                       :check-collide {:type "sequence-data"
                                                       :data [
                                                              {:type        "test-transitions-and-pointer-collide",
                                                               :success     "highlight",
                                                               :fail        "unhighlight",
                                                               :transitions ["front-box" "right-box" "down-box" "quiet-box" "day-box" "in-box"]
                                                               :action-params [{:var-name "front-box"}
                                                                               {:var-name "right-box"}
                                                                               {:var-name "down-box"}
                                                                               {:var-name "quiet-box"}
                                                                               {:var-name "day-box"}
                                                                               {:var-name  "in-box"}]
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

                                                                                      :image      "/raw/img/categorize-antonims/question.png"
                                                                                      :background "/raw/img/bg.png"
                                                                                      :answers    {:data
                                                                                                   [
                                                                                                    {:text       "A. They are both left."
                                                                                                     :correct    false
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
                                                                                                    {:text       "B. They are both circles."
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
                                                                                                    {:text       "C. They are both the same."
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
                                                                                                    {:text       "D. They are both sides of a girl."
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
                                                                                                     :correct    true}
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