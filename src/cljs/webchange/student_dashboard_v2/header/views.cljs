(ns webchange.student-dashboard-v2.header.views
  (:require
    [reagent.core :as r]))

(defn header
  []
  [:div.header-wrapper
   [:div.header-background]
   (into [:div.header]
         (-> (r/current-component)
             (r/children)))])
