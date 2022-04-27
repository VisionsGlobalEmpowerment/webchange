(ns webchange.admin.widgets.layout.views
  (:require
    [reagent.core :as r]
    [webchange.admin.widgets.layout.navigation.views :refer [navigation]]))

(defn- top-bar
  []
  [:div.top-bar
   [navigation]])

(defn layout
  []
  [:div.layout
   [top-bar]
   [:div.content-wrapper
    (->> (r/current-component)
         (r/children)
         (into [:div.content]))]])
