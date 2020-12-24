(ns webchange.editor-v2.course-table.fields.lesson-comment.views
  (:require
    [webchange.editor-v2.course-table.fields.lesson-comment.views-edit :refer [edit-form]]
    [webchange.editor-v2.course-table.fields.lesson-comment.views-info :refer [info-from]]))

(defn lesson-comment
  [{:keys [edit?] :as props}]
  (if edit?
    [edit-form props]
    [info-from props]))
