(ns webchange.templates.library.volleyball
  (:require
    [webchange.templates.core :as core]))

(def m {:id          9
        :name        "Volleyball"
        :tags        ["Independent Practice"]
        :description "Some description of volleyball mechanics and covered skills"
        :lesson-sets ["concepts"]
        :fields      [
                      {:name "image-src",
                       :type "image"}
                      {:name "word-image-1",
                       :type "image"}
                      {:name "word-image-2",
                       :type "image"}
                      {:name "word-image-3",
                       :type "image"}
                      {:name "word-image-4",
                       :type "image"}

                      ]})


(def t {:assets
                       [{:url "/raw/img/stadium/volleyball/background.jpg", :size 10, :type "image"}
                        {:url "/raw/img/stadium/volleyball/ball.png", :size 1, :type "image"}],
        :objects
                       {:background {:type "background", :src "/raw/img/stadium/volleyball/background.jpg"},
                        :ball
                                    {:type "group", :x 1163, :y 941, :width 126, :height 126, :transition "ball-transition", :children ["ball-image"]},
                        :ball-image
                                    {:type    "image",
                                     :src     "/raw/img/stadium/volleyball/ball.png"
                                     :x       -1
                                     :y       -1
                                     :width   128,
                                     :height  128,
                                     :origin  {:type "center-center"},
                                     :scale-x 0.55,
                                     :scale-y 0.55,
                                     :visible false
                                     :states  {:hidden  {:visible false},
                                               :visible {:visible true}}},
                        :box1
                                    {:type        "animation",
                                     :x           1304,
                                     :y           831,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.3, :y 0.3},
                                     :origin      {:type "center-center"},
                                     :scene-name  "box1",
                                     :actions     {:click {:id "check-concept", :on "click", :type "action", :params {:player "player1"}}},
                                     :anim        "idle1",
                                     :anim-offset {:x 0, :y -300},
                                     :name        "boxes",
                                     :skin        "empty"},
                        :box2
                                    {:type        "animation",
                                     :x           1665,
                                     :y           666,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.3, :y 0.3},
                                     :origin      {:type "center-center"},
                                     :scene-name  "box2",
                                     :actions     {:click {:id "check-concept", :on "click", :type "action", :params {:player "player2"}}},
                                     :anim        "idle1",
                                     :anim-offset {:x 0, :y -300},
                                     :name        "boxes",
                                     :skin        "empty"},
                        :box3
                                    {:type        "animation",
                                     :x           409,
                                     :y           831,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.3, :y 0.3},
                                     :origin      {:type "center-center"},
                                     :scene-name  "box3",
                                     :actions     {:click {:id "check-concept", :on "click", :type "action", :params {:player "player3"}}},
                                     :anim        "idle1",
                                     :anim-offset {:x 0, :y -300},
                                     :name        "boxes",
                                     :skin        "empty"},
                        :box4
                                    {:type        "animation",
                                     :x           729,
                                     :y           666,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.3, :y 0.3},
                                     :origin      {:type "center-center"},
                                     :scene-name  "box4",
                                     :actions     {:click {:id "check-concept", :on "click", :type "action", :params {:player "player4"}}},
                                     :anim        "idle1",
                                     :anim-offset {:x 0, :y -300},
                                     :name        "boxes",
                                     :skin        "empty"},
                        :boy1
                                    {:type       "animation",
                                     :x          1556,
                                     :y          871,
                                     :width      758,
                                     :height     1130,
                                     :scale      {:x 0.25, :y 0.25},
                                     :scene-name "boy1",
                                     :anim       "idle",
                                     :name       "vera-45",
                                     :skin       "1_boy",
                                     :speed      0.8,
                                     :start      true},
                        :girl2
                                    {:type       "animation",
                                     :x          297,
                                     :y          1010,
                                     :width      758,
                                     :height     1130,
                                     :scale      {:x -0.25, :y 0.25},
                                     :scene-name "girl2",
                                     :anim       "idle",
                                     :name       "vera-45",
                                     :skin       "2_girl",
                                     :speed      0.8,
                                     :start      true},
                        :girl3
                                    {:type       "animation",
                                     :x          612,
                                     :y          871,
                                     :width      758,
                                     :height     1130,
                                     :scale      {:x -0.25, :y 0.25},
                                     :scene-name "girl3",
                                     :anim       "idle",
                                     :name       "vera-45",
                                     :skin       "3_girl",
                                     :speed      0.8,
                                     :start      true},
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
                                     :x          1182,
                                     :y          1010,
                                     :width      758,
                                     :height     1130,
                                     :scale      {:x 0.25, :y 0.25},
                                     :scene-name "vera",
                                     :anim       "volley_idle",
                                     :name       "vera-45",
                                     :skin       "vera",
                                     :speed      0.8,
                                     :start      true}},
        :scene-objects [["background"] ["vera" "boy1" "girl2" "girl3" "box1" "box2" "box3" "box4"] ["ball" "mari"]],
        :actions
                       {:animate-catch-player1
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:id "volley_call", :type "animation", :target "vera"}
                                                         {:id "volley_idle", :loop true, :type "add-animation", :target "vera"}]},
                        :animate-catch-player2
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:id "volley_call", :type "animation", :target "boy1"}
                                                         {:id "volley_idle", :loop true, :type "add-animation", :target "boy1"}]},
                        :animate-catch-player3
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:id "volley_call", :type "animation", :target "girl2"}
                                                         {:id "volley_idle", :loop true, :type "add-animation", :target "girl2"}]},
                        :animate-catch-player4
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:id "volley_call", :type "animation", :target "girl3"}
                                                         {:id "volley_idle", :loop true, :type "add-animation", :target "girl3"}]},
                        :animate-throw-player1
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:id "volley_throw", :type "animation", :target "vera"}
                                                         {:id "idle", :loop true, :type "add-animation", :target "vera"}]},
                        :animate-throw-player2
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:id "volley_throw", :type "animation", :target "boy1"}
                                                         {:id "idle", :loop true, :type "add-animation", :target "boy1"}]},
                        :animate-throw-player3
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:id "volley_throw", :type "animation", :target "girl2"}
                                                         {:id "idle", :loop true, :type "add-animation", :target "girl2"}]},
                        :animate-throw-player4
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:id "volley_throw", :type "animation", :target "girl3"}
                                                         {:id "idle", :loop true, :type "add-animation", :target "girl3"}]},
                        :check-concept
                                                 {:type        "test-var-scalar",
                                                  :var-name    "current-target",
                                                  :success     "pick-correct",
                                                  :fail        "pick-wrong",
                                                  :from-params [{:param-property "player", :action-property "value"}]},
                        :clear-instruction       {:type "remove-flows", :flow-tag "instruction"},
                        :empty-before-dialog     {:type "empty", :duration 3000},
                        :finish-activity
                                                 {:type "sequence-data", :data [{:id "volleyball", :type "finish-activity"}]},
                        :init-audio-correct
                                                 {:type "parallel",
                                                  :data
                                                        [{:type "set-variable", :var-name "audio-correct1", :var-value "mari-audio-correct-1"}
                                                         {:type "set-variable", :var-name "audio-correct2", :var-value "mari-audio-correct-2"}
                                                         {:type "set-variable", :var-name "audio-correct3", :var-value "mari-audio-correct-3"}
                                                         {:type "set-variable", :var-name "audio-correct4", :var-value "mari-audio-correct-4"}
                                                         {:type "set-variable", :var-name "audio-correct5", :var-value "mari-audio-correct-5"}]},
                        :init-audio-try-again
                                                 {:type "parallel",
                                                  :data
                                                        [{:type "set-variable", :var-name "audio-try-again1", :var-value "mari-audio-try-again-1"}
                                                         {:type "set-variable", :var-name "audio-try-again2", :var-value "mari-audio-try-again-2"}]},
                        :init-first-player       {:type "set-variable", :var-name "current-player", :var-value "player1"},
                        :init-slots
                                                 {:type "parallel",
                                                  :data
                                                        [{:type "set-variable", :var-name "slot1", :var-value "player1"}
                                                         {:type "set-variable", :var-name "slot2", :var-value "player2"}
                                                         {:type "set-variable", :var-name "slot3", :var-value "player3"}
                                                         {:type "set-variable", :var-name "slot4", :var-value "player4"}]},
                        :mari-audio-correct-1 {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :concept-var        "current-word",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase                        "correct-1",
                                               :phrase-description-translated "correct-1",
                                               },
                        :mari-audio-correct-2 {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :concept-var        "current-word",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase                        "correct-2",
                                               :phrase-description-translated "correct-2",
                                               },
                        :mari-audio-correct-3 {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :concept-var        "current-word",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase                        "correct-3",
                                               :phrase-description-translated "correct-3",
                                               },
                        :mari-audio-correct-4 {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :concept-var        "current-word",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase                        "correct-4",
                                               :phrase-description-translated "correct-4",
                                               },
                        :mari-audio-correct-5 {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :concept-var        "current-word",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase                        "correct-5",
                                               :phrase-description-translated "correct-5",
                                               },
                        :mari-audio-try-again-1 {:type               "sequence-data",
                                                 :editor-type        "dialog",
                                                 :concept-var        "current-word",
                                                 :data               [{:type "sequence-data"
                                                                       :data [{:type "empty" :duration 0}
                                                                              {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                 :phrase                        "try-again-1",
                                                 :phrase-description-translated "try-again-1",
                                                 },
                        :mari-audio-try-again-2 {:type               "sequence-data",
                                                 :editor-type        "dialog",
                                                 :concept-var        "current-word",
                                                 :data               [{:type "sequence-data"
                                                                       :data [{:type "empty" :duration 0}
                                                                              {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                 :phrase                        "try-again-2",
                                                 :phrase-description-translated "try-again-2",
                                                 },
                        :mari-say-correct
                                                 {:type               "sequence",
                                                  :data               ["mari-say-correct-1" "mari-say-correct-2"]},
                        :mari-say-correct-1
                                                 {:type      "vars-var-provider",
                                                  :from      ["audio-correct1" "audio-correct2" "audio-correct3" "audio-correct4" "audio-correct5"],
                                                  :shuffled  true,
                                                  :variables ["current-audio-correct"]},
                        :mari-say-correct-2
                                                 {:type "action",
                                                  :from-var
                                                        [{:var-name        "current-audio-correct",
                                                          :action-property "id",
                                                          :possible-values
                                                                           ["mari-audio-correct-1"
                                                                            "mari-audio-correct-2"
                                                                            "mari-audio-correct-3"
                                                                            "mari-audio-correct-4"
                                                                            "mari-audio-correct-5"]}]},
                        :mari-say-wrong
                                                 {:type               "sequence-data",
                                                  :data
                                                                      [{:from      ["audio-try-again1" "audio-try-again2"],
                                                                        :type      "vars-var-provider",
                                                                        :shuffled  true,
                                                                        :variables ["current-audio-try-again"]}
                                                                       {:type "action",
                                                                        :from-var
                                                                              [{:var-name        "current-audio-try-again",
                                                                                :action-property "id",
                                                                                :possible-values ["mari-audio-try-again-1" "mari-audio-try-again-2"]}]}]},
                        :mari-welcome-audio-1 {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :concept-var        "current-word",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase             "welcome",
                                               :phrase-description "Welcome speech",
                                               },
                        :pick-correct
                                                 {:type "parallel",
                                                  :data
                                                        [{:id "mari-say-correct", :type "action"}
                                                         {:type     "pick-correct",
                                                          :activity "volleyball",
                                                          :from-var [{:var-name "current-concept", :var-property "concept-name", :action-property "concept-name"}]}
                                                         {:id "throw", :type "action"}]},
                        :pick-wrong
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:id "mari-say-wrong", :type "action"}
                                                         {:type "copy-variable", :var-name "picked", :from-params [{:param-property "player", :action-property "from"}]}
                                                         {:type     "pick-wrong",
                                                          :activity "volleyball",
                                                          :from-var
                                                                    [{:var-name "current-concept", :var-property "concept-name", :action-property "concept-name"}
                                                                     {:var-name "picked", :action-property "option"}]}]},
                        :renew-current-concept
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:from        ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8" "item-9" "item-10"],
                                                          :type        "vars-var-provider",
                                                          :on-end      "finish-activity",
                                                          :shuffled    false,
                                                          :variables   ["current-concept"],
                                                          :provider-id "current-concept"}
                                                         {:data
                                                                [{:data
                                                                        [{:type     "set-variable",
                                                                          :from-var [{:var-name "current-concept", :var-property "word-image-1", :action-property "var-value"}],
                                                                          :var-name "player1"}
                                                                         {:type     "set-variable",
                                                                          :from-var [{:var-name "current-concept", :var-property "word-image-2", :action-property "var-value"}],
                                                                          :var-name "player2"}
                                                                         {:type     "set-variable",
                                                                          :from-var [{:var-name "current-concept", :var-property "word-image-3", :action-property "var-value"}],
                                                                          :var-name "player3"}
                                                                         {:type     "set-variable",
                                                                          :from-var [{:var-name "current-concept", :var-property "word-image-4", :action-property "var-value"}],
                                                                          :var-name "player4"}],
                                                                  :type "parallel"}
                                                                 {:from      ["player1" "player2" "player3" "player4"],
                                                                  :type      "vars-var-provider",
                                                                  :shuffled  true,
                                                                  :variables ["player1" "player2" "player3" "player4"]}],
                                                          :type "sequence-data"}
                                                         {:data
                                                                [{:from      ["slot1" "slot2" "slot3" "slot4"],
                                                                  :type      "vars-var-provider",
                                                                  :from-var  [{:var-name "current-player", :to-vector true, :action-property "exclude-values"}],
                                                                  :shuffled  true,
                                                                  :variables ["current-target"]}
                                                                 {:type "set-variable",
                                                                  :from-var
                                                                        [{:var-name "current-concept", :var-property "image-src", :action-property "var-value"}
                                                                         {:var-name "current-target", :action-property "var-name"}]}
                                                                 {:type     "set-current-concept",
                                                                  :from-var [{:var-name "current-concept", :var-property "concept-name", :action-property "value"}]}],
                                                          :type "sequence-data"}
                                                         {:data
                                                                [{:type       "set-slot",
                                                                  :target     "box1",
                                                                  :from-var   [{:var-name "player1", :action-property "image"}],
                                                                  :slot-name  "box1",
                                                                  :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                                 {:type       "set-slot",
                                                                  :target     "box2",
                                                                  :from-var   [{:var-name "player2", :action-property "image"}],
                                                                  :slot-name  "box1",
                                                                  :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                                 {:type       "set-slot",
                                                                  :target     "box3",
                                                                  :from-var   [{:var-name "player3", :action-property "image"}],
                                                                  :slot-name  "box1",
                                                                  :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                                 {:type       "set-slot",
                                                                  :target     "box4",
                                                                  :from-var   [{:var-name "player4", :action-property "image"}],
                                                                  :slot-name  "box1",
                                                                  :attachment {:x 40, :scale-x 4, :scale-y 4}}],
                                                          :type "parallel"}]},
                        :renew-words
                                                 {:type        "lesson-var-provider",
                                                  :from        "concepts",
                                                  :limit       3,
                                                  :provider-id "words-set",
                                                  :repeat      4,
                                                  :shuffled    true,
                                                  :variables   ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8" "item-9" "item-10"]},
                        :start-activity          {:type "start-activity", :id "volleyball"},
                        :start-scene
                                                 {:type "sequence",
                                                  :data
                                                        ["start-activity"
                                                         "clear-instruction"
                                                         "init-slots"
                                                         "init-first-player"
                                                         "init-audio-correct"
                                                         "init-audio-try-again"
                                                         "renew-words"
                                                         "renew-current-concept"
                                                         "mari-welcome-audio-1"
                                                         "voice-high-var"]},
                        :stop-activity           {:type "stop-activity", :id "volleyball"},
                        :throw
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:type "action", :from-var [{:template "animate-throw-%", :var-name "current-player", :action-property "id"}]}
                                                         {:id "visible", :type "state", :target "ball-image"}
                                                         {:type "empty", :duration 200}
                                                         {:type "parallel"
                                                          :data [{:type "action", :from-var [{:template "throw-from-%", :var-name "current-player", :action-property "id"}]}
                                                                 {:type "sequence-data",
                                                                  :data [{:type "empty", :duration 900}
                                                                         {:type        "action",
                                                                          :from-params [{:template "animate-catch-%", :param-property "player", :action-property "id"}]}
                                                                         {:type "empty", :duration 800}
                                                                         {:id "hidden", :type "state", :target "ball-image"}]}],}
                                                         {:type        "set-variable",
                                                          :var-name    "current-player",
                                                          :from-params [{:param-property "player", :action-property "var-value"}]}
                                                         {:id "renew-current-concept", :type "action"}
                                                         {:id "voice-high-var", :type "action"}]},
                        :throw-from-player1
                                                 {:type "action", :from-params [{:template "throw-player1-%", :param-property "player", :action-property "id"}]},
                        :throw-from-player2
                                                 {:type "action", :from-params [{:template "throw-player2-%", :param-property "player", :action-property "id"}]},
                        :throw-from-player3
                                                 {:type "action", :from-params [{:template "throw-player3-%", :param-property "player", :action-property "id"}]},
                        :throw-from-player4
                                                 {:type "action", :from-params [{:template "throw-player4-%", :param-property "player", :action-property "id"}]},
                        :throw-player1-player2
                                                 {:type          "transition",
                                                  :to            {:bezier [{:x 1316, :y 342} {:x 1390, :y 203} {:x 1540, :y 803}], :duration 1.4},
                                                  :transition-id "ball-transition"},
                        :throw-player1-player3
                                                 {:type          "transition",
                                                  :to            {:bezier [{:x 825, :y 342} {:x 655, :y 342} {:x 314, :y 942}], :duration 1.4},
                                                  :transition-id "ball-transition"},
                        :throw-player1-player4
                                                 {:type          "transition",
                                                  :to            {:bezier [{:x 951, :y 342} {:x 843, :y 203} {:x 628, :y 803}], :duration 1.4},
                                                  :transition-id "ball-transition"},
                        :throw-player2-player1
                                                 {:type          "transition",
                                                  :to            {:bezier [{:x 1390, :y 203} {:x 1316, :y 342} {:x 1166, :y 942}], :duration 1.4},
                                                  :transition-id "ball-transition"},
                        :throw-player2-player3
                                                 {:type          "transition",
                                                  :to            {:bezier [{:x 1050, :y 203} {:x 804, :y 342} {:x 314, :y 942}], :duration 1.4},
                                                  :transition-id "ball-transition"},
                        :throw-player2-player4
                                                 {:type          "transition",
                                                  :to            {:bezier [{:x 1175, :y 203} {:x 993, :y 203} {:x 628, :y 803}], :duration 1.4},
                                                  :transition-id "ball-transition"},
                        :throw-player3-player1
                                                 {:type          "transition",
                                                  :to            {:bezier [{:x 655, :y 342} {:x 825, :y 342} {:x 1166, :y 942}], :duration 1.4},
                                                  :transition-id "ball-transition"},
                        :throw-player3-player2
                                                 {:type          "transition",
                                                  :to            {:bezier [{:x 804, :y 342} {:x 1050, :y 203} {:x 1540, :y 803}], :duration 1.4},
                                                  :transition-id "ball-transition"},
                        :throw-player3-player4
                                                 {:type          "transition",
                                                  :to            {:bezier [{:x 440, :y 342} {:x 502, :y 203} {:x 628, :y 803}], :duration 1.4},
                                                  :transition-id "ball-transition"},
                        :throw-player4-player1
                                                 {:type          "transition",
                                                  :to            {:bezier [{:x 843, :y 203} {:x 951, :y 342} {:x 1166, :y 942}], :duration 1.4},
                                                  :transition-id "ball-transition"},
                        :throw-player4-player2
                                                 {:type          "transition",
                                                  :to            {:bezier [{:x 993, :y 203} {:x 1175, :y 203} {:x 1540, :y 803}], :duration 1.4},
                                                  :transition-id "ball-transition"},
                        :throw-player4-player3
                                                 {:type          "transition",
                                                  :to            {:bezier [{:x 502, :y 203} {:x 440, :y 342} {:x 314, :y 942}], :duration 1.4},
                                                  :transition-id "ball-transition"},
                        :voice-high-var
                                                 {:type               "sequence-data",
                                                  :editor-type        "dialog",
                                                  :concept-var        "current-word",
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                  :phrase                        "Game voicer",
                                                  :phrase-description-translated "Game voice. Introduce concept",
                                                  },
                        :wait-for-box-animations {:type "empty", :duration 500}},
        :triggers      {:back {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata      {:prev "stadium", :autostart true},
        :templates     ["coaching"]})

(defn f
  [t args]
  t)

(core/register-template
  (:id m)
  m
  (partial f t))