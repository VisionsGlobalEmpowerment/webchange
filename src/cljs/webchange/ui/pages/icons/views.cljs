(ns webchange.ui.pages.icons.views
  (:require
    [webchange.ui.components.icon.navigation.index :as navigation-icons]
    [webchange.ui.index :as ui]
    [webchange.ui.pages.layout :refer [layout]]))

(defn- icons-list-item
  [{:keys [icon color]}]
  [:div.icons-list-item
   [:div.icon-wrapper
    [ui/navigation-icon {:icon  icon
                         :color color}]]
   [:div.name icon]])

(defn- icons-list
  [{:keys [color class-name]}]
  [:div {:class-name (ui/get-class-name {"icons-list" true
                                         class-name   (some? class-name)})}
   (for [icon (->> navigation-icons/data
                   (map first)
                   (sort))]
     ^{:key icon}
     [icons-list-item {:icon  icon
                       :color color}])])

(defn page
  []
  [:div#page--icons
   [layout {:title "Icons"}
    [:h2 "Navigation Icons"]
    [icons-list]
    [:h2 "Navigation Icons (Colored)"]
    [:div
     [icons-list {:color      "white"
                  :class-name "icons-list-colored"}]]]])
