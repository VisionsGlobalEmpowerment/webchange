(ns webchange.admin.pages.dashboard.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.auth.state :as auth]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/dashboard)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; statistic

(def statistics-key :statistics)

(defn- update-statistics
  [db data-patch]
  (update db statistics-key merge data-patch))

(re-frame/reg-sub
  ::statistics
  :<- [path-to-db]
  #(get % statistics-key {}))

(re-frame/reg-sub
  ::current-role
  :<- [::auth/current-user]
  (fn [{:keys [type]}]
    (case type
      "bbs-admin" "admin"
      type)))

(re-frame/reg-sub
  ::current-school
  :<- [::auth/current-user]
  (fn [{:keys [school-id]}]
    school-id))

(re-frame/reg-event-fx
  ::init-admin
  [(i/path path-to-db)]
  (fn [{:keys []} [_]]
    {:dispatch-n [[::warehouse/load-overall-statistics
                   {:on-success [::load-overall-statistics-success]}]]}))

(re-frame/reg-event-fx
  ::load-overall-statistics-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ statistics]]
    {:db (update-statistics db statistics)}))

(re-frame/reg-event-fx
  ::open-accounts-page
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :accounts :account-type "live"]}))

(re-frame/reg-event-fx
  ::open-activities-page
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :activities]}))

(re-frame/reg-event-fx
  ::open-books-page
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :books]}))

(re-frame/reg-event-fx
  ::open-create-book-page
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :book-create]}))

(re-frame/reg-event-fx
  ::open-courses-page
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :courses]}))

(re-frame/reg-event-fx
  ::open-schools-page
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :schools]}))
