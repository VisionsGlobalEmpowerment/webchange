(ns webchange.demo-scenes.stadium.volleyball)

(def volleyball-scene
  {:assets
                  [{:url "/raw/audio/l1/a3/vera/ardilla.m4a", :size 2, :type "audio"}
                   {:url "/raw/audio/l1/a3/vera/iman.m4a", :size 2, :type "audio"}
                   {:url "/raw/audio/l1/a3/vera/oso.m4a", :size 2, :type "audio"}
                   {:url "/raw/audio/l1/a3/rock/Ardilla.m4a", :size 2, :type "audio"}
                   {:url "/raw/audio/l1/a3/rock/Iman.m4a", :size 2, :type "audio"}
                   {:url "/raw/audio/l1/a3/rock/Oso.m4a", :size 2, :type "audio"}
                   {:url "/raw/audio/l1/a3/Mari_Level1_Activity3.m4a", :size 2, :type "audio"}

                   {:url "/raw/img/stadium/volleyball/background.jpg", :size 10 :type "image"}
                   {:url "/raw/img/stadium/volleyball/ball.png", :size 1 :type "image"}

                   ],
   :objects
                  {:background {:type "background", :src "/raw/img/stadium/volleyball/background.jpg"},
                   :vera       {:type "animation" :x 1182 :y 1010 :name "vera-45" :anim "volley_idle" :speed 0.3
                                :width 758 :height 1130 :scale {:x 0.25 :y 0.25} :start true
                                :scene-name "vera" :skin "vera"}

                   :boy1       {:type "animation" :x 1556 :y 871 :name "vera-45" :anim "idle" :speed 0.3
                                :width 758 :height 1130 :scale {:x 0.25 :y 0.25} :start true
                                :scene-name "boy1" :skin "1_boy"}

                   :girl2       {:type "animation" :x 197 :y 1010 :name "vera-45" :anim "idle" :speed 0.3
                                 :width 758 :height 1130 :scale {:x -0.25 :y 0.25} :start true
                                 :scene-name "girl2" :skin "2_girl"}

                   :girl3       {:type "animation" :x 512 :y 871 :name "vera-45" :anim "idle" :speed 0.3
                                 :width 758 :height 1130 :scale {:x -0.25 :y 0.25} :start true
                                 :scene-name "girl3" :skin "3_girl"}


                   :mari {:type "animation" :scene-name "mari" :name "mari" :anim "idle"
                          :start true :speed 0.35 :transition "mari"
                          :x 1265 :y 311 :width 473 :height 511 :anim-offset {:x 0 :y -150}
                          :scale-y 0.5 :scale-x 0.5}

                   :box1 {:type "animation" :x 1304 :y 831 :width 771 :height 1033 :origin {:type "center-center"} :skin "empty"
                          :scene-name "box1" :name "boxes" :anim "idle1" :scale {:x 0.3 :y 0.3} :anim-offset {:x 0 :y -300}
                          :actions {:click {:type "action", :id "check-concept" :params {:player "player1"}, :on "click"}}}

                   :box2 {:type "animation" :x 1665 :y 666 :width 771 :height 1033 :origin {:type "center-center"} :skin "empty"
                          :scene-name "box2" :name "boxes" :anim "idle1" :scale {:x 0.3 :y 0.3} :anim-offset {:x 0 :y -300}
                          :actions {:click {:type "action", :id "check-concept" :params {:player "player2"}, :on "click"}}}

                   :box3 {:type "animation" :x 309 :y 831 :width 771 :height 1033 :origin {:type "center-center"} :skin "empty"
                          :scene-name "box3" :name "boxes" :anim "idle1" :scale {:x 0.3 :y 0.3} :anim-offset {:x 0 :y -300}
                          :actions {:click {:type "action", :id "check-concept" :params {:player "player3"}, :on "click"}}}

                   :box4 {:type "animation" :x 629 :y 666 :width 771 :height 1033 :origin {:type "center-center"} :skin "empty"
                          :scene-name "box4" :name "boxes" :anim "idle1" :scale {:x 0.3 :y 0.3} :anim-offset {:x 0 :y -300}
                          :actions {:click {:type "action", :id "check-concept" :params {:player "player4"}, :on "click"}}}

                   :ball {:type "group" :x 1163 :y 941 :width 126 :height 126
                          :children ["ball-image"] :transition "ball-transition"}
                   :ball-image {:type "transparent" :width 126 :height 126 :origin {:type "center-center"}
                          :scale-x 0.5 :scale-y 0.5
                          :states {:hidden {:type "transparent"}
                                   :visible {:type "image" :src  "/raw/img/stadium/volleyball/ball.png"}}}

                   },
   :scene-objects [["background"] ["vera" "boy1" "girl2" "girl3"] ["ball"] ["box1" "box2" "box3" "box4"] ["mari"]],
   :actions
                  {:mari-welcome-audio-1 {:type "parallel"
                                          :data [{:type "audio", :id "mari-welcome", :start 1.047, :duration 11.568}
                                                 {:type "animation-sequence" :target "mari" :track 1 :offset 1.047
                                                  :data [{:start 1.285 :end 2.142 :anim "talk"}
                                                         {:start 2.523 :end 4.713 :anim "talk"}
                                                         {:start 5.248 :end 9.223 :anim "talk"}
                                                         {:start 9.723 :end 12.294 :anim "talk"}]}]}

                   :wait-for-box-animations {:type "empty" :duration 500}

                   :empty-before-dialog {:type "empty" :duration 3000}


                   :try-another
                                               {:type "parallel"
                                                :data [{:type "audio", :id "mari-welcome", :start 40.476, :duration 8.509}
                                                       {:type "animation-sequence" :target "mari" :track 1 :offset 40.476
                                                        :data [{:start 41.155 :end 43.225 :anim "talk"}
                                                               {:start 43.725 :end 45.534 :anim "talk"}
                                                               {:start 46.32 :end 48.759 :anim "talk"}]}]}

                   :pick-wrong {:type "sequence"
                                :data ["audio-wrong"]}

                   :audio-wrong {:type "audio" :id "fw-try-again" :start 0.892 :duration 1.869 :offset 0.2}

                   :renew-words  {:type "lesson-var-provider"
                                  :provider-id "words-set"
                                  :variables ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8" "item-9" "item-10"]
                                  :shuffled true
                                  :limit 3
                                  :repeat 4
                                  :from "concepts"}

                   :renew-current-concept {:type "sequence-data"
                                           :data [{:type "vars-var-provider"
                                                   :provider-id "current-concept"
                                                   :variables ["current-concept"]
                                                   :from ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8" "item-9" "item-10"]
                                                   :shuffled false
                                                   :on-end "finish-activity"}
                                                  {:type "sequence-data"
                                                   :data [{:type "parallel"
                                                           :data [{:type "set-variable" :var-name "player1"
                                                                   :from-var [{:var-name "current-concept" :action-property "var-value" :var-property "word-1-skin"}]}
                                                                  {:type "set-variable" :var-name "player2"
                                                                   :from-var [{:var-name "current-concept" :action-property "var-value" :var-property "word-2-skin"}]}
                                                                  {:type "set-variable" :var-name "player3"
                                                                   :from-var [{:var-name "current-concept" :action-property "var-value" :var-property "word-3-skin"}]}
                                                                  {:type "set-variable" :var-name "player4"
                                                                   :from-var [{:var-name "current-concept" :action-property "var-value" :var-property "word-4-skin"}]}]}
                                                          {:type "vars-var-provider"
                                                           :variables ["player1" "player2" "player3" "player4"]
                                                           :from ["player1" "player2" "player3" "player4"]
                                                           :shuffled true}
                                                          ]}
                                                  {:type "sequence-data"
                                                   :data [{:type "vars-var-provider"
                                                           :variables ["current-target"]
                                                           :from ["slot1" "slot2" "slot3" "slot4"]
                                                           :shuffled true
                                                           :from-var [{:var-name "current-player" :action-property "exclude-values" :to-vector true}]}
                                                          {:type "set-variable"
                                                           :from-var [{:var-name "current-concept" :action-property "var-value" :var-property "concept-skin"}
                                                                      {:var-name "current-target" :action-property "var-name"}]}]}
                                                  {:type "parallel"
                                                   :data [{:type "set-skin" :target "box1"
                                                           :from-var [{:var-name "player1" :action-property "skin"}]}
                                                          {:type "set-skin" :target "box2"
                                                           :from-var [{:var-name "player2" :action-property "skin"}]}
                                                          {:type "set-skin" :target "box3"
                                                           :from-var [{:var-name "player3" :action-property "skin"}]}
                                                          {:type "set-skin" :target "box4"
                                                           :from-var [{:var-name "player4" :action-property "skin"}]}]}
                                                  ]}

                   :init-slots {:type "parallel"
                                :data [{:type "set-variable" :var-name "slot1" :var-value "player1"}
                                       {:type "set-variable" :var-name "slot2" :var-value "player2"}
                                       {:type "set-variable" :var-name "slot3" :var-value "player3"}
                                       {:type "set-variable" :var-name "slot4" :var-value "player4"}]}

                   :init-boxes {:type "sequence-data"
                                :data [{:type "parallel"
                                        :data [{:type "set-variable" :var-name "player1"
                                                :from-var [{:var-name "current-concept" :action-property "var-value" :var-property "sandbox-state-word-1"}]}
                                               {:type "set-variable" :var-name "player2"
                                                :from-var [{:var-name "current-concept" :action-property "var-value" :var-property "sandbox-state-word-2"}]}
                                               {:type "set-variable" :var-name "player3"
                                                :from-var [{:var-name "current-concept" :action-property "var-value" :var-property "sandbox-state-word-3"}]}
                                               {:type "set-variable" :var-name "player4"
                                                :from-var [{:var-name "current-concept" :action-property "var-value" :var-property "sandbox-state-word-4"}]}]}
                                       {:type "vars-var-provider"
                                        :variables ["player1" "player2" "player3" "player4"]
                                        :from ["player1" "player2" "player3" "player4"]
                                        :shuffled true}
                                       ]}

                   :init-current-target {:type "sequence-data"
                                         :data [{:type "vars-var-provider"
                                                 :variables ["current-target"]
                                                 :from ["slot1" "slot2" "slot3" "slot4"]
                                                 :shuffled true}
                                                {:type "set-variable"
                                                 :from-var [{:var-name "current-concept" :action-property "var-value" :var-property "concept-name"}
                                                            {:var-name "current-target" :action-property "var-name"}]}]}

                   :init-first-player {:type "set-variable" :var-name "current-player" :var-value "player1"}

                   :clear-instruction {:type "remove-flows" :flow-tag "instruction"}
                   :start-activity {:type "sequence"
                                    :data ["clear-instruction"
                                           "init-slots"
                                           "init-first-player"
                                           "renew-words"
                                           "renew-current-concept"
                                           ]}

                   :check-concept {:type "test-var-scalar"
                                   :var-name "current-target"
                                   :success "throw"
                                   :fail "pick-wrong"
                                   :from-params [{:action-property "value" :param-property "player"}]}

                   :throw {:type "sequence-data"
                           :data [{:type "action"
                                   :from-var [{:template "throw-from-%" :action-property "id" :var-name "current-player"}]}
                                  {:type "action"
                                   :from-params [{:template "throw-to-%" :action-property "id" :param-property "player"}]}
                                  {:type "set-variable" :var-name "current-player"
                                   :from-params [{:action-property "var-value" :param-property "player"}]}
                                  {:type "action" :id "renew-current-concept"}]}

                   :throw-from-player1 {:type "parallel"
                                     :data [{:type "state" :target "ball-image" :id "visible"}
                                            {:type "sequence-data"
                                             :data [{:type "animation" :target "vera" :id "volley_throw"}
                                                    {:type "add-animation" :target "vera" :id "idle" :loop true}]}
                                            {:type "sequence-data"
                                             :data [{:type "empty" :duration 300}
                                                    {:type "test-var-scalar"
                                                     :var-name "current-target"
                                                     :value "player2"
                                                     :success {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 1272 :y 487} {:x 1376 :y 451}] :duration 0.7}}
                                                     :fail {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 1066 :y 284} {:x 960 :y 253}] :duration 0.7}}
                                                     }]}]}

                   :throw-from-player2 {:type "parallel"
                                     :data [{:type "state" :target "ball-image" :id "visible"}
                                            {:type "sequence-data"
                                             :data [{:type "animation" :target "boy1" :id "volley_throw"}
                                                    {:type "add-animation" :target "boy1" :id "idle" :loop true}]}
                                            {:type "sequence-data"
                                             :data [{:type "empty" :duration 300}
                                                    {:type "test-var-scalar"
                                                     :var-name "current-target"
                                                     :value "player1"
                                                     :success {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 1472 :y 487} {:x 1376 :y 451}] :duration 0.7}}
                                                     :fail {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 1066 :y 284} {:x 960 :y 253}] :duration 0.7}}}]}]}

                   :throw-from-player3 {:type "parallel"
                                     :data [{:type "state" :target "ball-image" :id "visible"}
                                            {:type "sequence-data"
                                             :data [{:type "animation" :target "girl2" :id "volley_throw"}
                                                    {:type "add-animation" :target "girl2" :id "idle" :loop true}]}
                                            {:type "sequence-data"
                                             :data [{:type "empty" :duration 300}
                                                    {:type "test-var-scalar"
                                                     :var-name "current-target"
                                                     :value "player4"
                                                     :success {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 246 :y 551} {:x 346 :y 451}] :duration 0.7}}
                                                     :fail {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 866 :y 284} {:x 960 :y 253}] :duration 0.7}}}]}]}

                   :throw-from-player4 {:type "parallel"
                                     :data [{:type "state" :target "ball-image" :id "visible"}
                                            {:type "sequence-data"
                                             :data [{:type "animation" :target "girl3" :id "volley_throw"}
                                                    {:type "add-animation" :target "girl3" :id "idle" :loop true}]}
                                            {:type "sequence-data"
                                             :data [{:type "empty" :duration 300}
                                                    {:type "test-var-scalar"
                                                     :var-name "current-target"
                                                     :value "player3"
                                                     :success {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 446 :y 551} {:x 346 :y 451}] :duration 0.7}}
                                                     :fail {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 866 :y 284} {:x 960 :y 253}] :duration 0.7}}}]}]}

                   :throw-to-player1 {:type "parallel"
                                      :data [{:type "sequence-data"
                                              :data [{:type "empty" :duration 100}
                                                     {:type "animation" :target "vera" :id "volley_call"}
                                                     {:type "add-animation" :target "vera" :id "volley_idle" :loop true}]}
                                             {:type "sequence-data"
                                              :data [{:type "transition" :transition-id "ball-transition" :to {:x 1163 :y 941 :duration 0.7 :loop false}}
                                                     {:type "state" :target "ball-image" :id "hidden"}]}]}

                   :throw-to-player2 {:type "parallel"
                                      :data [{:type "sequence-data"
                                              :data [{:type "empty" :duration 100}
                                                     {:type "animation" :target "boy1" :id "volley_call"}
                                                     {:type "add-animation" :target "boy1" :id "volley_idle" :loop true}]}
                                             {:type "sequence-data"
                                              :data [{:type "transition" :transition-id "ball-transition" :to {:x 1538 :y 801 :duration 0.7 :loop false}}
                                                     {:type "state" :target "ball-image" :id "hidden"}]}]}

                   :throw-to-player3 {:type "parallel"
                                     :data [{:type "sequence-data"
                                             :data [{:type "empty" :duration 100}
                                                    {:type "animation" :target "girl2" :id "volley_call"}
                                                    {:type "add-animation" :target "girl2" :id "volley_idle" :loop true}]}
                                            {:type "sequence-data"
                                             :data [{:type "transition" :transition-id "ball-transition" :to {:x 213 :y 940 :duration 0.7 :loop false}}
                                                    {:type "state" :target "ball-image" :id "hidden"}]}]}

                   :throw-to-player4 {:type "parallel"
                                    :data [{:type "sequence-data"
                                            :data [{:type "empty" :duration 100}
                                                   {:type "animation" :target "girl3" :id "volley_call"}
                                                   {:type "add-animation" :target "girl3" :id "volley_idle" :loop true}]}
                                           {:type "sequence-data"
                                            :data [{:type "transition" :transition-id "ball-transition" :to {:x 528 :y 799 :duration 0.7 :loop false}}
                                                   {:type "state" :target "ball-image" :id "hidden"}]}]}

                   :finish-activity {:type "finish-activity" :id "volleyball"}}
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