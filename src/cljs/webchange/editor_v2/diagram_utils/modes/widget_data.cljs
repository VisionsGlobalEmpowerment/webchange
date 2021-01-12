(ns webchange.editor-v2.diagram-utils.modes.widget-data
  (:require
    [webchange.editor-v2.diagram-utils.modes.full-scene.widget-data :as full-scene]
    [webchange.editor-v2.diagram-utils.modes.phrases.widget-data :as phrases]
    [webchange.editor-v2.diagram-utils.modes.translation.widget-data :as translation]
    [webchange.editor-v2.diagram-utils.modes.dialog.widget-data :as dialog]
    [webchange.editor-v2.diagram-utils.modes.question.widget-data :as question]
    ))

(defn get-widget-data
  [diagram-mode]
  (let [default-handlers {:get-node-custom-color (fn [_] nil)
                          :on-double-click       (fn [_] nil)}]
    (merge default-handlers
           (case diagram-mode
             :full-scene (full-scene/get-widget-data)
             :phrases (phrases/get-widget-data)
             :translation (translation/get-widget-data)
             :dialog (dialog/get-widget-data)
             :question (question/get-widget-data)
             {}))))
