(ns webchange.interpreter.renderer.scene.components.painting-area.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics Sprite Texture RenderTexture]]
    [webchange.interpreter.renderer.scene.components.painting-area.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.painting-area.graphics :refer [create-tool]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.app :as app]))

(def default-props {:x        {:default 0}
                    :y        {:default 0}
                    :width    {:default 1920}
                    :height   {:default 1080}
                    :name     {}
                    :on-click {}
                    :ref      {}
                    :scale    {:default {:x 1 :y 1}}
                    :tool     {:default "brush"}
                    :color    {:default "0xffffff"}})

(defn- create-container
  [{:keys [x y scale]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})
    (utils/set-scale scale)))

(defn- on-draw-start
  [event]
  (this-as this
    (let [pos (-> event .-data (.getLocalPosition (.-parent this)))]
      (set! (.-lastDrawPosition this) pos))
    (set! (.-data this) (.-data event))
    (set! (.-drawing this) true)))

(defn- on-draw-end
  []
  (this-as this
    (set! (.-data this) nil)
    (set! (.-drawing this) false)))

(defn- on-draw-move
  [texture atool]
  (fn []
    (this-as this
      (when (.-drawing this)
        (let [pos (-> this .-data (.getLocalPosition (.-parent this)))
              last-pos (.-lastDrawPosition this)
              graphic (@atool pos last-pos)]
          (.render (app/get-renderer)
                   graphic
                   texture false nil false)
          (.destroy graphic)
          (set! (.-lastDrawPosition this) pos))))))

(defn- create-trigger
  [texture tool {:keys [width height]}]
  (doto (Sprite. (.-EMPTY Texture))
    (utils/set-size {:width width :height height})
    (set! -interactive true)
    (.on "pointerdown" on-draw-start)
    (.on "pointerup" on-draw-end)
    (.on "pointerupoutside" on-draw-end)
    (.on "pointermove" (on-draw-move texture tool))))

(defn- create-render-texture
  [{:keys [width height]}]
  (.create RenderTexture #js {:width width :height height}))

(def component-type "painting-area")

(defn create
  "Create `painting-area` component.

  Props params:
  :x - component x-position.
  :y - component y-position.
  :width - image width.
  :height - image height.
  :scale - image scale. Default: {:x 1 :y 1}.
  :name - component name that will be set to sprite and container with corresponding suffixes.
  :on-click - on click event handler.
  :ref - callback function that must be called with component wrapper.
  :tool - tool which will be used for painting. Default brush. Possible options brush, felt-tip, pencil, eraser
  :color - drawing color. Default 0xffffff"
  [{:keys [parent type object-name tool color width height on-click] :as props}]
  (let [container (create-container props)
        texture (create-render-texture props)
        color (atom color)
        tool-name (atom tool)
        tool (atom (create-tool @tool-name @color))
        trigger (create-trigger texture tool props)
        wrapped-container (wrap type object-name container tool tool-name color texture width height)]

    (when-not (nil? on-click) (utils/set-handler container "click" on-click))

    (.addChild container (Sprite. texture))
    (.addChild container trigger)
    (.addChild parent container)

    wrapped-container))
