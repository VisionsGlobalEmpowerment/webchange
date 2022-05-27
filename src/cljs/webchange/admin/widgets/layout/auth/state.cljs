(ns webchange.admin.widgets.layout.auth.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.login.check-current-user :as current-user]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :widget/auth)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Current user

(def current-user-key :current-user)

(defn- set-current-user
  [db user-data]
  (assoc db current-user-key user-data))

(defn- get-current-user
  [db]
  (get db current-user-key {}))

(re-frame/reg-sub
  ::current-user
  :<- [path-to-db]
  get-current-user)

(re-frame/reg-sub
  ::user-name
  :<- [::current-user]
  #(get % :first-name ""))

;;

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_]]
    {:dispatch [::warehouse/load-current-user {:on-success [::load-current-user-success]}]}))

(re-frame/reg-event-fx
  ::load-current-user-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ current-user]]
    {:db (set-current-user db current-user)}))

(re-frame/reg-event-fx
  ::open-login-page
  (fn [_]
    {:dispatch [::routes/redirect :login]}))

(re-frame/reg-event-fx
  ::logout
  (fn [_]
    {:dispatch [::warehouse/logout {:on-success [::logout-success]}]}))

(re-frame/reg-event-fx
  ::logout-success
  (fn [_]
    {::current-user/redirect-to-login true}))
