(ns webchange.editor-v2.components.activity-tracks.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.state :as parent-state]
    [webchange.editor-v2.events :as events]
    [webchange.subs :as subs]
    [webchange.utils.scene-data :as utils]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:activity-tracks])
       (parent-state/path-to-db)))

(defn- meta-track->track-nodes
  ([track scene-data]
   (meta-track->track-nodes track scene-data nil))
  ([track scene-data second-track-name]
   (->> (get track :nodes [])
        (map (fn [{:keys [track-link type] :as node}]
               (case type
                 "dialog" (let [action-name (-> node :action-id keyword)
                                action (utils/get-action scene-data action-name)]
                            (-> node
                                (assoc :action-path [action-name])
                                (assoc :title (or (:phrase-description action) (:phrase action)))))
                 "track" (assoc node :selected? (= track-link second-track-name))
                 node))))))

;; Main track

(re-frame/reg-sub
  ::main-track
  (fn []
    [(re-frame/subscribe [::subs/current-scene-data])
     (re-frame/subscribe [::second-track-name])])
  (fn [[scene-data second-track-name]]
    (-> (utils/get-main-track scene-data)
        (meta-track->track-nodes scene-data second-track-name))))

;; Second track

(def second-track-name-path (path-to-db [:second-track-name]))

(re-frame/reg-sub
  ::second-track-name
  (fn [db]
    (get-in db second-track-name-path)))

(re-frame/reg-sub
  ::second-track-display-name
  (fn []
    [(re-frame/subscribe [::subs/current-scene-data])
     (re-frame/subscribe [::second-track-name])])
  (fn [[scene-data second-track-name]]
    (when (some? second-track-name)
      (-> (utils/get-track scene-data second-track-name)
          (get :title "Untitled track")))))

(re-frame/reg-event-fx
  ::set-second-track-name
  (fn [{:keys [db]} [_ track-name]]
    {:db (assoc-in db second-track-name-path track-name)}))

(re-frame/reg-event-fx
  ::reset-second-track-name
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-second-track-name nil]}))

(re-frame/reg-sub
  ::second-track
  (fn []
    [(re-frame/subscribe [::subs/current-scene-data])
     (re-frame/subscribe [::second-track-name])])
  (fn [[scene-data second-track-name]]
    (when (some? second-track-name)
      (-> (utils/get-track scene-data second-track-name)
          (meta-track->track-nodes scene-data)))))

;; Actions

(re-frame/reg-event-fx
  ::open-dialog-window
  (fn [{:keys [_]} [_ dialog-action-path]]
    {:dispatch [::events/show-dialog-translator-form {:path dialog-action-path}]}))
