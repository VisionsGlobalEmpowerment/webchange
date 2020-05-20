(ns webchange.editor-v2.graph-builder.graph.data.cycling-letters.source)

(def data {:assets
                          [{:url "/raw/img/stadium/cycling/cycle_race_bg_01.jpg", :size 10, :type "image"}
                           {:url "/raw/img/stadium/cycling/cycle_race_bg_02.jpg", :size 10, :type "image"}
                           {:url "/raw/img/stadium/cycling/cycle_race_bg_03.jpg", :size 10, :type "image"}
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
                           {:url "/raw/audio/l2/a9/L2_A9_Mari.m4a", :type "audio"}
                           {:url "/raw/audio/l2/mari-chants.m4a", :size 5, :type "audio", :alias "mari chants"}],
           :objects
                          {:background
                                   {:type   "carousel",
                                    :x      0,
                                    :y      0,
                                    :width  1920,
                                    :height 1080,
                                    :first  "/raw/img/stadium/cycling/cycle_race_bg_01.jpg",
                                    :last   "/raw/img/stadium/cycling/cycle_race_bg_03.jpg",
                                    :next   "/raw/img/stadium/cycling/cycle_race_bg_02.jpg"},
                           :box1
                                   {:type        "animation",
                                    :x           0,
                                    :y           0,
                                    :width       671,
                                    :height      633,
                                    :scale       {:x -0.25, :y 0.25},
                                    :scene-name  "box1",
                                    :transition  "box1",
                                    :actions     {:click {:id "pick-box-1", :on "click", :type "action"}},
                                    :anim        "idle2",
                                    :anim-offset {:x 0, :y -300},
                                    :loop        true,
                                    :name        "boxes",
                                    :skin        "default",
                                    :speed       0.3,
                                    :start       true},
                           :box2
                                   {:type        "animation",
                                    :x           0,
                                    :y           0,
                                    :width       671,
                                    :height      633,
                                    :scale       {:x -0.25, :y 0.25},
                                    :scene-name  "box2",
                                    :transition  "box2",
                                    :actions     {:click {:id "pick-box-2", :on "click", :type "action"}},
                                    :anim        "idle2",
                                    :anim-offset {:x 0, :y -300},
                                    :loop        true,
                                    :name        "boxes",
                                    :skin        "default",
                                    :speed       0.3,
                                    :start       true},
                           :box3
                                   {:type        "animation",
                                    :x           0,
                                    :y           0,
                                    :width       671,
                                    :height      633,
                                    :scale       {:x -0.25, :y 0.25},
                                    :scene-name  "box3",
                                    :transition  "box3",
                                    :actions     {:click {:id "pick-box-3", :on "click", :type "action"}},
                                    :anim        "idle2",
                                    :anim-offset {:x 0, :y -300},
                                    :loop        true,
                                    :name        "boxes",
                                    :skin        "default",
                                    :speed       0.3,
                                    :start       true},
                           :group1
                                   {:type       "group",
                                    :x          2000,
                                    :y          683,
                                    :scene-name "group1",
                                    :transition "group1",
                                    :children   ["box1" "letter1" "image1"],
                                    :states     {:init {:type "transparent"}, :reset {:type "group"}}},
                           :group2
                                   {:type       "group",
                                    :x          2200,
                                    :y          789,
                                    :scene-name "group2",
                                    :transition "group2",
                                    :children   ["box2" "letter2" "image2"],
                                    :states     {:init {:type "transparent"}, :reset {:type "group"}}},
                           :group3
                                   {:type       "group",
                                    :x          2400,
                                    :y          932,
                                    :scene-name "group3",
                                    :transition "group3",
                                    :children   ["box3" "letter3" "image3"],
                                    :states     {:init {:type "transparent"}, :reset {:type "group"}}},
                           :image1 {:type "image", :x 0, :y -280, :width 100, :height 100, :origin {:type "center-top"}},
                           :image2 {:type "image", :x 0, :y -280, :width 100, :height 100, :origin {:type "center-top"}},
                           :image3 {:type "image", :x 0, :y -280, :width 100, :height 100, :origin {:type "center-top"}},
                           :letter1
                                   {:type           "text",
                                    :x              -110,
                                    :y              -180,
                                    :width          200,
                                    :height         200,
                                    :actions        {:click {:id "pick-box-1", :on "click", :type "action"}},
                                    :align          "center",
                                    :fill           "#d25523",
                                    :font-family    "Lexend Deca",
                                    :font-size      120,
                                    :shadow-blur    5,
                                    :shadow-color   "#1a1a1a",
                                    :shadow-offset  {:x 5, :y 5},
                                    :shadow-opacity 0.5,
                                    :text           "x",
                                    :vertical-align "middle"},
                           :letter2
                                   {:type           "text",
                                    :x              -110,
                                    :y              -180,
                                    :width          200,
                                    :height         200,
                                    :actions        {:click {:id "pick-box-2", :on "click", :type "action"}},
                                    :align          "center",
                                    :fill           "#d25523",
                                    :font-family    "Lexend Deca",
                                    :font-size      120,
                                    :shadow-blur    5,
                                    :shadow-color   "#1a1a1a",
                                    :shadow-offset  {:x 5, :y 5},
                                    :shadow-opacity 0.5,
                                    :text           "x",
                                    :vertical-align "middle"},
                           :letter3
                                   {:type           "text",
                                    :x              -110,
                                    :y              -180,
                                    :width          200,
                                    :height         200,
                                    :actions        {:click {:id "pick-box-3", :on "click", :type "action"}},
                                    :align          "center",
                                    :fill           "#d25523",
                                    :font-family    "Lexend Deca",
                                    :font-size      120,
                                    :shadow-blur    5,
                                    :shadow-color   "#1a1a1a",
                                    :shadow-offset  {:x 5, :y 5},
                                    :shadow-opacity 0.5,
                                    :text           "x",
                                    :vertical-align "middle"},
                           :mari
                                   {:type        "animation",
                                    :x           1265,
                                    :y           311,
                                    :width       473,
                                    :height      511,
                                    :scene-name  "mari",
                                    :transition  "mari",
                                    :anim        "idle",
                                    :anim-offset {:x 0, :y -150},
                                    :name        "mari",
                                    :scale-x     0.5,
                                    :scale-y     0.5,
                                    :speed       0.35,
                                    :start       true},
                           :vera
                                   {:type       "animation",
                                    :x          500,
                                    :y          721,
                                    :width      727,
                                    :height     1091,
                                    :scale      {:x 0.4, :y 0.4},
                                    :scene-name "vera",
                                    :transition "vera",
                                    :anim       "bike_ride",
                                    :meshes     true,
                                    :name       "vera-90",
                                    :skin       "default",
                                    :speed      0.3,
                                    :start      true}},
           :scene-objects [["background"] ["group1" "group2" "group3"] ["vera" "mari"]],
           :actions
                          {:current-concept-sound-x3
                                                    {:phrase             :concept-chant
                                                     :phrase-description "Concept chant"
                                                     :type               "action", :from-var [{:var-name "current-concept", :var-property "mari-sound-3"}]},
                           :finish-activity
                                                    {:type "sequence-data",
                                                     :data [{:id "cycling-letters", :type "finish-activity"} {:type "scene", :scene-id "stadium"}]},
                           :go-to-box1-line
                                                    {:type "sequence-data",
                                                     :data
                                                           [{:id "bike_turnup", :type "animation", :target "vera"}
                                                            {:id "bike_ride", :loop true, :type "add-animation", :target "vera"}
                                                            {:type "set-variable", :var-name "current-line", :var-value "box1"}
                                                            {:type "transition", :from-var [{:var-name "current-line-pos", :action-property "to"}], :transition-id "vera"}]},
                           :go-to-box2-line-down
                                                    {:type "sequence-data",
                                                     :data
                                                           [{:id "bike_turndown", :type "animation", :target "vera"}
                                                            {:id "bike_ride", :loop true, :type "add-animation", :target "vera"}
                                                            {:type "set-variable", :var-name "current-line", :var-value "box2"}
                                                            {:type "transition", :from-var [{:var-name "current-line-pos", :action-property "to"}], :transition-id "vera"}]},
                           :go-to-box2-line-up
                                                    {:type "sequence-data",
                                                     :data
                                                           [{:id "bike_turnup", :type "animation", :target "vera"}
                                                            {:id "bike_ride", :loop true, :type "add-animation", :target "vera"}
                                                            {:type "set-variable", :var-name "current-line", :var-value "box2"}
                                                            {:type "transition", :from-var [{:var-name "current-line-pos", :action-property "to"}], :transition-id "vera"}]},
                           :go-to-box3-line
                                                    {:type "sequence-data",
                                                     :data
                                                           [{:id "bike_turndown", :type "animation", :target "vera"}
                                                            {:id "bike_ride", :loop true, :type "add-animation", :target "vera"}
                                                            {:type "set-variable", :var-name "current-line", :var-value "box3"}
                                                            {:type "transition", :from-var [{:var-name "current-line-pos", :action-property "to"}], :transition-id "vera"}]},
                           :init-slots
                                                    {:type "parallel",
                                                     :data
                                                           [{:type "set-variable", :var-name "slot1", :var-value "box1"}
                                                            {:type "set-variable", :var-name "slot2", :var-value "box2"}
                                                            {:type "set-variable", :var-name "slot3", :var-value "box3"}]},
                           :init-vera-position      {:type "set-variable", :var-name "current-line", :var-value "box2"},
                           :mari-welcome-audio
                                                    {:phrase                 :welcome
                                                     :phrase-description     "Welcome"
                                                     :phrase-text            "Listen to the sounds and touch the letter that goes with the sound!"
                                                     :phrase-text-translated "Escucha los sonidos y toca la letra que va con el sonido! "
                                                     :type                   "animation-sequence",
                                                     :data                   [{:end 5.314, :anim "talk", :start 0.802}],
                                                     :target                 "mari",
                                                     :audio                  "/raw/audio/l2/a9/L2_A9_Mari.m4a",
                                                     :start                  0.24,
                                                     :duration               5.387,
                                                     :track                  1,
                                                     :offset                 0.24},
                           :pick-box-1
                                                    {:type "sequence-data",
                                                     :data
                                                           [{:type "set-variable", :var-name "current-line-pos", :var-value {:y 621, :duration 0.5}}
                                                            {:type "set-variable", :var-name "current-line-jump", :var-value {:y 421, :duration 1}}
                                                            {:type "set-variable", :var-name "jump-wait", :var-value 500}
                                                            {:fail     "go-to-box1-line",
                                                             :type     "test-value",
                                                             :value1   "box1",
                                                             :success  "stay-on-line",
                                                             :from-var [{:var-name "current-line", :action-property "value2"}]}
                                                            {:fail     "pick-wrong",
                                                             :type     "test-var-scalar",
                                                             :value    true,
                                                             :success  "pick-correct",
                                                             :from-var [{:var-name "slot1", :action-property "value"}],
                                                             :var-name "current-concept"}]},
                           :pick-box-2
                                                    {:type "sequence-data",
                                                     :data
                                                           [{:type "set-variable", :var-name "current-line-pos", :var-value {:y 721, :duration 0.5}}
                                                            {:type "set-variable", :var-name "current-line-jump", :var-value {:y 521, :duration 1}}
                                                            {:type "set-variable", :var-name "jump-wait", :var-value 800}
                                                            {:type     "case",
                                                             :options
                                                                       {:box1 {:id "go-to-box2-line-down", :type "action"},
                                                                        :box2 {:id "stay-on-line", :type "action"},
                                                                        :box3 {:id "go-to-box2-line-up", :type "action"}},
                                                             :from-var [{:var-name "current-line", :action-property "value"}]}
                                                            {:fail     "pick-wrong",
                                                             :type     "test-var-scalar",
                                                             :value    true,
                                                             :success  "pick-correct",
                                                             :from-var [{:var-name "slot2", :action-property "value"}],
                                                             :var-name "current-concept"}]},
                           :pick-box-3
                                                    {:type "sequence-data",
                                                     :data
                                                           [{:type "set-variable", :var-name "current-line-pos", :var-value {:y 821, :duration 0.5}}
                                                            {:type "set-variable", :var-name "current-line-jump", :var-value {:y 621, :duration 1}}
                                                            {:type "set-variable", :var-name "jump-wait", :var-value 1200}
                                                            {:fail     "go-to-box3-line",
                                                             :type     "test-value",
                                                             :value1   "box3",
                                                             :success  "stay-on-line",
                                                             :from-var [{:var-name "current-line", :action-property "value2"}]}
                                                            {:fail     "pick-wrong",
                                                             :type     "test-var-scalar",
                                                             :value    true,
                                                             :success  "pick-correct",
                                                             :from-var [{:var-name "slot3", :action-property "value"}],
                                                             :var-name "current-concept"}]},
                           :pick-correct
                                                    {:type "sequence-data",
                                                     :data
                                                           [{:id "mari-correct", :type "action"}
                                                            {:data
                                                                   [{:to {:x -700, :duration 4}, :type "transition", :transition-id "group1"}
                                                                    {:to {:x -500, :duration 4}, :type "transition", :transition-id "group2"}
                                                                    {:to {:x -300, :duration 4}, :type "transition", :transition-id "group3"}
                                                                    {:data
                                                                           [{:type "empty", :from-var [{:var-name "jump-wait", :action-property "duration"}]}
                                                                            {:id "bike_jump", :type "animation", :target "vera"}
                                                                            {:type "transition", :from-var [{:var-name "current-line-jump", :action-property "to"}], :transition-id "vera"}
                                                                            {:id "bike_ride", :loop true, :type "add-animation", :target "vera"}
                                                                            {:type "transition", :from-var [{:var-name "current-line-pos", :action-property "to"}], :transition-id "vera"}],
                                                                     :type "sequence-data"}],
                                                             :type "parallel"}
                                                            {:id "renew-current-concept", :type "action"}
                                                            {:id "current-concept-sound-x3", :type "action"}]},
                           :pick-wrong              {:id "mari-wrong", :type "action"},
                           :renew-current-concept
                                                    {:type "sequence-data",
                                                     :data
                                                           [{:data
                                                                   [{:id "init", :type "state", :target "group1"}
                                                                    {:id "init", :type "state", :target "group2"}
                                                                    {:id "init", :type "state", :target "group3"}],
                                                             :type "parallel"}
                                                            {:id "wait-for-box-animations", :type "action"}
                                                            {:data
                                                                   [{:to {:x 2000, :duration 0.1}, :type "transition", :transition-id "group1"}
                                                                    {:to {:x 2200, :duration 0.1}, :type "transition", :transition-id "group2"}
                                                                    {:to {:x 2400, :duration 0.1}, :type "transition", :transition-id "group3"}],
                                                             :type "parallel"}
                                                            {:data
                                                                   [{:id "reset", :type "state", :target "group1"}
                                                                    {:id "reset", :type "state", :target "group2"}
                                                                    {:id "reset", :type "state", :target "group3"}],
                                                             :type "parallel"}
                                                            {:id "wait-for-box-animations", :type "action"}
                                                            {:from        ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8" "item-9" "item-10"],
                                                             :type        "vars-var-provider",
                                                             :on-end      "finish-activity",
                                                             :shuffled    true,
                                                             :variables   ["current-concept"],
                                                             :provider-id "current-concept"}
                                                            {:from      ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8" "item-9" "item-10"],
                                                             :type      "vars-var-provider",
                                                             :unique    true,
                                                             :from-var
                                                                        [{:var-key         "concept-name",
                                                                          :var-name        "current-concept",
                                                                          :var-property    "concept-name",
                                                                          :action-property "exclude-property-values"}],
                                                             :shuffled  true,
                                                             :variables ["pair-concept-1" "pair-concept-2"]}
                                                            {:from      ["current-concept" "pair-concept-1" "pair-concept-2"],
                                                             :type      "vars-var-provider",
                                                             :shuffled  true,
                                                             :variables ["slot1" "slot2" "slot3"]}
                                                            {:data
                                                                   [{:type      "set-attribute",
                                                                     :target    "letter1",
                                                                     :from-var  [{:var-name "slot1", :var-property "letter", :action-property "attr-value"}],
                                                                     :attr-name "text"}
                                                                    {:type      "set-attribute",
                                                                     :target    "image1",
                                                                     :from-var  [{:var-name "slot1", :var-property "image-src", :action-property "attr-value"}],
                                                                     :attr-name "src"}
                                                                    {:type      "set-attribute",
                                                                     :target    "letter2",
                                                                     :from-var  [{:var-name "slot2", :var-property "letter", :action-property "attr-value"}],
                                                                     :attr-name "text"}
                                                                    {:type      "set-attribute",
                                                                     :target    "image2",
                                                                     :from-var  [{:var-name "slot2", :var-property "image-src", :action-property "attr-value"}],
                                                                     :attr-name "src"}
                                                                    {:type      "set-attribute",
                                                                     :target    "letter3",
                                                                     :from-var  [{:var-name "slot3", :var-property "letter", :action-property "attr-value"}],
                                                                     :attr-name "text"}
                                                                    {:type      "set-attribute",
                                                                     :target    "image3",
                                                                     :from-var  [{:var-name "slot3", :var-property "image-src", :action-property "attr-value"}],
                                                                     :attr-name "src"}],
                                                             :type "parallel"}
                                                            {:data
                                                                   [{:to {:x 1300, :duration 2}, :type "transition", :transition-id "group1"}
                                                                    {:to {:x 1500, :duration 2}, :type "transition", :transition-id "group2"}
                                                                    {:to {:x 1700, :duration 2}, :type "transition", :transition-id "group3"}],
                                                             :type "parallel"}]},
                           :renew-words
                                                    {:type        "lesson-var-provider",
                                                     :from        "concepts-group",
                                                     :limit       3,
                                                     :provider-id "words-set",
                                                     :repeat      4,
                                                     :shuffled    true,
                                                     :variables   ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8" "item-9" "item-10"]},
                           :start-scene
                                                    {:type "sequence",
                                                     :data
                                                           ["start-activity"
                                                            "init-slots"
                                                            "init-vera-position"
                                                            "renew-words"
                                                            "mari-welcome-audio"
                                                            "renew-current-concept"
                                                            "current-concept-sound-x3"]},
                           :start-activity          {:type "start-activity", :id "cycling-letters"},
                           :start-background-music  {:type "audio", :id "background", :loop true},
                           :stay-on-line            {:type "empty", :duration 100},
                           :stop-activity           {:type "stop-activity", :id "cycling-letters"},
                           :wait-for-box-animations {:type "empty", :duration 100}
                           :init-audio-correct      {:type "sequence-data"
                                                     :data [{:type "set-variable" :var-name "audio-correct1" :var-value "mari-audio-correct-1"}
                                                            {:type "set-variable" :var-name "audio-correct2" :var-value "mari-audio-correct-2"}
                                                            {:type "set-variable" :var-name "audio-correct3" :var-value "mari-audio-correct-3"}
                                                            {:type "set-variable" :var-name "audio-correct4" :var-value "mari-audio-correct-4"}
                                                            {:type "set-variable" :var-name "audio-correct5" :var-value "mari-audio-correct-5"}]}

                           :init-audio-try-again    {:type "sequence-data"
                                                     :data [{:type "set-variable" :var-name "audio-try-again1" :var-value "mari-audio-try-again-1"}
                                                            {:type "set-variable" :var-name "audio-try-again2" :var-value "mari-audio-try-again-2"}]}

                           :mari-correct            {:phrase             :correct-answer
                                                     :phrase-description "Correct answer"
                                                     :type               "sequence-data"
                                                     :data               [{:type "action" :id "init-audio-correct"}
                                                                          {:type      "vars-var-provider"
                                                                           :variables ["current-audio-correct"]
                                                                           :from      ["audio-correct1" "audio-correct2" "audio-correct3" "audio-correct4" "audio-correct5"]
                                                                           :shuffled  true}
                                                                          {:type "action" :from-var [{:var-name        "current-audio-correct" :action-property "id"
                                                                                                      :possible-values [:mari-audio-correct-1
                                                                                                                        :mari-audio-correct-2
                                                                                                                        :mari-audio-correct-3
                                                                                                                        :mari-audio-correct-4
                                                                                                                        :mari-audio-correct-5]}]}]}

                           :mari-wrong              {:phrase             :wrong-answer
                                                     :phrase-description "Wrong answer"
                                                     :type               "sequence-data"
                                                     :data               [{:type "action" :id "init-audio-try-again"}
                                                                          {:type      "vars-var-provider"
                                                                           :variables ["current-audio-try-again"]
                                                                           :from      ["audio-try-again1" "audio-try-again2"]
                                                                           :shuffled  true}
                                                                          {:type "action" :from-var [{:var-name        "current-audio-try-again" :action-property "id"
                                                                                                      :possible-values [:mari-audio-try-again-1
                                                                                                                        :mari-audio-try-again-2]}]}]}

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
                                                     :data        [{:start 14.611, :end 15.11, :duration 0.499, :anim "talk"}
                                                                   {:start 15.529, :end 16.676, :duration 1.147, :anim "talk"}],
                                                     :phrase-text "Wow, excelent!"}

                           :mari-audio-try-again-1  {:type        "animation-sequence",
                                                     :target      "mari",
                                                     :track       1,
                                                     :offset      18.915,
                                                     :audio       "/raw/audio/l1/a5/Mari_Coaching_statements.m4a",
                                                     :start       18.915,
                                                     :duration    1.493,
                                                     :data        [{:start 18.983, :end 20.242, :duration 1.259, :anim "talk"}],
                                                     :phrase-text "Try again!"}

                           :mari-audio-try-again-2  {:type        "animation-sequence",
                                                     :target      "mari",
                                                     :track       1,
                                                     :offset      22.155,
                                                     :audio       "/raw/audio/l1/a5/Mari_Coaching_statements.m4a",
                                                     :start       22.155,
                                                     :duration    3.025,
                                                     :data        [{:start 23.743, :end 25.055, :duration 1.312, :anim "talk"}],
                                                     :phrase-text "Hmmmm. Try again."}},
           :triggers      {:stop {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
           :metadata      {:prev "stadium", :autostart true},
           :audio         {:background "/raw/audio/background/POL-daily-special-short.mp3"}})
