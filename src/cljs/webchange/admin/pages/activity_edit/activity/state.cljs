(ns webchange.admin.pages.activity-edit.activity.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.pages.activity-edit.common.state :as common-state]
    [webchange.admin.routes :as routes]
    [webchange.auth.state :as auth]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/activity-edit)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-sub
  ::activity-loading?
  :<- [::common-state/activity-loading?]
  identity)

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

;;

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ props]]
    {:dispatch [::common-state/init props]}))

;;

(re-frame/reg-event-fx
  ::handle-data-changed
  (fn [{:keys [_]} [_ activity-data-patch]]
    {:dispatch [::common-state/update-activity-data activity-data-patch]}))

(re-frame/reg-event-fx
  ::edit
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id]} (common-state/get-activity db)]
      {:dispatch [::routes/redirect :lesson-builder :activity-id id]})))

(re-frame/reg-event-fx
  ::play
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id]} (common-state/get-activity db)
          href (str "/s/" id)]                              ;; not working for main module [::routes/redirect :activity-sandbox :scene-id id]
      (js/window.open href "_blank"))))

(re-frame/reg-event-fx
  ::open-activities-page
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :activities]}))

(re-frame/reg-sub
  ::activity-ui-locked?
  :<- [::auth/super-admin?]
  :<- [::common-state/activity-locked?]
  (fn [[super-admin? activity-locked?]]
    (and activity-locked? (not super-admin?))))

;;

(re-frame/reg-event-fx
  ::duplicate
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [id name lang]} (common-state/get-activity db)] ;;<< TODO
      {:db       (-> db (common-state/set-activity-loading true))
       :dispatch [::warehouse/duplicate-activity
                  {:activity-id id
                   :data        {:name name
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
    {:db (-> db (common-state/set-activity-loading false))}))
