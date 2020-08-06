(ns webchange.editor-v2.scene-diagram.scene-parser.home-scene-source)

(def data {:assets        [{:url "/raw/audio/background/Casa.mp3", :size 10, :type "audio"}
                           {:url "/raw/audio/l1/a1/mari.mp4", :size 1, :type "audio", :alias "mari", :target "mari"}
                           {:url "/raw/audio/l1/a1/vera-casa-1.mp3", :size 2, :type "audio", :alias "vera 1", :target "vera"}
                           {:url "/raw/audio/l1/a1/vera-casa-2.mp3", :size 2, :type "audio", :alias "vera 2", :target "vera"}
                           {:url "/raw/audio/l1/a1/vera-casa-3.mp3", :size 2, :type "audio", :alias "vera 3", :target "vera"}
                           {:url "/raw/audio/effects/correct.mp3", :size 1, :type "audio"}
                           {:url "/raw/audio/effects/incorrect.mp3", :size 1, :type "audio"}
                           {:url "/raw/audio/effects/box-appear.mp3", :size 1, :type "audio"}
                           {:url "/raw/img/casa/background.jpg", :size 10, :type "image"}
                           {:url "/raw/img/casa_door.png", :size 1, :type "image"}
                           {:url "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a", :size 2, :type "audio", :alias "vaca voice 1", :target "vaca"}
                           {:url "/raw/audio/l1/a1/L1_A1_Vaca_Oso.m4a", :size 2, :type "audio", :alias "vaca voice 2", :target "vaca"}
                           {:url "/raw/audio/l1/a1/L1_A1_Vaca_Iman.m4a", :size 2, :type "audio", :alias "vaca voice 3", :target "vaca"}
                           {:url    "/raw/audio/l1/a1/L1_A1_Vaca_Diamente.m4a",
                            :size   2,
                            :type   "audio",
                            :alias  "vaca voice diamante",
                            :target "vaca"}
                           {:url    "/raw/audio/l1/a1/L1_A1_Vaca_Object_1_insertions.m4a",
                            :size   2,
                            :type   "audio",
                            :alias  "vaca insertions 1",
                            :target "vaca"}
                           {:url    "/raw/audio/l1/a1/L1_A1_Vaca_Object_2_insertions.m4a",
                            :size   2,
                            :type   "audio",
                            :alias  "vaca insertions 2",
                            :target "vaca"}
                           {:url    "/raw/audio/l1/a1/L1_A1_Vaca_Object_3_insertions.m4a",
                            :size   2,
                            :type   "audio",
                            :alias  "vaca insertions 3",
                            :target "vaca"}],
           :objects       {:background {:type "background", :src "/raw/img/casa/background.jpg"},
                           :box1
                                       {:type       "transparent",
                                        :x          505,
                                        :y          375,
                                        :width      772,
                                        :height     1032,
                                        :scale      {:x 0.3, :y 0.3},
                                        :origin     {:type "center-center"},
                                        :scene-name "box1",
                                        :transition "box1",
                                        :actions    {:click {:id "click-on-box1", :on "click", :type "action"}},
                                        :states
                                                    {:default {:type "transparent"},
                                                     :visible {:anim "come", :loop false, :name "boxes", :skin "qwestion", :type "animation", :speed 0.3, :start true}}},
                           :box2
                                       {:type       "transparent",
                                        :x          955,
                                        :y          375,
                                        :width      772,
                                        :height     1032,
                                        :scale      {:x 0.3, :y 0.3},
                                        :origin     {:type "center-center"},
                                        :scene-name "box2",
                                        :transition "box2",
                                        :actions    {:click {:id "click-on-box2", :on "click", :type "action"}},
                                        :states
                                                    {:default {:type "transparent"},
                                                     :visible {:anim "come", :loop false, :name "boxes", :skin "qwestion", :type "animation", :speed 0.3, :start true}}},
                           :box3
                                       {:type       "transparent",
                                        :x          1405,
                                        :y          375,
                                        :width      772,
                                        :height     1032,
                                        :scale      {:x 0.3, :y 0.3},
                                        :origin     {:type "center-center"},
                                        :scene-name "box3",
                                        :transition "box3",
                                        :actions    {:click {:id "click-on-box3", :on "click", :type "action"}},
                                        :states
                                                    {:default {:type "transparent"},
                                                     :visible {:anim "come", :loop false, :name "boxes", :skin "qwestion", :type "animation", :speed 0.3, :start true}}},
                           :door
                                       {:type       "image",
                                        :x          1146,
                                        :y          42,
                                        :width      732,
                                        :height     810,
                                        :transition "door",
                                        :filter     "brighten",
                                        :src        "/raw/img/casa/door.png"},
                           :door-trigger
                                       {:type    "transparent",
                                        :x       1146,
                                        :y       42,
                                        :width   732,
                                        :height  810,
                                        :actions {:click {:id "exit", :on "click", :type "action"}}},
                           :mari
                                       {:type        "animation",
                                        :x           2200,
                                        :y           311,
                                        :width       473,
                                        :height      511,
                                        :scene-name  "mari",
                                        :transition  "mari",
                                        :anim        "idle",
                                        :anim-offset {:x 0, :y -150},
                                        :name        "mari",
                                        :scale-x     0.5,
                                        :scale-y     0.5,
                                        :speed       0.35,
                                        :start       true},
                           :senora-vaca
                                       {:type    "animation",
                                        :x       655,
                                        :y       960,
                                        :width   351,
                                        :height  717,
                                        :scale   {:x 1, :y 1},
                                        :actions {:click {:id "restart", :on "click", :type "action"}},
                                        :anim    "idle",
                                        :name    "senoravaca",
                                        :skin    "vaca",
                                        :speed   0.3,
                                        :start   true},
                           :vera
                                       {:type   "animation",
                                        :x      1128,
                                        :y      960,
                                        :width  1800,
                                        :height 2558,
                                        :scale  {:x 0.2, :y 0.2},
                                        :anim   "idle",
                                        :name   "vera",
                                        :speed  0.3,
                                        :start  true}},
           :scene-objects [["background" "door"] ["vera" "senora-vaca" "mari" "door-trigger" "box1" "box2" "box3"]],
           :actions
                          {:audio-correct                    {:type "audio", :id "/raw/audio/effects/correct.mp3", :start 0, :duration 1.71},
                           :bye-current-box
                                                             {:type "sequence-data",
                                                              :data
                                                                    [{:data
                                                                            [{:id "jump", :type "animation", :from-var [{:var-name "current-box", :action-property "target"}]}
                                                                             {:to   {:y -100, :duration 2},
                                                                              :type "transition",
                                                                              :from-var
                                                                                    [{:var-name "current-box", :action-property "transition-id"}
                                                                                     {:var-name "current-position-x", :action-property "to.x"}]}],
                                                                      :type "parallel"}
                                                                     {:id "default", :type "state", :from-var [{:var-name "current-box", :action-property "target"}]}]},
                           :clear-instruction                {:type "remove-flows", :flow-tag "instruction"},
                           :click-on-box1
                                                             {:type "test-var-scalar", :var-name "current-box", :value "box1", :success "first-word", :fail "pick-wrong"},
                           :click-on-box2
                                                             {:type "test-var-scalar", :var-name "current-box", :value "box2", :success "second-word", :fail "pick-wrong"},
                           :click-on-box3
                                                             {:type "test-var-scalar", :var-name "current-box", :value "box3", :success "third-word", :fail "pick-wrong"},
                           :concept-chant
                                                             {:type               "sequence",
                                                              :data
                                                                                  ["vaca-say-3-times"
                                                                                   "vaca-3-times-var"
                                                                                   "empty-big"
                                                                                   "group-3-times-var"
                                                                                   "empty-small-2"
                                                                                   "vaca-once-more"
                                                                                   "empty-small"
                                                                                   "group-3-times-var"],
                                                              :phrase             "concept-chant",
                                                              :phrase-description "Chant concept",
                                                              :dialog-track       "Concept"},
                           :concept-intro
                                                             {:type               "sequence",
                                                              :data
                                                                                  ["vaca-this-is-var"
                                                                                   "empty-small"
                                                                                   "vaca-can-you-say"
                                                                                   "vaca-question-var"
                                                                                   "empty-small"
                                                                                   "vaca-word-var"
                                                                                   "empty-small-1"
                                                                                   "group-word-var"],
                                                              :phrase             "this-is-concept",
                                                              :phrase-description "Introduce concept",
                                                              :dialog-track       "Concept"},
                           :empty-big                        {:type "empty", :duration 1000},
                           :empty-small                      {:type "empty", :duration 500},
                           :empty-small-1                    {:type "empty", :duration 500},
                           :empty-small-2                    {:type "empty", :duration 500},
                           :exit                             {:type "sequence-data", :data [{:id "stop-activity", :type "action"} {:type "scene", :scene-id "map"}]},
                           :finish-activity                  {:type "finish-activity", :id "home"},
                           :first-word
                                                             {:type       "sequence",
                                                              :data
                                                                          ["show-first-box-word" "introduce-word" "bye-current-box" "set-current-box2" "senora-vaca-audio-touch-second-box"],
                                                              :unique-tag "box"},
                           :group-3-times-var
                                                             {:type "action", :from-var [{:var-name "current-word", :var-property "home-group-3-times-action"}]},
                           :group-word-var                   {:type "action", :from-var [{:var-name "current-word", :var-property "home-group-word-action"}]},
                           :intro
                                                             {:type "sequence",
                                                              :data
                                                                    ["empty-big"
                                                                     "start-activity"
                                                                     "renew-words"
                                                                     "senora-vaca-audio-1"
                                                                     "show-boxes"
                                                                     "wait-for-box-animations"
                                                                     "switch-box-animations-idle"
                                                                     "senora-vaca-audio-2"
                                                                     "set-current-box1"]},
                           :introduce-word
                                                             {:type "sequence",
                                                              :data
                                                                    ["audio-correct"
                                                                     "empty-big"
                                                                     "concept-intro"
                                                                     "empty-big"
                                                                     "concept-chant"
                                                                     "empty-small"
                                                                     "vaca-goodbye-var"
                                                                     "empty-big"]},
                           :mari-finish
                                                             {:type               "sequence-data",
                                                              :data
                                                                                  [{:to {:x 1403, :y 657, :duration 1.3}, :type "transition", :transition-id "mari"}
                                                                                   {:offset   0.745,
                                                                                    :phrase-text
                                                                                              "Tremendo trabajo pequeno genio! Quieres ver que hay dentro de la cajas otra vez? Toca a la Senora Vaca para poder ver adentro. Si no, toca a la puerta para ir al parque a tu proxima actividad.",
                                                                                    :start    0.745,
                                                                                    :type     "animation-sequence",
                                                                                    :duration 22.05,
                                                                                    :audio    "/raw/audio/l1/a1/mari.mp4",
                                                                                    :target   "mari",
                                                                                    :phrase-text-translated
                                                                                              "Tremendo trabajo pequeno genio! Quieres ver que hay dentro de la cajas otra vez? Toca a la Senora Vaca para poder ver adentro. Si no, toca a la puerta para ir al parque a tu proxima actividad.",
                                                                                    :track    1,
                                                                                    :data
                                                                                              [{:end 3.11, :anim "talk", :start 0.85}
                                                                                               {:end 6.59, :anim "talk", :start 3.6}
                                                                                               {:end 10.22, :anim "talk", :start 7.51}
                                                                                               {:end 16.01, :anim "talk", :start 10.7}
                                                                                               {:end 16.51, :anim "talk", :start 16.19}
                                                                                               {:end 18.61, :anim "talk", :start 17.23}
                                                                                               {:end 22.56, :anim "talk", :start 19.04}]}
                                                                                   {:type "set-variable", :var-name "restart", :var-value true}],
                                                              :phrase             "finish-activity",
                                                              :phrase-description "Repeat or go to the next activity"},
                           :pick-wrong
                                                             {:type               "sequence-data",
                                                              :data
                                                                                  [{:id          "/raw/audio/effects/incorrect.mp3",
                                                                                    :type        "audio",
                                                                                    :start       0,
                                                                                    :duration    0.527,
                                                                                    :phrase-text "Intenta de nuevo!"}],
                                                              :phrase-description "Wrong answer"},
                           :renew-words
                                                             {:type "lesson-var-provider", :from "concepts", :provider-id "words-set", :variables ["item-1" "item-2" "item-3"]},
                           :restart
                                                             {:type     "test-value",
                                                              :value1   true,
                                                              :success
                                                                        {:data
                                                                               [{:type "set-variable", :var-name "restart", :var-value false}
                                                                                {:to {:x 2200, :y 311, :duration 1.3}, :type "transition", :transition-id "mari"}
                                                                                {:type "clear-vars"}
                                                                                {:data
                                                                                       [{:id "default", :type "state", :target "box1"}
                                                                                        {:id "default", :type "state", :target "box2"}
                                                                                        {:id "default", :type "state", :target "box3"}],
                                                                                 :type "parallel"}
                                                                                {:id "intro", :type "action"}],
                                                                         :type "sequence-data"},
                                                              :from-var [{:var-name "restart", :action-property "value2"}]},
                           :second-word
                                                             {:type       "sequence",
                                                              :data
                                                                          ["show-second-box-word" "introduce-word" "bye-current-box" "set-current-box3" "senora-vaca-audio-touch-third-box"],
                                                              :unique-tag "box"},
                           :senora-vaca-anim-clapping-finish {:type "animation", :id "clapping_finish", :target "senoravaca", :loop false},
                           :senora-vaca-anim-clapping-start  {:type "animation", :id "clapping_start", :target "senoravaca", :loop false},
                           :senora-vaca-anim-idle            {:type "animation", :id "idle", :target "senoravaca"},
                           :senora-vaca-audio-1
                                                             {:type               "animation-sequence",
                                                              :data               [{:end 3.596, :anim "talk", :start 1.089}],
                                                              :phrase             "intro",
                                                              :phrase-description "Introduce activity",
                                                              :phrase-text        "Vamos a aprender nuevas palabras.",
                                                              :dialog-track       "Intro",
                                                              :target             "senoravaca",
                                                              :audio              "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a",
                                                              :start              1.089,
                                                              :duration           2.749,
                                                              :track              1,
                                                              :offset             1.089
                                                              :skippable          true},
                           :senora-vaca-audio-2
                                                             {:type               "animation-sequence",
                                                              :data
                                                                                  [{:end 5.484, :anim "talk", :start 4.395}
                                                                                   {:end 8.745, :anim "talk", :start 6.423}
                                                                                   {:end 10.342, :anim "talk", :start 9.607}
                                                                                   {:end 14.302, :anim "talk", :start 11.226}],
                                                              :phrase             "touch-first-box",
                                                              :phrase-description "Touch the first box",
                                                              :phrase-text        "Mira esto! Que podria estar dentro de las cajas? Vamos a ver adentro! Toca la primera caja par ver que hay adentro.",
                                                              :dialog-track       "Intro",
                                                              :target             "senoravaca",
                                                              :audio              "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a",
                                                              :start              4.395,
                                                              :duration           10.042,
                                                              :track              1,
                                                              :offset             4.395
                                                              :skippable          true},
                           :senora-vaca-audio-touch-second-box
                                                             {:type               "animation-sequence",
                                                              :data
                                                                                  [{:end 2.053, :anim "talk", :start 0.873}
                                                                                   {:end 4.952, :anim "talk", :start 2.794}
                                                                                   {:end 6.67, :anim "talk", :start 5.336}
                                                                                   {:end 9.136, :anim "talk", :start 7.697}],
                                                              :phrase             "touch-second-box",
                                                              :phrase-description "Touch the second box",
                                                              :phrase-text
                                                                                  "Muy bien! Vamos a ver que hay dentro de la segunda caja. Toco la siguiente caja para ver que hay adentro.",
                                                              :target             "senoravaca",
                                                              :audio              "/raw/audio/l1/a1/L1_A1_Vaca_Oso.m4a",
                                                              :start              0.587,
                                                              :duration           8.772,
                                                              :track              1,
                                                              :offset             0.587},
                           :senora-vaca-audio-touch-third-box
                                                             {:type               "animation-sequence",
                                                              :data
                                                                                  [{:end 1.844, :anim "talk", :start 0.844}
                                                                                   {:end 2.733, :anim "talk", :start 2.214}
                                                                                   {:end 5.674, :anim "talk", :start 2.928}],
                                                              :phrase             "touch-third-box",
                                                              :phrase-description "Touch the third box",
                                                              :phrase-text        "Muy bien! Ahora hay que ver dentro de la tercera caja. Toca la ultima caja para ver que hay adentro.",
                                                              :target             "senoravaca",
                                                              :audio              "/raw/audio/l1/a1/L1_A1_Vaca_Iman.m4a",
                                                              :start              0.76,
                                                              :duration           4.986,
                                                              :track              1,
                                                              :offset             0.76},
                           :set-current-box1                 {:type "set-variable", :var-name "current-box", :var-value "box1"},
                           :set-current-box2                 {:type "set-variable", :var-name "current-box", :var-value "box2"},
                           :set-current-box3                 {:type "set-variable", :var-name "current-box", :var-value "box3"},
                           :show-boxes
                                                             {:type "parallel",
                                                              :data
                                                                    [{:id "visible", :type "state", :target "box1"}
                                                                     {:id "visible", :type "state", :target "box2"}
                                                                     {:id "visible", :type "state", :target "box3"}]},
                           :show-first-box-word
                                                             {:type "parallel",
                                                              :data
                                                                    [{:id "wood", :loop false, :type "animation", :target "box1"}
                                                                     {:type       "set-slot",
                                                                      :target     "box1",
                                                                      :from-var   [{:var-name "item-1", :var-property "image-src", :action-property "image"}],
                                                                      :slot-name  "box1",
                                                                      :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                                     {:from "item-1", :type "copy-variable", :var-name "current-word"}
                                                                     {:id "idle_fly1", :loop true, :type "add-animation", :target "box1"}]},
                           :show-second-box-word
                                                             {:type "parallel",
                                                              :data
                                                                    [{:id "wood", :loop false, :type "animation", :target "box2"}
                                                                     {:type       "set-slot",
                                                                      :target     "box2",
                                                                      :from-var   [{:var-name "item-2", :var-property "image-src", :action-property "image"}],
                                                                      :slot-name  "box1",
                                                                      :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                                     {:from "item-2", :type "copy-variable", :var-name "current-word"}
                                                                     {:id "idle_fly2", :loop true, :type "add-animation", :target "box2"}]},
                           :show-third-box-word
                                                             {:type "parallel",
                                                              :data
                                                                    [{:id "wood", :loop false, :type "animation", :target "box3"}
                                                                     {:type       "set-slot",
                                                                      :target     "box3",
                                                                      :from-var   [{:var-name "item-3", :var-property "image-src", :action-property "image"}],
                                                                      :slot-name  "box1",
                                                                      :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                                     {:from "item-3", :type "copy-variable", :var-name "current-word"}
                                                                     {:id "idle_fly3", :loop true, :type "add-animation", :target "box3"}]},
                           :start-activity                   {:type "start-activity", :id "home"},
                           :start-background-music           {:type "audio", :id "/raw/audio/background/Casa.mp3", :loop true},
                           :stop-activity                    {:type "stop-activity", :id "home"},
                           :switch-box-animations-idle
                                                             {:type "parallel",
                                                              :data
                                                                    [{:id "idle_fly1", :loop true, :type "add-animation", :target "box1"}
                                                                     {:id "idle_fly2", :loop true, :type "add-animation", :target "box2"}
                                                                     {:id "idle_fly3", :loop true, :type "add-animation", :target "box3"}]},
                           :third-word
                                                             {:type       "sequence",
                                                              :data       ["show-third-box-word" "introduce-word" "bye-current-box" "mari-finish" "finish-activity"],
                                                              :unique-tag "box"},
                           :vaca-3-times-var                 {:type "action", :from-var [{:var-name "current-word", :var-property "home-vaca-3-times-action"}]},
                           :vaca-can-you-say
                                                             {:type        "animation-sequence",
                                                              :data        [{:end 12.62, :anim "talk", :start 11.75}],
                                                              :phrase-text "Puedes decir",
                                                              :target      "senoravaca",
                                                              :audio       "/raw/audio/l1/a1/L1_A1_Vaca_Iman.m4a",
                                                              :start       11.75,
                                                              :duration    0.935,
                                                              :track       1,
                                                              :offset      11.75},
                           :vaca-goodbye-var
                                                             {:type               "action",
                                                              :phrase             "concept-goodbye",
                                                              :phrase-description "Adios concepto",
                                                              :dialog-track       "Concept",
                                                              :from-var           [{:var-name "current-word", :var-property "home-vaca-goodbye-action"}]},
                           :vaca-once-more
                                                             {:type        "animation-sequence",
                                                              :data        [{:end 38.792, :anim "talk", :start 37.751}],
                                                              :phrase-text "Una vez mas!",
                                                              :target      "senoravaca",
                                                              :audio       "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a",
                                                              :start       37.751,
                                                              :duration    1.187,
                                                              :track       1,
                                                              :offset      37.751},
                           :vaca-question-var
                                                             {:type "action", :from-var [{:var-name "current-word", :var-property "home-vaca-question-action"}]},
                           :vaca-say-3-times
                                                             {:type        "animation-sequence",
                                                              :data        [{:end 25.513, :anim "talk", :start 25.152} {:end 27.2, :anim "talk", :start 25.853}],
                                                              :phrase-text "Bueno. Digalo tres veces",
                                                              :target      "senoravaca",
                                                              :audio       "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a",
                                                              :start       25.079,
                                                              :duration    2.381,
                                                              :track       1,
                                                              :offset      25.079},
                           :vaca-this-is-var                 {:type "action", :from-var [{:var-name "current-word", :var-property "home-vaca-this-is-action"}]},
                           :vaca-word-var                    {:type "action", :from-var [{:var-name "current-word", :var-property "home-vaca-word-action"}]},
                           :vera-anim-clapping-finish        {:type "animation", :id "clapping_finish", :target "vera", :loop false},
                           :vera-anim-clapping-start         {:type "animation", :id "clapping_start", :target "vera", :loop false},
                           :vera-anim-idle                   {:type "animation", :id "idle", :target "vera"},
                           :wait-for-box-animations          {:type "empty", :duration 500}},
           :triggers
                          {:back  {:on "back", :action "stop-activity"},
                           :music {:on "start", :action "start-background-music"},
                           :start {:on "start", :action "intro"}},
           :metadata      {:prev "map", :autostart true}})
