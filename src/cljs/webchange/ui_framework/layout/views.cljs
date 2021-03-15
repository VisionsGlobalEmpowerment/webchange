(ns webchange.ui-framework.layout.views
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.layout.toolbar.views :refer [toolbar]]
    [webchange.views-modals :refer [modal-windows]]))

(defn- menu-placeholder
  []
  [:div.menu-placeholder "Menu Placeholder"])

(defn layout
  []
  (let [this (r/current-component)]
    [:div.page-layout
     [:div.header
      [toolbar]]
     [:div.body
      [:div.side-menu
       [menu-placeholder]]
      (into [:div.content]
            (r/children this))]
     [modal-windows]]))
