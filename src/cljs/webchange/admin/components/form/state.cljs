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

;; Callbacks

(def callbacks-key :callbacks)

(defn- set-callback
  [db name value]
  (assoc-in db [callbacks-key name] value))

(defn- get-callback
  [db name]
  (get-in db [callbacks-key name]))

(re-frame/reg-fx
  ::callback
  (fn [[callback & params]]
    (when (fn? callback)
      (apply callback params))))

;; Form Data

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
    (let [on-change (get-callback db :on-change)]
      {:db        (form/update-data db form-id {field-name field-value})
       ::callback [on-change {field-name field-value}]})))

(re-frame/reg-sub
  ::errors-data
  :<- [path-to-db]
  (fn [db [_ form-id]]
    (errors/get-data db form-id)))

(def custom-errors-key :custom-errors)

(defn- get-custom-errors
  [db form-id]
  (get-in db [form-id custom-errors-key] {}))

(defn- set-custom-errors
  [db form-id errors]
  (assoc-in db [form-id custom-errors-key] errors))

(defn- reset-custom-errors
  [db form-id]
  (set-custom-errors db form-id {}))

(re-frame/reg-event-fx
  ::set-custom-errors
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ form-id custom-errors]]
    (let [current-errors (errors/get-data db form-id)]
      {:db (-> db
               (set-custom-errors form-id custom-errors)
               (errors/set-data form-id (merge current-errors custom-errors)))})))

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
  (fn [{:keys [db]} [_ {:keys [init-data data form-id model on-change errors]}]]
    {:db       (-> db
                   (set-callback :on-change on-change)
                   (set-form-data form-id (or init-data data) model)
                   (reset-custom-errors form-id)
                   (errors/reset-data form-id))
     :dispatch [::set-custom-errors form-id errors]}))

(re-frame/reg-event-fx
  ::reset
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [form-id]}]]
    {:db (dissoc db form-id)}))

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
          custom-errors (get-custom-errors db form-id)
          validation-errors (validate spec data)]
      (if (nil? validation-errors)
        {:db        (errors/set-data db form-id custom-errors)
         ::callback [on-success data]}
        {:db (errors/set-data db form-id (merge validation-errors custom-errors))}))))

(re-frame/reg-fx
  ::callback
  (fn [[callback & params]]
    (when (fn? callback)
      (apply callback params))))
