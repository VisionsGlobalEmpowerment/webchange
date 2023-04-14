(ns webchange.login.sign-in.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :form/login)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn- get-form-data
  [db]
  (get db :form-data))

(defn- update-form-data
  [db data-patch]
  (update db :form-data merge data-patch))

(re-frame/reg-sub
  ::form-data
  :<- [path-to-db]
  get-form-data)

;; Username

(def username-key :email)

(re-frame/reg-sub
  ::username
  :<- [::form-data]
  #(get % username-key ""))

(re-frame/reg-event-fx
  ::set-username
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (update-form-data db {username-key value})}))

;; Password

(def password-key :password)

(re-frame/reg-sub
  ::password
  :<- [::form-data]
  #(get % password-key ""))

(re-frame/reg-event-fx
  ::set-password
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (update-form-data db {password-key value})}))

;; Submit

(def loading-key :loading?)

(re-frame/reg-sub
  ::loading?
  :<- [path-to-db]
  #(get % loading-key false))

(defn- set-loading
  [db value]
  (assoc db loading-key value))

(re-frame/reg-event-fx
  ::login
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ type]]
    (let [data (get-form-data db)]
      {:db       (set-loading db true)
       :dispatch [::warehouse/login-with-credentials {:data data} {:on-success [::login-success type]
                                                                   :on-failure [::login-failure]}]})))

(re-frame/reg-event-fx
  ::login-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ type]]
    {:db                 (set-loading db false)
     :redirect-to-module (or type "educator")}))

(re-frame/reg-event-fx
  ::login-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-loading db false)}))

(re-frame/reg-sub
  ::sign-in-as-type
  (fn [_ [_ type]]
    (or type "educator")))
