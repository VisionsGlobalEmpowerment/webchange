(ns webchange.interpreter.renderer.scene.components.animated-svg-path.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics Sprite Texture]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.path :refer [paths]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.animation :as a]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.utils :as a-svg-utils]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

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
                    :scale        {:default {:x 1 :y 1}}})

(defn- create-container
  [{:keys [x y scale]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})
    (utils/set-scale scale)))

(defn- create-state
  [{:keys [path duration width height stroke-width line-cap] :as props}]
  (let [canvas (doto
                 (.createElement js/document "canvas")
                 (set! -width (* width 2))
                 (set! -height (* height 2)))
        ctx (doto
              (.getContext canvas "2d")
              (set! -lineWidth stroke-width)
              (set! -lineCap line-cap))

        texture (.from Texture canvas)
        state (atom {:ctx ctx :texture texture :paths (paths path duration)
                     :width     (* width 2) :height (* height 2) :duration duration})]
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
    :path - svg data
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
    :src - image src. Default: nil.
    :offset - container position offset. Default: {:x 0 :y 0}.
    :filters - filters params to be applied to sprite. Default: [].
    :border-radius - make rounded corners. Radius in pixels. Default: 0.
    :origin - where image pivot will be set. Can be '(left|center|right)-(top|center|bottom)'. Default: 'left-top'.
    :max-width - max image width.
    :max-height - max image height.
    :duration - animation duration in seconds"
  [{:keys [parent type object-name group-name] :as props}]
  (let [container (create-container props)
        state (create-state props)
        wrapped-container (wrap type object-name group-name container state)]

    (.addChild container (Sprite. (:texture @state)))
    (.addChild parent container)

    (a/stop state)
    wrapped-container))
