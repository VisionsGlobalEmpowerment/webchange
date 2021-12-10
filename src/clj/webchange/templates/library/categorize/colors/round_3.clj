(ns webchange.templates.library.categorize.colors.round-3
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(def template-round-3 {:assets        [{:url "/raw/img/categorize/background-3.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/02.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/03.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/yellow_box_small.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/blue_box_small.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/red_box_small.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/blue_crayons.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/red_crayons.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize/yellow_crayons.png", :size 10, :type "image"}],
                       :objects       {:layered-background {:type       "layered-background",
                                                            :background {:src "/raw/img/categorize/background-3.png"},
                                                            :surface    {:src "/raw/img/categorize/02.png"},
                                                            :decoration {:src "/raw/img/categorize/03.png"}}
                                       :yellow-box
                                                           {:type "image",
                                                            :x    943,
                                                            :y    628,
                                                            :src  "/raw/img/categorize/yellow_box_small.png"}
                                       :blue-box           {:type "image",
                                                            :x    1352,
                                                            :y    490,
                                                            :src  "/raw/img/categorize/blue_box_small.png"}
                                       :red-box            {:type "image",
                                                            :x    500,
                                                            :y    506,
                                                            :src  "/raw/img/categorize/red_box_small.png"},
                                       :red-crayon         {:type      "image",
                                                            :x         776,
                                                            :y         501,
                                                            :rotation  -90,
                                                            :scale     0.35
                                                            :src       "/raw/img/categorize/red_crayons.png",
                                                            :states    {:init-position {:x 776, :y 501,}},
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag",
                                                                                     :params {:colliders ["yellow-box" "blue-box" "red-box"
                                                                                                          "purple-table" "orange-table"
                                                                                                          "green-table"]
                                                                                              :self      "red-crayon"
                                                                                              :say-color "red-color"
                                                                                              :target    "red-box"}}
                                                                        :drag-end   {:id     "stop-drag-hide",
                                                                                     :on     "drag-end",
                                                                                     :type   "action",
                                                                                     :params {:colliders     ["yellow-box" "blue-box" "red-box"
                                                                                                              "purple-table" "orange-table"
                                                                                                              "green-table"]
                                                                                              :self          "red-crayon"
                                                                                              :target        "red-box"
                                                                                              :init-position {:x        776,
                                                                                                              :y        501,
                                                                                                              :duration 1}}}}}
                                       :purple-crayon      {:type      "image",
                                                            :x         60,
                                                            :y         1054,
                                                            :rotation  -90
                                                            :scale     0.35
                                                            :src       "/raw/img/categorize/purple_crayons.png",
                                                            :states    {:init-position {:x 60, :y 1054}},
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag",
                                                                                     :params {:colliders ["yellow-box" "blue-box" "red-box"
                                                                                                          "purple-table" "orange-table"
                                                                                                          "green-table"]
                                                                                              :self      "purple-crayon"
                                                                                              :say-color "purple-color"
                                                                                              :target    "purple-table"}}
                                                                        :drag-end   {:id     "stop-drag-hide",
                                                                                     :on     "drag-end",
                                                                                     :type   "action",
                                                                                     :params {:colliders     ["yellow-box" "blue-box" "red-box"
                                                                                                              "purple-table" "orange-table"
                                                                                                              "green-table"]
                                                                                              :self          "purple-crayon"
                                                                                              :target        "purple-table"
                                                                                              :init-position {:x        60,
                                                                                                              :y        1054,
                                                                                                              :duration 1}}}}}
                                       :yellow-crayon      {:type      "image",
                                                            :x         154,
                                                            :y         139,
                                                            :rotation  -90,
                                                            :scale     0.35
                                                            :src       "/raw/img/categorize/yellow_crayons.png",
                                                            :states    {:init-position {:x 154, :y 139, :visible true}},
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag",
                                                                                     :params {:colliders ["yellow-box" "blue-box" "red-box"
                                                                                                          "purple-table" "orange-table"
                                                                                                          "green-table"]
                                                                                              :say-color "yellow-color"
                                                                                              :self      "yellow-crayon"
                                                                                              :target    "yellow-box"}}
                                                                        :drag-end   {:id     "stop-drag-hide",
                                                                                     :on     "drag-end",
                                                                                     :type   "action",
                                                                                     :params {:colliders     ["yellow-box" "blue-box" "red-box"
                                                                                                              "purple-table" "orange-table"
                                                                                                              "green-table"]
                                                                                              :self          "yellow-crayon"
                                                                                              :target        "yellow-box"
                                                                                              :init-position {:x        154,
                                                                                                              :y        139,
                                                                                                              :duration 1}}}}}
                                       :green-crayon       {:type      "image",
                                                            :x         19,
                                                            :y         263,
                                                            :rotation  -90
                                                            :scale     0.35
                                                            :src       "/raw/img/categorize/green_crayons.png",
                                                            :states    {:init-position {:x 19, :y 263}},
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag",
                                                                                     :params {:colliders ["yellow-box" "blue-box" "red-box"
                                                                                                          "purple-table" "orange-table"
                                                                                                          "green-table"]
                                                                                              :self      "green-crayon"
                                                                                              :say-color "green-color"
                                                                                              :target    "green-table"}}
                                                                        :drag-end   {:id     "stop-drag-hide",
                                                                                     :on     "drag-end",
                                                                                     :type   "action",
                                                                                     :params {:colliders     ["yellow-box" "blue-box" "red-box"
                                                                                                              "purple-table" "orange-table"
                                                                                                              "green-table"]
                                                                                              :self          "green-crayon"
                                                                                              :target        "green-table"
                                                                                              :init-position {:x        19,
                                                                                                              :y        263,
                                                                                                              :duration 1}}}}}
                                       :blue-crayon        {:type      "image",
                                                            :x         1611,
                                                            :y         440,
                                                            :rotation  -90,
                                                            :scale     0.35,
                                                            :src       "/raw/img/categorize/blue_crayons.png",
                                                            :states    {:init-position {:x 1611, :y 440,}},
                                                            :draggable true,
                                                            :actions   {:drag-start {:type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag",
                                                                                     :params {:colliders ["yellow-box" "blue-box" "red-box"
                                                                                                          "purple-table" "orange-table"
                                                                                                          "green-table"]
                                                                                              :self      "blue-crayon"
                                                                                              :say-color "blue-color"
                                                                                              :target    "blue-box"}}
                                                                        :drag-end   {:id     "stop-drag-hide",
                                                                                     :on     "drag-end",
                                                                                     :type   "action",
                                                                                     :params {:colliders     ["yellow-box" "blue-box" "red-box"
                                                                                                              "purple-table" "orange-table"
                                                                                                              "green-table"]
                                                                                              :self          "blue-crayon"
                                                                                              :target        "blue-box"
                                                                                              :init-position {:x        1611,
                                                                                                              :y        440,
                                                                                                              :duration 1}}}}}
                                       :orange-crayon      {:type      "image",
                                                            :x         1495,
                                                            :y         914,
                                                            :rotation  -90
                                                            :scale     0.35
                                                            :src       "/raw/img/categorize/orange_crayons.png",
                                                            :states    {:init-position {:x 1495, :y 914,}}
                                                            :draggable true,
                                                            :actions   {:drag-start {
                                                                                     :type   "action",
                                                                                     :on     "drag-start",
                                                                                     :id     "start-drag",
                                                                                     :params {:colliders ["yellow-box" "blue-box" "red-box"
                                                                                                          "purple-table" "orange-table"
                                                                                                          "green-table"]
                                                                                              :self      "orange-crayon"
                                                                                              :say-color "orange-color"
                                                                                              :target    "orange-table"}}
                                                                        :drag-end   {:id     "stop-drag-hide",
                                                                                     :on     "drag-end",
                                                                                     :type   "action",
                                                                                     :params {:colliders     ["yellow-box" "blue-box" "red-box"
                                                                                                              "purple-table" "orange-table"
                                                                                                              "green-table"]
                                                                                              :self          "orange-crayon"
                                                                                              :target        "orange-table"
                                                                                              :init-position {:x        1495,
                                                                                                              :y        914,
                                                                                                              :duration 1}}}}}
                                       :purple-table       {:type "image",
                                                            :x    1120,
                                                            :y    652,
                                                            :src  "/raw/img/categorize/purple_table.png"}
                                       :green-table        {:type "image",
                                                            :x    330,
                                                            :y    667,
                                                            :src  "/raw/img/categorize/green_table.png"}
                                       :orange-table       {:type "image",
                                                            :x    745,
                                                            :y    773,
                                                            :src  "/raw/img/categorize/orange_table.png"}
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
                                                          :actions    {:click {:id "tap-instructions" :on "click" :type "action"}}}},

                       :scene-objects [["layered-background"]
                                       ["purple-table" "green-table" "orange-table"]
                                       ["librarian"]
                                       ["yellow-box" "blue-box" "red-box"]
                                       ["red-crayon" "purple-crayon" "yellow-crayon" "green-crayon" "blue-crayon" "orange-crayon"]]
                       :actions       {:object-revert            {:type        "state",
                                                                  :id          "init-position"
                                                                  :from-params [{:action-property "target" :param-property "self"}]}
                                       :correct-answer-single    {:type "sequence-data",
                                                                  :data [{:id "unhighlight-all" :type "action"}
                                                                         {:type        "state",
                                                                          :id          "init-position"
                                                                          :from-params [{:action-property "target" :param-property "self"}]}
                                                                         {:type     "action"
                                                                          :from-var [{:var-name "correct-answer", :action-property "id"}]}
                                                                         {:type     "action"
                                                                          :from-var [{:var-name "next-task", :action-property "id"}]}
                                                                         {:type "action", :id "object-revert"}]},

                                       :blink-objects            {:type "sequence-data"
                                                                  :data [{:type     "set-attribute" :attr-name "highlight" :attr-value true
                                                                          :from-var [{:var-name "object-1", :action-property "target"}]}
                                                                         {:type     "set-attribute" :attr-name "highlight" :attr-value true
                                                                          :from-var [{:var-name "object-2", :action-property "target"}]}
                                                                         {:type "empty", :duration 2000}
                                                                         {:type     "set-attribute" :attr-name "highlight" :attr-value false
                                                                          :from-var [{:var-name "object-1", :action-property "target"}]}
                                                                         {:type     "set-attribute" :attr-name "highlight" :attr-value false
                                                                          :from-var [{:var-name "object-2", :action-property "target"}]}]}
                                       :wrong-answer             {:type "parallel"
                                                                  :data [{:id "unhighlight-all" :type "action"}
                                                                         {:type "action", :id "object-revert"}
                                                                         {:type           "counter"
                                                                          :counter-action "increase"
                                                                          :counter-id     "wrong-answers-counter"}
                                                                         {:type       "test-var-inequality"
                                                                          :var-name   "wrong-answers-counter",
                                                                          :value      2,
                                                                          :inequality ">=",
                                                                          :success    "blink-objects"}
                                                                         {:type "sequence-data"
                                                                          :data [{:type "action" :id "wrong-answer-dialog"}
                                                                                 {:type "action"
                                                                                  :from-var [{:var-name "instruction", :action-property "id"}]}]}]}
                                       :tap-instructions {:type "action"
                                                          :from-var [{:var-name "instruction", :action-property "id"}]}
                                       :empty {:type "empty" :duration 100}
                                       :stop-drag-hide           {:type "sequence-data"
                                                                  :data [{:type        "copy-variables",
                                                                          :from-params [{:template        "colliding-%"
                                                                                         :action-property "var-names" :param-property "colliders"}
                                                                                        {:template        "colliding-raw-%"
                                                                                         :action-property "from-list" :param-property "colliders"}]}
                                                                         {:type "set-variable", :var-name "say", :var-value false}
                                                                         {:type "set-variable", :var-name "next-check-collide", :var-value false}

                                                                         {:type      "test-var-list",
                                                                          :values    [true true],
                                                                          :fail      "wrong-answer",
                                                                          :var-names ["check-collide-1" "check-collide-2"]
                                                                          :success   "correct-answer-single"
                                                                          :from-var  [{:var-name "check-collide", :action-property "var-names"}]}]},

                                       :yellow-color             (-> (dialog/default "Color yellow")
                                                                     (assoc :unique-tag "color"))
                                       :blue-color               (-> (dialog/default "Color blue")
                                                                     (assoc :unique-tag "color"))
                                       :red-color                (-> (dialog/default "Color red")
                                                                     (assoc :unique-tag "color"))
                                       :purple-color             (-> (dialog/default "Color purple")
                                                                     (assoc :unique-tag "color"))
                                       :orange-color             (-> (dialog/default "Color orange")
                                                                     (assoc :unique-tag "color"))
                                       :green-color              (-> (dialog/default "Color green")
                                                                     (assoc :unique-tag "color"))

                                       :correct-answer-1         (dialog/default "Correct answer for task 1")
                                       :correct-answer-2         (dialog/default "Correct answer for task 2")
                                       :correct-answer-3         (dialog/default "Correct answer for task 3")
                                       :correct-answer-4         (dialog/default "Correct answer for task 4")
                                       :correct-answer-5         (dialog/default "Correct answer for task 5")
                                       :correct-answer-6         (dialog/default "Correct answer for task 6")

                                       :say-color                {:type "sequence-data"
                                                                  :data [{:type "action" :from-params [{:action-property "id"
                                                                                                        :param-property  "say-color"}]}
                                                                         {:type "action" :id "check-say"}]}
                                       :check-say                {:type     "test-var-scalar",
                                                                  :success  "next-say",
                                                                  :value    true,
                                                                  :var-name "say"}
                                       :next-say                 {:type "sequence-data"
                                                                  :data [{:type     "set-timeout"
                                                                          :action   "say-color"
                                                                          :interval 100}]}

                                       :start-drag               {:type "sequence-data"
                                                                  :data [{:type     "set-variable-list"
                                                                          :values   [false false],
                                                                          :from-var [{:var-name "check-collide", :action-property "var-names"}]}
                                                                         {:type        "set-variable",
                                                                          :var-value   true
                                                                          :from-params [{:template       "colliding-object-%",
                                                                                         :param-property "self", :action-property "var-name"}]}
                                                                         {:type "set-variable", :var-name "say", :var-value true}
                                                                         {:type "set-variable", :var-name "next-check-collide", :var-value true}
                                                                         {:id "check-say" :type "action"}
                                                                         {:id "next-check-collide" :type "action"}]}
                                       :next-check-collide       {:type "sequence-data"
                                                                  :data [{:type     "set-timeout"
                                                                          :action   "check-collide"
                                                                          :interval 10}]}
                                       :check-collide            {:type "sequence-data"
                                                                  :data [{:type        "test-transitions-and-pointer-collide",
                                                                          :success     "highlight",
                                                                          :fail        "unhighlight",
                                                                          :from-params [{:param-property "colliders", :action-property "transitions"}]}
                                                                         {:type "action" :id "check-next-check-collide"}]}
                                       :check-next-check-collide {:type     "test-var-scalar",
                                                                  :success  "next-check-collide",
                                                                  :value    true,
                                                                  :var-name "next-check-collide"}
                                       :highlight                {:type "sequence-data"
                                                                  :data [{:type        "set-variable",
                                                                          :var-value   true
                                                                          :from-params [{:action-property "var-name",
                                                                                         :template        "colliding-raw-%",
                                                                                         :param-property  "transition"}]}
                                                                         {:type        "set-attribute" :attr-name "highlight" :attr-value true
                                                                          :from-params [{:action-property "target" :param-property "transition"}]}]}
                                       :unhighlight              {:type "sequence-data"
                                                                  :data [{:type        "set-variable",
                                                                          :var-value   false
                                                                          :from-params [{:action-property "var-name",
                                                                                         :template        "colliding-raw-%",
                                                                                         :param-property  "transition"}]}
                                                                         {:type        "set-attribute" :attr-name "highlight" :attr-value false
                                                                          :from-params [{:action-property "target" :param-property "transition"}]}]}
                                       :unhighlight-all     {:type "parallel"
                                                             :data [{:type "set-attribute" :attr-name "highlight" :attr-value false :target "yellow-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "blue-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "red-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "purple-table"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "orange-table"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "green-table"}]}
                                       :intro                    {:type               "sequence-data",
                                                                  :editor-type        "dialog",
                                                                  :data               [{:type "sequence-data"
                                                                                        :data [{:type "empty" :duration 0}
                                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                  :phrase             "continue-sorting",
                                                                  :phrase-description "Listen carefully, because it might be a different place than where you put it before!"}
                                       :instruction-1            {:type               "sequence-data",
                                                                  :editor-type        "dialog",
                                                                  :data               [{:type "sequence-data"
                                                                                        :data [{:type "empty" :duration 0}
                                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                  :phrase             "instruction-1",
                                                                  :phrase-description "Put the red crayon in its crayon box."}
                                       :instruction-2            {:type               "sequence-data",
                                                                  :editor-type        "dialog",
                                                                  :data               [{:type "sequence-data"
                                                                                        :data [{:type "empty" :duration 0}
                                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                  :phrase             "instruction-2",
                                                                  :phrase-description "Put the purple crayon on its table."}
                                       :instruction-3            {:type               "sequence-data",
                                                                  :editor-type        "dialog",
                                                                  :data               [{:type "sequence-data"
                                                                                        :data [{:type "empty" :duration 0}
                                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                  :phrase             "instruction-3",
                                                                  :phrase-description "Put the yellow crayon in its crayon box."}
                                       :instruction-4            {:type               "sequence-data",
                                                                  :editor-type        "dialog",
                                                                  :data               [{:type "sequence-data"
                                                                                        :data [{:type "empty" :duration 0}
                                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                  :phrase             "instruction-4",
                                                                  :phrase-description "Put the green crayon on its table."}
                                       :instruction-5            {:type               "sequence-data",
                                                                  :editor-type        "dialog",
                                                                  :data               [{:type "sequence-data"
                                                                                        :data [{:type "empty" :duration 0}
                                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                  :phrase             "instruction-5",
                                                                  :phrase-description "Put the blue crayon in its crayon box."}
                                       :instruction-6            {:type               "sequence-data",
                                                                  :editor-type        "dialog",
                                                                  :data               [{:type "sequence-data"
                                                                                        :data [{:type "empty" :duration 0}
                                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                  :phrase             "instruction-6",
                                                                  :phrase-description "Put the orange crayon on its table."}
                                       :stop-activity            {:type "stop-activity"},
                                       :finish-activity          {:type "finish-activity"}
                                       :finish                   {:type "sequence-data",
                                                                  :data [{:type "action" :id "finish-dialog"}
                                                                         {:type "action" :id "finish-activity"}]}
                                       :finish-dialog            {:type               "sequence-data",
                                                                  :editor-type        "dialog",
                                                                  :data               [{:type "sequence-data"
                                                                                        :data [{:type "empty" :duration 0}
                                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                  :phrase             "Finish dialog",
                                                                  :phrase-description "Finish dialog"}

                                       :wrong-answer-dialog      {:type               "sequence-data",
                                                                  :editor-type        "dialog",
                                                                  :data               [{:type "sequence-data"
                                                                                        :data [{:type "empty" :duration 0}
                                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                  :phrase             "wrong-answer-dialog",
                                                                  :phrase-description "Wrong answer dialog"}
                                       :correct-answer-dialog    {:type               "sequence-data",
                                                                  :editor-type        "dialog",
                                                                  :data               [{:type "sequence-data"
                                                                                        :data [{:type "empty" :duration 0}
                                                                                               {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                                  :phrase             "correct-answer-dialog",
                                                                  :phrase-description "Correct answer dialog"}
                                       :task-2                   {:type "sequence-data",
                                                                  :data [{:type "set-variable", :var-name "object-1", :var-value "purple-crayon"}
                                                                         {:type "set-variable", :var-name "object-2", :var-value "purple-table"}
                                                                         {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-purple-crayon" "colliding-purple-table"]}
                                                                         {:type "set-variable", :var-name "next-task", :var-value "task-3"}
                                                                         {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-2"}
                                                                         {:type "set-variable", :var-name "instruction", :var-value "instruction-2"}
                                                                         {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                         {:type "action", :id "instruction-2"}]}
                                       :task-3                   {:type "sequence-data",
                                                                  :data [{:type "set-variable", :var-name "object-1", :var-value "yellow-crayon"}
                                                                         {:type "set-variable", :var-name "object-2", :var-value "yellow-box"}
                                                                         {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-yellow-crayon" "colliding-yellow-box"]}
                                                                         {:type "set-variable", :var-name "next-task", :var-value "task-4"}
                                                                         {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-3"}
                                                                         {:type "set-variable", :var-name "instruction", :var-value "instruction-3"}
                                                                         {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                         {:type "action", :id "instruction-3"}]}
                                       :task-4                   {:type "sequence-data",
                                                                  :data [{:type "set-variable", :var-name "object-1", :var-value "green-crayon"}
                                                                         {:type "set-variable", :var-name "object-2", :var-value "green-table"}
                                                                         {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-green-crayon" "colliding-green-table"]}
                                                                         {:type "set-variable", :var-name "next-task", :var-value "task-5"}
                                                                         {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-4"}
                                                                         {:type "set-variable", :var-name "instruction", :var-value "instruction-4"}
                                                                         {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                         {:type "action", :id "instruction-4"}]}
                                       :task-5                   {:type "sequence-data",
                                                                  :data [{:type "set-variable", :var-name "object-1", :var-value "blue-crayon"}
                                                                         {:type "set-variable", :var-name "object-2", :var-value "blue-box"}
                                                                         {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-blue-crayon" "colliding-blue-box"]}
                                                                         {:type "set-variable", :var-name "next-task", :var-value "task-6"}
                                                                         {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-5"}
                                                                         {:type "set-variable", :var-name "instruction", :var-value "instruction-5"}
                                                                         {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                         {:type "action", :id "instruction-5"}]}
                                       :task-6                   {:type "sequence-data",
                                                                  :data [{:type "set-variable", :var-name "object-1", :var-value "orange-crayon"}
                                                                         {:type "set-variable", :var-name "object-2", :var-value "orange-table"}
                                                                         {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-orange-crayon" "colliding-orange-table"]}
                                                                         {:type "set-variable", :var-name "next-task", :var-value "finish"}
                                                                         {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-6"}
                                                                         {:type "set-variable", :var-name "instruction", :var-value "instruction-6"}
                                                                         {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                         {:type "action", :id "instruction-6"}]}

                                       :start-activity           {:type "sequence-data",
                                                                  :data [{:type "set-variable", :var-name "instruction", :var-value "empty"}
                                                                         {:type "action", :id "intro"}
                                                                         {:type "set-variable", :var-name "object-1", :var-value "red-crayon"}
                                                                         {:type "set-variable", :var-name "object-2", :var-value "red-box"}
                                                                         {:type "set-variable", :var-name "check-collide", :var-value ["colliding-object-red-crayon" "colliding-red-box"]}
                                                                         {:type "set-variable", :var-name "next-task", :var-value "task-2"}
                                                                         {:type "set-variable", :var-name "correct-answer", :var-value "correct-answer-1"}
                                                                         {:type "set-variable", :var-name "instruction", :var-value "instruction-1"}
                                                                         {:type "counter" :counter-action "reset" :counter-id "wrong-answers-counter"}
                                                                         {:type "action", :id "instruction-1"}]}}

                       :triggers      {:start {:on "start", :action "start-activity"}
                                       :back  {:on "back", :action "stop-activity"}},
                       :metadata      {:autostart true
                                       :tracks    [{:title "Round 3 - Intro and finish"
                                                    :nodes [{:type      "dialog"
                                                             :action-id :intro}
                                                            {:type      "dialog"
                                                             :action-id :finish-dialog}]}
                                                   {:title "Round 3 - Action result"
                                                    :nodes [{:type      "dialog"
                                                             :action-id :wrong-answer-dialog}
                                                            {:type      "dialog"
                                                             :action-id :correct-answer-dialog}]}
                                                   {:title "Round 3 - tasks"
                                                    :nodes [{:type "prompt"
                                                             :text "Put the red crayon in its crayon box."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-1}
                                                            {:type "prompt"
                                                             :text "Put the purple crayon on its table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-2}
                                                            {:type "prompt"
                                                             :text "Put the yellow crayon in its crayon box."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-3}
                                                            {:type "prompt"
                                                             :text "Put the green crayon on its table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-4}
                                                            {:type "prompt"
                                                             :text "Put the blue crayon on its table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-5}
                                                            {:type "prompt"
                                                             :text "Put the orange crayon on its table."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-6}]}
                                                   {:title "Round 3 - Correct responses"
                                                    :nodes [{:type "prompt"
                                                             :text "Put the red crayon in its crayon box."}
                                                            {:type      "dialog"
                                                             :action-id :correct-answer-1}
                                                            {:type "prompt"
                                                             :text "Put the purple crayon on its table."}
                                                            {:type      "dialog"
                                                             :action-id :correct-answer-2}
                                                            {:type "prompt"
                                                             :text "Put the yellow crayon in its crayon box."}
                                                            {:type      "dialog"
                                                             :action-id :correct-answer-3}
                                                            {:type "prompt"
                                                             :text "Put the green crayon on its table."}
                                                            {:type      "dialog"
                                                             :action-id :correct-answer-4}
                                                            {:type "prompt"
                                                             :text "Put the blue crayon on its table."}
                                                            {:type      "dialog"
                                                             :action-id :correct-answer-5}
                                                            {:type "prompt"
                                                             :text "Put the orange crayon on its table."}
                                                            {:type      "dialog"
                                                             :action-id :correct-answer-6}]}
                                                   {:title "Round 3 - colors"
                                                    :nodes [{:type      "dialog"
                                                             :action-id :yellow-color}
                                                            {:type      "dialog"
                                                             :action-id :blue-color}
                                                            {:type      "dialog"
                                                             :action-id :red-color}
                                                            {:type      "dialog"
                                                             :action-id :purple-color}
                                                            {:type      "dialog"
                                                             :action-id :orange-color}
                                                            {:type      "dialog"
                                                             :action-id :green-color}]}]}})
