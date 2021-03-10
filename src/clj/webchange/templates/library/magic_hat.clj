(ns webchange.templates.library.magic_hat
  (:require
    [webchange.templates.core :as core]))

(def m {:id          10
        :name        "Magic hat"
        :tags        ["Direct Instruction - Animated Instructor"]
        :description "Some description of magic hat mechanics and covered skills"
        :lesson-sets ["concepts-magic-hat-rounds", "concepts-all"]
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
                        :hat
                                            {:type        "animation",
                                             :x           625,
                                             :y           974,
                                             :width       710,
                                             :height      1231,
                                             :anim-offset {:x 300, :y -419.73},
                                             :scene-name  "magic-hat",
                                             :transition  "hat",
                                             :anim        "idle",
                                             :loop        true,
                                             :name        "hat",
                                             :scale-x     1,
                                             :scale-y     1,
                                             :speed       1,
                                             :start       true
                                             :actions     {:click {:id         "letter-sound",
                                                                   :on         "click",
                                                                   :type       "action",
                                                                   :unique-tag "click"}},

                                             },
                        :letter1
                                            {:type           "text",
                                             :x              915,
                                             :y              770,
                                             :width          200,
                                             :height         130,
                                             :transition     "letter1",
                                             :align          "center",
                                             :fill           "white",
                                             :font-family    "Lexend Deca",
                                             :font-size      160,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "",
                                             :vertical-align "middle",
                                             :actions        {:click {:id         "pick-box",
                                                                      :on         "click",
                                                                      :type       "action",
                                                                      :params     {:target "box1"
                                                                                   :image  "image1"
                                                                                   :letter "letter1"},
                                                                      :unique-tag "click"}},
                                             :visible        false},
                        :image1             {:type       "image",
                                             :x          965,
                                             :y          800,
                                             :scene-name "image1"
                                             :width      100,
                                             :height     100,
                                             :src        ""
                                             :visible    false},
                        :letter2
                                            {:type           "text",
                                             :x              1065,
                                             :y              740,
                                             :width          200,
                                             :height         130,
                                             :transition     "letter2",
                                             :align          "center",
                                             :fill           "white",
                                             :font-family    "Lexend Deca",
                                             :font-size      160,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "",
                                             :vertical-align "middle",
                                             :actions        {:click {:id         "pick-box",
                                                                      :on         "click",
                                                                      :type       "action",
                                                                      :params     {:target "box2"
                                                                                   :image  "image2"
                                                                                   :letter "letter2"},
                                                                      :unique-tag "click"}},
                                             :visible        false},
                        :image2             {:type       "image",
                                             :x          1115,
                                             :y          770,
                                             :scene-name "image2"
                                             :width      100,
                                             :height     100,
                                             :src        ""
                                             :visible    false},
                        :letter3
                                            {:type           "text",
                                             :x              1215,
                                             :y              760,
                                             :width          200,
                                             :height         130,
                                             :transition     "letter3",
                                             :align          "center",
                                             :fill           "white",
                                             :font-family    "Lexend Deca",
                                             :font-size      160,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "",
                                             :vertical-align "middle",
                                             :actions        {:click {:id         "pick-box",
                                                                      :on         "click",
                                                                      :type       "action",
                                                                      :params     {:target "box3"
                                                                                   :image  "image3"
                                                                                   :letter "letter3"},
                                                                      :unique-tag "click"}},
                                             :visible        false},
                        :image3             {:type       "image",
                                             :x          1265,
                                             :y          790,
                                             :scene-name "image3"
                                             :width      100,
                                             :height     100,
                                             :src        ""
                                             :visible    false},
                        :letter4
                                            {:type           "text",
                                             :x              1365,
                                             :y              730,
                                             :width          200,
                                             :height         130,
                                             :transition     "letter4",
                                             :align          "center",
                                             :fill           "white",
                                             :font-family    "Lexend Deca",
                                             :font-size      160,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "",
                                             :vertical-align "middle",
                                             :actions        {:click {:id         "pick-box",
                                                                      :on         "click",
                                                                      :type       "action",
                                                                      :params     {:target "box4"
                                                                                   :image  "image4"
                                                                                   :letter "letter4"},
                                                                      :unique-tag "click"}},
                                             :visible        false},
                        :image4             {:type       "image",
                                             :x          1415,
                                             :y          760,
                                             :scene-name "image4"
                                             :width      100,
                                             :height     100,
                                             :src        ""
                                             :visible    false},
                        :letter5
                                            {:type           "text",
                                             :x              1515,
                                             :y              720,
                                             :width          200,
                                             :height         130,
                                             :transition     "letter5",
                                             :align          "center",
                                             :fill           "white",
                                             :font-family    "Lexend Deca",
                                             :font-size      160,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "",
                                             :vertical-align "middle",
                                             :actions        {:click {:id         "pick-box",
                                                                      :on         "click",
                                                                      :type       "action",
                                                                      :params     {:target "box5"
                                                                                   :image  "image5"
                                                                                   :letter "letter5"},
                                                                      :unique-tag "click"}},
                                             :visible        false},
                        :image5             {:type       "image",
                                             :x          1565,
                                             :y          750,
                                             :scene-name "image5"
                                             :width      100,
                                             :height     100,
                                             :src        ""
                                             :visible    false},
                        :letter6
                                            {:type           "text",
                                             :x              1665,
                                             :y              780,
                                             :width          200,
                                             :height         130,
                                             :transition     "letter6",
                                             :align          "center",
                                             :fill           "white",
                                             :font-family    "Lexend Deca",
                                             :font-size      160,
                                             :scale-x        1,
                                             :scale-y        1,
                                             :text           "",
                                             :vertical-align "middle",
                                             :actions        {:click {:id         "pick-box",
                                                                      :on         "click",
                                                                      :type       "action",
                                                                      :params     {:target "box6"
                                                                                   :image  "image6"
                                                                                   :letter "letter6"
                                                                                   },
                                                                      :unique-tag "click"}},
                                             :visible        false},
                        :image6             {:type       "image",
                                             :x          1715,
                                             :y          810,
                                             :scene-name "image6"
                                             :width      100,
                                             :height     100,
                                             :src        ""
                                             :visible    false},
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
        :scene-objects [["layered-background"] ["hat"
                                                "letter1" "letter2" "letter3"
                                                "letter4" "letter5" "letter6"
                                                "image1" "image2" "image3"
                                                "image4" "image5" "image6"
                                                ] ["mari"]],
        :actions
                       {

                        :init-vars
                                                  {:type "parallel",
                                                   :data
                                                         [
                                                          {:type "set-random", :var-name "correct-letter-number", :start 2, :end 3}
                                                          {:type           "counter"
                                                           :counter-action "reset"
                                                           :counter-value  0
                                                           :counter-id     "correct-answer-counter"
                                                           }
                                                          ]},
                        :intro                    {:type "sequence",
                                                   :data ["mari-voice-welcome"
                                                          "mari-flies-to-hat"
                                                          "mari-init-wand"]},
                        :letters-hide
                                                  {:type "parallel",
                                                   :data
                                                         [{:type "set-attribute", :target "letter1", :attr-name "visible", :attr-value false}
                                                          {:type "set-attribute", :target "letter2", :attr-name "visible", :attr-value false}
                                                          {:type "set-attribute", :target "letter3", :attr-name "visible", :attr-value false}
                                                          {:type "set-attribute", :target "letter4", :attr-name "visible", :attr-value false}
                                                          {:type "set-attribute", :target "letter5", :attr-name "visible", :attr-value false}
                                                          {:type "set-attribute", :target "letter6", :attr-name "visible", :attr-value false}]},
                        :letters-show
                                                  {:type "parallel",
                                                   :data
                                                         [{:type "set-attribute", :target "letter1", :attr-name "visible", :attr-value true}
                                                          {:type "set-attribute", :target "letter2", :attr-name "visible", :attr-value true}
                                                          {:type "set-attribute", :target "letter3", :attr-name "visible", :attr-value true}
                                                          {:type "set-attribute", :target "letter4", :attr-name "visible", :attr-value true}
                                                          {:type "set-attribute", :target "letter5", :attr-name "visible", :attr-value true}
                                                          {:type       "test-var-inequality"
                                                           :var-name   "correct-letter-number",
                                                           :value      3,
                                                           :inequality ">=",
                                                           :success    "show-letter-6",
                                                           }
                                                          ]},
                        :show-letter-6            {:type "set-attribute", :target "letter6", :attr-name "visible", :attr-value true}
                        :mari-flies-to-hat        {:type          "transition",
                                                   :to            {:x 1075, :y 445, :loop false, :duration 1.5},
                                                   :transition-id "mari"},
                        :mari-init-wand           {:type   "add-animation",
                                                   :id     "wand_idle",
                                                   :target "mari",
                                                   :track  2,
                                                   :loop   true},

                        :mari-tap-hat             {:type "sequence-data",
                                                   :data
                                                         [{:id "wand_hit", :type "animation", :track 2, :target "mari"}
                                                          {:id "wand_idle", :loop true, :type "add-animation", :track 2, :target "mari"}]},
                        :pick-box
                                                  {:type "sequence-data",
                                                   :data
                                                         [{:fail        "pick-wrong",
                                                           :type        "test-var-scalar",
                                                           :success     "pick-correct",
                                                           :from-var    [{:var-name "current-concept", :action-property "value"}],
                                                           :from-params [{:param-property "target", :action-property "var-name"}]}]},
                        :pick-correct
                                                  {:type "sequence-data",
                                                   :data
                                                         [
                                                          {:id "letter-sound", :type "action"}
                                                          {:type        "set-attribute",
                                                           :attr-name   "visible",
                                                           :attr-value  false
                                                           :from-params [{:param-property "letter", :action-property "target"}]
                                                           }
                                                          {:type        "set-attribute",
                                                           :attr-name   "visible",
                                                           :attr-value  true
                                                           :from-params [{:param-property "image", :action-property "target"}]
                                                           }
                                                          {:type "empty" :duration 2000}
                                                          {:type        "set-attribute",
                                                           :attr-name   "visible",
                                                           :attr-value  false
                                                           :from-params [{:param-property "image", :action-property "target"}]
                                                           }
                                                          {:id "mari-says-correct-answer", :type "action"}
                                                          {:type "counter" :counter-action "increase" :counter-id "correct-answer-counter"}
                                                          {:type       "test-var-inequality"
                                                           :var-name   "correct-answer-counter",
                                                           :inequality ">=",
                                                           :from-var   [{:var-name "correct-letter-number", :action-property "value"}],
                                                           :success    "next-round",
                                                           }
                                                          ]},
                        :shake                    {:type "sequence-data",
                                                   :data [
                                                          {:to          {:offset-x 10, :duration 0.1}, :type "transition",
                                                           :from-params [{:param-property "letter", :action-property "transition-id"}]
                                                           }
                                                          {:to          {:offset-x -20, :duration 0.1}, :type "transition",
                                                           :from-params [{:param-property "letter", :action-property "transition-id"}]
                                                           }
                                                          {:to          {:offset-x 10, :duration 0.1}, :type "transition",
                                                           :from-params [{:param-property "letter", :action-property "transition-id"}]
                                                           }
                                                          ]},

                        :pick-wrong               {:type "sequence-data",
                                                   :data [
                                                          {:id "shake", :type "action"}
                                                          {:id "mari-says-wrong-answer", :type "action"}]},

                        :set-concept-data
                                                  {:type "sequence-data",
                                                   :data
                                                         [{:id "set-letters-text", :type "action"}
                                                          ]},
                        :set-letters-text         {:type "sequence-data",
                                                   :data
                                                         [{:type      "set-attribute",
                                                           :target    "letter1",
                                                           :from-var  [{:var-name "box1", :var-property "letter", :action-property "attr-value"}],
                                                           :attr-name "text"}
                                                          {:type      "set-attribute",
                                                           :target    "image1",
                                                           :from-var  [{:var-name "box1", :var-property "image-src", :action-property "attr-value"}],
                                                           :attr-name "src"}
                                                          {:type      "set-attribute",
                                                           :target    "letter2",
                                                           :from-var  [{:var-name "box2", :var-property "letter", :action-property "attr-value"}],
                                                           :attr-name "text"}
                                                          {:type      "set-attribute",
                                                           :target    "image2",
                                                           :from-var  [{:var-name "box2", :var-property "image-src", :action-property "attr-value"}],
                                                           :attr-name "src"}
                                                          {:type      "set-attribute",
                                                           :target    "letter3",
                                                           :from-var  [{:var-name "box3", :var-property "letter", :action-property "attr-value"}],
                                                           :attr-name "text"}
                                                          {:type      "set-attribute",
                                                           :target    "image3",
                                                           :from-var  [{:var-name "box3", :var-property "image-src", :action-property "attr-value"}],
                                                           :attr-name "src"}
                                                          {:type      "set-attribute",
                                                           :target    "letter4",
                                                           :from-var  [{:var-name "box4", :var-property "letter", :action-property "attr-value"}],
                                                           :attr-name "text"}
                                                          {:type      "set-attribute",
                                                           :target    "image4",
                                                           :from-var  [{:var-name "box4", :var-property "image-src", :action-property "attr-value"}],
                                                           :attr-name "src"}
                                                          {:type      "set-attribute",
                                                           :target    "letter5",
                                                           :from-var  [{:var-name "box5", :var-property "letter", :action-property "attr-value"}],
                                                           :attr-name "text"}
                                                          {:type      "set-attribute",
                                                           :target    "image5",
                                                           :from-var  [{:var-name "box5", :var-property "image-src", :action-property "attr-value"}],
                                                           :attr-name "src"}
                                                          {:type      "set-attribute",
                                                           :target    "letter6",
                                                           :from-var  [{:var-name "box6", :var-property "letter", :action-property "attr-value"}],
                                                           :attr-name "text"}
                                                          {:type      "set-attribute",
                                                           :target    "image6",
                                                           :from-var  [{:var-name "box6", :var-property "image-src", :action-property "attr-value"}],
                                                           :attr-name "src"}
                                                          {:id "letters-show", :type "action"}]},
                        :next-round               {:type "sequence",
                                                   :data [
                                                          "init-vars"
                                                          "renew-words"
                                                          "play"]},
                        :start-scene              {:type "sequence",
                                                   :data [
                                                          "start-activity"
                                                          "init-vars"
                                                          "renew-words"
                                                          "intro"
                                                          "introduce-concept"
                                                          "play"]},
                        :renew-words              {:type "sequence-data",
                                                   :data
                                                         [
                                                          {:from        "concepts-all",
                                                           :type        "lesson-var-provider",
                                                           :shuffled    true,
                                                           :from-var
                                                                        [{:var-key         "concept-name",
                                                                          :var-name        "item-1",
                                                                          :var-property    "concept-name",
                                                                          :action-property "exclude-property-values"}],
                                                           :variables   ["item-3" "item-4" "item-5"],
                                                           :provider-id "words-set"}
                                                          {:type       "test-var-inequality"
                                                           :var-name   "correct-letter-number",
                                                           :value      3,
                                                           :inequality ">=",
                                                           :success    "renew-words-6",
                                                           :fail       "renew-words-5"
                                                           }
                                                          ]},
                        :renew-words-6            {:type "sequence-data",
                                                   :data
                                                         [{:from        "concepts-magic-hat-rounds",
                                                           :type        "lesson-var-provider",
                                                           :limit       1,
                                                           :repeat      3,
                                                           :on-end      "finish-activity",
                                                           :variables   ["item-1" "item-2" "item-6"],
                                                           :provider-id "words-set"}

                                                          {:from      ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6"],
                                                           :type      "vars-var-provider",
                                                           :shuffled  true,
                                                           :variables ["box1" "box2" "box3" "box4" "box5" "box6"]}
                                                          {:from "item-1", :type "copy-variable", :var-name "current-concept"}
                                                          ]},
                        :renew-words-5            {:type "sequence-data",
                                                   :data
                                                         [{:from        "concepts-magic-hat-rounds",
                                                           :type        "lesson-var-provider",
                                                           :limit       1,
                                                           :repeat      2,
                                                           :on-end      "finish-activity",
                                                           :variables   ["item-1" "item-2"],
                                                           :provider-id "words-set"}
                                                          {:from      ["item-1" "item-2" "item-3" "item-4" "item-5"],
                                                           :type      "vars-var-provider",
                                                           :shuffled  true,
                                                           :variables ["box1" "box2" "box3" "box4" "box5"]}
                                                          {:from      ["item-5"],
                                                           :type      "vars-var-provider",
                                                           :variables ["box6"]}
                                                          {:from "item-1", :type "copy-variable", :var-name "current-concept"}
                                                          ]},
                        :play                     {:type "sequence",
                                                   :data [
                                                          "mari-tap-hat"
                                                          "set-concept-data"
                                                          ]},

                        :finish-activity          {:type "sequence-data",
                                                   :data [{:id "finish-activity-dialog", :type "action"}
                                                          {:id "magic-hat", :type "finish-activity"}
                                                          ]}
                        :start-activity           {:type "start-activity", :id "magic-hat"},
                        :stop-activity            {:type "stop-activity", :id "magic-hat"}

                        :mari-voice-welcome       {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :concept-var        "current-word",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type        "animation-sequence",
                                                                                 :phrase-text "Hello my friend! Did you see the wizard’s hat?",
                                                                                 :audio       nil}]}],
                                                   :phrase             "Voice intro",
                                                   :phrase-description "Welcome instructions."},
                        :introduce-concept        {:type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :concept-var        "current-concept",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type        "animation-sequence",
                                                                                 :phrase-text "Introduce concept",
                                                                                 :audio       nil}]}],
                                                   :phrase             "introduce-concept",
                                                   :phrase-description "Introduce concept."},
                        :mari-says-correct-answer {
                                                   :type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :concept-var        "current-concept",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             :correct-answer
                                                   :phrase-description "Correct answer"
                                                   },
                        :mari-says-wrong-answer   {
                                                   :type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :concept-var        "current-concept",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             :wrong-answer
                                                   :phrase-description "Wrong answer"
                                                   },
                        :letter-sound             {
                                                   :type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :concept-var        "current-concept",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                   :phrase             :word-sound
                                                   :phrase-description "word-sound"
                                                   },
                        :finish-activity-dialog   {
                                                   :type               "sequence-data",
                                                   :editor-type        "dialog",
                                                   :concept-var        "current-concept",
                                                   :data               [{:type "sequence-data"
                                                                         :data [{:type "empty" :duration 0}
                                                                                {:type "animation-sequence", :phrase-text "Your hard work is even better than magic!", :audio nil}]}],
                                                   :phrase             "finish-activity-dialog"
                                                   :phrase-description "finish activity dialog"
                                                   },
                        },
        :triggers      {:stop  {:on "back", :action "stop-activity"},
                        :start {:on "start", :action "start-scene"}},
        :metadata      {:prev "library", :autostart true}})

(defn f
  [t args]
  t)

(core/register-template
  m
  (partial f t))
