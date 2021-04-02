(ns webchange.templates.library.rhyming
  (:require
    [webchange.templates.utils.common :as common]
    [webchange.templates.core :as core]
    [clojure.string :as str]
    [webchange.templates.utils.dialog :as dialog]))

(def m {:id          27
        :name        "Rhyming"
        :tags        ["Independent Practice"]
        :description "Rhyme"
        :options     {:left  {:label       "Left Goal word"
                              :placeholder "Left Goal word"
                              :type        "string"}
                      :right {:label       "Right Goal word"
                              :placeholder "Right Goal word"
                              :type        "string"}}
        :actions     {
                      :add-ball    {:title   "Add ball",
                                    :options {
                                              :side {:label   "Correct ball side"
                                                     :type    "lookup"
                                                     :options [{:name "Left" :value "left"}
                                                               {:name "Right" :value "right"}]}
                                              :text {:label       "Name"
                                                     :placeholder "Place your word here"
                                                     :type        "string"}
                                              :img  {:label   "Dialog"
                                                     :type    "image"
                                                     :options {:max-width  50
                                                               :max-height 50
                                                               :min-height 20
                                                               :min-width  20}}
                                              }}
                      :remove-ball {:title   "Remove ball",
                                    :options {:remove-ball {:label "Remove ball"
                                                            :type  "remove-editable-object"}}}
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
                        :left-gate          {:type       "image"
                                             :src        "/raw/img/rhyming/left-gate.png"
                                             :x          50
                                             :y          210
                                             :transition "left-gate"
                                             :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},}
                        :left-gate-text     {:type           "text"
                                             :text           "rhyme"
                                             :x              355
                                             :y              330
                                             :skew-x         0
                                             :skew-y         0.19
                                             :align          "center"
                                             :vertical-align "middle"
                                             :font-family    "Lexend Deca"
                                             :font-size      80
                                             :fill           "#000000"}
                        :right-gate-text    {:type           "text"
                                             :text           "rhyme"
                                             :x              1590
                                             :y              330
                                             :skew-x         -0.05
                                             :skew-y         -0.19
                                             :align          "center"
                                             :vertical-align "middle"
                                             :font-family    "Lexend Deca"
                                             :font-size      80
                                             :fill           "#000000"}
                        :right-gate         {:type       "image"
                                             :src        "/raw/img/rhyming/right-gate.png"
                                             :x          1380
                                             :y          210
                                             :transition "right-gate"
                                             :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}},}

                        },
        :scene-objects [["layered-background"] ["left-gate" "right-gate" "left-gate-text" "right-gate-text"]],
        :actions       {:start-drag           {:type "sequence-data"
                                               :data [{:type "set-variable", :var-name "left-selected", :var-value false}
                                                      {:type "set-variable", :var-name "right-selected", :var-value false}
                                                      {:type "set-variable", :var-name "next-check-collide", :var-value true}
                                                      {:id "next-check-collide" :type "action"}
                                                      {:type "action", :from-params [{:action-property "id" :param-property "tap-dialog"}]}
                                                      ]
                                               }

                        :next-check-collide   {:type "sequence-data"
                                               :data [{:type     "set-timeout"
                                                       :action   "check-collide"
                                                       :interval 10}]}
                        :check-collide        {:type "sequence-data"
                                               :data [
                                                      {:type          "test-transitions-and-pointer-collide",
                                                       :success       "highlight",
                                                       :fail          "unhighlight",
                                                       :transitions   ["right-gate" "left-gate"]
                                                       :action-params [{:target   "right-gate"
                                                                        :variable "right-selected"}
                                                                       {:target   "left-gate"
                                                                        :variable "left-selected"}]
                                                       }
                                                      {:type     "test-var-scalar",
                                                       :success  "next-check-collide",
                                                       :value    true,
                                                       :var-name "next-check-collide"}
                                                      ]}

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
                        :wrong-option         {:type "parallel",
                                               :data [{:id "wrong-answer-dialog", :type "action"}
                                                      {:to          {:init-position true :duration 0.5},
                                                       :from-params [{:action-property "transition-id" :param-property "target"}]
                                                       :type        "transition"}
                                                      ]}
                        :wrong-answer-dialog  {:type               "sequence-data",
                                               :editor-type        "dialog",
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                               :phrase             "wrong-answer",
                                               :phrase-description "wrong answer"}
                        :check-wrong-option   {:type      "test-var-list-at-least-one-true"
                                               :var-names ["saved-left-selected" "saved-right-selected"]
                                               :success   "wrong-option"}
                        :end-dragging         {:type "sequence-data"
                                               :data [
                                                      {:type      "copy-variables",
                                                       :var-names ["saved-left-selected" "saved-right-selected"]
                                                       :from-list ["left-selected" "right-selected"]}
                                                      {:type        "test-var-scalar",
                                                       :success     "correct-option",
                                                       :fail        "check-wrong-option",
                                                       :value       true,
                                                       :from-params [{:param-property  "side",
                                                                      :template        "saved-%-selected",
                                                                      :action-property "var-name"}]
                                                       }
                                                      {:type "set-variable", :var-name "next-check-collide", :var-value false}
                                                      {:type        "state"
                                                       :id          "not-highlighted"
                                                       :from-params [{:action-property "target" :param-property "gate"}]}
                                                      ]
                                               }
                        :correct-option       {:type "sequence-data",
                                               :data [{:type "set-variable", :var-name "next-check-collide", :var-value false}
                                                      {:type "counter" :counter-action "increase" :counter-id "sorted-counter"}
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
                                               :tags               ["intro"],
                                               :data               [{:type "sequence-data"
                                                                     :data [{:type "empty" :duration 0}
                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil,
                                                                             }]}],

                                               :phrase             "intro",
                                               :phrase-description "introduction"}
                        :object-in-right-gate {:id          "park-position",
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
                                                      {:type "set-variable", :var-name "next-check-collide", :var-value false}
                                                      {:type "stop-activity", :id "rhyme"},
                                                      ]}
                        :read-all-word-left   {:type "sequence-data",
                                               :data [],}
                        :read-all-word-right  {:type "sequence-data",
                                               :data [],}
                        :finish-scene         {:type       "sequence-data",
                                               :data       [{:type "action" :id "read-all-word-left"}
                                                            {:type "action" :id "read-all-word-right"}
                                                            {:type "action" :id "finish-dialog"}
                                                            {:type "action", :id "stop-activity"}],
                                               :unique-tag "click"}
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
        :metadata      {
                        :tracks [{:title "Track left"
                                  :nodes []}
                                 {:title "Track right"
                                  :nodes []}
                                 ]

                        }}
  )

