(ns webchange.admin.pages.dashboard.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/dashboard)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; statistic

(def default-statistics (->> [:accounts]
                             (map #(vector % "-"))
                             (into {})))

(def statistics-key :statistics)

(defn- update-statistics
  [db data-patch]
  (update db statistics-key merge data-patch))

(re-frame/reg-sub
  ::statistics
  :<- [path-to-db]
  #(get % statistics-key default-statistics))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:dispatch-n [[::warehouse/load-accounts-by-type {:type "live"}
                   {:on-success [::load-accounts-success]}]
                  [::warehouse/load-available-books
                   {:on-success [::load-books-success]}]]}))

(re-frame/reg-event-fx
  ::load-accounts-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [total]}]]
    {:db (update-statistics db {:accounts total})}))

(re-frame/reg-event-fx
  ::load-books-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ books]]
    {:db (update-statistics db {:books (count books)})}))

(re-frame/reg-event-fx
  ::open-accounts-page
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::routes/redirect :accounts :account-type "live"]}))

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
