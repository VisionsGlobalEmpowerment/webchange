(ns webchange.interpreter.renderer.scene.components.image.utils
  (:require
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.logger.index :as logger]))

(defn apply-boundaries
  [container {:keys [max-width max-height min-width min-height]}]
  (when (and min-width (> min-width (.-width container)))
    (let [ratio (/ min-width (.-width container))]
      (set! (.-width container) (* ratio (.-width container)))
      (set! (.-height container) (* ratio (.-height container)))))

  (when (and min-height (> min-height (.-height container)))
    (let [ratio (/ min-height (.-height container))]
      (set! (.-width container) (* ratio (.-width container)))
      (set! (.-height container) (* ratio (.-height container)))))

  (when (and max-width (< max-width (.-width container)))
    (let [ratio (/ max-width (.-width container))]
      (set! (.-width container) (* ratio (.-width container)))
      (set! (.-height container) (* ratio (.-height container)))))

  (when (and max-height (< max-height (.-height container)))
    (let [ratio (/ max-height (.-height container))]
      (set! (.-width container) (* ratio (.-width container)))
      (set! (.-height container) (* ratio (.-height container))))))

(defn apply-origin
  [container {{origin :type} :origin {x :x y :x} :offset}]
  (when (and origin (= 0 (int x)) (= 0 (int y)))
    (let [[h v] (clojure.string/split origin #"-")
          pivot (.-pivot container)
          local-bounds (.getLocalBounds container)]
      (case h
        "left" (set! (.-x pivot) 0)
        "center" (set! (.-x pivot) (/ (+ (.-x local-bounds) (.-width local-bounds)) 2))
        "right" (set! (.-x pivot) (+ (.-x local-bounds) (.-width local-bounds)))
        (do (logger/warn (str "Wrong horizontal align option <" h ">. 'Left' will be used."))
            (set! (.-x pivot) 0)))
      (case v
        "top" (set! (.-y pivot) 0)
        "center" (set! (.-y pivot) (/ (+ (.-y local-bounds) (.-height local-bounds)) 2))
        "bottom" (set! (.-y pivot) (+ (.-y local-bounds) (.-height local-bounds)))
        (do (logger/warn (str "Wrong vertical align option <" v ">. 'Top' will be used."))
            (set! (.-y pivot) 0))))))

(defn set-image-size
  [sprite {:keys [image-size width height scale]}]
  (cond
    (some? scale) (utils/set-scale sprite scale)
    (some? image-size) (let [sprite-size (utils/get-size sprite)
                             w-scale (/ width (:width sprite-size))
                             h-scale (/ height (:height sprite-size))
                             scale (case image-size
                                     "cover" (Math/max w-scale h-scale)
                                     "contain" (Math/min w-scale h-scale))
                             x (->> (:width sprite-size) (* scale) (- width) (* 0.5))
                             y (->> (:height sprite-size) (* scale) (- height) (* 0.5))]
                         (utils/set-scale sprite scale)
                         (utils/set-position sprite x y))
    :else (do (utils/set-not-nil-value sprite "width" width)
              (utils/set-not-nil-value sprite "height" height))))

(defn set-image-position
  [sprite {:keys [mask-width mask-height mask-align]}]
  (when (and (every? some? [mask-width mask-height mask-align]))
    (let [{:keys [width]} (utils/get-size sprite)
          diff (- (/ width 2) mask-width)
          position (case mask-align
                     "right-of-center" {:x (* -1 (- (/ width 2) diff))}
                     "left-of-center" {:x 0})]
      (utils/set-position sprite position))))
