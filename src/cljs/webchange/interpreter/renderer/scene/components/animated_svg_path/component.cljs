(ns webchange.interpreter.renderer.scene.components.animated-svg-path.component
  (:require
    [webchange.interpreter.renderer.pixi :refer [Container Graphics Sprite Texture]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.path :refer [paths]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.animation :as a]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.app :refer [add-ticker]]))

(def default-props {:x            {}
                    :y            {}
                    :width        {}
                    :height       {}
                    :name         {}
                    :path         {}
                    :dash         {}
                    :stroke       {:default "#000000"}
                    :stroke-width {}
                    :line-cap     {:default "round"}
                    :animation    {}
                    :duration     {}
                    :scale        {:default {:x 1 :y 1}}})

(defn- create-container
  [{:keys [x y scale]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})
    (utils/set-scale scale)))

(defn- create-state
  [{:keys [path duration width height stroke stroke-width line-cap animation]}]
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
    (atom {:animation animation :ctx ctx :texture texture :paths (paths path duration)
           :width (* width 2) :height (* height 2) :duration duration})))

(def component-type "animated-svg-path")

(defn create
  [parent {:keys [type object-name group-name] :as props}]
  (let [container (create-container props)
        state (create-state props)
        wrapped-container (wrap type object-name group-name container state)]

    (.addChild container (Sprite. (:texture @state)))
    (.addChild parent container)

    (a/stop state)
    wrapped-container))
