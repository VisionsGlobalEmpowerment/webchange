(ns webchange.editor-v2.course-table.course-data-utils.events
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.course-data-utils.utils :as utils]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.state.edit-common :as common]
    [webchange.interpreter.events :as interpreter.events]
    [webchange.interpreter.subs :as interpreter.subs]
    [webchange.subs :as subs]
    [webchange.warehouse :as warehouse]))

(defn- lesson-set-name-unique?
  [course-data lesson-set-name]
  (->> (utils/get-course-lesson-sets-names course-data)
       (some #{lesson-set-name})
       (not)))

(defn- get-new-lesson-set-name
  [scheme-name]
  (->> (str (random-uuid))
       (take 8)
       (clojure.string/join "")
       (str "ls-" (clojure.core/name scheme-name) "-")))

(defn- generate-lesson-set-name
  [course-data scheme-name]
  (loop [name (get-new-lesson-set-name scheme-name)]
    (if-not (lesson-set-name-unique? course-data name)
      (recur (get-new-lesson-set-name scheme-name))
      name)))

(defn- get-lesson-set-items
  [db lesson-data scheme-name]
  (let [lesson-set-name (get-in lesson-data [:lesson-sets scheme-name])]
    (-> (interpreter.subs/lesson-set-data db lesson-set-name)
        (get-in [:data :items] []))))

(defn- get-lesson-sets-map
  [db course-data {:keys [selection-from selection-to]}]
  (let [lesson-data (utils/get-lesson course-data selection-from)
        lesson-type (-> lesson-data (get :type) (keyword))
        scheme (utils/get-lesson-sets-scheme course-data selection-to lesson-type)]
    (->> scheme
         (map keyword)
         (map (fn [scheme-name]
                [scheme-name {:new-name (generate-lesson-set-name course-data scheme-name)
                              :items    (get-lesson-set-items db lesson-data scheme-name)}]))
         (into {}))))

(defn- lessons-map->lessons-names
  [lessons-map]
  (->> lessons-map
       (map (fn [[scheme-name {:keys [new-name]}]] [scheme-name new-name]))
       (into {})))

(defn- get-default-activity-data
  [course-data]
  {:activity (-> (utils/get-available-activities-ids course-data) (first))})

(defn- get-default-lesson-data
  [course-data level-index]
  (let [lesson-type "lesson"
        scheme (utils/get-lesson-sets-scheme course-data {:level-idx level-index} (keyword lesson-type))
        lesson-sets (->> scheme
                         (map keyword)
                         (map (fn [scheme-name]
                                [scheme-name (generate-lesson-set-name course-data scheme-name)]))
                         (into {}))]
    {:type        lesson-type
     :activities  [(get-default-activity-data course-data)]
     :lesson-sets lesson-sets}))

(defn- get-default-level-data
  []
  {:lessons []})

(re-frame/reg-event-fx
  ::copy-lesson
  (fn [{:keys [db]} [_ {:keys [selection-from selection-to relative-position] :as params}]]
    (let [course-id (data-state/course-id db)
          course-data (subs/course-data db)
          dataset-id (-> (interpreter.subs/course-datasets db) (first) (get :id))

          lesson-sets-map (get-lesson-sets-map db course-data params)
          lesson-data (-> (utils/get-lesson course-data selection-from)
                          (select-keys [:type :activities])
                          (assoc :lesson-sets (lessons-map->lessons-names lesson-sets-map)))

          target-position (cond-> (:lesson-idx selection-to)
                                  (= relative-position :before) (identity)
                                  (= relative-position :after) (inc))
          updated-course-data (-> course-data
                                  (utils/add-lesson {:level-index (:level-idx selection-to)
                                                     :lesson-data lesson-data
                                                     :position    target-position}))]
      {:dispatch-n (concat [[::common/update-course course-id updated-course-data]]
                           (map (fn [[_ {:keys [new-name items]}]]
                                  [::warehouse/save-lesson-set {:dataset-id dataset-id
                                                                :name       new-name
                                                                :data       {:items items}}])
                                lesson-sets-map))})))

(re-frame/reg-event-fx
  ::add-level
  (fn [{:keys [db]} [_ {:keys [selection relative-position]}]]
    (let [course-id (data-state/course-id db)
          course-data (subs/course-data db)

          target-position (cond-> (:level-idx selection)
                                  (= relative-position :before) (identity)
                                  (= relative-position :after) (inc))
          level-data (get-default-level-data)
          updated-course-data (-> course-data
                                  (utils/add-level {:position   target-position
                                                    :level-data level-data}))]
      {:dispatch [::common/update-course
                  course-id
                  updated-course-data
                  {:on-success [::add-lesson {:selection         {:level-idx  target-position
                                                                  :lesson-idx 0}
                                              :relative-position :before}]}]})))

(re-frame/reg-event-fx
  ::remove-level
  (fn [{:keys [db]} [_ {:keys [selection]}]]
    (let [course-id (data-state/course-id db)
          course-data (subs/course-data db)
          updated-course-data (utils/remove-level course-data {:level-index (:level-idx selection)})]
      {:dispatch [::common/update-course course-id updated-course-data]})))

(re-frame/reg-event-fx
  ::add-lesson
  (fn [{:keys [db]} [_ {:keys [selection relative-position]}]]
    (let [course-id (data-state/course-id db)
          course-data (subs/course-data db)
          dataset-id (-> (interpreter.subs/course-datasets db) (first) (get :id))

          target-position (cond-> (:lesson-idx selection)
                                  (= relative-position :before) (identity)
                                  (= relative-position :after) (inc))
          lesson-data (get-default-lesson-data course-data (:level-idx selection))
          updated-course-data (-> course-data
                                  (utils/add-lesson {:level-index (:level-idx selection)
                                                     :position    target-position
                                                     :lesson-data lesson-data}))]
      {:dispatch-n (concat [[::common/update-course course-id updated-course-data]]
                           (map (fn [[_ lesson-set-name]]
                                  [::warehouse/save-lesson-set {:dataset-id dataset-id
                                                                :name       lesson-set-name
                                                                :data       {:items []}}])
                                (:lesson-sets lesson-data)))})))

(re-frame/reg-event-fx
  ::remove-lesson
  (fn [{:keys [db]} [_ {:keys [selection]}]]
    (let [course-id (data-state/course-id db)
          course-data (subs/course-data db)

          lesson-sets-ids (->> (utils/get-lesson-lesson-sets-names course-data selection)
                               (interpreter.subs/lesson-sets-data db)
                               (map :id))

          updated-course-data (utils/remove-lesson course-data {:level-index  (:level-idx selection)
                                                                :lesson-index (:lesson-idx selection)})]
      {:dispatch-n (concat [[::common/update-course course-id updated-course-data]]
                           (map (fn [lesson-set-id]
                                  [::warehouse/delete-lesson-set {:id lesson-set-id}])
                                lesson-sets-ids))})))

(re-frame/reg-event-fx
  ::add-activity
  (fn [{:keys [db]} [_ {:keys [selection relative-position]}]]
    (let [course-id (data-state/course-id db)
          course-data (subs/course-data db)

          target-position (cond-> (:activity-idx selection)
                                  (= relative-position :before) (identity)
                                  (= relative-position :after) (inc))
          activity-data (get-default-activity-data course-data)
          updated-course-data (-> course-data
                                  (utils/add-activity {:level-index   (:level-idx selection)
                                                       :lesson-index  (:lesson-idx selection)
                                                       :position      target-position
                                                       :activity-data activity-data}))]
      {:dispatch [::common/update-course course-id updated-course-data]})))

(re-frame/reg-event-fx
  ::remove-activity
  (fn [{:keys [db]} [_ {:keys [selection]}]]
    (let [course-id (data-state/course-id db)
          course-data (subs/course-data db)
          updated-course-data (utils/remove-activity course-data {:level-index    (:level-idx selection)
                                                                  :lesson-index   (:lesson-idx selection)
                                                                  :activity-index (:activity-idx selection)})]
      {:dispatch [::common/update-course course-id updated-course-data]})))
