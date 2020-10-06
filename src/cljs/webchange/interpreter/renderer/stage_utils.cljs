(ns webchange.interpreter.renderer.stage-utils)

(defn- compute-shift
  [display-size scale key]
  (let [dimension (get display-size key)]
    (-> (- scale 1)
        (* dimension)
        (/ 2))))

(defn- compute-scale
  [viewport {:keys [display-width display-height]}]
  (let [original-ratio (/ display-width display-height)
        window-ratio (/ (:width viewport) (:height viewport))]
    (if (> original-ratio window-ratio)
      (/ (:height viewport) display-height)
      (/ (:width viewport) display-width))))

(defn get-stage-params
  [viewport]
  (when (some? viewport)
    (let [display-size {:display-width  1920
                        :display-height 1080}
          scale (compute-scale viewport display-size)]
      (merge display-size
             {:x       (-> (compute-shift display-size scale :width) (Math/round))
              :y       (-> (compute-shift display-size scale :height) (Math/round))
              :width   (:width viewport)
              :height  (:height viewport)
              :scale-x scale
              :scale-y scale}))))
