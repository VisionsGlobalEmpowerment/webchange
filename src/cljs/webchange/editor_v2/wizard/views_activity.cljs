(ns webchange.editor-v2.wizard.views-activity
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor-v2.wizard.common-styles :as styles]
    [webchange.editor-v2.wizard.views-activity-skills :as activity-skills]
    [webchange.editor-v2.wizard.activity-template.views :as activity-template]
    [webchange.editor-v2.wizard.state.activity :as state-activity]
    [webchange.editor-v2.wizard.validator :as validator]
    [webchange.ui.theme :refer [get-in-theme]]))

(def activity-skills-info activity-skills/skills)
(def template-info activity-template/template)

(def validation-map {:name        [(fn [value] (when (empty? value) "Name is required"))]
                     :skills      [(fn [value] (when (empty? value) "Skills are required"))]
                     :template-id [(fn [value] (when (nil? value) "Template is required"))]})

(defn- get-styles
  []
  (merge (styles/activity)
         {:card                 {:margin      "12px"
                                 :flex-shrink "0"
                                 :max-width   "1280px"}
          :template-description {:border           "solid 1px"
                                 :border-color     "rgba(0,0,0,0)"
                                 :background-color (get-in-theme [:palette :background :darken])
                                 :color            (get-in-theme [:palette :text :secondary])
                                 :padding          "12px 18px"
                                 :border-radius    "10px"}
          :actions              {:padding "24px"}
          :save-button          {:margin-left "auto"}}))

(defn- activity-template
  [{:keys [data templates on-change validator]}]
  (let [styles (get-styles)
        {:keys [error-message]} validator]
    (if (some? templates)
      [ui/form-control {:style (:control-container styles)}
       [ui/input-label "Template"]
       [ui/select {:value     (or (:template-id @data) "")
                   :on-change #(on-change (-> % .-target .-value))
                   :style     (:control styles)}
        (for [template templates]
          ^{:key (:id template)}
          [ui/menu-item {:value (:id template)} (:name template)])]
       [error-message {:field-name :template-id}]]
      [ui/circular-progress])))

(defn- activity-info
  [{:keys [data validator]}]
  (let [templates @(re-frame/subscribe [::state-activity/templates])
        current-template (->> templates (filter #(= (:template-id @data) (:id %))) first)
        handle-change-template (fn [template-id]
                                 (when (some? current-template)
                                   (doseq [[key] (:options current-template)]
                                     (swap! data dissoc key)))
                                 (swap! data assoc :template-id template-id))
        {:keys [error-message]} validator
        styles (get-styles)]
    [ui/grid {:container   true
              :justify     "center"
              :spacing     16
              :align-items "center"}
     [ui/grid {:item true :xs 12}
      [ui/text-field {:label     "Name"
                      :variant   "outlined"
                      :value     (get @data :name "")
                      :style     (:control styles)
                      :on-change #(swap! data assoc :name (-> % .-target .-value))}]
      [error-message {:field-name :name}]]
     [ui/grid {:item true :xs 12}
      [activity-skills-info data]
      [error-message {:field-name :skills}]]
     [ui/grid {:item true :xs 12}
      [activity-template {:data      data
                          :templates templates
                          :on-change handle-change-template
                          :validator validator}]]
     (when (some? current-template)
       [ui/grid {:item true :xs 12} [ui/divider]])
     (when (some? current-template)
       [ui/grid {:item true :xs 12}
        [ui/typography {:variant "body1"
                        :style   (:template-description styles)}
         (:description current-template)]])
     (when (some? current-template)
       [ui/grid {:item true :xs 12}
        [template-info {:template  current-template
                        :data      data
                        :validator validator}]])]))

(defn create-activity-panel
  [course-slug]
  (re-frame/dispatch [::state-activity/load-templates])
  (re-frame/dispatch [::state-activity/load-skills])
  (r/with-let [data (r/atom {:skills []})
               {:keys [valid?] :as validator} (validator/init data validation-map)]
    (let [handle-save (fn []
                        (when (valid?)
                          (re-frame/dispatch [::state-activity/create-activity course-slug @data])))
          styles (get-styles)]
      [layout {:title "Activity"
               :align "center"}
       [ui/card {:style (:card styles)}
        [ui/dialog-title
         "Create Activity"]
        [ui/card-content
         [ui/grid {:container   true
                   :justify     "center"
                   :spacing     24
                   :align-items "center"}
          [ui/grid {:item true :xs 12}
           [activity-info {:data      data
                           :validator validator}]]]]
        [ui/card-actions {:style (:actions styles)}
         [ui/button {:color    "secondary"
                     :style    (:save-button styles)
                     :on-click handle-save}
          "Save"]]]])))
