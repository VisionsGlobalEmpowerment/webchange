(ns webchange.editor-v2.activity-form.generic.components.add-character.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.character-selector.views :refer [skeleton-selector skin-selector]]
    [webchange.editor-v2.activity-form.generic.components.add-character.state :as state]
    [webchange.editor-v2.components.character-form.views :as animation-form]
    [webchange.ui-framework.components.index :refer [button dialog label select-image]]))

(defn open-add-character-window
  []
  (re-frame/dispatch [::state/open]))

(defn- add-character-form
  []
  (let [handle-change #(re-frame/dispatch [::state/set-current-character %])
        current-character-name @(re-frame/subscribe [::state/current-character-name])]
    [animation-form/form {:only-characters?      true
                          :character             current-character-name
                          :on-change             handle-change
                          :show-character-label? true
                          :class-name            "add-character-form"}]))

(defn add-character-window
  []
  (let [open? @(re-frame/subscribe [::state/open?])
        handle-add #(re-frame/dispatch [::state/add-character])
        handle-close #(re-frame/dispatch [::state/close])]
    (when open?
      [dialog {:open?    open?
               :on-close handle-close
               :title    "Add Character"
               :actions  [[button {:on-click handle-add
                                   :size     "big"}
                           "Add"]
                          [button {:on-click handle-close
                                   :color    "default"
                                   :variant  "outlined"
                                   :size     "big"}
                           "Cancel"]]}
       [add-character-form]])))
