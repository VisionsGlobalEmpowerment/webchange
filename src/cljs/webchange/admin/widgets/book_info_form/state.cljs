(ns webchange.admin.widgets.book-info-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.ui.components.form.data :refer [init]]
    [webchange.admin.widgets.state :as widgets]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :widget/book-info-form)

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
  (fn [{:keys [db]} [_ {:keys [book-id]}]]
    {:db         (-> db
                     (reset-form-data)
                     (set-data-loading true))
     :dispatch-n [[::warehouse/load-activity
                   {:activity-id book-id}
                   {:on-success [::load-book-success]}]]}))

(re-frame/reg-event-fx
  ::load-book-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    (let [prepared-data (-> data
                            (merge (:metadata data))
                            (dissoc :metadata))]
      {:db (-> db
               (set-data-loading false)
               (set-form-data prepared-data))})))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data {:keys [on-success]}]]
    (let [activity-id (-> (get-form-data db)
                          (get :id))
          prepared-data {:name (:name data)
                         :lang (:lang data)
                         :metadata {:about (:about data)
                                    :short-description (:short-description data)}}]
      {:db       (set-data-saving db true)
       :dispatch [::warehouse/save-activity
                  {:activity-id activity-id :data prepared-data}
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
