(ns webchange.templates.library.i-spy-2
  (:require
    [webchange.templates.core :as core]))

(def m {:id          20
        :name        "I spy 2"
        :tags        ["Direct Instruction"]
        :description "Some description of i spy mechanics and covered skills"
        :fields      []
        :options     {}})

(def t {:assets
                       [{:url "/raw/img/i-spy-2/background.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy-2/decoration.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy-2/surface.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy-2/icons/img-1.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy-2/icons/img-2.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy-2/icons/img-3.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy-2/icons/img-4.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy-2/icons/img-5.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy-2/icons/img-6.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy-2/icons/img-7.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy-2/icons/img-8.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy-2/icons/img-9.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy-2/icons/img-10.png", :size 10, :type "image"}],
        :objects       {:layered-background
                                      {:type       "layered-background",
                                       :background {:src "/raw/img/i-spy-2/background.png"},
                                       :decoration {:src "/raw/img/i-spy-2/decoration.png"},
                                       :surface    {:src "/raw/img/i-spy-2/surface.png"}
                                       }
                        :bus-stop
                                      {:type           "text",
                                       :x              564,
                                       :y              463,
                                       :width          138,
                                       :height         39,
                                       :align          "center",
                                       :fill           "#ff3857",
                                       :font-family    "Staatliches",
                                       :font-size      38,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "BUS STOP",
                                       :vertical-align "middle",
                                       :visible        true
                                       :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "1"}}},
                                       },
                        :t-shirt
                                      {:type           "text",
                                       :x              392,
                                       :y              536,
                                       :width          61,
                                       :height         78,
                                       :align          "center",
                                       :fill           "#FFFFFF",
                                       :font-family    "Staatliches",
                                       :font-size      36,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "BE \nKIND",
                                       :vertical-align "middle",
                                       :visible        true
                                       :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "2"}}},
                                       },
                        :sale-plate
                                      {:type           "text",
                                       :x              1287,
                                       :y              584,
                                       :width          113,
                                       :height         56,
                                       :align          "center",
                                       :fill           "#DC1111",
                                       :font-family    "Staatliches",
                                       :font-size      64,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "SALE",
                                       :vertical-align "middle",
                                       :visible        true,
                                       :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "3"}}},
                                       },
                        :stop-sign
                                      {:type           "text",
                                       :x              1661,
                                       :y              417,
                                       :width          116,
                                       :height         58,
                                       :align          "center",
                                       :fill           "#FFFFFF",
                                       :font-family    "Staatliches",
                                       :font-size      60,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "STOP",
                                       :vertical-align "middle",
                                       :visible        true
                                       :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "4"}}},
                                       },
                        :street-sign
                                      {:type           "text",
                                       :x              1634,
                                       :y              261,
                                       :width          209,
                                       :height         44,
                                       :align          "center",
                                       :fill           "#FFFFFF",
                                       :font-family    "Staatliches",
                                       :font-size      42,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "LETTER LANE",
                                       :vertical-align "middle",
                                       :visible        true
                                       :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "5"}}},
                                       },
                        :bar          {:type          "rectangle",
                                       :x             672,
                                       :y             912,
                                       :width         576,
                                       :height        128,
                                       :border-radius 24,
                                       :fill          0xFFFFFF
                                       },
                        :rectangle-1  {:type          "rectangle",
                                       :x             688,
                                       :y             928,
                                       :width         96,
                                       :height        96,
                                       :border-radius 16,
                                       :fill          0xE4E4E4,
                                       :states        {:gray {:fill 0xE4E4E4}, :highlighted {:fill 0x76C700}}
                                       },
                        :rectangle-2  {:type          "rectangle",
                                       :x             800,
                                       :y             928,
                                       :width         96,
                                       :height        96,
                                       :border-radius 16,
                                       :fill          0xE4E4E4,
                                       :states        {:gray {:fill 0xE4E4E4}, :highlighted {:fill 0x76C700}}
                                       },
                        :rectangle-3  {:type          "rectangle",
                                       :x             912,
                                       :y             928,
                                       :width         96,
                                       :height        96,
                                       :border-radius 16,
                                       :fill          0xE4E4E4,
                                       :states        {:gray {:fill 0xE4E4E4}, :highlighted {:fill 0x76C700}}
                                       },
                        :rectangle-4  {:type          "rectangle",
                                       :x             1024,
                                       :y             928,
                                       :width         96,
                                       :height        96,
                                       :border-radius 16,
                                       :fill          0xE4E4E4,
                                       :states        {:gray {:fill 0xE4E4E4}, :highlighted {:fill 0x76C700}}
                                       },
                        :rectangle-5  {:type          "rectangle",
                                       :x             1136,
                                       :y             928,
                                       :width         96,
                                       :height        96,
                                       :border-radius 16,
                                       :fill          0xE4E4E4,
                                       :states        {:gray {:fill 0xE4E4E4}, :highlighted {:fill 0x76C700}}
                                       }
                        :icon-1       {:type       "image",
                                       :x          713,
                                       :y          940,
                                       :width      47,
                                       :height     72,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-2/icons/img-2.png"}
                        :icon-2       {:type       "image",
                                       :x          812,
                                       :y          947,
                                       :width      72,
                                       :height     58,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-2/icons/img-4.png"}
                        :icon-3       {:type       "image",
                                       :x          924,
                                       :y          961,
                                       :width      72,
                                       :height     29,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-2/icons/img-6.png"}
                        :icon-4       {:type       "image",
                                       :x          1036,
                                       :y          940,
                                       :width      72,
                                       :height     19.36,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-2/icons/img-10.png"}
                        :icon-5       {:type       "image",
                                       :x          1148,
                                       :y          967,
                                       :width      72,
                                       :height     18,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-2/icons/img-8.png"}
                        :icon-br-1    {:type     "group",
                                       :x        0,
                                       :y        0,
                                       :actions  {},
                                       :children ["icon-br-im-1" "bus-stop-1"],
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},
                        :icon-br-im-1 {:type       "image",
                                       :x          713,
                                       :y          940,
                                       :width      47,
                                       :height     72,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-2/icons/img-1.png"
                                       }
                        :bus-stop-1
                                      {:type           "text",
                                       :x              715,
                                       :y              977,
                                       :width          43,
                                       :height         12,
                                       :align          "center",
                                       :fill           "#15254B",
                                       :font-family    "Staatliches",
                                       :font-size      12,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "BUS STOP",
                                       :vertical-align "middle",
                                       :visible        true},
                        :icon-br-2    {:type     "group",
                                       :x        0,
                                       :y        0,
                                       :actions  {},
                                       :children ["icon-br-im-2" "t-shirt-2"],
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},
                        :t-shirt-2
                                      {:type           "text",
                                       :x              823,
                                       :y              967,
                                       :width          58,
                                       :height         24,
                                       :align          "center",
                                       :fill           "#FFFFFF",
                                       :font-family    "Staatliches",
                                       :font-size      19,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "BE \nKIND",
                                       :vertical-align "middle",
                                       :visible        true

                                       },
                        :icon-br-im-2 {:type       "image",
                                       :x          812,
                                       :y          947,
                                       :width      72,
                                       :height     58,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-2/icons/img-3.png"}
                        :icon-br-3    {:type     "group",
                                       :x        0,
                                       :y        0,
                                       :actions  {},
                                       :children ["icon-br-im-3" "sale-plate-3"],
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},
                        :sale-plate-3
                                      {:type           "text",
                                       :x              930,
                                       :y              964,
                                       :width          48,
                                       :height         23,
                                       :align          "center",
                                       :fill           "#DC1111",
                                       :font-family    "Staatliches",
                                       :font-size      28,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "SALE",
                                       :vertical-align "middle",
                                       :visible        true},

                        :icon-br-im-3 {:type       "image",
                                       :x          924,
                                       :y          961,
                                       :width      72,
                                       :height     29,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-2/icons/img-5.png"}

                        :icon-br-4    {:type     "group",
                                       :x        0,
                                       :y        0,
                                       :actions  {},
                                       :children ["icon-br-im-4" "stop-sign-4"],
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},


                        :stop-sign-4
                                      {:type           "text",
                                       :x              1047,
                                       :y              962,
                                       :width          52,
                                       :height         27,
                                       :align          "center",
                                       :fill           "#FFFFFF",
                                       :font-family    "Staatliches",
                                       :font-size      28,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "STOP",
                                       :vertical-align "middle",
                                       :visible        true}

                        :icon-br-im-4 {:type       "image",
                                       :x          1036,
                                       :y          940,
                                       :width      72,
                                       :height     19.36,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-2/icons/img-9.png"}
                        :icon-br-5    {:type     "group",
                                       :x        0,
                                       :y        0,
                                       :actions  {},
                                       :children ["icon-br-im-5" "street-sign-5"],
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},
                        :street-sign-5
                                      {:type           "text",
                                       :x              1165,
                                       :y              971,
                                       :width          47,
                                       :height         10,
                                       :align          "center",
                                       :fill           "#FFFFFF",
                                       :font-family    "Staatliches",
                                       :font-size      10,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "LETTER LANE",
                                       :vertical-align "middle",
                                       :visible        true},

                        :icon-br-im-5 {:type       "image",
                                       :x          1148,
                                       :y          967,
                                       :width      72,
                                       :height     18,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-2/icons/img-7.png"}
                        },
        :scene-objects [["layered-background"] ["bus-stop" "t-shirt" "sale-plate" "street-sign" "stop-sign"]
                        ["bar" "rectangle-1" "rectangle-2" "rectangle-3" "rectangle-4" "rectangle-5"]
                        ["icon-1" "icon-2" "icon-3" "icon-4" "icon-5"]
                        ["icon-br-1" "icon-br-2" "icon-br-3" "icon-br-4" "icon-br-5"]
                        ],
        :actions       {
                        :init-items         {:type "sequence-data",
                                             :data [
                                                    {:type "set-variable", :var-name "item-1", :var-value false}
                                                    {:type "set-variable", :var-name "item-2", :var-value false}
                                                    {:type "set-variable", :var-name "item-3", :var-value false}
                                                    {:type "set-variable", :var-name "item-4", :var-value false}
                                                    {:type "set-variable", :var-name "item-5", :var-value false}
                                                    {:type "set-variable", :var-name "finish-not-played", :var-value true}
                                                    ]
                                             }
                        :test-complete
                                            {:type      "test-var-list",
                                             :success   "finish-activity",
                                             :values    [true true true true true true],
                                             :var-names ["item-1" "item-2" "item-3" "item-4" "item-5" "finish-not-played"]},
                        :finish-activity    {:type "sequence-data"
                                             :data [
                                                    {:type "set-variable", :var-name "finish-not-played" :var-value false}
                                                    {:type "action" :id "finish-dialog"}
                                                    {:type "finish-activity"},
                                                    ]}
                        :finish-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "finish-dialog",
                                             :phrase-description "Everything done"}

                        :item-1-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "bus-stop-dialog",
                                             :phrase-description "Bus stop description"}
                        :item-2-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "t-shirt-dialog",
                                             :phrase-description "T-shirt description"}
                        :item-3-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "sale-plate-dialog",
                                             :phrase-description "Sale plate description"}
                        :item-4-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "stop-sign-dialog",
                                             :phrase-description "Stop sign description"}
                        :item-5-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "street-sign-dialog",
                                             :phrase-description "Street sign description"}

                        :check-and-set-item {:type       "sequence-data",
                                             :data       [
                                                          {:id          "highlighted",
                                                           :type        "state",
                                                           :from-params [{
                                                                          :template        "rectangle-%",
                                                                          :param-property  "target",
                                                                          :action-property "target"}
                                                                         ]
                                                           }
                                                          {:id          "show",
                                                           :type        "state",
                                                           :from-params [{
                                                                          :template        "icon-br-%",
                                                                          :param-property  "target",
                                                                          :action-property "target"}
                                                                         ]
                                                           }
                                                          {:type        "set-variable",
                                                           :var-value   true
                                                           :from-params [{
                                                                          :template        "item-%",
                                                                          :param-property  "target",
                                                                          :action-property "var-name"}
                                                                         ]
                                                           }
                                                          {:type        "action",
                                                           :from-params [{
                                                                          :template        "item-%-dialog",
                                                                          :param-property  "target",
                                                                          :action-property "id"}
                                                                         ]
                                                           }
                                                          {:type "action"
                                                           :id   "test-complete"}
                                                          ]
                                             :unique-tag "speech"
                                             }

                        :stop-activity      {:type "stop-activity", :id "home"},
                        :intro              {:type "sequence-data",
                                             :data [
                                                    {:type "action" :id "init-items"}
                                                    {:type "action" :id "intro-dialog"}
                                                    ]}
                        :intro-dialog       {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "intro",
                                             :phrase-description "Introduce task"}
                        },
        :triggers
                       {:back  {:on "back", :action "stop-activity"},
                        :start {:on "start", :action "intro"}},
        :metadata      {:autostart true}})


(defn f
  [t args]
  t)

(core/register-template
  m
  (partial f t))
