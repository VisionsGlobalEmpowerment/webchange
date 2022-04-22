(ns webchange.admin-dashboard.button.component
  (:require
    [reagent.core :as r]))

(defn component
  [{:keys [on-click]}]
  (->> (r/current-component)
       (r/children)
       (into [:button {:on-click   on-click
                       :class-name "admin-dashboard-button"}])))
