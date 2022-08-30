(ns webchange.auth.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]))

(def path-to-db :auth/index)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; current user

(def current-user-key :current-user)

(defn- get-current-user
  [db]
  (get db current-user-key))

(defn- set-current-user
  [db value]
  (assoc db current-user-key value))

(re-frame/reg-sub
  ::current-user
  :<- [path-to-db]
  get-current-user)

(re-frame/reg-event-fx
  ::set-current-user
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (set-current-user db value)}))

;;

(re-frame/reg-sub
  ::super-admin?
  :<- [::current-user]
  (fn [{:keys [type]}]
    (= "admin" type)))

;;

(re-frame/reg-sub
  ::can-lock-activity?
  :<- [::super-admin?]
  identity)
