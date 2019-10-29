(ns webchange.editor-v2.diagram.modes.widget-event-handlers
  (:require
    [webchange.editor-v2.diagram.modes.full-scene.widget-event-handlers :as full-scene]
    [webchange.editor-v2.diagram.modes.translation.widget-event-handlers :as translation]))

(defn get-widget-event-handlers
  [diagram-mode]
  (let [default-handlers {:get-node-custom-color (fn [_] nil)
                          :on-double-click       (fn [_] nil)}]
    (merge default-handlers
           (case diagram-mode
             :full-scene (full-scene/get-widget-event-handlers)
             :translation (translation/get-widget-event-handlers)))))
