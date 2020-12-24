(ns webchange.editor-v2.course-table.fields.concepts.views
  (:require
    [webchange.editor-v2.course-table.fields.concepts.views-edit :refer [edit-form]]
    [webchange.editor-v2.course-table.fields.concepts.views-info :refer [info-from]]
    [webchange.editor-v2.course-table.fields.lesson-comment.views :refer [lesson-comment]]))

(defn concepts
  [{:keys [data edit?] :as props}]
  (cond
    (empty? (:lesson-sets data)) [lesson-comment props]
    edit? [edit-form props]
    :else [info-from props]))
