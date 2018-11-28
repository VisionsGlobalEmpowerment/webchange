(ns webchange.scene)

(def home-scene
  {:assets
                  [{:url  "/raw/audio/background/POL-daily-special-short.mp3",
                    :size 10,
                    :type "audio"}
                   {:url  "/raw/audio/effects/NFF-fruit-collected.mp3",
                    :size 1,
                    :type "audio"}
                   {:url "/raw/audio/effects/NFF-glitter.mp3", :size 1, :type "audio"}
                   {:url  "/raw/audio/effects/NFF-robo-elastic.mp3",
                    :size 1,
                    :type "audio"}
                   {:url  "/raw/audio/effects/NFF-rusted-thing.mp3",
                    :size 1,
                    :type "audio"}
                   {:url "/raw/audio/effects/NFF-zing.mp3", :size 1, :type "audio"}
                   {:url  "/raw/audio/scripts/intro/teacher.mp3",
                    :size 5,
                    :type "audio"}
                   {:url "/raw/audio/scripts/intro/vera.mp3", :size 5, :type "audio"}
                   {:url  "/raw/audio/scripts/intro/syllables.mp3",
                    :size 2,
                    :type "audio"}
                   {:url "/raw/img/map/background.png", :size 10, :type "image"}
                   {:url "/raw/img/map/casa_01.png", :size 1, :type "image"}
                   {:url "/raw/img/map/casa_02.png", :size 1, :type "image"}
                   {:url "/raw/img/map/feria_01.png", :size 1, :type "image"}
                   {:url "/raw/img/map/feria_02.png", :size 1, :type "image"}
                   {:url "/raw/img/map/feria_03.png", :size 1, :type "image"}
                   {:url "/raw/img/map/feria_locked.png", :size 1, :type "image"}
                   {:url "/raw/img/casa/background.jpg", :size 10}
                   {:url "/raw/img/casa_door.png", :size 1, :type "image"}
                   {:url "/raw/img/chat_bubble_big.png", :size 1, :type "image"}
                   {:url "/raw/img/teacher.png", :size 1, :type "image"}
                   {:url "/raw/img/teacher_two.png", :size 1, :type "image"}
                   {:url "/raw/img/vera.png", :size 1, :type "image"}
                   {:url "/raw/img/vera/10_sprite_test.png", :size 4, :type "image"}
                   {:url "/raw/img/feria/background.png", :size 10, :type "image"}
                   {:url "/raw/img/feria/back.png", :size 1, :type "image"}
                   {:url "/raw/img/feria/back_active.png", :size 1, :type "image"}
                   {:url "/raw/img/feria/wheel.png", :size 2, :type "image"}
                   {:url "/raw/img/ui/back_button_01.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/back_button_02.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/close_button_01.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/close_button_02.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/play_button_01.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/play_button_02.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/reload_button_01.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/reload_button_02.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/settings_button_01.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/settings_button_02.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/star_01.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/star_02.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/star_03.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/form.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/clear.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/next_button_01.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/next_button_02.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/vera.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/settings/music.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/settings/music_icon.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/settings/sound_fx.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/settings/sound_fx_icon.png", :size 1, :type "image"}
                   {:url "/raw/img/ui/settings/settings.png", :size 1, :type "image"}
                   {:url  "/raw/img/ferris-wheel/words/Grapes.png",
                    :size 1,
                    :type "image"}
                   {:url  "/raw/img/ferris-wheel/words/Spoon.png",
                    :size 1,
                    :type "image"}
                   {:url  "/raw/img/ferris-wheel/words/Fork.png",
                    :size 1,
                    :type "image"}],
   :objects
                  {:background
                   {:type "background", :src "/raw/img/casa/background.jpg"},
                   :vera
                   {:type  "image",
                    :x     1050,
                    :y     400,
                    :src   "/raw/img/vera.png",
                    :layer 10,
                    :scale {:x 1, :y 1}},
                   :senora-vaca
                   {:type    "image",
                    :x       557,
                    :y       177,
                    :src     "/raw/img/teacher.png",
                    :layer   10,
                    :actions {:click {:type "action", :id "intro", :on "click"}}},
                   :door
                   {:type   "transparent",
                    :x      1146,
                    :y      42,
                    :width  732,
                    :height 810,
                    :states
                            {:default {:type "transparent", :src nil},
                             :hover   {:type "image", :src "/raw/img/casa_door.png"}},
                    :actions
                            {:mouseover
                                    {:type "state", :target "door", :id "hover", :on "mouseover"},
                             :mouseout
                                    {:type "state", :target "door", :id "default", :on "mouseout"},
                             :click {:type "scene", :scene-id "map", :on "click"}}}},
   :scene-objects [["background" "door"] ["vera" "senora-vaca"]],
   :actions
                  {:show-word-tenedor
                                   {:type "state", :target "wordImage", :id "tenedor"},
                   :show-word-uvas {:type "state", :target "wordImage", :id "uvas"},
                   :vera-audio-3
                                   {:type "audio", :id "vera", :start 7.871, :duration 2.59},
                   :senora-vaca-audio-5
                                   {:type "audio", :id "teacher", :start 17.577, :duration 5.084},
                   :senora-vaca-audio-7
                                   {:type "audio", :id "teacher", :start 29.75, :duration 2.2},
                   :senora-vaca-audio-2
                                   {:type "audio", :id "teacher", :start 4.453, :duration 6.266},
                   :show-word-cuchara
                                   {:type "state", :target "wordImage", :id "cuchara"},
                   :syllable-cu
                                   {:type "parallel",
                                    :data
                                          [{:type     "audio",
                                            :name     "syllable",
                                            :id       "syllables",
                                            :start    2.507,
                                            :duration 0.609,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap",
                                            :id       "syllables",
                                            :start    2.507,
                                            :duration 0.609,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap",
                                            :target "senoraVaca",
                                            :id     "clap"}
                                           {:type   "state",
                                            :name   "syllable",
                                            :target "syllable",
                                            :id     "show",
                                            :params {:text "cu"}}]},
                   :syllable-te
                                   {:type "parallel",
                                    :data
                                          [{:type     "audio",
                                            :name     "syllable-teacher",
                                            :id       "syllables",
                                            :start    4.21,
                                            :duration 0.646,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap-teacher",
                                            :id       "syllables",
                                            :start    4.21,
                                            :duration 0.646,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap-teacher",
                                            :target "senoraVaca",
                                            :id     "clap"}
                                           {:type     "audio",
                                            :name     "syllable-vera",
                                            :id       "syllables",
                                            :start    7.625,
                                            :duration 0.552,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap-vera",
                                            :id       "syllables",
                                            :start    7.625,
                                            :duration 0.552,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap-vera",
                                            :target "vera",
                                            :id     "clap"}
                                           {:type   "state",
                                            :name   "syllable",
                                            :target "syllable",
                                            :id     "show",
                                            :params {:text "te"}}]},
                   :syllable-ra
                                   {:type "parallel",
                                    :data
                                          [{:type     "audio",
                                            :name     "syllable",
                                            :id       "syllables",
                                            :start    2.035,
                                            :duration 0.388,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap",
                                            :id       "syllables",
                                            :start    2.035,
                                            :duration 0.388,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap",
                                            :target "senoraVaca",
                                            :id     "clap"}]},
                   :senora-vaca-audio-4
                                   {:type "audio", :id "teacher", :start 13.478, :duration 3.232},
                   :syllable-dor
                                   {:type "parallel",
                                    :data
                                          [{:type     "audio",
                                            :name     "syllable-teacher",
                                            :id       "syllables",
                                            :start    5.54,
                                            :duration 0.561,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap-teacher",
                                            :id       "syllables",
                                            :start    5.54,
                                            :duration 0.561,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap-teacher",
                                            :target "senoraVaca",
                                            :id     "clap"}
                                           {:type     "audio",
                                            :name     "syllable-vera",
                                            :id       "syllables",
                                            :start    8.798,
                                            :duration 0.813,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap-vera",
                                            :id       "syllables",
                                            :start    8.798,
                                            :duration 0.813,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap-vera",
                                            :target "vera",
                                            :id     "clap"}
                                           {:type   "state",
                                            :name   "syllable",
                                            :target "syllable",
                                            :id     "show",
                                            :params {:text "dor"}}]},
                   :syllable-u
                                   {:type "parallel",
                                    :data
                                          [{:type     "audio",
                                            :name     "syllable",
                                            :id       "syllables",
                                            :start    0.029,
                                            :duration 0.7,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap",
                                            :id       "syllables",
                                            :start    0.029,
                                            :duration 0.7,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap",
                                            :target "senoraVaca",
                                            :id     "clap"}
                                           {:type   "state",
                                            :name   "syllable",
                                            :target "syllable",
                                            :id     "show",
                                            :params {:text "u"}}]},
                   :group-tenedor
                                   {:type "sequence",
                                    :name "u-vas syllables",
                                    :data
                                          ["show-word-tenedor"
                                           "syllable-te"
                                           "syllable-ne"
                                           "syllable-dor"
                                           "empty-1"
                                           "syllable-te"
                                           "syllable-ne"
                                           "syllable-dor2"
                                           "empty-1"
                                           "syllable-te"
                                           "syllable-ne"
                                           "syllable-dor3"
                                           "empty-1"
                                           "hide-word"
                                           "hide-syllable"]},
                   :group-vera
                                   {:type "sequence",
                                    :name "vera syllables",
                                    :data
                                          ["syllable-ve"
                                           "syllable-ra"
                                           "empty-1"
                                           "syllable-ve"
                                           "syllable-ra"
                                           "empty-1"]},
                   :syllable-ra2
                                   {:type "parallel",
                                    :data
                                          [{:type     "audio",
                                            :name     "syllable",
                                            :id       "syllables",
                                            :start    3.829,
                                            :duration 0.362,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap",
                                            :id       "syllables",
                                            :start    3.829,
                                            :duration 0.362,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap",
                                            :target "senoraVaca",
                                            :id     "clap"}
                                           {:type   "state",
                                            :name   "syllable",
                                            :target "syllable",
                                            :id     "show",
                                            :params {:text "ra"}}]},
                   :syllable-cha
                                   {:type "parallel",
                                    :data
                                          [{:type     "audio",
                                            :name     "syllable",
                                            :id       "syllables",
                                            :start    3.178,
                                            :duration 0.628,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap",
                                            :id       "syllables",
                                            :start    3.178,
                                            :duration 0.628,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap",
                                            :target "senoraVaca",
                                            :id     "clap"}
                                           {:type   "state",
                                            :name   "syllable",
                                            :target "syllable",
                                            :id     "show",
                                            :params {:text "cha"}}]},
                   :senora-vaca-audio-6
                                   {:type "audio", :id "teacher", :start 23.09, :duration 1.86},
                   :syllable-ne
                                   {:type "parallel",
                                    :data
                                          [{:type     "audio",
                                            :name     "syllable-teacher",
                                            :id       "syllables",
                                            :start    4.878,
                                            :duration 0.653,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap-teacher",
                                            :id       "syllables",
                                            :start    4.878,
                                            :duration 0.653,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap-teacher",
                                            :target "senoraVaca",
                                            :id     "clap"}
                                           {:type     "audio",
                                            :name     "syllable-vera",
                                            :id       "syllables",
                                            :start    8.211,
                                            :duration 0.56,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap-vera",
                                            :id       "syllables",
                                            :start    8.211,
                                            :duration 0.56,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap-vera",
                                            :target "vera",
                                            :id     "clap"}
                                           {:type   "state",
                                            :name   "syllable",
                                            :target "syllable",
                                            :id     "show",
                                            :params {:text "ne"}}]},
                   :senora-vaca-audio-1
                                   {:type "audio", :id "teacher", :start 0.749, :duration 2.68},
                   :syllable-ve
                                   {:type "parallel",
                                    :data
                                          [{:type     "audio",
                                            :name     "syllable",
                                            :id       "syllables",
                                            :start    1.383,
                                            :duration 0.633,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap",
                                            :id       "syllables",
                                            :start    1.383,
                                            :duration 0.633,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap",
                                            :target "senoraVaca",
                                            :id     "clap"}]},
                   :senora-vaca-audio-3
                                   {:type "audio", :id "teacher", :start 11.508, :duration 1.931},
                   :intro
                                   {:type "sequence",
                                    :data
                                          ["senora-vaca-audio-1"
                                           "vera-audio-1"
                                           "senora-vaca-audio-2"
                                           "vera-audio-2"
                                           "vera-audio-3"
                                           "senora-vaca-audio-3"
                                           "senora-vaca-audio-4"
                                           "vera-audio-4"
                                           "senora-vaca-audio-5"
                                           "senora-vaca-audio-6"
                                           "group-vera"
                                           "vera-audio-5"
                                           "senora-vaca-audio-7"
                                           "group-uvas"
                                           "senora-vaca-audio-8"
                                           "group-cuchara"
                                           "vera-audio-6"
                                           "senora-vaca-audio-9"
                                           "group-tenedor"
                                           "senora-vaca-audio-10"]},
                   :syllable-dor3
                                   {:type "parallel",
                                    :data
                                          [{:type     "audio",
                                            :name     "syllable-teacher",
                                            :id       "syllables",
                                            :start    6.743,
                                            :duration 0.87,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap-teacher",
                                            :id       "syllables",
                                            :start    6.743,
                                            :duration 0.87,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap-teacher",
                                            :target "senoraVaca",
                                            :id     "clap"}
                                           {:type     "audio",
                                            :name     "syllable-vera",
                                            :id       "syllables",
                                            :start    8.798,
                                            :duration 0.813,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap-vera",
                                            :id       "syllables",
                                            :start    8.798,
                                            :duration 0.813,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap-vera",
                                            :target "vera",
                                            :id     "clap"}
                                           {:type   "state",
                                            :name   "syllable",
                                            :target "syllable",
                                            :id     "show",
                                            :params {:text "dor"}}]},
                   :hide-word      {:type "state", :target "wordImage", :id "default"},
                   :hide-syllable  {:type "state", :target "syllable", :id "default"},
                   :group-uvas
                                   {:type "sequence",
                                    :name "u-vas syllables",
                                    :data
                                          ["show-word-uvas"
                                           "syllable-u"
                                           "syllable-vas"
                                           "empty-1"
                                           "syllable-u"
                                           "syllable-vas"
                                           "empty-1"
                                           "syllable-u"
                                           "syllable-vas"
                                           "empty-1"
                                           "hide-word"
                                           "hide-syllable"]},
                   :vera-audio-1
                                   {:type "audio", :id "vera", :start 1.1, :duration 4.4232},
                   :syllable-vas
                                   {:type "parallel",
                                    :data
                                          [{:type     "audio",
                                            :name     "syllable",
                                            :id       "syllables",
                                            :start    0.744,
                                            :duration 0.607,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap",
                                            :id       "syllables",
                                            :start    0.744,
                                            :duration 0.607,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap",
                                            :target "senoraVaca",
                                            :id     "clap"}
                                           {:type   "state",
                                            :name   "syllable",
                                            :target "syllable",
                                            :id     "show",
                                            :params {:text "vas"}}]},
                   :syllable-dor2
                                   {:type "parallel",
                                    :data
                                          [{:type     "audio",
                                            :name     "syllable-teacher",
                                            :id       "syllables",
                                            :start    6.119,
                                            :duration 0.615,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap-teacher",
                                            :id       "syllables",
                                            :start    6.119,
                                            :duration 0.615,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap-teacher",
                                            :target "senoraVaca",
                                            :id     "clap"}
                                           {:type     "audio",
                                            :name     "syllable-vera",
                                            :id       "syllables",
                                            :start    8.798,
                                            :duration 0.813,
                                            :offset   0.1}
                                           {:type     "audio",
                                            :name     "clap-vera",
                                            :id       "syllables",
                                            :start    8.798,
                                            :duration 0.813,
                                            :offset   0.1}
                                           {:type   "animation",
                                            :name   "clap-vera",
                                            :target "vera",
                                            :id     "clap"}
                                           {:type   "state",
                                            :name   "syllable",
                                            :target "syllable",
                                            :id     "show",
                                            :params {:text "dor"}}]},
                   :group-cuchara
                                   {:type "sequence",
                                    :name "u-vas syllables",
                                    :data
                                          ["show-word-cuchara"
                                           "syllable-cu"
                                           "syllable-cha"
                                           "syllable-ra2"
                                           "empty-1"
                                           "syllable-cu"
                                           "syllable-cha"
                                           "syllable-ra2"
                                           "empty-1"
                                           "syllable-cu"
                                           "syllable-cha"
                                           "syllable-ra2"
                                           "empty-1"
                                           "hide-word"
                                           "hide-syllable"]},
                   :vera-audio-4
                                   {:type "audio", :id "vera", :start 11.426, :duration 1.253},
                   :vera-audio-5
                                   {:type "audio", :id "vera", :start 14.1, :duration 0.948},
                   :empty-1        {:type "empty", :duration 600},
                   :senora-vaca-audio-10
                                   {:type "audio", :id "teacher", :start 61.686, :duration 6.759},
                   :vera-audio-2
                                   {:type "audio", :id "vera", :start 6.365, :duration 1.405},
                   :senora-vaca-audio-9
                                   {:type "audio", :id "teacher", :start 50.809, :duration 1.409},
                   :senora-vaca-audio-8
                                   {:type "audio", :id "teacher", :start 38.102, :duration 4.739},
                   :vera-audio-6
                                   {:type "audio", :id "vera", :start 16.267, :duration 3.809}},
   :audio
                  {:teacher   "/raw/audio/scripts/intro/teacher.mp3",
                   :vera      "/raw/audio/scripts/intro/vera.mp3",
                   :syllables "/raw/audio/scripts/intro/syllables.mp3"},
   :metadata      {:autostart true}})

