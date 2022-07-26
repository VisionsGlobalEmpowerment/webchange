(ns webchange.lesson-builder.tools.effects-add.character-movements.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.components.draggable.views :refer [draggable draggable-list]]
    [webchange.lesson-builder.tools.effects-add.character-movements.state :as state]
    [webchange.ui.index :as ui]))

(defn character-movements
  []
  (let [current-character @(re-frame/subscribe [::state/current-character])
        character-options @(re-frame/subscribe [::state/character-options])
        handle-character-changed #(re-frame/dispatch [::state/set-current-character %])

        current-action @(re-frame/subscribe [::state/current-action])
        action-options @(re-frame/subscribe [::state/action-options])
        handle-action-changed #(re-frame/dispatch [::state/set-current-action %])

        current-target @(re-frame/subscribe [::state/current-target])
        target-options @(re-frame/subscribe [::state/target-options])
        handle-target-changed #(re-frame/dispatch [::state/set-current-target %])

        add-action-data @(re-frame/subscribe [::state/add-action-data])]
    [:div.character-emotions
     [ui/select {:label       "Character"
                 :value       current-character
                 :options     character-options
                 :on-change   handle-character-changed
                 :placeholder "Select Character"}]
     (when (some? current-character)
       [ui/select {:label       "Action"
                   :value       current-action
                   :options     action-options
                   :on-change   handle-action-changed
                   :placeholder "Select Action"}])
     (when (some? current-action)
       [ui/select {:label       "Target"
                   :value       current-target
                   :options     target-options
                   :on-change   handle-target-changed
                   :placeholder "Select Target"}])
     (when (some? add-action-data)
       [draggable {:text   (:title add-action-data)
                   :action "add-movement"
                   :data   (:data add-action-data)}])]))
