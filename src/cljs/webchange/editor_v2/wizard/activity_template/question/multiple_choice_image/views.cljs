(ns webchange.editor-v2.wizard.activity-template.question.multiple-choice-image.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]
    [webchange.ui-framework.components.index :refer [label select]]))

(defn- correct-answers-count
  []
  (r/with-let [value (r/atom :one)
               handle-change #(reset! value %)
               options [{:text  "One answer"
                         :value "one"}
                        {:text  "Multiple choice"
                         :value "multiple"}]]
    [:div.option-group
     [label "Number of correct answers:"]
     [select {:value     @value
              :on-change handle-change
              :options   options
              :variant   "outlined"}]]))

(def params {:task-type             {:label         "Task"
                                     :default-value "text-image"
                                     :options       [{:text  "Text with image"
                                                      :value "text-image"}
                                                     {:text  "Text only"
                                                      :value "text"}]}
             :layout                {:label         "Layout"
                                     :default-value "horizontal"
                                     :options       [{:text  "Horizontal"
                                                      :value "horizontal"}
                                                     {:text  "Vertical"
                                                      :value "vertical"}]}
             :options-number        {:label         "Options number"
                                     :default-value 2
                                     :options       (map (fn [number]
                                                           {:text  number
                                                            :value number})
                                                         [2 3 4])}
             :option-label          {:label         "Option label"
                                     :default-value "audio"
                                     :options       [{:text  "Audio only"
                                                      :value "audio"}
                                                     {:text  "Audio + text"
                                                      :value "audio-text"}]}
             :correct-answers-count {:label         "Number of correct answers"
                                     :default-value "one"
                                     :options       [{:text  "One answer"
                                                      :value "one"}
                                                     {:text  "Multiple choice"
                                                      :value "multiple"}]}})

(defn param-select
  [{:keys [data key]}]
  (r/with-let [param-label (get-in params [key :label])
               value (connect-data data [key] (get-in params [key :default-value]))
               options (get-in params [key :options])
               handle-change #(reset! value %)]
    [:div.option-group
     [label param-label]
     [select {:value     @value
              :on-change handle-change
              :options   options
              :variant   "outlined"}]]))

(defn multiple-choice-image
  [{:keys [data]}]
  (let [current-task-type (get-in @data [:task-type])]
    [:div
     [param-select {:key :task-type :data data}]
     (when (= current-task-type "text-image")
       [param-select {:key :layout :data data}])
     [param-select {:key :options-number :data data}]
     [param-select {:key :option-label :data data}]
     [param-select {:key :correct-answers-count :data data}]]))
