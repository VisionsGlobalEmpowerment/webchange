(ns webchange.editor-v2.layout.utils)

(defn get-activity-type
  [scene-data]
  (get-in scene-data [:metadata :template-name]))
