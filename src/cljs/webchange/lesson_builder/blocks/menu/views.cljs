(ns webchange.lesson-builder.blocks.menu.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.blocks.menu.state :as state]
    [webchange.lesson-builder.tools.voice-translate.views :refer [audio-manager]]
    [webchange.lesson-builder.tools.background-music.views :refer [background-music]]
    [webchange.lesson-builder.tools.character-add.views :refer [character-add]]
    [webchange.lesson-builder.widgets.design-actions.views :refer [design-actions]]
    [webchange.lesson-builder.tools.image-add.views :refer [image-add]]
    [webchange.lesson-builder.tools.object-form.views :refer [object-form]]
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
                 :choose-image     choose-image-overlay})

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
    (let [{:keys [components]} @(re-frame/subscribe [::state/current-state])
          n (count components)]
      [:div {:class-name (ui/get-class-name {"block--menu" true
                                             class-name    (some? class-name)})}
       [menu-tabs]
       [:div {:class-name "menu--body-wrapper"}
        [:div {:class-name "menu--body"}
         [menu-header]
         (for [[idx current-component] (map-indexed vector components)]
           ^{:key current-component}
           (let [body-component (get menu-items current-component :div)
                 hidden? (not= idx (dec n))]
             [:div {:class-name (ui/get-class-name {"menu--content"        true
                                                    "menu--content-hidden" hidden?})}
              [body-component]]))]]])))
