(ns webchange.lesson-builder.components.draggable.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]))

(defn draggable
  [{:keys [actions text]}]
  [:div {:class-name "component--draggable"
         :draggable  true}
   [ui/icon {:icon       "dnd"
             :class-name "draggable--icon"}]
   [:span.draggable--name text]
   (when (some? actions)
     [:div.draggable--actions
      (for [[idx action-data] (map-indexed vector actions)]
        ^{:key idx}
        [ui/button (merge {:color      "grey-3"
                           :class-name "draggable--action"}
                          action-data)])])])

(defn draggable-list
  []
  (->> (r/current-component)
       (r/children)
       (into [:div.component--draggable-list])))
