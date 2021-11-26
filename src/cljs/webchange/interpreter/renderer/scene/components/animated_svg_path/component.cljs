(ns webchange.interpreter.renderer.scene.components.animated-svg-path.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics Sprite Texture]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.path :refer [paths]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.animation :as a]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.tracing :as t]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.utils :as a-svg-utils]
    [webchange.interpreter.renderer.scene.components.letters-path :refer [get-svg-path]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.logger.index :as logger]))

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
                    :duration     {}
                    :scale        {:default {:x 1 :y 1}}
                    :offset       {:default {:x 0 :y 0}}
                    :traceable    {}
                    :on-finish    {}
                    :active       {:default true}})

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})))

(defn- create-state
  [{:keys [path duration width height stroke-width line-cap scale offset] :as props}]
  (let [canvas (doto
                 (.createElement js/document "canvas")
                 (set! -width (* width (:x scale)))
                 (set! -height (* height (:y scale))))
        ctx (doto
              (.getContext canvas "2d")
              (set! -lineWidth stroke-width)
              (set! -lineCap line-cap)
              (.scale (:x scale) (:y scale))
              (.translate (:x offset) (:y offset)))
        svg-path (get-svg-path path {:trace? true})

        texture (.from Texture canvas)
        state (atom {:ctx      ctx
                     :texture  texture
                     :paths    (paths svg-path duration)
                     :width    width
                     :height   height
                     :duration duration
                     :enable?  true})]
    (a-svg-utils/set-stroke state (select-keys props [:stroke]))
    state))

(def component-type "animated-svg-path")

(defn create
  "Create `animated-svg-path` component.

    Props params:
    :x - component x-position.
    :y - component y-position.
    :width - image width.
    :height - image height.
    :scale - image scale. Default: {:x 1 :y 1}.
    :name - component name that will be set to sprite and container with corresponding suffixes.
    :path - svg data or letter
    :dash - An Array of numbers that specify distances to alternately draw a line and a gap (in coordinate space units).
            If the number of elements in the array is odd, the elements of the array get copied and concatenated.
            For example, [5, 15, 25] will become [5, 15, 25, 5, 15, 25]. If the array is empty, the line dash list is
            cleared and line strokes return to being solid.
    :stroke - line color. default #000000.
    :stroke-width - A number specifying the line width, in coordinate space units.
    :line-cap - determines the shape used to draw the end points of lines.
                'butt' The ends of lines are squared off at the endpoints.
                'round' The ends of lines are rounded.
                'square' The ends of lines are squared off by adding a box with an equal width and half the height of the line's thickness.

    :on-click - on click event handler.
    :ref - callback function that must be called with component wrapper.
    :offset - container position offset. Default: {:x 0 :y 0}.
    :duration - animation duration in seconds
    :traceable - flag indicating whether finger tracing allowed"
  [{:keys [parent type object-name group-name traceable] :as props}]
  (let [container (create-container props)
        state (create-state props)
        wrapped-container (wrap type object-name group-name container state)]
    (logger/trace-folded "Create animated-svg-path" props)
    (.addChild container (Sprite. (:texture @state)))

    (if traceable
      (.addChild container (t/create-trigger state props))
      (a/stop state))

    (.addChild parent container)

    wrapped-container))
