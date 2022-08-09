(ns webchange.lesson-builder.blocks.menu.menu-tabs.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.blocks.menu.menu-tabs.state :as state]
    [webchange.ui.index :as ui]))

(defn- menu-tabs-item
  [{:keys [active? disabled? id title]}]
  (let [handle-click #(when-not disabled? (re-frame/dispatch [::state/set-current-tab id]))]
    [:div {:class-name (ui/get-class-name {"menu--tab"           true
                                           "menu--tab--active"   active?
                                           "menu--tab--disabled" disabled?})
           :on-click   handle-click}
     title]))

(defn menu-tabs
  [{:keys [disabled?] :or {disabled? false}}]
  (let [tabs @(re-frame/subscribe [::state/menu-tabs])
        current-tab @(re-frame/subscribe [::state/current-tab])]
    [:div {:class-name "menu--tabs"}
     (for [{:keys [id] :as tab-data} tabs]
       ^{:key id}
       [menu-tabs-item (merge tab-data
                              {:active?   (= id current-tab)
                               :disabled? disabled?})])]))
