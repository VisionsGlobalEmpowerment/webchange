(ns webchange.editor-v2.course-table.state.change-data
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.selection :as selection]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.course-data-utils.utils :as utils]
    [webchange.editor-v2.course-table.state.edit-common :as common]
    [webchange.subs :as subs]))

(re-frame/reg-event-fx
  ::insert
  (fn [{:keys [db]} [_ target]]
    (print "::insert" target)
    (let [current-selection (:data (selection/selection db))
          saved-selection (:data (selection/saved-selection db))
          course-id (data-state/course-id db)
          course-data (subs/course-data db)
          target-level-index (dec (:level current-selection))]
      ;(print saved-selection " -> ")
      ;(print current-selection " <- ")
      ;(print "lesson-data" copied-lesson-data )


      (print course-data (utils/add-lesson course-data target-level-index))

      {:dispatch [::common/update-course course-id (utils/add-lesson course-data target-level-index)]}
      )))

(re-frame/reg-event-fx
  ::add-lesson
  (fn [{:keys [db]} [_ target]]

    ))