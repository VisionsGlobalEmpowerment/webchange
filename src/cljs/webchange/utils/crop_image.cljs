(ns webchange.utils.crop-image)

(defn- load-image
  [image-blob callback]
  (let [src (.createObjectURL js/URL image-blob)]
    (doto (js/Image.)
      (aset "src" src)
      (.addEventListener "load" callback))))

(defn- create-canvas
  [width height]
  (let [canvas (doto (.createElement js/document "canvas")
                 (aset "width" width)
                 (aset "height" height))
        ctx (.getContext canvas "2d")]
    {:canvas canvas
     :ctx    ctx}))

(defn- get-image-dimensions
  [image]
  {:width      (.-width image)
   :width-half (-> (.-width image) (/ 2) (Math/ceil))
   :height     (.-height image)})

(defn- capture-image
  [canvas callback]
  (.toBlob canvas callback "image/png"))

(defn- draw-image
  [ctx image {:keys [x width height] :or {x 0}}]
  (.drawImage ctx image 0 0 width height x 0 width height))

(defn- get-half-image
  [image-blob callback {:keys [side] :or {side "left"}}]
  (load-image image-blob (fn [event]
                           (let [image (.-target event)
                                 {:keys [width width-half height]} (get-image-dimensions image)
                                 {:keys [canvas ctx]} (create-canvas width-half height)]
                             (draw-image ctx image {:x      (case side
                                                              "left" 0
                                                              "right" (- width-half))
                                                    :width  width
                                                    :height height})
                             (capture-image canvas callback)))))
