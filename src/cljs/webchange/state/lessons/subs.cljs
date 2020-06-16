(ns webchange.state.lessons.subs)

(defn- lessons
  [db]
  (get-in db [:lessons]))

(defn- lesson-set
  [db lesson-set-name]
  (if-let [lesson (-> (lessons db)
                      (get-in [lesson-set-name]))]
    lesson
    (throw (js/Error. (str "Lesson '" lesson-set-name "' is not defined")))))

(defn lesson-dataset-items
  [db lesson-name]
  (let [current-activity (:activity db)
        lesson-sets (->> (get-in db [:course-data :levels])
                         (filter #(= (:level current-activity) (:level %)))
                         first
                         :lessons
                         (filter #(= (:lesson current-activity) (:lesson %)))
                         first
                         :lesson-sets)
        lesson-set-name (or (get lesson-sets (keyword lesson-name))
                            lesson-name)
        lesson (lesson-set db lesson-set-name)]
    (->> (:item-ids lesson)
         (map #(get-in db [:dataset-items %]))
         (map #(merge (:data %) {:id (:name %)})))))
