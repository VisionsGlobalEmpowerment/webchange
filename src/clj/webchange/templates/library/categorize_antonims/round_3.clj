(ns webchange.templates.library.categorize-antonims.round-3)

(def template-round-3 {:assets        [{:url "/raw/img/categorize-antonims/background.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/right.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/quiet.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/in.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/day.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/front.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/down.png", :size 10, :type "image"}

                                       {:url "/raw/img/categorize-antonims/night.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/left.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/loud.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/back.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/out.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/up.png", :size 10, :type "image"}

                                       ],
                       :objects       {:background       {:type "background", :src "/raw/img/categorize-antonims/background.png"},
                                       :left-right-group {
                                                          :type       "group",
                                                          :children   [],
                                                          :draggable  true,
                                                          :transition "left-right-group",
                                                          :states     {:group-all {:children ["right" "left-object"]}
                                                                       :ungroup   {:children []}
                                                                       :hidden    {:visible false}},

                                                          :actions    {:drag-start {
                                                                                    :type   "action",
                                                                                    :on     "drag-start",
                                                                                    :id     "start-drag",
                                                                                    :params {:colliders ["quiet" "in"
                                                                                                         "down" "front"
                                                                                                         "day"]
                                                                                             :self      "left-right-group"
                                                                                             :object    "left-object"
                                                                                             }}
                                                                       :drag-end   {
                                                                                    :id     "stop-drag-hide",
                                                                                    :on     "drag-end",
                                                                                    :type   "action",
                                                                                    :params {
                                                                                             :colliders     ["quiet" "in"
                                                                                                             "down" "front"
                                                                                                             "day"]
                                                                                             :self          "left-right-group"
                                                                                             :target        "right"
                                                                                             :object        "left-object"
                                                                                             :init-position {:x        100,
                                                                                                             :y        600,
                                                                                                             :duration 1}}}},

                                                          }
                                       :day-night-group  {
                                                          :type       "group",
                                                          :children   [],
                                                          :draggable  true,
                                                          :transition "day-night-group",
                                                          :states     {:group-all {:children ["day" "night-object"]}
                                                                       :ungroup   {:children []}
                                                                       :hidden    {:visible false}},

                                                          :actions    {:drag-start {
                                                                                    :type   "action",
                                                                                    :on     "drag-start",
                                                                                    :id     "start-drag",
                                                                                    :params {:colliders ["quiet" "right"
                                                                                                         "down" "front"
                                                                                                         "in"]
                                                                                             :self      "day-night-group"
                                                                                             :object    "night-object"
                                                                                             }}
                                                                       :drag-end   {
                                                                                    :id     "stop-drag-hide",
                                                                                    :on     "drag-end",
                                                                                    :type   "action",
                                                                                    :params {
                                                                                             :colliders     ["quiet" "right"
                                                                                                             "down" "front"
                                                                                                             "in"]
                                                                                             :self          "day-night-group"
                                                                                             :target        "day"
                                                                                             :object        "night-object"
                                                                                             :init-position {:x        100,
                                                                                                             :y        600,
                                                                                                             :duration 1}}}},

                                                          }
                                       :back-front-group {
                                                          :type       "group",
                                                          :children   [],
                                                          :draggable  true,
                                                          :transition "back-front-group",
                                                          :states     {:group-all {:children ["front" "back-object"]}
                                                                       :ungroup   {:children []}
                                                                       :hidden    {:visible false}},
                                                          :actions    {:drag-start {
                                                                                    :type   "action",
                                                                                    :on     "drag-start",
                                                                                    :id     "start-drag",
                                                                                    :params {:colliders ["down" "day"
                                                                                                         "quiet" "right"
                                                                                                         "in"]
                                                                                             :self      "back-front-group"}}
                                                                       :drag-end   {
                                                                                    :id     "stop-drag-hide",
                                                                                    :on     "drag-end",
                                                                                    :type   "action",
                                                                                    :params {
                                                                                             :colliders     ["down" "day"
                                                                                                             "quiet" "right"
                                                                                                             "in"]
                                                                                             :self          "back-front-group"
                                                                                             :target        "front"
                                                                                             :object        "back-object"
                                                                                             :init-position {:x        100,
                                                                                                             :y        600,
                                                                                                             :duration 1}}}},
                                                          }
                                       :out-in-group       {
                                                            :type       "group",
                                                            :children   [],
                                                            :draggable  true,
                                                            :transition "out-in-group",
                                                            :states     {:group-all {:children ["in" "out-object"]}
                                                                         :ungroup   {:children []}
                                                                         :hidden    {:visible false}},

                                                            :actions    {:drag-start {
                                                                                      :type   "action",
                                                                                      :on     "drag-start",
                                                                                      :id     "start-drag",
                                                                                      :params {:colliders ["quiet" "right"
                                                                                                           "down" "front"
                                                                                                           "day"]
                                                                                               :self      "out-in-group"}}
                                                                         :drag-end   {
                                                                                      :id     "stop-drag-hide",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {
                                                                                               :colliders     ["quiet" "right"
                                                                                                               "down" "front"
                                                                                                               "day"]
                                                                                               :self          "out-in-group"
                                                                                               :target        "in"
                                                                                               :init-position {:x        100,
                                                                                                               :y        600,
                                                                                                               :duration 1}}}},
                                                            }

                                       :in               {:type       "image",
                                                          :x          786,
                                                          :y          874,
                                                          :width      428,
                                                          :height     549,
                                                          :scale      0.5,
                                                          :src        "/raw/img/categorize-antonims/in.png",
                                                          :transition "in",
                                                          :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                       :init-position {:x 786, :y 874, :visible true}
                                                                       },
                                                          },
                                       :quiet            {:type       "image",
                                                          :x          320,
                                                          :y          874,
                                                          :width      428,
                                                          :height     549,
                                                          :scale      0.5,
                                                          :transition "quiet",
                                                          :src        "/raw/img/categorize-antonims/quiet.png",
                                                          :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                          }
                                       :right            {:type       "image",
                                                          :x          1252,
                                                          :y          874,
                                                          :width      428,
                                                          :height     549,
                                                          :scale      0.5,
                                                          :transition "right",
                                                          :src        "/raw/img/categorize-antonims/right.png",
                                                          :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                          },
                                       :left-object      {
                                                          :type       "image",
                                                          :x          100,
                                                          :y          600,
                                                          :width      100,
                                                          :height     224,
                                                          :rotation   -90,
                                                          :scale      0.35,
                                                          :src        "/raw/img/categorize-antonims/left.png",
                                                          :transition "left-object",
                                                          :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                       :hidden        {:visible false}, :init-position {:x 100, :y 600,}
                                                                       :not-draggable {:draggable false}, :draggable {:draggable true}
                                                                       },
                                                          :draggable  true,
                                                          :actions    {:drag-start {
                                                                                    :type   "action",
                                                                                    :on     "drag-start",
                                                                                    :id     "start-drag",
                                                                                    :params {:colliders ["in" "quiet" "right"
                                                                                                         "down" "front"
                                                                                                         "day"]
                                                                                             :self      "left-object"}}
                                                                       :drag-end   {
                                                                                    :id     "stop-drag-hide",
                                                                                    :on     "drag-end",
                                                                                    :type   "action",
                                                                                    :params {
                                                                                             :colliders     ["in" "quiet" "right"
                                                                                                             "down" "front"
                                                                                                             "day"]
                                                                                             :self          "left-object"
                                                                                             :target        "right"
                                                                                             :init-position {:x        100,
                                                                                                             :y        600,
                                                                                                             :duration 1}}}},},
                                       :up-object        {
                                                          :type       "image",
                                                          :x          200,
                                                          :y          700,
                                                          :width      100,
                                                          :height     224,
                                                          :rotation   -90,
                                                          :scale      0.35,
                                                          :src        "/raw/img/categorize-antonims/up.png",
                                                          :transition "up-object",
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
                                                                                    :params {:colliders ["in" "quiet" "right"
                                                                                                         "down" "front"
                                                                                                         "day"]
                                                                                             :self      "up-object"}}
                                                                       :drag-end   {
                                                                                    :id     "stop-drag-hide",
                                                                                    :on     "drag-end",
                                                                                    :type   "action",
                                                                                    :params {
                                                                                             :colliders     ["in" "quiet" "right"
                                                                                                             "down" "front"
                                                                                                             "day"]
                                                                                             :self          "up-object"
                                                                                             :target        "down"
                                                                                             :init-position {:x        200,
                                                                                                             :y        700,
                                                                                                             :duration 1}}}}
                                                          },
                                       :out-object       {
                                                          :type       "image",
                                                          :x          150,
                                                          :y          800,
                                                          :width      100,
                                                          :height     224,
                                                          :rotation   -90,
                                                          :scale      0.35,
                                                          :src        "/raw/img/categorize-antonims/out.png",
                                                          :transition "out-object",
                                                          :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                       :hidden        {:visible false}, :init-position {:x 150, :y 800, :visible true}
                                                                       :not-draggable {:draggable false}, :draggable {:draggable true}
                                                                       },
                                                          :draggable  true,
                                                          :actions    {:drag-start {
                                                                                    :type   "action",
                                                                                    :on     "drag-start",
                                                                                    :id     "start-drag",
                                                                                    :params {:colliders ["in" "quiet" "right"
                                                                                                         "down" "front"
                                                                                                         "day"]
                                                                                             :self      "out-object"}}
                                                                       :drag-end   {
                                                                                    :id     "stop-drag-hide",
                                                                                    :on     "drag-end",
                                                                                    :type   "action",
                                                                                    :params {
                                                                                             :colliders     ["in" "quiet" "right"
                                                                                                             "down" "front"
                                                                                                             "day"]
                                                                                             :self          "out-object"
                                                                                             :target        "in"
                                                                                             :init-position {:x        150,
                                                                                                             :y        800,
                                                                                                             :duration 1}}}}
                                                          },
                                       :night-object     {
                                                          :type       "image",
                                                          :x          550,
                                                          :y          800,
                                                          :width      100,
                                                          :height     224,
                                                          :rotation   -90,
                                                          :scale      0.35,
                                                          :src        "/raw/img/categorize-antonims/night.png",
                                                          :transition "night-object",
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
                                                                                    :params {:colliders ["in" "quiet" "right"
                                                                                                         "down" "front"
                                                                                                         "day"]
                                                                                             :self      "night-object"}}
                                                                       :drag-end   {
                                                                                    :id     "stop-drag-hide",
                                                                                    :on     "drag-end",
                                                                                    :type   "action",
                                                                                    :params {
                                                                                             :colliders     ["in" "quiet" "right"
                                                                                                             "down" "front"
                                                                                                             "day"]
                                                                                             :self          "night-object"
                                                                                             :target        "day"
                                                                                             :init-position {:x        550,
                                                                                                             :y        800,
                                                                                                             :duration 1}}}}
                                                          },
                                       :loud-object      {
                                                          :type       "image",
                                                          :x          46,
                                                          :y          650,
                                                          :width      100,
                                                          :height     224,
                                                          :rotation   -90,
                                                          :scale      0.35,
                                                          :src        "/raw/img/categorize-antonims/loud.png",
                                                          :transition "loud-object",
                                                          :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                       :hidden        {:visible false}, :init-position {:x 46, :y 650,}
                                                                       :not-draggable {:draggable false}, :draggable {:draggable true}
                                                                       },
                                                          :draggable  true,
                                                          :actions    {:drag-start {
                                                                                    :type   "action",
                                                                                    :on     "drag-start",
                                                                                    :id     "start-drag",
                                                                                    :params {:colliders ["in" "quiet" "right"
                                                                                                         "down" "front"
                                                                                                         "day"]
                                                                                             :self      "loud-object"}}
                                                                       :drag-end   {
                                                                                    :id     "stop-drag-hide",
                                                                                    :on     "drag-end",
                                                                                    :type   "action",
                                                                                    :params {
                                                                                             :colliders     ["in" "quiet" "right"
                                                                                                             "down" "front"
                                                                                                             "day"]
                                                                                             :self          "loud-object"
                                                                                             :target        "quiet"
                                                                                             :init-position {:x        46,
                                                                                                             :y        650,
                                                                                                             :duration 1}}}}
                                                          },
                                       :back-object      {
                                                          :type       "image",
                                                          :x          350,
                                                          :y          800,
                                                          :width      100,
                                                          :height     224,
                                                          :rotation   -90,
                                                          :scale      0.35,
                                                          :src        "/raw/img/categorize-antonims/back.png",
                                                          :transition "back-object",
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
                                                                                    :params {:colliders ["in" "quiet" "right"
                                                                                                         "down" "front" "day"]
                                                                                             :self      "back-object"}}
                                                                       :drag-end   {
                                                                                    :id     "stop-drag-hide",
                                                                                    :on     "drag-end",
                                                                                    :type   "action",
                                                                                    :params {
                                                                                             :colliders     ["in" "quiet" "right"
                                                                                                             "down" "front" "day"]
                                                                                             :self          "back-object"
                                                                                             :target        "front"
                                                                                             :init-position {:x        350,
                                                                                                             :y        800,
                                                                                                             :duration 1}}}}
                                                          },
                                       :down
                                       {:type       "image",
                                        :x          286,
                                        :y          374,
                                        :width      428,
                                        :height     549,
                                        :scale      0.5,
                                        :src        "/raw/img/categorize-antonims/down.png",
                                        :transition "down",
                                        :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                        }
                                       :day
                                       {:type       "image",
                                        :x          756,
                                        :y          374,
                                        :width      428,
                                        :height     549,
                                        :scale      0.5,
                                        :src        "/raw/img/categorize-antonims/day.png",
                                        :transition "day",
                                        :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                        }
                                       :front
                                       {:type       "image",
                                        :x          1086,
                                        :y          374,
                                        :width      428,
                                        :height     549,
                                        :scale      0.5,
                                        :src        "/raw/img/categorize-antonims/front.png",
                                        :transition "front",
                                        :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                        }

                                       },

                       :scene-objects [["background"]
                                       ["in" "quiet" "right"]
                                       ["down" "front" "day"]
                                       ["left-object" "up-object" "out-object" "night-object" "loud-object" "back-object"]
                                       ["day-night-group" "left-right-group" "out-in-group" "back-front-group"]
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
                                                                            {:type "set-variable", :var-name "object-1", :var-value "loud-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "quiet"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-loud-object" "colliding-quiet"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-3"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-2"}]}
                                       :task-3                      {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "out-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "in"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-out-object" "colliding-in"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-4"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-3"}]
                                                                     }
                                       :task-4                      {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "night-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "day"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-night-object" "colliding-day"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-5"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-4"}]
                                                                     }
                                       :task-5                      {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "back-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "front"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-back-object" "colliding-front"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-6"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-5"}]
                                                                     }
                                       :task-6                      {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "up-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "down"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-up-object" "colliding-down"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-7-1"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-6"}]
                                                                     }
                                       :task-7-1                    {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "left-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "right"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-left-object" "colliding-right"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-7-2"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-group"}
                                                                            {:type "set-variable", :var-name "group-name", :var-value "left-right-group"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-7-1"}]
                                                                     }
                                       :task-7-2                    {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "left-right-group"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "quiet"}
                                                                            {:type "set-variable", :var-name "ungroup-object-1", :var-value "right"}
                                                                            {:type "set-variable", :var-name "ungroup-object-2", :var-value "left-object"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-left-right-group" "colliding-quiet"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-8-1"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-init-ungroup"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-7-2"}]}
                                       :task-8-1                    {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "back-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "front"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-back-object" "colliding-front"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-8-2"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-group"}
                                                                            {:type "set-variable", :var-name "group-name", :var-value "back-front-group"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-8-1"}]}
                                       :task-8-2                    {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "back-front-group"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "down"}
                                                                            {:type "set-variable", :var-name "ungroup-object-1", :var-value "back-object"}
                                                                            {:type "set-variable", :var-name "ungroup-object-2", :var-value "front"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-back-front-group" "colliding-down"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-9-1"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-init-ungroup"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-8-2"}]
                                                                     }

                                       :task-9-1                    {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "night-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "day"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-night-object" "colliding-day"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-9-2"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-group"}
                                                                            {:type "set-variable", :var-name "group-name", :var-value "day-night-group"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-9-1"}]
                                                                     }

                                       :task-9-2                    {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "day-night-group"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "right"}
                                                                            {:type "set-variable", :var-name "ungroup-object-1", :var-value "day"}
                                                                            {:type "set-variable", :var-name "ungroup-object-2", :var-value "night-object"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-day-night-group" "colliding-right"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-10-1"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-init-ungroup"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-9-2"}]
                                                                     }
                                       :task-10-1                   {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "out-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "in"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-out-object" "colliding-in"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-10-2"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-group"}
                                                                            {:type "set-variable", :var-name "group-name", :var-value "out-in-group"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-10-1"}]
                                                                     }
                                       :task-10-2                   {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "out-in-group"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "right"}
                                                                            {:type "set-variable", :var-name "ungroup-object-1", :var-value "in"}
                                                                            {:type "set-variable", :var-name "ungroup-object-2", :var-value "out-object"}

                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-out-in-group" "colliding-right"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "finish"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-init-ungroup"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-10-2"}]
                                                                     }

                                       :start-activity              {:type "sequence-data",
                                                                     :data [{:type "action", :id "intro"}
                                                                            {:type "set-variable", :var-name "object-1", :var-value "left-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "right"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-left-object" "colliding-right"]}
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
                                                             :text "Put the left in its box."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-1}
                                                            {:type "prompt"
                                                             :text "Put the up on its table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-2}
                                                            {:type "prompt"
                                                             :text "Put the night on its table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-3}
                                                            {:type "prompt"
                                                             :text "Put the out in its box."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-4}
                                                            {:type "prompt"
                                                             :text "Put the loud in its box."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-5}
                                                            {:type "prompt"
                                                             :text "Put the back on its table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-6}
                                                            ]}
                                                   {:title "Round 3 - Second 4 tasks"
                                                    :nodes [{:type "prompt"
                                                             :text "Put the out in its box;"}
                                                            {:type      "dialog"
                                                             :action-id :instruction-7-1}
                                                            {:type "prompt"
                                                             :text "then put the whole box on the night table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-7-2}
                                                            {:type "prompt"
                                                             :text "Put the loud in its box; "}
                                                            {:type      "dialog"
                                                             :action-id :instruction-8-1}
                                                            {:type "prompt"
                                                             :text "then put the whole box on the back table."}
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
                                                             :text "Put the left in its box;"}
                                                            {:type      "dialog"
                                                             :action-id :instruction-10-1}
                                                            {:type "prompt"
                                                             :text "then put the whole box on the night table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-10-2}
                                                            ]
                                                    }
                                                   ]
                                       },
                       })