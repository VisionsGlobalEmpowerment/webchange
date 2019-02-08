(ns webchange.dashboard.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.events :as events]

    [sodium.core :as na]
    [soda-ash.core :as sa]))

(defn manage-classes
  []
  [:div "manage classes"])

(defn manage-students
  []
  [:div "manage students"])

(defn dashboard-panel
  []
  [:div "dashboard panel"])

(defn main-content
  []
  (let [ui-screen (re-frame/subscribe [::ds/current-main-content])]
    (case @ui-screen
      :manage-classes [manage-classes]
      :manage-students [manage-students]
      [dashboard-panel]
      )
    ))

(defn manage-panel
  []
  (fn []
    [na/segment {}
     [na/header {:as "h4" :content "Manage"}]
     [na/divider {:clearing? true}]
     [sa/ItemGroup {}
      [sa/Item {}
       [sa/ItemContent {}
        [:a {:on-click #(re-frame/dispatch [::de/show-manage-classes])} "Classes"]
        ]]
      [sa/Item {}
       [sa/ItemContent {}
        [:a {:on-click #(re-frame/dispatch [::de/show-manage-students])} "Students"]
        ]]
      ]]))

(defn dashboard-page
  []
  [sa/SidebarPushable {}
   [sa/Sidebar {:visible true}
    [na/segment {}
     [na/header {} [:div {} "WebChange"]]
     [na/divider {:clearing? true}]
     [manage-panel]
     ]]
   [sa/SidebarPusher {}
    [:div {:class-name "ui segment"}
     [na/header {:dividing? true} "Dashboard"]
     [na/grid {}
      [na/grid-column {:width 10}
       [main-content]]
      [na/grid-column {:width 4}
       ]]
     ]]])