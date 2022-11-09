(ns webchange.admin.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]))

(def path-to-db :module/admin)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; previous page

(def previous-page-key :previous-page)

(defn- set-previous-page
  [db value]
  (assoc db previous-page-key value))

(re-frame/reg-sub
  ::previous-page
  :<- [path-to-db]
  #(get % previous-page-key))

;; current page

(def current-page-key :current-page)

(defn- get-current-page
  [db]
  (get db current-page-key))

(defn- set-current-page
  [db value]
  (assoc db current-page-key value))

(re-frame/reg-sub
  ::current-page
  :<- [path-to-db]
  #(get-current-page %))

(re-frame/reg-event-fx
  ::set-current-page
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    (let [current-page (get-current-page db)]
      {:db (-> db
               (set-current-page value)
               (set-previous-page current-page))})))

(re-frame/reg-event-fx
  ::add-current-page-props
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (-> db
             (update-in [current-page-key :props] merge value))}))
