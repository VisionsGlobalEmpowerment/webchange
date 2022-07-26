(ns webchange.lesson-builder.tools.script.dialog-item.character-movement.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.dialog-item.character-movement.state :as state]
    [webchange.lesson-builder.tools.script.dialog-item.wrapper.views :refer [item-wrapper]]
    [webchange.ui.index :as ui]))

(defn character-movement
  [{:keys [action-path]}]
  (let [name @(re-frame/subscribe [::state/name action-path])
        handle-remove-click #(re-frame/dispatch [::state/remove action-path])]
    [item-wrapper {:class-name "dialog-item--character-movement"
                   :actions    [{:icon     "trash"
                                 :title    "Delete phrase"
                                 :on-click handle-remove-click}]
                   :data       {:path action-path}}
     [ui/icon {:icon       "movement"
               :class-name "effect-general--icon"}]
     name]))