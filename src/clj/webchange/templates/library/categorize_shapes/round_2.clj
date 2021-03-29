(ns webchange.templates.library.categorize-shapes.round-2
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(def template-round-2 {:assets        [
                                       {:url "/raw/img/categorize-shapes/background-class.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/surface.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/decoration.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/circle-group.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/oval-group.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/rectangle-group.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/square-group.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/star-group.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/triangle-group.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/circle-table.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/oval-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/rectangle-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/square-table.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/star-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/triangle-table.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/question.png", :size 10, :type "image"}
                                       ],
                       :objects       {:layered-background {:type       "layered-background",
                                                            :background {:src "/raw/img/categorize-shapes/background-class.png"},
                                                            :decoration {:src "/raw/img/categorize-shapes/decoration.png"},
                                                            :surface    {:src "/raw/img/categorize-shapes/surface.png"}
                                                            },
                                       :circle-table
                                                           {:type       "image",
                                                            :x          1222,
                                                            :y          650,
                                                            :width      429,
                                                            :height     221,
                                                            :src        "/raw/img/categorize-shapes/circle-table.png",
                                                            :transition "circle-table",
                                                            :states     {:highlighted     {:highlight true}
                                                                         :not-highlighted {:highlight false}},
                                                            },
                                       :oval-box           {:type       "image",
                                                            :x          663,
                                                            :y          835,
                                                            :width      222,
                                                            :height     200
                                                            :transition "oval-box",
                                                            :src        "/raw/img/categorize-shapes/oval-box.png",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                            }
                                       :rectangle-box      {:type       "image",
                                                            :x          922,
                                                            :y          835,
                                                            :width      222,
                                                            :height     200,
                                                            :transition "rectangle-box",
                                                            :src        "/raw/img/categorize-shapes/rectangle-box.png",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                            }
                                       :square-table       {:type       "image",
                                                            :x          691,
                                                            :y          665,
                                                            :width      512
                                                            :height     251
                                                            :transition "square-table",
                                                            :src        "/raw/img/categorize-shapes/square-table.png",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                            }
                                       :star-box           {:type       "image",
                                                            :x          1181,
                                                            :y          835,
                                                            :width      221,
                                                            :height     200,
                                                            :transition "star-box",
                                                            :src        "/raw/img/categorize-shapes/star-box.png",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                            }
                                       :triangle-table     {:type       "image",
                                                            :x          196,
                                                            :y          631,
                                                            :width      460,
                                                            :height     243
                                                            :transition "triangle-table",
                                                            :src        "/raw/img/categorize-shapes/triangle-table.png",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                            }
                                       :circle-1           {
                                                            :type       "image",
                                                            :x          1661,
                                                            :y          809,
                                                            :width      75,
                                                            :height     94,
                                                            :src        "/raw/img/categorize-shapes/circle-group.png",
                                                            :transition "circle-1",
                                                            :draggable  true,
                                                            :actions    {:drag-start {:type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"
                                                                                      :params {:say-item         "circle-item"
                                                                                               :placement-target "circle-table"}
                                                                                      }
                                                                         :drag-end
                                                                                     {:id     "dragged",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {:box            "circle-table"
                                                                                               :target         "circle-1"
                                                                                               :init-position  {:x 1661, :y 809, :duration 1}
                                                                                               :check-variable "circle-table-selected"
                                                                                               }}},
                                                            :states     {:hide {:visible false}}},
                                       :oval-1             {
                                                            :type       "image",
                                                            :x          169,
                                                            :y          49,
                                                            :width      75,
                                                            :height     94
                                                            :src        "/raw/img/categorize-shapes/oval-group.png",
                                                            :transition "oval-1",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"
                                                                                      :params {:say-item         "oval-item"
                                                                                               :placement-target "oval-box"}
                                                                                      }
                                                                         :drag-end
                                                                                     {:id     "dragged",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {:box            "oval-box"
                                                                                               :target         "oval-1"
                                                                                               :init-position  {:x 169, :y 49, :duration 1}
                                                                                               :check-variable "oval-box-selected"
                                                                                               }}},
                                                            :states     {:hide {:visible false}}},
                                       :rectangle-1        {
                                                            :type       "image",
                                                            :x          59,
                                                            :y          359,
                                                            :width      75,
                                                            :height     94,
                                                            :src        "/raw/img/categorize-shapes/rectangle-group.png",
                                                            :transition "rectangle-1",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"
                                                                                      :params {:say-item         "rectangle-item"
                                                                                               :placement-target "rectangle-box"}
                                                                                      }
                                                                         :drag-end
                                                                                     {:id     "dragged",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {:box            "rectangle-box"
                                                                                               :target         "rectangle-1"
                                                                                               :init-position  {:x 59, :y 359, :duration 1}
                                                                                               :check-variable "rectangle-box-selected"
                                                                                               }}},
                                                            :states     {:hide {:visible false}}}
                                       :square-1           {
                                                            :type       "image",
                                                            :x          1682,
                                                            :y          136,
                                                            :width      75,
                                                            :height     94,
                                                            :src        "/raw/img/categorize-shapes/square-group.png",
                                                            :transition "square-1",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"
                                                                                      :params {:say-item         "square-item"
                                                                                               :placement-target "square-table"}
                                                                                      }
                                                                         :drag-end
                                                                                     {:id     "dragged",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {:box            "square-table"
                                                                                               :target         "square-1"
                                                                                               :init-position  {:x 1682, :y 136, :duration 1}
                                                                                               :check-variable "square-table-selected"
                                                                                               }}},
                                                            :states     {:hide {:visible false}}}
                                       :star-1             {
                                                            :type       "image",
                                                            :x          826,
                                                            :y          402,
                                                            :width      75,
                                                            :height     94,
                                                            :src        "/raw/img/categorize-shapes/star-group.png",
                                                            :transition "star-1",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"
                                                                                      :params {:say-item         "star-item"
                                                                                               :placement-target "star-box"}
                                                                                      }
                                                                         :drag-end
                                                                                     {:id     "dragged",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {:box            "star-box"
                                                                                               :target         "star-1"
                                                                                               :init-position  {:x 826, :y 402, :duration 1}
                                                                                               :check-variable "star-box-selected"
                                                                                               }}},
                                                            :states     {:hide {:visible false}}}
                                       :triangle-1         {
                                                            :type       "image",
                                                            :x          126,
                                                            :y          936,
                                                            :width      75,
                                                            :height     94,
                                                            :src        "/raw/img/categorize-shapes/triangle-group.png",
                                                            :transition "triangle-1",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"
                                                                                      :params {:say-item         "triangle-item"
                                                                                               :placement-target "triangle-table"}
                                                                                      }
                                                                         :drag-end
                                                                                     {:id     "dragged",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {:box            "triangle-table"
                                                                                               :target         "triangle-1"
                                                                                               :init-position  {:x 126, :y 936, :duration 1}
                                                                                               :check-variable "triangle-table-selected"
                                                                                               }}},
                                                            :states     {:hide {:visible false}}}

                                       },
                       :scene-objects [["layered-background"]
                                       ["square-table" "triangle-table"]
                                       ["circle-table" "oval-box" "rectangle-box" "star-box"]
                                       ["circle-1" "oval-1" "rectangle-1"]
                                       ["square-1" "star-1" "triangle-1"]
                                       ],
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
                                       :highlight                      {:type "sequence-data"
                                                                        :data [{:type "action", :id "reset-selected-vars"}
                                                                               {:type        "set-variable",
                                                                                :var-value   true
                                                                                :from-params [{:action-property "var-name" :param-property "variable"}]
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
                                       :unhighlight                    {:type "sequence-data"
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
                                                                     :transitions   ["oval-box" "circle-table"
                                                                                     "rectangle-box" "square-table"
                                                                                     "star-box" "triangle-table"]
                                                                     :action-params [{:target   "oval-box"
                                                                                      :variable "oval-box-selected"}
                                                                                     {:target   "circle-table"
                                                                                      :variable "circle-table-selected"}
                                                                                     {:target   "rectangle-box"
                                                                                      :variable "rectangle-box-selected"}
                                                                                     {:target   "square-table"
                                                                                      :variable "square-table-selected"}
                                                                                     {:target   "star-box"
                                                                                      :variable "star-box-selected"}
                                                                                     {:target   "triangle-table"
                                                                                      :variable "triangle-table-selected"}]}
                                                                    {:type     "test-var-scalar",
                                                                     :success  "next-check-collide",
                                                                     :value    true,
                                                                     :var-name "next-check-collide"}
                                                                    ]}


                                       :init-activity                  {:type "sequence-data"
                                                                        :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-objects"}
                                                                               {:type "action" :id "intro"}]
                                                                        }
                                       :reset-selected-vars            {:type "sequence-data"
                                                                        :data [
                                                                               {:type "set-variable", :var-name "oval-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "circle-table-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "rectangle-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "square-table-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "star-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "triangle-table-selected", :var-value false}
                                                                               ]
                                                                        }
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

                                       :start-drag                     {:type "sequence-data"
                                                                        :data [
                                                                               {:type "action", :id "reset-selected-vars"}
                                                                               {:type "set-variable", :var-name "say", :var-value true}
                                                                               {:type "set-variable", :var-name "next-check-collide", :var-value true}
                                                                               {:id "next-say" :type "action"}
                                                                               {:id "next-check-collide" :type "action"}
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
                                                                        :phrase             "correct-answer-dialog",
                                                                        :phrase-description "Correct answer dialog"}
                                       :fail-answer-dialog             {:type               "sequence-data",
                                                                        :editor-type        "dialog",
                                                                        :data               [{:type "sequence-data"
                                                                                              :data [{:type "empty" :duration 0}
                                                                                                     {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                        :phrase             "fail-answer-dialog",
                                                                        :phrase-description "Fail answer dialog"}
                                       :finish-scene                   {:type "sequence-data",
                                                                        :data [
                                                                               {:type "action" :id "finish-dialog"}
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
                                                     {:title "Round 2 - items"
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