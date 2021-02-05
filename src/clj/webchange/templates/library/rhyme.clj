(ns webchange.templates.library.rhyme
  (:require
    [webchange.templates.utils.common :as common]
    [webchange.templates.core :as core]))

(def m {:id          27
        :name        "Rhyme"
        :tags        ["Independent Practice"]
        :description "Rhyme"
        :options     {:left  {:label "Left gate word"
                              :type  "string"}
                      :right {:label "Right gate word"
                              :type  "string"}}
        :actions     {
                      :add-ball {:title   "Add ball",
                                 :options {
                                           :side {:label   "Correct ball side"
                                                  :type    "lookup"
                                                  :options [{:name "Left" :value "left"}
                                                            {:name "Right" :value "right"}]}
                                           :text {:label "Name"
                                                  :type  "string"}
                                           :img  {:label "Dialog"
                                                  :type  "image"}
                                           }}

                      }})

(def t {:assets        [
                        {:url "/raw/img/rhyming/background.png", :size 10 :type "image"}
                        {:url "/raw/img/rhyming/surface.png", :size 10 :type "image"}
                        {:url "/raw/img/rhyming/left-gate.png", :size 10 :type "image"}
                        {:url "/raw/img/rhyming/right-gate.png", :size 10 :type "image"}
                        {:url "/raw/img/rhyming/ball.png", :size 10 :type "image"}
                        {:url "/raw/img/rhyming/img.jpg", :size 10 :type "image"}
                        ],
        :objects
                       {:layered-background {:type       "layered-background"
                                             :background {:src "/raw/img/rhyming/background.png"}
                                             :surface    {:src "/raw/img/rhyming/surface.png"}}
                        :left-gate       {:type       "image"
                                          :src        "/raw/img/rhyming/left-gate.png"
                                          :x          50
                                          :y          210
                                          :transition "left-gate"
                                          :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},}
                        :left-gate-text  {:type           "text"
                                          :text           "rhyme"
                                          :x              355
                                          :y              330
                                          :skew-x          0
                                          :skew-y          0.19
                                          :align          "center"
                                          :vertical-align "middle"
                                          :font-family    "Lexend Deca"
                                          :font-size      80
                                          :fill           "#000000"}
                        :right-gate-text {:type           "text"
                                          :text           "rhyme"
                                          :x              1590
                                          :y              330
                                          :skew-x          -0.05
                                          :skew-y          -0.19
                                          :align          "center"
                                          :vertical-align "middle"
                                          :font-family    "Lexend Deca"
                                          :font-size      80
                                          :fill           "#000000"}
                        :right-gate      {:type       "image"
                                          :src        "/raw/img/rhyming/right-gate.png"
                                          :x          1380
                                          :y          210
                                          :transition "right-gate"
                                          :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},}

                        },
        :scene-objects [["layered-background"] ["left-gate" "right-gate" "left-gate-text" "right-gate-text"]],
        :actions       {:start-drag           {:type "sequence-data"
                                               :data [
                                                      {:type "set-variable", :var-name "left-selected", :var-value false}
                                                      {:type "set-variable", :var-name "right-selected", :var-value false}
                                                      {:type     "set-interval"
                                                       :id       "check-collide-2"
                                                       :interval 100
                                                       :action   "check-collide"}
                                                      ]
                                               }
                        :check-collide        {:type "sequence-data"
                                               :data [
                                                      {:type       "test-transition-and-pointer-collide",
                                                       :success    "highlight",
                                                       :fail       "unhighlight",
                                                       :transition "right-gate",
                                                       :params     {:target   "right-gate"
                                                                    :variable "right-selected"}
                                                       }
                                                      {:type       "test-transition-and-pointer-collide",
                                                       :success    "highlight",
                                                       :fail       "unhighlight",
                                                       :transition "left-gate"
                                                       :params     {:target   "left-gate"
                                                                    :variable "left-selected"}}]
                                               }
                        :highlight            {:type "sequence-data"
                                               :data [{:type        "set-variable",
                                                       :var-value   true
                                                       :from-params [{:action-property "var-name" :param-property "variable"}]}
                                                      {:type        "state"
                                                       :id          "highlighted"
                                                       :from-params [{:action-property "target" :param-property "target"}]}]
                                               }
                        :unhighlight          {:type "sequence-data"
                                               :data [{:type        "set-variable",
                                                       :var-value   false
                                                       :from-params [{:action-property "var-name" :param-property "variable"}]}
                                                      {:type        "state"
                                                       :id          "not-highlighted"
                                                       :from-params [{:action-property "target" :param-property "target"}]}]}
                        :wrong-option         {:type "sequence-data",
                                               :data [{:id "wrong-answer-dialog", :type "action"}]}
                        :wrong-answer-dialog  {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase             "wrong-answer",
                                               :phrase-description "wrong answer"}
                        :check-wrong-option   {:type      "test-var-list-at-least-one-true"
                                               :var-names ["left-selected" "right-selected"]
                                               :success   "wrong-option"}
                        :end-dragging         {:type "sequence-data"
                                               :data [
                                                      {:type        "test-var-scalar",
                                                       :success     "correct-option",
                                                       :fail        "check-wrong-option",
                                                       :value       true,
                                                       :from-params [{:param-property "check-variable", :action-property "var-name"}]
                                                       }
                                                      {:type "remove-interval"
                                                       :id   "check-collide-2"}
                                                      {:type        "state"
                                                       :id          "not-highlighted"
                                                       :from-params [{:action-property "target" :param-property "gate"}]}
                                                      ]
                                               }
                        :correct-option       {:type "sequence-data",
                                               :data [{:type "counter" :counter-action "increase" :counter-id "sorted-counter"}
                                                      {:id "object-in-right-gate", :type "action"}
                                                      {:id "correct-answer", :type "action"}
                                                      {:type       "test-var-inequality"
                                                       :var-name   "sorted-counter",
                                                       :value      0,
                                                       :inequality ">=",
                                                       :success    "finish-scene",
                                                       }]}
                        :init-activity        {:type "sequence-data"
                                               :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-counter"}
                                                      {:type "action" :id "intro"}]
                                               }
                        :intro                {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase             "intro",
                                               :phrase-description "Introduce task"}
                        :object-in-right-gate {:id          "hide",
                                               :type        "state",
                                               :from-params [{:action-property "target" :param-property "target"}]}
                        :correct-answer       {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase             "correct-answer",
                                               :phrase-description "correct answer"}
                        :stop-activity        {:type "sequence-data",
                                               :data [
                                                      {:type "remove-interval"
                                                       :id   "check-collide-2"}
                                                      {:type "stop-activity", :id "rhyme"},
                                                      ]}
                        :finish-scene         {:type "sequence-data",
                                               :data [{:type "action" :id "finish-dialog"}
                                                      {:type "action", :id "stop-activity"}
                                                      ],
                                               }
                        :finish-dialog        {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase             "finish-dialog",
                                               :phrase-description "finish dialog"}

                        }

        :triggers
                       {:back  {:on "back", :action "stop-activity"},
                        :start {:on "start", :action "init-activity"}},
        :metadata      {}}
  )

