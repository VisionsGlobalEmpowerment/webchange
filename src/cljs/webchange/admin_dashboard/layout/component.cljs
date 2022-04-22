(ns webchange.admin-dashboard.layout.component
  (:require
    [reagent.core :as r]))

(defn component
  [{:keys [header]}]
  [:div.admin-dashboard-layout
   [:div.header
    header]
   (->> (r/current-component)
        (r/children)
        (into [:div.content]))])
