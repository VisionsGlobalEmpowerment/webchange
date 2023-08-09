(ns webchange.lesson-builder.layout.menu.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.layout.index :refer [tools-data]]
    [webchange.lesson-builder.layout.menu.state :as state]
    [webchange.lesson-builder.layout.menu.menu-tabs.views :refer [menu-tabs]]
    [webchange.ui.index :as ui]))

(defn- menu-header
  []
  (let [show-back-button? @(re-frame/subscribe [::state/show-history-back?])
        undo-active? @(re-frame/subscribe [::state/undo-active?])
        handle-back-click #(re-frame/dispatch [::state/history-back])
        handle-undo-click #(re-frame/dispatch [::state/undo])]
    [:div {:class-name "menu--header"}
     (when show-back-button?
       [ui/button {:icon     "arrow-left"
                   :color    "blue-1"
                   :on-click handle-back-click}])
     [ui/button {:icon     "rewind-backward"
                 :class-name "button-undo"
                 :color    "blue-2"
                 :disabled? (not undo-active?)
                 :on-click handle-undo-click}]]))

(defn block-menu
  []
  (r/create-class
   {:component-did-mount
    (fn [this]
      (re-frame/dispatch [::state/init (r/props this)]))

    :reagent-render
    (fn [{:keys [class-name tabs-disabled?]}]
      (let [open-components @(re-frame/subscribe [::state/open-components])]
        [:div {:class-name (ui/get-class-name {"block--menu" true
                                               class-name    (some? class-name)})}
         [menu-tabs {:disabled? tabs-disabled?}]
         [:div {:class-name "menu--body-wrapper"}
          [:div {:class-name "menu--body"}
           [menu-header]
           (for [{:keys [component-id hidden? uid]} open-components]
             ^{:key uid}
             [:div {:class-name (ui/get-class-name {"menu--content"        true
                                                    "menu--content-hidden" hidden?})}
              [(get-in tools-data [component-id :menu] :div)]])]]]))}))
