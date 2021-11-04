(ns webchange.interpreter.subs
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.lessons.activity :as activity]))

(defn course-datasets
  [db]
  (get-in db [:datasets]))

(re-frame/reg-sub
  ::course-datasets
  course-datasets)

(re-frame/reg-sub
  ::course-dataset
  (fn []
    [(re-frame/subscribe [::course-datasets])])
  (fn [[datasets] [_ dataset-id]]
    (some (fn [{:keys [id] :as dataset}] (and (= id dataset-id) dataset)) datasets)))

(defn- has-out?
  "Check if scene data from scene-list has out with given object name"
  [scene-data object-key]
  (->> scene-data
       :outs
       (some #(= (name object-key) (:object %)))))

(defn- with-locations
  "Return scene data from scene-list with updated outs"
  [scene-data locations]
  (let [location->out (fn [object-key location] {:object (name object-key) :name (:scene location)})]
    (reduce (fn [scene-data [object-key lx]]
              (if (has-out? scene-data object-key)
                (update scene-data :outs concat (map #(location->out object-key %) lx))
                scene-data))
            scene-data
            locations)))

(defn navigation-scene-list
  "Returns scene list with updated outs for navigation.
  Outs are updated according to next activity."
  [db]
  (let [locations (get-in db [:course-data :locations])]
    (->> (get-in db [:course-data :scene-list])
         (filter #(-> % second :outs))
         (map (fn [[scene-id data]] [scene-id (with-locations data locations)]))
         (into {}))))

(re-frame/reg-sub
  ::navigation-scene-list
  (fn [db]
    (navigation-scene-list db)))

(re-frame/reg-sub
  ::course-scenes
  (fn [db]
    (get-in db [:course-data :scene-list])))

(re-frame/reg-sub
  ::after-current-activity
  (fn [db]
    (->> (get-in db [:progress-data :next])
         (activity/next-not-finished-for db))))

(re-frame/reg-sub
  ::user-mode
  (fn [db]
    (get-in db [:progress-data :user-mode])))

(defn lesson-sets-data
  [db lesson-sets-ids]
  (let [lesson-sets (get-in db [:lessons])]
    (map #(get lesson-sets %) lesson-sets-ids)))

(defn lesson-set-data
  [db lesson-set-ids]
  (->> [lesson-set-ids]
       (lesson-sets-data db)
       (first)))

(re-frame/reg-sub
  ::lesson-sets-data
  (fn [db [_ lesson-sets-ids]]
    (lesson-sets-data db lesson-sets-ids)))

(defn- dissoc-keys
  [data keys-to-remove]
  (reduce (fn [data key-to-remove]
            (dissoc data key-to-remove))
          data
          keys-to-remove))

(re-frame/reg-sub
  ::stage-size
  (fn [db]
    (:stage-size db)))

(re-frame/reg-sub
  ::lessons-data
  (fn [db [_ {:keys [exclude-items-fields]}]]
    (let [lessons (get-in db [:lessons])
          dataset-items (get-in db [:dataset-items])]
      (->> lessons
           (map (fn [[lesson-name lesson-data]]
                  (let [lesson-items (->> (:item-ids lesson-data)
                                          (map (fn [item-id] (cond-> (get dataset-items item-id)
                                                                     (some? exclude-items-fields) (dissoc-keys exclude-items-fields)))))]
                    [lesson-name (assoc lesson-data :items lesson-items)])))
           (into {})))))

(re-frame/reg-sub
  ::current-lesson-sets-data
  (fn [db]
    (let [activity-name (:current-scene db)
          {:keys [level lesson]} (activity/name->activity-action db activity-name)
          lesson-sets (get-in db [:course-data :levels level :lessons lesson :lesson-sets])
          lessons (get-in db [:lessons])
          loaded-lessons (get-in db [:sandbox :loaded-lessons])
          get-lesson (fn [[name id]] (or (get loaded-lessons name)
                                         (get lessons id)))]
      (map get-lesson lesson-sets))))

(defn dataset-items
  ([db]
   (dataset-items db {}))
  ([db {:keys [exclude-items-fields]}]
   (cond-> (get-in db [:dataset-items])
           (some? exclude-items-fields) (->> (map (fn [[dataset-item-id dataset-item]]
                                                    [dataset-item-id (dissoc-keys dataset-item exclude-items-fields)]))
                                             (into {})))))

(re-frame/reg-sub
  ::dataset-items
  (fn [db [_ options]]
    (dataset-items db options)))

(re-frame/reg-sub
  ::dataset-item
  (fn []
    [(re-frame/subscribe [::dataset-items])])
  (fn [[dataset-items] [_ dataset-item-id]]
    (get dataset-items dataset-item-id)))

(comment
  (let [db @re-frame.db/app-db
        locations (get-in db [:course-data :locations])]
    (->> (get-in db [:course-data :scene-list])
         (filter #(-> % second :outs))
         (map (fn [[scene-id data]] [scene-id (with-locations data locations)]))
         ))

  (let [db @re-frame.db/app-db
        data (-> (get-in db [:course-data :scene-list])
                 :map)]
    (has-out? data :cycling))

  (let [db @re-frame.db/app-db
        data (-> (get-in db [:course-data :scene-list])
                 :map)]
    (->> data
         :outs
         (some #(= :cycling (:object %)))))

  (let [db @re-frame.db/app-db
        locations (get-in db [:course-data :locations])
        data (-> (get-in db [:course-data :scene-list])
                 :map)
        location->out (fn [name location] {:object name :name (:scene location)})]
    (reduce (fn [scene-data [name lx]]
              (println name lx)
              (if (has-out? scene-data name)
                #_(update scene-data concat (map #(location->out name %) lx))
                scene-data
                scene-data))
            data
            locations)))
