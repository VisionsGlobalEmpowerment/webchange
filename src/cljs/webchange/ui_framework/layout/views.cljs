(ns webchange.ui-framework.layout.views
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.layout.navigation.views :refer [navigation-menu]]
    [webchange.ui-framework.layout.toolbar.views :refer [toolbar]]
    [webchange.ui-framework.layout.right-menu.views :refer [right-menu]]
    [webchange.views-modals :refer [modal-windows]]))

(defn layout
  [{:keys [actions show-navigation?]
    :or   {show-navigation? true}}]
  (let [this (r/current-component)]
    [:div.page-layout
    ;;  [:div.header
    ;;   [toolbar {:actions actions}]]
     [:div.body
      (when show-navigation?
        [navigation-menu])
      (into [:div.content]
            (r/children this))
      [right-menu {:actions actions}]]
     [modal-windows]]))
