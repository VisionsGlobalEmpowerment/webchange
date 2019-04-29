(ns webchange.demo-scenes.park.swings)

(def swings-scene
  {:assets
                  [{:url "/raw/audio/l1/a3/vera/ardilla.m4a", :size 2, :type "audio" :alias "vera-ardilla"}
                   {:url "/raw/audio/l1/a3/vera/iman.m4a", :size 2, :type "audio" :alias "vera-iman"}
                   {:url "/raw/audio/l1/a3/vera/oso.m4a", :size 2, :type "audio" :alias "vera-oso"}
                   {:url "/raw/audio/l1/a3/rock/Ardilla.m4a", :size 2, :type "audio" :alias "rock-ardilla"}
                   {:url "/raw/audio/l1/a3/rock/Iman.m4a", :size 2, :type "audio" :alias "rock-iman"}
                   {:url "/raw/audio/l1/a3/rock/Oso.m4a", :size 2, :type "audio" :alias "rock-oso"}
                   {:url "/raw/audio/l1/a3/Mari_Level1_Activity3.m4a", :size 2, :type "audio" :alias "mari-welcome"}

                   {:url "/raw/img/park/swings/background.jpg", :size 10 :type "image"}
                   {:url "/raw/img/park/swings/swings.png", :size 1, :type "image"}
                   {:url "/raw/img/park/swings/tree.png", :size 1, :type "image"}

                   ],
   :objects
                  {:background {:type "background", :src "/raw/img/park/swings/background.jpg"},
                   :vera       {:type "animation" :x 251 :y 990 :name "vera" :anim "idle" :speed 0.3
                                :width 1800 :height 2558 :scale {:x 0.2 :y 0.2} :start true}

                   :mari {:type "animation" :scene-name "mari" :name "mari" :anim "idle"
                          :start true :speed 0.35 :transition "mari"
                          :x 1535 :y 715 :width 473 :height 511
                          :scale-y 0.5 :scale-x 0.5}

                   :rock {:type "animation" :scene-name "rock" :name "rock" :anim "idle"
                          :start true :speed 0.35
                          :x 1168 :y 748 :width 591 :height 681
                          :scale-y 0.6 :scale-x 0.6}

                   :swings {:type "group" :x 589 :y 160 :children ["swings-image" "box-ph"] :transition "swings"
                            :origin {:type "center-center"}}
                   :swings-image {:type "image" :width 248 :height 681
                               :src  "/raw/img/park/swings/swings.png" :origin {:type "center-top"}},

                   :tree-image {:type "image" :width 592 :height 196 :x 656 :y 0
                                :src  "/raw/img/park/swings/tree.png" :origin {:type "center-top"}},

                   :box1 {:type "transparent" :x 500 :y 300 :width 206 :height 210 :origin {:type "center-center"}
                          :scene-name "box1" :draggable {:var-name "drag-box-1"} :transition "box1"
                          :states {:default {:type "transparent"}
                                   :come {:type "animation" :name "boxes" :anim "come"
                                             :scale {:x 0.3 :y 0.3} :speed 0.3 :loop false :start true
                                             :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}
                                   :initial-position {:x 600 :y 400}}
                          :actions {:drag-end {:type "action" :id "drag-box1" :on "drag-end"}}}

                   :box2 {:type "transparent" :x 900 :y 300 :width 206 :height 210 :origin {:type "center-center"}
                          :scene-name "box2" :draggable {:var-name "drag-box-2"} :transition "box2"
                          :states {:default {:type "transparent"}
                                   :come {:type "animation" :name "boxes" :anim "come"
                                             :from-var [{:var-name "item-2" :action-property "skin" :var-property "skin"}]
                                             :scale {:x 0.3 :y 0.3} :speed 0.3 :loop false :start true}
                                   :initial-position {:x 1000 :y 400}}
                          :actions {:drag-end {:type "action" :id "drag-box2" :on "drag-end"}}}

                   :box3 {:type "transparent" :x 1300 :y 300 :width 206 :height 210 :origin {:type "center-center"}
                          :scene-name "box3" :draggable {:var-name "drag-box-3"} :transition "box3"
                          :states {:default {:type "transparent"}
                                   :come {:type "animation" :name "boxes" :anim "come"
                                             :from-var [{:var-name "item-3" :action-property "skin" :var-property "skin"}]
                                             :scale {:x 0.3 :y 0.3} :speed 0.3 :loop false :start true}
                                   :initial-position {:x 1400 :y 400}}
                          :actions {:drag-end {:type "action" :id "drag-box3" :on "drag-end"}}}

                   :box-ph {:type "transparent" :width 300 :height 300 :x 113 :y 588 :origin {:type "center-center"}
                            :scene-name "box-ph" :transition "box-ph"
                            :states {:default {:type "transparent"}
                                     :visible {:type "animation" :name "boxes" :anim "idle2" :skin "empty"
                                               :scale {:x 0.3 :y 0.3} :speed 0.3 :loop true :start false}}}

                   },
   :scene-objects [["background"] ["swings"] ["tree-image"] ["vera" "mari" "rock"] ["box1" "box2" "box3"]],
   :actions
                  {:mari-welcome-audio-1
                   {:type "animation-sequence" :target "mari" :track 1 :offset 1.047
                    :audio "/raw/audio/l1/a3/Mari_Level1_Activity3.m4a", :start 1.047, :duration 11.568
                    :data [{:start 1.285 :end 2.142 :anim "talk"}
                           {:start 2.523 :end 4.713 :anim "talk"}
                           {:start 5.248 :end 9.223 :anim "talk"}
                           {:start 9.723 :end 12.294 :anim "talk"}]}

                   :mari-move-to-start
                   {:type "animation-sequence" :target "mari" :track 1 :offset 34.395
                            :audio "/raw/audio/l1/a3/Mari_Level1_Activity3.m4a", :start 34.395, :duration 2.773
                            :data [{:start 34.549 :end 36.894 :anim "talk"}]}

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
                                        "start-swings"
                                        "empty-before-dialog"
                                        "dialog-var"
                                        "stop-swings"
                                        "hide-box-1-ph"
                                        "set-current-box2"
                                        "mari-box-2"
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
                                          {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}]}

                   :box-2-revert {:type "transition" :transition-id "box2" :to {:x 900 :y 300 :duration 2}}

                   :box-2-start {:type "sequence"
                                 :data ["show-box-2-ph"
                                        "start-swings"
                                        "empty-before-dialog"
                                        "dialog-var"
                                        "stop-swings"
                                        "hide-box-2-ph"
                                        "set-current-box3"
                                        "mari-box-3"
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
                                          {:type "add-animation" :target "box2" :id "idle_fly2" :loop true}]}

                   :box-3-revert {:type "transition" :transition-id "box3" :to {:x 1300 :y 300 :duration 2}}

                   :box-3-start {:type "sequence"
                                 :data ["show-box-3-ph"
                                        "start-swings"
                                        "empty-before-dialog"
                                        "dialog-var"
                                        "stop-swings"
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

                   :dialog-var {:type "action" :from-var [{:var-name "current-word" :action-property "id" :var-property "swings-dialog"}]}

                   :dialog-ardilla
                   {:type "sequence-data"
                    :data [{:type "animation-sequence" :target "rock" :track 1 :offset 0.68
                                    :audio "/raw/audio/l1/a3/rock/Ardilla.m4a", :start 0.68, :duration 6.4
                                    :data [{:start 1.172 :end 1.676 :anim "talk"}
                                           {:start 2.204 :end 3.604 :anim "talk"}
                                           {:start 4.044 :end 6.629 :anim "talk"}]}
                           {:type "animation-sequence" :target "vera" :track 1 :offset 0.328
                                    :audio "/raw/audio/l1/a3/vera/ardilla.m4a", :start 0.328, :duration 1.835
                                    :data [{:start 0.447 :end 2.016 :anim "talk"}]}
                           {:type "animation-sequence" :target "rock" :track 1 :offset 10.356
                                    :audio "/raw/audio/l1/a3/rock/Ardilla.m4a", :start 10.356, :duration 6.166
                                    :data [{:start 10.684 :end 11.159 :anim "talk"}
                                           {:start 11.78 :end 16.264 :anim "talk"}]}
                           {:type "animation-sequence" :target "vera" :track 1 :offset 3.667
                                    :audio "/raw/audio/l1/a3/vera/ardilla.m4a", :start 3.667, :duration 2.966
                                    :data [{:start 3.822 :end 4.22 :anim "talk"}
                                           {:start 5.109 :end 6.309 :anim "talk"}]}
                           {:type "animation-sequence" :target "rock" :track 1 :offset 17.847
                                    :audio "/raw/audio/l1/a3/rock/Ardilla.m4a", :start 17.847, :duration 5.339
                                    :data [{:start 18.011 :end 19.013 :anim "talk"}
                                           {:start 19.47 :end 20.285 :anim "talk"}
                                           {:start 20.871 :end 22.963 :anim "talk"}]}
                           {:type "animation-sequence" :target "vera" :track 1 :offset 7.8
                                    :audio "/raw/audio/l1/a3/vera/ardilla.m4a", :start 7.8, :duration 8.923
                                    :data [{:start 7.927 :end 8.493 :anim "talk"}
                                           {:start 9.365 :end 9.922 :anim "talk"}
                                           {:start 11.049 :end 12.413 :anim "talk"}
                                           {:start 13.29 :end 16.547 :anim "talk"}]}
                           {:type "animation-sequence" :target "rock" :track 1 :offset 24.176
                                    :audio "/raw/audio/l1/a3/rock/Ardilla.m4a", :start 24.176, :duration 9.073
                                    :data [{:start 24.282 :end 24.915 :anim "talk"}
                                           {:start 25.325 :end 28.426 :anim "talk"}
                                           {:start 28.924 :end 32.997 :anim "talk"}]}
                           {:type "animation-sequence" :target "vera" :track 1 :offset 18.21
                                    :audio "/raw/audio/l1/a3/vera/ardilla.m4a", :start 18.21, :duration 9.603
                                    :data [{:start 18.251 :end 18.792 :anim "talk"}
                                           {:start 19.378 :end 21.242 :anim "talk"}
                                           {:start 22.2 :end 24.675 :anim "talk"}
                                           {:start 25.592 :end 27.567 :anim "talk"}]}
                           {:type "animation-sequence" :target "rock" :track 1 :offset 34.2
                                    :audio "/raw/audio/l1/a3/rock/Ardilla.m4a", :start 34.2, :duration 6.482
                                    :data [{:start 34.474 :end 35.81 :anim "talk"}
                                           {:start 36.226 :end 40.423 :anim "talk"}]}
                           ]}

                   :dialog-oso
                   {:type "sequence-data"
                    :data [{:type "animation-sequence" :target "rock" :track 1 :offset 0.481
                                    :audio "/raw/audio/l1/a3/rock/Oso.m4a", :start 0.481, :duration 5.775
                                    :data [{:start 0.605 :end 2.887 :anim "talk"}
                                           {:start 3.504 :end 5.726 :anim "talk"}]}
                           {:type "animation-sequence" :target "vera" :track 1 :offset 0.924
                                    :audio "/raw/audio/l1/a3/vera/oso.m4a", :start 0.924, :duration 5.258
                                    :data [{:start 1.071 :end 5.972 :anim "talk"}]}
                           {:type "animation-sequence" :target "rock" :track 1 :offset 6.54
                                    :audio "/raw/audio/l1/a3/rock/Oso.m4a", :start 6.54, :duration 3.936
                                    :data [{:start 6.781 :end 7.558 :anim "talk"}
                                           {:start 7.928 :end 10.365 :anim "talk"}]}
                           {:type "animation-sequence" :target "vera" :track 1 :offset 6.92
                                    :audio "/raw/audio/l1/a3/vera/oso.m4a", :start 6.92, :duration 5.036
                                    :data [{:start 7.068 :end 9.691 :anim "talk"}
                                           {:start 10.159 :end 11.587 :anim "talk"}]}
                           {:type "animation-sequence" :target "rock" :track 1 :offset 11.013
                                    :audio "/raw/audio/l1/a3/rock/Oso.m4a", :start 11.013, :duration 9.933
                                    :data [{:start 11.05 :end 13.629 :anim "talk"}
                                           {:start 14.061 :end 20.761 :anim "talk"}]}
                           {:type "animation-sequence" :target "vera" :track 1 :offset 12.744
                                    :audio "/raw/audio/l1/a3/vera/oso.m4a", :start 12.744, :duration 8.41
                                    :data [{:start 12.941 :end 13.692 :anim "talk"}
                                           {:start 14.222 :end 14.912 :anim "talk"}
                                           {:start 15.589 :end 16.906 :anim "talk"}
                                           {:start 17.374 :end 18.655 :anim "talk"}
                                           {:start 19.344 :end 20.871 :anim "talk"}]}

                           ]}

                   :dialog-incendio
                   {:type "sequence-data"
                    :data [{:type "animation-sequence" :target "rock" :track 1 :offset 0.573
                                    :audio "/raw/audio/l1/a3/rock/Iman.m4a", :start 0.573, :duration 4.29
                                    :data [{:start 0.745 :end 1.596 :anim "talk"}
                                           {:start 2.284 :end 2.685 :anim "talk"}
                                           {:start 2.964 :end 4.577 :anim "talk"}]}
                           {:type "animation-sequence" :target "vera" :track 1 :offset 0.411
                                    :audio "/raw/audio/l1/a3/vera/iman.m4a", :start 0.411, :duration 4.493
                                    :data [{:start 0.605 :end 1.108 :anim "talk"}
                                           {:start 1.537 :end 2.687 :anim "talk"}
                                           {:start 3.384 :end 4.593 :anim "talk"}]}
                           {:type "animation-sequence" :target "rock" :track 1 :offset 5.207
                                    :audio "/raw/audio/l1/a3/rock/Iman.m4a", :start 5.207, :duration 10.93
                                    :data [{:start 5.305 :end 5.796 :anim "talk"}
                                           {:start 6.263 :end 10.348 :anim "talk"}
                                           {:start 10.921 :end 12.027 :anim "talk"}
                                           {:start 12.624 :end 16.036 :anim "talk"}]}
                           {:type "animation-sequence" :target "vera" :track 1 :offset 6.264
                                    :audio "/raw/audio/l1/a3/vera/iman.m4a", :start 6.264, :duration 2.746
                                    :data [{:start 6.424 :end 8.826 :anim "talk"}]}
                           {:type "animation-sequence" :target "rock" :track 1 :offset 16.767
                                    :audio "/raw/audio/l1/a3/rock/Iman.m4a", :start 16.767, :duration 11.282
                                    :data [{:start 16.865 :end 19.067 :anim "talk"}
                                           {:start 19.542 :end 22.465 :anim "talk"}
                                           {:start 22.956 :end 27.713 :anim "talk"}]}
                           {:type "animation-sequence" :target "vera" :track 1 :offset 10.161
                                    :audio "/raw/audio/l1/a3/vera/iman.m4a", :start 10.161, :duration 3.964
                                    :data [{:start 10.287 :end 11.009 :anim "talk"}
                                           {:start 11.387 :end 12.143 :anim "talk"}
                                           {:start 12.604 :end 13.839 :anim "talk"}]}
                           ]}

                   :stop-swings {:type "sequence-data"
                                  :data [{:type "remove-flows" :flow-tag "swings"}
                                         {:type "transition" :transition-id "swings" :to {:rotation 0 :duration 0.5 :loop false}}]}

                   :start-swings {:type "action" :id "start-swings-do" :return-immediately true}

                   :start-swings-do {:type "sequence"
                                     :tags ["swings"]
                                     :data ["prepare-swing" "swing"]}

                   :swing {:type "sequence"
                           :tags ["swings"]
                           :data ["swing-right"
                                  "swing-left"
                                  "swing"]}

                   :empty-before-dialog {:type "empty" :duration 3000}

                   :prepare-swing  {:type "transition" :transition-id "swings" :to {:rotation -15 :duration 1 :loop false}}
                   :swing-right  {:type "transition" :transition-id "swings" :to {:rotation 15 :duration 2 :loop false}}
                   :swing-left  {:type "transition" :transition-id "swings" :to {:rotation -15 :duration 2 :loop false}}

                   :try-another
                   {:type "animation-sequence" :target "mari" :track 1 :offset 40.476
                            :audio "/raw/audio/l1/a3/Mari_Level1_Activity3.m4a", :start 40.476, :duration 8.509
                            :data [{:start 41.155 :end 43.225 :anim "talk"}
                                   {:start 43.725 :end 45.534 :anim "talk"}
                                   {:start 46.32 :end 48.759 :anim "talk"}]}

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
                                           "set-current-box1"
                                           "show-boxes"
                                           "wait-for-box-animations"
                                           "switch-box-animations-idle"
                                           "mari-welcome-audio-1"
                                           ;"vera-welcome-audio-1"
                                           ;"mari-welcome-audio-2"
                                           ;"vera-welcome-audio-2"
                                           ;"rock-welcome-audio-1"
                                           ;"mari-welcome-audio-3"
                                           ;"rock-welcome-audio-2"
                                           ;"vera-welcome-audio-3"
                                           ;"rock-welcome-audio-3"
                                           ;"mari-welcome-audio-4"
                                           "mari-box-1"
                                           "mari-move-to-start"
                                           ]}

                   :finish-activity {:type "finish-activity" :id "swings"}}
   :audio
                  {:mari-welcome "/raw/audio/l1/a3/Mari_Level1_Activity3.m4a"
                   :vera-ardilla "/raw/audio/l1/a3/vera/ardilla.m4a"
                   :vera-oso "/raw/audio/l1/a3/vera/oso.m4a"
                   :vera-iman "/raw/audio/l1/a3/vera/iman.m4a"
                   :rock-ardilla "/raw/audio/l1/a3/rock/Ardilla.m4a"
                   :rock-oso "/raw/audio/l1/a3/rock/Oso.m4a"
                   :rock-iman "/raw/audio/l1/a3/rock/Iman.m4a"}

   :triggers      {:start {:on "start" :action "start-activity"}}
   :metadata      {:autostart true
                   :prev "park"}})