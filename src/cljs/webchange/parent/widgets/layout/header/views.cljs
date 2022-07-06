(ns webchange.parent.widgets.layout.header.views
  (:require
    [webchange.ui.index :as ui]))

(defn- navigation-item
  [{:keys [text]}]
  [ui/button {:class-name "header--navigation-item"
              :color      "transparent"}
   text])

(defn- navigation
  []
  (let [items [{:text "Home"}
               {:text "Help"}
               {:text "Log Out"}]]
    [:div {:class-name "header--navigation"}
     (for [[idx item-data] (map-indexed vector items)]
       ^{:key idx}
       [navigation-item item-data])]))

(defn header
  []
  [:div.parent--layout--header
   [ui/logo-with-name {:class-name "header--logo"}]
   [navigation]])
