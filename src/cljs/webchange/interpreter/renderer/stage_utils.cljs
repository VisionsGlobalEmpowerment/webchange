(ns webchange.interpreter.renderer.stage-utils)

(defn- compute-shift
  [viewport target-size scale key]
  (let [viewport-dimension (get viewport key)
        target-dimension (get target-size key)]
    (->> (* scale target-dimension)
         (- viewport-dimension)
         (* 0.5))))

(defn- compute-scale
  [viewport target-size]
  (let [original-ratio (/ (:width target-size) (:height target-size))
        window-ratio (/ (:width viewport) (:height viewport))]
    (if (> original-ratio window-ratio)
      (/ (:height viewport) (:height target-size))
      (/ (:width viewport) (:width target-size)))))

(defn get-stage-params
  [viewport]
  (when (some? viewport)
    (let [target-size {:width  1920
                       :height 1080}
          scale (compute-scale viewport target-size)]
      {:x             (-> (compute-shift viewport target-size scale :width) (Math/round))
       :y             (-> (compute-shift viewport target-size scale :height) (Math/round))
       :width         (:width viewport)
       :height        (:height viewport)
       :target-width  (:width target-size)
       :target-height (:height target-size)
       :scale-x       scale
       :scale-y       scale})))
