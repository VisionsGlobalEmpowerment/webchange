(ns webchange.interpreter.renderer.scene.components.painting-area.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.app :as app]
    [webchange.interpreter.pixi :refer [Graphics blend-mode-erase]]
    [webchange.interpreter.renderer.scene.components.painting-area.graphics :as graphics]))

(defn wrap
  [type name container tool tool-name color texture width height]
  (create-wrapper {:name      name
                   :type      type
                   :object    container
                   :clear     (fn []
                                (let [
                                      tool (doto (Graphics.)
                                             (set! -blendMode blend-mode-erase)
                                             (.beginFill 0xff0000)
                                             (.drawRect 0 0 width height)
                                             (.endFill))
                                      ]
                                  (.render (app/get-renderer) tool texture false nil false)))
                   :set-tool  (fn [value]
                                (reset! tool-name value)
                                (reset! tool (graphics/create-tool @tool-name @color)))
                   :set-color (fn [value]
                                (reset! color value)
                                (reset! tool (graphics/create-tool @tool-name @color)))}))
