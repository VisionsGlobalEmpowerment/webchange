(ns webchange.lesson-builder.widgets.image-library.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :widget/image-library)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; filter

(def filter-key :filter)

(defn- get-filter
  [db]
  (get db filter-key))

(defn- set-filter
  [db value]
  (assoc db filter-key value))

(defn- update-filter
  [db data-patch]
  (update db filter-key merge data-patch))

;; assets

(def assets-loading-key :assets-loading?)

(defn- set-assets-loading
  [db value]
  (assoc db assets-loading-key value))

(re-frame/reg-sub
  ::assets-loading?
  :<- [path-to-db]
  #(get % assets-loading-key true))

(def assets-key :assets)

(defn- get-assets
  [db]
  (get db assets-key))

(defn- set-assets
  [db value]
  (assoc db assets-key value))

(re-frame/reg-sub
  ::assets
  :<- [path-to-db]
  #(get % assets-key []))

(re-frame/reg-event-fx
  ::load-assets
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [filter (get-filter db)]
      {:db       (-> db (set-assets-loading true))
       :dispatch [::warehouse/search-assets filter
                  {:on-success [::load-assets-success]
                   :on-failure [::load-assets-failure]}]})))

(re-frame/reg-event-fx
  ::load-assets-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ assets]]
    {:db (-> db
             (set-assets-loading false)
             (set-assets assets))}))

(re-frame/reg-event-fx
  ::load-assets-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-assets-loading false))}))

;; events

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [type]}]]
    {:db       (set-filter db {:type type})
     :dispatch [::load-assets]}))

(re-frame/reg-event-fx
  ::update-filter
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ filter-patch]]
    {:db       (update-filter db filter-patch)
     :dispatch [::load-assets]}))
