(ns webchange.ui-framework.layout.views
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.layout.navigation.views :refer [navigation-menu]]
    [webchange.ui-framework.layout.right-menu.views :refer [right-menu]]
    [webchange.views-modals :refer [modal-windows]]))

(defn layout
  [{:keys [actions content-title title-actions edit-menu-content on-edit-menu-back show-edit-menu? show-navigation? scene-data]
    :or   {show-navigation? true}}]
  (let [this (r/current-component)]
    [:div.page-layout
     [:div.body
      (when show-navigation?
        [navigation-menu {:class-name "left-side-menu"}])
      (into [:div.content
             (cond-> [:div.title
                      [:h1 content-title]]
                     (some? title-actions) (into title-actions))]
            (r/children this))
      (when show-navigation?
        [right-menu {:actions           actions
                     :edit-menu-content edit-menu-content
                     :show-edit-menu?   show-edit-menu?
                     :on-edit-menu-back on-edit-menu-back
                     :scene-data        scene-data
                     :class-name        "right-side-menu"}])]
     [modal-windows]]))
