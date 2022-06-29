(ns webchange.admin.pages.class-profile.teachers-add.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.class-profile.state :as parent-state]))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [_]} [_]]
    {:dispatch [::parent-state/open-class-form]}))

(re-frame/reg-event-fx
  ::close-and-update
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::parent-state/load-class]
                  [::parent-state/open-class-form]]}))
