(ns webchange.lesson-builder.widgets.select-stage.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state-flipbook :as flipbook-state]))

(re-frame/reg-sub
  ::has-prev-stage?
  :<- [::flipbook-state/current-stage]
  (fn [current-stage]
    (> current-stage 0)))

(re-frame/reg-event-fx
  ::open-next-stage
  (fn [{:keys [_]} [_]]
    {:dispatch [::flipbook-state/show-next-stage]}))

(re-frame/reg-event-fx
  ::open-prev-stage
  (fn [{:keys [_]} [_]]
    {:dispatch [::flipbook-state/show-prev-stage]}))
