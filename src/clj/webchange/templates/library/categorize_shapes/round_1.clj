(ns webchange.templates.library.categorize-shapes.round-1
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(def template-round-1 {:assets        [
                                       {:url "/raw/img/categorize-shapes/background-white.png", :size 10, :type "image"}
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
                       :objects       {:background    {:type "background", :src "/raw/img/categorize-shapes/background-white.png"},
                                       :circle-box
                                                      {:type       "image",
                                                       :x          1572,
                                                       :y          777,
                                                       :width      277,
                                                       :height     250,
                                                       :src        "/raw/img/categorize-shapes/circle-box.png",
                                                       :transition "circle-box",
                                                       :states     {:highlighted     {:highlight true}
                                                                    :not-highlighted {:highlight false}},
                                                       },
                                       :oval-box      {:type       "image",
                                                       :x          69,
                                                       :y          777,
                                                       :width      277,
                                                       :height     250,
                                                       :transition "oval-box",
                                                       :src        "/raw/img/categorize-shapes/oval-box.png",
                                                       :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                       }
                                       :rectangle-box {:type       "image",
                                                       :x          369,
                                                       :y          777,
                                                       :width      277,
                                                       :height     250,
                                                       :transition "rectangle-box",
                                                       :src        "/raw/img/categorize-shapes/rectangle-box.png",
                                                       :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                       }
                                       :square-box    {:type       "image",
                                                       :x          670,
                                                       :y          777,
                                                       :width      277,
                                                       :height     250,
                                                       :transition "square-box",
                                                       :src        "/raw/img/categorize-shapes/square-box.png",
                                                       :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                       }
                                       :star-box      {:type       "image",
                                                       :x          971,
                                                       :y          777,
                                                       :width      277,
                                                       :height     250,
                                                       :transition "star-box",
                                                       :src        "/raw/img/categorize-shapes/star-box.png",
                                                       :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                       }
                                       :triangle-box  {:type       "image",
                                                       :x          1271,
                                                       :y          777,
                                                       :width      277,
                                                       :height     250,
                                                       :transition "triangle-box",
                                                       :src        "/raw/img/categorize-shapes/triangle-box.png",
                                                       :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                       }
                                       :circle-1      {
                                                       :type       "image",
                                                       :width      198,
                                                       :height     197,
                                                       :y          442,
                                                       :x          710,
                                                       :src        "/raw/img/categorize-shapes/circle.png",
                                                       :transition "circle-1",
                                                       :draggable  true,
                                                       :actions    {:drag-start {:type   "action",
                                                                                 :on     "drag-start",
                                                                                 :id     "start-drag"
                                                                                 :params {:say-item         "circle-item"
                                                                                          :placement-target "circle-box"}
                                                                                 }
                                                                    :drag-end
                                                                                {:id     "dragged",
                                                                                 :on     "drag-end",
                                                                                 :type   "action",
                                                                                 :params {:box            "circle-box"
                                                                                          :target         "circle-1"
                                                                                          :init-position  {:x 710, :y 442, :duration 1}
                                                                                          :check-variable "circle-box-selected"
                                                                                          }}},
                                                       :states     {:hide {:visible false}}},
                                       :oval-1        {
                                                       :type       "image",
                                                       :x          925,
                                                       :y          124,
                                                       :width      237,
                                                       :height     165,
                                                       :src        "/raw/img/categorize-shapes/oval.png",
                                                       :transition "oval-1",
                                                       :draggable  true,
                                                       :actions    {:drag-start {
                                                                                 :type   "action",
                                                                                 :on     "drag-start",
                                                                                 :id     "start-drag"
                                                                                 :params {:say-item         "oval-item"
                                                                                          :placement-target "oval-box"}
                                                                                 }
                                                                    :drag-end
                                                                                {:id     "dragged",
                                                                                 :on     "drag-end",
                                                                                 :type   "action",
                                                                                 :params {:box            "oval-box"
                                                                                          :target         "oval-1"
                                                                                          :init-position  {:x 925, :y 124, :duration 1}
                                                                                          :check-variable "oval-box-selected"
                                                                                          }}},
                                                       :states     {:hide {:visible false}}},
                                       :rectangle-1   {
                                                       :type       "image",
                                                       :width      256,
                                                       :height     180,
                                                       :x          617,
                                                       :y          146,
                                                       :src        "/raw/img/categorize-shapes/rectangle.png",
                                                       :transition "rectangle-1",
                                                       :draggable  true,
                                                       :actions    {:drag-start {
                                                                                 :type   "action",
                                                                                 :on     "drag-start",
                                                                                 :id     "start-drag"
                                                                                 :params {:say-item         "rectangle-item"
                                                                                          :placement-target "rectangle-box"}
                                                                                 }
                                                                    :drag-end
                                                                                {:id     "dragged",
                                                                                 :on     "drag-end",
                                                                                 :type   "action",
                                                                                 :params {:box            "rectangle-box"
                                                                                          :target         "rectangle-1"
                                                                                          :init-position  {:x 617, :y 146, :duration 1}
                                                                                          :check-variable "rectangle-box-selected"
                                                                                          }}},
                                                       :states     {:hide {:visible false}}}
                                       :square-1      {
                                                       :type       "image",
                                                       :x          982,
                                                       :y          360,
                                                       :width      180,
                                                       :height     180,
                                                       :src        "/raw/img/categorize-shapes/square.png",
                                                       :transition "square-1",
                                                       :draggable  true,
                                                       :actions    {:drag-start {
                                                                                 :type   "action",
                                                                                 :on     "drag-start",
                                                                                 :id     "start-drag"
                                                                                 :params {:say-item         "square-item"
                                                                                          :placement-target "square-box"}
                                                                                 }
                                                                    :drag-end
                                                                                {:id     "dragged",
                                                                                 :on     "drag-end",
                                                                                 :type   "action",
                                                                                 :params {:box            "square-box"
                                                                                          :target         "square-1"
                                                                                          :init-position  {:x 982, :y 360, :duration 1}
                                                                                          :check-variable "square-box-selected"
                                                                                          }}},
                                                       :states     {:hide {:visible false}}}
                                       :star-1        {
                                                       :type       "image",
                                                       :x          1214,
                                                       :y          289,
                                                       :width      245,
                                                       :height     233,
                                                       :src        "/raw/img/categorize-shapes/star.png",
                                                       :transition "star-1",
                                                       :draggable  true,
                                                       :actions    {:drag-start {
                                                                                 :type   "action",
                                                                                 :on     "drag-start",
                                                                                 :id     "start-drag"
                                                                                 :params {:say-item         "star-item"
                                                                                          :placement-target "star-box"}
                                                                                 }
                                                                    :drag-end
                                                                                {:id     "dragged",
                                                                                 :on     "drag-end",
                                                                                 :type   "action",
                                                                                 :params {:box            "star-box"
                                                                                          :target         "star-1"
                                                                                          :init-position  {:x 1214, :y 289, :duration 1}
                                                                                          :check-variable "star-box-selected"
                                                                                          }}},
                                                       :states     {:hide {:visible false}}}
                                       :triangle-1    {
                                                       :type       "image",
                                                       :x          424,
                                                       :y          377,
                                                       :width      223,
                                                       :height     193,
                                                       :src        "/raw/img/categorize-shapes/triangle.png",
                                                       :transition "triangle-1",
                                                       :draggable  true,
                                                       :actions    {:drag-start {
                                                                                 :type   "action",
                                                                                 :on     "drag-start",
                                                                                 :id     "start-drag"
                                                                                 :params {:say-item         "triangle-item"
                                                                                          :placement-target "triangle-box"}
                                                                                 }
                                                                    :drag-end
                                                                                {:id     "dragged",
                                                                                 :on     "drag-end",
                                                                                 :type   "action",
                                                                                 :params {:box            "triangle-box"
                                                                                          :target         "triangle-1"
                                                                                          :init-position  {:x 424, :y 377, :duration 1}
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

                                       :object-in-right-box {:id          "hide",
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
                                                             :data [{:type "remove-interval"
                                                                     :id   "check-collide-2"}
                                                                    {:type "counter" :counter-action "increase" :counter-id "sorted-objects"}
                                                                    {:id "object-in-right-box", :type "action"}
                                                                    {:id "correct-answer", :type "action"}
                                                                    {:type       "test-var-inequality"
                                                                     :var-name   "sorted-objects",
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
                                                                     :fail        "check-option",
                                                                     :value       true,
                                                                     :var-name "current-selection-state"}
                                                                    ]
                                                             }
                                       :highlight           {:type "sequence-data"
                                                             :data [{:type "action", :id "reset-selected-vars"}
                                                                    {:type        "set-variable",
                                                                     :var-value   true
                                                                     :from-params [{:action-property "var-name" :param-property "variable"}]
                                                                     }
                                                                    {:type        "state"
                                                                     :id          "highlighted"
                                                                     :from-params [{:action-property "target" :param-property "target"}]
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
                                                                     :from-params [{:action-property "target" :param-property "target"}]}]}
                                       :next-check-collide  {:type "sequence-data"
                                                             :data [{:type     "set-timeout"
                                                                     :action   "check-collide"
                                                                     :interval 10}]}
                                       :check-collide       {:type "sequence-data"
                                                             :data [
                                                                    {:type          "test-transitions-and-pointer-collide",
                                                                     :success       "highlight",
                                                                     :fail          "unhighlight",
                                                                     :transitions   ["oval-box" "circle-box"
                                                                                     "rectangle-box" "square-box"
                                                                                     "star-box" "triangle-box"]
                                                                     :action-params [{:target   "oval-box"
                                                                                      :variable "oval-box-selected"}
                                                                                     {:target   "circle-box"
                                                                                      :variable "circle-box-selected"}
                                                                                     {:target   "rectangle-box"
                                                                                      :variable "rectangle-box-selected"}
                                                                                     {:target   "square-box"
                                                                                      :variable "square-box-selected"}
                                                                                     {:target   "star-box"
                                                                                      :variable "star-box-selected"}
                                                                                     {:target   "triangle-box"
                                                                                      :variable "triangle-box-selected"}]}
                                                                    {:type     "test-var-scalar",
                                                                     :success  "next-check-collide",
                                                                     :value    true,
                                                                     :var-name "next-check-collide"}
                                                                    ]}


                                       :init-activity       {:type "sequence-data"
                                                             :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-objects"}
                                                                    {:type "action" :id "intro"}]
                                                             }
                                       :reset-selected-vars {:type "sequence-data"
                                                             :data [
                                                                    {:type "set-variable", :var-name "oval-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "circle-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "rectangle-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "square-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "star-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "triangle-box-selected", :var-value false}]}
                                       :oval-item           (dialog/default "oval")
                                       :circle-item         (dialog/default "circle")
                                       :rectangle-item      (dialog/default "rectangle")
                                       :square-item         (dialog/default "square")
                                       :star-item           (dialog/default "star")
                                       :triangle-item       (dialog/default "triangle")

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
                                                                                          {:type "animation-sequence",
                                                                                           :phrase-text "New action", :audio nil}]}],
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
                                                             :action-id :oval-item}
                                                            {:type      "dialog"
                                                             :action-id :circle-item}
                                                            {:type      "dialog"
                                                             :action-id :rectangle-item}
                                                            {:type      "dialog"
                                                             :action-id :square-item}
                                                            {:type      "dialog"
                                                             :action-id :star-item}
                                                            {:type      "dialog"
                                                             :action-id :triangle-item}
                                                            ]}
                                                   ]
                                       },
                       })