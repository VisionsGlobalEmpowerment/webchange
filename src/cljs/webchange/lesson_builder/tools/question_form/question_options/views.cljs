(ns webchange.lesson-builder.tools.question-form.question-options.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.components.check-list.views :refer [check-list]]
    [webchange.lesson-builder.components.toolbox.views :refer [toolbox]]
    [webchange.lesson-builder.tools.question-form.question-options.state :as state]
    [webchange.ui.index :as ui]))

(defn- control-group
  [{:keys [label]}]
  [:div.control-group
   [ui/input-label label]
   (->> (r/current-component)
        (r/children)
        (into [:div.control-group--content]))])

(defn question-alias
  []
  (let [value @(re-frame/subscribe [::state/question-alias])
        handle-change #(re-frame/dispatch [::state/set-question-alias %])]
    [ui/input {:label       "Question Name"
               :placeholder "Name"
               :value       value
               :on-change   handle-change}]))

(defn question-type
  []
  (let [value @(re-frame/subscribe [::state/question-type])
        options @(re-frame/subscribe [::state/question-type-options])
        handle-change #(re-frame/dispatch [::state/set-question-type %])]
    [check-list {:items    options
                 :value    value
                 :on-click handle-change}]))

(defn task-type
  []
  (let [value @(re-frame/subscribe [::state/task-type])
        options @(re-frame/subscribe [::state/task-type-options])
        handle-change #(re-frame/dispatch [::state/set-task-type %])]
    [control-group {:label "Type"}
     [ui/select {:value     value
                 :options   options
                 :on-change handle-change}]]))

(defn options-number
  []
  (let [value @(re-frame/subscribe [::state/options-number])
        options @(re-frame/subscribe [::state/options-number-options])
        show? @(re-frame/subscribe [::state/show-options-number?])
        handle-change #(re-frame/dispatch [::state/set-options-number %])]
    (when show?
      [control-group {:label "Options number"}
       [ui/select {:value     value
                   :on-change handle-change
                   :options   options
                   :type      "int"}]])))

(defn mark-options
  []
  (let [show? @(re-frame/subscribe [::state/show-mark-options?])
        options @(re-frame/subscribe [::state/available-mark-options])
        handle-change #(re-frame/dispatch [::state/set-mark-option %1 %2])]
    (when show?
      [control-group {:label "Show Options"}
       (for [{:keys [checked? text value]} options]
         ^{:key value}
         [ui/switch {:checked?   checked?
                     :label      text
                     :label-side "right"
                     :value      value
                     :on-change  handle-change}])])))

(defn answers-number
  []
  (let [value @(re-frame/subscribe [::state/answers-number])
        options @(re-frame/subscribe [::state/answers-number-options])
        handle-change #(re-frame/dispatch [::state/set-answers-number %])]
    [control-group {:label "How Many Correct Answers?"}
     [ui/select {:value     value
                 :on-change handle-change
                 :options   options}]]))

(defn correct-answers-one
  []
  (let [value @(re-frame/subscribe [::state/correct-answers-one])
        options @(re-frame/subscribe [::state/correct-answers-one-options])
        handle-change #(re-frame/dispatch [::state/set-correct-answers-one %])]
    [control-group {:label "Correct answer"}
     [ui/select {:value       value
                 :options     options
                 :on-change   handle-change
                 :placeholder "Choose correct answer"}]]))

(defn correct-answers-many
  []
  (let [options @(re-frame/subscribe [::state/correct-answers-many-options])
        handle-change #(re-frame/dispatch [::state/set-correct-answers-many %1 %2])]
    [control-group {:label "Correct answers"}
     (for [{:keys [checked? text value]} options]
       ^{:key value}
       [ui/switch {:checked?   checked?
                   :label      text
                   :label-side "right"
                   :value      value
                   :on-change  handle-change}])]))

(defn correct-answers
  []
  (let [show-correct-answers? @(re-frame/subscribe [::state/show-correct-answers?])
        answers-number @(re-frame/subscribe [::state/answers-number])]
    (when show-correct-answers?
      (case answers-number
        "one" [correct-answers-one]
        "many" [correct-answers-many]
        nil))))
