(ns webchange.templates.library.onset-and-rime
  (:require
    [webchange.templates.core :as core]
    [webchange.templates.utils.common :as common]))

(def m {:id          25
        :name        "Onset and rime"
        :tags        ["Guided Practice" "onset and rime" "blending"]
        :description "Orally blend the onsets, rimes, and phonemes of words and orally delete the onsets of words, with the support of pictures or objects."
        :options     {:left-text  {:label       "Left cloud"
                                   :placeholder "Left cloud"
                                   :type        "string"}
                      :right-text {:label       "Right cloud"
                                   :placeholder "Right cloud"
                                   :type        "string"}
                      :whole-text {:label       "Whole word cloud"
                                   :placeholder "Whole word cloud"
                                   :type        "string"}
                      :image      {:label   "Image at result"
                                   :type    "image"
                                   :options {:max-width  100
                                             :max-height 100
                                             :min-height 50
                                             :min-width  50}}}
        :actions     {:add-ball {:title   "Add word",
                                 :options {:left-text  {:label       "Left cloud"
                                                        :placeholder "Left cloud"
                                                        :type        "string"}
                                           :right-text {:label       "Right cloud"
                                                        :placeholder "Right cloud"
                                                        :type        "string"}
                                           :whole-text {:label       "Whole word cloud"
                                                        :placeholder "Whole word cloud"
                                                        :type        "string"}
                                           :image      {:label   "Image at result"
                                                        :type    "image"
                                                        :options {:max-width  100
                                                                  :max-height 100
                                                                  :min-height 50
                                                                  :min-width  50}}}}}})

(def glow-color 0x2a76ff)
(def glow-strength 2)
(def cloud-states {:highlighted-0   {:glow-pulsation {:duration  200
                                                      :min-value 0
                                                      :max-value glow-strength
                                                      :color     glow-color}}
                   :highlighted-1   {:glow-pulsation {:duration  160
                                                      :min-value 0
                                                      :max-value glow-strength
                                                      :color     glow-color}}
                   :highlighted-2   {:glow-pulsation {:duration  130
                                                      :min-value 0
                                                      :max-value glow-strength
                                                      :color     glow-color}}
                   :highlighted-3   {:glow-pulsation {:duration  100
                                                      :min-value 0
                                                      :max-value glow-strength
                                                      :color     glow-color}}
                   :not-highlighted {:glow-pulsation false}})

