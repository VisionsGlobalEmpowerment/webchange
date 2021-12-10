(ns webchange.templates.library.categorize.antonyms.round-3
  (:require
    [webchange.templates.library.categorize-antonims.common :as common]
    [webchange.templates.utils.dialog :as dialog]))

(defn- get-draggable-item
  [{:keys [item-name box-name position src]}]
  (-> {:position position
       :src      src
       :target   (str item-name "-object")
       :say-item (str item-name "-item")
       :box      (str box-name "-box")}
      (common/get-draggable-item)
      (merge {:scale  {:x 0.65, :y 0.65}
              :states {:init-position   position
                       :highlighted     {:highlight true}
                       :not-highlighted {:highlight false}}})))

(defn- get-box
  [{:keys [name position src]}]
  (merge position
         {:y 763}
         {:type       "image"
          :src        src
          :width      253
          :height     253
          :transition (str name "-box")
          :states     {:highlighted     {:highlight true}
                       :not-highlighted {:highlight false}}}))

(defn- get-task
  [{:keys [item box instruction next]}]
  (let [item-name (str item "-object")
        box-name (str box "-box")]
    {:type "sequence-data",
     :data [{:type "set-variable" :var-name "check-collide" :var-value [item-name box-name]}
            {:type "set-variable" :var-name "next-task" :var-value next}
            {:type "set-variable" :var-name "instruction" :var-value instruction}
            {:type "action" :id instruction}]}))

