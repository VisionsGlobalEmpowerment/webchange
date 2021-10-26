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
  ::scene-info
  (fn [db [_ scene-id]]
    (get-in db [:course-data :scene-list (keyword scene-id)])))

(re-frame/reg-sub
  ::scene-list
  (fn [db]
    (get-in db [:course-data :scene-list])))


(re-frame/reg-sub
  ::scene-list-ordered
  (fn [db]
    (->> (get-in db [:course-data :scene-list])        
         (remove #(-> % second :archived))
         (map #(assoc (second %) :scene-id (first %)))
         (sort-by :scene-id))))

(re-frame/reg-sub
  ::navigation-mode
  (fn [db]
    (let [navigation-mode (get-in db [:course-data :navigation-mode])]
      (if (= navigation-mode nil)
        :activity
        (keyword navigation-mode)))))

(re-frame/reg-sub
  ::course-scenes
  (fn [db]
    (->> db :course-data :scene-list
         (remove #(-> % second :archived))
         (map first)
         (map name)
         (into []))))

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

(defn current-course
  [db]
  (:current-course db))

(re-frame/reg-sub ::current-course current-course)

(defn course-data
  [db]
  (:course-data db))

(re-frame/reg-sub ::course-data course-data)

(re-frame/reg-sub
  ::scene
  (fn [db [_ scene-id]]
    (get-in db [:scenes scene-id] {})))

(defn scene-skills
  [db]
  (get db :scene-skills {}))

(re-frame/reg-sub
  ::scene-skills
  scene-skills)

(re-frame/reg-sub
  ::scene-placeholders
  (fn [db]
    (get db :scene-placeholders {})))

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

(defn current-scene-objects
  [db]
  (let [current-scene-id (current-scene db)]
    (get-in db [:scenes current-scene-id :objects] {})))

(defn current-scene-background
  [db]
  (let [scene-objects (current-scene-objects db)]
    (some (fn [[name {:keys [type] :as object}]]
            (and (or (= type "background")
                     (= type "layered-background"))
                 (merge object
                        {:name name}))) scene-objects)))

(re-frame/reg-sub
  ::current-scene-objects
  current-scene-objects)

(defn current-scene-actions
  [db]
  (let [current-scene-id (current-scene db)]
    (get-in db [:scenes current-scene-id :actions] {})))

(re-frame/reg-sub
  ::current-scene-actions
  current-scene-actions)

(defn current-scene-action
  [db action-id]
  (-> (current-scene-actions db)
      (get action-id)))

(re-frame/reg-sub
  ::current-scene-back-button
  (fn []
    [(re-frame/subscribe [::current-scene-objects])])
  (fn [[scene-objects]]
    (:back scene-objects)))

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

(defn scene-object
  [db scene-id name]
  (get-in db [:scenes scene-id :objects (keyword name)] {}))

(re-frame/reg-sub
  ::scene-object
  (fn [db [_ scene-id name]]
    (scene-object db scene-id name)))

(re-frame/reg-sub
  ::scene-metadata
  (fn [db [_ scene-id]]
    (get-in db [:scenes scene-id :metadata] {})))

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

(defn scene-audio
  [db scene-id]
  (get-in db [:scenes scene-id :audio] {}))

(re-frame/reg-sub
  ::scene-audio
  (fn [db [_ scene-id]]
    (scene-audio db scene-id)))

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

(re-frame/reg-sub
  :app-version
  (fn [db]
    (:app-version db)))
