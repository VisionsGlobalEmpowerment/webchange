(ns webchange.interpreter.renderer.scene.components.painting-area.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.components.painting-area.graphics :as graphics]))

(defn wrap
  [type name container tool tool-name color]
  (create-wrapper {:name   name
                   :type   type
                   :object container
                   :set-tool (fn [value]
                               (reset! tool-name value)
                               (reset! tool (graphics/create-tool @tool-name @color)))
                   :set-color (fn [value]
                                (reset! color value)
                                (reset! tool (graphics/create-tool @tool-name @color))
                                )}))
