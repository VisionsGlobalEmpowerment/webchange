(ns webchange.interpreter.renderer.scene.components.transparent.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(defn wrap
  [type name container]
  (create-wrapper {:name         name
                   :type         type
                   :container    container
                   :set-position (fn [position]
                                   (utils/set-position container position))}))
