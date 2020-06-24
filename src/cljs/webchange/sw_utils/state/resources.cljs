(ns webchange.sw-utils.state.resources
  (:require
    [re-frame.core :as re-frame]
    [webchange.sw-utils.state.status :as status]
    [webchange.sw-utils.message :refer [set-cached-data cache-course]]
    [webchange.sw-utils.state.db :refer [path-to-db]]))

(re-frame/reg-fx
  :cache-course
  (fn [[course-id]]
    (cache-course course-id)))

(re-frame/reg-fx
  :set-cached-data
  (fn [data]
    (set-cached-data data)))

(re-frame/reg-event-fx
  ::cache-course
  [(re-frame/inject-cofx :online?)]
  (fn [{:keys [db online?]} [_ course-id]]
    (when-not (status/sync-disabled? db)
      (if online?
        {:cache-course [course-id]
         :dispatch     [::status/set-sync-status :syncing]}
        {:dispatch [::status/set-sync-status :offline]}))))

(defn synced-game-resources
  [db]
  (get-in db (path-to-db [:synced-resources :game]) []))

(re-frame/reg-sub
  ::synced-game-resources
  synced-game-resources)

(re-frame/reg-event-fx
  ::set-synced-game-resources
  (fn [{:keys [db]} [_ resources]]
    {:db (assoc-in db (path-to-db [:synced-resources :game]) resources)}))

(defn synced-game-endpoints
  [db]
  (get-in db (path-to-db [:synced-endpoints :game]) []))

(re-frame/reg-sub
  ::synced-game-endpoints
  synced-game-endpoints)

(re-frame/reg-event-fx
  ::set-synced-game-endpoints
  (fn [{:keys [db]} [_ endpoints]]
    {:db (assoc-in db (path-to-db [:synced-endpoints :game]) endpoints)}))

(re-frame/reg-event-fx
  ::add-game-data
  (fn [{:keys [db]} [_ {:keys [resources endpoints]}]]
    (let [current-course (:current-course db)]
      {:set-cached-data {:course    current-course
                         :resources {:add resources}
                         :endpoints {:add endpoints}}})))

(re-frame/reg-event-fx
  ::remove-game-data
  (fn [{:keys [db]} [_ {:keys [resources endpoints]}]]
    (let [current-course (:current-course db)]
      {:set-cached-data {:course    current-course
                         :resources {:remove resources}
                         :endpoints {:remove endpoints}}})))
