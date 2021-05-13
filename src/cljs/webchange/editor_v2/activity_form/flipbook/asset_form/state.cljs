(ns webchange.editor-v2.activity-form.flipbook.asset-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.flipbook.state :as state-book-creator]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.logger.index :as logger]
    [webchange.state.state :as state]
    [webchange.state.state-flipbook :as state-flipbook]
    [webchange.utils.map :refer [ignore-keys]]))

(defn path-to-db
  ([id]
   (path-to-db id []))
  ([id relative-path]
   (->> relative-path
        (concat [:page-page-asset-control id])
        (state-book-creator/path-to-db))))

;; Asset Name

(def assets-names-path [:assets-names])

(defn- get-assets-names
  [db id]
  {:pre [(some? id)]}
  (get-in db (path-to-db id assets-names-path)))

(defn- set-assets-names
  [db id names]
  {:pre [(some? id)]}
  (assoc-in db (path-to-db id assets-names-path) names))

;; Initial Data

(def initial-data-path :initial-data)

(defn- get-initial-data
  [db id]
  (get-in db (path-to-db id [initial-data-path]) {}))

(re-frame/reg-sub
  ::initial-data
  (fn [db [_ id]]
    {:pre [(some? id)]}
    (get-initial-data db id)))

(defn- set-initial-data
  [db id data]
  (assoc-in db (path-to-db id [initial-data-path]) data))

(defn- reset-initial-data
  [db id]
  (update-in db (path-to-db id []) dissoc initial-data-path))

;; Current Data

(def current-data-path :current-data)

(defn- get-current-data
  [db id]
  (get-in db (path-to-db id [current-data-path]) {}))

(re-frame/reg-sub
  ::current-data
  (fn [db [_ id]]
    {:pre [(some? id)]}
    (get-current-data db id)))

(re-frame/reg-event-fx
  ::set-current-data
  (fn [{:keys [db]} [_ id data]]
    {:db (assoc-in db (path-to-db id [current-data-path]) data)}))

(re-frame/reg-event-fx
  ::update-current-data
  (fn [{:keys [db]} [_ id data]]

    (logger/group-folded "Update current data" id)
    (logger/trace "data" data)
    (logger/group-end "Update current data" id)

    {:db       (update-in db (path-to-db id [current-data-path]) merge data)
     :dispatch [::update-stage-objects id data]}))

(defn- reset-current-data
  [db id]
  (update-in db (path-to-db id []) dissoc current-data-path))

;; Update stage

(re-frame/reg-event-fx
  ::update-stage-objects
  (fn [{:keys [db]} [_ id data]]
    (let [data-to-set (ignore-keys data [:chunks])
          assets-names (get-assets-names db id)]

      (logger/group-folded "Update stage objects" id)
      (logger/trace "assets-names" assets-names)
      (logger/trace "data" data)
      (logger/group-end "Update stage objects" id)

      {:dispatch-n (map (fn [asset-name]
                          [::state-renderer/set-scene-object-state asset-name data-to-set])
                        assets-names)})))

;; Loading status

(def loading-status-path [:loading-status])

(defn- get-loading-status
  [db id]
  (get-in db (path-to-db id loading-status-path) :done))

(re-frame/reg-sub
  ::loading-status
  (fn [db [_ id]]
    (get-loading-status db id)))

(re-frame/reg-event-fx
  ::set-loading-status
  (fn [{:keys [db]} [_ id status]]
    {:db (assoc-in db (path-to-db id loading-status-path) status)}))

;; Form States

(re-frame/reg-sub
  ::has-changes?
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::current-data id])
     (re-frame/subscribe [::initial-data id])])
  (fn [[current-data initial-data]]
    (not= current-data initial-data)))

(re-frame/reg-sub
  ::loading?
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::loading-status id])])
  (fn [[loading-status]]
    (= loading-status :loading)))

(re-frame/reg-sub
  ::disabled?
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::loading? id])
     (re-frame/subscribe [::has-changes? id])])
  (fn [[loading? has-changes?]]
    (or loading? (not has-changes?))))

;; Init

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_ id {:keys [assets-names data]}]]

    (logger/group-folded "Init asset form" id)
    (logger/trace "assets-names" assets-names)
    (logger/trace "data" data)
    (logger/group-end "Init asset form" id)

    {:db       (-> db
                   (set-assets-names id assets-names)
                   (set-initial-data id data))
     :dispatch [::set-current-data id data]}))

;; Save

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_ id {:keys [reset?] :or {reset? false}}]]
    (let [assets-names (get-assets-names db id)
          current-data (get-current-data db id)]
      {:dispatch-n (cond-> [[::set-loading-status id :loading]
                            [::state/update-scene-objects {:patches-list (map (fn [asset-name]
                                                                                {:object-name       asset-name
                                                                                 :object-data-patch current-data})
                                                                              assets-names)}
                             {:on-success [::save-success id current-data]}]]
                           reset? (conj [::reset id {:reset-stage-object? false}]))})))

(re-frame/reg-event-fx
  ::save-success
  (fn [{:keys [db]} [_ id data]]
    {:db         (set-initial-data db id data)
     :dispatch-n [[::set-loading-status id :done]
                  [::state-flipbook/generate-stages-screenshots]]}))

;; Reset

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_ id {:keys [reset-stage-object?] :or {reset-stage-object? true}}]]
    (let [initial-data (get-initial-data db id)]

      (logger/group-folded "Reset asset form" id)
      (logger/trace "reset-stage-object?" reset-stage-object?)
      (logger/trace "initial-data" initial-data)
      (logger/group-end "Reset asset form" id)

      (cond-> {:db (-> db
                       (reset-initial-data id)
                       (reset-current-data id))}
              reset-stage-object? (assoc :dispatch [::update-stage-objects id initial-data])))))
