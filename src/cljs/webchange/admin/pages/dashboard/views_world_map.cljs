(ns webchange.admin.pages.dashboard.views-world-map
  (:require
    [webchange.admin.pages.dashboard.world-map :as world-map-svg]
    [webchange.ui.index :as ui]))

(def country-names {"gt" "Guatemala"
                    "in" "India"
                    "ni" "Nicaragua"
                    "us" "USA"})

(defn- countries-list-item
  [{:keys [country]}]
  [:li.countries-list-item
   [ui/flag {:icon country}]
   (get country-names country)])

(defn- countries-list
  [{:keys [countries]}]
  [:ul.countries-list
   (for [country countries]
     ^{:key country}
     [countries-list-item {:country country}])])

(defn world-map
  [{:keys [countries title]}]
  [:div.component--world-map
   [:div.component--world-map--map-wrapper
    (when (some? title)
      [:h3.component--world-map--title title])
    world-map-svg/data]
   [countries-list {:countries countries}]])
