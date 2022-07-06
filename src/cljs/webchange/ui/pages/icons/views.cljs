(ns webchange.ui.pages.icons.views
  (:require
    [webchange.ui.components.icon.navigation.index :as navigation-icons]
    [webchange.ui.components.icon.social.index :as social-icons]
    [webchange.ui.components.icon.system.index :as system-icons]
    [webchange.ui.index :as ui]
    [webchange.ui.pages.layout :refer [layout]]))

(defn- icons-list-item
  [{:keys [icon color component]}]
  [:div.icons-list-item
   [:div.icon-wrapper
    [component {:icon  icon
                :color color}]]
   [:div.name icon]])

(defn- icons-list
  [{:keys [class-name color component data]}]
  [:div {:class-name (ui/get-class-name {"icons-list" true
                                         class-name   (some? class-name)})}
   (for [icon (->> data
                   (map first)
                   (sort))]
     ^{:key icon}
     [icons-list-item {:icon      icon
                       :component component
                       :color     color}])])

(defn page
  []
  [:div#page--icons
   [layout {:title "Icons"}
    [:h2 "Navigation Icons"]
    [icons-list {:data      navigation-icons/data
                 :component ui/navigation-icon}]
    [:h2 "Navigation Icons (Colored)"]
    [:div
     [icons-list {:data       navigation-icons/data
                  :component  ui/navigation-icon
                  :color      "white"
                  :class-name "icons-list-colored"}]]
    [:h2 "System Icons"]
    [icons-list {:data      system-icons/data
                 :component ui/system-icon}]
    [:h2 "System Icons (Colored)"]
    [:div
     [icons-list {:data       system-icons/data
                  :component  ui/system-icon
                  :color      "white"
                  :class-name "icons-list-colored"}]]

    [:h2 "Social Icons"]
    [icons-list {:data      social-icons/data
                 :component ui/social-icon}]]])
