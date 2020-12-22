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
                                                                :data       {:items items}}
                                   {:on-success [::save-lesson-set-success new-name]}])
                                lesson-sets-map))})))

(re-frame/reg-event-fx
  ::save-lesson-set-success
  (fn [{:keys [_]} [_ lesson-name {:keys [lesson]}]]
    {:dispatch [::interpreter.events/update-course-lessons [(assoc lesson :name lesson-name)]]}))

(re-frame/reg-event-fx
  ::add-activity
  (fn [{:keys [db]} [_ {:keys [selection relative-position]}]]
    (let [course-id (data-state/course-id db)
          course-data (subs/course-data db)

          target-position (cond-> (:activity-idx selection)
                                  (= relative-position :before) (identity)
                                  (= relative-position :after) (inc))
          updated-course-data (-> course-data
                                  (utils/add-activity {:level-index  (:level-idx selection)
                                                       :lesson-index (:lesson-idx selection)
                                                       :position     target-position}))]
      {:dispatch [::common/update-course course-id updated-course-data]})))
