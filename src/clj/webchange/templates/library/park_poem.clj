(ns webchange.templates.library.park-poem
  (:require
    [webchange.templates.core :as core]))

(def m {:id          13
        :name        "Park poem"
        :tags        ["Direct Instruction - Animated Instructor"]
        :description "Park poem"
        :lesson-sets ["concepts-group"]
        :fields      [{:name "poem-image-1",
                       :type "image"}
                      {:name "poem-image-2",
                       :type "image"}
                      {:name "poem-image-3",
                       :type "image"}]})

(def t {:assets   [{:url "/raw/img/park/sandbox/background.jpg", :size 10, :type "image"}],
        :objects
                  {:background {:type "background", :src "/raw/img/park/sandbox/background.jpg"},
                   :bubble-1
                               {:type       "image",
                                :x          470,
                                :y          670,
                                :width      450,
                                :height     450,
                                :transition "bubble-1",
                                :scale-x    1,
                                :scale-y    1,
                                :src        "",
                                :visible    false,
                                :states     {:hidden {:visible false}, :visible {:visible true}}},
                   :bubble-2
                               {:type       "image",
                                :x          470,
                                :y          670,
                                :width      450,
                                :height     450,
                                :transition "bubble-2",
                                :scale-x    1,
                                :scale-y    1,
                                :src        "",
                                :visible    false,
                                :states     {:hidden {:visible false}, :visible {:visible true}}},
                   :bubble-3
                               {:type       "image",
                                :x          470,
                                :y          670,
                                :width      450,
                                :height     450,
                                :transition "bubble-3",
                                :scale-x    1,
                                :scale-y    1,
                                :src        "",
                                :visible    false,
                                :states     {:hidden {:visible false}, :visible {:visible true}}},
                   :image-story-1
                               {:type    "image",
                                :x       426,
                                :y       551,
                                :width   450,
                                :height  450,
                                :actions {:click {:id "image-story-1-clicked", :on "click", :type "action" :unique-tag "story"}},
                                :scale-x 0.25,
                                :scale-y 0.25,
                                :src     "",
                                :visible false,
                                :states  {:visible {:visible true}, :clickable {:x 415, :y 540, :scale-x 1.2, :scale-y 1.2}}},
                   :image-story-2
                               {:type    "image",
                                :x       621,
                                :y       546,
                                :width   450,
                                :height  450,
                                :actions {:click {:id "image-story-2-clicked", :on "click", :type "action" :unique-tag "story"}},
                                :scale-x 0.25,
                                :scale-y 0.25,
                                :src     "",
                                :visible false,
                                :states  {:visible {:visible true}, :clickable {:x 610, :y 535, :scale-x 1.2, :scale-y 1.2}}},
                   :image-story-3
                               {:type    "image",
                                :x       791,
                                :y       581,
                                :width   450,
                                :height  450,
                                :actions {:click {:id "image-story-3-clicked", :on "click", :type "action" :unique-tag "story"}},
                                :scale-x 0.25,
                                :scale-y 0.25,
                                :src     "",
                                :visible false,
                                :states  {:visible {:visible true}, :clickable {:x 780, :y 570, :scale-x 1.2, :scale-y 1.2}}},

                   :mari
                               {:type       "animation",
                                :x          230,
                                :y          825,
                                :width      473,
                                :height     511,
                                :transition "mari",
                                :anim       "idle",
                                :name       "mari",
                                :scale-x    -0.5,
                                :scale-y    0.5,
                                :speed      0.35,
                                :start      true,
                                :states
                                            {:listening-eva  {:speed 0.35, :scale-x -0.5, :scale-y 0.5},
                                             :listening-user {:speed 0.1, :scale-x -0.7, :scale-y 0.7}}},
                   :rock
                               {:type       "animation",
                                :x          1570,
                                :y          800,
                                :width      591,
                                :height     681,
                                :scene-name "rock",
                                :anim       "idle",
                                :name       "rock",
                                :scale-x    0.6,
                                :scale-y    0.6,
                                :skin       "female",
                                :speed      0.35,
                                :start      true}},
        :scene-objects
                  [["background"]
                   ["image-story-1" "image-story-2" "image-story-3" "mari" "rock" "bubble-1" "bubble-2" "bubble-3"]],
        :actions
                  {:dialog-1-welcome
                                    {:type               "sequence-data",
                                     :editor-type        "dialog",
                                     :concept-var        "current-concept",
                                     :data               [{:type "sequence-data"
                                                           :data [{:type "empty" :duration 0}
                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                     :phrase             "welcome",
                                     :phrase-description "Welcome dialog",
                                     :dialog-track       "1 Welcome"}
                   :dialog-2-next
                                    {:type               "sequence-data",
                                     :editor-type        "dialog",
                                     :concept-var        "current-concept",
                                     :data               [{:type "sequence-data"
                                                           :data [{:type "empty" :duration 0}
                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                     :phrase             "next",
                                     :phrase-description "Next dialog",
                                     :dialog-track       "2 Next"}

                   :dialog-3-finish
                                    {:type               "sequence-data",
                                     :editor-type        "dialog",
                                     :concept-var        "current-concept",
                                     :data               [{:type "sequence-data"
                                                           :data [{:type "empty" :duration 0}
                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                     :phrase             "finish",
                                     :phrase-description "Finish dialog",
                                     :dialog-track       "3 Finish"},

                   :dialog-bubble-1
                                    {:type               "sequence-data",
                                     :editor-type        "dialog",
                                     :concept-var        "current-concept",
                                     :data               [{:type "sequence-data"
                                                           :data [{:type "empty" :duration 0}
                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                     :phrase             "bubble-1",
                                     :phrase-description "Bubble 1",
                                     :dialog-track       "4 Bubbles"},

                   :dialog-bubble-2
                                    {:type               "sequence-data",
                                     :editor-type        "dialog",
                                     :concept-var        "current-concept",
                                     :data               [{:type "sequence-data"
                                                           :data [{:type "empty" :duration 0}
                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                     :phrase             "bubble-2",
                                     :phrase-description "Bubble 2",
                                     :dialog-track       "4 Bubbles"},

                   :dialog-bubble-3
                                    {:type               "sequence-data",
                                     :editor-type        "dialog",
                                     :concept-var        "current-concept",
                                     :data               [{:type "sequence-data"
                                                           :data [{:type "empty" :duration 0}
                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                     :phrase             "bubble-3",
                                     :phrase-description "Bubble 3",
                                     :dialog-track       "4 Bubbles"},

                   :enable-story-1
                                    {:type "sequence-data",
                                     :data
                                           [{:id "clickable", :type "state", :target "image-story-1"}
                                            {:type "set-variable", :var-name "story-1-clickable", :var-value true}]},
                   :image-story-1-clicked
                                    {:type     "test-var-scalar",
                                     :var-name "story-1-clickable",
                                     :value    true,
                                     :success  "finish-story-1",
                                     :fail     {:type "empty", :duration 1}},
                   :finish-story-1
                                    {:type "sequence-data"
                                     :data
                                           [{:type     "set-variable",
                                             :from-var [{:var-name "concept-1", :action-property "var-value"}],
                                             :var-name "current-concept"}
                                            {:type "set-variable", :var-name "bubble-init-position", :var-value {:x 350, :y 470}}
                                            {:id "run-story", :type "action"}
                                            {:type "set-variable", :var-name "story-1-passed", :var-value true}
                                            {:type "set-variable", :var-name "story-2-clickable", :var-value true}
                                            {:id "clickable", :type "state", :target "image-story-2"}
                                            {:id "test-stories-complete", :type "action"}
                                            {:id "dialog-2-next", :type "action"}]}
                   :image-story-2-clicked
                                    {:type     "test-var-scalar",
                                     :var-name "story-2-clickable",
                                     :value    true,
                                     :success  "finish-story-2",
                                     :fail     {:type "empty", :duration 1}},
                   :finish-story-2
                                    {:type "sequence-data"
                                     :data
                                           [{:type     "set-variable",
                                             :from-var [{:var-name "concept-2", :action-property "var-value"}],
                                             :var-name "current-concept"}
                                            {:type "set-variable", :var-name "bubble-init-position", :var-value {:x 540, :y 465}}
                                            {:id "run-story", :type "action"}
                                            {:type "set-variable", :var-name "story-2-passed", :var-value true}
                                            {:type "set-variable", :var-name "story-3-clickable", :var-value true}
                                            {:id "clickable", :type "state", :target "image-story-3"}
                                            {:id "test-stories-complete", :type "action"}
                                            {:id "dialog-2-next", :type "action"}],}
                   :image-story-3-clicked
                                    {:type     "test-var-scalar",
                                     :var-name "story-3-clickable",
                                     :value    true,
                                     :success  "finish-story-3",
                                     :fail     {:type "empty", :duration 1}},
                   :finish-story-3
                                    {:type "sequence-data"
                                     :data
                                           [{:type     "set-variable",
                                             :from-var [{:var-name "concept-3", :action-property "var-value"}],
                                             :var-name "current-concept"}
                                            {:type "set-variable", :var-name "bubble-init-position", :var-value {:x 540, :y 465}}
                                            {:id "run-story", :type "action"}
                                            {:type "set-variable", :var-name "story-3-passed", :var-value true}
                                            {:id "test-stories-complete", :type "action"}],}
                   :init-concepts
                                    {:type "sequence-data",
                                     :data
                                           [{:from        "concepts-group",
                                             :type        "lesson-var-provider",
                                             :shuffled    false,
                                             :variables   ["concept-1" "concept-2" "concept-3"],
                                             :provider-id "words-set"}
                                            {:fail     "init-image-story-1",
                                             :type     "test-var-scalar",
                                             :value    nil,
                                             :success  {:type "set-variable", :var-name "story-1-passed", :var-value true},
                                             :var-name "concept-1"}
                                            {:fail     "init-image-story-2",
                                             :type     "test-var-scalar",
                                             :value    nil,
                                             :success  {:type "set-variable", :var-name "story-2-passed", :var-value true},
                                             :var-name "concept-2"}
                                            {:fail     "init-image-story-3",
                                             :type     "test-var-scalar",
                                             :value    nil,
                                             :success  {:type "set-variable", :var-name "story-3-passed", :var-value true},
                                             :var-name "concept-3"}]},
                   :init-image-story-1
                                    {:type "sequence-data",
                                     :data
                                           [{:id "visible", :type "state", :target "image-story-1"}
                                            {:type      "set-attribute",
                                             :target    "image-story-1",
                                             :from-var  [{:var-name "concept-1", :var-property "poem-image-1", :action-property "attr-value"}],
                                             :attr-name "src"}]},
                   :init-image-story-2
                                    {:type "sequence-data",
                                     :data
                                           [{:id "visible", :type "state", :target "image-story-2"}
                                            {:type      "set-attribute",
                                             :target    "image-story-2",
                                             :from-var  [{:var-name "concept-2", :var-property "poem-image-1", :action-property "attr-value"}],
                                             :attr-name "src"}]},
                   :init-image-story-3
                                    {:type "sequence-data",
                                     :data
                                           [{:id "visible", :type "state", :target "image-story-3"}
                                            {:type      "set-attribute",
                                             :target    "image-story-3",
                                             :from-var  [{:var-name "concept-3", :var-property "poem-image-1", :action-property "attr-value"}],
                                             :attr-name "src"}]},
                   :init-state
                                    {:type "sequence-data",
                                     :data
                                           [{:type "set-variable", :var-name "story-1-passed", :var-value false}
                                            {:type "set-variable", :var-name "story-2-passed", :var-value false}
                                            {:type "set-variable", :var-name "story-3-passed", :var-value false}
                                            {:type "set-variable", :var-name "story-1-clickable", :var-value false}
                                            {:type "set-variable", :var-name "story-2-clickable", :var-value false}
                                            {:type "set-variable", :var-name "story-3-clickable", :var-value false}]},


                   :none            {:type "empty", :duration 10},
                   :poem-pause
                                    {:type "empty", :duration 1000}
                   :prepare-bubbles
                                    {:type "sequence-data",
                                     :data
                                           [{:id "hidden", :type "state", :target "bubble-1"}
                                            {:type      "set-attribute",
                                             :target    "bubble-1",
                                             :from-var  [{:var-name "current-concept", :var-property "poem-image-1", :action-property "attr-value"}],
                                             :attr-name "src"}
                                            {:type      "set-attribute",
                                             :target    "bubble-1",
                                             :from-var  [{:var-name "bubble-init-position", :var-property "x", :action-property "attr-value"}],
                                             :attr-name "x"}
                                            {:type      "set-attribute",
                                             :target    "bubble-1",
                                             :from-var  [{:var-name "bubble-init-position", :var-property "y", :action-property "attr-value"}],
                                             :attr-name "y"}
                                            {:id "hidden", :type "state", :target "bubble-2"}
                                            {:type      "set-attribute",
                                             :target    "bubble-2",
                                             :from-var  [{:var-name "current-concept", :var-property "poem-image-2", :action-property "attr-value"}],
                                             :attr-name "src"}
                                            {:type      "set-attribute",
                                             :target    "bubble-2",
                                             :from-var  [{:var-name "bubble-init-position", :var-property "x", :action-property "attr-value"}],
                                             :attr-name "x"}
                                            {:type      "set-attribute",
                                             :target    "bubble-2",
                                             :from-var  [{:var-name "bubble-init-position", :var-property "y", :action-property "attr-value"}],
                                             :attr-name "y"}
                                            {:id "hidden", :type "state", :target "bubble-3"}
                                            {:type      "set-attribute",
                                             :target    "bubble-3",
                                             :from-var  [{:var-name "current-concept", :var-property "poem-image-3", :action-property "attr-value"}],
                                             :attr-name "src"}
                                            {:type      "set-attribute",
                                             :target    "bubble-3",
                                             :from-var  [{:var-name "bubble-init-position", :var-property "x", :action-property "attr-value"}],
                                             :attr-name "x"}
                                            {:type      "set-attribute",
                                             :target    "bubble-3",
                                             :from-var  [{:var-name "bubble-init-position", :var-property "y", :action-property "attr-value"}],
                                             :attr-name "y"}
                                            {:type "empty", :duration 300}]},
                   :run-bubble-1
                                    {:type "sequence-data",
                                     :data
                                           [{:id "visible", :type "state", :target "bubble-1"}
                                            {:type "empty", :duration 100}
                                            {:to            {:bezier [{:x 335, :y 390} {:x 350, :y 230} {:x 175, :y 100}], :duration 2.0},
                                             :type          "transition",
                                             :transition-id "bubble-1"}]},
                   :run-bubble-2
                                    {:type "sequence-data",
                                     :data
                                           [{:id "visible", :type "state", :target "bubble-2"}
                                            {:type "empty", :duration 100}
                                            {:to {:bezier [{:x 440, :y 215} {:x 735, :y 60}], :duration 2.0}, :type "transition", :transition-id "bubble-2"}]},
                   :run-bubble-3
                                    {:type "sequence-data",
                                     :data
                                           [{:id "visible", :type "state", :target "bubble-3"}
                                            {:type "empty", :duration 100}
                                            {:to            {:bezier [{:x 740, :y 450} {:x 950, :y 160} {:x 1315, :y 125}], :duration 2.0},
                                             :type          "transition",
                                             :transition-id "bubble-3"}]},
                   :run-story
                                    {:type "sequence-data",
                                     :data
                                           [{:id "prepare-bubbles", :type "action"}
                                            {:id "run-concept-poem", :type "action"}]},
                   :run-concept-poem
                                    {:type "sequence-data"
                                     :data
                                           [{:id "run-bubble-1", :type "action"}
                                            {:id "dialog-bubble-1", :type "action"}
                                            {:id "poem-pause", :type "action"}
                                            {:id "run-bubble-2", :type "action"}
                                            {:id "dialog-bubble-2", :type "action"}
                                            {:id "poem-pause", :type "action"}
                                            {:id "run-bubble-3", :type "action"}
                                            {:id "dialog-bubble-3", :type "action"}
                                            {:id "poem-pause", :type "action"}]}
                   :start-scene
                                    {:type        "sequence",
                                     :data        ["start-activity" "init-state" "init-concepts" "dialog-1-welcome" "enable-story-1"],
                                     :description "Initial action"},
                   :start-activity  {:type "start-activity"},
                   :stop-activity   {:type "stop-activity"},
                   :finish-activity {:type "finish-activity"},
                   :finish-poem     {:type "sequence-data"
                                     :data [{:id "dialog-3-finish" :type "action"}
                                            {:id "finish-activity" :type "action"}]}
                   :test-stories-complete
                                    {:type      "test-var-list",
                                     :success   "finish-poem",
                                     :values    [true true true],
                                     :var-names ["story-1-passed" "story-2-passed" "story-3-passed"]}},
        :triggers {:back {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata {:autostart true},})

(defn f
  [t args]
  t)

(core/register-template
  m
  (partial f t))