(defn park-ball-position
  [side balls-number]
  (let [balls-in-row 5
        step-left-y 160
        step-left-x 160
        start-left-y 10
        start-left-x 10

        step-right-y 160
        step-right-x -160
        start-right-y 10
        start-right-x 1730]


    (case side
      "left" {:y (+ start-left-y (* step-left-y (int (/ balls-number balls-in-row))))
              :x (+ start-left-x (* step-left-x (int (mod balls-number balls-in-row))))}
      "right" {:y (+ start-right-y (* step-right-y (int (/ balls-number balls-in-row))))
               :x (+ start-right-x (* step-right-x (int (mod balls-number balls-in-row))))}
      ))
  )

(defn- ball-dialog
  [suffix]
  (common/make-name-unique-by-suffix "ball-dialog" suffix))

(defn- ball
  [suffix]
  (common/make-name-unique-by-suffix "ball" suffix))

(defn- ball-group
  [suffix]
  (common/make-name-unique-by-suffix "ball-group" suffix))

(defn- ball-img
  [suffix]
  (common/make-name-unique-by-suffix "ball-img" suffix))

(defn- ball-text
  [suffix]
  (common/make-name-unique-by-suffix "ball-text" suffix))


(defn add-ball
  [scene-data {:keys [side img text]}]
  (let [
        suffix (common/get-unique-suffix scene-data)
        ball-dialog-name (ball-dialog suffix)
        ball-name (ball suffix)
        ball-group-name (ball-group suffix)
        balls (get-in scene-data [:metadata :balls (keyword side)])
        ball-img-name (ball-img suffix)
        ball-text-name (ball-text suffix)
        ]
    [[(ball-group suffix)]
     {ball-name       {:type       "image"
                       :src        "/raw/img/rhyming/ball.png"
                       :x          0
                       :y          0
                       :scale-y    1
                       :scale-x    1
                       :states     {:highlighted {:highlight true} :not-highlighted {:highlight false}}
                       :transition (name ball-name)
                       }

      ball-img-name   {:type    "image"
                       :src     (:src img)
                       :x       65
                       :y       25
                       :scale-y 1
                       :scale-x 1
                       }

      ball-text-name  {:type           "text"
                       :text           text
                       :x              85
                       :y              90
                       :align          "center"
                       :vertical-align "middle"
                       :font-family    "Lexend Deca"
                       :font-size      30
                       :fill           "#000000"}
      ball-group-name {:type       "group"
                       :x          500
                       :y          500
                       :width      150
                       :height     150
                       :children   [ball-name
                                    ball-img-name
                                    ball-text-name]
                       :draggable  true,
                       :editable?  true
                       :transition (name ball-group-name)
                       :actions    {:drag-start {:type   "action",
                                                 :on     "drag-start",
                                                 :id     "start-drag"
                                                 :params {:tap-dialog ball-dialog-name}
                                                 }
                                    :drag-end
                                                {:id     "end-dragging",
                                                 :on     "drag-end",
                                                 :type   "action",
                                                 :params {:gate           (str side "-gate")
                                                          :side           side
                                                          :target         ball-group-name
                                                          :check-variable (str side "-selected")
                                                          }}}
                       :states     {:park-position (park-ball-position side balls)}
                       }}
     {ball-dialog-name (dialog/default text)}
     [{:url (:src img), :size 10, :type "image"}]
     ball-dialog-name
     ball-name
     ])
  )

