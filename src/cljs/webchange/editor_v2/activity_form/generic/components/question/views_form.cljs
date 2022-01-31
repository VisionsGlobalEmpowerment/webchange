(ns webchange.editor-v2.activity-form.generic.components.question.views-form
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.generic.components.question.state :as state]
    [webchange.editor-v2.activity-form.generic.components.question.views-preview :refer [preview]]
    [webchange.question.get-question-data :refer [available-values]]
    [webchange.ui-framework.components.index :refer [checkbox label select input]]
    [webchange.utils.list :refer [without-item]]))

(defn- question-alias-control
  []
  (let [field-key :alias
        value @(re-frame/subscribe [::state/field-value field-key])
        handle-change #(re-frame/dispatch [::state/set-field-value field-key %])]
    [:div.option-group
     [label "Question name"]
     [input {:value       value
             :on-change   handle-change
             :placeholder "Enter question name"
             :variant     "outlined"}]
     [label {:class-name "explanation-label"}
      "Name your question so you can find and drag it into the correct place in the script editor."]]))

(defn- question-type-control
  []
  (let [field-key :question-type
        value @(re-frame/subscribe [::state/field-value field-key])
        handle-change #(re-frame/dispatch [::state/set-field-value field-key %])
        options @(re-frame/subscribe [::state/question-type-options])]
    [:div.option-group
     [label "Question type"]
     [select {:value     value
              :on-change handle-change
              :options   options
              :variant   "outlined"}]]))

(defn- task-type-control
  []
  (let [field-key :task-type
        value @(re-frame/subscribe [::state/field-value field-key])
        handle-change #(re-frame/dispatch [::state/set-field-value field-key %])
        options @(re-frame/subscribe [::state/task-type-options])]
    [:div.option-group
     [label {:class-name "label"} "Task"]
     [select {:value     value
              :on-change handle-change
              :options   options
              :variant   "outlined"}]]))

(defn- layout-control
  []
  (let [field-key :layout
        value @(re-frame/subscribe [::state/field-value field-key])
        handle-change #(re-frame/dispatch [::state/set-field-value field-key %])
        options [{:text  "Horizontal"
                  :value "horizontal"}
                 {:text  "Vertical"
                  :value "vertical"}]
        task-type @(re-frame/subscribe [::state/field-value :task-type])]
    (when (= task-type "text-image")
      [:div.option-group
       [label {:class-name "label"} "Layout"]
       [select {:value     value
                :on-change handle-change
                :options   options
                :variant   "outlined"}]])))

(defn- get-mark-option
  [{:keys [value on-change parent-value text]}]
  (let [checked? (->> parent-value (some #{value}) (boolean))]
    [[label {:class-name "mark-option-label"} text]
     [checkbox {:value     value
                :checked?  checked?
                :on-change on-change}]]))

(defn- mark-options-control
  []
  (let [options-list (:mark-options available-values)
        list->map (fn [values-list]
                    (->> options-list
                         (map (fn [option-value] [option-value (->> values-list (some #{option-value}) (boolean))]))
                         (into {})))
        map->list (fn [value]
                    (->> value
                         (filter second)
                         (map first)
                         (vec)))

        field-key :mark-options
        value @(re-frame/subscribe [::state/field-value field-key])
        handle-change #(re-frame/dispatch [::state/set-field-value field-key (-> (list->map value)
                                                                                 (assoc (:value %) (:checked? %))
                                                                                 (map->list))])

        question-type @(re-frame/subscribe [::state/field-value :question-type])]
    (when (= question-type "thumbs-up-n-down")
      [:div.option-group
       [label {:class-name "label"} "Options"]
       (into [:div.mark-options]
             (reduce (fn [result mark]
                       (concat result (get-mark-option {:text         mark
                                                        :value        mark
                                                        :on-change    handle-change
                                                        :parent-value value})))
                     []
                     (:mark-options available-values)))])))

(defn- options-number-control
  []
  (let [field-key :options-number
        value @(re-frame/subscribe [::state/field-value field-key])
        handle-change #(re-frame/dispatch [::state/set-field-value field-key %])
        options (->> (range 2 5)
                     (map (fn [number]
                            {:text  number
                             :value number})))
        question-type @(re-frame/subscribe [::state/field-value :question-type])]
    (when (not= question-type "thumbs-up-n-down")
      [:div.option-group
       [label {:class-name "label"} "Options number"]
       [select {:value     value
                :on-change handle-change
                :options   options
                :type      "int"
                :variant   "outlined"}]])))

(defn- answers-number-control
  []
  (let [field-key :answers-number
        value @(re-frame/subscribe [::state/field-value field-key])
        handle-change #(do (re-frame/dispatch [::state/set-field-value field-key %])
                           (re-frame/dispatch [::state/set-field-value :correct-answers []]))
        options [{:text  "One"
                  :value "one"}
                 {:text  "Many"
                  :value "many"}
                 {:text  "Any"
                  :value "any"}]]
    [:div.option-group
     [label {:class-name "label"} "Correct answers number"]
     [select {:value     value
              :on-change handle-change
              :options   options
              :variant   "outlined"}]]))

(defn- correct-answer-control
  []
  (let [answers-number @(re-frame/subscribe [::state/field-value :answers-number])
        mark-options @(re-frame/subscribe [::state/field-value :mark-options])
        options-number @(re-frame/subscribe [::state/field-value :options-number])
        question-type @(re-frame/subscribe [::state/field-value :question-type])

        field-key :correct-answers
        correct-answers @(re-frame/subscribe [::state/field-value field-key])
        handle-change #(re-frame/dispatch [::state/set-field-value field-key (if (= answers-number "one") [%] %)])

        options (if (= question-type "thumbs-up-n-down")
                  (->> mark-options
                       (map (fn [value]
                              {:text  value
                               :value value})))
                  (->> (range 1 (inc options-number))
                       (map (fn [number]
                              {:text  (str "Option " number)
                               :value (str "option-" number)}))))]

    (print "correct-answers" correct-answers)

    (when (not= answers-number "any")
      [:div.option-group
       [label {:class-name "label"} (str "Correct answer" (if (= answers-number "one") "" "s"))]
       (if (= answers-number "one")
         [select (cond-> {:value     (first correct-answers)
                          :on-change handle-change
                          :options   options
                          :variant   "outlined"}
                         (= answers-number "one") (assoc :placeholder "Select correct answers"))]
         [:div
          (for [{:keys [text value]} options]
            ^{:key value}
            [checkbox {:checked?  (or (some #{value} correct-answers) false)
                       :label     text
                       :value     value
                       :on-change (fn [{:keys [checked? value]}]
                                    (if checked?
                                      (handle-change (conj correct-answers value))
                                      (handle-change (without-item correct-answers value))))}])])])))

(defn question-form
  []
  [:div.question-form
   [question-alias-control]
   [question-type-control]
   [:hr]
   [:div.options-groups
    [:div
     [task-type-control]
     [options-number-control]
     [mark-options-control]
     ;[layout-control]
     ]
    [:div
     [answers-number-control]
     [correct-answer-control]]]
   [label {:class-name "explanation-label"}
    "After you hit save, use the script editor boxes to fill in your actual question and answers."]
   [:hr]
   [preview]])
