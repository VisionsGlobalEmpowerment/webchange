(ns webchange.templates.library.categorize-synonyms.round-1
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(def template-round-1 {:assets        [
                                       {:url "/raw/img/categorize-synonyms/background.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/afraid.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/scared.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/big.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/large.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/child.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/kid.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/glad.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/happy.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/cold.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/chilly.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/garbage.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/trash.png", :size 10, :type "image"}
                                       ],
                       :objects       {:background {:type "background", :src "/raw/img/categorize-synonyms/background.png"},
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
                                       :kid-box    {:type       "image",
                                                    :width      253,
                                                    :height     252,
                                                    :x          1274,
                                                    :y          763,
                                                    :transition "kid-box",
                                                    :src        "/raw/img/categorize-synonyms/kid.png",
                                                    :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                    }
                                       :big-box    {:type       "image",
                                                    :width      252,
                                                    :height     252,
                                                    :x          393,
                                                    :y          763,
                                                    :transition "big-box",
                                                    :src        "/raw/img/categorize-synonyms/big.png",
                                                    :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                    }
                                       :trash-box  {:type       "image",
                                                    :width      253,
                                                    :height     253,
                                                    :x          1568,
                                                    :y          763, ;
                                                    :transition "trash-box",
                                                    :src        "/raw/img/categorize-synonyms/trash.png",
                                                    :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                    }
                                       :afraid-box {:type       "image",
                                                    :width      253,
                                                    :height     253,
                                                    :x          686,
                                                    :y          763,
                                                    :transition "afraid-box",
                                                    :src        "/raw/img/categorize-synonyms/afraid.png",
                                                    :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                    }
                                       :glad-box   {:type       "image",
                                                    :width      253,
                                                    :height     253,
                                                    :x          99,
                                                    :y          763,
                                                    :transition "glad-box",
                                                    :src        "/raw/img/categorize-synonyms/glad.png",
                                                    :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                    }
                                       :cold-1     {
                                                    :type       "image",
                                                    :width      252,
                                                    :height     253,
                                                    :x          701,
                                                    :y          89,
                                                    :src        "/raw/img/categorize-synonyms/cold.png",
                                                    :transition "cold-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {:type   "action",
                                                                              :on     "drag-start",
                                                                              :id     "start-drag"
                                                                              :params {:say-item         "cold-item"
                                                                                       :placement-target "chilly-box"}
                                                                              }
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "chilly-box"
                                                                                       :target         "cold-1"
                                                                                       :init-position  {:x 701,
                                                                                                        :y 89, :duration 1}
                                                                                       :check-variable "chilly-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}},
                                       :child-1    {
                                                    :type       "image",
                                                    :width      253,
                                                    :height     253,
                                                    :x          863,
                                                    :y          243,
                                                    :src        "/raw/img/categorize-synonyms/child.png",
                                                    :transition "child-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type   "action",
                                                                              :on     "drag-start",
                                                                              :id     "start-drag"
                                                                              :params {:say-item         "child-item"
                                                                                       :placement-target "kid-box"}
                                                                              }
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "kid-box"
                                                                                       :target         "child-1"
                                                                                       :init-position  {:x 863,
                                                                                                        :y 243, :duration 1}
                                                                                       :check-variable "kid-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}},
                                       :large-1    {
                                                    :type       "image",
                                                    :width      253,
                                                    :height     253,
                                                    :x          448,
                                                    :y          256,
                                                    :src        "/raw/img/categorize-synonyms/large.png",
                                                    :transition "large-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type   "action",
                                                                              :on     "drag-start",
                                                                              :id     "start-drag"
                                                                              :params {:say-item         "large-item"
                                                                                       :placement-target "big-box"}
                                                                              }
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "big-box"
                                                                                       :target         "large-1"
                                                                                       :init-position  {:x 448,
                                                                                                        :y 256, :duration 1}
                                                                                       :check-variable "big-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}}
                                       :garbage-1  {
                                                    :type       "image",
                                                    :width      253,
                                                    :height     253,
                                                    :x          1035,
                                                    :y          383,
                                                    :src        "/raw/img/categorize-synonyms/garbage.png",
                                                    :transition "garbage-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type   "action",
                                                                              :on     "drag-start",
                                                                              :id     "start-drag"
                                                                              :params {:say-item         "garbage-item"
                                                                                       :placement-target "trash-box"}
                                                                              }
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "trash-box"
                                                                                       :target         "garbage-1"
                                                                                       :init-position  {:x 1035,
                                                                                                        :y 383, :duration 1}
                                                                                       :check-variable "trash-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}}
                                       :scared-1   {
                                                    :type       "image",
                                                    :width      253,
                                                    :height     253,
                                                    :x          1121,
                                                    :y          105,
                                                    :src        "/raw/img/categorize-synonyms/scared.png",
                                                    :transition "scared-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type   "action",
                                                                              :on     "drag-start",
                                                                              :id     "start-drag"
                                                                              :params {:say-item         "scared-item"
                                                                                       :placement-target "afraid-box"}
                                                                              }
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "afraid-box"
                                                                                       :target         "scared-1"
                                                                                       :init-position  {:x 1121,
                                                                                                        :y 105, :duration 1}
                                                                                       :check-variable "afraid-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}}
                                       :happy-1    {
                                                    :type       "image",
                                                    :width      253,
                                                    :height     253,
                                                    :x          701,
                                                    :y          393,
                                                    :src        "/raw/img/categorize-synonyms/happy.png",
                                                    :transition "happy-1",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type   "action",
                                                                              :on     "drag-start",
                                                                              :id     "start-drag"
                                                                              :params {:say-item         "happy-item"
                                                                                       :placement-target "glad-box"}
                                                                              }
                                                                 :drag-end
                                                                             {:id     "dragged",
                                                                              :on     "drag-end",
                                                                              :type   "action",
                                                                              :params {:box            "glad-box"
                                                                                       :target         "happy-1"
                                                                                       :init-position  {:x 701,
                                                                                                        :y 393, :duration 1}
                                                                                       :check-variable "glad-box-selected"
                                                                                       }}},
                                                    :states     {:hide {:visible false}}}

                                       },
                       :scene-objects [["background"]
                                       ["chilly-box" "kid-box" "big-box"]
                                       ["trash-box" "afraid-box" "glad-box"]
                                       ["cold-1" "child-1" "large-1"]
                                       ["garbage-1" "scared-1" "happy-1"]],
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

                                       :dragged             {:type "sequence-data"
                                                             :data [
                                                                    {:type        "copy-variable",
                                                                     :var-name "current-selection-state"
                                                                     :from-params [{:param-property "check-variable", :action-property "from"}]}
                                                                    {:type "set-variable", :var-name "say", :var-value false}
                                                                    {:type "set-variable", :var-name "next-check-collide", :var-value false}
                                                                    {:type        "test-var-scalar",
                                                                     :success     "correct-option",
                                                                     :fail        "object-revert",
                                                                     :value       true,
                                                                     :var-name "current-selection-state"}
                                                                    {:type        "state"
                                                                     :id          "not-highlighted"
                                                                     :from-params [{:action-property "target" :param-property "box"}]}
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
                                                                     :from-params [{:template        "%-selected"
                                                                                    :action-property "var-name" :param-property "placement-target"}]
                                                                     }
                                                                    ]
                                                             }
                                       :unhighlight         {:type "sequence-data"
                                                             :data [{:type        "state"
                                                                     :id          "not-highlighted"
                                                                     :from-params [{:action-property "target" :param-property "transition"}]}]
                                                             }

                                       :next-check-collide  {:type "sequence-data"
                                                             :data [{:type     "set-timeout"
                                                                     :action   "check-collide"
                                                                     :interval 10}]}
                                       :check-collide       {:type "sequence-data"
                                                             :data [
                                                                    {:type          "test-transitions-and-pointer-collide",
                                                                     :success       "highlight",
                                                                     :fail          "unhighlight",
                                                                     :transitions   ["kid-box" "chilly-box" "big-box" "trash-box" "afraid-box" "glad-box"]
                                                                     :action-params [{:var-name "kid-box"}
                                                                                     {:var-name "chilly-box"}
                                                                                     {:var-name "big-box"}
                                                                                     {:var-name "trash-box"}
                                                                                     {:var-name "afraid-box"}
                                                                                     {:var-name "glad-box"}]
                                                                     }
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
                                                                    {:type "set-variable", :var-name "kid-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "chilly-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "big-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "trash-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "afraid-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "glad-box-selected", :var-value false}]}

                                       :cold-item           (dialog/default "cold")
                                       :child-item          (dialog/default "child")
                                       :large-item          (dialog/default "large")
                                       :garbage-item        (dialog/default "garbage")
                                       :scared-item         (dialog/default "scared")
                                       :happy-item          (dialog/default "happy")

                                       :say-item            {:type "sequence-data"
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
                                                             :action-id :cold-item}
                                                            {:type      "dialog"
                                                             :action-id :child-item}
                                                            {:type      "dialog"
                                                             :action-id :large-item}
                                                            {:type      "dialog"
                                                             :action-id :garbage-item}
                                                            {:type      "dialog"
                                                             :action-id :scared-item}
                                                            {:type      "dialog"
                                                             :action-id :happy-item}
                                                            ]}]
                                       },
                       })