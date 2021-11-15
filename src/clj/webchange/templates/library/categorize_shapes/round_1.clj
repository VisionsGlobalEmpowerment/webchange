(ns webchange.templates.library.categorize-shapes.round-1
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(def template-round-1 {:assets        [
                                       {:url "/raw/img/categorize-shapes/background-white.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/circle.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/oval.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/rectangle.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/square.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/star.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/triangle.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/circle-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/oval-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/rectangle-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/square-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/star-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/triangle-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/question.png", :size 10, :type "image"}
                                       ],
                       :objects       {:background    {:type "background", :src "/raw/img/categorize-shapes/background-white.png"},
                                       :circle-box
                                       {:type       "image",
                                        :x          1572,
                                        :y          777,
                                        :src        "/raw/img/categorize-shapes/circle-box.png",
                                        },
                                       :oval-box      {:type       "image",
                                                       :x          69,
                                                       :y          777,
                                                       :src        "/raw/img/categorize-shapes/oval-box.png",
                                                       }
                                       :rectangle-box {:type       "image",
                                                       :x          369,
                                                       :y          777,
                                                       :src        "/raw/img/categorize-shapes/rectangle-box.png",
                                                       }
                                       :square-box    {:type       "image",
                                                       :x          670,
                                                       :y          777,
                                                       :src        "/raw/img/categorize-shapes/square-box.png",
                                                       }
                                       :star-box      {:type       "image",
                                                       :x          971,
                                                       :y          777,
                                                       :src        "/raw/img/categorize-shapes/star-box.png",
                                                       }
                                       :triangle-box  {:type       "image",
                                                       :x          1271,
                                                       :y          777,
                                                       :src        "/raw/img/categorize-shapes/triangle-box.png",
                                                       }
                                       :circle-1      {
                                                       :type       "image",
                                                       :y          442,
                                                       :x          710,
                                                       :src        "/raw/img/categorize-shapes/circle.png",
                                                       :draggable  true,
                                                       :actions    {:drag-start {:type   "action",
                                                                                 :on     "drag-start",
                                                                                 :id     "start-drag"
                                                                                 :params {:say-item         "circle-item"
                                                                                          :target         "circle-1"
                                                                                          :placement-target "circle-box"}
                                                                                 }
                                                                    :drag-end
                                                                    {:id     "dragged",
                                                                     :on     "drag-end",
                                                                     :type   "action",
                                                                     :params {:box            "circle-box"
                                                                              :target         "circle-1"
                                                                              :init-position  {:x 710, :y 442, :duration 1}
                                                                              :check-variable "circle-box-selected"
                                                                              }}}},
                                       :oval-1        {
                                                       :type       "image",
                                                       :x          925,
                                                       :y          124,
                                                       :src        "/raw/img/categorize-shapes/oval.png",
                                                       :draggable  true,
                                                       :actions    {:drag-start {
                                                                                 :type   "action",
                                                                                 :on     "drag-start",
                                                                                 :id     "start-drag"
                                                                                 :params {:say-item         "oval-item"
                                                                                          :target         "oval-1"
                                                                                          :placement-target "oval-box"}
                                                                                 }
                                                                    :drag-end
                                                                    {:id     "dragged",
                                                                     :on     "drag-end",
                                                                     :type   "action",
                                                                     :params {:box            "oval-box"
                                                                              :target         "oval-1"
                                                                              :init-position  {:x 925, :y 124, :duration 1}
                                                                              :check-variable "oval-box-selected"
                                                                              }}}},
                                       :rectangle-1   {
                                                       :type       "image",
                                                       :x          617,
                                                       :y          146,
                                                       :src        "/raw/img/categorize-shapes/rectangle.png",
                                                       :draggable  true,
                                                       :actions    {:drag-start {
                                                                                 :type   "action",
                                                                                 :on     "drag-start",
                                                                                 :id     "start-drag"
                                                                                 :params {:say-item         "rectangle-item"
                                                                                          :target         "rectangle-1"
                                                                                          :placement-target "rectangle-box"}
                                                                                 }
                                                                    :drag-end
                                                                    {:id     "dragged",
                                                                     :on     "drag-end",
                                                                     :type   "action",
                                                                     :params {:box            "rectangle-box"
                                                                              :target         "rectangle-1"
                                                                              :init-position  {:x 617, :y 146, :duration 1}
                                                                              :check-variable "rectangle-box-selected"
                                                                              }}}}
                                       :square-1      {
                                                       :type       "image",
                                                       :x          982,
                                                       :y          360,
                                                       :src        "/raw/img/categorize-shapes/square.png",
                                                       :draggable  true,
                                                       :actions    {:drag-start {
                                                                                 :type   "action",
                                                                                 :on     "drag-start",
                                                                                 :id     "start-drag"
                                                                                 :params {:say-item         "square-item"
                                                                                          :target         "square-1"
                                                                                          :placement-target "square-box"}
                                                                                 }
                                                                    :drag-end
                                                                    {:id     "dragged",
                                                                     :on     "drag-end",
                                                                     :type   "action",
                                                                     :params {:box            "square-box"
                                                                              :target         "square-1"
                                                                              :init-position  {:x 982, :y 360, :duration 1}
                                                                              :check-variable "square-box-selected"
                                                                              }}}}
                                       :star-1        {
                                                       :type       "image",
                                                       :x          1214,
                                                       :y          289,
                                                       :src        "/raw/img/categorize-shapes/star.png",
                                                       :draggable  true,
                                                       :actions    {:drag-start {
                                                                                 :type   "action",
                                                                                 :on     "drag-start",
                                                                                 :id     "start-drag"
                                                                                 :params {:say-item         "star-item"
                                                                                          :target         "star-1"
                                                                                          :placement-target "star-box"}
                                                                                 }
                                                                    :drag-end
                                                                    {:id     "dragged",
                                                                     :on     "drag-end",
                                                                     :type   "action",
                                                                     :params {:box            "star-box"
                                                                              :target         "star-1"
                                                                              :init-position  {:x 1214, :y 289, :duration 1}
                                                                              :check-variable "star-box-selected"
                                                                              }}}}
                                       :triangle-1    {
                                                       :type       "image",
                                                       :x          424,
                                                       :y          377,
                                                       :src        "/raw/img/categorize-shapes/triangle.png",
                                                       :draggable  true,
                                                       :actions    {:drag-start {
                                                                                 :type   "action",
                                                                                 :on     "drag-start",
                                                                                 :id     "start-drag"
                                                                                 :params {:say-item         "triangle-item"
                                                                                          :target         "triangle-1"
                                                                                          :placement-target "triangle-box"}
                                                                                 }
                                                                    :drag-end
                                                                    {:id     "dragged",
                                                                     :on     "drag-end",
                                                                     :type   "action",
                                                                     :params {:box            "triangle-box"
                                                                              :target         "triangle-1"
                                                                              :init-position  {:x 424, :y 377, :duration 1}
                                                                              :check-variable "triangle-box-selected"
                                                                              }}}}},
                       :scene-objects [["background"]
                                       ["circle-box" "oval-box" "rectangle-box"]
                                       ["square-box" "star-box" "triangle-box"]
                                       ["circle-1" "oval-1" "rectangle-1"]
                                       ["square-1" "star-1" "triangle-1"]],
                       :actions {:object-in-right-box {:type        "set-attribute", :attr-name "visible" :attr-value false
                                                       :from-params [{:action-property "target" :param-property "target"}]},
                                 :object-revert       {:type        "transition",
                                                       :from-params [{:action-property "transition-id" :param-property "target"}
                                                                     {:action-property "to" :param-property "init-position"}]}
                                 :wrong-option        {:type "sequence-data",
                                                       :data [{:id "unhighlight-all" :type "action"}
                                                              {:id "object-revert", :type "action"}
                                                              {:id "wrong-answer", :type "action"}],}

                                 :correct-option      {:type "sequence-data",
                                                       :data [{:id "unhighlight-all" :type "action"}
                                                              {:type "counter" :counter-action "increase" :counter-id "sorted-objects"}
                                                              {:id "object-in-right-box", :type "action"}
                                                              {:id "correct-answer", :type "action"}
                                                              {:type       "test-var-inequality"
                                                               :var-name   "sorted-objects",
                                                               :value      6,
                                                               :inequality ">=",
                                                               :success    "finish-scene"}]}

                                 :dragged             {:type "sequence-data"
                                                       :data [
                                                              {:type        "copy-variable",
                                                               :var-name "current-selection-state"
                                                               :from-params [{:param-property "check-variable", :action-property "from"}]}
                                                              {:type "set-variable", :var-name "say", :var-value false}
                                                              {:type "set-variable", :var-name "next-check-collide", :var-value false}

                                                              {:type        "test-var-scalar",
                                                               :success     "correct-option",
                                                               :fail       "object-revert",
                                                               :value       true,
                                                               :var-name "current-selection-state"}
                                                              ]
                                                       }
                                 :highlight           {:type "sequence-data"
                                                       :data [{:type        "set-variable",
                                                               :var-value   true
                                                               :from-params [{:action-property "var-name" :param-property "check-variable"}]}
                                                              {:type        "set-attribute" :attr-name "highlight" :attr-value true
                                                               :from-params [{:action-property "target" :param-property "transition"}]}]}
                                 :unhighlight         {:type "sequence-data"
                                                       :data [{:type        "set-variable",
                                                               :var-value   false
                                                               :from-params [{:action-property "var-name" :param-property "check-variable"}]}
                                                              {:type        "set-attribute" :attr-name "highlight" :attr-value false
                                                               :from-params [{:action-property "target" :param-property "transition"}]}]}
                                 :unhighlight-all     {:type "parallel"
                                                       :data [{:type "set-attribute" :attr-name "highlight" :attr-value false :target "oval-box"}
                                                              {:type "set-attribute" :attr-name "highlight" :attr-value false :target "circle-box"}
                                                              {:type "set-attribute" :attr-name "highlight" :attr-value false :target "rectangle-box"}
                                                              {:type "set-attribute" :attr-name "highlight" :attr-value false :target "square-box"}
                                                              {:type "set-attribute" :attr-name "highlight" :attr-value false :target "start-box"}
                                                              {:type "set-attribute" :attr-name "highlight" :attr-value false :target "triangle-box"}]}
                                 :next-check-collide  {:type "sequence-data"
                                                       :data [{:type     "set-timeout"
                                                               :action   "check-collide"
                                                               :interval 10}]}
                                 :check-collide       {:type "sequence-data"
                                                       :data [
                                                              {:type          "test-transitions-and-pointer-collide",
                                                               :success       "highlight",
                                                               :fail          "unhighlight",
                                                               :transitions   ["oval-box" "circle-box"
                                                                               "rectangle-box" "square-box"
                                                                               "star-box" "triangle-box"]
                                                               :action-params [{:check-variable "oval-box-selected"}
                                                                               {:check-variable "circle-box-selected"}
                                                                               {:check-variable "rectangle-box-selected"}
                                                                               {:check-variable "square-box-selected"}
                                                                               {:check-variable "star-box-selected"}
                                                                               {:check-variable "triangle-box-selected"}]}
                                                              {:type     "test-var-scalar",
                                                               :success  "next-check-collide",
                                                               :value    true,
                                                               :var-name "next-check-collide"}
                                                              ]}

                                 :oval-item           (-> (dialog/default "oval")
                                                          (assoc :unique-tag "item"))
                                 :circle-item         (-> (dialog/default "circle")
                                                          (assoc :unique-tag "item"))
                                 :rectangle-item      (-> (dialog/default "rectangle")
                                                          (assoc :unique-tag "item"))
                                 :square-item         (-> (dialog/default "square")
                                                          (assoc :unique-tag "item"))
                                 :star-item           (-> (dialog/default "star")
                                                          (assoc :unique-tag "item"))
                                 :triangle-item       (-> (dialog/default "triangle")
                                                          (assoc :unique-tag "item"))

                                 :say-item            {:type "sequence-data"
                                                       :data [{:type "action" :from-params [{:action-property "id"
                                                                                             :param-property  "say-item"}]}
                                                              {:type     "test-var-scalar",
                                                               :success  "next-say",
                                                               :value    true,
                                                               :var-name "say"}]}
                                 :next-say            {:type "sequence-data"
                                                       :data [{:type     "set-timeout"
                                                               :action   "say-item"
                                                               :interval 100}]}
                                 :start-drag          {:type "sequence-data"
                                                       :data [{:type        "stop-transition"
                                                               :from-params [{:action-property "id" :param-property "target"}]}
                                                              {:type "action", :id "reset-selected-vars"}
                                                              {:type "set-variable", :var-name "say", :var-value true}
                                                              {:type "set-variable", :var-name "next-check-collide", :var-value true}
                                                              {:id "next-say" :type "action"}
                                                              {:id "next-check-collide" :type "action"}]}

                                 :init-activity       {:type "sequence-data"
                                                       :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-objects"}
                                                              {:type "action" :id "intro"}]
                                                       }
                                 :reset-selected-vars {:type "sequence-data"
                                                       :data [
                                                              {:type "set-variable", :var-name "oval-box-selected", :var-value false}
                                                              {:type "set-variable", :var-name "circle-box-selected", :var-value false}
                                                              {:type "set-variable", :var-name "rectangle-box-selected", :var-value false}
                                                              {:type "set-variable", :var-name "square-box-selected", :var-value false}
                                                              {:type "set-variable", :var-name "star-box-selected", :var-value false}
                                                              {:type "set-variable", :var-name "triangle-box-selected", :var-value false}]}
                                 :intro               {:type               "sequence-data",
                                                       :editor-type        "dialog",
                                                       :data               [{:type "sequence-data"
                                                                             :data [{:type "empty" :duration 0}
                                                                                    {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                       :phrase             "intro",
                                                       :phrase-description "Introduce task"}
                                 :correct-answer      {:type               "sequence-data",
                                                       :editor-type        "dialog",
                                                       :data               [{:type "sequence-data"
                                                                             :data [{:type "empty" :duration 0}
                                                                                    {:type "animation-sequence",
                                                                                     :phrase-text "New action", :audio nil}]}],
                                                       :phrase             "correct-answer",
                                                       :phrase-description "correct answer"}
                                 :wrong-answer        {:type               "sequence-data",
                                                       :editor-type        "dialog",
                                                       :data               [{:type "sequence-data"
                                                                             :data [{:type "empty" :duration 0}
                                                                                    {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                       :phrase             "wrong-answer",
                                                       :phrase-description "wrong answer"}
                                 :finish-scene        {:type "sequence-data",
                                                       :data [
                                                              {:type "action" :id "finish-dialog"}
                                                              {:type "set-variable", :var-name "next-check-collide", :var-value false}
                                                              {:type "action", :id "finish-activity"}],}
                                 :finish-dialog       {:type               "sequence-data",
                                                       :editor-type        "dialog",
                                                       :data               [{:type "sequence-data"
                                                                             :data [{:type "empty" :duration 0}
                                                                                    {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                       :phrase             "finish-dialog",
                                                       :phrase-description "finish dialog"}
                                 :stop-activity       {:type "stop-activity"},
                                 :finish-activity     {:type "finish-activity"}
                                 },

                       :triggers
                       {:back  {:on "back", :action "stop-activity"},
                        :start {:on "start", :action "init-activity"}},
                       :metadata      {:autostart true
                                       :tracks    [{:title "Round 1"
                                                    :nodes [{:type      "dialog"
                                                             :action-id :intro}
                                                            {:type "prompt"
                                                             :text "Correct answer"}
                                                            {:type      "dialog"
                                                             :action-id :correct-answer}
                                                            {:type "prompt"
                                                             :text "Wrong answer"}
                                                            {:type      "dialog"
                                                             :action-id :wrong-answer}
                                                            {:type "prompt"
                                                             :text "Finish dialog"}
                                                            {:type      "dialog"
                                                             :action-id :finish-dialog}
                                                            ]}
                                                   {:title "Round 1 - items"
                                                    :nodes [{:type      "dialog"
                                                             :action-id :oval-item}
                                                            {:type      "dialog"
                                                             :action-id :circle-item}
                                                            {:type      "dialog"
                                                             :action-id :rectangle-item}
                                                            {:type      "dialog"
                                                             :action-id :square-item}
                                                            {:type      "dialog"
                                                             :action-id :star-item}
                                                            {:type      "dialog"
                                                             :action-id :triangle-item}
                                                            ]}]}})
