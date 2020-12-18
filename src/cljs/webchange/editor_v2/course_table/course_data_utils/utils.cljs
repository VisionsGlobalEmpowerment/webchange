(ns webchange.editor-v2.course-table.course-data-utils.utils)

(defn- insert-to-list
  [list index item]
  (let [[before after] (split-at index list)]
    (vec (concat before [item] after))))

;; Levels

(defn- get-level-path
  [{:keys [level-idx]}]
  {:pre [(number? level-idx)]}
  [:levels level-idx])

(defn- get-level
  [course-data selection]
  (->> (get-level-path selection)
       (get-in course-data)))

(defn get-lesson-sets-scheme
  ([course-data selection]
   (get-lesson-sets-scheme course-data selection nil))
  ([course-data selection type]
   {:pre [(number? (:level-idx selection))]}
   (let [level-data (get-level course-data selection)]
     (cond-> (:scheme level-data)
             (some? type) (get-in [type :lesson-sets])))))

(defn- update-level
  [course-data selection level-data-patch]
  (update-in course-data (get-level-path selection) merge level-data-patch))

;; Lessons

(defn- get-lesson-path
  [{:keys [lesson-idx] :as selection}]
  {:pre [(number? (:lesson-idx selection))
         (number? (:level-idx selection))]}
  (-> (get-level-path selection)
      (concat [:lessons lesson-idx])))

(defn get-lesson
  [course-data selection]
  (->> (get-lesson-path selection)
       (get-in course-data)))

(defn add-lesson
  [course-data {:keys [level-index position lesson-data]}]
  (let [level-selection {:level-idx level-index}
        updated-lessons (-> (get-level course-data level-selection)
                            (get :lessons)
                            (insert-to-list position lesson-data))]
    (update-level course-data level-selection {:lessons updated-lessons})))

(defn update-lesson
  [course-data selection lesson-data-patch]
  (update-in course-data (get-lesson-path selection) merge lesson-data-patch))

;;

(defn get-lesson-sets-names
  [course-data selection]
  (let [lesson-data (get-lesson course-data selection)
        lesson-type (-> (:type lesson-data) (keyword))
        scheme (->> (get-lesson-sets-scheme course-data selection lesson-type)
                    (map (fn [name] [(keyword name) nil]))
                    (into {}))]
    (-> scheme
        (merge (:lesson-sets lesson-data))
        (select-keys (keys scheme)))))

;; Activities

(defn- get-activity-path
  [{:keys [activity-idx] :as selection}]
  {:pre [(number? (:activity-idx selection))
         (number? (:lesson-idx selection))
         (number? (:level-idx selection))]}
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
