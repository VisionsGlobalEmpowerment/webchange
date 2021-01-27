(ns webchange.templates.library.slide
  (:require
    [webchange.templates.core :as core]))

(def m {:id          14
        :name        "slide"
        :tags        ["Independent Practice"]
        :description "Slide"
        :lesson-sets ["concepts-group"]
        :fields      [{:name "image-src",
                       :type "image"}
                      {:name "concept-name",
                       :type "string"}]})

(def t {:assets
                       [{:url "/raw/img/park/slide/background2.jpg", :type "image"}
                        {:url "/raw/img/park/slide/slide.png", :type "image"}
                        {:url "/raw/img/park/slide/side.png", :type "image"}],
        :objects
                       {:background {:type "background", :src "/raw/img/park/slide/background2.jpg"},
                        :box1
                                    {:type       "animation",
                                     :x          810,
                                     :y          216,
                                     :width      671,
                                     :height     633,
                                     :scale      {:x 0.25, :y 0.25},
                                     :scene-name "box1",
                                     :transition "box1",
                                     :actions    {:click {:id "pick-box-1", :on "click", :type "action" :unique-tag "clickable"}},
                                     :anim       "idle2",
                                     :loop       true,
                                     :name       "boxes",
                                     :skin       "qwestion",
                                     :speed      0.3,
                                     :start      true,
                                     :states     {:hidden {:visible false}, :visible {:visible true}, :init-position {:x 810, :y 216}}},
                        :box2
                                    {:type       "animation",
                                     :x          500,
                                     :y          287,
                                     :width      671,
                                     :height     633,
                                     :scale      {:x 0.25, :y 0.25},
                                     :scene-name "box2",
                                     :transition "box2",
                                     :actions    {:click {:id "pick-box-2", :on "click", :type "action" :unique-tag "clickable"}},
                                     :anim       "idle2",
                                     :loop       true,
                                     :name       "boxes",
                                     :skin       "qwestion",
                                     :speed      0.3,
                                     :start      true,
                                     :states     {:hidden {:visible false}, :visible {:visible true}, :init-position {:x 500, :y 287}}},
                        :box3
                                    {:type       "animation",
                                     :x          655,
                                     :y          212,
                                     :width      671,
                                     :height     633,
                                     :scale      {:x 0.25, :y 0.25},
                                     :scene-name "box3",
                                     :transition "box3",
                                     :actions    {:click {:id "pick-box-3", :on "click", :type "action" :unique-tag "clickable"}},
                                     :anim       "idle2",
                                     :loop       true,
                                     :name       "boxes",
                                     :skin       "qwestion",
                                     :speed      0.3,
                                     :start      true,
                                     :states     {:hidden {:visible false}, :visible {:visible true}, :init-position {:x 655, :y 212}}},
                        :mari
                                    {:type       "animation",
                                     :x          1600,
                                     :y          580,
                                     :width      473,
                                     :height     511,
                                     :transition "mari",
                                     :anim       "idle",
                                     :name       "mari",
                                     :scale-x    0.5,
                                     :scale-y    0.5,
                                     :speed      0.35,
                                     :start      true},
                        :slide      {:type "image", :x 200, :y 190, :width 997, :height 758, :src "/raw/img/park/slide/slide.png"},
                        :slide-side {:type "image", :x 591, :y 450, :width 234, :height 497, :src "/raw/img/park/slide/side.png"}},
        :scene-objects [["background"] ["slide" "slide-side" "mari" "box1" "box3" "box2"]],
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
                                            :dialog-track       "1 Welcome"},
                        :dialog-2-riddle
                                           {:type               "sequence-data",
                                            :tags               ["instruction"]
                                            :editor-type        "dialog",
                                            :concept-var        "current-concept",
                                            :data               [{:type "sequence-data"
                                                                  :data [{:type "empty" :duration 0}
                                                                         {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                            :phrase             "riddle",
                                            :phrase-description "Riddle dialog",
                                            :dialog-track       "2 Riddle"}
                        :dialog-3-correct
                                           {:type               "sequence-data",
                                            :editor-type        "dialog",
                                            :concept-var        "current-concept",
                                            :data               [{:type "sequence-data"
                                                                  :data [{:type "empty" :duration 0}
                                                                         {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                            :phrase             "correct",
                                            :phrase-description "Dialog correct",
                                            :dialog-track       "3 Options"},
                        :dialog-4-wrong
                                           {:type               "sequence-data",
                                            :editor-type        "dialog",
                                            :concept-var        "current-concept",
                                            :data               [{:type "sequence-data"
                                                                  :data [{:type "empty" :duration 0}
                                                                         {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                            :phrase             "wrong",
                                            :phrase-description "Dialog wrong",
                                            :dialog-track       "3 Options"},
                        :dialog-5-try-again
                                           {:type               "sequence-data",
                                            :editor-type        "dialog",
                                            :concept-var        "current-concept",
                                            :data               [{:type "sequence-data"
                                                                  :data [{:type "empty" :duration 0}
                                                                         {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                            :phrase             "try-again",
                                            :phrase-description "Dialog try another",
                                            :dialog-track       "3 Options"},
                        :dialog-6-picked
                                           {:type               "sequence-data",
                                            :editor-type        "dialog",
                                            :concept-var        "current-concept",
                                            :data               [{:type "sequence-data"
                                                                  :data [{:type "empty" :duration 0}
                                                                         {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                            :phrase             "picked",
                                            :phrase-description "Dialog picked",
                                            :dialog-track       "3 Options"},
                        :dialog-x-finish
                                           {:type               "sequence-data",
                                            :editor-type        "dialog",
                                            :concept-var        "current-concept",
                                            :data               [{:type "sequence-data"
                                                                  :data [{:type "empty" :duration 0}
                                                                         {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                            :phrase             "finish",
                                            :phrase-description "Finish dialog",
                                            :dialog-track       "4 Finish"},
                        :clear-instruction {:type "remove-flows", :flow-tag "instruction"},
                        :finish-activity
                                           {:type "sequence-data"
                                            :data [{:type "action" :id "dialog-x-finish"}
                                                   {:type "finish-activity"}]},
                        :pick-box-1
                                           {:type "sequence-data"
                                            :data [{:type "copy-variable" :var-name "selected-concept" :from "box1"}
                                                   {:id "clear-instruction" :type "action"}
                                                   {:type "action" :id "dialog-6-picked"}
                                                   {:id "clear-instruction" :type "action"}
                                                   {:fail     "pick-wrong",
                                                    :type     "test-value",
                                                    :success
                                                              {:data [{:type "set-variable", :var-name "current-target", :var-value "box1"} {:id "pick-correct", :type "action"}],
                                                               :type "sequence-data"},
                                                    :from-var [{:var-name "box1", :action-property "value1"} {:var-name "current-concept", :action-property "value2"}]}]}
                        :pick-box-2
                                           {:type "sequence-data"
                                            :data [{:type "copy-variable" :var-name "selected-concept" :from "box2"}
                                                   {:id "clear-instruction" :type "action"}
                                                   {:type "action" :id "dialog-6-picked"}
                                                   {:id "clear-instruction" :type "action"}
                                                   {:fail     "pick-wrong",
                                                    :type     "test-value",
                                                    :success
                                                              {:data [{:type "set-variable", :var-name "current-target", :var-value "box2"} {:id "pick-correct", :type "action"}],
                                                               :type "sequence-data"},
                                                    :from-var [{:var-name "box2", :action-property "value1"} {:var-name "current-concept", :action-property "value2"}]}]}
                        :pick-box-3
                                           {:type "sequence-data"
                                            :data [{:type "copy-variable" :var-name "selected-concept" :from "box3"}
                                                   {:id "clear-instruction" :type "action"}
                                                   {:type "action" :id "dialog-6-picked"}
                                                   {:id "clear-instruction" :type "action"}
                                                   {:fail     "pick-wrong",
                                                    :type     "test-value",
                                                    :success
                                                              {:data [{:type "set-variable", :var-name "current-target", :var-value "box3"} {:id "pick-correct", :type "action"}],
                                                               :type "sequence-data"},
                                                    :from-var [{:var-name "box3", :action-property "value1"} {:var-name "current-concept", :action-property "value2"}]}]}
                        :pick-correct
                                           {:type "sequence-data",
                                            :data
                                                  [{:id "dialog-3-correct", :type "action"}
                                                   {:id "slide-current-target", :type "action"}
                                                   {:id "reset-boxes", :type "action"}
                                                   {:type "empty", :duration 1000}
                                                   {:id "renew-current-concept", :type "action"}]},
                        :pick-wrong
                                           {:type "sequence-data",
                                            :data
                                                  [{:id "dialog-5-wrong", :type "action"}
                                                   {:type "empty", :duration 1000}
                                                   {:type "remove-flow-tag" :tag "clickable"}
                                                   {:id "dialog-2-riddle" :type "action"}]},
                        :renew-current-concept
                                           {:type "sequence-data",
                                            :data
                                                  [{:from        ["item-1" "item-2" "item-3"],
                                                    :type        "vars-var-provider",
                                                    :on-end      "finish-activity",
                                                    :shuffled    true,
                                                    :variables   ["current-concept"],
                                                    :provider-id "current-concept"}
                                                   {:id "dialog-5-try-again", :type "action"}
                                                   {:from      ["item-1" "item-2" "item-3"],
                                                    :type      "vars-var-provider",
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
                                                    :type "sequence-data"}
                                                   {:type "remove-flow-tag" :tag "clickable"}
                                                   {:id "dialog-2-riddle" :type "action"}]},
                        :renew-words
                                           {:type        "lesson-var-provider",
                                            :from        "concepts-group",
                                            :provider-id "words-set",
                                            :shuffled    true,
                                            :variables   ["item-1" "item-2" "item-3"]},
                        :reset-boxes
                                           {:type "sequence-data",
                                            :data
                                                  [{:id "hidden", :type "state", :target "box1"}
                                                   {:id "hidden", :type "state", :target "box2"}
                                                   {:id "hidden", :type "state", :target "box3"}
                                                   {:type "empty", :duration 100}
                                                   {:id "init-position", :type "state", :target "box1"}
                                                   {:id "init-position", :type "state", :target "box2"}
                                                   {:id "init-position", :type "state", :target "box3"}
                                                   {:skin "qwestion", :type "set-skin", :target "box1"}
                                                   {:skin "qwestion", :type "set-skin", :target "box2"}
                                                   {:skin "qwestion", :type "set-skin", :target "box3"}
                                                   {:id "visible", :type "state", :target "box1"}
                                                   {:id "visible", :type "state", :target "box2"}
                                                   {:id "visible", :type "state", :target "box3"}]},
                        :reset-current-target
                                           {:type "sequence-data",
                                            :data
                                                  [{:id "hidden", :type "state", :from-var [{:var-name "current-target", :action-property "target"}]}
                                                   {:type "empty", :duration 100}
                                                   {:id "init-position", :type "state", :from-var [{:var-name "current-target", :action-property "target"}]}
                                                   {:type "empty", :duration 100}
                                                   {:id "visible", :type "state", :from-var [{:var-name "current-target", :action-property "target"}]}]},
                        :slide-current-target
                                           {:type "sequence-data",
                                            :data
                                                  [{:to       {:ease [0.1 0.1], :bezier [{:x 770, :y 90} {:x 865, :y 460}], :duration 1.0},
                                                    :type     "transition",
                                                    :from-var [{:var-name "current-target", :action-property "transition-id"}]}
                                                   {:to       {:ease [0.1 0.1], :bezier [{:x 930, :y 560} {:x 795, :y 775} {:x 975, :y 920}], :duration 1.5},
                                                    :type     "transition",
                                                    :from-var [{:var-name "current-target", :action-property "transition-id"}]}
                                                   {:type "empty", :duration 1000}]},
                        :start-scene
                                           {:type "sequence",
                                            :tags ["clickable"]
                                            :data
                                                  ["start-activity" "renew-words" "dialog-1-welcome" "renew-current-concept"]},
                        :start-activity    {:type "start-activity"},
                        :stop-activity     {:type "stop-activity"}},
        :triggers      {:stop {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata      {:autostart true}})

(defn f
  [t args]
  t)

(core/register-template
  (:id m)
  m
  (partial f t))

