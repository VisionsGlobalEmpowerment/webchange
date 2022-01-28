(ns webchange.parent-dashboard.layout.views-header
  (:require
    [goog.string :as gstring]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.parent-dashboard.layout.state :as state]
    [webchange.routes :refer [location]]))

(defn- logo
  []
  [:div.logo
   [:img {:src "/images/parent_dashboard/tabschool_logo.svg"}]])

(defn- toolbar-button
  [{:keys [on-click]}]
  (into [:button {:on-click on-click}]
        (-> (r/current-component)
            (r/children))))

(defn- toolbar
  []
  (let [open-home #(re-frame/dispatch [::state/open-home-page])
        open-help #(re-frame/dispatch [::state/open-help-page])
        log-out #(location :logout)]
    [:div.toolbar
     [toolbar-button {:on-click open-home} "Home"]
     [toolbar-button {:on-click open-help} "Help"]
     [toolbar-button {:on-click log-out} "Log" (gstring/unescapeEntities "&nbsp;") "Out"]]))

(defn header
  []
  [:div.header
   [logo]
   [toolbar]])
