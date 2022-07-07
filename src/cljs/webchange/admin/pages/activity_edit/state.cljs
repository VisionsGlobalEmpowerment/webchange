(ns webchange.admin.pages.activity-edit.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
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

(re-frame/reg-sub
  ::activity
  :<- [path-to-db]
  #(get-activity %))

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

;; Remove

(def removing-key :removing?)

(defn- set-removing
  [db value]
  (assoc db removing-key value))

(re-frame/reg-sub
  ::removing?
  :<- [path-to-db]
  #(get % removing-key false))

(re-frame/reg-event-fx
  ::remove
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id]} (get-activity db)]
      {:db       (-> db (set-removing true))
       :dispatch [::warehouse/archive-activity
                  {:activity-id id}
                  {:on-success [::remove-success]
                   :on-failure [::remove-failure]}]})))

(re-frame/reg-event-fx
  ::remove-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (-> db (set-removing false))
     :dispatch [::routes/redirect :activities]}))

(re-frame/reg-event-fx
  ::remove-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-removing false))}))

;;

(re-frame/reg-event-fx
  ::edit
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id]} (get-activity db)
          href (str "/lesson-builder/" id)] ;; not working for main module [::routes/redirect :activity-sandbox :scene-id id]
      (set! js/document.location href))))

(re-frame/reg-event-fx
  ::play
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id]} (get-activity db)
          href (str "/s/" id)] ;; not working for main module [::routes/redirect :activity-sandbox :scene-id id]
      (set! js/document.location href))))

(re-frame/reg-event-fx
  ::open-activities-page
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :activities]}))
