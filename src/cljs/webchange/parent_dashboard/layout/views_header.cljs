(ns webchange.parent-dashboard.layout.views-header
  (:require
    [re-frame.core :as re-frame]
    [webchange.parent-dashboard.layout.state :as state]))

(defn- logo
  []
  [:div.logo
   [:img {:src "/images/parent_dashboard/tabschool_logo.svg"}]])

(defn- toolbar-button
  [{:keys [on-click text]}]
  [:button {:on-click on-click}
   text])

(defn- toolbar
  []
  (let [open-home #(re-frame/dispatch [::state/open-home-page])
        open-help #(re-frame/dispatch [::state/open-help-page])
        log-out #(re-frame/dispatch [::state/log-out])]
    [:div.toolbar
     [toolbar-button {:text "Home" :on-click open-home}]
     [toolbar-button {:text "Help" :on-click open-help}]
     [toolbar-button {:text "Log Out" :on-click log-out}]]))

(defn header
  []
  [:div.header
   [logo]
   [toolbar]])
