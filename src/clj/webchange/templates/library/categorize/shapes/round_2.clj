(ns webchange.templates.library.categorize.shapes.round-2
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(def template-round-2 {:assets        [
                                       {:url "/raw/img/categorize-shapes/background-class.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/surface.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/decoration.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/circle-group.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/oval-group.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/rectangle-group.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/square-group.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/star-group.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/triangle-group.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/circle-table.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/oval-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/rectangle-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/square-table.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/star-box.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/triangle-table.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-shapes/question.png", :size 10, :type "image"}
                                       ],
                       :objects       {:layered-background {:type       "layered-background",
                                                            :background {:src "/raw/img/categorize-shapes/background-class.png"},
                                                            :decoration {:src "/raw/img/categorize-shapes/decoration.png"},
                                                            :surface    {:src "/raw/img/categorize-shapes/surface.png"}
                                                            },
                                       :circle-table {:type       "image",
                                                      :x          1222,
                                                      :y          650,
                                                      :src        "/raw/img/categorize-shapes/circle-table.png",
                                                      },
                                       :oval-box           {:type       "image",
                                                            :x          463,
                                                            :y          835,
                                                            :src        "/raw/img/categorize-shapes/oval-box.png",
                                                            }
                                       :rectangle-box      {:type       "image",
                                                            :x          822,
                                                            :y          835,
                                                            :src        "/raw/img/categorize-shapes/rectangle-box.png",
                                                            }
                                       :square-table       {:type       "image",
                                                            :x          691,
                                                            :y          665,
                                                            :src        "/raw/img/categorize-shapes/square-table.png",
                                                            }
                                       :star-box           {:type       "image",
                                                            :x          1181,
                                                            :y          835,
                                                            :src        "/raw/img/categorize-shapes/star-box.png",
                                                            }
                                       :triangle-table     {:type       "image",
                                                            :x          196,
                                                            :y          631,
                                                            :src        "/raw/img/categorize-shapes/triangle-table.png",
                                                            }
                                       :circle-1           {
                                                            :type       "image",
                                                            :x          1661,
                                                            :y          809,
                                                            :src        "/raw/img/categorize-shapes/circle-group.png",
                                                            :transition "circle-1",
                                                            :draggable  true,
                                                            :actions    {:drag-start {:type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"
                                                                                      :params {:say-item         "circle-item"
                                                                                               :target         "circle-1"}}
                                                                         :drag-end
                                                                         {:id     "dragged",
                                                                          :on     "drag-end",
                                                                          :type   "action",
                                                                          :params {:box            "circle-table"
                                                                                   :target         "circle-1"
                                                                                   :init-position  {:x 1661, :y 809, :duration 1}
                                                                                   :check-variable "circle-table-selected"
                                                                                   }}},},
                                       :oval-1             {
                                                            :type       "image",
                                                            :x          169,
                                                            :y          49,
                                                            :src        "/raw/img/categorize-shapes/oval-group.png",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"
                                                                                      :params {:say-item         "oval-item"
                                                                                               :target         "oval-1"}}
                                                                         :drag-end
                                                                         {:id     "dragged",
                                                                          :on     "drag-end",
                                                                          :type   "action",
                                                                          :params {:box            "oval-box"
                                                                                   :target         "oval-1"
                                                                                   :init-position  {:x 169, :y 49, :duration 1}
                                                                                   :check-variable "oval-box-selected"
                                                                                   }}},},
                                       :rectangle-1        {
                                                            :type       "image",
                                                            :x          59,
                                                            :y          359,
                                                            :src        "/raw/img/categorize-shapes/rectangle-group.png",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"
                                                                                      :params {:say-item         "rectangle-item"
                                                                                               :target         "rectangle-1"}}
                                                                         :drag-end
                                                                         {:id     "dragged",
                                                                          :on     "drag-end",
                                                                          :type   "action",
                                                                          :params {:box            "rectangle-box"
                                                                                   :target         "rectangle-1"
                                                                                   :init-position  {:x 59, :y 359, :duration 1}
                                                                                   :check-variable "rectangle-box-selected"
                                                                                   }}},}
                                       :square-1           {
                                                            :type       "image",
                                                            :x          1682,
                                                            :y          136,
                                                            :src        "/raw/img/categorize-shapes/square-group.png",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"
                                                                                      :params {:say-item         "square-item"
                                                                                               :target         "square-1"}}
                                                                         :drag-end
                                                                         {:id     "dragged",
                                                                          :on     "drag-end",
                                                                          :type   "action",
                                                                          :params {:box            "square-table"
                                                                                   :target         "square-1"
                                                                                   :init-position  {:x 1682, :y 136, :duration 1}
                                                                                   :check-variable "square-table-selected"
                                                                                   }}},}
                                       :star-1             {
                                                            :type       "image",
                                                            :x          826,
                                                            :y          402,
                                                            :src        "/raw/img/categorize-shapes/star-group.png",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"
                                                                                      :params {:say-item         "star-item"
                                                                                               :target         "star-1"}}
                                                                         :drag-end
                                                                         {:id     "dragged",
                                                                          :on     "drag-end",
                                                                          :type   "action",
                                                                          :params {:box            "star-box"
                                                                                   :target         "star-1"
                                                                                   :init-position  {:x 826, :y 402, :duration 1}
                                                                                   :check-variable "star-box-selected"
                                                                                   }}},}
                                       :triangle-1         {
                                                            :type       "image",
                                                            :x          126,
                                                            :y          936,
                                                            :src        "/raw/img/categorize-shapes/triangle-group.png",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type "action",
                                                                                      :on   "drag-start",
                                                                                      :id   "start-drag"
                                                                                      :params {:say-item         "triangle-item"
                                                                                               :target         "triangle-1"}}
                                                                         :drag-end
                                                                         {:id     "dragged",
                                                                          :on     "drag-end",
                                                                          :type   "action",
                                                                          :params {:box            "triangle-table"
                                                                                   :target         "triangle-1"
                                                                                   :init-position  {:x 126, :y 936, :duration 1}
                                                                                   :check-variable "triangle-table-selected"
                                                                                   }}},}
                                       :librarian        {:type   "animation",
                                                          :x      250,
                                                          :y      1000,
                                                          :width  351,
                                                          :height 717,
                                                          :anim   "idle",
                                                          :name   "senoravaca",
                                                          :skin   "lion",
                                                          :speed  0.3,
                                                          :start  true
                                                          :editable? {:select true
                                                                      :show-in-tree? true}
                                                          :actions    {:click {:id "tap-instructions" :on "click" :type "action"}}}
                                       },
                       :scene-objects [["layered-background"]
                                       ["square-table" "triangle-table" "circle-table"]
                                       ["librarian"]
                                       ["oval-box" "rectangle-box" "star-box"]
                                       ["circle-1" "oval-1" "rectangle-1"]
                                       ["square-1" "star-1" "triangle-1"]
                                       ],
                       :actions       {:object-in-right-box            {:type        "set-attribute", :attr-name "visible" :attr-value false
                                                                        :from-params [{:action-property "target" :param-property "target"}]},
                                       :object-revert                  {:type        "transition",
                                                                        :from-params [{:action-property "transition-id" :param-property "target"}
                                                                                      {:action-property "to" :param-property "init-position"}]}
                                       :wrong-option                   {:type "parallel",
                                                                        :data [{:id "unhighlight-all" :type "action"}
                                                                               {:id "object-revert", :type "action"}
                                                                               {:id "wrong-answer", :type "action"}],}
                                       :correct-option                 {:type "sequence-data",
                                                                        :data [{:id "unhighlight-all" :type "action"}
                                                                               {:type "counter" :counter-action "increase" :counter-id "sorted-objects"}
                                                                               {:id "object-in-right-box", :type "action"}
                                                                               {:id "correct-answer", :type "action"}
                                                                               {:type       "test-var-inequality"
                                                                                :var-name   "sorted-objects",
                                                                                :value      6,
                                                                                :inequality ">=",
                                                                                :success    "finish-round"}]}
                                       :dragged                        {:type "sequence-data"
                                                                        :data [{:type        "copy-variable",
                                                                                :var-name "current-selection-state"
                                                                                :from-params [{:param-property "check-variable", :action-property "from"}]}
                                                                               {:type "set-variable", :var-name "say", :var-value false}
                                                                               {:type "set-variable", :var-name "next-check-collide", :var-value false}

                                                                               {:type        "test-var-scalar",
                                                                                :success     "correct-option",
                                                                                :fail       "object-revert",
                                                                                :value       true,
                                                                                :var-name "current-selection-state"}]}
                                       :highlight                      {:type "sequence-data"
                                                                        :data [{:type        "set-variable",
                                                                                :var-value   true
                                                                                :from-params [{:action-property "var-name" :param-property "check-variable"}]}
                                                                               {:type        "set-attribute" :attr-name "highlight" :attr-value true
                                                                                :from-params [{:action-property "target" :param-property "transition"}]}]}
                                       :unhighlight                    {:type "sequence-data"
                                                                        :data [{:type        "set-variable",
                                                                                :var-value   false
                                                                                :from-params [{:action-property "var-name" :param-property "check-variable"}]}
                                                                               {:type        "set-attribute" :attr-name "highlight" :attr-value false
                                                                                :from-params [{:action-property "target" :param-property "transition"}]}]}
                                       :unhighlight-all     {:type "parallel"
                                                             :data [{:type "set-attribute" :attr-name "highlight" :attr-value false :target "oval-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "circle-table"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "rectable-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "squere-table"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "star-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "triangle-table"}]}

                                       :next-check-collide  {:type "sequence-data"
                                                             :data [{:type     "set-timeout"
                                                                     :action   "check-collide"
                                                                     :interval 10}]}
                                       :check-collide       {:type "sequence-data"
                                                             :data [
                                                                    {:type          "test-transitions-and-pointer-collide",
                                                                     :success       "highlight",
                                                                     :fail          "unhighlight",
                                                                     :transitions   ["oval-box" "circle-table"
                                                                                     "rectangle-box" "square-table"
                                                                                     "star-box" "triangle-table"]
                                                                     :action-params [{:check-variable "oval-box-selected"}
                                                                                     {:check-variable "circle-table-selected"}
                                                                                     {:check-variable "rectangle-box-selected"}
                                                                                     {:check-variable "square-table-selected"}
                                                                                     {:check-variable "star-box-selected"}
                                                                                     {:check-variable "triangle-table-selected"}]}
                                                                    {:type     "test-var-scalar",
                                                                     :success  "next-check-collide",
                                                                     :value    true,
                                                                     :var-name "next-check-collide"}
                                                                    ]}


                                       :init-activity                  {:type "sequence-data"
                                                                        :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-objects"}
                                                                               {:type "action" :id "intro"}]
                                                                        }

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
                                       :tap-instructions              (-> (dialog/default "Tap instructions")
                                                                          (assoc :unique-tag "instructions"))
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
                                       :start-drag                     {:type "sequence-data"
                                                                        :data [{:type        "stop-transition"
                                                                                :from-params [{:action-property "id" :param-property "target"}]}
                                                                               {:type "action", :id "reset-selected-vars"}
                                                                               {:type "set-variable", :var-name "say", :var-value true}
                                                                               {:type "set-variable", :var-name "next-check-collide", :var-value true}
                                                                               {:id "next-say" :type "action"}
                                                                               {:id "next-check-collide" :type "action"}]}

                                       :reset-selected-vars            {:type "sequence-data"
                                                                        :data [
                                                                               {:type "set-variable", :var-name "oval-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "circle-table-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "rectangle-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "square-table-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "star-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "triangle-table-selected", :var-value false}
                                                                               ]
                                                                        }
                                       :intro                          (-> (dialog/default "Introduce task")
                                                                           (assoc :unique-tag "instructions"))
                                       :correct-answer                 {:type               "sequence-data",
                                                                        :editor-type        "dialog",
                                                                        :data               [{:type "sequence-data"
                                                                                              :data [{:type "empty" :duration 0}
                                                                                                     {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                        :phrase             "correct-answer",
                                                                        :phrase-description "correct answer"}
                                       :wrong-answer                   {:type               "sequence-data",
                                                                        :editor-type        "dialog",
                                                                        :data               [{:type "sequence-data"
                                                                                              :data [{:type "empty" :duration 0}
                                                                                                     {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                        :phrase             "wrong-answer",
                                                                        :phrase-description "wrong answer"}
                                       :finish-round                   {:type "sequence-data",
                                                                        :data [{:type "action" :id "finish-round-dialog"}
                                                                               {:type "action" :id "finish-scene"}]}

                                       :finish-round-dialog            (dialog/default "Finish round")
                                       :finish-scene                   {:type "sequence-data",
                                                                        :data [{:type "remove-interval"
                                                                                :id   "check-collide-2"}
                                                                               {:type "action", :id "finish-activity"}]}
                                       :finish-activity                {:type "finish-activity"}
                                       :stop-activity                  {:type "stop-activity"}}
                       :triggers
                       {:back  {:on "back", :action "stop-activity"},
                        :start {:on "start", :action "init-activity"}},
                       :metadata      {:autostart   true
                                       :tracks      [{:title "Round 2"
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
                                                              {:type      "dialog"
                                                               :action-id :tap-instructions}
                                                              {:type "prompt"
                                                               :text "Dialog after all elements correctly found"}
                                                              {:type      "dialog"
                                                               :action-id :finish-round-dialog}]}
                                                     {:title "Round 2 - items"
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
                                                               :action-id :triangle-item}]}]}})
