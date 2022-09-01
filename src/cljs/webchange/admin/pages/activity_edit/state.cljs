(ns webchange.admin.pages.activity-edit.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.auth.state :as auth]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/activity-edit)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Activity Loading

(def activity-loading-key :activity-loading?)

(defn- set-activity-loading
  [db value]
  (assoc db activity-loading-key value))

(re-frame/reg-sub
  ::activity-loading?
  :<- [path-to-db]
  #(get % activity-loading-key false))

;; Activity Data

(def activity-key :activity)

(defn- get-activity
  [db]
  (get db activity-key))

(defn- set-activity
  [db value]
  (assoc db activity-key value))

(defn- update-activity
  [db data-patch]
  (update db activity-key merge data-patch))

(defn- update-activity-metadata
  [db data-patch]
  (update-in db [activity-key :metadata] merge data-patch))

(re-frame/reg-sub
  ::activity
  :<- [path-to-db]
  #(get-activity %))

(re-frame/reg-sub
  ::activity-metadata
  :<- [::activity]
  #(get % :metadata {}))

;; Form editable

(def form-editable-key :form-editable?)

(re-frame/reg-sub
  ::form-editable?
  :<- [path-to-db]
  #(get % form-editable-key false))

(re-frame/reg-event-fx
  ::set-form-editable
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db form-editable-key value)}))

(re-frame/reg-event-fx
  ::toggle-form-editable
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update db form-editable-key not)}))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [activity-id]}]]
    {:db       (-> db
                   (set-activity-loading true)
                   (assoc form-editable-key false))
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

;;

(re-frame/reg-event-fx
  ::edit
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id]} (get-activity db)]
      {:dispatch [::routes/redirect :lesson-builder :activity-id id]})))

(re-frame/reg-event-fx
  ::play
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id]} (get-activity db)
          href (str "/s/" id)]                              ;; not working for main module [::routes/redirect :activity-sandbox :scene-id id]
      (js/window.open href "_blank"))))

(re-frame/reg-event-fx
  ::open-activities-page
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :activities]}))

;; lock activity

(re-frame/reg-sub
  ::activity-locked?
  :<- [::activity-metadata]
  #(get % :locked false))

(def lock-loading-key :lock-loading?)

(defn- set-lock-loading
  [db value]
  (assoc db lock-loading-key value))

(re-frame/reg-sub
  ::lock-loading?
  :<- [path-to-db]
  #(get % lock-loading-key false))

(re-frame/reg-event-fx
  ::set-locked
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    (let [{:keys [id]} (get-activity db)]
      {:db       (-> db (set-lock-loading true))
       :dispatch [::warehouse/toggle-activity-locked
                  {:activity-id id
                   :locked      value}
                  {:on-success [::set-locked-success value]
                   :on-failure [::set-locked-failure]}]})))

(re-frame/reg-event-fx
  ::set-locked-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (-> db
             (update-activity-metadata {:locked value})
             (set-lock-loading false))}))

(re-frame/reg-event-fx
  ::set-locked-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-lock-loading false))}))

(re-frame/reg-sub
  ::lock-control-state
  :<- [::auth/bbs-admin?]
  :<- [::auth/super-admin?]
  :<- [::activity-locked?]
  (fn [[bbs-admin? super-admin? activity-locked?]]
    {:disabled?  false
     :read-only? (and bbs-admin?
                      activity-locked?)
     :visible?   (or super-admin?
                     activity-locked?)}))

(re-frame/reg-sub
  ::activity-ui-locked?
  :<- [::auth/super-admin?]
  :<- [::activity-locked?]
  (fn [[super-admin? activity-locked?]]
    (and activity-locked? (not super-admin?))))

;; publish activity

(re-frame/reg-sub
  ::activity-published?
  :<- [::activity]
  #(= "visible" (:status %)))

(def publish-loading-key :publish-loading?)

(defn- set-publish-loading
  [db value]
  (assoc db publish-loading-key value))

(re-frame/reg-sub
  ::publish-loading?
  :<- [path-to-db]
  #(get % publish-loading-key false))

(re-frame/reg-event-fx
  ::set-published
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    (let [{:keys [id]} (get-activity db)]
      {:db       (-> db (set-publish-loading true))
       :dispatch [::warehouse/toggle-activity-visibility
                  {:activity-id id
                   :visible     value}
                  {:on-success [::set-published-success]
                   :on-failure [::set-published-failure]}]})))

(re-frame/reg-event-fx
  ::set-published-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ response]]
    {:db (-> db
             (update-activity response)
             (set-publish-loading false))}))

(re-frame/reg-event-fx
  ::set-published-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-publish-loading false))}))

(re-frame/reg-event-fx
  ::duplicate
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id name lang]} (get-activity db)]
      {:db       (-> db (set-activity-loading true))
       :dispatch [::warehouse/duplicate-activity
                  {:activity-id id
                   :data {:name name
                          :lang lang}}
                  {:on-success [::duplicate-success]
                   :on-failure [::duplicate-failure]}]})))

(re-frame/reg-event-fx
  ::duplicate-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [id]}]]
    {:dispatch-n [[::init {:activity-id id}]
                  [::routes/redirect :activity-edit :activity-id id]]}))

(re-frame/reg-event-fx
  ::duplicate-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-loading false))}))

(re-frame/reg-event-fx
  ::open-activities-page
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ response]]
    {:db (-> db
             (update-activity response)
             (set-publish-loading false))}))

(re-frame/reg-sub
  ::publish-control-state
  :<- [::auth/bbs-admin?]
  :<- [::auth/super-admin?]
  :<- [::activity-locked?]
  (fn [[bbs-admin? super-admin? activity-locked?]]
    {:disabled?  activity-locked?
     :read-only? (and bbs-admin?
                      activity-locked?)
     :visible?   (or bbs-admin? super-admin?)}))
