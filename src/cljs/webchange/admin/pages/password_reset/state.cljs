(ns webchange.admin.pages.password-reset.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.routes :as routes]))

(re-frame/reg-event-fx
  ::open-accounts-list
  (fn [{:keys []} [_ result]]
    (print "::open-accounts-list" result)
    {:dispatch [::routes/redirect :accounts :account-type "live"]}))
