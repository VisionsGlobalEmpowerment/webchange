(ns webchange.editor-v2.course-table.fields.concepts.views
  (:require
    [webchange.editor-v2.course-table.fields.concepts.views-edit :refer [edit-form]]
    [webchange.editor-v2.course-table.fields.concepts.views-info :refer [info-from]]
    [webchange.editor-v2.course-table.fields.lesson-comment.views :refer [lesson-comment]]
    [webchange.editor-v2.course-table.form-wrapper.views :refer [edit-form-wrapper]]))

(defn concepts
  [{:keys [data edit?] :as props}]
  (cond
    (empty? (:lesson-sets data)) [lesson-comment props]
    edit? [edit-form-wrapper
           [edit-form props]]
    :else [info-from props]))
