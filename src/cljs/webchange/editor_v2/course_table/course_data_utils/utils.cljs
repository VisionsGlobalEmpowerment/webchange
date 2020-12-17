(ns webchange.editor-v2.course-table.course-data-utils.utils)

;; Levels

(defn- get-level-path
  [{:keys [level-idx]}]
  [:levels level-idx])

(defn- get-level
  [course-data selection]
  (->> (get-level-path selection)
       (get-in course-data)))

;; Lessons

(defn- get-lesson-path
  [{:keys [lesson-idx] :as selection}]
  (-> (get-level-path selection)
      (concat [:lessons lesson-idx])))

(defn- get-lesson
  [course-data selection]
  (->> (get-lesson-path selection)
       (get-in course-data)))

(defn add-lesson
  [course-data level-index]
  ;(let [level (get-level course-data level-index)
  ;      lesson-data {:name        "New-Lesson",
  ;                   :type        "lesson",
  ;                   :activities  [{}],
  ;                   :lesson-sets {}}]
  ;  (update-in course-data (concat (get-level-path level-index) [:lessons]) conj lesson-data))
  )

(defn update-lesson
  [course-data selection lesson-data-patch]
  (update-in course-data (get-lesson-path selection) merge lesson-data-patch))

;;

(defn get-lesson-sets-names
  [course-data selection]
  (let [level-data (get-level course-data selection)
        lesson-data (get-lesson course-data selection)]
    (let [lesson-type (-> (:type lesson-data) (keyword))
          scheme (->> (get-in level-data [:scheme lesson-type :lesson-sets])
                      (map (fn [name] [(keyword name) nil]))
                      (into {}))]
      (-> scheme
          (merge (:lesson-sets lesson-data))
          (select-keys (keys scheme))))))

;; Activities

(defn- get-activity-path
  [{:keys [activity-idx] :as selection}]
  (-> (get-lesson-path selection)
      (concat [:activities activity-idx])))

(defn- get-activity
  [course-data selection]
  (->> (get-activity-path selection)
       (get-in course-data)))

;;

(defn get-activity-name
  [course-data selection]
  (->> (get-activity course-data selection)
       (:activity)))

(defn get-activity-tags-appointment
  [course-data selection]
  (-> (get-activity course-data selection)
      (get :tags-by-score {})))

(defn get-activity-tags-restriction
  [course-data selection]
  (-> (get-activity course-data selection)
      (get :only [])))

(defn update-activity
  [course-data selection activity-data-patch]
  (update-in course-data (get-activity-path selection) merge activity-data-patch))
