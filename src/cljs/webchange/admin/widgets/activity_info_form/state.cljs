(ns webchange.admin.widgets.activity-info-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.ui.components.form.data :refer [init]]
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
  [(re-frame/inject-cofx :current-user)
   (i/path path-to-db)]
  (fn [{:keys [db current-user]} [_ {:keys [activity-id] :as props}]]
    {:db         (-> db
                     (reset-form-data)
                     (set-data-loading true)
                     (widgets/set-callbacks (select-keys props [:on-remove]))
                     (assoc :activity-id activity-id)
                     (assoc :is-admin? (= "admin" (:type current-user))))
     :dispatch-n [[::warehouse/load-activity
                   {:activity-id activity-id}
                   {:on-success [::load-activity-success]}]]}))

(re-frame/reg-event-fx
  ::load-activity-success
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
    (let [prepared-data (-> data
                            (merge (:metadata data))
                            (dissoc :metadata))]
      {:db                (-> db
                              (set-data-saving false)
                              (update-form-data prepared-data))
       ::widgets/callback [success-handler data]})))

(re-frame/reg-event-fx
  ::save-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-data-saving db false)}))

;; Archive

(def remove-window-state-key :remove-window-state)

(def remove-window-default-state {:open?        false
                                  :in-progress? false
                                  :done?        false})

(defn- update-remove-window-state
  [db data-patch]
  (update db remove-window-state-key merge data-patch))

(re-frame/reg-sub
  ::remove-window-state
  :<- [path-to-db]
  #(get % remove-window-state-key remove-window-default-state))

(re-frame/reg-event-fx
  ::open-remove-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-window-state db {:open? true})}))

(re-frame/reg-event-fx
  ::close-remove-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-window-state db remove-window-default-state)}))

(re-frame/reg-event-fx
  ::handle-removed
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [success-handler (widgets/get-callback db :on-remove)]
      {:dispatch  [::close-remove-window]
       ::widgets/callback [success-handler]})))

(re-frame/reg-event-fx
  ::remove-activity
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [activity-id (:activity-id db)]
      {:db       (assoc db :activity-removing true)
       :dispatch [::warehouse/archive-activity
                  {:activity-id activity-id}
                  {:on-success [::remove-success]
                   :on-failure [::remove-failure]}]})))

(re-frame/reg-event-fx
  ::remove-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ response]]
    {:db (-> db
             (assoc :activity-removing false)
             (update-remove-window-state {:done? true}))}))

(re-frame/reg-event-fx
  ::remove-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :activity-removing false)}))

