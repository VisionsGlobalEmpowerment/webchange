(ns webchange.interpreter.renderer.scene.components.svg-path.component
  (:require
    [webchange.interpreter.renderer.pixi :refer [Container Graphics Sprite Texture]]
    [webchange.interpreter.renderer.scene.components.svg-path.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.common.svg-path.path-splitter :as ps]))

(def default-props {:x            {}
                    :y            {}
                    :width        {}
                    :height       {}
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
        path (js/Path2D. data)]
    (when dash
      (.setLineDash ctx (clj->js dash)))
    (.stroke ctx path)
    (Sprite. (.from Texture canvas))))

(def component-type "svg-path")

(defn create
  [parent {:keys [type object-name group-name] :as props}]
  (let [container (create-container props)
        graphics (create-graphics props)
        wrapped-container (wrap type object-name group-name container)]

    (.addChild container graphics)
    (.addChild parent container)

    wrapped-container))
