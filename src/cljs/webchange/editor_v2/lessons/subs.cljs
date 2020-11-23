(ns webchange.editor-v2.lessons.subs
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.concepts.subs :as concepts-subs]))

(re-frame/reg-sub
  ::lesson-sets
  (fn [db]
    (get-in db [:editor :course-lesson-sets])))

(defn get-lesson-set
  [db name]
  (->> (get-in db [:editor :course-lesson-sets])
       (filter #(= name (:name %)))
       first))

(re-frame/reg-sub
  ::lesson-set
  (fn [db [_ name]]
    (get-lesson-set db name)))

(defn get-level
  [db level]
  (->> (get-in db [:course-data :levels])
       (filter #(= level (:level %)))
       first))

(defn get-lesson
  [level lesson]
  (->> (:lessons level)
       (filter #(= lesson (:lesson %)))
       first))

(re-frame/reg-sub
  ::level-scheme
  (fn [db [_ level]]
    (-> db
        (get-level level)
        :scheme)))

(defn- get-items
  [db lesson-set-name]
  (let [lesson-set (get-lesson-set db lesson-set-name)]
    (->> lesson-set
         :data
         :items
         (map :id)
         (map #(concepts-subs/get-item db %))
         (map #(select-keys % [:id :name]))
         (remove empty?))))

(re-frame/reg-sub
  ::lesson-with-items
  (fn [db [_ level-id lesson-id]]
    (let [level (get-level db level-id)
          lesson (get-lesson level lesson-id)
          lesson-sets (:lesson-sets lesson)]
      (if lesson
        (assoc lesson :lesson-sets (->> (vals lesson-sets)
                                        (map (fn [name]
                                               {:name  name
                                                :items (get-items db name)}))
                                        (zipmap (keys lesson-sets))))))))

(re-frame/reg-sub
  ::level-activities
  (fn [db [_ level]]
    (let [scenes-list (get-in db [:course-data :scene-list])
          available-activities (-> db (get-level level) :available-activities)]
      (if (nil? available-activities)
        scenes-list
        (let [available-activity-name? (fn [activity-name available-activities]
                                         (some (fn [available-activity]
                                                 (= activity-name (keyword available-activity)))
                                               available-activities))]
          (->> scenes-list
               (filter (fn [[name]] (available-activity-name? name available-activities)))
               (into {})))))))
