(ns webchange.admin.pages.create-activity.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]
    [webchange.admin.pages.create-activity.state :as state]
    [webchange.utils.languages :refer [language-options]]))

(defn- activity-categories
  []
  (let [categories @(re-frame/subscribe [::state/categories])
        selected @(re-frame/subscribe [::state/selected-category])]
    [:div.activity-categories 
     (for [category categories]
       [:div {:class-name (ui/get-class-name {"activity-category" true
                                              "selected" (= (:id category) (:id selected))})
              :on-click #(re-frame/dispatch [::state/select-category (:id category)])}
        [:div.activity-category-header
         [:div (:name category)]
         [ui/icon {:icon "caret-right"}]]
        [:div.activity-category-description
         (:description category)]])]))

(defn- template-list
  []
  (let [templates @(re-frame/subscribe [::state/templates])
        selected @(re-frame/subscribe [::state/selected-template])]
    (if (empty? templates)
      [:div.empty-list "Please choose an activity category on the left"]
      [:div.template-list
       (for [template templates]
         [:div.template-wrapper
          [:div {:class-name (ui/get-class-name {"template" true})
                 :on-click #(re-frame/dispatch [::state/select-template (:activity-id template)])}
           [:div {:class-name (ui/get-class-name {"preview-wrapper" true
                                                  "selected" (= (:activity-id template) (:activity-id selected))})}
            [ui/image {:class-name (ui/get-class-name {"preview" true})
                       :src (or (:preview template) "/images/admin/create_activity/preview_placeholder.png")}]]
           [:div.template-name
            (:name template)]
           [:div.template-description
            (:description template)]]
          (when (= (:activity-id template) (:activity-id selected))
            [ui/icon {:class-name "selected-icon"
                      :icon "check"}])])])))

(defn- select-page
  []
  (let [selected-template-name @(re-frame/subscribe [::state/selected-template-name])
        confirm-selection #(re-frame/dispatch [::state/confirm-selection])]
    [page/page {:class-name "page--create-activity"}
     [page/header {:title      "Create Activity"
                   :icon       "games"
                   :icon-color "blue-2"
                   :class-name "page--create-activity--header"}]
     [page/content {:title "Choose a Teamplate"
                    :icon "check"
                    :class-name "page--create-activity--content"}
      [template-list]]
     [page/side-bar {:title   "Activity Categories"
                     :icon    "info"
                     :class-name "page--create-activity--side-bar"}
      [activity-categories]]
     (when selected-template-name
       [page/footer
        [:div.footer-text [:strong "Activity Choice:"] selected-template-name]
        [ui/button {:class-name "footer-button-confirm"
                    :icon     "arrow-right"
                    :on-click confirm-selection}
         "Next"]])]))

(defn- activity-form
  []
  (let [activity-name-value @(re-frame/subscribe [::state/activity-name])
        language-value @(re-frame/subscribe [::state/language])
        handle-change-name #(re-frame/dispatch [::state/change-name %])
        handle-change-lang #(re-frame/dispatch [::state/change-lang %])]
    [:div.activity-form
     [ui/input {:label "Activity Name"
                :value activity-name-value
                :required? true
                :on-change handle-change-name}]
     [:div.note
      [ui/icon {:icon "info"}]
      [:div "Name Your Activity - This will be shown in your library and in the course table"]]
     [ui/select {:label "Select Language"
                 :value language-value
                 :required? true
                 :options language-options
                 :on-change handle-change-lang}]]))

(defn- template-info
  []
  (let [template @(re-frame/subscribe [::state/selected-template])]
    [:div {:class-name (ui/get-class-name {"template-info" true})}
     [:div {:class-name (ui/get-class-name {"preview-wrapper" true})}
      [ui/image {:class-name (ui/get-class-name {"preview" true})
                 :src (or (:preview template) "/images/admin/create_activity/preview_placeholder.png")}]]
     [:div.template-name
      (:name template)]
     [:div.template-description
      (:description template)]]))

(defn- form-page
  []
  (let [selected-category @(re-frame/subscribe [::state/selected-category])
        selected-template @(re-frame/subscribe [::state/selected-template])
        handle-build #(re-frame/dispatch [::state/build])
        handle-back #(re-frame/dispatch [::state/back])]
    [page/page {:class-name "page--create-activity"}
     [page/header {:title      "Create Activity"
                   :icon       "games"
                   :icon-color "blue-2"
                   :class-name "page--create-activity--header"}]
     [page/content {:title "Edit"
                    :icon "edit"
                    :class-name "page--create-activity--content"}
      [activity-form]]
     [page/side-bar {:title   (:name selected-category)
                     :icon    "info"
                     :class-name "page--create-activity--side-bar"}
      [template-info]]
     [page/footer
      [ui/button {:class-name "footer-button-back"
                  :color "blue-1"
                  :on-click handle-back}
       "Back"]
      [ui/button {:class-name "footer-button-confirm"
                  :on-click handle-build}
       "Save & Build"]]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init])
  (fn []
    (let [selected-template-name @(re-frame/subscribe [::state/selected-template-name])
          selection-confirmed @(re-frame/subscribe [::state/selection-confirmed])
          confirm-selection #(re-frame/dispatch [::state/confirm-selection])]
      (if selection-confirmed
        [form-page]
        [select-page]))))