(def map-scene {:assets
                               [{:url  "/raw/audio/background/POL-daily-special-short.mp3" :size 10 :type "audio"}
                                {:url  "/raw/audio/effects/NFF-fruit-collected.mp3" :size 1 :type "audio"}
                                {:url "/raw/audio/effects/NFF-glitter.mp3", :size 1, :type "audio"}
                                {:url  "/raw/audio/effects/NFF-robo-elastic.mp3" :size 1 :type "audio"}
                                {:url  "/raw/audio/effects/NFF-rusted-thing.mp3" :size 1 :type "audio"}
                                {:url "/raw/audio/effects/NFF-zing.mp3", :size 1, :type "audio"}
                                {:url "/raw/img/map/background.png", :size 10, :type "image"}
                                {:url "/raw/img/map/casa_01.png", :size 1, :type "image"}
                                {:url "/raw/img/map/casa_02.png", :size 1, :type "image"}
                                {:url "/raw/img/map/feria_01.png", :size 1, :type "image"}
                                {:url "/raw/img/map/feria_02.png", :size 1, :type "image"}
                                {:url "/raw/img/map/feria_03.png", :size 1, :type "image"}
                                {:url "/raw/img/map/feria_locked.png", :size 1, :type "image"}
                                {:url "/raw/img/ui/back_button_01.png", :size 1, :type "image"}
                                {:url "/raw/img/ui/back_button_02.png", :size 1, :type "image"}
                                {:url "/raw/img/ui/close_button_01.png", :size 1, :type "image"}
                                {:url "/raw/img/ui/close_button_02.png", :size 1, :type "image"}
                                {:url "/raw/img/ui/play_button_01.png", :size 1, :type "image"}
                                {:url "/raw/img/ui/play_button_02.png", :size 1, :type "image"}
                                {:url "/raw/img/ui/reload_button_01.png", :size 1, :type "image"}
                                {:url "/raw/img/ui/reload_button_02.png", :size 1, :type "image"}
                                {:url "/raw/img/ui/settings_button_01.png", :size 1, :type "image"}
                                {:url "/raw/img/ui/settings_button_02.png", :size 1, :type "image"}
                                {:url "/raw/img/ui/star_01.png", :size 1, :type "image"}
                                {:url "/raw/img/ui/star_02.png", :size 1, :type "image"}
                                {:url "/raw/img/ui/star_03.png", :size 1, :type "image"}],
                :objects
                               {:background {:type "background", :src "/raw/img/map/background.png"},
                                :vera {:type "image":x 1045, :y 540 :scale {:x 0.2 :y 0.2}
                                       :src "/raw/img/vera.png" :transition "vera-transition"}
                                :home
                                {:type   "image",
                                 :x      731,
                                 :y      340,
                                 :width  433,
                                 :height 380,
                                 :src    "/raw/img/map/casa_01.png",
                                 :states
                                         {:default {:type "image", :src "/raw/img/map/casa_01.png"},
                                          :hover   {:type "image", :src "/raw/img/map/casa_02.png"}},
                                 :actions
                                         {:mouseover
                                                 {:type "state", :target "home", :id "hover", :on "mouseover"},
                                          :mouseout
                                                 {:type "state", :target "home", :id "default", :on "mouseout"},
                                          :click {:type "action", :id "move-to-home", :on "click"}}},
                                :feria
                                {:type   "image",
                                 :x      235,
                                 :y      683,
                                 :width  319,
                                 :height 280,
                                 :src    "/raw/img/map/feria_01.png",
                                 :states
                                         {:default {:type "image", :src "/raw/img/map/feria_01.png"},
                                          :hover   {:type "image", :src "/raw/img/map/feria_02.png"}},
                                 :actions
                                         {:mouseover
                                                 {:type "state", :target "feria", :id "hover", :on "mouseover"},
                                          :mouseout
                                                 {:type "state", :target "feria", :id "default", :on "mouseout"},
                                          :click {:type "action", :id "move-to-feria", :on "click"}}}},
                :scene-objects [["background" "home" "feria"] ["vera"]],
                :actions
                               {:move-to-feria-transition-1
                                            {:type          "transition",
                                             :transition-id "vera-transition",
                                             :to            {:x 915, :y 601}},
                                :open-home  {:type "scene", :scene-id "home"},
                                :move-to-feria-transition-4
                                            {:type          "transition",
                                             :transition-id "vera-transition",
                                             :to            {:x 850, :y 813}},
                                :move-to-home-transition
                                            {:type          "transition",
                                             :transition-id "vera-transition",
                                             :to            {:x 975, :y 495}},
                                :open-feria {:type "scene", :scene-id "feria"},
                                :move-to-feria-transition-2
                                            {:type          "transition",
                                             :transition-id "vera-transition",
                                             :to            {:x 950, :y 646}},
                                :move-to-feria-transition-3
                                            {:type          "transition",
                                             :transition-id "vera-transition",
                                             :to            {:x 870, :y 726}},
                                :move-to-home
                                            {:type "sequence", :data ["move-to-home-transition" "open-home"]},
                                :move-to-feria-transition-5
                                            {:type          "transition",
                                             :transition-id "vera-transition",
                                             :to            {:x 565, :y 835}},
                                :move-to-feria
                                            {:type "sequence",
                                             :data
                                                   ["move-to-feria-transition-1"
                                                    "move-to-feria-transition-2"
                                                    "move-to-feria-transition-3"
                                                    "move-to-feria-transition-4"
                                                    "move-to-feria-transition-5"
                                                    "open-feria"]}},
                :metadata      {:autostart true}})

