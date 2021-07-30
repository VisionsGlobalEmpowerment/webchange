(ns webchange.editor-v2.wizard.activity-template.question.views-controls
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.validator :refer [connect-data]]
    [webchange.ui-framework.components.index :refer [label select]]))

(def default-values
  {:question-type   "multiple-choice-image"
   :task-type       "text-image"
   :layout          "vertical"
   :option-label    "audio-text"
   :options-number  2
   :answers-number  "many"
   :correct-answers [0]})


(defn answers-count
  [{:keys [data]}]
  (r/with-let [value (connect-data data [:answers-number] nil (:answers-number default-values))
               handle-change #(reset! value %)]
    (let [options [{:text  "One"
                    :value "one"}
                   {:text  "Many"
                    :value "many"}]]
      [:div.option-group
       [label {:class-name "label"} "Correct answers number"]
       [select {:value     @value
                :on-change handle-change
                :options   options
                :variant   "outlined"}]])))

(defn correct-answer
  [{:keys [data]}]
  (r/with-let [answers-number (get-in @data [:answers-number])
               value (connect-data data [:correct-answers] nil (:correct-answers default-values))
               handle-change #(reset! value (if (= answers-number "one") [%] %))]
    (let [options-number (get-in @data [:options-number])

          options (->> (range options-number)
                       (map (fn [number]
                              {:text  (str "Option " (inc number))
                               :value number})))]
      [:div.option-group
       [label {:class-name "label"} (str "Correct answer" (if (= answers-number "one") "s" ""))]
       [select {:value     (if (= answers-number "one") (first @value) @value)
                :on-change handle-change
                :options   options
                :type      "int"
                :variant   "outlined"
                :multiple? (not= answers-number "one")}]])))

(defn layout
  [{:keys [data]}]
  (r/with-let [value (connect-data data [:layout] nil (:layout default-values))
               handle-change #(reset! value %)]
    (let [options [{:text  "Horizontal"
                    :value "horizontal"}
                   {:text  "Vertical"
                    :value "vertical"}]]
      [:div.option-group
       [label {:class-name "label"} "Layout"]
       [select {:value     @value
                :on-change handle-change
                :options   options
                :variant   "outlined"}]])))

(defn option-label
  [{:keys [data]}]
  (r/with-let [param-key :option-label
               value (connect-data data [param-key] (get default-values param-key))
               handle-change #(reset! value %)
               options [{:text  "Audio only"
                         :value "audio"}
                        {:text  "Audio + text"
                         :value "audio-text"}
                        {:text  "Empty"
                         :value "none"}]]
    [:div.option-group
     [label {:class-name "label"} "Option label"]
     [select {:value     @value
              :on-change handle-change
              :options   options
              :variant   "outlined"}]]))

(defn options-number
  [{:keys [data]}]
  (r/with-let [value (connect-data data [:options-number] (:options-number default-values))
               handle-change #(reset! value %)
               options (->> (range 2 5)
                            (map (fn [number]
                                   {:text  number
                                    :value number})))]
    [:div.option-group
     [label {:class-name "label"} "Options number"]
     [select {:value     @value
              :on-change handle-change
              :options   options
              :type      "int"
              :variant   "outlined"}]]))

(defn task-type
  [{:keys [data]}]
  (r/with-let [param-key :task-type
               value (connect-data data [param-key] (get default-values param-key))
               handle-change #(reset! value %)
               options [{:text  "Text with image"
                         :value "text-image"}
                        {:text  "Text only"
                         :value "text"}]]
    [:div.option-group
     [label {:class-name "label"} "Task"]
     [select {:value     @value
              :on-change handle-change
              :options   options
              :variant   "outlined"}]]))
