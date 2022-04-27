(ns webchange.admin.pages.login.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.state :as parent-state]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:login])
       (parent-state/path-to-db)))

(def form-data-path (path-to-db [:form-data]))

(defn- get-form-data
  [db]
  (get-in db form-data-path))

(re-frame/reg-sub
  ::form-data
  get-form-data)

(re-frame/reg-event-fx
  ::update-form-data
  (fn [{:keys [db]} [_ data-patch]]
    {:db (update-in db form-data-path merge data-patch)}))

;; Username

(def username-key :username)

(re-frame/reg-sub
  ::username
  (fn []
    (re-frame/subscribe [::form-data]))
  (fn [form-data]
    (get form-data username-key "")))

(re-frame/reg-event-fx
  ::set-username
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-form-data {username-key value}]}))

;; Password

(def password-key :password)

(re-frame/reg-sub
  ::password
  (fn []
    (re-frame/subscribe [::form-data]))
  (fn [form-data]
    (get form-data password-key "")))

(re-frame/reg-event-fx
  ::set-password
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-form-data {password-key value}]}))

;; Submit

(re-frame/reg-event-fx
  ::login
  (fn [{:keys [db]} [_]]
    (let [data (get-form-data db)]
      {:dispatch [::warehouse/admin-login {:data data} {:on-success [::login-success]}]})))

(re-frame/reg-event-fx
  ::login-success
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :dashboard]}))
