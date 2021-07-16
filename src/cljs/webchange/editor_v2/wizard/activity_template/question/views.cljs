(ns webchange.editor-v2.wizard.activity-template.question.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.activity-template.question.multiple-choice-image.views :refer [multiple-choice-image]]
    [webchange.editor-v2.wizard.activity-template.question.views-preview :refer [question-preview]]
    [webchange.editor-v2.wizard.validator :refer [connect-data]]
    [webchange.ui-framework.components.index :refer [label select input]]))

(def question-types
  {:multiple-choice-image {:name      "Multiple choice image"
                           :component multiple-choice-image}})

(defn- question-type-control
  [{:keys [data]}]
  (r/with-let [value (connect-data data [:question-type] (-> question-types keys first))
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