(def template-round-3 {:assets        [{:url "/raw/img/categorize-antonims/background-class.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/surface.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/decoration.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/right.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/quiet.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/in.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/day.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/front.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/down.png", :size 10, :type "image"}

                                       {:url "/raw/img/categorize-antonims/night.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/left.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/loud.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/back.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/out.png", :size 10, :type "image"}
                                       {:url "/raw/img/categorize-antonims/up.png", :size 10, :type "image"}]

                       :objects       {:layered-background {:type       "layered-background"
                                                            :background {:src "/raw/img/categorize-antonims/background-class.png"}
                                                            :decoration {:src "/raw/img/categorize-antonims/decoration.png"}
                                                            :surface    {:src "/raw/img/categorize-antonims/surface.png"}}

                                       :left-object        (get-draggable-item {:item-name "left"
                                                                                :box-name  "right"
                                                                                :position  {:x 1635 :y 107}
                                                                                :src       "/raw/img/categorize-antonims/left.png"})
                                       :up-object          (get-draggable-item {:item-name "up"
                                                                                :box-name  "down"
                                                                                :position  {:x 801 :y 481}
                                                                                :src       "/raw/img/categorize-antonims/up.png"})
                                       :out-object         (get-draggable-item {:item-name "out"
                                                                                :box-name  "in"
                                                                                :position  {:x 1095 :y 223}
                                                                                :src       "/raw/img/categorize-antonims/out.png"})
                                       :night-object       (get-draggable-item {:item-name "night"
                                                                                :box-name  "day"
                                                                                :position  {:x 1274 :y 481}
                                                                                :src       "/raw/img/categorize-antonims/night.png"})
                                       :loud-object        (get-draggable-item {:item-name "loud"
                                                                                :box-name  "quiet"
                                                                                :position  {:x 790 :y 160}
                                                                                :src       "/raw/img/categorize-antonims/loud.png"})
                                       :back-object        (get-draggable-item {:item-name "back"
                                                                                :box-name  "front"
                                                                                :position  {:x 415 :y 354}
                                                                                :src       "/raw/img/categorize-antonims/back.png"})

                                       :down-box           (get-box {:name     "down"
                                                                     :position {:x 1568}
                                                                     :src      "/raw/img/categorize-antonims/down.png"})
                                       :day-box            (get-box {:name     "day"
                                                                     :position {:x 980}
                                                                     :src      "/raw/img/categorize-antonims/day.png"})
                                       :front-box          (get-box {:name     "front"
                                                                     :position {:x 686}
                                                                     :src      "/raw/img/categorize-antonims/front.png"})
                                       :in-box             (get-box {:name     "in"
                                                                     :position {:x 1274}
                                                                     :src      "/raw/img/categorize-antonims/in.png"})
                                       :quiet-box          (get-box {:name     "quiet"
                                                                     :position {:x 393}
                                                                     :src      "/raw/img/categorize-antonims/quiet.png"})
                                       :right-box          (get-box {:name     "right"
                                                                     :position {:x 99}
                                                                     :src      "/raw/img/categorize-antonims/right.png"})

                                       :librarian          {:type      "animation",
                                                            :x         250,
                                                            :y         1000,
                                                            :width     351,
                                                            :height    717,
                                                            :anim      "idle",
                                                            :name      "senoravaca",
                                                            :skin      "lion",
                                                            :speed     0.3,
                                                            :start     true
                                                            :editable? {:select        true
                                                                        :show-in-tree? true}
                                                            :actions   {:click {:id "tap-instructions" :on "click" :type "action"}}}}

                       :scene-objects [["layered-background"]
                                       ["librarian"]
                                       ["in-box" "quiet-box" "right-box" "down-box" "front-box" "day-box"]
                                       ["left-object" "up-object" "out-object" "night-object" "loud-object" "back-object"]]

                       :actions       {:handle-collide-enter  {:type "sequence-data"
                                                               :data [{:type        "state"
                                                                       :id          "highlighted"
                                                                       :from-params [{:action-property "target" :param-property "target"}]}
                                                                      {:type        "set-variable"
                                                                       :from-params [{:action-property "var-name"
                                                                                      :param-property  "target"}]
                                                                       :var-value   true}]}

                                       :handle-collide-leave  {:type "sequence-data"
                                                               :data [{:type        "state"
                                                                       :id          "not-highlighted"
                                                                       :from-params [{:action-property "target" :param-property "target"}]}
                                                                      {:type        "set-variable"
                                                                       :from-params [{:action-property "var-name"
                                                                                      :param-property  "target"}]
                                                                       :var-value   false}]}

                                       :handle-drag-start     {:type        "set-variable"
                                                               :from-params [{:action-property "var-name"
                                                                              :param-property  "target"}]
                                                               :var-value   true}

                                       :handle-drag-move      {:type        "action"
                                                               :from-params [{:action-property "id"
                                                                              :param-property  "say-item"}]}

                                       :handle-drag-end       {:type "sequence-data"
                                                               :data [{:type     "test-var-list",
                                                                       :from-var [{:action-property "var-names"
                                                                                   :var-name        "check-collide"}]
                                                                       :values   [true true]
                                                                       :success  "correct-answer"
                                                                       :fail     "wrong-answer"}
                                                                      {:type        "set-variable"
                                                                       :from-params [{:action-property "var-name"
                                                                                      :param-property  "target"}]
                                                                       :var-value   false}]}

                                       :correct-answer        {:type "sequence-data",
                                                               :data [{:type        "state",
                                                                       :id          "init-position"
                                                                       :from-params [{:action-property "target" :param-property "target"}]}
                                                                      {:type "action"
                                                                       :id   "correct-answer-dialog"}
                                                                      {:type     "action"
                                                                       :from-var [{:var-name "next-task", :action-property "id"}]}]},

                                       :wrong-answer          {:type "sequence-data"
                                                               :data [{:type "action"
                                                                       :id   "wrong-answer-dialog"}
                                                                      {:type "action" :id "object-revert"}]}

                                       :object-revert         {:type        "state",
                                                               :id          "init-position"
                                                               :from-params [{:action-property "target" :param-property "target"}]}

                                       :intro                 {:type               "sequence-data",
                                                               :editor-type        "dialog",
                                                               :data               [{:type "sequence-data"
                                                                                     :data [{:type "empty" :duration 0}
                                                                                            {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                               :phrase             "continue-sorting",
                                                               :phrase-description "Task introduction"}

                                       :stop-activity         {:type "stop-activity"},
                                       :finish                {:type "sequence-data",
                                                               :data [{:type "action" :id "finish-dialog"}
                                                                      {:type "action" :id "finish-activity"}]}

                                       :start-activity        {:type "sequence-data",
                                                               :data [{:type "action", :id "intro"}
                                                                      {:type "action", :id "task-1"}]}

                                       :finish-activity       {:type "finish-activity"}

                                       :tap-instructions      {:type     "action"
                                                               :from-var [{:var-name "instruction", :action-property "id"}]}


                                       :task-1                (get-task {:item        "left"
                                                                         :box         "right"
                                                                         :instruction "instruction-1"
                                                                         :next        "task-2"})
                                       :task-2                (get-task {:item        "loud"
                                                                         :box         "quiet"
                                                                         :instruction "instruction-2"
                                                                         :next        "task-3"})
                                       :task-3                (get-task {:item        "out"
                                                                         :box         "in"
                                                                         :instruction "instruction-3"
                                                                         :next        "task-4"})
                                       :task-4                (get-task {:item        "night"
                                                                         :box         "day"
                                                                         :instruction "instruction-4"
                                                                         :next        "task-5"})
                                       :task-5                (get-task {:item        "back"
                                                                         :box         "front"
                                                                         :instruction "instruction-5"
                                                                         :next        "task-6"})
                                       :task-6                (get-task {:item        "up"
                                                                         :box         "down"
                                                                         :instruction "instruction-6"
                                                                         :next        "finish"})

                                       :left-item             (dialog/default "Left")
                                       :back-item             (dialog/default "Back")
                                       :up-item               (dialog/default "Up")
                                       :loud-item             (dialog/default "Loud")
                                       :night-item            (dialog/default "Night")
                                       :out-item              (dialog/default "Out")

                                       :instruction-1         (dialog/default "instruction-1" {:phrase-description "Left on right"})
                                       :instruction-2         (dialog/default "instruction-2" {:phrase-description "Loud on quiet"})
                                       :instruction-3         (dialog/default "instruction-3" {:phrase-description "Out on in"})
                                       :instruction-4         (dialog/default "instruction-4" {:phrase-description "Night on day"})
                                       :instruction-5         (dialog/default "instruction-5" {:phrase-description "Back on front"})
                                       :instruction-6         (dialog/default "instruction-6" {:phrase-description "Up on down"})

                                       :finish-dialog         (dialog/default "Finish dialog")
                                       :wrong-answer-dialog   (dialog/default "Wrong answer dialog")
                                       :correct-answer-dialog (dialog/default "Correct answer dialog")}

                       :triggers      {:start {:on "start", :action "start-activity"}},
                       :metadata      {:autostart true
                                       :tracks    [
                                                   {:title "Round 3 - Intro and finish"
                                                    :nodes [{:type      "dialog"
                                                             :action-id :intro}
                                                            {:type      "dialog"
                                                             :action-id :finish-dialog}]}
                                                   {:title "Round 3 - Action result"
                                                    :nodes [{:type      "dialog"
                                                             :action-id :wrong-answer-dialog}
                                                            {:type      "dialog"
                                                             :action-id :correct-answer-dialog}]}
                                                   {:title "Round 3 - First 6 tasks"
                                                    :nodes [{:type "prompt"
                                                             :text "Put the left picture on the right picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-1}
                                                            {:type "prompt"
                                                             :text "Put the loud picture on the quiet picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-2}
                                                            {:type "prompt"
                                                             :text "Put the out picture on the in picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-3}
                                                            {:type "prompt"
                                                             :text "Put the night picture on the day picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-4}
                                                            {:type "prompt"
                                                             :text "Put the back picture on the front picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-5}
                                                            {:type "prompt"
                                                             :text "Put the up picture on the down picture."}
                                                            {:type      "dialog"
                                                             :action-id :instruction-6}]}
                                                   {:title "Round 3 - items"
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
