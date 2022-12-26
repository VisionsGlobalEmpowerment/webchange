(ns webchange.interpreter.renderer.scene.components.svg-path.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.svg-path.utils :as utils]
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name group-name container texture canvas-ctx state]
  (create-wrapper {:name       name
                   :group-name group-name
                   :type       type
                   :object     container
                   :set-data   (fn [params]
                                 (utils/set-svg-path texture canvas-ctx params))
                   :set-fill   (fn [color]
                                 (swap! state assoc :fill color)
                                 (utils/re-draw canvas-ctx texture @state))
                   :set-stroke (fn [{:keys [stroke]}]
                                 (swap! state assoc :stroke stroke)
                                 (utils/re-draw canvas-ctx texture @state))}))
