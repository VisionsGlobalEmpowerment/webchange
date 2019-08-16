(ns webchange.demo-scenes.library.writing-lesson)

(def writing-lesson-scene
  {:assets        [{:url "/raw/img/library/main/background.jpg" :type "image"}
                   {:url "/raw/img/library/main/Easel_Enabled.png" :type "image"}
                   {:url "/raw/audio/l2/a6/L2_A6_Mari.m4a" :type "audio" :alias "mari voice"}
                   {:url "/raw/audio/l2/a6/L2_A6_Vaca_1.m4a" :type "audio" :alias "vaca voice 1"}
                   {:url "/raw/audio/l2/a6/L2_A6_Vaca_2.m4a" :type "audio" :alias "vaca voice 2"}
                   {:url "/raw/audio/l2/a6/L2_A6_Vaca_3.m4a" :type "audio" :alias "vaca voice 3"}
                   {:url "/raw/audio/l2/a6/L2_A6_Vaca_4.m4a" :type "audio" :alias "vaca voice 4"}]
   :objects       {:background  {:type "background" :src "/raw/img/library/main/background.jpg"}
                   :mari        {:type       "animation"
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
                   :senora-vaca {:type       "animation"
                                 :scene-name "senoravaca"
                                 :name       "senoravaca"
                                 :anim       "idle"
                                 :start      true
                                 :speed      0.3
                                 :x          270
                                 :y          1015
                                 :width      351
                                 :height     717
                                 :scale-x    1
                                 :scale-y    1}
                   :canvas      {:type    "image"
                                 :src     "/raw/img/library/main/Easel_Enabled.png"
                                 :x       700
                                 :y       333
                                 :width   287
                                 :height  430
                                 :scale-x 1
                                 :scale-y 1}
                   :picture-box {:type       "transparent"
                                 :name       "boxes"
                                 :transition "picture-box"
                                 :scene-name "picture-box"
                                 :skin       "squirrel"
                                 :x          595
                                 :y          645
                                 :width      671
                                 :height     633
                                 :scale      {:x 0.25 :y 0.25}
                                 :anim       "idle2"
                                 :speed      0.3
                                 :loop       true
                                 :start      true
                                 :states     {:hidden  {:type "transparent"}
                                              :visible {:type "animation"}}}
                   :letter-path {:type         "transparent"
                                 :scene-name   "letter-path"
                                 :duration     3000
                                 :animation    "stop"
                                 :path         "M 80 25 A 40,40 0 1,0 80,75 M 80 10 L 80 90"
                                 :stroke       "white"
                                 :stroke-width 15
                                 :line-cap     "round"
                                 :fill         "transparent"
                                 :x            930
                                 :y            610
                                 :width        100
                                 :height       100
                                 :scale-x      2
                                 :scale-y      2
                                 :states       {:hidden  {:type "transparent"}
                                                :visible {:type "animated-svg-path"}}}}
   :scene-objects [["background"] ["canvas" "picture-box" "letter-path"] ["mari" "senora-vaca"]]
   :actions       {:start                          {:type "sequence"
                                                    :data ["start-activity"
                                                           "clear-instruction"
                                                           "welcome"
                                                           "find-bear"
                                                           "introduce-picture"
                                                           "introduce-letter"
                                                           "draw-letter"
                                                           "invite-user"]}

                   :stop                           {:type "sequence" :data ["stop-activity"]}

                   :start-activity                 {:type "start-activity" :id "writing-lesson"}

                   :stop-activity                  {:type "stop-activity" :id "writing-lesson"}

                   :clear-instruction              {:type "remove-flows" :flow-tag "instruction"}

                   :start-letter-path              {:type       "set-attribute"
                                                    :target     "letter-path"
                                                    :attr-name  "animation"
                                                    :attr-value "play"}

                   :welcome                        {:type "sequence" :data ["vaca-voice-welcome"]}

                   :find-bear                      {:type "sequence" :data ["vaca-voice-find-bear"
                                                                            "mari-move-to-letter"
                                                                            "mari-voice-here-he-is"
                                                                            "vaca-clapping"
                                                                            "mari-voice-click-bear"
                                                                            "vaca-voice-thanks-to-bear"
                                                                            "bear-smiles"]}

                   :introduce-picture              {:type "sequence" :data ["vaca-asks-sound"
                                                                            "show-current-word-picture"
                                                                            "vaca-letter-pronouncing"
                                                                            "vaca-picture-pointing"]}

                   :introduce-letter               {:type "sequence" :data ["vaca-voice-sound-look"
                                                                            "show-letter"
                                                                            "pronounce-letter"]}

                   :draw-letter                    {:type "sequence" :data ["vaca-voice-help-mari"
                                                                            "mari-voice-sure"
                                                                            "letter-drawing"]}

                   :invite-user                    {:type "sequence" :data ["vaca-voice-your-turn"
                                                                            "mari-voice-click-to-practice"]}

                   ;; ---

                   :letter-drawing                 {:type "sequence-data"
                                                    :data [{:type "parallel"
                                                            :data [{:type "sequence" :data ["vaca-voice-watch-mari"
                                                                                            "vaca-voice-current-letter"]} ; <- replace
                                                                   {:type "action" :id "letter-drawing-animation"}]}
                                                           {:type "action" :id "bear-chuckles"}
                                                           {:type "action" :id "vaca-voice-well-done"}
                                                           {:type "empty" :duration 100}
                                                           {:type "action" :id "letter-drawing-animation"}
                                                           {:type "action" :id "bear-chuckles"}]}

                   :letter-drawing-animation       {:type "sequence-data"
                                                    :data [{:type "path-animation" :state "reset" :target "letter-path"}
                                                           {:type "path-animation" :state "play" :target "letter-path"}]}

                   :bear-chuckles                  {:type "empty" :duration 100}

                   :show-letter                    {:type "state" :target "letter-path" :id "visible"}

                   :pronounce-letter               {:type "sequence-data"
                                                    :data [{:type "action" :id "vaca-voice-letter-make-sound-1"}
                                                           ;; a
                                                           {:type "action" :id "vaca-voice-current-letter"} ; <- replace
                                                           {:type "empty" :duration 300}
                                                           {:type "action" :id "vaca-voice-letter-make-sound-2"}
                                                           ;; a
                                                           {:type "action" :id "vaca-voice-current-letter"} ; <- replace
                                                           {:type "action" :id "vaca-voice-letter-make-sound-3"}
                                                           ;; aaaaaaaa
                                                           {:type "action" :id "vaca-voice-current-sound"} ; <- replace
                                                           {:type "empty" :duration 300}
                                                           {:type "action" :id "vaca-voice-letter-make-sound-4"}
                                                           ;; Aaa aaaa aaaa
                                                           {:type "action" :id "vaca-voice-a-long-1"} ; <- replace
                                                           {:type "empty" :duration 300}
                                                           {:type "action" :id "vaca-voice-a-long-2"} ; <- replace
                                                           {:type "empty" :duration 300}
                                                           {:type "action" :id "vaca-voice-a-long-3"} ; <- replace
                                                           ]}

                   :vaca-clapping                  {:type "sequence-data"
                                                    :data [{:type "animation" :target "senoravaca"
                                                            :id   "clapping_start" :loop false}
                                                           {:type  "animation-sequence" :target "senoravaca"
                                                            :track 1 :offset 0 :data [{:start 0 :end 3 :anim "clapping_1clap"}]}
                                                           {:type "animation" :target "senoravaca"
                                                            :id   "clapping_finish" :loop false}]}

                   :mari-move-to-letter            {:type          "transition"
                                                    :transition-id "mari"
                                                    :to            {:x 1230 :y 815 :loop false :duration 1.5}}

                   :bear-smiles                    {:type "empty" :duration 100}

                   :vaca-asks-sound                {:type "sequence-data"
                                                    :data [{:type "action" :id "vaca-voice-pay-attention"}
                                                           {:type "action" :id "vaca-voice-current-letter"}]} ; <- replace

                   :show-current-word-picture      {:type "sequence-data"
                                                    :data [{:type "state" :target "picture-box" :id "visible"}
                                                           {:type  "animation-sequence" :target "picture-box"
                                                            :track 1 :offset 0 :data [{:start 0 :end 1 :anim "come2"}]}
                                                           {:type "empty" :duration 100}
                                                           {:type "set-skin" :target "picture-box"
                                                            :skin "squirrel"}]} ; <- replace

                   :vaca-letter-pronouncing        {:type "sequence-data"
                                                    :data [{:type "action" :id "vaca-voice-current-word"} ; <- replace
                                                           {:type "action" :id "vaca-voice-starts-with"}
                                                           {:type "action" :id "vaca-voice-current-sound"} ; <- replace
                                                           {:type "empty" :duration 500}
                                                           {:type "action" :id "vaca-voice-sound-and-word"}]} ; <- replace

                   :vaca-picture-pointing          {:type  "animation-sequence" :target "senoravaca"
                                                    :track 1 :offset 0 :data [{:start 0 :end 2 :anim "hand"}]}

                   :vaca-voice-current-letter      {:type        "parallel"
                                                    :description "a"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_2.m4a" :start 8.414 :duration 0.454}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 8.414
                                                                   :data [{:start 8.414 :end 8.868 :anim "talk"}]}]}

                   :vaca-voice-current-word        {:type        "parallel"
                                                    :description "ardilla"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_2.m4a" :start 9.439 :duration 0.802}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 9.439
                                                                   :data [{:start 9.439 :end 10.241 :anim "talk"}]}]}

                   :vaca-voice-current-sound       {:type        "parallel"
                                                    :description "aaaa"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_2.m4a" :start 11.476 :duration 0.728}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 11.476
                                                                   :data [{:start 11.476 :end 12.205 :anim "talk"}]}]}

                   :vaca-voice-sound-and-word      {:type        "parallel"
                                                    :description "‘aaaaaa’ ‘aaaaaa’. ‘aaaaaaaardilla’.  ‘aaaaaaaardilla’"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_2.m4a" :start 13.250 :duration 5.564}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 13.250
                                                                   :data [{:start 13.355 :end 14.094 :anim "talk"}
                                                                          {:start 14.728 :end 15.340 :anim "talk"}
                                                                          {:start 15.752 :end 16.956 :anim "talk"}
                                                                          {:start 17.769 :end 18.666 :anim "talk"}]}]}

                   :vaca-voice-help-mari           {:type "empty" :duration 100}

                   :vaca-voice-a-long-1            {:type        "parallel"
                                                    :description "a"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_3.m4a" :start 10.357 :duration 0.833}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 10.357
                                                                   :data [{:start 10.357 :end 11.190 :anim "talk"}]}]}

                   :vaca-voice-a-long-2            {:type        "parallel"
                                                    :description "a"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_3.m4a" :start 12.007 :duration 0.817}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 12.007
                                                                   :data [{:start 12.007 :end 12.825 :anim "talk"}]}]}

                   :vaca-voice-a-long-3            {:type        "parallel"
                                                    :description "a"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_3.m4a" :start 13.610 :duration 0.739}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 13.610 :data [{:start 13.610 :end 14.349 :anim "talk"}]}]}


                   :vaca-voice-welcome             {:type        "parallel"
                                                    :description "Bienvenido a la biblioteca pequeno genio! Me encantan las bibliotecas.
                                                                Son lugares para crecer la mente."
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_1.m4a" :start 2.466 :duration 8.953}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 2.466
                                                                   :data [{:start 2.623 :end 5.386 :anim "talk"}
                                                                          {:start 5.998 :end 7.904 :anim "talk"}
                                                                          {:start 8.586 :end 9.355 :anim "talk"}
                                                                          {:start 9.705 :end 11.209 :anim "talk"}]}]}

                   :vaca-voice-find-bear           {:type        "parallel"
                                                    :description "Pero primero, tenemos que aprender a leer!
                                                                Tengo un amigo que me gustaria que conocieras!
                                                                Mari, donde esta el Senor Abrazo?"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_1.m4a" :start 12.398 :duration 13.080}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 12.398
                                                                   :data [{:start 12.642 :end 16.175 :anim "talk"}
                                                                          {:start 17.539 :end 20.878 :anim "talk"}
                                                                          {:start 22.120 :end 22.680 :anim "talk"}
                                                                          {:start 23.134 :end 25.145 :anim "talk"}]}]}

                   :vaca-voice-thanks-to-bear      {:type        "parallel"
                                                    :description "Ah si. Buenos dias Senor Abrazo. Usted se ve muy mimoso como siempre!
                                                                Gracias por dejarnos dibujar sobre su pancita!"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_1.m4a" :start 28.083 :duration 10.929}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 28.083
                                                                   :data [{:start 28.275 :end 29.254 :anim "talk"}
                                                                          {:start 29.604 :end 31.318 :anim "talk"}
                                                                          {:start 31.877 :end 33.731 :anim "talk"}
                                                                          {:start 34.255 :end 35.375 :anim "talk"}
                                                                          {:start 35.969 :end 38.837 :anim "talk"}]}]}

                   :vaca-voice-pay-attention       {:type        "parallel"
                                                    :description "Ahora, pongan atencion amigos. Sabian que cada foto empieza con el sonido"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_2.m4a" :start 1.541 :duration 6.905}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 1.541
                                                                   :data [{:start 1.753 :end 2.249 :anim "talk"}
                                                                          {:start 2.587 :end 4.054 :anim "talk"}
                                                                          {:start 5.036 :end 6.704 :anim "talk"}
                                                                          {:start 7.021 :end 8.330 :anim "talk"}]}]}

                   :vaca-voice-starts-with         {:type        "parallel"
                                                    :description "empieza con el sonido"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_2.m4a" :start 10.114 :duration 1.130}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 10.114
                                                                   :data [{:start 10.114 :end 11.244 :anim "talk"}]}]}

                   :mari-voice-here-he-is          {:type        "parallel"
                                                    :description "Aqui esta!"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Mari.m4a" :start 0.440 :duration 1.951}
                                                                  {:type "animation-sequence" :target "mari" :track 1 :offset 0.440
                                                                   :data [{:start 0.670 :end 2.200 :anim "talk"}]}]}

                   :mari-voice-click-bear          {:type        "parallel"
                                                    :description "Haz clic sobre el oso para empezar tu proxima lecion."
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Mari.m4a" :start 2.392 :duration 4.429}
                                                                  {:type "animation-sequence" :target "mari" :track 1 :offset 2.392
                                                                   :data [{:start 2.592 :end 6.687 :anim "talk"}]}]}

                   :mari-voice-sure                {:type        "parallel"
                                                    :description "Claro que si!"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Mari.m4a" :start 11.632 :duration 1.444}
                                                                  {:type "animation-sequence" :target "mari" :track 1 :offset 11.632
                                                                   :data [{:start 11.757 :end 12.953 :anim "talk"}]}]}

                   :mari-voice-click-to-practice   {:type        "parallel"
                                                    :description "Haz click sobre la flecha para practicar rastreando letras!
                                                                Si quieres ver a la Senora Vaca ensenarle al Senor Abrazo, haz click sobre el oso! "
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Mari.m4a" :start 16.817 :duration 12.723}
                                                                  {:type "animation-sequence" :target "mari" :track 1 :offset 16.817
                                                                   :data [{:start 16.894 :end 21.849 :anim "talk"}
                                                                          {:start 22.289 :end 24.671 :anim "talk"}
                                                                          {:start 25.149 :end 27.206 :anim "talk"}
                                                                          {:start 27.608 :end 29.387 :anim "talk"}]}]}

                   :vaca-voice-sound-look          {:type        "parallel"
                                                    :description "Asi es como se ve este sonido."
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_2.m4a" :start 19.838 :duration 2.555}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 19.838
                                                                   :data [{:start 20.007 :end 20.619 :anim "talk"}
                                                                          {:start 21.041 :end 22.277 :anim "talk"}]}]}

                   :vaca-voice-letter-make-sound-1 {:type        "parallel"
                                                    :description "Esta es la letra"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_3.m4a" :start 1.194 :duration 1.682}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 1.194
                                                                   :data [{:start 1.289 :end 2.813 :anim "talk"}]}]}

                   :vaca-voice-letter-make-sound-2 {:type        "parallel"
                                                    :description "La letra"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_3.m4a" :start 3.913 :duration 0.974}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 3.913
                                                                   :data [{:start 4.039 :end 4.715 :anim "talk"}]}]}

                   :vaca-voice-letter-make-sound-3 {:type        "parallel"
                                                    :description "hace el sonido"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_3.m4a" :start 5.611 :duration 1.116}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 5.611
                                                                   :data [{:start 5.752 :end 6.617 :anim "talk"}]}]}

                   :vaca-voice-letter-make-sound-4 {:type        "parallel"
                                                    :description "Puedes decirlo conmigo?"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_3.m4a" :start 8.204 :duration 1.776}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 8.204
                                                                   :data [{:start 8.314 :end 9.713 :anim "talk"}]}]}

                   :vaca-voice-watch-mari          {:type        "parallel"
                                                    :description "Mira como Mari sigue la flecha para pintar la letra"
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_3.m4a" :start 16.031 :duration 4.479}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 16.031
                                                                   :data [{:start 16.141 :end 17.241 :anim "talk"}
                                                                          {:start 17.665 :end 18.593 :anim "talk"}
                                                                          {:start 19.253 :end 20.431 :anim "talk"}]}]}

                   :vaca-voice-well-done           {:type        "parallel"
                                                    :description "Maravilloso! Buen trabajo Mari! Nos puedes mostrar una vez mas?
                                                                  Recuerda ver como Mari sigue la flecha para pintar la letra."
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_3.m4a" :start 22.742 :duration 11.159}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 22.742
                                                                   :data [{:start 22.852 :end 24.046 :anim "talk"}
                                                                          {:start 24.628 :end 26.058 :anim "talk"}
                                                                          {:start 26.671 :end 28.635 :anim "talk"}
                                                                          {:start 29.798 :end 33.696 :anim "talk"}]}]}

                   :vaca-voice-your-turn           {:type        "parallel"
                                                    :description "Ahora es tu turno! En la biblioteca esta un lienzo de arte para que tu puedas practicar.
                                                                  Rastera las letras con cuidado, asi como Mari lo hace."
                                                    :data        [{:type "audio" :id "/raw/audio/l2/a6/L2_A6_Vaca_4.m4a" :start 0.774 :duration 14.255}
                                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 0.774
                                                                   :data [{:start 1.061 :end 2.667 :anim "talk"}
                                                                          {:start 3.643 :end 8.949 :anim "talk"}
                                                                          {:start 10.039 :end 12.276 :anim "talk"}
                                                                          {:start 12.936 :end 14.685 :anim "talk"}]}]}}

   :triggers      {:start {:on "start" :action "start"}
                   :stop  {:on "back" :action "stop"}}
   :metadata      {:autostart true
                   :prev      ""}})
