(ns webchange.dashboard.classes.classes-menu.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.dashboard.classes.class-form.state :as class-form]
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

(re-frame/reg-event-fx
  ::open-class-profile
  (fn [{:keys [_]} [_ class-id]]
    {:redirect [:dashboard-class-profile :class-id class-id]}))

(re-frame/reg-event-fx
  ::add-class
  (fn [{:keys [_]} [_]]
    {:dispatch [::class-form/open-add-class-window]}))
