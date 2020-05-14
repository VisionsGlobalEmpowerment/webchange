(ns webchange.editor-v2.graph-builder.graph.phrases-graph--letter-intro-source)

(def data {:assets
                     [{:url "/raw/img/casa/background.jpg", :size 10, :type "image"}
                      {:url "/raw/img/casa/painting_canvas.png", :size 10, :type "image"}
                      {:url "/raw/img/elements/squirrel.png", :size 10, :type "image"}
                      {:url "/raw/audio/l2/a2/L2_A2_Vaca_1.m4a", :size 2, :type "audio", :alias "vaca 1"}
                      {:url "/raw/audio/l2/a2/L2_A2_Vaca_2.m4a", :size 2, :type "audio", :alias "vaca 2"}
                      {:url "/raw/audio/l2/a2/L2_A2_Vaca_3.m4a", :size 2, :type "audio", :alias "vaca 3"}
                      {:url "/raw/audio/l2/a2/L2_A2_Mari.m4a", :size 2, :type "audio", :alias "mari"}
                      {:url   "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_1.m4a",
                       :size  2,
                       :type  "audio",
                       :alias "vaca insertions 1"}
                      {:url   "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_2.m4a",
                       :size  2,
                       :type  "audio",
                       :alias "vaca insertions 2"}
                      {:url   "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_3.m4a",
                       :size  2,
                       :type  "audio",
                       :alias "vaca insertions 3"}
                      {:url   "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_4.m4a",
                       :size  2,
                       :type  "audio",
                       :alias "vaca insertions 4"}
                      {:url   "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_5.m4a",
                       :size  2,
                       :type  "audio",
                       :alias "vaca insertions 5"}
                      {:url   "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_6.m4a",
                       :size  2,
                       :type  "audio",
                       :alias "vaca insertions 6"}
                      {:url   "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_7.m4a",
                       :size  2,
                       :type  "audio",
                       :alias "vaca insertions 7"}
                      {:url   "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_8.m4a",
                       :size  2,
                       :type  "audio",
                       :alias "vaca insertions 8"}
                      {:url   "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_9.m4a",
                       :size  2,
                       :type  "audio",
                       :alias "vaca insertions 9"}
                      {:url   "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_10.m4a",
                       :size  2,
                       :type  "audio",
                       :alias "vaca insertions 10"}
                      {:url   "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_11.m4a",
                       :size  2,
                       :type  "audio",
                       :alias "vaca insertions 11"}
                      {:url "/raw/audio/l2/mari-chants.m4a", :size 5, :type "audio", :alias "mari chants"}
                      {:url "/raw/audio/l2/mari-letter-names.mp4", :size 5, :type "audio", :alias "mari letter names"}],
           :objects
                     {:background {:type "background", :src "/raw/img/casa/background.jpg"},
                      :canvas
                                  {:type    "image",
                                   :x       650,
                                   :y       130,
                                   :width   287,
                                   :height  430,
                                   :scale-x 2,
                                   :scale-y 2,
                                   :src     "/raw/img/casa/painting_canvas.png"},
                      :image
                                  {:type       "image",
                                   :x          1805,
                                   :y          502,
                                   :width      100,
                                   :height     100,
                                   :transition "image",
                                   :scale-x    1.3,
                                   :scale-y    1.3,
                                   :src        "",
                                   :states     {:init-position {:x 1805, :y 502}}},
                      :letter-big
                                  {:type           "transparent",
                                   :x              690,
                                   :y              240,
                                   :width          200,
                                   :height         130,
                                   :actions        {:click {:id "big-letter-click", :on "click", :type "action", :unique-tag "click"}},
                                   :align          "center",
                                   :fill           "#ef545c",
                                   :font-family    "Lato",
                                   :font-size      120,
                                   :states         {:big {:font-size 160}, :hidden {:type "transparent"}, :normal {:font-size 120}, :visible {:type "text"}},
                                   :text           "",
                                   :vertical-align "middle"},
                      :letter-small
                                  {:type           "transparent",
                                   :x              870,
                                   :y              240,
                                   :width          180,
                                   :height         130,
                                   :actions        {:click {:id "small-letter-click", :on "click", :type "action", :unique-tag "click"}},
                                   :align          "center",
                                   :fill           "#ef545c",
                                   :font-family    "Lato",
                                   :font-size      120,
                                   :states         {:big {:font-size 160}, :hidden {:type "transparent"}, :normal {:font-size 120}, :visible {:type "text"}},
                                   :text           "",
                                   :vertical-align "middle"},
                      :mari
                                  {:type       "animation",
                                   :x          1600,
                                   :y          635,
                                   :width      473,
                                   :height     511,
                                   :transition "mari",
                                   :anim       "idle",
                                   :name       "mari",
                                   :scale-x    0.5,
                                   :scale-y    0.5,
                                   :speed      0.35,
                                   :start      true},
                      :senora-vaca
                                  {:type    "animation",
                                   :x       380,
                                   :y       1000,
                                   :width   351,
                                   :height  717,
                                   :actions {:click {:id "vaca-click", :on "click", :type "action"}},
                                   :anim    "idle",
                                   :name    "senoravaca",
                                   :skin    "vaca",
                                   :speed   0.3,
                                   :start   true},
                      :word-1-first
                                  {:type        "transparent",
                                   :x           725,
                                   :y           410,
                                   :width       130,
                                   :height      90,
                                   :align       "right",
                                   :fill        "#ef545c",
                                   :font-family "Lato",
                                   :font-size   100,
                                   :font-weight "bold",
                                   :states
                                                {:big     {:y 390, :font-size 120},
                                                 :hidden  {:type "transparent"},
                                                 :normal  {:y 410, :font-size 100},
                                                 :visible {:type "text"}},
                                   :text        ""},
                      :word-1-rest
                                  {:type        "transparent",
                                   :x           855,
                                   :y           410,
                                   :width       390,
                                   :height      90,
                                   :fill        "#fc8e51",
                                   :font-family "Lato",
                                   :font-size   100,
                                   :states
                                                {:big     {:y 390, :font-size 120},
                                                 :hidden  {:type "transparent"},
                                                 :normal  {:y 410, :font-size 100},
                                                 :visible {:type "text"}},
                                   :text        ""},
                      :word-2-first
                                  {:type        "transparent",
                                   :x           705,
                                   :y           510,
                                   :width       150,
                                   :height      90,
                                   :align       "right",
                                   :fill        "#ef545c",
                                   :font-family "Lato",
                                   :font-size   100,
                                   :states
                                                {:big     {:y 490, :font-size 120},
                                                 :hidden  {:type "transparent"},
                                                 :normal  {:y 510, :font-size 100},
                                                 :visible {:type "text"}},
                                   :text        ""},
                      :word-2-rest
                                  {:type        "transparent",
                                   :x           855,
                                   :y           510,
                                   :width       390,
                                   :height      90,
                                   :fill        "#fc8e51",
                                   :font-family "Lato",
                                   :font-size   100,
                                   :states
                                                {:big     {:y 490, :font-size 120},
                                                 :hidden  {:type "transparent"},
                                                 :normal  {:y 510, :font-size 100},
                                                 :visible {:type "text"}},
                                   :text        ""}},
           :scene-objects
                     [["background"]
                      ["canvas"]
                      ["letter-small" "letter-big" "word-1-first" "word-1-rest" "word-2-first" "word-2-rest"]
                      ["senora-vaca" "mari"]
                      ["image"]],
           :actions
                     {:big-letter-click
                                         {:type     "test-var-scalar",
                                          :var-name "big-letter-clicked",
                                          :value    0,
                                          :success  "big-letter-click-first",
                                          :fail     "big-letter-click-further"},
                      :big-letter-click-first
                                         {:type "sequence-data",
                                          :data
                                                [{:type "counter", :counter-id "big-letter-clicked", :counter-action "increase"}
                                                 {:id "normal", :type "state", :target "letter-big"}
                                                 {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-sound"}]}
                                                 {:id "stage-repeat-all", :type "action"}]},
                      :big-letter-click-further
                                         {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-sound"}]},
                      :clear-instruction {:type "remove-flows", :description "Remove flows", :flow-tag "instruction"},
                      :finish-activity
                                         {:type "sequence-data", :data [{:id "letter-intro", :type "finish-activity"} {:type "scene", :scene-id "map"}]},
                      :init-state
                                         {:type "parallel",
                                          :data
                                                [{:type "set-variable", :var-name "small-letter-clicked", :var-value 0}
                                                 {:type "set-variable", :var-name "big-letter-clicked", :var-value 0}
                                                 {:type "set-variable", :var-name "vaca-click-available", :var-value 0}
                                                 {:id "hidden", :type "state", :target "letter-small"}
                                                 {:type      "set-attribute",
                                                  :target    "letter-small",
                                                  :from-var  [{:var-name "current-concept", :var-property "letter", :action-property "attr-value"}],
                                                  :attr-name "text"}
                                                 {:id "hidden", :type "state", :target "letter-big"}
                                                 {:type      "set-attribute",
                                                  :target    "letter-big",
                                                  :from-var  [{:var-name "current-concept", :var-property "letter-big", :action-property "attr-value"}],
                                                  :attr-name "text"}
                                                 {:id "hidden", :type "state", :target "word-1-first"}
                                                 {:type      "set-attribute",
                                                  :target    "word-1-first",
                                                  :from-var  [{:var-name "current-concept", :var-property "letter", :action-property "attr-value"}],
                                                  :attr-name "text"}
                                                 {:id "hidden", :type "state", :target "word-1-rest"}
                                                 {:type      "set-attribute",
                                                  :target    "word-1-rest",
                                                  :from-var  [{:var-name "current-concept", :var-property "concept-rest", :action-property "attr-value"}],
                                                  :attr-name "text"}
                                                 {:id "hidden", :type "state", :target "word-2-first"}
                                                 {:type      "set-attribute",
                                                  :target    "word-2-first",
                                                  :from-var  [{:var-name "current-concept", :var-property "letter-big", :action-property "attr-value"}],
                                                  :attr-name "text"}
                                                 {:id "hidden", :type "state", :target "word-2-rest"}
                                                 {:type      "set-attribute",
                                                  :target    "word-2-rest",
                                                  :from-var  [{:var-name "current-concept", :var-property "concept-rest", :action-property "attr-value"}],
                                                  :attr-name "text"}
                                                 {:id "init-position", :type "state", :target "image"}
                                                 {:type      "set-attribute",
                                                  :target    "image",
                                                  :from-var  [{:var-name "current-concept", :var-property "image-src", :action-property "attr-value"}],
                                                  :attr-name "src"}]},
                      :mari-init-wand    {:type "add-animation", :id "wand_idle", :target "mari", :track 2, :loop true},
                      :mari-voice-finish
                                         {:type                   "animation-sequence",
                                          :data                   [{:end 32.322, :anim "talk", :start 22.57}],
                                          :target                 "mari",
                                          :audio                  "/raw/audio/l2/a2/L2_A2_Mari.m4a",
                                          :start                  22.481,
                                          :duration               9.915,
                                          :track                  1,
                                          :offset                 22.481,
                                          :phrase                 :finish
                                          :phrase-description     "Repeat or finish"
                                          :phrase-text            "Touch Senora Vaca to hear the lesson once again, or touch the arrow to go to your next activity!"
                                          :phrase-text-translated "Toca a la Senora Vaca para escuchar esta lecion otra vez, o toca la flecha para ir a tu proxima actividad!"},
                      :mari-wand-hit
                                         {:type "sequence-data",
                                          :data
                                                [{:id "wand_hit", :type "animation", :track 2, :target "mari"}
                                                 {:id "wand_idle", :loop true, :type "add-animation", :track 2, :target "mari"}]},
                      :renew-concept
                                         {:type "lesson-var-provider", :from "concepts-single", :provider-id "concepts", :variables ["current-concept"]},
                      :small-letter-click
                                         {:type     "test-var-scalar",
                                          :var-name "small-letter-clicked",
                                          :value    0,
                                          :success  "small-letter-click-first",
                                          :fail     "small-letter-click-further"},
                      :small-letter-click-first
                                         {:type "sequence-data",
                                          :data
                                                [{:type "counter", :counter-id "small-letter-clicked", :counter-action "increase"}
                                                 {:id "normal", :type "state", :target "letter-small"}
                                                 {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-sound"}]}
                                                 {:id "stage-big", :type "action"}
                                                 {:id "touch-big-letter", :type "action"}]},
                      :small-letter-click-further
                                         {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-sound"}]},
                      :stage-big
                                         {:type               "sequence-data",
                                          :phrase             :big-letter
                                          :phrase-description "Big letter"
                                          :data
                                                              [{:id "visible", :type "state", :target "letter-big"}
                                                               {:id "big", :type "state", :target "letter-big"}
                                                               {:offset                 25.783,
                                                                :start                  25.783,
                                                                :type                   "animation-sequence",
                                                                :duration               3.07,
                                                                :volume                 1.33,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_2.m4a",
                                                                :target                 "senoravaca",
                                                                :track                  1,
                                                                :phrase-text            "This is another way to write the sound",
                                                                :phrase-text-translated "Esta es otra forma de escribir sonido",
                                                                :data                   [{:end 28.759, :anim "talk", :start 25.919, :duration 2.84}]}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-sound"}]}
                                                               {:offset                 30.489,
                                                                :start                  30.489,
                                                                :type                   "animation-sequence",
                                                                :duration               1.004,
                                                                :volume                 1.33,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_2.m4a",
                                                                :target                 "senoravaca",
                                                                :track                  1,
                                                                :phrase-text            "The letter",
                                                                :phrase-text-translated "Esta es la",
                                                                :data                   [{:end 31.359, :anim "talk", :start 30.585, :duration 0.774}]}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-letter"}]}
                                                               {:offset                 32.143,
                                                                :start                  32.143,
                                                                :type                   "animation-sequence",
                                                                :duration               2.003,
                                                                :volume                 1.33,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_2.m4a",
                                                                :target                 "senoravaca",
                                                                :track                  1,
                                                                :phrase-text            "is big. The letter",
                                                                :phrase-text-translated "mayuscula. La",
                                                                :data
                                                                                        [{:end 33.065, :anim "talk", :start 32.252, :duration 0.813}
                                                                                         {:end 34.052, :anim "talk", :start 33.878, :duration 0.174}]}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-letter"}]}
                                                               {:offset                 34.409,
                                                                :start                  34.409,
                                                                :type                   "animation-sequence",
                                                                :duration               2.044,
                                                                :volume                 1.33,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_2.m4a",
                                                                :target                 "senoravaca",
                                                                :track                  1,
                                                                :phrase-text            "also makes the sound",
                                                                :phrase-text-translated "mayuscula tambien suena",
                                                                :data
                                                                                        [{:end 35.145, :anim "talk", :start 34.505, :duration 0.64}
                                                                                         {:end 36.398, :anim "talk", :start 35.492, :duration 0.906}]}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-sound"}]}
                                                               {:id "visible", :type "state", :target "word-2-first"}
                                                               {:id "visible", :type "state", :target "word-2-rest"}
                                                               {:data
                                                                      [{:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-word-repeat"}]}
                                                                       {:data
                                                                              [{:id "big", :type "state", :target "word-2-first"}
                                                                               {:type "empty", :duration 400}
                                                                               {:id "normal", :type "state", :target "word-2-first"}
                                                                               {:id "big", :type "state", :target "word-2-rest"}
                                                                               {:type "empty", :duration 500}
                                                                               {:id "normal", :type "state", :target "word-2-rest"}
                                                                               {:type "empty", :duration 500}
                                                                               {:id "big", :type "state", :target "word-2-first"}
                                                                               {:type "empty", :duration 400}
                                                                               {:id "normal", :type "state", :target "word-2-first"}
                                                                               {:id "big", :type "state", :target "word-2-rest"}
                                                                               {:type "empty", :duration 500}
                                                                               {:id "normal", :type "state", :target "word-2-rest"}],
                                                                        :type "sequence-data"}],
                                                                :type "parallel"}
                                                               {:type "empty", :duration 700}]},
                      :touch-big-letter
                                         {:phrase             :touch-big-letter
                                          :phrase-description "Touch big letter"
                                          :type               "sequence-data"
                                          :data
                                                              [{:id "mari-wand-hit", :type "action", :return-immediately true}
                                                               {:offset                 10.645,
                                                                :start                  10.645,
                                                                :type                   "animation-sequence",
                                                                :duration               0.906,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Mari.m4a",
                                                                :target                 "mari",
                                                                :track                  1,
                                                                :phrase-text            "Touch the big letter",
                                                                :phrase-text-translated "Toca la",
                                                                :data                   [{:end 11.456, :anim "talk", :start 10.789, :duration 0.667}]}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-letter-mari"}]}
                                                               {:offset                 12.192,
                                                                :start                  12.192,
                                                                :type                   "animation-sequence",
                                                                :duration               3.813,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Mari.m4a",
                                                                :target                 "mari",
                                                                :track                  1,
                                                                :phrase-text            "to hear how it sounds.",
                                                                :phrase-text-translated "mayuscula para escuchar como suena.",
                                                                :data                   [{:end 15.871, :anim "talk", :start 12.244, :duration 3.627}]}]}
                      :stage-finish
                                         {:type "sequence-data",
                                          :data
                                                [{:type "set-variable", :var-name "vaca-click-available", :var-value 1}
                                                 {:to {:x 1600, :y 635, :loop false, :duration 0.7}, :type "transition", :transition-id "mari"}
                                                 {:id "mari-voice-finish", :type "action"}]},
                      :stage-intro
                                         {:type               "sequence-data",
                                          :phrase             :intro
                                          :phrase-description "Let's begin"
                                          :data               [{:id "vaca-voice-welcome", :type "action"}
                                                               {:type "empty", :duration 700}
                                                               {:id "vaca-voice-give-word", :type "action"}
                                                               {:type "empty", :duration 600}]},
                      :stage-other-words {:type "empty", :duration 100, :description "Other words that starts with sound a"},
                      :stage-repeat-all
                                         {:type "sequence-data",
                                          :data
                                                [{:to {:x 1325, :y 500, :loop false, :duration 0.7}, :type "transition", :transition-id "mari"}
                                                 {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-song"}]}
                                                 {:to {:x 1325, :y 610, :loop false, :duration 0.7}, :type "transition", :transition-id "mari"}
                                                 {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-song"}]}
                                                 {:id "stage-finish", :type "action"}]},
                      :stage-small
                                         {:type               "sequence-data",
                                          :phrase             :small-letter
                                          :phrase-description "Small letter"
                                          :data
                                                              [{:id "big", :type "state", :target "letter-small"}
                                                               {:offset                 1.286,
                                                                :start                  1.286,
                                                                :type                   "animation-sequence",
                                                                :duration               1.521,
                                                                :volume                 1.33,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_2.m4a",
                                                                :target                 "senoravaca",
                                                                :track                  1,
                                                                :phrase-text            "This letter",
                                                                :phrase-text-translated "Esta es la",
                                                                :data
                                                                                        [{:end 1.907, :anim "talk", :start 1.413, :duration 0.494}
                                                                                         {:end 2.707, :anim "talk", :start 2.333, :duration 0.374}]}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-letter"}]}
                                                               {:offset                 3.193,
                                                                :start                  3.193,
                                                                :type                   "animation-sequence",
                                                                :duration               1.001,
                                                                :volume                 1.33,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_2.m4a",
                                                                :target                 "senoravaca",
                                                                :track                  1,
                                                                :phrase-text            "is small",
                                                                :phrase-text-translated "miniscula",
                                                                :data                   [{:end 3.946, :anim "talk", :start 3.347, :duration 0.599}]}
                                                               {:type "empty", :duration 500}
                                                               {:offset                 5.172,
                                                                :start                  5.172,
                                                                :type                   "animation-sequence",
                                                                :duration               1.676,
                                                                :volume                 1.33,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_2.m4a",
                                                                :target                 "senoravaca",
                                                                :track                  1,
                                                                :phrase-text            "The letter",
                                                                :phrase-text-translated "Esta letra",
                                                                :data
                                                                                        [{:end 5.746, :anim "talk", :start 5.28, :duration 0.466} {:end 6.653, :anim "talk", :start 6.053, :duration 0.6}]}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-letter"}]}
                                                               {:offset                 7.932,
                                                                :start                  7.932,
                                                                :type                   "animation-sequence",
                                                                :duration               1.036,
                                                                :volume                 1.33,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_2.m4a",
                                                                :target                 "senoravaca",
                                                                :phrase-text            "makes the sound",
                                                                :phrase-text-translated "hace el sonido",
                                                                :track                  1,
                                                                :data                   [{:end 8.8, :anim "talk", :start 8.026, :duration 0.774}]}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-sound"}]}
                                                               {:type "empty", :duration 500}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-sound"}]}
                                                               {:offset                 12.961,
                                                                :start                  12.961,
                                                                :type                   "animation-sequence",
                                                                :duration               2.568,
                                                                :volume                 1.33,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_2.m4a",
                                                                :target                 "senoravaca",
                                                                :track                  1,
                                                                :phrase-text            "is the first sound in the word",
                                                                :phrase-text-translated "es el primer sonido en la palabra",
                                                                :data
                                                                                        [{:end 14.453, :anim "talk", :start 13.053, :duration 1.4}
                                                                                         {:end 15.466, :anim "talk", :start 14.773, :duration 0.693}]}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-word"}]}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-sound"}]}
                                                               {:offset                 18.039,
                                                                :start                  18.039,
                                                                :type                   "animation-sequence",
                                                                :duration               0.667,
                                                                :volume                 1.33,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_2.m4a",
                                                                :target                 "senoravaca",
                                                                :track                  1,
                                                                :phrase-text            "is for",
                                                                :phrase-text-translated "es para",
                                                                :data                   [{:end 18.586, :anim "talk", :start 18.159, :duration 0.427}]}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-word"}]}
                                                               {:id "visible", :type "state", :target "word-1-first"}
                                                               {:id "visible", :type "state", :target "word-1-rest"}
                                                               {:data
                                                                      [{:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-word-repeat"}]}
                                                                       {:data
                                                                              [{:id "big", :type "state", :target "word-1-first"}
                                                                               {:type "empty", :duration 400}
                                                                               {:id "normal", :type "state", :target "word-1-first"}
                                                                               {:id "big", :type "state", :target "word-1-rest"}
                                                                               {:type "empty", :duration 500}
                                                                               {:id "normal", :type "state", :target "word-1-rest"}
                                                                               {:type "empty", :duration 500}
                                                                               {:id "big", :type "state", :target "word-1-first"}
                                                                               {:type "empty", :duration 400}
                                                                               {:id "normal", :type "state", :target "word-1-first"}
                                                                               {:id "big", :type "state", :target "word-1-rest"}
                                                                               {:type "empty", :duration 500}
                                                                               {:id "normal", :type "state", :target "word-1-rest"}],
                                                                        :type "sequence-data"}],
                                                                :type "parallel"}
                                                               {:type "empty", :duration 1000}]},
                      :touch-small-letter
                                         {:phrase             :touch-small-letter
                                          :phrase-description "Touch small letter"
                                          :type               "sequence-data"
                                          :data               [
                                                               {:id "mari-wand-hit", :type "action", :return-immediately true}
                                                               {:offset                 1.414,
                                                                :start                  1.414,
                                                                :type                   "animation-sequence",
                                                                :duration               0.852,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Mari.m4a",
                                                                :target                 "mari",
                                                                :track                  1,
                                                                :phrase-text            "Touch the small letter",
                                                                :phrase-text-translated "Toca la",
                                                                :data                   [{:end 2.152, :anim "talk", :start 1.491, :duration 0.661}]}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-letter-mari"}]}
                                                               {:offset                 2.681,
                                                                :start                  2.681,
                                                                :type                   "animation-sequence",
                                                                :duration               3.666,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Mari.m4a",
                                                                :target                 "mari",
                                                                :track                  1,
                                                                :phrase-text            "to hear how it sounds.",
                                                                :phrase-text-translated "minuscula para escuchar como suena.",
                                                                :data                   [{:end 6.241, :anim "talk", :start 2.814, :duration 3.427}]}]}
                      :current-sound
                                         {:phrase             :concept-sound
                                          :phrase-description "Current sound"
                                          :type               "sequence-data",
                                          :data
                                                              [{:id "visible", :type "state", :target "letter-small"}
                                                               {:to {:x 1903, :y 557, :loop false, :duration 0.7}, :type "transition", :transition-id "mari"}
                                                               {:data
                                                                      [{:to {:x 1146, :y 295, :loop false, :duration 2}, :type "transition", :transition-id "mari"}
                                                                       {:to {:x 1025, :y 240, :loop false, :duration 2}, :type "transition", :transition-id "image"}],
                                                                :type "parallel"}
                                                               {:type "empty", :duration 1000}
                                                               {:id "mari-init-wand", :type "action"}
                                                               {:to {:x 1346, :y 295, :loop false, :duration 1.5}, :type "transition", :transition-id "mari"}
                                                               {:id "vaca-voice-listen", :type "action"}
                                                               {:id "vaca-voice-say-with-me", :type "action"}
                                                               {:type "empty", :duration 500}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-song"}]}
                                                               {:type "empty", :duration 500}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-song"}]}
                                                               {:type "empty", :duration 500}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-song"}]}
                                                               {:type "empty", :duration 500}]},
                      :start-scene
                                         {:type        "sequence",
                                          :data        ["start-activity" "renew-concept" "init-state" "stage-intro" "stage-sound-start"],
                                          :description "Initial action"},
                      :start-activity    {:type "start-activity", :id "letter-intro"},
                      :stop-activity     {:type "stop-activity", :id "letter-intro"},
                      :stage-sound-start
                                         {:type "sequence"
                                          :data ["current-sound"
                                                 "stage-write-2-ways"
                                                 "vaca-voice-2-ways-write"
                                                 "stage-small"
                                                 "touch-small-letter"]}
                      :vaca-click
                                         {:type     "test-var-scalar",
                                          :var-name "vaca-click-available",
                                          :value    1,
                                          :success  "vaca-click-success",
                                          :fail     "vaca-click-fail"},
                      :vaca-click-fail   {:type "empty", :duration 100},
                      :vaca-click-success
                                         {:type "sequence", :data ["init-state" "stage-sound-start"]},
                      :vaca-voice-2-ways-write
                                         {:type               "sequence-data",
                                          :phrase             :way-to-write,
                                          :phrase-description "Two ways to write",
                                          :data
                                                              [{:offset                 47.75,
                                                                :start                  47.75,
                                                                :type                   "animation-sequence",
                                                                :duration               6.109,
                                                                :volume                 1.33,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_1.m4a",
                                                                :target                 "senoravaca",
                                                                :track                  1,
                                                                :phrase-text            "Good! There are 2 ways to write this sound. You can write it with "
                                                                :phrase-text-translated "Bueno! Hay dos formas de escribir este sonido. Puedes escribir con"
                                                                :data
                                                                                        [{:end 48.214, :anim "talk", :start 47.88, :duration 0.334}
                                                                                         {:end 51.694, :anim "talk", :start 48.947, :duration 2.747}
                                                                                         {:end 53.8, :anim "talk", :start 52.734, :duration 1.066}]}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-letter"}]}
                                                               {:offset                 54.723,
                                                                :start                  54.723,
                                                                :type                   "animation-sequence",
                                                                :duration               1.843,
                                                                :volume                 1.33,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_1.m4a",
                                                                :target                 "senoravaca",
                                                                :track                  1,
                                                                :phrase-text            "a small letter or",
                                                                :phrase-text-translated "miniscula o"
                                                                :data
                                                                                        [{:end 55.587, :anim "talk", :start 54.92, :duration 0.667}
                                                                                         {:end 56.427, :anim "talk", :start 56.16, :duration 0.267}]}
                                                               {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-letter"}]}
                                                               {:offset                 57.19,
                                                                :start                  57.19,
                                                                :type                   "animation-sequence",
                                                                :duration               1.056,
                                                                :volume                 1.33,
                                                                :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_1.m4a",
                                                                :target                 "senoravaca",
                                                                :track                  1,
                                                                :phrase-text            "a big letter",
                                                                :phrase-text-translated "mayuscula."
                                                                :data                   [{:end 58.094, :anim "talk", :start 57.32, :duration 0.774}]}]},
                      :vaca-voice-give-word
                                         {:type "sequence-data",
                                          :data
                                                [{:offset                 7.578,
                                                  :start                  7.578,
                                                  :type                   "animation-sequence",
                                                  :duration               4.007,
                                                  :volume                 1.33,
                                                  :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_1.m4a",
                                                  :target                 "senoravaca",
                                                  :track                  1,
                                                  :phrase-text            "Mari, can you give me a word that starts with the letter"
                                                  :phrase-text-translated "Mari, puedes darme una palabra que comienze con la letra"
                                                  :data
                                                                          [{:end 8.213, :anim "talk", :start 7.754, :duration 0.459}
                                                                           {:end 11.549, :anim "talk", :start 8.699, :duration 2.85}]}
                                                 {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-letter"}]}],},
                      :vaca-voice-listen
                                         {:type "sequence-data",
                                          :data
                                                [{:offset                 13.16,
                                                  :start                  13.16,
                                                  :type                   "animation-sequence",
                                                  :duration               3.093,
                                                  :volume                 1.33,
                                                  :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_1.m4a",
                                                  :target                 "senoravaca",
                                                  :track                  1,
                                                  :phrase-text            "Excellent! We have a"
                                                  :phrase-text-translated "Excellente! Tenemos una"
                                                  :data
                                                                          [{:end 14.453, :anim "talk", :start 13.267, :duration 1.186}
                                                                           {:end 16.2, :anim "talk", :start 15.28, :duration 0.92}]}
                                                 {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-word"}]}
                                                 {:offset                 17.64,
                                                  :start                  17.64,
                                                  :type                   "animation-sequence",
                                                  :duration               2.76,
                                                  :volume                 1.33,
                                                  :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_1.m4a",
                                                  :target                 "senoravaca",
                                                  :track                  1,
                                                  :phrase-text            "Listen to the first sound in the word"
                                                  :phrase-text-translated "Escucha el primer sonida en la palabra"
                                                  :data
                                                                          [{:end 19.16, :anim "talk", :start 17.747, :duration 1.413}
                                                                           {:end 20.28, :anim "talk", :start 19.547, :duration 0.733}]}
                                                 {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-word"}]}
                                                 {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-word-repeat"}]}
                                                 {:offset                 25.436,
                                                  :start                  25.436,
                                                  :type                   "animation-sequence",
                                                  :duration               1.71,
                                                  :volume                 1.33,
                                                  :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_1.m4a",
                                                  :target                 "senoravaca",
                                                  :track                  1,
                                                  :data                   [{:end 26.894, :anim "talk", :start 25.694, :duration 1.2}]
                                                  :phrase-text            "Sing it with me like this"
                                                  :phrase-text-translated "Cantalo conmigo!"}
                                                 {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-song"}]}]},
                      :vaca-voice-say-with-me
                                         {:type                   "animation-sequence",
                                          :data
                                                                  [{:end 33.054, :anim "talk", :start 31.703}
                                                                   {:end 34.364, :anim "talk", :start 33.662}
                                                                   {:end 35.634, :anim "talk", :start 34.634}],
                                          :target                 "senoravaca",
                                          :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_1.m4a",
                                          :start                  31.527,
                                          :duration               4.255,
                                          :track                  1,
                                          :offset                 31.527,
                                          :volume                 1.33,
                                          :phrase-text            "Say it with me! Say it with me 3 times.",
                                          :phrase-text-translated "Dilo conmigo! Cantalo tres veces!"},
                      :vaca-voice-welcome
                                         {:type "sequence-data",
                                          :data
                                                [{:offset                 1.108,
                                                  :start                  1.108,
                                                  :type                   "animation-sequence",
                                                  :duration               5.234,
                                                  :volume                 1.33,
                                                  :audio                  "/raw/audio/l2/a2/L2_A2_Vaca_1.m4a",
                                                  :target                 "senoravaca",
                                                  :phrase-text            "Hello little genius! Let’s learn some new letters! Let’s start with the letter"
                                                  :phrase-text-translated "Hola pequeno genio! Hoy empezaremos a aprender sobre la letra"
                                                  :track                  1,
                                                  :data
                                                                          [{:end 2.634, :anim "talk", :start 1.283, :duration 1.351}
                                                                           {:end 6.281, :anim "talk", :start 3.309, :duration 2.972}]}
                                                 {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-letter"}]}],}},
           :triggers {:back {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
           :metadata {:prev "map", :autostart true},
           :audio    {:mari   "/raw/audio/l2/a2/L2_A2_Mari.m4a",
                      :vaca-1 "/raw/audio/l2/a2/L2_A2_Vaca_1.m4a",
                      :vaca-2 "/raw/audio/l2/a2/L2_A2_Vaca_2.m4a",
                      :vaca-3 "/raw/audio/l2/a2/L2_A2_Vaca_3.m4a"}})
