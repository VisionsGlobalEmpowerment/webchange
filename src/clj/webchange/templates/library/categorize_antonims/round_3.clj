(ns webchange.templates.library.categorize-antonims.round-3
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(def template-round-3 {:assets        [{:url "/raw/img/categorize-antonims/background-class.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/surface.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/decoration.png", :size 10, :type "image"}
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
                       :objects       {:layered-background {:type       "layered-background",
                                                            :background {:src "/raw/img/categorize-antonims/background-class.png"},
                                                            :decoration {:src "/raw/img/categorize-antonims/decoration.png"},
                                                            :surface    {:src "/raw/img/categorize-antonims/surface.png"}
                                                            },
                                       :left-right-group {
                                                          :type       "group",
                                                          :children   [],
                                                          :transition "left-right-group",
                                                          :states     {:group-all {:children ["right" "left-object"]}
                                                                       :ungroup   {:children []}
                                                                       :hidden    {:visible false}}}
                                       :day-night-group  {
                                                          :type       "group",
                                                          :children   [],
                                                          :transition "day-night-group",
                                                          :states     {:group-all {:children ["day" "night-object"]}
                                                                       :ungroup   {:children []}
                                                                       :hidden    {:visible false}}}
                                       :back-front-group {
                                                          :type       "group",
                                                          :children   [],
                                                          :transition "back-front-group",
                                                          :states     {:group-all {:children ["front" "back-object"]}
                                                                       :ungroup   {:children []}
                                                                       :hidden    {:visible false}}}
                                       :out-in-group     {
                                                          :type       "group",
                                                          :children   [],
                                                          :transition "out-in-group",
                                                          :states     {:group-all {:children ["in" "out-object"]}
                                                                       :ungroup   {:children []}
                                                                       :hidden    {:visible false}}}
                                       :left-object      {
                                                          :type       "image",
                                                          :width      160,
                                                          :height     160,
                                                          :x          1635,
                                                          :y          107,
                                                          :src        "/raw/img/categorize-antonims/left.png",
                                                          :transition "left-object",
                                                          :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                       :hidden        {:visible false}, :init-position {:x          1635,
                                                                                                                        :y          107,}
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
                                                                                             :say-item         "left-item"
                                                                                             :placement-target "right"
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
                                                                                             :init-position {:x          1635,
                                                                                                             :y          107,
                                                                                                             :duration 1}}}},},
                                       :up-object        {
                                                          :type       "image",
                                                          :width      160,
                                                          :height     160,
                                                          :x          801,
                                                          :y          481,
                                                          :src        "/raw/img/categorize-antonims/up.png",
                                                          :transition "up-object",
                                                          :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                       :hidden        {:visible false}, :init-position {:x          801,
                                                                                                                        :y          481,}
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
                                                                                             :say-item         "up-item"
                                                                                             :placement-target "down"
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
                                                          :width      160,
                                                          :height     160,
                                                          :x          1095,
                                                          :y          223,
                                                          :src        "/raw/img/categorize-antonims/out.png",
                                                          :transition "out-object",
                                                          :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                       :hidden        {:visible false}, :init-position {:x          1095,
                                                                                                                        :y          223, :visible true}
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
                                                                                             :say-item         "out-item"
                                                                                             :placement-target "in"
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
                                                                                             :init-position {:x          1095,
                                                                                                             :y          223,
                                                                                                             :duration 1}}}}
                                                          },
                                       :night-object     {
                                                          :type       "image",
                                                          :width      160,
                                                          :height     160,
                                                          :x          1274,
                                                          :y          481,
                                                          :src        "/raw/img/categorize-antonims/night.png",
                                                          :transition "night-object",
                                                          :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                       :hidden        {:visible false}, :init-position {:x          1274,
                                                                                                                        :y          481,}
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
                                                                                             :say-item         "night-item"
                                                                                             :placement-target "day"
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
                                                                                             :init-position {:x          1274,
                                                                                                             :y          481,
                                                                                                             :duration 1}}}}
                                                          },
                                       :loud-object      {
                                                          :type       "image",
                                                          :width      160,
                                                          :height     160,
                                                          :x          790,
                                                          :y          160,
                                                          :src        "/raw/img/categorize-antonims/loud.png",
                                                          :transition "loud-object",
                                                          :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                       :hidden        {:visible false}, :init-position {:x          790,
                                                                                                                        :y          160,}
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
                                                                                             :say-item         "loud-item"
                                                                                             :placement-target "quiet"
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
                                                                                             :init-position {:x          790,
                                                                                                             :y          160,
                                                                                                             :duration 1}}}}
                                                          },
                                       :back-object      {
                                                          :type       "image",
                                                          :width      160,
                                                          :height     160,
                                                          :x          415,
                                                          :y          354,
                                                          :src        "/raw/img/categorize-antonims/back.png",
                                                          :transition "back-object",
                                                          :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                       :hidden        {:visible false}, :init-position {:x          415,
                                                                                                                        :y          354,}
                                                                       :not-draggable {:draggable false}, :draggable {:draggable true}
                                                                       },
                                                          :draggable  true,
                                                          :actions    {:drag-start {
                                                                                    :type   "action",
                                                                                    :on     "drag-start",
                                                                                    :id     "start-drag",
                                                                                    :params {:colliders ["in" "quiet" "right"
                                                                                                         "down" "front" "day"]
                                                                                             :say-item         "back-item"
                                                                                             :placement-target "front"
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
                                                                                             :init-position {:x          415,
                                                                                                             :y          354,
                                                                                                             :duration 1}}}}
                                                          },
                                       :down             {:type       "image",
                                                          :width      253,
                                                          :height     253,
                                                          :x          1568,
                                                          :y          763,
                                                          :src        "/raw/img/categorize-antonims/down.png",
                                                          :transition "down",
                                                          :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                          :actions    {:click {:id     "click-on-box",
                                                                               :on     "click",
                                                                               :type   "action",
                                                                               :params {:target "down"}
                                                                               }}
                                                          }
                                       :day              {:type       "image",
                                                          :width      253,
                                                          :height     253,
                                                          :x          980,
                                                          :y          763,
                                                          :src        "/raw/img/categorize-antonims/day.png",
                                                          :transition "day",
                                                          :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                          :actions    {:click {:id     "click-on-box",
                                                                               :on     "click",
                                                                               :type   "action",
                                                                               :params {:target "day"}
                                                                               }}
                                                          }
                                       :front            {:type       "image",
                                                          :width      252,
                                                          :height     252,
                                                          :x          686,
                                                          :y          763,
                                                          :src        "/raw/img/categorize-antonims/front.png",
                                                          :transition "front",
                                                          :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                          :actions    {:click {:id     "click-on-box",
                                                                               :on     "click",
                                                                               :type   "action",
                                                                               :params {:target "front"}
                                                                               }}
                                                          }
                                       :in               {:type       "image",
                                                          :width      253,
                                                          :height     252,
                                                          :x          1274,
                                                          :y          763,
                                                          :src        "/raw/img/categorize-antonims/in.png",
                                                          :transition "in",
                                                          :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                       :init-position {:x          1274,
                                                                                       :y          763, :visible true}
                                                                       },
                                                          :actions    {:click {:id     "click-on-box",
                                                                               :on     "click",
                                                                               :type   "action",
                                                                               :params {:target "in"}
                                                                               }}
                                                          },
                                       :quiet            {:type       "image",
                                                          :width      253,
                                                          :height     253,
                                                          :y          763,
                                                          :x          393,
                                                          :transition "quiet",
                                                          :src        "/raw/img/categorize-antonims/quiet.png",
                                                          :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                          :actions    {:click {:id     "click-on-box",
                                                                               :on     "click",
                                                                               :type   "action",
                                                                               :params {:target "quiet"}
                                                                               }}
                                                          }
                                       :right            {:type       "image",
                                                          :width      252,
                                                          :height     253,
                                                          :x          99,
                                                          :y          763,
                                                          :transition "right",
                                                          :src        "/raw/img/categorize-antonims/right.png",
                                                          :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                          :actions    {:click {:id     "click-on-box",
                                                                               :on     "click",
                                                                               :type   "action",
                                                                               :params {:target "right"}
                                                                               }}
                                                          },
                                       :librarian        {:type   "animation",
                                                          :x      250,
                                                          :y      1000,
                                                          :width  351,
                                                          :height 717,
                                                          :anim   "idle",
                                                          :name   "senoravaca",
                                                          :skin   "lion",
                                                          :speed  0.3,
                                                          :start  true
                                                          :actions    {:click {:id "tap-instructions" :on "click" :type "action"}}}
                                       },

                       :scene-objects [["layered-background"]
                                       ["librarian"]
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
                                                                            {:type "set-variable", :var-name "say", :var-value false}
                                                                            {:type "set-variable", :var-name "next-check-collide", :var-value false}
                                                                            {
                                                                             :type        "mass-state"
                                                                             :id          "not-highlighted"
                                                                             :from-params [{:action-property "targets" :param-property "colliders"}]
                                                                             }

                                                                            ]
                                                                     },
                                       :click-on-box                {:type        "test-var-scalar",
                                                                     :fail        "wrong-answer",
                                                                     :var-name    "object-2"
                                                                     :from-params [{:action-property "value" :param-property "target"}]
                                                                     :from-var    [{:var-name "correct-answer", :action-property "success"}]},
                                       :left-item                      (dialog/default "Left")
                                       :back-item                      (dialog/default "Back")
                                       :up-item                        (dialog/default "Up")
                                       :loud-item                      (dialog/default "Loud")
                                       :night-item                     (dialog/default "Night")
                                       :out-item                       (dialog/default "Out")

                                       :say-item                       {:type "sequence-data"
                                                                        :data [{:type "action" :from-params [{:action-property "id"
                                                                                                              :param-property  "say-item"}]}
                                                                               {:type     "test-var-scalar",
                                                                                :success  "next-say",
                                                                                :value    true,
                                                                                :var-name "say"}]}
                                       :next-say                       {:type "sequence-data"
                                                                        :data [{:type     "set-timeout"
                                                                                :action   "say-item"
                                                                                :interval 100}]}

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
                                                                            {:type "set-variable", :var-name "say", :var-value true}
                                                                            {:type "set-variable", :var-name "next-check-collide", :var-value true}
                                                                            {:id "next-say" :type "action"}
                                                                            {:id "next-check-collide" :type "action"}
                                                                            ]},

                                       :next-check-collide             {:type "sequence-data"
                                                                        :data [{:type     "set-timeout"
                                                                                :action   "check-collide"
                                                                                :interval 10}]}
                                       :check-collide                  {:type "sequence-data"
                                                                        :data [
                                                                               {:type        "test-transitions-and-pointer-collide",
                                                                                :success     "highlight",
                                                                                :fail        "unhighlight",
                                                                                :from-params [{:param-property "colliders", :action-property "transitions"}]}
                                                                               {:type     "test-var-scalar",
                                                                                :success  "next-check-collide",
                                                                                :value    true,
                                                                                :var-name "next-check-collide"}
                                                                               ]}
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
                                                                             }
                                                                            {:type        "test-var-scalar",
                                                                             :success     "wrong-answer-dialog",
                                                                             :value false
                                                                             :from-params [{:action-property "var-name",
                                                                                            :template        "colliding-%",
                                                                                            :param-property  "placement-target"}]},

                                                                            ]
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
                                                                     :phrase-description "Left on right"
                                                                     }
                                       :instruction-2               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-2",
                                                                     :phrase-description "Loud on quiet"
                                                                     }
                                       :instruction-3               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-3",
                                                                     :phrase-description "Out on in"
                                                                     }
                                       :instruction-4               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-4",
                                                                     :phrase-description "Night on day"
                                                                     }
                                       :instruction-5               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-5",
                                                                     :phrase-description "Back on front"
                                                                     }
                                       :instruction-6               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-6",
                                                                     :phrase-description "Up on down"
                                                                     }
                                       
                                       :stop-activity               {:type "stop-activity"},
                                       :finish                      {:type "sequence-data",
                                                                     :data [{:type "action" :id   "finish-dialog"}
                                                                            {:type "action" :id   "finish-activity"}]}
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
                                                                            {:type "set-variable", :var-name "next-task", :var-value "finish"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-6"}]
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
                                       :finish-activity                {:type "finish-activity"}
                                       :tap-instructions {:type "action"
                                                          :from-var [{:var-name "instruction", :action-property "id"}]}
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
                                                             :text "Put the left picture on the right picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-1}
                                                            {:type "prompt"
                                                             :text "Put the loud picture on the quiet picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-2}
                                                            {:type "prompt"
                                                             :text "Put the out picture on the in picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-3}
                                                            {:type "prompt"
                                                             :text "Put the night picture on the day picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-4}
                                                            {:type "prompt"
                                                             :text "Put the back picture on the front picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-5}
                                                            {:type "prompt"
                                                             :text "Put the up picture on the down picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-6}
                                                            ]}
                                                   {:title "Round 3 - items"
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
                                                             :action-id :out-item}]}]}})
