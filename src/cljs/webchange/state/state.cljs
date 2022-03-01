(ns webchange.state.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.core :as core]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:state])
       (vec)))

;; Scene data
(def last-saved-path (path-to-db [:last-saved]))

(re-frame/reg-sub
  ::last-saved
  (fn [db]
    (get-in db last-saved-path)))

(re-frame/reg-sub
  ::current-scene-id
  (fn []
    [(re-frame/subscribe [::core/current-scene-id])])
  (fn [[scene-id]]
    scene-id))

(re-frame/reg-event-fx
  ::set-current-scene-id
  (fn [{:keys [_]} [_ scene-id]]
    {:dispatch [::core/set-current-scene-id scene-id]}))

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
  ::load-scene
  (fn [{:keys [_]} [_ {:keys [course-slug scene-slug]} handlers]]
    {:dispatch [::warehouse/load-scene {:course-slug course-slug
                                        :scene-slug  scene-slug}
                {:on-success [::load-scene-success handlers scene-slug]}]}))

(re-frame/reg-event-fx
  ::load-scene-success
  (fn [{:keys [_]} [_ {:keys [on-success]} scene-slug scene-data]]
    {:dispatch-n (cond-> [[::set-scene-data scene-slug scene-data]]
                         (some? on-success) (conj (conj on-success scene-data)))}))

(re-frame/reg-event-fx
  ::set-scene-data
  (fn [{:keys [_]} [_ scene-slug scene-data]]
    {:dispatch [::core/set-scene-data {:scene-id   scene-slug
                                       :scene-data scene-data}]}))

(re-frame/reg-event-fx
  ::update-scene-data
  (fn [{:keys [_]} [_ scene-slug data-path data-patch]]
    {:dispatch [::core/update-scene-data {:scene-id   scene-slug
                                          :data-path  data-path
                                          :data-patch data-patch}]}))

(re-frame/reg-event-fx
  ::update-last-saved
  (fn [{:keys [db]} _]
    {:db (assoc-in db last-saved-path (js/Date.))}))

(re-frame/reg-event-fx
  ::update-scene
  (fn [{:keys [db]} [_ {:keys [course-id scene-id scene-data-patch]} {:keys [on-success]}]]
    (let [course-id (or course-id (core/current-course-id db))
          scene-id (if (some? scene-id) scene-id (core/current-scene-id db))
          current-scene-data (core/get-scene-data db scene-id)
          scene-data (-> (merge current-scene-data scene-data-patch)
                         (select-keys [:assets :objects :scene-objects :actions :triggers :skills :metadata]))]
      {:dispatch [::warehouse/save-scene {:course-slug course-id
                                          :scene-slug  scene-id
                                          :scene-data  scene-data}
                  {:on-success [::update-scene-success on-success]}]})))

(re-frame/reg-event-fx
  ::update-scene-success
  (fn [{:keys [db]} [_ on-success {:keys [name data] :as response}]]
    {:dispatch-n (cond-> [[::core/set-scene-data {:scene-id   name
                                                  :scene-data data}]
                          [::update-last-saved]]
                         (some? on-success) (conj (conj on-success response)))}))

; Objects

(defn get-objects-data
  [db]
  (-> (scene-data db)
      (get :objects {})))

(re-frame/reg-sub
  ::objects-data
  (fn [[_ scene-id]]
    [(re-frame/subscribe [::scene-data scene-id])])
  (fn [[scene-data]]
    (get scene-data :objects {})))

(defn get-object-data
  [db object-name]
  (-> (get-objects-data db)
      (get object-name)))

(re-frame/reg-event-fx
  ::update-scene-objects
  (fn [{:keys [db]} [_ {:keys [course-id scene-id patches-list]} handlers]]
    {:pre [(sequential? patches-list)]}
    (let [scene-id (if (some? scene-id) scene-id (core/current-scene-id db))
          objects-data (reduce (fn [objects-data {:keys [object-name object-data-patch]}]
                                 (update objects-data object-name merge object-data-patch))
                               (-> (core/get-scene-data db scene-id)
                                   (get :objects {}))
                               patches-list)]
      {:dispatch [::update-scene {:course-id        course-id
                                  :scene-id         scene-id
                                  :scene-data-patch {:objects objects-data}}
                  handlers]})))

(re-frame/reg-event-fx
  ::update-scene-object
  (fn [{:keys [_]} [_ {:keys [course-id scene-id object-name object-data-patch]} handlers]]
    {:pre [(keyword? object-name)]}
    {:dispatch [::update-scene-objects {:course-id    course-id
                                        :scene-id     scene-id
                                        :patches-list [{:object-name       object-name
                                                        :object-data-patch object-data-patch}]} handlers]}))

(def current-object-path (path-to-db [:current-object]))

(defn get-current-object
  [db]
  (get-in db current-object-path))

(re-frame/reg-sub
  ::current-object
  get-current-object)

(re-frame/reg-event-fx
  ::set-current-object
  (fn [{:keys [db]} [_ object-name]]
    {:db (assoc-in db current-object-path object-name)}))

(re-frame/reg-event-fx
  ::reset-current-object
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-current-object nil]}))

; Metadata

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
