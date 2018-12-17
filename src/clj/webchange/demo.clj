(ns webchange.demo)

(def home-scene
  {:assets
                  [{:url "/raw/audio/background/POL-daily-special-short.mp3", :size 10, :type "audio"}
                   {:url "/raw/audio/effects/NFF-fruit-collected.mp3", :size 1, :type "audio"}
                   {:url "/raw/audio/effects/NFF-glitter.mp3", :size 1, :type "audio"}
                   {:url "/raw/audio/effects/NFF-robo-elastic.mp3", :size 1, :type "audio"}
                   {:url "/raw/audio/effects/NFF-rusted-thing.mp3", :size 1, :type "audio"}
                   {:url "/raw/audio/effects/NFF-zing.mp3", :size 1, :type "audio"}
                   {:url "/raw/audio/demo/welcome.mp3", :size 2, :type "audio"}
                   {:url "/raw/audio/demo/teacher.mp3", :size 5, :type "audio"}
                   {:url "/raw/audio/demo/vera.mp3", :size 5, :type "audio"}
                   {:url "/raw/audio/demo/intro-teacher-syllables.mp3", :size 2, :type "audio"}
                   {:url "/raw/audio/demo/intro-finish.mp3", :size 2, :type "audio"}
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
                   {:url "/raw/img/ferris-wheel/words/form_green.png", :size 1, :type "image"}
                   {:url "/raw/img/ferris-wheel/words/flower.png", :size 1, :type "image"}
                   {:url "/raw/img/ferris-wheel/words/violin.png", :size 1, :type "image"}
                   {:url "/raw/img/ferris-wheel/words/strawberry.png", :size 1, :type "image"}

                   {:url "/raw/img/butterfly.png", :size 2, :type "image"}

                   {:url "/raw/anim/senoravaca/skeleton.atlas", :size 1, :type "anim-text"}
                   {:url "/raw/anim/senoravaca/skeleton.json", :size 1, :type "anim-text"}
                   {:url "/raw/anim/senoravaca/skeleton.png", :size 1, :type "anim-texture"}
                   {:url "/raw/anim/senoravaca/skeleton2.png", :size 1, :type "anim-texture"}
                   {:url "/raw/anim/senoravaca/skeleton3.png", :size 1, :type "anim-texture"}
                   {:url "/raw/anim/senoravaca/skeleton4.png", :size 1, :type "anim-texture"}
                   {:url "/raw/anim/senoravaca/skeleton5.png", :size 1, :type "anim-texture"}
                   {:url "/raw/anim/senoravaca/skeleton6.png", :size 1, :type "anim-texture"}
                   {:url "/raw/anim/senoravaca/skeleton7.png", :size 1, :type "anim-texture"}
                   {:url "/raw/anim/senoravaca/skeleton8.png", :size 1, :type "anim-texture"}
                   {:url "/raw/anim/senoravaca/skeleton9.png", :size 1, :type "anim-texture"}
                   {:url "/raw/anim/senoravaca/skeleton10.png", :size 1, :type "anim-texture"}

                   {:url "/raw/anim/vera/skeleton.atlas", :size 1, :type "anim-text"}
                   {:url "/raw/anim/vera/skeleton.json", :size 1, :type "anim-text"}
                   {:url "/raw/anim/vera/skeleton.png", :size 1, :type "anim-texture"}
                   {:url "/raw/anim/vera/skeleton2.png", :size 1, :type "anim-texture"}
                   {:url "/raw/anim/vera/skeleton3.png", :size 1, :type "anim-texture"}
                   {:url "/raw/anim/vera/skeleton4.png", :size 1, :type "anim-texture"}
                   {:url "/raw/anim/vera/skeleton5.png", :size 1, :type "anim-texture"}

                   {:url "/raw/anim/test/skeleton.atlas", :size 1, :type "anim-text"}
                   {:url "/raw/anim/test/skeleton.json", :size 1, :type "anim-text"}
                   {:url "/raw/anim/test/skeleton.png", :size 1, :type "anim-texture"}],
   :objects
                  {:background {:type "background", :src "/raw/img/casa/background.jpg"},
                   :vera       {:type "animation" :x 1210 :y 960 :name "vera" :anim "idle" :speed 0.3
                                :width 1800 :height 2558
                                :scale {:x 0.2 :y 0.2}
                                :states {:idle {:anim "idle"}
                                         :jump-clapping {:anim "jump+clapping"}
                                         :jump {:anim "jump"}
                                         :clapping-start {:anim "clapping-start"}
                                         :clapping-finish {:anim "clapping-finish"}
                                         :clapping-1clap {:anim "clapping-1clap"}
                                         :talking {:anim "talking"}}}
                   :senora-vaca {:type "animation" :x 757 :y 960 :name "senoravaca" :anim "idle" :speed 0.3
                                 :width 715 :height 1461
                                 :scale {:x 0.55 :y 0.55}
                                 :actions {:click {:type "action", :id "intro", :on "click"}}
                                 :states {:idle {:anim "idle"}
                                          :talk {:anim "talk"}
                                          :hand {:anim "hand"}}},
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
                                         :click {:type "scene", :scene-id "map", :on "click"}}}
                   :syllable {:type "transparent" :x 350 :y 299 :width 300 :height 200
                              :states {:default {:type "transparent"}
                                       :show {:type "text" :width 300 :height 200
                                               :align "center" :vertical-align "middle"
                                               :font-family "Luckiest Guy" :font-size 80
                                               :shadow-color "#1a1a1a" :shadow-offset {:x 5 :y 5} :shadow-blur 5 :shadow-opacity 0.5
                                               :fill "white"}}}
                   :word-form {:type "transparent" :x 500 :y 210 :width 206 :height 210 :origin {:type "center-center"}
                                :states {:default {:type "transparent"}
                                         :show {:type "image" :src "/raw/img/ferris-wheel/words/form_green.png"}}}
                   :word-image {:type "transparent" :x 500 :y 210 :origin {:type "center-center"}
                                :states {:default {:type "transparent" :src nil :width 100 :height 100}
                                         :flower {:type "image" :width 73 :height 136
                                                :src "/raw/img/ferris-wheel/words/flower.png"}
                                         :violin {:type "image" :width 52 :height 140
                                                   :src "/raw/img/ferris-wheel/words/violin.png"}
                                         :strawberry {:type "image" :width 84 :height 132
                                                   :src "/raw/img/ferris-wheel/words/strawberry.png"}}}},
   :scene-objects [["background" "door"] ["vera" "senora-vaca"] ["word-form" "word-image" "syllable"]],
   :actions
                  {:audio-welcome {:type "audio", :id "casa-welcome", :start 0, :duration 7.622 :offset 0.7}
                   :audio-finish {:type "audio", :id "casa-finish", :start 0, :duration 2.533 :offset 0.7}
                   :show-word-strawberry {:type "parallel"
                                       :data [{:type "state", :target "word-image", :id "strawberry"}
                                              {:type "state", :target "word-form", :id "show"}]}
                   :show-word-flower {:type "parallel"
                                    :data [{:type "state", :target "word-image", :id "flower"}
                                           {:type "state", :target "word-form", :id "show"}]}
                   :show-word-violin {:type "parallel"
                                       :data [{:type "state", :target "word-image", :id "violin"}
                                              {:type "state", :target "word-form", :id "show"}]}

                   :senora-vaca-audio-1 {:type "parallel"
                                         :data [{:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                                                {:type "animation" :target "senoravaca" :id "talk"}
                                                {:type "animation" :target "vera" :id "idle"}]}
                   :senora-vaca-audio-2 {:type "parallel"
                                         :data [{:type "audio", :id "teacher", :start 4.72, :duration 5.84}
                                                {:type "animation" :target "senoravaca" :id "talk"}
                                                {:type "animation" :target "vera" :id "idle"}]}
                   :senora-vaca-audio-3 {:type "parallel"
                                         :data [{:type "audio", :id "teacher", :start 11.9, :duration 1.3}
                                                {:type "animation" :target "senoravaca" :id "hand"}
                                                {:type "animation" :target "vera" :id "idle"}]}
                   :senora-vaca-audio-4 {:type "parallel"
                                         :data [{:type "audio", :id "teacher", :start 13.31, :duration 3.22}
                                                {:type "animation" :target "senoravaca" :id "talk"}
                                                {:type "animation" :target "vera" :id "idle"}]}
                   :senora-vaca-audio-5 {:type "parallel"
                                         :data [{:type "audio", :id "teacher", :start 17.92, :duration 4.55}
                                                {:type "animation" :target "senoravaca" :id "talk"}
                                                {:type "animation" :target "vera" :id "idle"}]}
                   :senora-vaca-audio-6 {:type "parallel"
                                         :data [{:type "audio", :id "teacher", :start 23.05, :duration 3.14}
                                                {:type "animation" :target "senoravaca" :id "talk"}
                                                {:type "animation" :target "vera" :id "idle"}]}
                   :senora-vaca-audio-7 {:type "parallel"
                                         :data [{:type "audio", :id "teacher", :start 29.75, :duration 2.2}
                                                {:type "animation" :target "senoravaca" :id "talk"}
                                                {:type "animation" :target "vera" :id "idle"}]}
                   :senora-vaca-audio-8 {:type "parallel"
                                         :data [{:type "audio", :id "teacher", :start 38.102, :duration 4.739}
                                                {:type "animation" :target "senoravaca" :id "talk"}
                                                {:type "animation" :target "vera" :id "idle"}]}
                   :senora-vaca-audio-9 {:type "parallel"
                                         :data [{:type "audio", :id "teacher", :start 40.66, :duration 1.65}
                                                {:type "animation" :target "senoravaca" :id "talk"}
                                                {:type "animation" :target "vera" :id "idle"}]}
                   :senora-vaca-audio-10 {:type "parallel"
                                          :data [{:type "audio", :id "teacher", :start 47.47, :duration 5.78}
                                                 {:type "animation" :target "senoravaca" :id "talk"}
                                                 {:type "animation" :target "vera" :id "idle"}]}


                   :vera-audio-1 {:type "parallel"
                                  :data [{:type "audio", :id "vera", :start 1.4, :duration 3.38}
                                         {:type "animation", :target "senoravaca" :id "idle"}
                                         {:type "animation" :target "vera" :id "talking"}]}
                   :vera-audio-2 {:type "parallel"
                                  :data [{:type "audio", :id "vera", :start 5.74, :duration 1.5}
                                         {:type "animation", :target "senoravaca" :id "idle"}
                                         {:type "animation" :target "vera" :id "jump+clapping"}]}
                   :vera-audio-3 {:type "parallel"
                                  :data [{:type "audio", :id "vera", :start 7.32, :duration 3.02}
                                         {:type "animation", :target "senoravaca" :id "idle"}
                                         {:type "animation" :target "vera" :id "talking"}]}
                   :vera-audio-4 {:type "parallel"
                                  :data [{:type "audio", :id "vera", :start 11.85, :duration 1.37}
                                         {:type "animation", :target "senoravaca" :id "idle"}
                                         {:type "animation" :target "vera" :id "talking"}]}
                   :vera-audio-5 {:type "parallel"
                                  :data [{:type "audio", :id "vera", :start 14.45, :duration 0.93}
                                         {:type "animation", :target "senoravaca" :id "idle"}
                                         {:type "animation" :target "vera" :id "talking"}]}
                   :vera-audio-6 {:type "parallel"
                                  :data [{:type "audio", :id "vera", :start 16.66, :duration 3.58}
                                         {:type "animation", :target "senoravaca" :id "idle"}
                                         {:type "animation" :target "vera" :id "talking"}]}

                   :empty-1        {:type "empty", :duration 600}

                   :senora-vaca-anim-idle {:type "animation" :target "senoravaca" :id "idle"}
                   :senora-vaca-anim-clapping-start {:type "animation" :target "senoravaca" :id "clapping_start" :loop false}
                   :senora-vaca-anim-clapping-finish {:type "animation" :target "senoravaca" :id "clapping_finish" :loop false}
                   :vera-anim-idle {:type "animation" :target "vera" :id "idle"}
                   :vera-anim-clapping-start {:type "animation" :target "vera" :id "clapping_start" :loop false}
                   :vera-anim-clapping-finish {:type "animation" :target "vera" :id "clapping_finish" :loop false}

                   :syllable-ve {:type "parallel",
                                 :data [{:type "audio" :id "teacher" :start 27.192 :duration 0.475 :offset 0.1}
                                        {:type "animation" :target "senoravaca" :id "clapping_1clap" :loop false}]}

                   :syllable-ra {:type "parallel",
                                 :data [{:type "audio" :id "teacher" :start 27.649 :duration 0.432 :offset 0.1}
                                        {:type "animation" :target "senoravaca" :id "clapping_1clap" :loop false}]}

                   :syllable-flow {:type "parallel",
                                 :data [{:type "audio" :id "syllables" :start 5.416 :duration 0.537 :offset 0.1}
                                        {:type "animation" :target "senoravaca" :id "clapping_1clap" :loop false}
                                        {:type "state" :target "syllable" :id "show" :params {:text "flow"}}]}

                   :syllable-er {:type "parallel",
                                   :data [{:type "audio" :id "syllables" :start 5.947 :duration 0.625 :offset 0.1}
                                          {:type "animation" :target "senoravaca" :id "clapping_1clap" :loop false}
                                          {:type "state" :target "syllable" :id "show" :params {:text "er"}}]}

                   :syllable-va {:type "parallel",
                                 :data [{:type "audio" :id "syllables" :start 10.075 :duration 0.608 :offset 0.1}
                                        {:type "animation" :target "senoravaca" :id "clapping_1clap" :loop false}
                                        {:type "state" :target "syllable" :id "show" :params {:text "va"}}]}

                   :syllable-o {:type "parallel",
                                 :data [{:type "audio" :id "syllables" :start 10.641 :duration 0.52 :offset 0.1}
                                        {:type "animation" :target "senoravaca" :id "clapping_1clap" :loop false}
                                        {:type "state" :target "syllable" :id "show" :params {:text "o"}}]}

                   :syllable-lin {:type "parallel",
                                 :data [{:type "audio" :id "syllables" :start 11.155 :duration 0.757 :offset 0.1}
                                        {:type "animation" :target "senoravaca" :id "clapping_1clap" :loop false}
                                        {:type "state" :target "syllable" :id "show" :params {:text "lin"}}]}

                   :syllable-straw {:type "parallel",
                                 :data [{:type "audio" :id "syllables" :start 18.728 :duration 0.788 :offset 0.1}
                                        {:type "animation" :target "senoravaca" :id "clapping_1clap" :loop false}
                                        {:type "animation" :target "vera" :id "clapping_1clap" :loop false}
                                        {:type "state" :target "syllable" :id "show" :params {:text "straw"}}]}

                   :syllable-ber {:type "parallel",
                                 :data [{:type "audio" :id "syllables" :start 19.427 :duration 0.679 :offset 0.1}
                                        {:type "animation" :target "senoravaca" :id "clapping_1clap" :loop false}
                                        {:type "animation" :target "vera" :id "clapping_1clap" :loop false}
                                        {:type "state" :target "syllable" :id "show" :params {:text "ber"}}]}

                   :syllable-ry {:type "parallel",
                                 :data [{:type "audio" :id "syllables" :start 20.043 :duration 0.855 :offset 0.1}
                                        {:type "animation" :target "senoravaca" :id "clapping_1clap" :loop false}
                                        {:type "animation" :target "vera" :id "clapping_1clap" :loop false}
                                        {:type "state" :target "syllable" :id "show" :params {:text "ry"}}]}

                   :group-vera {:type "sequence",
                                :name "vera syllables",
                                :data ["senora-vaca-anim-clapping-start" "empty-1"
                                       "syllable-ve" "syllable-ra" "empty-1"
                                       "syllable-ve" "syllable-ra" "senora-vaca-anim-clapping-finish" "empty-1"
                                       "senora-vaca-anim-idle"]}

                   :group-flower {:type "sequence" :name "u-vas syllables",
                                  :data ["senora-vaca-anim-clapping-start" "empty-1" "show-word-flower"
                                         "syllable-flow" "syllable-er" "empty-1"
                                         "syllable-flow" "syllable-er" "empty-1"
                                         "syllable-flow" "syllable-er" "senora-vaca-anim-clapping-finish" "empty-1"
                                         "senora-vaca-anim-idle" "hide-word" "hide-syllable"]}

                   :group-violin {:type "sequence", :name "cu-cha-ra syllables",
                                  :data ["show-word-violin" "senora-vaca-anim-clapping-start" "empty-1"
                                         "syllable-va" "syllable-o" "syllable-lin" "empty-1"
                                         "syllable-va" "syllable-o" "syllable-lin" "empty-1"
                                         "syllable-va" "syllable-o" "syllable-lin" "senora-vaca-anim-clapping-finish" "empty-1"
                                         "senora-vaca-anim-idle" "hide-word" "hide-syllable"]}

                   :group-strawberry {:type "sequence", :name "u-vas syllables",
                                      :data ["show-word-strawberry" "senora-vaca-anim-clapping-start" "vera-anim-clapping-start" "empty-1"
                                             "syllable-straw" "syllable-ber" "syllable-ry" "empty-1"
                                             "syllable-straw" "syllable-ber" "syllable-ry" "empty-1"
                                             "syllable-straw" "syllable-ber" "syllable-ry"
                                             "senora-vaca-anim-clapping-finish" "vera-anim-clapping-finish" "empty-1"
                                             "vera-anim-idle" "senora-vaca-anim-idle" "hide-word" "hide-syllable"]}

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
                                           "vera-anim-idle"

                                           "group-flower"

                                           "group-violin"
                                           "vera-audio-6"
                                           "vera-anim-idle"
                                           "senora-vaca-audio-9"
                                           "group-strawberry"
                                           "senora-vaca-audio-10"
                                           "senora-vaca-anim-idle"
                                           "vera-anim-idle"
                                           "audio-finish"]},

                   :hide-word      {:type "parallel"
                                    :data [{:type "state", :target "word-image", :id "default"}
                                           {:type "state", :target "word-form", :id "default"}]}
                   :hide-syllable  {:type "state", :target "syllable", :id "default"},

                   :start-background-music {:type "audio" :id "background" :loop true}}
   :audio
                  {:casa-welcome "/raw/audio/demo/welcome.mp3"
                   :teacher   "/raw/audio/demo/teacher.mp3",
                   :vera      "/raw/audio/demo/vera.mp3",
                   :syllables "/raw/audio/demo/intro-teacher-syllables.mp3"
                   :casa-finish "/raw/audio/demo/intro-finish.mp3"
                   :background "/raw/audio/background/POL-daily-special-short.mp3"},
   :triggers      {:music {:on "start" :action "start-background-music"}
                   :welcome {:on "start" :action "audio-welcome"}}
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
                                                    "open-feria"]}
                                :start-background-music {:type "audio" :id "background" :loop true}},
                :audio {:background "/raw/audio/background/POL-daily-special-short.mp3"}
                :triggers      {:music {:on "start" :action "start-background-music"}}
                :metadata      {:autostart true}})

