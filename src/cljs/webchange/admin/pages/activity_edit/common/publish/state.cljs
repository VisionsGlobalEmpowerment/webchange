(ns webchange.admin.pages.activity-edit.common.publish.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.pages.activity-edit.common.state :as common-state]
    [webchange.auth.state :as auth]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/activity-edit--common-publish)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; lock

(re-frame/reg-sub
  ::lock-control-state
  :<- [::auth/bbs-admin?]
  :<- [::auth/super-admin?]
  :<- [::common-state/activity-locked?]
  (fn [[bbs-admin? super-admin? activity-locked?]]
    {:disabled?  false
     :read-only? (and bbs-admin?
                      activity-locked?)
     :visible?   (or super-admin?
                     activity-locked?)}))

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
  (fn [{:keys [db]} [_ activity-id value]]
    {:db       (-> db (set-lock-loading true))
     :dispatch [::warehouse/toggle-activity-locked
                {:activity-id activity-id
                 :locked      value}
                {:on-success [::set-locked-success value]
                 :on-failure [::set-locked-failure]}]}))

(re-frame/reg-event-fx
  ::set-locked-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db       (-> db (set-lock-loading false))
     :dispatch [::common-state/update-activity-data {:metadata {:locked value}}]}))

(re-frame/reg-event-fx
  ::set-locked-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-lock-loading false))}))

;; publish

(re-frame/reg-sub
  ::publish-control-state
  :<- [::auth/bbs-admin?]
  :<- [::auth/super-admin?]
  :<- [::common-state/activity-locked?]
  (fn [[bbs-admin? super-admin? activity-locked?]]
    {:disabled?  activity-locked?
     :read-only? (and bbs-admin?
                      activity-locked?)
     :visible?   (or bbs-admin? super-admin?)}))

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
  (fn [{:keys [db]} [_ activity-id value]]
    {:db       (-> db (set-publish-loading true))
     :dispatch [::warehouse/toggle-activity-visibility
                {:activity-id activity-id
                 :visible     value}
                {:on-success [::set-published-success]
                 :on-failure [::set-published-failure]}]}))

(re-frame/reg-event-fx
  ::set-published-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ updated-activity]]
    {:db       (-> db (set-publish-loading false))
     :dispatch [::common-state/update-activity-data updated-activity]}))

(re-frame/reg-event-fx
  ::set-published-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-publish-loading false))}))
