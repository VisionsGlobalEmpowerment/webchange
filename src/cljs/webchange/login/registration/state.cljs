(ns webchange.login.registration.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.ui.components.form.data :refer [init]]
    [webchange.login.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :form/registration)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Form Data

(def form-data (init :form-data))
(def get-form-data (:get-data form-data))

;; Data Saving?

(def data-saving-key :data-saving?)

(defn- set-data-saving
  [db value]
  (assoc db data-saving-key value))

(re-frame/reg-sub
  ::data-saving?
  :<- [path-to-db]
  #(get % data-saving-key false))

;; Custom Error

(def custom-errors-key :custom-errors)

(defn- set-custom-errors
  [db errors]
  (assoc db custom-errors-key errors))

(re-frame/reg-sub
  ::custom-errors
  :<- [path-to-db]
  #(get % custom-errors-key {}))

(re-frame/reg-event-fx
  ::register
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [password password-confirm] :as data}]]
    (if (not= password password-confirm)
      {:db (set-custom-errors db {:password-confirm "Passwords not equal"})}
      {:db       (-> db
                     (set-data-saving true)
                     (set-custom-errors nil))
       :dispatch [::warehouse/register-account data
                  {:on-success [::register-success]
                   :on-failure [::register-failure]}]})))

(re-frame/reg-event-fx
  ::register-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-data-saving db false)
     :dispatch [::routes/redirect :email-confirmation-success]}))

(re-frame/reg-event-fx
  ::register-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-data-saving db false)}))
