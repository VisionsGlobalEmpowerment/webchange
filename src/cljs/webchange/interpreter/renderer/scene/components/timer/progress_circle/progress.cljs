(ns webchange.interpreter.renderer.scene.components.timer.progress-circle.progress
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics Sprite WHITE]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x
                         :y y})))

(defn- create-mask
  [position]
  (doto (Graphics.)
    (utils/set-position position)))

(defn- create-sprite
  [{:keys [size color]} position]
  (doto (Sprite. WHITE)
    (aset "tint" color)
    (aset "width" size)
    (aset "height" size)
    (utils/set-position position)))

(defn- set-progress
  [mask {:keys [radius size thickness]} value]
  (let [fixed-value (-> value (Math/max 0) (Math/min 1))
        progress->angle #(* 2 % Math/PI)
        center (/ size 2)
        start-angle (* Math/PI 1.5)]
    (doto mask
      (.clear)
      (.lineStyle thickness 0x000000 1)
      (.arc center center radius start-angle (->> (progress->angle fixed-value) (+ start-angle))))))

(defn progress
  [{:keys [radius thickness progress] :as props
    :or   {progress 0}}]
  (let [size (+ (* radius 2) thickness)
        coordinate (- 0 radius (/ thickness 2))
        position {:x coordinate
                  :y coordinate}
        updated-props (merge props
                             {:size size})
        mask (create-mask position)
        set-progress (partial set-progress mask updated-props)
        sprite (create-sprite updated-props position)
        container (create-container updated-props)]
    (set-progress progress)
    (aset sprite "mask" mask)
    (.addChild container mask)
    (.addChild container sprite)

    {:component container
     :set-value (fn [value]
                  (set-progress value))}))
