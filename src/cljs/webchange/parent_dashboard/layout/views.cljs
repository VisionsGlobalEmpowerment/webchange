(ns webchange.parent-dashboard.layout.views
  (:require
    [reagent.core :as r]
    [webchange.page-title.views :refer [page-title]]
    [webchange.parent-dashboard.layout.views-greeting :refer [greeting]]
    [webchange.parent-dashboard.layout.views-header :refer [header]]))

(defn layout
  [{:keys [actions title]
    :or   {actions []
           title   ""}}]
  [:div.parent-page-layout
   [page-title {:title (str "Parent Dashboard | " title)}]
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
