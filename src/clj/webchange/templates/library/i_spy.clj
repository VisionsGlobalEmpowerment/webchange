(ns webchange.templates.library.i-spy
  (:require
    [webchange.templates.core :as core]))

(def m {:id          18
        :name        "I spy"
        :tags        ["Guided Practice"]
        :description "Some description of i spy mechanics and covered skills"
        :fields      []
        :options     {}})

(def t {:assets
                       [{:url "/raw/img/i-spy/background.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy/decoration.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy/surface.png", :size 10, :type "image"}
                        {:url "/raw/img/i-spy/icons/newspaper_01.png" :size 1 :type "image"}
                        {:url "/raw/img/i-spy/icons/newspaper_02.png" :size 1 :type "image"}
                        {:url "/raw/img/i-spy/icons/fire_station_01.png" :size 1 :type "image"}
                        {:url "/raw/img/i-spy/icons/fire_station_02.png" :size 1 :type "image"}
                        {:url "/raw/img/i-spy/icons/hat_01.png" :size 1 :type "image"}
                        {:url "/raw/img/i-spy/icons/hat_02.png" :size 1 :type "image"}
                        {:url "/raw/img/i-spy/icons/car_plate_01.png" :size 1 :type "image"}
                        {:url "/raw/img/i-spy/icons/car_plate_02.png" :size 1 :type "image"}
                        {:url "/raw/img/i-spy/icons/bakery_01.png" :size 1 :type "image"}
                        {:url "/raw/img/i-spy/icons/bakery_02.png" :size 1 :type "image"}
                        {:url "/raw/img/i-spy/transparent.png" :size 1 :type "image"}]
        :objects       {:layered-background
                                     {:type       "layered-background",
                                      :background {:src "/raw/img/i-spy/background.png"},
                                      :decoration {:src "/raw/img/i-spy/decoration.png"},
                                      :surface    {:src "/raw/img/i-spy/surface.png"}}
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
                                      :editable?      {:select true}
                                      :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "3"}}}}
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
                                      :editable?      {:select true}
                                      :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "5"}}},}
                        :fire-station
                                     {:type           "text",
                                      :x              1102,
                                      :y              346,
                                      :width          219,
                                      :height         63,
                                      :align          "center",
                                      :fill           "#CC001B",
                                      :font-family    "Roboto",
                                      :font-size      45,
                                      :scale-x        1,
                                      :scale-y        1,
                                      :text           "FIRE STATION",
                                      :vertical-align "middle",
                                      :visible        true,
                                      :editable?      {:select true}
                                      :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "4"}}},}
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
                                      :editable?      {:select true}
                                      :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "1"}}},}
                        :plate
                                     {:type           "text",
                                      :x              759,
                                      :y              730,
                                      :width          104,
                                      :height         43,
                                      :align          "center",
                                      :fill           "#000000",
                                      :font-family    "Roboto",
                                      :font-size      30,
                                      :scale-x        1,
                                      :scale-y        1,
                                      :text           "6TRJ244",
                                      :vertical-align "middle",
                                      :visible        true
                                      :editable?      {:select true}
                                      :actions        {:click {:id "check-and-set-item", :on "click", :type "action", :params {:target "2"}}},}
                        :bar         {:type          "rectangle",
                                      :x             672,
                                      :y             912,
                                      :width         576,
                                      :height        128,
                                      :border-radius 24,
                                      :fill          0xFFFFFF}
                        :rectangle-1 {:type          "rectangle",
                                      :x             688,
                                      :y             928,
                                      :width         96,
                                      :height        96,
                                      :border-radius 16,
                                      :fill          0xE4E4E4,
                                      :states        {:gray {:fill 0xE4E4E4}, :highlighted {:fill 0x76C700}}}
                        :rectangle-2 {:type          "rectangle",
                                      :x             800,
                                      :y             928,
                                      :width         96,
                                      :height        96,
                                      :border-radius 16,
                                      :fill          0xE4E4E4,
                                      :states        {:gray {:fill 0xE4E4E4}, :highlighted {:fill 0x76C700}}}
                        :rectangle-3 {:type          "rectangle",
                                      :x             912,
                                      :y             928,
                                      :width         96,
                                      :height        96,
                                      :border-radius 16,
                                      :fill          0xE4E4E4,
                                      :states        {:gray {:fill 0xE4E4E4}, :highlighted {:fill 0x76C700}}}
                        :rectangle-4 {:type          "rectangle",
                                      :x             1024,
                                      :y             928,
                                      :width         96,
                                      :height        96,
                                      :border-radius 16,
                                      :fill          0xE4E4E4,
                                      :states        {:gray {:fill 0xE4E4E4}, :highlighted {:fill 0x76C700}}}
                        :rectangle-5 {:type          "rectangle",
                                      :x             1136,
                                      :y             928,
                                      :width         96,
                                      :height        96,
                                      :border-radius 16,
                                      :fill          0xE4E4E4,
                                      :states        {:gray {:fill 0xE4E4E4}, :highlighted {:fill 0x76C700}}}
                        :icon-1      {:type       "image",
                                      :x          700,
                                      :y          940,
                                      :width      72,
                                      :height     72,
                                      :transition "door",
                                      :filter     "brighten",
                                      :src        "/raw/img/i-spy/icons/bakery_02.png"}
                        :icon-2      {:type       "image",
                                      :x          812,
                                      :y          960,
                                      :width      72,
                                      :height     32.62,
                                      :transition "door",
                                      :filter     "brighten",
                                      :src        "/raw/img/i-spy/icons/car_plate_02.png"}
                        :icon-3      {:type       "image",
                                      :x          924,
                                      :y          957,
                                      :width      72,
                                      :height     38.04,
                                      :transition "door",
                                      :filter     "brighten",
                                      :src        "/raw/img/i-spy/icons/hat_02.png"}
                        :icon-4      {:type       "image",
                                      :x          1036,
                                      :y          966,
                                      :width      72,
                                      :height     19.36,
                                      :transition "door",
                                      :filter     "brighten",
                                      :src        "/raw/img/i-spy/icons/fire_station_02.png"}
                        :icon-5      {:type       "image",
                                      :x          1148,
                                      :y          942,
                                      :width      72,
                                      :height     67.62,
                                      :transition "door",
                                      :filter     "brighten",
                                      :src        "/raw/img/i-spy/icons/newspaper_02.png"}
                        :icon-59     {:type           "text",
                                      :align          "center",
                                      :fill           "#FFB59C"
                                      :font-family    "Pacifico",
                                      :font-size      40,
                                      :scale-x        1,
                                      :scale-y        1,
                                      :text           "59",
                                      :vertical-align "middle",
                                      :visible        true
                                      :x              210,
                                      :y              405,
                                      :width          55,
                                      :height         55,
                                      :transition     "icon-59",
                                      :editable?      {:drag true, :select true},
                                      :actions        {:click {:id "icon-59-click", :on "click", :type "action" :unique-tag "speech"}}}
                        :icon-open   {:type           "text",
                                      :align          "center",
                                      :fill           "#FFC2CE"
                                      :font-family    "Pacifico",
                                      :font-size      32,
                                      :scale-x        1,
                                      :scale-y        1,
                                      :text           "Open",
                                      :vertical-align "middle",
                                      :visible        true
                                      :x              200,
                                      :y              605,
                                      :skew-y         -0.1,
                                      :width          95,
                                      :height         80,
                                      :transition     "icon-open",
                                      :editable?      {:drag true, :select true},
                                      :actions        {:click {:id "icon-open-click", :on "click", :type "action" :unique-tag "speech"}}}
                        :icon-276    {:type           "text",
                                      :align          "center",
                                      :fill           "#E7C6CB"
                                      :font-family    "Pacifico",
                                      :font-size      32,
                                      :scale-x        1,
                                      :scale-y        1,
                                      :text           "276",
                                      :vertical-align "middle",
                                      :visible        true
                                      :x              1485,
                                      :y              490,
                                      :width          43,
                                      :height         25,
                                      :transition     "icon-276",
                                      :editable?      {:drag true, :select true},
                                      :actions        {:click {:id "icon-276-click", :on "click", :type "action" :unique-tag "speech"}}}
                        :icon-br-1   {:type       "image",
                                      :x          700,
                                      :y          940,
                                      :width      72,
                                      :height     72,
                                      :transition "door",
                                      :filter     "brighten",
                                      :src        "/raw/img/i-spy/icons/bakery_01.png"
                                      :visible    false,
                                      :states     {:show {:visible true}, :hide {:visible false}}},
                        :icon-br-2   {:type       "image",
                                      :x          812,
                                      :y          960,
                                      :width      72,
                                      :height     32.62,
                                      :transition "door",
                                      :filter     "brighten",
                                      :src        "/raw/img/i-spy/icons/car_plate_01.png"
                                      :visible    false,
                                      :states     {:show {:visible true}, :hide {:visible false}}},
                        :icon-br-3   {:type       "image",
                                      :x          924,
                                      :y          957,
                                      :width      72,
                                      :height     38.04,
                                      :transition "door",
                                      :filter     "brighten",
                                      :src        "/raw/img/i-spy/icons/hat_01.png"
                                      :visible    false,
                                      :states     {:show {:visible true}, :hide {:visible false}}},
                        :icon-br-4   {:type       "image",
                                      :x          1036,
                                      :y          966,
                                      :width      72,
                                      :height     19.36,
                                      :transition "door",
                                      :filter     "brighten",
                                      :src        "/raw/img/i-spy/icons/fire_station_01.png"
                                      :visible    false,
                                      :states     {:show {:visible true}, :hide {:visible false}}},
                        :icon-br-5   {:type       "image",
                                      :x          1148,
                                      :y          942,
                                      :width      72,
                                      :height     67.62,
                                      :transition "door",
                                      :filter     "brighten",
                                      :src        "/raw/img/i-spy/icons/newspaper_01.png"
                                      :visible    false,
                                      :states     {:show {:visible true}, :hide {:visible false}}},
                        },
        :scene-objects [["layered-background"] ["cap" "newspaper" "fire-station" "bakery" "plate"]
                        ["bar" "rectangle-1" "rectangle-2" "rectangle-3" "rectangle-4" "rectangle-5"]
                        ["icon-1" "icon-2" "icon-3" "icon-4" "icon-5"]
                        ["icon-br-1" "icon-br-2" "icon-br-3" "icon-br-4" "icon-br-5"]
                        ["icon-59" "icon-open" "icon-276"]],
        :actions       {:init-items         {:type "sequence-data",
                                             :data [{:type "set-attribute", :target "icon-br-1", :attr-name "visible", :attr-value false}
                                                    {:type "set-attribute", :target "icon-br-2", :attr-name "visible", :attr-value false}
                                                    {:type "set-attribute", :target "icon-br-3", :attr-name "visible", :attr-value false}
                                                    {:type "set-attribute", :target "icon-br-4", :attr-name "visible", :attr-value false}
                                                    {:type "set-attribute", :target "icon-br-5", :attr-name "visible", :attr-value false}
                                                    {:type "set-variable", :var-name "item-1", :var-value false}
                                                    {:type "set-variable", :var-name "item-2", :var-value false}
                                                    {:type "set-variable", :var-name "item-3", :var-value false}
                                                    {:type "set-variable", :var-name "item-4", :var-value false}
                                                    {:type "set-variable", :var-name "item-5", :var-value false}
                                                    {:type "set-variable", :var-name "finish-not-played", :var-value true}]}
                        :test-complete
                                            {:type      "test-var-list",
                                             :success   "finish-activity",
                                             :values    [true true true true true true],
                                             :var-names ["item-1" "item-2" "item-3" "item-4" "item-5" "finish-not-played"]},
                        :finish-activity    {:type "sequence-data"
                                             :data [{:type "set-variable", :var-name "finish-not-played" :var-value false}
                                                    {:type "action" :id "finish-dialog"}
                                                    {:type "finish-activity"}]}
                        :finish-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "finish-dialog",
                                             :phrase-description "Activity completion script"}
                        :icon-59-click      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "icon-59-click",
                                             :phrase-description "59 click"}
                        :icon-open-click    {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "icon-open-click",
                                             :phrase-description "Bakery open click"}
                        :icon-276-click     {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "icon-276-click",
                                             :phrase-description "276 click"}

                        :item-1-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "bakery-dialog",
                                             :phrase-description "Bakery description"}
                        :item-1-next-dialog {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "bakery-next-dialog",
                                             :phrase-description "Bakery description next time"}
                        :item-2-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "plate-dialog",
                                             :phrase-description "Car plate description"}
                        :item-2-next-dialog {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "plate-next-dialog",
                                             :phrase-description "Car plate description next time"}
                        :item-3-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "cap-dialog",
                                             :phrase-description "Cap description"}
                        :item-3-next-dialog {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "cap-next-dialog",
                                             :phrase-description "Cap description next time"}
                        :item-4-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "fire-station-dialog",
                                             :phrase-description "Fire station description"}
                        :item-4-next-dialog {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "fire-station-next-dialog",
                                             :phrase-description "Fire station description next time"}
                        :item-5-dialog      {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "newspaper-dialog",
                                             :phrase-description "Newspaper description"}
                        :item-5-next-dialog {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "newspaper-next-dialog",
                                             :phrase-description "Newspaper description next time"}

                        :check-and-set-item {:type       "sequence-data",
                                             :data       [
                                                          {:id          "highlighted",
                                                           :type        "state",
                                                           :from-params [{
                                                                          :template        "rectangle-%",
                                                                          :param-property  "target",
                                                                          :action-property "target"}]}
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
                                                           :from-params [{:template        "item-%",
                                                                          :param-property  "target",
                                                                          :action-property "var-name"}]}
                                                          {:type "action"
                                                           :id   "test-complete"}]
                                             :unique-tag "speech"}

                        :stop-activity      {:type "stop-activity", :id "home"},
                        :intro              {:type       "sequence-data",
                                             :data       [{:type "start-activity"}
                                                          {:type "action" :id "init-items"}
                                                          {:type "action" :id "intro-dialog"}]
                                             :unique-tag "speech"}
                        :intro-dialog       {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase             "Introduce",
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
                                              :action-id :icon-59-click}
                                             {:type      "dialog"
                                              :action-id :icon-open-click}
                                             {:type      "dialog"
                                              :action-id :icon-276-click}]}
                                    ]
                        }}

  )


(defn f
  [t args]
  t)

(core/register-template
  m
  (partial f t))
