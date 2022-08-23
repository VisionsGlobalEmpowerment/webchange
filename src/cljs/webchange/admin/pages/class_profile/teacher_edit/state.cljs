(ns webchange.admin.pages.class-profile.teacher-edit.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.class-profile.state :refer [path-to-db] :as parent-state]
    [webchange.state.warehouse :as warehouse]))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [_]} [_]]
    {:dispatch [::parent-state/open-class-form]}))

(re-frame/reg-event-fx
  ::close-and-update
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::parent-state/load-class]
                  [::parent-state/open-class-form]]}))

(re-frame/reg-event-fx
  ::remove-from-class
  (fn [{:keys [_]} [_ teacher-id class-id]]
    {:dispatch [::warehouse/remove-teacher-from-class
                {:class-id   class-id
                 :teacher-id teacher-id}
                {:on-success [::close-and-update]}]}))
