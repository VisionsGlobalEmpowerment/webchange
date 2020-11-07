(ns webchange.editor-v2.graph-builder.graph.data.concept-sample)

(def data {:name "ardilla",
           :data {:sandbox-long-letter-action
                                          {:data
                                                     [{:end 21.893, :anim "talk", :start 20.919, :duration 0.974}],
                                           :type     "animation-sequence",
                                           :audio    "/raw/audio/l1/a4/Mari_Level1_Activity4.m4a",
                                           :start    20.799,
                                           :offset   20.799,
                                           :target   "mari",
                                           :duration 1.254,
                                           :scene-id "sandbox"},
                  :word-image-4           "/raw/img/elements/spider.png",
                  :word-3-skin            "airplane",
                  :game-voice-action
                                          {:description            "Game Voice High",
                                           :scene-id               "cycling",
                                           :phrase-text            "Ardilla. Ardilla.  ARDILLA!",
                                           :start                  4.532,
                                           :type                   "audio",
                                           :duration               4.345,
                                           :id                     "/raw/audio/l1/a2/game-voice-1-redo.mp3",
                                           :audio                  "/raw/audio/l1/a2/game-voice-1-redo.mp3",
                                           :phrase-text-translated "Ardilla. Ardilla.  ARDILLA!"},
                  :letter-intro-letter
                                          {:offset      0.813,
                                           :scene-id    "letter-intro",
                                           :phrase-text "A",
                                           :start       0.813,
                                           :type        "animation-sequence",
                                           :duration    0.414,
                                           :audio
                                                        "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_1.m4a",
                                           :target      "senoravaca",
                                           :data
                                                        [{:end 1.147, :anim "talk", :start 0.853, :duration 0.294}]},
                  :sandbox-this-is-letter "mari-this-is-letter-a",
                  :word-4-skin            "spider",
                  :test-new
                                          {:id       "init",
                                           :type     "action",
                                           :scene-id "sandbox",
                                           :description
                                                     "Really long description for this action to test label"},
                  :hide-n-seek-current-concept-audio
                                          {:description "Hide and seek question",
                                           :offset      0,
                                           :scene-id    "hide-n-seek",
                                           :start       0.133,
                                           :type        "audio",
                                           :duration    1.307,
                                           :id          "/raw/audio/l1/assessment1/game-voice-set-main.mp3",
                                           :audio       "/raw/audio/l1/assessment1/game-voice-set-main.mp3",
                                           :target      "mari",
                                           :track       1,
                                           :data        []},
                  :word-image-2           "/raw/img/elements/tree.png",
                  :sandbox-state-word-4-action
                                          {:id          "/raw/audio/l1/a4/game-voice-set-1.mp3",
                                           :data
                                                        [{:id     "show",
                                                          :type   "state",
                                                          :params {:text "Arana"},
                                                          :target "word"}
                                                         {:id "voice-1", :type "audio", :start 11.144, :duration 1.06}
                                                         {:id "default", :type "state", :target "word"}],
                                           :type        "audio",
                                           :start       4.187,
                                           :duration    1.186,
                                           :scene-id    "sandbox",
                                           :description ":sandbox-state-word-arana"},
                  :word-image-3           "/raw/img/elements/airplane.png",
                  :letter-src             "/raw/img/letters/a.png",
                  :letter                 "a",
                  :mari-word
                                          {:offset      0.533,
                                           :scene-id    "magic-hat",
                                           :phrase-text "Ardilla",
                                           :start       0.533,
                                           :type        "animation-sequence",
                                           :duration    1.2,
                                           :audio       "/raw/audio/l2/mari-chants.m4a",
                                           :target      "mari",
                                           :data        [{:end 1.84, :anim "talk", :start 0.65}]},
                  :sandbox-change-skin-4-action
                                          {:skin        "spider",
                                           :type        "set-skin",
                                           :target      "box4",
                                           :scene-id    "sandbox",
                                           :description ":box-1-set-skin-arana"},
                  :poem-image-1           "/raw/img/park/poem/ardilla_01.png",
                  :mari-sound
                                          {:offset      2.973,
                                           :scene-id    "magic-hat",
                                           :phrase-text "A",
                                           :start       2.973,
                                           :type        "animation-sequence",
                                           :duration    0.427,
                                           :audio       "/raw/audio/l2/mari-chants.m4a",
                                           :target      "mari",
                                           :data        [{:end 3.69, :anim "talk", :start 3.15}]},
                  :sandbox-state-word-3-action
                                          {:id          "/raw/audio/l1/a4/game-voice-set-1.mp3",
                                           :data
                                                        [{:id     "show",
                                                          :type   "state",
                                                          :params {:text "Avion"},
                                                          :target "word"}
                                                         {:id "voice-1", :type "audio", :start 9.506, :duration 0.871}
                                                         {:id "default", :type "state", :target "word"}],
                                           :type        "audio",
                                           :start       2.707,
                                           :duration    1.053,
                                           :scene-id    "sandbox",
                                           :description ":sandbox-state-word-avion"},
                  :home-group-3-times-action
                                          {:data
                                                     [{:offset                 1.613,
                                                       :phrase-text            "Ardilla, ardilla, ARDILLA!!",
                                                       :start                  1.613,
                                                       :type                   "animation-sequence",
                                                       :duration               3.653,
                                                       :audio                  "/raw/audio/l1/a1/vera-casa-1.mp3",
                                                       :target                 "vera",
                                                       :phrase-text-translated "Ardilla, ardilla, ARDILLA!!",
                                                       :data
                                                                               [{:end 2.45, :anim "talk", :start 1.71}
                                                                                {:end 3.77, :anim "talk", :start 2.99}
                                                                                {:end 5.17, :anim "talk", :start 4.02}]}],
                                           :type     "parallel",
                                           :scene-id "home"},
                  :poem-image-3           "/raw/img/park/poem/ardilla_03.png",
                  :home-vaca-word-action
                                          {:offset                 20.864,
                                           :scene-id               "home",
                                           :phrase-text            "Ardilla!",
                                           :start                  20.864,
                                           :type                   "animation-sequence",
                                           :duration               1.613,
                                           :id                     "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a",
                                           :audio                  "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a",
                                           :target                 "senoravaca",
                                           :phrase-text-translated "Ardilla!",
                                           :data
                                                                   [{:end 22.304, :anim "talk", :start 20.957, :duration 1.347}]},
                  :word-image-1           "/raw/img/elements/bee.png",
                  :home-vaca-goodbye      "goodbye-ardilla",
                  :sandbox-change-skin-2-action
                                          {:skin        "tree",
                                           :type        "set-skin",
                                           :target      "box2",
                                           :scene-id    "sandbox",
                                           :description ":box-1-set-skin-arbol"},
                  :sandbox-this-is-letter-action
                                          {:description ":mari-this-is-letter-a",
                                           :offset      "14.397",
                                           :scene-id    "sandbox",
                                           :start       "14.397",
                                           :type        "animation-sequence",
                                           :duration    "16.202",
                                           :audio       "/raw/audio/l1/a4/Mari_Level1_Activity4.m4a",
                                           :target      "mari",
                                           :track       "1",
                                           :data
                                                        [{:end 16.639, :anim "talk", :start 14.552}
                                                         {:end 18.999, :anim "talk", :start 17.341}
                                                         {:end 20.475, :anim "talk", :start 19.373}
                                                         {:end 21.997, :anim "talk", :start 20.885}
                                                         {:end 30.417, :anim "talk", :start 23.173}]},
                  :letter-intro-word-repeat
                                          {:offset      7.254,
                                           :scene-id    "letter-intro",
                                           :phrase-text "Aaaaaardilla. Aaaaaardilla",
                                           :start       7.254,
                                           :type        "animation-sequence",
                                           :duration    2.053,
                                           :audio
                                                        "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_1.m4a",
                                           :target      "senoravaca",
                                           :data
                                                        [{:end 8.12, :anim "talk", :start 7.32, :duration 0.8}
                                                         {:end 9.187, :anim "talk", :start 8.387, :duration 0.8}]},
                  :game-voice-chanting
                                          {:id          "/raw/audio/l2/game-voice.mp3",
                                           :type        "audio",
                                           :start       0.067,
                                           :duration    4.373,
                                           :scene-id    "running",
                                           :phrase-text "Ardilla, ardilla, a, a, A!"},
                  :seesaw-voice-high      "word-ardilla-high",
                  :letter-path            "M144.76,92.43a37.5,37.5,0,1,0,0,39.28m0-57.21v75",
                  :letter-big             "A",
                  :home-group-word-action
                                          {:data
                                                     [{:offset                 0.2,
                                                       :phrase-text            "Ardilla!",
                                                       :start                  0.2,
                                                       :type                   "animation-sequence",
                                                       :duration               0.974,
                                                       :audio                  "/raw/audio/l1/a1/vera-casa-1.mp3",
                                                       :target                 "vera",
                                                       :phrase-text-translated "Ardilla!",
                                                       :data                   [{:end 0.87, :anim "talk", :start 0.3}]}
                                                      {:offset                 20.851,
                                                       :phrase-text            "Ardilla!",
                                                       :start                  20.851,
                                                       :type                   "animation-sequence",
                                                       :duration               1.613,
                                                       :audio                  "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a",
                                                       :target                 "senoravaca",
                                                       :phrase-text-translated "Ardilla!",
                                                       :data
                                                                               [{:end 22.344, :anim "talk", :start 20.944, :duration 1.4}]}],
                                           :type     "parallel",
                                           :scene-id "home"},
                  :letter-intro-sound
                                          {:offset      3.827,
                                           :scene-id    "letter-intro",
                                           :phrase-text "Aaaaa...",
                                           :start       3.827,
                                           :type        "animation-sequence",
                                           :duration    0.64,
                                           :audio
                                                        "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_1.m4a",
                                           :target      "senoravaca",
                                           :data        [{:end 4.24, :anim "talk", :start 3.92, :duration 0.32}]},
                  :sandbox-state-word-2   "arbol",
                  :vaca-chanting-song
                                          {:offset      22.037,
                                           :phrase-text "Ardilla! Ardilla! a-a-A!",
                                           :start       22.037,
                                           :type        "animation-sequence",
                                           :duration    3.593,
                                           :audio       "/raw/audio/l2/a1/L2_A1_Vaca_2.m4a",
                                           :target      "senoravaca",
                                           :track       1,
                                           :data
                                                        [{:end 23.021, :anim "talk", :start 22.201}
                                                         {:end 24.065, :anim "talk", :start 23.26}
                                                         {:end 25.511, :anim "talk", :start 24.423}]},
                  :sandbox-short-letter-action
                                          {:data
                                                     [{:end 16.626, :anim "talk", :start 16.253, :duration 0.373}],
                                           :type     "animation-sequence",
                                           :audio    "/raw/audio/l1/a4/Mari_Level1_Activity4.m4a",
                                           :start    16.199,
                                           :offset   16.199,
                                           :target   "mari",
                                           :duration 0.534,
                                           :scene-id "sandbox"},
                  :home-vaca-3-times      "vaca-3-times-ardilla",
                  :word-1-skin            "bee",
                  :image-src              "/raw/img/elements/squirrel.png",
                  :home-vaca-3-times-action
                                          {:offset                 27.89,
                                           :scene-id               "home",
                                           :phrase-text            "Ardilla, ardilla, ARDILLA!!",
                                           :start                  27.89,
                                           :type                   "animation-sequence",
                                           :duration               4.146,
                                           :audio                  "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a",
                                           :target                 "senoravaca",
                                           :phrase-text-translated "Ardilla, ardilla, ARDILLA!!",
                                           :data
                                                                   [{:end 28.81, :anim "talk", :start 28.076, :duration 0.734}
                                                                    {:end 29.823, :anim "talk", :start 29.13, :duration 0.693}
                                                                    {:end 31.369, :anim "talk", :start 30.396, :duration 0.973}]},
                  :home-vaca-this-is      "this-is-ardilla",
                  :swings-dialog-action
                                          {:data
                                                     [{:offset   1,
                                                       :phrase-text
                                                                 "Eso se ve divertido! A quién estás empujando en el columpio?",
                                                       :start    1,
                                                       :type     "animation-sequence",
                                                       :duration 5.853,
                                                       :audio    "/raw/audio/l1/a3/rock/Ardilla.m4a",
                                                       :target   "rock",
                                                       :phrase-text-translated
                                                                 "Eso se ve divertido! A quién estás empujando en el columpio?",
                                                       :data
                                                                 [{:end 1.547, :anim "talk", :start 1.16, :duration 0.387}
                                                                  {:end 3.586, :anim "talk", :start 2.16, :duration 1.426}
                                                                  {:end 6.573, :anim "talk", :start 4.066, :duration 2.507}]}
                                                      {:description            "",
                                                       :offset                 0.493,
                                                       :phrase-text            "A quien estoy empujando?",
                                                       :start                  0.493,
                                                       :type                   "animation-sequence",
                                                       :duration               1.947,
                                                       :audio                  "/raw/audio/l1/a3/vera/ardilla.mp3",
                                                       :target                 "vera",
                                                       :phrase-text-translated "A quien estoy empujando?",
                                                       :data                   [{:end 2.34, :anim "talk", :start 0.6}]}
                                                      {:offset   10.572,
                                                       :phrase-text
                                                                 "Si! Cuál es el nombre del animal con ojos grandes y una cola tupida?",
                                                       :start    10.572,
                                                       :type     "animation-sequence",
                                                       :duration 5.92,
                                                       :audio    "/raw/audio/l1/a3/rock/Ardilla.m4a",
                                                       :target   "rock",
                                                       :phrase-text-translated
                                                                 "Si! Cuál es el nombre del animal con ojos grandes y una cola tupida?",
                                                       :data
                                                                 [{:end 11.212, :anim "talk", :start 10.732, :duration 0.48}
                                                                  {:end 16.212, :anim "talk", :start 11.772, :duration 4.44}]}
                                                      {:offset                 3.374,
                                                       :phrase-text            "Ah, es una ardilla!",
                                                       :start                  3.374,
                                                       :type                   "animation-sequence",
                                                       :duration               2.866,
                                                       :audio                  "/raw/audio/l1/a3/vera/ardilla.mp3",
                                                       :target                 "vera",
                                                       :phrase-text-translated "Ah, es una ardilla!",
                                                       :data                   [{:end 6.06, :anim "talk", :start 3.51}]}
                                                      {:offset   17.852,
                                                       :phrase-text
                                                                 "Una ardilla! Eso pensé! Has visto antes una ardilla?",
                                                       :start    17.852,
                                                       :type     "animation-sequence",
                                                       :duration 5.319,
                                                       :audio    "/raw/audio/l1/a3/rock/Ardilla.m4a",
                                                       :target   "rock",
                                                       :phrase-text-translated
                                                                 "Una ardilla! Eso pensé! Has visto antes una ardilla?",
                                                       :data
                                                                 [{:end 18.892, :anim "talk", :start 17.985, :duration 0.907}
                                                                  {:end 20.145, :anim "talk", :start 19.492, :duration 0.653}
                                                                  {:end 22.918, :anim "talk", :start 20.851, :duration 2.067}]}
                                                      {:offset   7.053,
                                                       :phrase-text
                                                                 "Mmmm, si! He visto una ardilla. Una vez vi una ardilla treparse en un arbol.",
                                                       :start    7.053,
                                                       :type     "animation-sequence",
                                                       :duration 7.88,
                                                       :audio    "/raw/audio/l1/a3/vera/ardilla.mp3",
                                                       :target   "vera",
                                                       :phrase-text-translated
                                                                 "Mmmm, si! He visto una ardilla. Una vez vi una ardilla treparse en un arbol.",
                                                       :data
                                                                 [{:end 8.53, :anim "talk", :start 7.13}
                                                                  {:end 10.56, :anim "talk", :start 8.98}
                                                                  {:end 13, :anim "talk", :start 11.3}
                                                                  {:end 14.76, :anim "talk", :start 13.13}]}
                                                      {:offset   24.144,
                                                       :phrase-text
                                                                 "Ahhhh. A las ardillas les gusta treparse a los árboles. También les gusta comer nueces, frutas y semillas.",
                                                       :start    24.144,
                                                       :type     "animation-sequence",
                                                       :duration 9.2,
                                                       :audio    "/raw/audio/l1/a3/rock/Ardilla.m4a",
                                                       :target   "rock",
                                                       :phrase-text-translated
                                                                 "Ahhhh. A las ardillas les gusta treparse a los árboles. También les gusta comer nueces, frutas y semillas.",
                                                       :data
                                                                 [{:end 24.771, :anim "talk", :start 24.344, :duration 0.427}
                                                                  {:end 28.291, :anim "talk", :start 25.384, :duration 2.907}
                                                                  {:end 33.05, :anim "talk", :start 28.944, :duration 4.106}]}
                                                      {:offset   15.612,
                                                       :phrase-text
                                                                 "Si? Nueces, frutas y semillas? A mi me gusta las nueces y las frutas. Talvez pueda probar las semillas tambien",
                                                       :start    15.612,
                                                       :type     "animation-sequence",
                                                       :duration 12.933,
                                                       :audio    "/raw/audio/l1/a3/vera/ardilla.mp3",
                                                       :target   "vera",
                                                       :phrase-text-translated
                                                                 "Si? Nueces, frutas y semillas? A mi me gusta las nueces y las frutas. Talvez pueda probar las semillas tambien",
                                                       :data
                                                                 [{:end 16.45, :anim "talk", :start 15.86}
                                                                  {:end 20.15, :anim "talk", :start 17.53}
                                                                  {:end 24.13, :anim "talk", :start 20.84}
                                                                  {:end 25.18, :anim "talk", :start 25.04}
                                                                  {:end 28.28, :anim "talk", :start 25.44}]}
                                                      {:offset   34.01,
                                                       :phrase-text
                                                                 "Jajaja. Diviértete jugando con la a, a, a, ardilla!",
                                                       :start    34.01,
                                                       :type     "animation-sequence",
                                                       :duration 6.653,
                                                       :audio    "/raw/audio/l1/a3/rock/Ardilla.m4a",
                                                       :target   "rock",
                                                       :phrase-text-translated
                                                                 "Jajaja. Diviértete jugando con la a, a, a, ardilla!",
                                                       :data
                                                                 [{:end 35.743, :anim "talk", :start 34.397, :duration 1.346}
                                                                  {:end 38.263, :anim "talk", :start 36.237, :duration 2.026}
                                                                  {:end 38.783, :anim "talk", :start 38.623, :duration 0.16}
                                                                  {:end 39.436, :anim "talk", :start 39.276, :duration 0.16}
                                                                  {:end 40.183, :anim "talk", :start 39.81, :duration 0.373}]}],
                                           :type     "sequence-data",
                                           :scene-id "swings"},
                  :letter-intro-word
                                          {:offset      2.24,
                                           :scene-id    "letter-intro",
                                           :phrase-text "ardilla",
                                           :start       2.24,
                                           :type        "animation-sequence",
                                           :duration    0.84,
                                           :audio
                                                        "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_1.m4a",
                                           :target      "senoravaca",
                                           :data        [{:end 2.92, :anim "talk", :start 2.347, :duration 0.573}]},
                  :concept-name           "ardilla",
                  :home-group-3-times     "group-3-times-ardilla",
                  :poem-image-2           "/raw/img/park/poem/ardilla_02.png",
                  :home-vaca-question     "question-ardilla",
                  :home-vaca-word         "word-ardilla",
                  :chanting-video-src     "/raw/video/l2a1/letter-a.mp4",
                  :home-vaca-goodbye-action
                                          {:offset                 44.768,
                                           :scene-id               "home",
                                           :phrase-text            "Adios ardilla! Adios!",
                                           :start                  44.768,
                                           :type                   "animation-sequence",
                                           :duration               2.639,
                                           :id                     "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a",
                                           :audio                  "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a",
                                           :target                 "senoravaca",
                                           :phrase-text-translated "Adios ardilla! Adios!",
                                           :data
                                                                   [{:end 46.127, :anim "talk", :start 44.847, :duration 1.28}
                                                                    {:end 47.181, :anim "talk", :start 46.634, :duration 0.547}]},
                  :mari-letter
                                          {:offset      3.053,
                                           :scene-id    "magic-hat",
                                           :phrase-text "A",
                                           :start       3.053,
                                           :type        "animation-sequence",
                                           :duration    0.32,
                                           :audio       "/raw/audio/l2/mari-chants.m4a",
                                           :target      "mari",
                                           :data        [{:end 3.32, :anim "talk", :start 3.16, :duration 0.16}]},
                  :mari-sound-3
                                          {:offset      2.987,
                                           :scene-id    "cycling-letters",
                                           :phrase-text "A, a, a...",
                                           :start       2.987,
                                           :type        "animation-sequence",
                                           :duration    1.533,
                                           :audio       "/raw/audio/l2/mari-chants.m4a",
                                           :target      "mari",
                                           :data
                                                        [{:end 4.426, :anim "talk", :start 3.067, :duration 1.359}]},
                  :mari-chant
                                          {:offset      0.573,
                                           :scene-id    "magic-hat",
                                           :phrase-text "Ardilla, ardilla, a, a, a...",
                                           :start       0.573,
                                           :type        "animation-sequence",
                                           :duration    3.96,
                                           :audio       "/raw/audio/l2/mari-chants.m4a",
                                           :target      "mari",
                                           :data
                                                        [{:end 1.627, :anim "talk", :start 0.653, :duration 0.974}
                                                         {:end 2.733, :anim "talk", :start 1.827, :duration 0.906}
                                                         {:end 3.293, :anim "talk", :start 3.16, :duration 0.133}
                                                         {:end 3.84, :anim "talk", :start 3.706, :duration 0.134}
                                                         {:end 4.36, :anim "talk", :start 4.2, :duration 0.16}]},
                  :letter-tutorial-path
                                          "M72.5,131.2a37.5,37.5,0,1,1,0-39.3m33.5,0a37.6,37.6,0,0,1,69.5,19.7v37.5M106,0V149",
                  :letter-intro-song
                                          {:offset      7.2,
                                           :scene-id    "letter-intro",
                                           :phrase-text "Ardilla, ardilla, a, a, A!",
                                           :start       7.2,
                                           :type        "animation-sequence",
                                           :duration    3.974,
                                           :audio
                                                        "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_1.m4a",
                                           :target      "senoravaca",
                                           :data
                                                        [{:end 8.054, :anim "talk", :start 7.36, :duration 0.694}
                                                         {:end 9.094, :anim "talk", :start 8.334, :duration 0.76}
                                                         {:end 9.814, :anim "talk", :start 9.734, :duration 0.08}
                                                         {:end 10.267, :anim "talk", :start 10.107, :duration 0.16}
                                                         {:end 10.96, :anim "talk", :start 10.694, :duration 0.266}]},
                  :sandbox-state-word-4   "arana",
                  :sandbox-state-word-3   "avion",
                  :home-group-word        "group-word-ardilla",
                  :sandbox-state-word-1   "abeja",
                  :sandbox-state-word-2-action
                                          {:id          "/raw/audio/l1/a4/game-voice-set-1.mp3",
                                           :data
                                                        [{:id     "show",
                                                          :type   "state",
                                                          :params {:text "Arbol"},
                                                          :target "word"}
                                                         {:id "voice-1", :type "audio", :start 7.873, :duration 0.871}
                                                         {:id "default", :type "state", :target "word"}],
                                           :type        "audio",
                                           :start       1.453,
                                           :duration    0.934,
                                           :scene-id    "sandbox",
                                           :description ":sandbox-state-word-arbol"},
                  :sandbox-change-skin-1-action
                                          {:skin        "bee",
                                           :type        "set-skin",
                                           :target      "box1",
                                           :scene-id    "sandbox",
                                           :description ":box-1-set-skin-abeja"},
                  :book-content-action
                                          {:data
                                                     [{:id     "default",
                                                       :type   "state",
                                                       :params {:text "a"},
                                                       :target "letter"}
                                                      {:id     "default",
                                                       :type   "state",
                                                       :params
                                                               {:src "/raw/img/elements/squirrel.png", :filter "grayscale"},
                                                       :target "image"}
                                                      {:id     "default",
                                                       :type   "state",
                                                       :params {:src "/raw/img/elements/bee.png", :filter "grayscale"},
                                                       :target "image2"}
                                                      {:id     "default",
                                                       :type   "state",
                                                       :params
                                                               {:src "/raw/img/elements/tree.png", :filter "grayscale"},
                                                       :target "image3"}],
                                           :type     "parallel",
                                           :scene-id "book"},
                  :word-2-skin            "tree",
                  :book-read-action
                                          {:id       "/raw/audio/l1/a6/lion/2La a.mp3",
                                           :type     "audio",
                                           :start    0.574,
                                           :duration 9.814,
                                           :scene-id "book"},
                  :letter-intro-letter-mari
                                          {:offset      1.12,
                                           :scene-id    "letter-intro",
                                           :phrase-text "a",
                                           :start       1.12,
                                           :type        "animation-sequence",
                                           :duration    1.013,
                                           :audio       "/raw/audio/l2/mari-letter-names.mp4",
                                           :target      "mari",
                                           :data        [{:end 1.89, :anim "talk", :start 1.3}]},
                  :swings-dialog          "dialog-ardilla",
                  :poem-story
                                          {:data
                                                     [{:id "run-bubble-1", :type "action"}
                                                      {:data     [{:end 3.47, :anim "talk", :start 0.19}],
                                                       :type     "animation-sequence",
                                                       :audio    "/raw/audio/l2/a3/poems-pro/ardilla.mp3",
                                                       :start    0.054,
                                                       :track    1,
                                                       :offset   0.054,
                                                       :target   "rock",
                                                       :duration 3.613}
                                                      {:id "poem-pause", :type "action"}
                                                      {:id "run-bubble-2", :type "action"}
                                                      {:data     [{:end 7.45, :anim "talk", :start 4.27}],
                                                       :type     "animation-sequence",
                                                       :audio    "/raw/audio/l2/a3/poems-pro/ardilla.mp3",
                                                       :start    4.094,
                                                       :track    1,
                                                       :offset   4.094,
                                                       :target   "rock",
                                                       :duration 3.493}
                                                      {:id "poem-pause", :type "action"}
                                                      {:data     [{:end 10.65, :anim "talk", :start 8.21}],
                                                       :type     "animation-sequence",
                                                       :audio    "/raw/audio/l2/a3/poems-pro/ardilla.mp3",
                                                       :start    8.107,
                                                       :track    1,
                                                       :offset   8.107,
                                                       :target   "rock",
                                                       :duration 2.68}
                                                      {:id "poem-pause", :type "action"}
                                                      {:id "run-bubble-3", :type "action"}
                                                      {:data     [{:end 14.13, :anim "talk", :start 11.33}],
                                                       :type     "animation-sequence",
                                                       :audio    "/raw/audio/l2/a3/poems-pro/ardilla.mp3",
                                                       :start    11.214,
                                                       :track    1,
                                                       :offset   11.214,
                                                       :target   "rock",
                                                       :duration 2.933}
                                                      {:id "poem-pause", :type "action"}
                                                      {:data
                                                                 [{:end 16.16, :anim "talk", :start 14.89}
                                                                  {:end 17.8, :anim "talk", :start 16.57}],
                                                       :type     "animation-sequence",
                                                       :audio    "/raw/audio/l2/a3/poems-pro/ardilla.mp3",
                                                       :start    14.666,
                                                       :track    1,
                                                       :offset   14.666,
                                                       :target   "rock",
                                                       :duration 3.486}
                                                      {:id "poem-pause", :type "action"}],
                                           :type     "sequence-data",
                                           :scene-id "park-poem"},
                  :seesaw-voice-low       "word-ardilla-low",
                  :vaca-chanting-word
                                          {:offset      22.007,
                                           :phrase-text "Ardilla",
                                           :start       22.007,
                                           :type        "animation-sequence",
                                           :duration    1.208,
                                           :audio       "/raw/audio/l2/a1/L2_A1_Vaca_2.m4a",
                                           :target      "senoravaca",
                                           :track       1,
                                           :data        [{:end 23.066, :anim "talk", :start 22.201}]},
                  :riddle
                                          {:id          "/raw/audio/l2/a5/riddles/ardilla.mp3",
                                           :type        "audio",
                                           :start       0.021,
                                           :duration    9.628,
                                           :scene-id    "slide",
                                           :phrase-text "Riddle"},
                  :home-vaca-this-is-action
                                          {:offset                 15.651,
                                           :scene-id               "home",
                                           :phrase-text            "Este es un ardilla",
                                           :start                  15.651,
                                           :type                   "animation-sequence",
                                           :duration               2.28,
                                           :id                     "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a",
                                           :audio                  "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a",
                                           :target                 "senoravaca",
                                           :phrase-text-translated "Este es un ardilla",
                                           :data
                                                                   [{:end 17.264, :anim "talk", :start 15.705, :duration 1.559}
                                                                    {:end 17.798, :anim "talk", :start 17.438, :duration 0.36}]},
                  :sandbox-state-word-1-action
                                          {:id          "/raw/audio/l1/a4/game-voice-set-1.mp3",
                                           :data
                                                        [{:id     "show",
                                                          :type   "state",
                                                          :params {:text "Abeja"},
                                                          :target "word"}
                                                         {:id "voice-1", :type "audio", :start 6.218, :duration 0.871}
                                                         {:id "default", :type "state", :target "word"}],
                                           :type        "audio",
                                           :start       0.187,
                                           :duration    1,
                                           :scene-id    "sandbox",
                                           :description ":sandbox-state-word-abeja"},
                  :game-voice-action-low
                                          {:id                     "/raw/audio/l1/a2/game-voice-1-redo.mp3",
                                           :type                   "audio",
                                           :audio                  "/raw/audio/l1/a2/game-voice-1-redo.mp3",
                                           :start                  0.307,
                                           :duration               3.998,
                                           :scene-id               "see-saw",
                                           :phrase-text            "Ardilla. Ardilla.  ARDILLA!",
                                           :phrase-text-translated "Ardilla. Ardilla.  ARDILLA!"},
                  :skin                   "squirrel",
                  :home-vaca-question-action
                                          {:offset                 19.371,
                                           :scene-id               "home",
                                           :phrase-text            "Ardilla",
                                           :start                  19.371,
                                           :type                   "animation-sequence",
                                           :duration               0.826,
                                           :id                     "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a",
                                           :audio                  "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a",
                                           :target                 "senoravaca",
                                           :phrase-text-translated "Ardilla",
                                           :data
                                                                   [{:end 19.984, :anim "talk", :start 19.424, :duration 0.56}]},
                  :concept-rest           "rdilla",
                  :sandbox-change-skin-3-action
                                          {:skin        "airplane",
                                           :type        "set-skin",
                                           :target      "box3",
                                           :scene-id    "sandbox",
                                           :description ":box-1-set-skin-avion"}}})
