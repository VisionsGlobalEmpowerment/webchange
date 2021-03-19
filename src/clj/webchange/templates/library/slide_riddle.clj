(ns webchange.templates.library.slide-riddle
  (:require
    [webchange.templates.core :as core]))

(def m {:id          37
        :name        "slide riddle"
        :tags        ["listening comprehension" "rhyming"]
        :description "Slide"
        :lesson-sets ["concepts-slide-riddle", "concepts-all"]
        :fields      [{:name "image-src",
                       :type "image"}
                      {:name "concept-name",
                       :type "string"}]})

(def t {:assets
                       [{:url "/raw/img/park/slide/background2.jpg", :type "image"}
                        {:url "/raw/img/park/slide/slide.png", :type "image"}
                        {:url "/raw/img/park/slide/side.png", :type "image"}
                        {:url "/raw/img/alliteration/spot.png", :size 10, :type "image"}
                        ],
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
                                     :anim       "idle2",
                                     :loop       true,
                                     :name       "boxes",
                                     :skin       "qwestion",
                                     :speed      0.3,
                                     :start      true,
                                     :draggable  true,
                                     :actions    {:click
                                                              {:on     "click",
                                                               :type   "action",
                                                               :id     "box-clicked",
                                                               :params {:target "box1"}},
                                                  :drag-end   {
                                                               :on     "drag-end",
                                                               :type   "action",
                                                               :id     "stop-drag",
                                                               :params {:box           "box1"
                                                                        :init-position {:x 810, :y 216 :duration 1}}
                                                               },
                                                  :drag-start {:id "start-drag", :on "drag-start", :type "action"}},
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
                                     :anim       "idle2",
                                     :loop       true,
                                     :name       "boxes",
                                     :skin       "qwestion",
                                     :speed      0.3,
                                     :start      true,
                                     :draggable  true,
                                     :actions    {:click
                                                              {:on     "click",
                                                               :type   "action",
                                                               :id     "box-clicked",
                                                               :params {:target "box2"}},
                                                  :drag-end   {:on     "drag-end",
                                                               :type   "action",
                                                               :id     "stop-drag",
                                                               :params {:box           "box2"
                                                                        :init-position {:x 500, :y 287 :duration 1}}},
                                                  :drag-start {:id "start-drag", :on "drag-start", :type "action"}},
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
                                     :anim       "idle2",
                                     :loop       true,
                                     :name       "boxes",
                                     :skin       "qwestion",
                                     :speed      0.3,
                                     :start      true,
                                     :draggable  true,
                                     :actions    {:click
                                                              {:on     "click",
                                                               :type   "action",
                                                               :id     "box-clicked",
                                                               :params {:target "box3"}},
                                                  :drag-end   {
                                                               :on     "drag-end",
                                                               :type   "action",
                                                               :id     "stop-drag",
                                                               :params {:box           "box3"
                                                                        :init-position {:x 655, :y 212 :duration 1}}
                                                               },
                                                  :drag-start {:id "start-drag", :on "drag-start", :type "action"}},
                                     :states     {:hidden {:visible false}, :visible {:visible true}, :init-position {:x 655, :y 212}}},
                        :spot
                                    {:type       "image",
                                     :x          586,
                                     :y          354,
                                     :scale      0.6,
                                     :transition "spot",
                                     :src        "/raw/img/alliteration/spot.png",
                                     :states     {:highlighted {:highlight true}, :not-highlighted {:highlight false}}},
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
        :scene-objects [["background"] ["slide" "slide-side" "spot" "mari" "box1" "box3" "box2"]],
        :actions       {
                        :start-drag           {:type "sequence-data",
                                               :data [{:id       "check-collide-2",
                                                       :type     "set-interval",
                                                       :action   "check-collide",
                                                       :interval 100}]},
                        :highlight            {:type "sequence-data",
                                               :data
                                                     [{:type "set-variable", :var-name "spot-selected", :var-value true}
                                                      {:id "highlighted", :type "state", :target "spot"}]}
                        :unhighlight          {:type "sequence-data",
                                               :data
                                                     [{:type "set-variable", :var-name "spot-selected", :var-value false}
                                                      {:id "not-highlighted", :type "state", :target "spot"}]},
                        :check-collide        {:type "sequence-data",
                                               :data
                                                     [{:fail        "unhighlight",
                                                       :type        "test-transitions-and-pointer-collide",
                                                       :success     "highlight",
                                                       :transitions ["spot"]}
                                                      ]},
                        :stop-drag            {:type "sequence-data",
                                               :data [{:id "check-collide-2", :type "remove-interval"}
                                                      {:type     "test-var-scalar",
                                                       :value    true,
                                                       :success  "spot-selected",
                                                       :fail     "revert-position"
                                                       :var-name "spot-selected"}
                                                      {:id "unhighlight", :type "action", :params {:transition "spot"}}
                                                      ]},
                        :revert-position      {:type        "transition"
                                               :from-params [{:param-property "box", :action-property "transition-id"}
                                                             {:param-property "init-position", :action-property "to"}]}
                        :pick-wrong           {:type "sequence-data",
                                               :data [{:type "action"  :id "dialog-wrong"}
                                                      {:type "action" :id "revert-position"}]},
                        :pick-correct         {:type "sequence-data",
                                               :data
                                                     [{:id "dialog-correct", :type "action"}
                                                      {:id "slide-current-target", :type "action"}
                                                      {:type "empty", :duration 1000}
                                                      {:id "next-round", :type "action"}]},
                        :spot-selected        {:type "sequence-data",
                                               :data
                                                     [{:fail        "pick-wrong",
                                                       :type        "test-var-scalar",
                                                       :success     "pick-correct",
                                                       :from-var    [{:var-name "current-concept", :action-property "value"}],
                                                       :from-params [{:param-property "box", :action-property "var-name"}]}
                                                      {:id "unhighlight", :type "action", :params {:transition "spot"}}
                                                      ]},
                        :dialog-welcome       {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase             "welcome",
                                               :phrase-description "Welcome dialog",
                                               :dialog-track       "1 Welcome"},
                        :round-riddle         {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :concept-var        "current-concept",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase             "round-riddle",
                                               :phrase-description "Round riddle",
                                               },

                        :box-clicked          {:type "sequence-data",
                                               :data [
                                                      {:type "action" :id "next-counter"}
                                                      {:type        "copy-variable"
                                                       :var-name    "clicked-item"
                                                       :from-params [{:action-property "from" :param-property "target"}]}
                                                      {:type     "test-var-scalar",
                                                       :value    false,
                                                       :success  "click-dialog",
                                                       :var-name "spot-selected"}
                                                      ]}
                        :click-dialog         {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :concept-var        "clicked-item",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase             "click-dialog",
                                               :phrase-description "click dialog",
                                               :dialog-track       "1 Welcome"},
                        :dialog-correct  {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :concept-var        "current-concept",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase             "correct",
                                               :phrase-description "Dialog correct"},
                        :dialog-wrong
                                              {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :concept-var        "current-concept",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase             "wrong",
                                               :phrase-description "Dialog wrong"},
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
                        :slide-current-target {:type "sequence-data",
                                               :data
                                                     [{:to          {:ease [0.1 0.1], :bezier [{:x 770, :y 90} {:x 865, :y 460}], :duration 1.0},
                                                       :type        "transition",
                                                       :from-params [{:param-property "box", :action-property "transition-id"}]
                                                       }
                                                      {:to          {:ease [0.1 0.1], :bezier [{:x 930, :y 560} {:x 795, :y 775} {:x 975, :y 920}], :duration 1.5},
                                                       :type        "transition",
                                                       :from-params [{:param-property "box", :action-property "transition-id"}]}
                                                      {:type "empty", :duration 1000}]},
                        :renew-words          {:type "sequence-data",
                                               :data
                                                     [
                                                      {:from        "concepts-slide-riddle",
                                                       :type        "lesson-var-provider",
                                                       :limit       1,
                                                       :on-end      "finish-activity",
                                                       :variables   ["item-1"],
                                                       :provider-id "words-set"}

                                                      {:from        "concepts-all",
                                                       :type        "lesson-var-provider",
                                                       :shuffled    true,
                                                       :from-var
                                                                    [{:var-key         "concept-name",
                                                                      :var-name        "item-1",
                                                                      :var-property    "concept-name",
                                                                      :action-property "exclude-property-values"}],
                                                       :variables   ["item-2" "item-3"],
                                                       :provider-id "words-set"}
                                                      {:from      ["item-1" "item-2" "item-3"],
                                                       :type      "vars-var-provider",
                                                       :shuffled  true,
                                                       :variables ["box1" "box2" "box3"]}
                                                      {:from "item-1", :type "copy-variable", :var-name "current-concept"}
                                                      ]},
                        :next-round           {:type "sequence-data",
                                               :data [
                                                      {:type "action" :id "renew-words"}
                                                      {:type "action" :id "reset-boxes"}
                                                      {:type "action" :id "assign-boxes"}
                                                      {:type "action" :id "round-riddle"}]}
                        :assign-boxes         {:type "sequence-data",
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
                                                       :attachment {:x 40, :scale-x 4, :scale-y 4}}]}
                        :start-timeout       {:type      "start-timeout-counter",
                                                :id        "inactive-counter",
                                                :action    "continue-try",
                                                :autostart true
                                                :interval  10000}
                        :next-counter          {:type "next-timeout-counter"
                                                :id   "inactive-counter",}
                        :continue-try {:type "sequence",
                                      :data ["start-timeout"
                                             "dialog-continue-try"]},
                        :dialog-continue-try {:type               "sequence-data",
                                              :editor-type        "dialog",
                                              :concept-var        "current-concept",
                                              :data               [{:type "sequence-data"
                                                                    :data [{:type "empty" :duration 0}
                                                                           {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                              :phrase             "dialog-continue-try",
                                              :phrase-description "Dialog continue try"},
                        :start-scene {:type "sequence",
                                               :tags ["clickable"]
                                               :data ["start-activity"
                                                      "dialog-welcome"
                                                      "start-timeout"
                                                      "next-round"
                                                      "start-timeout"
                                                      ]},
                        :finish-activity      {:type "sequence-data"
                                               :data [{:id "inactive-counter", :type "remove-interval"}
                                                      {:type "action" :id "dialog-x-finish"}
                                                      {:type "finish-activity"}]},
                        :start-activity       {:type "start-activity"},
                        :stop-activity        {:type "stop-activity"}
                        },
        :triggers      {:stop {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata      {:autostart true}})

(defn f
  [t args]
  t)

(core/register-template
  m
  (partial f t))

