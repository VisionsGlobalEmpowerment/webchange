(ns webchange.admin.pages.activities.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.activities.state :as state]
    [webchange.admin.widgets.activities-list.views :refer [activities-list]]
    [webchange.admin.widgets.page.views :as page]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [activities @(re-frame/subscribe [::state/activities])
          loading? @(re-frame/subscribe [::state/activities-loading?])
          handle-card-click #(re-frame/dispatch [::state/open-activity %])
          handle-edit-click #(re-frame/dispatch [::state/edit-activity %])]
      [page/page {:class-name "page--activities"}
       [page/header {:title "Activities"
                     :icon  "activity"}]
       [page/main-content
        [activities-list {:data                   activities
                          :loading?               loading?
                          :on-activity-click      handle-card-click
                          :on-edit-activity-click handle-edit-click}]]])))
