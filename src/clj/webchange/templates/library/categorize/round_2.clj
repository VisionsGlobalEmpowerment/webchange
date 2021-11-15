(ns webchange.templates.library.categorize.round-2
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(def template-round-2 {:assets        [{:url "/raw/img/categorize/01.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/02.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/03.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/purple_table.png", :size 1, :type "image"}
                                       {:url "/raw/img/categorize/orange_table.png", :size 1, :type "image"}
                                       {:url "/raw/img/categorize/green_table.png", :size 1, :type "image"}
                                       {:url "/raw/img/categorize/yellow_box_small.png", :size 1, :type "image"}
                                       {:url "/raw/img/categorize/blue_box_small.png", :size 1, :type "image"}
                                       {:url "/raw/img/categorize/red_box_small.png", :size 1, :type "image"}
                                       {:url "/raw/img/categorize/question.png", :size 1, :type "image"}
                                       {:url "/raw/img/categorize/blue_crayons.png", :size 1, :type "image"}
                                       {:url "/raw/img/categorize/red_crayons.png", :size 1, :type "image"}
                                       {:url "/raw/img/categorize/yellow_crayons.png", :size 1, :type "image"}
                                       {:url "/raw/img/categorize/purple_crayons.png", :size 1, :type "image"}
                                       {:url "/raw/img/categorize/orange_crayons.png", :size 1, :type "image"}
                                       {:url "/raw/img/categorize/green_crayons.png", :size 1, :type "image"}],
                       :objects       {:layered-background {:type       "layered-background",
                                                            :background {:src "/raw/img/categorize/01.png"},
                                                            :decoration {:src "/raw/img/categorize/03.png"},
                                                            :surface    {:src "/raw/img/categorize/02.png"}}
                                       :yellow-box         {:type "image",
                                                            :x    500,
                                                            :y    506,
                                                            :src  "/raw/img/categorize/yellow_box_small.png"}
                                       :blue-box           {:type "image",
                                                            :x    943,
                                                            :y    628,
                                                            :src  "/raw/img/categorize/blue_box_small.png"}
                                       :red-box            {:type "image",
                                                            :x    1352,
                                                            :y    490,
                                                            :src  "/raw/img/categorize/red_box_small.png"}
                                       :purple-table       {:type "image",
                                                            :x    745,
                                                            :y    773,
                                                            :src  "/raw/img/categorize/purple_table.png"}
                                       :orange-table       {:type "image",
                                                            :x    1120,
                                                            :y    652,
                                                            :src  "/raw/img/categorize/orange_table.png"}
                                       :green-table        {:type "image",
                                                            :x    330,
                                                            :y    667,
                                                            :src  "/raw/img/categorize/green_table.png"},
                                       :blue-crayon-1      {:type      "image",
                                                            :x         46,
                                                            :y         1050,
                                                            :rotation  -90,
                                                            :scale     0.35,
                                                            :src       "/raw/img/categorize/blue_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "blue-color"
                                                                                              :target         "blue-crayon-1"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "blue-box"
                                                                                            :target         "blue-crayon-1"
                                                                                            :init-position  {:x 46, :y 1050, :duration 1}
                                                                                            :check-variable "blue-box-selected"}}}},
                                       :blue-crayon-2      {:type      "image",
                                                            :x         592,
                                                            :y         500,
                                                            :rotation  -90,
                                                            :scale     0.35,
                                                            :src       "/raw/img/categorize/blue_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "blue-color"
                                                                                              :target         "blue-crayon-2"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "blue-box"
                                                                                            :target         "blue-crayon-2"
                                                                                            :init-position  {:x 592, :y 500, :duration 1}
                                                                                            :check-variable "blue-box-selected"}}}},
                                       :blue-crayon-3      {:type      "image",
                                                            :x         17,
                                                            :y         143,
                                                            :rotation  -90,
                                                            :scale     0.35,
                                                            :src       "/raw/img/categorize/blue_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "blue-color"
                                                                                              :target         "blue-crayon-3"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "blue-box"
                                                                                            :target         "blue-crayon-3"
                                                                                            :init-position  {:x 17, :y 143, :duration 1}
                                                                                            :check-variable "blue-box-selected"}}}},

                                       :orange-crayon-1    {:type      "image",
                                                            :x         746,
                                                            :y         850,
                                                            :scale     0.35
                                                            :rotation  90
                                                            :src       "/raw/img/categorize/orange_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "orange-color"
                                                                                              :target         "orange-crayon-1"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "orange-table"
                                                                                            :target         "orange-crayon-1"
                                                                                            :init-position  {:x 746, :y 850, :duration 1}
                                                                                            :check-variable "orange-table-selected"}}}},
                                       :orange-crayon-2    {:type      "image",
                                                            :x         892,
                                                            :y         400,
                                                            :scale     0.35
                                                            :rotation  90
                                                            :src       "/raw/img/categorize/orange_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "orange-color"
                                                                                              :target         "orange-crayon-2"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "orange-table"
                                                                                            :target         "orange-crayon-2"
                                                                                            :init-position  {:x 892, :y 400, :duration 1}
                                                                                            :check-variable "orange-table-selected"}}}},
                                       :orange-crayon-3    {:type      "image",
                                                            :x         317,
                                                            :y         238,
                                                            :scale     0.35
                                                            :rotation  90
                                                            :src       "/raw/img/categorize/orange_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "orange-color"
                                                                                              :target         "orange-crayon-3"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "orange-table"
                                                                                            :target         "orange-crayon-3"
                                                                                            :init-position  {:x 317, :y 238, :duration 1}
                                                                                            :check-variable "orange-table-selected"}}}},

                                       :yellow-crayon-1    {:type      "image",
                                                            :x         764,
                                                            :y         691,
                                                            :rotation  -90,
                                                            :scale     0.35,
                                                            :src       "/raw/img/categorize/yellow_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "yellow-color"
                                                                                              :target         "yellow-crayon-1"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "yellow-box"
                                                                                            :target         "yellow-crayon-1"
                                                                                            :init-position  {:x 764, :y 691, :duration 1}
                                                                                            :check-variable "yellow-box-selected"}}}},
                                       :yellow-crayon-2    {:type      "image",
                                                            :x         1171,
                                                            :y         126,
                                                            :rotation  -90,
                                                            :scale     0.35,
                                                            :src       "/raw/img/categorize/yellow_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "yellow-color"
                                                                                              :target         "yellow-crayon-2"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "yellow-box"
                                                                                            :target         "yellow-crayon-2"
                                                                                            :init-position  {:x 1171, :y 126, :duration 1}
                                                                                            :check-variable "yellow-box-selected"}}}},
                                       :yellow-crayon-3    {:type      "image",
                                                            :x         1618,
                                                            :y         440,
                                                            :rotation  -90,
                                                            :scale     0.35,
                                                            :src       "/raw/img/categorize/yellow_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "yellow-color"
                                                                                              :target         "yellow-crayon-3"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "yellow-box"
                                                                                            :target         "yellow-crayon-3"
                                                                                            :init-position  {:x 1618, :y 440, :duration 1}
                                                                                            :check-variable "yellow-box-selected"}}}},
                                       :purple-crayon-1    {:type      "image",
                                                            :x         664,
                                                            :y         541,
                                                            :scale     0.35
                                                            :rotation  90
                                                            :src       "/raw/img/categorize/purple_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "purple-color"
                                                                                              :target         "purple-crayon-1"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "purple-table"
                                                                                            :target         "purple-crayon-1"
                                                                                            :init-position  {:x 664, :y 541, :duration 1}
                                                                                            :check-variable "purple-table-selected"}}}},
                                       :purple-crayon-2    {:type      "image",
                                                            :x         1271,
                                                            :y         236,
                                                            :scale     0.35
                                                            :rotation  90
                                                            :src       "/raw/img/categorize/purple_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "purple-color"
                                                                                              :target         "purple-crayon-2"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "purple-table"
                                                                                            :target         "purple-crayon-2"
                                                                                            :init-position  {:x 1271, :y 236, :duration 1}
                                                                                            :check-variable "purple-table-selected"}}}},
                                       :purple-crayon-3    {:type      "image",
                                                            :x         1418,
                                                            :y         310,
                                                            :scale     0.35
                                                            :rotation  90
                                                            :src       "/raw/img/categorize/purple_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "purple-color"
                                                                                              :target         "purple-crayon-3"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "purple-table"
                                                                                            :target         "purple-crayon-3"
                                                                                            :init-position  {:x 1418, :y 310, :duration 1}
                                                                                            :check-variable "purple-table-selected"}}}},
                                       :red-crayon-1       {:type      "image",
                                                            :x         924,
                                                            :y         500,
                                                            :rotation  -90,
                                                            :scale     0.35,
                                                            :src       "/raw/img/categorize/red_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "red-color"
                                                                                              :target         "red-crayon-1"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "red-box"
                                                                                            :target         "red-crayon-1"
                                                                                            :init-position  {:x 924, :y 500, :duration 1}
                                                                                            :check-variable "red-box-selected"}}}},
                                       :red-crayon-2       {:type      "image",
                                                            :x         1618,
                                                            :y         958,
                                                            :rotation  -90,
                                                            :scale     0.35,
                                                            :src       "/raw/img/categorize/red_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "red-color"
                                                                                              :target         "red-crayon-2"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "red-box"
                                                                                            :target         "red-crayon-2"
                                                                                            :init-position  {:x 1618, :y 958, :duration 1}
                                                                                            :check-variable "red-box-selected"}}}}
                                       :red-crayon-3       {:type      "image",
                                                            :x         1548,
                                                            :y         164,
                                                            :rotation  -90,
                                                            :scale     0.35,
                                                            :src       "/raw/img/categorize/red_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "red-color"
                                                                                              :target         "red-crayon-3"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "red-box"
                                                                                            :target         "red-crayon-3"
                                                                                            :init-position  {:x 1548, :y 164, :duration 1}
                                                                                            :check-variable "red-box-selected"}}}}
                                       :green-crayon-1     {:type      "image",
                                                            :x         714,
                                                            :y         210,
                                                            :rotation  90
                                                            :scale     0.35
                                                            :src       "/raw/img/categorize/green_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "green-color"
                                                                                              :target         "green-crayon-1"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "green-table"
                                                                                            :target         "green-crayon-1"
                                                                                            :init-position  {:x 714, :y 210, :duration 1}
                                                                                            :check-variable "green-table-selected"}}}},
                                       :green-crayon-2     {:type      "image",
                                                            :x         1418,
                                                            :y         818,
                                                            :rotation  90
                                                            :scale     0.35
                                                            :src       "/raw/img/categorize/green_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "green-color"
                                                                                              :target         "green-crayon-2"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "green-table"
                                                                                            :target         "green-crayon-2"
                                                                                            :init-position  {:x 1418, :y 818, :duration 1}
                                                                                            :check-variable "green-table-selected"}}}}
                                       :green-crayon-3     {:type      "image",
                                                            :x         1678,
                                                            :y         364,
                                                            :rotation  90
                                                            :scale     0.35
                                                            :src       "/raw/img/categorize/green_crayons.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-color     "green-color"
                                                                                              :target         "green-crayon-3"}}
                                                                        :drag-end {:id     "drag-crayon",
                                                                                   :on     "drag-end",
                                                                                   :type   "action",
                                                                                   :params {:box            "green-table"
                                                                                            :target         "green-crayon-3"
                                                                                            :init-position  {:x 1678, :y 364, :duration 1}
                                                                                            :check-variable "green-table-selected"}}}}
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
                                                          :actions    {:click {:id "tap-instructions" :on "click" :type "action"}}}}
                       :scene-objects [["layered-background"]
                                       ["orange-table" "green-table" "purple-table"]
                                       ["librarian"]
                                       ["yellow-box" "blue-box" "red-box"]
                                       ["blue-crayon-1" "blue-crayon-2" "blue-crayon-3"]
                                       ["yellow-crayon-1" "yellow-crayon-2" "yellow-crayon-3"]
                                       ["purple-crayon-1" "purple-crayon-2" "purple-crayon-3"]
                                       ["red-crayon-1" "red-crayon-2" "red-crayon-3"]
                                       ["green-crayon-1" "green-crayon-2" "green-crayon-3"]
                                       ["orange-crayon-1" "orange-crayon-2" "orange-crayon-3"]],
                       :actions       {:crayon-in-right-box            {:type        "set-attribute", :attr-name "visible" :attr-value false
                                                                        :from-params [{:action-property "target" :param-property "target"}]},
                                       :crayon-revert                  {:type        "transition",
                                                                        :from-params [{:action-property "transition-id" :param-property "target"}
                                                                                      {:action-property "to" :param-property "init-position"}]}
                                       :wrong-option                   {:type "parallel",
                                                                        :data [{:id "unhighlight-all" :type "action"}
                                                                               {:id "crayon-revert", :type "action"}
                                                                               {:id "wrong-answer", :type "action"}],}
                                       :correct-option                 {:type "sequence-data",
                                                                        :data [{:id "unhighlight-all" :type "action"}
                                                                               {:type "counter" :counter-action "increase" :counter-id "sorted-crayons"}
                                                                               {:id "crayon-in-right-box", :type "action"}
                                                                               {:id "correct-answer", :type "action"}
                                                                               {:type       "test-var-inequality"
                                                                                :var-name   "sorted-crayons",
                                                                                :value      18,
                                                                                :inequality ">=",
                                                                                :success    "scene-question"}]}
                                       :drag-crayon                    {:type "sequence-data"
                                                                        :data [{:type        "copy-variable",
                                                                                :var-name    "current-selection-state"
                                                                                :from-params [{:param-property "check-variable", :action-property "from"}]}
                                                                               {:type "set-variable", :var-name "say", :var-value false}
                                                                               {:type "set-variable", :var-name "next-check-collide", :var-value false}

                                                                               {:type     "test-var-scalar",
                                                                                :success  "correct-option",
                                                                                :fail     "wrong-option",
                                                                                :value    true,
                                                                                :var-name "current-selection-state"}]}
                                       :highlight                      {:type "sequence-data"
                                                                        :data [{:type        "set-variable",
                                                                                :var-value   true
                                                                                :from-params [{:action-property "var-name" :param-property "check-variable"}]}
                                                                               {:type        "set-attribute" :attr-name "highlight" :attr-value true
                                                                                :from-params [{:action-property "target" :param-property "transition"}]}]}
                                       :unhighlight                    {:type "sequence-data"
                                                                        :data [{:type        "set-variable",
                                                                                :var-value   false
                                                                                :from-params [{:action-property "var-name" :param-property "check-variable"}]}
                                                                               {:type        "set-attribute" :attr-name "highlight" :attr-value false
                                                                                :from-params [{:action-property "target" :param-property "transition"}]}]}
                                       :unhighlight-all     {:type "parallel"
                                                             :data [{:type "set-attribute" :attr-name "highlight" :attr-value false :target "yellow-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "blue-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "red-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "purple-table"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "orange-table"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "green-table"}]}
                                       :next-check-collide             {:type "sequence-data"
                                                                        :data [{:type     "set-timeout"
                                                                                :action   "check-collide"
                                                                                :interval 10}]}
                                       :check-collide                  {:type "sequence-data"
                                                                        :data [{:type          "test-transitions-and-pointer-collide",
                                                                                :success       "highlight",
                                                                                :fail          "unhighlight",
                                                                                :transitions   ["purple-table" "orange-table" "green-table"
                                                                                                "yellow-box" "blue-box" "red-box"]
                                                                                :action-params [{:check-variable "purple-table-selected"}
                                                                                                {:check-variable "orange-table-selected"}
                                                                                                {:check-variable "green-table-selected"}
                                                                                                {:check-variable "yellow-box-selected"}
                                                                                                {:check-variable "blue-box-selected"}
                                                                                                {:check-variable "red-box-selected"}]}
                                                                               {:type     "test-var-scalar",
                                                                                :success  "next-check-collide",
                                                                                :value    true,
                                                                                :var-name "next-check-collide"}]}
                                       :init-activity                  {:type "sequence-data"
                                                                        :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-crayons"}
                                                                               {:type "action" :id "intro"}]}
                                       :yellow-color                   (-> (dialog/default "Color yellow")
                                                                           (assoc :unique-tag "color"))
                                       :blue-color                     (-> (dialog/default "Color blue")
                                                                           (assoc :unique-tag "color"))
                                       :red-color                      (-> (dialog/default "Color red")
                                                                           (assoc :unique-tag "color"))
                                       :purple-color                   (-> (dialog/default "Color purple")
                                                                           (assoc :unique-tag "color"))
                                       :orange-color                   (-> (dialog/default "Color orange")
                                                                           (assoc :unique-tag "color"))
                                       :green-color                    (-> (dialog/default "Color green")
                                                                           (assoc :unique-tag "color"))
                                       :tap-instructions              (-> (dialog/default "Tap instructions")
                                                                          (assoc :unique-tag "instructions"))
                                       :say-color                      {:type "sequence-data"
                                                                        :data [{:type "action" :from-params [{:action-property "id"
                                                                                                              :param-property  "say-color"}]}
                                                                               {:type     "test-var-scalar",
                                                                                :success  "next-say",
                                                                                :value    true,
                                                                                :var-name "say"}]}
                                       :next-say                       {:type "sequence-data"
                                                                        :data [{:type     "set-timeout"
                                                                                :action   "say-color"
                                                                                :interval 100}]}
                                       :start-drag                     {:type "sequence-data"
                                                                        :data [{:type        "stop-transition"
                                                                                :from-params [{:action-property "id" :param-property "target"}]}
                                                                               {:type "set-variable", :var-name "purple-table-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "orange-table-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "green-table-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "yellow-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "blue-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "red-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "say", :var-value true}
                                                                               {:type "set-variable", :var-name "next-check-collide", :var-value true}
                                                                               {:id "next-say" :type "action"}
                                                                               {:id "next-check-collide" :type "action"}]}
                                       :intro                          (-> (dialog/default "Introduce task")
                                                                           (assoc :unique-tag "instructions"))
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
                                       :scene-question                 {:type        "show-question"
                                                                        :description "What is the same about these two groups?"
                                                                        :data        {:type       "type-1"
                                                                                      :text       "What is the same about these two groups?"
                                                                                      :chunks     [{:start 0, :end 4},
                                                                                                   {:start 5, :end 7},
                                                                                                   {:start 8, :end 11},
                                                                                                   {:start 12, :end 16},
                                                                                                   {:start 17, :end 22},
                                                                                                   {:start 23, :end 28},
                                                                                                   {:start 29, :end 32},
                                                                                                   {:start 33, :end 40}]
                                                                                      :success    "correct-answer-question",
                                                                                      :fail       "fail-answer-question"
                                                                                      :audio-data {:audio     ""
                                                                                                   :start     0,
                                                                                                   :duration  0,
                                                                                                   :animation "color",
                                                                                                   :fill      0x00B2FF
                                                                                                   :data      []}
                                                                                      :image      "/raw/img/categorize/question.png"
                                                                                      :background "/raw/img/bg.png"
                                                                                      :answers    {:data
                                                                                                   [{:text       "A. They are both red"
                                                                                                     :correct    false
                                                                                                     :chunks     [{:start 0, :end 2},
                                                                                                                  {:start 3, :end 7},
                                                                                                                  {:start 8, :end 11},
                                                                                                                  {:start 12, :end 16},
                                                                                                                  {:start 17, :end 20}]
                                                                                                     :audio-data {:audio     ""
                                                                                                                  :start     0,
                                                                                                                  :duration  0,
                                                                                                                  :animation "color",
                                                                                                                  :fill      0x00B2FF
                                                                                                                  :data      []}}
                                                                                                    {:text       "B. They are both crayons you can use to color"
                                                                                                     :chunks     [{:start 0, :end 2},
                                                                                                                  {:start 3, :end 7},
                                                                                                                  {:start 8, :end 11},
                                                                                                                  {:start 12, :end 16},
                                                                                                                  {:start 17, :end 24},
                                                                                                                  {:start 25, :end 28},
                                                                                                                  {:start 29, :end 32},
                                                                                                                  {:start 33, :end 36},
                                                                                                                  {:start 37, :end 39},
                                                                                                                  {:start 40, :end 45}],
                                                                                                     :audio-data {:audio     ""
                                                                                                                  :start     0,
                                                                                                                  :duration  0,
                                                                                                                  :animation "color",
                                                                                                                  :fill      0x00B2FF
                                                                                                                  :data      []}
                                                                                                     :correct    true}
                                                                                                    {:text       "C. They are both in their crayon boxes"
                                                                                                     :chunks     [{:start 0, :end 2},
                                                                                                                  {:start 3, :end 7},
                                                                                                                  {:start 8, :end 11},
                                                                                                                  {:start 12, :end 16},
                                                                                                                  {:start 17, :end 19},
                                                                                                                  {:start 20, :end 25},
                                                                                                                  {:start 26, :end 32},
                                                                                                                  {:start 33, :end 38}]
                                                                                                     :audio-data {:audio     ""
                                                                                                                  :start     0,
                                                                                                                  :duration  0,
                                                                                                                  :animation "color",
                                                                                                                  :fill      0x00B2FF
                                                                                                                  :data      []}
                                                                                                     :correct    false}
                                                                                                    {:text       "D. They are both books"
                                                                                                     :chunks     [{:start 0, :end 2},
                                                                                                                  {:start 3, :end 7},
                                                                                                                  {:start 8, :end 11},
                                                                                                                  {:start 12, :end 16},
                                                                                                                  {:start 17, :end 22}],
                                                                                                     :audio-data {:audio     ""
                                                                                                                  :start     0,
                                                                                                                  :duration  0,
                                                                                                                  :animation "color",
                                                                                                                  :fill      0x00B2FF
                                                                                                                  :data      []}
                                                                                                     :correct    false}]}}}
                                       :fail-answer-question           {:type "sequence-data",
                                                                        :data [{:type "action" :id "fail-answer-dialog"}],}
                                       :correct-answer-question        {:type "sequence-data",
                                                                        :data [{:type "empty" :duration 500}
                                                                               {:type "hide-question"}
                                                                               {:type "action" :id "correct-answer-dialog"}
                                                                               {:type "action" :id "technical-question-placeholder"}]}
                                       :skip-question                  {:type "sequence-data",
                                                                        :data [{:type "hide-question"}
                                                                               {:type "action" :id "technical-question-placeholder"}]}
                                       :technical-question-placeholder {:type "action" :id "finish-scene"}
                                       :correct-answer-dialog          (-> (dialog/default "Correct question answer")
                                                                           (assoc :tags ["instructions"]))
                                       :fail-answer-dialog             (-> (dialog/default "Incorrect question answer")
                                                                           (assoc :tags ["instructions"]))
                                       :finish-scene                   {:type "sequence-data",
                                                                        :data [{:type "remove-interval"
                                                                                :id   "check-collide-2"}
                                                                               {:type "action", :id "finish-activity"}]}
                                       :finish-activity                {:type "finish-activity"}
                                       :stop-activity                  {:type "stop-activity"}}

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
                                                              {:type      "dialog"
                                                               :action-id :tap-instructions}
                                                              {:type "question" :action-id :scene-question}
                                                              {:type      "dialog"
                                                               :action-id :correct-answer-dialog}
                                                              {:type      "dialog"
                                                               :action-id :fail-answer-dialog}]}
                                                     {:title "Round 2 - colors"
                                                      :nodes [{:type      "dialog"
                                                               :action-id :yellow-color}
                                                              {:type      "dialog"
                                                               :action-id :blue-color}
                                                              {:type      "dialog"
                                                               :action-id :red-color}
                                                              {:type      "dialog"
                                                               :action-id :purple-color}
                                                              {:type      "dialog"
                                                               :action-id :orange-color}
                                                              {:type      "dialog"
                                                               :action-id :green-color}]}]}})
