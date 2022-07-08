(ns webchange.lesson-builder.blocks.menu.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.blocks.menu.state :as state]
    [webchange.lesson-builder.widgets.character-add.views :refer [character-add]]
    [webchange.lesson-builder.widgets.design-actions.views :refer [design-actions]]
    [webchange.lesson-builder.widgets.image-add.views :refer [image-add]]
    [webchange.lesson-builder.widgets.scene-layers.views :refer [scene-layers]]
    [webchange.lesson-builder.widgets.settings.views :refer [settings]]
    [webchange.lesson-builder.widgets.template-options.views :refer [template-options]]
    [webchange.ui.index :as ui]))

(def menu-items {:character-add    character-add
                 :design-actions   design-actions
                 :image-add        image-add
                 :scene-layers     scene-layers
                 :settings         settings
                 :template-options template-options})

(defn- menu-tabs-item
  [{:keys [active? id title]}]
  (let [handle-click #(re-frame/dispatch [::state/set-current-tab id])]
    [:div {:class-name (ui/get-class-name {"menu--tab"         true
                                           "menu--tab--active" active?})
           :on-click   handle-click}
     title]))

(defn- menu-tabs
  []
  (let [tabs @(re-frame/subscribe [::state/menu-tabs])
        {:keys [current-tab]} @(re-frame/subscribe [::state/current-state])]
    [:div {:class-name "menu--tabs"}
     (for [{:keys [id] :as tab-data} tabs]
       ^{:key id}
       [menu-tabs-item (assoc tab-data :active? (= id current-tab))])]))

(defn- menu-header
  []
  (let [show-back-button? @(re-frame/subscribe [::state/show-history-back?])
        handle-back-click #(re-frame/dispatch [::state/history-back])]
    (when show-back-button?
      [:div {:class-name "menu--header"}
       [ui/button {:icon     "arrow-left"
                   :color    "blue-1"
                   :on-click handle-back-click}]])))

(defn block-menu
  []
  (re-frame/dispatch [::state/init {:tab       :design
                                    :component :design-actions}])
  (fn [{:keys [class-name]}]
    (let [{:keys [current-component]} @(re-frame/subscribe [::state/current-state])
          body-component (get menu-items current-component :div)]
      [:div {:id         "block--menu"
             :class-name class-name}
       [menu-tabs]
       [:div {:class-name "menu--body"}
        [menu-header]
        [:div {:class-name "menu--content"}
         [body-component]]]])))
