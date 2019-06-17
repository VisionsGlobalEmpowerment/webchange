(ns webchange.dashboard.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.subs :as ds]
    [webchange.dashboard.side-menu.views :refer [side-menu]]
    [webchange.dashboard.classes.views :refer [classes-dashboard]]
    [webchange.dashboard.students.views :refer [students-dashboard]]
    [webchange.dashboard.views-app-bar :refer [app-bar]]
    [webchange.dashboard.views-common :refer [get-shift-styles]]
    [webchange.dashboard.views-drawer :refer [drawer]]
    [webchange.ui.theme :refer [with-mui-theme]]))

(def drawer-width 300)

(defn main-content
  [main-content]
  (case main-content
    :manage-classes [classes-dashboard]
    :manage-students [students-dashboard]
    [:div]))

(defn dashboard-page
  []
  (let [drawer-open (r/atom true)]
    (fn []
      (let [current-main-content @(re-frame/subscribe [::ds/current-main-content])]
        [with-mui-theme
         [:div.dashboard
          [app-bar
           {:title        "Dashboard"
            :on-open-menu #(reset! drawer-open true)
            :drawer-open? @drawer-open
            :drawer-width drawer-width}]
          [drawer {:open     @drawer-open
                   :on-close #(reset! drawer-open false)}
           [side-menu]]
          [:div {:style (get-shift-styles @drawer-open drawer-width)}
           [main-content current-main-content]]]]))))
