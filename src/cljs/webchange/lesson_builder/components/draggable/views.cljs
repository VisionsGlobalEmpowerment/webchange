(ns webchange.lesson-builder.components.draggable.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]))

(defn draggable
  [{:keys [:text]}]
  [:div {:class-name "component--draggable"
         :draggable  true}
   [ui/icon {:icon       "dnd"
             :class-name "draggable--icon"}]
   [:span.draggable--name text]])

(defn draggable-list
  []
  (->> (r/current-component)
       (r/children)
       (into [:div.component--draggable-list])))
