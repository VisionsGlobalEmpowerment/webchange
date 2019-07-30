(ns webchange.demo-scenes.stadium.running)

(def running-scene
  {:assets        [{:url "/raw/img/stadium/running/bg_01.jpg" :type "image"}
                   {:url "/raw/img/stadium/running/bg_02.jpg" :type "image"}
                   {:url "/raw/img/stadium/running/bg_03.jpg" :type "image"}

                   {:url "/raw/audio/l2/a4/L2_A4_Mari.m4a" :type "audio"}

                   {:url "/raw/audio/l2/a4/L2_A4_Gamevoice_1.m4a" :type "audio"}
                   {:url "/raw/audio/l2/a4/L2_A4_Gamevoice_2.m4a" :type "audio"}
                   {:url "/raw/audio/l2/a4/L2_A4_Gamevoice_3.m4a" :type "audio"}
                   {:url "/raw/audio/l2/a4/L2_A4_Gamevoice_4.m4a" :type "audio"}]
   :audio         {:mari "/raw/audio/l2/a4/L2_A4_Mari.m4a"}
   :objects       {:background {:type  "carousel" :x 0 :y 0 :width 1920 :height 1080
                                :first "/raw/img/stadium/running/bg_01.jpg"
                                :next  "/raw/img/stadium/running/bg_02.jpg"
                                :last  "/raw/img/stadium/running/bg_03.jpg"}

                   :vera       {:type       "animation"
                                :scene-name "vera"
                                :transition "vera"
                                :x          500
                                :y          775
                                :width      727
                                :height     1091
                                :scaleX     0.4
                                :scaleY     0.4
                                :name       "vera-90"
                                :anim       "run"
                                :skin       "default"
                                :speed      0.3
                                :start      true
                                :meshes     true}

                   :mari       {:type        "animation"
                                :scene-name  "mari"
                                :transition  "mari"
                                :x           1265
                                :y           311
                                :width       473
                                :height      511
                                :scale-y     0.5
                                :scale-x     0.5
                                :name        "mari"
                                :anim        "idle"
                                :speed       0.35
                                :start       true
                                :anim-offset {:x 0 :y -150}}

                   :box1       {:type        "animation"
                                :name        "boxes"
                                :skin        "qwestion"
                                :scene-name  "box1"
                                :transition  "box1"
                                :x           2000
                                :y           683
                                :width       671
                                :height      633
                                :scale       {:x -0.25 :y 0.25}
                                :anim        "idle2"
                                :anim-offset {:x 0 :y -300}
                                :speed       0.3
                                :loop        true
                                :start       true
                                :states      {:init  {:type "transparent"}
                                              :reset {:type "animation"
                                                      :x    2000}}
                                :actions     {:click {:type "action" :id "pick-box-1" :on "click"}}}

                   :letter1    {:type           "text"
                                :transition     "letter1"
                                :text           "?"
                                :x              1945
                                :y              400
                                :width          150
                                :height         150
                                :align          "center"
                                :vertical-align "middle"
                                :font-family    "Luckiest Guy"
                                :font-size      120
                                :shadow-color   "#1a1a1a"
                                :shadow-offset  {:x 5 :y 5}
                                :shadow-blur    5
                                :shadow-opacity 0.5
                                :fill           "white"
                                :states         {:init  {:type "transparent"}
                                                 :reset {:type "text"
                                                         :x    1945}}}

                   :box2       {:type        "animation"
                                :name        "boxes"
                                :skin        "qwestion"
                                :scene-name  "box2"
                                :transition  "box2"
                                :x           2200
                                :y           789
                                :width       671
                                :height      633
                                :scale       {:x -0.25 :y 0.25}
                                :anim        "idle2"
                                :anim-offset {:x 0 :y -300}
                                :speed       0.3
                                :loop        true
                                :start       true
                                :states      {:init  {:type "transparent"}
                                              :reset {:type "animation"
                                                      :x    2200}}
                                :actions     {:click {:type "action" :id "pick-box-2" :on "click"}}}

                   :letter2    {:type           "text"
                                :transition     "letter2"
                                :text           "?"
                                :x              2145
                                :y              506
                                :width          150
                                :height         150
                                :align          "center"
                                :vertical-align "middle"
                                :font-family    "Luckiest Guy"
                                :font-size      120
                                :shadow-color   "#1a1a1a"
                                :shadow-offset  {:x 5 :y 5}
                                :shadow-blur    5
                                :shadow-opacity 0.5
                                :fill           "white"
                                :states         {:init  {:type "transparent"}
                                                 :reset {:type "text"
                                                         :x    2145}}}

                   :box3       {:type        "animation"
                                :name        "boxes"
                                :skin        "qwestion"
                                :scene-name  "box3"
                                :transition  "box3"
                                :x           2400
                                :y           932
                                :width       671
                                :height      633
                                :scale       {:x -0.25 :y 0.25}
                                :anim        "idle2"
                                :anim-offset {:x 0 :y -300}
                                :speed       0.3
                                :loop        true
                                :start       true
                                :states      {:init  {:type "transparent"}
                                              :reset {:type "animation"
                                                      :x    2400}}
                                :actions     {:click {:type "action" :id "pick-box-3" :on "click"}}}

                   :letter3    {:type           "text"
                                :transition     "letter3"
                                :text           "?"
                                :x              2345
                                :y              649
                                :width          150
                                :height         150
                                :align          "center"
                                :vertical-align "middle"
                                :font-family    "Luckiest Guy"
                                :font-size      120
                                :shadow-color   "#1a1a1a"
                                :shadow-offset  {:x 5 :y 5}
                                :shadow-blur    5
                                :shadow-opacity 0.5
                                :fill           "white"
                                :states         {:init  {:type "transparent"}
                                                 :reset {:type "text"
                                                         :x    2345}}}}

   :scene-objects [["background"] ["box1" "letter1" "box2" "letter2" "box3" "letter3"] ["vera" "mari"]]

   :actions       {:clear-instruction       {:type "remove-flows" :flow-tag "instruction"}

                   :start                   {:type "sequence"
                                             :data ["start-activity"
                                                    "clear-instruction"
                                                    "mari-voice-welcome"
                                                    "init-slots"
                                                    "init-vera-position"
                                                    "renew-words"
                                                    "renew-current-concept"]}

                   :stop                    {:type "sequence" :data ["stop-activity"]}

                   :start-activity          {:type "start-activity" :id "running"}

                   :stop-activity           {:type "stop-activity" :id "running"}

                   :init-slots              {:type "parallel"
                                             :data [{:type "set-variable" :var-name "slot1" :var-value "box1"}
                                                    {:type "set-variable" :var-name "slot2" :var-value "box2"}
                                                    {:type "set-variable" :var-name "slot3" :var-value "box3"}]}

                   :init-vera-position      {:type "set-variable" :var-name "current-line" :var-value "box2"}

                   :renew-words             {:type "sequence-data"
                                             :data [{:type        "lesson-var-provider"
                                                     :provider-id "words-set"
                                                     :variables   ["item-1" "item-2" "item-3" "item-4"
                                                                   "item-5" "item-6" "item-7" "item-8"]
                                                     :shuffled    false
                                                     :limit       3
                                                     :repeat      4
                                                     :from        "concepts"}
                                                    {:type "copy-variable" :var-name "item-1-1" :from "item-1"}
                                                    {:type "copy-variable" :var-name "item-1-2" :from "item-1"}
                                                    {:type "copy-variable" :var-name "item-1-3" :from "item-1"}]}

                   :renew-current-concept   {:type "sequence-data"
                                             :data [{:type "parallel"
                                                     :data [{:type "state" :target "box1" :id "init"}
                                                            {:type "state" :target "box2" :id "init"}
                                                            {:type "state" :target "box3" :id "init"}
                                                            {:type "state" :target "letter1" :id "init"}
                                                            {:type "state" :target "letter2" :id "init"}
                                                            {:type "state" :target "letter3" :id "init"}
                                                            ]}
                                                    {:type "action" :id "wait-for-box-animations"}
                                                    {:type "parallel"
                                                     :data [{:type "state" :target "box1" :id "reset"}
                                                            {:type "state" :target "box2" :id "reset"}
                                                            {:type "state" :target "box3" :id "reset"}
                                                            {:type "state" :target "letter1" :id "reset"}
                                                            {:type "state" :target "letter2" :id "reset"}
                                                            {:type "state" :target "letter3" :id "reset"}]}
                                                    {:type "action" :id "wait-for-box-animations"}
                                                    {:type        "vars-var-provider"
                                                     :provider-id "current-concept"
                                                     :variables   ["current-concept"]
                                                     :from        ["item-1-1" "item-1-2" "item-1-3"
                                                                   "item-2" "item-3" "item-4"
                                                                   "item-5" "item-6" "item-7" "item-8"]
                                                     :shuffled    true
                                                     :on-end      "stop"}
                                                    {:type      "vars-var-provider"
                                                     :variables ["pair-concept-1" "pair-concept-2"]
                                                     :from      ["item-1" "item-2" "item-3" "item-4"
                                                                 "item-5" "item-6" "item-7" "item-8"]
                                                     :shuffled  true
                                                     :from-var  [{:var-name "current-concept" :var-property "concept-name"
                                                                  :var-key  "concept-name" :action-property "exclude-property-values"}]}
                                                    {:type      "vars-var-provider"
                                                     :variables ["box1" "box2" "box3"]
                                                     :from      ["current-concept" "pair-concept-1" "pair-concept-2"]
                                                     :shuffled  true}
                                                    {:type "parallel"
                                                     :data [{:type     "set-skin" :target "box1"
                                                             :from-var [{:var-name "box1" :var-property "skin" :action-property "skin"}]}
                                                            {:type     "set-skin" :target "box2"
                                                             :from-var [{:var-name "box2" :var-property "skin" :action-property "skin"}]}
                                                            {:type     "set-skin" :target "box3"
                                                             :from-var [{:var-name "box3" :var-property "skin" :action-property "skin"}]}]}
                                                    {:type "parallel"
                                                     :data [{:type     "set-attribute" :target "letter1" :attr-name "text"
                                                             :from-var [{:var-name        "box1"
                                                                         :var-property    "letter"
                                                                         :action-property "attr-value"}]}
                                                            {:type     "set-attribute" :target "letter2" :attr-name "text"
                                                             :from-var [{:var-name        "box2"
                                                                         :var-property    "letter"
                                                                         :action-property "attr-value"}]}
                                                            {:type     "set-attribute" :target "letter3" :attr-name "text"
                                                             :from-var [{:var-name        "box3"
                                                                         :var-property    "letter"
                                                                         :action-property "attr-value"}]}]}
                                                    {:type "parallel"
                                                     :data [{:type "transition" :transition-id "box1" :to {:x 1300 :duration 2}}
                                                            {:type "transition" :transition-id "box2" :to {:x 1500 :duration 2}}
                                                            {:type "transition" :transition-id "box3" :to {:x 1700 :duration 2}}
                                                            {:type "transition" :transition-id "letter1" :to {:x 1245 :duration 2}}
                                                            {:type "transition" :transition-id "letter2" :to {:x 1445 :duration 2}}
                                                            {:type "transition" :transition-id "letter3" :to {:x 1645 :duration 2}}]}

                                                    {:type "action" :id "current-concept-chant"}]}

                   :pick-correct            {:type "sequence-data"
                                             :data [{:type "parallel"
                                                     :data [{:type "action" :id "mari-voice-correct"}
                                                            {:type "transition" :transition-id "box1" :to {:x -700 :duration 4}}
                                                            {:type "transition" :transition-id "box2" :to {:x -500 :duration 4}}
                                                            {:type "transition" :transition-id "box3" :to {:x -300 :duration 4}}
                                                            {:type "transition" :transition-id "letter1" :to {:x -755 :duration 4}}
                                                            {:type "transition" :transition-id "letter2" :to {:x -555 :duration 4}}
                                                            {:type "transition" :transition-id "letter3" :to {:x -355 :duration 4}}
                                                            {:type "sequence-data"
                                                             :data [{:type     "empty"
                                                                     :from-var [{:var-name "jump-wait" :action-property "duration"}]}
                                                                    {:type "animation" :target "vera" :id "run_jump"}
                                                                    {:type     "transition" :transition-id "vera"
                                                                     :from-var [{:var-name "current-line-jump" :action-property "to"}]}
                                                                    {:type "add-animation" :target "vera" :id "run" :loop true}
                                                                    {:type     "transition" :transition-id "vera"
                                                                     :from-var [{:var-name "current-line-pos" :action-property "to"}]}]}]}
                                                    {:type "action" :id "renew-current-concept"}]}

                   :pick-wrong              {:type "action" :id "mari-voice-wrong"}

                   :stay-on-line            {:type "empty" :duration 100}

                   :wait-for-box-animations {:type "empty" :duration 100}

                   :pick-box-1              {:type "sequence-data"
                                             :data [{:type "set-variable" :var-name "current-line-pos" :var-value {:y 675 :duration 0.5}}
                                                    {:type "set-variable" :var-name "current-line-jump" :var-value {:y 475 :duration 1}}
                                                    {:type "set-variable" :var-name "jump-wait" :var-value 600}
                                                    {:type     "test-value"
                                                     :value1   "box1"
                                                     :from-var [{:var-name "current-line" :action-property "value2"}]
                                                     :success  "stay-on-line"
                                                     :fail     "go-to-box1-line"}
                                                    {:type     "test-value"

                                                     :from-var [{:var-name "box1" :action-property "value1"}
                                                                {:var-name "current-concept" :action-property "value2"}]
                                                     :success  "pick-correct"
                                                     :fail     "pick-wrong"}]}

                   :go-to-box1-line         {:type "sequence-data"
                                             :data [{:type "set-variable" :var-name "current-line" :var-value "box1"}
                                                    {:type     "transition" :transition-id "vera"
                                                     :from-var [{:var-name "current-line-pos" :action-property "to"}]}]}

                   :go-to-box2-line-up      {:type "sequence-data"
                                             :data [{:type "set-variable" :var-name "current-line" :var-value "box2"}
                                                    {:type     "transition" :transition-id "vera"
                                                     :from-var [{:var-name "current-line-pos" :action-property "to"}]}]}

                   :go-to-box2-line-down    {:type "sequence-data"
                                             :data [{:type "set-variable" :var-name "current-line" :var-value "box2"}
                                                    {:type     "transition" :transition-id "vera"
                                                     :from-var [{:var-name "current-line-pos" :action-property "to"}]}]}

                   :pick-box-2              {:type "sequence-data"
                                             :data [{:type "set-variable" :var-name "current-line-pos" :var-value {:y 775 :duration 0.5}}
                                                    {:type "set-variable" :var-name "current-line-jump" :var-value {:y 575 :duration 1}}
                                                    {:type "set-variable" :var-name "jump-wait" :var-value 900}
                                                    {:type     "case"
                                                     :from-var [{:var-name "current-line" :action-property "value"}]
                                                     :options  {:box1 {:type "action" :id "go-to-box2-line-down"}
                                                                :box2 {:type "action" :id "stay-on-line"}
                                                                :box3 {:type "action" :id "go-to-box2-line-up"}}}
                                                    {:type     "test-value"
                                                     :from-var [{:var-name "box2" :action-property "value1"}
                                                                {:var-name "current-concept" :action-property "value2"}]
                                                     :success  "pick-correct"
                                                     :fail     "pick-wrong"}]}

                   :go-to-box3-line         {:type "sequence-data"
                                             :data [{:type "set-variable" :var-name "current-line" :var-value "box3"}
                                                    {:type     "transition" :transition-id "vera"
                                                     :from-var [{:var-name "current-line-pos" :action-property "to"}]}]}

                   :pick-box-3              {:type "sequence-data"
                                             :data [{:type "set-variable" :var-name "current-line-pos" :var-value {:y 895 :duration 0.5}}
                                                    {:type "set-variable" :var-name "current-line-jump" :var-value {:y 695 :duration 1}}
                                                    {:type "set-variable" :var-name "jump-wait" :var-value 1400}
                                                    {:type     "test-value"
                                                     :value1   "box3"
                                                     :from-var [{:var-name "current-line" :action-property "value2"}]
                                                     :success  "stay-on-line"
                                                     :fail     "go-to-box3-line"}
                                                    {:type     "test-value"

                                                     :from-var [{:var-name "box3" :action-property "value1"}
                                                                {:var-name "current-concept" :action-property "value2"}]
                                                     :success  "pick-correct"
                                                     :fail     "pick-wrong"}]}

                   :current-concept-chant   {:type     "action"
                                             :from-var [{:var-name     "current-concept"
                                                         :var-property "gamevoice-chanting"}]}

                   :mari-voice-welcome      {:type        "parallel"
                                             :description "Hola amigo! Es hora de ponernos activos!
                                                          Mientras corres alrededor de la pista, escucharas una cancion.
                                                          Toca la foto que va con la cancion"
                                             :data        [{:type "audio" :id "mari" :start 0.710 :duration 13.816}
                                                           {:type "animation-sequence" :target "mari" :track 1 :offset 0.710
                                                            :data [{:start 0.905 :end 1.925 :anim "talk"}
                                                                   {:start 2.360 :end 4.743 :anim "talk"}
                                                                   {:start 5.316 :end 10.677 :anim "talk"}
                                                                   {:start 11.697 :end 14.322 :anim "talk"}]}]}

                   :mari-voice-correct      {:type        "parallel"
                                             :description "Wiiii"
                                             :data        [{:type "audio" :id "mari" :start 16.291 :duration 1.661}
                                                           {:type "animation-sequence" :target "mari" :track 1 :offset 16.291
                                                            :data [{:start 16.405 :end 17.734 :anim "talk"}]}]}

                   :mari-voice-wrong        {:type        "parallel"
                                             :description "Ayy intentalo de nuevo, escucha atentamente!"
                                             :data        [{:type "audio" :id "mari" :start 19.704 :duration 4.537}
                                                           {:type "animation-sequence" :target "mari" :track 1 :offset 19.704
                                                            :data [{:start 19.830 :end 24.138 :anim "talk"}]}]}}
   :triggers      {:start {:on "start" :action "start"}
                   :stop  {:on "back" :action "stop"}}
   :metadata      {:autostart true
                   :prev      "stadium"}})
