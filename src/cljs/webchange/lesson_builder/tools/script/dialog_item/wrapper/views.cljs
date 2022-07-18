(ns webchange.lesson-builder.tools.script.dialog-item.wrapper.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]))

(defn item-wrapper
  []
  [:div {:class-name "component--item-wrapper"}
   [ui/icon {:icon       "move"
             :class-name "item-wrapper--move-icon"}]
   (->> (r/current-component)
        (r/children)
        (into [:div.item-wrapper--content]))])
