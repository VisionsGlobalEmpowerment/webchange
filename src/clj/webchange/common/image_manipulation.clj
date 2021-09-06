(ns webchange.common.image-manipulation
  (:require
   [mikera.image.core :as imagez]
   [clojure.tools.logging :as log])
  (:import [javax.imageio ImageIO]))

(defn- try-image-size
  [reader is]
  (try
    (.setInput reader is)
    (let [width (.getWidth reader 0)
          height (.getHeight reader 0)]
      [width height])
    (catch Exception e
      (log/debug e))
    (finally
      (.dispose reader))))

(defn get-image-info
  "Get image size info without loading image into memory"
  [file]
  (with-open [is (ImageIO/createImageInputStream file)]
    (let [readers (-> (ImageIO/getImageReaders is)
                      iterator-seq)]
      (loop [[reader & tail] readers]
        (when reader
          (if-let [[width height] (try-image-size reader is)]
            {:width width :height height}
            (recur tail)))))))

(defn calculate-scale
  [width height {:keys [max-width max-height min-width min-height]}]
  (cond
    (and max-width max-height (or (> width max-width) (> height max-height)))
    (min (/ max-width width) (/ max-height height))

    (and min-width min-height (or (< width min-width) (< height min-height)))
    (max (/ width min-width) (/ height min-height))
    
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
