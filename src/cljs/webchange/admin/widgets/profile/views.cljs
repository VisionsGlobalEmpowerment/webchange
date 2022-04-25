(ns webchange.admin.widgets.profile.views
  (:require
    [reagent.core :as r]))

(defn profile
  []
  [:div.widget-profile
   (->> (r/current-component)
        (r/children)
        (into [:div.main-content]))])
