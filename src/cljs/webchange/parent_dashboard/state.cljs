(ns webchange.parent-dashboard.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:parent-dashboard])
       (vec)))

(re-frame/reg-sub
  ::students
  (fn [db]
    (get-in db [:parent :students])))

(re-frame/reg-event-fx
  ::load-students
  (fn [{:keys [db]} [_]]
    {:dispatch [::warehouse/load-parent-students
                {:on-success [::load-students-success]}]}))

(re-frame/reg-event-fx
  ::load-students-success
  (fn [{:keys [db]} [_ result]]
    {:db (assoc-in db [:parent :students] result)}))

(re-frame/reg-event-fx
  ::add-student
  (fn [{:keys [db]} [_ data]]
    {:dispatch [::warehouse/add-parent-student
                {:data data}
                {:on-success [::add-student-success]}]}))

(re-frame/reg-event-fx
  ::add-student-success
  (fn [{:keys [db]} [_ result]]
    {:dispatch-n (list [::load-students]
                       [::open-dashboard-page])}))

(re-frame/reg-event-fx
  ::open-dashboard-page
  (fn [{:keys [db]} [_]]
    {:dispatch [::events/redirect :parent-dashboard]}))

(re-frame/reg-event-fx
  ::open-add-student-page
  (fn [{:keys [db]} [_]]
    {:dispatch [::events/redirect :parent-dashboard-add-student]}))

(re-frame/reg-event-fx
  ::open-help-page
  (fn [{:keys [db]} [_]]
    {:dispatch [::events/redirect :parent-dashboard-help]}))

(re-frame/reg-event-fx
  ::login-as
  (fn [{:keys [db]} [_ user-id]]
    {}))
