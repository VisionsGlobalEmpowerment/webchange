(ns webchange.templates.library.running_time_limited
  (:require
    [webchange.templates.core :as core]))

(def m {:id          31
        :name        "Running (time limited)"
        :tags        ["Independent Practice"]
        :description "Running"
        :lesson-sets ["concepts-group"]
        :fields      [{:name "image-src"
                       :type "image"}
                      {:name "concept-name"
                       :type "string"}
                      {:name "letter"
                       :type "string"}]})

(def t {:assets        [{:url "/raw/img/stadium/running/bg_01.jpg" :type "image"}
                        {:url "/raw/img/stadium/running/bg_02.jpg" :type "image"}
                        {:url "/raw/img/stadium/running/bg_03.jpg" :type "image"}]
        :objects       {:background          {:type   "carousel"
                                              :x      0
                                              :y      0
                                              :width  1920
                                              :height 1080
                                              :first  "/raw/img/stadium/running/bg_01.jpg"
                                              :last   "/raw/img/stadium/running/bg_03.jpg"
                                              :next   "/raw/img/stadium/running/bg_02.jpg"}
                        :timer               {:type          "timer"
                                              :transition    "timer"
                                              :x             900
                                              :y             100
                                              :show-minutes  false
                                              :show-progress true
                                              :time          60
                                              :actions       {:end {:on "end" :type "action" :id "finish-game"}}}
                        :counter             {:type       "counter"
                                              :transition "counter"
                                              :x          480
                                              :y          100}
                        :box-target          {:type        "animation"
                                              :x           350
                                              :y           210
                                              :width       671
                                              :height      633
                                              :scale       {:x 0.25 :y 0.25}
                                              :scene-name  "box-target"
                                              :transition  "box-target"
                                              :anim        "idle2"
                                              :anim-offset {:x 0 :y -300}
                                              :loop        true
                                              :name        "boxes"
                                              :skin        "qwestion"
                                              :speed       0.3
                                              :start       true}
                        :line-1              {:type    "transparent"
                                              :x       0
                                              :y       540
                                              :width   1920
                                              :height  140
                                              :actions {:click {:id "go-line" :on "click" :type "action" :params {:line "box1"}}}}
                        :line-2              {:type    "transparent"
                                              :x       0
                                              :y       655
                                              :width   1920
                                              :height  140
                                              :actions {:click {:id "go-line" :on "click" :type "action" :params {:line "box2"}}}}
                        :line-3              {:type    "transparent"
                                              :x       0
                                              :y       790
                                              :width   1920
                                              :height  150
                                              :actions {:click {:id "go-line" :on "click" :type "action" :params {:line "box3"}}}}
                        :box-1-group         {:type       "group"
                                              :children   ["box1" "letter1"]
                                              :transition "box-1-group"
                                              :x          2000
                                              :y          683
                                              :states     {:init {:visible false} :reset {:x 2000 :visible true}}}
                        :box1                {:type        "animation"
                                              :x           0
                                              :y           0
                                              :width       671
                                              :height      633
                                              :scale       {:x -0.25 :y 0.25}
                                              :scene-name  "box1"
                                              :transition  "box1"
                                              :anim        "idle2"
                                              :anim-offset {:x 0 :y -300}
                                              :loop        true
                                              :name        "boxes"
                                              :skin        "empty"
                                              :speed       0.3
                                              :start       true}
                        :letter1             {:type           "text"
                                              :x              -80
                                              :y              -150
                                              :width          150
                                              :height         150
                                              :transition     "letter1"
                                              :align          "center"
                                              :fill           0xff9000
                                              :font-family    "Lexend Deca"
                                              :font-size      120
                                              :shadow-blur    5
                                              :shadow-color   "#1a1a1a"
                                              :shadow-opacity 0.5
                                              :text           "?"
                                              :vertical-align "middle"}

                        :box-2-group         {:type       "group"
                                              :children   ["box2" "letter2"]
                                              :transition "box-2-group"
                                              :x          2200
                                              :y          789
                                              :states     {:init {:visible false} :reset {:x 2200 :visible true}}}
                        :box2                {:type        "animation"
                                              :x           0
                                              :y           0
                                              :width       671
                                              :height      633
                                              :scale       {:x -0.25 :y 0.25}
                                              :scene-name  "box2"
                                              :transition  "box2"
                                              :anim        "idle2"
                                              :anim-offset {:x 0 :y -300}
                                              :loop        true
                                              :name        "boxes"
                                              :skin        "empty"
                                              :speed       0.3
                                              :start       true}
                        :letter2             {:type           "text"
                                              :x              -90
                                              :y              -150
                                              :width          150
                                              :height         150
                                              :transition     "letter2"
                                              :align          "center"
                                              :fill           0xff9000
                                              :font-family    "Lexend Deca"
                                              :font-size      120
                                              :shadow-blur    5
                                              :shadow-color   "#1a1a1a"
                                              :shadow-opacity 0.5
                                              :text           "?"
                                              :vertical-align "middle"}

                        :box-3-group         {:type       "group"
                                              :children   ["box3" "letter3"]
                                              :transition "box-3-group"
                                              :x          2400
                                              :y          932
                                              :states     {:init {:visible false} :reset {:x 2400 :visible true}}}
                        :box3                {:type        "animation"
                                              :x           0
                                              :y           0
                                              :width       671
                                              :height      633
                                              :scale       {:x -0.25 :y 0.25}
                                              :scene-name  "box3"
                                              :transition  "box3"
                                              :anim        "idle2"
                                              :anim-offset {:x 0 :y -300}
                                              :loop        true
                                              :name        "boxes"
                                              :skin        "empty"
                                              :speed       0.3
                                              :start       true}
                        :letter3             {:type           "text"
                                              :x              -100
                                              :y              -150
                                              :width          150
                                              :height         150
                                              :transition     "letter3"
                                              :align          "center"
                                              :fill           0xff9000
                                              :font-family    "Lexend Deca"
                                              :font-size      120
                                              :shadow-blur    5
                                              :shadow-color   "#1a1a1a"
                                              :shadow-opacity 0.5
                                              :text           "?"
                                              :vertical-align "middle"}

                        :mari                {:type        "animation"
                                              :x           1265
                                              :y           311
                                              :width       473
                                              :height      511
                                              :scene-name  "mari"
                                              :transition  "mari"
                                              :anim        "idle"
                                              :anim-offset {:x 0 :y -150}
                                              :name        "mari"
                                              :scale-x     0.5
                                              :scale-y     0.5
                                              :speed       0.5
                                              :start       true}
                        :vera-group          {:type       "group"
                                              :x          500
                                              :y          775
                                              :transition "vera-group"
                                              :children   ["vera" "vera-collision-test"]}
                        :vera                {:type       "animation"
                                              :x          0
                                              :y          0
                                              :width      727
                                              :height     1091
                                              :scene-name "vera"
                                              :transition "vera"
                                              :anim       "run"
                                              :meshes     true
                                              :name       "vera-90"
                                              :scale-x    0.4
                                              :scale-y    0.4
                                              :skin       "default"
                                              :speed      1
                                              :start      true}
                        :vera-collision-test {:type        "rectangle"
                                              :x           150
                                              :y           -50
                                              :width       10
                                              :height      10
                                              :transition  "vera-collision-test"
                                              :collidable? true
                                              :actions     {:collide {:on "collide" :test ["box1" "box2" "box3"] :type "action" :id "check-line" :pick-event-param "target"}}}}
        :scene-objects [["background"]
                        ["vera-group" "mari"]
                        ["box-1-group" "box-2-group" "box-3-group"]
                        ["box-target" "timer" "counter" "line-1" "line-2" "line-3"]]
        :actions       {:dialog-1-welcome        {:type               "sequence-data"
                                                  :editor-type        "dialog"
                                                  :concept-var        "current-concept"
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence" :phrase-text "New action" :audio nil}]}]
                                                  :phrase             "welcome"
                                                  :phrase-description "Welcome dialog"
                                                  :dialog-track       "1 Welcome"
                                                  :skippable          true}
                        :dialog-2-chant          {:type               "sequence-data"
                                                  :editor-type        "dialog"
                                                  :concept-var        "current-concept"
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence" :phrase-text "New action" :audio nil}]}]
                                                  :phrase             "chant"
                                                  :phrase-description "Chant dialog"
                                                  :dialog-track       "2 Chant"
                                                  :tags               ["instruction"]}
                        :dialog-3-correct        {:type               "sequence-data"
                                                  :editor-type        "dialog"
                                                  :concept-var        "current-concept"
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence" :phrase-text "New action" :audio nil}]}]
                                                  :phrase             "correct"
                                                  :phrase-description "Correct dialog"
                                                  :dialog-track       "3 Options"}
                        :dialog-4-wrong          {:type               "sequence-data"
                                                  :editor-type        "dialog"
                                                  :concept-var        "current-concept"
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence" :phrase-text "New action" :audio nil}]}]
                                                  :phrase             "wrong"
                                                  :phrase-description "Wrong dialog"
                                                  :dialog-track       "3 Options"}

                        :go-line                 {:type "sequence-data"
                                                  :data [{:type     "set-variable"
                                                          :var-name "previous-line"
                                                          :from-var [{:var-name "current-line" :action-property "var-value"}]}
                                                         {:type        "set-variable"
                                                          :var-name    "current-line"
                                                          :from-params [{:param-property "line" :action-property "var-value"}]}
                                                         {:type     "case"
                                                          :options  {:box1 {:id "vera-go-line-1" :type "action"}
                                                                     :box2 {:id "vera-go-line-2" :type "action"}
                                                                     :box3 {:id "vera-go-line-3" :type "action"}}
                                                          :from-var [{:var-name "current-line" :action-property "value"}]}]}

                        :vera-go-line-1          {:type     "test-value"
                                                  :from-var [{:var-name "previous-line" :action-property "value2"}]
                                                  :value1   "box1"
                                                  :success  "stay-on-line"
                                                  :fail     "go-to-box1-line"}

                        :vera-go-line-2          {:type     "test-value"
                                                  :from-var [{:var-name "previous-line" :action-property "value2"}]
                                                  :value1   "box2"
                                                  :success  "stay-on-line"
                                                  :fail     "go-to-box2-line"}

                        :vera-go-line-3          {:type     "test-value"
                                                  :from-var [{:var-name "previous-line" :action-property "value2"}]
                                                  :value1   "box3"
                                                  :success  "stay-on-line"
                                                  :fail     "go-to-box3-line"}

                        :go-to-box1-line         {:type "sequence-data"
                                                  :data [{:type "set-variable" :var-name "current-line-pos" :var-value {:y 675 :duration 0.5}}
                                                         {:type          "transition"
                                                          :from-var      [{:var-name "current-line-pos" :action-property "to"}]
                                                          :transition-id "vera-group"}]}
                        :go-to-box2-line         {:type "sequence-data"
                                                  :data [{:type "set-variable" :var-name "current-line-pos" :var-value {:y 775 :duration 0.5}}
                                                         {:type          "transition"
                                                          :from-var      [{:var-name "current-line-pos" :action-property "to"}]
                                                          :transition-id "vera-group"}]}
                        :go-to-box3-line         {:type "sequence-data"
                                                  :data [{:type "set-variable" :var-name "current-line-pos" :var-value {:y 895 :duration 0.5}}
                                                         {:type          "transition"
                                                          :from-var      [{:var-name "current-line-pos" :action-property "to"}]
                                                          :transition-id "vera-group"}]}

                        :init-vars               {:type "parallel"
                                                  :data [{:type "set-variable" :var-name "game-finished" :var-value false}
                                                         {:type "set-variable" :var-name "slot1" :var-value "box1"}
                                                         {:type "set-variable" :var-name "slot2" :var-value "box2"}
                                                         {:type "set-variable" :var-name "slot3" :var-value "box3"}
                                                         {:type "set-variable" :var-name "current-line" :var-value "box2"}]}

                        :renew-current-concept   {:type "sequence-data"
                                                  :data [{:data [{:id "init" :type "state" :target "box-1-group"}
                                                                 {:id "init" :type "state" :target "box-2-group"}
                                                                 {:id "init" :type "state" :target "box-3-group"}]
                                                          :type "parallel"}
                                                         {:id "wait-for-box-animations" :type "action"}
                                                         {:data [{:id "reset" :type "state" :target "box-1-group"}
                                                                 {:id "reset" :type "state" :target "box-2-group"}
                                                                 {:id "reset" :type "state" :target "box-3-group"}]
                                                          :type "parallel"}
                                                         {:id "wait-for-box-animations" :type "action"}
                                                         {:from        ["item-1-1" "item-1-2" "item-1-3" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8"]
                                                          :type        "vars-var-provider"
                                                          :on-end      "finish-activity"
                                                          :shuffled    true
                                                          :variables   ["current-concept"]
                                                          :provider-id "current-concept"}
                                                         {:from      ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8"]
                                                          :type      "vars-var-provider"
                                                          :unique    true
                                                          :from-var  [{:var-key         "concept-name"
                                                                       :var-name        "current-concept"
                                                                       :var-property    "concept-name"
                                                                       :action-property "exclude-property-values"}]
                                                          :shuffled  true
                                                          :variables ["pair-concept-1" "pair-concept-2"]}
                                                         {:from      ["current-concept" "pair-concept-1" "pair-concept-2"]
                                                          :type      "vars-var-provider"
                                                          :shuffled  true
                                                          :variables ["box1" "box2" "box3"]}
                                                         {:data [{:type       "set-slot"
                                                                  :target     "box-target"
                                                                  :from-var   [{:var-name "current-concept" :var-property "image-src" :action-property "image"}]
                                                                  :slot-name  "box1"
                                                                  :attachment {:x 40 :scale-x 4 :scale-y 4}}]
                                                          :type "parallel"}
                                                         {:data [{:type      "set-attribute"
                                                                  :target    "letter1"
                                                                  :from-var  [{:var-name "box1" :var-property "letter" :action-property "attr-value"}]
                                                                  :attr-name "text"}
                                                                 {:type      "set-attribute"
                                                                  :target    "letter2"
                                                                  :from-var  [{:var-name "box2" :var-property "letter" :action-property "attr-value"}]
                                                                  :attr-name "text"}
                                                                 {:type      "set-attribute"
                                                                  :target    "letter3"
                                                                  :from-var  [{:var-name "box3" :var-property "letter" :action-property "attr-value"}]
                                                                  :attr-name "text"}]
                                                          :type "parallel"}
                                                         {:data [{:id "dialog-2-chant" :type "action"}
                                                                 {:to {:x -700 :duration 7} :type "transition" :transition-id "box-1-group"}
                                                                 {:to {:x -500 :duration 7} :type "transition" :transition-id "box-2-group"}
                                                                 {:to {:x -300 :duration 7} :type "transition" :transition-id "box-3-group"}]
                                                          :type "parallel"}
                                                         {:type "remove-flow-tag" :tag "box"}
                                                         {:type     "test-var-scalar"
                                                          :var-name "game-finished"
                                                          :value    false
                                                          :success  "renew-current-concept"
                                                          :fail     "finish-activity"}]}

                        :check-line              {:type        "case"
                                                  :options     {:box1 {:id "check-line-1" :type "action"}
                                                                :box2 {:id "check-line-2" :type "action"}
                                                                :box3 {:id "check-line-3" :type "action"}}
                                                  :from-params [{:param-property "target" :action-property "value"}]}
                        :check-line-1            {:type "sequence-data"
                                                  :data [{:type     "test-value"
                                                          :from-var [{:var-name "box1" :action-property "value1"}
                                                                     {:var-name "current-concept" :action-property "value2"}]
                                                          :success  "pick-correct"
                                                          :fail     "pick-wrong"}]}
                        :check-line-2            {:type "sequence-data"
                                                  :data [{:type     "test-value"
                                                          :from-var [{:var-name "box2" :action-property "value1"}
                                                                     {:var-name "current-concept" :action-property "value2"}]
                                                          :success  "pick-correct"
                                                          :fail     "pick-wrong"}]}
                        :check-line-3            {:type "sequence-data"
                                                  :data [{:type     "test-value"
                                                          :from-var [{:var-name "box3" :action-property "value1"}
                                                                     {:var-name "current-concept" :action-property "value2"}]
                                                          :success  "pick-correct"
                                                          :fail     "pick-wrong"}]}

                        :pick-correct            {:type "sequence-data"
                                                  :data [{:id "dialog-3-correct" :type "action" :return-immediately true}
                                                         {:type "counter-inc" :target "counter"}
                                                         {:data [{:data               [{:id "run_jump" :type "animation" :target "vera" :loop false}
                                                                                       {:id "run" :loop true :type "add-animation" :target "vera"}]
                                                                  :return-immediately true
                                                                  :type               "sequence-data"}]
                                                          :type "parallel"}]}

                        :pick-wrong              {:type "sequence-data"
                                                  :data [{:id "dialog-4-wrong" :type "action"}]}

                        :renew-words             {:type "sequence-data"
                                                  :data
                                                        [{:from        "concepts-group"
                                                          :type        "lesson-var-provider"
                                                          :limit       3
                                                          :repeat      4
                                                          :shuffled    false
                                                          :variables   ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8"]
                                                          :provider-id "words-set"}
                                                         {:from "item-1" :type "copy-variable" :var-name "item-1-1"}
                                                         {:from "item-1" :type "copy-variable" :var-name "item-1-2"}
                                                         {:from "item-1" :type "copy-variable" :var-name "item-1-3"}]}

                        :start-scene             {:type "sequence"
                                                  :data ["start-activity"
                                                         "dialog-1-welcome"
                                                         "init-vars"
                                                         "renew-words"
                                                         "start-timer"
                                                         "renew-current-concept"]}

                        :finish-game             {:type "set-variable" :var-name "game-finished" :var-value true}
                        :start-timer             {:type "timer-start" :target "timer"}
                        :stay-on-line            {:type "empty" :duration 100}
                        :stop-scene              {:type "sequence" :data ["stop-activity"]}
                        :start-activity          {:type "start-activity"}
                        :stop-activity           {:type "stop-activity"}
                        :finish-activity         {:type "finish-activity"}
                        :wait-for-box-animations {:type "empty" :duration 100}}
        :triggers      {:stop {:on "back" :action "stop-scene"} :start {:on "start" :action "start-scene"}}
        :metadata      {:autostart true}})

(defn f
  [t args]
  t)

(core/register-template
  m
  (partial f t))

