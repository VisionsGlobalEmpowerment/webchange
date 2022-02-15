(ns webchange.ui-framework.layout.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.page-title.views :refer [page-title]]
    [webchange.ui-framework.layout.navigation.views :refer [navigation-menu]]
    [webchange.ui-framework.layout.right-menu.views :refer [right-menu]]
    [webchange.ui-framework.layout.state :as state]
    [webchange.views-modals :refer [modal-windows]]))

(defn layout
  [{:keys [actions content-title title-actions edit-menu-content on-edit-menu-back show-edit-menu? show-navigation? scene-data]
    :or   {show-navigation? true}}]
  (r/with-let [this (r/current-component)
               set-ref #(re-frame/dispatch [::state/init %])]
    [:div.page-layout
     [page-title {:scene-data scene-data}]
     [:div.body
      (when show-navigation?
        [navigation-menu {:class-name "left-side-menu"}])
      (into [:div {:class-name "content"
                   :ref        #(when (some? %) (set-ref %))}
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
