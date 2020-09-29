(ns webchange.editor-v2.wizard.views-activity
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor-v2.wizard.state.activity :as state-activity]
    [webchange.editor-v2.wizard.views-activity-characters :refer [characters-option]]
    [webchange.editor-v2.wizard.views-activity-lookup :refer [lookup-option]]
    [webchange.editor-v2.wizard.views-activity-pages :refer [pages-option]]
    [webchange.editor-v2.wizard.views-activity-string :refer [string-option]]
    [webchange.editor-v2.wizard.validator :as validator]
    [webchange.ui.theme :refer [get-in-theme]]))

(def validation-map {:name        [(fn [value] (when (empty? value) "Name is required"))]
                     :skills      [(fn [value] (when (empty? value) "Skills are required"))]
                     :template-id [(fn [value] (when (nil? value) "Template is required"))]})

(defn- get-styles
  []
  {:card                 {:margin      "12px"
                          :flex-shrink "0"
                          :max-width   "1280px"}
   :control-container    {:margin-top "-8px"}
   :control              {:min-width "500px"}
   :skills-list          {:padding 0}
   :skills-list-item     {:padding-left 0
                          :white-space  "normal"}
   :template-description {:border           "solid 1px"
                          :border-color     "rgba(0,0,0,0)"
                          :background-color (get-in-theme [:palette :background :darken])
                          :color            (get-in-theme [:palette :text :secondary])
                          :padding          "12px 18px"
                          :border-radius    "10px"}
   :actions              {:padding "24px"}
   :save-button          {:margin-left "auto"}})

(defn- render-selected
  [selected]
  (let [skills (->> @(re-frame/subscribe [::state-activity/skills])
                    :skills
                    (map (juxt :id identity))
                    (into {}))
        values (->> selected
                    vals
                    (map #(get skills %)))
        styles (get-styles)]
    [ui/list {:style (:skills-list styles)}
     (for [value values]
       [ui/list-item {:key   (:id value)
                      :style (:skills-list-item styles)}
        (:name value)])]))

(def react-render-selected (r/reactify-component render-selected))

(defn- option-info
  [{:keys [option] :as props}]
  (case (:type option)
    "characters" [characters-option props]
    "lookup" [lookup-option props]
    "pages" [pages-option props]
    "string" [string-option props]
    nil))

(defn template-info
  [{:keys [template data validator]}]
  [ui/grid {:container   true
            :justify     "space-between"
            :spacing     24
            :align-items "center"}
   (for [[key option] (:options template)]
     ^{:key key}
     [ui/grid {:item true :xs 12}
      [option-info {:key       key
                    :option    option
                    :data      data
                    :validator validator}]])])

(defn activity-skill-info
  [data]
  (let [skills (-> @(re-frame/subscribe [::state-activity/skills]) :skills)
        styles (get-styles)]
    [ui/form-control {:style (:control-container styles)}
     [ui/input-label "Skills"]
     [ui/select {:multiple     true
                 :render-value #(r/create-element react-render-selected %)
                 :value        (:skills @data)
                 :on-change    #(swap! data assoc :skills (->> % .-target .-value))
                 :style        (:control styles)}
      (for [skill skills]
        ^{:key (:id skill)}
        [ui/menu-item {:value (:id skill)} (:name skill)])]]))

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
      [activity-skill-info data]
      [error-message {:field-name :skills}]]
     [ui/grid {:item true :xs 12}
      [ui/form-control {:style (:control-container styles)}
       [ui/input-label "Template"]
       [ui/select {:value     (or (:template-id @data) "")
                   :on-change #(handle-change-template (-> % .-target .-value))
                   :style     (:control styles)}
        (for [template templates]
          ^{:key (:id template)}
          [ui/menu-item {:value (:id template)} (:name template)])]
       [error-message {:field-name :template-id}]]]
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
