(ns webchange.lesson-builder.components.draggable.views
  (:require
    [reagent.core :as r]
    [webchange.utils.drag-and-drop :as drag-and-drop]
    [webchange.ui.index :as ui]))

(defn draggable
  [{:keys [action actions data text]}]
  [drag-and-drop/draggable {:class-name "component--draggable"
                            :data       (assoc data :action action)}
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
  [{:keys [class-name]}]
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name (ui/get-class-name {"component--draggable-list" true
                                                    class-name                  (some? class-name)})}])))
