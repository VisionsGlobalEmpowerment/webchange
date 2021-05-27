(ns webchange.templates.library.alliteration
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.utils.dialog :as dialog]))

(def m {:id          31
        :name        "Alliteration"
        :tags        ["Alliteration"]
        :description "Some description of Alliteration mechanics and covered skills"
        :lesson-sets ["concepts" "concepts-incorrect"]
        :fields      [{:name "alliteration-target-image-1",
                       :type "image"}
                      {:name "alliteration-target-image-2",
                       :type "image"}
                      {:name "alliteration-target-image-3",
                       :type "image"}
                      {:name "alliteration-correct-image-1",
                       :type "image"}
                      {:name "alliteration-correct-image-2",
                       :type "image"}
                      {:name "alliteration-correct-image-3",
                       :type "image"}
                      {:name "letter",
                       :type "string"}]})

(def t {:assets        [{:url "/raw/img/park/sandbox/Background.png", :size 10, :type "image"}
                        {:url "/raw/img/park/sandbox/Surface", :size 10, :type "image"}
                        {:url "/raw/img/park/sandbox/Decoration.png", :size 10, :type "image"}
                        {:url "/raw/img/park/sandbox/Skirting.png", :size 1 :type "image"}
                        {:url "/raw/img/park/sandbox/Sandbox.png", :size 1, :type "image"}
                        {:url "/raw/img/park/sandbox/Easel.png", :size 1, :type "image"}
                        {:url "/raw/img/park/sandbox/Shadow.png", :size 1, :type "image"}],
        :objects
                       {:background {:type       "layered-background"
                                     :background {:src "/raw/img/park/sandbox/Background.png"}
                                     :decoration {:src "/raw/img/park/sandbox/Surface.png"}
                                     :surface    {:src "/raw/img/park/sandbox/Decoration.png"}},
                        :mari       {:type       "animation"
                                     :x          1600
                                     :y          520
                                     :width      473
                                     :height     511
                                     :scene-name "mari"
                                     :transition "mari"
                                     :anim       "idle"
                                     :loop       true
                                     :name       "mari"
                                     :editable?  true
                                     :scale-x    0.5
                                     :scale-y    0.5
                                     :speed      0.35
                                     :start      true
                                     :states     {:left {:scale-x 1} :right {:scale-x -1}}}
                        :box1
                                    {:type        "animation",
                                     :x           304,
                                     :y           826,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.25, :y 0.25},
                                     :scene-name  "box1",
                                     :transition  "box1",
                                     :actions     {:click      {:on "click", :type "action", :params {:box "box1"}, :id "box-clicked"},
                                                   :drag-end   {:id     "stop-drag", :on "drag-end", :type "action",
                                                                :params {:box           "box1"
                                                                         :init-position {:x 304, :y 826 :duration 1}}},
                                                   :drag-start {:id "start-drag", :on "drag-start", :type "action" :params {:box "box1"}}},
                                     :anim        "idle2",
                                     :anim-offset {:x 0, :y -300},
                                     :draggable   true,
                                     :loop        true,
                                     :name        "boxes",
                                     :skin        "qwestion",
                                     :speed       0.3,
                                     :start       false,
                                     :filters     [{:name "brightness" :value 0}
                                                   {:name "glow" :outer-strength 0 :color 0xffd700}]
                                     :states      {:init-position {:x 304, :y 826}
                                                   :highlighted   {:highlight true}, :not-highlighted {:highlight false}}
                                     },
                        :box2
                                    {:type        "animation",
                                     :x           411,
                                     :y           659,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.25, :y 0.25},
                                     :scene-name  "box2",
                                     :transition  "box2",
                                     :actions     {:click      {:on "click", :type "action", :params {:box "box2"}, :id "box-clicked"},
                                                   :drag-end   {:id     "stop-drag", :on "drag-end", :type "action",
                                                                :params {:box           "box2"
                                                                         :init-position {:x 411, :y 659 :duration 1}}},
                                                   :drag-start {:id "start-drag", :on "drag-start", :type "action" :params {:box "box2"}}},
                                     :anim        "idle2",
                                     :anim-offset {:x 0, :y -300},
                                     :draggable   true,
                                     :loop        true,
                                     :name        "boxes",
                                     :skin        "qwestion",
                                     :speed       0.3,
                                     :start       false,
                                     :filters     [{:name "brightness" :value 0}
                                                   {:name "glow" :outer-strength 0 :color 0xffd700}]
                                     :states      {:init-position {:x 401, :y 696}
                                                   :highlighted   {:highlight true}, :not-highlighted {:highlight false}}
                                     }
                        :box3
                                    {:type        "animation",
                                     :x           700,
                                     :y           654,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.25, :y 0.25},
                                     :scene-name  "box3",
                                     :transition  "box3",
                                     :actions     {:click      {:on "click", :type "action", :params {:box "box3"}, :id "box-clicked"},
                                                   :drag-end   {:id     "stop-drag", :on "drag-end", :type "action",
                                                                :params {:box           "box3"
                                                                         :init-position {:x 700, :y 654 :duration 1}}},
                                                   :drag-start {:id "start-drag", :on "drag-start", :type "action" :params {:box "box3"}}},
                                     :anim        "idle2",
                                     :anim-offset {:x 0, :y -300},
                                     :draggable   true,
                                     :loop        true,
                                     :name        "boxes",
                                     :skin        "qwestion",
                                     :speed       0.3,
                                     :start       false,
                                     :filters     [{:name "brightness" :value 0}
                                                   {:name "glow" :outer-strength 0 :color 0xffd700}]
                                     :states      {:init-position {:x 600, :y 674}
                                                   :highlighted   {:highlight true}, :not-highlighted {:highlight false}}
                                     },
                        :easel      {:type "image",
                                     :x    1108,
                                     :y    462,
                                     :src  "/raw/img/park/sandbox/Easel.png"},
                        :sandbox      {:type "image",
                                     :x    219,
                                     :y    655,
                                     :src  "/raw/img/park/sandbox/Sandbox.png"}
                        :skirting      {:type "image",
                                        :x    208,
                                        :y    618,
                                        :src  "/raw/img/park/sandbox/Skirting.png"}
                        :shadow      {:type "image",
                                     :x    141,
                                     :y    743,
                                     :src  "/raw/img/park/sandbox/Shadow.png"}
                        :target-letter
                                    {:type           "text",
                                     :x              1330,
                                     :y              590,
                                     :transition     "letter",
                                     :actions        {:click {:id "letter-intro", :on "click", :type "action"}},
                                     :align          "center",
                                     :fill           "#ff8c41",
                                     :font-family    "Lexend Deca",
                                     :font-size      200,
                                     :text           "",
                                     :states         {:highlighted {:highlight true}, :not-highlighted {:highlight false}}
                                     :vertical-align "middle"}
                        :target-image
                                    {:type    "image",
                                     :x       1150,
                                     :y       570
                                     :filters [{:name "brightness" :value 0}
                                               {:name "glow" :outer-strength 0 :color 0xffd700}]
                                     :src     ""},},
        :scene-objects [["background"] ["easel" "shadow" "sandbox" "skirting" "target-letter" "target-image" "box3" "box2" "box1" "mari"]],
        :actions
                       {:start-drag             {:type "sequence-data"
                                                 :data [{:type        "copy-variable" :var-name "active-box"
                                                         :from-params [{:param-property "box", :action-property "from"}]}
                                                        {:id "check-collide-2", :type "set-interval", :action "check-collide", :interval 100}]}
                        :stop-drag              {:type "sequence-data",
                                                 :data [{:id "check-collide-2", :type "remove-interval"}
                                                        {:type "test-var-scalar", :value true, :success "spot-selected", :var-name "spot-selected"}
                                                        {:type "set-attribute", :target "easel", :attr-value false :attr-name "highlight"}]}
                        :check-collide          {:type "sequence-data",
                                                 :data [{:fail        "unhighlight",
                                                         :type        "test-transitions-and-pointer-collide"
                                                         :success     "highlight"
                                                         :transitions ["easel"]}]}
                        :spot-selected          {:type     "test-value",
                                                 :var-name "item-correct-box",
                                                 :success  "correct-box-selected",
                                                 :fail     "incorrect-box-selected",
                                                 :from-var [{:var-name "active-box" :var-property "letter" :action-property "value1"}
                                                            {:var-name "item-correct" :var-property "letter" :action-property "value2"}]}
                        :box-clicked            {:type "sequence-data"
                                                 :data [{:type        "copy-variable" :var-name "active-box"
                                                         :from-params [{:param-property "box", :action-property "from"}]}
                                                        {:type     "action"
                                                         :from-var [{:template        "word-dialog-%",
                                                                     :var-name        "round-counter",
                                                                     :action-property "id"}]}]}
                        :correct-box-selected
                                                {:type "sequence-data",
                                                 :data [{:type "parallel"
                                                         :data [{:type          "transition"
                                                                 :transition-id "target-image"
                                                                 :from          {:brightness 0 :glow 0}
                                                                 :to            {:brightness 0.1 :glow 10 :yoyo true :duration 0.5 :repeat 3}}
                                                                {:type        "transition"
                                                                 :from        {:brightness 0 :glow 0}
                                                                 :to          {:brightness 0.1 :glow 10 :yoyo true :duration 0.5 :repeat 3}
                                                                 :from-params [{:param-property "box", :action-property "transition-id"}]}
                                                                {:type "action", :id "correct-image-selected-dialog"}
                                                                {:type "action" :id "revert-position"}]}
                                                        {:fail       "new-round",
                                                         :type       "test-var-inequality",
                                                         :value      3,
                                                         :success    "finish-activity",
                                                         :var-name   "round-counter",
                                                         :inequality ">="}]}
                        :incorrect-box-selected {:type "sequence-data",
                                                 :data [{:type "action" :id "revert-position"}
                                                        {:type     "action"
                                                         :from-var [{:template        "incorrect-image-selected-dialog-%",
                                                                     :var-name        "round-counter",
                                                                     :action-property "id"}]}
                                                        {:type "action" :id "try-correct-dialog"}]}
                        :finish-activity        {:type "sequence-data",
                                                 :data [{:type "action" :id "finish-activity-dialog"}
                                                        {:type "finish-activity"}]},
                        :highlight              {:type "sequence-data",
                                                 :data [{:type "set-variable", :var-name "spot-selected", :var-value true}
                                                        {:type "set-attribute", :target "easel", :attr-value true :attr-name "highlight"}]},
                        :unhighlight
                                                {:type "sequence-data",
                                                 :data [{:type "set-variable", :var-name "spot-selected", :var-value false}
                                                        {:type "set-attribute", :target "easel", :attr-value false :attr-name "highlight"}]}

                        :revert-position        {:type        "transition"
                                                 :from-params [{:param-property "box", :action-property "transition-id"}
                                                               {:param-property "init-position", :action-property "to"}]}
                        :start                  {:type "sequence-data"
                                                 :data [{:type "start-activity"}
                                                        {:from      "concepts"
                                                         :type      "lesson-var-provider"
                                                         :variables ["item-correct"]}
                                                        {:from      "concepts-incorrect"
                                                         :type      "lesson-var-provider"
                                                         :variables ["item-incorrect-1" "item-incorrect-2"]}
                                                        {:from      ["item-correct" "item-incorrect-1" "item-incorrect-2"]
                                                         :type      "vars-var-provider"
                                                         :variables ["box1" "box2" "box3"]}
                                                        {:type "set-variable", :var-name "round-counter", :var-value 0}
                                                        {:type      "set-attribute",
                                                         :target    "target-letter",
                                                         :from-var  [{:var-name "item-correct", :var-property "letter", :action-property "attr-value"}],
                                                         :attr-name "text"}
                                                        {:type "action" :id "intro-dialog"}
                                                        {:type "action" :id "new-round"}]}
                        :new-round              {:type "sequence-data"
                                                 :data [{:type "counter", :counter-id "round-counter", :counter-action "increase"}
                                                        {:type     "set-variable",
                                                         :from-var [{:template "alliteration-target-image-%", :var-name "round-counter", :action-property "var-value"}],
                                                         :var-name "round-target-image"}
                                                        {:type     "set-variable",
                                                         :from-var [{:template "alliteration-correct-image-%", :var-name "round-counter", :action-property "var-value"}],
                                                         :var-name "round-correct-image"}
                                                        {:type      "set-attribute",
                                                         :target    "target-image",
                                                         :from-var  [{:var-name "item-correct", :var-property-from-var "round-target-image", :action-property "attr-value"}],
                                                         :attr-name "src"}
                                                        {:from      ["box1" "box2" "box3"]
                                                         :type      "vars-var-provider"
                                                         :shuffled  true
                                                         :variables ["box1" "box2" "box3"]}
                                                        {:type       "set-slot",
                                                         :from-var   [{:var-name "box1", :action-property "image", :var-property-from-var "round-correct-image"}],
                                                         :target     "box1"
                                                         :attachment {:x 40, :scale-x 4, :scale-y 4},
                                                         :slot-name  "box1"}
                                                        {:type       "set-slot",
                                                         :from-var   [{:var-name "box2", :action-property "image", :var-property-from-var "round-correct-image"}],
                                                         :target     "box2"
                                                         :attachment {:x 40, :scale-x 4, :scale-y 4},
                                                         :slot-name  "box1"}
                                                        {:type       "set-slot",
                                                         :from-var   [{:var-name "box3", :action-property "image", :var-property-from-var "round-correct-image"}],
                                                         :target     "box3"
                                                         :attachment {:x 40, :scale-x 4, :scale-y 4},
                                                         :slot-name  "box1"}
                                                        {:type     "action"
                                                         :from-var [{:template        "target-word-dialog-%",
                                                                     :var-name        "round-counter",
                                                                     :action-property "id"}]}]}
                        :stop-activity          {:type "stop-activity"},

                        :intro-dialog           (-> (dialog/default "Intro")
                                                    (assoc :concept-var "item-correct"))
                        :finish-activity-dialog (-> (dialog/default "Finish activity")
                                                    (assoc :concept-var "item-correct"))
                        :target-word-dialog-1   (-> (dialog/default "Target word Round 1")
                                                    (assoc :concept-var "item-correct"))
                        :target-word-dialog-2   (-> (dialog/default "Target word Round 2")
                                                    (assoc :concept-var "item-correct"))
                        :target-word-dialog-3   (-> (dialog/default "Target word Round 3")
                                                    (assoc :concept-var "item-correct"))
                        :correct-image-selected-dialog
                                                (-> (dialog/default "Correct image selected")
                                                    (assoc :concept-var "item-correct"))
                        :incorrect-image-selected-dialog-1
                                                (-> (dialog/default "Incorrect image selected Round 1")
                                                    (assoc :concept-var "active-box"))
                        :incorrect-image-selected-dialog-2
                                                (-> (dialog/default "Incorrect image selected Round 2")
                                                    (assoc :concept-var "active-box"))
                        :incorrect-image-selected-dialog-3
                                                (-> (dialog/default "Incorrect image selected Round 3")
                                                    (assoc :concept-var "active-box"))
                        :try-correct-dialog     (-> (dialog/default "Try corect")
                                                    (assoc :concept-var "item-correct"))
                        :word-dialog-1          (-> (dialog/default "Word clicked Round 1")
                                                    (assoc :concept-var "active-box"))
                        :word-dialog-2          (-> (dialog/default "Word clicked Round 2")
                                                    (assoc :concept-var "active-box"))
                        :word-dialog-3          (-> (dialog/default "Word clicked Round 3")
                                                    (assoc :concept-var "active-box"))},
        :triggers      {:back {:on "back", :action "stop-activity"}, :start {:on "start", :action "start"}},
        :metadata      {:prev   "park", :autostart true
                        :tracks [{:title "Intro"
                                  :nodes [{:type      "dialog"
                                           :action-id :intro-dialog}
                                          {:type "prompt"
                                           :text "Guide introduce letter and gives instruction. New round begins"}]}
                                 {:title "Round 1"
                                  :nodes [{:type      "dialog"
                                           :action-id :target-word-dialog-1}
                                          {:type "prompt"
                                           :text "Waiting for student to pick correct image"}
                                          {:type      "dialog"
                                           :action-id :correct-image-selected-dialog}
                                          {:type      "dialog"
                                           :action-id :incorrect-image-selected-dialog-1}
                                          {:type      "dialog"
                                           :action-id :try-correct-dialog}
                                          {:type "prompt"
                                           :text "On click following word will be played"}
                                          {:type      "dialog"
                                           :action-id :word-dialog-1}
                                          {:type "prompt"
                                           :text "When correctly placed to easel new round begins"}]}
                                 {:title "Round 2"
                                  :nodes [{:type      "dialog"
                                           :action-id :target-word-dialog-2}
                                          {:type "prompt"
                                           :text "Waiting for student to pick correct image"}
                                          {:type      "dialog"
                                           :action-id :correct-image-selected-dialog}
                                          {:type      "dialog"
                                           :action-id :incorrect-image-selected-dialog-2}
                                          {:type      "dialog"
                                           :action-id :try-correct-dialog}
                                          {:type "prompt"
                                           :text "On click following word will be played"}
                                          {:type      "dialog"
                                           :action-id :word-dialog-2}
                                          {:type "prompt"
                                           :text "When correctly placed to easel new round begins"}]}
                                 {:title "Round 3"
                                  :nodes [{:type      "dialog"
                                           :action-id :target-word-dialog-3}
                                          {:type "prompt"
                                           :text "Waiting for student to pick correct image"}
                                          {:type      "dialog"
                                           :action-id :correct-image-selected-dialog}
                                          {:type      "dialog"
                                           :action-id :incorrect-image-selected-dialog-3}
                                          {:type      "dialog"
                                           :action-id :try-correct-dialog}
                                          {:type "prompt"
                                           :text "On click following word will be played"}
                                          {:type      "dialog"
                                           :action-id :word-dialog-3}
                                          {:type "prompt"
                                           :text "When correctly placed to easel new round begins"}
                                          {:type "prompt"
                                           :text "After the last round activity finishes with a dialog"}
                                          {:type      "dialog"
                                           :action-id :finish-activity-dialog}]}]}})

(defn f
  [t args]
  t)

(core/register-template
  m
  (partial f t))
