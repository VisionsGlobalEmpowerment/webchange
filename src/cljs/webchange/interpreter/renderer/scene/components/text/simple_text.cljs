(ns webchange.interpreter.renderer.scene.components.text.simple-text
  (:require
    [webchange.interpreter.pixi :refer [Text TextMetrics TextStyle]]
    [webchange.interpreter.renderer.scene.components.text.utils :as text-utils]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(defn- set-shadow
  [text {:keys [shadow-color shadow-distance shadow-blur shadow-opacity shadow-angle]}]
  (when-not (nil? shadow-color)
    (aset (.-style text) "dropShadow" true)
    (aset (.-style text) "dropShadowColor" shadow-color)
    (aset (.-style text) "dropShadowDistance" shadow-distance)
    (aset (.-style text) "dropShadowBlur" shadow-blur)
    (aset (.-style text) "dropShadowAlpha" shadow-opacity)
    (aset (.-style text) "dropShadowAngle" shadow-angle)))

(defn- set-skew
  [display-object skew-x skew-y]
  (.setTransform display-object 0 0 1 1 0 skew-x skew-y 0 0))

(defn- set-scale
  [object scale]
  (when (some? scale)
    (utils/set-scale object scale)))

(defn- get-font-props
  [{:keys [font-family font-size font-weight]}]
  (cond-> {:fontFamily font-family
           :fontWeight font-weight
           :fontSize   font-size}))

(defn- set-align
  [text {:keys [align vertical-align]}]
  (case align
    "left" (aset (.-anchor text) "x" 0)
    "center" (aset (.-anchor text) "x" 0.5)
    "right" (aset (.-anchor text) "x" 1)
    nil)
  (case vertical-align
    "top" (aset (.-anchor text) "y" 0)
    "middle" (aset (.-anchor text) "y" 0.5)
    "bottom" (aset (.-anchor text) "y" 1)
    nil))

(defn- calculate-position
  [{:keys [x y width height align vertical-align text] :as props}]
  (let [style (TextStyle. (clj->js (get-font-props props)))
        metrics (.measureText TextMetrics text style)
        top-empty-line (- (.-lineHeight metrics)
                          (.. metrics -fontProperties -ascent))]
    {:x (if (number? width)
          (case align
            "left" x
            "center" (+ x (/ width 2))
            "right" (+ x width))
          x)
     :y (if (number? height)
          (case vertical-align
            "top" (- y top-empty-line)
            "middle" (-> y
                         (+ (/ height 2))
                         (- (/ top-empty-line 2)))
            "bottom" (+ y height))
          y)}))

(defn create-simple-text
  [{:keys [align text font-family font-size font-weight fill scale skew-x skew-y width word-wrap on-click line-height] :as props}]
  (let [position (calculate-position props)
        text-object (doto (Text. text (clj->js (cond-> {:align         align
                                                        :fontFamily    font-family
                                                        :fontWeight    font-weight
                                                        :fill          fill
                                                        :wordWrapWidth width}
                                                       (some? line-height) (assoc :lineHeight line-height)
                                                       (some? font-size) (assoc :fontSize font-size)
                                                       (true? word-wrap) (assoc :wordWrap true))))
                      (set-skew skew-x skew-y)
                      (utils/set-position position)
                      (set-scale scale)
                      (set-shadow props)
                      (set-align props))]

    (text-utils/check-text-placeholder text-object props)

    (when-not (nil? on-click) (utils/set-handler text-object "click" on-click))

    text-object))
