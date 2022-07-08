(ns webchange.lesson-builder.widgets.design-actions.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.blocks.menu.state :as menu]))

(re-frame/reg-event-fx
  ::open-add-image-menu
  (fn []
    {:dispatch [::menu/set-current-component :image-add]}))

(re-frame/reg-event-fx
  ::open-add-character-menu
  (fn []
    {:dispatch [::menu/set-current-component :character-add]}))
