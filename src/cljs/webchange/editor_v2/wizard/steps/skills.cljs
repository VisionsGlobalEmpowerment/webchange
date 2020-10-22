(ns webchange.editor-v2.wizard.steps.skills
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.wizard.state.activity :as state-activity]
    [webchange.editor-v2.wizard.state.course :as state-course]
    [webchange.editor-v2.wizard.steps.common :as common :refer [progress-block select-control with-empty-item]]
    [webchange.editor-v2.wizard.validator :as validator :refer [connect-data]]))

(defn- get-styles
  []
  (common/get-styles))

(def validation-map {:level   [(fn [value] (when (nil? value) "Level is required"))]
                     :subject [(fn [value] (when (nil? value) "Subject is required"))]
                     :strands [(fn [value] (when (empty? value) "Strands are required"))]
                     :topics  [(fn [value] (when (empty? value) "Topics are required"))]
                     :skills  [(fn [value] (when (empty? value) "Skills are required"))]
                     :concept [(fn [value] (when (nil? value) "Concept is required"))]})

(defn form
  [{:keys [data-key parent-data validator]}]
  (r/with-let [_ (re-frame/dispatch [::state-activity/load-skills])
               _ (re-frame/dispatch [::state-course/load-datasets-library])

               data (connect-data parent-data data-key)

               handle-level-changed (fn [level-id] (swap! data assoc :level level-id))
               handle-subject-changed (fn [subject-id] (swap! data assoc :subject subject-id))
               handle-strands-changed (fn [strands-ids] (swap! data assoc :strands strands-ids))
               handle-topics-changed (fn [topics-ids] (swap! data assoc :topics topics-ids))
               handle-skills-changed (fn [skills-ids] (swap! data assoc :skills skills-ids))
               handle-concept-changed (fn [concept-id] (swap! data assoc :concept concept-id))

               {:keys [error-message destroy]} (validator/init data validation-map validator)
               styles (get-styles)]
    (let [skills-data @(re-frame/subscribe [::state-activity/skills])
          datasets @(re-frame/subscribe [::state-course/datasets-library])]
      (if (and (some? skills-data)
               (some? datasets))
        (let [{:keys [levels subjects strands topics skills]} skills-data

              current-level (get @data :level "")
              current-subject (get @data :subject "")
              current-strands (get @data :strands [])
              current-skills (get @data :skills [])
              current-topics (get @data :topics [])
              current-concept (get @data :concept "")

              levels-options (->> levels
                                  (map (fn [[key name]] {:value (clojure.core/name key)
                                                         :text  name})))
              subjects-options (->> subjects
                                    (map (fn [[key name]] {:value (clojure.core/name key)
                                                           :text  name})))
              strands-options (->> strands
                                   (map (fn [[key name]] {:value (clojure.core/name key)
                                                          :text  name})))
              topics-options (->> topics
                                  (filter (fn [[_ {:keys [strand]}]]
                                            (some #{strand} current-strands)))
                                  (map (fn [[key {:keys [name]}]] {:value (clojure.core/name key) :text name})))
              skills-options (->> skills
                                  (filter (fn [{:keys [topic]}]
                                            (some #{topic} current-topics)))
                                  (map (fn [{:keys [id name]}] {:value id :text name})))
              concepts-options (->> datasets
                                    (map (fn [{:keys [id name]}] {:value id
                                                                  :text  name})))]
          [ui/grid {:container   true
                    :justify     "center"
                    :spacing     24
                    :align-items "center"
                    :style       (:form styles)}

           [select-control {:label         "Level"
                            :description   "Which level of learner will the activity be suited for?"
                            :value         current-level
                            :options       levels-options
                            :multiple?     false
                            :on-change     handle-level-changed
                            :error-message [error-message {:field-name :level}]}]

           [select-control {:label         "Subject"
                            :description   "What subject area will the activity teach?"
                            :value         current-subject
                            :options       subjects-options
                            :multiple?     false
                            :on-change     handle-subject-changed
                            :error-message [error-message {:field-name :subject}]}]

           [select-control {:label         "Strands"
                            :description   "What specific strands of literacy will the activity teach?"
                            :value         current-strands
                            :options       strands-options
                            :multiple?     true
                            :on-change     handle-strands-changed
                            :error-message [error-message {:field-name :strands}]}]

           [select-control {:label         "Topics"
                            :description   "For each strand, which literacy topics will the activity teach?"
                            :value         current-topics
                            :options       topics-options
                            :multiple?     true
                            :on-change     handle-topics-changed
                            :error-message [error-message {:field-name :topics}]}]

           [select-control {:label         "Skills"
                            :description   "Please select the standards / competencies that your activity will teach. Be sure to select all that apply."
                            :value         current-skills
                            :options       skills-options
                            :multiple?     true
                            :on-change     handle-skills-changed
                            :error-message [error-message {:field-name :skills}]}]

           [select-control {:label         "Concept"
                            :description   "Exactly what skill(s) or concept(s) do you want your activity to help teach or practice?"
                            :value         current-concept
                            :options       concepts-options
                            :multiple?     false
                            :on-change     handle-concept-changed
                            :error-message [error-message {:field-name :concept}]}]])
        [progress-block]))
    (finally
      (destroy))))

(defn get-step
  [{:keys [data data-key validator]}]
  {:label      "Skills"
   :header     "Skills"
   :sub-header "Decide what you will teach"
   :content    [form {:data-key    data-key
                      :parent-data data
                      :validator   validator}]})
