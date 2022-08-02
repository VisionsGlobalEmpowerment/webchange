(ns webchange.lesson-builder.tools.scene-layers.views
  (:require
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.scene-layers.state :as state]
    [webchange.ui.index :as ui]))

(defn scene-layers
  []
  (re-frame/dispatch [::state/init])
  (fn []
    (let [layers @(re-frame/subscribe [::state/layers])]
      [:div.widget--scene-layers
       (for [{:keys [alias object-type visible] :as layer} layers]
         [:div.scene-layer
          [:div.scene-layer-name
           [:div.scene-layer-icon
            [ui/icon {:icon (case object-type
                              :uploaded-image "images"
                              :added-character "character"
                              "images")
                      :color "grey-3"}]]
           (:alias layer)]
          [:div.scene-layer-actions
           [ui/button {:icon "edit"
                       :color "grey-3"
                       :on-click #(re-frame/dispatch [::state/edit-object (:name layer)])}]
           [ui/button {:icon (if visible "visibility-on" "visibility-off")
                       :color "grey-3"
                       :on-click #(re-frame/dispatch [::state/toggle-visibility (:name layer)])}]
           [ui/button {:icon "trash"
                       :color "grey-3"
                       :on-click #(re-frame/dispatch [::state/remove-object (:name layer)])}]]])])))
