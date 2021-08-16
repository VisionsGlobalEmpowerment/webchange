(ns webchange.editor-v2.activity-form.generic.components.add-character.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.state-activity :as state-activity]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:editor-v2 :add-character-modal])))

;; Modal window

(def modal-state-path (path-to-db [:modal-state]))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path true)}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))

(re-frame/reg-sub
  ::open?
  (fn [db]
    (get-in db modal-state-path false)))

;; Skin

(def current-skin-path (path-to-db [:current-skin]))

(defn- get-current-skin
  [db]
  (get-in db current-skin-path))

(re-frame/reg-sub
  ::current-skin
  get-current-skin)

(re-frame/reg-event-fx
  ::set-current-skin
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db current-skin-path value)}))

;; Skeleton

(def current-skeleton-path (path-to-db [:current-skeleton]))

(defn- get-current-skeleton
  [db]
  (get-in db current-skeleton-path))

(re-frame/reg-sub
  ::current-skeleton
  get-current-skeleton)

(re-frame/reg-event-fx
  ::set-current-skeleton
  (fn [{:keys [db]} [_ value]]
    {:db       (assoc-in db current-skeleton-path value)
     :dispatch [::set-current-skin nil]}))

;; Save

(re-frame/reg-event-fx
  ::add-character
  (fn [{:keys [db]} [_]]
    (let [skeleton (get-current-skeleton db)
          skin (get-current-skin db)
          action-data (cond-> {:name skeleton}
                              (string? skin) (assoc :skin skin)
                              (map? skin) (assoc :skin-names skin))]
      {:dispatch [::state-activity/call-activity-common-action
                  {:action :add-character
                   :data   action-data}
                  {:on-success [::close]}]})))
