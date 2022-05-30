(ns webchange.login.check-current-user
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :utils/check-current-user)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(def in-progress-key :in-progress?)

(defn- set-in-progress
  [db value]
  (assoc db in-progress-key value))

(re-frame/reg-sub
  ::in-progress?
  :<- [path-to-db]
  #(get % in-progress-key true))

(re-frame/reg-event-fx
  ::check-current-user
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (set-in-progress db true)
     :dispatch [::warehouse/load-current-user
                {:on-success [::load-current-user-success]
                 :on-failure [::load-current-user-failure]}]}))

(re-frame/reg-event-fx
  ::load-current-user-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-in-progress db false)}))

(re-frame/reg-event-fx
  ::load-current-user-failure
  (fn [{:keys [_]} [_]]
    {::redirect-to-login true}))

(re-frame/reg-fx
  ::redirect-to-login
  (fn []
    (set! (.-location js/document) "/")))
