(ns webchange.interpreter.renderer.rectangle
  (:require
    [cljsjs.pixi]
    [webchange.interpreter.renderer.common-utils :as utils]
    [webchange.interpreter.renderer.filters :refer [apply-outline-filter
                                                    apply-shadow-filter]]))

(def Container (.. js/PIXI -Container))
(def Graphics (.. js/PIXI -Graphics))
(def Sprite (.. js/PIXI -Sprite))
(def WHITE (.. js/PIXI -Texture -WHITE))

(def default-params {:x               :x
                     :y               :y
                     :width           :width
                     :height          :height
                     :fill            :fill
                     :border-radius   :border-radius
                     :border-width    :border-width
                     :border-color    :border-color
                     :shadow-color    :shadow-color
                     :shadow-distance :shadow-distance})

(def mask-params (utils/pick-params default-params [:width :height :border-radius]))
(def sprite-params (utils/pick-params default-params [:fill :width :height :border-width :border-color :shadow-color :shadow-distance]))
(def container-params (utils/pick-params default-params [:x :y]))

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

(defn create-rectangle
  [parent props]
  (let [mask (create-mask (utils/get-specific-params props mask-params))
        sprite (create-sprite (utils/get-specific-params props sprite-params))
        container (create-container (utils/get-specific-params props container-params))]

    (aset sprite "mask" mask)
    (.addChild container sprite)
    (.addChild container mask)
    (.addChild parent container)

    (utils/check-rest-props (str "Rectangle <" (:object-name props) ">")
                            props
                            mask-params
                            sprite-params
                            container-params
                            [:object-name :parent])))