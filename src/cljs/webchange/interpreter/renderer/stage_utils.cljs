(ns webchange.interpreter.renderer.stage-utils)

(defn- get-display-size
  []
  {:width  1920
   :height 1080})

(defn- compute-x
  [viewbox {:keys [display-width]}]
  (/ (- (:width viewbox) display-width) 2))

(defn- compute-y
  [viewbox {:keys [display-height]}]
  (/ (- display-height (:height viewbox)) 2))

(defn- compute-scale
  [viewport {:keys [display-width display-height]}]
  (let [
        original-ratio (/ display-width display-height)
        window-ratio (/ (:width viewport) (:height viewport))]
    (if (> original-ratio window-ratio)
      (/ (:height viewport) display-height)
      (/ (:width viewport) display-width))))

(defn- get-viewbox
  [viewport {:keys [display-width display-height]}]
  (let [
        original-ratio (/ display-width display-height)
        window-ratio (/ (:width viewport) (:height viewport))]
    (if (< original-ratio window-ratio)
      {:width display-width :height (Math/round (* display-height (/ original-ratio window-ratio)))}
      {:width (Math/round (* display-width (/ window-ratio original-ratio))) :height display-height})))


(defn get-stage-params
  [viewport]
  (let [display-size {:display-width  1920
                      :display-height 1080}
        viewbox (get-viewbox viewport display-size)]
    (merge display-size
           {:x       (Math/round (compute-x viewbox display-size))
            :y       (Math/round (- (compute-y viewbox display-size)))
            :width   (:width viewport)
            :height  (:height viewport)
            :scale-x (compute-scale viewport display-size)
            :scale-y (compute-scale viewport display-size)})))
