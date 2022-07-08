(ns webchange.admin.widgets.layout.views
  (:require
    [reagent.core :as r]
    [webchange.admin.widgets.breadcrumbs.views :refer [breadcrumbs]]
    [webchange.admin.widgets.layout.auth.views :refer [auth]]
    [webchange.admin.widgets.navigation.views :refer [navigation]]
    [webchange.ui.index :as ui]))

(defn- top-bar
  []
  [:div.top-bar
   [navigation]
   [auth]])

(defn layout
  [{:keys [no-padding?]}]
  [:div.layout
   [top-bar]
   [breadcrumbs]
   [:div {:class-name (ui/get-class-name {"content-wrapper"             true
                                          "content-wrapper--no-padding" no-padding?})}
    (->> (r/current-component)
         (r/children)
         (into [:div.content]))]])
