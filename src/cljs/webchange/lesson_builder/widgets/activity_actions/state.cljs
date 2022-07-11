(ns webchange.lesson-builder.widgets.activity-actions.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.blocks.menu.state :as menu]))

(re-frame/reg-event-fx
  ::handle-item-click
  (fn [{:keys [_]} [_ component-name]]
    {:dispatch [::menu/set-current-component component-name]}))