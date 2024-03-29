(ns webchange.admin.widgets.course-info-form.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.ui.components.form.data :refer [init]]
    [webchange.admin.widgets.state :as widgets]
    [webchange.state.warehouse :as warehouse]
    [webchange.auth.state :as auth]))

(def path-to-db :widget/course-info-form)

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

;; Data Saving?

(def data-saving-key :data-saving?)

(defn- set-data-saving
  [db value]
  (assoc db data-saving-key value))

(re-frame/reg-sub
  ::data-saving?
  :<- [path-to-db]
  #(get % data-saving-key false))

;; Data Loading?

(def data-loading-key :data-loading?)

(defn- set-data-loading
  [db value]
  (assoc db data-loading-key value))

(re-frame/reg-sub
  ::data-loading?
  :<- [path-to-db]
  #(get % data-loading-key false))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [course-slug] :as props}]]
    {:db       (-> db
                   (reset-form-data)
                   (set-data-loading true)
                   (assoc :course-slug course-slug))
     :dispatch [::warehouse/load-course-info course-slug
                {:on-success [::load-course-info-success]}]}))

(re-frame/reg-event-fx
  ::load-course-info-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ course-info]]
    (let [prepared-data (-> course-info
                            (assoc :locked (get-in course-info [:metadata :locked])))]
      {:db (-> db
               (set-form-data prepared-data)
               (set-data-loading false)
               (assoc :status (:status course-info)))})))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data {:keys [on-success]}]]
    (let [{course-id :id} (get-form-data db)
          data (assoc-in data [:metadata :locked] (:locked data))]
      {:db       (set-data-saving db true)
       :dispatch [::warehouse/save-course-info
                  {:course-id course-id
                   :data      data}
                  {:on-success [::save-success on-success]
                   :on-failure [::save-failure]}]})))

(re-frame/reg-event-fx
  ::save-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ success-handler response]]
    {:db                (set-data-saving db false)
     ::widgets/callback [success-handler response]}))

(re-frame/reg-event-fx
  ::save-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (set-data-saving db false)}))

;; Publish
(re-frame/reg-sub
  ::published
  :<- [path-to-db]
  #(-> (get % :status)
       (= "published")))

(re-frame/reg-sub
  ::publish-loading?
  :<- [path-to-db]
  #(get % :publish-loading false))

(re-frame/reg-event-fx
  ::set-published
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    (let [course-slug (:course-slug db)]
      {:db       (assoc db :publish-loading true)
       :dispatch [::warehouse/toggle-course-visibility
                  {:course-slug course-slug
                   :visible value}
                  {:on-success [::set-published-success]
                   :on-failure [::set-published-failure]}]})))

(re-frame/reg-event-fx
  ::set-published-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [status]}]]
    {:db (-> db
             (assoc :publish-loading false)
             (assoc :status status))}))

(re-frame/reg-event-fx
  ::set-published-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :publish-loading false)}))

(re-frame/reg-sub
  ::can-publish?
  :<- [::auth/super-admin?]
  (fn [super-admin?]
    super-admin?))
