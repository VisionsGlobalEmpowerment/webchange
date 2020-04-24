(ns webchange.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::name
  (fn [db]
    (:name db)))

(re-frame/reg-sub
  ::viewport
  (fn [db]
    (:viewport db)))

(re-frame/reg-sub
  ::course-scenes
  (fn [db]
    (-> db :course-data :scenes)))

(re-frame/reg-sub
  ::course-levels
  (fn [db]
    (-> db :course-data :levels)))

(defn current-scene
  [db]
  (:current-scene db))

(re-frame/reg-sub
  ::current-scene
  current-scene)

(re-frame/reg-sub
  ::current-course
  (fn [db]
    (:current-course db)))

(re-frame/reg-sub
  ::course-data
  (fn [db]
    (:course-data db)))

(re-frame/reg-sub
  ::scene
  (fn [db [_ scene-id]]
    (get-in db [:scenes scene-id] {})))

(re-frame/reg-sub
  ::scene-objects
  (fn [db [_ scene-id]]
    (get-in db [:scenes scene-id :scene-objects] [])))

(defn current-scene-data
  [db]
  (let [current-scene-id (current-scene db)]
    (get-in db [:scenes current-scene-id] {})))

(re-frame/reg-sub
  ::current-scene-data
  current-scene-data)

(re-frame/reg-sub
  ::current-scene-objects
  (fn [db [_]]
    (let [current-scene-id (current-scene db)]
      (get-in db [:scenes current-scene-id :objects] {}))))

(re-frame/reg-sub
  ::scene-actions
  (fn [db [_ scene-id]]
    (get-in db [:scenes scene-id :actions] [])))

(re-frame/reg-sub
  ::scene-triggers
  (fn [db [_ scene-id]]
    (get-in db [:scenes scene-id :triggers] [])))

(re-frame/reg-sub
  ::current-scene-data-objects
  (fn [db]
    (get-in db [:current-scene-data :scene-objects] [])))

(re-frame/reg-sub
  ::scene-object
  (fn [db [_ scene-id name]]
    (get-in db [:scenes scene-id :objects (keyword name)] {})))

(re-frame/reg-sub
  ::scene-object-with-var
  (fn [db [_ scene-id name]]
    (let [object (get-in db [:scenes scene-id :objects (keyword name)] {})
          var-name (:var-name object)]
      (if var-name
        (assoc object :var (get-in db [:scenes scene-id :variables var-name]))
        object))))

(re-frame/reg-sub
  ::scene-action
  (fn [db [_ scene-id name]]
    (get-in db [:scenes scene-id :actions (keyword name)] {})))

(re-frame/reg-sub
  ::scene-asset
  (fn [db [_ scene-id id]]
    (get-in db [:scenes scene-id :assets id] {})))

(defn scene-assets
  [db scene-id]
  (get-in db [:scenes scene-id :assets] {}))

(re-frame/reg-sub
  ::scene-assets
  (fn [db [_ scene-id]]
    (scene-assets db scene-id)))

(re-frame/reg-sub
  ::current-scene-object
  (fn [db [_ name]]
    (get-in db [:current-scene-data :objects (keyword name)] {})))

(re-frame/reg-sub
  ::scene-loading-progress
  (fn [db [_ scene-id]]
    (get-in db [:scene-loading-progress scene-id] 0)))

(re-frame/reg-sub
  ::scene-loading-complete
  (fn [db [_ scene-id]]
    (get-in db [:scene-loading-complete scene-id] false)))

(re-frame/reg-sub
  ::playing
  (fn [db]
    (:playing db)))

(re-frame/reg-sub
  ::scene-started
  (fn [db]
    (:scene-started db)))

(re-frame/reg-sub
  ::ui-screen
  (fn [db]
    (:ui-screen db)))

(re-frame/reg-sub
  ::get-music-volume
  (fn [db]
    (get-in db [:settings :music-volume])))

(re-frame/reg-sub
  ::get-effects-volume
  (fn [db]
    (get-in db [:settings :effects-volume])))

(re-frame/reg-sub
  :loading
  (fn [db]
    (:loading db)))

(re-frame/reg-sub
  :errors
  (fn [db]
    (:errors db)))

(re-frame/reg-sub
  ::entity-errors
  (fn [db [_ entity]]
    (get-in db (conj [:errors] entity))))

(re-frame/reg-sub
  ::active-route
  (fn [db]
    (:active-route db)))

(re-frame/reg-sub
  ::navigation
  (fn [db]
    (:navigation db)))