(def feria-scene {:assets
                                 [{:url  "/raw/audio/background/POL-daily-special-short.mp3",
                                   :size 10,
                                   :type "audio"}
                                  {:url  "/raw/audio/effects/NFF-fruit-collected.mp3",
                                   :size 1,
                                   :type "audio"}
                                  {:url "/raw/audio/effects/NFF-glitter.mp3", :size 1, :type "audio"}
                                  {:url  "/raw/audio/effects/NFF-robo-elastic.mp3",
                                   :size 1,
                                   :type "audio"}
                                  {:url  "/raw/audio/effects/NFF-rusted-thing.mp3",
                                   :size 1,
                                   :type "audio"}
                                  {:url "/raw/audio/effects/NFF-zing.mp3", :size 1, :type "audio"}
                                  {:url "/raw/img/feria/background.png", :size 10, :type "image"}
                                  {:url "/raw/img/feria/back.png", :size 1, :type "image"}
                                  {:url "/raw/img/feria/back_active.png", :size 1, :type "image"}
                                  {:url "/raw/img/feria/wheel.png", :size 2, :type "image"}
                                  {:url "/raw/img/ui/back_button_01.png", :size 1, :type "image"}
                                  {:url "/raw/img/ui/back_button_02.png", :size 1, :type "image"}
                                  {:url "/raw/img/ui/close_button_01.png", :size 1, :type "image"}
                                  {:url "/raw/img/ui/close_button_02.png", :size 1, :type "image"}
                                  {:url "/raw/img/ui/play_button_01.png", :size 1, :type "image"}
                                  {:url "/raw/img/ui/play_button_02.png", :size 1, :type "image"}
                                  {:url "/raw/img/ui/reload_button_01.png", :size 1, :type "image"}
                                  {:url "/raw/img/ui/reload_button_02.png", :size 1, :type "image"}
                                  {:url "/raw/img/ui/settings_button_01.png", :size 1, :type "image"}
                                  {:url "/raw/img/ui/settings_button_02.png", :size 1, :type "image"}
                                  {:url "/raw/img/ui/star_01.png", :size 1, :type "image"}
                                  {:url "/raw/img/ui/star_02.png", :size 1, :type "image"}
                                  {:url "/raw/img/ui/star_03.png", :size 1, :type "image"}],
                  :objects
                                 {:background
                                  {:type "background", :src "/raw/img/feria/background.png"},
                                  :wheel
                                  {:type   "transparent",
                                   :x      467,
                                   :y      105,
                                   :width  708,
                                   :height 778,
                                   :states
                                           {:default {:type "transparent", :src nil},
                                            :hover   {:type "image", :src "/raw/img/feria/wheel.png"}},
                                   :actions
                                           {:mouseover {:type "state", :target "wheel", :id "hover", :on "mouseover"},
                                            :mouseout  {:type "state", :target "wheel", :id "default", :on "mouseout"}
                                            :click     {:type "scene", :scene-id "ferris-wheel", :on "click"}},
                                   :src    nil},
                                  :exit
                                  {:type "image",
                                   :x    1600,
                                   :y    800,
                                   :src  "/raw/img/feria/back.png",
                                   :states
                                         {:default {:src "/raw/img/feria/back.png"},
                                          :hover   {:src "/raw/img/feria/back_active.png"}},
                                   :actions
                                         {:mouseover
                                                 {:type "state", :target "exit", :id "hover", :on "mouseover"},
                                          :mouseout
                                                 {:type "state", :target "exit", :id "default", :on "mouseout"},
                                          :click {:type "scene", :scene-id "map", :on "click"}}},
                                  :vera
                                  {:type  "image",
                                   :x     1100,
                                   :y     650,
                                   :src   "/raw/img/vera.png",
                                   :scale {:x 0.55, :y 0.55}}},
                  :scene-objects [["background" "wheel" "exit"] ["vera"]],
                  :metadata      {:autostart true}})

