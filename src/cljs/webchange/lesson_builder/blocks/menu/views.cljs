(ns webchange.lesson-builder.blocks.menu.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.blocks.menu.state :as state]
    [webchange.lesson-builder.blocks.menu.menu-tabs.views :refer [menu-tabs]]
    [webchange.lesson-builder.tools.voice-translate.views :refer [audio-manager]]
    [webchange.lesson-builder.tools.background-music.views :refer [background-music]]
    [webchange.lesson-builder.tools.character-add.views :refer [character-add]]
    [webchange.lesson-builder.widgets.design-actions.views :refer [design-actions]]
    [webchange.lesson-builder.tools.image-add.views :refer [image-add]]
    [webchange.lesson-builder.tools.object-form.views :refer [object-form]]
    [webchange.lesson-builder.tools.question-form.index :as question-form]
    [webchange.lesson-builder.tools.scene-layers.views :refer [scene-layers]]
    [webchange.lesson-builder.tools.settings.views :refer [settings]]
    [webchange.lesson-builder.tools.template-options.views :refer [template-options]]
    [webchange.lesson-builder.widgets.select-image.views :refer [choose-image-overlay]]
    [webchange.ui.index :as ui]))

(def menu-items {:default          design-actions
                 :audio-manager    audio-manager
                 :background-music background-music
                 :character-add    character-add
                 :design-actions   design-actions
                 :image-add        image-add
                 :object-form      object-form
                 :scene-layers     scene-layers
                 :settings         settings
                 :template-options template-options
                 :choose-image     choose-image-overlay
                 :question-form    (:menu question-form/data)})

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
              (let [body-component (get menu-items component-id :div)]
                [:div {:class-name (ui/get-class-name {"menu--content"        true
                                                       "menu--content-hidden" hidden?})}
                 [body-component]]))]]]))}))
