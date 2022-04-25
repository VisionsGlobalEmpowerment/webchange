(ns webchange.teacher.widgets.header.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.teacher.routes :as routes]))

(re-frame/reg-event-fx
  ::open-dashboard
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :dashboard]}))

(re-frame/reg-event-fx
  ::open-classes
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :classes]}))
