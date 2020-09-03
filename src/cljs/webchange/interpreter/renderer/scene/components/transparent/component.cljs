(ns webchange.interpreter.renderer.scene.components.transparent.component
  (:require
    [webchange.interpreter.renderer.pixi :refer [Container Rectangle]]
    [webchange.interpreter.renderer.scene.components.transparent.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(def default-props {:x        {}
                    :y        {}
                    :width    {}
                    :height   {}
                    :name     {}
                    :on-click {}})

(defn- create-container
  [{:keys [x y width height]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})
    (utils/set-size {:width width :height height})
    (set! -hitArea (Rectangle. 0 0 width height))))

(def component-type "transparent")

(defn create
  [parent {:keys [type object-name on-click] :as props}]
  (let [container (create-container props)]
    (when on-click
      (utils/set-handler container "click" on-click))
    (.addChild parent container)
    (wrap type object-name container)))
