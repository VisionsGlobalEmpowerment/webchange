(ns webchange.lesson-builder.tools.script.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.state :as state]
    [webchange.utils.scene-data :as utils]))

(def path-to-db :lesson-builder/script)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; current track

(def current-track-key :current-track)

(re-frame/reg-sub
  ::current-track
  :<- [path-to-db]
  #(get % current-track-key 0))

(re-frame/reg-event-fx
  ::set-current-track
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db current-track-key value)}))

(re-frame/reg-sub
  ::show-track-selector?
  :<- [::state/flipbook?]
  not)

;; selected action

(def selected-action-key :selected-action)

(re-frame/reg-sub
  ::selected-action
  :<- [path-to-db]
  #(get % selected-action-key))

(re-frame/reg-event-fx
  ::set-selected-action
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    {:db (assoc db selected-action-key value)}))

;; dialogs

(defn collect-untracked-actions
  [activity-data]
  (let [all-actions (->> (utils/get-dialog-actions activity-data)
                         (map clojure.core/name))
        tracked-actions (->> (utils/get-tracks activity-data)
                             (map :nodes)
                             (flatten)
                             (filter #(= (:type %) "dialog"))
                             (map :action-id))]
    (->> (clojure.set/difference (set all-actions)
                                 (set tracked-actions))
         (vec))))

(re-frame/reg-sub
  ::track-dialogs
  (fn []
    [(re-frame/subscribe [::current-track])
     (re-frame/subscribe [::state/activity-data])])
  (fn [[current-track-idx activity-data]]
    (let [current-track (utils/get-track-by-index activity-data current-track-idx)
          untracked-actions (collect-untracked-actions activity-data)]
      (if (some? current-track)
        (->> (:nodes current-track)
             (filter (fn [{:keys [type]}]
                       (= type "dialog")))
             (map (fn [{:keys [action-id]}]
                    {:id          action-id
                     :action-path [(keyword action-id)]})))
        (map (fn [action-name]
               {:id          action-name
                :action-path [(keyword action-name)]})
             untracked-actions)))))
