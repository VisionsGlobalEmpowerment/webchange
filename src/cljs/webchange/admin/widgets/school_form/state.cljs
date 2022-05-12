(ns webchange.admin.widgets.school-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.components.form.data :refer [init]]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :widget/school-form)

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

;; Init

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school-id]}]]
    {:db       (-> db
                   (reset-form-data)
                   (set-data-loading true))
     :dispatch [::warehouse/load-school {:school-id school-id}
                {:on-success [::load-school-success]}]}))

(re-frame/reg-event-fx
  ::init-add-form
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [_]}]]
    {:db (reset-form-data db)}))

(re-frame/reg-event-fx
  ::reset-form
  (fn [{:keys [db]} [_ {:keys [_]}]]
    {:db (dissoc db path-to-db)}))

(re-frame/reg-event-fx
  ::load-school-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [school]}]]
    {:db (-> db
             (set-form-data school)
             (set-data-loading false))}))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ school-data {:keys [on-success]}]]
    (let [school-id (-> (get-form-data db)
                        (get :id))]
      {:db       (set-data-saving db true)
       :dispatch [::warehouse/edit-school
                  {:school-id school-id :data school-data}
                  {:on-success [::save-success on-success]
                   :on-failure [::save-failure]}]})))

(re-frame/reg-event-fx
  ::create-school
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ school-data {:keys [on-success]}]]
    {:db       (set-data-saving db true)
     :dispatch [::warehouse/create-school
                {:data school-data}
                {:on-success [::save-success on-success]
                 :on-failure [::save-failure]}]}))

(re-frame/reg-event-fx
  ::save-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ success-handler school-data]]
    {:db        (-> db
                    (set-data-saving false)
                    (update-form-data school-data))
     ::callback [success-handler school-data]}))

(re-frame/reg-event-fx
  ::save-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-data-saving db false)}))

(re-frame/reg-fx
  ::callback
  (fn [[callback & params]]
    (when (fn? callback)
      (apply callback params))))
