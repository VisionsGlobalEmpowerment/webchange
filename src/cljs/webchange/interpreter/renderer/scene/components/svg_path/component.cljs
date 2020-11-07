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
  "Create `svg-path` component.

  Props params:
  :x - component x-position.
  :y - component y-position.
  :width - image width.
  :height - image height.
  :scale - image scale. Default: {:x 1 :y 1}.
  :name - component name that will be set to sprite and container with corresponding suffixes.
  :data - string with svg data. e.g M144.76,92.43a37.5,37.5,0,1,0,0,39.28m0-57.21v75
  :dash - An Array of numbers that specify distances to alternately draw a line and a gap (in coordinate space units). If the number of elements in the array is odd, the elements of the array get copied and concatenated. For example, [5, 15, 25] will become [5, 15, 25, 5, 15, 25]. If the array is empty, the line dash list is cleared and line strokes return to being solid.
  :stroke - line color. default #000000.
  :stroke-width - A number specifying the line width, in coordinate space units.
  :line-cap - determines the shape used to draw the end points of lines.
              'butt' The ends of lines are squared off at the endpoints.
              'round' The ends of lines are rounded.
              'square' The ends of lines are squared off by adding a box with an equal width and half the height of the line's thickness."
  [{:keys [parent type object-name group-name] :as props}]
  (let [container (create-container props)
        {:keys [sprite texture canvas-context]} (create-graphics props)
        wrapped-container (wrap type object-name group-name container texture canvas-context)]

    (.addChild container sprite)
    (.addChild parent container)

    wrapped-container))
