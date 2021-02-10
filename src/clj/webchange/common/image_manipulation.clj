(ns webchange.common.image-manipulation
  (:require
    [clojure.java.io :as io]
    [mikera.image.core :as imagez]))

(defn get-image-info [file]
  (let [image (imagez/load-image file)]
    {:width  (imagez/width image)
     :height (imagez/height image)}))

(defn calculate-scale
  [width height {:keys [max-width max-height min-width min-height]}]
  (cond
    (and max-width max-height (or (> width max-width) (> height max-height))) (min (/ max-width width) (/ max-height height))
    (and min-width min-height (or (< width min-width) (< height min-height))) (max (/ width min-width) (/ height min-height))
    :else 1))

(defn scale-to-window
  [file target options]
  (let [image (-> file imagez/load-image)
        width (imagez/width image)
        height (imagez/height image)
        scale (calculate-scale width height options)]
    (imagez/save
      (imagez/scale image scale)
      target)))
