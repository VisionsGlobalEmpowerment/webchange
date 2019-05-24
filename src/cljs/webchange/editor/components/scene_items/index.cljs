(ns webchange.editor.components.scene-items.index
  (:require
    [re-frame.core :as re-frame]
    [soda-ash.core :refer [Button] :rename {Button button}]
    [webchange.editor.components.scene-items.actions.index :refer [list-actions-panel]]
    [webchange.editor.components.scene-items.animations.index :refer [list-animations-panel]]
    [webchange.editor.components.scene-items.assets.index :refer [list-assets-panel]]
    [webchange.editor.components.scene-items.objects.index :refer [add-object-panel
                                                                   list-objects-panel]]
    [webchange.editor.components.scene-items.properties-rail.index :refer [properties-rail]]
    [webchange.editor.events :as events]
    [webchange.editor.subs :as es]))

(def items {
            :add-object               {:label     "Add Object"
                                       :component add-object-panel}
            :list-objects             {:label     "Objects"
                                       :component list-objects-panel}
            :list-actions             {:label     "Actions"
                                       :component list-actions-panel}
            :list-asset-templates     {:label     "Assets"
                                       :component list-assets-panel}
            :list-animation-templates {:label     "Animations"
                                       :component list-animations-panel}})

(defn- shown-form-panel
  []
  (let [show-form (re-frame/subscribe [::es/shown-form])]
    (if (contains? items @show-form)
      [(-> items (@show-form) :component)]
      [:div])))

(defn- get-menu-item
  [{:keys [label key]}]
  ^{:key key} [button {:basic    true
                       :content  label
                       :on-click #(re-frame/dispatch [::events/show-form key])}])

(defn- map-items-to-list
  [items-map]
  (map #(merge (% items-map) {:key %}) (keys items-map)))

(defn scene-items
  []
  [:div
   (for [item (map-items-to-list items)]
     (get-menu-item item))
   [shown-form-panel]
   [properties-rail]])
