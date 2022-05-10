(ns webchange.admin.widgets.class-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]
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

;; Form Options

(def from-options-key :form-options)

(defn- get-form-options
  [db]
  (get db from-options-key {}))

(defn- update-form-options
  [db data-patch]
  (update db from-options-key merge data-patch))

(re-frame/reg-sub
  ::form-options
  :<- [path-to-db]
  get-form-options)

(def course-options-key :course-options)

(re-frame/reg-sub
  ::course-options
  :<- [::form-options]
  #(get % course-options-key []))

(defn- set-course-options
  [db options]
  (update-form-options db {course-options-key options}))

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
  (fn [{:keys [db]} [_ {:keys [class-id school-id on-save]}]]
    {:db         (-> db
                     (set-class-id class-id)
                     (set-school-id school-id)
                     (reset-form-data)
                     (set-callbacks {:on-save on-save})
                     (set-data-loading true))
     :dispatch-n [[::warehouse/load-class {:class-id class-id}
                   {:on-success [::load-class-success]}]
                  [::warehouse/load-school-courses {:school-id school-id}
                   {:on-success [::load-school-courses-success]}]]}))

(re-frame/reg-event-fx
  ::load-class-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class]}]]
    {:db (-> db
             (set-form-data class)
             (set-data-loading false))}))

(re-frame/reg-event-fx
  ::load-school-courses-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ response]]
    {:db (->> response
              (map (fn [{:keys [id name]}]
                     {:text  name
                      :value id}))
              (set-course-options db))}))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ class-data]]
    (let [class-id (get-class-id db)]
      {:db       (set-data-saving db true)
       :dispatch [::warehouse/save-class
                  {:class-id class-id :data class-data}
                  {:on-success [::save-success]
                   :on-failure [::save-failure]}]})))

(re-frame/reg-event-fx
  ::save-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [data]}]]
    (let [callback (get-callback db :on-save)]
      {:db        (-> db
                      (set-data-saving false)
                      (set-form-data data))
       ::callback [callback data]})))

(re-frame/reg-event-fx
  ::save-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-data-saving db false)}))
