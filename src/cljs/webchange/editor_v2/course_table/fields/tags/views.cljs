(ns webchange.editor-v2.course-table.fields.tags.views
  (:require
    [webchange.editor-v2.course-table.fields.tags.views-edit :refer [edit-form]]
    [webchange.editor-v2.course-table.fields.tags.views-info :refer [info-from]]))

(defn tags
  [{:keys [edit?] :as props}]
  (if edit?
    [edit-form props]
    [info-from props]))
