(ns webchange.interpreter.renderer.scene.components.group.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(defn wrap
  [type name container]
  (create-wrapper {:name           name
                   :type           type
                   :object         container
                   :container      container
                   :get-data       (fn []
                                     (merge (utils/get-stage-position container)))
                   :get-position   (fn []
                                     (utils/get-position container))
                   :set-position   (fn [position]
                                     (utils/set-position container position))
                   :set-scale      (fn [scale]
                                     (utils/set-scale container scale))
                   :set-visibility (fn [visible?]
                                     (utils/set-visibility container visible?))
                   :get-rotation   (fn []
                                     (utils/get-rotation container))
                   :set-rotation   (fn [value]
                                     (utils/set-rotation container value))}))
