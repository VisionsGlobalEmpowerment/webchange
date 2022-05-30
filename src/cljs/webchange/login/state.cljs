(ns webchange.login.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :module/login)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(def current-page-key :current-page)

(re-frame/reg-sub
  ::current-page
  :<- [path-to-db]
  #(get % current-page-key))

(re-frame/reg-event-fx
  ::set-current-page
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db current-page-key value)}))

;; Authentication

(def current-user-loaded-key :current-user-loaded?)

(defn- set-current-user-loaded
  [db value]
  (assoc db current-user-loaded-key value))

(re-frame/reg-sub
  ::current-user-loaded?
  :<- [path-to-db]
  #(get % current-user-loaded-key false))

(re-frame/reg-event-fx
  ::load-current-user
  (fn [{:keys [_]} [_]]
    {:dispatch [::warehouse/load-current-user
                {:on-success [::load-current-user-success]
                 :on-failure [::load-current-user-failure]}]}))

(re-frame/reg-event-fx
  ::load-current-user-success
  (fn [{:keys [_]} [_ {:keys [type]}]]
    {:redirect-to-module type}))

(re-frame/reg-event-fx
  ::load-current-user-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-current-user-loaded db true)}))
