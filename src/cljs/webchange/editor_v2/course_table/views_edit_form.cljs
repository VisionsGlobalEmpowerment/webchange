(ns webchange.editor-v2.course-table.views-edit-form
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.data :as data-state]
    [webchange.editor-v2.course-table.views-edit-form-activity :refer [activity-form]]
    [webchange.editor-v2.course-table.views-edit-form-skills :refer [skills-form]]
    [webchange.editor-v2.course-table.views-edit-form-tags :refer [tags-form]]))

(def available-fields {:skills      skills-form
                       :abbr-global skills-form
                       :tags        tags-form
                       :activity    activity-form})

(defn field-editable?
  [{:keys [field]}]
  (contains? available-fields field))

(defn- default-form [] nil)

(defn edit-form
  []
  (let [{:keys [field activity]} @(re-frame/subscribe [::data-state/current-data])
        component (get available-fields field default-form)]
    [component activity]))
