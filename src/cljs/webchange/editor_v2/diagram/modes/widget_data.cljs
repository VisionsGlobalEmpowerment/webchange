(ns webchange.editor-v2.diagram.modes.widget-data
  (:require
    [webchange.editor-v2.diagram.modes.full-scene.widget-data :as full-scene]
    [webchange.editor-v2.diagram.modes.phrases.widget-data :as phrases]
    [webchange.editor-v2.diagram.modes.translation.widget-data :as translation]))

(defn get-widget-data
  [diagram-mode]
  (let [default-handlers {:get-node-custom-color (fn [_] nil)
                          :on-double-click       (fn [_] nil)}]
    (merge default-handlers
           (case diagram-mode
             :full-scene (full-scene/get-widget-data)
             :phrases (phrases/get-widget-data)
             :translation (translation/get-widget-data)
             {}))))
