(ns webchange.interpreter.renderer.stage-utils)

(defn- compute-x
  [viewbox]
  (let [width 1920]
    (/ (- (:width viewbox) width) 2)))

(defn- compute-y
  [viewbox]
  (let [height 1080]
    (/ (- height (:height viewbox)) 2)))

(defn- compute-scale
  [viewport]
  (let [width 1920
        height 1080
        original-ratio (/ width height)
        window-ratio (/ (:width viewport) (:height viewport))]
    (if (> original-ratio window-ratio)
      (/ (:height viewport) height)
      (/ (:width viewport) width))))

(defn- get-viewbox
  [viewport]
  (let [width 1920
        height 1080
        original-ratio (/ width height)
        window-ratio (/ (:width viewport) (:height viewport))]
    (if (< original-ratio window-ratio)
      {:width width :height (Math/round (* height (/ original-ratio window-ratio)))}
      {:width (Math/round (* width (/ window-ratio original-ratio))) :height height})))


(defn get-stage-params
  [viewport]
  (let [viewbox (get-viewbox viewport)]
    {:x       (Math/round (compute-x viewbox))
     :y       (Math/round (- (compute-y viewbox)))
     :width   (:width viewport)
     :height  (:height viewport)
     :scale-x (compute-scale viewport)
     :scale-y (compute-scale viewport)}))
