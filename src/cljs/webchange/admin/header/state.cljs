(ns webchange.admin.header.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.routes :as routes]))

(re-frame/reg-event-fx
  ::open-dashboard
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :home]}))

(re-frame/reg-event-fx
  ::open-schools
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :schools]}))

(re-frame/reg-event-fx
  ::open-school-profile
  (fn [{:keys [_]} [_ school-id]]
    {:dispatch [::routes/redirect :school-profile :school-id school-id]}))
