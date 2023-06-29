(ns webchange.lesson-builder.tools.settings.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.settings.state :as state]
    [webchange.ui.index :as ui]
    [webchange.utils.languages :refer [language-options]]
    [webchange.utils.book-library :refer [age-options
                                          genre-options
                                          reading-level-options
                                          category-options
                                          tag-options]]))

(defn- menu-header
  [label]
  [:div.menu-header
   [ui/button {:icon     "caret-left"
               :color    "blue-1"
               :on-click #(re-frame/dispatch [::state/back])}]
   label])

(defn- activity-settings
  []
  [:div.settings-item
   [:div {:class-name "settings-item-header-wrapper"
          :on-click   #(re-frame/dispatch [::state/open-activity-settings])}
    [:div.settings-item-header
     "Activity Settings"]
    [ui/icon {:icon  "caret-right"
              :color "grey-4"}]]])

(defn- book-settings
  []
  [:div.settings-item
   [:div {:class-name "settings-item-header-wrapper"
          :on-click   #(re-frame/dispatch [::state/open-book-settings])}
    [:div.settings-item-header
     "Book Info"]
    [ui/icon {:icon  "caret-right"
              :color "grey-4"}]]])

(defn- create-preview-image
  []
  [:div.settings-item
   [:div {:class-name "settings-item-header-wrapper"
          :on-click   #(re-frame/dispatch [::state/open-create-preview-image])}
    [:div.settings-item-header
     "Create Preview Image"]
    [ui/icon {:icon  "caret-right"
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

(defn- assessment-settings
  []
  (let [is-assessment @(re-frame/subscribe [::state/is-assessment])]
    [:div.settings-item
     [:div.settings-item-header
      "Assessment Settings"]
     [:div.option-group
      [ui/input-label "Assessment?"]
      [ui/switch {:checked?  is-assessment
                  :on-change #(re-frame/dispatch [::state/set-is-assessment %])}]]]))

(defn- activity-settings-panel
  []
  (let [activity-name-value @(re-frame/subscribe [::state/activity-name])
        language-value @(re-frame/subscribe [::state/language])]
    [:div.activity-settings-panel
     [menu-header "Activity Settings"]
     [:div.activity-settings-fields
      [:div.activity-settings-field
       [ui/input-label "Activity Name"]
       [ui/input {:value     activity-name-value
                  :on-change #(re-frame/dispatch [::state/set-activity-name %])}]]
      [:div.activity-settings-field
       [ui/input-label "Language"]
       [ui/select {:value     language-value
                   :required? true
                   :options   language-options
                   :on-change #(re-frame/dispatch [::state/set-language %])}]]]]))

(defn- book-settings-panel
  []
  (let [activity-name-value @(re-frame/subscribe [::state/activity-name])
        language-value @(re-frame/subscribe [::state/language])
        ages-value @(re-frame/subscribe [::state/ages])
        genres-value @(re-frame/subscribe [::state/genres])
        reading-level-value @(re-frame/subscribe [::state/reading-level])
        categories-value @(re-frame/subscribe [::state/categories])
        tags-value @(re-frame/subscribe [::state/tags])
        about-value @(re-frame/subscribe [::state/about])
        short-description-value @(re-frame/subscribe [::state/short-description])]
    [:div.activity-settings-panel
     [menu-header "Book Info"]
     [:div.activity-settings-fields
      [:div.activity-settings-field
       [ui/input-label "Name"]
       [ui/input {:value     activity-name-value
                  :on-change #(re-frame/dispatch [::state/set-activity-name %])}]]
      [:div.activity-settings-field
       [ui/input-label "Language"]
       [ui/select {:value     language-value
                   :required? true
                   :options   language-options
                   :on-change #(re-frame/dispatch [::state/set-language %])}]]
      [:div.activity-settings-field
       [ui/input-label "Ages"]
       [ui/select {:value     (first ages-value)
                   :options   age-options
                                        ;:multiple? true
                   :on-change #(re-frame/dispatch [::state/set-ages [%]])}]]
      [:div.activity-settings-field
       [ui/input-label "Genres"]
       [ui/select {:value     (first genres-value)
                   :options   genre-options
                                        ;:multiple? true
                   :on-change #(re-frame/dispatch [::state/set-genres [%]])}]]
      [:div.activity-settings-field
       [ui/input-label "Reading Level"]
       [ui/select {:value     reading-level-value
                   :options   reading-level-options
                   :on-change #(re-frame/dispatch [::state/set-reading-level %])}]]
      [:div.activity-settings-field
       [ui/input-label "Categories"]
       [ui/select {:value     (first categories-value)
                   :options   category-options
                                        ;:multiple? true
                   :on-change #(re-frame/dispatch [::state/set-categories [%]])}]]
      [:div.activity-settings-field
       [ui/input-label "Tags"]
       [ui/select {:value     (first tags-value)
                   :options   tag-options
                                        ;:multiple? true
                   :on-change #(re-frame/dispatch [::state/set-tags [%]])}]]
      [:div.activity-settings-field
       [ui/input-label "About"]
       [ui/text-area {:value     about-value
                      :on-change #(re-frame/dispatch [::state/set-about %])}]]
      [:div.activity-settings-field
       [ui/input-label "Short Desrciption"]
       [ui/text-area {:value     short-description-value
                      :on-change #(re-frame/dispatch [::state/set-short-description %])}]]]
     [:div.settings-actions
      [ui/button {:on-click #(re-frame/dispatch [::state/cancel])
                  :color "blue-1"}
       "Cancel"]
      [ui/button {:on-click #(re-frame/dispatch [::state/apply])}
       "Save"]]]))

(defn- create-preview-panel
  []
  (let [preview-value @(re-frame/subscribe [::state/preview])
        saving? @(re-frame/subscribe [::state/saving?])
        flipbook? @(re-frame/subscribe [::state/flipbook?])
        handle-take-screenshot (if flipbook?
                                 #(re-frame/dispatch [::state/create-book-preview])
                                 #(re-frame/dispatch [::state/create-preview]))]
    [:<>
     [:div.create-preview-panel
      [menu-header "Create Preview"]
      [:div.create-preview-fields
       [ui/note {:text "Click button to create a snapshot of the current preview to show in the student navigation"}]
       [ui/image {:class-name (ui/get-class-name {"img-wrapper" true
                                                  "img-wrapper-book" flipbook?})
                  :src        preview-value}]
       [ui/button {:class-name "take-screenshot-button"
                   :color      "blue-1"
                   :on-click   handle-take-screenshot}
        "Take Screenshot"]]]
     [ui/button {:class-name "apply-button"
                 :shape      "rounded"
                 :loading?   saving?
                 :on-click   #(re-frame/dispatch [::state/apply])}
      "Apply"]]))


(defn- main-settings-panel
  []
  (let [saving? @(re-frame/subscribe [::state/saving?])
        flipbook? @(re-frame/subscribe [::state/flipbook?])]
    [:<>
     [:div.main-setting-panel
      (if flipbook?
        [book-settings]
        [activity-settings])
      [create-preview-image]
      [guide-settings]
      [animations-settings]
      [assessment-settings]]
     [ui/button {:class-name "apply-button"
                 :shape      "rounded"
                 :loading?   saving?
                 :on-click   #(re-frame/dispatch [::state/apply])}
      "Apply"]]))

(defn settings
  []
  (re-frame/dispatch [::state/init])
  (fn []
    (let [panel @(re-frame/subscribe [::state/panel])]
      [:div.widget--settings
       (case panel
         :main-settings [main-settings-panel]
         :activity-settings [activity-settings-panel]
         :book-settings [book-settings-panel]
         :create-preview [create-preview-panel])])))
