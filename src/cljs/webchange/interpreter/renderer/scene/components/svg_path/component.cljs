(ns webchange.interpreter.renderer.scene.components.svg-path.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics Sprite Texture]]
    [webchange.interpreter.renderer.scene.components.svg-path.utils :as svg-utils]
    [webchange.interpreter.renderer.scene.components.svg-path.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.logger.index :as logger]))

(def default-props {:x            {}
                    :y            {}
                    :width        {:default 225}            ;default width/height for svg letters
                    :height       {:default 225}            ;these defaults should be removed.
                    :name         {}
                    :data         {}
                    :dash         {}
                    :on-click     {}
                    :stroke       {:default "#000000"}
                    :fill         {:default false}
                    :stroke-width {}
                    :line-cap     {:default "round"}
                    :scale        {:default {:x 1 :y 1}}})

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})))

(defn- create-graphics
  [{:keys [data width height stroke stroke-width line-cap dash fill scale]}]
  (let [canvas (doto
                 (.createElement js/document "canvas")
                 (set! -width (* width (:x scale)))
                 (set! -height (* height (:y scale))))
        ctx (doto
              (.getContext canvas "2d")
              (set! -strokeStyle stroke)
              (set! -lineWidth stroke-width)
              (set! -lineCap line-cap)
              (.scale (:x scale) (:y scale)))
        texture (.from Texture canvas)]
    (when (and fill  (not (boolean? fill)))
      (doto
            (.getContext canvas "2d")
            (set! -fillStyle fill)))
    (svg-utils/set-svg-path texture ctx {:data data
                                         :fill fill
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
  :fill - fill shape instead of stroke.
  :stroke-width - A number specifying the line width, in coordinate space units.
  :line-cap - determines the shape used to draw the end points of lines.
              'butt' The ends of lines are squared off at the endpoints.
              'round' The ends of lines are rounded.
              'square' The ends of lines are squared off by adding a box with an equal width and half the height of the line's thickness."
  [{:keys [parent type object-name group-name on-click] :as props}]
  (let [container (create-container props)
        {:keys [sprite texture canvas-context]} (create-graphics props)
        wrapped-container (wrap type object-name group-name container texture canvas-context)]
    (logger/trace-folded "Create svg-path" props)
    (.addChild container sprite)
    (.addChild parent container)

    (when-not (nil? on-click) (utils/set-handler container "click" on-click))

    wrapped-container))
