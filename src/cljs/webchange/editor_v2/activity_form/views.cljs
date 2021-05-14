(ns webchange.editor-v2.activity-form.views
  (:require
    [webchange.editor-v2.activity-form.flipbook.views :as flipbook]
    [webchange.editor-v2.activity-form.generic.views :as generic]
    [webchange.editor-v2.activity-form.get-activity-type :refer [get-activity-type]]))

(defn activity-form
  [{:keys [scene-data]}]
  (let [activity-type (get-activity-type scene-data)
        form-props {:scene-data scene-data}]
    (case activity-type
      "flipbook" [flipbook/activity-form form-props]
      [generic/activity-form form-props])))
