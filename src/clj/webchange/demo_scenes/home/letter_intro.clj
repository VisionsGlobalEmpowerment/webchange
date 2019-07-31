(ns webchange.demo-scenes.home.letter-intro)

(def letter-intro-scene
  {:assets        [{:url "/raw/img/casa/background.jpg", :size 10 :type "image"}
                   {:url "/raw/img/casa/painting_canvas.png", :size 10 :type "image"}
                   {:url "/raw/img/elements/squirrel.png", :size 10 :type "image"}
                   {:url "/raw/audio/l2/a2/L2_A2_Vaca_1.m4a", :size 2, :type "audio"}
                   {:url "/raw/audio/l2/a2/L2_A2_Vaca_2.m4a", :size 2, :type "audio"}
                   {:url "/raw/audio/l2/a2/L2_A2_Vaca_3.m4a", :size 2, :type "audio"}
                   {:url "/raw/audio/l2/a2/L2_A2_Mari.m4a", :size 2, :type "audio"}
                   {:url "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_1.m4a", :size 2, :type "audio"}
                   ]
   :audio         {:vaca-1 "/raw/audio/l2/a2/L2_A2_Vaca_1.m4a"
                   :vaca-2 "/raw/audio/l2/a2/L2_A2_Vaca_2.m4a"
                   :vaca-3 "/raw/audio/l2/a2/L2_A2_Vaca_3.m4a"
                   :mari   "/raw/audio/l2/a2/L2_A2_Mari.m4a"}
   :objects       {:background   {:type "background"
                                  :src  "/raw/img/casa/background.jpg"}

                   :senora-vaca  {:type    "animation"
                                  :name    "senoravaca"
                                  :anim    "idle"
                                  :x       380
                                  :y       1000
                                  :speed   0.3
                                  :width   351
                                  :height  717
                                  :start   true
                                  :actions {:click {:type "action"
                                                    :id   "vaca-click"
                                                    :on   "click"}}}
                   :mari         {:type       "animation"
                                  :name       "mari"
                                  :transition "mari"
                                  :anim       "idle"
                                  :start      true
                                  :speed      0.35
                                  :x          1600
                                  :y          635
                                  :width      473
                                  :height     511
                                  :scale-y    0.5
                                  :scale-x    0.5}

                   :canvas       {:type    "image"
                                  :src     "/raw/img/casa/painting_canvas.png"
                                  :x       650
                                  :y       130
                                  :width   287
                                  :height  430
                                  :scale-y 2
                                  :scale-x 2}

                   :image        {:type       "image"
                                  :transition "image"
                                  :src        "/raw/img/elements/squirrel.png"
                                  :x          1805
                                  :y          502
                                  :width      100
                                  :height     100
                                  :scale-y    1.3
                                  :scale-x    1.3
                                  :states     {:init-position {:x 1805 :y 502}}}

                   :letter-small {:type           "transparent"
                                  :text           "a"
                                  :align          "center"
                                  :vertical-align "middle"
                                  :x              1040
                                  :y              240
                                  :width          130
                                  :height         130
                                  :font-size      120
                                  :font-family    "Lato"
                                  :fill           "#b23c18"
                                  :states         {:big     {:font-size 160}
                                                   :normal  {:font-size 120}
                                                   :hidden  {:type "transparent"}
                                                   :visible {:type "text"}}
                                  :actions        {:click {:type "action"
                                                           :id   "small-letter-click"
                                                           :on   "click"}}}

                   :letter-big   {:type           "transparent"
                                  :text           "A"
                                  :align          "center"
                                  :vertical-align "middle"
                                  :x              910
                                  :y              240
                                  :width          130
                                  :height         130
                                  :font-size      120
                                  :font-family    "Lato"
                                  :fill           "#b23c18"
                                  :states         {:big     {:font-size 160}
                                                   :normal  {:font-size 120}
                                                   :hidden  {:type "transparent"}
                                                   :visible {:type "text"}}
                                  :actions        {:click {:type "action"
                                                           :id   "big-letter-click"
                                                           :on   "click"}}}

                   :word-1-first {:type        "transparent"
                                  :text        "a"
                                  :x           740
                                  :y           410
                                  :width       110
                                  :height      90
                                  :font-size   100
                                  :font-family "Lato"
                                  :font-weight "bold"
                                  :align       "right"
                                  :fill        "#b23c18"
                                  :states      {:normal  {:y 410 :font-size 100}
                                                :big     {:y 390 :font-size 120}
                                                :hidden  {:type "transparent"}
                                                :visible {:type "text"}}}
                   :word-1-rest  {:type        "transparent"
                                  :text        "rdilla"
                                  :x           855
                                  :y           410
                                  :width       315
                                  :height      90
                                  :font-size   100
                                  :font-family "Lato"
                                  :fill        "#b23c18"
                                  :states      {:normal  {:y 410 :font-size 100}
                                                :big     {:y 390 :font-size 120}
                                                :hidden  {:type "transparent"}
                                                :visible {:type "text"}}}

                   :word-2-first {:type        "transparent"
                                  :text        "A"
                                  :x           740
                                  :y           510
                                  :width       110
                                  :height      90
                                  :font-size   100
                                  :font-family "Lato"
                                  :align       "right"
                                  :fill        "#b23c18"
                                  :states      {:normal  {:y 510 :font-size 100}
                                                :big     {:y 490 :font-size 120}
                                                :hidden  {:type "transparent"}
                                                :visible {:type "text"}}}

                   :word-2-rest  {:type        "transparent"
                                  :text        "rdilla"
                                  :x           855
                                  :y           510
                                  :width       315
                                  :height      90
                                  :font-size   100
                                  :font-family "Lato"
                                  :fill        "#b23c18"
                                  :states      {:normal  {:y 510 :font-size 100}
                                                :big     {:y 490 :font-size 120}
                                                :hidden  {:type "transparent"}
                                                :visible {:type "text"}}}}

   :scene-objects [["background"]
                   ["canvas"]
                   ["letter-small" "letter-big" "word-1-first" "word-1-rest" "word-2-first" "word-2-rest"]
                   ["senora-vaca" "mari"]
                   ["image"]]

   :actions       {:clear-instruction                  {:type        "remove-flows"
                                                        :description "Remove flows"
                                                        :flow-tag    "instruction"}
                   :start-activity                     {:type        "sequence"
                                                        :description "Initial action"
                                                        :data        ["clear-instruction"
                                                                      "init-state"
                                                                      "stage-intro"
                                                                      "stage-sound-a"]}
                   :stage-intro                        {:type "sequence" :data ["vaca-voice-welcome"]}
                   :stage-sound-a                      {:type "sequence-data"
                                                        :data [{:type "state" :target "letter-small" :id "visible"}
                                                               {:type "empty" :duration 700}
                                                               {:type "action" :id "vaca-voice-give-word-a"}
                                                               {:type "empty" :duration 300}
                                                               {:type "empty" :duration 300}
                                                               {:type "transition" :transition-id "mari" :to {:x 1903 :y 557 :duration 0.7 :loop false}}
                                                               {:type "parallel"
                                                                :data [{:type "transition" :transition-id "mari" :to {:x 866 :y 295 :duration 2 :loop false}}
                                                                       {:type "transition" :transition-id "image" :to {:x 740 :y 240 :duration 2 :loop false}}]}
                                                               {:type "empty" :duration 1000}
                                                               {:type "parallel"
                                                                :data [{:type "transition" :transition-id "mari" :to {:x 1600 :y 635 :duration 1.5 :loop false}}
                                                                       {:type "action" :id "vaca-voice-listen-a"}
                                                                       ]}
                                                               {:type "action" :id "vaca-voice-say-with-me"}
                                                               {:type "empty" :duration 500}
                                                               {:type "action" :id "vaca-voice-chanting"}
                                                               {:type "empty" :duration 500}
                                                               {:type "action" :id "stage-write-2-ways"}]}
                   :stage-write-2-ways                 {:type "sequence" :data ["vaca-voice-2-ways-write"
                                                                                "stage-small-a"]}
                   :stage-small-a                      {:type "sequence-data"
                                                        :data [
                                                               {:type "state" :target "letter-small" :id "big"}
                                                               {:type "action" :id "vaca-voice-small-a"}
                                                               {:type "action" :id "vaca-voice-small-a-sound"}
                                                               {:type "action" :id "vaca-voice-a-first-sound"}
                                                               {:type "state" :target "word-1-first" :id "visible"}
                                                               {:type "state" :target "word-1-rest" :id "visible"}
                                                               {:type "parallel"
                                                                :data [
                                                                       {:type "action" :id "vaca-voice-ardilla"}
                                                                       {:type "sequence-data"
                                                                        :data [{:type "state" :target "word-1-first" :id "big"}
                                                                               {:type "empty" :duration 400}
                                                                               {:type "state" :target "word-1-first" :id "normal"}
                                                                               {:type "state" :target "word-1-rest" :id "big"}
                                                                               {:type "empty" :duration 500}
                                                                               {:type "state" :target "word-1-rest" :id "normal"}
                                                                               {:type "empty" :duration 500}
                                                                               {:type "state" :target "word-1-first" :id "big"}
                                                                               {:type "empty" :duration 400}
                                                                               {:type "state" :target "word-1-first" :id "normal"}
                                                                               {:type "state" :target "word-1-rest" :id "big"}
                                                                               {:type "empty" :duration 500}
                                                                               {:type "state" :target "word-1-rest" :id "normal"}]}]}
                                                               {:type "empty" :duration 1000}
                                                               {:type "action" :id "mari-voice-touch-small-a"}]}
                   :stage-big-a                        {:type "sequence-data"
                                                        :data [{:type "state" :target "letter-big" :id "visible"}
                                                               {:type "state" :target "letter-big" :id "big"}
                                                               {:type "action" :id "vaca-voice-big-a"}
                                                               {:type "state" :target "word-2-first" :id "visible"}
                                                               {:type "state" :target "word-2-rest" :id "visible"}
                                                               {:type "parallel"
                                                                :data [
                                                                       {:type "action" :id "vaca-voice-ardilla-2"}
                                                                       {:type "sequence-data"
                                                                        :data [{:type "state" :target "word-2-first" :id "big"}
                                                                               {:type "empty" :duration 400}
                                                                               {:type "state" :target "word-2-first" :id "normal"}
                                                                               {:type "state" :target "word-2-rest" :id "big"}
                                                                               {:type "empty" :duration 500}
                                                                               {:type "state" :target "word-2-rest" :id "normal"}
                                                                               {:type "empty" :duration 500}
                                                                               {:type "state" :target "word-2-first" :id "big"}
                                                                               {:type "empty" :duration 400}
                                                                               {:type "state" :target "word-2-first" :id "normal"}
                                                                               {:type "state" :target "word-2-rest" :id "big"}
                                                                               {:type "empty" :duration 500}
                                                                               {:type "state" :target "word-2-rest" :id "normal"}]}]}
                                                               {:type "empty" :duration 700}
                                                               {:type "action" :id "mari-voice-touch-big-a"}]}

                   :stage-repeat-all                   {:type "sequence-data"
                                                        :data [{:type "transition" :transition-id "mari" :to {:x 1325 :y 500 :duration 0.7 :loop false}}
                                                               {:type "action" :id "vaca-voice-insertion-chant"}
                                                               {:type "transition" :transition-id "mari" :to {:x 1325 :y 610 :duration 0.7 :loop false}}
                                                               {:type "action" :id "vaca-voice-insertion-chant"}
                                                               {:type "action" :id "stage-finish"}]}

                   :stage-other-words                  {:description "Other words that starts with sound a"
                                                        :type        "empty" :duration 100}

                   :stage-finish                       {:type "sequence-data"
                                                        :data [{:type "set-variable" :var-name "vaca-click-available" :var-value 1}
                                                               {:type "transition" :transition-id "mari" :to {:x 1600 :y 635 :duration 0.7 :loop false}}
                                                               {:type "action" :id "mari-voice-finish"}]}

                   :init-state                         {:type "parallel"
                                                        :data [{:type "set-variable" :var-name "small-a-clicked" :var-value 0}
                                                               {:type "set-variable" :var-name "big-a-clicked" :var-value 0}
                                                               {:type "set-variable" :var-name "vaca-click-available" :var-value 0}
                                                               {:type "state" :target "letter-small" :id "hidden"}
                                                               {:type "state" :target "letter-big" :id "hidden"}
                                                               {:type "state" :target "word-1-first" :id "hidden"}
                                                               {:type "state" :target "word-1-rest" :id "hidden"}
                                                               {:type "state" :target "word-2-first" :id "hidden"}
                                                               {:type "state" :target "word-2-rest" :id "hidden"}
                                                               {:type "state" :target "image" :id "init-position"}]}

                   :vaca-click                         {:type     "test-var-scalar"
                                                        :var-name "vaca-click-available"
                                                        :success  "vaca-click-success"
                                                        :fail     "vaca-click-fail"
                                                        :value    1}

                   :vaca-click-success                 {:type "sequence-data"
                                                        :data [{:type "action" :id "init-state"}
                                                               {:type "action" :id "stage-sound-a"}]}
                   :vaca-click-fail                    {:type "empty" :duration 100}

                   :small-letter-click                 {:type     "test-var-scalar"
                                                        :var-name "small-a-clicked"
                                                        :success  "small-letter-click-first"
                                                        :fail     "small-letter-click-further"
                                                        :value    0}
                   :small-letter-click-first           {:type "sequence-data"
                                                        :data [{:type "counter" :counter-action "increase" :counter-id "small-a-clicked"}
                                                               {:type "state" :target "letter-small" :id "normal"}
                                                               {:type "action" :id "vaca-voice-insertion-a-small"}
                                                               {:type "action" :id "stage-big-a"}]}
                   :small-letter-click-further         {:type "action" :id "vaca-voice-insertion-a-small"}

                   :big-letter-click                   {:type     "test-var-scalar"
                                                        :var-name "big-a-clicked"
                                                        :success  "big-letter-click-first"
                                                        :fail     "big-letter-click-further"
                                                        :value    0}
                   :big-letter-click-first             {:type "sequence-data"
                                                        :data [{:type "counter" :counter-action "increase" :counter-id "big-a-clicked"}
                                                               {:type "state" :target "letter-big" :id "normal"}
                                                               {:type "action" :id "vaca-voice-insertion-a-big"}
                                                               {:type "action" :id "stage-repeat-all"}]}
                   :big-letter-click-further           {:type "action" :id "vaca-voice-insertion-a-big"}

                   :vaca-voice-welcome                 {:type        "parallel"
                                                        :description "Hola pequeno genio! Hoy empezaremos a aprender sobre la letra “a.”"
                                                        :data        [{:type "audio" :id "vaca-1" :start 1.108 :duration 5.741 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 1.108
                                                                       :data [{:start 1.283 :end 2.634 :anim "talk"}
                                                                              {:start 3.309 :end 6.281 :anim "talk"}
                                                                              {:start 6.430 :end 6.686 :anim "talk"}]}]}
                   :vaca-voice-give-word-a             {:type        "parallel"
                                                        :description "Mari, puedes darme una palabra que comienze con la letra ‘a’?"
                                                        :data        [{:type "audio" :id "vaca-1" :start 7.578 :duration 4.674 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 7.578
                                                                       :data [{:start 7.754 :end 8.213 :anim "talk"}
                                                                              {:start 8.699 :end 11.549 :anim "talk"}
                                                                              {:start 11.630 :end 12.090 :anim "talk"}]}]}
                   :vaca-voice-listen-a                {:type        "parallel"
                                                        :description "Excellente! Tenemos una ardilla.  Escucha el primer sonida en la palabra ‘ardilla’.
                                                          Aaaaaaaaardilla. Aaaaaardilla. Cantalo conmigo! Ardilla, ardilla, a, a, a!!!"
                                                        :data        [{:type "audio" :id "vaca-1" :start 13.116 :duration 17.830 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 13.116
                                                                       :data [{:start 13.305 :end 14.616 :anim "talk"}
                                                                              {:start 15.318 :end 16.993 :anim "talk"}
                                                                              {:start 17.749 :end 20.991 :anim "talk"}
                                                                              {:start 21.640 :end 22.963 :anim "talk"}
                                                                              {:start 23.693 :end 24.990 :anim "talk"}
                                                                              {:start 25.665 :end 27.002 :anim "talk"}
                                                                              {:start 27.543 :end 30.812 :anim "talk"}]}]}
                   :vaca-voice-say-with-me             {:type        "parallel"
                                                        :description "Dilo conmigo! Cantalo tres veces!"
                                                        :data        [{:type "audio" :id "vaca-1" :start 31.527 :duration 4.255 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 31.527
                                                                       :data [{:start 31.703 :end 33.054 :anim "talk"}
                                                                              {:start 33.662 :end 34.364 :anim "talk"}
                                                                              {:start 34.634 :end 35.634 :anim "talk"}]}]}
                   :vaca-voice-chanting                {:type        "parallel"
                                                        :description "Ardilla, ardilla, a, a, a!!!  Ardilla, ardilla, a, a, a!!!  Ardilla, ardilla, a, a, a!!"
                                                        :data        [{:type "audio" :id "vaca-1" :start 36.127 :duration 11.043 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 36.127
                                                                       :data [{:start 36.495 :end 46.829 :anim "talk"}]}]}
                   :vaca-voice-2-ways-write            {:type        "parallel"
                                                        :description "Bueno! Hay dos formas de escribir este sonido.
                                                          Puedes escribir con ‘a’ miniscula o ‘a’ mayuscula."
                                                        :data        [{:type "audio" :id "vaca-1" :start 47.750 :duration 10.496 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 47.750
                                                                       :data [{:start 47.886 :end 48.385 :anim "talk"}
                                                                              {:start 48.926 :end 51.816 :anim "talk"}
                                                                              {:start 52.654 :end 54.437 :anim "talk"}
                                                                              {:start 54.856 :end 55.747 :anim "talk"}
                                                                              {:start 56.247 :end 58.098 :anim "talk"}]}]}
                   :vaca-voice-small-a                 {:type        "parallel"
                                                        :description "Esta es la ‘a’ miniscula."
                                                        :data        [{:type "audio" :id "vaca-2" :start 1.286 :duration 2.908 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 1.286
                                                                       :data [{:start 1.365 :end 1.948 :anim "talk"}
                                                                              {:start 2.285 :end 4.134 :anim "talk"}]}]}
                   :vaca-voice-small-a-sound           {:type        "parallel"
                                                        :description "Esta letra ‘a’ hace el sonido ‘aaaaaaa’."
                                                        :data        [{:type "audio" :id "vaca-2" :start 5.172 :duration 4.609 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 5.172
                                                                       :data [{:start 5.242 :end 5.835 :anim "talk"}
                                                                              {:start 6.082 :end 6.735 :anim "talk"}
                                                                              {:start 6.943 :end 7.457 :anim "talk"}
                                                                              {:start 7.961 :end 9.662 :anim "talk"}]}]}
                   :vaca-voice-a-first-sound           {:type        "parallel"
                                                        :description "Aaaaa es el primer sonido en la palabra ardilla.  Aaaaa es para ardilla."
                                                        :data        [{:type "audio" :id "vaca-2" :start 11.561 :duration 5.021 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 11.561
                                                                       :data [{:start 11.670 :end 12.392 :anim "talk"}
                                                                              {:start 12.966 :end 16.140 :anim "talk"}]}]}
                   :vaca-voice-ardilla                 {:type        "parallel"
                                                        :description "Aaaaaardilla.  Aaaaaardilla."
                                                        :data        [{:type "audio" :id "vaca-2" :start 20.907 :duration 2.581 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 20.907
                                                                       :data [{:start 21.046 :end 21.867 :anim "talk"}
                                                                              {:start 22.450 :end 23.350 :anim "talk"}]}]}
                   :vaca-voice-big-a                   {:type        "parallel"
                                                        :description "Esta es otra forma de escribir sonido ‘aaaa’.
                                                            Esta es la ‘a’ mayuscula. La ‘a’ mayuscula tambien suena ‘aaaaaa’."
                                                        :data        [{:type "audio" :id "vaca-2" :start 25.783 :duration 11.443 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 25.783
                                                                       :data [{:start 25.872 :end 29.314 :anim "talk"}
                                                                              {:start 30.520 :end 33.121 :anim "talk"}
                                                                              {:start 33.913 :end 37.107 :anim "talk"}]}]}
                   :vaca-voice-ardilla-2               {:type        "parallel"
                                                        :description "Aaaaaardilla.  Aaaaaardilla."
                                                        :data        [{:type "audio" :id "vaca-2" :start 38.066 :duration 3.452 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 38.066
                                                                       :data [{:start 38.165 :end 39.382 :anim "talk"}
                                                                              {:start 40.203 :end 41.419 :anim "talk"}]}]}
                   :mari-voice-touch-small-a           {:type        "parallel"
                                                        :description "Toca la ‘a’ minuscula para escuchar como suena."
                                                        :data        [{:type "audio" :id "mari" :start 1.414 :duration 4.973}
                                                                      {:type "animation-sequence" :target "mari" :track 1 :offset 1.414
                                                                       :data [{:start 1.824 :end 6.313 :anim "talk"}]}]}
                   :mari-voice-touch-big-a             {:type        "parallel"
                                                        :description "Toca la ‘a’ mayuscula para escuchar como suena."
                                                        :data        [{:type "audio" :id "mari" :start 10.645 :duration 5.360}
                                                                      {:type "animation-sequence" :target "mari" :track 1 :offset 10.645
                                                                       :data [{:start 10.764 :end 15.871 :anim "talk"}]}]}
                   :mari-voice-finish                  {:type        "parallel"
                                                        :description "Toca a la Senora Vaca para escuchar esta lecion otra vez,
                                                            o toca la flecha para ir a tu proxima actividad!"
                                                        :data        [{:type "audio" :id "mari" :start 22.481 :duration 9.915}
                                                                      {:type "animation-sequence" :target "mari" :track 1 :offset 22.481
                                                                       :data [{:start 22.570 :end 32.322 :anim "talk"}]}]}
                   :vaca-voice-insertion-a-big         {:type        "parallel"
                                                        :description "A"
                                                        :data        [{:type  "audio" :id "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_1.m4a"
                                                                       :start 0.539 :duration 1.078 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 0.539
                                                                       :data [{:start 0.859 :end 1.196 :anim "talk"}]}]}
                   :vaca-voice-insertion-ardilla-big   {:type        "parallel"
                                                        :description "Ardilla"
                                                        :data        [{:type  "audio" :id "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_1.m4a"
                                                                       :start 2.089 :duration 1.162 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 2.089
                                                                       :data [{:start 2.257 :end 3.083 :anim "talk"}]}]}
                   :vaca-voice-insertion-a-small       {:type        "parallel"
                                                        :description "a"
                                                        :data        [{:type  "audio" :id "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_1.m4a"
                                                                       :start 3.723 :duration 0.893 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 3.723
                                                                       :data [{:start 3.908 :end 4.430 :anim "talk"}]}]}
                   :vaca-voice-insertion-ardilla-small {:type        "parallel"
                                                        :description "ardilla"
                                                        :data        [{:type  "audio" :id "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_1.m4a"
                                                                       :start 5.256 :duration 1.482 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 5.256
                                                                       :data [{:start 5.509 :end 6.553 :anim "talk"}]}]}
                   :vaca-voice-insertion-ardilla-twice {:type        "parallel"
                                                        :description "Ardilla, ardilla"
                                                        :data        [{:type  "audio" :id "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_1.m4a"
                                                                       :start 7.058 :duration 2.342 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 7.058
                                                                       :data [{:start 7.311 :end 9.265 :anim "talk"}]}]}
                   :vaca-voice-insertion-chant         {:type        "parallel"
                                                        :description "Ardilla, ardilla, a, a, a!!!"
                                                        :data        [{:type  "audio" :id "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_1.m4a"
                                                                       :start 6.957 :duration 4.430 :volume 1.33}
                                                                      {:type "animation-sequence" :target "senoravaca" :track 1 :offset 6.957
                                                                       :data [{:start 7.311 :end 11.068 :anim "talk"}]}]}}
   :triggers      {:start {:on "start" :action "start-activity"}}
   :metadata      {:autostart true
                   :prev      "map"}})
