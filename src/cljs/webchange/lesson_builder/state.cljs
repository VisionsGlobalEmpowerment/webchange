(ns webchange.lesson-builder.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :lesson-builder/index)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; activity data

(def activity-loading-key :activity-loading?)

(defn- set-activity-loading
  [db value]
  (assoc db activity-loading-key value))

(re-frame/reg-sub
  ::activity-loading?
  :<- [path-to-db]
  #(get % activity-loading-key true))

(def activity-data-key :activity-data)

(defn get-activity-data
  ([db]
   (get db activity-data-key))
  ([db path-to-db]
   (get-in db [path-to-db activity-data-key])))

(defn- set-activity-data
  [db value]
  (assoc db activity-data-key value))

(re-frame/reg-sub
  ::activity-data
  :<- [path-to-db]
  #(get-activity-data %))

(re-frame/reg-cofx
  :activity-data
  (fn [{:keys [db] :as co-effects}]
    (->> (get-activity-data db path-to-db)
         (assoc co-effects :activity-data))))

(re-frame/reg-event-fx
  ::set-activity-data
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ activity-data]]
    {:db (-> db (set-activity-data activity-data))}))

;; activity info

(def activity-info-loading-key :activity-info-loading?)

(defn- set-activity-info-loading
  [db value]
  (assoc db activity-info-loading-key value))

(re-frame/reg-sub
  ::activity-info-loading?
  :<- [path-to-db]
  #(get % activity-info-loading-key true))

(def activity-info-key :activity-info)

(defn get-activity-info
  ([db]
   (get db activity-info-key))
  ([db path-to-db]
   (get-in db [path-to-db activity-info-key])))

(defn- set-activity-info
  [db value]
  (assoc db activity-info-key value))

(re-frame/reg-sub
  ::activity-info
  :<- [path-to-db]
  #(get-activity-info %))

;; activity versions

(def activity-versions-loading-key :activity-versions-loading?)

(defn- set-activity-versions-loading
  [db value]
  (assoc db activity-versions-loading-key value))

(re-frame/reg-sub
  ::activity-versions-loading?
  :<- [path-to-db]
  #(get % activity-versions-loading-key false))

(def activity-versions-key :activity-versions)

(defn- set-activity-versions
  [db value]
  (assoc db activity-versions-key value))

(re-frame/reg-sub
  ::activity-versions
  :<- [path-to-db]
  #(get % activity-versions-key []))

(re-frame/reg-event-fx
  ::load-versions
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [course-slug slug]} (get-activity-info db)]
      {:db       (-> db (set-activity-versions-loading true))
       :dispatch [::warehouse/load-versions
                  {:course-slug course-slug
                   :scene-slug  slug}
                  {:on-success [::load-versions-success]
                   :on-failure [::load-versions-failure]}]})))

(re-frame/reg-event-fx
  ::load-versions-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [versions]}]]
    {:db (-> db
             (set-activity-versions versions)
             (set-activity-versions-loading false))}))

(re-frame/reg-event-fx
  ::load-versions-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-versions-loading false))}))

;; general

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [activity-id]}]]
    {:db         (-> db
                     (set-activity-loading true)
                     (set-activity-info-loading true))
     :dispatch-n [[::warehouse/load-activity-current-version
                   {:activity-id activity-id}
                   {:on-success [::load-activity-success]
                    :on-failure [::load-activity-failure]}]
                  [::warehouse/load-activity
                   {:activity-id activity-id}
                   {:on-success [::load-activity-info-success]
                    :on-failure [::load-activity-info-failure]}]]}))

(re-frame/reg-event-fx
  ::load-activity-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ activity-data]]
    {:db (-> db
             (set-activity-loading false)
             (set-activity-data activity-data))}))

(re-frame/reg-event-fx
  ::load-activity-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-loading false))}))

(re-frame/reg-event-fx
  ::load-activity-info-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ activity-info]]
    {:db       (-> db
                   (set-activity-info-loading false)
                   (set-activity-info activity-info))
     :dispatch [::load-versions]}))

(re-frame/reg-event-fx
  ::load-activity-info-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-info-loading false))}))

(re-frame/reg-sub
  ::loading?
  :<- [::activity-loading?]
  :<- [::activity-info-loading?]
  (fn [[activity-loading? activity-info-loading?]]
    (or activity-loading? activity-info-loading?)))

;; save

(def activity-saving-key :activity-saving?)

(defn- set-activity-saving
  [db value]
  (assoc db activity-saving-key value))

(re-frame/reg-sub
  ::activity-saving?
  :<- [path-to-db]
  #(get % activity-saving-key false))

(re-frame/reg-event-fx
  ::save-activity
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [{:keys [slug course-slug]} (get-activity-info db)
          data (get-activity-data db)]
      {:db       (-> db (set-activity-saving true))
       :dispatch [::warehouse/save-scene
                  {:course-slug course-slug
                   :scene-slug  slug
                   :scene-data  data}
                  {:on-success [::save-activity-success]
                   :on-failure [::save-activity-failure]}]})))

(re-frame/reg-event-fx
  ::save-activity-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (-> db
                   (set-activity-saving false)
                   (set-activity-versions-loading true))
     :dispatch [::load-versions]}))

(re-frame/reg-event-fx
  ::save-activity-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-activity-saving false))}))
