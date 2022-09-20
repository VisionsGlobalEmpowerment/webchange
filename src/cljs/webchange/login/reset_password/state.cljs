(ns webchange.login.reset-password.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]
    [webchange.login.routes :as routes]))

(def path-to-db :login/reset-password)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Custom Error

(def custom-errors-key :custom-errors)

(defn- set-custom-errors
  [db errors]
  (assoc db custom-errors-key errors))

(re-frame/reg-sub
  ::custom-errors
  :<- [path-to-db]
  #(get % custom-errors-key {}))

;; Data Saving?

(def data-saving-key :data-saving?)

(defn- set-data-saving
  [db value]
  (assoc db data-saving-key value))

(re-frame/reg-sub
  ::data-saving?
  :<- [path-to-db]
  #(get % data-saving-key false))

;;

(re-frame/reg-event-fx
  ::init-reset
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [code]}]]
    {:db (assoc db :code code)}))

(re-frame/reg-event-fx
  ::change-password
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [password password-confirm]}]]
    (let [code (:code db)]
      (if (not= password password-confirm)
        {:db (set-custom-errors db {:password-confirm "Passwords must be equal"})}
        {:db       (-> db
                       (set-custom-errors {})
                       (set-data-saving true))
         :dispatch [::warehouse/reset-password-by-code
                    {:code code
                     :data {:password password}}
                    {:on-success [::change-password-success]
                     :on-failure [::change-password-failure]}]}))))

(re-frame/reg-event-fx
  ::change-password-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ success-handler response]]
    {:db (set-data-saving db false)
     :dispatch [::routes/redirect :sign-in]}))

(re-frame/reg-event-fx
  ::change-password-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-data-saving db false)}))

(re-frame/reg-sub
  ::email-submitted?
  :<- [path-to-db]
  #(get % :email-submitted? false))

(re-frame/reg-event-fx
  ::submit-email
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [email (:email db)]
      {:db       (-> db
                     (set-data-saving true))
       :dispatch [::warehouse/reset-password-by-email
                  {:email email}
                  {:on-success [::submit-email-success]
                   :on-failure [::submit-email-failure]}]})))

(re-frame/reg-event-fx
  ::submit-email-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ response]]
    {:db (-> db
             (set-data-saving false)
             (assoc :email-submitted? true))}))

(re-frame/reg-event-fx
  ::submit-email-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-data-saving db false)}))

(re-frame/reg-event-fx
  ::set-email
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (-> db
             (assoc :email value))}))
