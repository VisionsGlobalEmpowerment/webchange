(ns webchange.parent-dashboard.layout.views
  (:require
    [reagent.core :as r]
    [webchange.parent-dashboard.layout.views-greeting :refer [greeting]]
    [webchange.parent-dashboard.layout.views-header :refer [header]]))

(defn layout
  []
  [:div.parent-page-layout
   [header]
   [greeting]
   (into [:div.body]
         (-> (r/current-component)
             (r/children)))])
