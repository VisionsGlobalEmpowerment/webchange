(ns webchange.dashboard.classes.classes-list.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.dashboard.classes.class-form.state :as class-form]
    [webchange.dashboard.classes.remove-form.state :as remove-form]
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
  ::add
  (fn [{:keys [_]} [_ class-id]]
    {:dispatch [::class-form/open-add-class-window]}))

(re-frame/reg-event-fx
  ::edit
  (fn [{:keys [_]} [_ class-id]]
    {:dispatch [::class-form/open-edit-class-window class-id]}))

(re-frame/reg-event-fx
  ::remove
  (fn [{:keys [_]} [_ class-id]]
    {:dispatch [::remove-form/open-remove-class-window class-id]}))

(re-frame/reg-event-fx
  ::open-profile
  (fn [{:keys [_]} [_ class-id]]
    {:redirect [:dashboard-class-profile :class-id class-id]}))

(re-frame/reg-event-fx
  ::open-students
  (fn [{:keys [_]} [_ class-id]]
    {:redirect [:dashboard-students :class-id class-id]}))
