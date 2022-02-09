(ns webchange.dashboard.classes.classes-list.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.dashboard.classes.state :as parent-state]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_]]
    {:dispatch [::parent-state/init]}))

(re-frame/reg-sub
  ::classes
  (fn []
    (re-frame/subscribe [::parent-state/classes]))
  (fn [classes]
    classes))
