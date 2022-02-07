(ns webchange.student-dashboard-v2.toolbar.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]
    [webchange.state.warehouse :as warehouse]))

(re-frame/reg-event-fx
  ::open-page
  (fn [{:keys [_]} [_ page-id]]
    {:dispatch [::events/redirect :student-dashboard]}))

(re-frame/reg-event-fx
  ::login-as-parent
  (fn [{:keys [_]} [_]]
    {:dispatch [::warehouse/login-as-student-parent {}
                {:on-success [::login-as-parent-success]}]}))

(re-frame/reg-event-fx
  ::login-as-parent-success
  (fn [{:keys [db]} [_ user]]
    {:db       (update-in db [:user] merge user)
     :dispatch [::events/redirect :parent-dashboard]}))

(re-frame/reg-event-fx
  ::exit
  (fn [{:keys [db]} [_]]
    (let [current-school (-> db :user :school-id)]
      (if current-school
        {:dispatch [::events/redirect :student-login]}
        {:dispatch [::login-as-parent]}))))
