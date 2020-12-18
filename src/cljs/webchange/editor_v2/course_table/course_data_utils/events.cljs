(ns webchange.editor-v2.course-table.course-data-utils.events
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.course-data-utils.utils :as utils]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.state.edit-common :as common]
    [webchange.subs :as subs]))

(re-frame/reg-event-fx
  ::copy-lesson
  (fn [{:keys [db]} [_ {:keys [selection-from selection-to relative-position]}]]
    (print "::copy-lesson")
    (print "selection-from" selection-from)
    (print "selection-to" selection-to)
    (print "relative-position" relative-position)
    (let [course-id (data-state/course-id db)
          target-position (cond-> (:lesson-idx selection-to)
                                  (= relative-position :before) (identity)
                                  (= relative-position :after) (inc))
          updated-course-data (-> (subs/course-data db)
                                  (utils/add-lesson {:level-index (:level-idx selection-to)
                                                     :position    target-position}))]
      (print "updated-course-data" updated-course-data)
      ;{:dispatch [::common/update-course course-id updated-course-data]}
      {})))
