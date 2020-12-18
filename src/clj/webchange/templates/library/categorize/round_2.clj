(ns webchange.templates.library.categorize.round-2)

(def template-round-2 {:assets        [{:url "/raw/img/categorize/01.png", :size 10, :type "image"}
                 {:url "/raw/img/categorize/02.png", :size 10, :type "image"}
                 {:url "/raw/img/categorize/03.png", :size 10, :type "image"}
                 {:url "/raw/img/categorize/yellow_box.png", :size 10, :type "image"}
                 {:url "/raw/img/categorize/blue_box.png", :size 10, :type "image"}
                 {:url "/raw/img/categorize/red_box.png", :size 10, :type "image"}
                 {:url "/raw/img/categorize/blue_crayons.png", :size 10, :type "image"}
                 {:url "/raw/img/categorize/red_crayons.png", :size 10, :type "image"}
                 {:url "/raw/img/categorize/yellow_crayons.png", :size 10, :type "image"}],
 :objects       {:layered-background {:type       "layered-background",
                                      :background {:src "/raw/img/categorize/01.png"},
                                      :decoration {:src "/raw/img/categorize/03.png"},
                                      :surface    {:src "/raw/img/categorize/02.png"}
                                      }
                 :yellow-box
                                     {:type       "image",
                                      :x          786,
                                      :y          754,
                                      :width      428,
                                      :height     549,
                                      :scale      0.8,
                                      :src        "/raw/img/categorize/yellow_box.png",
                                      :transition "yellow-box",
                                      :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                      },
                 :blue-box           {:type       "image",
                                      :x          320,
                                      :y          754,
                                      :width      428,
                                      :height     549,
                                      :scale      0.8,
                                      :transition "blue-box",
                                      :src        "/raw/img/categorize/blue_box.png",
                                      :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                      }
                 :red-box            {:type       "image",
                                      :x          1252,
                                      :y          754,
                                      :width      428,
                                      :height     549,
                                      :scale      0.8,
                                      :transition "red-box",
                                      :src        "/raw/img/categorize/red_box.png",
                                      :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                      },
                 :blue-crayon-1      {
                                      :type       "image",
                                      :x          46,
                                      :y          1050,
                                      :width      23,
                                      :height     224,
                                      :rotation   -90,
                                      :scale      0.35,
                                      :src        "/raw/img/categorize/blue_crayons.png",
                                      :transition "blue-crayon-1",
                                      :draggable  true,
                                      :actions    {:drag-start {:type   "action",
                                                                :on     "drag-start",
                                                                :id     "start-drag"}
                                                   :drag-end
                                                               {:id     "drag-crayon",
                                                                :on     "drag-end",
                                                                :type   "action",
                                                                :params {:box "blue-box"
                                                                         :target         "blue-crayon-1"
                                                                         :init-position  {:x 46, :y 1050, :duration 1}
                                                                         :check-variable "blue-box-selected"
                                                                         }}},
                                      :states     {:hide {:visible false}}},
                 :blue-crayon-2      {
                                      :type       "image",
                                      :x          592,
                                      :y          500,
                                      :width      23,
                                      :height     224,
                                      :rotation   -90,
                                      :scale      0.35,
                                      :src        "/raw/img/categorize/blue_crayons.png",
                                      :transition "blue-crayon-2",
                                      :draggable  true,
                                      :actions    {:drag-start {
                                                                :type   "action",
                                                                :on     "drag-start",
                                                                :id     "start-drag"}
                                                   :drag-end
                                                               {:id     "drag-crayon",
                                                                :on     "drag-end",
                                                                :type   "action",
                                                                :params {:box "blue-box"
                                                                         :target         "blue-crayon-2"
                                                                         :init-position  {:x 592, :y 500, :duration 1}
                                                                         :check-variable "blue-box-selected"
                                                                         }}},
                                      :states     {:hide {:visible false}}},
                 :blue-crayon-3      {
                                      :type       "image",
                                      :x          17,
                                      :y          438,
                                      :width      23,
                                      :height     224,
                                      :rotation   -90,
                                      :scale      0.35,
                                      :src        "/raw/img/categorize/blue_crayons.png",
                                      :transition "blue-crayon-3",
                                      :draggable  true,
                                      :actions    {:drag-start {:type   "action",
                                                                :on     "drag-start",
                                                                :id     "start-drag"}
                                                   :drag-end
                                                               {:id     "drag-crayon",
                                                                :on     "drag-end",
                                                                :type   "action",
                                                                :params {:box "blue-box"
                                                                         :target         "blue-crayon-3"
                                                                         :init-position  {:x 17, :y 438, :duration 1}
                                                                         :check-variable "blue-box-selected"
                                                                         }}},
                                      :states     {:hide {:visible false}}},
                 :yellow-crayon-1    {
                                      :type       "image",
                                      :x          764,
                                      :y          691,
                                      :width      23,
                                      :height     224,
                                      :rotation   -90,
                                      :scale      0.35,
                                      :src        "/raw/img/categorize/yellow_crayons.png",
                                      :transition "yellow-crayon-1",
                                      :draggable  true,
                                      :actions    {:drag-start {:type   "action",
                                                                :on     "drag-start",
                                                                :id     "start-drag"}
                                                   :drag-end
                                                               {:id     "drag-crayon",
                                                                :on     "drag-end",
                                                                :type   "action",
                                                                :params {:box "yellow-box"
                                                                         :target         "yellow-crayon-1"
                                                                         :init-position  {:x 764, :y 691, :duration 1}
                                                                         :check-variable "yellow-box-selected"
                                                                         }}},
                                      :states     {:hide {:visible false}}
                                      },
                 :yellow-crayon-2    {
                                      :type       "image",
                                      :x          1171,
                                      :y          126,
                                      :width      23,
                                      :height     224,
                                      :rotation   -90,
                                      :scale      0.35,
                                      :src        "/raw/img/categorize/yellow_crayons.png",
                                      :transition "yellow-crayon-2",
                                      :draggable  true,
                                      :actions    {:drag-start {:type   "action",
                                                                :on     "drag-start",
                                                                :id     "start-drag"}
                                                   :drag-end
                                                               {:id     "drag-crayon",
                                                                :on     "drag-end",
                                                                :type   "action",
                                                                :params {:box "yellow-box"
                                                                         :target         "yellow-crayon-2"
                                                                         :init-position  {:x 1171, :y 126, :duration 1}
                                                                         :check-variable "yellow-box-selected"
                                                                         }}},
                                      :states     {:hide {:visible false}}
                                      },
                 :yellow-crayon-3    {
                                      :type       "image",
                                      :x          1618,
                                      :y          440,
                                      :width      23,
                                      :height     224,
                                      :rotation   -90,
                                      :scale      0.35,
                                      :src        "/raw/img/categorize/yellow_crayons.png",
                                      :transition "yellow-crayon-3",
                                      :draggable  true,
                                      :actions    {:drag-start {:type   "action",
                                                                :on     "drag-start",
                                                                :id     "start-drag"}
                                                   :drag-end
                                                               {:id     "drag-crayon",
                                                                :on     "drag-end",
                                                                :type   "action",
                                                                :params {:box "yellow-box"
                                                                         :target         "yellow-crayon-3"
                                                                         :init-position  {:x 1618, :y 440, :duration 1}
                                                                         :check-variable "yellow-box-selected"
                                                                         }}},
                                      :states     {:hide {:visible false}}
                                      },
                 :red-crayon-1       {
                                      :type       "image",
                                      :x          924,
                                      :y          500,
                                      :width      23,
                                      :height     224,
                                      :rotation   -90,
                                      :scale      0.35,
                                      :src        "/raw/img/categorize/red_crayons.png",
                                      :transition "red-crayon-1",
                                      :draggable  true,
                                      :actions    {:drag-start {:type   "action",
                                                                :on     "drag-start",
                                                                :id     "start-drag"}
                                                   :drag-end
                                                               {:id     "drag-crayon",
                                                                :on     "drag-end",
                                                                :type   "action",
                                                                :params {:box "red-box"
                                                                         :target         "red-crayon-1"
                                                                         :init-position  {:x 924, :y 500, :duration 1}
                                                                         :check-variable "red-box-selected"
                                                                         }}},
                                      :states     {:hide {:visible false}}
                                      },
                 :red-crayon-2       {
                                      :type       "image",
                                      :x          1618,
                                      :y          958,
                                      :width      23,
                                      :height     224,
                                      :rotation   -90,
                                      :scale      0.35,
                                      :src        "/raw/img/categorize/red_crayons.png",
                                      :transition "red-crayon-2",
                                      :draggable  true,
                                      :actions    {:drag-start {:type   "action",
                                                                :on     "drag-start",
                                                                :id     "start-drag"}
                                                   :drag-end
                                                               {:id     "drag-crayon",
                                                                :on     "drag-end",
                                                                :type   "action",
                                                                :params {:box "red-box"
                                                                         :target         "red-crayon-2"
                                                                         :init-position  {:x 1618, :y 958, :duration 1}
                                                                         :check-variable "red-box-selected"
                                                                         }}},
                                      :states     {:hide {:visible false}}
                                      }
                 :red-crayon-3       {
                                      :type       "image",
                                      :x          1548,
                                      :y          164,
                                      :width      23,
                                      :height     224,
                                      :rotation   -90,
                                      :scale      0.35,
                                      :src        "/raw/img/categorize/red_crayons.png",
                                      :transition "red-crayon-3",
                                      :draggable  true,
                                      :actions    {:drag-start {:type   "action",
                                                                :on     "drag-start",
                                                                :id     "start-drag"}
                                                   :drag-end
                                                               {:id     "drag-crayon",
                                                                :on     "drag-end",
                                                                :type   "action",
                                                                :params {:box "red-box"
                                                                         :target         "red-crayon-3"
                                                                         :init-position  {:x 1548, :y 164, :duration 1}
                                                                         :check-variable "red-box-selected"
                                                                         }}},
                                      :states     {:hide {:visible false}}}},
 :scene-objects [["layered-background"]
                 ["yellow-box" "blue-box" "red-box"]
                 ["blue-crayon-1" "blue-crayon-2" "blue-crayon-3"]
                 ["yellow-crayon-1" "yellow-crayon-2" "yellow-crayon-3"]
                 ["red-crayon-1" "red-crayon-2" "red-crayon-3"]],
 :actions       {

                 :crayon-in-right-box {:id          "hide",
                                       :type        "state",
                                       :from-params [{:action-property "target" :param-property "target"}]},
                 :crayon-revert       {:type        "transition",
                                       :from-params [{:action-property "transition-id" :param-property "target"}
                                                     {:action-property "to" :param-property "init-position"}]
                                       }
                 :wrong-option        {:type "sequence-data",
                                       :data [{:id "crayon-revert", :type "action"}
                                              {:id "wrong-answer", :type "action"}],}

                 :correct-option      {:type "sequence-data",
                                       :data [{:type "counter" :counter-action "increase" :counter-id "sorted-crayons"}
                                              {:id "crayon-in-right-box", :type "action"}
                                              {:id "correct-answer", :type "action"}
                                              {:type       "test-var-inequality"
                                               :var-name   "sorted-crayons",
                                               :value      9,
                                               :inequality ">=",
                                               :success    "finish-scene",
                                               :fail       "continue-sorting"
                                               }
                                              ]}
                 :check-option        {:type      "test-var-list-at-least-one-true"
                                       :var-names ["yellow-box-selected" "blue-box-selected" "red-box-selected"]
                                       :success   "wrong-option",
                                       :fail      "crayon-revert"
                                       }
                 :drag-crayon         {:type "sequence-data"
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
                 :highlight           {:type "sequence-data"
                                       :data [{:type        "set-variable",
                                               :var-value   true
                                               :from-params [{:action-property "var-name" :param-property "variable"}]
                                               }
                                              {:type        "state"
                                               :id          "highlighted"
                                               :from-params [{:action-property "target" :param-property "target"}]
                                               }]
                                       }
                 :unhighlight         {:type "sequence-data"
                                       :data [{:type        "state"
                                               :id          "not-highlighted"
                                               :from-params [{:action-property "target" :param-property "target"}]}]
                                       }
                 :check-collide       {:type "sequence-data"
                                       :data [
                                              {:type       "test-transition-and-pointer-collide",
                                               :success    "highlight",
                                               :fail       "unhighlight",
                                               :transition "yellow-box",
                                               :params     {:target   "yellow-box"
                                                            :variable "yellow-box-selected"}
                                               }
                                              {:type       "test-transition-and-pointer-collide",
                                               :success    "highlight",
                                               :fail       "unhighlight",
                                               :transition "blue-box"
                                               :params     {:target   "blue-box"
                                                            :variable "blue-box-selected"
                                                            }
                                               }
                                              {:type       "test-transition-and-pointer-collide",
                                               :success    "highlight",
                                               :fail       "unhighlight",
                                               :transition "red-box"
                                               :params     {:target   "red-box"
                                                            :variable "red-box-selected"
                                                            }
                                               }
                                              ]

                                       }

                 :init-activity       {:type "sequence-data"
                                       :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-crayons"}
                                              {:type "action" :id "intro"}]
                                       }
                 :start-drag {:type "sequence-data"
                              :data [
                                     {:type "set-variable", :var-name "yellow-box-selected", :var-value false}
                                     {:type "set-variable", :var-name "blue-box-selected", :var-value false}
                                     {:type "set-variable", :var-name "red-box-selected", :var-value false}
                                     {:type     "set-interval"
                                      :id       "check-collide-2"
                                      :interval 100
                                      :action   "check-collide"}
                                     ]
                              }
                 :intro               {:type               "sequence-data",
                                       :editor-type        "dialog",
                                       :data               [{:type "sequence-data"
                                                             :data [{:type "empty" :duration 0}
                                                                    {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                       :phrase             "intro",
                                       :phrase-description "Introduce task"}
                 :correct-answer      {:type               "sequence-data",
                                       :editor-type        "dialog",
                                       :data               [{:type "sequence-data"
                                                             :data [{:type "empty" :duration 0}
                                                                    {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                       :phrase             "correct-answer",
                                       :phrase-description "correct answer"}
                 :wrong-answer        {:type               "sequence-data",
                                       :editor-type        "dialog",
                                       :data               [{:type "sequence-data"
                                                             :data [{:type "empty" :duration 0}
                                                                    {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                       :phrase             "wrong-answer",
                                       :phrase-description "wrong answer"}
                 :finish-scene        {:type "sequence-data",
                                       :data [{:type "action" :id "finish-dialog"}
                                              {:type "remove-interval"
                                               :id   "check-collide-2"}
                                              {:type "action", :id "stop-activity"}
                                              ],
                                       }
                 :finish-dialog       {:type               "sequence-data",
                                       :editor-type        "dialog",
                                       :data               [{:type "sequence-data"
                                                             :data [{:type "empty" :duration 0}
                                                                    {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                       :phrase             "finish-dialog",
                                       :phrase-description "finish dialog"}
                 :continue-sorting    {:type               "sequence-data",
                                       :editor-type        "dialog",
                                       :data               [{:type "sequence-data"
                                                             :data [{:type "empty" :duration 0}
                                                                    {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                       :phrase             "continue-sorting",
                                       :phrase-description "Continue sorting"}
                 :stop-activity       {:type "stop-activity", :id "categorize"},

                 },

 :triggers
                {:back  {:on "back", :action "stop-activity"},
                 :start {:on "start", :action "init-activity"}},
 :metadata      {:autostart true
                 :tracks [{:title "Round 2"
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