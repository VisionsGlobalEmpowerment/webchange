(ns webchange.templates.library.categorize.antonyms.round-1
  (:require
    [webchange.templates.library.categorize.templates.common :refer [get-draggable-item]]
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
                                       :right-box  {:type       "image",
                                                    :y          763,
                                                    :x          99,
                                                    :src        "/raw/img/categorize-antonims/right.png"
                                                    :transition "right-box"},

                                       :front-box  {:type       "image",
                                                    :y          763,
                                                    :x          686,
                                                    :src        "/raw/img/categorize-antonims/front.png",
                                                    :transition "front-box"}

                                       :down-box   {:type       "image",
                                                    :y          763,
                                                    :x          1568,
                                                    :src        "/raw/img/categorize-antonims/down.png"
                                                    :transition "down-box"}

                                       :quiet-box  {:type       "image",
                                                    :y          763,
                                                    :x          393,
                                                    :src        "/raw/img/categorize-antonims/quiet.png",
                                                    :transition "quiet-box"}

                                       :day-box    {:type       "image",
                                                    :y          763,
                                                    :x          980,
                                                    :src        "/raw/img/categorize-antonims/day.png",
                                                    :transition "day-box"}

                                       :in-box     {:type       "image",
                                                    :y          763,
                                                    :x          1274,
                                                    :src        "/raw/img/categorize-antonims/in.png"
                                                    :transition "in-box"}

                                       :left-1     (get-draggable-item {:position    {:x 448 :y 256}
                                                                        :src         "/raw/img/categorize-antonims/left.png"
                                                                        :target      "left-1"
                                                                        :say-item    "left-item"
                                                                        :say-correct "left-correct"
                                                                        :box         "right-box"})

                                       :back-1     (get-draggable-item {:position    {:x 701 :y 393}
                                                                        :src         "/raw/img/categorize-antonims/back.png"
                                                                        :target      "back-1"
                                                                        :say-item    "back-item"
                                                                        :say-correct "back-correct"
                                                                        :box         "front-box"})

                                       :up-1       (get-draggable-item {:position    {:x 1035 :y 383}
                                                                        :src         "/raw/img/categorize-antonims/up.png"
                                                                        :target      "up-1"
                                                                        :say-item    "up-item"
                                                                        :say-correct "up-correct"
                                                                        :box         "down-box"})

                                       :loud-1     (get-draggable-item {:position    {:x 701 :y 89}
                                                                        :src         "/raw/img/categorize-antonims/loud.png"
                                                                        :target      "loud-1"
                                                                        :say-item    "loud-item"
                                                                        :say-correct "loud-correct"
                                                                        :box         "quiet-box"})

                                       :night-1    (get-draggable-item {:position    {:x 1121 :y 105}
                                                                        :src         "/raw/img/categorize-antonims/night.png"
                                                                        :target      "night-1"
                                                                        :say-item    "night-item"
                                                                        :say-correct "night-correct"
                                                                        :box         "day-box"})

                                       :out-1      (get-draggable-item {:position    {:x 863 :y 243}
                                                                        :src         "/raw/img/categorize-antonims/out.png"
                                                                        :target      "out-1"
                                                                        :say-item    "out-item"
                                                                        :say-correct "out-correct"
                                                                        :box         "in-box"})}

                       :scene-objects [["background"]
                                       ["right-box" "front-box" "down-box"]
                                       ["quiet-box" "day-box" "in-box"]
                                       ["left-1" "back-1" "up-1"]
                                       ["loud-1" "night-1" "out-1"]],
                       :actions       {:object-in-right-box  {:type        "set-attribute", :attr-name "visible" :attr-value false
                                                              :from-params [{:action-property "target" :param-property "target"}]},

                                       :object-revert        {:type        "transition",
                                                              :from-params [{:action-property "transition-id" :param-property "target"}
                                                                            {:action-property "to" :param-property "init-position"}]}

                                       :wrong-option         {:type "sequence-data",
                                                              :data [{:id "unhighlight-all" :type "action"}
                                                                     {:id "object-revert", :type "action"}
                                                                     {:id "wrong-answer", :type "action"}]}

                                       :correct-option       {:type "sequence-data",
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
                                                                      :success    "finish-scene",}]}

                                       :handle-drag-start    {:type        "stop-transition"
                                                              :from-params [{:action-property "id" :param-property "target"}]}

                                       :handle-drag-move     {:type        "action"
                                                              :from-params [{:action-property "id"
                                                                             :param-property  "say-item"}]}

                                       :handle-drag-end      {:type        "test-var-scalar"
                                                              :from-params [{:action-property "var-name"
                                                                             :param-property  "box"}]
                                                              :value       true
                                                              :success     "correct-option"
                                                              :fail        "object-revert"}

                                       :handle-collide-enter {:type "sequence-data"
                                                              :data [{:type        "set-attribute"
                                                                      :attr-name   "highlight"
                                                                      :attr-value  true
                                                                      :from-params [{:action-property "target" :param-property "target"}]}
                                                                     {:type        "set-variable"
                                                                      :from-params [{:action-property "var-name"
                                                                                     :param-property  "target"}]
                                                                      :var-value   true}]}

                                       :handle-collide-leave {:type "sequence-data"
                                                              :data [{:type        "set-attribute"
                                                                      :attr-name   "highlight"
                                                                      :attr-value  false
                                                                      :from-params [{:action-property "target" :param-property "target"}]}
                                                                     {:type        "set-variable"
                                                                      :from-params [{:action-property "var-name"
                                                                                     :param-property  "target"}]
                                                                      :var-value   false}]}

                                       :unhighlight-all      {:type "parallel"
                                                              :data [{:type "set-attribute" :attr-name "highlight" :attr-value false :target "right-box"}
                                                                     {:type "set-attribute" :attr-name "highlight" :attr-value false :target "front-box"}
                                                                     {:type "set-attribute" :attr-name "highlight" :attr-value false :target "down-box"}
                                                                     {:type "set-attribute" :attr-name "highlight" :attr-value false :target "quiet-box"}
                                                                     {:type "set-attribute" :attr-name "highlight" :attr-value false :target "day-box"}
                                                                     {:type "set-attribute" :attr-name "highlight" :attr-value false :target "in-box"}]}

                                       :left-item            (-> (dialog/default "Left")
                                                                 (assoc :unique-tag "item"))
                                       :left-correct         (-> (dialog/default "Right")
                                                                 (assoc :unique-tag "item"))
                                       :back-item            (-> (dialog/default "Back")
                                                                 (assoc :unique-tag "item"))
                                       :back-correct         (-> (dialog/default "Front")
                                                                 (assoc :unique-tag "item"))
                                       :up-item              (-> (dialog/default "Up")
                                                                 (assoc :unique-tag "item"))
                                       :up-correct           (-> (dialog/default "Down")
                                                                 (assoc :unique-tag "item"))
                                       :loud-item            (-> (dialog/default "Loud")
                                                                 (assoc :unique-tag "item"))
                                       :loud-correct         (-> (dialog/default "Quiet")
                                                                 (assoc :unique-tag "item"))
                                       :night-item           (-> (dialog/default "Night")
                                                                 (assoc :unique-tag "item"))
                                       :night-correct        (-> (dialog/default "Day")
                                                                 (assoc :unique-tag "item"))
                                       :out-item             (-> (dialog/default "Out")
                                                                 (assoc :unique-tag "item"))
                                       :out-correct          (-> (dialog/default "In")
                                                                 (assoc :unique-tag "item"))

                                       :init-activity        {:type "sequence-data"
                                                              :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-objects"}
                                                                     {:type "action" :id "intro"}]}

                                       :intro                {:type               "sequence-data",
                                                              :editor-type        "dialog",
                                                              :data               [{:type "sequence-data"
                                                                                    :data [{:type "empty" :duration 0}
                                                                                           {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                              :phrase             "intro",
                                                              :phrase-description "Introduce task"}
                                       :correct-answer       {:type               "sequence-data",
                                                              :editor-type        "dialog",
                                                              :data               [{:type "sequence-data"
                                                                                    :data [{:type "empty" :duration 0}
                                                                                           {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                              :phrase             "correct-answer",
                                                              :phrase-description "correct answer"}
                                       :wrong-answer         {:type               "sequence-data",
                                                              :editor-type        "dialog",
                                                              :data               [{:type "sequence-data"
                                                                                    :data [{:type "empty" :duration 0}
                                                                                           {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                              :phrase             "wrong-answer",
                                                              :phrase-description "wrong answer"}
                                       :finish-scene         {:type "sequence-data",
                                                              :data [{:type "action" :id "finish-dialog"}
                                                                     {:type "action", :id "finish-activity"}]}
                                       :finish-dialog        {:type               "sequence-data",
                                                              :editor-type        "dialog",
                                                              :data               [{:type "sequence-data"
                                                                                    :data [{:type "empty" :duration 0}
                                                                                           {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                              :phrase             "finish-dialog",
                                                              :phrase-description "finish dialog"}
                                       :stop-activity        {:type "stop-activity"},
                                       :finish-activity      {:type "finish-activity"}},

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
                                                             :action-id :out-correct}]}]}})
