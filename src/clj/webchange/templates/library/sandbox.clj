(ns webchange.templates.library.sandbox
    (:require
      [webchange.templates.core :as core]))

(def m {:id          7
        :name        "Sandbox"
        :description "Some description of Sandbox mechanics and covered skills"
        :lesson-sets ["concepts"]
        :fields      [
                      {:name "word-image-1",
                       :type "image"}
                      {:name "word-image-2",
                       :type "image"}
                      {:name "word-image-3",
                       :type "image"}
                      {:name "word-image-4",
                       :type "image"}
                      {:name "letter",
                       :type "string"}
                      ]})


(def t {:assets
                       [{:url "/raw/img/park/sandbox/background.jpg", :size 10, :type "image"}
                        {:url "/raw/img/casa/painting_canvas.png", :type "image"}
                        ],
        :objects
                       {:background {:type "background", :src "/raw/img/park/sandbox/background.jpg"},
                        :box1
                                    {:type        "animation",
                                     :x           304,
                                     :y           826,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.25, :y 0.25},
                                     :scene-name  "box1",
                                     :transition  "box1",
                                     :actions     {:click {:id "box-1-start", :on "click", :type "action", :unique-tag "box"}},
                                     :anim        "idle2",
                                     :anim-offset {:x 0, :y -300},
                                     :loop        true,
                                     :name        "boxes",
                                     :skin        "qwestion",
                                     :speed       0.3,
                                     :start       false},
                        :box2
                                    {:type        "animation",
                                     :x           401,
                                     :y           696,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.25, :y 0.25},
                                     :scene-name  "box2",
                                     :transition  "box2",
                                     :actions     {:click {:id "box-2-start", :on "click", :type "action", :unique-tag "box"}},
                                     :anim        "idle2",
                                     :anim-offset {:x 0, :y -300},
                                     :loop        true,
                                     :name        "boxes",
                                     :skin        "qwestion",
                                     :speed       0.3,
                                     :start       false},
                        :box3
                                    {:type        "animation",
                                     :x           600,
                                     :y           674,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.25, :y 0.25},
                                     :scene-name  "box3",
                                     :transition  "box3",
                                     :actions     {:click {:id "box-3-start", :on "click", :type "action", :unique-tag "box"}},
                                     :anim        "idle2",
                                     :anim-offset {:x 0, :y -300},
                                     :loop        true,
                                     :name        "boxes",
                                     :skin        "qwestion",
                                     :speed       0.3,
                                     :start       false},
                        :box4
                                    {:type        "animation",
                                     :x           919,
                                     :y           741,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.25, :y 0.25},
                                     :scene-name  "box4",
                                     :transition  "box4",
                                     :actions     {:click {:id "box-4-start", :on "click", :type "action", :unique-tag "box"}},
                                     :anim        "idle2",
                                     :anim-offset {:x 0, :y -300},
                                     :loop        true,
                                     :name        "boxes",
                                     :skin        "qwestion",
                                     :speed       0.3,
                                     :start       false},
                        :canvas
                                    {:type    "image",
                                     :x       1120,
                                     :y       500,
                                     :width   287,
                                     :height  430,
                                     :scale-x 1,
                                     :scale-y 1,
                                     :src     "/raw/img/casa/painting_canvas.png"},
                        :letter
                                    {:type           "text",
                                     :x              1270,
                                     :y              620,
                                     :align          "center",
                                     :fill           "#ff8c41",
                                     :font-family    "Lexend Deca",
                                     :font-size      200,
                                     :text           "",
                                     :vertical-align "middle"},
                        :mari
                                    {:type       "animation",
                                     :x          1535,
                                     :y          715,
                                     :width      473,
                                     :height     511,
                                     :scene-name "mari",
                                     :anim       "idle",
                                     :name       "mari",
                                     :scale-x    0.5,
                                     :scale-y    0.5,
                                     :start      true},
                        :word
                                    {:type   "transparent",
                                     :x      313,
                                     :y      91,
                                     :width  1200,
                                     :height 400,
                                     :states
                                             {:show
                                                       {:shadow-offset  {:x 5, :y 5},
                                                        :align          "center",
                                                        :shadow-color   "#1a1a1a",
                                                        :vertical-align "middle",
                                                        :shadow-blur    5,
                                                        :font-size      160,
                                                        :fill           "white",
                                                        :width          1200,
                                                        :type           "text",
                                                        :shadow-opacity 0.5,
                                                        :font-family    "Luckiest Guy",
                                                        :height         400},
                                              :default {:type "transparent"}}}},
        :scene-objects [["background"] ["canvas" "box3" "box4" "box2" "box1" "word" "letter" "mari"]],
        :actions
                       {:box-1-change-skin
                                                       {:type       "set-slot",
                                                        :target     "box1",
                                                        :from-var   [{:var-name "current-word", :var-property "word-image-1", :action-property "image"}],
                                                        :attachment {:x 40, :scale-x 4, :scale-y 4},
                                                        :slot-name  "box1"},
                        :box-1-jump-in
                                                       {:type "sequence",
                                                        :data
                                                              ["box-1-jump-in-animation-jump" "box-1-jump-in-empty" "box-1-jump-in-transition" "box-1-jump-in-animation-idle"]},
                        :box-1-jump-in-animation-idle  {:type "add-animation", :id "idle2", :target "box1", :loop true},
                        :box-1-jump-in-animation-jump  {:type "animation", :id "jump2", :target "box1"},
                        :box-1-jump-in-empty           {:type "empty", :duration 500},
                        :box-1-jump-in-transition
                                                       {:type "transition", :to {:x 668, :y 798, :loop false, :duration 0.7}, :transition-id "box1"},
                        :box-1-jump-out
                                                       {:type "sequence",
                                                        :data
                                                              ["box-1-jump-out-animation-jump"
                                                               "box-1-jump-out-empty"
                                                               "box-1-jump-out-transition"
                                                               "box-1-jump-out-animation-idle"]},
                        :box-1-jump-out-animation-idle {:type "add-animation", :id "idle2", :target "box1", :loop true},
                        :box-1-jump-out-animation-jump {:type "animation", :id "jump2", :target "box1"},
                        :box-1-jump-out-empty          {:type "empty", :duration 500},
                        :box-1-jump-out-transition
                                                       {:type "transition", :to {:x 304, :y 826, :loop false, :duration 0.7}, :transition-id "box1"},
                        :box-1-start
                                                       {:type "sequence",
                                                        :data
                                                              ["complete-word-1"
                                                               "word-1-state-var"
                                                               "box-1-start-animation"
                                                               "box-1-jump-in"
                                                               "word-1-state-var"
                                                               "box-1-jump-out"
                                                               "word-1-state-var"
                                                               "test-concept-complete"]},
                        :box-1-start-animation         {:type "start-animation", :target "box1"},
                        :box-2-change-skin
                                                       {:type       "set-slot",
                                                        :target     "box2",
                                                        :from-var   [{:var-name "current-word", :var-property "word-image-2", :action-property "image"}],
                                                        :attachment {:x 40, :scale-x 4, :scale-y 4},
                                                        :slot-name  "box1"},
                        :box-2-jump-in
                                                       {:type "sequence",
                                                        :data
                                                              ["box-2-jump-in-animation-jump" "box-2-jump-in-empty" "box-2-jump-in-transition" "box-2-jump-in-animation-idle"]},
                        :box-2-jump-in-animation-idle  {:type "add-animation", :id "idle2", :target "box2", :loop true},
                        :box-2-jump-in-animation-jump  {:type "animation", :id "jump2", :target "box2"},
                        :box-2-jump-in-empty           {:type "empty", :duration 500},
                        :box-2-jump-in-transition
                                                       {:type "transition", :to {:x 668, :y 798, :loop false, :duration 0.7}, :transition-id "box2"},
                        :box-2-jump-out
                                                       {:type "sequence",
                                                        :data
                                                              ["box-2-jump-out-animation-jump"
                                                               "box-2-jump-out-empty"
                                                               "box-2-jump-out-transition"
                                                               "box-2-jump-out-animation-idle"]},
                        :box-2-jump-out-animation-idle {:type "add-animation", :id "idle2", :target "box2", :loop true},
                        :box-2-jump-out-animation-jump {:type "animation", :id "jump2", :target "box2"},
                        :box-2-jump-out-empty          {:type "empty", :duration 500},
                        :box-2-jump-out-transition
                                                       {:type "transition", :to {:x 401, :y 696, :loop false, :duration 0.7}, :transition-id "box2"},
                        :box-2-start
                                                       {:type "sequence",
                                                        :data
                                                              ["complete-word-2"
                                                               "word-2-state-var"
                                                               "box-2-start-animation"
                                                               "box-2-jump-in"
                                                               "word-2-state-var"
                                                               "box-2-jump-out"
                                                               "word-2-state-var"
                                                               "test-concept-complete"]},
                        :box-2-start-animation         {:type "start-animation", :target "box2"},
                        :box-3-change-skin
                                                       {:type       "set-slot",
                                                        :target     "box3",
                                                        :from-var   [{:var-name "current-word", :var-property "word-image-3", :action-property "image"}],
                                                        :attachment {:x 40, :scale-x 4, :scale-y 4},
                                                        :slot-name  "box1"},
                        :box-3-jump-in
                                                       {:type "sequence",
                                                        :data
                                                              ["box-3-jump-in-animation-jump" "box-3-jump-in-empty" "box-3-jump-in-transition" "box-3-jump-in-animation-idle"]},
                        :box-3-jump-in-animation-idle  {:type "add-animation", :id "idle2", :target "box3", :loop true},
                        :box-3-jump-in-animation-jump  {:type "animation", :id "jump2", :target "box3"},
                        :box-3-jump-in-empty           {:type "empty", :duration 500},
                        :box-3-jump-in-transition
                                                       {:type "transition", :to {:x 668, :y 798, :loop false, :duration 0.7}, :transition-id "box3"},
                        :box-3-jump-out
                                                       {:type "sequence",
                                                        :data
                                                              ["box-3-jump-out-animation-jump"
                                                               "box-3-jump-out-empty"
                                                               "box-3-jump-out-transition"
                                                               "box-3-jump-out-animation-idle"]},
                        :box-3-jump-out-animation-idle {:type "add-animation", :id "idle2", :target "box3", :loop true},
                        :box-3-jump-out-animation-jump {:type "animation", :id "jump2", :target "box3"},
                        :box-3-jump-out-empty          {:type "empty", :duration 500},
                        :box-3-jump-out-transition
                                                       {:type "transition", :to {:x 600, :y 674, :loop false, :duration 0.7}, :transition-id "box3"},
                        :box-3-start
                                                       {:type "sequence",
                                                        :data
                                                              ["complete-word-3"
                                                               "word-3-state-var"
                                                               "box-3-start-animation"
                                                               "box-3-jump-in"
                                                               "word-3-state-var"
                                                               "box-3-jump-out"
                                                               "word-3-state-var"
                                                               "test-concept-complete"]},
                        :box-3-start-animation         {:type "start-animation", :target "box3"},
                        :box-4-change-skin
                                                       {:type       "set-slot",
                                                        :target     "box4",
                                                        :from-var   [{:var-name "current-word", :var-property "word-image-4", :action-property "image"}],
                                                        :attachment {:x 40, :scale-x 4, :scale-y 4},
                                                        :slot-name  "box1"},
                        :box-4-jump-in
                                                       {:type "sequence",
                                                        :data
                                                              ["box-4-jump-in-animation-jump" "box-4-jump-in-empty" "box-4-jump-in-transition" "box-4-jump-in-animation-idle"]},
                        :box-4-jump-in-animation-idle  {:type "add-animation", :id "idle2", :target "box4", :loop true},
                        :box-4-jump-in-animation-jump  {:type "animation", :id "jump2", :target "box4"},
                        :box-4-jump-in-empty           {:type "empty", :duration 500},
                        :box-4-jump-in-transition
                                                       {:type "transition", :to {:x 668, :y 798, :loop false, :duration 0.7}, :transition-id "box4"},
                        :box-4-jump-out
                                                       {:type "sequence",
                                                        :data
                                                              ["box-4-jump-out-animation-jump"
                                                               "box-4-jump-out-empty"
                                                               "box-4-jump-out-transition"
                                                               "box-4-jump-out-animation-idle"]},
                        :box-4-jump-out-animation-idle {:type "add-animation", :id "idle2", :target "box4", :loop true},
                        :box-4-jump-out-animation-jump {:type "animation", :id "jump2", :target "box4"},
                        :box-4-jump-out-empty          {:type "empty", :duration 500},
                        :box-4-jump-out-transition
                                                       {:type "transition", :to {:x 919, :y 741, :loop false, :duration 0.7}, :transition-id "box4"},
                        :box-4-start
                                                       {:type "sequence",
                                                        :data
                                                              ["complete-word-4"
                                                               "word-4-state-var"
                                                               "box-4-start-animation"
                                                               "box-4-jump-in"
                                                               "word-4-state-var"
                                                               "box-4-jump-out"
                                                               "word-4-state-var"
                                                               "test-concept-complete"]},
                        :box-4-start-animation         {:type "start-animation", :target "box4"},
                        :clear-instruction             {:type "remove-flows", :flow-tag "instruction"},
                        :complete-word-1               {:type "set-variable", :var-name "word-1", :var-value true},
                        :complete-word-2               {:type "set-variable", :var-name "word-2", :var-value true},
                        :complete-word-3               {:type "set-variable", :var-name "word-3", :var-value true},
                        :complete-word-4               {:type "set-variable", :var-name "word-4", :var-value true},
                        :finish-activity               {:type "finish-activity", :id "sandbox"},
                        :hide-word                     {:type "state", :id "default", :target "word"},
                        :letter-intro   {:type               "sequence-data",
                                         :editor-type        "dialog",
                                         :concept-var        "current-word",
                                         :data               [{:type "sequence-data"
                                                               :data [{:type "empty" :duration 0}
                                                                      {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                         :phrase                 "letter-intro",
                                         :phrase-description     "Learn concept sound",
                                         },
                        :mari-more-audio  {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :concept-var        "current-word",
                                             :data               [{:type "sequence-data"
                                                                   :data [{:type "empty" :duration 0}
                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                             :phrase                 "more-words",
                                             :phrase-description     "More words. Do you want to hear more words?",
                                             },
                       :mari-welcome-audio {:type               "sequence-data",
                                            :editor-type        "dialog",
                                            :concept-var        "current-word",
                                            :data               [{:type "sequence-data"
                                                                  :data [{:type "empty" :duration 0}
                                                                         {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                            :phrase                        "welcome",
                                            :phrase-description            "Welcome to the sandbox!",
                                            },
                        :next-concept-workflow         {:type "sequence", :data ["mari-more-audio" "renew-current-concept-workflow"]},
                        :nil                           {:data [{:end 4.23, :anim "talk", :start 3}]},
                        :renew-current-concept
                                                       {:type "parallel",
                                                        :data
                                                              [{:type "set-variable", :var-name "word-1", :var-value false}
                                                               {:type "set-variable", :var-name "word-2", :var-value false}
                                                               {:type "set-variable", :var-name "word-3", :var-value false}
                                                               {:type "set-variable", :var-name "word-4", :var-value false}
                                                               {:from        ["item-1" "item-2" "item-3"],
                                                                :type        "vars-var-provider",
                                                                :on-end      "finish-activity",
                                                                :shuffled    false,
                                                                :variables   ["current-word"],
                                                                :provider-id "current-word"}]},
                        :renew-current-concept-workflow
                                                       {:type "sequence",
                                                        :data
                                                              ["renew-current-concept"
                                                               "box-1-change-skin"
                                                               "box-2-change-skin"
                                                               "box-3-change-skin"
                                                               "box-4-change-skin"
                                                               "show-letter"
                                                               "letter-intro"]},
                        :renew-words
                                                       {:type        "lesson-var-provider",
                                                        :from        "concepts",
                                                        :provider-id "words-set",
                                                        :shuffled    false,
                                                        :variables   ["item-1" "item-2" "item-3"]},
                        :show-letter
                                                       {:type      "set-attribute",
                                                        :target    "letter",
                                                        :from-var  [{:var-name "current-word", :var-property "letter", :action-property "attr-value"}],
                                                        :attr-name "text"},
                        :start-activity                {:type "start-activity", :id "sandbox"},
                        :start-scene
                                                       {:type "sequence",
                                                        :data ["start-activity" "clear-instruction" "renew-words" "mari-welcome-audio" "renew-current-concept-workflow"]},
                        :stop-activity                 {:type "stop-activity", :id "sandbox"},
                        :test-concept-complete
                                                       {:type      "test-var-list",
                                                        :success   "next-concept-workflow",
                                                        :values    [true true true true],
                                                        :var-names ["word-1" "word-2" "word-3" "word-4"]},
                        :word-1-state-var {:type               "sequence-data",
                                           :editor-type        "dialog",
                                           :concept-var        "current-word",
                                           :data               [{:type "sequence-data"
                                                                 :data [{:type "empty" :duration 0}
                                                                        {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                           :phrase             "word-1",
                                           :phrase-description "Pair word 1",
                                           },
                        :word-2-state-var {:type               "sequence-data",
                                           :editor-type        "dialog",
                                           :concept-var        "current-word",
                                           :data               [{:type "sequence-data"
                                                                 :data [{:type "empty" :duration 0}
                                                                        {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                           :phrase             "word-2",
                                           :phrase-description "Pair word 2",
                                           },
                        :word-3-state-var {:type               "sequence-data",
                                           :editor-type        "dialog",
                                           :concept-var        "current-word",
                                           :data               [{:type "sequence-data"
                                                                 :data [{:type "empty" :duration 0}
                                                                        {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                           :phrase             "word-3",
                                           :phrase-description "Pair word 3",
                                           },
                        :word-4-state-var {:type               "sequence-data",
                                           :editor-type        "dialog",
                                           :concept-var        "current-word",
                                           :data               [{:type "sequence-data"
                                                                 :data [{:type "empty" :duration 0}
                                                                        {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                           :phrase             "word-4",
                                           :phrase-description "Pair word 4",
                                           },
                        }
        :triggers      {:back {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata      {:prev "park", :autostart true},
        :variables     {:status nil}}
  )

(defn f
      [t args]
      t)

(core/register-template
  (:id m)
  m
  (partial f t))