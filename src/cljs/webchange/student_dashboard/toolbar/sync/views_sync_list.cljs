(ns webchange.student-dashboard.toolbar.sync.views-sync-list
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.student-dashboard.toolbar.sync.state.course-resources :as course-resources]
    [webchange.student-dashboard.toolbar.sync.state.sync-list :as sync-list]
    [webchange.student-dashboard.toolbar.sync.views-resources-list :refer [resources-list]]))

(defn sync-list-modal
  []
  (let [open? @(re-frame/subscribe [::sync-list/open?])
        loading? @(re-frame/subscribe [::course-resources/loading?])
        handle-close #(re-frame/dispatch [::sync-list/close])]
    [ui/dialog
     {:open       open?
      :on-close   handle-close
      :full-width true
      :max-width  "sm"}
     [ui/dialog-title
      "Select Resources"]
     [ui/dialog-content
      [ui/dialog-content-text
       "Select levels/scenes to be available offline."]
      [ui/divider {:style {:margin "15px 0px"}}]
      (if-not loading?
        [resources-list]
        [:div
         {:style {:display         "flex"
                  :min-height      "128px"
                  :align-items     "center"
                  :justify-content "center"}}
         [ui/circular-progress]])]
     [ui/dialog-actions
      [ui/button
       {:variant  "contained"
        :on-click handle-close}
       "Close"]]]))
