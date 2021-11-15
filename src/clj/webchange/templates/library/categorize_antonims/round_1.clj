(ns webchange.templates.library.categorize-antonims.round-1
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(def template-round-1 {:assets        [{:url "/raw/img/categorize-antonims/background.png", :size 10, :type "image"}
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
                                       {:url "/raw/img/categorize-antonims/quiet.png", :size 10, :type "image"}],
                       :objects       {:background {:type "background", :src "/raw/img/categorize-antonims/background.png"},
                                       :right-box {:type       "image",
                                                   :y          763,
                                                   :x          99,
                                                   :src        "/raw/img/categorize-antonims/right.png",},
                                       :front-box  {:type       "image",
                                                    :y          763,
                                                    :x          686,
                                                    :src        "/raw/img/categorize-antonims/front.png",
                                                    }
                                       :down-box   {:type       "image",
                                                    :y          763,
                                                    :x          1568,
                                                    :src        "/raw/img/categorize-antonims/down.png",
                                                    }
                                       :quiet-box  {:type       "image",
                                                    :y          763,
                                                    :x          393,
                                                    :src        "/raw/img/categorize-antonims/quiet.png",
                                                    }
                                       :day-box    {:type       "image",
                                                    :y          763,
                                                    :x          980,
                                                    :src        "/raw/img/categorize-antonims/day.png",
                                                    }
                                       :in-box     {:type       "image",
                                                    :y          763,
                                                    :x          1274,
                                                    :src        "/raw/img/categorize-antonims/in.png",
                                                    }
                                       :left-1     {
                                                    :type       "image",
                                                    :y          256,
                                                    :x          448,
                                                    :src        "/raw/img/categorize-antonims/left.png",
                                                    :draggable  true,
                                                    :actions    {:drag-start {:type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"
                                                                              :params {:say-item "left-item"
                                                                                       :placement-target "right-box"
                                                                                       :target         "left-1"}
                                                                              }
                                                                 :drag-end
                                                                 {:id     "dragged",
                                                                  :on     "drag-end",
                                                                  :type   "action",
                                                                  :params {:box            "right-box"
                                                                           :target         "left-1"
                                                                           :init-position  {:y 256, :x 448, :duration 1}
                                                                           :check-variable "right-box-selected"
                                                                           }}}},
                                       :back-1     {
                                                    :type       "image",
                                                    :y          393,
                                                    :x          701,
                                                    :src        "/raw/img/categorize-antonims/back.png",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"
                                                                              :params {:say-item "back-item"
                                                                                       :placement-target "front-box"
                                                                                       :target         "back-1"}
                                                                              }
                                                                 :drag-end
                                                                 {:id     "dragged",
                                                                  :on     "drag-end",
                                                                  :type   "action",
                                                                  :params {:box            "front-box"
                                                                           :target         "back-1"
                                                                           :init-position  {:y 393,
                                                                                            :x 701, :duration 1}
                                                                           :check-variable "front-box-selected"
                                                                           }}},
                                                    :states     {:hide {:visible false}}},
                                       :up-1       {
                                                    :type       "image",
                                                    :x          1035,
                                                    :y          383,
                                                    :src        "/raw/img/categorize-antonims/up.png",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"
                                                                              :params {:say-item "up-item"
                                                                                       :placement-target "down-box"
                                                                                       :target         "up-1"}
                                                                              }
                                                                 :drag-end
                                                                 {:id     "dragged",
                                                                  :on     "drag-end",
                                                                  :type   "action",
                                                                  :params {:box            "down-box"
                                                                           :target         "up-1"
                                                                           :init-position  {:x 1035,
                                                                                            :y 383, :duration 1}
                                                                           :check-variable "down-box-selected"
                                                                           }}},}
                                       :loud-1     {
                                                    :type       "image",
                                                    :y          89,
                                                    :x          701,
                                                    :src        "/raw/img/categorize-antonims/loud.png",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"
                                                                              :params {:say-item "loud-item"
                                                                                       :target         "loud-1"
                                                                                       :placement-target "quiet-box"}
                                                                              }
                                                                 :drag-end
                                                                 {:id     "dragged",
                                                                  :on     "drag-end",
                                                                  :type   "action",
                                                                  :params {:box            "quiet-box"
                                                                           :target         "loud-1"
                                                                           :init-position  {:y 89,
                                                                                            :x 701, :duration 1}
                                                                           :check-variable "quiet-box-selected"
                                                                           }}},}
                                       :night-1    {
                                                    :type       "image",
                                                    :x          1121,
                                                    :y          105,
                                                    :src        "/raw/img/categorize-antonims/night.png",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"
                                                                              :params {:say-item "night-item"
                                                                                       :target         "night-1"
                                                                                       :placement-target "day-box"}
                                                                              }
                                                                 :drag-end
                                                                 {:id     "dragged",
                                                                  :on     "drag-end",
                                                                  :type   "action",
                                                                  :params {:box            "day-box"
                                                                           :target         "night-1"
                                                                           :init-position  {:x 1121,
                                                                                            :y 105, :duration 1}
                                                                           :check-variable "day-box-selected"
                                                                           }}}}
                                       :out-1      {
                                                    :type       "image",
                                                    :x          863,
                                                    :y          243,
                                                    :src        "/raw/img/categorize-antonims/out.png",
                                                    :draggable  true,
                                                    :actions    {:drag-start {
                                                                              :type "action",
                                                                              :on   "drag-start",
                                                                              :id   "start-drag"
                                                                              :params {:say-item "out-item"
                                                                                       :target         "out-1"
                                                                                       :placement-target "in-box"}
                                                                              }
                                                                 :drag-end
                                                                 {:id     "dragged",
                                                                  :on     "drag-end",
                                                                  :type   "action",
                                                                  :params {:box            "in-box"
                                                                           :target         "out-1"
                                                                           :init-position  {:x 863,
                                                                                            :y 243, :duration 1}
                                                                           :check-variable "in-box-selected"
                                                                           }}}}

                                       },
                       :scene-objects [["background"]
                                       ["right-box" "front-box" "down-box"]
                                       ["quiet-box" "day-box" "in-box"]
                                       ["left-1" "back-1" "up-1"]
                                       ["loud-1" "night-1" "out-1"]],
                       :actions       {:object-in-right-box {:type        "set-attribute", :attr-name "visible" :attr-value false
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
                                                                     :success    "finish-scene",
                                                                     }]}

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
                                                             :data [{:type "set-attribute" :attr-name "highlight" :attr-value false :target "yellow-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "blue-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "red-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "purple-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "orange-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "green-box"}]}

                                       :next-check-collide            {:type "sequence-data"
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

                                       :left-item        (-> (dialog/default "Left")
                                                             (assoc :unique-tag "item"))
                                       :back-item          (-> (dialog/default "Back")
                                                               (assoc :unique-tag "item"))
                                       :up-item           (-> (dialog/default "Up")
                                                              (assoc :unique-tag "item"))
                                       :loud-item        (-> (dialog/default "Loud")
                                                             (assoc :unique-tag "item"))
                                       :night-item        (-> (dialog/default "Night")
                                                              (assoc :unique-tag "item"))
                                       :out-item         (-> (dialog/default "Out")
                                                             (assoc :unique-tag "item"))

                                       :say-item           {:type "sequence-data"
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
                                                                    {:type "action" :id "intro"}]}
                                       :reset-selected-vars {:type "sequence-data"
                                                             :data [{:type "set-variable", :var-name "front-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "right-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "down-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "quiet-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "day-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "in-box-selected", :var-value false}]}
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
                                       :finish-scene        {:type "sequence-data",
                                                             :data [{:type "set-variable", :var-name "next-check-collide", :var-value false}
                                                                    {:type "action" :id "finish-dialog"}
                                                                    {:type "action", :id "finish-activity"}]}
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
                                                             :action-id :left-item}
                                                            {:type      "dialog"
                                                             :action-id :back-item}
                                                            {:type      "dialog"
                                                             :action-id :up-item}
                                                            {:type      "dialog"
                                                             :action-id :loud-item}
                                                            {:type      "dialog"
                                                             :action-id :night-item}
                                                            {:type      "dialog"
                                                             :action-id :out-item}]}]}})
