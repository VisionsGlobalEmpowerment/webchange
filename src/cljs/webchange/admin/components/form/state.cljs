(ns webchange.admin.components.form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.components.form.errors-data :as errors]
    [webchange.admin.components.form.form-data :as form]
    [webchange.validation.validate :refer [validate]]))

(def path-to-db :component/form)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-sub
  ::form-data
  :<- [path-to-db]
  (fn [db [_ form-id]]
    (form/get-data db form-id)))

(re-frame/reg-sub
  ::field-value
  (fn [[_ form-id]]
    (re-frame.core/subscribe [::form-data form-id]))
  (fn [form-data [_ _ field-name]]
    (get form-data field-name)))

(re-frame/reg-event-fx
  ::set-field-value
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ form-id field-name field-value]]
    {:db (form/update-data db form-id {field-name field-value})}))

(re-frame/reg-sub
  ::errors-data
  :<- [path-to-db]
  (fn [db [_ form-id]]
    (errors/get-data db form-id)))

(re-frame/reg-sub
  ::field-error
  (fn [[_ form-id]]
    (re-frame.core/subscribe [::errors-data form-id]))
  (fn [errors-data [_ _ field-name]]
    (get errors-data field-name)))

;; Init

(defn- set-form-data
  [db form-id form-data model]
  (->> (keys model)
       (select-keys form-data)
       (form/set-data db form-id)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [data form-id model]}]]
    {:db (-> db
             (set-form-data form-id data model)
             (errors/reset-data form-id))}))

(re-frame/reg-event-fx
  ::set-form-data
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ form-id form-data model]]
    {:db (set-form-data db form-id form-data model)}))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ form-id spec on-success]]
    (let [data (form/get-data db form-id)
          validation-errors (validate spec data)]
      (if (nil? validation-errors)
        {:db        (errors/reset-data db form-id)
         ::callback [on-success data]}
        {:db (errors/set-data db form-id validation-errors)}))))

(re-frame/reg-fx
  ::callback
  (fn [[callback & params]]
    (when (fn? callback)
      (apply callback params))))