(defn add-ball
  [scene-data {:keys [side img text]}]
  [[(common/make-name-unique scene-data "ball-group")]
   {(common/make-name-unique scene-data "ball")       {:type    "image"
                                                       :src     "/raw/img/rhyming/ball.png"
                                                       :x       0
                                                       :y       0
                                                       :scale-y 1
                                                       :scale-x 1}

    (common/make-name-unique scene-data "ball-img")   {:type    "image"
                                                       :src     (:src img)
                                                       :x       65
                                                       :y       25
                                                       :scale-y 1
                                                       :scale-x 1}

    (common/make-name-unique scene-data "ball-text")  {:type           "text"
                                                       :text           text
                                                       :x              85
                                                       :y              90
                                                       :align          "center"
                                                       :vertical-align "middle"
                                                       :font-family    "Lexend Deca"
                                                       :font-size      30
                                                       :fill           "#000000"}
    (common/make-name-unique scene-data "ball-group") {:type       "group"
                                                       :x          500
                                                       :y          500
                                                       :width      150
                                                       :height     150
                                                       :children   [(common/make-name-unique scene-data "ball")
                                                                    (common/make-name-unique scene-data "ball-img")
                                                                    (common/make-name-unique scene-data "ball-text")]
                                                       :draggable  true,
                                                       :editable?  true
                                                       :transition (name (common/make-name-unique scene-data "ball-group"))
                                                       :actions    {:drag-start {:type "action",
                                                                                 :on   "drag-start",
                                                                                 :id   "start-drag"}
                                                                    :drag-end
                                                                                {:id     "end-dragging",
                                                                                 :on     "drag-end",
                                                                                 :type   "action",
                                                                                 :params {:gate           (str side "-gate")
                                                                                          :target         (common/make-name-unique scene-data "ball-group")
                                                                                          :check-variable (str side "-selected")
                                                                                          :init-position  {:x 500, :y 500, :duration 1}
                                                                                          }}}
                                                       :states     {:hide {:visible false}}
                                                       }}
   [{:url (:src img), :size 10, :type "image"}]
   ]
  )

(defn f
  [args]
  (-> (common/init-metadata m t args)
      (assoc-in [:objects :left-gate-text :text] (:left args))
      (assoc-in [:objects :right-gate-text :text] (:right args))))

(defn fu
  [old-data args]
  (let [[scene-objects objects assets] (add-ball old-data args)]
    (-> old-data
        (update-in [:objects] merge objects)
        (update-in [:assets] concat assets)
        (common/add-scene-object scene-objects)
        (common/update-unique-suffix)
        (update-in [:actions :correct-option :data 3 :value] inc)
        )))

(core/register-template
  m f fu)

