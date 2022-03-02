(ns webchange.templates.library.i-spy-3
  (:require
    [webchange.templates.core :as core]))

(def m {:id          21
        :name        "I spy 3"
        :tags        ["Guided Practice"]
        :description "Some description of i spy mechanics and covered skills"
        :fields      []
        :options     {}})

(def t {:assets
        [{:url "/raw/img/i-spy-3/background.png", :size 10, :type "image"}
         {:url "/raw/img/i-spy-3/decoration.png", :size 10, :type "image"}
         {:url "/raw/img/i-spy-3/surface.png", :size 10, :type "image"}
         {:url "/raw/img/i-spy-3/icons/img-1.png", :size 10, :type "image"}
         {:url "/raw/img/i-spy-3/icons/img-2.png", :size 10, :type "image"}
         {:url "/raw/img/i-spy-3/icons/img-3.png", :size 10, :type "image"}
         {:url "/raw/img/i-spy-3/icons/img-4.png", :size 10, :type "image"}
         {:url "/raw/img/i-spy-3/icons/img-5.png", :size 10, :type "image"}
         {:url "/raw/img/i-spy-3/icons/img-6.png", :size 10, :type "image"}
         {:url "/raw/img/i-spy-3/icons/img-7.png", :size 10, :type "image"}
         {:url "/raw/img/i-spy-3/icons/img-8.png", :size 10, :type "image"}
         {:url "/raw/img/i-spy-3/icons/img-9.png", :size 10, :type "image"}
         {:url "/raw/img/i-spy-3/icons/img-10.png", :size 10, :type "image"}
         {:url "/raw/img/i-spy-3/icons/22.png", :size 10, :type "image"}
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
                         :font-family    "Roboto",
                         :font-size      32,
                         :scale-x        1,
                         :scale-y        1,
                         :text           "    OPEN \n8AM-8PM",
                         :vertical-align "middle",
                         :visible        true
                         :editable?      {:select true}
                         :actions        {}
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
                         :editable?      {:select true}
                         :actions        {}
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
                         :editable?      {:select true}
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
                        :monument-text
                        {:type           "text",
                         :align          "center",
                         :fill           "#83683E",
                         :font-family    "Roboto",
                         :font-size      24,
                         :scale-x        1,
                         :scale-y        1,
                         :text           "Maya\nAngelou",
                         :vertical-align "middle",
                         :visible        true,
                         :x       675,
                         :skew-y  -0.12,
                         :y       545,
                         :width   100,
                         :height  100,
                         :editable?      {:select true}
                         },
                        :trash-bin-1
                        {:type           "text",
                         :x              1579,
                         :y              718,
                         :width          76,
                         :height         24,
                         :align          "center",
                         :fill           "#FFFFFF",
                         :font-family    "Roboto",
                         :font-size      20,
                         :scale-x        1,
                         :scale-y        1,
                         :text           "TRASH",
                         :vertical-align "middle",
                         :visible        true,
                         :editable?      {:select true}
                         :actions        {}
                         },
                        :trash-bin-2
                        {:type           "text",
                         :x              1692,
                         :y              718,
                         :width          76,
                         :height         24,
                         :align          "center",
                         :fill           "#FFFFFF",
                         :font-family    "Roboto",
                         :font-size      20,
                         :scale-x        1,
                         :scale-y        1,
                         :text           "RECYCLE",
                         :vertical-align "middle",
                         :visible        true
                         :editable?      {:select true}
                         :actions        {}
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
                        :menu-rectangle  {:type          "transparent",
                                          :x             30,
                                          :y             830,                                          
                                          :width         250,
                                          :height        250,
                                          :border-radius 0,
                                          :fill          0xFF0000
                                          :actions    {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "2"}}}
                                          },
                        :schedule-rectangle  {:type          "transparent",
                                              :x             1066,
                                              :y             582,
                                              :width         226,
                                              :height        136,
                                              :border-radius 0,
                                              :fill          0xFF0000
                                              :actions    {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "1"}}}
                                              },
                        :trash-rectangle  {:type          "transparent",
                                           :x             1554,
                                           :y             588,
                                           :width         119,
                                           :height        200,
                                           :border-radius 0,
                                           :fill          0xFF0000
                                           :actions    {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "4"}}}
                                           },
                        :recycle-rectangle  {:type          "transparent",
                                             :x              1675,
                                             :y              588,
                                             :width          119,
                                             :height         200,
                                             :border-radius 0,
                                             :fill          0xFF0000
                                             :actions    {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "4"}}}
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
                        :icon-max     {:type           "text",
                                       :align          "center",
                                       :fill           "#BF7424",
                                       :font-family    "Roboto",
                                       :font-size      36,
                                       :scale-x        1,
                                       :scale-y        1,
                                       :text           "MAX",
                                       :vertical-align "middle",
                                       :visible        true,
                                       :x          1545,
                                       :y          1008,
                                       :skew-y     0.1,
                                       :width      60,
                                       :height     60,
                                       :editable?      {:select true}
                                       :actions    {:click {:id "icon-max-click", :on "click", :type "action" :unique-tag "speech"}}}
                        :icon-park    {:type       "transparent",
                                       :x          575,
                                       :y          230,
                                       :width      530,
                                       :height     120,
                                       :actions    {:click {:id "icon-park-click", :on "click", :type "action" :unique-tag "speech"}}}
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
                        :icon-br-1    {:type       "image",
                                       :x          700,
                                       :y          940,
                                       :width      72,
                                       :height     72,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-3/icons/img-1.png"
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},
                        :icon-br-2    {:type       "image",
                                       :x          810,
                                       :y          941,
                                       :width      72,
                                       :height     69,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-3/icons/img-3.png"
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},
                        :icon-br-3    {:type       "image",
                                       :x          930,
                                       :y          940,
                                       :width      61,
                                       :height     72,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-3/icons/img-5.png"
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},
                        :icon-br-4    {:type       "image",
                                       :x          1036,
                                       :y          940,
                                       :width      72,
                                       :height     72,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-3/icons/img-9.png"
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},
                        :icon-br-5    {:type       "image",
                                       :x          1147,
                                       :y          940,
                                       :width      72,
                                       :height     69,
                                       :transition "door",
                                       :filter     "brighten",
                                       :src        "/raw/img/i-spy-3/icons/img-7.png"
                                       :visible  false,
                                       :states   {:show {:visible true}, :hide {:visible false}}},
                        },
        :scene-objects [["layered-background"] ["schedule" "menu" "park-name" "house-sign" "trash-bin-1" "trash-bin-2" "monument"]
                        ["bar" "menu-rectangle" "schedule-rectangle" "trash-rectangle" "recycle-rectangle" "rectangle-1" "rectangle-2" "rectangle-3" "rectangle-4" "rectangle-5"]
                        ["icon-1" "icon-2" "icon-3" "icon-4" "icon-5"]
                        ["icon-br-1" "icon-br-2" "icon-br-3" "icon-br-4" "icon-br-5"]
                        ["icon-max" "icon-park" "monument-text"]
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
                                             :phrase-description "Activity completion script"}

                        :icon-max-click      {:type               "sequence-data",
                                              :editor-type        "dialog",
                                              :data               [{:type "sequence-data"
                                                                    :data [{:type "empty" :duration 0}
                                                                           {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                              :phrase             "icon-max-click",
                                              :phrase-description "Max click"}
                        :icon-park-click      {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase             "icon-park-click",
                                               :phrase-description "Park name click"}

                        :item-1-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "schedule-dialog",
                                             :phrase-description "Park hours"}
                        :item-1-next-dialog      {:type               "sequence-data",
                                                  :editor-type        "dialog",
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                  :phrase             "schedule-next-dialog",
                                                  :phrase-description "Park hours next click"}
                        :item-2-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "menu-dialog",
                                             :phrase-description "menu description"}
                        :item-2-next-dialog      {:type               "sequence-data",
                                                  :editor-type        "dialog",
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                  :phrase             "menu-next-dialog",
                                                  :phrase-description "Menu description next click"}
                        :item-3-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "monument-dialog",
                                             :phrase-description "Statue"}
                        :item-3-next-dialog      {:type               "sequence-data",
                                                  :editor-type        "dialog",
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                  :phrase             "monument-next-dialog",
                                                  :phrase-description "Statue next click"}
                        :item-4-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "trash-bin-dialog",
                                             :phrase-description "Trash bin description"}
                        :item-4-next-dialog      {:type               "sequence-data",
                                                  :editor-type        "dialog",
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                  :phrase             "trash-bin-next-dialog",
                                                  :phrase-description "Trash bin description next click"}
                        :item-5-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "house-plate-dialog",
                                             :phrase-description "Building address number"}
                        :item-5-next-dialog      {:type               "sequence-data",
                                                  :editor-type        "dialog",
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                  :phrase             "house-plate-next-dialog",
                                                  :phrase-description "Building address number next click"}

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
                                                                          :action-property "target"}]}

                                                          {:type     "test-var-scalar",
                                                           :value false
                                                           :from-params [{:template        "item-%",
                                                                          :param-property  "target",
                                                                          :action-property "var-name"}
                                                                         {:template        "item-%-dialog",
                                                                          :param-property  "target",
                                                                          :action-property "success"}
                                                                         {:template        "item-%-next-dialog",
                                                                          :param-property  "target",
                                                                          :action-property "fail"}]},

                                                          {:type        "set-variable",
                                                           :var-value   true
                                                           :from-params [{
                                                                          :template        "item-%",
                                                                          :param-property  "target",
                                                                          :action-property "var-name"}]}
                                                          {:type "action"
                                                           :id   "test-complete"}
                                                          ]
                                             :unique-tag "speech"}
                        :stop-activity      {:type "stop-activity", :id "home"},
                        :intro              {:type "sequence-data",
                                             :data [{:type "start-activity"}
                                                    {:type "action" :id "init-items"}
                                                    {:type "action" :id "intro-dialog"}]
                                             :unique-tag "speech"}
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
        :metadata      {:autostart true
                        :tracks    [{:title "Dialogs"
                                     :nodes [{:type      "dialog"
                                              :action-id :intro-dialog}
                                             {:type      "dialog"
                                              :action-id :item-1-dialog}
                                             {:type      "dialog"
                                              :action-id :item-2-dialog}
                                             {:type      "dialog"
                                              :action-id :item-3-dialog}
                                             {:type      "dialog"
                                              :action-id :item-4-dialog}
                                             {:type      "dialog"
                                              :action-id :item-5-dialog}
                                             {:type      "dialog"
                                              :action-id :finish-dialog}]}
                                    {:title "Next time"
                                     :nodes [{:type      "dialog"
                                              :action-id :item-1-next-dialog}
                                             {:type      "dialog"
                                              :action-id :item-2-next-dialog}
                                             {:type      "dialog"
                                              :action-id :item-3-next-dialog}
                                             {:type      "dialog"
                                              :action-id :item-4-next-dialog}
                                             {:type      "dialog"
                                              :action-id :item-5-next-dialog}]}
                                    {:title "Other Print"
                                     :nodes [{:type      "dialog"
                                              :action-id :icon-max-click}
                                             {:type      "dialog"
                                              :action-id :icon-park-click}
                                             ]}
                                    ]
                        }})


(defn f
  [t args]
  t)

(core/register-template
  m
  (partial f t))
