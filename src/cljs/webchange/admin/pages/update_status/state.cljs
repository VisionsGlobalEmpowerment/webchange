(ns webchange.admin.pages.update-status.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.events :as ie]
    [webchange.state.warehouse :as warehouse]
    [webchange.admin.routes :as routes]))

(def path-to-db :pages-update-status/index)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::retry-status]}))

(re-frame/reg-event-fx
  ::sync-status-received
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [status]}]]
    (let [was-ready? (= "ready" (:status db))
          is-ready? (= "ready" status)
          redirect? (and is-ready? was-ready?)]
      {:db (assoc db :status status)
       :dispatch (if redirect?
                   [::routes/redirect :login]
                   [::retry-status])})))

(re-frame/reg-event-fx
  ::sync-status-failed
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :status "server-offline")
     :dispatch [::retry-status]}))

(re-frame/reg-event-fx
  ::retry-status
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch-later [{:ms 2000 :dispatch [::warehouse/sync-status
                                           {:on-success [::sync-status-received]
                                            :on-failure [::sync-status-failed]
                                            :suppress-api-error? true}]}]}))

(re-frame/reg-sub
  ::status
  :<- [path-to-db]
  (fn [db]
    (get db :status "loading")))
