(ns webchange.templates.library.see-saw
    (:require
      [webchange.templates.core :as core]))

(def m {:id          6
        :name        "See saw"
        :description "Some description of see saw mechanics and covered skills"
        :lesson-sets ["concepts"]
        :fields      [{:name "image-src",
                       :type "image"}]})


(def t {:assets
                       [{:url "/raw/img/park/see-saw/background.jpg", :size 10, :type "image"}
                        {:url "/raw/img/park/see-saw/saw_01.png", :size 1, :type "image"}
                        {:url "/raw/img/park/see-saw/saw_02.png", :size 1, :type "image"}
                        {:url "/raw/img/park/see-saw/saw_03.png", :size 1, :type "image"}],
        :objects
                       {:background    {:type "background", :src "/raw/img/park/see-saw/background.jpg"},
                        :box-ph
                                       {:type        "animation",
                                        :x           495,
                                        :y           175,
                                        :width       771,
                                        :height      1033,
                                        :scale       {:x 0.3, :y 0.3},
                                        :origin      {:type "center-center"},
                                        :scene-name  "box-ph",
                                        :transition  "box-ph",
                                        :anim        "idle1",
                                        :anim-offset {:x 0, :y -300},
                                        :loop        true,
                                        :name        "boxes",
                                        :skin        "default",
                                        :start       false,
                                        :states      {:default {:visible false}, :visible {:visible true}},
                                        :visible     false},
                        :box1
                                       {:type        "animation",
                                        :x           500,
                                        :y           550,
                                        :width       771,
                                        :height      1033,
                                        :scale       {:x 0.3, :y 0.3},
                                        :origin      {:type "center-center"},
                                        :scene-name  "box1",
                                        :transition  "box1",
                                        :actions
                                                     {:drag-end
                                                      {:id     "drag-box1",
                                                       :on     "drag-end",
                                                       :type   "action",
                                                       :params {:start "box-1-start", :revert "box-1-revert", :target "box1"}}},
                                        :anim        "come",
                                        :anim-offset {:x 0, :y -300},
                                        :draggable   true,
                                        :loop        false,
                                        :name        "boxes",
                                        :start       true,
                                        :states      {:come {:x 500, :y 550, :visible true}, :default {:visible false}},
                                        :visible     false},
                        :box2
                                       {:type        "animation",
                                        :x           1000,
                                        :y           550,
                                        :width       771,
                                        :height      1033,
                                        :scale       {:x 0.3, :y 0.3},
                                        :origin      {:type "center-center"},
                                        :scene-name  "box2",
                                        :transition  "box2",
                                        :actions
                                                     {:drag-end
                                                      {:id     "drag-box2",
                                                       :on     "drag-end",
                                                       :type   "action",
                                                       :params {:start "box-2-start", :revert "box-2-revert", :target "box2"}}},
                                        :anim        "come",
                                        :anim-offset {:x 0, :y -300},
                                        :draggable   true,
                                        :loop        false,
                                        :name        "boxes",
                                        :start       true,
                                        :states      {:come {:x 1000, :y 550, :visible true}, :default {:visible false}},
                                        :visible     false},
                        :box3
                                       {:type        "animation",
                                        :x           1500,
                                        :y           550,
                                        :width       771,
                                        :height      1033,
                                        :scale       {:x 0.3, :y 0.3},
                                        :origin      {:type "center-center"},
                                        :scene-name  "box3",
                                        :transition  "box3",
                                        :actions
                                                     {:drag-end
                                                      {:id     "drag-box3",
                                                       :on     "drag-end",
                                                       :type   "action",
                                                       :params {:start "box-3-start", :revert "box-3-revert", :target "box3"}}},
                                        :anim        "come",
                                        :anim-offset {:x 0, :y -300},
                                        :draggable   true,
                                        :loop        false,
                                        :name        "boxes",
                                        :start       true,
                                        :states      {:come {:x 1500, :y 550, :visible true}, :default {:visible false}},
                                        :visible     false},
                        :mari
                                       {:type       "animation",
                                        :x          1535,
                                        :y          615,
                                        :width      473,
                                        :height     511,
                                        :scene-name "mari",
                                        :transition "mari",
                                        :anim       "idle",
                                        :name       "mari",
                                        :scale-x    0.5,
                                        :scale-y    0.5,
                                        :speed      0.35,
                                        :start      true},
                        :see-saw       {:type "group", :x 694, :y 716, :children ["see-saw-3" "see-saw-plank" "see-saw-2"]},
                        :see-saw-1
                                       {:type "image", :width 889, :height 106, :origin {:type "center-center"}, :src "/raw/img/park/see-saw/saw_01.png"},
                        :see-saw-2
                                       {:type   "image",
                                        :x      0,
                                        :y      67,
                                        :width  195,
                                        :height 175,
                                        :origin {:type "center-center"},
                                        :src    "/raw/img/park/see-saw/saw_02.png"},
                        :see-saw-3
                                       {:type   "image",
                                        :x      -10,
                                        :y      48,
                                        :width  195,
                                        :height 179,
                                        :origin {:type "center-center"},
                                        :src    "/raw/img/park/see-saw/saw_03.png"},
                        :see-saw-plank {:type "group", :transition "see-saw-plank", :children ["see-saw-1" "box-ph" "vera"], :rotation -13},
                        :vera
                                       {:type       "animation",
                                        :x          -380,
                                        :y          61,
                                        :width      727,
                                        :height     1091,
                                        :scale      {:x 0.4, :y 0.4},
                                        :scene-name "vera",
                                        :anim       "swing",
                                        :meshes     true,
                                        :name       "vera-90",
                                        :skin       "default",
                                        :speed      0.3,
                                        :start      false}},
        :scene-objects [["background"] ["see-saw" "mari" "box1" "box2" "box3"]],
        :actions
                       {:audio-wrong             {:type "audio", :id "fw-try-again", :start 0.892, :duration 1.869, :offset 0.2},
                        :box-1-revert            {:type "transition", :to {:x 500, :y 550, :duration 2}, :transition-id "box1"},
                        :box-1-start
                                                 {:type "sequence",
                                                  :data
                                                        ["disable-drag"
                                                         "show-box-1-ph"
                                                         "introduce-concept"
                                                         "hide-box-1-ph"
                                                         "set-current-box2"
                                                         "try-another"
                                                         "mari-box-2"
                                                         "mari-move-to-start"
                                                         "enable-drag"]},
                        :box-2-revert            {:type "transition", :to {:x 1000, :y 550, :duration 2}, :transition-id "box2"},
                        :box-2-start
                                                 {:type "sequence",
                                                  :data
                                                        ["disable-drag"
                                                         "show-box-2-ph"
                                                         "introduce-concept"
                                                         "hide-box-2-ph"
                                                         "set-current-box3"
                                                         "try-another"
                                                         "mari-box-3"
                                                         "mari-move-to-start"
                                                         "enable-drag"]},
                        :box-3-revert            {:type "transition", :to {:x 1500, :y 550, :duration 2}, :transition-id "box3"},
                        :box-3-start
                                                 {:type "sequence",
                                                  :data ["disable-drag" "show-box-3-ph" "introduce-concept" "hide-box-3-ph" "mari-finish" "finish-activity"]},
                        :check-box1              {:type "test-var-scalar", :var-name "box1", :value true, :success "box-1-start", :fail "box-1-revert"},
                        :check-box2              {:type "test-var-scalar", :var-name "box2", :value true, :success "box-2-start", :fail "box-2-revert"},
                        :check-box3              {:type "test-var-scalar", :var-name "box3", :value true, :success "box-3-start", :fail "box-3-revert"},
                        :clear-instruction       {:type "remove-flows", :flow-tag "instruction"},
                        :disable-drag            {:type "set-variable", :var-name "draggable", :var-value false},
                        :drag-box1
                                                 {:type         "test-transitions-collide",
                                                  :success      "check-box1",
                                                  :fail         "box-1-revert",
                                                  :transition-1 "box1",
                                                  :transition-2 "box-ph"},
                        :drag-box2
                                                 {:type         "test-transitions-collide",
                                                  :success      "check-box2",
                                                  :fail         "box-2-revert",
                                                  :transition-1 "box2",
                                                  :transition-2 "box-ph"},
                        :drag-box3
                                                 {:type         "test-transitions-collide",
                                                  :success      "check-box3",
                                                  :fail         "box-3-revert",
                                                  :transition-1 "box3",
                                                  :transition-2 "box-ph"},
                        :enable-drag             {:type "set-variable", :var-name "draggable", :var-value true},
                        :finish-activity         {:type "finish-activity", :id "see-saw"},
                        :go-down
                                                 {:type "parallel",
                                                  :data
                                                        [{:to {:loop false, :duration 5, :rotation 13}, :type "transition", :transition-id "see-saw-plank"}]},
                        :go-up
                                                 {:type "parallel",
                                                  :data
                                                        [{:to {:loop false, :duration 5, :rotation -13}, :type "transition", :transition-id "see-saw-plank"}]},
                        :hide-box-1-ph
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:id "default", :type "state", :target "box-ph"}
                                                         {:id "come", :type "state", :target "box1"}
                                                         {:type "empty", :duration 500}
                                                         {:type       "set-slot",
                                                          :target     "box1",
                                                          :from-var   [{:var-name "item-1", :var-property "image-src", :action-property "image"}],
                                                          :slot-name  "box1",
                                                          :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                         {:id "idle_fly1", :loop true, :type "add-animation", :target "box1"}]},
                        :hide-box-2-ph
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:id "default", :type "state", :target "box-ph"}
                                                         {:id "come", :type "state", :target "box2"}
                                                         {:type "empty", :duration 500}
                                                         {:type       "set-slot",
                                                          :target     "box2",
                                                          :from-var   [{:var-name "item-2", :var-property "image-src", :action-property "image"}],
                                                          :slot-name  "box1",
                                                          :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                         {:id "idle_fly2", :loop true, :type "add-animation", :target "box2"}]},
                        :hide-box-3-ph
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:id "default", :type "state", :target "box-ph"}
                                                         {:id "come", :type "state", :target "box3"}
                                                         {:type "empty", :duration 500}
                                                         {:type       "set-slot",
                                                          :target     "box3",
                                                          :from-var   [{:var-name "item-3", :var-property "image-src", :action-property "image"}],
                                                          :slot-name  "box1",
                                                          :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                         {:id "idle_fly3", :loop true, :type "add-animation", :target "box3"}]},
                        :introduce-concept {:type               "sequence-data",
                                            :editor-type        "dialog",
                                            :concept-var        "current-word",
                                            :available-activities ["go-down" "go-up"]
                                            :data               [
                                                                 {
                                                                  :data [
                                                                         {:type "sequence-data"
                                                                          :data [{:type "empty"
                                                                                  :duration 5000}
                                                                                 {:type "action" :id "go-down"}]},
                                                                         {:type "sequence-data"
                                                                          :data [{:type "empty" :duration 0}
                                                                                 {:type "animation-sequence", :phrase-text "New action", :audio nil}]}
                                                                         ]
                                                                  :type "parallel"
                                                                  },

                                                                 {
                                                                  :data [
                                                                         {:type "sequence-data"
                                                                          :data [{:type "empty"
                                                                                  :duration 5000}
                                                                                 {:type "action" :id "go-up"}]},
                                                                         {:type "sequence-data"
                                                                          :data [{:type "empty" :duration 0}
                                                                                 {:type "animation-sequence", :phrase-text "New action", :audio nil}]}
                                                                         ]
                                                                  :type "parallel"
                                                                  },
                                                                 {
                                                                  :data [
                                                                         {:type "sequence-data"
                                                                          :data [{:type "empty"
                                                                                  :duration 5000}
                                                                                 {:type "action" :id "go-down"}]},
                                                                         {:type "sequence-data"
                                                                          :data [{:type "empty" :duration 0}
                                                                                 {:type "animation-sequence", :phrase-text "New action", :audio nil}]}
                                                                         ]
                                                                  :type "parallel"
                                                                  },
                                                                 {
                                                                  :data [
                                                                         {:type "sequence-data"
                                                                          :data [{:type "empty"
                                                                                  :duration 5000}
                                                                                 {:type "action" :id "go-up"}]},
                                                                         {:type "sequence-data"
                                                                          :data [{:type "empty" :duration 0}
                                                                                 {:type "animation-sequence", :phrase-text "New action", :audio nil}]}
                                                                         ]
                                                                  :type "parallel"
                                                                  },
                                                                 ],
                                            :phrase             "concept-chant",
                                            :phrase-description            "Concept on see saw",
                                            },
                        :mari-box-1
                                                 {:type "transition", :skippable true, :to {:x 671, :y 350, :loop false, :duration 2}, :transition-id "mari"},
                        :mari-box-2
                                                 {:type "transition", :skippable true, :to {:x 1181, :y 350, :loop false, :duration 2}, :transition-id "mari"},
                        :mari-box-3
                                                 {:type "transition", :skippable true, :to {:x 1658, :y 350, :loop false, :duration 2}, :transition-id "mari"},
                        :mari-to-finish {:to {:x 244, :y 300, :duration 1.3}, :type "transition", :transition-id "mari"},
                        :mari-finish   {:type               "sequence-data",
                                        :editor-type        "dialog",
                                        :concept-var        "current-word",
                                        :available-activities ["mari-to-finish"]
                                        :data               [
                                                             {
                                                              :data [
                                                                     {:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "action" :id "mari-to-finish"}]},
                                                                     {:type "sequence-data"
                                                                      :data [{:type "empty" :duration 0}
                                                                             {:type "animation-sequence", :phrase-text "New action", :audio nil}]}
                                                                     ]
                                                              :type "parallel"
                                                              }
                                                             ],
                                        :phrase             "finish",
                                        :phrase-description            "Finish dialog",
                                        },


                        :mari-init-wand          {:type "add-animation", :id "wand_idle", :target "mari", :track 2, :loop true},
                        :mari-to-start {:data
                                              [{:id "wand_hit", :type "animation", :track 2, :target "mari"}
                                               {:id "wand_idle", :loop true, :type "add-animation", :track 2, :target "mari"}],
                                        :type "sequence-data"}
                        :mari-move-to-start {:type               "sequence-data",
                                             :editor-type        "dialog",
                                             :concept-var        "current-word",
                                             :available-activities ["mari-to-start"]
                                             :data               [
                                                                  {
                                                                   :data [
                                                                          {:type "sequence-data"
                                                                           :data [{:type "empty" :duration 0}
                                                                                  {:type "action" :id "mari-to-start"}]},
                                                                          {:type "sequence-data"
                                                                           :data [{:type "empty" :duration 0}
                                                                                  {:type "animation-sequence", :phrase-text "New action", :audio nil}]}
                                                                          ]
                                                                   :type "parallel"
                                                                   }
                                                                  ],
                                             :phrase                        "move-box-to-start",
                                             :phrase-description            "Move box to see saw",
                                             },
                        :mari-welcome-audio  {:type               "sequence-data",
                                              :editor-type        "dialog",
                                              :concept-var        "current-word",
                                              :data               [{:type "sequence-data"
                                                                    :data [{:type "empty" :duration 0}
                                                                           {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                              :phrase                 "welcome",
                                              :phrase-description            "Welcome instructions",
                                              },
                        :pick-wrong              {:type "sequence", :data ["audio-wrong"]},
                        :renew-words
                                                 {:type "lesson-var-provider", :from "concepts", :provider-id "words-set", :variables ["item-1" "item-2" "item-3"]},
                        :set-current-box1        {:type "set-variable", :var-name "box1", :var-value true},
                        :set-current-box2        {:type "set-variable", :var-name "box2", :var-value true},
                        :set-current-box3        {:type "set-variable", :var-name "box3", :var-value true},
                        :show-box-1-ph
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:id "default", :type "state", :target "box1"}
                                                         {:id "visible", :type "state", :target "box-ph"}
                                                         {:type "empty", :duration 500}
                                                         {:type       "set-slot",
                                                          :target     "box-ph",
                                                          :from-var   [{:var-name "item-1", :var-property "image-src", :action-property "image"}],
                                                          :slot-name  "box1",
                                                          :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                         {:from "item-1", :type "copy-variable", :var-name "current-word"}]},
                        :show-box-2-ph
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:id "default", :type "state", :target "box2"}
                                                         {:id "visible", :type "state", :target "box-ph"}
                                                         {:type "empty", :duration 500}
                                                         {:type       "set-slot",
                                                          :target     "box-ph",
                                                          :from-var   [{:var-name "item-2", :var-property "image-src", :action-property "image"}],
                                                          :slot-name  "box1",
                                                          :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                         {:from "item-2", :type "copy-variable", :var-name "current-word"}]},
                        :show-box-3-ph
                                                 {:type "sequence-data",
                                                  :data
                                                        [{:id "default", :type "state", :target "box3"}
                                                         {:id "visible", :type "state", :target "box-ph"}
                                                         {:type "empty", :duration 500}
                                                         {:type       "set-slot",
                                                          :target     "box-ph",
                                                          :from-var   [{:var-name "item-3", :var-property "image-src", :action-property "image"}],
                                                          :slot-name  "box1",
                                                          :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                         {:from "item-3", :type "copy-variable", :var-name "current-word"}]},
                        :show-boxes
                                                 {:type "parallel",
                                                  :data
                                                        [{:id "come", :type "state", :target "box1"}
                                                         {:id "come", :type "state", :target "box2"}
                                                         {:id "come", :type "state", :target "box3"}]},
                        :start-activity          {:type "start-activity", :id "see-saw"},
                        :start-scene
                                                 {:type "sequence",
                                                  :data
                                                        ["start-activity"
                                                         "clear-instruction"
                                                         "renew-words"
                                                         "disable-drag"
                                                         "mari-welcome-audio"
                                                         "mari-init-wand"
                                                         "set-current-box1"
                                                         "show-boxes"
                                                         "wait-for-box-animations"
                                                         "switch-box-animations-idle"
                                                         "mari-box-1"
                                                         "mari-move-to-start"
                                                         "enable-drag"]},
                        :stop-activity           {:type "stop-activity", :id "see-saw"},
                        :switch-box-animations-idle
                                                 {:type "parallel",
                                                  :data
                                                        [{:type       "set-slot",
                                                          :target     "box1",
                                                          :from-var   [{:var-name "item-1", :var-property "image-src", :action-property "image"}],
                                                          :slot-name  "box1",
                                                          :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                         {:type       "set-slot",
                                                          :target     "box2",
                                                          :from-var   [{:var-name "item-2", :var-property "image-src", :action-property "image"}],
                                                          :slot-name  "box1",
                                                          :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                         {:type       "set-slot",
                                                          :target     "box3",
                                                          :from-var   [{:var-name "item-3", :var-property "image-src", :action-property "image"}],
                                                          :slot-name  "box1",
                                                          :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                         {:id "idle_fly1", :loop true, :type "add-animation", :target "box1"}
                                                         {:id "idle_fly2", :loop true, :type "add-animation", :target "box2"}
                                                         {:id "idle_fly3", :loop true, :type "add-animation", :target "box3"}]},
                        :try-another  {:type               "sequence-data",
                                       :editor-type        "dialog",
                                       :concept-var        "current-word",
                                       :data               [{:type "sequence-data"
                                                             :data [{:type "empty" :duration 0}
                                                                    {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                       :phrase                        "try-another",
                                       :phrase-description-translated "Mari Transition to next photo",
                                       },
                        :wait-for-box-animations {:type "empty", :duration 500}},
        :triggers      {:back {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata      {:prev "park", :autostart true},
        }
  )

(defn f
      [t args]
      t)

(core/register-template
  (:id m)
  m
  (partial f t))