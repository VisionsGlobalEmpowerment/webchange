(ns webchange.interpreter.renderer.scene.components.rectangle.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics Sprite WHITE]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-outline-filter
                                                                  apply-shadow-filter]]
    [webchange.interpreter.renderer.scene.components.rectangle.wrapper :refer [wrap]]))

(def default-props {:x               {}
                    :y               {}
                    :width           {}
                    :height          {}
                    :fill            {}
                    :border-radius   {}
                    :border-width    {}
                    :border-color    {}
                    :shadow-color    {}
                    :shadow-distance {}})

(defn- create-mask
  [{:keys [width height border-radius]}]
  (doto (Graphics.)
    (.beginFill 0x000000)
    (.drawRoundedRect 0 0 width height border-radius)
    (.endFill 0x000000)))

(defn- create-sprite
  [{:keys [fill width height border-width border-color shadow-color shadow-distance]}]
  (let [sprite (doto (Sprite. WHITE)
                 (aset "tint" fill)
                 (aset "width" width)
                 (aset "height" height)
                 (apply-outline-filter {:width border-width
                                        :color border-color}))]
    (when (or (-> shadow-color nil? not)
              (-> shadow-distance nil? not))
      (apply-shadow-filter sprite {:color    shadow-color
                                   :distance shadow-distance}))
    sprite))

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})))

(def component-type "rectangle")

(defn create
  "Create `rectangle` component.

  Props params:
  :x - component x-position.
  :y - component y-position.
  :width - image width.
  :height - image height.
  :fill - color of internal area of rectangle.
  :border-radius - make rounded corners. Radius in pixels. Default: 0.
  :border-width - width of of border in pixels
  :border-color - color of border
  :shadow-color - color of shadow. E.g. 0x75016e
  :shadow-distance - width of shadow."
  [{:keys [parent type object-name] :as props}]
  (let [mask (create-mask props)
        sprite (create-sprite props)
        container (create-container props)]

    (aset sprite "mask" mask)
    (.addChild container sprite)
    (.addChild container mask)
    (.addChild parent container)

    (wrap type object-name container)))
