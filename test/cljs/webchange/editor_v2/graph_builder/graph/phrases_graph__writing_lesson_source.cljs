(ns webchange.editor-v2.graph-builder.graph.phrases-graph--writing-lesson-source)

(def data {:assets
 [{:url "/raw/img/library/room2/background.jpg", :type "image"}
  {:url "/raw/img/library/room2/canvas.png", :type "image"}
  {:url "/raw/audio/l2/a6/mari.mp4", :type "audio", :alias "mari voice"}
  {:url "/raw/audio/l2/a6/vaca.mp4", :type "audio", :alias "vaca voice"}
  {:url "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_1.m4a",
   :size 2,
   :type "audio",
   :alias "vaca insertions 1"}
  {:url "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_2.m4a",
   :size 2,
   :type "audio",
   :alias "vaca insertions 2"}
  {:url "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_3.m4a",
   :size 2,
   :type "audio",
   :alias "vaca insertions 3"}
  {:url "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_4.m4a",
   :size 2,
   :type "audio",
   :alias "vaca insertions 4"}
  {:url "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_5.m4a",
   :size 2,
   :type "audio",
   :alias "vaca insertions 5"}
  {:url "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_6.m4a",
   :size 2,
   :type "audio",
   :alias "vaca insertions 6"}
  {:url "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_7.m4a",
   :size 2,
   :type "audio",
   :alias "vaca insertions 7"}
  {:url "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_8.m4a",
   :size 2,
   :type "audio",
   :alias "vaca insertions 8"}
  {:url "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_9.m4a",
   :size 2,
   :type "audio",
   :alias "vaca insertions 9"}
  {:url "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_10.m4a",
   :size 2,
   :type "audio",
   :alias "vaca insertions 10"}
  {:url "/raw/audio/l2/a2/L2_A2_Vaca_Insertions/L2_A2_Insertions_11.m4a",
   :size 2,
   :type "audio",
   :alias "vaca insertions 11"}],
 :objects
 {:background {:type "background", :src "/raw/img/library/room2/background.jpg"},
  :canvas
  {:type "image",
   :x 950,
   :y 83,
   :width 529,
   :height 810,
   :scale-x 1.2,
   :scale-y 1.2,
   :src "/raw/img/library/room2/canvas.png"},
  :letter-image
  {:type "transparent",
   :x 1050,
   :y 260,
   :width 651,
   :height 651,
   :scale {:x 0.3, :y 0.3},
   :origin {:type "center-center"},
   :states {:hidden {:type "transparent"}, :visible {:type "image"}}},
  :letter-path
  {:type "transparent",
   :x 1070,
   :y 183,
   :width 225,
   :height 225,
   :scene-name "letter-path",
   :animation "stop",
   :duration 5000,
   :fill "transparent",
   :line-cap "round",
   :path "M 0 0",
   :scale-x 2,
   :scale-y 2,
   :states {:hidden {:type "transparent"}, :visible {:type "animated-svg-path"}},
   :stroke "white",
   :stroke-width 15},
  :mari
  {:type "animation",
   :x 1700,
   :y 520,
   :width 473,
   :height 511,
   :scene-name "mari",
   :transition "mari",
   :anim "idle",
   :anim-offset {:x 225.87, :y 163.82},
   :name "mari",
   :scale-x 0.5,
   :scale-y 0.5,
   :speed 0.35,
   :start true},
  :picture-box
  {:type "transparent",
   :x 811,
   :y 330,
   :width 771.81,
   :height 1033.42,
   :scale {:x 0.35, :y 0.35},
   :origin {:type "center-center"},
   :scene-name "picture-box",
   :transition "picture-box",
   :anim "come",
   :anim-offset {:x 391.97, :y 317.31},
   :loop false,
   :name "boxes",
   :skin "empty",
   :speed 0.7,
   :start true,
   :states {:hidden {:type "transparent"}, :visible {:type "animation"}}},
  :senora-vaca
  {:type "animation",
   :x 486,
   :y 986,
   :width 351,
   :height 717,
   :scene-name "senoravaca",
   :anim "idle",
   :name "senoravaca",
   :scale-x 1,
   :scale-y 1,
   :skin "vaca",
   :speed 0.3,
   :start true}},
 :scene-objects [["background"] ["canvas" "picture-box" "letter-image" "letter-path"] ["senora-vaca" "mari"]],
 :actions
 {:draw-letter
  {:type "sequence", :data ["letter-drawing-prepare" "mari-move-to-letter" "letter-drawing"]},
  :finish-activity {:type "finish-activity", :id "writing-lesson"},
  :init-letter
  {:type "parallel",
   :data
   [{:type "set-attribute",
     :target "letter-path",
     :from-var [{:var-name "path", :action-property "attr-value"}],
     :attr-name "path"}
    {:type "set-attribute",
     :target "letter-path",
     :from-var [{:var-name "path-color-1", :action-property "attr-value"}],
     :attr-name "stroke"}]},
  :init-vars
  {:type "parallel",
   :data
   [{:type "set-variable",
     :from-var [{:var-name "current-concept", :var-property "letter-path", :action-property "var-value"}],
     :var-name "path"}
    {:type "set-variable", :var-name "path-color-1", :var-value "#eec166"}
    {:type "set-variable", :var-name "path-color-2", :var-value "#6cd38a"}]},
  :introduce-letter
  {:phrase :introduce-letter
   :phrase-description "Introduce letter"
   :type "sequence", :data ["vaca-voice-sound-look" "init-letter" "show-letter" "pronounce-letter"]},
  :introduce-picture
  {:phrase :introduce-picture
   :phrase-description "Introduce picture"
   :type "sequence", :data ["vaca-asks-sound" "show-current-word-picture" "vaca-letter-pronouncing"]},
  :invite-user
  {:phrase :invite-user
   :phrase-description "Invite user"
   :type "sequence",
   :data ["vaca-voice-your-turn" "finish-activity" "mari-click-to-practice"]},
  :letter-drawing-prepare
  {:phrase :mari-starts
   :phrase-description "Mari starts drawing"
   :type "sequence", :data ["vaca-voice-help-mari" "mari-voice-sure"]}
  :letter-drawing
  {:phrase :letter-drawing
   :phrase-description "Letter drawing"
   :type "sequence-data",
   :data
   [{:data
     [{:data
       [{:phrase-text "Watch how Mari follows the arrows to paint the letter"
         :phrase-text-translated "Mira como Mari sigue la flecha para pintar la letra",
         :offset 86.226,
         :start 86.226,
         :type "animation-sequence",
         :duration 5.494,
         :audio "/raw/audio/l2/a6/vaca.mp4",
         :target "senoravaca",
         :track 1,
         :data
         [{:end 87.75, :anim "talk", :start 86.37}
          {:end 89.9, :anim "talk", :start 88.12}
          {:end 91.61, :anim "talk", :start 90.15}]}
        {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-letter"}]}],
       :type "sequence-data"}
      {:id "letter-drawing-animation", :type "action"}],
     :type "parallel"}
    {:phrase-text "Wonderful! Well done Mari! Can you show us one more time? Remember to watch how Mari follows the arrows to paint the letter."
     :phrase-text-translated "Maravilloso! Buen trabajo Mari! Nos puedes mostrar una vez mas? Recuerda ver como Mari sigue la flecha para pintar la letra."
     :offset 93.533,
     :start 93.533,
     :type "animation-sequence",
     :duration 15.907,
     :audio "/raw/audio/l2/a6/vaca.mp4",
     :target "senoravaca",
     :track 1,
     :data
     [{:end 95.47, :anim "talk", :start 93.76}
      {:end 97.92, :anim "talk", :start 96.26}
      {:end 102.05, :anim "talk", :start 99.08}
      {:end 107.23, :anim "talk", :start 103.33}
      {:end 109.1, :anim "talk", :start 107.41}]}
    {:type "empty", :duration 100}
    {:type "path-animation", :state "reset", :target "letter-path"}
    {:type "set-attribute",
     :target "letter-path",
     :from-var [{:var-name "path-color-2", :action-property "attr-value"}],
     :attr-name "stroke"}
    {:id "letter-drawing-animation", :type "action"}]},
  :letter-drawing-animation
  {:type "sequence-data",
   :data
   [{:id "wand_idle", :loop true, :type "add-animation", :track 2, :target "mari"}
    {:to {:x 1364, :y 514, :loop false, :duration 1}, :type "transition", :transition-id "mari"}
    {:data
     [{:type "path-animation", :state "play", :target "letter-path"}
      {:to {:path "", :scale {:x 2, :y 2}, :origin {:x 1130, :y 193}, :duration 5},
       :type "transition",
       :from-var [{:var-name "path", :action-property "to.path"}],
       :transition-id "mari"}],
     :type "parallel"}
    {:to {:x 1505, :y 485, :loop false, :duration 0.7}, :type "transition", :transition-id "mari"}]},
  :mari-click-to-practice
  {:type "sequence-data",
   :data
   [{:to {:x 144, :y 200, :duration 1.3}, :type "transition", :transition-id "mari"}
    {:phrase-text "Click here to start your letter practice!"
     :phrase-text-translated "Haz click sobre la flecha para practicar rastreando las letras!",
     :offset 16.506,
     :start 16.506,
     :type "animation-sequence",
     :duration 5.506,
     :audio "/raw/audio/l2/a6/mari.mp4",
     :target "mari",
     :track 1,
     :data [{:end 21.68, :anim "talk", :start 16.73}]}]},
  :mari-move-to-letter {:type "transition", :to {:x 1186, :y 414, :loop false, :duration 1.5}, :transition-id "mari"},
  :mari-voice-sure
  {:type "animation-sequence",
   :data [{:end 10.71, :anim "talk", :start 9.69}],
   :target "mari",
   :audio "/raw/audio/l2/a6/mari.mp4",
   :start 9.432,
   :duration 1.44,
   :track 1,
   :offset 9.432,
   :phrase-text "Sure, of course!"
   :phrase-text-translated "Claro que si!"},
  :mari-voice-welcome
  {:type "animation-sequence",
   :data [{:end 1.77, :anim "talk", :start 1.06}],
   :target "mari",
   :audio "/raw/audio/l2/a6/mari.mp4",
   :start 0.88,
   :duration 1.107,
   :track 1,
   :offset 0.88,
   :phrase-text "Hello!"
   :phrase-text-translated "Hola!"},
  :pronounce-letter
  {:type "sequence-data",
   :data
   [{:phrase-text "This is the letter"
     :phrase-text-translated "Esta es la letra",
     :offset 58.826,
     :start 58.826,
     :type "animation-sequence",
     :duration 2.4,
     :audio "/raw/audio/l2/a6/vaca.mp4",
     :target "senoravaca",
     :track 1,
     :data [{:end 59.67, :anim "talk", :start 59.02} {:end 61.24, :anim "talk", :start 59.96}]}
    {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-letter"}]}
    {:type "empty", :duration 300}
    {:phrase-text "The letter"
     :phrase-text-translated "La letra",
     :offset 62.786,
     :start 62.786,
     :type "animation-sequence",
     :duration 0.88,
     :audio "/raw/audio/l2/a6/vaca.mp4",
     :target "senoravaca",
     :track 1,
     :data [{:end 64.22, :anim "talk", :start 62.89}]}
    {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-letter"}]}
    {:phrase-text "makes the sound"
     :phrase-text-translated "hace el sonido",
     :offset 64.413,
     :start 64.413,
     :type "animation-sequence",
     :duration 1.76,
     :audio "/raw/audio/l2/a6/vaca.mp4",
     :target "senoravaca",
     :track 1,
     :data [{:end 66.37, :anim "talk", :start 64.61}]}
    {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-sound"}]}
    {:type "empty", :duration 300}
    {:phrase-text "Can you say it with me 3 times?"
     :phrase-text-translated "Puedes decirlo conmigo 3 veces?",
     :offset 68.426,
     :start 68.426,
     :type "animation-sequence",
     :duration 3.64,
     :audio "/raw/audio/l2/a6/vaca.mp4",
     :target "senoravaca",
     :track 1,
     :data [{:end 71.87, :anim "talk", :start 68.67}]}
    {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-sound"}]}
    {:type "empty", :duration 300}
    {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-sound"}]}
    {:type "empty", :duration 300}
    {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-sound"}]}]},
  :renew-concept
  {:type "lesson-var-provider", :from "concepts-single", :provider-id "concepts", :variables ["current-concept"]},
  :show-current-word-picture
  {:type "sequence-data",
   :data
   [{:id "visible", :type "state", :target "letter-image"}
    {:type "set-attribute",
     :target "letter-image",
     :from-var [{:var-name "current-concept", :var-property "letter-src", :action-property "attr-value"}],
     :attr-name "src"}
    {:id "visible", :type "state", :target "picture-box"}
    {:type "empty", :duration 500}
    {:type "set-slot",
     :target "picture-box",
     :from-var [{:var-name "current-concept", :var-property "image-src", :action-property "image"}],
     :slot-name "box1",
     :attachment {:x 40, :scale-x 4, :scale-y 4}}
    {:id "idle_fly1", :loop true, :type "add-animation", :target "picture-box"}]},
  :show-letter {:type "state", :id "visible", :target "letter-path"},
  :start-scene
  {:type "sequence",
   :data
   ["start-activity"
    "renew-concept"
    "init-vars"
    "welcome-voice"
    "introduce-picture"
    "introduce-letter"
    "draw-letter"
    "invite-user"]},
  :welcome-voice
  {:phrase :welcome
   :phrase-description "Welcome"
   :type "sequence",
   :data
   ["vaca-voice-welcome"
    "mari-voice-welcome"]}
  :start-activity {:type "start-activity", :id "writing-lesson"},
  :start-letter-path {:type "set-attribute", :target "letter-path", :attr-name "animation", :attr-value "play"},
  :stop-activity {:type "stop-activity", :id "writing-lesson"},
  :vaca-asks-sound
  {:type "sequence-data",
   :data
   [{:phrase-text "Now, pay attention friends. Did you know that each picture begins with a sound"
     :phrase-text-translated "Ahora, pongan atencion amigos. Sabian que cada foto empieza con el sonido"
     :offset 29.76,
     :start 29.76,
     :type "animation-sequence",
     :duration 9.573,
     :audio "/raw/audio/l2/a6/vaca.mp4",
     :target "senoravaca",
     :track 1,
     :data
     [{:end 30.66, :anim "talk", :start 30.08}
      {:end 33.61, :anim "talk", :start 31.5}
      {:end 39.15, :anim "talk", :start 34.92}]}
    {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-letter"}]}]},
  :vaca-letter-pronouncing
  {:type "sequence-data",
   :data
   [{:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-word"}]}
    {:phrase-text "starts with sound"
     :phrase-text-translated "empieza con el sonido",
     :offset 42.493,
     :start 42.493,
     :type "animation-sequence",
     :duration 2.507,
     :audio "/raw/audio/l2/a6/vaca.mp4",
     :target "senoravaca",
     :track 1,
     :data [{:end 45.69, :anim "talk", :start 42.74}]}
    {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-sound"}]}
    {:type "empty", :duration 500}
    {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-sound"}]}
    {:type "empty", :duration 500}
    {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-sound"}]}
    {:type "empty", :duration 500}
    {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-word"}]}
    {:type "empty", :duration 500}
    {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-word"}]}]},
  :vaca-voice-help-mari
  {:type "sequence-data",
   :data
   [{:phrase-text "Mari, can you help me paint the letter"
     :phrase-text-translated "Mari, puedes ayudarme a pintar la letra"
     :offset 79.88,
     :start 79.88,
     :type "animation-sequence",
     :duration 4.2,
     :audio "/raw/audio/l2/a6/vaca.mp4",
     :target "senoravaca",
     :track 1,
     :data [{:end 80.43, :anim "talk", :start 80.09} {:end 84.16, :anim "talk", :start 80.93}]}
    {:type "action", :from-var [{:var-name "current-concept", :var-property "letter-intro-letter"}]}]},
  :vaca-voice-sound-look
  {:type "animation-sequence",
   :data [{:end 55.89, :anim "talk", :start 53.91} {:end 57.57, :anim "talk", :start 56.14}],
   :target "senoravaca",
   :audio "/raw/audio/l2/a6/vaca.mp4",
   :start 53.826,
   :duration 3.774,
   :track 1,
   :offset 53.826,
   :phrase-text "This is what the sound looks like."
   :phrase-text-translated "Asi es como se ve este sonido."},
  :vaca-voice-welcome
  {:type "animation-sequence",
   :data
   [{:end 4.29, :anim "talk", :start 1.9}
    {:end 5.56, :anim "talk", :start 4.44}
    {:end 7.93, :anim "talk", :start 6.67}
    {:end 9.91, :anim "talk", :start 8.2}
    {:end 10.6, :anim "talk", :start 10.48}
    {:end 13.96, :anim "talk", :start 11.05}
    {:end 14.99, :anim "talk", :start 14.37}
    {:end 16.01, :anim "talk", :start 15.12}
    {:end 17.83, :anim "talk", :start 16.24}
    {:end 21.21, :anim "talk", :start 18.5}
    {:end 24.2, :anim "talk", :start 21.62}
    {:end 25.75, :anim "talk", :start 24.53}
    {:end 26.3, :anim "talk", :start 26.15}
    {:end 28.7, :anim "talk", :start 26.85}],
   :target "senoravaca",
   :audio "/raw/audio/l2/a6/vaca.mp4",
   :start 1.826,
   :duration 27.1,
   :track 1,
   :offset 1.826,
   :phrase-text
   "Welcome to the library genius! Isn’t it such a fantastic place? I love how libraries are filled with so many books.  Hmmm...Well, are you ready to learn about letters?  Let’s begin!"
   :phrase-text-translated
   "Bienvenido a la biblioteca pequeño genio! ¿No es un lugar tan fantástico? Me encanta cómo las bibliotecas están llenas de tantos libros. Hmmm ... bueno, ¿estás listo para aprender sobre letras? ¡Vamos a empezar"},
  :vaca-voice-your-turn
  {:type "animation-sequence",
   :data
   [{:end 115.63, :anim "talk", :start 110.74}
    {:end 117.53, :anim "talk", :start 116.44}
    {:end 119.06, :anim "talk", :start 118.07}
    {:end 121.04, :anim "talk", :start 120.15}
    {:end 122.67, :anim "talk", :start 121.53}
    {:end 126.67, :anim "talk", :start 123.66}
    {:end 129.14, :anim "talk", :start 127.66}],
   :target "senoravaca",
   :audio "/raw/audio/l2/a6/vaca.mp4",
   :start 110.413,
   :duration 19.266,
   :track 1,
   :offset 110.413,
   :phrase-text
   "Now it’s your turn to practice tracing letters.  Be sure to trace each letter carefully, the same way that Mari did! Have fun!"
   :phrase-text-translated
   "Ahora es tu turno! En la biblioteca esta un lienzo de arte para que tu puedas practicar. Rastera las letras con cuidado, asi como Mari lo hace."}},
 :triggers {:stop {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
 :metadata {:prev "library", :autostart true}}


)
