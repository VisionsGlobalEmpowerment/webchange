(ns webchange.editor-v2.components.activity-tracks.state
  (:require
    [clojure.set :refer [difference]]
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

(defn- equal
  [value1 value2]
  (and (some? value1)
       (some? value2)
       (= value1 value2)))

(defn- get-track-by-data
  [scene-data track-data]
  (or (utils/get-track-by-id scene-data (:id track-data))
      (utils/get-track-by-index scene-data (:idx track-data))))

;; ---

(defn- meta-track->track-nodes
  ([track scene-data]
   (meta-track->track-nodes track scene-data nil))
  ([track scene-data second-track-data]
   (->> (get track :nodes [])
        (map (fn [{:keys [track-id track-idx type] :as node}]
               (case type
                 "dialog" (let [action-name (-> node :action-id keyword)
                                action (utils/get-action scene-data action-name)]
                            (-> node
                                (assoc :action-path [action-name])
                                (assoc :title (or (:phrase-description action) (:phrase action)))))
                 "track" (-> node
                             (assoc :selected? (or (equal track-id (:id second-track-data))
                                                   (equal track-idx (:idx second-track-data))))
                             (assoc :title (-> (get-track-by-data scene-data {:id track-id :idx track-idx})
                                               (get :title "Untitled Track"))))
                 node))))))

;; Main track

(defn- generate-main-track
  [scene-data]
  {:title "Main Track"
   :nodes (->> (utils/get-tracks scene-data)
               (map-indexed (fn [idx _]
                              {:type      "track"
                               :track-idx idx}))
               (vec))})

(re-frame/reg-sub
  ::main-track
  (fn []
    [(re-frame/subscribe [::subs/current-scene-data])
     (re-frame/subscribe [::second-track-data])])
  (fn [[scene-data second-track-data]]
    (-> (or (utils/get-main-track scene-data)
            (generate-main-track scene-data))
        (meta-track->track-nodes scene-data second-track-data))))

;; Second track

(def second-track-data-path (path-to-db [:second-track-name]))

(re-frame/reg-sub
  ::second-track-data
  (fn [db]
    (get-in db second-track-data-path)))

(re-frame/reg-sub
  ::second-track-display-name
  (fn []
    [(re-frame/subscribe [::subs/current-scene-data])
     (re-frame/subscribe [::second-track-data])])
  (fn [[scene-data second-track-data]]
    (when (some? second-track-data)
      (-> (get-track-by-data scene-data second-track-data)
          (get :title "Untitled track")))))

(re-frame/reg-event-fx
  ::set-second-track-data
  (fn [{:keys [db]} [_ track-name]]
    {:db (assoc-in db second-track-data-path track-name)}))

(re-frame/reg-sub
  ::second-track
  (fn []
    [(re-frame/subscribe [::subs/current-scene-data])
     (re-frame/subscribe [::second-track-data])])
  (fn [[scene-data second-track-data]]
    (when (some? second-track-data)
      (-> (get-track-by-data scene-data second-track-data)
          (meta-track->track-nodes scene-data)))))

;; Untracked Actions

(defn- collect-untracked-actions
  [scene-data]
  (let [all-actions (->> (utils/get-dialog-actions scene-data)
                         (map clojure.core/name))
        tracked-actions (->> (utils/get-tracks scene-data)
                             (map :nodes)
                             (flatten)
                             (filter #(= (:type %) "dialog"))
                             (map :action-id))]
    (->> (difference (set all-actions)
                     (set tracked-actions))
         (vec))))

(re-frame/reg-sub
  ::untracked-actions
  (fn []
    (re-frame/subscribe [::subs/current-scene-data]))
  (fn [scene-data]
    (let [untracked-actions (collect-untracked-actions scene-data)]
      (when-not (empty? untracked-actions)
        (-> {:nodes (map (fn [action-name]
                           {:type      "dialog"
                            :action-id action-name})
                         untracked-actions)}
            (meta-track->track-nodes scene-data))))))

;; Actions

(re-frame/reg-event-fx
  ::open-dialog-window
  (fn [{:keys [_]} [_ dialog-action-path]]
    {:dispatch [::events/show-dialog-translator-form {:path dialog-action-path}]}))
