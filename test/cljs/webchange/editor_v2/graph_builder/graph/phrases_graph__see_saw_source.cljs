(ns webchange.editor-v2.graph-builder.graph.phrases-graph--see-saw-source)

(def data {:assets        [{:url "/raw/audio/english/l1/game_voice.m4a", :size 5, :type "audio" :alias "game voice"}
                           {:url "/raw/audio/english/l1/a2/mari.m4a", :size 2, :type "audio" :alias "mari"}
                           {:url "/raw/audio/english/l1/a2/mari_ending.m4a", :size 2, :type "audio" :alias "mari ending"}

                           {:url "/raw/img/park/see-saw/background.jpg", :size 10 :type "image"}
                           {:url "/raw/img/park/see-saw/saw_01.png", :size 1, :type "image"}
                           {:url "/raw/img/park/see-saw/saw_02.png", :size 1, :type "image"}
                           {:url "/raw/img/park/see-saw/saw_03.png", :size 1, :type "image"}],
           :objects       {:background    {:type "background", :src "/raw/img/park/see-saw/background.jpg"},
                           :vera          {:type  "animation" :x -380 :y 61 :scene-name "vera" :name "vera-90" :anim "swing" :speed 0.3 :skin "default"
                                           :width 727 :height 1091 :scale {:x 0.4 :y 0.4} :start false :meshes true}

                           :mari          {:type    "animation" :scene-name "mari" :name "mari" :anim "idle"
                                           :start   true :speed 0.35 :transition "mari"
                                           :x       1535 :y 615 :width 473 :height 511 :anim-offset {:x 0 :y -150}
                                           :scale-y 0.5 :scale-x 0.5}

                           :see-saw       {:type "group" :x 694 :y 716 :children ["see-saw-3" "see-saw-plank" "see-saw-2"]}
                           :see-saw-plank {:type   "group" :children ["see-saw-1" "box-ph" "vera"] :rotation -13
                                           :origin {:type "center-center"} :transition "see-saw-plank"}
                           :see-saw-1     {:type "image" :width 889 :height 106
                                           :src  "/raw/img/park/see-saw/saw_01.png" :origin {:type "center-center"}},
                           :see-saw-2     {:type "image" :width 195 :height 175 :x 0 :y 67
                                           :src  "/raw/img/park/see-saw/saw_02.png" :origin {:type "center-center"}},
                           :see-saw-3     {:type "image" :width 195 :height 179 :x -10 :y 48
                                           :src  "/raw/img/park/see-saw/saw_03.png" :origin {:type "center-center"}},

                           :box1          {:type       "transparent" :x 350 :y 300 :width 771 :height 1033 :origin {:type "center-center"}
                                           :scale      {:x 0.3 :y 0.3} :anim-offset {:x 0 :y -300}
                                           :scene-name "box1" :draggable {:var-name "drag-box-1"} :transition "box1"
                                           :states     {:default {:type "transparent" :x 350 :y 300}
                                                        :come    {:type  "animation" :name "boxes" :anim "come" :x 500 :y 550
                                                                  :speed 0.3 :loop false :start true}}
                                           :actions    {:drag-end {:type "action" :id "drag-box1" :on "drag-end"}}}

                           :box2          {:type       "transparent" :x 850 :y 300 :width 771 :height 1033 :origin {:type "center-center"}
                                           :scale      {:x 0.3 :y 0.3} :anim-offset {:x 0 :y -300}
                                           :scene-name "box2" :draggable {:var-name "drag-box-2"} :transition "box2"
                                           :states     {:default {:type "transparent" :x 850 :y 300}
                                                        :come    {:type  "animation" :name "boxes" :anim "come" :x 1000 :y 550
                                                                  :speed 0.3 :loop false :start true}}
                                           :actions    {:drag-end {:type "action" :id "drag-box2" :on "drag-end"}}}

                           :box3          {:type       "transparent" :x 1350 :y 300 :width 771 :height 1033 :origin {:type "center-center"}
                                           :scale      {:x 0.3 :y 0.3} :anim-offset {:x 0 :y -300}
                                           :scene-name "box3" :draggable {:var-name "drag-box-3"} :transition "box3"
                                           :states     {:default {:type "transparent" :x 1350 :y 300}
                                                        :come    {:type  "animation" :name "boxes" :anim "come" :x 1500 :y 550
                                                                  :speed 0.3 :loop false :start true}}
                                           :actions    {:drag-end {:type "action" :id "drag-box3" :on "drag-end"}}}

                           :box-ph        {:type       "transparent" :width 771 :height 1033 :x 360 :y -30 :origin {:type "center-center"}
                                           :scale      {:x 0.3 :y 0.3} :anim-offset {:x 0 :y -300}
                                           :scene-name "box-ph" :transition "box-ph"
                                           :states     {:default {:type "transparent" :x 360 :y -30}
                                                        :visible {:type  "animation" :name "boxes" :anim "idle1" :skin "default" :x 495 :y 175
                                                                  :speed 0.3 :loop true :start false}}}},
           :scene-objects [["background"] ["see-saw"] ["mari"] ["box1" "box2" "box3"]],
           :actions       {:mari-welcome-audio         {:type        "animation-sequence",
                                                        :target      "mari",
                                                        :track       1,
                                                        :offset      0.64,
                                                        :audio       "/raw/audio/english/l1/a2/mari.m4a",
                                                        :start       0.64,
                                                        :duration    5.876,
                                                        :data        [{:start 0.795, :end 2.767, :duration 1.972, :anim "talk"}
                                                                      {:start 3.137, :end 6.353, :duration 3.216, :anim "talk"}]
                                                        :phrase      :welcome
                                                        :phrase-text "Welcome to the park my friend!
                                                            Let’s review some words while playing on the see saw."}

                           :mari-move-to-start         {:type        "parallel"
                                                        :data        [{:type "sequence-data"
                                                                       :data [{:type "animation" :target "mari" :id "wand_hit" :track 2}
                                                                              {:type "add-animation" :target "mari" :id "wand_idle" :track 2 :loop true}]}
                                                                      {:type     "animation-sequence",
                                                                       :target   "mari",
                                                                       :track    1,
                                                                       :offset   7.283,
                                                                       :audio    "/raw/audio/english/l1/a2/mari.m4a",
                                                                       :start    7.283,
                                                                       :duration 4.555,
                                                                       :data     [{:start 7.388, :end 8.854, :duration 1.466, :anim "talk"}
                                                                                  {:start 9.134, :end 11.561, :duration 2.427, :anim "talk"}]}]
                                                        :phrase      :move-to-start
                                                        :phrase-text "Move this picture to the see saw to start the game!"}

                           :show-boxes                 {:type "parallel"
                                                        :data [{:type "state" :target "box1" :id "come"}
                                                               {:type "state" :target "box2" :id "come"}
                                                               {:type "state" :target "box3" :id "come"}]}

                           :switch-box-animations-idle {:type "parallel"
                                                        :data [{:type     "set-skin" :target "box1"
                                                                :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}
                                                               {:type     "set-skin" :target "box2"
                                                                :from-var [{:var-name "item-2" :action-property "skin" :var-property "skin"}]}
                                                               {:type     "set-skin" :target "box3"
                                                                :from-var [{:var-name "item-3" :action-property "skin" :var-property "skin"}]}
                                                               {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}
                                                               {:type "add-animation" :target "box2" :id "idle_fly2" :loop true}
                                                               {:type "add-animation" :target "box3" :id "idle_fly3" :loop true}]}

                           :wait-for-box-animations    {:type "empty" :duration 500}

                           :set-current-box1           {:type "set-variable" :var-name "box1" :var-value true}
                           :set-current-box2           {:type "set-variable" :var-name "box2" :var-value true}
                           :set-current-box3           {:type "set-variable" :var-name "box3" :var-value true}

                           :check-box1                 {:type     "test-var-scalar"
                                                        :var-name "box1"
                                                        :value    true
                                                        :success  "box-1-start"
                                                        :fail     "box-1-revert"}

                           :check-box2                 {:type     "test-var-scalar"
                                                        :var-name "box2"
                                                        :value    true
                                                        :success  "box-2-start"
                                                        :fail     "box-2-revert"}

                           :check-box3                 {:type     "test-var-scalar"
                                                        :var-name "box3"
                                                        :value    true
                                                        :success  "box-3-start"
                                                        :fail     "box-3-revert"}

                           :drag-box1                  {:type         "test-transitions-collide"
                                                        :transition-1 "box1"
                                                        :transition-2 "box-ph"
                                                        :success      "check-box1"
                                                        :fail         "box-1-revert"}

                           :drag-box2                  {:type         "test-transitions-collide"
                                                        :transition-1 "box2"
                                                        :transition-2 "box-ph"
                                                        :success      "check-box2"
                                                        :fail         "box-2-revert"}

                           :drag-box3                  {:type         "test-transitions-collide"
                                                        :transition-1 "box3"
                                                        :transition-2 "box-ph"
                                                        :success      "check-box3"
                                                        :fail         "box-3-revert"}

                           :box-1-revert               {:type "transition" :transition-id "box1" :to {:x 500 :y 550 :duration 0.5}}

                           :box-1-start                {:type       "sequence"
                                                        :data       ["show-box-1-ph"
                                                                     "introduce-concept"
                                                                     "hide-box-1-ph"
                                                                     "try-another"
                                                                     "mari-box-2"
                                                                     "mari-move-to-start"
                                                                     "set-current-box2"]
                                                        :unique-tag "box"}

                           :show-box-1-ph              {:type "sequence-data"
                                                        :data [{:type "state" :target "box1" :id "default"}
                                                               {:type "state" :target "box-ph" :id "visible"}
                                                               {:type "empty" :duration 500}
                                                               {:type     "set-skin" :target "box-ph"
                                                                :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}
                                                               {:type "copy-variable" :var-name "current-word" :from "item-1"}]}

                           :hide-box-1-ph              {:type "sequence-data"
                                                        :data [{:type "state" :target "box-ph" :id "default"}
                                                               {:type "state" :target "box1" :id "come"}
                                                               {:type "empty" :duration 500}
                                                               {:type     "set-skin" :target "box1"
                                                                :from-var [{:var-name "item-1" :action-property "skin" :var-property "skin"}]}
                                                               {:type "add-animation" :target "box1" :id "idle_fly1" :loop true}]}

                           :box-2-revert               {:type "transition" :transition-id "box2" :to {:x 1000 :y 550 :duration 0.5}}

                           :box-2-start                {:type       "sequence"
                                                        :data       ["show-box-2-ph"
                                                                     "introduce-concept"
                                                                     "hide-box-2-ph"
                                                                     "try-another"
                                                                     "mari-box-3"
                                                                     "mari-move-to-start"
                                                                     "set-current-box3"]
                                                        :unique-tag "box"}

                           :show-box-2-ph              {:type "sequence-data"
                                                        :data [{:type "state" :target "box2" :id "default"}
                                                               {:type "state" :target "box-ph" :id "visible"}
                                                               {:type "empty" :duration 500}
                                                               {:type     "set-skin" :target "box-ph"
                                                                :from-var [{:var-name "item-2" :action-property "skin" :var-property "skin"}]}
                                                               {:type "copy-variable" :var-name "current-word" :from "item-2"}]}

                           :hide-box-2-ph              {:type "sequence-data"
                                                        :data [{:type "state" :target "box-ph" :id "default"}
                                                               {:type "state" :target "box2" :id "come"}
                                                               {:type "empty" :duration 500}
                                                               {:type     "set-skin" :target "box2"
                                                                :from-var [{:var-name "item-2" :action-property "skin" :var-property "skin"}]}
                                                               {:type "add-animation" :target "box2" :id "idle_fly2" :loop true}]}

                           :box-3-revert               {:type "transition" :transition-id "box3" :to {:x 1500 :y 550 :duration 0.5}}

                           :box-3-start                {:type       "sequence"
                                                        :data       ["show-box-3-ph"
                                                                     "introduce-concept"
                                                                     "hide-box-3-ph"
                                                                     "finish-activity"
                                                                     "mari-finish"]
                                                        :unique-tag "box"}

                           :show-box-3-ph              {:type "sequence-data"
                                                        :data [{:type "state" :target "box3" :id "default"}
                                                               {:type "state" :target "box-ph" :id "visible"}
                                                               {:type "empty" :duration 500}
                                                               {:type     "set-skin" :target "box-ph"
                                                                :from-var [{:var-name "item-3" :action-property "skin" :var-property "skin"}]}
                                                               {:type "copy-variable" :var-name "current-word" :from "item-3"}]}

                           :hide-box-3-ph              {:type "sequence"
                                                        :data ["hide-box-3-ph-1"
                                                               "hide-box-3-ph-2"
                                                               "hide-box-3-ph-3"
                                                               "hide-box-3-ph-4"
                                                               "hide-box-3-ph-5"]}
                           :hide-box-3-ph-1            {:type "state" :target "box-ph" :id "default"}
                           :hide-box-3-ph-2            {:type "state" :target "box3" :id "come"}
                           :hide-box-3-ph-3            {:type "empty" :duration 500}
                           :hide-box-3-ph-4            {:type     "set-skin" :target "box3"
                                                        :from-var [{:var-name "item-3" :action-property "skin" :var-property "skin"}]}
                           :hide-box-3-ph-5            {:type "add-animation" :target "box3" :id "idle_fly3" :loop true}

                           :introduce-concept          {:type        "sequence"
                                                        :data        ["go-down-1"
                                                                      "go-up-1"
                                                                      "go-down-2"
                                                                      "go-up-2"]
                                                        :phrase      :concept-chant
                                                        :phrase-text "%Concept%. %Concept%. %CONCEPT%!  (low-pitched tone)
                                                            %Concept%. %Concept%. %CONCEPT%!  (high-pitched tone)
                                                            %Concept%. %Concept%. %CONCEPT%!  (low-pitched tone)
                                                            %Concept%. %Concept%. %CONCEPT%!  (high-pitched tone)"}

                           :go-down-1                  {:type "parallel"
                                                        :data [{:type "empty" :duration 5000}
                                                               {:type "transition" :transition-id "see-saw-plank" :to {:rotation 13 :duration 5 :loop false}}
                                                               {:type "action" :from-var [{:var-name "current-word" :var-property "game-voice-action-low"}]}]}

                           :go-up-1                    {:type "parallel"
                                                        :data [{:type "empty" :duration 5000}
                                                               {:type "transition" :transition-id "see-saw-plank" :to {:rotation -13 :duration 5 :loop false}}
                                                               {:type "action" :from-var [{:var-name "current-word" :var-property "game-voice-action"}]}]}

                           :go-down-2                  {:type "parallel"
                                                        :data [{:type "empty" :duration 5000}
                                                               {:type "transition" :transition-id "see-saw-plank" :to {:rotation 13 :duration 5 :loop false}}
                                                               {:type "action" :from-var [{:var-name "current-word" :var-property "game-voice-action-low"}]}]}

                           :go-up-2                    {:type "parallel"
                                                        :data [{:type "empty" :duration 5000}
                                                               {:type "transition" :transition-id "see-saw-plank" :to {:rotation -13 :duration 5 :loop false}}
                                                               {:type "action" :from-var [{:var-name "current-word" :var-property "game-voice-action"}]}]}

                           :try-another                {:type        "animation-sequence",
                                                        :target      "mari",
                                                        :track       1,
                                                        :offset      12.814,
                                                        :audio       "/raw/audio/english/l1/a2/mari.m4a",
                                                        :start       12.814,
                                                        :duration    5.574,
                                                        :data        [{:start 13.021, :end 14.113, :duration 1.092, :anim "talk"}
                                                                      {:start 14.521, :end 15.682, :duration 1.161, :anim "talk"}
                                                                      {:start 16.28, :end 18.159, :duration 1.879, :anim "talk"}]
                                                        :phrase      :try-another
                                                        :phrase-text "Yippee! Are you having fun? Let’s try another picture."}

                           :renew-words                {:type        "lesson-var-provider"
                                                        :provider-id "words-set"
                                                        :variables   ["item-1" "item-2" "item-3"]
                                                        :from        "concepts"}

                           :mari-box-1                 {:type "transition" :transition-id "mari" :to {:x 671 :y 350 :duration 2 :loop false}}
                           :mari-box-2                 {:type "transition" :transition-id "mari" :to {:x 1181 :y 350 :duration 2 :loop false}}
                           :mari-box-3                 {:type "transition" :transition-id "mari" :to {:x 1658 :y 350 :duration 2 :loop false}}

                           :mari-init-wand             {:type "add-animation" :target "mari" :id "wand_idle" :track 2 :loop true}

                           :clear-instruction          {:type "remove-flows" :flow-tag "instruction"}
                           :start-scene                {:type "sequence"
                                                        :data ["start-activity"
                                                               "clear-instruction"
                                                               "renew-words"
                                                               "mari-welcome-audio"
                                                               "mari-init-wand"
                                                               "show-boxes"
                                                               "wait-for-box-animations"
                                                               "switch-box-animations-idle"
                                                               "mari-box-1"
                                                               "mari-move-to-start"
                                                               "set-current-box1"]}

                           :mari-finish                {:type        "sequence-data",
                                                        :data        [{:type          "transition",
                                                                       :transition-id "mari",
                                                                       :to            {:x 244, :y 300, :duration 1.3}}
                                                                      {:type     "animation-sequence",
                                                                       :target   "mari",
                                                                       :track    1,
                                                                       :offset   0.907,
                                                                       :audio    "/raw/audio/english/l1/a2/mari_ending.m4a",
                                                                       :data     [{:start 1.12, :end 2.708, :duration 1.588, :anim "talk"}
                                                                                  {:start 3.148, :end 6.456, :duration 3.308, :anim "talk"}
                                                                                  {:start 7.163, :end 9.457, :duration 2.294, :anim "talk"}
                                                                                  {:start 9.884, :end 11.871, :duration 1.987, :anim "talk"}
                                                                                  {:start 12.631, :end 16.513, :duration 3.882, :anim "talk"}],
                                                                       :start    0.907,
                                                                       :duration 15.819}]
                                                        :phrase      :finish
                                                        :phrase-text "Congratulations! You helped all of the pictures ride on the see-saw!
                                                            You can play this game again, or, hit this arrow to start your next game."}

                           :start-activity             {:type "start-activity" :id "see-saw"}
                           :finish-activity            {:type "finish-activity" :id "see-saw"}
                           :stop-activity              {:type "stop-activity" :id "see-saw"}}
           :audio         {:voice-1 "/raw/audio/l1/a2/L1_A2_GameVoice_Object_1_List.m4a"
                           :voice-2 "/raw/audio/l1/a2/L1_A2_GameVoice_Object_2_List.m4a"
                           :voice-3 "/raw/audio/l1/a2/L1_A2_GameVoice_Object_3_List.m4a"
                           :mari    "/raw/audio/l1/a2/Mari_Level1_Activity2.m4a"},
           :triggers      {:start {:on "start" :action "start-scene"}
                           :back  {:on "back" :action "stop-activity"}}
           :metadata      {:autostart true
                           :prev      "park"}})

