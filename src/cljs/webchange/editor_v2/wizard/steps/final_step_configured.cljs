(ns webchange.editor-v2.wizard.steps.final-step-configured
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.wizard.state.activity :as state-activity]
    [webchange.editor-v2.wizard.state.course :as state-course]
    [webchange.editor-v2.wizard.steps.common :refer [progress-block]]))

(defn- get-course-data
  [data]
  {:name            (get-in data [:activity-data :course])
   :lang            (get-in data [:activity-data :language])
   :concept-list-id (get-in data [:skills :concept])
   :level           (get-in data [:skills :level])
   :subject         (get-in data [:skills :subject])})

(defn- get-activity-data
  [data]
  (merge {:template-id (get-in data [:template-id])}
         (get-in data [:template-data])))

(defn- save-activity
  [course-slug scene-slug activity-data]
  (js/Promise. (fn [resolve]
                 (re-frame/dispatch [::state-activity/create-activity-version course-slug scene-slug activity-data resolve]))))

(defn- save-data
  [data]
  (let [course-slug (:course-slug data)
        scene-slug (:scene-slug data)
        activity-data (get-activity-data data)
        callback (fn [{:keys [course-slug scene-slug]}]
                   (re-frame/dispatch [::state-course/redirect-to-editor course-slug scene-slug]))]
    (-> (save-activity course-slug scene-slug activity-data)
        (.then callback))))

(defn final-form
  [{:keys [data]}]
  (save-data @data)
  (fn []
    [progress-block {:title "Saving your data..."}]))

(def data
  {:header     "Finish"
   :sub-header "Activity creation completed"
   :component  final-form})
