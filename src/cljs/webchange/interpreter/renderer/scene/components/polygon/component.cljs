(ns webchange.interpreter.renderer.scene.components.polygon.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics Sprite WHITE]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.polygon.wrapper :refer [wrap]]))

(def default-props {:x    {}
                    :y    {}
                    :fill {}
                    :path {:default []}})

(defn- get-dimensions
  [{:keys [path]}]
  (let [bounds (reduce (fn [result [x y]]
                         (-> result
                             (update :x-min min x)
                             (update :x-max max x)
                             (update :y-min min y)
                             (update :y-max max y)))
                       {:x-min ##Inf
                        :x-max ##-Inf
                        :y-min ##Inf
                        :y-max ##-Inf}
                       path)]
    (merge bounds
           {:width  (:x-max bounds)
            :height (:y-max bounds)})))

(defn- create-mask
  [{:keys [path]}]
  (let [graphic (Graphics.)
        drawPolygon (.-drawPolygon graphic)
        points (clj->js (flatten path))]
    (.beginFill graphic 0x000000)
    (.apply drawPolygon graphic points)
    (.endFill graphic)
    graphic))

(defn- create-sprite
  [{:keys [fill width height]}]
  (doto (Sprite. WHITE)
    (aset "tint" fill)
    (aset "width" width)
    (aset "height" height)))

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})))

(def component-type "polygon")

(defn create
  "Create `polygon` component.

  Props params:
  :x - component x-position.
  :y - component y-position.
  :fill - color of internal area of rectangle.
  :path - array of points [x y]"
  [{:keys [parent type object-name] :as props}]
  (let [dimensions (get-dimensions props)

        mask (create-mask (merge props dimensions))
        sprite (create-sprite (merge props dimensions))
        container (create-container props)
        wrapped-component (wrap type object-name container)]

    (aset sprite "mask" mask)
    (.addChild container sprite)
    (.addChild container mask)
    (.addChild parent container)

    wrapped-component))
