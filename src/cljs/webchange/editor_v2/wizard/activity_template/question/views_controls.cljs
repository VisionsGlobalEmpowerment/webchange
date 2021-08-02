(ns webchange.editor-v2.wizard.activity-template.question.views-controls
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.validator :refer [connect-data]]
    [webchange.question.get-question-data :refer [available-mark-options default-options]]
    [webchange.ui-framework.components.index :refer [checkbox label select]]))

(def default-values
  {:question-type  "multiple-choice-text"
   :task-type      "text-image"
   :layout         "vertical"
   :option-label   "audio-text"
   :options-number 2
   :answers-number "many"
   :mark-options   ["thumbs-up" "thumbs-down"]})

(defn answers-count
  [{:keys [any? data]
    :or   {any? false}}]
  (r/with-let [value (connect-data data [:answers-number] nil (:answers-number default-values))
               handle-change #(reset! value %)]
    (let [options (cond-> [{:text  "One"
                            :value "one"}
                           {:text  "Many"
                            :value "many"}]
                          any? (conj {:text  "Any"
                                      :value "any"}))]
      [:div.option-group
       [label {:class-name "label"} "Correct answers number"]
       [select {:value     @value
                :on-change handle-change
                :options   options
                :variant   "outlined"}]])))

(defn correct-answer
  [{:keys [data]}]
  (r/with-let [question-type (get-in @data [:question-type])
               answers-number (get-in @data [:answers-number])
               value (connect-data data [:correct-answers] nil [])
               handle-change #(reset! value (if (= answers-number "one") [%] %))]
    (let [options-number (get-in @data [:options-number])
          options (if (= question-type "thumbs-up-n-down")
                    (->> available-mark-options
                         (filter (fn [{:keys [value]}]
                                   (some #{value} (get @data :mark-options [])))))
                    (->> default-options
                         (take options-number)
                         (map-indexed vector)
                         (map (fn [[number {:keys [value]}]]
                                {:text  (str "Option " (inc number))
                                 :value value}))))]
      [:div.option-group
       [label {:class-name "label"} (str "Correct answer" (if (= answers-number "one") "s" ""))]
       [select (cond-> {:value     (if (= answers-number "one") (first @value) @value)
                        :on-change handle-change
                        :options   options
                        :variant   "outlined"
                        :multiple? (not= answers-number "one")}
                       (= answers-number "one") (assoc :placeholder "Select correct answers"))]])))

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

(defn- get-mark-option
  [{:keys [value on-change parent-value text]}]
  (let [checked? (->> @parent-value (some #{value}) (boolean))]
    [[label {:class-name "mark-option-label"} text]
     [checkbox {:value     value
                :checked?  checked?
                :on-change on-change}]]))

(defn mark-options
  [{:keys [data]}]
  (let [options-list (map :value available-mark-options)
        list->map (fn [values-list]
                    (->> options-list
                         (map (fn [option-value]
                                [option-value (->> values-list (some #{option-value}) (boolean))]))
                         (into {})))
        map->list (fn [value]
                    (->> value
                         (filter second)
                         (map first)
                         (vec)))

        param-key :mark-options
        option-value (connect-data data [param-key] (get default-values param-key))
        handle-change (fn [{:keys [value checked?]}]
                        (reset! option-value (-> (list->map @option-value)
                                                 (assoc value checked?)
                                                 (map->list))))]
    [:div.option-group
     [label {:class-name "label"} "Options"]
     (into [:div.mark-options]
           (reduce (fn [result option]
                     (concat result (get-mark-option (merge option
                                                            {:on-change    handle-change
                                                             :parent-value option-value}))))
                   []
                   available-mark-options))]))

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
