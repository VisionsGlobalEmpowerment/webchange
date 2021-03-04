(ns webchange.editor-v2.course-data-utils.utils
  (:require
    [webchange.utils.list :refer [insert-at-position remove-at-position]]))

;; Scenes

(defn course-scenes-list
  [course-data]
  (->> (:scene-list course-data)
       (map (fn [[scene-id scene-data]] (assoc scene-data :id scene-id)))))

(defn get-scene-data
  [course-data scene-id]
  (->> (course-scenes-list course-data)
       (some (fn [{:keys [id] :as scene-data}]
               (and (= id scene-id)
                    scene-data)))))

(defn update-scene-data
  [course-data scene-id data-key data-value]
  (assoc-in course-data [:scene-list scene-id data-key] data-value))

(defn get-scene-outs
  [course-data scene-id]
  (-> (get-scene-data course-data scene-id)
      (get :outs [])))

(defn set-scene-out
  [course-data scene-id scene-object out-scene-id]
  (->> (get-scene-outs course-data scene-id)
       (map (fn [{:keys [object] :as scene-out-data}]
              (if (= object scene-object)
                (assoc scene-out-data :name out-scene-id)
                scene-out-data)))
       (update-scene-data course-data scene-id :outs)))

;; Levels

(defn- get-level-path
  [{:keys [level-idx]}]
  {:pre [(number? level-idx)]}
  [:levels level-idx])

(defn get-levels
  [course-data]
  (get course-data :levels []))

(defn- get-level
  [course-data selection]
  (->> (get-level-path selection)
       (get-in course-data)))

(defn get-level-lessons
  [level-data]
  (get level-data :lessons []))

(defn add-level
  [course-data {:keys [position level-data]}]
  (let [updated-levels (-> (get-levels course-data)
                           (insert-at-position level-data position))]
    (assoc course-data :levels updated-levels)))

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

(defn remove-level
  [course-data {:keys [level-index]}]
  (let [updated-levels (-> (get-levels course-data)
                           (remove-at-position level-index))]
    (assoc course-data :levels updated-levels)))

;; Locations

(defn scenes-locations
  [course-data]
  (get course-data :locations {}))

(defn scene-locations
  [course-data scene-id]
  (-> (scenes-locations course-data)
      (get scene-id)))

(defn set-scene-location
  [course-data scene-id locations-data]
  (assoc-in course-data [:locations scene-id] locations-data))

(defn init-scene-locations
  [course-data scene-id]
  (if-not (some? (scene-locations course-data scene-id))
    (let [locations-data (->> (get-levels course-data)
                              (map-indexed (fn [level-idx _]
                                             {:level level-idx
                                              :scene (clojure.core/name scene-id)})))]
      (set-scene-location course-data scene-id locations-data))
    course-data))

(defn add-scene-location
  "Set record in :locations for scene
  - scene-id - the scene-id for which the alternative location will be recorded;
  - location - the name of alternative location;
  - level - current level;

  Result:
  :locations
      {:_scene-id_ [{:level 0, :scene '_scene-id_'} {:level _level_, :scene _location_}],"
  [course-data scene-id level location]
  {:pre [(keyword? scene-id) (number? level) (string? location)]}
  (let [course-data (init-scene-locations course-data scene-id)
        scene-locations (scene-locations course-data scene-id)
        location-idx (->> (map-indexed vector scene-locations)
                          (some (fn [[idx location-data]]
                                  (and (= (:level location-data) level)
                                       idx))))]
    (->> (update-in scene-locations [location-idx] assoc :scene location)
         (set-scene-location course-data scene-id))))

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
                            (insert-at-position lesson-data position))]
    (update-level course-data level-selection {:lessons updated-lessons})))

(defn update-lesson
  [course-data selection lesson-data-patch]
  (update-in course-data (get-lesson-path selection) merge lesson-data-patch))

(defn remove-lesson
  [course-data {:keys [level-index lesson-index]}]
  (let [level-selection {:level-idx level-index}
        updated-lessons (-> (get-level course-data level-selection)
                            (get :lessons)
                            (remove-at-position lesson-index))]
    (update-level course-data level-selection {:lessons updated-lessons})))

