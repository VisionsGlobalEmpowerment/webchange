(ns webchange.editor-v2.wizard.activity-template.question.multiple-choice-image.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]
    [webchange.ui-framework.components.index :refer [label select]]))

(def params {:task-type             {:title         "Task"
                                     :default-value "text-image"
                                     :options       [{:text  "Text with image"
                                                      :value "text-image"}
                                                     {:text  "Text only"
                                                      :value "text"}]}
             :layout                {:title         "Layout"
                                     :default-value "horizontal"
                                     :options       [{:text  "Horizontal"
                                                      :value "horizontal"}
                                                     {:text      "Vertical"
                                                      :value     "vertical"
                                                      :disabled? true}]}
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

(defn- param-select
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

(defn- options-number-control
  [{:keys [data]}]
  (r/with-let [value (connect-data data [:options-number] 4)
               handle-change #(reset! value %)]
    [:div.option-group
     [label {:class-name "label"} "Options number"]
     [select {:value     @value
              :on-change handle-change
              :options   (->> (range 2 5)
                              (map (fn [number]
                                     {:text  number
                                      :value number})))
              :type      "int"
              :variant   "outlined"}]]))

(defn- correct-answer-control
  [{:keys [data]}]
  (let [value (connect-data data [:correct-answer] 1)
        handle-change #(reset! value %)
        options-number (get-in @data [:options-number])
        options (->> (range 1 (inc options-number))
                     (map (fn [number]
                            {:text  (str "Option " number)
                             :value number})))]
    [:div.option-group
     [label {:class-name "label"} "Correct answer"]
     [select {:value     @value
              :on-change handle-change
              :options   options
              :type      "int"
              :variant   "outlined"}]]))

(defn multiple-choice-image
  [{:keys [data]}]
  (let [current-task-type (get-in @data [:task-type])]
    [:div.options-groups
     [:div
      [param-select {:key :task-type :data data}]
      (when (= current-task-type "text-image")
        [param-select {:key :layout :data data}])
      [param-select {:key :option-label :data data}]]
     [:div
      [options-number-control {:data data}]
      [param-select {:key :correct-answers-count :data data}]
      [correct-answer-control {:data data}]]]))
