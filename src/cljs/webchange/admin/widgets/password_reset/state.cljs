(ns webchange.admin.widgets.password-reset.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.ui.components.form.data :refer [init]]
    [webchange.admin.widgets.state :as widgets]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :widget/reset-password)

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
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (dissoc db path-to-db)}))

(re-frame/reg-event-fx
  ::change-password
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ account-id {:keys [password confirm]} {:keys [on-success]}]]
    (if (not= password confirm)
      {:db (set-custom-errors db {:confirm "Passwords must be equal"})}
      {:db       (-> db
                     (set-custom-errors {})
                     (set-data-saving true))
       :dispatch [::warehouse/change-account-password
                  {:id   account-id
                   :data {:password password}}
                  {:on-success [::change-password-success on-success]
                   :on-failure [::change-password-failure]}]})))

(re-frame/reg-event-fx
  ::change-password-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ success-handler response]]
    {:db                (set-data-saving db false)
     ::widgets/callback [success-handler response]}))

(re-frame/reg-event-fx
  ::change-password-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-data-saving db false)}))
