(ns webchange.parent-dashboard.layout.views-header)

(defn- logo
  []
  [:div.logo
   [:img {:src "/images/parent_dashboard/tabschool_logo.svg"}]])

(defn- toolbar-button
  [{:keys [on-click text]}]
  [:button {:on-click on-click}
   text])

(defn- toolbar
  []
  [:div.toolbar
   [toolbar-button {:text "Home"}]
   [toolbar-button {:text "Help"}]
   [toolbar-button {:text "Log Out"}]])

(defn header
  []
  [:div.header
   [logo]
   [toolbar]])
