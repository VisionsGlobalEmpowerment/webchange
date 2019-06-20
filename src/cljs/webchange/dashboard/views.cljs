(ns webchange.dashboard.views
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.subs :as ds]
    [webchange.dashboard.side-menu.views :refer [side-menu]]
    [webchange.dashboard.classes.views :refer [classes-dashboard]]
    [webchange.dashboard.students.views :refer [students-dashboard]]
    [webchange.dashboard.views-drawer :refer [drawer]]
    [webchange.ui.theme :refer [with-mui-theme]]))

(defn main-content
  [main-content]
  (case main-content
    :manage-classes [classes-dashboard]
    :manage-students [students-dashboard]
    [:div]))

(defn dashboard-page
  []
  (let [drawer-open (r/atom false)]
    (fn []
      (let [current-main-content @(re-frame/subscribe [::ds/current-main-content])
            view-port-class (str "view-port" (when @drawer-open " shrink"))]
        [with-mui-theme
         [:div.dashboard
          [ui/app-bar {:position "static"}
           [ui/toolbar
            [ui/icon-button {:color      "inherit"
                             :aria-label "Menu"
                             :on-click   #(reset! drawer-open true)}
             [ic/menu]]
            [ui/typography {:color   "inherit"
                            :variant "h6"}
             "Dashboard"]]]
          [drawer {:open @drawer-open
                   :on-close #(reset! drawer-open false)}
           [side-menu]]
          [:div {:class view-port-class}
           [main-content current-main-content]]]]))))
