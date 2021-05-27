(ns webchange.editor-v2.activity-form.common.object-form.voice-over-control.utils
  (:require
    [webchange.editor-v2.activity-form.common.object-form.voice-over-control.utils-flipbook :as flipbook]
    [webchange.state.state :as state]
    [webchange.utils.scene-data :refer [get-template-name]]))

(defn get-actions-data
  [db selected-object-name]
  (case (-> (state/scene-data db)
            (get-template-name))
    "flipbook" (flipbook/get-actions-data db selected-object-name)
    nil))
