(ns webchange.interpreter.renderer.scene.components.painting-area.component
  (:require
    [webchange.interpreter.renderer.pixi :refer [Container Sprite Texture RenderTexture]]
    [webchange.interpreter.renderer.scene.components.painting-area.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.painting-area.graphics :refer [create-tool]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.app :as app]))

(def default-props {:x        {}
                    :y        {}
                    :width    {}
                    :height   {}
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
      (set! (.-lastDrawPosition this) {:x (.-x pos) :y (.-y pos)}))
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
              tool @atool]
          (-> tool .-position (.copyFrom pos))
          (.render (app/get-renderer) tool texture false nil false))))))

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
  [{:keys [parent type object-name tool color] :as props}]
  (let [container (create-container props)
        texture (create-render-texture props)
        color (atom color)
        tool-name (atom tool)
        tool (atom (create-tool @tool-name @color))
        trigger (create-trigger texture tool props)
        wrapped-container (wrap type object-name container tool tool-name color)]

    (.addChild container (Sprite. texture))
    (.addChild container trigger)
    (.addChild parent container)

    wrapped-container))
