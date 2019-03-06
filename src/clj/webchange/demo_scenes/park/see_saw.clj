(ns webchange.demo-scenes.park.see-saw)

(def see-saw-scene
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

                   {:url "/raw/img/park/see-saw/background.jpg", :size 10 :type "image"}
                   {:url "/raw/img/park/see-saw/saw_01.png", :size 1, :type "image"}
                   {:url "/raw/img/park/see-saw/saw_02.png", :size 1, :type "image"}
                   {:url "/raw/img/park/see-saw/saw_03.png", :size 1, :type "image"}

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
                  {:background {:type "background", :src "/raw/img/park/see-saw/background.jpg"},
                   :vera       {:type "animation" :x 350 :y 960 :name "vera" :anim "idle" :speed 0.3
                                :width 1800 :height 2558 :scale {:x 0.2 :y 0.2} :start true}

                   :mari {:type "animation" :scene-name "mari" :name "mari" :anim "idle"
                          :start true :speed 0.35
                          :x 1535 :y 715 :width 473 :height 511
                          :scale-y 0.5 :scale-x 0.5}

                   :see-saw {:type "group" :x 711 :y 726 :children ["see-saw-3" "see-saw-plank" "see-saw-2"]}
                   :see-saw-plank {:type "group" :children ["see-saw-1" "box-ph"] :rotation -15
                                   :origin {:type "center-center"} :transition "see-saw-plank"}
                   :see-saw-1 {:type "image" :width 889 :height 106
                                :src  "/raw/img/park/see-saw/saw_01.png" :origin {:type "center-center"}},
                   :see-saw-2    {:type "image" :width 195 :height 175 :x 0 :y 67
                                :src  "/raw/img/park/see-saw/saw_02.png" :origin {:type "center-center"}},
                   :see-saw-3    {:type "image" :width 195 :height 179 :x -10 :y 48
                                :src  "/raw/img/park/see-saw/saw_03.png" :origin {:type "center-center"}},

                   :box1 {:type "transparent" :x 600 :y 400 :width 206 :height 210 :origin {:type "center-center"}
                          :scene-name "box1" :draggable {:var-name "drag-box-1"} :transition "box1"
                          :states {:default {:type "transparent"}
                                   :come {:type "animation" :name "boxes" :anim "come"
                                             :scale {:x 0.2 :y 0.2} :speed 0.3 :loop false :start true
                                             :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}
                                   :initial-position {:x 600 :y 400}}
                          :actions {:drag-end {:type "action" :id "drag-box1" :on "drag-end"}}}

                   :box2 {:type "transparent" :x 1000 :y 400 :width 206 :height 210 :origin {:type "center-center"}
                          :scene-name "box2" :draggable {:var-name "drag-box-2"} :transition "box2"
                          :states {:default {:type "transparent"}
                                   :come {:type "animation" :name "boxes" :anim "come"
                                             :from-var [{:var-name "item-2" :action-property "skin" :var-property "skin"}]
                                             :scale {:x 0.2 :y 0.2} :speed 0.3 :loop false :start true}
                                   :initial-position {:x 1000 :y 400}}
                          :actions {:drag-end {:type "action" :id "drag-box2" :on "drag-end"}}}

                   :box3 {:type "transparent" :x 1400 :y 400 :width 206 :height 210 :origin {:type "center-center"}
                          :scene-name "box3" :draggable {:var-name "drag-box-3"} :transition "box3"
                          :states {:default {:type "transparent"}
                                   :come {:type "animation" :name "boxes" :anim "come"
                                             :from-var [{:var-name "item-3" :action-property "skin" :var-property "skin"}]
                                             :scale {:x 0.2 :y 0.2} :speed 0.3 :loop false :start true}
                                   :initial-position {:x 1400 :y 400}}
                          :actions {:drag-end {:type "action" :id "drag-box3" :on "drag-end"}}}

                   :box-ph {:type "transparent" :width 300 :height 300 :x 400 :y 0 :origin {:type "center-center"}
                            :scene-name "box-ph" :transition "box-ph"
                            :states {:default {:type "transparent"}
                                     :visible {:type "animation" :name "boxes" :anim "idle" :skin "empty"
                                               :scale {:x 0.2 :y 0.2} :speed 0.3 :loop true :start true}}}

                   },
   :scene-objects [["background"] ["see-saw"] ["vera" "mari"] ["box1" "box2" "box3"]],
   :actions
                  {:mari-welcome-audio
                                       {:type "parallel"
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

                   :mari-move-to-start
                                       {:type "parallel"
                                        :data [{:type "audio", :id "teacher", :start 0.77, :duration 3.24}
                                               {:type "sequence-data"
                                                :data [{:type "empty" :duration 225}
                                                       {:type "animation" :target "mari" :id "talk" :track 1}
                                                       {:type "empty" :duration 1307}
                                                       {:type "animation" :target "mari" :id "empty" :track 1}
                                                       {:type "empty" :duration 550}
                                                       {:type "animation" :target "mari" :id "talk" :track 1}
                                                       {:type "empty" :duration 1069}
                                                       {:type "animation" :target "mari" :id "empty" :track 1}]}]}

                   :show-boxes {:type "parallel"
                                :data [{:type "state" :target "box1" :id "come"}
                                       {:type "state" :target "box2" :id "come"}
                                       {:type "state" :target "box3" :id "come"}]}

                   :switch-box-animations-idle {:type "parallel"
                                                :data [{:type "set-skin" :target "box1"
                                                        :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}
                                                       {:type "set-skin" :target "box2"
                                                        :from-var [{:var-name "item-2" :action-property "skin" :var-property "skin"}]}
                                                       {:type "set-skin" :target "box3"
                                                        :from-var [{:var-name "item-3" :action-property "skin" :var-property "skin"}]}
                                                       {:type "add-animation" :target "box1" :id "idle" :loop true}
                                                       {:type "add-animation" :target "box2" :id "idle" :loop true}
                                                       {:type "add-animation" :target "box3" :id "idle" :loop true}]}

                   :wait-for-box-animations {:type "empty" :duration 500}

                   :set-current-box1 {:type "set-variable" :var-name "current-box" :var-value "box1"}
                   :set-current-box2 {:type "set-variable" :var-name "current-box" :var-value "box2"}
                   :set-current-box3 {:type "set-variable" :var-name "current-box" :var-value "box3"}

                   :check-box1 {:type "test-var-scalar"
                                   :var-name "current-box"
                                   :value "box1"
                                   :success "box-1-start"
                                   :fail "box-1-revert"}

                   :check-box2 {:type "test-var-scalar"
                                   :var-name "current-box"
                                   :value "box2"
                                   :success "box-2-start"
                                   :fail "box-2-revert"}

                   :check-box3 {:type "test-var-scalar"
                                   :var-name "current-box"
                                   :value "box3"
                                   :success "box-3-start"
                                   :fail "box-3-revert"}

                   :drag-box1 {:type "test-transitions-collide"
                                :transition-1 "box1"
                                :transition-2 "box-ph"
                                :success "check-box1"
                                :fail "box-1-revert"}

                   :drag-box2 {:type "test-transitions-collide"
                               :transition-1 "box2"
                               :transition-2 "box-ph"
                               :success "check-box2"
                               :fail "box-2-revert"}

                   :drag-box3 {:type "test-transitions-collide"
                               :transition-1 "box3"
                               :transition-2 "box-ph"
                               :success "check-box3"
                               :fail "box-3-revert"}

                   :box-1-revert {:type "transition" :transition-id "box1" :to {:x 600 :y 400 :duration 2}}

                   :box-1-start {:type "sequence"
                                 :data ["show-box-1-ph"
                                        "introduce-concept"
                                        "hide-box-1-ph"
                                        "set-current-box2"
                                        "try-another"]}

                   :show-box-1-ph {:type "sequence-data"
                                   :data [{:type "state" :target "box1" :id "default"}
                                          {:type "state" :target "box-ph" :id "visible"}
                                          {:type "empty" :duration 500}
                                          {:type "set-skin" :target "box-ph"
                                           :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}
                                          {:type "copy-variable" :var-name "current-word" :from "item-1"}
                                          ]}

                   :hide-box-1-ph {:type "sequence-data"
                                   :data [{:type "state" :target "box-ph" :id "default"}
                                          {:type "state" :target "box1" :id "come"}
                                          {:type "empty" :duration 500}
                                          {:type "set-skin" :target "box1"
                                           :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}
                                          {:type "add-animation" :target "box1" :id "idle" :loop true}]}

                   :box-2-revert {:type "transition" :transition-id "box2" :to {:x 1000 :y 400 :duration 2}}

                   :box-2-start {:type "sequence"
                                 :data ["show-box-2-ph"
                                        "introduce-concept"
                                        "hide-box-2-ph"
                                        "set-current-box3"
                                        "try-another"]}

                   :show-box-2-ph {:type "sequence-data"
                                   :data [{:type "state" :target "box2" :id "default"}
                                          {:type "state" :target "box-ph" :id "visible"}
                                          {:type "empty" :duration 500}
                                          {:type "set-skin" :target "box-ph"
                                           :from-var [{:var-name "item-2" :action-property "skin" :var-property "skin"}]}
                                          {:type "copy-variable" :var-name "current-word" :from "item-2"}
                                          ]}

                   :hide-box-2-ph {:type "sequence-data"
                                   :data [{:type "state" :target "box-ph" :id "default"}
                                          {:type "state" :target "box2" :id "come"}
                                          {:type "empty" :duration 500}
                                          {:type "set-skin" :target "box2"
                                           :from-var [{:var-name "item-2" :action-property "skin" :var-property "skin"}]}
                                          {:type "add-animation" :target "box2" :id "idle" :loop true}]}

                   :box-3-revert {:type "transition" :transition-id "box3" :to {:x 1400 :y 400 :duration 2}}

                   :box-3-start {:type "sequence"
                                 :data ["show-box-3-ph"
                                        "introduce-concept"
                                        "hide-box-3-ph"
                                        "finish-activity"]}

                   :show-box-3-ph {:type "sequence-data"
                                   :data [{:type "state" :target "box3" :id "default"}
                                          {:type "state" :target "box-ph" :id "visible"}
                                          {:type "empty" :duration 500}
                                          {:type "set-skin" :target "box-ph"
                                           :from-var [{:var-name "item-3" :action-property "skin" :var-property "skin"}]}
                                          {:type "copy-variable" :var-name "current-word" :from "item-3"}
                                          ]}

                   :hide-box-3-ph {:type "sequence-data"
                                   :data [{:type "state" :target "box-ph" :id "default"}
                                          {:type "state" :target "box3" :id "come"}
                                          {:type "empty" :duration 500}
                                          {:type "set-skin" :target "box3"
                                           :from-var [{:var-name "item-3" :action-property "skin" :var-property "skin"}]}
                                          {:type "add-animation" :target "box3" :id "idle" :loop true}]}

                   :voice-low-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "seesaw-voice-low"}]}
                   :voice-high-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "seesaw-voice-high"}]}

                   :word-ardilla-low {:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                   :word-oso-low {:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                   :word-incendio-low {:type "audio", :id "syllables", :start 0.77, :duration 1.24}

                   :word-ardilla-high {:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                   :word-oso-high {:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                   :word-incendio-high {:type "audio", :id "syllables", :start 0.77, :duration 1.24}

                   :introduce-concept {:type "sequence"
                                       :data ["go-down"
                                              "go-up"
                                              "go-down"
                                              "go-up"]}

                   :go-down {:type "parallel"
                             :data [{:type "empty" :duration 5000}
                                    {:type "transition" :transition-id "see-saw-plank" :to {:rotation 15 :duration 5 :loop false}}
                                    {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "seesaw-voice-low"}]}]}

                   :go-up {:type "parallel"
                           :data [{:type "empty" :duration 5000}
                                  {:type "transition" :transition-id "see-saw-plank" :to {:rotation -15 :duration 5 :loop false}}
                                  {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "seesaw-voice-high"}]}]}

                   :try-another {:type "parallel"
                                 :data [{:type "audio", :id "syllables", :start 0.77, :duration 1.24}
                                        {:type "sequence-data"
                                         :data [{:type "empty" :duration 225}
                                                {:type "animation" :target "mari" :id "talk"}
                                                {:type "empty" :duration 1307}
                                                {:type "animation" :target "mari" :id "idle"}]}]}

                   :pick-wrong {:type "sequence"
                                :data ["audio-wrong"]}

                   :audio-wrong {:type "audio" :id "fw-try-again" :start 0.892 :duration 1.869 :offset 0.2}

                   :renew-words  {:type "lesson-var-provider"
                                  :provider-id "words-set"
                                  :variables ["item-1" "item-2" "item-3"]
                                  :from      "concepts"}

                   :clear-instruction {:type "remove-flows" :flow-tag "instruction"}
                   :start-activity {:type "sequence"
                                    :data ["clear-instruction"
                                           "renew-words"
                                           "mari-welcome-audio"
                                           "set-current-box1"
                                           "show-boxes"
                                           "wait-for-box-animations"
                                           "switch-box-animations-idle"
                                           "mari-move-to-start"]}

                   :finish-activity {:type "finish-activity" :id "see-saw"}}
   :audio
                  {:casa-welcome "/raw/audio/scripts/intro/intro-welcome.mp3"
                   :casa-finish "/raw/audio/scripts/intro/intro-finish.mp3"
                   :teacher   "/raw/audio/scripts/intro/teacher.mp3",
                   :vera      "/raw/audio/scripts/intro/vera.mp3",
                   :syllables "/raw/audio/scripts/intro/syllables.mp3"
                   :background "/raw/audio/background/POL-daily-special-short.mp3"
                   :fw-try-again "/raw/audio/ferris-wheel/fw-try-again.mp3"},
   :triggers      {:start {:on "start" :action "start-activity"}}
   :metadata      {:autostart true}})