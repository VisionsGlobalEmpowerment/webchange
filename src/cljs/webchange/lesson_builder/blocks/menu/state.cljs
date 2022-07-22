(ns webchange.lesson-builder.blocks.menu.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]))

(def path-to-db :lesson-builder/menu)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; current state

(def current-state-key :current-state)

(defn- get-current-state
  [db]
  (get db current-state-key {:current-tab nil
                             :components  nil
                             :title       ""}))

(re-frame/reg-sub
  ::current-state
  :<- [path-to-db]
  get-current-state)

(defn- update-current-state
  [db data-patch]
  (update db current-state-key merge data-patch))

(defn- set-current-tab
  [db tab-key]
  (update-current-state db {:current-tab tab-key}))

(defn- set-current-component
  [db component-key]
  (update-current-state db {:components [component-key]}))

(defn- push-current-component
  [db component-key]
  (update-in db [current-state-key :components] concat [component-key]))

;; history

(def history-key :history)

(defn- get-history
  [db]
  (get db history-key []))

(defn- get-history-last
  [db]
  (-> (get-history db)
      (last)))

(defn- pop-history
  [db]
  (update db history-key butlast))

(defn- push-history
  [db item]
  (update db history-key concat [item]))

(re-frame/reg-event-fx
  ::history-back
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [on-back (get db :on-back)
          last-item (get-history-last db)]
      (if (seq on-back)
        (let [callback (last on-back)]
          {:db (update db :on-back drop-last)
           :dispatch callback})
        {:db (-> db
                 (update-current-state last-item)
                 (pop-history))}))))

(re-frame/reg-sub
  ::show-history-back?
  :<- [path-to-db]
  #(-> (get-history %)
       (count)
       (> 0)))

(re-frame/reg-event-fx
  ::on-back
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ callback]]
    {:db (update db :on-back concat [callback])}))

;; tabs

(def menu-tabs {:design       {:title     "Design"
                               :component :design-actions}
                :scene-layers {:title     "Scene Layers"
                               :component :scene-layers}
                :settings     {:title     "Settings"
                               :component :settings}})

(re-frame/reg-sub
  ::menu-tabs
  (fn []
    (map (fn [[id data]]
           (assoc data :id id))
         menu-tabs)))

(re-frame/reg-event-fx
  ::set-current-tab
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ tab-key]]
    (let [tab-component (-> (get menu-tabs tab-key)
                            (get :component))]
      {:db (-> db
               (set-current-tab tab-key)
               (set-current-component tab-component))})))

(re-frame/reg-event-fx
  ::set-current-component
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ component-key]]
    (let [current-state (get-current-state db)]
      {:db (-> db
               (push-history current-state)
               (push-current-component component-key))})))

;; events

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [tab component]}]]
    {:db (-> db
             (set-current-tab tab)
             (set-current-component component)
             (assoc :on-back [])
             (assoc history-key []))}))
