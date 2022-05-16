(ns webchange.admin.widgets.class-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.components.form.data :refer [init]]
    [webchange.admin.widgets.state :as widgets]
    [webchange.state.warehouse :as warehouse]
    [webchange.validation.validate :refer [validate]]))

(def path-to-db :widget/class-form)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Form Data

(def form-data (init :form-data))
(def get-form-data (:get-data form-data))
(def set-form-data (:set-data form-data))
(def reset-form-data (:reset-data form-data))
(def update-form-data (:update-data form-data))

(re-frame/reg-sub
  ::form-data
  :<- [path-to-db]
  #(get-form-data %))

;; Form Options

(def form-options (init :form-options))
(def get-form-options (:get-data form-options))
(def set-form-options (:set-data form-options))
(def reset-form-options (:reset-data form-options))
(def update-form-options (:update-data form-options))

(re-frame/reg-sub
  ::form-options
  :<- [path-to-db]
  #(get-form-options %))

(def course-options-key :course-options)

(re-frame/reg-sub
  ::course-options
  :<- [::form-options]
  #(get % course-options-key []))

(defn- set-course-options
  [db options]
  (update-form-options db {course-options-key options}))

;; Flags

(def flags-data (init :flags))
(def get-flags-data (:get-data flags-data))
(def set-flags-data (:set-data flags-data))
(def reset-flags-data (:reset-data flags-data))
(def update-flags-data (:update-data flags-data))

(re-frame/reg-sub
  ::flags-data
  :<- [path-to-db]
  #(get-flags-data %))

(def data-loading-key :data-loading?)

(defn- set-data-loading
  [db value]
  (update-flags-data db {data-loading-key value}))

(re-frame/reg-sub
  ::data-loading?
  :<- [::flags-data]
  #(get % data-loading-key true))

(def data-saving-key :data-saving?)

(defn- set-data-saving
  [db value]
  (update-flags-data db {data-saving-key value}))

(re-frame/reg-sub
  ::data-saving?
  :<- [::flags-data]
  #(get % data-saving-key false))

;; Init

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [class-id school-id]}]]
    {:db         (-> db
                     (reset-form-data)
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
  (fn [{:keys [db]} [_ class-data {:keys [on-success]}]]
    (let [class-id (-> (get-form-data db)
                       (get :id))]
      {:db       (set-data-saving db true)
       :dispatch [::warehouse/save-class
                  {:class-id class-id :data class-data}
                  {:on-success [::save-success on-success]
                   :on-failure [::save-failure]}]})))

(re-frame/reg-event-fx
  ::save-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ success-handler {:keys [data]}]]
    {:db                (-> db
                            (set-data-saving false)
                            (update-form-data data))
     ::widgets/callback [success-handler data]}))

(re-frame/reg-event-fx
  ::save-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-data-saving db false)}))
