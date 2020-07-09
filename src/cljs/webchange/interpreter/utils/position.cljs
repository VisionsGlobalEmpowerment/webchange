(ns webchange.interpreter.utils.position
  (:require
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]))

(defn get-viewbox
  [viewport]
  (let [width 1920
        height 1080
        original-ratio (/ width height)
        window-ratio (/ (:width viewport) (:height viewport))]
    (if (< original-ratio window-ratio)
      {:width width :height (Math/round (* height (/ original-ratio window-ratio)))}
      {:width (Math/round (* width (/ window-ratio original-ratio))) :height height})))

(defn compute-x
  [viewbox]
  (let [width 1920]
    (/ (- (:width viewbox) width) 2)))

(defn compute-y
  [viewbox]
  (let [height 1080]
    (/ (- height (:height viewbox)) 2)))

(defn compute-scale [viewport]
  (let [width 1920
        height 1080
        original-ratio (/ width height)
        window-ratio (/ (:width viewport) (:height viewport))]
    (if (> original-ratio window-ratio)
      (/ (:height viewport) height)
      (/ (:width viewport) width))))

(defn top-left
  []
  (let [viewport (re-frame/subscribe [::subs/viewport])
        viewbox (get-viewbox @viewport)
        scale (/ (:width viewbox) (:width @viewport))
        x (Math/round (* (compute-x viewbox) scale -1))
        y (Math/round (* (compute-y viewbox) scale))]
    {:x x :y y}))

(defn top-right
  []
  (let [left (top-left)
        viewport (re-frame/subscribe [::subs/viewport])
        viewbox (get-viewbox @viewport)
        scale (/ (:width viewbox) (:width @viewport))
        x (+ (* (:width @viewport) scale) (:x left))
        y (:y left)]
    {:x x :y y}))

(defn bottom-center
  []
  (let [right (top-right)
        x (/ (:x right) 2)
        y 1080]
    {:x x :y y}))
