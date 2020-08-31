(ns webchange.interpreter.renderer.scene.components.group.group-wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.components.group.group-utils :as utils]))

(defn wrap
  [name container]
  (create-wrapper {:name           name
                   :type           :group
                   :container      container
                   :get-position   (fn []
                                     (utils/get-position container))
                   :set-position   (fn [position]
                                     (utils/set-position container position))
                   :set-visibility (fn [visible?]
                                     (utils/set-visibility container visible?))}))
