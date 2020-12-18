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

(defn- generate-lesson-set-name
  [scheme-name]
  (->> (str (random-uuid))
       (take 8)
       (clojure.string/join "")
       (str "ls-" scheme-name "-")))

(defn- get-lesson-sets
  [course-data {:keys [selection-from selection-to]}]
  (let [lesson-type (-> (utils/get-lesson course-data selection-from)
                        (get :type)
                        (keyword))
        scheme (utils/get-lesson-sets-scheme course-data selection-to lesson-type)]
    (->> scheme
         (map (fn [scheme-name]
                [(keyword scheme-name) (generate-lesson-set-name scheme-name)]))
         (into {}))))

(re-frame/reg-event-fx
  ::copy-lesson
  (fn [{:keys [db]} [_ {:keys [selection-from selection-to relative-position] :as params}]]
    (let [course-id (data-state/course-id db)
          course-data (subs/course-data db)

          dataset-id (-> (interpreter.subs/course-datasets db) (first) (get :id))
          lesson-sets (get-lesson-sets course-data params)
          lesson-data (-> (utils/get-lesson course-data selection-from)
                          (select-keys [:type :activities])
                          (assoc :lesson-sets lesson-sets))

          target-position (cond-> (:lesson-idx selection-to)
                                  (= relative-position :before) (identity)
                                  (= relative-position :after) (inc))
          updated-course-data (-> course-data
                                  (utils/add-lesson {:level-index (:level-idx selection-to)
                                                     :lesson-data lesson-data
                                                     :position    target-position}))]
      {:dispatch-n (concat [[::common/update-course course-id updated-course-data]]
                           (map (fn [lesson-set-name]
                                  [::warehouse/save-lesson-set {:dataset-id dataset-id
                                                                :name       lesson-set-name
                                                                :data       {:name  lesson-set-name
                                                                             :items []}}
                                   {:on-success [::save-lesson-set-success lesson-set-name]}])
                                (vals lesson-sets)))})))

(re-frame/reg-event-fx
  ::save-lesson-set-success
  (fn [{:keys [_]} [_ lesson-name {:keys [lesson]}]]
    {:dispatch [::interpreter.events/update-course-lessons [(assoc lesson :name lesson-name)]]}))
