(ns webchange.parent-dashboard.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:parent-dashboard])
       (vec)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_]]
    {:dispatch [::warehouse/login-as-student-parent {}
                {:on-success [::update-auth-state {:ready? true}]}]}))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [_]} [_]]
    {:dispatch [::update-auth-state {:ready? false}]}))

(def auth-state-path (path-to-db [:auth-state]))

(re-frame/reg-sub
  ::auth-state
  (fn [db]
    (get-in db auth-state-path {:ready? false})))

(re-frame/reg-event-fx
  ::update-auth-state
  (fn [{:keys [db]} [_ data]]
    {:db (update-in db auth-state-path merge data)}))

(re-frame/reg-sub
  ::ready?
  (fn []
    (re-frame/subscribe [::auth-state]))
  (fn [state]
    (get state :ready?)))
