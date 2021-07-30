(ns webchange.editor-v2.wizard.activity-template.question.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.activity-template.question.views-controls :as controls]
    [webchange.editor-v2.wizard.activity-template.question.views-preview :refer [question-preview]]
    [webchange.editor-v2.wizard.validator :refer [connect-data]]
    [webchange.ui-framework.components.index :refer [label select input]]))

(defn multiple-choice-image
  [{:keys [data]}]
  (let [current-task-type (get-in @data [:task-type])
        options-number (get-in @data [:options-number])
        answers-number (get-in @data [:answers-number])]
    [:div.options-groups
     [:div
      [controls/task-type {:data data}]
      (when (= current-task-type "text-image")
        [controls/layout {:data data}])
      [controls/option-label {:data data}]]
     [:div
      [controls/options-number {:data data}]
      ^{:key (str options-number)}
      [controls/answers-count {:data data}]
      ^{:key (str options-number "-" answers-number)}
      [controls/correct-answer {:data data}]]]))

(defn multiple-choice-text
  [{:keys [data]}]
  (let [current-task-type (get-in @data [:task-type])
        options-number (get-in @data [:options-number])
        answers-number (get-in @data [:answers-number])]
    [:div.options-groups
     [:div
      [controls/task-type {:data data}]
      (when (= current-task-type "text-image")
        [controls/layout {:data data}])]
     [:div
      [controls/options-number {:data data}]
      ^{:key (str options-number)}
      [controls/answers-count {:data data}]
      ^{:key (str options-number "-" answers-number)}
      [controls/correct-answer {:data data}]]]))

(def question-types
  {"multiple-choice-image" {:name      "Multiple choice image"
                            :component multiple-choice-image}
   "multiple-choice-text"  {:name      "Multiple choice text"
                            :component multiple-choice-text}})

(defn- question-type-control
  [{:keys [data]}]
  (r/with-let [param-key :question-type
               value (connect-data data [param-key] (get controls/default-values param-key))
               handle-change #(reset! value %)
               options (->> question-types
                            (map (fn [[type {:keys [name]}]]
                                   {:text  name
                                    :value type})))]
    [:div.option-group
     [label "Question type"]
     [select {:value     @value
              :on-change handle-change
              :options   options
              :variant   "outlined"}]]))

(defn- question-alias-control
  [{:keys [data]}]
  (r/with-let [value (connect-data data [:alias] "")
               current-value (r/atom @value)

               handle-blur #(reset! value @current-value)
               handle-change #(reset! current-value %)]
    [:div.option-group
     [label "Question name"]
     [input {:value       @current-value
             :on-change   handle-change
             :on-blur     handle-blur
             :placeholder "Enter question name"
             :variant     "outlined"}]]))

(defn question-option
  [{:keys [key option data validator] :as props}]
  (r/with-let [option-data (connect-data data [key] {})]
    (let [current-question-type (get @option-data :question-type)
          current-question-component (get-in question-types [current-question-type :component])]
      [:div.question-option
       [question-alias-control {:data option-data}]
       [question-type-control {:data option-data}]
       [:hr]
       (when (some? current-question-component)
         [current-question-component (merge props
                                            {:data option-data})])
       [:hr]
       [question-preview @option-data]])))
