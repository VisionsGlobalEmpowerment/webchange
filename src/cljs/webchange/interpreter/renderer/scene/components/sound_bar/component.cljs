(ns webchange.interpreter.renderer.scene.components.sound-bar.component
  (:require
    [webchange.interpreter.pixi :refer [Container Sprite Texture WHITE]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.sound-bar.utils-mask :refer [create-mask! update-mask!]]
    [webchange.interpreter.renderer.scene.components.sound-bar.utils-value :refer [init-value!]]
    [webchange.interpreter.renderer.scene.components.sound-bar.wrapper :refer [wrap]]))

(def default-props {:x      {}
                    :y      {}
                    :width  {}
                    :height {}
                    :ref    {}})


(defn- create-gradient
  [width colors]
  (let [canvas (doto (.createElement js/document "canvas")
                 (aset "width" width)
                 (aset "height" 1))
        ctx (.getContext canvas "2d")
        gradient (.createLinearGradient ctx 0 0 width 0)]
    (doseq [[position color] colors]
      (.addColorStop gradient position color))
    (aset ctx "fillStyle" gradient)
    (.fillRect ctx 0 0 width 1)
    (.from Texture canvas)))

(defn- create-sprite
  [{:keys [width height]}]
  (let [gradient (create-gradient width [[0 "#ec96bd"] [1 "#90268e"]])]
    (doto (Sprite. gradient)
      (aset "width" width)
      (aset "height" height))))

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})))

(def component-type "sound-bar")

(defn create
  "Create `sound-bar` component.

  Props params:
  :x - component x-position.
  :y - component y-position.

"                                                           ;; ToDo: Update docs
  [{:keys [parent type object-name ref] :as props}]
  (let [state (atom {:size (select-keys props [:width :height])})
        sprite (create-sprite props)
        container (create-container props)
        wrapped-component (wrap type object-name container state)]
    (init-value! state)
    (create-mask! state)

    (aset sprite "mask" (:mask @state))
    (.addChild container sprite)
    (.addChild container (:mask @state))
    (.addChild parent container)

    (when-not (nil? ref) (ref wrapped-component))

    wrapped-component))
