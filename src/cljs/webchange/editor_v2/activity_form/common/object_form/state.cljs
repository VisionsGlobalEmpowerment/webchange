(ns webchange.editor-v2.activity-form.common.object-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.state :as state-parent]
    [webchange.interpreter.renderer.state.scene :as state-renderer]
    [webchange.logger.index :as logger]
    [webchange.state.state :as state]
    [webchange.utils.deep-merge :refer [deep-merge]]
    [webchange.utils.map :refer [ignore-keys]]))

(defn path-to-db
  [id relative-path]
  (->> relative-path
       (concat [:object-form id])
       (state-parent/path-to-db)))

;; Editor selected objects

(re-frame/reg-sub
  ::selected-objects
  (fn [db]
    (state-parent/get-selected-objects db)))

(re-frame/reg-sub
  ::show-edit-menu?
  (fn []
    [(re-frame/subscribe [::selected-objects])])
  (fn [[{:keys [data]}]]
    (some? data)))

;;

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ id {:keys [data names form-params]}]]
    {:dispatch-n [[::set-selected-objects-names id names]
                  [::set-initial-data id data]
                  [::set-current-data id data]
                  [::set-form-params id form-params]]}))

;; Form params

(def form-params-path :form-params)

(re-frame/reg-event-fx
  ::set-form-params
  (fn [{:keys [db]} [_ id form-params]]
    {:db (assoc-in db (path-to-db id [form-params-path]) (or form-params {}))}))

(re-frame/reg-sub
  ::form-component-available?
  (fn [db [_ id component-key]]
    {:pre [(some? id)]}
    (let [form-params (get-in db (path-to-db id [form-params-path]))]
      (cond
        (nil? form-params) false
        (empty? form-params) true
        :else (get form-params component-key false)))))

;; Selected Objects

(def selected-objects-names-path :selected-objects-names)

(re-frame/reg-event-fx
  ::set-selected-objects-names
  (fn [{:keys [db]} [_ id names]]
    {:db (assoc-in db (path-to-db id [selected-objects-names-path]) names)}))

(defn get-selected-objects-names
  [db id]
  (get-in db (path-to-db id [selected-objects-names-path])))

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

(re-frame/reg-event-fx
  ::set-initial-data
  (fn [{:keys [db]} [_ id data]]
    {:db (assoc-in db (path-to-db id [initial-data-path]) data)}))

(re-frame/reg-event-fx
  ::reset-initial-data
  (fn [{:keys [db]} [_ id]]
    {:db (update-in db (path-to-db id []) dissoc initial-data-path)}))

;; Current Data

(def current-data-path :current-data)

(defn get-current-data
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
  ::reset-current-data
  (fn [{:keys [db]} [_ id]]
    {:db (update-in db (path-to-db id []) dissoc current-data-path)}))

(re-frame/reg-event-fx
  ::update-current-data
  (fn [{:keys [db]} [_ id data]]

    (logger/group-folded "Update current data" id)
    (logger/trace "data" data)
    (logger/group-end "Update current data" id)

    {:db       (update-in db (path-to-db id [current-data-path]) merge data)
     :dispatch [::update-stage-objects id data]}))

;; Update stage

(re-frame/reg-event-fx
  ::update-stage-objects
  (fn [{:keys [db]} [_ id data]]
    (let [data-to-set (ignore-keys data [:chunks])
          objects-names (get-selected-objects-names db id)]
      (logger/group-folded "Update stage objects" id)
      (logger/trace "objects-names" objects-names)
      (logger/trace "data" data)
      (logger/group-end "Update stage objects" id)

      {:dispatch-n (map (fn [object-name]
                          [::state-renderer/set-scene-object-state object-name data-to-set])
                        objects-names)})))

;; Loading Status

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
  ::loading?
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::loading-status id])])
  (fn [[loading-status]]
    (= loading-status :loading)))

(re-frame/reg-sub
  ::has-changes?
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::current-data id])
     (re-frame/subscribe [::initial-data id])])
  (fn [[current-data initial-data]]
    (not= current-data initial-data)))

(re-frame/reg-sub
  ::disabled?
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::loading? id])
     (re-frame/subscribe [::has-changes? id])])
  (fn [[loading? has-changes?]]
    (or loading? (not has-changes?))))

;; Save

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_ id {:keys [reset?] :or {reset? false}}]]
    (let [saving-forms-ids (if (sequential? id) id [id])
          patches-list (reduce (fn [patches-list form-id]
                                 (let [objects-names (get-selected-objects-names db form-id)
                                       current-data (get-current-data db form-id)]
                                   (->> objects-names
                                        (map (fn [object-name]
                                               {:object-name       object-name
                                                :object-data-patch current-data}))
                                        (concat patches-list))))
                               []
                               saving-forms-ids)
          forms-data (map (fn [form-id]
                            {:id   form-id
                             :data (get-current-data db form-id)})
                          saving-forms-ids)]
      {:dispatch-n (cond-> [[::state/update-scene-objects {:patches-list patches-list}
                             {:on-success [::save-success forms-data]}]]
                           :always (concat (map (fn [form-id]
                                                  [::set-loading-status form-id :loading])
                                                saving-forms-ids))
                           reset? (concat (map (fn [form-id]
                                                 [::reset form-id {:reset-stage-object? false}])
                                               saving-forms-ids)))})))

(re-frame/reg-event-fx
  ::save-success
  (fn [{:keys [_]} [_ forms-data]]
    {:dispatch-n (reduce (fn [result {:keys [id data]}]
                           (->> [[::set-initial-data id data]
                                 [::set-loading-status id :done]]
                                (concat result)))
                         []
                         forms-data)}))

;; Reset

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_ id {:keys [reset-stage-object?] :or {reset-stage-object? true}}]]
    (let [initial-data (get-initial-data db id)]

      (logger/group-folded "Reset asset form" id)
      (logger/trace "reset-stage-object?" reset-stage-object?)
      (logger/trace "initial-data" initial-data)
      (logger/group-end "Reset asset form" id)

      {:dispatch-n (cond-> [[::reset-initial-data id]
                            [::reset-current-data id]]
                           reset-stage-object? (conj [::update-stage-objects id initial-data]))})))