;;

(defn get-lesson-sets-names
  [course-data selection]
  (let [lesson-data (get-lesson course-data selection)]
    (:lesson-sets lesson-data)))

(defn get-lesson-comment
  [course-data selection]
  (-> (get-lesson course-data selection)
      (get-in [:comment])))

(defn update-lesson-comment
  [course-data selection comment]
  (update-lesson course-data selection {:comment comment}))

;; Activities

(defn- get-available-activities
  [course-data]
  (->> (:scene-list course-data)
       (map (fn [[id data]] (assoc data :id id)))))

(defn get-available-activities-ids
  [course-data]
  (->> (get-available-activities course-data)
       (map :id)
       (map clojure.core/name)))

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

(defn add-activity
  [course-data {:keys [level-index lesson-index position activity-data]
                :or   {activity-data {}}}]
  (let [lesson-selection {:level-idx  level-index
                          :lesson-idx lesson-index}
        updated-activities (-> (get-lesson course-data lesson-selection)
                               (get :activities)
                               (insert-at-position activity-data position))]
    (update-lesson course-data lesson-selection {:activities updated-activities})))

(defn remove-activity
  [course-data {:keys [level-index lesson-index activity-index]}]
  (let [lesson-selection {:level-idx  level-index
                          :lesson-idx lesson-index}
        updated-activities (-> (get-lesson course-data lesson-selection)
                               (get :activities)
                               (remove-at-position activity-index))]
    (update-lesson course-data lesson-selection {:activities updated-activities})))

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

;;

(defn get-lesson-lesson-sets-names
  ([course-data selection]
   (-> (get-lesson course-data selection)
       (get-lesson-lesson-sets-names)))
  ([lesson-data]
   (->> (get lesson-data :lesson-sets)
        (vals)
        (distinct))))

(defn get-level-lesson-sets-names
  ([course-data selection]
   (-> (get-level course-data selection)
       (get-level-lesson-sets-names)))
  ([level-data]
   (->> (get-level-lessons level-data)
        (map get-lesson-lesson-sets-names)
        (flatten)
        (distinct))))

(defn get-course-lesson-sets-names
  [course-data]
  (->> (get-levels course-data)
       (map get-level-lesson-sets-names)
       (flatten)
       (distinct)))

(defn activities->lesson-sets-scheme
  [course-data activities]
  (let [activity-name->lesson-sets #(get-in course-data [:scene-list (keyword %) :lesson-sets])]
    (->> activities
         (map :activity)
         (mapcat activity-name->lesson-sets)
         (distinct))))

(defn- lesson-set-name-unique?
  [course-data lesson-set-name]
  (->> (get-course-lesson-sets-names course-data)
       (some #{lesson-set-name})
       (not)))

(defn- get-new-lesson-set-name
  [scheme-name]
  (->> (str (random-uuid))
       (take 8)
       (clojure.string/join "")
       (str "ls-" (clojure.core/name scheme-name) "-")))

(defn generate-lesson-set-name
  [course-data scheme-name]
  (loop [name (get-new-lesson-set-name scheme-name)]
    (if-not (lesson-set-name-unique? course-data name)
      (recur (get-new-lesson-set-name scheme-name))
      name)))

(defn update-lesson-sets
  [course-data selection]
  (let [lesson (get-lesson course-data selection)
        scheme (->> (activities->lesson-sets-scheme course-data (:activities lesson))
                    (map keyword))
        scheme-lesson-sets (->> scheme
                                (map (fn [scheme-name]
                                       [scheme-name (generate-lesson-set-name course-data scheme-name)]))
                                (into {}))
        lesson-sets (-> scheme-lesson-sets
                        (merge (:lesson-sets lesson))
                        (select-keys scheme))]
    (update-lesson course-data selection {:lesson-sets lesson-sets})))

(defn get-lessons-sets-diff
  [old-lesson-sets new-lesson-sets]
  {:removed (-> (apply dissoc old-lesson-sets (keys new-lesson-sets)) vals)
   :added (-> (apply dissoc new-lesson-sets (keys old-lesson-sets)) vals)})
