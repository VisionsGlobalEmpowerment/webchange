(ns webchange.demo-scenes.library.magic-hat)

(def magic-hat-scene
  {:assets        [{:url "/raw/img/library/magic-hat/background.jpg" :type "image"}
                   {:url "/raw/audio/l2/a8/L2_A8_Mari.m4a" :type "audio"}]
   :objects       {:background {:type "background"
                                :src  "/raw/img/library/magic-hat/background.jpg"}
                   :hat        {:type       "animation"
                                :scene-name "magic-hat"
                                :name       "hat"
                                :transition "hat"
                                :anim       "idle"
                                :start      true
                                :loop       true
                                :speed      0.35
                                :x          925
                                :y          555
                                :width      473
                                :height     511
                                :scale-x    1
                                :scale-y    1}
                   :mari       {:type       "animation"
                                :scene-name "mari"
                                :name       "mari"
                                :transition "mari"
                                :anim       "idle"
                                :start      true
                                :loop       true
                                :speed      0.35
                                :x          1500
                                :y          370
                                :width      473
                                :height     511
                                :scale-y    0.5
                                :scale-x    0.5}
                   :letter1    {:type           "transparent"
                                :text           ""
                                :x              1200
                                :y              570
                                :width          150
                                :height         150
                                :scale-x        1
                                :scale-y        1
                                :align          "center"
                                :vertical-align "middle"
                                :font-family    "Lexend Deca"
                                :font-size      160
                                :fill           "white"}
                   :box1       {:type       "transparent"
                                :scene-name "box1"
                                :name       "boxes"
                                :skin       "qwestion"
                                :transition "box1"
                                :anim       "idle2"
                                :start      true
                                :loop       true
                                :speed      0.3
                                :x          1270
                                :y          800
                                :width      671
                                :height     633
                                :scale-x    0.25
                                :scale-y    0.25
                                :actions    {:click {:type "action" :id "pick-box" :params {:target "box1"} :on "click"}}}
                   :letter2    {:type           "transparent"
                                :text           ""
                                :x              1390
                                :y              540
                                :width          150
                                :height         150
                                :scale-x        1
                                :scale-y        1
                                :align          "center"
                                :vertical-align "middle"
                                :font-family    "Lexend Deca"
                                :font-size      160
                                :fill           "white"}
                   :box2       {:type       "transparent"
                                :scene-name "box2"
                                :name       "boxes"
                                :skin       "qwestion"
                                :transition "box2"
                                :anim       "idle2"
                                :start      true
                                :loop       true
                                :speed      0.3
                                :x          1460
                                :y          770
                                :width      671
                                :height     633
                                :scale-x    0.25
                                :scale-y    0.25
                                :actions    {:click {:type "action" :id "pick-box" :params {:target "box2"} :on "click"}}}
                   :letter3    {:type           "transparent"
                                :text           ""
                                :x              1590
                                :y              560
                                :width          150
                                :height         150
                                :scale-x        1
                                :scale-y        1
                                :align          "center"
                                :vertical-align "middle"
                                :font-family    "Lexend Deca"
                                :font-size      160
                                :fill           "white"}
                   :box3       {:type       "transparent"
                                :scene-name "box3"
                                :name       "boxes"
                                :skin       "qwestion"
                                :transition "box3"
                                :anim       "idle2"
                                :start      true
                                :loop       true
                                :speed      0.3
                                :x          1660
                                :y          790
                                :width      671
                                :height     633
                                :scale-x    0.25
                                :scale-y    0.25
                                :actions    {:click {:type "action" :id "pick-box" :params {:target "box3"} :on "click"}}}}
   :scene-objects [["background"] ["hat" "box1" "box2" "box3" "letter1" "letter2" "letter3"] ["mari"]]
   :actions       {:start                    {:type "sequence"
                                              :data ["start-activity"
                                                     "clear-instruction"
                                                     "init-vars"
                                                     "intro"
                                                     "play"]}

                   :stop                     {:type "sequence" :data ["stop-activity"]}

                   :start-activity           {:type "start-activity" :id "magic-hat"}

                   :stop-activity            {:type "stop-activity" :id "magic-hat"}

                   :clear-instruction        {:type "remove-flows" :flow-tag "instruction"}

                   :intro                    {:type "sequence"
                                              :data ["mari-voice-welcome"
                                                     "mari-flies-to-hat"
                                                     "mari-voice-intro"]}

                   :play                     {:type "sequence"
                                              :data ["renew-current-concept"
                                                     "check-level"
                                                     "mari-tap-hat"
                                                     "boxes-jump-out"
                                                     "set-concept-data"
                                                     "mary-says-task"
                                                     ]}

                   :init-vars                {:type "parallel"
                                              :data [{:type "set-variable" :var-name "current-level" :var-value "mid"}
                                                     {:type "set-variable" :var-name "correct-answers-in-row" :var-value 0}
                                                     {:type "set-variable" :var-name "wrong-answers-in-row" :var-value 0}
                                                     {:type "set-variable" :var-name "last-pick-wrong" :var-value false}]}

                   ;; ---

                   :renew-current-concept    {:type "sequence-data"
                                              :data [{:type "set-variable" :var-name "current-concept" :var-value {:letter "a" :skin "squirrel"}}
                                                     {:type "set-variable" :var-name "pair-concept-1" :var-value {:letter "i" :skin "magnet"}}
                                                     {:type "set-variable" :var-name "pair-concept-2" :var-value {:letter "o" :skin "bear"}}
                                                     ;; ---
                                                     {:type      "vars-var-provider"
                                                      :variables ["box1" "box2" "box3"]
                                                      :from      ["current-concept" "pair-concept-1" "pair-concept-2"]
                                                      :shuffled  true}]}

                   :check-level              {:type "sequence-data"
                                              :data [{:type     "test-var-scalar"
                                                      :var-name "correct-answers-in-row"
                                                      :value    3
                                                      :success  {:type "sequence-data"
                                                                 :data [{:type "set-variable" :var-name "correct-answers-in-row" :var-value 0}
                                                                        {:type     "test-var-scalar"
                                                                         :var-name "current-level"
                                                                         :value    "easy"
                                                                         :success  {:type "set-variable" :var-name "current-level" :var-value "mid"}
                                                                         :fail     {:type "set-variable" :var-name "current-level" :var-value "hard"}}]}
                                                      :fail     {:type "empty" :duration 100}}
                                                     {:type     "test-var-scalar"
                                                      :var-name "wrong-answers-in-row"
                                                      :value    3
                                                      :success  {:type "sequence-data"
                                                                 :data [{:type "set-variable" :var-name "wrong-answers-in-row" :var-value 0}
                                                                        {:type     "test-var-scalar"
                                                                         :var-name "current-level"
                                                                         :value    "hard"
                                                                         :success  {:type "set-variable" :var-name "current-level" :var-value "mid"}
                                                                         :fail     {:type "set-variable" :var-name "current-level" :var-value "easy"}}]}
                                                      :fail     {:type "empty" :duration 100}}]}

                   :mari-tap-hat             {:type "sequence-data"
                                              :data [{:type "transition" :transition-id "mari" :to {:x 1005 :y 453 :loop false :duration 0.5}}
                                                     {:type "transition" :transition-id "mari" :to {:x 1075 :y 445 :loop false :duration 0.5}}]}

                   :set-concept-data         {:type "sequence-data"
                                              :data [{:type "action" :id "set-letters-text"}
                                                     {:type     "test-var-scalar"
                                                      :var-name "current-level"
                                                      :value    "hard"
                                                      :success  {:type "empty" :duration 100}
                                                      :fail     "set-boxes-skin"}]}


                   :mary-says-task           {:type     "test-var-scalar"
                                              :var-name "current-level"
                                              :value    "easy"
                                              :success  "current-concept-chant"
                                              :fail     "current-concept-sound-x3"}

                   ;; ---

                   :pick-box                 {:type "sequence-data"
                                              :data [{:type "action" :id "set-concept-data"}
                                                     {:type        "test-var-scalar"
                                                      :from-params [{:action-property "var-name" :param-property "target"}]
                                                      :from-var    [{:var-name "current-concept" :action-property "value"}]
                                                      :success     "pick-correct"
                                                      :fail        "pick-wrong"}]}

                   :pick-correct             {:type "sequence-data"
                                              :data [{:type "action" :id "mari-says-correct-answer"}
                                                     {:type     "test-var-scalar"
                                                      :var-name "last-pick-wrong"
                                                      :value    false
                                                      :success  "count-correct-answer"
                                                      :fail     {:type "empty" :duration 100}}
                                                     {:type "action" :id "reset-and-repeat"}]}

                   :pick-wrong               {:type "sequence-data"
                                              :data [{:type "action" :id "mari-says-wrong-answer"}
                                                     {:type "action" :id "count-wrong-answer"}
                                                     {:type "action" :id "set-boxes-skin"}
                                                     {:type "set-variable" :var-name "last-pick-wrong" :var-value true}]}

                   :reset-and-repeat         {:type "sequence-data"
                                              :data [{:type "set-variable" :var-name "last-pick-wrong" :var-value false}
                                                     {:type "action" :id "letters-hide"}
                                                     {:type "action" :id "boxes-disappear"}
                                                     {:type "action" :id "play"}]}

                   ;; ---

                   :set-boxes-skin           {:type "parallel"
                                              :data [{:type "set-skin" :target "box1" :from-var [{:var-name "box1" :action-property "skin" :var-property "skin"}]}
                                                     {:type "set-skin" :target "box2" :from-var [{:var-name "box2" :action-property "skin" :var-property "skin"}]}
                                                     {:type "set-skin" :target "box3" :from-var [{:var-name "box3" :action-property "skin" :var-property "skin"}]}]}

                   :set-letters-text         {:type "sequence-data"
                                              :data [{:type "parallel"
                                                      :data [{:type     "set-attribute" :target "letter1" :attr-name "text"
                                                              :from-var [{:var-name "box1" :action-property "attr-value" :var-property "letter"}]}
                                                             {:type     "set-attribute" :target "letter2" :attr-name "text"
                                                              :from-var [{:var-name "box2" :action-property "attr-value" :var-property "letter"}]}
                                                             {:type     "set-attribute" :target "letter3" :attr-name "text"
                                                              :from-var [{:var-name "box3" :action-property "attr-value" :var-property "letter"}]}]}
                                                     {:type "action" :id "letters-show"}]}


                   :count-correct-answer     {:type "sequence-data"
                                              :data [{:type "counter" :counter-action "increase" :counter-id "correct-answers-in-row"}
                                                     {:type "set-variable" :var-name "wrong-answers-in-row" :var-value 0}]}

                   :count-wrong-answer       {:type "sequence-data"
                                              :data [{:type "counter" :counter-action "increase" :counter-id "wrong-answers-in-row"}
                                                     {:type "set-variable" :var-name "correct-answers-in-row" :var-value 0}]}

                   ;; ---

                   :letters-hide             {:type "parallel"
                                              :data [{:type "set-attribute" :target "letter1" :attr-name "type" :attr-value "transparent"}
                                                     {:type "set-attribute" :target "letter2" :attr-name "type" :attr-value "transparent"}
                                                     {:type "set-attribute" :target "letter3" :attr-name "type" :attr-value "transparent"}]}

                   :letters-show             {:type "parallel"
                                              :data [{:type "set-attribute" :target "letter1" :attr-name "type" :attr-value "text"}
                                                     {:type "set-attribute" :target "letter2" :attr-name "type" :attr-value "text"}
                                                     {:type "set-attribute" :target "letter3" :attr-name "type" :attr-value "text"}]}

                   :boxes-jump-out           {:type "sequence-data"
                                              :data [{:type "action" :id "boxes-reset-position"}
                                                     {:type "empty" :duration 200}
                                                     {:type "action" :id "boxes-show-up"}
                                                     {:type "action" :id "boxes-move-to-position"}]}

                   :boxes-reset-position     {:type "parallel"
                                              :data [{:type "set-attribute" :target "box1" :attr-name "x" :attr-value 610}
                                                     {:type "set-attribute" :target "box1" :attr-name "y" :attr-value 435}
                                                     {:type "set-attribute" :target "box2" :attr-name "x" :attr-value 610}
                                                     {:type "set-attribute" :target "box2" :attr-name "y" :attr-value 435}
                                                     {:type "set-attribute" :target "box3" :attr-name "x" :attr-value 610}
                                                     {:type "set-attribute" :target "box3" :attr-name "y" :attr-value 435}]}

                   :boxes-show-up            {:type "parallel"
                                              :data [{:type "sequence-data"
                                                      :data [{:type "set-attribute" :target "box1" :attr-name "type" :attr-value "animation"}
                                                             {:type "empty" :duration 200}
                                                             {:type "animation" :target "box1" :id "come2"}
                                                             {:type "add-animation" :target "box1" :id "idle2" :loop true}]}
                                                     {:type "sequence-data"
                                                      :data [{:type "set-attribute" :target "box2" :attr-name "type" :attr-value "animation"}
                                                             {:type "empty" :duration 200}
                                                             {:type "animation" :target "box2" :id "come2"}
                                                             {:type "add-animation" :target "box2" :id "idle2" :loop true}]}
                                                     {:type "sequence-data"
                                                      :data [{:type "set-attribute" :target "box3" :attr-name "type" :attr-value "animation"}
                                                             {:type "empty" :duration 200}
                                                             {:type "animation" :target "box3" :id "come2"}
                                                             {:type "add-animation" :target "box3" :id "idle2" :loop true}]}]}

                   :boxes-move-to-position   {:type "parallel"
                                              :data [{:type "transition" :transition-id "box1"
                                                      :to   {:bezier [{:x 960 :y 100} {:x 1270 :y 800}] :duration 2.0}}
                                                     {:type "transition" :transition-id "box2"
                                                      :to   {:bezier [{:x 1055 :y 100} {:x 1460 :y 770}] :duration 2.0}}
                                                     {:type "transition" :transition-id "box3"
                                                      :to   {:bezier [{:x 1195 :y 100} {:x 1660 :y 790}] :duration 2.0}}]}

                   :boxes-disappear          {:type "parallel"
                                              :data [{:type "set-skin" :target "box1" :skin "qwestion"}
                                                     {:type "set-skin" :target "box2" :skin "qwestion"}
                                                     {:type "set-skin" :target "box3" :skin "qwestion"}
                                                     {:type "set-attribute" :target "box1" :attr-name "type" :attr-value "transparent"}
                                                     {:type "set-attribute" :target "box2" :attr-name "type" :attr-value "transparent"}
                                                     {:type "set-attribute" :target "box3" :attr-name "type" :attr-value "transparent"}]}

                   :mari-flies-to-hat        {:type "transition" :transition-id "mari" :to {:x 1075 :y 445 :loop false :duration 1.5}}

                   :mari-says-sounds         {:type "sequence-data"
                                              :data [{:type "action" :id "current-concept-sound-x3"}]}

                   :mari-says-correct-answer {:type "sequence-data"
                                              :data [{:type "action" :id "current-concept-word"}]}

                   :mari-says-wrong-answer   {:type "sequence-data"
                                              :data [{:type "action" :id "current-concept-word-2"}
                                                     {:type "empty" :duration 500}
                                                     {:type "action" :id "mari-voice-wrong-1"}
                                                     {:type "action" :id "current-concept-word-2"}
                                                     {:type "empty" :duration 500}
                                                     {:type "action" :id "current-concept-word-2"}
                                                     {:type "action" :id "mari-voice-wrong-2"}
                                                     {:type "action" :id "current-concept-sound"}
                                                     {:type "empty" :duration 500}
                                                     {:type "action" :id "mari-voice-wrong-3"}
                                                     {:type "action" :id "current-concept-sound"}]}

                   ;; ---

                   :current-concept-word     {:type        "parallel"
                                              :description "Ardilla!"
                                              :data        [{:type "audio" :id "/raw/audio/l2/a8/L2_A8_Mari.m4a" :start 22.977 :duration 1.116 :volume 0.2}
                                                            {:type "animation-sequence" :target "mari" :track 1 :offset 22.977
                                                             :data [{:start 22.977 :end 24.013 :anim "talk"}]}]}

                   :current-concept-word-2   {:type        "parallel"
                                              :description "Ardilla."
                                              :data        [{:type "audio" :id "/raw/audio/l2/a8/L2_A8_Mari.m4a" :start 26.951 :duration 0.747 :volume 0.2}
                                                            {:type "animation-sequence" :target "mari" :track 1 :offset 26.951
                                                             :data [{:start 26.951 :end 27.698 :anim "talk"}]}]}

                   :current-concept-sound    {:type        "parallel"
                                              :description "aaaa"
                                              :data        [{:type "audio" :id "/raw/audio/l2/a8/L2_A8_Mari.m4a" :start 33.746 :duration 1.240 :volume 0.2}
                                                            {:type "animation-sequence" :target "mari" :track 1 :offset 33.746
                                                             :data [{:start 33.746 :end 34.986 :anim "talk"}]}]}

                   :current-concept-sound-x3 {:type        "parallel"
                                              :description "Aaa, aaa, aaa"
                                              :data        [{:type "audio" :id "/raw/audio/l2/a8/L2_A8_Mari.m4a" :start 19.542 :duration 3.460 :volume 0.2}
                                                            {:type "animation-sequence" :target "mari" :track 1 :offset 19.542
                                                             :data [{:start 19.728 :end 20.584 :anim "talk"}
                                                                    {:start 20.943 :end 21.824 :anim "talk"}
                                                                    {:start 22.121 :end 22.853 :anim "talk"}]}]}

                   :current-concept-chant    {:type        "parallel"
                                              :description "Ardilla, ardilla, a, a, a"
                                              :data        [{:type "audio" :id "/raw/audio/l2/a8/L2_A8_Mari.m4a" :start 45.526 :duration 5.289 :volume 0.2}
                                                            {:type "animation-sequence" :target "mari" :track 1 :offset 45.526
                                                             :data [{:start 45.656 :end 46.481 :anim "talk"}
                                                                    {:start 46.840 :end 47.984 :anim "talk"}
                                                                    {:start 48.180 :end 48.855 :anim "talk"}
                                                                    {:start 49.196 :end 49.854 :anim "talk"}
                                                                    {:start 50.170 :end 50.709 :anim "talk"}]}]}

                   ;; ---

                   :mari-voice-welcome       {:type        "parallel"
                                              :description "Hola mi amigo! Puedes ver el sombrero de magos?"
                                              :data        [{:type "audio" :id "/raw/audio/l2/a8/L2_A8_Mari.m4a" :start 0.533 :duration 4.538 :volume 0.2}
                                                            {:type "animation-sequence" :target "mari" :track 1 :offset 0.533
                                                             :data [{:start 0.918 :end 2.083 :anim "talk"}
                                                                    {:start 2.629 :end 4.799 :anim "talk"}]}]}

                   :mari-voice-intro         {:type        "parallel"
                                              :description "Hay muchas fotos por dentro del sombrero de magos.
                                                            Escucha el canto, y despues toca la foto que completa el canto!"
                                              :data        [{:type "audio" :id "/raw/audio/l2/a8/L2_A8_Mari.m4a" :start 6.969 :duration 10.354 :volume 0.2}
                                                            {:type "animation-sequence" :target "mari" :track 1 :offset 6.969
                                                             :data [{:start 7.130 :end 10.850 :anim "talk"}
                                                                    {:start 11.433 :end 17.050 :anim "talk"}]}]}

                   :mari-voice-wrong-1       {:type        "parallel"
                                              :description "Es una"
                                              :data        [{:type "audio" :id "/raw/audio/l2/a8/L2_A8_Mari.m4a" :start 28.882 :duration 0.729 :volume 0.2}
                                                            {:type "animation-sequence" :target "mari" :track 1 :offset 28.882
                                                             :data [{:start 28.882 :end 29.611 :anim "talk"}]}]}

                   :mari-voice-wrong-2       {:type        "parallel"
                                              :description "empieza con el sonido"
                                              :data        [{:type "audio" :id "/raw/audio/l2/a8/L2_A8_Mari.m4a" :start 31.579 :duration 1.922 :volume 0.2}
                                                            {:type "animation-sequence" :target "mari" :track 1 :offset 31.579
                                                             :data [{:start 31.579 :end 33.501 :anim "talk"}]}]}

                   :mari-voice-wrong-3       {:type        "parallel"
                                              :description "Toca la foto que hace este sonido."
                                              :data        [{:type "audio" :id "/raw/audio/l2/a8/L2_A8_Mari.m4a" :start 35.879 :duration 2.461 :volume 0.2}
                                                            {:type "animation-sequence" :target "mari" :track 1 :offset 35.879
                                                             :data [{:start 35.879 :end 38.340 :anim "talk"}]}]}

                   :mari-voice-can-you-find  {:type        "parallel"
                                              :description "Puedes encontrar este foto"
                                              :data        [{:type "audio" :id "/raw/audio/l2/a8/L2_A8_Mari.m4a" :start 41.323 :duration 2.926 :volume 0.2}
                                                            {:type "animation-sequence" :target "mari" :track 1 :offset 41.323
                                                             :data [{:start 41.453 :end 42.792 :anim "talk"}
                                                                    {:start 43.021 :end 44.100 :anim "talk"}]}]}

                   }
   :triggers      {:start {:on "start" :action "start"}
                   :stop  {:on "back" :action "stop"}}
   :metadata      {:autostart true
                   :prev      "library"}})