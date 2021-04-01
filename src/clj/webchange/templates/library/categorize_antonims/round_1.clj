(ns webchange.templates.library.categorize-antonims.round-1
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(def template-round-1 {:assets        [
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
                                       ],
                       :objects       {:background {:type "background", :src "/raw/img/categorize-antonims/background.png"},
                                       :right-box
                                                   {:type       "image",
                                                    :width      252,
                                                    :height     253,
                                                    :y          763,
                                                    :x          99,
                                                    :src        "/raw/img/categorize-antonims/right.png",
                                                    :transition "right-box",
                                                    :states     {:highlighted     {:highlight true}
                                                                 :not-highlighted {:highlight false}},
                                                    },
                                       :front-box  {:type       "image",
                                                    :width      252,
                                                    :height     252,
                                                    :y          763,
                                                    :x          686,
                                                    :transition "front-box",
                                                    :src        "/raw/img/categorize-antonims/front.png",
                                                    :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                    }
                                       :down-box   {:type       "image",
                                                    :width      253,
                                                    :height     253,
                                                    :y          763,
                                                    :x          1568,
                                                    :transition "down-box",
                                                    :src        "/raw/img/categorize-antonims/down.png",
                                                    :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                    }
                                       :quiet-box  {:type       "image",
                                                    :width      253,
                                                    :height     253,
                                                    :y          763,
                                                    :x          393,
                                                    :transition "quiet-box",
                                                    :src        "/raw/img/categorize-antonims/quiet.png",
                                                    :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                    }
                                       :day-box    {:type       "image",
                                                    :width      253,
                                                    :height     253,
                                                    :y          763,
                                                    :x          980,
                                                    :transition "day-box",
                                                    :src        "/raw/img/categorize-antonims/day.png",
                                                    :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                    }
                                       :in-box     {:type       "image",
                                                    :width      253,
                                                    :height     252,
                                                    :y          763,
                                                    :x          1274,
                                                    :transition "in-box",
                                                    :src        "/raw/img/categorize-antonims/in.png",
                                                    :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                    }
                                       :left-1     {
                                                    :type       "image",
                                                    :width      253,
                                                    :height     252,
                                                    :y          256,
                                                    :x          448,
                                                    :src        "/raw/img/categorize-antonims/left.png",
                                                    :transition "left-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {:type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"
                                                                              :params {:say-item "left-item"
                                                                                       :placement-target "right-box"}
                                                                              }
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "right-box"
                                                                                       :target         "left-1"
                                                                                       :init-position  {:y 256,
                                                                                                        :x 448, :duration 1}
                                                                                       :check-variable "right-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}},
                                       :back-1     {
                                                    :type       "image",
                                                    :width      253,
                                                    :height     253,
                                                    :y          393,
                                                    :x          701,
                                                    :src        "/raw/img/categorize-antonims/back.png",
                                                    :transition "back-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"
                                                                              :params {:say-item "back-item"
                                                                                       :placement-target "front-box"}
                                                                              }
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "front-box"
                                                                                       :target         "back-1"
                                                                                       :init-position  {:y 393,
                                                                                                        :x 701, :duration 1}
                                                                                       :check-variable "front-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}},
                                       :up-1       {
                                                    :type       "image",
                                                    :width      252,
                                                    :height     253,
                                                    :x          1035,
                                                    :y          383,
                                                    :src        "/raw/img/categorize-antonims/up.png",
                                                    :transition "up-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"
                                                                              :params {:say-item "up-item"
                                                                                       :placement-target "down-box"}
                                                                              }
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "down-box"
                                                                                       :target         "up-1"
                                                                                       :init-position  {:x 1035,
                                                                                                        :y 383, :duration 1}
                                                                                       :check-variable "down-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}}
                                       :loud-1     {
                                                    :type       "image",
                                                    :width      252,
                                                    :height     253,
                                                    :y          89,
                                                    :x          701,
                                                    :src        "/raw/img/categorize-antonims/loud.png",
                                                    :transition "loud-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"
                                                                              :params {:say-item "loud-item"
                                                                                       :placement-target "quiet-box"}
                                                                              }
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "quiet-box"
                                                                                       :target         "loud-1"
                                                                                       :init-position  {:y 89,
                                                                                                        :x 701, :duration 1}
                                                                                       :check-variable "quiet-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}}
                                       :night-1    {
                                                    :type       "image",
                                                    :width      253,
                                                    :height     253,
                                                    :x          1121,
                                                    :y          105,
                                                    :src        "/raw/img/categorize-antonims/night.png",
                                                    :transition "night-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"
                                                                              :params {:say-item "night-item"
                                                                                       :placement-target "day-box"}
                                                                              }
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "day-box"
                                                                                       :target         "night-1"
                                                                                       :init-position  {:x 1121,
                                                                                                        :y 105, :duration 1}
                                                                                       :check-variable "day-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}}
                                       :out-1      {
                                                    :type       "image",
                                                    :width      253,
                                                    :height     253,
                                                    :x          863,
                                                    :y          243,
                                                    :src        "/raw/img/categorize-antonims/out.png",
                                                    :transition "out-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"
                                                                              :params {:say-item "out-item"
                                                                                       :placement-target "in-box"}
                                                                              }
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "in-box"
                                                                                       :target         "out-1"
                                                                                       :init-position  {:x 863,
                                                                                                        :y 243, :duration 1}
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

                                       :crayon-in-right-box {:id          "hide",
                                                             :type        "state",
                                                             :from-params [{:action-property "target" :param-property "target"}]},
                                       :object-revert       {:type        "transition",
                                                             :from-params [{:action-property "transition-id" :param-property "target"}
                                                                           {:action-property "to" :param-property "init-position"}]
                                                             }
                                       :wrong-option        {:type "sequence-data",
                                                             :data [{:id "object-revert", :type "action"}
                                                                    {:id "wrong-answer", :type "action"}],}

                                       :correct-option      {:type "sequence-data",
                                                             :data [{:type "remove-interval" :id "check-collide-2"}
                                                                    {:type "counter" :counter-action "increase" :counter-id "sorted-crayons"}
                                                                    {:id "crayon-in-right-box", :type "action"}
                                                                    {:id "correct-answer", :type "action"}
                                                                    {:type       "test-var-inequality"
                                                                     :var-name   "sorted-crayons",
                                                                     :value      6,
                                                                     :inequality ">=",
                                                                     :success    "finish-scene",
                                                                     :fail       "continue-sorting"
                                                                     }
                                                                    ]}
                                       :check-option        {:type      "test-var-list-at-least-one-true"
                                                             :var-names ["front-box-selected" "right-box-selected" "down-box-selected"
                                                                         "quiet-box-selected" "day-box-selected" "in-box-selected"]
                                                             :success   "wrong-option",
                                                             :fail      "object-revert"
                                                             }
                                       :dragged             {:type "sequence-data"
                                                             :data [
                                                                    {:type        "copy-variable",
                                                                     :var-name "current-selection-state"
                                                                     :from-params [{:param-property "check-variable", :action-property "from"}]}
                                                                    {:type "set-variable", :var-name "say", :var-value false}
                                                                    {:type "set-variable", :var-name "next-check-collide", :var-value false}

                                                                    {:type        "test-var-scalar",
                                                                     :success     "correct-option",
                                                                     :fail       "object-revert",
                                                                     :value       true,
                                                                     :var-name "current-selection-state"}
                                                                    ]
                                                             }
                                       :highlight           {:type "sequence-data"
                                                             :data [{:type "action", :id "reset-selected-vars"}
                                                                    {:type        "set-variable",
                                                                     :var-value   true
                                                                     :from-params [{:action-property "var-name",
                                                                                    :template        "%-selected",
                                                                                    :param-property  "var-name"}]
                                                                     }
                                                                    {:type        "state"
                                                                     :id          "highlighted"
                                                                     :from-params [{:action-property "target" :param-property "transition"}]
                                                                     }

                                                                    {:type        "test-var-scalar",
                                                                     :success     "wrong-answer",
                                                                     :value       false,
                                                                     :from-params [{:template "%-selected"
                                                                                    :action-property "var-name" :param-property "placement-target"}]
                                                                     }

                                                                    ]
                                                             }
                                       :unhighlight         {:type "sequence-data"
                                                             :data [{:type        "state"
                                                                     :id          "not-highlighted"
                                                                     :from-params [{:action-property "target" :param-property "transition"}]}]
                                                             }

                                       :next-check-collide            {:type "sequence-data"
                                                                       :data [{:type     "set-timeout"
                                                                               :action   "check-collide"
                                                                               :interval 10}]}
                                       :check-collide       {:type "sequence-data"
                                                             :data [
                                                                    {:type          "test-transitions-and-pointer-collide",
                                                                     :success       "highlight",
                                                                     :fail          "unhighlight",
                                                                     :transitions   ["front-box" "right-box" "down-box" "quiet-box" "day-box" "in-box"]
                                                                     :action-params [{:var-name "front-box"}
                                                                                     {:var-name "right-box"}
                                                                                     {:var-name "down-box"}
                                                                                     {:var-name "quiet-box"}
                                                                                     {:var-name "day-box"}
                                                                                     {:var-name "in-box"}]}
                                                                    {:type     "test-var-scalar",
                                                                     :success  "next-check-collide",
                                                                     :value    true,
                                                                     :var-name "next-check-collide"}
                                                                    ]}

                                       :init-activity       {:type "sequence-data"
                                                             :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-crayons"}
                                                                    {:type "action" :id "intro"}]
                                                             }
                                       :reset-selected-vars {:type "sequence-data"
                                                             :data [
                                                                    {:type "set-variable", :var-name "front-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "right-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "down-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "quiet-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "day-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "in-box-selected", :var-value false}
                                                                    ]
                                                             }
                                       :left-item        (dialog/default "Left")
                                       :back-item          (dialog/default "Back")
                                       :up-item           (dialog/default "Up")
                                       :loud-item        (dialog/default "Loud")
                                       :night-item        (dialog/default "Night")
                                       :out-item         (dialog/default "Out")

                                       :say-item           {:type "sequence-data"
                                                            :data [{:type "action" :from-params [{:action-property "id"
                                                                                                  :param-property  "say-item"}]}
                                                                   {:type     "test-var-scalar",
                                                                    :success  "next-say",
                                                                    :value    true,
                                                                    :var-name "say"}]}
                                       :next-say            {:type "sequence-data"
                                                             :data [{:type     "set-timeout"
                                                                     :action   "say-item"
                                                                     :interval 100}]}
                                       :start-drag          {:type "sequence-data"
                                                             :data [
                                                                    {:type "action", :id "reset-selected-vars"}
                                                                    {:type "set-variable", :var-name "say", :var-value true}
                                                                    {:type "set-variable", :var-name "next-check-collide", :var-value true}
                                                                    {:id "next-say" :type "action"}
                                                                    {:id "next-check-collide" :type "action"}
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
                                                             :data [
                                                                    {:type "action" :id "finish-dialog"}
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
                                       :tracks    [{:title "Round 1"
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
                                                   {:title "Round 1 - items"
                                                    :nodes [{:type      "dialog"
                                                             :action-id :left-item}
                                                            {:type      "dialog"
                                                             :action-id :back-item}
                                                            {:type      "dialog"
                                                             :action-id :up-item}
                                                            {:type      "dialog"
                                                             :action-id :loud-item}
                                                            {:type      "dialog"
                                                             :action-id :night-item}
                                                            {:type      "dialog"
                                                             :action-id :out-item}
                                                            ]}
                                                   ]
                                       },
                       })