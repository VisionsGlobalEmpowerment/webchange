(ns webchange.templates.library.categorize-synonyms.round-1)

(def template-round-1 {:assets        [
                                       {:url "/raw/img/categorize-synonyms/background.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/scared.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/afraid.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/big.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/large.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/kid.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/child.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/happy.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/glad.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/chilly.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/cold.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/garbage.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/trash.png", :size 10, :type "image"}
                                       ],
                       :objects       {:background      {:type "background", :src "/raw/img/categorize-synonyms/background.png"},
                                       :cold-box
                                                        {:type       "image",
                                                         :x          15,
                                                         :y          800,
                                                         :src        "/raw/img/categorize-synonyms/cold.png",
                                                         :transition "cold-box",
                                                         :states     {:highlighted     {:highlight true}
                                                                      :not-highlighted {:highlight false}},
                                                         },
                                       :child-box        {:type       "image",
                                                          :x          250,
                                                          :y          800,
                                                          :transition "child-box",
                                                          :src        "/raw/img/categorize-synonyms/child.png",
                                                          :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                          }
                                       :big-box   {:type       "image",
                                                    :x          500,
                                                    :y          800,
                                                    :transition "big-box",
                                                    :src        "/raw/img/categorize-synonyms/big.png",
                                                    :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                    }
                                       :trash-box      {:type       "image",
                                                        :x          800,
                                                        :y          800,
                                                        :transition "trash-box",
                                                        :src        "/raw/img/categorize-synonyms/trash.png",
                                                        :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                        }
                                       :scared-box        {:type       "image",
                                                        :x          1100,
                                                        :y          800,
                                                        :transition "scared-box",
                                                        :src        "/raw/img/categorize-synonyms/scared.png",
                                                        :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                        }
                                       :happy-box    {:type       "image",
                                                   :x          1400,
                                                   :y          800,
                                                   :transition "happy-box",
                                                   :src        "/raw/img/categorize-synonyms/happy.png",
                                                   :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                   }
                                       :chilly-1        {
                                                       :type       "image",
                                                       :x          46,
                                                       :y          700,
                                                       :scale      0.35,
                                                       :src        "/raw/img/categorize-synonyms/chilly.png",
                                                       :transition "chilly-1",
                                                       :draggable  true,
                                                       :actions    {:drag-start {:type "action",
                                                                                 :on   "drag-start",
                                                                                 :id   "start-drag"}
                                                                    :drag-end
                                                                                {:id     "dragged",
                                                                                 :on     "drag-end",
                                                                                 :type   "action",
                                                                                 :params {:box            "cold-box"
                                                                                          :target         "chilly-1"
                                                                                          :init-position  {:x 46, :y 700, :duration 1}
                                                                                          :check-variable "cold-box-selected"
                                                                                          }}},
                                                       :states     {:hide {:visible false}}},
                                       :kid-1   {
                                                  :type       "image",
                                                  :x          592,
                                                  :y          500,
                                                  :rotation   -90,
                                                  :scale      0.35,
                                                  :src        "/raw/img/categorize-synonyms/kid.png",
                                                  :transition "kid-1",
                                                  :draggable  true,
                                                  :actions    {:drag-start {
                                                                            :type "action",
                                                                            :on   "drag-start",
                                                                            :id   "start-drag"}
                                                               :drag-end
                                                                           {:id     "dragged",
                                                                            :on     "drag-end",
                                                                            :type   "action",
                                                                            :params {:box            "child-box"
                                                                                     :target         "kid-1"
                                                                                     :init-position  {:x 592, :y 500, :duration 1}
                                                                                     :check-variable "child-box-selected"
                                                                                     }}},
                                                  :states     {:hide {:visible false}}},
                                       :large-1   {
                                                :type       "image",
                                                :x          392,
                                                :y          700,
                                                :rotation   -90,
                                                :scale      0.35,
                                                :src        "/raw/img/categorize-synonyms/large.png",
                                                :transition "large-1",
                                                :draggable  true,
                                                :actions    {:drag-start {
                                                                          :type "action",
                                                                          :on   "drag-start",
                                                                          :id   "start-drag"}
                                                             :drag-end
                                                                         {:id     "dragged",
                                                                          :on     "drag-end",
                                                                          :type   "action",
                                                                          :params {:box            "big-box"
                                                                                   :target         "large-1"
                                                                                   :init-position  {:x 392, :y 700, :duration 1}
                                                                                   :check-variable "big-box-selected"
                                                                                   }}},
                                                :states     {:hide {:visible false}}}
                                       :garbage-1   {
                                                  :type       "image",
                                                  :x          1192,
                                                  :y          700,
                                                  :rotation   -90,
                                                  :scale      0.35,
                                                  :src        "/raw/img/categorize-synonyms/garbage.png",
                                                  :transition "garbage-1",
                                                  :draggable  true,
                                                  :actions    {:drag-start {
                                                                            :type "action",
                                                                            :on   "drag-start",
                                                                            :id   "start-drag"}
                                                               :drag-end
                                                                           {:id     "dragged",
                                                                            :on     "drag-end",
                                                                            :type   "action",
                                                                            :params {:box            "trash-box"
                                                                                     :target         "garbage-1"
                                                                                     :init-position  {:x 1192, :y 700, :duration 1}
                                                                                     :check-variable "trash-box-selected"
                                                                                     }}},
                                                  :states     {:hide {:visible false}}}
                                       :afraid-1   {
                                                   :type       "image",
                                                   :x          992,
                                                   :y          200,
                                                   :rotation   -90,
                                                   :scale      0.35,
                                                   :src        "/raw/img/categorize-synonyms/afraid.png",
                                                   :transition "afraid-1",
                                                   :draggable  true,
                                                   :actions    {:drag-start {
                                                                             :type "action",
                                                                             :on   "drag-start",
                                                                             :id   "start-drag"}
                                                                :drag-end
                                                                            {:id     "dragged",
                                                                             :on     "drag-end",
                                                                             :type   "action",
                                                                             :params {:box            "scared-box"
                                                                                      :target         "afraid-1"
                                                                                      :init-position  {:x 992, :y 200, :duration 1}
                                                                                      :check-variable "scared-box-selected"
                                                                                      }}},
                                                   :states     {:hide {:visible false}}}
                                       :glad-1   {
                                                 :type       "image",
                                                 :x          392,
                                                 :y          200,
                                                 :rotation   -90,
                                                 :scale      0.35,
                                                 :src        "/raw/img/categorize-synonyms/glad.png",
                                                 :transition "glad-1",
                                                 :draggable  true,
                                                 :actions    {:drag-start {
                                                                           :type "action",
                                                                           :on   "drag-start",
                                                                           :id   "start-drag"}
                                                              :drag-end
                                                                          {:id     "dragged",
                                                                           :on     "drag-end",
                                                                           :type   "action",
                                                                           :params {:box            "happy-box"
                                                                                    :target         "glad-1"
                                                                                    :init-position  {:x 392, :y 200, :duration 1}
                                                                                    :check-variable "happy-box-selected"
                                                                                    }}},
                                                 :states     {:hide {:visible false}}}

                                       },
                       :scene-objects [["background"]
                                       ["cold-box" "child-box" "big-box"]
                                       ["trash-box" "scared-box" "happy-box"]
                                       ["chilly-1" "kid-1" "large-1"]
                                       ["garbage-1" "afraid-1" "glad-1"]],
                       :actions {

                                 :crayon-in-right-box            {:id          "hide",
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
                                                                  :data [{:type "remove-interval" :id   "check-collide-2"}
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
                                 :check-option                   {:type      "test-var-list-at-least-one-true"
                                                                  :var-names ["child-box-selected" "cold-box-selected" "big-box-selected"
                                                                              "trash-box-selected" "scared-box-selected" "happy-box-selected"]
                                                                  :success   "wrong-option",
                                                                  :fail      "object-revert"
                                                                  }
                                 :dragged                    {:type "sequence-data"
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
                                                         :transitions ["child-box" "cold-box" "big-box" "trash-box" "scared-box" "happy-box"]
                                                         :action-params [{:var-name "child-box"}
                                                                         {:var-name "cold-box"}
                                                                         {:var-name "big-box"}
                                                                         {:var-name "trash-box"}
                                                                         {:var-name "scared-box"}
                                                                         {:var-name  "happy-box"}]
                                                         }]}

                                 :init-activity                  {:type "sequence-data"
                                                                  :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-crayons"}
                                                                         {:type "action" :id "intro"}]
                                                                  }
                                 :reset-selected-vars {:type "sequence-data"
                                                       :data [
                                                              {:type "set-variable", :var-name "child-box-selected", :var-value false}
                                                              {:type "set-variable", :var-name "cold-box-selected", :var-value false}
                                                              {:type "set-variable", :var-name "big-box-selected", :var-value false}
                                                              {:type "set-variable", :var-name "trash-box-selected", :var-value false}
                                                              {:type "set-variable", :var-name "scared-box-selected", :var-value false}
                                                              {:type "set-variable", :var-name "happy-box-selected", :var-value false}
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
                       :metadata {:autostart   true
                                  :tracks      [{:title "Round 1"
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