(def ferris-wheel-scene
  {:assets
                  [{:url "/raw/audio/ferris-wheel/instructions.mp3", :size 10, :type "audio"}

                   {:url "/raw/img/ferris-wheel/background.png", :size 10, :type "image"},
                   {:url "/raw/img/ferris-wheel/cloud_01.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/cloud_02.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/cloud_03.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/cloud_04.png", :size 1, :type "image"},

                   {:url "/raw/img/ferris-wheel/ferris_wheel_01.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/ferris_wheel_02.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/ferris_wheel_03.png", :size 1, :type "image"},

                   {:url "/raw/img/ferris-wheel/words/bat_alert.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/words/bat_default.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/words/bat_done.png", :size 1, :type "image"},

                   {:url "/raw/img/ferris-wheel/words/broccoli_alert.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/words/broccoli_default.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/words/broccoli_done.png", :size 1, :type "image"},

                   {:url "/raw/img/ferris-wheel/words/crocodile_alert.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/words/crocodile_default.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/words/crocodile_done.png", :size 1, :type "image"},

                   {:url "/raw/img/ferris-wheel/words/dinosaur_alert.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/words/dinosaur_default.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/words/dinosaur_done.png", :size 1, :type "image"},

                   {:url "/raw/img/ferris-wheel/words/orange_alert.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/words/orange_default.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/words/orange_done.png", :size 1, :type "image"},

                   {:url "/raw/img/ferris-wheel/words/whale_alert.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/words/whale_default.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/words/whale_done.png", :size 1, :type "image"},],
   :objects
                  {:background {:type "background", :src "/raw/img/ferris-wheel/background.png"},
                   :wheel      {:type "group" :x 816 :y 457 :children ["wheel-1", "wheel-2", "wheel-3", "items"]}
                   :wheel-1    {:type "image" :width 772 :height 772 :transition "wheel-1"
                                :src  "/raw/img/ferris-wheel/ferris_wheel_01.png" :origin {:type "center-center"}},
                   :wheel-2    {:type "image" :width 359 :height 527
                                :src  "/raw/img/ferris-wheel/ferris_wheel_02.png" :origin {:type "center-top"}},
                   :wheel-3    {:type "image" :width 261 :height 262 :transition "wheel-3"
                                :src  "/raw/img/ferris-wheel/ferris_wheel_03.png" :origin {:type "center-center"}},
                   :items      {:type     "group" :width 772 :height 772 :transition "items"
                                :children ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6"]
                                :origin   {:type "center-center"}}
                   :item-1     {:type     "placeholder" :width 205 :height 209 :x 599 :y 263 :transition "item-1" :rotation 360
                                :var-name "item-1" :image-src "src" :origin {:type "center-center"}
                                :actions {:click {:type "action", :id "check-current-word", :on "click"}}}
                   :item-2     {:type     "placeholder" :width 205 :height 209 :x 386 :y 140 :transition "item-2" :rotation 360
                                :var-name "item-2" :image-src "src" :origin {:type "center-center"}
                                :actions {:click {:type "action", :id "check-current-word", :on "click"}}}
                   :item-3     {:type     "placeholder" :width 205 :height 209 :x 173 :y 263 :transition "item-3" :rotation 360
                                :var-name "item-3" :image-src "src" :origin {:type "center-center"}
                                :actions {:click {:type "action", :id "check-current-word", :on "click"}}}
                   :item-4     {:type     "placeholder" :width 205 :height 209 :x 173 :y 509 :transition "item-4" :rotation 360
                                :var-name "item-4" :image-src "src" :origin {:type "center-center"}
                                :actions {:click {:type "action", :id "check-current-word", :on "click"}}}
                   :item-5     {:type     "placeholder" :width 205 :height 209 :x 386 :y 632 :transition "item-5" :rotation 360
                                :var-name "item-5" :image-src "src" :origin {:type "center-center"}
                                :actions {:click {:type "action", :id "check-current-word", :on "click"}}}
                   :item-6     {:type     "placeholder" :width 205 :height 209 :x 599 :y 509 :transition "item-6" :rotation 360
                                :var-name "item-6" :image-src "src" :origin {:type "center-center"}
                                :actions {:click {:type "action", :id "check-current-word", :on "click"}}}
                   :score      {:type "score" :successes "successes" :fails "fails"}
                   },
   :actions       {:rotate-wheel {:type "parallel",
                                  :data [{:type "transition" :transition-id "wheel-1" :to {:rotation 360 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "wheel-3" :to {:rotation 360 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "items" :to {:rotation 360 :duration 30 :loop true}}

                                         {:type "transition" :transition-id "item-1" :to {:rotation 0 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "item-2" :to {:rotation 0 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "item-3" :to {:rotation 0 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "item-4" :to {:rotation 0 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "item-5" :to {:rotation 0 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "item-6" :to {:rotation 0 :duration 30 :loop true}}]}
                   :start-game {:type "sequence"
                                :data ["renew-words" "renew-current-word" "repeat-current-word"]}
                   :renew-words  {:type      "dataset-var-provider"
                                  :provider-id        "words-set"
                                  :variables ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6"]
                                  :from      "items"}
                   :renew-current-word {:type "vars-var-provider"
                                        :provider-id "current-word"
                                        :variables ["current-word"]
                                        :from ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6"]
                                        :on-end "finish-game"}
                   :play-word {:type "placeholder-audio" :var-name "current-word" :id "audio-id" :start "start" :duration "duration" :offset "offset"}
                   :empty-5        {:type "empty", :duration 5000},
                   :repeat-current-word {:type "sequence"
                                 :tag "repeat-word"
                                 :data ["play-word", "empty-5", "repeat-current-word"]}
                   :clear-repeat-word {:type "remove-flows"
                                       :flow-tag "repeat-word"}
                   :restart-repeat-word {:type "sequence"
                                       :data ["clear-repeat-word" "increase-success" "renew-current-word" "repeat-current-word"]}
                   :increase-fail     {:type "counter"
                                       :counter-action "increase"
                                       :counter-id "fails"}
                   :increase-success  {:type "counter"
                                       :counter-action "increase"
                                       :counter-id "successes"}
                   :check-current-word {:type "test-var"
                                        :var-name "current-word"
                                        :property "src"
                                        :success "restart-repeat-word"
                                        :fail "increase-fail"}
                   :finish-game {:type "set-variable" :var-name "score" :var {:visible true}}
                   }

   :triggers      {:rotation {:on "start" :action "rotate-wheel"}
                   :start    {:on "start" :action "start-game"}}

   :datasets      {:items {:bat       {:id "bat" :src "/raw/img/ferris-wheel/words/bat_default.png" :audio-id "instructions" :start 45.119 :duration 3.184 :offset 1}
                           :broccoli  {:id "broccoli" :src "/raw/img/ferris-wheel/words/broccoli_default.png" :audio-id "instructions" :start 21.235, :duration 2.032 :offset 1}
                           :dinosaur  {:id "dinosaur" :src "/raw/img/ferris-wheel/words/dinosaur_default.png" :audio-id "instructions" :start 49.068, :duration 3.217 :offset 1}
                           :orange    {:id "orange" :src "/raw/img/ferris-wheel/words/orange_default.png" :audio-id "instructions" :start 70.751, :duration 2.19 :offset 1}
                           :crocodile {:id "crocodile" :src "/raw/img/ferris-wheel/words/crocodile_default.png" :audio-id "instructions" :start 11.328, :duration 2.66 :offset 1}
                           :whale     {:id "whale" :src "/raw/img/ferris-wheel/words/whale_default.png" :audio-id "instructions" :start 32.891, :duration 2.043 :offset 1}}}
   :scene-objects [["background" "wheel"]]
   :audio {:instructions "/raw/audio/ferris-wheel/instructions.mp3"}
   :metadata      {:autostart false
                   :next "feria"}})

(defn get-course
  [course-id]
  {:initial-scene "home"
   :preload       ["home", "map", "feria"]})

(defn get-scene
  [couse-id scene-id]
  (case scene-id
    "home" home-scene
    "map" map-scene
    "feria" feria-scene
    "ferris-wheel" ferris-wheel-scene))
