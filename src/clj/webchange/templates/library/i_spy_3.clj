(ns webchange.templates.library.i-spy-3
  (:require
    [webchange.templates.core :as core]))

(def m {:id          21
        :name        "I spy 3"
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
                        {:url "/raw/img/i-spy-2/icons/img-10.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy-2/icons/22.png", :size 10, :type "image"}
                        ],
        :objects       {:layered-background
                                      {:type       "layered-background",
                                       :background {:src "/raw/img/i-spy-3/background.png"},
                                       :decoration {:src "/raw/img/i-spy-3/decoration.png"},
                                       :surface    {:src "/raw/img/i-spy-3/surface.png"}
                                       }
                        :schedule
                                      {:type           "text",
                                       :x              1121,
                                       :y              623,
                                       :width          108,
                                       :height         73,
                                       :align          "center",
                                       :fill           "#FFFFFF",
                                       :font-family    "Staatliches",
                                       :font-size      32,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "    OPEN \n8AM-8PM",
                                       :vertical-align "middle",
                                       :visible        true
                                       :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "1"}}},
                                       },

                        :menu
                                      {:type           "text",
                                       :x              65,
                                       :y              858,
                                       :width          169,
                                       :height         62,
                                       :align          "center",
                                       :fill           "#B90000",
                                       :font-family    "AbrilFatface",
                                       :font-size      56,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "MENU",
                                       :vertical-align "middle",
                                       :visible        true,
                                       :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "2"}}},
                                       },
                        :park-name
                                      {:type           "text",
                                       :x              619,
                                       :y              249,
                                       :width          459,
                                       :height         82,
                                       :align          "center",
                                       :fill           "#333333",
                                       :font-family    "AbrilFatface",
                                       :font-size      60,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "Wordbury Park",
                                       :vertical-align "middle",
                                       :visible        true,
                                       },
                        :monument
                                      {:type    "image",
                                       :x       620,
                                       :y       455,
                                       :width   100,
                                       :height  100,
                                       :scale-x 2,
                                       :scale-y 3,
                                       :filter  "brighten",
                                       :src     "/raw/img/i-spy-3/icons/22.png",
                                       :actions {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "3"}}},
                                       },
                        :trash-bin-1
                                      {:type           "text",
                                       :x              1579,
                                       :y              718,
                                       :width          76,
                                       :height         24,
                                       :align          "center",
                                       :fill           "#FFFFFF",
                                       :font-family    "Staatliches",
                                       :font-size      24,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "TRASH",
                                       :vertical-align "middle",
                                       :visible        true
                                       :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "4"}}},
                                       },
                        :trash-bin-2
                                      {:type           "text",
                                       :x              1692,
                                       :y              718,
                                       :width          76,
                                       :height         24,
                                       :align          "center",
                                       :fill           "#FFFFFF",
                                       :font-family    "Staatliches",
                                       :font-size      24,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "RECYCLE",
                                       :vertical-align "middle",
                                       :visible        true
                                       :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "4"}}},
                                       },
                        :house-sign
                                      {:type    "image",
                                       :x       1330,
                                       :y       235,
                                       :width   100,
                                       :height  100,
                                       :filter  "brighten",
                                       :src     "/raw/img/i-spy-3/icons/22.png",
                                       :actions {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "5"}}},
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
                                       :x          700,
                                       :y          940,
                                       :width      72,
                                       :height     72,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-3/icons/img-2.png"}
                        :icon-2       {:type       "image",
                                       :x          812,
                                       :y          941,
                                       :width      72,
                                       :height     69,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-3/icons/img-4.png"}
                        :icon-3       {:type       "image",
                                       :x          930,
                                       :y          940,
                                       :width      61,
                                       :height     72,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-3/icons/img-6.png"}
                        :icon-4       {:type       "image",
                                       :x          1036,
                                       :y          940,
                                       :width      72,
                                       :height     72,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-3/icons/img-10.png"}
                        :icon-5       {:type       "image",
                                       :x          1148,
                                       :y          942,
                                       :width      72,
                                       :height     69,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-3/icons/img-8.png"}
                        :icon-br-1    {:type     "group",
                                       :x        0,
                                       :y        0,
                                       :actions  {},
                                       :children ["icon-br-im-1" "schedule-1"],
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},
                        :icon-br-im-1 {:type       "image",
                                       :x          700,
                                       :y          940,
                                       :width      72,
                                       :height     72,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-3/icons/img-1.png"
                                       }
                        :schedule-1
                                      {:type           "text",
                                       :x              717,
                                       :y              958,
                                       :width          37,
                                       :height         23,
                                       :align          "center",
                                       :fill           "#FFFFFF",
                                       :font-family    "Staatliches",
                                       :font-size      12,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "    OPEN \n8AM-8PM",
                                       :vertical-align "middle",
                                       :visible        true},
                        :icon-br-2    {:type     "group",
                                       :x        0,
                                       :y        0,
                                       :actions  {},
                                       :children ["icon-br-im-2" "menu-2"],
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},
                        :menu-2
                                      {:type           "text",
                                       :x              822,
                                       :y              946,
                                       :width          49,
                                       :height         17,
                                       :align          "center",
                                       :fill           "#B90000",
                                       :font-family    "Staatliches",
                                       :font-size      19,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "MENU",
                                       :vertical-align "middle",
                                       :visible        true
                                       },
                        :icon-br-im-2 {:type       "image",
                                       :x          810,
                                       :y          941,
                                       :width      72,
                                       :height     69,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-3/icons/img-3.png"}
                        :icon-br-3    {:type     "group",
                                       :x        0,
                                       :y        0,
                                       :actions  {},
                                       :children ["icon-br-im-3"],
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},


                        :icon-br-im-3 {:type       "image",
                                       :x          930,
                                       :y          940,
                                       :width      61,
                                       :height     72,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-3/icons/img-5.png"}

                        :icon-br-4    {:type     "group",
                                       :x        0,
                                       :y        0,
                                       :actions  {},
                                       :children ["icon-br-im-4" "trash-bin-4" "trash-bin-5"],
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},


                        :trash-bin-4
                                      {:type           "text",
                                       :x              1048,
                                       :y              985,
                                       :width          19,
                                       :height         9,
                                       :align          "center",
                                       :fill           "#FFFFFF",
                                       :font-family    "Staatliches",
                                       :font-size      7,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "TRASH",
                                       :vertical-align "middle",
                                       :visible        true}
                        :trash-bin-5
                                      {:type           "text",
                                       :x              1075,
                                       :y              985,
                                       :width          23,
                                       :height         9,
                                       :align          "center",
                                       :fill           "#FFFFFF",
                                       :font-family    "Staatliches",
                                       :font-size      7,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "TRASH",
                                       :vertical-align "middle",
                                       :visible        true}

                        :icon-br-im-4 {:type       "image",
                                       :x          1036,
                                       :y          940,
                                       :width      72,
                                       :height     19.36,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-3/icons/img-9.png"}
                        :icon-br-5    {:type     "group",
                                       :x        0,
                                       :y        0,
                                       :actions  {},
                                       :children ["icon-br-im-5"],
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},

                        :icon-br-im-5 {:type       "image",
                                       :x          1147,
                                       :y          940,
                                       :width      72,
                                       :height     69,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-3/icons/img-7.png"}
                        },
        :scene-objects [["layered-background"] ["schedule" "menu" "park-name" "house-sign" "trash-bin-1" "trash-bin-2" "monument"]
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
                                             :phrase             "schedule-dialog",
                                             :phrase-description "schedule description"}
                        :item-2-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "menu-dialog",
                                             :phrase-description "menu description"}
                        :item-3-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "monument-dialog",
                                             :phrase-description "Monument description"}
                        :item-4-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "trash-bin-dialog",
                                             :phrase-description "Trash bin description"}
                        :item-5-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "house-plate-dialog",
                                             :phrase-description "House plate description"}

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
  (:id m)
  m
  (partial f t))
