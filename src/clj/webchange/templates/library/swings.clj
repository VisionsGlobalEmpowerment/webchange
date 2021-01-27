(ns webchange.templates.library.swings
    (:require
      [webchange.templates.core :as core]))

(def m {:id          8
        :name        "Swings"
        :tags        ["Direct Instruction - Animated Instructor"]
        :description "Some description of swings mechanics and covered skills"
        :lesson-sets ["concepts"]
        :fields      [{:name "image-src",
                       :type "image"}
                      ]})


(def t {:assets
                       [{:url "/raw/img/park/swings/background.jpg", :size 10, :type "image"}
                        {:url "/raw/img/park/swings/swings.png", :size 1, :type "image"}
                        {:url "/raw/img/park/swings/tree.png", :size 1, :type "image"}
                      ],
        :objects
                       {:background {:type "background", :src "/raw/img/park/swings/background.jpg"},
                        :box-ph
                                    {:type        "animation",
                                     :x           177,
                                     :y           785,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.3, :y 0.3},
                                     :origin      {:type "center-center"},
                                     :scene-name  "box-ph",
                                     :transition  "box-ph",
                                     :anim        "idle2",
                                     :anim-offset {:x 0, :y -300},
                                     :loop        true,
                                     :name        "boxes",
                                     :skin        "empty",
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
                                     :skin        "default",
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
                                                    :params {:start "box-2-start", :revert "box-2-revert", :target "box1"}}},
                                     :anim        "come",
                                     :anim-offset {:x 0, :y -300},
                                     :draggable   true,
                                     :loop        false,
                                     :name        "boxes",
                                     :skin        "default",
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
                                                    :params {:start "box-2-start", :revert "box-3-revert", :target "box1"}}},
                                     :anim        "come",
                                     :anim-offset {:x 0, :y -300},
                                     :draggable   true,
                                     :loop        false,
                                     :name        "boxes",
                                     :skin        "default",
                                     :start       true,
                                     :states      {:come {:x 1500, :y 550, :visible true}, :default {:visible false}},
                                     :visible     false},
                        :mari
                                    {:type       "animation",
                                     :x          1535,
                                     :y          715,
                                     :width      473,
                                     :height     511,
                                     :scene-name "mari",
                                     :transition "mari",
                                     :anim       "idle",
                                     :name       "mari",
                                     :scale-x    0.5,
                                     :scale-y    0.5,
                                     :start      true},
                        :rock
                                    {:type       "animation",
                                     :x          1168,
                                     :y          748,
                                     :width      591,
                                     :height     681,
                                     :scene-name "rock",
                                     :anim       "idle",
                                     :name       "rock",
                                     :scale-x    0.6,
                                     :scale-y    0.6,
                                     :skin       "female",
                                     :start      true},
                        :swings
                                    {:type       "group",
                                     :x          589,
                                     :y          160,
                                     :origin     {:type "center-center"},
                                     :transition "swings",
                                     :children   ["swings-image" "box-ph"]},
                        :swings-image
                                    {:type "image", :width 248, :height 681, :origin {:type "center-top"}, :src "/raw/img/park/swings/swings.png"},
                        :tree-image
                                    {:type   "image",
                                     :x      656,
                                     :y      0,
                                     :width  592,
                                     :height 196,
                                     :origin {:type "center-top"},
                                     :src    "/raw/img/park/swings/tree.png"},
                        :vera
                                    {:type   "animation",
                                     :x      251,
                                     :y      990,
                                     :width  1800,
                                     :height 2558,
                                     :scale  {:x 0.2, :y 0.2},
                                     :anim   "idle",
                                     :name   "vera",
                                     :start  true}},
        :scene-objects [["background"] ["swings" "tree-image" "vera" "mari" "rock" "box1" "box2" "box3"]],
        :actions
                       {:audio-wrong             {:type "audio", :id "fw-try-again", :start 0.892, :duration 1.869, :offset 0.2},
                        :box-1-revert            {:type "transition", :to {:x 500, :y 550, :duration 2}, :transition-id "box1"},
                        :box-1-start
                                                 {:type "sequence",
                                                  :data
                                                        ["show-box-1-ph"
                                                         "start-swings"
                                                         "dialog-var"
                                                         "stop-swings"
                                                         "hide-box-1-ph"
                                                         "set-current-box2"
                                                         "mari-box-2"
                                                         "try-another"]},
                        :box-2-revert            {:type "transition", :to {:x 1000, :y 550, :duration 2}, :transition-id "box2"},
                        :box-2-start
                                                 {:type "sequence",
                                                  :data
                                                        ["show-box-2-ph"
                                                         "start-swings"
                                                         "dialog-var"
                                                         "stop-swings"
                                                         "hide-box-2-ph"
                                                         "set-current-box3"
                                                         "mari-box-3"
                                                         "try-another"]},
                        :box-3-revert            {:type "transition", :to {:x 1500, :y 550, :duration 2}, :transition-id "box3"},
                        :box-3-start
                                                 {:type "sequence",
                                                  :data ["show-box-3-ph" "start-swings" "dialog-var" "stop-swings" "hide-box-3-ph" "mari-finish" "finish-activity"]},
                        :check-box1              {:type "test-var-scalar", :var-name "box1", :value true, :success "box-1-start", :fail "box-1-revert"},
                        :check-box2              {:type "test-var-scalar", :var-name "box2", :value true, :success "box-2-start", :fail "box-2-revert"},
                        :check-box3              {:type "test-var-scalar", :var-name "box3", :value true, :success "box-3-start", :fail "box-3-revert"},
                        :clear-instruction       {:type "remove-flows", :flow-tag "instruction"},
                        :dialog-var              {:type               "sequence-data",
                                                  :editor-type        "dialog",
                                                  :concept-var        "current-word",
                                                  :data               [{:type "sequence-data"
                                                                        :data [{:type "empty" :duration 0}
                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                  :phrase                        "dialog",
                                                  :phrase-description            "After start swing",
                                                  },
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
                        :finish-activity         {:type "finish-activity", :id "swings"},
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
                        :mari-box-1
                                                 {:type "transition", :skippable true, :to {:x 671, :y 350, :loop false, :duration 2}, :transition-id "mari"},
                        :mari-box-2
                                                 {:type "transition", :skippable true, :to {:x 1181, :y 350, :loop false, :duration 2}, :transition-id "mari"},
                        :mari-box-3
                                                 {:type "transition", :skippable true, :to {:x 1658, :y 350, :loop false, :duration 2}, :transition-id "mari"},
                        :mari-finish {:type               "sequence-data",
                                      :editor-type        "dialog",
                                      :concept-var        "current-word",
                                      :data               [{:type "sequence-data"
                                                            :data [{:type "empty" :duration 0}
                                                                   {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                      :phrase             "finish",
                                      :phrase-description "Mari finish",
                                      }

                        :mari-init-wand          {:type "add-animation", :id "wand_idle", :target "mari", :track 2, :loop true},
                        :mari-move-to-start
                                                 {:type               "parallel",
                                                  :data
                                                                      [{:data
                                                                              [{:id "wand_hit", :type "animation", :track 2, :target "mari"}
                                                                               {:id "wand_idle", :loop true, :type "add-animation", :track 2, :target "mari"}],
                                                                        :type "sequence-data"}
                                                                       {:type               "sequence-data",
                                                                        :editor-type        "dialog",
                                                                        :concept-var        "current-word",
                                                                        :data               [{:type "sequence-data"
                                                                                              :data [{:type "empty" :duration 0}
                                                                                                     {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                        :phrase             "Instructions",
                                                                        :phrase-description "What to do?",
                                                                        }
                                                                       ],
                                                  :skippable          true},
                        :pick-wrong              {:type "sequence", :data ["audio-wrong"]},
                        :prepare-swing           {:type "transition", :to {:loop false, :duration 1, :rotation -15}, :transition-id "swings"},
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
                        :start-activity          {:type "start-activity", :id "swings"},
                        :start-scene
                                                 {:type "sequence",
                                                  :data
                                                        ["start-activity"
                                                         "clear-instruction"
                                                         "renew-words"
                                                         "set-current-box1"
                                                         "show-boxes"
                                                         "wait-for-box-animations"
                                                         "switch-box-animations-idle"
                                                         "welcome-audio"
                                                         "mari-init-wand"
                                                         "mari-box-1"
                                                         "mari-move-to-start"]},
                        :start-swings            {:type "sequence", :data ["prepare-swing" "swing"], :tags ["swings"]},
                        :stop-activity           {:type "stop-activity", :id "swings"},
                        :stop-swings             {:type "sequence", :data ["stop-swings-1" "stop-swings-2"]},
                        :stop-swings-1           {:type "remove-flows", :flow-tag "swings"},
                        :stop-swings-2           {:type "transition", :to {:loop false, :duration 0.5, :rotation 0}, :transition-id "swings"},
                        :swing                   {:type "sequence", :data ["swing-right" "swing-left" "swing"], :return-immediately true, :tags ["swings"]},
                        :swing-left              {:type "transition", :to {:loop false, :duration 2, :rotation -15}, :transition-id "swings"},
                        :swing-right             {:type "transition", :to {:loop false, :duration 2, :rotation 15}, :transition-id "swings"},
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
                        :try-another {:type               "sequence-data",
                                      :editor-type        "dialog",
                                      :concept-var        "current-word",
                                      :data               [{:type "sequence-data"
                                                            :data [{:type "empty" :duration 0}
                                                                   {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                      :phrase                 "try-another",
                                      :phrase-description            "Try another",
                                      },

                        :wait-for-box-animations {:type "empty", :duration 500},
                        :welcome-audio {:type               "sequence-data",
                                       :editor-type        "dialog",
                                       :concept-var        "current-word",
                                       :data               [{:type "sequence-data"
                                                             :data [{:type "empty" :duration 0}
                                                                    {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                        :phrase             "welcome",
                                        :phrase-description "Welcome audio",
                                       }},
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