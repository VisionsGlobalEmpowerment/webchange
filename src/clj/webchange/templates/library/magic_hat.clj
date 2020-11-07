(ns webchange.templates.library.magic_hat
  (:require
    [webchange.templates.core :as core]))

(def m {:id          10
        :name        "Magic hat"
        :tags        ["Direct Instruction - Animated Instructor"]
        :description "Some description of magic hat mechanics and covered skills"
        :lesson-sets ["concepts-single", "concepts-all"]
        :fields      [
                      {:name "image-src",
                       :type "image"}
                      {:name "letter",
                       :type "string"}
                      ]})


(def t {:assets
                       [{:url "/raw/img/library/magic-hat/background_magic_hat.png", :type "image"}
                        {:url "/raw/img/library/magic-hat/decoration_magic_hat.png", :type "image"}
                        {:url "/raw/img/library/magic-hat/surface_magic_hat.png", :type "image"}],
        :objects
                       {
                        :layered-background {
                                             :type       "layered-background"
                                             :background {:src "/raw/img/library/magic-hat/background_magic_hat.png"}
                                             :decoration {:src "/raw/img/library/magic-hat/decoration_magic_hat.png"},
                                             :surface    {:src "/raw/img/library/magic-hat/surface_magic_hat.png"},
                                             },
                        :box1
                                            {:type       "animation",
                                             :x          1270,
                                             :y          950,
                                             :width       771,
                                             :height      1033,
                                             :scene-name "box1",
                                             :transition "box1",
                                             :actions    {:click {:id "pick-box", :on "click", :type "action", :params {:target "box1"}, :unique-tag "click"}},
                                             :anim       "idle2",
                                             :loop       true,
                                             :name       "boxes",
                                             :scale-x    0.25,
                                             :scale-y    0.25,
                                             :skin       "qwestion",
                                             :speed      0.5,
                                             :start      true,
                                             :visible    false,
                                             :states     {:animation {:visible true}}},
                        :box2
                                            {:type       "animation",
                                             :x          1460,
                                             :y          920,
                                             :width       771,
                                             :height      1033,
                                             :scene-name "box2",
                                             :transition "box2",
                                             :actions    {:click {:id "pick-box", :on "click", :type "action", :params {:target "box2"}, :unique-tag "click"}},
                                             :anim       "idle2",
                                             :loop       true,
                                             :name       "boxes",
                                             :scale-x    0.25,
                                             :scale-y    0.25,
                                             :skin       "qwestion",
                                             :speed      0.5,
                                             :start      true,
                                             :visible    false,
                                             :states {:animation {:visible true}}},
                        :box3
                                            {:type       "animation",
                                             :x          1660,
                                             :y          940,
                                             :width       771,
                                             :height      1033,
                                             :scene-name "box3",
                                             :transition "box3",
                                             :actions    {:click {:id "pick-box", :on "click", :type "action", :params {:target "box3"}, :unique-tag "click"}},
                                             :anim       "idle2",
                                             :loop       true,
                                             :name       "boxes",
                                             :scale-x    0.25,
                                             :scale-y    0.25,
                                             :skin       "qwestion",
                                             :speed      0.5,
                                             :start      true,
                                             :visible    false,
                                             :states {:animation {:visible true}}},
                        :hat
                                            {:type       "animation",
                                             :x          625,
                                             :y          974,
                                             :width      710,
                                             :height     1231,
                                             :anim-offset {:x 300, :y -419.73},
                                             :scene-name "magic-hat",
                                             :transition "hat",
                                             :anim       "idle",
                                             :loop       true,
                                             :name       "hat",
                                             :scale-x    1,
                                             :scale-y    1,
                                             :speed      1,
                                             :start      true},
                        :letter1
                                            {:type           "text",
                                             :x              1175,
                                             :y              570,
                                             :width          200,
                                             :height         130,
                                             :align          "center",
                                             :fill           "white",
                                             :font-family    "Lexend Deca",
                                             :font-size      160,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "",
                                             :vertical-align "middle",
                                             :visible        false},
                        :letter2
                                            {:type           "text",
                                             :x              1365,
                                             :y              540,
                                             :width          200,
                                             :height         130,
                                             :align          "center",
                                             :fill           "white",
                                             :font-family    "Lexend Deca",
                                             :font-size      160,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "",
                                             :vertical-align "middle",
                                             :visible        false},
                        :letter3
                                            {:type           "text",
                                             :x              1565,
                                             :y              560,
                                             :width          200,
                                             :height         130,
                                             :align          "center",
                                             :fill           "white",
                                             :font-family    "Lexend Deca",
                                             :font-size      160,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "",
                                             :vertical-align "middle",
                                             :visible        false},
                        :mari
                                            {:type       "animation",
                                             :x          1500,
                                             :y          370,
                                             :width      473,
                                             :height     511,
                                             :scene-name "mari",
                                             :transition "mari",
                                             :anim       "idle",
                                             :loop       true,
                                             :name       "mari",
                                             :scale-x    0.5,
                                             :scale-y    0.5,
                                             :speed      1,
                                             :start      true}
                        },
        :scene-objects [["layered-background"] ["hat" "box1" "box2" "box3" "letter1" "letter2" "letter3"] ["mari"]],
        :actions
                       {
                        :box-jump-in
                                           {:type "parallel",
                                            :data
                                                  [{:id "jump2", :type "animation", :from-params [{:param-property "target", :action-property "target"}]}
                                                   {:data
                                                          [{:type "empty", :duration 500}
                                                           {:to          {:bezier [{:x 960, :y 100} {:x 610, :y 435}], :duration 1.0},
                                                            :type        "transition",
                                                            :from-params [{:param-property "target", :action-property "transition-id"}]}],
                                                    :type "sequence-data"}
                                                   {:id          "idle2",
                                                    :loop        true,
                                                    :type        "add-animation",
                                                    :from-params [{:param-property "target", :action-property "target"}]}]},
                        :boxes-disappear
                                           {:type "parallel",
                                            :data
                                                  [{:skin "qwestion", :type "set-skin", :target "box1"}
                                                   {:skin "qwestion", :type "set-skin", :target "box2"}
                                                   {:skin "qwestion", :type "set-skin", :target "box3"}
                                                   {:type "set-attribute", :target "box1", :attr-name "visible", :attr-value false}
                                                   {:type "set-attribute", :target "box2", :attr-name "visible", :attr-value false}
                                                   {:type "set-attribute", :target "box3", :attr-name "visible", :attr-value false}]},
                        :boxes-jump-out
                                           {:type "sequence-data",
                                            :data
                                                  [{:id "boxes-reset-position", :type "action"}
                                                   {:type "empty", :duration 200}
                                                   {:id "boxes-show-up", :type "action"}
                                                   {:id "boxes-move-to-position", :type "action"}]},
                        :boxes-move-to-position
                                           {:type "parallel",
                                            :data
                                                  [{:to {:bezier [{:x 960, :y 100} {:x 1270, :y 800}], :duration 2.0}, :type "transition", :transition-id "box1"}
                                                   {:to {:bezier [{:x 1055, :y 100} {:x 1460, :y 770}], :duration 2.0}, :type "transition", :transition-id "box2"}
                                                   {:to {:bezier [{:x 1195, :y 100} {:x 1660, :y 790}], :duration 2.0}, :type "transition", :transition-id "box3"}]},
                        :boxes-reset-position
                                           {:type "parallel",
                                            :data
                                                  [{:type "set-attribute", :target "box1", :attr-name "x", :attr-value 610}
                                                   {:type "set-attribute", :target "box1", :attr-name "y", :attr-value 435}
                                                   {:type "set-attribute", :target "box2", :attr-name "x", :attr-value 610}
                                                   {:type "set-attribute", :target "box2", :attr-name "y", :attr-value 435}
                                                   {:type "set-attribute", :target "box3", :attr-name "x", :attr-value 610}
                                                   {:type "set-attribute", :target "box3", :attr-name "y", :attr-value 435}]},
                        :boxes-show-up
                                           {:type "parallel",
                                            :data
                                                  [{:data
                                                          [{:type "set-attribute", :target "box1", :attr-name "visible", :attr-value true}
                                                           {:type "empty", :duration 200}
                                                           {:id "come2", :type "animation", :target "box1"}
                                                           {:id "idle2", :loop true, :type "add-animation", :target "box1"}],
                                                    :type "sequence-data"}
                                                   {:data
                                                          [{:type "set-attribute", :target "box2", :attr-name "visible", :attr-value true}
                                                           {:type "empty", :duration 200}
                                                           {:id "come2", :type "animation", :target "box2"}
                                                           {:id "idle2", :loop true, :type "add-animation", :target "box2"}],
                                                    :type "sequence-data"}
                                                   {:data
                                                          [{:type "set-attribute", :target "box3", :attr-name "visible", :attr-value true}
                                                           {:type "empty", :duration 200}
                                                           {:id "come2", :type "animation", :target "box3"}
                                                           {:id "idle2", :loop true, :type "add-animation", :target "box3"}],
                                                    :type "sequence-data"}]},
                        :check-level
                                           {:type "sequence-data",
                                            :data
                                                  [{:fail     {:type "empty", :duration 100},
                                                    :type     "test-var-scalar",
                                                    :value    3,
                                                    :success
                                                              {:data
                                                                     [{:type "set-variable", :var-name "correct-answers-in-row", :var-value 0}
                                                                      {:fail     {:type "set-variable", :var-name "current-level", :var-value "hard"},
                                                                       :type     "test-var-scalar",
                                                                       :value    "easy",
                                                                       :success  {:type "set-variable", :var-name "current-level", :var-value "mid"},
                                                                       :var-name "current-level"}],
                                                               :type "sequence-data"},
                                                    :var-name "correct-answers-in-row"}
                                                   {:fail     {:type "empty", :duration 100},
                                                    :type     "test-var-scalar",
                                                    :value    3,
                                                    :success
                                                              {:data
                                                                     [{:type "set-variable", :var-name "wrong-answers-in-row", :var-value 0}
                                                                      {:fail     {:type "set-variable", :var-name "current-level", :var-value "easy"},
                                                                       :type     "test-var-scalar",
                                                                       :value    "hard",
                                                                       :success  {:type "set-variable", :var-name "current-level", :var-value "mid"},
                                                                       :var-name "current-level"}],
                                                               :type "sequence-data"},
                                                    :var-name "wrong-answers-in-row"}]},
                        :count-correct-answer
                                           {:type "sequence-data",
                                            :data
                                                  [{:type "counter", :counter-id "correct-answers-in-row", :counter-action "increase"}
                                                   {:type "set-variable", :var-name "wrong-answers-in-row", :var-value 0}]},
                        :count-wrong-answer
                                           {:type "sequence-data",
                                            :data
                                                  [{:type "counter", :counter-id "wrong-answers-in-row", :counter-action "increase"}
                                                   {:type "set-variable", :var-name "correct-answers-in-row", :var-value 0}]},
                        :current-concept-chant {:type               "sequence-data",
                                                :editor-type        "dialog",
                                                :concept-var        "current-word",
                                                :data               [{:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                :phrase             :task-easy
                                                :phrase-description "Task easy"
                                                },
                        :current-concept-sound-x3 {
                                                   :type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :concept-var        "current-word",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             :task-hard
                                                   :phrase-description "Task hard"
                                                   },
                        :finish-activity
                                           {:id "magic-hat", :type "finish-activity"},
                        :init-vars
                                           {:type "parallel",
                                            :data
                                                  [{:type "set-variable", :var-name "current-level", :var-value "mid"}
                                                   {:type "set-variable", :var-name "correct-answers-in-row", :var-value 0}
                                                   {:type "set-variable", :var-name "wrong-answers-in-row", :var-value 0}
                                                   {:type "set-variable", :var-name "last-pick-wrong", :var-value false}]},
                        :intro             {:type               "sequence",
                                            :data               ["mari-voice-welcome" "mari-flies-to-hat" "mari-init-wand" "mari-voice-intro"]},
                        :letters-hide
                                           {:type "parallel",
                                            :data
                                                  [{:type "set-attribute", :target "letter1", :attr-name "visible", :attr-value false}
                                                   {:type "set-attribute", :target "letter2", :attr-name "visible", :attr-value false}
                                                   {:type "set-attribute", :target "letter3", :attr-name "visible", :attr-value false}]},
                        :letters-show
                                           {:type "parallel",
                                            :data
                                                  [{:type "set-attribute", :target "letter1", :attr-name "visible", :attr-value true}
                                                   {:type "set-attribute", :target "letter2", :attr-name "visible", :attr-value true}
                                                   {:type "set-attribute", :target "letter3", :attr-name "visible", :attr-value true}]},
                        :mari-flies-to-hat {:type "transition", :to {:x 1075, :y 445, :loop false, :duration 1.5}, :transition-id "mari"},
                        :mari-init-wand    {:type "add-animation", :id "wand_idle", :target "mari", :track 2, :loop true},
                        :mari-says-correct-answer {
                                                   :type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :concept-var        "current-word",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             :correct-answer
                                                   :phrase-description "Correct answer"
                                                   },
                        :mari-word {
                                    :type               "sequence-data",
                                    :editor-type        "dialog",
                                    :concept-var        "current-word",
                                    :data               [{:type "sequence-data"
                                                          :data [{:type "empty" :duration 0}
                                                                 {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                    :phrase             :mari-word
                                    :phrase-description "Mari word about concept. Says wrong answer"
                                    },
                        :mari-sound {
                                     :type               "sequence-data",
                                     :editor-type        "dialog",
                                     :concept-var        "current-word",
                                     :data               [{:type "sequence-data"
                                                           :data [{:type "empty" :duration 0}
                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                     :phrase             :mari-sound
                                     :phrase-description "sound word about concept. Says wrong answer"
                                     },
                        :mari-says-wrong-answer
                                           {:type               "sequence-data",
                                            :data
                                                                [{:type        "copy-variable", :var-name "wrong-concept"
                                                                  :from-params [{:param-property "target", :action-property "from"}]}
                                                                 {:id "mari-word", :type "action"}
                                                                 {:type "empty", :duration 500}
                                                                 {:id "mari-voice-wrong-1", :type "action"}
                                                                 {:id "mari-word", :type "action"}
                                                                 {:type "empty", :duration 500}
                                                                 {:id "mari-word", :type "action"}
                                                                 {:id "mari-voice-wrong-2", :type "action"}
                                                                 {:id "mari-sound", :type "action"}
                                                                 {:type "empty", :duration 500}
                                                                 {:id "mari-voice-wrong-3", :type "action"}
                                                                 {:id "mari-sound", :type "action"}]},
                        :mari-tap-hat
                                           {:type "sequence-data",
                                            :data
                                                  [{:id "wand_hit", :type "animation", :track 2, :target "mari"}
                                                   {:id "wand_idle", :loop true, :type "add-animation", :track 2, :target "mari"}]},
                        :mari-voice-intro {:type               "sequence-data",
                                           :editor-type        "dialog",
                                           :concept-var        "current-word",
                                           :data               [{:type "sequence-data"
                                                                 :data [{:type "empty" :duration 0}
                                                                        {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                           :phrase                 "welcome",
                                           :phrase-description            "Welcome instructions. After start animation",
                                           },
                        :mari-voice-welcome {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :concept-var        "current-word",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase                 "Voice intro",
                                             :phrase-description            "Welcome instructions. Before animation start",
                                             },
                        :mari-voice-wrong-1 {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :concept-var        "current-word",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase                 "Wrong 1",
                                             :phrase-description     "Wrong 1",
                                             },
                        :mari-voice-wrong-2 {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :concept-var        "current-word",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase                 "Wrong 2",
                                             :phrase-description     "Wrong 2",
                                             },
                        :mari-voice-wrong-3 {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :concept-var        "current-word",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase                 "Wrong 3",
                                             :phrase-description     "Wrong 3",
                                             },
                        :mary-says-task
                                           {:type     "test-var-scalar",
                                            :var-name "current-level",
                                            :value    "easy",
                                            :success  "current-concept-chant",
                                            :fail     "current-concept-sound-x3"},
                        :pick-box
                                           {:type "sequence-data",
                                            :data
                                                  [{:id "set-concept-data", :type "action"}
                                                   {:fail        "pick-wrong",
                                                    :type        "test-var-scalar",
                                                    :success     "pick-correct",
                                                    :from-var    [{:var-name "current-concept", :action-property "value"}],
                                                    :from-params [{:param-property "target", :action-property "var-name"}]}]},
                        :pick-correct
                                           {:type "sequence-data",
                                            :data
                                                  [{:id "mari-says-correct-answer", :type "action"}
                                                   {:fail     {:type "empty", :duration 100},
                                                    :type     "test-var-scalar",
                                                    :value    false,
                                                    :success  "count-correct-answer",
                                                    :var-name "last-pick-wrong"}
                                                   {:id "box-jump-in", :type "action"}
                                                   {:id "reset-and-repeat", :type "action"}]},
                        :pick-wrong
                                           {:type "sequence-data",
                                            :data
                                                  [{:id "mari-says-wrong-answer", :type "action"}
                                                   {:id "count-wrong-answer", :type "action"}
                                                   {:id "set-boxes-skin", :type "action"}
                                                   {:type "set-variable", :var-name "last-pick-wrong", :var-value true}]},
                        :play
                                           {:type "sequence",
                                            :data ["renew-current-concept"
                                                   "check-level"
                                                   "mari-tap-hat"
                                                   "boxes-jump-out"
                                                   "set-concept-data"
                                                   "mary-says-task"]},
                        :renew-current-concept
                                           {:type "sequence-data",
                                            :data
                                                  [{:from        ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8" "item-9" "item-10"],
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
                                                    :variables ["box1" "box2" "box3"]}]},
                        :renew-words
                                           {:type "sequence-data",
                                            :data
                                                  [{:from        "concepts-single",
                                                    :type        "lesson-var-provider",
                                                    :limit       1,
                                                    :repeat      3,
                                                    :variables   ["item-1" "item-2" "item-3"],
                                                    :provider-id "words-set"}
                                                   {:from        "concepts-all",
                                                    :type        "lesson-var-provider",
                                                    :shuffled    true,
                                                    :variables   ["item-4" "item-5" "item-6" "item-7" "item-8" "item-9" "item-10"],
                                                    :provider-id "words-set"}
                                                   {:from      ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8" "item-9" "item-10"],
                                                    :type      "vars-var-provider",
                                                    :shuffled  true,
                                                    :variables ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8" "item-9" "item-10"]}]},
                        :reset-and-repeat
                                           {:type "sequence-data",
                                            :data
                                                  [{:type "set-variable", :var-name "last-pick-wrong", :var-value false}
                                                   {:id "letters-hide", :type "action"}
                                                   {:id "boxes-disappear", :type "action"}
                                                   {:id "play", :type "action"}
                                                   ]},
                        :set-boxes-skin
                                           {:type "parallel",
                                            :data
                                                  [{:type       "set-slot",
                                                    :target     "box1",
                                                    :from-var   [{:var-name "box1", :var-property "image-src", :action-property "image"}],
                                                    :slot-name  "box1",
                                                    :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                   {:type       "set-slot",
                                                    :target     "box2",
                                                    :from-var   [{:var-name "box2", :var-property "image-src", :action-property "image"}],
                                                    :slot-name  "box1",
                                                    :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                   {:type       "set-slot",
                                                    :target     "box3",
                                                    :from-var   [{:var-name "box3", :var-property "image-src", :action-property "image"}],
                                                    :slot-name  "box1",
                                                    :attachment {:x 40, :scale-x 4, :scale-y 4}}]},
                        :set-boxes-skin-copy-2
                                           {:type "parallel",
                                            :data
                                                  [{:type       "set-slot",
                                                    :target     "box1",
                                                    :from-var   [{:var-name "box1", :var-property "image-src", :action-property "image"}],
                                                    :slot-name  "box1",
                                                    :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                   {:type       "set-slot",
                                                    :target     "box2",
                                                    :from-var   [{:var-name "box2", :var-property "image-src", :action-property "image"}],
                                                    :slot-name  "box1",
                                                    :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                   {:type       "set-slot",
                                                    :target     "box3",
                                                    :from-var   [{:var-name "box3", :var-property "image-src", :action-property "image"}],
                                                    :slot-name  "box1",
                                                    :attachment {:x 40, :scale-x 4, :scale-y 4}}]},
                        :set-concept-data
                                           {:type "sequence-data",
                                            :data
                                                  [{:id "set-letters-text", :type "action"}
                                                   {:fail     "set-boxes-skin-copy-2",
                                                    :type     "test-var-scalar",
                                                    :value    "hard",
                                                    :success  {:type "empty", :duration 100},
                                                    :var-name "current-level"}]},
                        :set-letters-text
                                           {:type "sequence-data",
                                            :data
                                                  [{:type      "set-attribute",
                                                    :target    "letter1",
                                                    :from-var  [{:var-name "box1", :var-property "letter", :action-property "attr-value"}],
                                                    :attr-name "text"}
                                                   {:type      "set-attribute",
                                                    :target    "letter2",
                                                    :from-var  [{:var-name "box2", :var-property "letter", :action-property "attr-value"}],
                                                    :attr-name "text"}
                                                   {:type      "set-attribute",
                                                    :target    "letter3",
                                                    :from-var  [{:var-name "box3", :var-property "letter", :action-property "attr-value"}],
                                                    :attr-name "text"}
                                                   {:id "letters-show", :type "action"}]},
                        :start-scene       {:type "sequence", :data ["start-activity" "renew-words" "init-vars" "intro" "play"]},
                        :start-activity    {:type "start-activity", :id "magic-hat"},
                        :stop-activity     {:type "stop-activity", :id "magic-hat"}},
        :triggers      {:stop {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata      {:prev "library", :autostart true}})

(defn f
  [t args]
  t)

(core/register-template
  (:id m)
  m
  (partial f t))