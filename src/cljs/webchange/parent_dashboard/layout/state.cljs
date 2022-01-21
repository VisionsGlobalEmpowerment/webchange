(ns webchange.parent-dashboard.layout.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]))

(re-frame/reg-event-fx
  ::open-home-page
  (fn [{:keys [_]} [_]]
    {:dispatch [::events/redirect :parent-dashboard]}))

(re-frame/reg-event-fx
  ::open-help-page
  (fn [{:keys [_]} [_]]
    {:dispatch [::events/redirect :parent-help]}))

(re-frame/reg-event-fx
  ::log-out
  (fn [{:keys [_]} [_]]
    (print ::log-out)
    {}))
