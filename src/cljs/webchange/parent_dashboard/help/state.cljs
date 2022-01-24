(ns webchange.parent-dashboard.help.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]))

(re-frame/reg-event-fx
  ::open-dashboard
  (fn [{:keys [_]} [_]]
    {:dispatch [::events/redirect :parent-dashboard]}))
