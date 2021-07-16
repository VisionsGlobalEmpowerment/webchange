(ns webchange.editor-v2.wizard.activity-template.question.multiple-choice-image.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]
    [webchange.ui-framework.components.index :refer [label select]]))

(def default-values
  {:task-type       "text"
   :layout          "horizontal"
   :option-label    "audio-text"
   :options-number  2
   :answers-number  "many"
   :correct-answers [0]})

(def params {:task-type    {:title         "Task"
                            :default-value (:task-type default-values)
                            :options       [{:text  "Text with image"
                                             :value "text-image"}
                                            {:text  "Text only"
                                             :value "text"}]}
             :layout       {:title         "Layout"
                            :default-value (:layout default-values)
                            :options       [{:text  "Horizontal"
                                             :value "horizontal"}
                                            {:text      "Vertical"
                                             :value     "vertical"
                                             :disabled? true}]}
             :option-label {:title         "Option label"
                            :default-value (:option-label default-values)
                            :options       [{:text      "Audio only"
                                             :value     "audio"
                                             :disabled? true}
                                            {:text  "Audio + text"
                                             :value "audio-text"}
                                            {:text      "Empty"
                                             :value     "none"
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

(defn- answers-count-control
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

(defn- correct-answer-control
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

(defn multiple-choice-image
  [{:keys [data]}]
  (let [current-task-type (get-in @data [:task-type])
        options-number (get-in @data [:options-number])
        answers-number (get-in @data [:answers-number])]
    [:div.options-groups
     [:div
      [param-select {:key :task-type :data data}]
      (when (= current-task-type "text-image")
        [param-select {:key :layout :data data}])
      [param-select {:key :option-label :data data}]]
     [:div
      [options-number-control {:data data}]
      ^{:key (str options-number)}
      [answers-count-control {:data data}]
      ^{:key (str options-number "-" answers-number)}
      [correct-answer-control {:data data}]]]))
