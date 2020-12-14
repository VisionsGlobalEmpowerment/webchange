(ns webchange.editor-v2.course-table.fields.activities.views
  (:require
    [webchange.editor-v2.course-table.fields.activities.views-edit :refer [edit-form]]
    [webchange.editor-v2.course-table.fields.activities.views-info :refer [info-from]]))

(defn activities
  [{:keys [edit?] :as props}]
  (if edit?
    [edit-form props]
    [info-from props]))
