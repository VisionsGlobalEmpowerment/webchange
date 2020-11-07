(ns webchange.templates.library.running
  (:require
    [webchange.templates.core :as core]))

(def m {:id          16
        :name        "Running"
        :tags        ["Independent Practice"]
        :description "Running"
        :lesson-sets ["concepts-group"]
        :fields      [{:name "image-src",
                       :type "image"}
                      {:name "concept-name",
                       :type "string"}
                      {:name "letter",
                       :type "string"}]})

(def t {:assets
                       [{:url "/raw/img/stadium/running/bg_01.jpg", :type "image"}
                        {:url "/raw/img/stadium/running/bg_02.jpg", :type "image"}
                        {:url "/raw/img/stadium/running/bg_03.jpg", :type "image"}],
        :objects
                       {:background
                        {:type   "carousel",
                         :x      0,
                         :y      0,
                         :width  1920,
                         :height 1080,
                         :first  "/raw/img/stadium/running/bg_01.jpg",
                         :last   "/raw/img/stadium/running/bg_03.jpg",
                         :next   "/raw/img/stadium/running/bg_02.jpg"},
                        :box1
                        {:type        "animation",
                         :x           2000,
                         :y           683,
                         :width       671,
                         :height      633,
                         :scale       {:x -0.25, :y 0.25},
                         :scene-name  "box1",
                         :transition  "box1",
                         :actions     {:click {:id "pick-box-1", :on "click", :type "action" :unique-tag "box"}},
                         :anim        "idle2",
                         :anim-offset {:x 0, :y -300},
                         :loop        true,
                         :name        "boxes",
                         :skin        "qwestion",
                         :speed       0.3,
                         :start       true,
                         :states      {:init {:visible false}, :reset {:x 2000, :visible true}}},
                        :box2
                        {:type        "animation",
                         :x           2200,
                         :y           789,
                         :width       671,
                         :height      633,
                         :scale       {:x -0.25, :y 0.25},
                         :scene-name  "box2",
                         :transition  "box2",
                         :actions     {:click {:id "pick-box-2", :on "click", :type "action" :unique-tag "box"}},
                         :anim        "idle2",
                         :anim-offset {:x 0, :y -300},
                         :loop        true,
                         :name        "boxes",
                         :skin        "qwestion",
                         :speed       0.3,
                         :start       true,
                         :states      {:init {:visible false}, :reset {:x 2200, :visible true}}},
                        :box3
                        {:type        "animation",
                         :x           2400,
                         :y           932,
                         :width       671,
                         :height      633,
                         :scale       {:x -0.25, :y 0.25},
                         :scene-name  "box3",
                         :transition  "box3",
                         :actions     {:click {:id "pick-box-3", :on "click", :type "action" :unique-tag "box"}},
                         :anim        "idle2",
                         :anim-offset {:x 0, :y -300},
                         :loop        true,
                         :name        "boxes",
                         :skin        "qwestion",
                         :speed       0.3,
                         :start       true,
                         :states      {:init {:visible false}, :reset {:x 2400, :visible true}}},
                        :letter1
                        {:type           "text",
                         :x              2000,
                         :y              400,
                         :width          150,
                         :height         150,
                         :transition     "letter1",
                         :align          "center",
                         :fill           "white",
                         :font-family    "Lexend Deca",
                         :font-size      120,
                         :shadow-blur    5,
                         :shadow-color   "#1a1a1a",
                         :shadow-opacity 0.5,
                         :states         {:init {:visible false}, :reset {:x 2000, :visible true}},
                         :text           "?",
                         :vertical-align "middle"},
                        :letter2
                        {:type           "text",
                         :x              2200,
                         :y              506,
                         :width          150,
                         :height         150,
                         :transition     "letter2",
                         :align          "center",
                         :fill           "white",
                         :font-family    "Lexend Deca",
                         :font-size      120,
                         :shadow-blur    5,
                         :shadow-color   "#1a1a1a",
                         :shadow-opacity 0.5,
                         :states         {:init {:visible false}, :reset {:x 2200, :visible true}},
                         :text           "?",
                         :vertical-align "middle"},
                        :letter3
                        {:type           "text",
                         :x              2400,
                         :y              649,
                         :width          150,
                         :height         150,
                         :transition     "letter3",
                         :align          "center",
                         :fill           "white",
                         :font-family    "Lexend Deca",
                         :font-size      120,
                         :shadow-blur    5,
                         :shadow-color   "#1a1a1a",
                         :shadow-opacity 0.5,
                         :states         {:init {:visible false}, :reset {:x 2400, :visible true}},
                         :text           "?",
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
                         :speed       0.5,
                         :start       true},
                        :vera
                        {:type       "animation",
                         :x          500,
                         :y          775,
                         :width      727,
                         :height     1091,
                         :scene-name "vera",
                         :transition "vera",
                         :anim       "run",
                         :meshes     true,
                         :name       "vera-90",
                         :scale-x    0.4,
                         :scale-y    0.4,
                         :skin       "default",
                         :speed      1,
                         :start      true}},
        :scene-objects [["background"] ["box1" "letter1" "box2" "letter2" "box3" "letter3" "vera" "mari"]],
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
                                                  :dialog-track       "1 Welcome"
                                                  :skippable              true},
                        :dialog-2-chant
                                                 {:type               "sequence-data",
                                                  :editor-type        "dialog",
                                                  :concept-var        "current-concept",
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                  :phrase             "chant",
                                                  :phrase-description "Chant dialog",
                                                  :dialog-track       "2 Chant"
                                                  :tags ["instruction"]},
                        :dialog-3-correct
                                                 {:type               "sequence-data",
                                                  :editor-type        "dialog",
                                                  :concept-var        "current-concept",
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                  :phrase             "correct",
                                                  :phrase-description "Correct dialog",
                                                  :dialog-track       "3 Options"}
                        :dialog-4-wrong
                                                 {:type               "sequence-data",
                                                  :editor-type        "dialog",
                                                  :concept-var        "current-concept",
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                  :phrase             "wrong",
                                                  :phrase-description "Wrong dialog",
                                                  :dialog-track       "3 Options"}
                        :clear-instruction {:type "remove-flows", :flow-tag "instruction"},
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
                        :init-vera-position      {:type "set-variable", :var-name "current-line", :var-value "box2"},
                        :pick-box-1
                        {:type "sequence-data",
                         :data
                               [{:type "set-variable", :var-name "current-line-pos", :var-value {:y 675, :duration 0.5}}
                                {:type "set-variable", :var-name "current-line-jump", :var-value {:y 475, :duration 1}}
                                {:type "set-variable", :var-name "jump-wait", :var-value 10}
                                {:fail     "go-to-box1-line",
                                 :type     "test-value",
                                 :value1   "box1",
                                 :success  "stay-on-line",
                                 :from-var [{:var-name "current-line", :action-property "value2"}]}
                                {:id "clear-instruction" :type "action"}
                                {:fail    "pick-wrong",
                                 :type    "test-value",
                                 :success "pick-correct",
                                 :from-var
                                          [{:var-name "box1", :action-property "value1"} {:var-name "current-concept", :action-property "value2"}]}]},
                        :pick-box-2
                        {:type "sequence-data",
                         :data
                               [{:type "set-variable", :var-name "current-line-pos", :var-value {:y 775, :duration 0.5}}
                                {:type "set-variable", :var-name "current-line-jump", :var-value {:y 575, :duration 1}}
                                {:type "set-variable", :var-name "jump-wait", :var-value 100}
                                {:type     "case",
                                 :options
                                           {:box1 {:id "go-to-box2-line-down", :type "action"},
                                            :box2 {:id "stay-on-line", :type "action"},
                                            :box3 {:id "go-to-box2-line-up", :type "action"}},
                                 :from-var [{:var-name "current-line", :action-property "value"}]}
                                {:id "clear-instruction" :type "action"}
                                {:fail    "pick-wrong",
                                 :type    "test-value",
                                 :success "pick-correct",
                                 :from-var
                                          [{:var-name "box2", :action-property "value1"} {:var-name "current-concept", :action-property "value2"}]}]},
                        :pick-box-3
                        {:type "sequence-data",
                         :data
                               [{:type "set-variable", :var-name "current-line-pos", :var-value {:y 895, :duration 0.5}}
                                {:type "set-variable", :var-name "current-line-jump", :var-value {:y 695, :duration 1}}
                                {:type "set-variable", :var-name "jump-wait", :var-value 200}
                                {:fail     "go-to-box3-line",
                                 :type     "test-value",
                                 :value1   "box3",
                                 :success  "stay-on-line",
                                 :from-var [{:var-name "current-line", :action-property "value2"}]}
                                {:id "clear-instruction" :type "action"}
                                {:fail    "pick-wrong",
                                 :type    "test-value",
                                 :success "pick-correct",
                                 :from-var
                                          [{:var-name "box3", :action-property "value1"} {:var-name "current-concept", :action-property "value2"}]}]},
                        :pick-correct
                        {:type "sequence-data",
                         :data
                               [{:id "dialog-3-correct", :type "action" :return-immediately true}
                                {:data
                                       [{:to {:x -700, :duration 2}, :type "transition", :transition-id "box1"}
                                        {:to {:x -500, :duration 2}, :type "transition", :transition-id "box2"}
                                        {:to {:x -300, :duration 2}, :type "transition", :transition-id "box3"}
                                        {:to {:x -755, :duration 2}, :type "transition", :transition-id "letter1"}
                                        {:to {:x -555, :duration 2}, :type "transition", :transition-id "letter2"}
                                        {:to {:x -355, :duration 2}, :type "transition", :transition-id "letter3"}
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
                         :data [{:id "dialog-4-wrong", :type "action"}
                                {:type "remove-flow-tag" :tag "box"}
                                {:id "dialog-2-chant", :type "action"}]},
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
                                {:from        ["item-1-1" "item-1-2" "item-1-3" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8"],
                                 :type        "vars-var-provider",
                                 :on-end      "finish-activity",
                                 :shuffled    true,
                                 :variables   ["current-concept"],
                                 :provider-id "current-concept"}
                                {:from      ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8"],
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
                                 :variables ["box1" "box2" "box3"]}
                                {:data
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
                                         :attachment {:x 40, :scale-x 4, :scale-y 4}}],
                                 :type "parallel"}
                                {:data
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
                                         :attr-name "text"}],
                                 :type "parallel"}
                                {:data
                                       [{:to {:x 1300, :duration 1}, :type "transition", :transition-id "box1"}
                                        {:to {:x 1500, :duration 1}, :type "transition", :transition-id "box2"}
                                        {:to {:x 1700, :duration 1}, :type "transition", :transition-id "box3"}
                                        {:to {:x 1300, :duration 1}, :type "transition", :transition-id "letter1"}
                                        {:to {:x 1500, :duration 1}, :type "transition", :transition-id "letter2"}
                                        {:to {:x 1700, :duration 1}, :type "transition", :transition-id "letter3"}],
                                 :type "parallel"}
                                {:type "remove-flow-tag" :tag "box"}
                                {:id "dialog-2-chant", :type "action"}]},
                        :renew-words
                        {:type "sequence-data",
                         :data
                               [{:from        "concepts-group",
                                 :type        "lesson-var-provider",
                                 :limit       3,
                                 :repeat      4,
                                 :shuffled    false,
                                 :variables   ["item-1" "item-2" "item-3" "item-4" "item-5" "item-6" "item-7" "item-8"],
                                 :provider-id "words-set"}
                                {:from "item-1", :type "copy-variable", :var-name "item-1-1"}
                                {:from "item-1", :type "copy-variable", :var-name "item-1-2"}
                                {:from "item-1", :type "copy-variable", :var-name "item-1-3"}]},
                        :start-scene
                        {:type "sequence",
                         :data
                               ["start-activity"
                                "dialog-1-welcome"
                                "init-slots"
                                "init-vera-position"
                                "renew-words"
                                "renew-current-concept"]},
                        :stay-on-line            {:type "empty", :duration 100},
                        :stop-scene              {:type "sequence", :data ["stop-activity"]},
                        :start-activity          {:type "start-activity"},
                        :stop-activity           {:type "stop-activity"},
                        :finish-activity         {:type "finish-activity"},
                        :wait-for-box-animations {:type "empty", :duration 100}},
        :triggers      {:stop {:on "back", :action "stop-scene"}, :start {:on "start", :action "start-scene"}},
        :metadata      {:autostart true}})

(defn f
  [t args]
  t)

(core/register-template
  (:id m)
  m
  (partial f t))

