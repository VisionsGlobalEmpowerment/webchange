(ns webchange.templates.library.categorize-synonyms.round-2
  (:require
    [webchange.templates.utils.dialog :as dialog]))

(def template-round-2 {:assets        [
                                       {:url "/raw/img/categorize-synonyms/background-class.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/surface.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/decoration.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/afraid.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/scared.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/large.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/big.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/child.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/kid.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/glad.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/happy.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/cold.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/chilly.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/trash.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/garbage.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-synonyms/question.png", :size 10, :type "image"}
                                       ],
                       :objects       {:layered-background {:type       "layered-background",
                                                            :background {:src "/raw/img/categorize-shapes/background-class.png"},
                                                            :decoration {:src "/raw/img/categorize-shapes/decoration.png"},
                                                            :surface    {:src "/raw/img/categorize-shapes/surface.png"}},
                                       :chilly-box {:type       "image",
                                                    :x          980,
                                                    :y          763,
                                                    :src        "/raw/img/categorize-synonyms/chilly.png",
                                                    },
                                       :kid-box {:type       "image",
                                                 :x          1274,
                                                 :y          763,
                                                 :src        "/raw/img/categorize-synonyms/kid.png",
                                                 }
                                       :large-box {:type       "image",
                                                   :x          392,
                                                   :y          762,
                                                   :src        "/raw/img/categorize-synonyms/large.png",
                                                   }
                                       :garbage-box {:type       "image",
                                                     :x          1568,
                                                     :y          762,
                                                     :src        "/raw/img/categorize-synonyms/garbage.png",
                                                     }
                                       :afraid-box {:type       "image",
                                                    :x          686,
                                                    :y          763,
                                                    :src        "/raw/img/categorize-synonyms/afraid.png",
                                                    }
                                       :glad-box {:type       "image",
                                                  :x          99,
                                                  :y          763,
                                                  :src        "/raw/img/categorize-synonyms/glad.png",
                                                  }
                                       :cold-1 {
                                                :type       "image",
                                                :x          768,
                                                :y          481,
                                                :scale      {:x 0.65, :y 0.65}
                                                :src        "/raw/img/categorize-synonyms/cold.png",
                                                :draggable  true,
                                                :actions    {:drag-start {:type   "action",
                                                                          :on     "drag-start",
                                                                          :id     "start-drag"
                                                                          :params {:say-item         "cold-item"
                                                                                   :target         "cold-1"}}
                                                             :drag-end
                                                             {:id     "dragged",
                                                              :on     "drag-end",
                                                              :type   "action",
                                                              :params {:box            "chilly-box"
                                                                       :target         "cold-1"
                                                                       :init-position  {:x 768,
                                                                                        :y 481, :duration 1}
                                                                       :check-variable "chilly-box-selected"
                                                                       }}},},
                                       :child-1            {
                                                            :type       "image",
                                                            :x          1094,
                                                            :y          223,
                                                            :scale      {:x 0.65, :y 0.65}
                                                            :src        "/raw/img/categorize-synonyms/child.png",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type   "action",
                                                                                      :on     "drag-start",
                                                                                      :id     "start-drag"
                                                                                      :params {:say-item         "child-item"
                                                                                               :target         "child-1"}}
                                                                         :drag-end
                                                                         {:id     "dragged",
                                                                          :on     "drag-end",
                                                                          :type   "action",
                                                                          :params {:box            "kid-box"
                                                                                   :target         "child-1"
                                                                                   :init-position  {:x 1094,
                                                                                                    :y 223, :duration 1}
                                                                                   :check-variable "kid-box-selected"
                                                                                   }}},},
                                       :big-1              {
                                                            :type       "image",
                                                            :x          415,
                                                            :y          355,
                                                            :scale      {:x 0.65, :y 0.65}
                                                            :src        "/raw/img/categorize-synonyms/big.png",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type   "action",
                                                                                      :on     "drag-start",
                                                                                      :id     "start-drag"
                                                                                      :params {:say-item         "big-item"
                                                                                               :target         "big-1"}}
                                                                         :drag-end
                                                                         {:id     "dragged",
                                                                          :on     "drag-end",
                                                                          :type   "action",
                                                                          :params {:box            "large-box"
                                                                                   :target         "big-1"
                                                                                   :init-position  {:x 415,
                                                                                                    :y 355, :duration 1}
                                                                                   :check-variable "large-box-selected"
                                                                                   }}},}
                                       :trash-1            {
                                                            :type       "image",
                                                            :x          1388,
                                                            :y          561,
                                                            :scale      {:x 0.65, :y 0.65}
                                                            :src        "/raw/img/categorize-synonyms/trash.png",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type   "action",
                                                                                      :on     "drag-start",
                                                                                      :id     "start-drag"
                                                                                      :params {:say-item         "trash-item"
                                                                                               :target         "trash-1"}}
                                                                         :drag-end
                                                                         {:id     "dragged",
                                                                          :on     "drag-end",
                                                                          :type   "action",
                                                                          :params {:box            "garbage-box"
                                                                                   :target         "trash-1"
                                                                                   :init-position  {:x 1388,
                                                                                                    :y 561, :duration 1}
                                                                                   :check-variable "garbage-box-selected"
                                                                                   }}},}
                                       :scared-1           {
                                                            :type       "image",
                                                            :x          1636,
                                                            :y          107,
                                                            :scale      {:x 0.65, :y 0.65}
                                                            :src        "/raw/img/categorize-synonyms/scared.png",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type   "action",
                                                                                      :on     "drag-start",
                                                                                      :id     "start-drag"
                                                                                      :params {:say-item         "scared-item"
                                                                                               :target         "scared-1"}}
                                                                         :drag-end
                                                                         {:id     "dragged",
                                                                          :on     "drag-end",
                                                                          :type   "action",
                                                                          :params {:box            "afraid-box"
                                                                                   :target         "scared-1"
                                                                                   :init-position  {:x 1636,
                                                                                                    :y 107, :duration 1}
                                                                                   :check-variable "afraid-box-selected"
                                                                                   }}},}
                                       :happy-1            {
                                                            :type       "image",
                                                            :x          1674,
                                                            :y          434,
                                                            :scale      {:x 0.65, :y 0.65}
                                                            :src        "/raw/img/categorize-synonyms/happy.png",
                                                            :draggable  true,
                                                            :actions    {:drag-start {
                                                                                      :type   "action",
                                                                                      :on     "drag-start",
                                                                                      :id     "start-drag"
                                                                                      :params {:say-item         "happy-item"
                                                                                               :target         "happy-1"}}
                                                                         :drag-end
                                                                         {:id     "dragged",
                                                                          :on     "drag-end",
                                                                          :type   "action",
                                                                          :params {:box            "glad-box"
                                                                                   :target         "happy-1"
                                                                                   :init-position  {:x 1674,
                                                                                                    :y 434, :duration 1}
                                                                                   :check-variable "glad-box-selected"
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
                                                          :actions    {:click {:id "tap-instructions" :on "click" :type "action"}}}
                                       },
                       :scene-objects [["layered-background"]
                                       ["librarian"]
                                       ["chilly-box" "kid-box" "large-box"]
                                       ["garbage-box" "afraid-box" "glad-box"]
                                       ["cold-1" "child-1" "big-1"]
                                       ["trash-1" "scared-1" "happy-1"]],
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
                                                             :data [{:type "set-attribute" :attr-name "highlight" :attr-value false :target "kid-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "chilly-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "large-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "garbage-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "afraid-box"}
                                                                    {:type "set-attribute" :attr-name "highlight" :attr-value false :target "glad-box"}]}
                                       :next-check-collide             {:type "sequence-data"
                                                                        :data [{:type     "set-timeout"
                                                                                :action   "check-collide"
                                                                                :interval 10}]}
                                       :check-collide                  {:type "sequence-data"
                                                                        :data [
                                                                               {:type          "test-transitions-and-pointer-collide",
                                                                                :success       "highlight",
                                                                                :fail          "unhighlight",
                                                                                :transitions   ["kid-box" "chilly-box" "large-box"
                                                                                                "garbage-box" "afraid-box" "glad-box"]
                                                                                :action-params [{:check-variable "kid-box-selected"}
                                                                                                {:check-variable "chilly-box-selected"}
                                                                                                {:check-variable "large-box-selected"}
                                                                                                {:check-variable "garbage-box-selected"}
                                                                                                {:check-variable "afraid-box-selected"}
                                                                                                {:check-variable "glad-box-selected"}]}
                                                                               {:type     "test-var-scalar",
                                                                                :success  "next-check-collide",
                                                                                :value    true,
                                                                                :var-name "next-check-collide"}]}

                                       :init-activity                  {:type "sequence-data"
                                                                        :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "sorted-objects"}
                                                                               {:type "action" :id "intro"}]}
                                       
                                       :cold-item                      (-> (dialog/default "cold")
                                                                           (assoc :unique-tag "item"))
                                       :child-item                     (-> (dialog/default "child")
                                                                           (assoc :unique-tag "item"))
                                       :big-item                       (-> (dialog/default "big")
                                                                           (assoc :unique-tag "item"))
                                       :trash-item                     (-> (dialog/default "trash")
                                                                           (assoc :unique-tag "item"))
                                       :scared-item                    (-> (dialog/default "scared")
                                                                           (assoc :unique-tag "item"))
                                       :happy-item                     (-> (dialog/default "happy")
                                                                           (assoc :unique-tag "item"))
                                       :tap-instructions               (-> (dialog/default "Tap instructions")
                                                                           (assoc :unique-tag "instructions"))
                                       :say-item                       {:type "sequence-data"
                                                                        :data [{:type "action" :from-params [{:action-property "id"
                                                                                                              :param-property  "say-item"}]}
                                                                               {:type     "test-var-scalar",
                                                                                :success  "next-say",
                                                                                :value    true,
                                                                                :var-name "say"}]}
                                       :next-say                       {:type "sequence-data"
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
                                                                               {:type "set-variable", :var-name "kid-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "chilly-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "large-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "garbage-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "afraid-box-selected", :var-value false}
                                                                               {:type "set-variable", :var-name "glad-box-selected", :var-value false}
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
                                       :stop-activity                  {:type "stop-activity"}},

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
                                                               :action-id :cold-item}
                                                              {:type      "dialog"
                                                               :action-id :child-item}
                                                              {:type      "dialog"
                                                               :action-id :big-item}
                                                              {:type      "dialog"
                                                               :action-id :trash-item}
                                                              {:type      "dialog"
                                                               :action-id :scared-item}
                                                              {:type      "dialog"
                                                               :action-id :happy-item}]}]}})
