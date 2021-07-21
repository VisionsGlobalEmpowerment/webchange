(ns webchange.editor-v2.wizard.activity-template.question.multiple-choice-image.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]
    [webchange.ui-framework.components.index :refer [label select]]))

(def params {:task-type             {:title         "Task"
                                     :default-value "text-image"
                                     :options       [{:text  "Text with image"
                                                      :value "text-image"}
                                                     {:text      "Text only"
                                                      :value     "text"
                                                      :disabled? true}]}
             :layout                {:title         "Layout"
                                     :default-value "horizontal"
                                     :options       [{:text  "Horizontal"
                                                      :value "horizontal"}
                                                     {:text      "Vertical"
                                                      :value     "vertical"
                                                      :disabled? true}]}
             :options-number        {:title         "Options number"
                                     :default-value 4
                                     :type          "int"
                                     :options       (map (fn [number]
                                                           {:text  number
                                                            :value number})
                                                         [2 3 4])}
             :option-label          {:title         "Option label"
                                     :default-value "audio-text"
                                     :options       [{:text      "Audio only"
                                                      :value     "audio"
                                                      :disabled? true}
                                                     {:text  "Audio + text"
                                                      :value "audio-text"}
                                                     {:text      "Empty"
                                                      :value     "none"
                                                      :disabled? true}]}
             :correct-answers-count {:title         "Number of correct answers"
                                     :default-value "one"
                                     :options       [{:text  "One answer"
                                                      :value "one"}
                                                     {:text      "Multiple choice"
                                                      :value     "multiple"
                                                      :disabled? true}]}})

(defn param-select
  [{:keys [data key]}]
  (r/with-let [{:keys [default-value options title type]} (get params key)
               value (connect-data data [key] default-value)
               handle-change #(reset! value %)]
    [:div.option-group
     [label {:class-name "label"} title]
     [select (cond-> {:value     @value
                      :on-change handle-change
                      :options   options
                      :variant   "outlined"}
                     (some? type) (assoc :type type))]]))

(defn multiple-choice-image
  [{:keys [data]}]
  (let [current-task-type (get-in @data [:task-type])]
    [:div.options-groups
     [param-select {:key :task-type :data data}]
     (when (= current-task-type "text-image")
       [param-select {:key :layout :data data}])
     [param-select {:key :options-number :data data}]
     [param-select {:key :option-label :data data}]
     [param-select {:key :correct-answers-count :data data}]]))