(def t {:assets        [{:url "/raw/img/onset-and-rime/background.png", :size 10 :type "image"}
                        {:url "/raw/img/onset-and-rime/cloud.png", :size 1, :type "image"}],
        :objects       {:background               {:type "background", :src "/raw/img/onset-and-rime/background.png"},
                        :senoravaca               {:type       "animation"
                                                   :x          1100
                                                   :y          970
                                                   :name       "senoravaca"
                                                   :anim       "idle"
                                                   :speed      0.3
                                                   :skin       "vaca"
                                                   :scale-x    0.75 :scale-y 0.75
                                                   :editable?  {:select true :drag true :show-in-tree? true}
                                                   :start      true
                                                   :scene-name "senoravaca"}
                        :cloud-left-img--1        {:type   "image"
                                                   :src    "/raw/img/onset-and-rime/cloud.png"
                                                   :x      0
                                                   :y      0
                                                   :states cloud-states}
                        :cloud-left-text--1       {:type           "text"
                                                   :text           ""
                                                   :x              320
                                                   :y              220
                                                   :align          "center"
                                                   :vertical-align "bottom"
                                                   :font-family    "Lexend Deca"
                                                   :font-size      110
                                                   :fill           "black"}
                        :cloud-left--1            {:type       "group"
                                                   :x          25
                                                   :y          176
                                                   :transition "cloud-left--1"
                                                   :children   ["cloud-left-img--1"
                                                                "cloud-left-text--1"]},
                        :cloud-right-img--1       {:type       "image"
                                                   :src        "/raw/img/onset-and-rime/cloud.png"
                                                   :transition "cloud-right-img"
                                                   :states     cloud-states
                                                   :x          0
                                                   :y          0}
                        :cloud-right-text--1      {:type           "text"
                                                   :text           ""
                                                   :x              320
                                                   :y              220
                                                   :align          "center"
                                                   :vertical-align "bottom"
                                                   :font-family    "Lexend Deca"
                                                   :font-size      110
                                                   :fill           "black"}
                        :cloud-right--1           {:type       "group"
                                                   :x          1214
                                                   :y          176
                                                   :transition "cloud-right--1"
                                                   :children   ["cloud-right-img--1"
                                                                "cloud-right-text--1"]}
                        :moving-clouds--1         {:type       "group"
                                                   :visible    true
                                                   :transition "moving-clouds--1"
                                                   :children   ["cloud-right--1"
                                                                "cloud-left--1"]
                                                   :states     {:hide {:visible false} :show {:visible true}}}
                        :cloud-center-img--1      {:type       "image"
                                                   :src        "/raw/img/onset-and-rime/cloud.png"
                                                   :transition "cloud-center-img"
                                                   :x          0
                                                   :y          0}
                        :cloud-center-text-img--1 {:type       "image"
                                                   :src        ""
                                                   :transition "cloud-center-text-img"
                                                   :x          400
                                                   :y          120}
                        :cloud-center-text--1     {:type           "text"
                                                   :text           ""
                                                   :x              250
                                                   :y              220
                                                   :align          "center"
                                                   :vertical-align "bottom"
                                                   :font-family    "Lexend Deca"
                                                   :font-size      110
                                                   :fill           "black"}
                        :cloud-center--1          {:type       "group"
                                                   :x          600
                                                   :y          176
                                                   :transition "cloud-center--1"
                                                   :visible    false
                                                   :opacity    0
                                                   :children   ["cloud-center-img--1"
                                                                "cloud-center-text--1"
                                                                "cloud-center-text-img--1"]
                                                   :states     {:hide {:visible false} :show {:visible true}}}}
        :scene-objects [["background"] ["senoravaca"] ["cloud-center--1" "moving-clouds--1"]],
        :actions       {:sync-highlights             {:type "parallel"
                                                      :data [{:type     "state"
                                                              :from-var [{:var-name "next-cloud-img" :action-property "target"}
                                                                         {:template "highlighted-%" :var-name "step-counter" :action-property "id"}]}
                                                             {:type     "state" :id "not-highlighted"
                                                              :from-var [{:var-name "prev-cloud-img" :action-property "target"}]}
                                                             {:type     "state" :id "disable"
                                                              :from-var [{:var-name "prev-cloud" :action-property "target"}]}
                                                             {:type     "state" :id "enable"
                                                              :from-var [{:var-name "next-cloud" :action-property "target"}]}]}
                        :init-scene-cloud-vars       {:type "sequence-data"
                                                      :data [{:type "set-variable" :var-name "cloud-left-x" :var-value 25}
                                                             {:type "set-variable" :var-name "cloud-right-x" :var-value 1214}
                                                             {:type     "set-variable" :var-name "next-cloud"
                                                              :from-var [{:template "cloud-left-%" :var-name "unique-suffix" :action-property "var-value"}]}
                                                             {:type     "set-variable" :var-name "prev-cloud"
                                                              :from-var [{:template "cloud-right-%" :var-name "unique-suffix" :action-property "var-value"}]}
                                                             {:type     "set-variable" :var-name "prev-cloud-img"
                                                              :from-var [{:template "cloud-right-img-%" :var-name "unique-suffix" :action-property "var-value"}]}
                                                             {:type     "set-variable" :var-name "next-cloud-img"
                                                              :from-var [{:template "cloud-left-img-%" :var-name "unique-suffix" :action-property "var-value"}]}]}
                        :update-left                 {:type "sequence-data"
                                                      :data [{:type     "calc" :var-name "cloud-left-x" :operation "plus" :value-1 140
                                                              :from-var [{:var-name "cloud-left-x" :action-property "value-2"}]}
                                                             {:type     "set-variable" :var-name "next-cloud" :var-value "cloud-right"
                                                              :from-var [{:template "cloud-right-%" :var-name "unique-suffix" :action-property "var-value"}]}
                                                             {:type     "set-variable" :var-name "prev-cloud" :var-value "cloud-left"
                                                              :from-var [{:template "cloud-left-%" :var-name "unique-suffix" :action-property "var-value"}]}
                                                             {:type     "set-variable" :var-name "prev-cloud-img"
                                                              :from-var [{:template "cloud-left-img-%" :var-name "unique-suffix" :action-property "var-value"}]}
                                                             {:type     "set-variable" :var-name "next-cloud-img"
                                                              :from-var [{:template "cloud-right-img-%" :var-name "unique-suffix" :action-property "var-value"}]}
                                                             {:type "action" :id "sync-highlights"}]}
                        :update-right                {:type "sequence-data"
                                                      :data [{:type     "calc" :var-name "cloud-right-x" :operation "plus" :value-1 -140
                                                              :from-var [{:var-name "cloud-right-x" :action-property "value-2"}]}
                                                             {:type     "set-variable" :var-name "next-cloud"
                                                              :from-var [{:template "cloud-left-%" :var-name "unique-suffix" :action-property "var-value"}]}
                                                             {:type     "set-variable" :var-name "prev-cloud"
                                                              :from-var [{:template "cloud-right-%" :var-name "unique-suffix" :action-property "var-value"}]}
                                                             {:type     "set-variable" :var-name "prev-cloud-img"
                                                              :from-var [{:template "cloud-right-img-%" :var-name "unique-suffix" :action-property "var-value"}]}
                                                             {:type     "set-variable" :var-name "next-cloud-img"
                                                              :from-var [{:template "cloud-left-img-%" :var-name "unique-suffix" :action-property "var-value"}]}
                                                             {:type "action" :id "sync-highlights"}]}
                        :animate-clouds              {:type "sequence-data"
                                                      :data [{:type     "transition"
                                                              :to       {:duration 0.5}
                                                              :from-var [{:var-name "cloud-left-x" :action-property "to.x"}
                                                                         {:template "cloud-left-%" :var-name "unique-suffix" :action-property "transition-id"}]}
                                                             {:type          "transition"
                                                              :transition-id "cloud-right"
                                                              :to            {:duration 0.5}
                                                              :from-var      [{:var-name "cloud-right-x" :action-property "to.x"}
                                                                              {:template "cloud-right-%" :var-name "unique-suffix" :action-property "transition-id"}]}]}
                        :shake-step-left             {:type "sequence-data"
                                                      :data [{:type        "transition",
                                                              :from-var    [{:var-name        "cloud-left-x"
                                                                             :action-property "to.x"
                                                                             :offset          20}]
                                                              :to          {:duration 0.1},
                                                              :from-params [{:param-property "target", :action-property "transition-id"}]}
                                                             {:type        "transition",
                                                              :from-var    [{:var-name        "cloud-left-x"
                                                                             :action-property "to.x"
                                                                             :offset          0}]
                                                              :to          {:duration 0.1},
                                                              :from-params [{:param-property "target", :action-property "transition-id"}]}]}
                        :shake-left                  {:type "sequence-data"
                                                      :data [{:type "action" :id "shake-step-left"}
                                                             {:type "action" :id "shake-step-left"}
                                                             {:type "action" :id "shake-step-left"}]}
                        :cloud-left-click-check      {:type     "test-var-scalar",
                                                      :success  "cloud-left-clicked",
                                                      :var-name "left-click-unlocked"
                                                      :value    true}
                        :cloud-left-clicked          {:type        "test-var-scalar",
                                                      :success     "cloud-left-clicked-correct",
                                                      :fail        "shake-left",
                                                      :var-name    "next-cloud"
                                                      :from-params [{:param-property "target", :action-property "value"}]}
                        :cloud-left-clicked-correct  {:type "sequence-data"
                                                      :data [{:type "action" :id "update-left"}
                                                             {:type     "action"
                                                              :from-var [{:template "cloud-left-dialog-%" :var-name "unique-suffix" :action-property "id"}]}
                                                             {:type "action" :id "animate-clouds"}]}
                        :shake-step-right            {:type "sequence-data"
                                                      :data [{:type        "transition",
                                                              :from-var    [{:var-name        "cloud-right-x"
                                                                             :action-property "to.x"
                                                                             :offset          20}]
                                                              :to          {:duration 0.1},
                                                              :from-params [{:param-property "target", :action-property "transition-id"}]}
                                                             {:type        "transition",
                                                              :from-var    [{:var-name        "cloud-right-x"
                                                                             :action-property "to.x"
                                                                             :offset          0}]
                                                              :to          {:duration 0.1},
                                                              :from-params [{:param-property "target", :action-property "transition-id"}]}]}
                        :shake-right                 {:type "sequence-data"
                                                      :data [{:type "action" :id "shake-step-right"}
                                                             {:type "action" :id "shake-step-right"}
                                                             {:type "action" :id "shake-step-right"}]}
                        :cloud-right-clicked         {:type        "test-var-scalar",
                                                      :success     "cloud-right-clicked-correct",
                                                      :fail        "shake-right",
                                                      :var-name    "next-cloud"
                                                      :from-params [{:param-property "target", :action-property "value"}]}
                        :cloud-right-clicked-correct {:type "parallel"
                                                      :data [{:type     "action"
                                                              :from-var [{:template "cloud-right-dialog-%" :var-name "unique-suffix" :action-property "id"}]}
                                                             {:type "set-variable" :var-name "left-click-unlocked" :var-value false}
                                                             {:type "sequence-data"
                                                              :data [{:type "action" :id "update-right"}
                                                                     {:type "counter" :counter-action "increase" :counter-id "step-counter"}
                                                                     {:type "action" :id "animate-clouds"}]}
                                                             {:type "sequence-data"
                                                              :data [{:type       "test-var-inequality"
                                                                      :var-name   "step-counter",
                                                                      :value      3,
                                                                      :inequality ">=",
                                                                      :success    "finish-step",}
                                                                     {:type "set-variable" :var-name "left-click-unlocked" :var-value true}]}]}
                        :finish-step                 {:type       "sequence-data"
                                                      :unique-tag "finish-step"
                                                      :data       [{:type "action" :id "animate-finish-step"}
                                                                   {:type     "action"
                                                                    :from-var [{:template        "correct-answer-dialog-%"
                                                                                :var-name        "unique-suffix"
                                                                                :action-property "id"}]}
                                                                   {:type "action" :id "next-step"}]}
                        :animate-finish-step         {:type "sequence-data"
                                                      :data [{:type     "transition" :to {:opacity 0 :duration 0.1}
                                                              :from-var [{:template "moving-clouds-%" :var-name "unique-suffix" :action-property "transition-id"}]}
                                                             {:type     "state" :id "hide"
                                                              :from-var [{:template "moving-clouds-%" :var-name "unique-suffix" :action-property "target"}]}
                                                             {:type     "state" :id "show"
                                                              :from-var [{:template "cloud-center-%" :var-name "unique-suffix" :action-property "target"}]}
                                                             {:type     "transition" :to {:opacity 1 :duration 1}
                                                              :from-var [{:template "cloud-center-%" :var-name "unique-suffix" :action-property "transition-id"}]}
                                                             {:type     "action"
                                                              :from-var [{:template "cloud-center-dialog-%" :var-name "unique-suffix" :action-property "id"}]}
                                                             {:type "empty" :duration 2000}
                                                             {:type     "transition" :to {:opacity 0 :duration 0.01}
                                                              :from-var [{:template "cloud-center-%" :var-name "unique-suffix" :action-property "transition-id"}]}
                                                             {:type     "state" :id "hide"
                                                              :from-var [{:template "cloud-center-%" :var-name "unique-suffix" :action-property "target"}]}]}
                        :next-step                   {:type "sequence-data"
                                                      :data [{:type "counter" :counter-action "reset" :counter-value 0 :counter-id "step-counter"}
                                                             {:type "counter" :counter-action "increase" :counter-id "goal-counter"}
                                                             {:type "action" :id "check-scene-finished"}]}
                        :check-scene-finished        {:type       "test-var-inequality"
                                                      :var-name   "goal-counter",
                                                      :value      0,
                                                      :inequality ">=",
                                                      :success    "finish-scene",
                                                      :fail       "init-next",}
                        :init-next                   {:type "sequence-data"
                                                      :data [{:type "counter" :counter-action "increase" :counter-id "unique-suffix"}
                                                             {:type "action" :id "init-scene-cloud-vars"}
                                                             {:type "action" :id "sync-highlights"}
                                                             {:type "set-variable" :var-name "left-click-unlocked" :var-value true}
                                                             {:type     "state" :id "show"
                                                              :from-var [{:template "moving-clouds-%" :var-name "unique-suffix" :action-property "target"}]}]}
                        :introduce-task              {:type "parallel"
                                                      :data [{:type "action" :id "intro-dialog"}
                                                             {:type "sequence-data"
                                                              :data [{:type "set-variable" :var-name "unique-suffix" :var-value -1}
                                                                     {:type "action" :id "init-scene-cloud-vars"}
                                                                     {:type "set-variable" :var-name "step-counter" :var-value 0}
                                                                     {:type "action" :id "sync-highlights"}
                                                                     {:type "empty" :duration 500}
                                                                     {:type "set-variable" :var-name "step-counter" :var-value 0}
                                                                     {:type "action" :id "update-left"}
                                                                     {:type "action" :id "animate-clouds"}
                                                                     {:type "empty" :duration 410}
                                                                     {:type "action" :id "update-right"}
                                                                     {:type "action" :id "animate-clouds"}
                                                                     {:type "empty" :duration 320}
                                                                     {:type "set-variable" :var-name "step-counter" :var-value 1}
                                                                     {:type "action" :id "update-left"}
                                                                     {:type "action" :id "animate-clouds"}
                                                                     {:type "empty" :duration 230}
                                                                     {:type "action" :id "update-right"}
                                                                     {:type "action" :id "animate-clouds"}
                                                                     {:type "empty" :duration 140}
                                                                     {:type "set-variable" :var-name "step-counter" :var-value 2}
                                                                     {:type "action" :id "update-left"}
                                                                     {:type "action" :id "animate-clouds"}
                                                                     {:type "empty" :duration 50}
                                                                     {:type "action" :id "update-right"}
                                                                     {:type "action" :id "animate-clouds"}
                                                                     {:type "empty" :duration 10}
                                                                     {:type "action" :id "animate-finish-step"}]}]}
                        :cloud-center-dialog--1      {:type "empty" :duration 0}
                        :init-scene                  {:type "sequence-data"
                                                      :data [{:type "start-activity"},
                                                             {:type "action" :id "introduce-task"}
                                                             {:type "set-variable" :var-name "left-click-unlocked" :var-value true}
                                                             {:type "set-variable" :var-name "unique-suffix" :var-value 0}
                                                             {:type "action" :id "init-scene-cloud-vars"}
                                                             {:type "counter" :counter-action "reset" :counter-value 0 :counter-id "goal-counter"}
                                                             {:type "counter" :counter-action "reset" :counter-value 0 :counter-id "step-counter"}
                                                             {:type "action" :id "sync-highlights"}
                                                             {:type     "state" :id "show"
                                                              :from-var [{:template "moving-clouds-%" :var-name "unique-suffix" :action-property "target"}]}]}
                        :intro-dialog                {:type               "sequence-data",
                                                      :editor-type        "dialog",
                                                      :data               [{:type "sequence-data"
                                                                            :data [{:type "empty" :duration 0}
                                                                                   {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                      :phrase             "intro",
                                                      :phrase-description "Activity Introduction"}
                        :finish-scene                {:type "sequence-data",
                                                      :data [{:type "action" :id "finish-dialog"}
                                                             {:type "finish-activity"}]}
                        :finish-dialog               {:type               "sequence-data",
                                                      :editor-type        "dialog",
                                                      :data               [{:type "sequence-data"
                                                                            :data [{:type "empty" :duration 0}
                                                                                   {:type "animation-sequence", :phrase-text "New action", :audio nil}]}],
                                                      :phrase             "Task Completion",
                                                      :phrase-description "Task Completion"}}
        :triggers      {:start {:on "start" :action "init-scene"}}
        :metadata      {:autostart true}})

(defn add-cloud
  [suffix args]
  [{(common/make-name-unique-by-suffix "cloud-left-dialog" suffix)     {:type               "sequence-data",
                                                                        :editor-type        "dialog",
                                                                        :data               [{:type "sequence-data"
                                                                                              :data [{:type "empty" :duration 0}
                                                                                                     {:type "animation-sequence", :phrase-text (:left-text args), :audio nil}]}],
                                                                        :phrase             "cloud-left-dialog",
                                                                        :phrase-description "Cloud left dialog"}
    (common/make-name-unique-by-suffix "cloud-right-dialog" suffix)    {:type               "sequence-data",
                                                                        :editor-type        "dialog",
                                                                        :data               [{:type "sequence-data"
                                                                                              :data [{:type "empty" :duration 0}
                                                                                                     {:type "animation-sequence", :phrase-text (:right-text args), :audio nil}]}],
                                                                        :phrase             "cloud-right-dialog",
                                                                        :phrase-description "Cloud right dialog"}
    (common/make-name-unique-by-suffix "cloud-center-dialog" suffix)   {:type               "sequence-data",
                                                                        :editor-type        "dialog",
                                                                        :data               [{:type "sequence-data"
                                                                                              :data [{:type "empty" :duration 0}
                                                                                                     {:type "animation-sequence", :phrase-text (:whole-text args), :audio nil}]}],
                                                                        :phrase             "cloud-center-dialog",
                                                                        :phrase-description "Cloud center dialog"}
    (common/make-name-unique-by-suffix "correct-answer-dialog" suffix) {:type               "sequence-data",
                                                                        :editor-type        "dialog",
                                                                        :data               [{:type "sequence-data"
                                                                                              :data [{:type "empty" :duration 0}
                                                                                                     {:type        "animation-sequence",
                                                                                                      :phrase-text "New action",
                                                                                                      :audio       nil}]}],
                                                                        :phrase             "correct-answer",
                                                                        :phrase-description "Correct answer"}}

   {(common/make-name-unique-by-suffix "cloud-left-img" suffix)        {:type   "image"
                                                                        :src    "/raw/img/onset-and-rime/cloud.png"
                                                                        :x      0
                                                                        :states cloud-states,
                                                                        :y      0}
    (common/make-name-unique-by-suffix "cloud-left-text" suffix)       {:type           "text"
                                                                        :text           (:left-text args)
                                                                        :x              320
                                                                        :y              220
                                                                        :align          "center"
                                                                        :vertical-align "bottom"
                                                                        :font-family    "Lexend Deca"
                                                                        :font-size      110
                                                                        :fill           "black"}
    (common/make-name-unique-by-suffix "cloud-left" suffix)            {:type       "group"
                                                                        :x          25
                                                                        :y          176
                                                                        :transition (common/make-name-unique-by-suffix "cloud-left" suffix)
                                                                        :children   [(common/make-name-unique-by-suffix "cloud-left-img" suffix)
                                                                                     (common/make-name-unique-by-suffix "cloud-left-text" suffix)]
                                                                        :states     {:disable {:interactive false}
                                                                                     :enable  {:interactive true}}
                                                                        :actions    {:click {:type   "action"
                                                                                             :id     "cloud-left-click-check"
                                                                                             :on     "click"
                                                                                             :params {:target     (common/make-name-unique-by-suffix "cloud-left" suffix)
                                                                                                      :target-img (common/make-name-unique-by-suffix "cloud-left-img" suffix)}}}},
    (common/make-name-unique-by-suffix "cloud-right-img" suffix)       {:type       "image"
                                                                        :src        "/raw/img/onset-and-rime/cloud.png"
                                                                        :transition (common/make-name-unique-by-suffix "cloud-right-img" suffix)
                                                                        :states     cloud-states
                                                                        :x          0
                                                                        :y          0}
    (common/make-name-unique-by-suffix "cloud-right-text" suffix)      {:type           "text"
                                                                        :text           (:right-text args)
                                                                        :x              320
                                                                        :y              220
                                                                        :align          "center"
                                                                        :vertical-align "bottom"
                                                                        :font-family    "Lexend Deca"
                                                                        :font-size      110
                                                                        :fill           "black"}
    (common/make-name-unique-by-suffix "cloud-right" suffix)           {:type       "group"
                                                                        :x          1214
                                                                        :y          176
                                                                        :transition (common/make-name-unique-by-suffix "cloud-right" suffix)
                                                                        :children   [(common/make-name-unique-by-suffix "cloud-right-img" suffix)
                                                                                     (common/make-name-unique-by-suffix "cloud-right-text" suffix)]
                                                                        :states     {:disable {:interactive false}
                                                                                     :enable  {:interactive true}}
                                                                        :actions    {:click {:type   "action"
                                                                                             :id     "cloud-right-clicked"
                                                                                             :on     "click"
                                                                                             :params {:target     (common/make-name-unique-by-suffix "cloud-right" suffix)
                                                                                                      :target-img (common/make-name-unique-by-suffix "cloud-right-img" suffix)}}}}
    (common/make-name-unique-by-suffix "moving-clouds" suffix)         {:type       "group"
                                                                        :visible    false
                                                                        :transition (common/make-name-unique-by-suffix "moving-clouds" suffix)
                                                                        :children   [(common/make-name-unique-by-suffix "cloud-right" suffix)
                                                                                     (common/make-name-unique-by-suffix "cloud-left" suffix)]
                                                                        :states     {:hide {:visible false} :show {:visible true}}}

    (common/make-name-unique-by-suffix "cloud-center-img" suffix)      {:type       "image"
                                                                        :src        "/raw/img/onset-and-rime/cloud.png"
                                                                        :transition (common/make-name-unique-by-suffix "cloud-center-img" suffix)
                                                                        :x          0
                                                                        :y          0}
    (common/make-name-unique-by-suffix "cloud-center-text-img" suffix) {:type       "image"
                                                                        :src        (get-in args [:image :src])
                                                                        :transition (common/make-name-unique-by-suffix "cloud-center-text-img" suffix)
                                                                        :x          400
                                                                        :y          120}
    (common/make-name-unique-by-suffix "cloud-center-text" suffix)     {:type           "text"
                                                                        :text           (:whole-text args)
                                                                        :x              250
                                                                        :y              220
                                                                        :align          "center"
                                                                        :vertical-align "bottom"
                                                                        :font-family    "Lexend Deca"
                                                                        :font-size      110
                                                                        :fill           "black"}
    (common/make-name-unique-by-suffix "cloud-center" suffix)          {:type       "group"
                                                                        :x          600
                                                                        :y          176
                                                                        :transition (common/make-name-unique-by-suffix "cloud-center" suffix)
                                                                        :visible    false
                                                                        :opacity    0
                                                                        :children   [(common/make-name-unique-by-suffix "cloud-center-img" suffix)
                                                                                     (common/make-name-unique-by-suffix "cloud-center-text" suffix)
                                                                                     (common/make-name-unique-by-suffix "cloud-center-text-img" suffix)]
                                                                        :states     {:hide {:visible false} :show {:visible true}}}}

   [(common/make-name-unique-by-suffix "cloud-center" suffix) (common/make-name-unique-by-suffix "moving-clouds" suffix)]])

(defn- set-data
  [activity-data args]
  (-> activity-data
      (assoc-in [:objects :cloud-left-text--1 :text] (:left-text args))
      (assoc-in [:objects :cloud-right-text--1 :text] (:right-text args))
      (assoc-in [:objects :cloud-center-text--1 :text] (:whole-text args))
      (assoc-in [:objects :cloud-center-text-img--1 :src] (get-in args [:image :src]))))

(defn create-activity
  [args]
  (-> (common/init-metadata m t args)
      (set-data args)
      (assoc-in [:metadata :saved-props :template-options] (select-keys args [:left-text :right-text :whole-text :image]))))

(defn- add-word
  [scene args]
  (let [suffix (common/get-unique-suffix scene)
        [actions objects scene-objects] (add-cloud suffix args)]
    (-> scene
        (update-in [:objects] merge objects)
        (update-in [:actions] merge actions)
        (update-in [:actions :check-scene-finished :value] inc)
        (common/add-scene-object scene-objects)
        (common/add-track-actions (vec (map name (keys actions))) "dialog" (str "Word " (inc (common/get-unique-suffix scene))))
        (common/update-unique-suffix))))

(defn- add-word-action
  [scene args]
  (let [suffix (common/get-unique-suffix scene)]
    (-> scene
        (add-word args)
        (update-in [:metadata :saved-props :template-options :rounds] concat [(assoc args
                                                                                :id suffix)]))))
(defn- delete-round
  [activity-data round-id]
  (let [[actions objects scene-objects] (add-cloud round-id {})
        action-names (keys actions)
        object-names (keys objects)]
    (-> activity-data
        (common/remove-objects object-names)
        (common/remove-actions action-names))))

(defn- delete-rounds
  [activity-data rounds]
  (reduce delete-round activity-data rounds))

(defn- edit-round
  [activity-data {:keys [id left-text right-text whole-text image] :as round}]
  (let [left-text-name (common/make-name-unique-by-suffix "cloud-left-text" id)
        right-text-name (common/make-name-unique-by-suffix "cloud-right-text" id)
        center-img-name (common/make-name-unique-by-suffix "cloud-center-text-img" id)
        center-text-name (common/make-name-unique-by-suffix "cloud-center-text" id)]
    (-> activity-data
        (assoc-in [:objects left-text-name :text] left-text)
        (assoc-in [:objects right-text-name :text] right-text)
        (assoc-in [:objects center-text-name :text] whole-text)
        (assoc-in [:objects center-img-name :src] (:src image)))))

(defn- edit-rounds
  [activity-data rounds]
  (reduce edit-round activity-data rounds))

(defn- add-rounds
  [activity-data rounds]
  (reduce add-word activity-data rounds))

(defn- process-rounds
  [activity-data {:keys [rounds delete-last-round] :as args}]
  (let [prev-rounds-number (-> activity-data
                               (get-in [:metadata :saved-props :template-options :rounds])
                               (count))
        new-rounds-number (count rounds)
        rounds-to-delete (concat (if delete-last-round
                                   [prev-rounds-number]
                                   [])
                                 (range new-rounds-number prev-rounds-number))
        rounds-to-edit (->> rounds
                            (take prev-rounds-number)
                            (drop-last (if delete-last-round 1 0)))
        rounds-to-add (->> rounds
                           (drop (count rounds-to-edit)))]
    (-> activity-data
        (delete-rounds rounds-to-delete)
        (edit-rounds rounds-to-edit)
        (add-rounds rounds-to-add)
        (assoc-in [:actions :check-scene-finished :value] new-rounds-number)
        (assoc-in [:metadata :unique-suffix] new-rounds-number)
        (assoc-in [:metadata :saved-props :template-options :rounds] rounds))))

(defn- template-options
  [activity-data args]
  (-> activity-data
      (set-data args)
      (process-rounds args)))

(defn- update-activity
  [old-data {:keys [action-name] :as args}]
  (case (keyword action-name)
    :add-ball (add-word-action old-data args)
    :template-options (template-options old-data args)))

(core/register-template
 m create-activity update-activity)
