(ns webchange.interpreter.renderer.scene.components.svg-path.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics Sprite Texture]]
    [webchange.interpreter.renderer.scene.components.svg-path.utils :as svg-utils]
    [webchange.interpreter.renderer.scene.components.svg-path.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(def default-props {:x            {}
                    :y            {}
                    :width        {:default 100}
                    :height       {:default 100}
                    :name         {}
                    :data         {}
                    :dash         {}
                    :stroke       {:default "#000000"}
                    :stroke-width {}
                    :line-cap     {:default "round"}
                    :scale        {:default {:x 1 :y 1}}})

(defn- create-container
  [{:keys [x y scale]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})
    (utils/set-scale scale)))

(defn- create-graphics
  [{:keys [data width height stroke stroke-width line-cap dash]}]
  (let [canvas (doto
                 (.createElement js/document "canvas")
                 (set! -width (* width 2))
                 (set! -height (* height 2)))
        ctx (doto
              (.getContext canvas "2d")
              (set! -strokeStyle stroke)
              (set! -lineWidth stroke-width)
              (set! -lineCap line-cap))
        texture (.from Texture canvas)]
    (svg-utils/set-svg-path texture ctx {:data data
                                         :dash dash})
    {:sprite         (Sprite. texture)
     :texture        texture
     :canvas-context ctx}))

(def component-type "svg-path")

(defn create
  [{:keys [parent type object-name group-name] :as props}]
  (let [container (create-container props)
        {:keys [sprite texture canvas-context]} (create-graphics props)
        wrapped-container (wrap type object-name group-name container texture canvas-context)]

    (.addChild container sprite)
    (.addChild parent container)

    wrapped-container))
