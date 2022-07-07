(ns webchange.parent.widgets.layout.header.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.parent.routes :as routes]
    [webchange.login.check-current-user :as current-user]
    [webchange.state.warehouse :as warehouse]))

(re-frame/reg-event-fx
  ::open-home-page
  (fn [{:keys []} [_]]
    {:dispatch [::routes/redirect :students]}))

(re-frame/reg-event-fx
  ::open-faq-page
  (fn [{:keys []} [_]]
    {:dispatch [::routes/redirect :faq]}))

(re-frame/reg-event-fx
  ::logout
  (fn [_]
    {:dispatch [::warehouse/logout {:on-success [::logout-success]}]}))

(re-frame/reg-event-fx
  ::logout-success
  (fn [_]
    {::current-user/redirect-to-login true}))
