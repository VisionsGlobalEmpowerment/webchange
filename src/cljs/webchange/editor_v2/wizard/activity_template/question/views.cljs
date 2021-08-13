(ns webchange.editor-v2.wizard.activity-template.question.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.wizard.activity-template.question.views-preview :refer [question-preview]]
    [webchange.editor-v2.wizard.validator :refer [connect-data]]
    [webchange.question.get-question-data :refer [available-mark-options default-options]]
    [webchange.ui-framework.components.index :refer [checkbox label select input]]))

(def default-values
  {:question-type  "multiple-choice-image"
   :task-type      "text-image"
   :layout         "vertical"
   :option-label   "audio-text"
   :options-number 2
   :answers-number "one"
   :mark-options   ["thumbs-up" "thumbs-down"]})

(defn- answers-number-control
  [{:keys [value on-change]}]
  (let [options [{:text  "One"
                  :value "one"}
                 {:text  "Many"
                  :value "many"}
                 {:text  "Any"
                  :value "any"}]]
    [:div.option-group
     [label {:class-name "label"} "Correct answers number"]
     [select {:value     value
              :on-change on-change
              :options   options
              :variant   "outlined"}]]))

(defn- correct-answer-control
  [{:keys [value answers-number mark-options options-number question-type on-change]}]
  (let [handle-change #(on-change (if (= answers-number "one") [%] %))
        options (if (= question-type "thumbs-up-n-down")
                  (->> available-mark-options
                       (filter (fn [{:keys [value]}]
                                 (some #{value} mark-options))))
                  (->> default-options
                       (take options-number)
                       (map-indexed vector)
                       (map (fn [[number {:keys [value]}]]
                              {:text  (str "Option " (inc number))
                               :value value}))))
        show? (not= answers-number "any")]
    (when show?
      [:div.option-group
       [label {:class-name "label"} (str "Correct answer" (if (= answers-number "one") "s" ""))]
       [select (cond-> {:value     (if (= answers-number "one") (first value) value)
                        :on-change handle-change
                        :options   options
                        :variant   "outlined"
                        :multiple? (not= answers-number "one")}
                       (= answers-number "one") (assoc :placeholder "Select correct answers"))]])))

(defn- layout-control
  [{:keys [value task-type on-change]}]
  (let [show? (= task-type "text-image")
        options [{:text  "Horizontal"
                  :value "horizontal"}
                 {:text  "Vertical"
                  :value "vertical"}]]
    (when show?
      [:div.option-group
       [label {:class-name "label"} "Layout"]
       [select {:value     value
                :on-change on-change
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
  [{:keys [value question-type on-change]}]
  (let [options-list (map :value available-mark-options)
        list->map (fn [values-list]
                    (->> options-list
                         (map (fn [option-value] [option-value (->> values-list (some #{option-value}) (boolean))]))
                         (into {})))
        map->list (fn [value]
                    (->> value
                         (filter second)
                         (map first)
                         (vec)))
        handle-change (fn [option]
                        (on-change (-> (list->map value)
                                       (assoc (:value option) (:checked? option))
                                       (map->list))))
        show? (= question-type "thumbs-up-n-down")]
    (when show?
      [:div.option-group
       [label {:class-name "label"} "Options"]
       (into [:div.mark-options]
             (reduce (fn [result option]
                       (concat result (get-mark-option (merge option
                                                              {:on-change    handle-change
                                                               :parent-value value}))))
                     []
                     available-mark-options))])))

(defn- option-label-control
  [{:keys [value question-type on-change]}]
  (r/with-let [options [{:text  "Audio only"
                         :value "audio"}
                        {:text  "Audio + text"
                         :value "audio-text"}
                        {:text  "Empty"
                         :value "none"}]]
    (let [show? (= question-type "multiple-choice-image")]
      (when show?
        [:div.option-group
         [label {:class-name "label"} "Option label"]
         [select {:value     value
                  :on-change on-change
                  :options   options
                  :variant   "outlined"}]]))))

(defn- options-number-control
  [{:keys [value question-type on-change]}]
  (r/with-let [options (->> (range 2 5)
                            (map (fn [number]
                                   {:text  number
                                    :value number})))]
    (let [show? (not= question-type "thumbs-up-n-down")]
      (when show?
        [:div.option-group
         [label {:class-name "label"} "Options number"]
         [select {:value     value
                  :on-change on-change
                  :options   options
                  :type      "int"
                  :variant   "outlined"}]]))))

(defn- task-type-control
  [{:keys [value on-change]}]
  (r/with-let [options [{:text  "Text with image"
                         :value "text-image"}
                        {:text  "Text only"
                         :value "text"}]]
    [:div.option-group
     [label {:class-name "label"} "Task"]
     [select {:value     value
              :on-change on-change
              :options   options
              :variant   "outlined"}]]))

(defn- question-params-form
  [{:keys [data]}]
  (r/with-let [answers-number (connect-data data [:answers-number] (:answers-number default-values))
               correct-answer (connect-data data [:correct-answers] [])
               layout (connect-data data [:layout] (:layout default-values))
               mark-options (connect-data data [:mark-options] (:mark-options default-values))
               option-label (connect-data data [:option-label] (get default-values :option-label))
               options-number (connect-data data [:options-number] (:options-number default-values))
               task-type (connect-data data [:task-type] (get default-values :task-type))

               handle-answers-number-changed #(do (reset! answers-number %)
                                                  (reset! correct-answer []))
               handle-correct-answer-changed #(reset! correct-answer %)
               handle-layout-changed #(reset! layout %)
               handle-mark-options-changed #(reset! mark-options %)
               handle-option-label-changed #(reset! option-label %)
               handle-options-number-changed (fn [new-value]
                                               (let [old-value @options-number]
                                                 (reset! options-number new-value)
                                                 (when (< new-value old-value)
                                                   (reset! correct-answer []))))
               handle-task-type-changed #(reset! task-type %)]
    (let [question-type (:question-type @data)]
      [:div.options-groups
       [:div
        [task-type-control {:value     @task-type
                            :on-change handle-task-type-changed}]
        [layout-control {:value     @layout
                         :task-type @task-type
                         :on-change handle-layout-changed}]
        [option-label-control {:value         @option-label
                               :question-type question-type
                               :on-change     handle-option-label-changed}]]
       [:div
        [mark-options-control {:value         @mark-options
                               :question-type question-type
                               :on-change     handle-mark-options-changed}]
        [options-number-control {:value         @options-number
                                 :question-type question-type
                                 :on-change     handle-options-number-changed}]
        [answers-number-control {:value     @answers-number
                                 :on-change handle-answers-number-changed}]
        [correct-answer-control {:value          @correct-answer
                                 :answers-number @answers-number
                                 :mark-options   @mark-options
                                 :options-number @options-number
                                 :question-type  question-type
                                 :on-change      handle-correct-answer-changed}]]])))

(defn- question-type-control
  [{:keys [data]}]
  (r/with-let [param-key :question-type
               value (connect-data data [param-key] (get default-values param-key))
               handle-change #(reset! value %)
               options [{:text  "Multiple choice image"
                         :value "multiple-choice-image"}
                        {:text  "Multiple choice text"
                         :value "multiple-choice-text"}
                        {:text  "Thumbs up & thumbs down"
                         :value "thumbs-up-n-down"}]]
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
             :variant     "outlined"}]
     [label {:class-name "explanation-label"}
      "Name your question so you can find and drag it into the correct place in the script editor."]]))

(defn question-option
  [{:keys [key option data validator]}]
  (r/with-let [option-data (connect-data data [key] {})]
    [:div.question-option
     [question-alias-control {:data option-data}]
     [question-type-control {:data option-data}]
     [:hr]
     [question-params-form {:data option-data}]
     [label {:class-name "explanation-label"}
      "After you hit save, use the script editor boxes to fill in your actual question and answers."]
     [:hr]
     [question-preview @option-data]]))
