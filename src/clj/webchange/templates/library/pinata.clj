(ns webchange.templates.library.pinata
  (:require
    [webchange.templates.core :as core]))

(def m {:id          3
        :name        "pinata"
        :description "Some description of pinata mechanics and covered skills"
        :lesson-sets ["assessment"]
        :fields      [{:name "image-src",
                       :type "image"}
                      {:name "letter",
                       :type "string"}]})

(def t {:assets   [{:url "/raw/img/park/pinata/background.jpg", :type "image"}],
        :objects  {:background {:type "background", :src "/raw/img/park/pinata/background.jpg"},
                   :box1
                               {:type       "animation",
                                :x          205,
                                :y          930,
                                :width      671,
                                :height     633,
                                :scene-name "box1",
                                :transition "box1",
                                :visible    false,
                                :actions    {:drag-end {:id "check-box-drop", :on "drag-end", :type "action", :params {:target "box1"}}},
                                :anim       "idle2",
                                :draggable  {:var-name "drag-box-1"},
                                :loop       true,
                                :name       "boxes",
                                :scale-x    0.25,
                                :scale-y    0.25,
                                :skin       "qwestion",
                                :speed      0.3,
                                :start      true},
                   :box1-ph
                               {:type       "transparent",
                                :x          880,
                                :y          930,
                                :width      40,
                                :height     40,
                                :scene-name "box1-ph",
                                :transition "box1-ph",
                                :scale-x    1,
                                :scale-y    1},
                   :box2
                               {:type       "animation",
                                :x          410,
                                :y          985,
                                :width      671,
                                :height     633,
                                :scene-name "box2",
                                :transition "box2",
                                :visible    false,
                                :actions    {:drag-end {:id "check-box-drop", :on "drag-end", :type "action", :params {:target "box2"}}},
                                :anim       "idle2",
                                :draggable  {:var-name "drag-box-2"},
                                :loop       true,
                                :name       "boxes",
                                :scale-x    0.25,
                                :scale-y    0.25,
                                :skin       "qwestion",
                                :speed      0.3,
                                :start      true},
                   :box2-ph
                               {:type       "transparent",
                                :x          1110,
                                :y          930,
                                :width      40,
                                :height     40,
                                :scene-name "box2-ph",
                                :transition "box2-ph",
                                :scale-x    1,
                                :scale-y    1},
                   :box3
                               {:type       "animation",
                                :x          610,
                                :y          955,
                                :width      671,
                                :height     633,
                                :scene-name "box3",
                                :transition "box3",
                                :visible    false,
                                :actions    {:drag-end {:id "check-box-drop", :on "drag-end", :type "action", :params {:target "box3"}}},
                                :anim       "idle2",
                                :draggable  {:var-name "drag-box-3"},
                                :loop       true,
                                :name       "boxes",
                                :scale-x    0.25,
                                :scale-y    0.25,
                                :skin       "qwestion",
                                :speed      0.3,
                                :start      true},
                   :box3-ph
                               {:type       "transparent",
                                :x          1340,
                                :y          930,
                                :width      40,
                                :height     40,
                                :scene-name "box3-ph",
                                :transition "box3-ph",
                                :scale-x    1,
                                :scale-y    1},
                   :letter1
                               {:type           "text",
                                :x              850,
                                :y              760,
                                :width          150,
                                :height         150,
                                :align          "center",
                                :fill           "white",
                                :font-family    "Lexend Deca",
                                :font-size      140,
                                :scale-x        1,
                                :scale-y        1,
                                :text           "",
                                :vertical-align "bottom"},
                   :letter2
                               {:type           "text",
                                :x              1050,
                                :y              760,
                                :width          150,
                                :height         150,
                                :align          "center",
                                :fill           "white",
                                :font-family    "Lexend Deca",
                                :font-size      140,
                                :scale-x        1,
                                :scale-y        1,
                                :text           "",
                                :vertical-align "bottom"},
                   :letter3
                               {:type           "text",
                                :x              1250,
                                :y              760,
                                :width          150,
                                :height         150,
                                :align          "center",
                                :fill           "white",
                                :font-family    "Lexend Deca",
                                :font-size      140,
                                :scale-x        1,
                                :scale-y        1,
                                :text           "",
                                :vertical-align "bottom"},
                   :mari
                               {:type       "animation",
                                :x          1100,
                                :y          420,
                                :width      473,
                                :height     511,
                                :scene-name "mari",
                                :transition "mari",
                                :anim       "idle",
                                :loop       true,
                                :name       "mari",
                                :scale-x    0.5,
                                :scale-y    0.5,
                                :speed      0.35,
                                :start      true},
                   :pinata
                               {:type       "animation",
                                :x          925,
                                :y          555,
                                :width      678,
                                :height     899,
                                :scene-name "pinata",
                                :transition "pinata",
                                :anim       "idle",
                                :loop       true,
                                :name       "pinata",
                                :scale-x    1,
                                :scale-y    1,
                                :speed      0.35,
                                :start      true}},
        :scene-objects
                  [["background"]
                   ["pinata" "mari" "letter1" "letter2" "letter3" "box1-ph" "box2-ph" "box3-ph" "box1" "box2" "box3"]],
        :actions
                  {:dialog-1-welcome {:type               "sequence-data",
                                      :editor-type        "dialog",
                                      :concept-var        "current-slot",
                                      :data               [{:type "sequence-data"
                                                            :data [{:type "empty" :duration 0}
                                                                   {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                      :phrase             "welcome",
                                      :phrase-description "Welcome",
                                      :dialog-track       "1 Intro"}
                   :dialog-2-task
                                     {:type               "sequence-data",
                                      :editor-type        "dialog",
                                      :concept-var        "current-slot",
                                      :data               [{:type "sequence-data"
                                                            :data [{:type "empty" :duration 0}
                                                                   {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                      :phrase             "task",
                                      :phrase-description "Task",
                                      :dialog-track       "2 Task"},
                   :dialog-3-finish
                                     {:type               "sequence-data",
                                      :editor-type        "dialog",
                                      :concept-var        "current-slot",
                                      :data               [{:type "sequence-data"
                                                            :data [{:type "empty" :duration 0}
                                                                   {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                      :phrase             "finish",
                                      :phrase-description "Finish",
                                      :dialog-track       "3 Finish"},
                   :dialog-4-wrong
                                     {:type               "sequence-data",
                                      :editor-type        "dialog",
                                      :concept-var        "current-slot",
                                      :data               [{:type "sequence-data"
                                                            :data [{:type "empty" :duration 0}
                                                                   {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                      :phrase             "wrong",
                                      :phrase-description "wrong",
                                      :dialog-track       "4 Wrong"},
                   :dialog-5-correct
                                     {:type               "sequence-data",
                                      :editor-type        "dialog",
                                      :concept-var        "current-slot",
                                      :data               [{:type "sequence-data"
                                                            :data [{:type "empty" :duration 0}
                                                                   {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                      :phrase             "correct",
                                      :phrase-description "Correct",
                                      :dialog-track       "5 Correct"},

                   :boxes-disappear
                                     {:type "parallel",
                                      :data
                                            [{:skin "qwestion", :type "set-skin", :target "box1"}
                                             {:skin "qwestion", :type "set-skin", :target "box2"}
                                             {:skin "qwestion", :type "set-skin", :target "box3"}
                                             {:type "set-attribute", :target "box1", :attr-name "visible", :attr-value false}
                                             {:type "set-attribute", :target "box2", :attr-name "visible", :attr-value false}
                                             {:type "set-attribute", :target "box3", :attr-name "visible", :attr-value false}]},
                   :check-box-drop
                                     {:type       "sequence-data",
                                      :unique-tag "drop"
                                      :data
                                                  [{:type        "set-variable",
                                                    :var-name    "current-box",
                                                    :from-params [{:param-property "target", :action-property "var-value"}]}
                                                   {:fail         "empty",
                                                    :type         "test-transitions-collide",
                                                    :success      "check-place-1",
                                                    :from-params  [{:param-property "target", :action-property "transition-1"}],
                                                    :transition-2 "box1-ph"}
                                                   {:fail         "empty",
                                                    :type         "test-transitions-collide",
                                                    :success      "check-place-2",
                                                    :from-params  [{:param-property "target", :action-property "transition-1"}],
                                                    :transition-2 "box2-ph"}
                                                   {:fail         "empty",
                                                    :type         "test-transitions-collide",
                                                    :success      "check-place-3",
                                                    :from-params  [{:param-property "target", :action-property "transition-1"}],
                                                    :transition-2 "box3-ph"}]},
                   :check-current-slot
                                     {:type    "test-var-scalar",
                                      :success "correct",
                                      :fail    "dialog-4-wrong",
                                      :from-var
                                               [{:var-name "current-box", :action-property "var-name"} {:var-name "current-slot", :action-property "value"}]},
                   :check-place-1
                                     {:type     "test-var-scalar",
                                      :success  "check-current-slot",
                                      :fail     "dialog-4-wrong",
                                      :from-var [{:var-name "current-box", :action-property "var-name"} {:var-name "letter1", :action-property "value"}]},
                   :check-place-2
                                     {:type     "test-var-scalar",
                                      :success  "check-current-slot",
                                      :fail     "dialog-4-wrong",
                                      :from-var [{:var-name "current-box", :action-property "var-name"} {:var-name "letter2", :action-property "value"}]},
                   :check-place-3
                                     {:type     "test-var-scalar",
                                      :success  "check-current-slot",
                                      :fail     "dialog-4-wrong",
                                      :from-var [{:var-name "current-box", :action-property "var-name"} {:var-name "letter3", :action-property "value"}]},
                   :correct          {:type "sequence-data",
                                      :data [{:id "clear-instruction", :type "action"}
                                             {:id "dialog-5-correct", :type "action"}
                                             {:id "next-task", :type "action"}]},
                   :empty            {:type "empty", :duration 100},
                   :finish-activity
                                     {:type "sequence-data",
                                      :data
                                            [{:id "mari-hits-pinata-toward", :type "action"}
                                             {:id "mari-hits-pinata-backward", :type "action"}
                                             {:id "pinata-fall-down", :type "action"}
                                             {:type "empty", :duration 2000}
                                             {:type "action" :id "dialog-3-finish"}
                                             {:id "pinata", :type "finish-activity"}]},
                   :mari-hits-pinata-backward
                                     {:type "sequence-data",
                                      :data
                                            [{:to            {:ease [1 1], :bezier [{:x 691, :y 500} {:x 1100, :y 420}], :duration 2},
                                              :type          "transition",
                                              :transition-id "mari"}]},
                   :mari-hits-pinata-toward
                                     {:type "sequence-data",
                                      :data
                                            [{:to {:bezier [{:x 691, :y 636} {:x 513, :y 570}], :duration 1.3}, :type "transition", :transition-id "mari"}]},
                   :next-round
                                     {:type "sequence-data",
                                      :data
                                            [{:type "set-variable", :var-name "current-slot-number", :var-value 0}
                                             {:from        "assessment",
                                              :type        "lesson-var-provider",
                                              :on-end      "finish-activity",
                                              :shuffled    false,
                                              :variables   ["concept-1" "concept-2" "concept-3"],
                                              :provider-id "words-set"}
                                             {:from      ["concept-1" "concept-2" "concept-3"],
                                              :type      "vars-var-provider",
                                              :shuffled  true,
                                              :variables ["box1" "box2" "box3"]}
                                             {:from      ["concept-1" "concept-2" "concept-3"],
                                              :type      "vars-var-provider",
                                              :shuffled  true,
                                              :variables ["letter1" "letter2" "letter3"]}
                                             {:from      ["concept-1" "concept-2" "concept-3"],
                                              :type      "vars-var-provider",
                                              :shuffled  true,
                                              :variables ["slot1" "slot2" "slot3"]}
                                             {:id "mari-hits-pinata-toward", :type "action"}
                                             {:type "parallel"
                                              :data
                                                    [{:id "mari-hits-pinata-backward", :type "action"}
                                                     {:type "sequence-data",
                                                      :data
                                                            [{:id "hit", :type "animation", :target "pinata"}
                                                             {:id "idle", :loop true, :type "add-animation", :target "pinata"}]}
                                                     {:type "sequence-data",
                                                      :data
                                                            [{:type "set-attribute", :target "box1", :attr-name "x", :attr-value 420}
                                                             {:type "set-attribute", :target "box1", :attr-name "y", :attr-value 595}
                                                             {:type "set-attribute", :target "box2", :attr-name "x", :attr-value 420}
                                                             {:type "set-attribute", :target "box2", :attr-name "y", :attr-value 595}
                                                             {:type "set-attribute", :target "box3", :attr-name "x", :attr-value 420}
                                                             {:type "set-attribute", :target "box3", :attr-name "y", :attr-value 595}
                                                             {:type "parallel",
                                                              :data
                                                                    [{:type "sequence-data"
                                                                      :data
                                                                            [{:type "set-attribute", :target "box1", :attr-name "visible", :attr-value true}
                                                                             {:id "come2", :type "animation", :target "box1"}
                                                                             {:id "idle2", :loop true, :type "add-animation", :target "box1"}]}
                                                                     {:type "sequence-data"
                                                                      :data
                                                                            [{:type "set-attribute", :target "box2", :attr-name "visible", :attr-value true}
                                                                             {:id "come2", :type "animation", :target "box2"}
                                                                             {:id "idle2", :loop true, :type "add-animation", :target "box2"}]}
                                                                     {:type "sequence-data"
                                                                      :data
                                                                            [{:type "set-attribute", :target "box3", :attr-name "visible", :attr-value true}
                                                                             {:id "come2", :type "animation", :target "box3"}
                                                                             {:id "idle2", :loop true, :type "add-animation", :target "box3"}]}]}
                                                             {:type "parallel",
                                                              :data
                                                                    [{:to {:bezier [{:x 256, :y 640} {:x 205, :y 930}], :duration 0.7}, :type "transition", :transition-id "box1"}
                                                                     {:to {:bezier [{:x 395, :y 758} {:x 410, :y 985}], :duration 0.7}, :type "transition", :transition-id "box2"}
                                                                     {:to {:bezier [{:x 570, :y 680} {:x 610, :y 955}], :duration 0.7}, :type "transition", :transition-id "box3"}]}]}]}
                                             {:id "set-concepts-data", :type "action"}
                                             {:id "next-task", :type "action"}]},
                   :next-task
                                     {:type "sequence-data",
                                      :data
                                            [{:type "counter", :counter-id "current-slot-number", :counter-action "increase"}
                                             {:id "set-next-current-slot", :type "action"}
                                             {:id "dialog-2-task", :type "action"}]},
                   :pinata-fall-down
                                     {:type "sequence-data",
                                      :data
                                            [{:id "empty", :type "animation", :target "pinata"}
                                             {:id "empty_idle", :loop true, :type "add-animation", :target "pinata"}]},
                   :set-concepts-data
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
                                              :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                             {:type      "set-attribute",
                                              :target    "letter1",
                                              :from-var  [{:var-name "letter1", :var-property "letter", :action-property "attr-value"}],
                                              :attr-name "text"}
                                             {:type      "set-attribute",
                                              :target    "letter2",
                                              :from-var  [{:var-name "letter2", :var-property "letter", :action-property "attr-value"}],
                                              :attr-name "text"}
                                             {:type      "set-attribute",
                                              :target    "letter3",
                                              :from-var  [{:var-name "letter3", :var-property "letter", :action-property "attr-value"}],
                                              :attr-name "text"}]},
                   :set-next-current-slot
                                     {:type "sequence-data",
                                      :data
                                            [{:fail
                                                        {:fail
                                                                   {:fail     "next-round",
                                                                    :type     "test-var-scalar",
                                                                    :value    3,
                                                                    :success
                                                                              {:type "set-variable", :from-var [{:var-name "slot3", :action-property "var-value"}], :var-name "current-slot"},
                                                                    :var-name "current-slot-number"},
                                                         :type     "test-var-scalar",
                                                         :value    2,
                                                         :success
                                                                   {:type "set-variable", :from-var [{:var-name "slot2", :action-property "var-value"}], :var-name "current-slot"},
                                                         :var-name "current-slot-number"},
                                              :type     "test-var-scalar",
                                              :value    1,
                                              :success
                                                        {:type "set-variable", :from-var [{:var-name "slot1", :action-property "var-value"}], :var-name "current-slot"},
                                              :var-name "current-slot-number"}]},
                   :start-scene      {:type "sequence-data"
                                      :data [{:type "start-activity"}
                                             {:type "action" :id "dialog-1-welcome"}
                                             {:type "action" :id "next-round"}]},
                   :stop-activity    {:type "stop-activity"}},
        :triggers {:stop {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata {:autostart true}})

(defn f
  [t args]
  t)

(core/register-template
  (:id m)
  m
  (partial f t))

