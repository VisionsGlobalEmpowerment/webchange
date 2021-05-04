(ns webchange.templates.library.alliteration
  (:require
    [webchange.templates.core :as core]))

(def m {:id          31
        :name        "Alliteration"
        :tags        ["Alliteration"]
        :description "Some description of Alliteration mechanics and covered skills"
        :lesson-sets ["concepts" "concepts-incorrect"]
        :fields      [
                      {:name "word-image-1",
                       :type "image"}
                      {:name "word-image-2",
                       :type "image"}
                      {:name "word-image-3",
                       :type "image"}
                      {:name "letter",
                       :type "string"}
                      ]})


(def t {:assets
                       [{:url "/raw/img/park/sandbox/background.jpg", :size 10, :type "image"}
                        {:url "/raw/img/casa/painting_canvas.png", :type "image"}
                        {:url "/raw/img/alliteration/spot.png", :size 10, :type "image"}],
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
                                     :actions
                                                  {:click
                                                               {:on     "click",
                                                                :type   "action",
                                                                :params {:box "box1"},
                                                                :from-var
                                                                        [{:template "word-for-image-%", :var-name "round-counter", :action-property "id"}
                                                                         {:var-name "box1-concept", :action-property "params.concept"}]},
                                                   :drag-end   {:id       "stop-drag",
                                                                :on       "drag-end",
                                                                :type     "action",
                                                                :params   {:box           "box1"
                                                                           :init-position {:x 304, :y 826 :duration 1}}
                                                                :from-var [{:var-name "box1-concept", :action-property "params.concept"}]},
                                                   :drag-start {:id "start-drag", :on "drag-start", :type "action"}},
                                     :anim        "idle2",
                                     :anim-offset {:x 0, :y -300},
                                     :draggable   true,
                                     :loop        true,
                                     :name        "boxes",
                                     :skin        "qwestion",
                                     :speed       0.3,
                                     :start       false,
                                     :states      {:init-position {:x 304, :y 826}
                                                   :highlighted   {:highlight true}, :not-highlighted {:highlight false}}
                                     },
                        :box2
                                    {:type        "animation",
                                     :x           401,
                                     :y           696,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.25, :y 0.25},
                                     :scene-name  "box2",
                                     :transition  "box2",
                                     :actions
                                                  {:click
                                                               {:on     "click",
                                                                :type   "action",
                                                                :params {:box "box2"},
                                                                :from-var
                                                                        [{:template "word-for-image-%", :var-name "round-counter", :action-property "id"}
                                                                         {:var-name "box2-concept", :action-property "params.concept"}]},
                                                   :drag-end   {:id       "stop-drag",
                                                                :on       "drag-end",
                                                                :type     "action",
                                                                :params   {:box           "box2"
                                                                           :init-position {:x 401, :y 696 :duration 1}}
                                                                :from-var [{:var-name "box2-concept", :action-property "params.concept"}]},
                                                   :drag-start {:id "start-drag", :on "drag-start", :type "action"}},
                                     :anim        "idle2",
                                     :anim-offset {:x 0, :y -300},
                                     :draggable   true,
                                     :loop        true,
                                     :name        "boxes",
                                     :skin        "qwestion",
                                     :speed       0.3,
                                     :start       false,
                                     :states      {:init-position {:x 401, :y 696}
                                                   :highlighted   {:highlight true}, :not-highlighted {:highlight false}}
                                     }
                        :box3
                                    {:type        "animation",
                                     :x           600,
                                     :y           674,
                                     :width       771,
                                     :height      1033,
                                     :scale       {:x 0.25, :y 0.25},
                                     :scene-name  "box3",
                                     :transition  "box3",
                                     :actions
                                                  {:click
                                                               {:on     "click",
                                                                :type   "action",
                                                                :params {:box "box3"},
                                                                :from-var
                                                                        [{:template "word-for-image-%", :var-name "round-counter", :action-property "id"}
                                                                         {:var-name "box3-concept", :action-property "params.concept"}]},
                                                   :drag-end   {:id       "stop-drag",
                                                                :on       "drag-end",
                                                                :type     "action",
                                                                :params   {
                                                                           :box           "box3"
                                                                           :init-position {:x 600, :y 674 :duration 1}}
                                                                :from-var [{:var-name "box2-concept", :action-property "params.concept"}]},
                                                   :drag-start {:id "start-drag", :on "drag-start", :type "action"}},
                                     :anim        "idle2",
                                     :anim-offset {:x 0, :y -300},
                                     :draggable   true,
                                     :loop        true,
                                     :name        "boxes",
                                     :skin        "qwestion",
                                     :speed       0.3,
                                     :start       false,
                                     :states      {:init-position {:x 600, :y 674}
                                                   :highlighted   {:highlight true}, :not-highlighted {:highlight false}}
                                     },
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
                                     :transition     "letter",
                                     :actions        {:click {:id "letter-intro", :on "click", :type "action"}},
                                     :align          "center",
                                     :fill           "#ff8c41",
                                     :font-family    "Lexend Deca",
                                     :font-size      200,
                                     :text           "",
                                     :states         {:highlighted {:highlight true}, :not-highlighted {:highlight false}}
                                     :vertical-align "middle"},
                        :spot
                                    {:type       "image",
                                     :x          786,
                                     :y          754,
                                     :width      428,
                                     :height     549,
                                     :scale      0.8,
                                     :transition "spot",
                                     :src        "/raw/img/alliteration/spot.png",
                                     :states     {:highlighted {:highlight true}, :not-highlighted {:highlight false}}}},
        :scene-objects [["background"] ["spot"] ["canvas" "box3" "box2" "box1" "letter"]],
        :actions
                       {:box-1-change-skin
                                                {:type       "set-slot",
                                                 :from-var
                                                             [{:var-name "item-correct", :action-property "image", :var-property-from-var "round-image"}
                                                              {:var-name "item-correct-box", :action-property "target"}],
                                                 :attachment {:x 40, :scale-x 4, :scale-y 4},
                                                 :slot-name  "box1"},
                        :box-2-change-skin
                                                {:type       "set-slot",
                                                 :from-var
                                                             [{:var-name "item-incorrect-1", :action-property "image", :var-property-from-var "round-image"}
                                                              {:var-name "item-incorrect-1-box", :action-property "target"}],
                                                 :attachment {:x 40, :scale-x 4, :scale-y 4},
                                                 :slot-name  "box1"},
                        :box-3-change-skin
                                                {:type       "set-slot",
                                                 :from-var
                                                             [{:var-name "item-incorrect-2", :action-property "image", :var-property-from-var "round-image"}
                                                              {:var-name "item-incorrect-2-box", :action-property "target"}],
                                                 :attachment {:x 40, :scale-x 4, :scale-y 4},
                                                 :slot-name  "box1"},
                        :check-collide
                                                {:type "sequence-data",
                                                 :data
                                                       [{:fail "unhighlight", :type "test-transitions-and-pointer-collide", :success "highlight", :transitions ["spot"]}]},

                        :correct-concept-selected
                                                {:type               "sequence-data",
                                                 :data
                                                                     [{:data [{:type "empty", :duration 0} {:type "animation-sequence", :audio nil, :phrase-text "New action"}],
                                                                       :type "sequence-data"}],
                                                 :phrase             "correct-concept-selected",
                                                 :phrase-description "Correct concept selected",
                                                 :concept-var        "item-correct",
                                                 :editor-type        "dialog"
                                                 :dialog-track       "Box selection result"
                                                 },
                        :correct-box-selected
                                                {:type "sequence-data",
                                                 :data
                                                       [
                                                        {:type "counter", :counter-id "round-counter", :counter-action "increase"}
                                                        {:type "action", :id "correct-concept-selected"}

                                                        {:id          "highlighted", :type "state",
                                                         :from-params [{:param-property "box", :action-property "target"}]}
                                                        {:id "highlighted", :type "state", :target "letter"}
                                                        {:type "empty" :duration 1000}
                                                        {:id "not-highlighted", :type "state", :target "letter"}
                                                        {:id "not-highlighted", :type "state", :from-params [{:param-property "box", :action-property "target"}]}

                                                        {:type "action" :id "revert-position"}
                                                        {:fail       "init-next-round",
                                                         :type       "test-var-inequality",
                                                         :value      4,
                                                         :success    "finish-scene",
                                                         :var-name   "round-counter",
                                                         :inequality ">="}
                                                        ]},
                        :finish-activity        {:type "finish-activity", :id "sandbox"},
                        :finish-scene           {:type "sequence-data", :data [{:id "stop-activity", :type "action"}]},
                        :highlight
                                                {:type "sequence-data",
                                                 :data
                                                       [{:type "set-variable", :var-name "spot-selected", :var-value true}
                                                        {:id "highlighted", :type "state", :from-params [{:param-property "transition", :action-property "target"}]}]},

                        :revert-position        {:type        "transition"
                                                 :from-params [{:param-property "box", :action-property "transition-id"}
                                                               {:param-property "init-position", :action-property "to"}]
                                                 }
                        :incorrect-box-selected-1-1
                                                {:type               "sequence-data",
                                                 :data
                                                                     [{:data [{:type "empty", :duration 0} {:type "animation-sequence", :audio nil, :phrase-text "New action"}],
                                                                       :type "sequence-data"}],
                                                 :phrase             "correct-concept-selected-1",
                                                 :phrase-description "For word image 1. The word ___ starts with the sound ___",
                                                 :concept-var        "current-dialog",
                                                 :editor-type        "dialog"
                                                 :dialog-track       "Box selection result"},
                        :incorrect-box-selected-1-2
                                                {:type               "sequence-data",
                                                 :data
                                                                     [{:data [{:type "empty", :duration 0} {:type "animation-sequence", :audio nil, :phrase-text "New action"}],
                                                                       :type "sequence-data"}],
                                                 :phrase             "correct-concept-selected-1",
                                                 :phrase-description "For word image 2. The word ___ starts with the sound ___",
                                                 :concept-var        "current-dialog",
                                                 :editor-type        "dialog"
                                                 :dialog-track       "Box selection result"},
                        :incorrect-box-selected-1-3
                                                {:type               "sequence-data",
                                                 :data
                                                                     [{:data [{:type "empty", :duration 0} {:type "animation-sequence", :audio nil, :phrase-text "New action"}],
                                                                       :type "sequence-data"}],
                                                 :phrase             "correct-concept-selected-1",
                                                 :phrase-description "For word image 3. The word ___ starts with the sound ___",
                                                 :concept-var        "current-dialog",
                                                 :editor-type        "dialog"
                                                 :dialog-track       "Box selection result"},
                        :incorrect-box-selected-2
                                                {:type               "sequence-data",
                                                 :data
                                                                     [{:data [{:type "empty", :duration 0} {:type "animation-sequence", :audio nil, :phrase-text "New action"}],
                                                                       :type "sequence-data"}],
                                                 :phrase             "correct-concept-selected-2",
                                                 :phrase-description "Can you find another word that starts with the sound ____?",
                                                 :concept-var        "item-correct",
                                                 :editor-type        "dialog"
                                                 :dialog-track       "Box selection result"},
                        :incorrect-box-selected {
                                                 :type "sequence-data",
                                                 :data [
                                                        {:type        "copy-variable",
                                                         :var-name    "current-dialog",
                                                         :from-params [{:param-property "concept", :action-property "from"}]}
                                                        {:type "action" :id "revert-position"}
                                                        {:type "action"
                                                         :from-var  [{:template "incorrect-box-selected-1-%",
                                                                      :var-name "round-counter",
                                                                      :action-property "id"
                                                                      }],
                                                         }
                                                        {:type "action" :id "incorrect-box-selected-2"}
                                                        {:type        "set-variable",
                                                         :var-name    "incorrect-box-selected",
                                                         :var-value   true,
                                                         :from-params [{:param-property "box", :action-property "var-value"}]}]
                                                 }
                        :init-next-round
                                                {:type "sequence-data",
                                                 :data
                                                       [{:type        "set-variable",
                                                         :var-name    "correct-box-selected-1",
                                                         :var-value   true,
                                                         :from-params [{:param-property "box", :action-property "var-value"}]}
                                                        {:id "reset-box-vars", :type "action"}
                                                        {:id "set-round-vars", :type "action"}
                                                        {:id "renew-current-concept-workflow", :type "action"}]},
                        :init-round-counter     {:type "counter", :counter-action "reset", :counter-id "round-counter", :counter-value 1},
                        :init-words
                                                {:type "sequence-data",
                                                 :data
                                                       [{:from        "concepts",
                                                         :type        "lesson-var-provider",
                                                         :shuffled    false,
                                                         :variables   ["item-correct"],
                                                         :provider-id "words-set"}
                                                        {:from        "concepts-incorrect",
                                                         :type        "lesson-var-provider",
                                                         :shuffled    false,
                                                         :variables   ["item-incorrect-1" "item-incorrect-2"],
                                                         :provider-id "words-set"}]},
                        :letter-intro
                                                {:type               "sequence-data",
                                                 :data
                                                                     [{:data [{:type "empty", :duration 0} {:type "animation-sequence", :audio nil, :phrase-text "New action"}],
                                                                       :type "sequence-data"}],
                                                 :phrase             "letter-intro",
                                                 :phrase-description "Learn concept sound",
                                                 :concept-var        "item-correct",
                                                 :editor-type        "dialog"},
                        :mari-welcome-audio
                                                {:type               "sequence-data",
                                                 :data
                                                                     [{:data [{:type "empty", :duration 0} {:type "animation-sequence", :audio nil, :phrase-text "New action"}],
                                                                       :type "sequence-data"}],
                                                 :phrase             "welcome",
                                                 :phrase-description "Welcome to the sandbox!",
                                                 :concept-var        "current-word",
                                                 :editor-type        "dialog"},
                        :renew-current-concept-workflow
                                                {:type "sequence", :data ["box-1-change-skin" "box-2-change-skin" "box-3-change-skin" "show-letter"]},
                        :reset-box-vars
                                                {:type "sequence-data",
                                                 :data
                                                       [{:type      "set-variable-list",
                                                         :values    ["box1" "box2" "box3"],
                                                         :shuffled  true,
                                                         :var-names ["item-correct-box" "item-incorrect-1-box" "item-incorrect-2-box"]}
                                                        {:type      "set-variable",
                                                         :from-var  [{:template "%-concept", :var-name "item-correct-box", :action-property "var-name"}],
                                                         :var-value "item-correct"}
                                                        {:type      "set-variable",
                                                         :from-var  [{:template "%-concept", :var-name "item-incorrect-1-box", :action-property "var-name"}],
                                                         :var-value "item-incorrect-1"}
                                                        {:type      "set-variable",
                                                         :from-var  [{:template "%-concept", :var-name "item-incorrect-2-box", :action-property "var-name"}],
                                                         :var-value "item-incorrect-2"}]},
                        :set-round-vars
                                                {:type "sequence-data",
                                                 :data
                                                       [{:type     "set-variable",
                                                         :from-var [{:template "word-image-%", :var-name "round-counter", :action-property "var-value"}],
                                                         :var-name "round-image"}]},
                        :show-letter
                                                {:type      "set-attribute",
                                                 :target    "letter",
                                                 :from-var  [{:var-name "item-correct", :var-property "letter", :action-property "attr-value"}],
                                                 :attr-name "text"},
                        :spot-selected
                                                {:type        "test-var-scalar",
                                                 :var-name    "item-correct-box",
                                                 :success     "correct-box-selected",
                                                 :fail        "incorrect-box-selected",
                                                 :from-params [{:param-property "box", :action-property "value"}]},
                        :start-activity         {:type "start-activity", :id "alliteration"},
                        :start-drag
                                                {:type "sequence-data",
                                                 :data [{:id "check-collide-2", :type "set-interval", :action "check-collide", :interval 100}]},
                        :start-scene
                                                {:type "sequence",
                                                 :data
                                                       ["start-activity"
                                                        "reset-box-vars"
                                                        "init-round-counter"
                                                        "init-words"
                                                        "set-round-vars"
                                                        "mari-welcome-audio"
                                                        "renew-current-concept-workflow"]},
                        :stop-activity          {:type "stop-activity", :id "alliteration"},
                        :stop-drag
                                                {:type "sequence-data",
                                                 :data
                                                       [{:id "check-collide-2", :type "remove-interval"}
                                                        {:type "test-var-scalar", :value true, :success "spot-selected", :var-name "spot-selected"}
                                                        {:id "unhighlight", :type "action", :params {:transition "spot"}}]},
                        :unhighlight
                                                {:type "sequence-data",
                                                 :data
                                                       [{:type "set-variable", :var-name "spot-selected", :var-value false}
                                                        {:id "not-highlighted", :type "state", :from-params [{:param-property "transition", :action-property "target"}]}]},
                        :word-for-image-1
                                                {:type "sequence-data",
                                                 :data
                                                       [{:type        "copy-variable",
                                                         :var-name    "current-dialog",
                                                         :from-params [{:param-property "concept", :action-property "from"}]}
                                                        {:id "word-for-image-dialog-1", :type "action"}]},
                        :word-for-image-2
                                                {:type "sequence-data",
                                                 :data
                                                       [{:type        "copy-variable",
                                                         :var-name    "current-dialog",
                                                         :from-params [{:param-property "concept", :action-property "from"}]}
                                                        {:id "word-for-image-dialog-2", :type "action"}]},
                        :word-for-image-3
                                                {:type "sequence-data",
                                                 :data
                                                       [{:type        "copy-variable",
                                                         :var-name    "current-dialog",
                                                         :from-params [{:param-property "concept", :action-property "from"}]}
                                                        {:id "word-for-image-dialog-3", :type "action"}]},
                        :word-for-image-dialog-1
                                                {:type               "sequence-data",
                                                 :data
                                                                     [{:data [{:type "empty", :duration 0} {:type "animation-sequence", :audio nil, :phrase-text "New action"}],
                                                                       :type "sequence-data"}],
                                                 :phrase             "word-1",
                                                 :phrase-description "Word for image 1",
                                                 :concept-var        "current-dialog",
                                                 :editor-type        "dialog"
                                                 :dialog-track       "Word image"},
                        :word-for-image-dialog-2
                                                {:type               "sequence-data",
                                                 :data
                                                                     [{:data [{:type "empty", :duration 0} {:type "animation-sequence", :audio nil, :phrase-text "New action"}],
                                                                       :type "sequence-data"}],
                                                 :phrase             "word-2",
                                                 :phrase-description "Word for image 2",
                                                 :concept-var        "current-dialog",
                                                 :editor-type        "dialog"
                                                 :dialog-track       "Word image"},
                        :word-for-image-dialog-3
                                                {:type               "sequence-data",
                                                 :data
                                                                     [{:data [{:type "empty", :duration 0} {:type "animation-sequence", :audio nil, :phrase-text "New action"}],
                                                                       :type "sequence-data"}],
                                                 :phrase             "word-3",
                                                 :phrase-description "Word for image 3",
                                                 :concept-var        "current-dialog",
                                                 :editor-type        "dialog"
                                                 :dialog-track       "Word image"
                                                  }},
        :triggers      {:back {:on "back", :action "stop-activity"}, :start {:on "start", :action "start-scene"}},
        :metadata      {:prev "park", :autostart true, :template-id 30},
        :variables     {:status nil}})

(defn f
  [t args]
  t)

(core/register-template
  m
  (partial f t))
