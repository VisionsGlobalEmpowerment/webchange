(ns webchange.demo-scenes.home)

(def home-scene
  {:assets
                  [{:url "/raw/audio/ferris-wheel/fw-try-again.mp3", :size 2, :type "audio"}

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

                   {:url "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a", :size 2, :type "audio"}
                   {:url "/raw/audio/l1/a1/L1_A1_Vaca_Iman.m4a", :size 2, :type "audio"}
                   {:url "/raw/audio/l1/a1/L1_A1_Vaca_Oso.m4a", :size 2, :type "audio"}
                   {:url "/raw/audio/l1/a1/L1_A1_Vera_Ardilla.m4a", :size 2, :type "audio"}
                   {:url "/raw/audio/l1/a1/L1_A1_Vera_Iman.m4a", :size 2, :type "audio"}
                   {:url "/raw/audio/l1/a1/L1_A1_Vera_Oso.m4a", :size 2, :type "audio"}
                   ],
   :objects
                  {:background {:type "background", :src "/raw/img/casa/background.jpg"},
                   :vera       {:type "animation" :x 1128 :y 960 :name "vera" :anim "idle" :speed 0.3
                                :width 1800 :height 2558 :scale {:x 0.2 :y 0.2} :start true}
                   :senora-vaca {:type "animation" :x 655 :y 960 :name "senoravaca" :anim "idle" :speed 0.3
                                 :width 351 :height 717 :scale {:x 1 :y 1} :start true
                                 :actions {:click {:type "action" :id "intro" :on "click" :options {:unique-tag "intro"}}}}

                   :door-trigger {:type "transparent" :x 1146 :y 42 :width 732 :height 810
                                  :actions {:click {:type "scene", :scene-id "map", :on "click"}}}

                   :box1 {:type "transparent" :x 400 :y 300 :width 206 :height 210 :origin {:type "center-center"}
                          :scene-name "box1"
                          :states {:default {:type "transparent"}
                                   :visible {:type "animation" :name "boxes" :anim "come" :skin "qwestion"
                                             :scale {:x 0.25 :y 0.25} :speed 0.3 :loop false :start true}}
                          :actions {:click {:type "action" :id "click-on-box1" :on "click"}}}

                   :box2 {:type "transparent" :x 850 :y 300 :width 206 :height 210 :origin {:type "center-center"}
                          :scene-name "box2"
                          :states {:default {:type "transparent"}
                                   :visible {:type "animation" :name "boxes" :anim "come" :skin "qwestion"
                                             :scale {:x 0.25 :y 0.25} :speed 0.3 :loop false :start true}}
                          :actions {:click {:type "action" :id "click-on-box2" :on "click"}}}

                   :box3 {:type "transparent" :x 1300 :y 300 :width 206 :height 210 :origin {:type "center-center"}
                          :scene-name "box3"
                          :states {:default {:type "transparent"}
                                   :visible {:type "animation" :name "boxes" :anim "come" :skin "qwestion"
                                             :scale {:x 0.25 :y 0.25} :speed 0.3 :loop false :start true}}
                          :actions {:click {:type "action" :id "click-on-box3" :on "click"}}}

                   },
   :scene-objects [["background"] ["vera" "senora-vaca"] ["door-trigger"] ["box1" "box2" "box3"]],
   :actions
                  {:senora-vaca-audio-1
                   {:type "parallel"
                    :data [{:type "audio", :id "vaca-1", :start 1.089, :duration 2.749}
                           {:type "animation-sequence" :target "senoravaca" :track 1 :offset 1.089
                            :data [{:start 1.089 :end 3.596 :anim "talk"}]}]}

                   :senora-vaca-audio-2
                   {:type "parallel"
                    :data [{:type "audio", :id "vaca-1", :start 4.395, :duration 10.042}
                           {:type "animation-sequence" :target "senoravaca" :track 1 :offset 4.395
                            :data [{:start 4.395 :end 5.484 :anim "talk"}
                                   {:start 6.423 :end 8.745 :anim "talk"}
                                   {:start 9.607 :end 10.342 :anim "talk"}
                                   {:start 11.226 :end 14.302 :anim "talk"}]}]}

                   :senora-vaca-audio-touch-second-box
                   {:type "parallel"
                    :data [{:type "audio", :id "vaca-2", :start 0.587, :duration 8.772}
                           {:type "animation-sequence" :target "senoravaca" :track 1 :offset 0.587
                            :data [{:start 0.873 :end 2.053 :anim "talk"}
                                   {:start 2.794 :end 4.952 :anim "talk"}
                                   {:start 5.336 :end 6.67 :anim "talk"}
                                   {:start 7.697 :end 9.136 :anim "talk"}]}]}

                   :senora-vaca-audio-touch-third-box
                   {:type "parallel"
                    :data [{:type "audio", :id "vaca-3", :start 0.76, :duration 4.986}
                           {:type "animation-sequence" :target "senoravaca" :track 1 :offset 0.76
                            :data [{:start 0.844 :end 1.844 :anim "talk"}
                                   {:start 2.214 :end 2.733 :anim "talk"}
                                   {:start 2.928 :end 5.674 :anim "talk"}]}]}

                   :show-boxes {:type "parallel"
                                :data [{:type "state" :target "box1" :id "visible"}
                                       {:type "state" :target "box2" :id "visible"}
                                       {:type "state" :target "box3" :id "visible"}]}

                   :switch-box-animations-idle {:type "parallel"
                                                :data [{:type "add-animation" :target "box1" :id "idle_fly1" :loop true}
                                                       {:type "add-animation" :target "box2" :id "idle_fly2" :loop true}
                                                       {:type "add-animation" :target "box3" :id "idle_fly3" :loop true}]}

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
                                                {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}]}

                   :show-second-box-word {:type "parallel"
                                         :data [{:type "animation" :target "box2" :id "wood" :loop false}
                                                {:type "set-skin" :target "box2"
                                                 :from-var [{:var-name "item-2" :action-property "skin" :var-property "skin"}]}
                                                {:type "copy-variable" :var-name "current-word" :from "item-2"}
                                                {:type "add-animation" :target "box2" :id "idle_fly2" :loop true}]}

                   :show-third-box-word {:type "parallel"
                                         :data [{:type "animation" :target "box3" :id "wood" :loop false}
                                                {:type "set-skin" :target "box3"
                                                 :from-var [{:var-name "item-2" :action-property "skin" :var-property "skin"}]}
                                                {:type "copy-variable" :var-name "current-word" :from "item-3"}
                                                {:type "add-animation" :target "box3" :id "idle_fly3" :loop true}]}

                   :vaca-this-is-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "home-vaca-this-is"}]}

                   :vaca-can-you-say {:type "parallel"
                                      :data [{:type "audio", :id "vaca-3", :start 11.75, :duration 0.935}
                                             {:type "animation-sequence" :target "senoravaca" :track 1 :offset 11.75
                                              :data [{:start 11.75 :end 12.62 :anim "talk"}]}]}

                   :vaca-question-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "home-vaca-question"}]}

                   :vaca-word-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "home-vaca-word"}]}

                   :group-word-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "home-group-word"}]}

                   :vaca-say-3-times {:type "parallel"
                                      :data [{:type "audio", :id "vaca-1", :start 25.079, :duration 2.381}
                                             {:type "animation-sequence" :target "senoravaca" :track 1 :offset 25.079
                                              :data [{:start 25.152 :end 25.513 :anim "talk"}
                                                     {:start 25.853 :end 27.2 :anim "talk"}]}]}

                   :vaca-3-times-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "home-vaca-3-times"}]}

                   :group-3-times-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "home-group-3-times"}]}

                   :vaca-once-more {:type "parallel"
                                      :data [{:type "audio", :id "vaca-1", :start 37.751, :duration 1.187}
                                             {:type "animation-sequence" :target "senoravaca" :track 1 :offset 37.751
                                              :data [{:start 37.751 :end 38.792 :anim "talk"}]}]}

                   :vaca-goodbye-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "home-vaca-goodbye"}]}

                   :introduce-word {:type "sequence"
                                    :data ["empty-big"
                                           "vaca-this-is-var"
                                           "empty-small"
                                           "vaca-can-you-say"
                                           "vaca-question-var"
                                           "empty-small"
                                           "vaca-word-var"
                                           "empty-big"
                                           "group-word-var"
                                           "empty-small"
                                           "vaca-say-3-times"
                                           "vaca-3-times-var"
                                           "empty-big"
                                           "group-3-times-var"
                                           "empty-small"
                                           "vaca-once-more"
                                           "empty-small"
                                           "group-3-times-var"
                                           "empty-small"
                                           "vaca-goodbye-var"
                                           "empty-big"]}

                   :this-is-ardilla {:type "parallel"
                                     :data [{:type "audio", :id "vaca-1", :start 15.621, :duration 2.313}
                                            {:type "animation-sequence" :target "senoravaca" :track 1 :offset 15.621
                                             :data [{:start 15.621 :end 17.88 :anim "talk"}]}]}

                   :this-is-oso {:type "parallel"
                                     :data [{:type "audio", :id "vaca-2", :start 10.267, :duration 2.417}
                                            {:type "animation-sequence" :target "senoravaca" :track 1 :offset 10.267
                                             :data [{:start 10.33 :end 10.91 :anim "talk"}
                                                    {:start 11.175 :end 12.586 :anim "talk"}]}]}

                   :this-is-incendio {:type "parallel"
                                     :data [{:type "audio", :id "vaca-3", :start 9.062, :duration 1.922}
                                            {:type "animation-sequence" :target "senoravaca" :track 1 :offset 9.062
                                             :data [{:start 9.127 :end 10.88 :anim "talk"}]}]}


                   :question-ardilla {:type "parallel"
                                     :data [{:type "audio", :id "vaca-1", :start 20.935, :duration 1.49}
                                            {:type "animation-sequence" :target "senoravaca" :track 1 :offset 20.935
                                             :data [{:start 20.935 :end 22.357 :anim "talk"}]}]}

                   :question-oso {:type "parallel"
                                 :data [{:type "audio", :id "vaca-2", :start 14.577, :duration 0.936}
                                        {:type "animation-sequence" :target "senoravaca" :track 1 :offset 14.577
                                         :data [{:start 14.632 :end 15.394 :anim "talk"}]}]}

                   :question-incendio {:type "parallel"
                                      :data [{:type "audio", :id "vaca-3", :start 12.782, :duration 0.649}
                                             {:type "animation-sequence" :target "senoravaca" :track 1 :offset 12.782
                                              :data [{:start 12.815 :end 13.347 :anim "talk"}]}]}


                   :word-ardilla {:type "parallel"
                                  :data [{:type "audio", :id "vaca-1", :start 20.935, :duration 1.49}
                                         {:type "animation-sequence" :target "senoravaca" :track 1 :offset 20.935
                                          :data [{:start 20.935 :end 22.357 :anim "talk"}]}]}

                   :word-oso {:type "parallel"
                                  :data [{:type "audio", :id "vaca-2", :start 16.567, :duration 1.104}
                                         {:type "animation-sequence" :target "senoravaca" :track 1 :offset 16.567
                                          :data [{:start 16.762 :end 17.531 :anim "talk"}]}]}

                   :word-incendio {:type "parallel"
                                       :data [{:type "audio", :id "vaca-3", :start 14.684, :duration 0.831}
                                              {:type "animation-sequence" :target "senoravaca" :track 1 :offset 14.684
                                               :data [{:start 14.795 :end 15.437 :anim "talk"}]}]}


                   :group-word-ardilla {:type "parallel"
                                  :data [{:type "audio", :id "vaca-1", :start 20.935, :duration 1.49}
                                         {:type "animation-sequence" :target "senoravaca" :track 1 :offset 20.935
                                          :data [{:start 20.935 :end 22.357 :anim "talk"}]}
                                         {:type "audio", :id "vera-1", :start 1.25, :duration 0.854}
                                         {:type "animation-sequence" :target "vera" :track 1 :offset 1.25
                                          :data [{:start 1.25 :end 2.05 :anim "talk"}]}]}

                   :group-word-oso {:type "parallel"
                              :data [{:type "audio", :id "vaca-2", :start 16.567, :duration 1.104}
                                     {:type "animation-sequence" :target "senoravaca" :track 1 :offset 16.567
                                      :data [{:start 16.762 :end 17.531 :anim "talk"}]}
                                     {:type "audio", :id "vera-2", :start 0.812, :duration 0.754}
                                     {:type "animation-sequence" :target "vera" :track 1 :offset 0.812
                                      :data [{:start 0.812 :end 1.487 :anim "talk"}]}]}

                   :group-word-incendio {:type "parallel"
                                   :data [{:type "audio", :id "vaca-3", :start 14.684, :duration 0.831}
                                          {:type "animation-sequence" :target "senoravaca" :track 1 :offset 14.684
                                           :data [{:start 14.795 :end 15.437 :anim "talk"}]}
                                          {:type "audio", :id "vera-3", :start 0.715, :duration 0.914}
                                          {:type "animation-sequence" :target "vera" :track 1 :offset 0.715
                                           :data [{:start 0.852 :end 1.479 :anim "talk"}]}]}

                   :vaca-3-times-ardilla {:type "parallel"
                                      :data [{:type "audio", :id "vaca-1", :start 28.006, :duration 3.904}
                                             {:type "animation-sequence" :target "senoravaca" :track 1 :offset 28.006
                                              :data [{:start 28.018 :end 28.892 :anim "talk"}
                                                     {:start 29.127 :end 29.978 :anim "talk"}
                                                     {:start 30.365 :end 31.849 :anim "talk"}]}]}

                   :vaca-3-times-oso {:type "parallel"
                                  :data [{:type "audio", :id "vaca-2", :start 32.631, :duration 3.946}
                                         {:type "animation-sequence" :target "senoravaca" :track 1 :offset 32.631
                                          :data [{:start 32.715 :end 33.456 :anim "talk"}
                                                 {:start 34.035 :end 34.664 :anim "talk"}
                                                 {:start 35.418 :end 36.445 :anim "talk"}]}]}

                   :vaca-3-times-incendio {:type "parallel"
                                       :data [{:type "audio", :id "vaca-3", :start 32.127, :duration 3.538}
                                              {:type "animation-sequence" :target "senoravaca" :track 1 :offset 32.127
                                               :data [{:start 32.205 :end 32.809 :anim "talk"}
                                                      {:start 33.296 :end 33.919 :anim "talk"}
                                                      {:start 34.412 :end 35.49 :anim "talk"}]}]}

                   :group-3-times-ardilla {:type "parallel"
                                          :data [{:type "audio", :id "vaca-1", :start 28.006, :duration 3.904}
                                                 {:type "animation-sequence" :target "senoravaca" :track 1 :offset 28.006
                                                  :data [{:start 28.018 :end 28.892 :anim "talk"}
                                                         {:start 29.127 :end 29.978 :anim "talk"}
                                                         {:start 30.365 :end 31.849 :anim "talk"}]}
                                                 {:type "audio", :id "vera-1", :start 3.788, :duration 3.407}
                                                 {:type "animation-sequence" :target "vera" :track 1 :offset 3.788
                                                  :data [{:start 3.849 :end 4.52 :anim "talk"}
                                                         {:start 4.939 :end 5.678 :anim "talk"}
                                                         {:start 6.021 :end 7.134 :anim "talk"}]}]}

                   :group-3-times-oso {:type "parallel"
                                      :data [{:type "audio", :id "vaca-2", :start 32.631, :duration 3.946}
                                             {:type "animation-sequence" :target "senoravaca" :track 1 :offset 32.631
                                              :data [{:start 32.715 :end 33.456 :anim "talk"}
                                                     {:start 34.035 :end 34.664 :anim "talk"}
                                                     {:start 35.418 :end 36.445 :anim "talk"}]}
                                             {:type "audio", :id "vera-2", :start 2.887, :duration 3.678}
                                             {:type "animation-sequence" :target "vera" :track 1 :offset 2.887
                                              :data [{:start 2.960 :end 3.54 :anim "talk"}
                                                     {:start 4.2 :end 4.853 :anim "talk"}
                                                     {:start 5.339 :end 6.391 :anim "talk"}]}]}

                   :group-3-times-incendio {:type "parallel"
                                           :data [{:type "audio", :id "vaca-3", :start 32.127, :duration 3.538}
                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 32.127
                                                   :data [{:start 32.205 :end 32.809 :anim "talk"}
                                                          {:start 33.296 :end 33.919 :anim "talk"}
                                                          {:start 34.412 :end 35.49 :anim "talk"}]}
                                                  {:type "audio", :id "vera-3", :start 5.874, :duration 3.823}
                                                  {:type "animation-sequence" :target "vera" :track 1 :offset 5.874
                                                   :data [{:start 6.042 :end 6.639 :anim "talk"}
                                                          {:start 7.061 :end 7.627 :anim "talk"}
                                                          {:start 8.261 :end 9.455 :anim "talk"}]}]}

                   :goodbye-ardilla {:type "parallel"
                                           :data [{:type "audio", :id "vaca-1", :start 44.817, :duration 2.492}
                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 44.817
                                                   :data [{:start 44.817 :end 46.167 :anim "talk"}
                                                          {:start 46.555 :end 47.198 :anim "talk"}]}]}

                   :goodbye-oso {:type "parallel"
                                       :data [{:type "audio", :id "vaca-2", :start 46.125, :duration 2.528}
                                              {:type "animation-sequence" :target "senoravaca" :track 1 :offset 46.125
                                               :data [{:start 46.223 :end 47.425 :anim "talk"}
                                                      {:start 47.914 :end 48.493 :anim "talk"}]}]}

                   :goodbye-incendio {:type "parallel"
                                            :data [{:type "audio", :id "vaca-3", :start 42.754, :duration 2.506}
                                                   {:type "animation-sequence" :target "senoravaca" :track 1 :offset 42.754
                                                    :data [{:start 42.819 :end 44.046 :anim "talk"}
                                                           {:start 44.449 :end 45.111 :anim "talk"}]}]}

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

                   :empty-small {:type "empty" :duration 500}
                   :empty-big {:type "empty" :duration 1000}
                   :clear-instruction {:type "remove-flows" :flow-tag "instruction"}
                   :start-background-music {:type "audio" :id "background" :loop true}
                   :finish-activity {:type "finish-activity" :id "home-introduce"}}
   :audio
                  {:casa-welcome "/raw/audio/scripts/intro/intro-welcome.mp3"
                   :casa-finish "/raw/audio/scripts/intro/intro-finish.mp3"
                   :background "/raw/audio/background/POL-daily-special-short.mp3"
                   :fw-try-again "/raw/audio/ferris-wheel/fw-try-again.mp3"
                   :vaca-1 "/raw/audio/l1/a1/L1_A1_Vaca_Ardilla.m4a"
                   :vaca-2 "/raw/audio/l1/a1/L1_A1_Vaca_Oso.m4a"
                   :vaca-3 "/raw/audio/l1/a1/L1_A1_Vaca_Iman.m4a"
                   :vera-1 "/raw/audio/l1/a1/L1_A1_Vera_Ardilla.m4a"
                   :vera-2 "/raw/audio/l1/a1/L1_A1_Vera_Oso.m4a"
                   :vera-3 "/raw/audio/l1/a1/L1_A1_Vera_Iman.m4a"},
   :triggers      {:music {:on "start" :action "start-background-music"}}
   :metadata      {:autostart true
                   :prev "map"}})