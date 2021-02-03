(ns webchange.templates.library.categorize-synonyms.round-3)

(def template-round-3 {:assets        [{:url "/raw/img/categorize-synonyms/background.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/cold.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/trash.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/happy.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/scared.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/child.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/big.png", :size 10, :type "image"}

                                       {:url "/raw/img/categorize-synonyms/afraid.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/chilly.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/garbage.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/kid.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/glad.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/large.png", :size 10, :type "image"}

                                       ],
                       :objects       {:background       {:type "background", :src "/raw/img/categorize-synonyms/background.png"},
                                       :large-big-group {
                                                         :type       "group",
                                                         :children   [],
                                                         :draggable  true,
                                                         :transition "large-big-group",
                                                         :states     {:group-all {:children ["big" "large-object"]}
                                                                      :ungroup   {:children []}
                                                                      :hidden    {:visible false}},

                                                         :actions    {:drag-start {
                                                                                   :type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag",
                                                                                   :params {:colliders ["trash" "happy"
                                                                                                        "cold" "child"
                                                                                                        "scared"]
                                                                                            :self      "large-big-group"
                                                                                            :object    "large-object"
                                                                                            }}
                                                                      :drag-end   {
                                                                                   :id     "stop-drag-hide",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {
                                                                                            :colliders     ["trash" "happy"
                                                                                                            "cold" "child"
                                                                                                            "scared"]
                                                                                            :self          "large-big-group"
                                                                                            :target        "trash"
                                                                                            :object        "large-object"
                                                                                            :init-position {:x        100,
                                                                                                            :y        600,
                                                                                                            :duration 1}}}},

                                                         }
                                       :scared-afraid-group  {
                                                              :type       "group",
                                                              :children   [],
                                                              :draggable  true,
                                                              :transition "scared-afraid-group",
                                                              :states     {:group-all {:children ["scared" "afraid-object"]}
                                                                           :ungroup   {:children []}
                                                                           :hidden    {:visible false}},

                                                              :actions    {:drag-start {
                                                                                        :type   "action",
                                                                                        :on     "drag-start",
                                                                                        :id     "start-drag",
                                                                                        :params {:colliders ["trash" "cold"
                                                                                                             "big" "child"
                                                                                                             "happy"]
                                                                                                 :self      "scared-afraid-group"
                                                                                                 :object    "afraid-object"
                                                                                                 }}
                                                                           :drag-end   {
                                                                                        :id     "stop-drag-hide",
                                                                                        :on     "drag-end",
                                                                                        :type   "action",
                                                                                        :params {
                                                                                                 :colliders     ["trash" "cold"
                                                                                                                 "big" "child"
                                                                                                                 "happy"]
                                                                                                 :self          "scared-afraid-group"
                                                                                                 :target        "scared"
                                                                                                 :object        "afraid-object"
                                                                                                 :init-position {:x        100,
                                                                                                                 :y        600,
                                                                                                                 :duration 1}}}},

                                                              }
                                       :kid-child-group {
                                                         :type       "group",
                                                         :children   [],
                                                         :draggable  true,
                                                         :transition "kid-child-group",
                                                         :states     {:group-all {:children ["child" "kid-object"]}
                                                                      :ungroup   {:children []}
                                                                      :hidden    {:visible false}},
                                                         :actions    {:drag-start {
                                                                                   :type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag",
                                                                                   :params {:colliders ["big" "scared"
                                                                                                        "trash" "cold"
                                                                                                        "happy"]
                                                                                            :self      "kid-child-group"}}
                                                                      :drag-end   {
                                                                                   :id     "stop-drag-hide",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {
                                                                                            :colliders     ["big" "scared"
                                                                                                            "trash" "cold"
                                                                                                            "happy"]
                                                                                            :self          "kid-child-group"
                                                                                            :target        "child"
                                                                                            :object        "kid-object"
                                                                                            :init-position {:x        100,
                                                                                                            :y        600,
                                                                                                            :duration 1}}}},
                                                         }
                                       :glad-happy-group       {
                                                                :type       "group",
                                                                :children   [],
                                                                :draggable  true,
                                                                :transition "glad-happy-group",
                                                                :states     {:group-all {:children ["happy" "glad-object"]}
                                                                             :ungroup   {:children []}
                                                                             :hidden    {:visible false}},

                                                                :actions    {:drag-start {
                                                                                          :type   "action",
                                                                                          :on     "drag-start",
                                                                                          :id     "start-drag",
                                                                                          :params {:colliders ["trash" "cold"
                                                                                                               "big" "child"
                                                                                                               "scared"]
                                                                                                   :self      "glad-happy-group"}}
                                                                             :drag-end   {
                                                                                          :id     "stop-drag-hide",
                                                                                          :on     "drag-end",
                                                                                          :type   "action",
                                                                                          :params {
                                                                                                   :colliders     ["trash" "cold"
                                                                                                                   "big" "child"
                                                                                                                   "scared"]
                                                                                                   :self          "glad-happy-group"
                                                                                                   :target        "happy"
                                                                                                   :init-position {:x        100,
                                                                                                                   :y        600,
                                                                                                                   :duration 1}}}},
                                                                }

                                       :happy               {:type       "image",
                                                             :x          786,
                                                             :y          874,
                                                             :width      428,
                                                             :height     549,
                                                             :scale      0.5,
                                                             :src        "/raw/img/categorize-synonyms/happy.png",
                                                             :transition "happy",
                                                             :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                          :init-position {:x 786, :y 874, :visible true}
                                                                          },
                                                             },
                                       :trash            {:type       "image",
                                                          :x          320,
                                                          :y          874,
                                                          :width      428,
                                                          :height     549,
                                                          :scale      0.5,
                                                          :transition "trash",
                                                          :src        "/raw/img/categorize-synonyms/trash.png",
                                                          :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                          }
                                       :cold            {:type       "image",
                                                         :x          1252,
                                                         :y          874,
                                                         :width      428,
                                                         :height     549,
                                                         :scale      0.5,
                                                         :transition "cold",
                                                         :src        "/raw/img/categorize-synonyms/cold.png",
                                                         :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                         },
                                       :chilly-object      {
                                                            :type       "image",
                                                            :x          100,
                                                            :y          600,
                                                            :width      100,
                                                            :height     224,
                                                            :rotation   -90,
                                                            :scale      0.35,
                                                            :src        "/raw/img/categorize-synonyms/chilly.png",
                                                            :transition "chilly-object",
                                                            :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                         :hidden        {:visible false}, :init-position {:x 100, :y 600,}
                                                                         :not-draggable {:draggable false}, :draggable {:draggable true}
                                                                         },
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type   "action",
                                                                                      :on     "drag-start",
                                                                                      :id     "start-drag",
                                                                                      :params {:colliders ["happy" "trash" "cold"
                                                                                                           "big" "child"
                                                                                                           "scared"]
                                                                                               :self      "chilly-object"}}
                                                                         :drag-end   {
                                                                                      :id     "stop-drag-hide",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {
                                                                                               :colliders     ["happy" "trash" "cold"
                                                                                                               "big" "child"
                                                                                                               "scared"]
                                                                                               :self          "chilly-object"
                                                                                               :target        "cold"
                                                                                               :init-position {:x        100,
                                                                                                               :y        600,
                                                                                                               :duration 1}}}},},
                                       :large-object        {
                                                             :type       "image",
                                                             :x          200,
                                                             :y          700,
                                                             :width      100,
                                                             :height     224,
                                                             :rotation   -90,
                                                             :scale      0.35,
                                                             :src        "/raw/img/categorize-synonyms/large.png",
                                                             :transition "large-object",
                                                             :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}
                                                                          :hidden      {:visible false}, :init-position {:x 200,
                                                                                                                         :y 700}
                                                                          :not-draggable {:draggable false}, :draggable {:draggable true}
                                                                          },
                                                             :draggable  true,
                                                             :actions    {:drag-start {
                                                                                       :type   "action",
                                                                                       :on     "drag-start",
                                                                                       :id     "start-drag",
                                                                                       :params {:colliders ["happy" "trash" "cold"
                                                                                                            "big" "child"
                                                                                                            "scared"]
                                                                                                :self      "large-object"}}
                                                                          :drag-end   {
                                                                                       :id     "stop-drag-hide",
                                                                                       :on     "drag-end",
                                                                                       :type   "action",
                                                                                       :params {
                                                                                                :colliders     ["happy" "trash" "cold"
                                                                                                                "big" "child"
                                                                                                                "scared"]
                                                                                                :self          "large-object"
                                                                                                :target        "big"
                                                                                                :init-position {:x        200,
                                                                                                                :y        700,
                                                                                                                :duration 1}}}}
                                                             },
                                       :glad-object       {
                                                           :type       "image",
                                                           :x          150,
                                                           :y          800,
                                                           :width      100,
                                                           :height     224,
                                                           :rotation   -90,
                                                           :scale      0.35,
                                                           :src        "/raw/img/categorize-synonyms/glad.png",
                                                           :transition "glad-object",
                                                           :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                        :hidden        {:visible false}, :init-position {:x 150, :y 800, :visible true}
                                                                        :not-draggable {:draggable false}, :draggable {:draggable true}
                                                                        },
                                                           :draggable  true,
                                                           :actions    {:drag-start {
                                                                                     :type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag",
                                                                                     :params {:colliders ["happy" "trash" "cold"
                                                                                                          "big" "child"
                                                                                                          "scared"]
                                                                                              :self      "glad-object"}}
                                                                        :drag-end   {
                                                                                     :id     "stop-drag-hide",
                                                                                     :on     "drag-end",
                                                                                     :type   "action",
                                                                                     :params {
                                                                                              :colliders     ["happy" "trash" "cold"
                                                                                                              "big" "child"
                                                                                                              "scared"]
                                                                                              :self          "glad-object"
                                                                                              :target        "happy"
                                                                                              :init-position {:x        150,
                                                                                                              :y        800,
                                                                                                              :duration 1}}}}
                                                           },
                                       :afraid-object     {
                                                           :type       "image",
                                                           :x          550,
                                                           :y          800,
                                                           :width      100,
                                                           :height     224,
                                                           :rotation   -90,
                                                           :scale      0.35,
                                                           :src        "/raw/img/categorize-synonyms/afraid.png",
                                                           :transition "afraid-object",
                                                           :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}
                                                                        :hidden      {:visible false}, :init-position {:x 550,
                                                                                                                       :y 800,}
                                                                        :not-draggable {:draggable false}, :draggable {:draggable true}
                                                                        },
                                                           :draggable  true,
                                                           :actions    {:drag-start {
                                                                                     :type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag",
                                                                                     :params {:colliders ["happy" "trash" "cold"
                                                                                                          "big" "child"
                                                                                                          "scared"]
                                                                                              :self      "afraid-object"}}
                                                                        :drag-end   {
                                                                                     :id     "stop-drag-hide",
                                                                                     :on     "drag-end",
                                                                                     :type   "action",
                                                                                     :params {
                                                                                              :colliders     ["happy" "trash" "cold"
                                                                                                              "big" "child"
                                                                                                              "scared"]
                                                                                              :self          "afraid-object"
                                                                                              :target        "scared"
                                                                                              :init-position {:x        550,
                                                                                                              :y        800,
                                                                                                              :duration 1}}}}
                                                           },
                                       :garbage-object      {
                                                             :type       "image",
                                                             :x          46,
                                                             :y          650,
                                                             :width      100,
                                                             :height     224,
                                                             :rotation   -90,
                                                             :scale      0.35,
                                                             :src        "/raw/img/categorize-synonyms/garbage.png",
                                                             :transition "garbage-object",
                                                             :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                          :hidden        {:visible false}, :init-position {:x 46, :y 650,}
                                                                          :not-draggable {:draggable false}, :draggable {:draggable true}
                                                                          },
                                                             :draggable  true,
                                                             :actions    {:drag-start {
                                                                                       :type   "action",
                                                                                       :on     "drag-start",
                                                                                       :id     "start-drag",
                                                                                       :params {:colliders ["happy" "trash" "cold"
                                                                                                            "big" "child"
                                                                                                            "scared"]
                                                                                                :self      "garbage-object"}}
                                                                          :drag-end   {
                                                                                       :id     "stop-drag-hide",
                                                                                       :on     "drag-end",
                                                                                       :type   "action",
                                                                                       :params {
                                                                                                :colliders     ["happy" "trash" "cold"
                                                                                                                "big" "child"
                                                                                                                "scared"]
                                                                                                :self          "garbage-object"
                                                                                                :target        "trash"
                                                                                                :init-position {:x        46,
                                                                                                                :y        650,
                                                                                                                :duration 1}}}}
                                                             },
                                       :kid-object      {
                                                         :type       "image",
                                                         :x          350,
                                                         :y          800,
                                                         :width      100,
                                                         :height     224,
                                                         :rotation   -90,
                                                         :scale      0.35,
                                                         :src        "/raw/img/categorize-synonyms/kid.png",
                                                         :transition "kid-object",
                                                         :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}
                                                                      :hidden      {:visible false}, :init-position {:x 350,
                                                                                                                     :y 800,}
                                                                      :not-draggable {:draggable false}, :draggable {:draggable true}
                                                                      },
                                                         :draggable  true,
                                                         :actions    {:drag-start {
                                                                                   :type   "action",
                                                                                   :on     "drag-start",
                                                                                   :id     "start-drag",
                                                                                   :params {:colliders ["happy" "trash" "cold"
                                                                                                        "big" "child" "scared"]
                                                                                            :self      "kid-object"}}
                                                                      :drag-end   {
                                                                                   :id     "stop-drag-hide",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {
                                                                                            :colliders     ["happy" "trash" "cold"
                                                                                                            "big" "child" "scared"]
                                                                                            :self          "kid-object"
                                                                                            :target        "child"
                                                                                            :init-position {:x        350,
                                                                                                            :y        800,
                                                                                                            :duration 1}}}}
                                                         },
                                       :big
                                       {:type       "image",
                                        :x          286,
                                        :y          374,
                                        :width      428,
                                        :height     549,
                                        :scale      0.5,
                                        :src        "/raw/img/categorize-synonyms/big.png",
                                        :transition "big",
                                        :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                        }
                                       :scared
                                       {:type       "image",
                                        :x          756,
                                        :y          374,
                                        :width      428,
                                        :height     549,
                                        :scale      0.5,
                                        :src        "/raw/img/categorize-synonyms/scared.png",
                                        :transition "scared",
                                        :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                        }
                                       :child
                                       {:type       "image",
                                        :x          1086,
                                        :y          374,
                                        :width      428,
                                        :height     549,
                                        :scale      0.5,
                                        :src        "/raw/img/categorize-synonyms/child.png",
                                        :transition "child",
                                        :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                        }

                                       },

                       :scene-objects [["background"]
                                       ["happy" "trash" "cold"]
                                       ["big" "child" "scared"]
                                       ["chilly-object" "large-object" "glad-object" "afraid-object" "garbage-object" "kid-object"]
                                       ["scared-afraid-group" "large-big-group" "glad-happy-group" "kid-child-group"]
                                       ],
                       :actions       {
                                       :object-revert               {:type        "state",
                                                                     :id          "init-position"
                                                                     :from-params [{:action-property "target" :param-property "self"}]
                                                                     }
                                       :correct-answer-group        {:type "sequence-data"
                                                                     :data [
                                                                            {
                                                                             :id       "group-all",
                                                                             :type     "state",
                                                                             :from-var [{:action-property "target" :var-name "group-name"}]
                                                                             }
                                                                            {:type        "state"
                                                                             :id          "not-draggable"
                                                                             :from-params [{:action-property "target" :param-property "self"}]}
                                                                            {:type "action"
                                                                             :id   "correct-answer-dialog"
                                                                             }
                                                                            {:type     "action"
                                                                             :from-var [{:var-name "next-task", :action-property "id"}]
                                                                             }
                                                                            ]
                                                                     }
                                       :correct-answer-single       {:type "sequence-data",
                                                                     :data [
                                                                            {:type        "state",
                                                                             :id          "init-position"
                                                                             :from-params [{:action-property "target" :param-property "self"}]
                                                                             }
                                                                            {:type "action"
                                                                             :id   "correct-answer-dialog"
                                                                             }
                                                                            {:type     "action"
                                                                             :from-var [{:var-name "next-task", :action-property "id"}]
                                                                             }
                                                                            ]
                                                                     },
                                       :correct-answer-init-ungroup {:type "sequence-data",
                                                                     :data [
                                                                            {
                                                                             :id       "ungroup",
                                                                             :type     "state",
                                                                             :from-var [{:action-property "target" :var-name "group-name"}]
                                                                             }
                                                                            {:type     "state"
                                                                             :id       "init-position"
                                                                             :from-var [{:var-name "ungroup-object-1", :action-property "target"}]
                                                                             }
                                                                            {:type     "state"
                                                                             :id       "init-position"
                                                                             :from-var [{:var-name "ungroup-object-2", :action-property "target"}]
                                                                             }
                                                                            {:type        "state"
                                                                             :id          "draggable"
                                                                             :from-params [{:action-property "target" :param-property "object"}]}
                                                                            {:type "action"
                                                                             :id   "correct-answer-dialog"
                                                                             }
                                                                            {:type     "action"
                                                                             :from-var [{:var-name "next-task", :action-property "id"}]
                                                                             }

                                                                            ]
                                                                     },
                                       :blink-objects               {:type "sequence-data"
                                                                     :data [
                                                                            {:type     "state"
                                                                             :id       "highlighted"
                                                                             :from-var [{:var-name "object-1", :action-property "target"}]
                                                                             }
                                                                            {:type     "state"
                                                                             :id       "highlighted"
                                                                             :from-var [{:var-name "object-2", :action-property "target"}]
                                                                             }
                                                                            {:type "empty", :duration 2000}
                                                                            {:type     "state"
                                                                             :id       "not-highlighted"
                                                                             :from-var [{:var-name "object-1", :action-property "target"}]
                                                                             }
                                                                            {:type     "state"
                                                                             :id       "not-highlighted"
                                                                             :from-var [{:var-name "object-2", :action-property "target"}]
                                                                             }
                                                                            ]
                                                                     },
                                       :wrong-answer                {:type "sequence-data"
                                                                     :data [{:type "action"
                                                                             :id   "wrong-answer-dialog"}
                                                                            {:type "action", :id "object-revert"}
                                                                            {:type           "counter"
                                                                             :counter-action "increase"
                                                                             :counter-id     "wrong-answers-counter"}
                                                                            {:type       "test-var-inequality"
                                                                             :var-name   "wrong-answers-counter",
                                                                             :value      2,
                                                                             :inequality ">=",
                                                                             :success    "blink-objects"
                                                                             }
                                                                            ]}
                                       :check-answer                {:type     "test-var-list",
                                                                     :fail     "wrong-answer",
                                                                     :values   [true true],
                                                                     :from-var [{:var-name "check-collide", :action-property "var-names"}
                                                                                {:var-name "correct-answer", :action-property "success"}]
                                                                     }
                                       :stop-drag-hide              {:type "sequence-data"
                                                                     :data [
                                                                            {:type        "test-var-list-at-least-one-true",
                                                                             :success     "check-answer",
                                                                             :fail        "object-revert"
                                                                             :from-params [{:template        "colliding-%"
                                                                                            :action-property "var-names" :param-property "colliders"}]}
                                                                            {:type "remove-interval"
                                                                             :id   "check-collide-3"}
                                                                            {
                                                                             :type        "mass-state"
                                                                             :id          "not-highlighted"
                                                                             :from-params [{:action-property "targets" :param-property "colliders"}]
                                                                             }

                                                                            ]

                                                                     },

                                       :start-drag                  {:type "sequence-data"
                                                                     :data [
                                                                            {:type     "set-variable-list"
                                                                             :values   [false false],
                                                                             :from-var [{:var-name "check-collide", :action-property "var-names"}]
                                                                             }
                                                                            {:type        "set-variable",
                                                                             :var-value   true
                                                                             :from-params [{
                                                                                            :template       "colliding-object-%",
                                                                                            :param-property "self", :action-property "var-name"}]
                                                                             }
                                                                            {:type     "set-interval"
                                                                             :id       "check-collide-3"
                                                                             :interval 100
                                                                             :action   "check-collide"}]},
                                       :check-collide               {:type "sequence-data"
                                                                     :data [
                                                                            {:type        "test-transitions-and-pointer-collide",
                                                                             :success     "highlight",
                                                                             :fail        "unhighlight",
                                                                             :from-params [{:param-property "colliders", :action-property "transitions"}]}]}
                                       :highlight                   {:type "sequence-data"
                                                                     :data [{:type        "set-variable",
                                                                             :var-value   true
                                                                             :from-params [{:action-property "var-name",
                                                                                            :template        "colliding-%",
                                                                                            :param-property  "transition"}]
                                                                             }
                                                                            {:type        "state"
                                                                             :id          "highlighted"
                                                                             :from-params [{:action-property "target" :param-property "transition"}]
                                                                             }]
                                                                     }
                                       :unhighlight                 {:type "sequence-data"
                                                                     :data [
                                                                            {:type        "set-variable",
                                                                             :var-value   false
                                                                             :from-params [{:action-property "var-name",
                                                                                            :template        "colliding-%",
                                                                                            :param-property  "transition"}]
                                                                             }
                                                                            {:type        "state"
                                                                             :id          "not-highlighted"
                                                                             :from-params [{:action-property "target" :param-property "transition"}]}]}

                                       :intro                       {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "continue-sorting",
                                                                     :phrase-description "Task introduction"
                                                                     }
                                       :instruction-1               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-1",
                                                                     :phrase-description "Action instructions"
                                                                     }
                                       :instruction-2               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-2",
                                                                     :phrase-description "Action instructions"
                                                                     }
                                       :instruction-3               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-3",
                                                                     :phrase-description "Action instructions"
                                                                     }
                                       :instruction-4               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-4",
                                                                     :phrase-description "Action instructions"
                                                                     }
                                       :instruction-5               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-5",
                                                                     :phrase-description "Action instructions"
                                                                     }
                                       :instruction-6               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-6",
                                                                     :phrase-description "Action instructions"
                                                                     }
                                       :instruction-7-1             {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-7-1",
                                                                     :phrase-description "First part of task"
                                                                     :dialog-track       "Second 4 tasks"
                                                                     }
                                       :instruction-7-2             {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-7-2",
                                                                     :phrase-description "Before second part of first task"
                                                                     :dialog-track       "Second 4 tasks"
                                                                     }
                                       :instruction-8-1             {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-8-1",
                                                                     :phrase-description "First part of task"
                                                                     :dialog-track       "Second 4 tasks"
                                                                     }
                                       :instruction-8-2             {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-8-2",
                                                                     :phrase-description "Before second part of second task"
                                                                     :dialog-track       "Second 4 tasks"
                                                                     }
                                       :instruction-9-1             {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-9-1",
                                                                     :phrase-description "First part of task"
                                                                     :dialog-track       "Second 4 tasks"
                                                                     }
                                       :instruction-9-2             {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-9-2",
                                                                     :phrase-description "Before second part of third task"
                                                                     :dialog-track       "Second 4 tasks"
                                                                     }
                                       :instruction-10-1            {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-10-1",
                                                                     :phrase-description "First part of task"
                                                                     :dialog-track       "Second 4 tasks"
                                                                     }
                                       :instruction-10-2            {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-10-2",
                                                                     :phrase-description "Before second part of fourth task"
                                                                     :dialog-track       "Second 4 tasks"
                                                                     }
                                       :stop-activity               {:type "stop-activity", :id "categorize"},
                                       :finish                      {:type "sequence-data",
                                                                     :data [
                                                                            {:type "action"
                                                                             :id   "finish-dialog"
                                                                             }
                                                                            {:type "action"
                                                                             :id   "stop-activity"
                                                                             }
                                                                            ]
                                                                     }
                                       :finish-dialog               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "Finish dialog",
                                                                     :phrase-description "Finish dialog"
                                                                     }

                                       :wrong-answer-dialog         {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "wrong-answer-dialog",
                                                                     :phrase-description "Wrong answer dialog"
                                                                     }
                                       :correct-answer-dialog       {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "correct-answer-dialog",
                                                                     :phrase-description "Correct answer dialog"
                                                                     }
                                       :task-2                      {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "garbage-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "trash"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-garbage-object" "colliding-trash"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-3"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-2"}]}
                                       :task-3                      {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "glad-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "happy"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-glad-object" "colliding-happy"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-4"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-3"}]
                                                                     }
                                       :task-4                      {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "afraid-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "scared"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-afraid-object" "colliding-scared"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-5"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-4"}]
                                                                     }
                                       :task-5                      {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "kid-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "child"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-kid-object" "colliding-child"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-6"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-5"}]
                                                                     }
                                       :task-6                      {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "large-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "big"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-large-object" "colliding-big"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-7-1"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-6"}]
                                                                     }
                                       :task-7-1                    {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "afraid-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "scared"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-afraid-object" "colliding-scared"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-7-2"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-group"}
                                                                            {:type "set-variable", :var-name "group-name", :var-value "scared-afraid-group"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-7-1"}]
                                                                     }
                                       :task-7-2                    {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "scared-afraid-group"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "happy"}
                                                                            {:type "set-variable", :var-name "ungroup-object-1", :var-value "scared"}
                                                                            {:type "set-variable", :var-name "ungroup-object-2", :var-value "afraid-object"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-scared-afraid-group" "colliding-happy"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-8-1"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-init-ungroup"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-7-2"}]}
                                       :task-8-1                    {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "kid-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "child"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-kid-object" "colliding-child"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-8-2"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-group"}
                                                                            {:type "set-variable", :var-name "group-name", :var-value "kid-child-group"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-8-1"}]}
                                       :task-8-2                    {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "kid-child-group"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "cold"}
                                                                            {:type "set-variable", :var-name "ungroup-object-1", :var-value "kid-object"}
                                                                            {:type "set-variable", :var-name "ungroup-object-2", :var-value "child"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-kid-child-group" "colliding-cold"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-9-1"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-init-ungroup"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-8-2"}]
                                                                     }

                                       :task-9-1                    {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "large-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "big"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-large-object" "colliding-big"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-9-2"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-group"}
                                                                            {:type "set-variable", :var-name "group-name", :var-value "large-big-group"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-9-1"}]
                                                                     }

                                       :task-9-2                    {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "large-big-group"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "trash"}
                                                                            {:type "set-variable", :var-name "ungroup-object-1", :var-value "big"}
                                                                            {:type "set-variable", :var-name "ungroup-object-2", :var-value "large-object"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-large-big-group" "colliding-trash"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-10-1"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-init-ungroup"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-9-2"}]
                                                                     }
                                       :task-10-1                   {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "glad-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "happy"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-glad-object" "colliding-happy"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-10-2"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-group"}
                                                                            {:type "set-variable", :var-name "group-name", :var-value "glad-happy-group"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-10-1"}]
                                                                     }
                                       :task-10-2                   {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "glad-happy-group"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "big"}
                                                                            {:type "set-variable", :var-name "ungroup-object-1", :var-value "happy"}
                                                                            {:type "set-variable", :var-name "ungroup-object-2", :var-value "glad-object"}

                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-glad-happy-group" "colliding-big"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "finish"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-init-ungroup"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-10-2"}]
                                                                     }

                                       :start-activity              {:type "sequence-data",
                                                                     :data [{:type "action", :id "intro"}
                                                                            {:type "set-variable", :var-name "object-1", :var-value "chilly-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "cold"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-chilly-object" "colliding-cold"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-2"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-1"}
                                                                            ]},
                                       }


                       :triggers      {:start {:on "start", :action "start-activity"}},
                       :metadata      {:autostart true
                                       :tracks    [
                                                   {:title "Round 3 - Intro and finish"
                                                    :nodes [{:type      "dialog"
                                                             :action-id :intro}
                                                            {:type      "dialog"
                                                             :action-id :finish-dialog}
                                                            ]}
                                                   {:title "Round 3 - Action result"
                                                    :nodes [{:type      "dialog"
                                                             :action-id :wrong-answer-dialog}
                                                            {:type      "dialog"
                                                             :action-id :correct-answer-dialog}
                                                            ]}
                                                   {:title "Round 3 - First 6 tasks"
                                                    :nodes [{:type "prompt"
                                                             :text "Put the chilly in its box."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-1}
                                                            {:type "prompt"
                                                             :text "Put the up on its table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-2}
                                                            {:type "prompt"
                                                             :text "Put the afraid on its table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-3}
                                                            {:type "prompt"
                                                             :text "Put the out in its box."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-4}
                                                            {:type "prompt"
                                                             :text "Put the garbage in its box."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-5}
                                                            {:type "prompt"
                                                             :text "Put the kid on its table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-6}
                                                            ]}
                                                   {:title "Round 3 - Second 4 tasks"
                                                    :nodes [{:type "prompt"
                                                             :text "Put the out in its box;"}
                                                            {:type      "dialog"
                                                             :action-id :instruction-7-1}
                                                            {:type "prompt"
                                                             :text "then put the whole box on the afraid table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-7-2}
                                                            {:type "prompt"
                                                             :text "Put the garbage in its box; "}
                                                            {:type      "dialog"
                                                             :action-id :instruction-8-1}
                                                            {:type "prompt"
                                                             :text "then put the whole box on the kid table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-8-2}
                                                            {:type "prompt"
                                                             :text "Put the out in its box;"}
                                                            {:type      "dialog"
                                                             :action-id :instruction-9-1}
                                                            {:type "prompt"
                                                             :text "then put the whole box on the up table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-9-2}
                                                            {:type "prompt"
                                                             :text "Put the chilly in its box;"}
                                                            {:type      "dialog"
                                                             :action-id :instruction-10-1}
                                                            {:type "prompt"
                                                             :text "then put the whole box on the afraid table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-10-2}
                                                            ]
                                                    }
                                                   ]
                                       },
                       })