(ns webchange.editor-v2.activity-form.generic.components.add-character.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.common.character-selector.views :refer [skeleton-selector skin-selector]]
    [webchange.editor-v2.activity-form.generic.components.add-character.state :as state]
    [webchange.ui-framework.components.index :refer [button dialog label select-image]]))

(defn open-add-character-window
  []
  (re-frame/dispatch [::state/open]))

(defn- add-character-form
  []
  (let [current-skeleton @(re-frame/subscribe [::state/current-skeleton])
        handle-skeleton-changed #(re-frame/dispatch [::state/set-current-skeleton %])
        current-skin @(re-frame/subscribe [::state/current-skin])
        handle-skin-changed #(re-frame/dispatch [::state/set-current-skin %])]
    [:div.add-character-form
     [:div.select-group
      [label {:class-name "select-label"}
       "Select Animation:"]
      [skeleton-selector {:value      current-skeleton
                          :on-change  handle-skeleton-changed
                          :class-name "select-control"}]]
     [:div.select-group
      [label {:class-name "select-label"}
       "Select Skin:"]
      [skin-selector {:value      current-skin
                      :skeleton   current-skeleton
                      :on-change  handle-skin-changed
                      :disabled?  (nil? current-skeleton)
                      :class-name "select-control"}]]]))

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
                          ;; [button {:on-click handle-close
                          ;;          :color    "default"
                          ;;          :variant  "outlined"
                          ;;          :size     "big"}
                          ;;  "Cancel"]
                          ]}
       [add-character-form]])))
