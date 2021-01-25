(ns webchange.templates.library.categorize.round-1)

(def template-round-1 {:assets        [
                 {:url "/raw/img/categorize/background.png", :size 10, :type "image"}
                 {:url "/raw/img/categorize/yellow_box.png", :size 10, :type "image"}
                 {:url "/raw/img/categorize/blue_box.png", :size 10, :type "image"}
                 {:url "/raw/img/categorize/red_box.png", :size 10, :type "image"}
                 {:url "/raw/img/categorize/blue_crayons.png", :size 10, :type "image"}
                 {:url "/raw/img/categorize/red_crayons.png", :size 10, :type "image"}
                 {:url "/raw/img/categorize/yellow_crayons.png", :size 10, :type "image"}
                 ],
 :objects       {:background      {:type "background", :src "/raw/img/categorize/background.png"},
                 :yellow-box
                                  {:type       "image",
                                   :x          240,
                                   :y          664,
                                   :src        "/raw/img/categorize/yellow_box.png",
                                   :transition "yellow-box",
                                   :states     {:highlighted     {:highlight true}
                                                :not-highlighted {:highlight false}},
                                   },
                 :blue-box        {:type       "image",
                                   :x          746,
                                   :y          664,
                                   :transition "blue-box",
                                   :src        "/raw/img/categorize/blue_box.png",
                                   :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                   }
                 :red-box         {:type       "image",
                                   :x          1252,
                                   :y          664,
                                   :transition "red-box",
                                   :src        "/raw/img/categorize/red_box.png",
                                   :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                   }
                 :blue-crayon-1   {
                                   :type       "image",
                                   :x          184,
                                   :y          228,
                                   :rotation   -53,
                                   :src        "/raw/img/categorize/blue_crayons.png",
                                   :transition "blue-crayon-1",
                                   :draggable  true,
                                   :actions    {
                                                :drag-end
                                                {:id     "drag-crayon",
                                                 :on     "drag-end",
                                                 :type   "action",
                                                 :params {:target         "blue-crayon-1"
                                                          :init-position  {:x 184, :y 228, :duration 1}
                                                          :check-variable "blue-box-selected"
                                                          }}},
                                   :states     {:hide {:visible false}},
                                   }
                 :yellow-crayon-1 {
                                   :type       "image",
                                   :x          145,
                                   :y          521,
                                   :rotation   -129,
                                   :src        "/raw/img/categorize/yellow_crayons.png",
                                   :transition "yellow-crayon-1",
                                   :draggable  true,
                                   :actions    {:drag-end
                                                {:id     "drag-crayon",
                                                 :on     "drag-end",
                                                 :type   "action",
                                                 :params {:target         "yellow-crayon-1"
                                                          :init-position  {:x 145, :y 521, :duration 1}
                                                          :check-variable "yellow-box-selected"}}},
                                   :states     {:hide {:visible false}}
                                   }
                 :red-crayon-1    {
                                   :type       "image",
                                   :x          760,
                                   :y          64,
                                   :rotation   31,
                                   :src        "/raw/img/categorize/red_crayons.png",
                                   :transition "red-crayon-1",
                                   :draggable  true,
                                   :actions    {:drag-end
                                                {:id     "drag-crayon",
                                                 :on     "drag-end",
                                                 :type   "action",
                                                 :params {:target         "red-crayon-1"
                                                          :init-position  {:x 760, :y 64, :duration 1}
                                                          :check-variable "red-box-selected"
                                                          }}},
                                   :states     {:hide {:visible false}}
                                   }

                 :blue-crayon-2   {
                                   :type       "image",
                                   :x          1406,
                                   :y          456,
                                   :rotation   125,
                                   :src        "/raw/img/categorize/blue_crayons.png",
                                   :transition "blue-crayon-2",
                                   :draggable  true,
                                   :actions    {:drag-end
                                                {:id     "drag-crayon",
                                                 :on     "drag-end",
                                                 :type   "action",
                                                 :params {:target         "blue-crayon-2"
                                                          :init-position  {:x 1406, :y 456, :duration 1}
                                                          :check-variable "blue-box-selected"
                                                          }}},
                                   :states     {:hide {:visible false}}
                                   }
                 :yellow-crayon-2 {
                                   :type       "image",
                                   :x          1071,
                                   :y          279,
                                   :rotation   75,
                                   :src        "/raw/img/categorize/yellow_crayons.png",
                                   :transition "yellow-crayon-2",
                                   :draggable  true,
                                   :actions    {:drag-end
                                                {:id     "drag-crayon",
                                                 :on     "drag-end",
                                                 :type   "action",
                                                 :params {:target         "yellow-crayon-2"
                                                          :init-position  {:x 1071, :y 279, :duration 1}
                                                          :check-variable "yellow-box-selected"
                                                          }}},
                                   :states     {:hide {:visible false}}
                                   }
                 :red-crayon-2    {
                                   :type       "image",
                                   :x          736,
                                   :y          387,
                                   :rotation   -69,
                                   :src        "/raw/img/categorize/red_crayons.png",
                                   :transition "red-crayon-2",
                                   :draggable  true,
                                   :actions    {:drag-end
                                                {:id     "drag-crayon",
                                                 :on     "drag-end",
                                                 :type   "action",
                                                 :params {:target         "red-crayon-2"
                                                          :init-position  {:x 736, :y 387, :duration 1}
                                                          :check-variable "red-box-selected"
                                                          }}},
                                   :states     {:hide {:visible false}}
                                   }
                 :blue-crayon-3   {
                                   :type       "image",
                                   :x          1753,
                                   :y          144,
                                   :rotation   45,
                                   :src        "/raw/img/categorize/blue_crayons.png",
                                   :transition "blue-crayon-3",
                                   :draggable  true,
                                   :actions    {:drag-end
                                                {:id     "drag-crayon",
                                                 :on     "drag-end",
                                                 :type   "action",
                                                 :params {:target         "blue-crayon-3"
                                                          :init-position  {:x 1753, :y 144, :duration 1}
                                                          :check-variable "blue-box-selected"
                                                          }}},
                                   :states     {:hide {:visible false}}
                                   }
                 :yellow-crayon-3 {
                                   :type       "image",
                                   :x          1033,
                                   :y          558,
                                   :rotation   -120,
                                   :src        "/raw/img/categorize/yellow_crayons.png",
                                   :transition "yellow-crayon-3",
                                   :draggable  true,
                                   :actions    {:drag-end
                                                {:id     "drag-crayon",
                                                 :on     "drag-end",
                                                 :type   "action",
                                                 :params {:target         "yellow-crayon-3"
                                                          :init-position  {:x 1033, :y 558, :duration 1}
                                                          :check-variable "yellow-box-selected"
                                                          }}},
                                   :states     {:hide {:visible false}}
                                   }
                 :red-crayon-3    {
                                   :type       "image",
                                   :x          1097,
                                   :y          106,
                                   :rotation   -52,
                                   :src        "/raw/img/categorize/red_crayons.png",
                                   :transition "red-crayon-3",
                                   :draggable  true,
                                   :actions    {:drag-end
                                                {:id     "drag-crayon",
                                                 :on     "drag-end",
                                                 :type   "action",
                                                 :params {:target         "red-crayon-3"
                                                          :init-position  {:x 1097, :y 106, :duration 1}
                                                          :check-variable "red-box-selected"
                                                          }}},
                                   :states     {:hide {:visible false}}
                                   }
                 },
 :scene-objects [["background"]
                 ["yellow-box" "blue-box" "red-box"]
                 ["blue-crayon-1" "yellow-crayon-1" "red-crayon-1"]
                 ["red-crayon-2" "blue-crayon-2" "yellow-crayon-2"]
                 ["yellow-crayon-3" "red-crayon-3" "blue-crayon-3"]

                 ],
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
                                              ]
                                       }
                 :highlight           {:type "sequence-data"
                                       :data [{:type        "set-variable",
                                               :var-value   true
                                               :from-params [{:action-property "var-name" :param-property "check-variable"}]
                                               }
                                              {:type        "state"
                                               :id          "highlighted"
                                               :from-params [{:action-property "target" :param-property "target"}]
                                               }]
                                       }
                 :unhighlight         {:type "sequence-data"
                                       :data [{:type        "set-variable",
                                               :var-value   false
                                               :from-params [{:action-property "var-name" :param-property "check-variable"}]
                                               }
                                              {:type        "state"
                                               :id          "not-highlighted"
                                               :from-params [{:action-property "target" :param-property "target"}]}
                                              ]
                                       }
                 :check-collide       {:type "sequence-data"
                                       :data [
                                              {:type       "test-transition-and-pointer-collide",
                                               :success    "highlight",
                                               :fail       "unhighlight",
                                               :transition "yellow-box",
                                               :params     {:target         "yellow-box"
                                                            :check-variable "yellow-box-selected"
                                                            }
                                               }
                                              {:type       "test-transition-and-pointer-collide",
                                               :success    "highlight",
                                               :fail       "unhighlight",
                                               :transition "blue-box"
                                               :params     {:target         "blue-box"
                                                            :check-variable "blue-box-selected"
                                                            }
                                               }
                                              {:type       "test-transition-and-pointer-collide",
                                               :success    "highlight",
                                               :fail       "unhighlight",
                                               :transition "red-box"
                                               :params     {:target         "red-box"
                                                            :check-variable "red-box-selected"
                                                            }
                                               }
                                              ]

                                       }

                 :init-activity       {:type "sequence-data"
                                       :data [

                                              {:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-crayons"}
                                              {:type "set-variable", :var-name "yellow-box-selected", :var-value false}
                                              {:type "set-variable", :var-name "blue-box-selected", :var-value false}
                                              {:type "set-variable", :var-name "red-box-selected", :var-value false}
                                              {:type     "set-interval"
                                               :id       "check-collide-1"
                                               :interval 100
                                               :action   "check-collide"}
                                              {:type "action" :id "intro"}
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
                                               :id   "check-collide-1"}
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
                 :tracks [{:title "Round 1"
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