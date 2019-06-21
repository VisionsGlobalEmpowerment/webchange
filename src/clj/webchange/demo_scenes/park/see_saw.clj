(ns webchange.demo-scenes.park.see-saw)

(def see-saw-scene
  {:assets
                  [{:url "/raw/audio/l1/a2/L1_A2_GameVoice_Object 1 List.m4a", :size 5, :type "audio" :alias "game voice 1"}
                   {:url "/raw/audio/l1/a2/L1_A2_GameVoice_Object 2 List.m4a", :size 5, :type "audio" :alias "game voice 2"}
                   {:url "/raw/audio/l1/a2/L1_A2_GameVoice_Object 3 List.m4a", :size 5, :type "audio" :alias "game voice 3"}
                   {:url "/raw/audio/l1/a2/Mari_Level1_Activity2.m4a", :size 2, :type "audio" :alias "mari"}

                   {:url "/raw/img/park/see-saw/background.jpg", :size 10 :type "image"}
                   {:url "/raw/img/park/see-saw/saw_01.png", :size 1, :type "image"}
                   {:url "/raw/img/park/see-saw/saw_02.png", :size 1, :type "image"}
                   {:url "/raw/img/park/see-saw/saw_03.png", :size 1, :type "image"}
                   ],
   :objects
                  {:background {:type "background", :src "/raw/img/park/see-saw/background.jpg"},
                   :vera       {:type "animation" :x -380 :y 61 :scene-name "vera" :name "vera-90" :anim "swing" :speed 0.3 :skin "default"
                                :width 727 :height 1091 :scale {:x 0.4 :y 0.4} :start false :meshes true}

                   :mari {:type "animation" :scene-name "mari" :name "mari" :anim "idle"
                          :start true :speed 0.35 :transition "mari"
                          :x 1535 :y 615 :width 473 :height 511
                          :scale-y 0.5 :scale-x 0.5}

                   :see-saw {:type "group" :x 694 :y 716 :children ["see-saw-3" "see-saw-plank" "see-saw-2"]}
                   :see-saw-plank {:type "group" :children ["see-saw-1" "box-ph" "vera"] :rotation -13
                                   :origin {:type "center-center"} :transition "see-saw-plank"}
                   :see-saw-1 {:type "image" :width 889 :height 106
                                :src  "/raw/img/park/see-saw/saw_01.png" :origin {:type "center-center"}},
                   :see-saw-2    {:type "image" :width 195 :height 175 :x 0 :y 67
                                :src  "/raw/img/park/see-saw/saw_02.png" :origin {:type "center-center"}},
                   :see-saw-3    {:type "image" :width 195 :height 179 :x -10 :y 48
                                :src  "/raw/img/park/see-saw/saw_03.png" :origin {:type "center-center"}},

                   :box1 {:type "transparent" :x 500 :y 300 :width 206 :height 210 :origin {:type "center-center"}
                          :scene-name "box1" :draggable {:var-name "drag-box-1"} :transition "box1"
                          :states {:default {:type "transparent"}
                                   :come {:type "animation" :name "boxes" :anim "come"
                                             :scale {:x 0.3 :y 0.3} :speed 0.3 :loop false :start true}
                                   :initial-position {:x 600 :y 400}}
                          :actions {:drag-end {:type "action" :id "drag-box1" :on "drag-end"}}}

                   :box2 {:type "transparent" :x 900 :y 300 :width 206 :height 210 :origin {:type "center-center"}
                          :scene-name "box2" :draggable {:var-name "drag-box-2"} :transition "box2"
                          :states {:default {:type "transparent"}
                                   :come {:type "animation" :name "boxes" :anim "come"
                                             :scale {:x 0.3 :y 0.3} :speed 0.3 :loop false :start true}
                                   :initial-position {:x 1000 :y 400}}
                          :actions {:drag-end {:type "action" :id "drag-box2" :on "drag-end"}}}

                   :box3 {:type "transparent" :x 1300 :y 300 :width 206 :height 210 :origin {:type "center-center"}
                          :scene-name "box3" :draggable {:var-name "drag-box-3"} :transition "box3"
                          :states {:default {:type "transparent"}
                                   :come {:type "animation" :name "boxes" :anim "come"
                                             :scale {:x 0.3 :y 0.3} :speed 0.3 :loop false :start true}
                                   :initial-position {:x 1400 :y 400}}
                          :actions {:drag-end {:type "action" :id "drag-box3" :on "drag-end"}}}

                   :box-ph {:type "transparent" :width 300 :height 300 :x 430 :y -30 :origin {:type "center-center"}
                            :scene-name "box-ph" :transition "box-ph"
                            :states {:default {:type "transparent"}
                                     :visible {:type "animation" :name "boxes" :anim "idle1" :skin "empty"
                                               :scale {:x 0.3 :y 0.3} :speed 0.3 :loop true :start false}}}

                   },
   :scene-objects [["background"] ["see-saw"] ["mari"] ["box1" "box2" "box3"]],
   :actions
                  {:mari-welcome-audio
                   {:type "animation-sequence" :target "mari" :track 1 :offset 0.787
                    :audio "/raw/audio/l1/a2/Mari_Level1_Activity2.m4a", :start 0.787, :duration 9.716
                    :data [{:start 1.062 :end 3.914 :anim "talk"}
                           {:start 4.897 :end 10.167 :anim "talk"}]}

                   :mari-move-to-start
                   {:type "animation-sequence" :target "mari" :track 1 :offset 11.643
                    :audio "/raw/audio/l1/a2/Mari_Level1_Activity2.m4a" :start 11.643, :duration 5.822
                    :data [{:start 11.722 :end 17.268 :anim "talk"}]}

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
                                                       {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}
                                                       {:type "add-animation" :target "box2" :id "idle_fly2" :loop true}
                                                       {:type "add-animation" :target "box3" :id "idle_fly3" :loop true}]}

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

                   :box-1-revert {:type "transition" :transition-id "box1" :to {:x 500 :y 300 :duration 2}}

                   :box-1-start {:type "sequence"
                                 :data ["show-box-1-ph"
                                        "introduce-concept"
                                        "hide-box-1-ph"
                                        "set-current-box2"
                                        "try-another"
                                        "mari-box-2"
                                        "mari-move-to-start"]}

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
                                          {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}]}

                   :box-2-revert {:type "transition" :transition-id "box2" :to {:x 900 :y 300 :duration 2}}

                   :box-2-start {:type "sequence"
                                 :data ["show-box-2-ph"
                                        "introduce-concept"
                                        "hide-box-2-ph"
                                        "set-current-box3"
                                        "try-another"
                                        "mari-box-3"
                                        "mari-move-to-start"]}

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
                                          {:type "add-animation" :target "box2" :id "idle_fly2" :loop true}]}

                   :box-3-revert {:type "transition" :transition-id "box3" :to {:x 1300 :y 400 :duration 2}}

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
                                          {:type "add-animation" :target "box3" :id "idle_fly3" :loop true}]}

                   :voice-low-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "seesaw-voice-low"}]}
                   :voice-high-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "seesaw-voice-high"}]}

                   :word-ardilla-low {:type "audio", :id "voice-1", :start 7.795, :duration 3.911}
                   :word-oso-low {:type "audio", :id "voice-2", :start 5.918, :duration 3.943}
                   :word-incendio-low {:type "audio", :id "voice-3", :start 5.795, :duration 3.903}

                   :word-ardilla-high {:type "audio", :id "voice-1", :start 12.987, :duration 3.196}
                   :word-oso-high {:type "audio", :id "voice-2", :start 11.361, :duration 3.663}
                   :word-incendio-high {:type "audio", :id "voice-3", :start 10.781, :duration 3.301}

                   :introduce-concept {:type "sequence"
                                       :data ["go-down"
                                              "go-up"
                                              "go-down"
                                              "go-up"]}

                   :go-down {:type "parallel"
                             :data [{:type "empty" :duration 5000}
                                    {:type "transition" :transition-id "see-saw-plank" :to {:rotation 13 :duration 5 :loop false}}
                                    {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "seesaw-voice-low"}]}]}

                   :go-up {:type "parallel"
                           :data [{:type "empty" :duration 5000}
                                  {:type "transition" :transition-id "see-saw-plank" :to {:rotation -13 :duration 5 :loop false}}
                                  {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "seesaw-voice-high"}]}]}

                   :try-another
                   {:type "animation-sequence" :target "mari" :track 1 :offset 21.162
                    :audio "/raw/audio/l1/a2/Mari_Level1_Activity2.m4a", :start 21.162, :duration 5.979
                    :data [{:start 21.275 :end 22.367 :anim "talk"}
                           {:start 22.588 :end 23.989 :anim "talk"}
                           {:start 24.747 :end 26.92 :anim "talk"}]}

                   :pick-wrong {:type "sequence"
                                :data ["audio-wrong"]}

                   :audio-wrong {:type "audio" :id "fw-try-again" :start 0.892 :duration 1.869 :offset 0.2}

                   :renew-words  {:type "lesson-var-provider"
                                  :provider-id "words-set"
                                  :variables ["item-1" "item-2" "item-3"]
                                  :from      "concepts"}

                   :mari-box-1 {:type "transition" :transition-id "mari" :to {:x 800 :y 400 :duration 2 :loop false}}
                   :mari-box-2 {:type "transition" :transition-id "mari" :to {:x 1200 :y 400 :duration 2 :loop false}}
                   :mari-box-3 {:type "transition" :transition-id "mari" :to {:x 1600 :y 400 :duration 2 :loop false}}

                   :clear-instruction {:type "remove-flows" :flow-tag "instruction"}
                   :start-activity {:type "sequence"
                                    :data ["clear-instruction"
                                           "renew-words"
                                           "mari-welcome-audio"
                                           "set-current-box1"
                                           "show-boxes"
                                           "wait-for-box-animations"
                                           "switch-box-animations-idle"
                                           "mari-box-1"
                                           "mari-move-to-start"]}

                   :finish-activity {:type "finish-activity" :id "see-saw"}}
   :audio
                  {:voice-1 "/raw/audio/l1/a2/L1_A2_GameVoice_Object 1 List.m4a"
                   :voice-2 "/raw/audio/l1/a2/L1_A2_GameVoice_Object 2 List.m4a"
                   :voice-3 "/raw/audio/l1/a2/L1_A2_GameVoice_Object 3 List.m4a"
                   :mari "/raw/audio/l1/a2/Mari_Level1_Activity2.m4a"},
   :triggers      {:start {:on "start" :action "start-activity"}}
   :metadata      {:autostart true
                   :prev "park"}})