(ns webchange.interpreter.renderer.stage-utils
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.subs :as isubs]))

(defn- compute-shift
  [viewport target-size scale key]
  (let [viewport-dimension (get viewport key)
        target-dimension (get target-size key)]
    (->> (* scale target-dimension)
         (- viewport-dimension)
         (* 0.5))))

(defn- compute-scale
  [viewport target-size stage-size]
  (let [original-ratio (/ (:width target-size) (:height target-size))
        window-ratio (/ (:width viewport) (:height viewport))
        height-ratio (/ (:height viewport) (:height target-size))
        width-ratio (/ (:width viewport) (:width target-size))]
    (case stage-size
      :contain (if (< original-ratio window-ratio) height-ratio width-ratio)
      :cover (if (> original-ratio window-ratio) height-ratio width-ratio)
      (if (< original-ratio window-ratio) height-ratio width-ratio))))

(defn get-stage-params
  [viewport]
   (when (some? viewport)
     (let [stage-size @(re-frame/subscribe [::isubs/stage-size])
           target-size {:width  1920
                        :height 1080}
           scale (compute-scale viewport target-size stage-size)]
       {:x             (-> (compute-shift viewport target-size scale :width) (Math/round))
        :y             (-> (compute-shift viewport target-size scale :height) (Math/round))
        :width         (:width viewport)
        :height        (:height viewport)
        :target-width  (:width target-size)
        :target-height (:height target-size)
        :scale-x       scale
        :scale-y       scale})))
