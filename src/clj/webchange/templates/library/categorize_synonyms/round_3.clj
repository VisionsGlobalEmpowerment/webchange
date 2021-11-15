(ns webchange.templates.library.categorize-synonyms.round-3
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(def template-round-3 {:assets        [{:url "/raw/img/categorize-synonyms/background-class.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/surface.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/decoration.png", :size 10, :type "image"}
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
                       :objects       {:layered-background  {:type       "layered-background",
                                                             :background {:src "/raw/img/categorize-shapes/background-class.png"},
                                                             :decoration {:src "/raw/img/categorize-shapes/decoration.png"},
                                                             :surface    {:src "/raw/img/categorize-shapes/surface.png"}},
                                       :large-big-group     {
                                                             :type       "group",
                                                             :children   [],
                                                             :transition "large-big-group",
                                                             :states     {:group-all {:children ["big" "large-object"]}
                                                                          :ungroup   {:children []}
                                                                          :hidden    {:visible false}},
                                                             }
                                       :scared-afraid-group {
                                                             :type       "group",
                                                             :children   [],
                                                             :transition "scared-afraid-group",
                                                             :states     {:group-all {:children ["scared" "afraid-object"]}
                                                                          :ungroup   {:children []}
                                                                          :hidden    {:visible false}},
                                                             }
                                       :kid-child-group     {
                                                             :type       "group",
                                                             :children   [],
                                                             :transition "kid-child-group",
                                                             :states     {:group-all {:children ["child" "kid-object"]}
                                                                          :ungroup   {:children []}
                                                                          :hidden    {:visible false}}}
                                       :glad-happy-group    {
                                                             :type       "group",
                                                             :children   [],
                                                             :transition "glad-happy-group",
                                                             :states     {:group-all {:children ["happy" "glad-object"]}
                                                                          :ungroup   {:children []}
                                                                          :hidden    {:visible false}}}
                                       :chilly-object       {
                                                             :type       "image",
                                                             :width      159,
                                                             :height     160,
                                                             :x          768,
                                                             :y          481,
                                                             :src        "/raw/img/categorize-synonyms/chilly.png",
                                                             :transition "chilly-object",
                                                             :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                          :hidden        {:visible false}, :init-position {:x 768,
                                                                                                                           :y 481,}
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
                                                                                                :say-item         "chilly-item"
                                                                                                :placement-target "cold"
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
                                                                                                :init-position {:x        768,
                                                                                                                :y        481,
                                                                                                                :duration 1}}}},},
                                       :large-object        {
                                                             :type       "image",
                                                             :width      160,
                                                             :height     159,
                                                             :x          415,
                                                             :y          355,
                                                             :src        "/raw/img/categorize-synonyms/large.png",
                                                             :transition "large-object",
                                                             :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                          :hidden        {:visible false}, :init-position {:x 415,
                                                                                                                           :y 355,}
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
                                                                                                :say-item         "large-item"
                                                                                                :placement-target "big"
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
                                                                                                :init-position {:x        415,
                                                                                                                :y        355,
                                                                                                                :duration 1}}}}
                                                             },
                                       :glad-object         {
                                                             :type       "image",
                                                             :width      160,
                                                             :height     161,
                                                             :x          1674,
                                                             :y          434,
                                                             :src        "/raw/img/categorize-synonyms/glad.png",
                                                             :transition "glad-object",
                                                             :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                          :hidden        {:visible false}, :init-position {:x 1674,
                                                                                                                           :y 434, :visible true}
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
                                                                                                :say-item         "glad-item"
                                                                                                :placement-target "happy"
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
                                                                                                :init-position {:x        1674,
                                                                                                                :y        434,
                                                                                                                :duration 1}}}}
                                                             },
                                       :afraid-object       {
                                                             :type       "image",
                                                             :width      160,
                                                             :height     160,
                                                             :x          1636,
                                                             :y          107,
                                                             :src        "/raw/img/categorize-synonyms/afraid.png",
                                                             :transition "afraid-object",
                                                             :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                          :hidden        {:visible false}, :init-position {:x 1636,
                                                                                                                           :y 107,}
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
                                                                                                :say-item         "afraid-item"
                                                                                                :placement-target "scared"
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
                                                                                                :init-position {:x        1636,
                                                                                                                :y        107,
                                                                                                                :duration 1}}}}
                                                             },
                                       :garbage-object      {
                                                             :type       "image",
                                                             :width      160,
                                                             :height     160,
                                                             :x          1388,
                                                             :y          561,
                                                             :src        "/raw/img/categorize-synonyms/garbage.png",
                                                             :transition "garbage-object",
                                                             :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                          :hidden        {:visible false}, :init-position {:x 1388,
                                                                                                                           :y 561,}
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
                                                                                                :say-item         "garbage-item"
                                                                                                :placement-target "trash"
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
                                                                                                :init-position {:x        1388,
                                                                                                                :y        561,
                                                                                                                :duration 1}}}}
                                                             },
                                       :kid-object          {
                                                             :type       "image",
                                                             :width      161,
                                                             :height     160,
                                                             :x          1094,
                                                             :y          223,
                                                             :src        "/raw/img/categorize-synonyms/kid.png",
                                                             :transition "kid-object",
                                                             :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                          :hidden        {:visible false}, :init-position {:x 1094,
                                                                                                                           :y 223,}
                                                                          :not-draggable {:draggable false}, :draggable {:draggable true}
                                                                          },
                                                             :draggable  true,
                                                             :actions    {:drag-start {
                                                                                       :type   "action",
                                                                                       :on     "drag-start",
                                                                                       :id     "start-drag",
                                                                                       :params {:colliders ["happy" "trash" "cold"
                                                                                                            "big" "child" "scared"]
                                                                                                :say-item         "kid-item"
                                                                                                :placement-target "child"
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
                                                                                                :init-position {:x        1094,
                                                                                                                :y        223,
                                                                                                                :duration 1}}}}
                                                             },
                                       :big                 {:type       "image",
                                                             :width      253,
                                                             :height     253,
                                                             :x          392,
                                                             :y          762,
                                                             :src        "/raw/img/categorize-synonyms/big.png",
                                                             :transition "big",
                                                             :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                             :actions    {:click {:id     "click-on-box",
                                                                                  :on     "click",
                                                                                  :type   "action",
                                                                                  :params {:target "big"}
                                                                                  }}
                                                             }
                                       :scared              {:type       "image",
                                                             :width      253,
                                                             :height     253,
                                                             :x          686,
                                                             :y          763,
                                                             :src        "/raw/img/categorize-synonyms/scared.png",
                                                             :transition "scared",
                                                             :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                             :actions    {:click {:id     "click-on-box",
                                                                                  :on     "click",
                                                                                  :type   "action",
                                                                                  :params {:target "scared"}
                                                                                  }}
                                                             }
                                       :child               {:type       "image",
                                                             :width      253,
                                                             :height     252,
                                                             :x          1274,
                                                             :y          763,
                                                             :src        "/raw/img/categorize-synonyms/child.png",
                                                             :transition "child",
                                                             :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                             :actions    {:click {:id     "click-on-box",
                                                                                  :on     "click",
                                                                                  :type   "action",
                                                                                  :params {:target "child"}
                                                                                  }}
                                                             }

                                       :happy               {:type       "image",
                                                             :width      253,
                                                             :height     253,
                                                             :x          99,
                                                             :y          763,
                                                             :src        "/raw/img/categorize-synonyms/happy.png",
                                                             :transition "happy",
                                                             :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                          :init-position {:x 99, :y 763, :visible true}},
                                                             :actions    {:click {:id     "click-on-box",
                                                                                  :on     "click",
                                                                                  :type   "action",
                                                                                  :params {:target "happy"}
                                                                                  }}
                                                             },
                                       :trash               {:type       "image",
                                                             :width      253,
                                                             :height     253,
                                                             :x          1568,
                                                             :y          762,
                                                             :transition "trash",
                                                             :src        "/raw/img/categorize-synonyms/trash.png",
                                                             :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                             :actions    {:click {:id     "click-on-box",
                                                                                  :on     "click",
                                                                                  :type   "action",
                                                                                  :params {:target "trash"}
                                                                                  }}
                                                             }
                                       :cold                {:type       "image",
                                                             :width      253,
                                                             :height     253,
                                                             :x          980,
                                                             :y          763,
                                                             :transition "cold",
                                                             :src        "/raw/img/categorize-synonyms/cold.png",
                                                             :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                             :actions    {:click {:id     "click-on-box",
                                                                                  :on     "click",
                                                                                  :type   "action",
                                                                                  :params {:target "cold"}
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
                                       ["happy" "trash" "cold"]
                                       ["big" "child" "scared"]
                                       ["chilly-object" "large-object" "glad-object" "afraid-object" "garbage-object" "kid-object"]
                                       ["scared-afraid-group" "large-big-group" "kid-child-group" "glad-happy-group"]
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
                                       :click-on-box                {:type        "test-var-scalar",
                                                                     :fail        "wrong-answer",
                                                                     :var-name    "object-2"
                                                                     :from-params [{:action-property "value" :param-property "target"}]
                                                                     :from-var    [{:var-name "correct-answer", :action-property "success"}]},
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
                                                                             }]},
                                       :chilly-item                 (dialog/default "chilly")
                                       :large-item                  (dialog/default "large")
                                       :glad-item                   (dialog/default "glad")
                                       :afraid-item                 (dialog/default "afraid")
                                       :garbage-item                (dialog/default "garbage")
                                       :kid-item                    (dialog/default "kid")

                                       :say-item                    {:type "sequence-data"
                                                                     :data [{:type "action" :from-params [{:action-property "id"
                                                                                                           :param-property  "say-item"}]}
                                                                            {:type     "test-var-scalar",
                                                                             :success  "next-say",
                                                                             :value    true,
                                                                             :var-name "say"}]}
                                       :next-say                    {:type "sequence-data"
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
                                                                            {:id "next-check-collide" :type "action"}]},
                                       :next-check-collide          {:type "sequence-data"
                                                                     :data [{:type     "set-timeout"
                                                                             :action   "check-collide"
                                                                             :interval 10}]}
                                       :check-collide               {:type "sequence-data"
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
                                                                             :from-params [{:action-property "target" :param-property "transition"}]}
                                                                            {:type        "test-var-scalar",
                                                                             :success     "wrong-answer-dialog",
                                                                             :value       false
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
                                                                     :phrase-description "Chilly on cold"
                                                                     }
                                       :instruction-2               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-2",
                                                                     :phrase-description "Garbage on trash"
                                                                     }
                                       :instruction-3               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-3",
                                                                     :phrase-description "Glad on happy"
                                                                     }
                                       :instruction-4               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-4",
                                                                     :phrase-description "Afraid on scared"
                                                                     }
                                       :instruction-5               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-5",
                                                                     :phrase-description "Kid on child"
                                                                     }
                                       :instruction-6               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-6",
                                                                     :phrase-description "Large on big"
                                                                     }
                                       :stop-activity               {:type "stop-activity"},
                                       :finish-activity             {:type "finish-activity"}
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
                                                                            {:type "set-variable", :var-name "next-task", :var-value "finish"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-6"}]
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
                                                             :text "Put the chilly picture on the cold picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-1}
                                                            {:type "prompt"
                                                             :text "Put the garbage picture on the trash picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-2}
                                                            {:type "prompt"
                                                             :text "Put the glad picture on the happy picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-3}
                                                            {:type "prompt"
                                                             :text "Put the afraid picture on the scared picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-4}
                                                            {:type "prompt"
                                                             :text "Put the kid picture on the child picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-5}
                                                            {:type "prompt"
                                                             :text "Put the large picture on the big picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-6}
                                                            ]}
                                                   {:title "Round 3 - items"
                                                    :nodes [{:type      "dialog"
                                                             :action-id :chilly-item}
                                                            {:type      "dialog"
                                                             :action-id :large-item}
                                                            {:type      "dialog"
                                                             :action-id :glad-item}
                                                            {:type      "dialog"
                                                             :action-id :afraid-item}
                                                            {:type      "dialog"
                                                             :action-id :garbage-item}
                                                            {:type      "dialog"
                                                             :action-id :kid-item}]}]}})
