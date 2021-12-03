(ns webchange.templates.library.categorize-antonims.round-2
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(def template-round-2 {:assets        [
                                       {:url "/raw/img/categorize-antonims/background-class.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/surface.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/decoration.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/day.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/night.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/down.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/up.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/back.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/front.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/in.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/out.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/left.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/right.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/loud.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/quiet.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/question.png", :size 10, :type "image"}
                                       ],
                       :objects       {:layered-background {:type       "layered-background",
                                                            :background {:src "/raw/img/categorize-antonims/background-class.png"},
                                                            :decoration {:src "/raw/img/categorize-antonims/decoration.png"},
                                                            :surface    {:src "/raw/img/categorize-antonims/surface.png"}
                                                            },
                                       :right-box          {:type "image",
                                                            :x    99,
                                                            :y    763,
                                                            :src  "/raw/img/categorize-antonims/right.png",
                                                            },
                                       :front-box          {:type "image",
                                                            :x    686,
                                                            :y    763,
                                                            :src  "/raw/img/categorize-antonims/front.png",
                                                            }
                                       :down-box           {:type "image",
                                                            :x    1568,
                                                            :y    763,
                                                            :src  "/raw/img/categorize-antonims/down.png",
                                                            }
                                       :quiet-box          {:type "image",
                                                            :y    763,
                                                            :x    393,
                                                            :src  "/raw/img/categorize-antonims/quiet.png",
                                                            }
                                       :day-box            {:type "image",
                                                            :x    980,
                                                            :y    763,
                                                            :src  "/raw/img/categorize-antonims/day.png",
                                                            }
                                       :in-box             {:type "image",
                                                            :x    1274,
                                                            :y    763,
                                                            :src  "/raw/img/categorize-antonims/in.png",
                                                            }
                                       :left-1             {
                                                            :type      "image",
                                                            :x         1635,
                                                            :y         107,
                                                            :scale     {:x 0.65, :y 0.65}
                                                            :src       "/raw/img/categorize-antonims/left.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-item "left-item"
                                                                                              :target   "left-1"}}
                                                                        :drag-end
                                                                                    {:id     "dragged",
                                                                                     :on     "drag-end",
                                                                                     :type   "action",
                                                                                     :params {:box            "right-box"
                                                                                              :target         "left-1"
                                                                                              :init-position  {:x 1635,
                                                                                                               :y 107, :duration 1}
                                                                                              :check-variable "right-box-selected"
                                                                                              :correct-drop   "left-correct"
                                                                                              }}},},
                                       :back-1             {
                                                            :type      "image",
                                                            :x         415,
                                                            :y         354,
                                                            :scale     {:x 0.65, :y 0.65}
                                                            :src       "/raw/img/categorize-antonims/back.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {
                                                                                     :type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-item "back-item"
                                                                                              :target   "back-1"}}
                                                                        :drag-end
                                                                                    {:id     "dragged",
                                                                                     :on     "drag-end",
                                                                                     :type   "action",
                                                                                     :params {:box            "front-box"
                                                                                              :target         "back-1"
                                                                                              :init-position  {:x 415,
                                                                                                               :y 354, :duration 1}
                                                                                              :check-variable "front-box-selected"
                                                                                              :correct-drop   "back-correct"
                                                                                              }}},},
                                       :up-1               {
                                                            :type      "image",
                                                            :x         801,
                                                            :y         481,
                                                            :scale     {:x 0.65, :y 0.65}
                                                            :src       "/raw/img/categorize-antonims/up.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {
                                                                                     :type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-item "up-item"
                                                                                              :target   "up-1"}}
                                                                        :drag-end
                                                                                    {:id     "dragged",
                                                                                     :on     "drag-end",
                                                                                     :type   "action",
                                                                                     :params {:box            "down-box"
                                                                                              :target         "up-1"
                                                                                              :init-position  {:x 801,
                                                                                                               :y 481, :duration 1}
                                                                                              :check-variable "down-box-selected"
                                                                                              :correct-drop   "up-correct"
                                                                                              }}},}
                                       :loud-1             {
                                                            :type      "image",
                                                            :x         790,
                                                            :y         160,
                                                            :scale     {:x 0.65, :y 0.65}
                                                            :src       "/raw/img/categorize-antonims/loud.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {
                                                                                     :type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-item "loud-item"
                                                                                              :target   "loud-1"}}
                                                                        :drag-end
                                                                                    {:id     "dragged",
                                                                                     :on     "drag-end",
                                                                                     :type   "action",
                                                                                     :params {:box            "quiet-box"
                                                                                              :target         "loud-1"
                                                                                              :init-position  {:x 790,
                                                                                                               :y 160, :duration 1}
                                                                                              :check-variable "quiet-box-selected"
                                                                                              :correct-drop   "loud-correct"
                                                                                              }}},}
                                       :night-1            {
                                                            :type      "image",
                                                            :x         1274,
                                                            :y         481,
                                                            :scale     {:x 0.65, :y 0.65}
                                                            :src       "/raw/img/categorize-antonims/night.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {
                                                                                     :type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-item "night-item"
                                                                                              :target   "night-1"}}
                                                                        :drag-end
                                                                                    {:id     "dragged",
                                                                                     :on     "drag-end",
                                                                                     :type   "action",
                                                                                     :params {:box            "day-box"
                                                                                              :target         "night-1"
                                                                                              :init-position  {:x 1274,
                                                                                                               :y 481, :duration 1}
                                                                                              :check-variable "day-box-selected"
                                                                                              :correct-drop   "night-correct"}}},}
                                       :out-1              {
                                                            :type      "image",
                                                            :x         1095,
                                                            :y         223,
                                                            :scale     {:x 0.65, :y 0.65}
                                                            :src       "/raw/img/categorize-antonims/out.png",
                                                            :draggable true,
                                                            :actions   {:drag-start {
                                                                                     :type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag"
                                                                                     :params {:say-item "out-item"
                                                                                              :target   "out-1"}}
                                                                        :drag-end
                                                                                    {:id     "dragged",
                                                                                     :on     "drag-end",
                                                                                     :type   "action",
                                                                                     :params {:box            "in-box"
                                                                                              :target         "out-1"
                                                                                              :init-position  {:x 1095,
                                                                                                               :y 223, :duration 1}
                                                                                              :check-variable "in-box-selected"
                                                                                              :correct-drop   "out-correct"
                                                                                              }}},}
                                       :librarian          {:type    "animation",
                                                            :x       250,
                                                            :y       1000,
                                                            :width   351,
                                                            :height  717,
                                                            :anim    "idle",
                                                            :name    "senoravaca",
                                                            :skin    "lion",
                                                            :speed   0.3,
                                                            :start   true
                                                            :editable? {:select true
                                                                        :show-in-tree? true}
                                                            :actions {:click {:id "tap-instructions" :on "click" :type "action"}}}
                                       },
                       :scene-objects [["layered-background"]
                                       ["librarian"]
                                       ["right-box" "front-box" "down-box"]
                                       ["quiet-box" "day-box" "in-box"]
                                       ["left-1" "back-1" "up-1"]
                                       ["loud-1" "night-1" "out-1"]],
                       :actions       {:object-in-right-box {:type        "set-attribute", :attr-name "visible" :attr-value false
                                                             :from-params [{:action-property "target" :param-property "target"}]},
                                       :object-revert       {:type        "transition",
                                                             :from-params [{:action-property "transition-id" :param-property "target"}
                                                                           {:action-property "to" :param-property "init-position"}]}
                                       :wrong-option        {:type "parallel",
                                                             :data [{:id "unhighlight-all" :type "action"}
                                                                    {:id "object-revert", :type "action"}
                                                                    {:id "wrong-answer", :type "action"}],}
                                       :correct-option      {:type "sequence-data",
                                                             :data [{:id "unhighlight-all" :type "action"}
                                                                    {:type "counter" :counter-action "increase" :counter-id "sorted-objects"}
                                                                    {:id "object-in-right-box", :type "action"}
                                                                    {:type        "action"
                                                                     :from-params [{:param-property  "correct-drop",
                                                                                    :action-property "id"}]}
                                                                    {:id "correct-answer", :type "action"}
                                                                    {:type       "test-var-inequality"
                                                                     :var-name   "sorted-objects",
                                                                     :value      6,
                                                                     :inequality ">=",
                                                                     :success    "finish-round"}]}

                                       :dragged             {:type "sequence-data"
                                                             :data [{:type        "copy-variable",
                                                                     :var-name    "current-selection-state"
                                                                     :from-params [{:param-property "check-variable", :action-property "from"}]}
                                                                    {:type "set-variable", :var-name "say", :var-value false}
                                                                    {:type "set-variable", :var-name "next-check-collide", :var-value false}

                                                                    {:type     "test-var-scalar",
                                                                     :success  "correct-option",
                                                                     :fail     "object-revert",
                                                                     :value    true,
                                                                     :var-name "current-selection-state"}]}
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
                                                             :data [{:type "set-attribute" :attr-name "highlight" :attr-value false :target "front-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "right-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "down-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "quiet-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "day-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "in-box"}]}

                                       :next-check-collide  {:type "sequence-data"
                                                             :data [{:type     "set-timeout"
                                                                     :action   "check-collide"
                                                                     :interval 10}]}
                                       :check-collide       {:type "sequence-data"
                                                             :data [
                                                                    {:type          "test-transitions-and-pointer-collide",
                                                                     :success       "highlight",
                                                                     :fail          "unhighlight",
                                                                     :transitions   ["front-box" "right-box" "down-box" "quiet-box" "day-box" "in-box"]
                                                                     :action-params [{:check-variable "front-box-selected"}
                                                                                     {:check-variable "right-box-selected"}
                                                                                     {:check-variable "down-box-selected"}
                                                                                     {:check-variable "quiet-box-selected"}
                                                                                     {:check-variable "day-box-selected"}
                                                                                     {:check-variable "in-box-selected"}]}
                                                                    {:type     "test-var-scalar",
                                                                     :success  "next-check-collide",
                                                                     :value    true,
                                                                     :var-name "next-check-collide"}
                                                                    ]}

                                       :init-activity       {:type "sequence-data"
                                                             :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-objects"}
                                                                    {:type "action" :id "intro"}]
                                                             }

                                       :left-item           (-> (dialog/default "Left")
                                                                (assoc :unique-tag "item"))
                                       :left-correct        (-> (dialog/default "Right")
                                                                (assoc :unique-tag "item"))
                                       :back-item           (-> (dialog/default "Back")
                                                                (assoc :unique-tag "item"))
                                       :back-correct        (-> (dialog/default "Front")
                                                                (assoc :unique-tag "item"))
                                       :up-item             (-> (dialog/default "Up")
                                                                (assoc :unique-tag "item"))
                                       :up-correct          (-> (dialog/default "Down")
                                                                (assoc :unique-tag "item"))
                                       :loud-item           (-> (dialog/default "Loud")
                                                                (assoc :unique-tag "item"))
                                       :loud-correct        (-> (dialog/default "Quiet")
                                                                (assoc :unique-tag "item"))
                                       :night-item          (-> (dialog/default "Night")
                                                                (assoc :unique-tag "item"))
                                       :night-correct       (-> (dialog/default "Day")
                                                                (assoc :unique-tag "item"))
                                       :out-item            (-> (dialog/default "Out")
                                                                (assoc :unique-tag "item"))
                                       :out-correct         (-> (dialog/default "In")
                                                                (assoc :unique-tag "item"))
                                       :tap-instructions    (-> (dialog/default "Tap instructions")
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
                                       :start-drag          {:type "sequence-data"
                                                             :data [{:type        "stop-transition"
                                                                     :from-params [{:action-property "id" :param-property "target"}]}
                                                                    {:type "action", :id "reset-selected-vars"}
                                                                    {:type "set-variable", :var-name "say", :var-value true}
                                                                    {:type "set-variable", :var-name "next-check-collide", :var-value true}
                                                                    {:id "next-say" :type "action"}
                                                                    {:id "next-check-collide" :type "action"}]}
                                       :reset-selected-vars {:type "sequence-data"
                                                             :data [
                                                                    {:type "set-variable", :var-name "in-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "day-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "quiet-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "down-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "front-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "right-box-selected", :var-value false}]}
                                       :intro               (-> (dialog/default "Introduce task")
                                                                (assoc :unique-tag "instructions"))
                                       :correct-answer      {:type               "sequence-data",
                                                             :editor-type        "dialog",
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                             :phrase             "correct-answer",
                                                             :phrase-description "correct answer"}
                                       :wrong-answer        {:type               "sequence-data",
                                                             :editor-type        "dialog",
                                                             :data               [{:type "sequence-data"
                                                                                   :data [{:type "empty" :duration 0}
                                                                                          {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                             :phrase             "wrong-answer",
                                                             :phrase-description "wrong answer"}

                                       :finish-round        {:type "sequence-data",
                                                             :data [{:type "action" :id "finish-round-dialog"}
                                                                    {:type "action" :id "finish-scene"}]}

                                       :finish-round-dialog (dialog/default "Finish round")
                                       :finish-scene        {:type "sequence-data",
                                                             :data [{:type "remove-interval"
                                                                     :id   "check-collide-2"}
                                                                    {:type "action", :id "finish-activity"}]}
                                       :finish-activity     {:type "finish-activity"}
                                       :stop-activity       {:type "stop-activity"}}
                       :triggers
                                      {:back  {:on "back", :action "stop-activity"},
                                       :start {:on "start", :action "init-activity"}},
                       :metadata      {:autostart   true
                                       :last-insert "technical-question-placeholder"
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
                                                               :action-id :left-item}
                                                              {:type      "dialog"
                                                               :action-id :left-correct}
                                                              {:type      "dialog"
                                                               :action-id :back-item}
                                                              {:type      "dialog"
                                                               :action-id :back-correct}
                                                              {:type      "dialog"
                                                               :action-id :up-item}
                                                              {:type      "dialog"
                                                               :action-id :up-correct}
                                                              {:type      "dialog"
                                                               :action-id :loud-item}
                                                              {:type      "dialog"
                                                               :action-id :loud-correct}
                                                              {:type      "dialog"
                                                               :action-id :night-item}
                                                              {:type      "dialog"
                                                               :action-id :night-correct}
                                                              {:type      "dialog"
                                                               :action-id :out-item}
                                                              {:type      "dialog"
                                                               :action-id :out-correct}]}]},})
