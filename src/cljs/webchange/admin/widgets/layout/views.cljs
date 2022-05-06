(ns webchange.admin.widgets.layout.views
  (:require
    [reagent.core :as r]
    [webchange.admin.widgets.layout.auth.views :refer [auth]]
    [webchange.admin.widgets.layout.navigation.views :refer [navigation]]
    [webchange.admin.widgets.breadcrumbs.views :refer [breadcrumbs]]))

(defn- top-bar
  []
  [:div.top-bar
   [navigation]
   [auth]])

(defn layout
  []
  [:div.layout
   [top-bar]
   [breadcrumbs]
   [:div.content-wrapper
    (->> (r/current-component)
         (r/children)
         (into [:div.content]))]])
