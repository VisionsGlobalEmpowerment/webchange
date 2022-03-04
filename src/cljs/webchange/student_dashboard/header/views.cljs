(ns webchange.student-dashboard.header.views
  (:require
    [reagent.core :as r]))

(defn header
  []
  [:div.header-wrapper
   [:div.header-background]
   (into [:div.header]
         (-> (r/current-component)
             (r/children)))])
