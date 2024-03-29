(ns webchange.interpreter.renderer.scene.components.transparent.component
  (:require
    [webchange.interpreter.pixi :refer [Container Sprite Texture]]
    [webchange.interpreter.renderer.scene.components.transparent.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.logger.index :as logger]))

(def default-props {:x              {}
                    :y              {}
                    :width          {}
                    :height         {}
                    :name           {}
                    :on-click       {}
                    :on-pointerdown {}
                    :on-pointerover {}
                    :ref            {}
                    :offset         {:default {:x 0 :y 0}}
                    :scale          {:default {:x 1 :y 1}}})

(defn- create-container
  [{:keys [x y scale offset]}]
  (let [position {:x (- x (* (:x offset) (:x scale)))
                  :y (- y (* (:y offset) (:y scale)))}]
    (doto (Container.)
      (utils/set-position position)
      (utils/set-scale scale))))

(defn- create-sprite
  [{:keys [width height]}]
  (doto (Sprite. (.-EMPTY Texture))
    (utils/set-size {:width  width
                     :height height})))

(def component-type "transparent")

(defn create
  "Create `transparent` component.

  Props params:
  :x - component x-position.
  :y - component y-position.
  :width - image width.
  :height - image height.
  :name - component name that will be set to sprite and container with corresponding suffixes.
  :on-click - on click event handler.
  :ref - callback function that must be called with component wrapper.
  :offset - container position offset. Default: {:x 0 :y 0}.
  :scale - image scale. Default: {:x 1 :y 1}."
  [{:keys [parent type object-name on-click on-pointerdown on-pointerover ref] :as props}]
  (let [container (create-container props)
        sprite (create-sprite props)
        wrapped-container (wrap type object-name container)]

    (when on-click (utils/set-handler container "click" on-click))
    (when on-pointerdown (utils/set-handler container "pointerdown" on-pointerdown))
    (when on-pointerover (utils/set-handler container
                                            "pointermove"
                                            (utils/throttle #(when (and (.-target %)
                                                                        (= (name object-name)
                                                                           (.. % -target -name)))
                                                               (on-pointerover))
                                                            100)))
    (when ref (ref wrapped-container))

    (.addChild container sprite)
    (.addChild parent container)

    wrapped-container))
