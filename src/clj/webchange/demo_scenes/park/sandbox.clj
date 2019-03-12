(ns webchange.demo-scenes.park.sandbox)

(def sandbox-scene
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

                   {:url "/raw/img/park/sandbox/background.jpg", :size 10 :type "image"}

                   {:url "/raw/anim/mari/skeleton.atlas", :size 1, :type "anim-text"}
                   {:url "/raw/anim/mari/skeleton.json", :size 1, :type "anim-text"}
                   {:url "/raw/anim/mari/skeleton.png", :size 1, :type "anim-texture"}

                   {:url "/raw/anim/boxes/skeleton.atlas", :size 1, :type "anim-text"}
                   {:url "/raw/anim/boxes/skeleton.json", :size 1, :type "anim-text"}
                   {:url "/raw/anim/boxes/skeleton.png", :size 1, :type "anim-texture"}

                   ],
   :objects
                  {:background {:type "background", :src "/raw/img/park/sandbox/background.jpg"},

                   :mari {:type "animation" :scene-name "mari" :name "mari" :anim "idle"
                          :start true :speed 0.35
                          :x 1535 :y 715 :width 473 :height 511
                          :scale-y 0.5 :scale-x 0.5}

                   :word {:type "transparent" :x 313 :y 91 :width 1200 :height 400
                          :states {:default {:type "transparent"}
                                   :show {:type "text" :width 1200 :height 400
                                          :align "center" :vertical-align "middle"
                                          :font-family "Luckiest Guy" :font-size 160
                                          :shadow-color "#1a1a1a" :shadow-offset {:x 5 :y 5} :shadow-blur 5 :shadow-opacity 0.5
                                          :fill "white"}}}

                   :box1 {:type "animation" :x 383 :y 846 :width 771 :height 1033 :anim-offset {:x 0 :y -300}
                          :name "boxes" :anim "idle2" :scale {:x 0.25 :y 0.25} :speed 0.3 :loop true :start true
                          :scene-name "box1" :transition "box1" :skin "dino"
                          :actions {:click {:type "action" :id "box-1-start" :on "click"}}}

                   :box2 {:type "animation" :x 500 :y 740 :width 771 :height 1033 :anim-offset {:x 0 :y -300}
                          :name "boxes" :anim "idle2" :scale {:x 0.25 :y 0.25} :speed 0.3 :loop true :start true
                          :scene-name "box2" :transition "box2" :skin "fire"
                          :actions {:click {:type "action" :id "box-2-start" :on "click"}}}

                   :box3 {:type "animation" :x 689 :y 718 :width 771 :height 1033 :anim-offset {:x 0 :y -300}
                          :name "boxes" :anim "idle2" :scale {:x 0.25 :y 0.25} :speed 0.3 :loop true :start true
                          :scene-name "box3" :transition "box3" :skin "empty"
                          :actions {:click {:type "action" :id "box-3-start" :on "click"}}}

                   :box4 {:type "animation" :x 886 :y 743 :width 771 :height 1033 :anim-offset {:x 0 :y -300}
                          :name "boxes" :anim "idle2" :scale {:x 0.25 :y 0.25} :speed 0.3 :loop true :start true
                          :scene-name "box4" :transition "box4" :skin "empty"
                          :actions {:click {:type "action" :id "box-4-start" :on "click"}}}

                   },
   :scene-objects [["background"] ["box3" "box4" "box2" "box1" "word"] ["mari"]],
   :actions
                  {:mari-welcome-audio {:type "parallel"
                                        :data [{:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                                               {:type "sequence-data"
                                                :data [{:type "empty" :duration 225}
                                                       {:type "animation" :target "mari" :id "talk" :track 1}
                                                       {:type "empty" :duration 1307}
                                                       {:type "animation" :target "mari" :id "empty" :track 1}
                                                       {:type "empty" :duration 550}
                                                       {:type "animation" :target "mari" :id "talk":track 1}
                                                       {:type "empty" :duration 1069}
                                                       {:type "animation" :target "mari" :id "empty" :track 1}]}]}

                   :mari-touch-audio {:type "parallel"
                                        :data [{:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                                               {:type "sequence-data"
                                                :data [{:type "empty" :duration 225}
                                                       {:type "animation" :target "mari" :id "talk" :track 1}
                                                       {:type "empty" :duration 1307}
                                                       {:type "animation" :target "mari" :id "empty" :track 1}
                                                       {:type "empty" :duration 550}
                                                       {:type "animation" :target "mari" :id "talk":track 1}
                                                       {:type "empty" :duration 1069}
                                                       {:type "animation" :target "mari" :id "empty" :track 1}]}]}

                   :mari-more-audio {:type "parallel"
                                      :data [{:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                                             {:type "sequence-data"
                                              :data [{:type "empty" :duration 225}
                                                     {:type "animation" :target "mari" :id "talk" :track 1}
                                                     {:type "empty" :duration 1307}
                                                     {:type "animation" :target "mari" :id "empty" :track 1}
                                                     {:type "empty" :duration 550}
                                                     {:type "animation" :target "mari" :id "talk":track 1}
                                                     {:type "empty" :duration 1069}
                                                     {:type "animation" :target "mari" :id "empty" :track 1}]}]}

                   :mari-one-more-audio {:type "parallel"
                                     :data [{:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                                            {:type "sequence-data"
                                             :data [{:type "empty" :duration 225}
                                                    {:type "animation" :target "mari" :id "talk" :track 1}
                                                    {:type "empty" :duration 1307}
                                                    {:type "animation" :target "mari" :id "empty" :track 1}
                                                    {:type "empty" :duration 550}
                                                    {:type "animation" :target "mari" :id "talk":track 1}
                                                    {:type "empty" :duration 1069}
                                                    {:type "animation" :target "mari" :id "empty" :track 1}]}]}

                   :mari-good-job {:type "parallel"
                                     :data [{:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                                            {:type "sequence-data"
                                             :data [{:type "empty" :duration 225}
                                                    {:type "animation" :target "mari" :id "talk" :track 1}
                                                    {:type "empty" :duration 1307}
                                                    {:type "animation" :target "mari" :id "empty" :track 1}
                                                    {:type "empty" :duration 550}
                                                    {:type "animation" :target "mari" :id "talk":track 1}
                                                    {:type "empty" :duration 1069}
                                                    {:type "animation" :target "mari" :id "empty" :track 1}]}]}

                   :mari-this-is-letter-a {:type "parallel"
                                     :data [{:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                                            {:type "sequence-data"
                                             :data [{:type "empty" :duration 225}
                                                    {:type "animation" :target "mari" :id "talk" :track 1}
                                                    {:type "empty" :duration 1307}
                                                    {:type "animation" :target "mari" :id "empty" :track 1}
                                                    {:type "empty" :duration 550}
                                                    {:type "animation" :target "mari" :id "talk":track 1}
                                                    {:type "empty" :duration 1069}
                                                    {:type "animation" :target "mari" :id "empty" :track 1}]}]}

                   :mari-this-is-letter-o {:type "parallel"
                                     :data [{:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                                            {:type "sequence-data"
                                             :data [{:type "empty" :duration 225}
                                                    {:type "animation" :target "mari" :id "talk" :track 1}
                                                    {:type "empty" :duration 1307}
                                                    {:type "animation" :target "mari" :id "empty" :track 1}
                                                    {:type "empty" :duration 550}
                                                    {:type "animation" :target "mari" :id "talk":track 1}
                                                    {:type "empty" :duration 1069}
                                                    {:type "animation" :target "mari" :id "empty" :track 1}]}]}

                   :mari-this-is-letter-i {:type "parallel"
                                     :data [{:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                                            {:type "sequence-data"
                                             :data [{:type "empty" :duration 225}
                                                    {:type "animation" :target "mari" :id "talk" :track 1}
                                                    {:type "empty" :duration 1307}
                                                    {:type "animation" :target "mari" :id "empty" :track 1}
                                                    {:type "empty" :duration 550}
                                                    {:type "animation" :target "mari" :id "talk":track 1}
                                                    {:type "empty" :duration 1069}
                                                    {:type "animation" :target "mari" :id "empty" :track 1}]}]}

                   :mari-this-is-letter-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "sandbox-this-is-letter"}]}

                   :complete-word-1  {:type "set-variable" :var-name "word-1" :var-value true}
                   :complete-word-2  {:type "set-variable" :var-name "word-2" :var-value true}
                   :complete-word-3  {:type "set-variable" :var-name "word-3" :var-value true}
                   :complete-word-4  {:type "set-variable" :var-name "word-4" :var-value true}

                   :box-1-start {:type "sequence"
                                 :data ["complete-word-1"
                                        "word-1-state-var"
                                        "box-1-jump-in"
                                        "word-1-state-var"
                                        "box-1-jump-out"
                                        "test-concept-complete"]}

                   :word-1-state-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "sandbox-state-word-1"}]}

                   :box-1-jump-in {:type "parallel"
                                   :data [{:type "animation" :target "box1" :id "jump2"}
                                          {:type "sequence-data"
                                           :data [{:type "empty" :duration 500}
                                                  {:type "transition" :transition-id "box1" :to {:x 718 :y 848 :duration 0.7 :loop false}}]}
                                          {:type "add-animation" :target "box1" :id "idle2" :loop true}]}

                   :box-1-jump-out {:type "parallel"
                                   :data [{:type "animation" :target "box1" :id "jump2"}
                                          {:type "sequence-data"
                                           :data [{:type "empty" :duration 500}
                                                  {:type "transition" :transition-id "box1" :to {:x 383 :y 846 :duration 0.7 :loop false}}]}
                                          {:type "add-animation" :target "box1" :id "idle2" :loop true}]}

                   :box-2-start {:type "sequence"
                                 :data ["complete-word-2"
                                        "word-2-state-var"
                                        "box-2-jump-in"
                                        "word-2-state-var"
                                        "box-2-jump-out"
                                        "test-concept-complete"]}

                   :word-2-state-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "sandbox-state-word-2"}]}

                   :box-2-jump-in {:type "parallel"
                                   :data [{:type "animation" :target "box2" :id "jump2"}
                                          {:type "sequence-data"
                                           :data [{:type "empty" :duration 500}
                                                  {:type "transition" :transition-id "box2" :to {:x 718 :y 848 :duration 0.7 :loop false}}]}
                                          {:type "add-animation" :target "box2" :id "idle2" :loop true}]}

                   :box-2-jump-out {:type "parallel"
                                    :data [{:type "animation" :target "box2" :id "jump2"}
                                           {:type "sequence-data"
                                            :data [{:type "empty" :duration 500}
                                                   {:type "transition" :transition-id "box2" :to {:x 500 :y 740 :duration 0.7 :loop false}}]}
                                           {:type "add-animation" :target "box2" :id "idle2" :loop true}]}

                   :box-3-start {:type "sequence"
                                 :data ["complete-word-3"
                                        "word-3-state-var"
                                        "box-3-jump-in"
                                        "word-3-state-var"
                                        "box-3-jump-out"
                                        "test-concept-complete"]}

                   :word-3-state-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "sandbox-state-word-3"}]}

                   :box-3-jump-in {:type "parallel"
                                   :data [{:type "animation" :target "box3" :id "jump2"}
                                          {:type "sequence-data"
                                           :data [{:type "empty" :duration 500}
                                                  {:type "transition" :transition-id "box3" :to {:x 718 :y 848 :duration 0.7 :loop false}}]}
                                          {:type "add-animation" :target "box3" :id "idle2" :loop true}]}

                   :box-3-jump-out {:type "parallel"
                                    :data [{:type "animation" :target "box3" :id "jump2"}
                                           {:type "sequence-data"
                                            :data [{:type "empty" :duration 500}
                                                   {:type "transition" :transition-id "box3" :to {:x 689 :y 718 :duration 0.7 :loop false}}]}
                                           {:type "add-animation" :target "box3" :id "idle2" :loop true}]}


                   :box-4-start {:type "sequence"
                                 :data ["complete-word-4"
                                        "word-4-state-var"
                                        "box-4-jump-in"
                                        "word-4-state-var"
                                        "box-4-jump-out"
                                        "test-concept-complete"]}

                   :word-4-state-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "sandbox-state-word-4"}]}

                   :box-4-jump-in {:type "parallel"
                                   :data [{:type "animation" :target "box4" :id "jump2"}
                                          {:type "sequence-data"
                                           :data [{:type "empty" :duration 500}
                                                  {:type "transition" :transition-id "box4" :to {:x 718 :y 848 :duration 0.7 :loop false}}]}
                                          {:type "add-animation" :target "box4" :id "idle2" :loop true}]}

                   :box-4-jump-out {:type "parallel"
                                    :data [{:type "animation" :target "box4" :id "jump2"}
                                           {:type "sequence-data"
                                            :data [{:type "empty" :duration 500}
                                                   {:type "transition" :transition-id "box4" :to {:x 886 :y 743 :duration 0.7 :loop false}}]}
                                           {:type "add-animation" :target "box4" :id "idle2" :loop true}]}

                   :sandbox-state-word-abeja {:type "sequence-data"
                                              :data [{:type "state" :target "word" :id "show" :params {:text "Abeja"}}
                                                     {:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                                                     {:type "state" :target "word" :id "default"}]}

                   :sandbox-state-word-arbol {:type "sequence-data"
                                              :data [{:type "state" :target "word" :id "show" :params {:text "Arbol"}}
                                                     {:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                                                     {:type "state" :target "word" :id "default"}]}

                   :sandbox-state-word-avion {:type "sequence-data"
                                              :data [{:type "state" :target "word" :id "show" :params {:text "Avion"}}
                                                     {:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                                                     {:type "state" :target "word" :id "default"}]}

                   :sandbox-state-word-arana {:type "sequence-data"
                                              :data [{:type "state" :target "word" :id "show" :params {:text "Arana"}}
                                                     {:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                                                     {:type "state" :target "word" :id "default"}]}

                   :test-concept-complete  {:type "test-var-list"
                                            :var-names ["word-1" "word-2" "word-3" "word-4"]
                                            :values [true true true true]
                                            :success "renew-current-concept"}

                   :hide-word  {:type "state" :target "word" :id "default"}

                   :renew-words  {:type "lesson-var-provider"
                                  :provider-id "words-set"
                                  :variables ["item-1" "item-2" "item-3"]
                                  :shuffled false
                                  :from      "concepts"}

                   :renew-current-concept {:type "parallel"
                                           :data [{:type "set-variable" :var-name "word-1" :var-value false}
                                                  {:type "set-variable" :var-name "word-2" :var-value false}
                                                  {:type "set-variable" :var-name "word-3" :var-value false}
                                                  {:type "set-variable" :var-name "word-4" :var-value false}
                                                  {:type "vars-var-provider"
                                                   :provider-id "current-word"
                                                   :variables ["current-word"]
                                                   :from ["item-1" "item-2" "item-3"]
                                                   :on-end "finish-activity"}]}

                   :clear-instruction {:type "remove-flows" :flow-tag "instruction"}
                   :start-activity {:type "sequence"
                                    :data ["clear-instruction"
                                           "renew-words"
                                           "mari-welcome-audio"
                                           "renew-current-concept"
                                           "mari-this-is-letter-var"
                                           ]}

                   :finish-activity {:type "finish-activity" :id "sandbox"}}
   :audio
                  {:casa-welcome "/raw/audio/scripts/intro/intro-welcome.mp3"
                   :casa-finish "/raw/audio/scripts/intro/intro-finish.mp3"
                   :teacher   "/raw/audio/scripts/intro/teacher.mp3",
                   :vera      "/raw/audio/scripts/intro/vera.mp3",
                   :syllables "/raw/audio/scripts/intro/syllables.mp3"
                   :background "/raw/audio/background/POL-daily-special-short.mp3"
                   :fw-try-again "/raw/audio/ferris-wheel/fw-try-again.mp3"},
   :triggers      {:start {:on "start" :action "start-activity"}}
   :metadata      {:autostart true
                   :prev "park"}})