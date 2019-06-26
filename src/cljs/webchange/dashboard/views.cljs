(ns webchange.dashboard.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.subs :as ds]
    [webchange.dashboard.side-menu.views :refer [side-menu]]
    [webchange.dashboard.classes.views :refer [classes-dashboard]]
    [webchange.dashboard.classes.class-profile.views :refer [class-profile]]
    [webchange.dashboard.students.views :refer [students-dashboard student-profile]]
    [webchange.dashboard.views-app-bar :refer [app-bar]]
    [webchange.dashboard.views-common :refer [get-shift-styles]]
    [webchange.dashboard.views-drawer :refer [drawer]]
    [webchange.ui.theme :refer [with-mui-theme]]
    [webchange.dashboard.students.views-common :refer [student-modal]]))

(def app-bar-height 64)
(def drawer-width 300)

(defn main-content
  [main-content]
  (case main-content
    :manage-classes [classes-dashboard]
    :manage-students [students-dashboard]
    :student-profile [student-profile]
    :class-profile [class-profile]
    [:div]))

(defn dashboard-page
  []
  (let [drawer-open (r/atom true)]
    (fn []
      (let [current-main-content @(re-frame/subscribe [::ds/current-main-content])]
        [with-mui-theme
         [:div.dashboard
          [app-bar
           {:on-open-menu #(reset! drawer-open true)
            :drawer-open? @drawer-open
            :drawer-width drawer-width}]
          [drawer {:open     @drawer-open
                   :on-close #(reset! drawer-open false)}
           [side-menu]]
          [:div {:style (merge {:height (str "calc(100vh - " app-bar-height "px)")}
                               (get-shift-styles @drawer-open drawer-width))}
           [main-content current-main-content]
           [student-modal]
           ]]]))))
