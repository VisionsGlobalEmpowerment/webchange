(ns webchange.interpreter.renderer.scene.components.animated-svg-path.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.animation :refer [start stop reset]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.path :refer [paths]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.utils :as a-svg-utils]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.tracing :as tracing]
    [webchange.interpreter.renderer.scene.components.svg-path :refer [get-svg-path]]))

(defn wrap
  [type name group-name container state dimensions]
  (create-wrapper {:name                    name
                   :group-name              group-name
                   :type                    type
                   :object                  container
                   :state                   state
                   :animated-svg-path-start #(start state %)
                   :animated-svg-path-stop  #(stop state)
                   :animated-svg-path-reset #(reset state)
                   :set-path                #(swap! state assoc :paths (paths (get-svg-path % {:trace? true})
                                                                              (:duration @state)))
                   :set-stroke              #(a-svg-utils/set-stroke state %)
                   :activate                #(do
                                               (swap! state assoc :active true)
                                               (tracing/draw state {:path-idx 0 :point-idx 0} dimensions))
                   :set-enable              #(swap! state assoc :enable? %)}))
