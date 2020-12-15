(ns webchange.editor-v2.course-table.views-edit-form
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.views-edit-form-activity :refer [activity-form]]
    [webchange.editor-v2.course-table.views-edit-form-concepts :refer [concepts-form]]
    [webchange.editor-v2.course-table.views-edit-form-skills :refer [skills-form]]
    [webchange.editor-v2.course-table.views-edit-form-tags :refer [tags-form]]))

(def available-fields {:abbr-global skills-form
                       :activity    activity-form
                       :concepts    concepts-form
                       :skills      skills-form
                       :tags        tags-form})

(defn- default-form [] nil)

(defn edit-form
  []
  (let [{:keys [field activity]} @(re-frame/subscribe [::data-state/current-data])
        component (get available-fields field default-form)]
    [component activity]))
