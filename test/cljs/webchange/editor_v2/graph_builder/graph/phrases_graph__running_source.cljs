(ns webchange.editor-v2.graph-builder.graph.phrases-graph--running-source)

(def data {:assets
 [{:url "/raw/img/stadium/running/bg_01.jpg", :type "image"}
  {:url "/raw/img/stadium/running/bg_02.jpg", :type "image"}
  {:url "/raw/img/stadium/running/bg_03.jpg", :type "image"}
  {:url "/raw/audio/l2/a4/L2_A4_Mari.m4a", :type "audio"}
  {:url "/raw/audio/l2/game-voice.mp3", :type "audio", :alias "game voice"}
  {:url "/raw/audio/l2/game-voice-redo.mp3", :type "audio", :alias "game voice redo"}],
 :objects
 {:background
  {:type "carousel",
   :x 0,
   :y 0,
   :width 1920,
   :height 1080,
   :first "/raw/img/stadium/running/bg_01.jpg",
   :last "/raw/img/stadium/running/bg_03.jpg",
   :next "/raw/img/stadium/running/bg_02.jpg"},
  :box1
  {:type "animation",
   :x 2000,
   :y 683,
   :width 671,
   :height 633,
   :scale {:x -0.25, :y 0.25},
   :scene-name "box1",
   :transition "box1",
   :actions {:click {:id "pick-box-1", :on "click", :type "action"}},
   :anim "idle2",
   :anim-offset {:x 0, :y -300},
   :loop true,
   :name "boxes",
   :skin "qwestion",
   :speed 0.3,
   :start true,
   :states {:init {:type "transparent"}, :reset {:x 2000, :type "animation"}}},
  :box2
  {:type "animation",
   :x 2200,
   :y 789,
   :width 671,
   :height 633,
   :scale {:x -0.25, :y 0.25},
   :scene-name "box2",
   :transition "box2",
   :actions {:click {:id "pick-box-2", :on "click", :type "action"}},
   :anim "idle2",
   :anim-offset {:x 0, :y -300},
   :loop true,
   :name "boxes",
   :skin "qwestion",
   :speed 0.3,
   :start true,
   :states {:init {:type "transparent"}, :reset {:x 2200, :type "animation"}}},
  :box3
  {:type "animation",
   :x 2400,
   :y 932,
   :width 671,
   :height 633,
   :scale {:x -0.25, :y 0.25},
   :scene-name "box3",
   :transition "box3",
   :actions {:click {:id "pick-box-3", :on "click", :type "action"}},
   :anim "idle2",
   :anim-offset {:x 0, :y -300},
   :loop true,
   :name "boxes",
   :skin "qwestion",
   :speed 0.3,
   :start true,
   :states {:init {:type "transparent"}, :reset {:x 2400, :type "animation"}}},
  :letter1
  {:type "text",
   :x 1945,
   :y 400,
   :width 150,
   :height 150,
   :transition "letter1",
   :align "center",
   :fill "white",
   :font-family "Lexend Deca",
   :font-size 120,
   :shadow-blur 5,
   :shadow-color "#1a1a1a",
   :shadow-offset {:x 5, :y 5},
   :shadow-opacity 0.5,
   :states {:init {:type "transparent"}, :reset {:x 1945, :type "text"}},
   :text "?",
   :vertical-align "middle"},
  :letter2
  {:type "text",
   :x 2145,
   :y 506,
   :width 150,
   :height 150,
   :transition "letter2",
   :align "center",
   :fill "white",
   :font-family "Lexend Deca",
   :font-size 120,
   :shadow-blur 5,
   :shadow-color "#1a1a1a",
   :shadow-offset {:x 5, :y 5},
   :shadow-opacity 0.5,
   :states {:init {:type "transparent"}, :reset {:x 2145, :type "text"}},
   :text "?",
   :vertical-align "middle"},
  :letter3
  {:type "text",
   :x 2345,
   :y 649,
   :width 150,
   :height 150,
   :transition "letter3",
   :align "center",
   :fill "white",
   :font-family "Lexend Deca",
   :font-size 120,
   :shadow-blur 5,
   :shadow-color "#1a1a1a",
   :shadow-offset {:x 5, :y 5},
   :shadow-opacity 0.5,
   :states {:init {:type "transparent"}, :reset {:x 2345, :type "text"}},
   :text "?",
   :vertical-align "middle"},
  :mari
  {:type "animation",
   :x 1265,
   :y 311,
   :width 473,
   :height 511,
   :scene-name "mari",
   :transition "mari",
   :anim "idle",
   :anim-offset {:x 0, :y -150},
   :name "mari",
   :scale-x 0.5,
   :scale-y 0.5,
   :speed 0.35,
   :start true},
  :vera
  {:type "animation",
   :x 500,
   :y 775,
   :width 727,
   :height 1091,
   :scene-name "vera",
   :transition "vera",
   :anim "run",
   :meshes true,
   :name "vera-90",
   :scalex 0.4,
   :scaley 0.4,
   :skin "default",
   :speed 1,
   :start true}},
 :scene-objects [["background"] ["box1" "letter1" "box2" "letter2" "box3" "letter3"] ["vera" "mari"]],
 :actions
 {:clear-instruction {:type "remove-flows", :flow-tag "instruction"},
  :current-concept-chant
  {:phrase :chant
   :phrase-description "Chant current word"
   :type "action", :from-var [{:var-name "current-concept", :var-property "game-voice-chanting"}]},
  :go-to-box1-line
  {:type "sequence-data",
   :data
   [{:type "set-variable", :var-name "current-line", :var-value "box1"}
    {:type "transition", :from-var [{:var-name "current-line-pos", :action-property "to"}], :transition-id "vera"}]},
  :go-to-box2-line-down
  {:type "sequence-data",
   :data
   [{:type "set-variable", :var-name "current-line", :var-value "box2"}
    {:type "transition", :from-var [{:var-name "current-line-pos", :action-property "to"}], :transition-id "vera"}]},
  :go-to-box2-line-up
  {:type "sequence-data",
   :data
   [{:type "set-variable", :var-name "current-line", :var-value "box2"}
    {:type "transition", :from-var [{:var-name "current-line-pos", :action-property "to"}], :transition-id "vera"}]},
  :go-to-box3-line
  {:type "sequence-data",
   :data
   [{:type "set-variable", :var-name "current-line", :var-value "box3"}
    {:type "transition", :from-var [{:var-name "current-line-pos", :action-property "to"}], :transition-id "vera"}]},
  :init-slots
  {:type "parallel",
   :data
   [{:type "set-variable", :var-name "slot1", :var-value "box1"}
    {:type "set-variable", :var-name "slot2", :var-value "box2"}
    {:type "set-variable", :var-name "slot3", :var-value "box3"}]},
  :init-vera-position {:type "set-variable", :var-name "current-line", :var-value "box2"},
  :mari-voice-correct
  {:phrase :correct-answer
   :phrase-description "Correct answer"
   :offset 16.291,
   :phrase-text "Yippee!",
   :phrase-text-translated "Wiiii",
   :start 16.291,
   :type "animation-sequence",
   :duration 1.661,
   :audio "mari",
   :target "mari",
   :track 1,
   :data [{:end 17.734, :anim "talk", :start 16.405}]},
  :mari-voice-welcome
  {:phrase :intro
   :phrase-description "Intro"
   :offset 0.71,
   :phrase-text "Hello friend! It’s time to get active! As you run around the track, you will hear a song. Touch the picture that goes with the song.",
   :phrase-text-translated "Hola amigo! Es hora de ponernos activos! Mientras corres alrededor de la pista, escucharas una cancion. Toca la foto que va con la cancion",
   :start 0.71,
   :type "animation-sequence",
   :duration 13.816,
   :audio "mari",
   :target "mari",
   :track 1,
   :data [{:end 1.925, :anim "talk", :start 0.905}
          {:end 4.743, :anim "talk", :start 2.36}
          {:end 10.677, :anim "talk", :start 5.316}
          {:end 14.322, :anim "talk", :start 11.697}]},
  :mari-voice-wrong
  {:phrase :wrong-answer
   :phrase-description "Wrong answer"
   :offset 19.704,
   :phrase-text "Ayyy, try again, listen carefully!",
   :phrase-text-translated "Ayy intentalo de nuevo, escucha atentamente!",
   :start 19.704,
   :type "animation-sequence",
   :duration 4.537,
   :audio "mari",
   :target "mari",
   :track 1,
   :data [{:end 24.138, :anim "talk", :start 19.83}]},
  :pick-box-1
  {:type "sequence-data",
   :data
   [{:type "set-variable", :var-name "current-line-pos", :var-value {:y 675, :duration 0.5}}
    {:type "set-variable", :var-name "current-line-jump", :var-value {:y 475, :duration 1}}
    {:type "set-variable", :var-name "jump-wait", :var-value 600}
    {:fail "go-to-box1-line",
     :type "test-value",
     :value1 "box1",
     :success "stay-on-line",
     :from-var [{:var-name "current-line", :action-property "value2"}]}
    {:fail "pick-wrong",
     :type "test-value",
     :success "pick-correct",
     :from-var
     [{:var-name "box1", :action-property "value1"} {:var-name "current-concept", :action-property "value2"}]}]},
  :pick-box-2
  {:type "sequence-data",
   :data
   [{:type "set-variable", :var-name "current-line-pos", :var-value {:y 775, :duration 0.5}}
    {:type "set-variable", :var-name "current-line-jump", :var-value {:y 575, :duration 1}}
    {:type "set-variable", :var-name "jump-wait", :var-value 900}
    {:type "case",
     :options
     {:box1 {:id "go-to-box2-line-down", :type "action"},
      :box2 {:id "stay-on-line", :type "action"},
      :box3 {:id "go-to-box2-line-up", :type "action"}},
     :from-var [{:var-name "current-line", :action-property "value"}]}
    {:fail "pick-wrong",
     :type "test-value",
     :success "pick-correct",
     :from-var
     [{:var-name "box2", :action-property "value1"} {:var-name "current-concept", :action-property "value2"}]}]},
  :pick-box-3
  {:type "sequence-data",
   :data
   [{:type "set-variable", :var-name "current-line-pos", :var-value {:y 895, :duration 0.5}}
    {:type "set-variable", :var-name "current-line-jump", :var-value {:y 695, :duration 1}}
    {:type "set-variable", :var-name "jump-wait", :var-value 1400}
    {:fail "go-to-box3-line",
     :type "test-value",
     :value1 "box3",
     :success "stay-on-line",
     :from-var [{:var-name "current-line", :action-property "value2"}]}
    {:fail "pick-wrong",
     :type "test-value",
     :success "pick-correct",
     :from-var
     [{:var-name "box3", :action-property "value1"} {:var-name "current-concept", :action-property "value2"}]}]},
  :pick-correct
  {:type "sequence-data",
   :data
   [{:id "mari-voice-correct", :type "action"}
    {:data
     [{:to {:x -700, :duration 4}, :type "transition", :transition-id "box1"}
      {:to {:x -500, :duration 4}, :type "transition", :transition-id "box2"}
      {:to {:x -300, :duration 4}, :type "transition", :transition-id "box3"}
      {:to {:x -755, :duration 4}, :type "transition", :transition-id "letter1"}
      {:to {:x -555, :duration 4}, :type "transition", :transition-id "letter2"}
      {:to {:x -355, :duration 4}, :type "transition", :transition-id "letter3"}
      {:data
       [{:type "empty", :from-var [{:var-name "jump-wait", :action-property "duration"}]}
        {:id "run_jump", :type "animation", :target "vera"}
        {:type "transition", :from-var [{:var-name "current-line-jump", :action-property "to"}], :transition-id "vera"}
        {:id "run", :loop true, :type "add-animation", :target "vera"}
        {:type "transition", :from-var [{:var-name "current-line-pos", :action-property "to"}], :transition-id "vera"}],
       :type "sequence-data"}],
     :type "parallel"}
    {:id "renew-current-concept", :type "action"}]},
  :pick-wrong
  {:type "sequence-data",
   :data [{:id "mari-voice-wrong", :type "action"} {:id "current-concept-chant", :type "action"}]},
  :renew-current-concept
  {:type "sequence-data",
   :data
   [{:data
     [{:id "init", :type "state", :target "box1"}
      {:id "init", :type "state", :target "box2"}
      {:id "init", :type "state", :target "box3"}
      {:id "init", :type "state", :target "letter1"}
      {:id "init", :type "state", :target "letter2"}
      {:id "init", :type "state", :target "letter3"}],
     :type "parallel"}
    {:id "wait-for-box-animations", :type "action"}
    {:data
     [{:id "reset", :type "state", :target "box1"}
      {:id "reset", :type "state", :target "box2"}
      {:id "reset", :type "state", :target "box3"}
      {:id "reset", :type "state", :target "letter1"}
      {:id "reset", :type "state", :target "letter2"}
      {:id "reset", :type "state", :target "letter3"}],
     :type "parallel"}
    {:id "wait-for-box-animations", :type "action"}
    {:from ["item-1-1" "item-1-2" "item-1-3" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8"],
     :type "vars-var-provider",
     :on-end "stop",
     :shuffled true,
     :variables ["current-concept"],
     :provider-id "current-concept"}
    {:from ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8"],
     :type "vars-var-provider",
     :unique true,
     :from-var
     [{:var-key "concept-name",
       :var-name "current-concept",
       :var-property "concept-name",
       :action-property "exclude-property-values"}],
     :shuffled true,
     :variables ["pair-concept-1" "pair-concept-2"]}
    {:from ["current-concept" "pair-concept-1" "pair-concept-2"],
     :type "vars-var-provider",
     :shuffled true,
     :variables ["box1" "box2" "box3"]}
    {:data
     [{:type "set-slot",
       :target "box1",
       :from-var [{:var-name "box1", :var-property "image-src", :action-property "image"}],
       :slot-name "box1",
       :attachment {:x 40, :scale-x 4, :scale-y 4}}
      {:type "set-slot",
       :target "box2",
       :from-var [{:var-name "box2", :var-property "image-src", :action-property "image"}],
       :slot-name "box1",
       :attachment {:x 40, :scale-x 4, :scale-y 4}}
      {:type "set-slot",
       :target "box3",
       :from-var [{:var-name "box3", :var-property "image-src", :action-property "image"}],
       :slot-name "box1",
       :attachment {:x 40, :scale-x 4, :scale-y 4}}],
     :type "parallel"}
    {:data
     [{:type "set-attribute",
       :target "letter1",
       :from-var [{:var-name "box1", :var-property "letter", :action-property "attr-value"}],
       :attr-name "text"}
      {:type "set-attribute",
       :target "letter2",
       :from-var [{:var-name "box2", :var-property "letter", :action-property "attr-value"}],
       :attr-name "text"}
      {:type "set-attribute",
       :target "letter3",
       :from-var [{:var-name "box3", :var-property "letter", :action-property "attr-value"}],
       :attr-name "text"}],
     :type "parallel"}
    {:data
     [{:to {:x 1300, :duration 2}, :type "transition", :transition-id "box1"}
      {:to {:x 1500, :duration 2}, :type "transition", :transition-id "box2"}
      {:to {:x 1700, :duration 2}, :type "transition", :transition-id "box3"}
      {:to {:x 1245, :duration 2}, :type "transition", :transition-id "letter1"}
      {:to {:x 1445, :duration 2}, :type "transition", :transition-id "letter2"}
      {:to {:x 1645, :duration 2}, :type "transition", :transition-id "letter3"}],
     :type "parallel"}
    {:id "current-concept-chant", :type "action"}]},
  :renew-words
  {:type "sequence-data",
   :data
   [{:from "concepts-group",
     :type "lesson-var-provider",
     :limit 3,
     :repeat 4,
     :shuffled false,
     :variables ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8"],
     :provider-id "words-set"}
    {:from "item-1", :type "copy-variable", :var-name "item-1-1"}
    {:from "item-1", :type "copy-variable", :var-name "item-1-2"}
    {:from "item-1", :type "copy-variable", :var-name "item-1-3"}]},
  :start-scene
  {:type "sequence",
   :data
   ["start-activity"
    "clear-instruction"
    "mari-voice-welcome"
    "init-slots"
    "init-vera-position"
    "renew-words"
    "renew-current-concept"]},
  :start-activity {:type "start-activity", :id "running"},
  :stay-on-line {:type "empty", :duration 100},
  :stop-scene {:type "sequence", :data ["stop-activity"]},
  :stop-activity {:type "stop-activity", :id "running"},
  :wait-for-box-animations {:type "empty", :duration 100}},
 :triggers {:stop {:on "back", :action "stop-scene"}, :start {:on "start", :action "start-scene"}},
 :metadata {:prev "stadium", :autostart true},
 :audio {:mari "/raw/audio/l2/a4/L2_A4_Mari.m4a"}}
)
