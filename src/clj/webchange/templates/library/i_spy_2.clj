(ns webchange.templates.library.i-spy-2
  (:require
    [webchange.templates.core :as core]))

(def m {:id          20
        :name        "I spy 2"
        :tags        ["Guided Practice"]
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
                        :target-1           {:type      "transparent"
                                             :x         529
                                             :y         305
                                             :width     200
                                             :height    300
                                             :editable? {:drag true :select true :show-in-tree? true}
                                             :actions   {:click {:id "check-and-set-item" :on "click" :type "action" :params {:target "1"}}}}
                        :target-2           {:type      "transparent"
                                             :x         304
                                             :y         479
                                             :width     200
                                             :height    200
                                             :editable? {:drag true :select true :show-in-tree? true}
                                             :actions   {:click {:id "check-and-set-item" :on "click" :type "action" :params {:target "2"}}}}
                        :target-3           {:type      "transparent"
                                             :x         1259
                                             :y         511
                                             :width     200
                                             :height    200
                                             :editable? {:drag true :select true :show-in-tree? true}
                                             :actions   {:click {:id "check-and-set-item" :on "click" :type "action" :params {:target "3"}}}}
                        :target-4           {:type      "transparent"
                                             :x         1619
                                             :y         345
                                             :width     200
                                             :height    200
                                             :editable? {:drag true :select true :show-in-tree? true}
                                             :actions   {:click {:id "check-and-set-item" :on "click" :type "action" :params {:target "4"}}}}
                        :target-5           {:type      "transparent"
                                             :x         1530
                                             :y         229
                                             :width     380
                                             :height    100
                                             :editable? {:drag true :select true :show-in-tree? true}
                                             :actions   {:click {:id "check-and-set-item" :on "click" :type "action" :params {:target "5"}}}}
                        :bus-stop
                                            {:type           "text",
                                             :x              564,
                                             :y              463,
                                             :width          138,
                                             :height         39,
                                             :align          "center",
                                             :fill           "#ff3857",
                                             :font-family    "Roboto",
                                             :font-size      30,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "BUS STOP",
                                             :vertical-align "middle",
                                             :editable?      {:select true}
                                             :visible        true
                                             },
                        :t-shirt
                                            {:type           "text",
                                             :x              392,
                                             :y              536,
                                             :width          61,
                                             :height         78,
                                             :align          "center",
                                             :fill           "#FFFFFF",
                                             :font-family    "Roboto",
                                             :font-size      34,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "BE \nKIND",
                                             :vertical-align "middle",
                                             :visible        true
                                             :editable?      {:select true}
                                             },
                        :sale-plate
                                            {:type           "text",
                                             :x              1287,
                                             :y              584,
                                             :width          113,
                                             :height         56,
                                             :align          "center",
                                             :fill           "#DC1111",
                                             :font-family    "Roboto",
                                             :font-size      60,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "SALE",
                                             :vertical-align "middle",
                                             :visible        true,
                                             :editable?      {:select true}
                                             },
                        :stop-sign
                                            {:type           "text",
                                             :x              1661,
                                             :y              417,
                                             :width          116,
                                             :height         58,
                                             :align          "center",
                                             :fill           "#FFFFFF",
                                             :font-family    "Roboto",
                                             :font-size      50,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "STOP",
                                             :vertical-align "middle",
                                             :visible        true
                                             :editable?      {:select true}
                                             },
                        :street-sign
                                            {:type           "text",
                                             :x              1634,
                                             :y              261,
                                             :width          209,
                                             :height         44,
                                             :align          "center",
                                             :fill           "#FFFFFF",
                                             :font-family    "Roboto",
                                             :font-size      35,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "LETTER LANE",
                                             :vertical-align "middle",
                                             :editable?      {:select true}
                                             :visible        true
                                             },
                        :bar                {:type          "rectangle",
                                             :x             672,
                                             :y             912,
                                             :width         576,
                                             :height        128,
                                             :border-radius 24,
                                             :fill          0xFFFFFF
                                             },
                        :rectangle-1        {:type          "rectangle",
                                             :x             688,
                                             :y             928,
                                             :width         96,
                                             :height        96,
                                             :border-radius 16,
                                             :fill          0xE4E4E4,
                                             :states        {:gray {:fill 0xE4E4E4}, :highlighted {:fill 0x76C700}}
                                             },
                        :rectangle-2        {:type          "rectangle",
                                             :x             800,
                                             :y             928,
                                             :width         96,
                                             :height        96,
                                             :border-radius 16,
                                             :fill          0xE4E4E4,
                                             :states        {:gray {:fill 0xE4E4E4}, :highlighted {:fill 0x76C700}}
                                             },
                        :rectangle-3        {:type          "rectangle",
                                             :x             912,
                                             :y             928,
                                             :width         96,
                                             :height        96,
                                             :border-radius 16,
                                             :fill          0xE4E4E4,
                                             :states        {:gray {:fill 0xE4E4E4}, :highlighted {:fill 0x76C700}}
                                             },
                        :rectangle-4        {:type          "rectangle",
                                             :x             1024,
                                             :y             928,
                                             :width         96,
                                             :height        96,
                                             :border-radius 16,
                                             :fill          0xE4E4E4,
                                             :states        {:gray {:fill 0xE4E4E4}, :highlighted {:fill 0x76C700}}
                                             },
                        :rectangle-5        {:type          "rectangle",
                                             :x             1136,
                                             :y             928,
                                             :width         96,
                                             :height        96,
                                             :border-radius 16,
                                             :fill          0xE4E4E4,
                                             :states        {:gray {:fill 0xE4E4E4}, :highlighted {:fill 0x76C700}}
                                             }
                        :icon-1             {:type       "image",
                                             :x          713,
                                             :y          940,
                                             :width      47,
                                             :height     72,
                                             :transition "door",
                                             :filter     "brighten",
                                             :src        "/raw/img/i-spy-2/icons/img-2.png"}
                        :icon-2             {:type       "image",
                                             :x          812,
                                             :y          947,
                                             :width      72,
                                             :height     58,
                                             :transition "door",
                                             :filter     "brighten",
                                             :src        "/raw/img/i-spy-2/icons/img-4.png"}
                        :icon-3             {:type       "image",
                                             :x          924,
                                             :y          961,
                                             :width      72,
                                             :height     29,
                                             :transition "door",
                                             :filter     "brighten",
                                             :src        "/raw/img/i-spy-2/icons/img-6.png"}
                        :icon-4             {:type       "image",
                                             :x          1036,
                                             :y          940,
                                             :width      72,
                                             :height     72,
                                             :transition "door",
                                             :filter     "brighten",
                                             :src        "/raw/img/i-spy-2/icons/img-10.png"}
                        :icon-5             {:type       "image",
                                             :x          1148,
                                             :y          967,
                                             :width      72,
                                             :height     18,
                                             :transition "door",
                                             :filter     "brighten",
                                             :src        "/raw/img/i-spy-2/icons/img-8.png"}
                        :icon-post          {:type           "text",
                                             :align          "center",
                                             :fill           "#FFEA80",
                                             :font-family    "Roboto",
                                             :font-size      80,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "POST",
                                             :vertical-align "middle",
                                             :visible        true
                                             :x              75,
                                             :y              900,
                                             :width          210,
                                             :height         140,
                                             :editable?      {:select true}
                                             :actions        {:click {:id "icon-post-click", :on "click", :type "action"}}}
                        :icon-street-closed {:type           "text",
                                             :align          "center",
                                             :fill           "#EB9189",
                                             :font-family    "Roboto",
                                             :font-size      30,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "STREET\nCLOSED",
                                             :vertical-align "middle",
                                             :visible        true
                                             :x              1800,
                                             :y              660,
                                             :skew-y         0.05
                                             :width          120,
                                             :height         90,
                                             :editable?      {:select true}
                                             :actions        {:click {:id "icon-street-closed-click", :on "click", :type "action"}}}
                        :icon-2942          {:type           "text",
                                             :align          "center",
                                             :fill           "#15254B",
                                             :font-family    "Roboto",
                                             :font-size      24,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "2942",
                                             :vertical-align "middle",
                                             :visible        true
                                             :x              610,
                                             :y              508,
                                             :width          50,
                                             :height         20,
                                             :editable?      {:select true}
                                             :actions        {:click {:id "icon-2942-click", :on "click", :type "action"}}}
                        :icon-br-1          {:type       "image",
                                             :x          713,
                                             :y          940,
                                             :width      47,
                                             :height     72,
                                             :transition "door",
                                             :filter     "brighten",
                                             :src        "/raw/img/i-spy-2/icons/img-1.png"
                                             :visible    false,
                                             :states     {:show {:visible true}, :hide {:visible false}}},
                        :icon-br-2          {:type       "image",
                                             :x          812,
                                             :y          947,
                                             :width      72,
                                             :height     58,
                                             :transition "door",
                                             :filter     "brighten",
                                             :src        "/raw/img/i-spy-2/icons/img-3.png"
                                             :visible    false,
                                             :states     {:show {:visible true}, :hide {:visible false}}},
                        :icon-br-3          {:type       "image",
                                             :x          924,
                                             :y          961,
                                             :width      72,
                                             :height     29,
                                             :transition "door",
                                             :filter     "brighten",
                                             :src        "/raw/img/i-spy-2/icons/img-5.png"
                                             :visible    false,
                                             :states     {:show {:visible true}, :hide {:visible false}}},
                        :icon-br-4          {:type       "image",
                                             :x          1036,
                                             :y          940,
                                             :width      72,
                                             :height     72,
                                             :transition "door",
                                             :filter     "brighten",
                                             :src        "/raw/img/i-spy-2/icons/img-9.png"
                                             :visible    false,
                                             :states     {:show {:visible true}, :hide {:visible false}}},
                        :icon-br-5          {:type       "image",
                                             :x          1148,
                                             :y          967,
                                             :width      72,
                                             :height     18,
                                             :transition "door",
                                             :filter     "brighten",
                                             :src        "/raw/img/i-spy-2/icons/img-7.png"
                                             :visible    false,
                                             :states     {:show {:visible true}, :hide {:visible false}}},
                        },
        :scene-objects [["layered-background"]
                        ["bar" "rectangle-1" "rectangle-2" "rectangle-3" "rectangle-4" "rectangle-5"]
                        ["icon-1" "icon-2" "icon-3" "icon-4" "icon-5"]
                        ["icon-br-1" "icon-br-2" "icon-br-3" "icon-br-4" "icon-br-5"]
                        ["icon-post" "icon-street-closed" "icon-2942"]
                        ["target-1" "target-2" "target-3" "target-4" "target-5"]
                        ["bus-stop" "t-shirt" "sale-plate" "street-sign" "stop-sign"]
                        ],
        :actions       {
                        :init-items               {:type "sequence-data",
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
                        :finish-activity          {:type "sequence-data"
                                                   :data [
                                                          {:type "set-variable", :var-name "finish-not-played" :var-value false}
                                                          {:type "action" :id "finish-dialog"}
                                                          {:type "finish-activity"},
                                                          ]}
                        :finish-dialog            {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             "finish-dialog",
                                                   :phrase-description "Activity completion script"}
                        :item-1-next-dialog       {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             "bus-stop-next-dialog",
                                                   :phrase-description "Bus stop description next time"}

                        :item-1-dialog            {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             "bus-stop-dialog",
                                                   :phrase-description "Bus stop description"}
                        :item-2-dialog            {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             "t-shirt-dialog",
                                                   :phrase-description "T-shirt description"}
                        :item-2-next-dialog       {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             "t-shirt-next-dialog",
                                                   :phrase-description "T-shirt next click"}
                        :item-3-dialog            {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             "sale-plate-dialog",
                                                   :phrase-description "Sale sign"}
                        :item-3-next-dialog       {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             "sale-plate-next-dialog",
                                                   :phrase-description "Sale sign next click"}
                        :item-4-dialog            {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             "stop-sign-dialog",
                                                   :phrase-description "Stop sign description"}
                        :item-4-next-dialog       {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             "stop-sign-next-dialog",
                                                   :phrase-description "Stop sign next click"}
                        :item-5-dialog            {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             "street-sign-dialog",
                                                   :phrase-description "Street sign description"}
                        :item-5-next-dialog       {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             "street-sign-next-dialog",
                                                   :phrase-description "Street sign next click"}
                        :icon-post-click          {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             "icon-post-click",
                                                   :phrase-description "Post click"}
                        :icon-2942-click          {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             "icon-2942-click",
                                                   :phrase-description "Bus stop 2942 click"}
                        :icon-street-closed-click {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             "icon-street-closed-click",
                                                   :phrase-description "Street closed click"}

                        :check-and-set-item       {:type       "sequence-data",
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

                                                                {:type        "test-var-scalar",
                                                                 :value       false
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
                                                                ]}

                        :stop-activity            {:type "stop-activity", :id "home"},
                        :intro                    {:type       "sequence-data",
                                                   :data       [{:type "start-activity"}
                                                                {:type "action" :id "init-items"}
                                                                {:type "action" :id "intro-dialog"}]}
                        :intro-dialog             {:type               "sequence-data",
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
                                              :action-id :icon-post-click}
                                             {:type      "dialog"
                                              :action-id :icon-street-closed-click}
                                             {:type      "dialog"
                                              :action-id :icon-2942-click}]}
                                    ]
                        }})


(defn f
  [t args]
  t)

(core/register-template
  m
  (partial f t))
