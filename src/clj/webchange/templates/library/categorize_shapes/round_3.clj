(ns webchange.templates.library.categorize-shapes.round-3
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(def template-round-3 {:assets        [{:url "/raw/img/categorize-shapes/background-class.png", :size 10, :type "image"}
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
                                       :star-group         {
                                                            :type       "group",
                                                            :children   [],
                                                            :draggable  true,
                                                            :transition "star-group",
                                                            :states     {:group-all {:children ["star-box" "star-object"]}
                                                                         :ungroup   {:children []}
                                                                         :hidden    {:visible false}},

                                                            :actions    {:drag-start {
                                                                                      :type             "action",
                                                                                      :on               "drag-start",
                                                                                      :id               "start-drag",
                                                                                      :params           {:colliders ["rectangle-box" "oval-box"
                                                                                                                     "triangle-table" "square-table"
                                                                                                                     "circle-table"]
                                                                                                         :say-item         "star-group"
                                                                                                         :placement-target "circle-table"
                                                                                                         :self      "star-group"
                                                                                                         :object    "star-object"
                                                                                                         }}
                                                                         :drag-end   {
                                                                                      :id     "stop-drag-hide",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {
                                                                                               :colliders     ["rectangle-box" "oval-box"
                                                                                                               "triangle-table" "square-table"
                                                                                                               "circle-table"]
                                                                                               :self          "star-group"
                                                                                               :target        "triangle-table"
                                                                                               :object        "star-object"
                                                                                               :init-position {:x        100,
                                                                                                               :y        600,
                                                                                                               :duration 1}}}},

                                                            }
                                       :star-group-1       {
                                                            :type       "group",
                                                            :children   [],
                                                            :draggable  true,
                                                            :transition "star-group-1",
                                                            :states     {:group-all {:children ["star-box" "star-object"]}
                                                                         :ungroup   {:children []}
                                                                         :hidden    {:visible false}},

                                                            :actions    {:drag-start {
                                                                                      :type             "action",
                                                                                      :on               "drag-start",
                                                                                      :id               "start-drag",
                                                                                      :params           {:colliders ["rectangle-box" "oval-box"
                                                                                                                     "triangle-table" "square-table"
                                                                                                                     "circle-table"]
                                                                                                         :say-item         "star-group-1"
                                                                                                         :placement-target "triangle-table"
                                                                                                         :self      "star-group-1"
                                                                                                         :object    "star-object"
                                                                                                         }}
                                                                         :drag-end   {
                                                                                      :id     "stop-drag-hide",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {
                                                                                               :colliders     ["rectangle-box" "oval-box"
                                                                                                               "triangle-table" "square-table"
                                                                                                               "circle-table"]
                                                                                               :self          "star-group"
                                                                                               :target        "circle-table"
                                                                                               :object        "star-object"
                                                                                               :init-position {:x        100,
                                                                                                               :y        600,
                                                                                                               :duration 1}}}},

                                                            }
                                       :rectangle-group    {
                                                            :type       "group",
                                                            :children   [],
                                                            :draggable  true,
                                                            :transition "rectangle-group",
                                                            :states     {:group-all {:children ["rectangle-box" "rectangle-object"]}
                                                                         :ungroup   {:children []}
                                                                         :hidden    {:visible false}},

                                                            :actions    {:drag-start {
                                                                                      :type             "action",
                                                                                      :on               "drag-start",
                                                                                      :id               "start-drag",
                                                                                      :params           {:colliders ["star-box" "oval-box"
                                                                                                                     "triangle-table" "square-table"
                                                                                                                     "circle-table"]
                                                                                                         :say-item         "rectangle-group"
                                                                                                         :placement-target "square-table"
                                                                                                         :self      "rectangle-group"}}
                                                                         :drag-end   {
                                                                                      :id     "stop-drag-hide",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {
                                                                                               :colliders     ["rectangle-box" "oval-box"
                                                                                                               "triangle-table" "square-table"
                                                                                                               "circle-table"]
                                                                                               :self          "rectangle-group"
                                                                                               :target        "circle-table"
                                                                                               :init-position {:x        100,
                                                                                                               :y        600,
                                                                                                               :duration 1}}}},
                                                            }
                                       :oval-group         {
                                                            :type       "group",
                                                            :children   [],
                                                            :draggable  true,
                                                            :transition "oval-group",
                                                            :states     {:group-all {:children ["oval-box" "oval-object"]}
                                                                         :ungroup   {:children []}
                                                                         :hidden    {:visible false}},

                                                            :actions    {:drag-start {
                                                                                      :type             "action",
                                                                                      :on               "drag-start",
                                                                                      :id               "start-drag",
                                                                                      :params           {:colliders ["rectangle-box" "star-box"
                                                                                                                     "triangle-table" "square-table"
                                                                                                                     "circle-table"]
                                                                                                         :say-item         "oval-group"
                                                                                                         :placement-target "circle-table"
                                                                                                         :self      "oval-group"}}
                                                                         :drag-end   {
                                                                                      :id     "stop-drag-hide",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {
                                                                                               :colliders     ["rectangle-box" "oval-box"
                                                                                                               "triangle-table" "square-table"
                                                                                                               "circle-table"]
                                                                                               :self          "oval-group"
                                                                                               :target        "square-table"
                                                                                               :init-position {:x        100,
                                                                                                               :y        600,
                                                                                                               :duration 1}}}},
                                                            }

                                       :star-box           {:type       "image",
                                                            :x          1181,
                                                            :y          835,
                                                            :width      221,
                                                            :height     200,
                                                            :src        "/raw/img/categorize-shapes/star-box.png",
                                                            :transition "star-box",
                                                            :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                         :init-position {:x 1181, :y 835, :visible true}
                                                                         },
                                                            },
                                       :rectangle-box      {:type       "image",
                                                            :x          922,
                                                            :y          835,
                                                            :width      222,
                                                            :height     200,
                                                            :transition "rectangle-box",
                                                            :src        "/raw/img/categorize-shapes/rectangle-box.png",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                            }
                                       :oval-box           {:type       "image",
                                                            :x          663,
                                                            :y          835,
                                                            :width      222,
                                                            :height     200
                                                            :transition "oval-box",
                                                            :src        "/raw/img/categorize-shapes/oval-box.png",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                                            },
                                       :oval-object        {
                                                            :type       "image",
                                                            :x          1685,
                                                            :y          113,
                                                            :width      75,
                                                            :height     94,
                                                            :src        "/raw/img/categorize-shapes/oval-group.png",
                                                            :transition "oval-object",
                                                            :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                         :hidden        {:visible false}, :init-position {:x 1685, :y 113,}
                                                                         :not-draggable {:draggable false}, :draggable {:draggable true}
                                                                         },
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type             "action",
                                                                                      :on               "drag-start",
                                                                                      :id               "start-drag",
                                                                                      :params           {:colliders ["star-box" "rectangle-box" "oval-box"
                                                                                                                     "triangle-table" "square-table"
                                                                                                                     "circle-table"]
                                                                                                         :say-item         "oval-item"
                                                                                                         :placement-target "oval-box"
                                                                                                         :self      "oval-object"}}
                                                                         :drag-end   {
                                                                                      :id     "stop-drag-hide",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {
                                                                                               :colliders     ["star-box" "rectangle-box" "oval-box"
                                                                                                               "triangle-table" "square-table"
                                                                                                               "circle-table"]
                                                                                               :self          "oval-object"
                                                                                               :target        "oval-box"
                                                                                               :init-position {:x        1685, :y 113,
                                                                                                               :duration 1}}}},},
                                       :triangle-object    {
                                                            :type       "image",
                                                            :x          410,
                                                            :y          584,
                                                            :width      75,
                                                            :height     94,
                                                            :src        "/raw/img/categorize-shapes/triangle-group.png",
                                                            :transition "triangle-object",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}
                                                                         :hidden      {:visible false}, :init-position {:x 410,
                                                                                                                        :y 584,}
                                                                         },
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type             "action",
                                                                                      :on               "drag-start",
                                                                                      :id               "start-drag",
                                                                                      :params           {:colliders ["star-box" "rectangle-box" "oval-box"
                                                                                                                     "triangle-table" "square-table"
                                                                                                                     "circle-table"]
                                                                                                         :say-item         "triangle-item"
                                                                                                         :placement-target "triangle-table"
                                                                                                         :self      "triangle-object"}}
                                                                         :drag-end   {
                                                                                      :id     "stop-drag-hide",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {
                                                                                               :colliders     ["star-box" "rectangle-box" "oval-box"
                                                                                                               "triangle-table" "square-table"
                                                                                                               "circle-table"]
                                                                                               :self          "triangle-object"
                                                                                               :target        "triangle-table"
                                                                                               :init-position {:x        410,
                                                                                                               :y        584,
                                                                                                               :duration 1}}}}
                                                            },
                                       :star-object        {
                                                            :type       "image",
                                                            :x          830,
                                                            :y          404,
                                                            :width      75,
                                                            :height     94,
                                                            :src        "/raw/img/categorize-shapes/star-group.png",
                                                            :transition "star-object",
                                                            :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                         :hidden        {:visible false}, :init-position {:x 830,
                                                                                                                          :y 404, :visible true}
                                                                         :not-draggable {:draggable false}, :draggable {:draggable true}
                                                                         },
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type             "action",
                                                                                      :on               "drag-start",
                                                                                      :id               "start-drag",
                                                                                      :params           {:colliders ["star-box" "rectangle-box" "oval-box"
                                                                                                                     "triangle-table" "square-table"
                                                                                                                     "circle-table"]
                                                                                                         :say-item         "star-item"
                                                                                                         :placement-target "star-box"
                                                                                                         :self      "star-object"}}
                                                                         :drag-end   {
                                                                                      :id     "stop-drag-hide",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {
                                                                                               :colliders     ["star-box" "rectangle-box" "oval-box"
                                                                                                               "triangle-table" "square-table"
                                                                                                               "circle-table"]
                                                                                               :self          "star-object"
                                                                                               :target        "star-box"
                                                                                               :init-position {:x        830,
                                                                                                               :y        404,
                                                                                                               :duration 1}}}}
                                                            },
                                       :circle-object      {
                                                            :type       "image",
                                                            :x          143,
                                                            :y          921,
                                                            :width      75,
                                                            :height     94,
                                                            :src        "/raw/img/categorize-shapes/circle-group.png",
                                                            :transition "circle-object",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}
                                                                         :hidden      {:visible false}, :init-position {:x 143,
                                                                                                                        :y 921,}
                                                                         },
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type             "action",
                                                                                      :on               "drag-start",
                                                                                      :id               "start-drag",
                                                                                      :params           {:colliders ["star-box" "rectangle-box" "oval-box"
                                                                                                                     "triangle-table" "square-table"
                                                                                                                     "circle-table"]
                                                                                                         :say-item         "circle-item"
                                                                                                         :placement-target "circle-table"
                                                                                                         :self      "circle-object"}}
                                                                         :drag-end   {
                                                                                      :id     "stop-drag-hide",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {
                                                                                               :colliders     ["star-box" "rectangle-box" "oval-box"
                                                                                                               "triangle-table" "square-table"
                                                                                                               "circle-table"]
                                                                                               :self          "circle-object"
                                                                                               :target        "circle-table"
                                                                                               :init-position {:x        143,
                                                                                                               :y        921,
                                                                                                               :duration 1}}}}
                                                            },
                                       :rectangle-object   {
                                                            :type       "image",
                                                            :x          123,
                                                            :y          51,
                                                            :width      75,
                                                            :height     94,
                                                            :src        "/raw/img/categorize-shapes/rectangle-group.png",
                                                            :transition "rectangle-object",
                                                            :states     {:highlighted   {:highlight true} :not-highlighted {:highlight false}
                                                                         :hidden        {:visible false}, :init-position {:x 123,
                                                                                                                          :y 51,}
                                                                         :not-draggable {:draggable false}, :draggable {:draggable true}
                                                                         },
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type             "action",
                                                                                      :on               "drag-start",
                                                                                      :id               "start-drag",
                                                                                      :params           {:colliders ["star-box" "rectangle-box" "oval-box"
                                                                                                                     "triangle-table" "square-table"
                                                                                                                     "circle-table"]
                                                                                                         :say-item         "rectangle-item"
                                                                                                         :placement-target "rectangle-box"
                                                                                                         :self      "rectangle-object"}}
                                                                         :drag-end   {
                                                                                      :id     "stop-drag-hide",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {
                                                                                               :colliders     ["star-box" "rectangle-box" "oval-box"
                                                                                                               "triangle-table" "square-table"
                                                                                                               "circle-table"]
                                                                                               :self          "rectangle-object"
                                                                                               :target        "rectangle-box"
                                                                                               :init-position {:x        123,
                                                                                                               :y        51,
                                                                                                               :duration 1}}}}
                                                            },
                                       :square-object      {
                                                            :type       "image",
                                                            :x          512,
                                                            :y          232,
                                                            :width      75,
                                                            :height     94,
                                                            :src        "/raw/img/categorize-shapes/square-group.png",
                                                            :transition "square-object",
                                                            :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}
                                                                         :hidden      {:visible false}, :init-position {:x 512,
                                                                                                                        :y 232,}
                                                                         },
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type             "action",
                                                                                      :on               "drag-start",
                                                                                      :id               "start-drag",
                                                                                      :params           {:colliders ["star-box" "rectangle-box" "oval-box"
                                                                                                                     "triangle-table" "square-table"
                                                                                                                     "circle-table"]
                                                                                                         :say-item         "square-item"
                                                                                                         :placement-target "square-table"
                                                                                                         :self      "square-object"}}
                                                                         :drag-end   {
                                                                                      :id     "stop-drag-hide",
                                                                                      :on     "drag-end",
                                                                                      :type   "action",
                                                                                      :params {
                                                                                               :colliders     ["star-box" "rectangle-box" "oval-box"
                                                                                                               "triangle-table" "square-table"
                                                                                                               "circle-table"]
                                                                                               :self          "square-object"
                                                                                               :target        "square-table"
                                                                                               :init-position {:x        512,
                                                                                                               :y        232,
                                                                                                               :duration 1}}}}
                                                            },
                                       :triangle-table
                                       {:type       "image",
                                        :x          196,
                                        :y          631,
                                        :width      460,
                                        :height     243
                                        :src        "/raw/img/categorize-shapes/triangle-table.png",
                                        :transition "triangle-table",
                                        :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                        }
                                       :circle-table
                                       {:type       "image",
                                        :x          1222,
                                        :y          650,
                                        :width      429,
                                        :height     221,
                                        :src        "/raw/img/categorize-shapes/circle-table.png",
                                        :transition "circle-table",
                                        :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                        }
                                       :square-table
                                       {:type       "image",
                                        :x          691,
                                        :y          665,
                                        :width      512
                                        :height     251
                                        :src        "/raw/img/categorize-shapes/square-table.png",
                                        :transition "square-table",
                                        :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},
                                        }
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
                                       ["triangle-table" "square-table" "circle-table"]
                                       ["librarian"]
                                       ["star-box" "rectangle-box" "oval-box"]
                                       ["oval-object" "triangle-object" "star-object" "circle-object" "rectangle-object" "square-object"]
                                       ["star-group-1" "star-group" "oval-group" "rectangle-group"]
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
                                       :star-group                  (dialog/default "Star group")
                                       :star-group-1                (dialog/default "Star group 1")
                                       :rectangle-group             (dialog/default "Rectangle group")
                                       :oval-group                  (dialog/default "Oval group")
                                       :oval-item                   (dialog/default "oval")
                                       :circle-item                 (dialog/default "circle")
                                       :rectangle-item              (dialog/default "rectangle")
                                       :square-item                 (dialog/default "square")
                                       :star-item                   (dialog/default "star")
                                       :triangle-item               (dialog/default "triangle")

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
                                                                            {:id "next-check-collide" :type "action"}
                                                                            ]},
                                       :next-check-collide  {:type "sequence-data"
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
                                                                     :phrase-description "Oval in oval box"
                                                                     }
                                       :instruction-2               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-2",
                                                                     :phrase-description "Triangle on triangle table"
                                                                     }
                                       :instruction-3               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-3",
                                                                     :phrase-description "Circle on circle table"
                                                                     }
                                       :instruction-4               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-4",
                                                                     :phrase-description "Star in star box"
                                                                     }
                                       :instruction-5               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-5",
                                                                     :phrase-description "Rectangle in rectangle box"
                                                                     }
                                       :instruction-6               {:type               "sequence-data",
                                                                     :editor-type        "dialog",
                                                                     :data               [{:type "sequence-data"
                                                                                           :data [{:type "empty" :duration 0}
                                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                     :phrase             "instruction-6",
                                                                     :phrase-description "Square on square table"
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
                                                                            {:type "set-variable", :var-name "object-1", :var-value "triangle-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "triangle-table"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-triangle-object" "colliding-triangle-table"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-3"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-2"}]}
                                       :task-3                      {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "circle-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "circle-table"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-circle-object" "colliding-circle-table"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-4"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-3"}]
                                                                     }
                                       :task-4                      {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "star-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "star-box"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-star-object" "colliding-star-box"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-5"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-4"}]
                                                                     }
                                       :task-5                      {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "rectangle-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "rectangle-box"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-rectangle-object" "colliding-rectangle-box"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "task-6"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-5"}]
                                                                     }
                                       :task-6                      {
                                                                     :type "sequence-data",
                                                                     :data [
                                                                            {:type "set-variable", :var-name "object-1", :var-value "square-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "square-table"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-square-object" "colliding-square-table"]}
                                                                            {:type "set-variable", :var-name "next-task", :var-value "finish"}
                                                                            {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-single"}
                                                                            {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                            {:type "action", :id "instruction-6"}]
                                                                     }
                                       :start-activity              {:type "sequence-data",
                                                                     :data [{:type "action", :id "intro"}
                                                                            {:type "set-variable", :var-name "object-1", :var-value "oval-object"}
                                                                            {:type "set-variable", :var-name "object-2", :var-value "oval-box"}
                                                                            {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-oval-object" "colliding-oval-box"]}
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
                                                             :text "Put the oval in its box."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-1}
                                                            {:type "prompt"
                                                             :text "Put the triangle on its table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-2}
                                                            {:type "prompt"
                                                             :text "Put the circle on its table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-3}
                                                            {:type "prompt"
                                                             :text "Put the star in its box."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-4}
                                                            {:type "prompt"
                                                             :text "Put the rectangle in its box."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-5}
                                                            {:type "prompt"
                                                             :text "Put the square on its table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-6}
                                                            ]}
                                                   {:title "Round 3 - items"
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
                                                            {:type      "dialog"
                                                             :action-id :star-group}
                                                            {:type      "dialog"
                                                             :action-id :star-group-1}
                                                            {:type      "dialog"
                                                             :action-id :rectangle-group}
                                                            {:type      "dialog"
                                                             :action-id :oval-group}]}]}})
