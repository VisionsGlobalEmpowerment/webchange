(ns webchange.editor-v2.course-table.fields.skills.views-info
  (:require
    [webchange.editor-v2.course-table.fields.skills.views-edit :refer [selected-skills-list]]))

(defn info-from
  [{:keys [data field]}]
  [selected-skills-list {:skills     (:skills data)
                         :show-field field}])
