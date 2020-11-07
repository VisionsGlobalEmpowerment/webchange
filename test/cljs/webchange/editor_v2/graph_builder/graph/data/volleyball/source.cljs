(ns webchange.editor-v2.graph-builder.graph.data.volleyball.source)

(def data {:assets        [{:url "/raw/audio/l1/a5/Mari_Coaching_statements.m4a", :size 2, :type "audio" :alias "statements" :target "mari"}
                           {:url "/raw/audio/l1/a5/Mari_Level1_Activity5.m4a", :size 2, :type "audio" :alias "mari" :target "mari"}
                           {:url "/raw/audio/l1/a3/Mari_Level1_Activity3.m4a", :size 2, :type "audio" :alias "mari" :target "mari"}

                           {:url "/raw/audio/l1/a2/L1_A2_GameVoice_Object_1_List.m4a", :size 5, :type "audio" :alias "game voice 1" :target "game-voice"}
                           {:url "/raw/audio/l1/a2/L1_A2_GameVoice_Object_2_List.m4a", :size 5, :type "audio" :alias "game voice 2" :target "game-voice"}
                           {:url "/raw/audio/l1/a2/L1_A2_GameVoice_Object_3_List.m4a", :size 5, :type "audio" :alias "game voice 3" :target "game-voice"}

                           {:url "/raw/audio/l1/a3/rock/Ardilla.m4a", :size 5, :type "audio" :alias "rock-ardilla" :target "rock"}
                           {:url "/raw/audio/l1/a3/rock/Oso.m4a", :size 5, :type "audio" :alias "rock-oso" :target "rock"}
                           {:url "/raw/audio/l1/a3/rock/Iman.m4a", :size 5, :type "audio" :alias "rock-iman" :target "rock"}

                           {:url "/raw/audio/l1/a3/vera/ardilla.m4a", :size 5, :type "audio" :alias "vera-ardilla" :target "vera"}
                           {:url "/raw/audio/l1/a3/vera/oso.m4a", :size 5, :type "audio" :alias "vera-oso" :target "vera"}
                           {:url "/raw/audio/l1/a3/vera/iman.m4a", :size 5, :type "audio" :alias "vera-iman" :target "vera"}

                           {:url "/raw/img/stadium/volleyball/background.jpg", :size 10 :type "image"}
                           {:url "/raw/img/stadium/volleyball/ball.png", :size 1 :type "image"}],
           :objects       {:background {:type "background", :src "/raw/img/stadium/volleyball/background.jpg"},
                           :vera       {:type       "animation" :x 1182 :y 1010 :name "vera-45" :anim "volley_idle" :speed 0.3
                                        :width      758 :height 1130 :scale {:x 0.25 :y 0.25} :start true
                                        :scene-name "vera" :skin "vera"}
                           :boy1       {:type       "animation" :x 1556 :y 871 :name "vera-45" :anim "idle" :speed 0.3
                                        :width      758 :height 1130 :scale {:x 0.25 :y 0.25} :start true
                                        :scene-name "boy1" :skin "1_boy"}
                           :girl2      {:type       "animation" :x 197 :y 1010 :name "vera-45" :anim "idle" :speed 0.3
                                        :width      758 :height 1130 :scale {:x -0.25 :y 0.25} :start true
                                        :scene-name "girl2" :skin "2_girl"}
                           :girl3      {:type       "animation" :x 512 :y 871 :name "vera-45" :anim "idle" :speed 0.3
                                        :width      758 :height 1130 :scale {:x -0.25 :y 0.25} :start true
                                        :scene-name "girl3" :skin "3_girl"}
                           :mari       {:type    "animation" :scene-name "mari" :name "mari" :anim "idle"
                                        :start   true :speed 0.35 :transition "mari"
                                        :x       1265 :y 311 :width 473 :height 511 :anim-offset {:x 0 :y -150}
                                        :scale-y 0.5 :scale-x 0.5}
                           :box1       {:type       "animation" :x 1304 :y 831 :width 771 :height 1033 :origin {:type "center-center"} :skin "empty"
                                        :scene-name "box1" :name "boxes" :anim "idle1" :scale {:x 0.3 :y 0.3} :anim-offset {:x 0 :y -300}
                                        :actions    {:click {:type "action", :id "check-concept" :params {:player "player1"}, :on "click"}}} ;; throw check-concept

                           :box2       {:type       "animation" :x 1665 :y 666 :width 771 :height 1033 :origin {:type "center-center"} :skin "empty"
                                        :scene-name "box2" :name "boxes" :anim "idle1" :scale {:x 0.3 :y 0.3} :anim-offset {:x 0 :y -300}
                                        :actions    {:click {:type "action", :id "check-concept" :params {:player "player2"}, :on "click"}}}

                           :box3       {:type       "animation" :x 309 :y 831 :width 771 :height 1033 :origin {:type "center-center"} :skin "empty"
                                        :scene-name "box3" :name "boxes" :anim "idle1" :scale {:x 0.3 :y 0.3} :anim-offset {:x 0 :y -300}
                                        :actions    {:click {:type "action", :id "check-concept" :params {:player "player3"}, :on "click"}}}

                           :box4       {:type       "animation" :x 629 :y 666 :width 771 :height 1033 :origin {:type "center-center"} :skin "empty"
                                        :scene-name "box4" :name "boxes" :anim "idle1" :scale {:x 0.3 :y 0.3} :anim-offset {:x 0 :y -300}
                                        :actions    {:click {:type "action", :id "check-concept" :params {:player "player4"}, :on "click"}}}
                           :ball       {:type     "group" :x 1163 :y 941 :width 126 :height 126
                                        :children ["ball-image"] :transition "ball-transition"}
                           :ball-image {:type    "transparent" :width 126 :height 126 :origin {:type "center-center"}
                                        :scale-x 0.5 :scale-y 0.5
                                        :states  {:hidden  {:type "transparent"}
                                                  :visible {:type "image" :src "/raw/img/stadium/volleyball/ball.png"}}}},
           :scene-objects [["background"] ["vera" "boy1" "girl2" "girl3"] ["ball"] ["box1" "box2" "box3" "box4"] ["mari"]],
           :actions       {:mari-welcome-audio-1    {:type               "animation-sequence",
                                                     :target             "mari",
                                                     :track              1,
                                                     :offset             0.511,
                                                     :audio              "/raw/audio/l1/a5/Mari_Level1_Activity5.m4a",
                                                     :start              0.511,
                                                     :duration           9.076,
                                                     :data               [{:start 0.572, :end 1.612, :duration 1.04, :anim "talk"}
                                                                          {:start 2.188, :end 4.97, :duration 2.782, :anim "talk"}
                                                                          {:start 5.84, :end 9.418, :duration 3.578, :anim "talk"}]
                                                     :phrase             :welcome
                                                     :phrase-description "Welcome speech"
                                                     :phrase-text        "Hello! Do you want to play volleyball with us?
                                                         Touch the picture that you hear being chanted"}

                           :mari-audio-correct-1    {:type        "animation-sequence",
                                                     :target      "mari",
                                                     :track       1,
                                                     :offset      0.858,
                                                     :audio       "/raw/audio/l1/a5/Mari_Coaching_statements.m4a",
                                                     :start       0.858,
                                                     :duration    1.837,
                                                     :data        [{:start 1.052, :end 2.505, :duration 1.453, :anim "talk"}],
                                                     :phrase-text "That’s correct!"}

                           :mari-audio-correct-2    {:type        "animation-sequence",
                                                     :target      "mari",
                                                     :track       1,
                                                     :offset      4.697,
                                                     :audio       "/raw/audio/l1/a5/Mari_Coaching_statements.m4a",
                                                     :start       4.697,
                                                     :duration    1.358,
                                                     :data        [{:start 4.867, :end 5.926, :duration 1.059, :anim "talk"}],
                                                     :phrase-text "Good job!"}

                           :mari-audio-correct-3    {:type        "animation-sequence",
                                                     :target      "mari",
                                                     :track       1,
                                                     :offset      7.949,
                                                     :audio       "/raw/audio/l1/a5/Mari_Coaching_statements.m4a",
                                                     :start       7.949,
                                                     :duration    1.345,
                                                     :data        [{:start 8.066, :end 9.098, :duration 1.032, :anim "talk"}],
                                                     :phrase-text "Well done!"}

                           :mari-audio-correct-4    {:type        "animation-sequence",
                                                     :target      "mari",
                                                     :track       1,
                                                     :offset      10.997,
                                                     :audio       "/raw/audio/l1/a5/Mari_Coaching_statements.m4a",
                                                     :start       10.997,
                                                     :duration    1.147,
                                                     :data        [{:start 11.119, :end 11.964, :duration 0.845, :anim "talk"}],
                                                     :phrase-text "You got it!"}

                           :mari-audio-correct-5    {:type        "animation-sequence",
                                                     :target      "mari",
                                                     :track       1,
                                                     :offset      14.463,
                                                     :audio       "/raw/audio/l1/a5/Mari_Coaching_statements.m4a",
                                                     :start       14.463,
                                                     :duration    2.333,
                                                     :data
                                                                  [{:start 14.611, :end 15.11, :duration 0.499, :anim "talk"}
                                                                   {:start 15.529, :end 16.676, :duration 1.147, :anim "talk"}],
                                                     :phrase-text "Wow, excelent!"}

                           :mari-say-correct        {:type               "sequence"
                                                     :data               ["mari-say-correct-1"
                                                                          "mari-say-correct-2"]
                                                     :phrase             :correct
                                                     :phrase-description "Correct answer"}

                           :mari-say-correct-1      {:type      "vars-var-provider"
                                                     :variables ["current-audio-correct"]
                                                     :from      ["audio-correct1" "audio-correct2" "audio-correct3" "audio-correct4" "audio-correct5"]
                                                     :shuffled  true}

                           :mari-say-correct-2      {:type     "action"
                                                     :from-var [{:var-name        "current-audio-correct"
                                                                 :action-property "id"
                                                                 :possible-values [:mari-audio-correct-1
                                                                                   :mari-audio-correct-2
                                                                                   :mari-audio-correct-3
                                                                                   :mari-audio-correct-4
                                                                                   :mari-audio-correct-5]}]}

                           :mari-audio-try-again-1  {:type        "animation-sequence",
                                                     :target      "mari",
                                                     :track       1,
                                                     :offset      18.915,
                                                     :audio       "/raw/audio/l1/a5/Mari_Coaching_statements.m4a",
                                                     :start       18.915,
                                                     :duration    1.493,
                                                     :data        [{:start 18.983, :end 20.242, :duration 1.259, :anim "talk"}]
                                                     :phrase-text "Try again!"}

                           :mari-audio-try-again-2  {:type        "animation-sequence",
                                                     :target      "mari",
                                                     :track       1,
                                                     :offset      22.155,
                                                     :audio       "/raw/audio/l1/a5/Mari_Coaching_statements.m4a",
                                                     :start       22.155,
                                                     :duration    3.025,
                                                     :data        [{:start 23.743, :end 25.055, :duration 1.312, :anim "talk"}]
                                                     :phrase-text "Hmmmm. Try again."}

                           :mari-audio-wrong        {:type               "sequence-data"
                                                     :data               [{:type      "vars-var-provider"
                                                                           :variables ["current-audio-try-again"]
                                                                           :from      ["audio-try-again1" "audio-try-again2"]
                                                                           :shuffled  true}
                                                                          {:type "action" :from-var [{:var-name        "current-audio-try-again" :action-property "id"
                                                                                                      :possible-values [:mari-audio-try-again-1
                                                                                                                        :mari-audio-try-again-2]}]}]
                                                     :phrase             :wrong
                                                     :phrase-description "Wrong answer"}

                           :wait-for-box-animations {:type "empty" :duration 500}

                           :empty-before-dialog     {:type "empty" :duration 3000}

                           :pick-wrong              {:type "sequence-data"
                                                     :data [
                                                            {:type "action" :id "mari-audio-wrong"}
                                                            {:type        "copy-variable" :var-name "picked"
                                                             :from-params [{:action-property "from" :param-property "player"}]}
                                                            {:type     "pick-wrong" :activity "volleyball"
                                                             :from-var [{:var-name "current-concept" :action-property "concept-name" :var-property "concept-name"}
                                                                        {:var-name "picked" :action-property "option"}]}]}

                           :renew-words             {:type        "lesson-var-provider"
                                                     :provider-id "words-set"
                                                     :variables   ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8" "item-9" "item-10"]
                                                     :shuffled    true
                                                     :limit       3
                                                     :repeat      4
                                                     :from        "concepts"}

                           :renew-current-concept   {:type "sequence-data"
                                                     :data [{:type        "vars-var-provider"
                                                             :provider-id "current-concept"
                                                             :variables   ["current-concept"]
                                                             :from        ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8" "item-9" "item-10"]
                                                             :shuffled    false
                                                             :on-end      "finish-activity"}
                                                            {:type "sequence-data"
                                                             :data [{:type "parallel"
                                                                     :data [{:type     "set-variable" :var-name "player1"
                                                                             :from-var [{:var-name "current-concept" :action-property "var-value" :var-property "word-1-skin"}]}
                                                                            {:type     "set-variable" :var-name "player2"
                                                                             :from-var [{:var-name "current-concept" :action-property "var-value" :var-property "word-2-skin"}]}
                                                                            {:type     "set-variable" :var-name "player3"
                                                                             :from-var [{:var-name "current-concept" :action-property "var-value" :var-property "word-3-skin"}]}
                                                                            {:type     "set-variable" :var-name "player4"
                                                                             :from-var [{:var-name "current-concept" :action-property "var-value" :var-property "word-4-skin"}]}]}
                                                                    {:type      "vars-var-provider"
                                                                     :variables ["player1" "player2" "player3" "player4"]
                                                                     :from      ["player1" "player2" "player3" "player4"]
                                                                     :shuffled  true}
                                                                    ]}
                                                            {:type "sequence-data"
                                                             :data [{:type      "vars-var-provider"
                                                                     :variables ["current-target"]
                                                                     :from      ["slot1" "slot2" "slot3" "slot4"]
                                                                     :shuffled  true
                                                                     :from-var  [{:var-name "current-player" :action-property "exclude-values" :to-vector true}]}
                                                                    {:type     "set-variable"
                                                                     :from-var [{:var-name "current-concept" :action-property "var-value" :var-property "skin"}
                                                                                {:var-name "current-target" :action-property "var-name"}]}
                                                                    {:type     "set-current-concept"
                                                                     :from-var [{:var-name "current-concept" :action-property "value" :var-property "skin"}]}]}
                                                            {:type "parallel"
                                                             :data [{:type     "set-skin" :target "box1"
                                                                     :from-var [{:var-name "player1" :action-property "skin"}]}
                                                                    {:type     "set-skin" :target "box2"
                                                                     :from-var [{:var-name "player2" :action-property "skin"}]}
                                                                    {:type     "set-skin" :target "box3"
                                                                     :from-var [{:var-name "player3" :action-property "skin"}]}
                                                                    {:type     "set-skin" :target "box4"
                                                                     :from-var [{:var-name "player4" :action-property "skin"}]}]}]}

                           :init-slots              {:type "parallel"
                                                     :data [{:type "set-variable" :var-name "slot1" :var-value "player1"}
                                                            {:type "set-variable" :var-name "slot2" :var-value "player2"}
                                                            {:type "set-variable" :var-name "slot3" :var-value "player3"}
                                                            {:type "set-variable" :var-name "slot4" :var-value "player4"}]}

                           :init-first-player       {:type "set-variable" :var-name "current-player" :var-value "player1"}

                           :init-audio-correct      {:type "parallel"
                                                     :data [{:type "set-variable" :var-name "audio-correct1" :var-value "mari-audio-correct-1"}
                                                            {:type "set-variable" :var-name "audio-correct2" :var-value "mari-audio-correct-2"}
                                                            {:type "set-variable" :var-name "audio-correct3" :var-value "mari-audio-correct-3"}
                                                            {:type "set-variable" :var-name "audio-correct4" :var-value "mari-audio-correct-4"}
                                                            {:type "set-variable" :var-name "audio-correct5" :var-value "mari-audio-correct-5"}]}

                           :init-audio-try-again    {:type "parallel"
                                                     :data [{:type "set-variable" :var-name "audio-try-again1" :var-value "mari-audio-try-again-1"}
                                                            {:type "set-variable" :var-name "audio-try-again2" :var-value "mari-audio-try-again-2"}]}

                           :clear-instruction       {:type "remove-flows" :flow-tag "instruction"}

                           :start-scene             {:type "sequence"
                                                     :data ["start-activity"
                                                            "clear-instruction"
                                                            "init-slots"
                                                            "init-first-player"
                                                            "init-audio-correct"
                                                            "init-audio-try-again"
                                                            "renew-words"
                                                            "renew-current-concept"
                                                            "mari-welcome-audio-1"
                                                            "voice-high-var"]}

                           :check-concept           {:type        "test-var-scalar"
                                                     :var-name    "current-target"
                                                     :success     "pick-correct"
                                                     :fail        "pick-wrong"
                                                     :from-params [{:action-property "value" :param-property "player"}]}

                           :pick-correct            {:type "sequence"
                                                     :data ["mari-say-correct"
                                                            "send-correct-answer"
                                                            "throw"]}

                           :send-correct-answer     {:type     "pick-correct" :activity "volleyball"
                                                     :from-var [{:var-name "current-concept" :action-property "concept-name" :var-property "concept-name"}]}


                           :throw                   {:type "sequence"
                                                     :data ["throw-1"
                                                            "throw-2"
                                                            "throw-3"
                                                            "throw-4"
                                                            "throw-5"
                                                            "renew-current-concept"
                                                            "voice-high-var"]}

                           :throw-1                 {:type     "action"
                                                     :from-var [{:template        "animate-throw-%" :action-property "id" :var-name "current-player"
                                                                 :possible-values [:animate-throw-player1
                                                                                   :animate-throw-player2
                                                                                   :animate-throw-player3
                                                                                   :animate-throw-player4]}]}

                           :throw-2                 {:type "state" :target "ball-image" :id "visible"}
                           :throw-3                 {:type "empty" :duration 200}

                           :throw-4                 {:type "sequence"
                                                     :data ["throw-4-1"
                                                            "throw-4-2"
                                                            "throw-4-4"]}
                           :throw-4-1               {:type     "action"
                                                     :from-var [{:template        "throw-from-%" :action-property "id" :var-name "current-player"
                                                                 :possible-values [:throw-from-player1
                                                                                   :throw-from-player2
                                                                                   :throw-from-player3
                                                                                   :throw-from-player4]}]}
                           :throw-4-2               {:type "state" :target "ball-image" :id "hidden"}
                           :throw-4-4               {:type "action" :from-params [{:template        "animate-catch-%" :action-property "id" :param-property "player"
                                                                                   :possible-values [:animate-catch-player1
                                                                                                     :animate-catch-player2
                                                                                                     :animate-catch-player3
                                                                                                     :animate-catch-player4]}]}

                           :throw-5                 {:type        "set-variable" :var-name "current-player"
                                                     :from-params [{:action-property "var-value" :param-property "player"}]}

                           :animate-throw-player1   {:type "sequence-data"
                                                     :data [{:type "animation" :target "vera" :id "volley_throw"}
                                                            {:type "add-animation" :target "vera" :id "idle" :loop true}]}

                           :animate-throw-player2   {:type "sequence-data"
                                                     :data [{:type "animation" :target "boy1" :id "volley_throw"}
                                                            {:type "add-animation" :target "boy1" :id "idle" :loop true}]}

                           :animate-throw-player3   {:type "sequence-data"
                                                     :data [{:type "animation" :target "girl2" :id "volley_throw"}
                                                            {:type "add-animation" :target "girl2" :id "idle" :loop true}]}

                           :animate-throw-player4   {:type "sequence-data"
                                                     :data [{:type "animation" :target "girl3" :id "volley_throw"}
                                                            {:type "add-animation" :target "girl3" :id "idle" :loop true}]}

                           :animate-catch-player1   {:type "sequence-data"
                                                     :data [{:type "animation" :target "vera" :id "volley_call"}
                                                            {:type "add-animation" :target "vera" :id "volley_idle" :loop true}]}

                           :animate-catch-player2   {:type "sequence-data"
                                                     :data [{:type "animation" :target "boy1" :id "volley_call"}
                                                            {:type "add-animation" :target "boy1" :id "volley_idle" :loop true}]}

                           :animate-catch-player3   {:type "sequence-data"
                                                     :data [{:type "animation" :target "girl2" :id "volley_call"}
                                                            {:type "add-animation" :target "girl2" :id "volley_idle" :loop true}]}

                           :animate-catch-player4   {:type "sequence-data"
                                                     :data [{:type "animation" :target "girl3" :id "volley_call"}
                                                            {:type "add-animation" :target "girl3" :id "volley_idle" :loop true}]}

                           :throw-player1-player2   {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 1272 :y 487} {:x 1376 :y 451} {:x 1538 :y 801}] :duration 1.4}}
                           :throw-player1-player3   {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 1066 :y 284} {:x 960 :y 253} {:x 213 :y 940}] :duration 1.4}}
                           :throw-player1-player4   {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 1066 :y 284} {:x 960 :y 253} {:x 528 :y 799}] :duration 1.4}}

                           :throw-player2-player1   {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 1472 :y 487} {:x 1376 :y 451} {:x 1163 :y 941}] :duration 1.4}}
                           :throw-player2-player3   {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 1066 :y 284} {:x 960 :y 253} {:x 213 :y 940}] :duration 1.4}}
                           :throw-player2-player4   {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 1066 :y 284} {:x 960 :y 253} {:x 528 :y 799}] :duration 1.4}}

                           :throw-player3-player1   {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 866 :y 284} {:x 960 :y 253} {:x 1163 :y 941}] :duration 1.4}}
                           :throw-player3-player2   {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 866 :y 284} {:x 960 :y 253} {:x 1538 :y 801}] :duration 1.4}}
                           :throw-player3-player4   {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 246 :y 551} {:x 346 :y 451} {:x 528 :y 799}] :duration 1.4}}

                           :throw-player4-player1   {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 866 :y 284} {:x 960 :y 253} {:x 1163 :y 941}] :duration 1.4}}
                           :throw-player4-player2   {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 866 :y 284} {:x 960 :y 253} {:x 1538 :y 801}] :duration 1.4}}
                           :throw-player4-player3   {:type "transition" :transition-id "ball-transition" :to {:bezier [{:x 446 :y 551} {:x 346 :y 451} {:x 213 :y 940}] :duration 1.4}}

                           :throw-from-player1      {:type "action" :from-params [{:template "throw-player1-%" :action-property "id" :param-property "player"}]}
                           :throw-from-player2      {:type "action" :from-params [{:template "throw-player2-%" :action-property "id" :param-property "player"}]}
                           :throw-from-player3      {:type "action" :from-params [{:template "throw-player3-%" :action-property "id" :param-property "player"}]}
                           :throw-from-player4      {:type "action" :from-params [{:template "throw-player4-%" :action-property "id" :param-property "player"}]}

                           :word-ardilla-high       {:type               "audio", :id "/raw/audio/l1/a2/L1_A2_GameVoice_Object_1_List.m4a", :start 12.987, :duration 3.196
                                                     :phrase             :concept-squirrel
                                                     :phrase-description "Chant squirrel"
                                                     :phrase-text        "Squirrel, squirrel, squirrel!"}
                           :word-oso-high           {:type               "audio", :id "/raw/audio/l1/a2/L1_A2_GameVoice_Object_2_List.m4a", :start 11.361, :duration 3.663
                                                     :phrase             :concept-bear
                                                     :phrase-description "Chant bear"
                                                     :phrase-text        "Bear, bear, bear!"}
                           :word-incendio-high      {:type               "audio", :id "/raw/audio/l1/a2/L1_A2_GameVoice_Object_3_List.m4a", :start 10.781, :duration 3.301
                                                     :phrase             :concept-magnet
                                                     :phrase-description "Chant magnet"
                                                     :phrase-text        "Magnet, magnet, magnet!"}

                           :voice-high-var          {:type "action" :from-var [{:var-name        "current-concept" :action-property "id" :var-property "seesaw-voice-high"
                                                                                :possible-values [:word-ardilla-high
                                                                                                  :word-oso-high
                                                                                                  :word-incendio-high]}]}

                           :start-activity          {:type "start-activity" :id "volleyball"}
                           :stop-activity           {:type "stop-activity" :id "volleyball"}
                           :finish-activity         {:type "sequence-data"
                                                     :data [{:type "finish-activity" :id "volleyball"}
                                                            {:type "scene" :scene-id "stadium"}]}}
           :audio         {:mari-welcome "/raw/audio/l1/a3/Mari_Level1_Activity3.m4a"
                           :vera-ardilla "/raw/audio/l1/a3/vera/ardilla.m4a"
                           :vera-oso     "/raw/audio/l1/a3/vera/oso.m4a"
                           :vera-iman    "/raw/audio/l1/a3/vera/iman.m4a"
                           :rock-ardilla "/raw/audio/l1/a3/rock/Ardilla.m4a"
                           :rock-oso     "/raw/audio/l1/a3/rock/Oso.m4a"
                           :rock-iman    "/raw/audio/l1/a3/rock/Iman.m4a"}
           :triggers      {:start {:on "start" :action "start-scene"}
                           :back  {:on "back" :action "stop-activity"}}
           :metadata      {:autostart true
                           :prev      "stadium"}})
