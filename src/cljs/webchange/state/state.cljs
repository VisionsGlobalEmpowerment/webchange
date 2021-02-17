(ns webchange.state.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.core :as core]
    [webchange.state.warehouse :as warehouse]))

;; Course-data

(defn course-data
  [db]
  (get db :course-data))

(re-frame/reg-sub
  ::course-data
  course-data)

(re-frame/reg-event-fx
  ::set-course-data
  (fn [{:keys [db]} [_ course-data]]
    {:db (assoc db :course-data course-data)}))

(re-frame/reg-event-fx
  ::load-course
  (fn [{:keys [_]} [_ course-slug]]
    {:dispatch [::warehouse/load-course course-slug {:on-success [::set-course-data]}]}))

;; Scene data

(defn scene-data
  ([db]
   (core/get-scene-data db (core/current-scene-id db)))
  ([db scene-id]
   (core/get-scene-data db scene-id)))

(re-frame/reg-sub
  ::scene-data
  (fn []
    [(re-frame/subscribe [::core/scenes-data])
     (re-frame/subscribe [::core/current-scene-id])])
  (fn [[scenes-data current-scene-id] [_ scene-id]]
    (if (some? scene-id)
      (get scenes-data scene-id)
      (get scenes-data current-scene-id))))

(defn scene-metadata
  ([db]
   (-> (scene-data db)
       (get-in [:metadata])))
  ([db scene-id]
   (-> (scene-data db scene-id)
       (get-in [:metadata]))))

(re-frame/reg-event-fx
  ::update-scene
  (fn [{:keys [db]} [_ {:keys [scene-id scene-data-patch]} {:keys [on-success]}]]
    (let [course-id (core/current-course-id db)
          scene-id (if (some? scene-id) scene-id (core/current-scene-id db))
          current-scene-data (core/get-scene-data db scene-id)
          scene-data (merge current-scene-data scene-data-patch)]
      {:dispatch [::warehouse/save-scene {:course-id  course-id
                                          :scene-id   scene-id
                                          :scene-data scene-data}
                  {:on-success [::update-scene-success on-success]}]})))

(re-frame/reg-event-fx
  ::update-scene-success
  (fn [{:keys [_]} [_ on-success {:keys [name data] :as response}]]
    {:dispatch-n (cond-> [[::core/set-scene-data {:scene-id   name
                                                  :scene-data data}]]
                         (some? on-success) (conj (conj on-success response)))}))

(re-frame/reg-event-fx
  ::update-scene-object
  (fn [{:keys [db]} [_ {:keys [scene-id object-name object-data-patch]} handlers]]
    {:pre [(keyword? object-name)]}
    (let [scene-id (if (some? scene-id) scene-id (core/current-scene-id db))
          objects-data (-> (core/get-scene-data db scene-id)
                           (get :objects {})
                           (update object-name merge object-data-patch))]
      {:dispatch [::update-scene {:scene-id         scene-id
                                  :scene-data-patch {:objects objects-data}}
                  handlers]})))

(re-frame/reg-event-fx
  ::update-scene-metadata
  (fn [{:keys [db]} [_ {:keys [scene-id metadata-patch]}]]
    (let [scene-id (if (some? scene-id) scene-id (core/current-scene-id db))
          current-metadata (-> (core/get-scene-data db scene-id) (get :metadata {}))
          metadata (merge current-metadata metadata-patch)]
      {:dispatch [::update-scene {:scene-id         scene-id
                                  :scene-data-patch {:metadata metadata}}]})))

;; Lesson sets

(defn lesson-sets
  [db]
  (get db :lessons {}))

(defn lesson-set-id->name
  [db lesson-set-id]
  (->> (lesson-sets db)
       (some (fn [[lesson-set-name {:keys [id]}]]
               (and (= id lesson-set-id)
                    lesson-set-name)))))

(defn- prepare-lesson-set-data [{data :data :as prepare-lesson-set}]
  (assoc prepare-lesson-set :item-ids (map #(:id %) (:items data))))

(re-frame/reg-sub
  ::lesson-sets
  lesson-sets)

(re-frame/reg-event-fx
  ::create-lesson-set
  (fn [{:keys [_]} [_ props]]
    {:dispatch [::warehouse/create-lesson-set props {:on-success [::update-lesson-set]}]}))

(re-frame/reg-event-fx
  ::update-lesson-set
  (fn [{:keys [db]} [_ {:keys [lesson]}]]
    (let [lesson-set-name (:name lesson)]
      {:db (->> (prepare-lesson-set-data lesson)
                (update-in db [:lessons lesson-set-name] merge))})))

(re-frame/reg-event-fx
  ::delete-lesson-set
  (fn [{:keys [_]} [_ props]]
    {:dispatch [::warehouse/delete-lesson-set props {:on-success [::delete-lesson-set-by-id]}]}))

(re-frame/reg-event-fx
  ::delete-lesson-set-by-name
  (fn [{:keys [db]} [_ lesson-set-name]]
    {:db (update db :lessons dissoc lesson-set-name)}))

(re-frame/reg-event-fx
  ::delete-lesson-set-by-id
  (fn [{:keys [db]} [_ {:keys [id]}]]
    {:dispatch [::delete-lesson-set-by-name (lesson-set-id->name db id)]}))

;; Activity Template

(re-frame/reg-event-fx
  ::call-activity-action
  (fn [{:keys [db]} [_ {:keys [course-id scene-id action data] :or {data {}}} {:keys [on-success]}]]
    {:pre [(some? action)]}
    (let [course-id (or course-id (core/current-course-id db))
          scene-id (or scene-id (core/current-scene-id db))]
      {:dispatch [::warehouse/update-activity
                  {:course-id course-id
                   :scene-id  scene-id
                   :data      {:action action
                               :data   data}}
                  {:on-success [::call-activity-action-success on-success]}]})))

(re-frame/reg-event-fx
  ::call-activity-action-success
  (fn [{:keys [_]} [_ on-success {:keys [name data] :as response}]]
    {:dispatch-n (cond-> [[::core/set-scene-data {:scene-id   name
                                                  :scene-data data}]]
                         (some? on-success) (conj (conj on-success response)))}))
