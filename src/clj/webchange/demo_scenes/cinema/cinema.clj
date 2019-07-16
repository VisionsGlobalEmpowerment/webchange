(ns webchange.demo-scenes.cinema.cinema
  (:require
    [webchange.demo-scenes.cinema.cinema-objects :refer [cinema-objects]]))

(def cinema-scene {:assets        [{:url "/raw/img/cinema/background.jpg", :size 10, :type "image"}
                                   {:url "/raw/img/cinema/screen-off.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/baby.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/ball.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/bear.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/boots.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/boy.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/cat.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/cheese.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/chocolate.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/diamond.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/elephant.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/frog.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/garden.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/grapes.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/home.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/hand.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/kimono.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/leaf.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/lion.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/magnet.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/ostrich.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/squirrel.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/sunflower.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/tomato.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/website.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/wrench.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/xylophone.png", :size 10, :type "image"}
                                   {:url "/raw/img/elements/yoyo.png", :size 10, :type "image"}

                                   {:url "/raw/audio/l2/a1/L2_A1_Mari.m4a", :size 5, :type "audio" :alias "vaca voice 1"}
                                   {:url "/raw/audio/l2/a1/L2_A1_Vaca_1.m4a", :size 5, :type "audio" :alias "vaca voice 1"}
                                   {:url "/raw/audio/l2/a1/L2_A1_Vaca_2.m4a", :size 5, :type "audio" :alias "vaca voice 2"}
                                   {:url "/raw/audio/l2/a1/L2_A1_Vaca_3.m4a", :size 5, :type "audio" :alias "vaca voice 3"}
                                   {:url "/raw/audio/l2/a1/L2_A1_Vaca_4.m4a", :size 5, :type "audio" :alias "vaca voice 4"}
                                   {:url "/raw/audio/l2/a1/L2_A1_Vaca_5.m4a", :size 5, :type "audio" :alias "vaca voice 5"}
                                   {:url "/raw/audio/l2/a1/L2_A1_Vaca_6.m4a", :size 5, :type "audio" :alias "vaca voice 6"}
                                   {:url "/raw/audio/l2/a1/L2_A1_Vaca_7.m4a", :size 5, :type "audio" :alias "vaca voice 7"}]
                   :audio         {:mari   "/raw/audio/l2/a1/L2_A1_Mari.m4a"
                                   :vaca-1 "/raw/audio/l2/a1/L2_A1_Vaca_1.m4a"
                                   :vaca-2 "/raw/audio/l2/a1/L2_A1_Vaca_2.m4a"
                                   :vaca-3 "/raw/audio/l2/a1/L2_A1_Vaca_3.m4a"
                                   :vaca-6 "/raw/audio/l2/a1/L2_A1_Vaca_6.m4a"
                                   :vaca-7 "/raw/audio/l2/a1/L2_A1_Vaca_7.m4a"}
                   :objects       cinema-objects
                   :scene-objects [["background"]
                                   ["card-a" "card-b" "card-c" "card-ch" "card-d" "card-e" "card-f" "card-g" "card-h" "card-i"
                                    "card-j" "card-k" "card-l" "card-ll" "card-m" "card-n" "card-ñ" "card-o" "card-p" "card-q"
                                    "card-r" "card-rr" "card-s" "card-t" "card-u" "card-v" "card-w" "card-x" "card-y" "card-z"]
                                   ["screen-overlay"]
                                   ["senora-vaca" "mari"]]
                   :actions       {:clear-instruction
                                   {:type        "remove-flows"
                                    :description "Remove flows"
                                    :flow-tag    "instruction"}

                                   :init-vars
                                   {:type "parallel"
                                    :data [{:type "set-variable" :var-name "current-round" :var-value 1}
                                           {:type "set-variable" :var-name "rounds-number" :var-value 1}
                                           {:type "set-variable" :var-name "exclude-concepts" :var-value {:concept-name ["ardilla" "oso"]}}]}

                                   :start-activity
                                   {:type        "sequence"
                                    :description "Initial action"
                                    :data        ["clear-instruction"
                                                  "init-vaca-voice-next"
                                                  "init-vars"
                                                  ;"intro"
                                                  "renew-current-letter"]}

                                   :finish-activity
                                   {:type "sequence-data"
                                    :data [{:type "finish-activity" :id "cinema"}
                                           {:type "scene" :scene-id "cinema-video"}]}

                                   :renew-current-letter
                                   {:type "sequence-data"
                                    :data [{:type                    "lesson-var-provider"
                                            :from                    "concepts"
                                            :from-var                [{:template "concept-round-%" :action-property "provider-id" :var-name "current-round"}
                                                                      {:action-property "exclude-property-values" :var-name "exclude-concepts"}]
                                            :variables               ["current-concept"]
                                            :shuffled                true
                                            :on-end                  "check-round"}
                                           {:type     "set-variable" :var-name "current-letter"
                                            :from-var [{:var-name        "current-concept" :var-property "letter"
                                                        :action-property "var-value"}]}
                                           {:type "action" :id "chant-current-letter"}
                                           {:type "action" :id "renew-current-letter"}]}

                                   :check-round
                                   {:type     "test-var-scalar"
                                    :var-name "current-round"
                                    :from-var [{:action-property "value" :var-name "rounds-number"}]
                                    :success  "finish-chanting"
                                    :fail     "go-next-round"}

                                   :go-next-round
                                   {:type "sequence-data"
                                    :data [{:type "inc-variable" :var-name "current-round" :inc-value 1}
                                           {:type "set-variable" :var-name "exclude-concepts" :var-value {:concept-name []}}
                                           {:type "action" :id "vaca-voice-one-more-round"}
                                           {:type "action" :id "renew-current-letter"}]}

                                   :finish-chanting
                                   {:type        "sequence-data"
                                    :data        [{:type "parallel"
                                                   :data [{:type "action" :id "vaca-voice-finish"}
                                                          {:type "state" :target "screen-overlay" :id "visible"}
                                                          {:type "transition" :transition-id "mari" :to {:x 1613 :y 785 :loop false}}]}
                                                  {:type "action" :id "finish-activity"}]}

                                   :chant-current-letter
                                   {:type "sequence-data"
                                    :data [
                                           {:type "action" :id "vaca-voice-next"}
                                           {:type     "state" :id "pointed"
                                            :from-var [{:action-property "target"
                                                        :template        "card-%"
                                                        :var-name        "current-letter"}]}
                                           {:type "action" :id "move-to-current-card"}
                                           {:type     "action"
                                            :from-var [{:var-name     "current-concept"
                                                        :var-property "vaca-chanting-word"}]}
                                           {:type "action" :id "vaca-voice-chanting-4"}
                                           {:type     "action"
                                            :from-var [{:var-name     "current-concept"
                                                        :var-property "vaca-chanting-song"}]}
                                           {:type     "state" :id "normal"
                                            :from-var [{:action-property "target"
                                                        :template        "card-%"
                                                        :var-name        "current-letter"}]}]}

                                   :intro
                                   {:type        "sequence-data"
                                    :description "Initial action",
                                    :data        [{:type "parallel"
                                                   :description
                                                         "Buenas Dias! It’s learning time. Look at all these words you already know.
                                                         Let’s start this activity by first reviewing the beginning sounds of words.
                                                         Mari, can you please turn the screen on?"
                                                   :data [{:type "audio" :id "vaca-1" :start 0.593 :duration 20.321}
                                                          {:type "animation-sequence" :target "senoravaca" :track 1 :offset 0.593
                                                           :data [{:start 0.763 :end 2.017 :anim "talk"}
                                                                  {:start 2.672 :end 4.335 :anim "talk"}
                                                                  {:start 5.098 :end 8.642 :anim "talk"}
                                                                  {:start 9.406 :end 16.058 :anim "talk"}
                                                                  {:start 17.067 :end 20.502 :anim "talk"}]}]}
                                                  {:type        "parallel"
                                                   :description "Claro que si, Senora Vaca!"
                                                   :data        [{:type "audio" :id "mari" :start 0.793 :duration 2.379}
                                                                 {:type "animation-sequence" :target "mari" :track 1 :offset 0.793
                                                                  :data [{:start 0.793 :end 3.173 :anim "talk"}]}]}
                                                  {:type "transition" :transition-id "mari" :to {:x 1290 :y 580 :loop false :duration 1.5}}
                                                  {:type "state" :target "screen-overlay" :id "hidden"}
                                                  {:type "parallel"
                                                   :description
                                                         "Escucha los sonidos al principio de cada palabra.
                                                         Por ejemplo, cuando decimos la palabra “ardilla,” escuchamos el sonido “aaaaaa”.
                                                         “Aaaardilla. Aaaardilla.”"
                                                   :data [{:type "audio" :id "vaca-2" :start 0.954 :duration 17.236}
                                                          {:type "animation-sequence" :target "senoravaca" :track 1 :offset 0.954
                                                           :data [{:start 1.252 :end 4.98 :anim "talk"}
                                                                  {:start 5.636 :end 6.59 :anim "talk"}
                                                                  {:start 7.127 :end 10.288 :anim "talk"}
                                                                  {:start 10.944 :end 12.644 :anim "talk"}
                                                                  {:start 12.882 :end 13.747 :anim "talk"}
                                                                  {:start 14.612 :end 15.924 :anim "talk"}
                                                                  {:start 16.699 :end 18.071 :anim "talk"}]}]}

                                                  ;; Mari! Nos puedes mostrar como se escucha el sonido aaaaa? aaaaaa.

                                                  {:type "state" :id "pointed" :target "card-a"}
                                                  {:type "set-variable" :var-name "current-letter" :var-value "a"}
                                                  {:type "action" :id "move-to-current-card"}
                                                  {:type        "parallel"
                                                   :description "Vamos a componer una cancion- Ardilla! Ardilla! a-a-A!"
                                                   :data        [{:type "audio" :id "vaca-2" :start 19.115 :duration 6.531}
                                                                 {:type "animation-sequence" :target "senoravaca" :track 1 :offset 19.115
                                                                  :data [{:start 19.204 :end 21.381 :anim "talk"}
                                                                         {:start 22.156 :end 25.586 :anim "talk"}]}]}
                                                  {:type "parallel"
                                                   :description
                                                         "Dilo conmigo tres veces!- Ardilla! Ardilla! a-a-A!
                                                         Ardilla! Ardilla! a-a-A! Ardilla! Ardilla! a-a-A!"
                                                   :data [{:type "audio" :id "vaca-2" :start 26.301 :duration 11.66}
                                                          {:type "animation-sequence" :target "senoravaca" :track 1 :offset 26.301
                                                           :data [{:start 26.361 :end 28.001 :anim "talk"}
                                                                  {:start 28.568 :end 31.430 :anim "talk"}
                                                                  {:start 31.758 :end 34.740 :anim "talk"}
                                                                  {:start 35.009 :end 37.901 :anim "talk"}]}]}
                                                  {:type "state" :id "normal" :target "card-a"}
                                                  {:type "action" :id "vaca-voice-chanting-4"}
                                                  {:type "state" :id "pointed" :target "card-o"}
                                                  {:type "set-variable" :var-name "current-letter" :var-value "o"}
                                                  {:type "action" :id "move-to-current-card"}
                                                  {:type "parallel"
                                                   :description
                                                         "Oso. Al principio de la palabra
                                                         “Oso”, escuchamos el sonido “ooooo.” Ooooso. Oooso"
                                                   :data [{:type "audio" :id "vaca-2" :start 41.927 :duration 10.735}
                                                          {:type "animation-sequence" :target "senoravaca" :track 1 :offset 41.927
                                                           :data [{:start 41.927 :end 42.673 :anim "talk"}
                                                                  {:start 43.627 :end 46.549 :anim "talk"}
                                                                  {:start 43.627 :end 46.549 :anim "talk"}
                                                                  {:start 46.937 :end 48.010 :anim "talk"}
                                                                  {:start 46.937 :end 47.861 :anim "talk"}
                                                                  {:start 48.219 :end 49.084 :anim "talk"}
                                                                  {:start 49.800 :end 50.903 :anim "talk"}
                                                                  {:start 51.470 :end 52.513 :anim "talk"}]}]}
                                                  {:type "parallel"
                                                   :description
                                                         "El sonido oooooo se escucha asi. Ooooo.
                                                         Vamos a cantarlo- Oso, Oso, o-o-O!!"
                                                   :data [{:type "audio" :id "vaca-2" :start 53.438 :duration 11.004}
                                                          {:type "animation-sequence" :target "senoravaca" :track 1 :offset 53.438
                                                           :data [{:start 53.527 :end 55.018 :anim "talk"}
                                                                  {:start 55.525 :end 57.076 :anim "talk"}
                                                                  {:start 57.404 :end 58.388 :anim "talk"}
                                                                  {:start 59.581 :end 61.833 :anim "talk"}
                                                                  {:start 61.698 :end 64.412 :anim "talk"}]}]}
                                                  {:type "state" :id "normal" :target "card-o"}]}

                                   :move-to-current-card
                                   {:type            "transition" :transition-id "mari"
                                    :to              {:duration 2 :loop false}
                                    :from-var-object [{:var-name             "current-letter"
                                                       :object-name-template "card-%"
                                                       :object-property      "x"
                                                       :action-property      "to.x"
                                                       :offset               170}
                                                      {:var-name             "current-letter"
                                                       :object-name-template "card-%"
                                                       :object-property      "y"
                                                       :action-property      "to.y"
                                                       :offset               180}]}

                                   ;; Vaca voices for letter chanting

                                   :vaca-voice-chanting-1
                                   {:type        "parallel"
                                    :description "Al principio de la palabra"
                                    :data        [{:type "audio" :id "vaca-2" :start 43.545 :duration 2.297}
                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 43.545
                                                   :data [{:start 43.677 :end 45.766 :anim "talk"}]}]}

                                   :vaca-voice-chanting-2
                                   {:type        "parallel"
                                    :description "el sonido"
                                    :data        [{:type "audio" :id "vaca-2" :start 53.541 :duration 0.762}
                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 53.541
                                                   :data [{:start 53.596 :end 54.243 :anim "talk"}]}]}

                                   :vaca-voice-chanting-3
                                   {:type        "parallel"
                                    :description "se escucha asi"
                                    :data        [{:type "audio" :id "vaca-2" :start 55.481 :duration 1.628}
                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 55.481
                                                   :data [{:start 55.481 :end 57.109 :anim "talk"}]}]}

                                   :vaca-voice-chanting-4
                                   {:type        "parallel"
                                    :description "Vamos a cantarlo"
                                    :data        [{:type "audio" :id "vaca-2" :start 59.566 :duration 1.261}
                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 59.566
                                                   :data [{:start 59.660 :end 60.742 :anim "talk"}]}]}

                                   ;; Vaca voices for round ending

                                   :vaca-voice-one-more-round
                                   {:type        "parallel"
                                    :description "Fantastico!! Lo estas haciendo muy bien!
                                                  Crees que podremos hacer todas estas fotos y los sonidos una vez mas?
                                                  Vamos a hacerlo!"
                                    :data        [{:type "audio" :id "vaca-6" :start 15.147 :duration 13.047}
                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 15.147
                                                   :data [{:start 15.221 :end 18.641 :anim "talk"}
                                                          {:start 19.087 :end 25.908 :anim "talk"}
                                                          {:start 26.428 :end 28.027 :anim "talk"}]}]}

                                   :vaca-voice-finish
                                   {:type        "parallel"
                                    :description "Wiiiiiiii! Lo cantaste conmigo asi de fuerte? Estoy impresionada!"
                                    :data        [{:type "audio" :id "vaca-7" :start 0.897 :duration 8.719}
                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 0.897
                                                   :data [{:start 1.158 :end 2.915 :anim "talk"}
                                                          {:start 3.400 :end 6.191 :anim "talk"}
                                                          {:start 6.926 :end 8.968 :anim "talk"}]}]}

                                   ;; Vaca voices for going to the next letter

                                   :vaca-voice-next
                                   {:type "sequence-data"
                                    :data [{:type      "vars-var-provider"
                                            :variables ["current-vaca-voice-next"]
                                            :from      ["vaca-voice-next-1"
                                                        "vaca-voice-next-2"
                                                        "vaca-voice-next-3"
                                                        "vaca-voice-next-4"]
                                            :shuffled  true}
                                           {:type "action" :from-var [{:var-name "current-vaca-voice-next" :action-property "id"}]}]}

                                   :init-vaca-voice-next
                                   {:type "parallel"
                                    :data [{:type "set-variable" :var-name "vaca-voice-next-1" :var-value "vaca-voice-next-1"}
                                           {:type "set-variable" :var-name "vaca-voice-next-2" :var-value "vaca-voice-next-2"}
                                           {:type "set-variable" :var-name "vaca-voice-next-3" :var-value "vaca-voice-next-3"}
                                           {:type "set-variable" :var-name "vaca-voice-next-4" :var-value "vaca-voice-next-4"}]}

                                   :vaca-voice-next-1
                                   {:type        "parallel"
                                    :description "Cual es la siguiente foto?"
                                    :data        [{:type "audio" :id "vaca-2" :start 38.932 :duration 2.560}
                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 38.932
                                                   :data [{:start 39.303 :end 41.182 :anim "talk"}]}]}

                                   :vaca-voice-next-2
                                   {:type        "parallel"
                                    :description "Y ahora la proxima foto!"
                                    :data        [{:type "audio" :id "vaca-2" :start 65.604 :duration 2.505}
                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 65.604
                                                   :data [{:start 65.664 :end 66.409 :anim "talk"}
                                                          {:start 66.708 :end 68.050 :anim "talk"}]}]}
                                   :vaca-voice-next-3
                                   {:type        "parallel"
                                    :description "Que sigue?!"
                                    :data        [{:type "audio" :id "vaca-2" :start 73.805 :duration 1.324}
                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 73.805
                                                   :data [{:start 73.984 :end 74.878 :anim "talk"}]}]}
                                   :vaca-voice-next-4
                                   {:type        "parallel"
                                    :description "Sigue! Sigue!"
                                    :data        [{:type "audio" :id "vaca-3" :start 0.907 :duration 1.360}
                                                  {:type "animation-sequence" :target "senoravaca" :track 1 :offset 0.907
                                                   :data [{:start 0.970 :end 2.189 :anim "talk"}]}]}}
                   :triggers      {:start {:on "start" :action "start-activity"}}
                   :metadata      {:autostart true
                                   :prev      "park"}})
