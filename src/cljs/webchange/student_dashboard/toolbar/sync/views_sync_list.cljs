(ns webchange.student-dashboard.toolbar.sync.views-sync-list
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [clojure.set :refer [difference union]]
    [webchange.student-dashboard.toolbar.sync.events :as events]
    [webchange.student-dashboard.toolbar.sync.subs :as subs]
    [webchange.sw-utils.message :as sw]
    [webchange.sw-utils.subs :as sw-subs]
    [webchange.student-dashboard.toolbar.sync.views-sync-list-present :refer [sync-list-modal-view]]
    [webchange.subs :as global-subs]))

;; Get initial state

(defn- get-entity-loaded-stat
  [activity-resources loaded-resources]
  (let [not-loaded-count (-> (difference (set activity-resources)
                                         (set loaded-resources))
                             (count))]
    (cond
      (= not-loaded-count 0) :loaded
      (= not-loaded-count (count activity-resources)) :not-loaded
      :else :part-loaded)))

(defn- get-level-sync-state
  [activities]
  (cond
    (every? #(= :loaded (:sync-stat %)) activities) :loaded
    (some #(= :loaded (:sync-stat %)) activities) :part-loaded
    :else :not-loaded))

(defn- get-activities-data
  [activities loaded-resources]
  (->> activities
       (map (fn [{:keys [resources] :as activity}]
              (assoc activity :sync-stat (get-entity-loaded-stat resources loaded-resources))))
       (vec)))

(defn- get-levels-data
  [levels loaded-resources]
  (->> levels
       (map (fn [{:keys [activities] :as level}]
              (let [updated-activities (get-activities-data activities loaded-resources)
                    sync-stat (get-level-sync-state updated-activities)]
                (-> level
                    (assoc :activities updated-activities)
                    (assoc :sync-stat sync-stat)))))
       (vec)))

;; Update current state

(defn- get-level-scenes
  [levels level-id]
  (->> levels
       (some (fn [{:keys [id] :as level}]
               (and (= id level-id)
                    level)))
       (:activities)
       (map :id)))

(defn- find-index-by
  [list predicate]
  (->> list
       (map-indexed vector)
       (filter #(predicate (second %)))
       (map first)
       (first)))

(defn- perform-on-level
  [data level-id func]
  (let [level-path (find-index-by @data #(= level-id (:id %)))]
    (func {:data       data
           :level-path [level-path]})))

(defn- perform-on-scene
  [data level-id scene-id func]
  (perform-on-level data level-id
                    (fn [{:keys [data level-path]}]
                      (let [scenes (get-in @data (conj level-path :activities))
                            scene-path (conj level-path
                                             :activities
                                             (find-index-by scenes #(= scene-id (:id %))))]
                        (func {:data       data
                               :scene-path scene-path})))))

(defn- update-level-sync-state!
  [data level-id]
  (perform-on-level data level-id
                    (fn [{:keys [data level-path]}]
                      (let [path (conj level-path :sync-stat)
                            sync-state (get-level-sync-state (get-in @data (conj level-path :activities)))]
                        (swap! data assoc-in path sync-state)))))

(defn- add-scenes-selection!
  [data level-id scenes-ids]
  (doseq [scene-id scenes-ids]
    (perform-on-scene data level-id scene-id
                      (fn [{:keys [data scene-path]}]
                        (swap! data assoc-in (conj scene-path :sync-stat) :loaded))))
  (update-level-sync-state! data level-id))

(defn- remove-scenes-selection!
  [data level-id scenes-ids]
  (doseq [scene-id scenes-ids]
    (perform-on-scene data level-id scene-id
                      (fn [{:keys [data scene-path]}]
                        (swap! data assoc-in (conj scene-path :sync-stat) :not-loaded))))
  (update-level-sync-state! data level-id))

;; Get update-info

(defn- get-selected-scenes-data-list
  [levels getter]
  (->> levels
       (map (fn [{:keys [activities]}]
              (->> activities
                   (filter (fn [{:keys [sync-stat]}] (= sync-stat :loaded)))
                   (map getter))))
       (flatten)
       (distinct)
       (vec)))

(defn- get-selected-resources
  [levels]
  (get-selected-scenes-data-list levels :resources))

(defn- get-selected-endpoints
  [levels]
  (get-selected-scenes-data-list levels :endpoint))

(defn- get-lists-diff
  [list-1 list-2]
  (-> (difference (set list-1)
                  (set list-2))
      (vec)))

(defn- get-resources-to-add
  [synced-resources selected-resources]
  (get-lists-diff selected-resources synced-resources))

(defn- get-resources-to-remove
  [synced-resources selected-resources]
  (get-lists-diff synced-resources selected-resources))

;;

(def data (r/atom []))

(defn- component-did-mount
  [this]
  (let [{:keys [scenes-data synced-game-resources course]} (r/props this)]
    (reset! data (get-levels-data scenes-data synced-game-resources))
    (re-frame/dispatch [::events/load-scenes])
    (sw/get-cached-resources course)))

(defn- component-did-update
  [this [_ prev-props]]
  (let [prev-scenes-data (:scenes-data prev-props)
        {:keys [scenes-data synced-game-resources]} (r/props this)]
    (when (nil? prev-scenes-data)
      (reset! data (get-levels-data scenes-data synced-game-resources)))))

(defn- sync-list-modal-render
  [{:keys [course scenes-loading synced-game-resources window-opened?]}]
  (let [handle-list-item-click (fn [action level-id scene-id]
                                 (let [selected-scenes (if (nil? scene-id)
                                                         (get-level-scenes @data level-id)
                                                         [scene-id])]
                                   (case action
                                     :add (add-scenes-selection! data level-id selected-scenes)
                                     :remove (remove-scenes-selection! data level-id selected-scenes))))
        handle-save (fn []
                      (let [selected-resources (get-selected-resources @data)]
                        (sw/set-cached-scenes {:course    course
                                               :scenes    {:add (get-selected-endpoints @data)}
                                               :resources {:add    (get-resources-to-add synced-game-resources selected-resources)
                                                           :remove (get-resources-to-remove synced-game-resources selected-resources)}})
                        (re-frame/dispatch [::events/close-sync-list])))
        handle-close #(re-frame/dispatch [::events/close-sync-list])]
    [sync-list-modal-view {:data          @data
                           :open?         window-opened?
                           :loading?      scenes-loading
                           :on-close      handle-close
                           :on-save       handle-save
                           :on-item-click handle-list-item-click}]))

(def sync-list-modal-wrapper
  (with-meta sync-list-modal-render
             {:component-did-mount  component-did-mount
              :component-did-update component-did-update}))

(defn sync-list-modal
  []
  (let [current-course @(re-frame/subscribe [::global-subs/current-course])
        scenes-data @(re-frame/subscribe [::subs/scenes-data])
        scenes-loading @(re-frame/subscribe [::subs/scenes-loading])
        synced-game-resources @(re-frame/subscribe [::sw-subs/get-synced-game-resources])
        window-opened? @(re-frame/subscribe [::subs/list-open])]
    [sync-list-modal-wrapper {:course                current-course
                              :scenes-data           scenes-data
                              :scenes-loading        scenes-loading
                              :synced-game-resources synced-game-resources
                              :window-opened?        window-opened?}]))

