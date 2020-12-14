(ns webchange.editor-v2.course-table.fields.skills.views
  (:require
    [webchange.editor-v2.course-table.fields.skills.views-edit :refer [edit-form]]
    [webchange.editor-v2.course-table.fields.skills.views-info :refer [info-from]]))

(defn skills
  [{:keys [edit?] :as props}]
  (if edit?
    [edit-form props]
    [info-from props]))
