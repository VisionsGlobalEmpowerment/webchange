(ns webchange.admin.pages.activities.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.activities.state :as state]
    [webchange.admin.widgets.activities-list.views :refer [activities-list]]
    [webchange.admin.widgets.page.views :as page]
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
  [ui/input {:icon        "search"
             :class-name  "activities-search-input"
             :placeholder "search"}
   ])

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [activities @(re-frame/subscribe [::state/activities])
          loading? @(re-frame/subscribe [::state/activities-loading?])
          handle-card-click #(re-frame/dispatch [::state/open-activity %])
          handle-edit-click #(re-frame/dispatch [::state/edit-activity %])
          current-language @(re-frame/subscribe [::state/current-language])
          handle-select-language #(re-frame/dispatch [::state/select-language %])

          show-my-global? @(re-frame/subscribe [::state/show-my-global?])]
      [page/single-page {:class-name "page--activities"
                         :search     [search-bar]
                         :header     {:title      "Activities"
                                      :icon       "games"
                                      :icon-color "blue-2"
                                      :controls   [[activity-type-selector]
                                                   [ui/switch {:class-name "show-global-selector"
                                                               :label      "Show My Global Activities"
                                                               :checked?   show-my-global?
                                                               :on-change  #(re-frame/dispatch [::state/set-show-global (not show-my-global?)])}]
                                                   [ui/select {:label     "Language"
                                                               :value     current-language
                                                               :options   language-options
                                                               :on-change handle-select-language}]]}}
       [activities-list {:data                   activities
                         :loading?               loading?
                         :on-activity-click      handle-card-click
                         :on-edit-activity-click handle-edit-click}]])))
