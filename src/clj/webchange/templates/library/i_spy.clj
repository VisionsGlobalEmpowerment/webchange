(ns webchange.templates.library.i-spy
  (:require
    [webchange.templates.core :as core]))

(def m {:id          12
        :name        "I spy"
        :tags        ["Direct Instruction"]
        :description "Some description of i spy mechanics and covered skills"
        :fields      []
        :options     {}})

(def t {:assets
                       [{:url "/raw/img/i-spy/background.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy/decoration.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy/surface.png", :size 10, :type "image"}],
        :objects       {:layered-background
                                      {:type       "layered-background",
                                       :background {:src "/raw/img/i-spy/background.png"},
                                       :decoration {:src "/raw/img/i-spy/decoration.png"},
                                       :surface    {:src "/raw/img/i-spy/surface.png"}
                                       }
                        :cap
                                      {:type           "text",
                                       :x              1555,
                                       :y              520,
                                       :width          200,
                                       :height         130,
                                       :align          "center",
                                       :fill           "#ff3857",
                                       :font-family    "Pacifico",
                                       :font-size      32,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "Wombats",
                                       :vertical-align "middle",
                                       :visible        true
                                       :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "3"}}},
                                       },
                        :newspaper
                                      {:type           "text",
                                       :x              210,
                                       :y              917,
                                       :width          235,
                                       :height         32,
                                       :align          "center",
                                       :fill           "#000000",
                                       :font-family    "AbrilFatface",
                                       :font-size      24,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "The Wordbury Times",
                                       :vertical-align "middle",
                                       :visible        true
                                       :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "5"}}},
                                       },
                        :fire-station
                                      {:type           "text",
                                       :x              1102,
                                       :y              346,
                                       :width          219,
                                       :height         63,
                                       :align          "center",
                                       :fill           "#CC001B",
                                       :font-family    "Staatliches",
                                       :font-size      50,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "FIRE STATION",
                                       :vertical-align "middle",
                                       :visible        true,
                                       :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "4"}}},
                                       },
                        :bakery
                                      {:type           "text",
                                       :x              392,
                                       :y              243,
                                       :width          181,
                                       :height         102,
                                       :align          "center",
                                       :fill           "#FFFFFF",
                                       :font-family    "Pacifico",
                                       :font-size      58,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "Bakery",
                                       :vertical-align "middle",
                                       :visible        true
                                       :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "1"}}},
                                       },
                        :plate
                                      {:type           "text",
                                       :x              759,
                                       :y              730,
                                       :width          104,
                                       :height         43,
                                       :align          "center",
                                       :fill           "#000000",
                                       :font-family    "Staatliches",
                                       :font-size      34,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "6TRJ244",
                                       :vertical-align "middle",
                                       :visible        true
                                       :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "2"}}},
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
                                       :src        "/raw/img/i-spy/icons/bakery_02.png"}
                        :icon-2       {:type       "image",
                                       :x          812,
                                       :y          960,
                                       :width      72,
                                       :height     32.62,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy/icons/car_plate_02.png"}
                        :icon-3       {:type       "image",
                                       :x          924,
                                       :y          957,
                                       :width      72,
                                       :height     38.04,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy/icons/hat_02.png"}
                        :icon-4       {:type       "image",
                                       :x          1036,
                                       :y          966,
                                       :width      72,
                                       :height     19.36,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy/icons/fire_station_02.png"}
                        :icon-5       {:type       "image",
                                       :x          1148,
                                       :y          942,
                                       :width      72,
                                       :height     67.62,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy/icons/newspaper_02.png"}
                        :icon-br-1    {:type     "group",
                                       :x        0,
                                       :y        0,
                                       :actions  {},
                                       :children ["icon-br-im-1" "bakery-1"],
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},
                        :icon-br-im-1 {:type       "image",
                                       :x          700,
                                       :y          940,
                                       :width      72,
                                       :height     72,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy/icons/bakery_01.png"
                                       }
                        :bakery-1
                                      {:type           "text",
                                       :x              708,
                                       :y              958,
                                       :width          53,
                                       :height         30,
                                       :align          "center",
                                       :fill           "#FFFFFF",
                                       :font-family    "Pacifico",
                                       :font-size      17,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "Bakery",
                                       :vertical-align "middle",
                                       :visible        true},
                        :icon-br-2    {:type     "group",
                                       :x        0,
                                       :y        0,
                                       :actions  {},
                                       :children ["icon-br-im-2" "plate-2"],
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},
                        :plate-2
                                      {:type           "text",
                                       :x              819,
                                       :y              964.31,
                                       :width          58,
                                       :height         24,
                                       :align          "center",
                                       :fill           "#000000",
                                       :font-family    "Staatliches",
                                       :font-size      19,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "6TRJ244",
                                       :vertical-align "middle",
                                       :visible        true

                                       },
                        :icon-br-im-2 {:type       "image",
                                       :x          812,
                                       :y          960,
                                       :width      72,
                                       :height     32.62,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy/icons/car_plate_01.png"}
                        :icon-br-3    {:type     "group",
                                       :x        0,
                                       :y        0,
                                       :actions  {},
                                       :children ["icon-br-im-3" "cap-3"],
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},
                        :cap-3
                                      {:type           "text",
                                       :x              947,
                                       :y              964,
                                       :width          38,
                                       :height         16,
                                       :align          "center",
                                       :fill           "#ff3857",
                                       :font-family    "Pacifico",
                                       :font-size      9,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "Wombats",
                                       :vertical-align "middle",
                                       :visible        true},

                        :icon-br-im-3 {:type       "image",
                                       :x          924,
                                       :y          957,
                                       :width      72,
                                       :height     38.04,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy/icons/hat_01.png"}

                        :icon-br-4    {:type     "group",
                                       :x        0,
                                       :y        0,
                                       :actions  {},
                                       :children ["icon-br-im-4" "fire-station-4"],
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},


                        :fire-station-4
                                      {:type           "text",
                                       :x              1045.5,
                                       :y              968.5,
                                       :width          53,
                                       :height         15,
                                       :align          "center",
                                       :fill           "#CC001B",
                                       :font-family    "Staatliches",
                                       :font-size      12,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "FIRE STATION",
                                       :vertical-align "middle",
                                       :visible        true}

                        :icon-br-im-4 {:type       "image",
                                       :x          1036,
                                       :y          966,
                                       :width      72,
                                       :height     19.36,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy/icons/fire_station_01.png"}
                        :icon-br-5    {:type     "group",
                                       :x        0,
                                       :y        0,
                                       :actions  {},
                                       :children ["icon-br-im-5" "newspaper-5"],
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},
                        :newspaper-5
                                      {:type           "text",
                                       :x              1168,
                                       :y              959,
                                       :width          44,
                                       :height         6,
                                       :align          "center",
                                       :fill           "#000000",
                                       :font-family    "AbrilFatface",
                                       :font-size      4.5,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "The Wordbury Times",
                                       :vertical-align "middle",
                                       :visible        true},

                        :icon-br-im-5 {:type       "image",
                                       :x          1148,
                                       :y          942,
                                       :width      72,
                                       :height     67.62,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy/icons/newspaper_01.png"}
                        },
        :scene-objects [["layered-background"] ["cap" "newspaper" "fire-station" "bakery" "plate"]
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
                        :finish-activity     {:type "sequence-data"
                                            :data [
                                                   {:type "set-variable", :var-name "finish-not-played" :var-value false}
                                                   {:type "action" :id "finish-dialog"}
                                                   {:type "finish-activity"},
                                                   ]}
                        :finish-dialog {:type               "sequence-data",
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
                                             :phrase             "bakery-dialog",
                                             :phrase-description "Bakery description"}
                        :item-2-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "plate-dialog",
                                             :phrase-description "Car plate description"}
                        :item-3-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "cap-dialog",
                                             :phrase-description "cap description"}
                        :item-4-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "fire-station-dialog",
                                             :phrase-description "Fire station description"}
                        :item-5-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "newspaper-dialog",
                                             :phrase-description "Newspaper description"}

                        :check-and-set-item {:type "sequence-data",
                                             :data [
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
                                                    {:type               "sequence-data",
                                                     :editor-type        "dialog",
                                                     :data               [{:type "sequence-data"
                                                                           :data [{:type "empty" :duration 0}
                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                     :phrase             "intro",
                                                     :phrase-description "Introduce task"}
                                                    ]}
                        },
        :triggers
                       {:back  {:on "back", :action "stop-activity"},
                        :start {:on "start", :action "intro"}},
        :metadata      {:autostart true}}

  )


(defn f
  [t args]
  t)

(core/register-template
  (:id m)
  m
  (partial f t))
