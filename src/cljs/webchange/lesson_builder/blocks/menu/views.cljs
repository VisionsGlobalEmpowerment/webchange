(ns webchange.lesson-builder.blocks.menu.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.blocks.menu.state :as state]
    [webchange.lesson-builder.blocks.menu.menu-tabs.views :refer [menu-tabs]]
    [webchange.lesson-builder.tools.background-music.views :refer [background-music]]
    [webchange.lesson-builder.tools.character-add.views :refer [character-add]]
    [webchange.lesson-builder.widgets.design-actions.views :refer [design-actions]]
    [webchange.lesson-builder.tools.image-add.views :refer [image-add]]
    [webchange.lesson-builder.tools.object-form.views :refer [object-form]]
    [webchange.lesson-builder.tools.question-form.index :as question-form]
    [webchange.lesson-builder.tools.scene-layers.views :refer [scene-layers]]
    [webchange.lesson-builder.tools.settings.views :refer [settings]]
    [webchange.lesson-builder.tools.template-options.index :as template-options]
    [webchange.lesson-builder.tools.voice-translate.index :as voice-translate]
    [webchange.lesson-builder.widgets.select-image.views :refer [choose-image-overlay]]
    [webchange.ui.index :as ui]))

(defn- add-menu-items
  [items-map key value]
  (if (map? value)
    (merge items-map value)
    (assoc items-map key value)))

(def menu-items (-> {:default          design-actions
                     :background-music background-music
                     :character-add    character-add
                     :design-actions   design-actions
                     :image-add        image-add
                     :object-form      object-form
                     :scene-layers     scene-layers
                     :settings         settings
                     :choose-image     choose-image-overlay}
                    (add-menu-items :question-form question-form/menu)
                    (add-menu-items :template-options template-options/menu)
                    (add-menu-items :voice-translate voice-translate/menu)))

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
  (r/create-class
    {:component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset (r/props this)]))

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
               [(get menu-items component-id :div)]])]]]))}))
