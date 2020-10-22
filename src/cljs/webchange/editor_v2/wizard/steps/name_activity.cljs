(ns webchange.editor-v2.wizard.steps.name-activity
  (:require
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.wizard.validator :refer [connect-data]]
    [webchange.editor-v2.wizard.validator :as validator :refer [connect-data]]))

(defn- get-styles
  []
  {:form              {:padding-bottom "16px"}
   :text-input        {:margin-top "16px"}
   :control-container {:margin-top "8px"}
   :input-description {:margin-bottom "8px"}})

(def validation-map {:language [(fn [value] (when (nil? value) "Language is required"))]
                     :name [(fn [value] (when (nil? value) "Activity name is required"))]
                     :course [(fn [value] (when (nil? value) "Course name is required"))]})

(defn form
  [{:keys [parent-data validator]}]
  (r/with-let [data (connect-data parent-data :activity-name)
               {:keys [error-message destroy]} (validator/init data validation-map validator)
               styles (get-styles)]
    [ui/grid {:container   true
              :justify     "center"
              :spacing     24
              :align-items "center"
              :style       (:form styles)}
     [ui/grid {:item true :xs 12}
      [ui/text-field {:label      "Language"
                      :variant    "outlined"
                      :full-width true
                      :value      (get @data :language "")
                      :on-change  #(swap! data assoc :language (-> % .-target .-value))}]
      [error-message {:field-name :language}]]
     [ui/grid {:item true :xs 12}
      [ui/typography {:variant "body1"
                      :style   (:input-description styles)}
       "What would you like to name this activity? Don't worry, you can always change this later."]
      [ui/text-field {:label      "Activity Name"
                      :variant    "outlined"
                      :full-width true
                      :value      (get @data :name "")
                      :on-change  #(swap! data assoc :name (-> % .-target .-value))}]
      [error-message {:field-name :name}]]
     [ui/grid {:item true :xs 12}
      [ui/typography {:variant "body1"
                      :style   (:input-description styles)}
       "Inside which course should this activity be included?"]
      [ui/text-field {:label      "Course Name"
                      :variant    "outlined"
                      :full-width true
                      :value      (get @data :course "")
                      :on-change  #(swap! data assoc :course (-> % .-target .-value))}]
      [error-message {:field-name :course}]]]
    (finally
      (destroy))))

(defn get-step
  [{:keys [data validator]}]
  {:label      "Name Activity"
   :header     "Name Your Activity & Course"
   :sub-header "Create new activity"
   :content    [form {:parent-data data
                      :validator   validator}]})
