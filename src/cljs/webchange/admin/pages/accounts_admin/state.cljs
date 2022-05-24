(ns webchange.admin.pages.accounts-admin.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.routes :as routes]))

(re-frame/reg-event-fx
  ::add-admin
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :add-account :type "admin"]}))
