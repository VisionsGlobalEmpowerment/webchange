(ns webchange.parent-dashboard.layout.views
  (:require
    [reagent.core :as r]
    [webchange.parent-dashboard.layout.views-greeting :refer [greeting]]
    [webchange.parent-dashboard.layout.views-header :refer [header]]))

(defn layout
  [{:keys [actions title]
    :or   {actions []
           title   ""}}]
  [:div.parent-page-layout
   [header]
   [greeting]
   [:div.body
    [:div.sub-header
     [:h2 title]
     (into [:div.actions]
           actions)]
    (into [:div.content]
          (-> (r/current-component)
              (r/children)))]])
