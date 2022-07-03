(ns webchange.admin.pages.activities.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.activities.state :as state]
    [webchange.admin.widgets.activities-list.views :refer [activities-list]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]
    [webchange.utils.languages :refer [language-options]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [activities @(re-frame/subscribe [::state/activities])
          loading? @(re-frame/subscribe [::state/activities-loading?])
          handle-card-click #(re-frame/dispatch [::state/open-activity %])
          handle-edit-click #(re-frame/dispatch [::state/edit-activity %])
          current-language @(re-frame/subscribe [::state/current-language])
          handle-select-language #(re-frame/dispatch [::state/select-language %])]
      [page/single-page {:class-name "page--activities"
                         :header     {:title   "Activities"
                                      :icon    "games"
                                      :controls [[ui/select {:label     "Language"
                                                             :value     current-language
                                                             :options   language-options
                                                             :on-change handle-select-language}]]}}
       [page/main-content
        [activities-list {:data                   activities
                          :loading?               loading?
                          :on-activity-click      handle-card-click
                          :on-edit-activity-click handle-edit-click}]]])))
