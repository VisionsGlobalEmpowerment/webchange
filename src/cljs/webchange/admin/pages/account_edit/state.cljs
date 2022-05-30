(ns webchange.admin.pages.account-edit.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.date :refer [date-str->locale-date]]
    [webchange.utils.devices :refer [devices]]))

(def path-to-db :page/edit-account)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; Account Loading

(def account-loading-key :account-loading?)

(defn- set-account-loading
  [db value]
  (assoc db account-loading-key value))

(re-frame/reg-sub
  ::account-loading?
  :<- [path-to-db]
  #(get % account-loading-key false))

;; Account Data

(def account-data-key :account-data)

(defn- get-account-data
  [db]
  (get db account-data-key))

(defn- set-account-data
  [db value]
  (assoc db account-data-key value))

(defn- update-account-data
  [db key handler]
  (update-in db [account-data-key key] handler))

(defn- remove-child
  [db child-id]
  (update-account-data db :children #(filter (fn [{:keys [id]}] (not= id child-id)) %)))

(re-frame/reg-sub
  ::account-data
  :<- [path-to-db]
  #(get-account-data %))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [account-id]}]]
    {:db       (set-account-loading db true)
     :dispatch [::warehouse/load-account {:id account-id}
                {:on-success [::load-account-success]}]}))

(re-frame/reg-event-fx
  ::load-account-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (set-account-loading false)
             (set-account-data data))}))

(defn- prepare-child
  [{:keys [id first-name last-name created-at course data]}]
  {:id          id
   :name        (str first-name " " last-name)
   :created-at  (date-str->locale-date created-at)
   :device      (->> (:device data)
                     (keyword)
                     (get devices))
   :course-name (:name course)})

(re-frame/reg-sub
  ::children
  :<- [::account-data]
  #(->> (get % :children [])
        (map prepare-child)))

;; Child removing

(def child-removing-key :child-removing)

(defn get-child-removing
  [db child-id]
  (get-in db [child-removing-key child-id] false))

(defn set-child-removing
  [db child-id value]
  (assoc-in db [child-removing-key child-id] value))

(re-frame/reg-sub
  ::child-removing?
  :<- [path-to-db]
  (fn [db [_ child-id]]
    (get-child-removing db child-id)))

(re-frame/reg-event-fx
  ::remove-child
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ child-id]]
    {:db       (set-child-removing db child-id true)
     :dispatch [::warehouse/delete-parent-student {:id child-id}
                {:on-success [::remove-child-success child-id]
                 :on-failure [::remove-child-failure child-id]}]}))

(re-frame/reg-event-fx
  ::remove-child-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ child-id]]
    {:db (-> db
             (remove-child child-id)
             (set-child-removing child-id false))}))

(re-frame/reg-event-fx
  ::remove-child-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ child-id]]
    {:db (-> db (set-child-removing child-id false))}))

(re-frame/reg-event-fx
  ::open-accounts-list
  (fn [{:keys []} [_]]
    {:dispatch [::routes/redirect :accounts :account-type "live"]}))
