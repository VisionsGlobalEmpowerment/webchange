(ns webchange.admin.pages.dashboard.views
  (:require
    [webchange.admin.pages.dashboard.views-world-map :refer [world-map]]
    [webchange.ui.index :refer [panel]]))

(defn- overview
  []
  [panel {:title      "Blue Brick School Overview"
          :icon       "info"
          :class-name "overview-panel"}
   "Overview Data"])

(defn- statistics
  []
  [panel {:title      "Statistics"
          :icon       "statistics"
          :class-name "statistics-panel"}
   "Statistics Data"])

(defn- countries
  []
  [panel {:title      "Countries"
          :class-name "countries-panel"}
   [world-map {:title     "Schools"
               :countries ["us" "ni" "in" "gt"]}]])

(defn page
  []
  (fn []
    (let []
      [:div.page--dashboard
       [overview]
       [statistics]
       [countries]])))
