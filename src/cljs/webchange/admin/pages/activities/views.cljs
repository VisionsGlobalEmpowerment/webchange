(ns webchange.admin.pages.activities.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.activities.state :as state]
    [webchange.admin.widgets.activities-list.views :refer [activities-list]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.search.views :refer [search]]
    [webchange.ui.index :as ui]
    [webchange.utils.languages :refer [language-options]]))

(defn- activity-type-selector
  []
  (let [selected-type @(re-frame/subscribe [::state/selected-type])
        activities-counter @(re-frame/subscribe [::state/activities-counter])]
    [:div {:class-name "activity-type-selector"}
     [:div {:class-name (ui/get-class-name {"activity-type-selector-item"        true
                                            "activity-type-selector-item-active" (= selected-type :visible)})
            :on-click   #(re-frame/dispatch [::state/select-type :visible])}
      "Global library"
      [ui/chip {:counter (:visible activities-counter)}]]
     [:div.activity-type-spacer]
     [:div {:class-name (ui/get-class-name {"activity-type-selector-item"        true
                                            "activity-type-selector-item-active" (= selected-type :my)})
            :on-click   #(re-frame/dispatch [::state/select-type :my])}
      "My Activities"
      [ui/chip {:counter (:my activities-counter)}]]]))

(defn- search-bar
  []
  (let [value @(re-frame/subscribe [::state/search-string])
        handle-change #(re-frame/dispatch [::state/set-search-string %])]
    [search {:value      value
             :on-change  handle-change
             :class-name "activities-search-input"}]))

(defn- my-global-switcher
  []
  (let [show-my-global? @(re-frame/subscribe [::state/show-my-global?])
        handle-change #(re-frame/dispatch [::state/set-show-global (not show-my-global?)])]
    [ui/switch {:label      "My Global"
                :checked?   show-my-global?
                :on-change  handle-change
                :color      "yellow-1"
                :class-name "show-global-selector"}]))

(defn- assessments-switcher
  []
  (let [show-only-assessments? @(re-frame/subscribe [::state/show-only-assessments?])
        handle-change #(re-frame/dispatch [::state/set-show-only-assessments (not show-only-assessments?)])]
    [ui/switch {:label      "Only Assessments"
                :checked?   show-only-assessments?
                :on-change  handle-change
                :color      "yellow-1"
                :class-name "show-global-selector"}]))

(defn- language-selector
  []
  (let [current-language @(re-frame/subscribe [::state/current-language])
        handle-select-language #(re-frame/dispatch [::state/select-language %])]
    [ui/select {:label      "Language"
                :value      current-language
                :options    language-options
                :on-change  handle-select-language
                :class-name "language-selector"}]))

(defn- select-group
  []
  (let [groups @(re-frame/subscribe [::state/groups])
        loading? @(re-frame/subscribe [::state/activities-loading?])
        handle-card-click #(re-frame/dispatch [::state/select-group %])]
    [activities-list {:data groups
                      :loading? loading?
                      :on-card-click handle-card-click}]))

(defn- select-activity
  []
  (let [activities @(re-frame/subscribe [::state/activities])
        loading? @(re-frame/subscribe [::state/activities-loading?])
        handle-card-click #(re-frame/dispatch [::state/open-activity %])
        handle-edit-click #(re-frame/dispatch [::state/edit-activity %])
        handle-back-click #(re-frame/dispatch [::state/reset-group])]
    [activities-list {:data activities
                      :loading? loading?
                      :on-back-click handle-back-click
                      :on-card-click handle-card-click
                      :on-edit-click handle-edit-click}]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [group-selected? @(re-frame/subscribe [::state/selected-group])]
      [page/single-page {:class-name "page--activities"
                         :search     [search-bar]
                         :header     {:title      "Activities"
                                      :icon       "games"
                                      :icon-color "blue-2"
                                      :controls   [[activity-type-selector]
                                                   [my-global-switcher]
                                                   [assessments-switcher]
                                                   [language-selector]]}}
       (if group-selected?
         [select-activity]
         [select-group])])))
