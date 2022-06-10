(ns webchange.admin.widgets.activity-info-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.components.form.data :refer [init]]
    [webchange.admin.widgets.state :as widgets]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :widget/activity-info-form)

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

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [activity-id]}]]
    {:db         (-> db
                     (reset-form-data)
                     (set-data-loading true))
     :dispatch-n [[::warehouse/load-available-activity
                   {:activity-id activity-id}
                   {:on-success [::load-activity-success]}]]}))

(re-frame/reg-event-fx
  ::load-activity-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (set-data-loading false)
             (set-form-data data))}))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ class-data {:keys [on-success]}]]
    (let [class-id (-> (get-form-data db)
                       (get :id))]
      {:db       (set-data-saving db true)
       :dispatch [::warehouse/save-available-activity
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
