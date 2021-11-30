(ns webchange.templates.library.categorize-synonyms.round-1
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(def template-round-1 {:assets        [
                                       {:url "/raw/img/categorize-synonyms/background.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/afraid.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/scared.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/big.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/large.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/child.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/kid.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/glad.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/happy.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/cold.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/chilly.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/garbage.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/trash.png", :size 10, :type "image"}
                                       ],
                       :objects       {:background {:type "background", :src "/raw/img/categorize-synonyms/background.png"},
                                       :chilly-box {:type "image",
                                                    :x    980,
                                                    :y    763,
                                                    :src  "/raw/img/categorize-synonyms/chilly.png",
                                                    },
                                       :kid-box    {:type "image",
                                                    :x    1274,
                                                    :y    763,
                                                    :src  "/raw/img/categorize-synonyms/kid.png",
                                                    }
                                       :big-box    {:type "image",
                                                    :x    393,
                                                    :y    763,
                                                    :src  "/raw/img/categorize-synonyms/big.png",
                                                    }
                                       :trash-box  {:type "image",
                                                    :x    1568,
                                                    :y    763, ;
                                                    :src  "/raw/img/categorize-synonyms/trash.png",
                                                    }
                                       :afraid-box {:type "image",
                                                    :x    686,
                                                    :y    763,
                                                    :src  "/raw/img/categorize-synonyms/afraid.png",
                                                    }
                                       :glad-box   {:type "image",
                                                    :x    99,
                                                    :y    763,
                                                    :src  "/raw/img/categorize-synonyms/glad.png",
                                                    }
                                       :cold-1     {
                                                    :type      "image",
                                                    :x         701,
                                                    :y         89,
                                                    :src       "/raw/img/categorize-synonyms/cold.png",
                                                    :draggable true,
                                                    :actions   {:drag-start {:type   "action",
                                                                             :on     "drag-start",
                                                                             :id     "start-drag"
                                                                             :params {:say-item         "cold-item"
                                                                                      :target           "cold-1"
                                                                                      :placement-target "chilly-box"}
                                                                             }
                                                                :drag-end
                                                                            {:id     "dragged",
                                                                             :on     "drag-end",
                                                                             :type   "action",
                                                                             :params {:box            "chilly-box"
                                                                                      :target         "cold-1"
                                                                                      :init-position  {:x 701,
                                                                                                       :y 89, :duration 1}
                                                                                      :check-variable "chilly-box-selected"
                                                                                      :correct-drop   "cold-correct"
                                                                                      }}},},
                                       :child-1    {
                                                    :type      "image",
                                                    :x         863,
                                                    :y         243,
                                                    :src       "/raw/img/categorize-synonyms/child.png",
                                                    :draggable true,
                                                    :actions   {:drag-start {
                                                                             :type   "action",
                                                                             :on     "drag-start",
                                                                             :id     "start-drag"
                                                                             :params {:say-item         "child-item"
                                                                                      :target           "child-1"
                                                                                      :placement-target "kid-box"}
                                                                             }
                                                                :drag-end
                                                                            {:id     "dragged",
                                                                             :on     "drag-end",
                                                                             :type   "action",
                                                                             :params {:box            "kid-box"
                                                                                      :target         "child-1"
                                                                                      :init-position  {:x 863,
                                                                                                       :y 243, :duration 1}
                                                                                      :check-variable "kid-box-selected"
                                                                                      :correct-drop   "child-correct"
                                                                                      }}},},
                                       :large-1    {
                                                    :type      "image",
                                                    :x         448,
                                                    :y         256,
                                                    :src       "/raw/img/categorize-synonyms/large.png",
                                                    :draggable true,
                                                    :actions   {:drag-start {
                                                                             :type   "action",
                                                                             :on     "drag-start",
                                                                             :id     "start-drag"
                                                                             :params {:say-item         "large-item"
                                                                                      :target           "large-1"
                                                                                      :placement-target "big-box"}
                                                                             }
                                                                :drag-end
                                                                            {:id     "dragged",
                                                                             :on     "drag-end",
                                                                             :type   "action",
                                                                             :params {:box            "big-box"
                                                                                      :target         "large-1"
                                                                                      :init-position  {:x 448,
                                                                                                       :y 256, :duration 1}
                                                                                      :check-variable "big-box-selected"
                                                                                      :correct-drop   "large-correct"
                                                                                      }}},}
                                       :garbage-1  {
                                                    :type      "image",
                                                    :x         1035,
                                                    :y         383,
                                                    :src       "/raw/img/categorize-synonyms/garbage.png",
                                                    :draggable true,
                                                    :actions   {:drag-start {
                                                                             :type   "action",
                                                                             :on     "drag-start",
                                                                             :id     "start-drag"
                                                                             :params {:say-item         "garbage-item"
                                                                                      :transition       "garbage-1",
                                                                                      :placement-target "trash-box"}
                                                                             }
                                                                :drag-end
                                                                            {:id     "dragged",
                                                                             :on     "drag-end",
                                                                             :type   "action",
                                                                             :params {:box            "trash-box"
                                                                                      :target         "garbage-1"
                                                                                      :init-position  {:x 1035,
                                                                                                       :y 383, :duration 1}
                                                                                      :check-variable "trash-box-selected"
                                                                                      :correct-drop   "garbage-correct"}}},}
                                       :scared-1   {
                                                    :type      "image",
                                                    :x         1121,
                                                    :y         105,
                                                    :src       "/raw/img/categorize-synonyms/scared.png",
                                                    :draggable true,
                                                    :actions   {:drag-start {
                                                                             :type   "action",
                                                                             :on     "drag-start",
                                                                             :id     "start-drag"
                                                                             :params {:say-item         "scared-item"
                                                                                      :target           "scared-1"
                                                                                      :placement-target "afraid-box"}
                                                                             }
                                                                :drag-end
                                                                            {:id     "dragged",
                                                                             :on     "drag-end",
                                                                             :type   "action",
                                                                             :params {:box            "afraid-box"
                                                                                      :target         "scared-1"
                                                                                      :init-position  {:x 1121,
                                                                                                       :y 105, :duration 1}
                                                                                      :check-variable "afraid-box-selected"
                                                                                      :correct-drop   "scared-correct"
                                                                                      }}}}
                                       :happy-1    {
                                                    :type      "image",
                                                    :x         701,
                                                    :y         393,
                                                    :src       "/raw/img/categorize-synonyms/happy.png",
                                                    :draggable true,
                                                    :actions   {:drag-start {
                                                                             :type   "action",
                                                                             :on     "drag-start",
                                                                             :id     "start-drag"
                                                                             :params {:say-item         "happy-item"
                                                                                      :target           "happy-1"
                                                                                      :placement-target "glad-box"}
                                                                             }
                                                                :drag-end
                                                                            {:id     "dragged",
                                                                             :on     "drag-end",
                                                                             :type   "action",
                                                                             :params {:box            "glad-box"
                                                                                      :target         "happy-1"
                                                                                      :init-position  {:x 701,
                                                                                                       :y 393, :duration 1}
                                                                                      :check-variable "glad-box-selected"
                                                                                      :correct-drop   "happy-correct"
                                                                                      }}}}},
                       :scene-objects [["background"]
                                       ["chilly-box" "kid-box" "big-box"]
                                       ["trash-box" "afraid-box" "glad-box"]
                                       ["cold-1" "child-1" "large-1"]
                                       ["garbage-1" "scared-1" "happy-1"]],
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
                                                                    {:type        "action"
                                                                     :from-params [{:param-property  "correct-drop",
                                                                                    :action-property "id"}]}
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
                                                                     :var-name    "current-selection-state"
                                                                     :from-params [{:param-property "check-variable", :action-property "from"}]}
                                                                    {:type "set-variable", :var-name "say", :var-value false}
                                                                    {:type "set-variable", :var-name "next-check-collide", :var-value false}

                                                                    {:type     "test-var-scalar",
                                                                     :success  "correct-option",
                                                                     :fail     "object-revert",
                                                                     :value    true,
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
                                                             :data [{:type "set-attribute" :attr-name "highlight" :attr-value false :target "kid-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "chilly-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "big-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "trash-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "afraid-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "glad-box"}]}
                                       :next-check-collide  {:type "sequence-data"
                                                             :data [{:type     "set-timeout"
                                                                     :action   "check-collide"
                                                                     :interval 10}]}
                                       :check-collide       {:type "sequence-data"
                                                             :data [
                                                                    {:type          "test-transitions-and-pointer-collide",
                                                                     :success       "highlight",
                                                                     :fail          "unhighlight",
                                                                     :transitions   ["kid-box" "chilly-box" "big-box" "trash-box" "afraid-box" "glad-box"]
                                                                     :action-params [{:check-variable "kid-box-selected"}
                                                                                     {:check-variable "chilly-box-selected"}
                                                                                     {:check-variable "big-box-selected"}
                                                                                     {:check-variable "trash-box-selected"}
                                                                                     {:check-variable "afraid-box-selected"}
                                                                                     {:check-variable "glad-box-selected"}]}
                                                                    {:type     "test-var-scalar",
                                                                     :success  "next-check-collide",
                                                                     :value    true,
                                                                     :var-name "next-check-collide"}
                                                                    ]}

                                       :cold-item           (-> (dialog/default "cold")
                                                                (assoc :unique-tag "item"))
                                       :cold-correct        (-> (dialog/default "cold correct")
                                                                (assoc :unique-tag "item"))
                                       :child-item          (-> (dialog/default "child")
                                                                (assoc :unique-tag "item"))
                                       :child-correct       (-> (dialog/default "child correct")
                                                                (assoc :unique-tag "item"))
                                       :large-item          (-> (dialog/default "large")
                                                                (assoc :unique-tag "item"))
                                       :large-correct       (-> (dialog/default "large correct")
                                                                (assoc :unique-tag "item"))
                                       :garbage-item        (-> (dialog/default "garbage")
                                                                (assoc :unique-tag "item"))
                                       :garbage-correct     (-> (dialog/default "garbage correct")
                                                                (assoc :unique-tag "item"))
                                       :scared-item         (-> (dialog/default "scared")
                                                                (assoc :unique-tag "item"))
                                       :scared-correct      (-> (dialog/default "scared correct")
                                                                (assoc :unique-tag "item"))
                                       :happy-item          (-> (dialog/default "happy")
                                                                (assoc :unique-tag "item"))
                                       :happy-correct       (-> (dialog/default "happy correct")
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
                                                                    {:type "set-variable", :var-name "kid-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "chilly-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "big-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "trash-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "afraid-box-selected", :var-value false}
                                                                    {:type "set-variable", :var-name "glad-box-selected", :var-value false}]}
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
                                       :stop-activity       {:type "stop-activity"}
                                       :finish-activity     {:type "finish-activity"}
                                       }
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
                                                             :action-id :cold-item}
                                                            {:type      "dialog"
                                                             :action-id :cold-correct}
                                                            {:type      "dialog"
                                                             :action-id :child-item}
                                                            {:type      "dialog"
                                                             :action-id :child-correct}
                                                            {:type      "dialog"
                                                             :action-id :large-item}
                                                            {:type      "dialog"
                                                             :action-id :large-correct}
                                                            {:type      "dialog"
                                                             :action-id :garbage-item}
                                                            {:type      "dialog"
                                                             :action-id :garbage-correct}
                                                            {:type      "dialog"
                                                             :action-id :scared-item}
                                                            {:type      "dialog"
                                                             :action-id :scared-correct}
                                                            {:type      "dialog"
                                                             :action-id :happy-item}
                                                            {:type      "dialog"
                                                             :action-id :happy-correct}
                                                            ]}]}})
