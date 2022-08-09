(ns webchange.lesson-builder.widgets.activity-actions.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.blocks.menu.state :as menu]
    [webchange.lesson-builder.blocks.state :as layout-state]))

(re-frame/reg-sub
  ::menu-items
  (constantly [{:id   :image-add
                :text "Add Image"
                :icon "plus"}
               {:id   :character-add
                :text "Add Character"
                :icon "plus"}
               {:id   :background-image
                :text "Background"
                :icon "plus"}
               {:id   :background-music
                :text "Background Music"
                :icon "plus"}]))

(re-frame/reg-event-fx
  ::handle-item-click
  (fn [{:keys [_]} [_ component-name]]
    {:dispatch (cond
                 (= component-name :background-image) [::layout-state/set-state :change-background-image]
                 :default [::menu/open-component component-name])}))
