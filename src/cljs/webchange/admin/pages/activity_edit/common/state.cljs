(ns webchange.admin.pages.activity-edit.common.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.auth.state :as auth]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/activity-edit--common)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; activity loading

(def activity-loading-key :activity-loading?)

(defn- set-activity-loading
  [db value]
  (assoc db activity-loading-key value))

(re-frame/reg-sub
  ::activity-loading?
  :<- [path-to-db]
  #(get % activity-loading-key false))

;; activity data

(def activity-key :activity-data)

(defn- get-activity
  [db]
  (get db activity-key))

(defn- set-activity
  [db value]
  (assoc db activity-key value))

(defn- update-activity
  [db data-patch]
  (let [activity-data (get-activity db)
        updated-data (-> (merge activity-data data-patch)
                         (assoc :metadata (merge (:metadata activity-data)
                                                 (:metadata data-patch))))]
    (assoc db activity-key updated-data)))

(re-frame/reg-sub
  ::activity-data
  :<- [path-to-db]
  #(get-activity %))

(re-frame/reg-event-fx
  ::update-activity-data
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data-patch]]
    {:db (update-activity db data-patch)}))

;; activity properties

(re-frame/reg-sub
  ::activity-id
  :<- [::activity-data]
  #(get % :id))

(re-frame/reg-sub
  ::activity-locked?
  :<- [::activity-data]
  #(get-in % [:metadata :locked] false))

(re-frame/reg-sub
  ::activity-published?
  :<- [::activity-data]
  #(= "visible" (:status %)))

(re-frame/reg-sub
  ::activity-ui-locked?
  :<- [::auth/super-admin?]
  :<- [::activity-locked?]
  (fn [[super-admin? activity-locked?]]
    (and activity-locked? (not super-admin?))))

;; load activity

(re-frame/reg-event-fx
  ::load-activity
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ activity-id]]
    {:db       (set-activity-loading db true)
     :dispatch [::warehouse/load-activity
                {:activity-id activity-id}
                {:on-success [::load-activity-success]
                 :on-failure [::load-activity-failure]}]}))

(re-frame/reg-event-fx
  ::load-activity-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (set-activity-loading false)
             (set-activity data))}))

(re-frame/reg-event-fx
  ::load-activity-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-loading false))}))


;; actions

(def init-props-key :init-props)

(defn- get-init-props
  [db]
  (get db init-props-key))

(defn- set-init-props
  [db value]
  (assoc db init-props-key value))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [activity-id] :as props}]]
    {:db       (-> db (set-init-props props))
     :dispatch [::load-activity activity-id]}))

(re-frame/reg-event-fx
  ::duplicate-activity
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [on-success]}]]
    (let [{:keys [id name lang]} (get-activity db)]
      {:db       (-> db (set-activity-loading true))
       :dispatch [::warehouse/duplicate-activity
                  {:activity-id id
                   :data        {:name name
                                 :lang lang}}
                  {:on-success [::duplicate-activity-success on-success]
                   :on-failure [::duplicate-activity-failure]}]})))

(re-frame/reg-event-fx
  ::duplicate-activity-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ on-success {:keys [id] :as activity-data}]]
    (let [props (-> (get-init-props db)
                    (assoc :activity-id id))]
      {:db         (-> db (set-activity-loading false))
       :dispatch-n (cond-> [[::init props]]                 ;; reset current activity data for new duplicated activity
                           (some? on-success) (conj (conj on-success activity-data)))})))

(re-frame/reg-event-fx
  ::duplicate-activity-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-loading false))}))

(re-frame/reg-event-fx
  ::edit-activity
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id]} (get-activity db)]
      {:dispatch [::routes/redirect :lesson-builder :activity-id id]})))

(re-frame/reg-event-fx
  ::play-activity
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id]} (get-activity db)
          href (str "/s/" id)]                              ;; not working for main module [::routes/redirect :activity-sandbox :scene-id id]
      (js/window.open href "_blank"))))