(def feria-scene {:assets
                                 [{:url "/raw/audio/background/POL-daily-special-short.mp3", :size 10, :type "audio"}
                                  {:url "/raw/audio/effects/NFF-fruit-collected.mp3", :size 1, :type "audio"}
                                  {:url "/raw/audio/effects/NFF-glitter.mp3", :size 1, :type "audio"}
                                  {:url "/raw/audio/effects/NFF-robo-elastic.mp3", :size 1, :type "audio"}
                                  {:url "/raw/audio/effects/NFF-rusted-thing.mp3", :size 1, :type "audio"}
                                  {:url "/raw/audio/effects/NFF-zing.mp3", :size 1, :type "audio"}
                                  {:url "/raw/img/feria/background.jpg", :size 10, :type "image"}
                                  {:url "/raw/img/feria/back.png", :size 1, :type "image"}
                                  {:url "/raw/img/feria/back_active.png", :size 1, :type "image"}
                                  {:url "/raw/img/feria/ferris_wheel_default.png", :size 2, :type "image"}
                                  {:url "/raw/img/feria/ferris_wheel_highlight.png", :size 2, :type "image"}
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
                                  {:type "background", :src "/raw/img/feria/background.jpg"},
                                  :wheel
                                  {:type "image",
                                   :src "/raw/img/feria/ferris_wheel_default.png"
                                   :x      467,
                                   :y      105,
                                   :width  708,
                                   :height 778
                                   :states
                                           {:default {:src "/raw/img/feria/ferris_wheel_default.png"},
                                            :hover   {:src "/raw/img/feria/ferris_wheel_highlight.png"}},
                                   :actions
                                           {:mouseover {:type "state", :target "wheel", :id "hover", :on "mouseover"},
                                            :mouseout  {:type "state", :target "wheel", :id "default", :on "mouseout"}
                                            :click     {:type "scene", :scene-id "ferris-wheel", :on "click"}}},
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
                                   :scale {:x 0.55, :y 0.55}}}
                  :actions { :start-background-music {:type "audio" :id "background" :loop true}},
                  :audio {:background "/raw/audio/background/POL-daily-special-short.mp3"}
                  :triggers      {:music {:on "start" :action "start-background-music"}}
                  :scene-objects [["background" "wheel" "exit"] ["vera"]],
                  :metadata      {:autostart true}})

