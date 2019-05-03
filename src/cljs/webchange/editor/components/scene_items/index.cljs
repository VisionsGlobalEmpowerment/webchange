(ns webchange.editor.components.scene-items.index
  (:require
    [re-frame.core :as re-frame]
    [sodium.core :as na]
    [webchange.editor.components.scene-items.action-templates.index :refer [list-action-templates-panel]]
    [webchange.editor.components.scene-items.actions.index :refer [list-actions-panel]]
    [webchange.editor.components.scene-items.animations.index :refer [list-animations-panel]]
    [webchange.editor.components.scene-items.assets.index :refer [list-assets-panel]]
    [webchange.editor.components.scene-items.objects.index :refer [add-object-panel
                                                                   list-objects-panel]]
    [webchange.editor.components.scene-items.properties-rail.index :refer [properties-rail]]
    [webchange.editor.events :as events]
    [webchange.editor.subs :as es]))

(defn shown-form-panel
  []
  (let [show-form (re-frame/subscribe [::es/shown-form])]
    (case @show-form
      :add-object [add-object-panel]
      :list-objects [list-objects-panel]
      :list-actions [list-actions-panel]
      :list-asset-templates [list-assets-panel]
      :list-animation-templates [list-animations-panel]
      :list-action-templates [list-action-templates-panel]
      [:div])))

(defn scene-items
  []
  [:div
   [:div
    [na/button {:basic? true :content "Objects" :on-click #(re-frame/dispatch [::events/show-form :list-objects])}]
    [na/button {:basic? true :content "Actions" :on-click #(re-frame/dispatch [::events/show-form :list-actions])}]
    ]
   [:div
    [:label "Templates: "]
    [na/button {:basic? true :content "Assets" :on-click #(re-frame/dispatch [::events/show-form :list-asset-templates])}]
    [na/button {:basic? true :content "Animations" :on-click #(re-frame/dispatch [::events/show-form :list-animation-templates])}]
    #_[na/button {:basic? true :content "Actions" :on-click #(re-frame/dispatch [::events/show-form :list-action-templates])}]]
   [shown-form-panel]
   [properties-rail]])
