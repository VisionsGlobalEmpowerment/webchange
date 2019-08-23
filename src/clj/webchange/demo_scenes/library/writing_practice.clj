(ns webchange.demo-scenes.library.writing-practice)

(def writing-practice-scene
  {:assets        [{:url "/raw/img/library/painting-tablet/background.jpg" :type "image"}
                   {:url "/raw/img/ui/back_button_01.png" :type "image"}
                   {:url "/raw/audio/l2/a7/L2_A7_Mari.m4a" :type "audio" :alias "mari voice"}
                   {:url "/raw/img/elements/squirrel.png" :type "image"}]
   :objects       {:background            {:type       "background"
                                           :scene-name "background"
                                           :src        "/raw/img/library/painting-tablet/background.jpg"}
                   :mari                  {:type       "animation"
                                           :scene-name "mari"
                                           :name       "mari"
                                           :transition "mari"
                                           :anim       "idle"
                                           :start      true
                                           :speed      0.35
                                           :x          1700
                                           :y          520
                                           :width      473
                                           :height     511
                                           :scale-x    0.5
                                           :scale-y    0.5}
                   :next-button           {:type     "image"
                                           :src      "/raw/img/ui/back_button_01.png"
                                           :x        1875
                                           :y        915
                                           :width    97
                                           :height   99
                                           :scale-x  -1
                                           :scale-y  1
                                           :rotation 0
                                           :actions  {:click {:type "action"
                                                              :id   "go-next"
                                                              :on   "click"}}}
                   :copybook              {:scene-name     "copybook"
                                           :type           "copybook"
                                           :line-height    180
                                           :padding-height 90
                                           :x              60
                                           :y              0
                                           :width          1800
                                           :height         1050
                                           :scale-x        1
                                           :scale-y        1
                                           :rotation       0
                                           }
                   :practice-canvas       {:type       "painting-area"
                                           :scene-name "practice-canvas"
                                           :var-name   "writing-practice-stage-1-pt-1"
                                           :x          0
                                           :y          0
                                           :width      1920
                                           :height     1080
                                           :scale-x    1
                                           :scale-y    1
                                           :rotation   0
                                           :tool       "felt-tip"
                                           :color      "#323232"
                                           :states     {:hidden  {:type "transparent"}
                                                        :visible {:type "painting-area"}}}
                   :letter-tutorial-1     {:type         "svg-path"
                                           :scene-name   "letter-tutorial-1"
                                           :data         ""
                                           :x            0
                                           :y            0
                                           :stroke       "#323232"
                                           :stroke-width 4
                                           :line-cap     "round"
                                           :scale-x      2
                                           :scale-y      2
                                           :rotation     0}
                   :letter-tutorial-2     {:type         "svg-path"
                                           :scene-name   "letter-tutorial-2"
                                           :data         ""
                                           :x            350
                                           :y            0
                                           :stroke       "#323232"
                                           :stroke-width 4
                                           :line-cap     "round"
                                           :scale-x      2
                                           :scale-y      2
                                           :rotation     0}
                   :letter-tutorial-3     {:type         "svg-path"
                                           :scene-name   "letter-tutorial-3"
                                           :data         ""
                                           :x            700
                                           :y            0
                                           :stroke       "#323232"
                                           :stroke-width 4
                                           :line-cap     "round"
                                           :scale-x      2
                                           :scale-y      2
                                           :rotation     0}
                   :letter-tutorial-group {:type     "transparent"
                                           :x        1980
                                           :y        1024
                                           :width    400
                                           :height   300
                                           :children ["letter-tutorial-1" "letter-tutorial-2" "letter-tutorial-3"]
                                           :states   {:hidden  {:type "transparent"}
                                                      :visible {:x    450
                                                                :y    30
                                                                :type "group"}}}
                   :letter-trace          {:type         "svg-path"
                                           :scene-name   "letter-trace"
                                           :data         ""
                                           :x            600
                                           :y            115
                                           :stroke       "#898989"
                                           :stroke-width 4
                                           :dash         [10 15]
                                           :line-cap     "round"
                                           :scale-x      2
                                           :scale-y      2
                                           :rotation     0
                                           :states       {:hidden  {:type "transparent"}
                                                          :visible {:type "painting-area"}}}
                   :letter-trace-matrix   {:type   "matrix"
                                           :el     "letter-trace"
                                           :x      170
                                           :y      80
                                           :width  1580
                                           :height 1000
                                           :dx     340
                                           :dy     360
                                           :skip   [0 0]
                                           :max    30}
                   :letter-path           {:type         "transparent"
                                           :scene-name   "letter-path"
                                           :duration     5000
                                           :animation    "stop"
                                           :path         ""
                                           :stroke       "#323232"
                                           :stroke-width 10
                                           :line-cap     "round"
                                           :fill         "transparent"
                                           :x            505
                                           :y            82
                                           :width        100
                                           :height       100
                                           :scale-x      2
                                           :scale-y      2
                                           :states       {:hidden  {:type "transparent"}
                                                          :visible {:type "animated-svg-path"}}}
                   :concept-card          {:type     "group"
                                           :x        60
                                           :y        0
                                           :width    400
                                           :height   300
                                           :children ["concept-image" "concept-word"]}
                   :concept-image         {:type     "image"
                                           :src      "/raw/img/elements/squirrel.png"
                                           :x        100
                                           :y        0
                                           :width    100
                                           :height   100
                                           :scale-x  2
                                           :scale-y  2
                                           :rotation 0}
                   :concept-word          {:type           "text"
                                           :text           "ardilla"
                                           :x              0
                                           :y              190
                                           :width          400
                                           :height         100
                                           :scale-x        1
                                           :scale-y        1
                                           :align          "center"
                                           :vertical-align "middle"
                                           :font-family    "Lexend Deca"
                                           :font-size      80
                                           :fill           "black"}}
   :scene-objects [["background" "copybook" "concept-card"]
                   ["letter-trace-matrix" "letter-path"]
                   ["letter-tutorial-group"]
                   ["practice-canvas" "next-button" "mari"]]
   :actions       {:start                       {:type "sequence"
                                                 :data ["start-activity"
                                                        "clear-instruction"
                                                        "init-vars"
                                                        "init-stage-1"
                                                        "mari-voice-intro"
                                                        "mari-shows-example"
                                                        "mari-voice-go-next-practice"
                                                        "mari-go-wait"]}

                   :stop                        {:type "sequence" :data ["stop-activity"]}

                   :start-activity              {:type "start-activity" :id "writing-practice"}

                   :stop-activity               {:type "stop-activity" :id "writing-practice"}

                   :clear-instruction           {:type "remove-flows" :flow-tag "instruction"}

                   :init-vars                   {:type "parallel"
                                                 :data [{:type "set-variable" :var-name "current-stage" :var-value 1}
                                                        {:type "set-variable" :var-name "current-page" :var-value 1}
                                                        {:type "calc" :var-name "pages-number" :operation "div-ceil" :value-1 30 :value-2 13}
                                                        {:type "set-variable" :var-name "letter-path" :var-value "M 80 25 A 40,40 0 1,0 80,75 M 80 10 L 80 90"}
                                                        {:type "set-variable" :var-name "letter-tutorial-path" :var-value "M 105 50 A 40,40 0 1,0 105,100 M 105 35 L 105 115 M 70 15 A 60,60 0 0,0 10,75 l 5 0 l -5 10 l -5 -10 l 5 0 M 130 35 L 130 100 l 5 0 l -5 10 l -5 -10 l 5 0 M 85 2 l 0 20 M 125 5 a 5 5 0 1 1 10 5 l -10 13 l 10 0"}]}

                   :init-stage-1                {:type "parallel"
                                                 :data [{:type     "set-attribute" :target "letter-trace" :attr-name "data"
                                                         :from-var [{:var-name "letter-path" :action-property "attr-value"}]}
                                                        {:type     "set-attribute" :target "letter-path" :attr-name "path"
                                                         :from-var [{:var-name "letter-path" :action-property "attr-value"}]}]}

                   :hide-stage-1                {:type "parallel"
                                                 :data [{:type "state" :target "letter-trace" :id "hidden"}
                                                        {:type "state" :target "letter-path" :id "hidden"}]}

                   :init-stage-2                {:type "parallel"
                                                 :data [{:type     "set-attribute" :target "letter-tutorial-1" :attr-name "data"
                                                         :from-var [{:var-name "letter-tutorial-path" :action-property "attr-value"}]}
                                                        {:type     "set-attribute" :target "letter-tutorial-2" :attr-name "data"
                                                         :from-var [{:var-name "letter-tutorial-path" :action-property "attr-value"}]}
                                                        {:type     "set-attribute" :target "letter-tutorial-3" :attr-name "data"
                                                         :from-var [{:var-name "letter-tutorial-path" :action-property "attr-value"}]}
                                                        {:type "state" :target "letter-tutorial-group" :id "visible"}]}

                   :go-next                     {:type "action" :id "check-page"}

                   :check-page                  {:type     "test-var-scalar"
                                                 :var-name "current-page"
                                                 :from-var [{:action-property "value" :var-name "pages-number"}]
                                                 :success  "check-stage"
                                                 :fail     "go-next-page"}

                   :check-stage                 {:type     "test-var-scalar"
                                                 :var-name "current-stage"
                                                 :value    2
                                                 :success  "stop"
                                                 :fail     "go-next-stage"}

                   :go-next-page                {:type "sequence-data"
                                                 :data [{:type "counter" :counter-action "increase" :counter-id "current-page"}
                                                        {:type "action" :id "update-painting-var"}]}

                   :go-next-stage               {:type "sequence-data"
                                                 :data [{:type "counter" :counter-action "increase" :counter-id "current-stage"}
                                                        {:type "counter" :counter-action "reset" :counter-value 0 :counter-id "current-page"}
                                                        {:type "action" :id "go-next-page"}
                                                        {:type "action" :id "hide-stage-1"}
                                                        {:type "action" :id "init-stage-2"}
                                                        {:type "action" :id "mari-voice-intro-2"}]}

                   :update-painting-var         {:type     "test-var-scalar"
                                                 :var-name "current-stage"
                                                 :value    1
                                                 :success  {:type     "set-attribute" :target "practice-canvas" :attr-name "var-name"
                                                            :from-var [{:action-property "attr-value" :var-name "current-page"
                                                                        :template        "writing-practice-stage-1-pt-%"}]}
                                                 :fail     {:type     "set-attribute" :target "practice-canvas" :attr-name "var-name"
                                                            :from-var [{:action-property "attr-value" :var-name "current-page"
                                                                        :template        "writing-practice-stage-2-pt-%"}]}}

                   :mari-shows-example          {:type "sequence-data"
                                                 :data [{:type          "transition"
                                                         :transition-id "mari"
                                                         :to            {:x 574 :y 62 :loop false :duration 2.5}}
                                                        {:type "state" :target "letter-path" :id "visible"}
                                                        {:type "parallel"
                                                         :data [{:type "path-animation" :state "play" :target "letter-path"}
                                                                {:type          "transition"
                                                                 :transition-id "mari"
                                                                 :to            {:path     ""
                                                                                 :origin   {:x 574 :y 62}
                                                                                 :scale    {:x 2 :y 2}
                                                                                 :duration 5}
                                                                 :from-var      [{:var-name "letter-path" :action-property "to.path"}]}]}
                                                        {:type "set-attribute" :target "mari" :attr-name "scale-x" :attr-value -0.5}
                                                        {:type          "transition"
                                                         :transition-id "mari"
                                                         :to            {:x 405 :y 435 :loop false :duration 1.5}}]}

                   :mari-go-wait                {:type          "transition"
                                                 :transition-id "mari"
                                                 :to            {:x 90 :y 790 :loop false :duration 2}}

                   :mari-voice-intro            {:type        "parallel"
                                                 :description "Escoge un lapiz o un color y sigue las flechas para rastrear la letra.
                                                    Recuerda seguir el modelo correcto de rastrear. Yo te ensenare como hacer el primero. "
                                                 :data        [{:type "audio" :id "/raw/audio/l2/a7/L2_A7_Mari.m4a" :start 32.357 :duration 17.005 :volume 0.2}
                                                               {:type "animation-sequence" :target "mari" :track 1 :offset 32.357
                                                                :data [{:start 33.026 :end 36.254 :anim "talk"}
                                                                       {:start 36.707 :end 40.367 :anim "talk"}
                                                                       {:start 41.627 :end 45.485 :anim "talk"}
                                                                       {:start 46.193 :end 49.047 :anim "talk"}]}]}

                   :mari-voice-try-again        {:type        "parallel"
                                                 :description "Toca aqui para seguir rastreando"
                                                 :data        [{:type "audio" :id "/raw/audio/l2/a7/L2_A7_Mari.m4a" :start 52.432 :duration 3.228 :volume 0.2}
                                                               {:type "animation-sequence" :target "mari" :track 1 :offset 52.432
                                                                :data [{:start 52.629 :end 55.562 :anim "talk"}]}]}

                   :mari-voice-good-job         {:type        "parallel"
                                                 :description "Buen trabajo rastreando la letra"
                                                 :data        [{:type "audio" :id "/raw/audio/l2/a7/L2_A7_Mari.m4a" :start 58.908 :duration 2.637 :volume 0.2}
                                                               {:type "animation-sequence" :target "mari" :track 1 :offset 58.908
                                                                :data [{:start 59.223 :end 62.529 :anim "talk"}]}]}

                   :mari-voice-go-next-practice {:type        "parallel"
                                                 :description "Haz click en la flecha para seguir con tu trabajo."
                                                 :data        [{:type "audio" :id "/raw/audio/l2/a7/L2_A7_Mari.m4a" :start 62.923 :duration 4.054 :volume 0.2}
                                                               {:type "animation-sequence" :target "mari" :track 1 :offset 62.923
                                                                :data [{:start 63.080 :end 66.859 :anim "talk"}]}]}

                   :mari-voice-intro-2          {:type        "parallel"
                                                 :description "En esta pagina, podras practicar a dibujar las letras O podras dibujar lo que gustes.
                                                            Cuando termines, haz click sobre la flecha para ir a tu proxima actividad."
                                                 :data        [{:type "audio" :id "/raw/audio/l2/a7/L2_A7_Mari.m4a" :start 70.402 :duration 16.848 :volume 0.2}
                                                               {:type "animation-sequence" :target "mari" :track 1 :offset 70.402
                                                                :data [{:start 70.677 :end 75.952 :anim "talk"}
                                                                       {:start 76.680 :end 79.298 :anim "talk"}
                                                                       {:start 80.046 :end 81.483 :anim "talk"}
                                                                       {:start 82.073 :end 86.915 :anim "talk"}]}]}

                   }
   :triggers      {:start {:on "start" :action "start"}
                   :stop  {:on "back" :action "stop"}}
   :metadata      {:autostart true
                   :prev      ""}})
