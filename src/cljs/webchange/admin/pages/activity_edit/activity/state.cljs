(ns webchange.admin.pages.activity-edit.activity.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.pages.activity-edit.common.state :as common-state]
    [webchange.admin.routes :as routes]
    [webchange.auth.state :as auth]))

(def path-to-db :page/activity-edit)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-sub
  ::activity
  :<- [::common-state/activity-data]
  identity)

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

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ props]]
    {:db       (-> db (assoc form-editable-key false))
     :dispatch [::common-state/init props]}))

(re-frame/reg-event-fx
  ::open-activities-page
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :activities]}))

;; Duplicate
(re-frame/reg-event-fx
  ::handle-duplicated
  [(i/path common-state/path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [id (:duplicated-activity-id db)]
      {:dispatch-n [[::common-state/close-duplicate-window]
                    [::routes/redirect :activity-edit :activity-id id]]})))


