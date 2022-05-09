(ns webchange.admin.widgets.class-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]
    [webchange.validation.specs.class-spec :as class-spec]
    [webchange.validation.validate :refer [validate]]))

(def path-to-db :widget/class-form)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; School ID

(def school-id-key :school-id)

(defn- get-school-id
  [db]
  (get db school-id-key))

(defn- set-school-id
  [db school-id]
  (assoc db school-id-key school-id))

;; Class ID

(def class-id-key :class-id)

(defn- get-class-id
  [db]
  (get db class-id-key))

(defn- set-class-id
  [db class-id]
  (assoc db class-id-key class-id))

;; Form Data

(def from-data-key :form)

(defn- get-form-data
  [db]
  (get db from-data-key {}))

(defn- set-form-data
  [db data]
  (assoc db from-data-key data))

(defn- reset-form-data
  [db]
  (set-form-data db {}))

(defn- update-form-data
  [db data-patch]
  (update db from-data-key merge data-patch))

(re-frame/reg-sub
  ::form-data
  :<- [path-to-db]
  get-form-data)

;; -- name

(def name-key :name)

(re-frame/reg-sub
  ::class-name
  :<- [::form-data]
  #(get % name-key ""))

(re-frame/reg-event-fx
  ::set-class-name
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (update-form-data db {name-key value})}))

(re-frame/reg-sub
  ::class-name-error
  :<- [::errors]
  #(get % name-key))

;; Errors

(def errors-key :errors)

(defn- get-errors
  [db]
  (get db errors-key {}))

(defn- set-errors
  [db errors]
  (assoc db errors-key errors))

(defn- reset-errors
  [db]
  (set-errors db {}))

(re-frame/reg-sub
  ::errors
  :<- [path-to-db]
  get-errors)

;; Callbacks

(def callbacks-key :callbacks)

(defn- set-callbacks
  [db callbacks]
  (assoc db callbacks-key callbacks))

(defn- get-callback
  [db callback-key]
  (-> (get db callbacks-key)
      (get callback-key)))

(re-frame/reg-fx
  ::callback
  (fn [[callback & params]]
    (when (fn? callback)
      (apply callback params))))

;; Indicators

(def data-loading-key :data-loading?)

(defn- set-data-loading
  [db value]
  (assoc db data-loading-key value))

(re-frame/reg-sub
  ::data-loading?
  :<- [path-to-db]
  #(get % data-loading-key true))

(def data-saving-key :data-saving?)

(defn- set-data-saving
  [db value]
  (assoc db data-saving-key value))

(re-frame/reg-sub
  ::data-saving?
  :<- [path-to-db]
  #(get % data-saving-key false))

;; Init

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class-id school-id on-save] :as props}]]
    (print "::init" props)
    {:db       (-> db
                   (set-class-id class-id)
                   (set-school-id school-id)
                   (reset-form-data)
                   (reset-errors)
                   (set-callbacks {:on-save on-save})
                   (set-data-loading true))
     :dispatch [::warehouse/load-class {:class-id class-id}
                {:on-success [::load-class-success]}]}))


(re-frame/reg-event-fx
  ::load-class-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class]}]]
    (print "::load-class-success" class)
    {:db (-> db
             (set-form-data class)
             (set-data-loading false))}))


(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [class-id (get-class-id db)
          class-data (get-form-data db)
          validation-errors (validate ::class-spec/class class-data)]
      (if (nil? validation-errors)
        {:db       (-> db
                       (reset-errors)
                       (set-data-saving true))
         :dispatch [::warehouse/save-class
                    {:class-id class-id :data class-data}
                    {:on-success [::save-success]
                     :on-failure [::save-failure]}]}
        {:db (set-errors db validation-errors)}))))

(re-frame/reg-event-fx
  ::save-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ school-data]]
    (let [callback (get-callback db :on-save)]
      {:db        (set-data-saving db false)
       ::callback [callback school-data]})))

(re-frame/reg-event-fx
  ::save-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-data-saving db false)}))

