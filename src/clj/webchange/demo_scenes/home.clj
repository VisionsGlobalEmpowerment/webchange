(ns webchange.demo-scenes.home)

(def home-scene
  {:assets
                  [{:url "/raw/audio/background/POL-daily-special-short.mp3", :size 10, :type "audio"}
                   {:url "/raw/audio/effects/NFF-fruit-collected.mp3", :size 1, :type "audio"}
                   {:url "/raw/audio/effects/NFF-glitter.mp3", :size 1, :type "audio"}
                   {:url "/raw/audio/effects/NFF-robo-elastic.mp3", :size 1, :type "audio"}
                   {:url "/raw/audio/effects/NFF-rusted-thing.mp3", :size 1, :type "audio"}
                   {:url "/raw/audio/effects/NFF-zing.mp3", :size 1, :type "audio"}

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
                   {:url "/raw/img/ferris-wheel/words/Grapes.png", :size 1, :type "image"}
                   {:url "/raw/img/ferris-wheel/words/Spoon.png", :size 1, :type "image"}
                   {:url "/raw/img/ferris-wheel/words/Fork.png", :size 1, :type "image"}

                   {:url "/raw/audio/scripts/intro/teacher.mp3", :size 5, :type "audio"}
                   {:url "/raw/audio/scripts/intro/vera.mp3", :size 5, :type "audio"}
                   {:url "/raw/audio/scripts/intro/syllables.mp3", :size 2, :type "audio"}
                   {:url "/raw/audio/scripts/intro/intro-welcome.mp3", :size 2, :type "audio"}
                   {:url "/raw/audio/scripts/intro/intro-finish.mp3", :size 2, :type "audio"}
                   {:url "/raw/audio/ferris-wheel/fw-try-again.mp3", :size 2, :type "audio"}

                   {:url "/raw/img/casa/background.jpg", :size 10 :type "image"}
                   {:url "/raw/img/casa_door.png", :size 1, :type "image"}

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

                   {:url "/raw/anim/mari/skeleton.atlas", :size 1, :type "anim-text"}
                   {:url "/raw/anim/mari/skeleton.json", :size 1, :type "anim-text"}
                   {:url "/raw/anim/mari/skeleton.png", :size 1, :type "anim-texture"}

                   {:url "/raw/anim/boxes/skeleton.atlas", :size 1, :type "anim-text"}
                   {:url "/raw/anim/boxes/skeleton.json", :size 1, :type "anim-text"}
                   {:url "/raw/anim/boxes/skeleton.png", :size 1, :type "anim-texture"}
                   ],
   :objects
                  {:background {:type "background", :src "/raw/img/casa/background.jpg"},
                   :vera       {:type "animation" :x 1128 :y 960 :name "vera" :anim "idle" :speed 0.3
                                :width 1800 :height 2558 :scale {:x 0.2 :y 0.2} :start true}
                   :senora-vaca {:type "animation" :x 655 :y 960 :name "senoravaca" :anim "idle" :speed 0.3
                                 :width 715 :height 1461 :scale {:x 0.55 :y 0.55} :start true
                                 :actions {:click {:type "action" :id "intro" :on "click" :options {:unique-tag "intro"}}}}

                   :door-trigger {:type "transparent" :x 1146 :y 42 :width 732 :height 810
                                  :actions {:click {:type "scene", :scene-id "map", :on "click"}}}

                   :box1 {:type "transparent" :x 600 :y 400 :width 206 :height 210 :origin {:type "center-center"}
                          :scene-name "box1"
                          :states {:default {:type "transparent"}
                                   :visible {:type "animation" :name "boxes" :anim "come" :skin "qwestion"
                                             :scale {:x 0.2 :y 0.2} :speed 0.3 :loop false :start true}}
                          :actions {:click {:type "action" :id "click-on-box1" :on "click"}}}

                   :box2 {:type "transparent" :x 1000 :y 400 :width 206 :height 210 :origin {:type "center-center"}
                          :scene-name "box2"
                          :states {:default {:type "transparent"}
                                   :visible {:type "animation" :name "boxes" :anim "come" :skin "qwestion"
                                             :scale {:x 0.2 :y 0.2} :speed 0.3 :loop false :start true}}
                          :actions {:click {:type "action" :id "click-on-box2" :on "click"}}}

                   :box3 {:type "transparent" :x 1400 :y 400 :width 206 :height 210 :origin {:type "center-center"}
                          :scene-name "box3"
                          :states {:default {:type "transparent"}
                                   :visible {:type "animation" :name "boxes" :anim "come" :skin "qwestion"
                                             :scale {:x 0.2 :y 0.2} :speed 0.3 :loop false :start true}}
                          :actions {:click {:type "action" :id "click-on-box3" :on "click"}}}

                   },
   :scene-objects [["background"] ["vera" "senora-vaca"] ["box1" "box2" "box3"] ["door-trigger"]],
   :actions
                  {:senora-vaca-audio-1
                   {:type "parallel"
                    :data [{:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                           {:type "sequence-data"
                            :data [{:type "empty" :duration 225}
                                   {:type "animation" :target "senoravaca" :id "talk"}
                                   {:type "empty" :duration 1307}
                                   {:type "animation" :target "senoravaca" :id "idle"}
                                   {:type "empty" :duration 550}
                                   {:type "animation" :target "senoravaca" :id "talk"}
                                   {:type "empty" :duration 1069}
                                   {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :senora-vaca-audio-2
                   {:type "parallel"
                    :data [{:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                           {:type "sequence-data"
                            :data [{:type "empty" :duration 225}
                                   {:type "animation" :target "senoravaca" :id "talk"}
                                   {:type "empty" :duration 1307}
                                   {:type "animation" :target "senoravaca" :id "idle"}
                                   {:type "empty" :duration 550}
                                   {:type "animation" :target "senoravaca" :id "talk"}
                                   {:type "empty" :duration 1069}
                                   {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :senora-vaca-audio-touch-second-box
                   {:type "parallel"
                    :data [{:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                           {:type "sequence-data"
                            :data [{:type "empty" :duration 225}
                                   {:type "animation" :target "senoravaca" :id "talk"}
                                   {:type "empty" :duration 1307}
                                   {:type "animation" :target "senoravaca" :id "idle"}
                                   {:type "empty" :duration 550}
                                   {:type "animation" :target "senoravaca" :id "talk"}
                                   {:type "empty" :duration 1069}
                                   {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :senora-vaca-audio-touch-third-box
                   {:type "parallel"
                    :data [{:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                           {:type "sequence-data"
                            :data [{:type "empty" :duration 225}
                                   {:type "animation" :target "senoravaca" :id "talk"}
                                   {:type "empty" :duration 1307}
                                   {:type "animation" :target "senoravaca" :id "idle"}
                                   {:type "empty" :duration 550}
                                   {:type "animation" :target "senoravaca" :id "talk"}
                                   {:type "empty" :duration 1069}
                                   {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :show-boxes {:type "parallel"
                                :data [{:type "state" :target "box1" :id "visible"}
                                       {:type "state" :target "box2" :id "visible"}
                                       {:type "state" :target "box3" :id "visible"}]}

                   :switch-box-animations-idle {:type "parallel"
                                                :data [{:type "add-animation" :target "box1" :id "idle" :loop true}
                                                       {:type "add-animation" :target "box2" :id "idle" :loop true}
                                                       {:type "add-animation" :target "box3" :id "idle" :loop true}]}

                   :wait-for-box-animations {:type "empty" :duration 500}

                   :intro {:type "sequence",
                               :data ["clear-instruction"
                                      "renew-words"
                                      "senora-vaca-audio-1"
                                      "set-current-box1"
                                      "show-boxes"
                                      "wait-for-box-animations"
                                      "switch-box-animations-idle"
                                      "senora-vaca-audio-2"]},

                   :set-current-box1 {:type "set-variable" :var-name "current-box" :var-value "box1"}
                   :set-current-box2 {:type "set-variable" :var-name "current-box" :var-value "box2"}
                   :set-current-box3 {:type "set-variable" :var-name "current-box" :var-value "box3"}

                   :click-on-box1 {:type "test-var-scalar"
                                   :var-name "current-box"
                                   :value "box1"
                                   :success "first-word"
                                   :fail "pick-wrong"}

                   :click-on-box2 {:type "test-var-scalar"
                                   :var-name "current-box"
                                   :value "box2"
                                   :success "second-word"
                                   :fail "pick-wrong"}

                   :click-on-box3 {:type "test-var-scalar"
                                   :var-name "current-box"
                                   :value "box3"
                                   :success "third-word"
                                   :fail "pick-wrong"}

                   :first-word {:type "sequence"
                                :data ["show-first-box-word"
                                       "introduce-word"
                                       "set-current-box2"
                                       "senora-vaca-audio-touch-second-box"]}

                   :second-word {:type "sequence"
                                 :data ["show-second-box-word"
                                        "introduce-word"
                                        "set-current-box3"
                                        "senora-vaca-audio-touch-third-box"]}

                   :third-word {:type "sequence"
                                :data ["show-third-box-word"
                                       "introduce-word"
                                       "finish-activity"
                                       "mari-finish"]}

                   :show-first-box-word {:type "parallel"
                                         :data [{:type "animation" :target "box1" :id "wood" :loop false}
                                                {:type "set-skin" :target "box1"
                                                 :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}
                                                {:type "copy-variable" :var-name "current-word" :from "item-1"}
                                                {:type "add-animation" :target "box1" :id "idle" :loop true}]}

                   :show-second-box-word {:type "parallel"
                                         :data [{:type "animation" :target "box2" :id "wood" :loop false}
                                                {:type "set-skin" :target "box2"
                                                 :from-var [{:var-name "item-2" :action-property "skin" :var-property "skin"}]}
                                                {:type "copy-variable" :var-name "current-word" :from "item-2"}
                                                {:type "add-animation" :target "box2" :id "idle" :loop true}]}

                   :show-third-box-word {:type "parallel"
                                         :data [{:type "animation" :target "box3" :id "wood" :loop false}
                                                {:type "set-skin" :target "box3"
                                                 :from-var [{:var-name "item-2" :action-property "skin" :var-property "skin"}]}
                                                {:type "copy-variable" :var-name "current-word" :from "item-3"}
                                                {:type "add-animation" :target "box3" :id "idle" :loop true}]}

                   :vaca-this-is-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "home-vaca-this-is"}]}

                   :vaca-can-you-say {:type "parallel"
                                      :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                             {:type "sequence-data"
                                              :data [{:type "empty" :duration 225}
                                                     {:type "animation" :target "senoravaca" :id "talk"}
                                                     {:type "empty" :duration 1307}
                                                     {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :vaca-question-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "home-vaca-question"}]}

                   :vaca-word-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "home-vaca-word"}]}

                   :group-word-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "home-group-word"}]}

                   :vaca-say-3-times {:type "parallel"
                                      :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                             {:type "sequence-data"
                                              :data [{:type "empty" :duration 225}
                                                     {:type "animation" :target "senoravaca" :id "talk"}
                                                     {:type "empty" :duration 1307}
                                                     {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :vaca-3-times-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "home-vaca-3-times"}]}

                   :group-3-times-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "home-group-3-times"}]}

                   :vaca-once-more {:type "parallel"
                                      :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                             {:type "sequence-data"
                                              :data [{:type "empty" :duration 225}
                                                     {:type "animation" :target "senoravaca" :id "talk"}
                                                     {:type "empty" :duration 1307}
                                                     {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :vaca-goodbye-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "home-vaca-goodbye"}]}

                   :introduce-word {:type "sequence"
                                    :data ["vaca-this-is-var"
                                           "vaca-can-you-say"
                                           "vaca-question-var"
                                           "vaca-word-var"
                                           "group-word-var"
                                           "vaca-say-3-times"
                                           "vaca-3-times-var"
                                           "group-3-times-var"
                                           "vaca-once-more"
                                           "group-3-times-var"
                                           "vaca-goodbye-var"]}

                   :this-is-ardilla {:type "parallel"
                                     :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                            {:type "sequence-data"
                                             :data [{:type "empty" :duration 225}
                                                    {:type "animation" :target "senoravaca" :id "talk"}
                                                    {:type "empty" :duration 1307}
                                                    {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :this-is-oso {:type "parallel"
                                     :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                            {:type "sequence-data"
                                             :data [{:type "empty" :duration 225}
                                                    {:type "animation" :target "senoravaca" :id "talk"}
                                                    {:type "empty" :duration 1307}
                                                    {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :this-is-incendio {:type "parallel"
                                     :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                            {:type "sequence-data"
                                             :data [{:type "empty" :duration 225}
                                                    {:type "animation" :target "senoravaca" :id "talk"}
                                                    {:type "empty" :duration 1307}
                                                    {:type "animation" :target "senoravaca" :id "idle"}]}]}


                   :question-ardilla {:type "parallel"
                                     :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                            {:type "sequence-data"
                                             :data [{:type "empty" :duration 225}
                                                    {:type "animation" :target "senoravaca" :id "talk"}
                                                    {:type "empty" :duration 1307}
                                                    {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :question-oso {:type "parallel"
                                 :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                        {:type "sequence-data"
                                         :data [{:type "empty" :duration 225}
                                                {:type "animation" :target "senoravaca" :id "talk"}
                                                {:type "empty" :duration 1307}
                                                {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :question-incendio {:type "parallel"
                                      :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                             {:type "sequence-data"
                                              :data [{:type "empty" :duration 225}
                                                     {:type "animation" :target "senoravaca" :id "talk"}
                                                     {:type "empty" :duration 1307}
                                                     {:type "animation" :target "senoravaca" :id "idle"}]}]}


                   :word-ardilla {:type "parallel"
                                      :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                             {:type "sequence-data"
                                              :data [{:type "empty" :duration 225}
                                                     {:type "animation" :target "senoravaca" :id "talk"}
                                                     {:type "empty" :duration 1307}
                                                     {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :word-oso {:type "parallel"
                                  :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                         {:type "sequence-data"
                                          :data [{:type "empty" :duration 225}
                                                 {:type "animation" :target "senoravaca" :id "talk"}
                                                 {:type "empty" :duration 1307}
                                                 {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :word-incendio {:type "parallel"
                                       :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                              {:type "sequence-data"
                                               :data [{:type "empty" :duration 225}
                                                      {:type "animation" :target "senoravaca" :id "talk"}
                                                      {:type "empty" :duration 1307}
                                                      {:type "animation" :target "senoravaca" :id "idle"}]}]}


                   :group-word-ardilla {:type "parallel"
                                  :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                         {:type "sequence-data"
                                          :data [{:type "empty" :duration 225}
                                                 {:type "animation" :target "senoravaca" :id "talk"}
                                                 {:type "empty" :duration 1307}
                                                 {:type "animation" :target "senoravaca" :id "idle"}]}
                                         {:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                         {:type "sequence-data"
                                          :data [{:type "empty" :duration 225}
                                                 {:type "animation" :target "vera" :id "talk"}
                                                 {:type "empty" :duration 1307}
                                                 {:type "animation" :target "vera" :id "idle"}]}]}

                   :group-word-oso {:type "parallel"
                              :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                     {:type "sequence-data"
                                      :data [{:type "empty" :duration 225}
                                             {:type "animation" :target "senoravaca" :id "talk"}
                                             {:type "empty" :duration 1307}
                                             {:type "animation" :target "senoravaca" :id "idle"}]}
                                     {:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                     {:type "sequence-data"
                                      :data [{:type "empty" :duration 225}
                                             {:type "animation" :target "vera" :id "talk"}
                                             {:type "empty" :duration 1307}
                                             {:type "animation" :target "vera" :id "idle"}]}]}

                   :group-word-incendio {:type "parallel"
                                   :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                          {:type "sequence-data"
                                           :data [{:type "empty" :duration 225}
                                                  {:type "animation" :target "senoravaca" :id "talk"}
                                                  {:type "empty" :duration 1307}
                                                  {:type "animation" :target "senoravaca" :id "idle"}]}
                                          {:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                          {:type "sequence-data"
                                           :data [{:type "empty" :duration 225}
                                                  {:type "animation" :target "vera" :id "talk"}
                                                  {:type "empty" :duration 1307}
                                                  {:type "animation" :target "vera" :id "idle"}]}]}

                   :vaca-3-times-ardilla {:type "parallel"
                                      :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                             {:type "sequence-data"
                                              :data [{:type "empty" :duration 225}
                                                     {:type "animation" :target "senoravaca" :id "talk"}
                                                     {:type "empty" :duration 1307}
                                                     {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :vaca-3-times-oso {:type "parallel"
                                  :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                         {:type "sequence-data"
                                          :data [{:type "empty" :duration 225}
                                                 {:type "animation" :target "senoravaca" :id "talk"}
                                                 {:type "empty" :duration 1307}
                                                 {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :vaca-3-times-incendio {:type "parallel"
                                       :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                              {:type "sequence-data"
                                               :data [{:type "empty" :duration 225}
                                                      {:type "animation" :target "senoravaca" :id "talk"}
                                                      {:type "empty" :duration 1307}
                                                      {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :group-3-times-ardilla {:type "parallel"
                                          :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                                 {:type "sequence-data"
                                                  :data [{:type "empty" :duration 225}
                                                         {:type "animation" :target "senoravaca" :id "talk"}
                                                         {:type "empty" :duration 1307}
                                                         {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :group-3-times-oso {:type "parallel"
                                      :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                             {:type "sequence-data"
                                              :data [{:type "empty" :duration 225}
                                                     {:type "animation" :target "senoravaca" :id "talk"}
                                                     {:type "empty" :duration 1307}
                                                     {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :group-3-times-incendio {:type "parallel"
                                           :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                                  {:type "sequence-data"
                                                   :data [{:type "empty" :duration 225}
                                                          {:type "animation" :target "senoravaca" :id "talk"}
                                                          {:type "empty" :duration 1307}
                                                          {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :goodbye-ardilla {:type "parallel"
                                           :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                                  {:type "sequence-data"
                                                   :data [{:type "empty" :duration 225}
                                                          {:type "animation" :target "senoravaca" :id "talk"}
                                                          {:type "empty" :duration 1307}
                                                          {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :goodbye-oso {:type "parallel"
                                       :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                              {:type "sequence-data"
                                               :data [{:type "empty" :duration 225}
                                                      {:type "animation" :target "senoravaca" :id "talk"}
                                                      {:type "empty" :duration 1307}
                                                      {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :goodbye-incendio {:type "parallel"
                                            :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                                   {:type "sequence-data"
                                                    :data [{:type "empty" :duration 225}
                                                           {:type "animation" :target "senoravaca" :id "talk"}
                                                           {:type "empty" :duration 1307}
                                                           {:type "animation" :target "senoravaca" :id "idle"}]}]}

                   :senora-vaca-anim-idle {:type "animation" :target "senoravaca" :id "idle"}
                   :senora-vaca-anim-clapping-start {:type "animation" :target "senoravaca" :id "clapping_start" :loop false}
                   :senora-vaca-anim-clapping-finish {:type "animation" :target "senoravaca" :id "clapping_finish" :loop false}
                   :vera-anim-idle {:type "animation" :target "vera" :id "idle"}
                   :vera-anim-clapping-start {:type "animation" :target "vera" :id "clapping_start" :loop false}
                   :vera-anim-clapping-finish {:type "animation" :target "vera" :id "clapping_finish" :loop false}


                   :pick-wrong {:type "sequence"
                                :data ["audio-wrong"]}

                   :audio-wrong {:type "audio" :id "fw-try-again" :start 0.892 :duration 1.869 :offset 0.2}

                   :renew-words  {:type "lesson-var-provider"
                                  :provider-id "words-set"
                                  :variables ["item-1" "item-2" "item-3"]
                                  :from      "concepts"}

                   :clear-instruction {:type "remove-flows" :flow-tag "instruction"}
                   :start-background-music {:type "audio" :id "background" :loop true}
                   :finish-activity {:type "finish-activity" :id "home-introduce"}}
   :audio
                  {:casa-welcome "/raw/audio/scripts/intro/intro-welcome.mp3"
                   :casa-finish "/raw/audio/scripts/intro/intro-finish.mp3"
                   :teacher   "/raw/audio/scripts/intro/teacher.mp3",
                   :vera      "/raw/audio/scripts/intro/vera.mp3",
                   :syllables "/raw/audio/scripts/intro/syllables.mp3"
                   :background "/raw/audio/background/POL-daily-special-short.mp3"
                   :fw-try-again "/raw/audio/ferris-wheel/fw-try-again.mp3"},
   :triggers      {:music {:on "start" :action "start-background-music"}}
   :metadata      {:autostart true
                   :prev "map"}})