(ns webchange.editor-v2.course-table.fields.concepts.views
  (:require
    [webchange.editor-v2.course-table.fields.concepts.views-edit :refer [edit-form]]
    [webchange.editor-v2.course-table.fields.concepts.views-info :refer [info-from]]))

(defn concepts
  [{:keys [edit?] :as props}]
  (if edit?
    [edit-form props]
    [info-from props]))