(def ferris-wheel-scene
  {:assets
                  [{:url "/raw/audio/demo/ferris-wheel-instructions.mp3", :size 10, :type "audio"}
                   {:url "/raw/audio/demo/ferris-wheel-syllables.mp3", :size 10, :type "audio"}
                   {:url "/raw/audio/demo/fw-thats-correct.mp3", :size 2, :type "audio"}
                   {:url "/raw/audio/demo/fw-try-again.mp3", :size 2, :type "audio"}

                   {:url "/raw/img/ferris-wheel/background.jpg", :size 10, :type "image"},
                   {:url "/raw/img/ferris-wheel/cloud_01.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/cloud_02.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/cloud_03.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/cloud_04.png", :size 1, :type "image"},

                   {:url "/raw/img/ferris-wheel/ferris_wheel_01.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/ferris_wheel_02.png", :size 1, :type "image"},
                   {:url "/raw/img/ferris-wheel/ferris_wheel_03.png", :size 1, :type "image"},

                   {:url "/raw/img/ferris-wheel/words/ladybug.png", :size 1, :type "image"}
                   {:url "/raw/img/ferris-wheel/words/broccoli.png", :size 1, :type "image"}
                   {:url "/raw/img/ferris-wheel/words/crocodile.png", :size 1, :type "image"}
                   {:url "/raw/img/ferris-wheel/words/dino.png", :size 1, :type "image"}
                   {:url "/raw/img/ferris-wheel/words/orange.png", :size 1, :type "image"}
                   {:url "/raw/img/ferris-wheel/words/pumpkin.png", :size 1, :type "image"}

                   {:url "/raw/img/ferris-wheel/words/form_green.png", :size 1, :type "image"}
                   {:url "/raw/img/ferris-wheel/words/form_red.png", :size 1, :type "image"}
                   {:url "/raw/img/ferris-wheel/words/form_yellow.png", :size 1, :type "image"}

                   {:url "/raw/img/butterfly.png", :size 2, :type "image"}
                   ],
   :objects
                  {:background {:type "background", :src "/raw/img/ferris-wheel/background.jpg"}
                   :butterfly {:type "image" :scene-name "butterfly"
                               :src "/raw/img/butterfly.png" :x 1347 :y 520 :width 697 :height 768
                               :scale-y 0.45 :scale-x 0.45}
                   :wheel      {:type "group" :x 806 :y 457 :children ["wheel-1", "wheel-2", "wheel-3", "items"]}
                   :wheel-1    {:type "image" :width 772 :height 772 :transition "wheel-1"
                                :src  "/raw/img/ferris-wheel/ferris_wheel_01.png" :origin {:type "center-center"}},
                   :wheel-2    {:type "image" :width 359 :height 527
                                :src  "/raw/img/ferris-wheel/ferris_wheel_02.png" :origin {:type "center-top"}},
                   :wheel-3    {:type "image" :width 261 :height 262 :transition "wheel-3"
                                :src  "/raw/img/ferris-wheel/ferris_wheel_03.png" :origin {:type "center-center"}},
                   :items      {:type     "group" :width 772 :height 772 :transition "items"
                                :children ["item-1f" "item-2f" "item-3f" "item-4f" "item-5f" "item-6f"
                                           "item-1" "item-2" "item-3" "item-4" "item-5" "item-6"]
                                :origin   {:type "center-center"}}

                   :item-1f {:type "image" :width 206 :height 210 :x 599 :y 263 :transition "item-1f" :rotation 360
                             :src "/raw/img/ferris-wheel/words/form_yellow.png" :origin {:type "center-center"}
                             :states {:yellow {:src "/raw/img/ferris-wheel/words/form_yellow.png"}
                                      :green {:src "/raw/img/ferris-wheel/words/form_green.png"}
                                      :red {:src "/raw/img/ferris-wheel/words/form_red.png"}}
                             :states-aliases {:default "yellow"}}
                   :item-2f {:type "image" :width 206 :height 210 :x 386 :y 140 :transition "item-2f" :rotation 360
                             :src "/raw/img/ferris-wheel/words/form_yellow.png" :origin {:type "center-center"}
                             :states {:yellow {:src "/raw/img/ferris-wheel/words/form_yellow.png"}
                                      :green {:src "/raw/img/ferris-wheel/words/form_green.png"}
                                      :red {:src "/raw/img/ferris-wheel/words/form_red.png"}}
                             :states-aliases {:default "yellow"}}
                   :item-3f {:type "image" :width 206 :height 210 :x 173 :y 263 :transition "item-3f" :rotation 360
                             :src "/raw/img/ferris-wheel/words/form_yellow.png" :origin {:type "center-center"}
                             :states {:yellow {:src "/raw/img/ferris-wheel/words/form_yellow.png"}
                                      :green {:src "/raw/img/ferris-wheel/words/form_green.png"}
                                      :red {:src "/raw/img/ferris-wheel/words/form_red.png"}}
                             :states-aliases {:default "yellow"}}
                   :item-4f {:type "image" :width 206 :height 210 :x 173 :y 509 :transition "item-4f" :rotation 360
                             :src "/raw/img/ferris-wheel/words/form_yellow.png" :origin {:type "center-center"}
                             :states {:yellow {:src "/raw/img/ferris-wheel/words/form_yellow.png"}
                                      :green {:src "/raw/img/ferris-wheel/words/form_green.png"}
                                      :red {:src "/raw/img/ferris-wheel/words/form_red.png"}}
                             :states-aliases {:default "yellow"}}
                   :item-5f {:type "image" :width 206 :height 210 :x 386 :y 632 :transition "item-5f" :rotation 360
                             :src "/raw/img/ferris-wheel/words/form_yellow.png" :origin {:type "center-center"}
                             :states {:yellow {:src "/raw/img/ferris-wheel/words/form_yellow.png"}
                                      :green {:src "/raw/img/ferris-wheel/words/form_green.png"}
                                      :red {:src "/raw/img/ferris-wheel/words/form_red.png"}}
                             :states-aliases {:default "yellow"}}
                   :item-6f {:type "image" :width 206 :height 210 :x 599 :y 509 :transition "item-6f" :rotation 360
                             :src "/raw/img/ferris-wheel/words/form_yellow.png" :origin {:type "center-center"}
                             :states {:yellow {:src "/raw/img/ferris-wheel/words/form_yellow.png"}
                                      :green {:src "/raw/img/ferris-wheel/words/form_green.png"}
                                      :red {:src "/raw/img/ferris-wheel/words/form_red.png"}}
                             :states-aliases {:default "yellow"}}

                   :item-1     {:type "placeholder" :x 599 :y 263 :transition "item-1" :rotation 360
                                :var-name "item-1" :image-src "src" :image-width "width" :image-height "height"
                                :origin {:type "center-center"}
                                :actions {:click {:type "action" :id "check-current-word" :on "click" :params {:form "item-1f"}}}}
                   :item-2     {:type     "placeholder" :x 386 :y 140 :transition "item-2" :rotation 360
                                :var-name "item-2" :image-src "src" :image-width "width" :image-height "height"
                                :origin {:type "center-center"}
                                :actions {:click {:type "action", :id "check-current-word", :on "click" :params {:form "item-2f"}}}}
                   :item-3     {:type     "placeholder" :x 173 :y 263 :transition "item-3" :rotation 360
                                :var-name "item-3" :image-src "src" :image-width "width" :image-height "height"
                                :origin {:type "center-center"}
                                :actions {:click {:type "action", :id "check-current-word", :on "click" :params {:form "item-3f"}}}}
                   :item-4     {:type     "placeholder" :x 173 :y 509 :transition "item-4" :rotation 360
                                :var-name "item-4" :image-src "src" :image-width "width" :image-height "height"
                                :origin {:type "center-center"}
                                :actions {:click {:type "action", :id "check-current-word", :on "click" :params {:form "item-4f"}}}}
                   :item-5     {:type     "placeholder" :x 386 :y 632 :transition "item-5" :rotation 360
                                :var-name "item-5" :image-src "src" :image-width "width" :image-height "height"
                                :origin {:type "center-center"}
                                :actions {:click {:type "action", :id "check-current-word", :on "click" :params {:form "item-5f"}}}}
                   :item-6     {:type     "placeholder" :x 599 :y 509 :transition "item-6" :rotation 360
                                :var-name "item-6" :image-src "src" :image-width "width" :image-height "height"
                                :origin {:type "center-center"}
                                :actions {:click {:type "action", :id "check-current-word", :on "click" :params {:form "item-6f"}}}}
                   },
   :actions       {:rotate-wheel {:type "parallel",
                                  :data [{:type "transition" :transition-id "wheel-1" :to {:rotation 360 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "wheel-3" :to {:rotation 360 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "items" :to {:rotation 360 :duration 30 :loop true}}

                                         {:type "transition" :transition-id "item-1f" :to {:rotation 0 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "item-2f" :to {:rotation 0 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "item-3f" :to {:rotation 0 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "item-4f" :to {:rotation 0 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "item-5f" :to {:rotation 0 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "item-6f" :to {:rotation 0 :duration 30 :loop true}}

                                         {:type "transition" :transition-id "item-1" :to {:rotation 0 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "item-2" :to {:rotation 0 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "item-3" :to {:rotation 0 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "item-4" :to {:rotation 0 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "item-5" :to {:rotation 0 :duration 30 :loop true}}
                                         {:type "transition" :transition-id "item-6" :to {:rotation 0 :duration 30 :loop true}}]}
                   :start-game {:type "sequence"
                                :data ["renew-words" "renew-current-word" "audio-instructions" "repeat-current-word"]}
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
                                 :tags ["repeat-word"]
                                 :data ["play-word", "empty-5", "repeat-current-word"]}
                   :clear-repeat-word {:type "remove-flows"
                                       :flow-tag "repeat-word"}
                   :pick-correct {:type "sequence"
                                       :data ["clear-repeat-word" "increase-success" "reset-forms" "set-green" "audio-correct" "renew-current-word" "repeat-current-word"]}
                   :increase-fail     {:type "counter"
                                       :counter-action "increase"
                                       :counter-id "fails"}
                   :increase-success  {:type "counter"
                                       :counter-action "increase"
                                       :counter-id "successes"}
                   :check-current-word {:type "test-var"
                                        :var-name "current-word"
                                        :property "id"
                                        :success "pick-correct"
                                        :fail "pick-wrong"}
                   :pick-wrong {:type "sequence" :data ["increase-fail" "set-red" "audio-wrong"]}
                   :set-red {:type "state" :from-params {:target "form"} :id "red"}
                   :set-green {:type "parallel" :data [{:type "add-alias" :from-params {:target "form"} :alias "default" :state "green"}
                                                       {:type "state" :from-params {:target "form"} :id "green"}]}

                   :reset-forms {:type "parallel" :data [{:type "state" :target "item-1f" :id "default"}
                                                         {:type "state" :target "item-2f" :id "default"}
                                                         {:type "state" :target "item-3f" :id "default"}
                                                         {:type "state" :target "item-4f" :id "default"}
                                                         {:type "state" :target "item-5f" :id "default"}
                                                         {:type "state" :target "item-6f" :id "default"}]}
                   :finish-game {:type "set-variable" :var-name "score" :var-value {:visible true}}
                   :audio-instructions {:type "audio" :id "instructions" :start 0.3 :duration 6.2 :offset 0}
                   :audio-correct {:type "audio" :id "fw-correct" :start 0 :duration 1.225 :offset 0.2}
                   :audio-wrong {:type "audio" :id "fw-try-again" :start 0 :duration 1.755 :offset 0.2}
                   }

   :triggers      {:rotation {:on "start" :action "rotate-wheel"}
                   :start    {:on "start" :action "start-game"}}

   :datasets      {:items {:ladybug   {:id "ladybug" :src "/raw/img/ferris-wheel/words/ladybug.png" :width 98 :height 88
                                       :audio-id "fw-syllables" :start 15.896 :duration 2.233 :offset 1}
                           :broccoli  {:id "broccoli" :src "/raw/img/ferris-wheel/words/broccoli.png" :width 100 :height 100
                                       :audio-id "fw-syllables" :start 5.191, :duration 2.171 :offset 1}
                           :dinosaur  {:id "dinosaur" :src "/raw/img/ferris-wheel/words/dino.png" :width 102 :height 118
                                       :audio-id "fw-syllables" :start 8.959, :duration 2.508 :offset 1}
                           :orange    {:id "orange" :src "/raw/img/ferris-wheel/words/orange.png" :width 108 :height 102
                                       :audio-id "fw-syllables" :start 19.34, :duration 2.283 :offset 1}
                           :crocodile {:id "crocodile" :src "/raw/img/ferris-wheel/words/crocodile.png" :width 112 :height 121
                                       :audio-id "fw-syllables" :start 1.335, :duration 2.308 :offset 1}
                           :pumpkin   {:id "pumpkin" :src "/raw/img/ferris-wheel/words/pumpkin.png" :width 112 :height 114
                                       :audio-id "fw-syllables" :start 12.939, :duration 1.585 :offset 1}}}
   :scene-objects [["background" "wheel"] ["butterfly"]]
   :audio {:instructions "/raw/audio/demo/ferris-wheel-instructions.mp3"
           :fw-syllables "/raw/audio/demo/ferris-wheel-syllables.mp3"
           :fw-correct "/raw/audio/demo/fw-thats-correct.mp3"
           :fw-try-again "/raw/audio/demo/fw-try-again.mp3"}
   :metadata      {:autostart false
                   :next "feria"}})