(defn f
  [args]
  (-> (common/init-metadata m t args)
      (assoc-in [:metadata :balls :left] 0)
      (assoc-in [:metadata :balls :right] 0)
      (assoc-in [:objects :left-gate-text :text] (:left args))
      (assoc-in [:objects :right-gate-text :text] (:right args))))

(defn add-ball-action
  [old-data {:keys [side] :as args}]
  (let [[scene-objects objects actions assets ball-dialog-name ball-name] (add-ball old-data args)]
    (-> old-data
        (update-in [:objects] merge objects)
        (update-in [:assets] concat assets)
        (update-in [:actions] merge actions)
        (update-in [:actions (keyword (str "read-all-word-" side)) :data]
                   concat [{:type "state" :target ball-name :id "highlighted"}
                           {:type "action" :id ball-dialog-name}
                           {:type "state" :target ball-name :id "not-highlighted"}])
        (common/add-track-action {:track-name (str "Track " side)
                                  :type       "dialog"
                                  :action-id  (keyword ball-dialog-name)})
        (update-in [:metadata :balls (keyword side)] inc)
        (common/add-scene-object scene-objects)
        (common/update-unique-suffix)
        (update-in [:actions :correct-option :data 4 :value] inc)
        ))
  )

(defn- move-side-balls-to-remove-gaps
  [old-data side]
  (let [
        suffixes (as-> old-data dt
                       (get-in dt [:actions (keyword (str "read-all-word-" side)) :data])
                       (filter (fn [action]
                                 (= (:type action) "action")) dt)
                       (map (fn [action]
                              (last (str/split (:id action) #"-"))) dt))
        objects (into {} (map-indexed (fn [idx suffix]
                                        (let [ball-group-name (ball-group suffix)]
                                          [ball-group-name
                                           (-> old-data
                                               (get-in [:objects ball-group-name])
                                               (assoc-in [:states :park-position] (park-ball-position side idx)))])
                                        ) suffixes))]
    (update-in old-data [:objects] merge objects)))

(defn remove-ball-action
  [old-data {:keys [object] :as args}]
  (let [[_ _ suffix] (str/split object #"-")

        ball-dialog-name (ball-dialog suffix)
        ball-name (ball suffix)
        ball-group-name (ball-group suffix)
        ball-img-name (ball-img suffix)
        ball-text-name (ball-text suffix)
        side (get-in old-data [:objects ball-group-name :actions :drag-end :params :side])
        ]
    (-> old-data
        (common/remove-asset (get-in old-data [:objects ball-img-name :src]))
        (common/remove-object ball-group-name)
        (common/remove-object ball-text-name)
        (common/remove-object ball-img-name)
        (common/remove-object ball-name)
        (update-in [:actions] dissoc ball-dialog-name)
        (update-in [:actions (keyword (str "read-all-word-" side)) :data] common/remove-actions-by-key-value :target (name ball-name))
        (update-in [:actions (keyword (str "read-all-word-" side)) :data] common/remove-actions-by-key-value :id (name ball-dialog-name))
        (update-in [:metadata :balls (keyword side)] dec)
        (update-in [:actions :correct-option :data 4 :value] dec)
        (move-side-balls-to-remove-gaps side)
        (common/remove-action-from-tracks ball-dialog-name)
        )
    ))

(defn fu
  [old-data {:keys [action-name] :as args}]
  (case action-name
    "add-ball" (add-ball-action old-data args)
    "remove-ball" (remove-ball-action old-data args)))

(core/register-template
  m f fu)

