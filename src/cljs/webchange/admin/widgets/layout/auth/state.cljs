(ns webchange.admin.widgets.layout.auth.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.routes :as routes]))

(re-frame/reg-event-fx
  ::open-login-page
  (fn [_]
    {:dispatch [::routes/redirect :login]}))
