(ns webchange.templates.library.writing-lesson
  (:require
    [webchange.templates.core :as core]))

(def m {:id          15
        :name        "Writing lesson"
        :tags        ["Direct Instruction - Animated Instructor"]
        :description "Writing lesson"
        :lesson-sets ["concepts-single"]
        :fields      [{:name "image-src",
                       :type "image"}
                      {:name "letter-path",
                       :type "string"}]})

(def t {:assets
                       [{:url "/raw/img/library/room2/background.jpg", :type "image"}
                        {:url "/raw/img/library/room2/canvas.png", :type "image"}],
        :objects
                       {:background {:type "background", :src "/raw/img/library/room2/background.jpg"},
                        :canvas
                                    {:type "image",
                                     :x 950,
                                     :y 83,
                                     :width 529,
                                     :height 810,
                                     :scale-x 1.2,
                                     :scale-y 1.2,
                                     :src "/raw/img/library/room2/canvas.png"},
                        :letter-image
                                    {:type "image",
                                     :x 1050,
                                     :y 260,
                                     :width 651,
                                     :height 651,
                                     :visible false,
                                     :scale {:x 0.3, :y 0.3},
                                     :origin {:type "center-center"},
                                     :states {:hidden {:visible false}, :visible {:visible true}}},
                        :letter-path
                                    {:type "animated-svg-path",
                                     :x 1070,
                                     :y 183,
                                     :width 225,
                                     :height 225,
                                     :visible false,
                                     :scene-name "letter-path",
                                     :animation "stop",
                                     :duration 5000,
                                     :line-cap "round",
                                     :path "M 0 0",
                                     :scale-x 2,
                                     :scale-y 2,
                                     :states {:hidden {:visible false}, :visible {:visible true}},
                                     :stroke "white",
                                     :stroke-width 15},
                        :mari
                                    {:type "animation",
                                     :x 1700,
                                     :y 520,
                                     :width 473,
                                     :height 511,
                                     :scene-name "mari",
                                     :transition "mari",
                                     :anim "idle",
                                     :anim-offset {:x 225.87, :y 163.82},
                                     :name "mari",
                                     :scale-x 0.5,
                                     :scale-y 0.5,
                                     :speed 0.35,
                                     :start true},
                        :picture-box
                                    {:type "animation",
                                     :x 811,
                                     :y 330,
                                     :width 771.81,
                                     :height 1033.42,
                                     :visible false,
                                     :scale {:x 0.35, :y 0.35},
                                     :origin {:type "center-center"},
                                     :scene-name "picture-box",
                                     :transition "picture-box",
                                     :anim "come",
                                     :anim-offset {:x 391.97, :y 317.31},
                                     :loop false,
                                     :name "boxes",
                                     :skin "empty",
                                     :speed 0.7,
                                     :start true,
                                     :states {:hidden {:visible false}, :visible {:visible true}}},
                        :senora-vaca
                                    {:type "animation",
                                     :x 486,
                                     :y 986,
                                     :width 351,
                                     :height 717,
                                     :scene-name "senoravaca",
                                     :anim "idle",
                                     :name "senoravaca",
                                     :scale-x 1,
                                     :scale-y 1,
                                     :skin "vaca",
                                     :speed 0.3,
                                     :start true}},
        :scene-objects [["background"] ["canvas" "picture-box" "letter-image" "letter-path" "senora-vaca" "mari"]],
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
                        :dialog-2-before-picture
                                             {:type               "sequence-data",
                                              :editor-type        "dialog",
                                              :concept-var        "current-concept",
                                              :data               [{:type "sequence-data"
                                                                    :data [{:type "empty" :duration 0}
                                                                           {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                              :phrase             "before-picture",
                                              :phrase-description "Before picture",
                                              :dialog-track       "2 Intro"},
                        :dialog-3-after-picture
                                             {:type               "sequence-data",
                                              :editor-type        "dialog",
                                              :concept-var        "current-concept",
                                              :data               [{:type "sequence-data"
                                                                    :data [{:type "empty" :duration 0}
                                                                           {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                              :phrase             "after-picture",
                                              :phrase-description "After picture",
                                              :dialog-track       "2 Intro"},
                        :dialog-4-before-letter
                                             {:type               "sequence-data",
                                              :editor-type        "dialog",
                                              :concept-var        "current-concept",
                                              :data               [{:type "sequence-data"
                                                                    :data [{:type "empty" :duration 0}
                                                                           {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                              :phrase             "before-letter",
                                              :phrase-description "Before letter",
                                              :dialog-track       "2 Intro"},
                        :dialog-5-after-letter
                                             {:type               "sequence-data",
                                              :editor-type        "dialog",
                                              :concept-var        "current-concept",
                                              :data               [{:type "sequence-data"
                                                                    :data [{:type "empty" :duration 0}
                                                                           {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                              :phrase             "after-letter",
                                              :phrase-description "After letter",
                                              :dialog-track       "2 Intro"},

                        :dialog-2-prepare-draw
                                             {:type               "sequence-data",
                                              :editor-type        "dialog",
                                              :concept-var        "current-concept",
                                              :data               [{:type "sequence-data"
                                                                    :data [{:type "empty" :duration 0}
                                                                           {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                              :phrase             "prepare-draw",
                                              :phrase-description "Prepare draw",
                                              :dialog-track       "2 Draw"},
                        :dialog-3-before-draw
                                             {:type               "sequence-data",
                                              :editor-type        "dialog",
                                              :concept-var        "current-concept",
                                              :data               [{:type "sequence-data"
                                                                    :data [{:type "empty" :duration 0}
                                                                           {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                              :phrase             "before-draw",
                                              :phrase-description "Before draw",
                                              :dialog-track       "2 Draw"},
                        :dialog-4-repeat-draw
                                             {:type               "sequence-data",
                                              :editor-type        "dialog",
                                              :concept-var        "current-concept",
                                              :data               [{:type "sequence-data"
                                                                    :data [{:type "empty" :duration 0}
                                                                           {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                              :phrase             "repeat-draw",
                                              :phrase-description "Repeat draw",
                                              :dialog-track       "2 Draw"},
                        :dialog-2-finish
                                             {:type               "sequence-data",
                                              :editor-type        "dialog",
                                              :concept-var        "current-concept",
                                              :data               [{:type "sequence-data"
                                                                    :data [{:type "empty" :duration 0}
                                                                           {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                              :phrase             "finish",
                                              :phrase-description "Finish",
                                              :dialog-track       "3 Finish"},
                        :draw-letter
                                             {:type "sequence-data",
                                              :data [{:type "action" :id "dialog-2-prepare-draw"}
                                                     {:type "transition", :to {:x 1186, :y 414, :loop false, :duration 1.5}, :transition-id "mari"},
                                                     {:type "action" :id "dialog-3-before-draw"}
                                                     {:id "letter-drawing-animation", :type "action"}
                                                     {:type "action" :id "dialog-4-repeat-draw"}
                                                     {:type "path-animation", :state "reset", :target "letter-path"}
                                                     {:type "set-attribute",
                                                      :target "letter-path",
                                                      :from-var [{:var-name "path-color-2", :action-property "attr-value"}],
                                                      :attr-name "stroke"}
                                                     {:id "letter-drawing-animation", :type "action"}]},
                        :finish-activity {:type "finish-activity"},
                        :init-letter
                                             {:type "parallel",
                                              :data
                                                    [{:type "set-attribute",
                                                      :target "letter-path",
                                                      :from-var [{:var-name "path", :action-property "attr-value"}],
                                                      :attr-name "path"}
                                                     {:type "set-attribute",
                                                      :target "letter-path",
                                                      :from-var [{:var-name "path-color-1", :action-property "attr-value"}],
                                                      :attr-name "stroke"}]},
                        :init-vars
                                             {:type "parallel",
                                              :data
                                                    [{:type "set-variable",
                                                      :from-var [{:var-name "current-concept", :var-property "letter-path", :action-property "var-value"}],
                                                      :var-name "path"}
                                                     {:type "set-variable", :var-name "path-color-1", :var-value "#eec166"}
                                                     {:type "set-variable", :var-name "path-color-2", :var-value "#6cd38a"}]},
                        :introduce-letter
                                             {:type "sequence",
                                              :data ["dialog-4-before-letter" "init-letter" "show-letter" "dialog-4-after-letter"]},
                        :introduce-picture
                                             {:type "sequence",
                                              :data ["dialog-2-before-picture" "show-current-word-picture" "dialog-3-after-picture"]},
                        :invite-user
                                             {:type "sequence",
                                              :data ["dialog-2-finish" "finish-activity"]},
                        :letter-drawing-animation
                                             {:type "sequence-data",
                                              :data
                                                    [{:id "wand_idle", :loop true, :type "add-animation", :track 2, :target "mari"}
                                                     {:to {:x 1364, :y 514, :loop false, :duration 1}, :type "transition", :transition-id "mari"}
                                                     {:data
                                                            [{:type "path-animation", :state "play", :target "letter-path"}
                                                             {:to {:path "", :scale {:x 2, :y 2}, :origin {:x 1130, :y 193}, :duration 5},
                                                              :type "transition",
                                                              :from-var [{:var-name "path", :action-property "to.path"}],
                                                              :transition-id "mari"}],
                                                      :type "parallel"}
                                                     {:to {:x 1505, :y 485, :loop false, :duration 0.7}, :type "transition", :transition-id "mari"}]},
                        :renew-concept
                                             {:type "lesson-var-provider", :from "concepts-single", :provider-id "concepts", :variables ["current-concept"]},
                        :show-current-word-picture
                                             {:type "sequence-data",
                                              :data
                                                    [{:id "visible", :type "state", :target "letter-image"}
                                                     {:type "set-attribute",
                                                      :target "letter-image",
                                                      :from-var [{:var-name "current-concept", :var-property "letter-src", :action-property "attr-value"}],
                                                      :attr-name "src"}
                                                     {:id "visible", :type "state", :target "picture-box"}
                                                     {:type "empty", :duration 500}
                                                     {:type "set-slot",
                                                      :target "picture-box",
                                                      :from-var [{:var-name "current-concept", :var-property "image-src", :action-property "image"}],
                                                      :slot-name "box1",
                                                      :attachment {:x 40, :scale-x 4, :scale-y 4}}
                                                     {:id "idle_fly1", :loop true, :type "add-animation", :target "picture-box"}]},
                        :show-letter {:type "state", :id "visible", :target "letter-path"},
                        :start-scene
                                             {:type "sequence",
                                              :data
                                                    ["start-activity"
                                                     "renew-concept"
                                                     "init-vars"
                                                     "dialog-1-welcome"
                                                     "introduce-picture"
                                                     "introduce-letter"
                                                     "draw-letter"
                                                     "invite-user"]},
                        :start-activity {:type "start-activity"},
                        :start-letter-path {:type "set-attribute", :target "letter-path", :attr-name "animation", :attr-value "play"},
                        :stop-activity {:type "stop-activity"}},
        :triggers {:stop {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata {:autostart true}})

(defn f
  [t args]
  t)

(core/register-template
  m
  (partial f t))

