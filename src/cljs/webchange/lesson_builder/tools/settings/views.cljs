(ns webchange.lesson-builder.tools.settings.views
  (:require
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.settings.state :as state]
    [webchange.ui.index :as ui]
    [webchange.utils.languages :refer [language-options]]))

(defn- menu-header
  [label]
  [:div.menu-header
   [ui/button {:icon "caret-left"
               :color "blue-1"
               :on-click #(re-frame/dispatch [::state/back])}]
   label])

(defn- activity-settings
  []
  [:div.settings-item
   [:div {:class-name "settings-item-header-wrapper"
          :on-click #(re-frame/dispatch [::state/open-activity-settings])}
    [:div.settings-item-header
     "Activity Settings"]
    [ui/icon {:icon "caret-right"
              :color "grey-4"}]]])

(defn- create-preview-image
  []
  [:div.settings-item
   [:div {:class-name "settings-item-header-wrapper"
          :on-click #(re-frame/dispatch [::state/open-create-preview-image])}
    [:div.settings-item-header
     "Create Preview Image"]
    [ui/icon {:icon "caret-right"
              :color "grey-4"}]]])

(defn- guide-settings
  []
  (let [show-guide @(re-frame/subscribe [::state/show-guide])
        character @(re-frame/subscribe [::state/character])]
    [:div.settings-item
     [:div.settings-item-header
      "Guide Settings"]
     [:div.option-group
      [ui/input-label "Guide Enabled?"]
      [ui/switch {:checked?  show-guide
                  :on-change #(re-frame/dispatch [::state/set-show-guide %])}]]
     [:div.option-group
      [ui/input-label "Guide type"]
      [ui/select {:value     character
                  :on-change #(re-frame/dispatch [::state/set-character %])
                  :options   [{:text  "Vaca"
                               :value "vaca"}
                              {:text  "Vera"
                               :value "vera"}
                              {:text  "Mari"
                               :value "mari"}
                              {:text  "Lion"
                               :value "lion"}]}]]]))

(defn- animations-settings
  []
  (let [animations-on @(re-frame/subscribe [::state/animations-on])]
    [:div.settings-item
     [:div.settings-item-header
      "Animations Settings"]
     [:div.option-group
      [ui/input-label "Animations On?"]
      [ui/switch {:checked?  animations-on
                  :on-change #(re-frame/dispatch [::state/set-animations-on %])}]]]))

(defn- activity-settings-panel
  []
  (let [activity-name-value @(re-frame/subscribe [::state/activity-name])
        language-value @(re-frame/subscribe [::state/language])]
    [:div.activity-settings-panel
     [menu-header "Activity Settings"]
     [:div.activity-settings-fields
      [:div.activity-settings-field
       [ui/input-label "Activity Name"]
       [ui/input {:value activity-name-value
                  :on-change #(re-frame/dispatch [::state/set-activity-name])}]]
      [:div.activity-settings-field
       [ui/input-label "Language"]
       [ui/select {:value language-value
                   :required? true
                   :options language-options
                   :on-change #(re-frame/dispatch [::state/set-languag])}]]]]))

(defn- create-preview-panel
  []
  (let [preview-value @(re-frame/subscribe [::state/preview])]
    [:div.create-preview-panel
     [menu-header "Create Preview"]
     [:div.create-preview-fields
      [ui/note {:text "Click button to create a snapshot of the current preview to show in the student navigation"}]
      [ui/image {:class-name "img-wrapper"
                 :src preview-value}]
      [ui/button {:class-name "take-screenshot-button"
                  :color "blue-1"
                  :on-click #(re-frame/dispatch [::state/create-preview])}
       "Take Screenshot"]]]))


(defn- main-settings-panel
  []
  [:<>
   [:div.main-setting-panel
    [:div.menu-header]
    [activity-settings]
    [create-preview-image]
    [guide-settings]
    [animations-settings]]
   [ui/button {:class-name "apply-button"
               :on-click #(re-frame/dispatch [::state/apply])}
    "Apply"]])

(defn settings
  []
  (re-frame/dispatch [::state/init])
  (fn []
    (let [panel @(re-frame/subscribe [::state/panel])]
      [:div.widget--settings
       (case panel
         :main-settings [main-settings-panel]
         :activity-settings [activity-settings-panel]
         :create-preview [create-preview-panel])])))
