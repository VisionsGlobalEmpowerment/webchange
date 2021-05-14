(ns webchange.editor-v2.activity-form.get-activity-type)

(defn get-activity-type
  [scene-data]
  (get-in scene-data [:metadata :template-name]))
