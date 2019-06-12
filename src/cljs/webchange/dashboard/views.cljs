(ns webchange.dashboard.views
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.core :refer [create-mui-theme color]]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.subs :as ds]
    [webchange.dashboard.side-menu.views :refer [side-menu]]
    [webchange.dashboard.classes.views :refer [classes-dashboard]]
    [webchange.dashboard.students.views :refer [students-dashboard]]))

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
      (let [current-main-content @(re-frame/subscribe [::ds/current-main-content])
            view-port-class (str "view-port" (when @drawer-open " shrink"))]
        [ui/mui-theme-provider
         {:theme (create-mui-theme {})}
         [:div.dashboard
          [:div.header
           [ui/app-bar {}
            [ui/toolbar {}
             [ui/icon-button {:on-click #(reset! drawer-open (not @drawer-open))} [ic/menu]]
             [ui/typography {} "Dashboard"]]]]
          [ui/drawer {:variant "persistent" :open @drawer-open :classes {:paper "side-bar"}}

           [side-menu]]
          [:div {:class view-port-class}
           [main-content current-main-content]]
          ]]))))
