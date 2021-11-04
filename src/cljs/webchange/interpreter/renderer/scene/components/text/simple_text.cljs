(ns webchange.interpreter.renderer.scene.components.text.simple-text
  (:require
    [webchange.interpreter.pixi :refer [Text]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(defn- set-shadow
  [text {:keys [shadow-color shadow-distance shadow-blur shadow-opacity]}]
  (when-not (nil? shadow-color)
    (aset (.-style text) "dropShadow" true)
    (aset (.-style text) "dropShadowColor" shadow-color)
    (aset (.-style text) "dropShadowDistance" shadow-distance)
    (aset (.-style text) "dropShadowBlur" shadow-blur)
    (aset (.-style text) "dropShadowAlpha" shadow-opacity)))

(defn- set-align
  [text {:keys [align vertical-align]}]
  (case align
    "left" (aset (.-anchor text) "x" 0)
    "center" (aset (.-anchor text) "x" 0.5)
    "right" (aset (.-anchor text) "x" 1))

  (case vertical-align
    "top" (aset (.-anchor text) "y" 0)
    "middle" (aset (.-anchor text) "y" 0.5)
    "bottom" (aset (.-anchor text) "y" 1)))

(defn- set-skew
  [display-object skew-x skew-y]
  (.setTransform display-object 0 0 1 1 0 skew-x skew-y 0 0))

(defn- set-scale
  [object scale]
  (when (some? scale)
    (utils/set-scale object scale)))

(defn- calculate-position
  [{:keys [x y width height align vertical-align]}]
  {:x (case align
        "left" x
        "center" (+ x (/ width 2))
        "right" (+ x width))
   :y (case vertical-align
        "top" y
        "middle" (+ y (/ height 2))
        "bottom" (+ y height))})

(defn create-simple-text
  [{:keys [align text font-family font-size font-weight fill scale skew-x skew-y width word-wrap on-click] :as props}]
  (let [position (calculate-position props)
        text-object (doto (Text. text (clj->js (cond-> {:align      align
                                                        :fontFamily font-family
                                                        :fontWeight font-weight
                                                        :fill       fill}
                                                       (some? font-size) (assoc :fontSize font-size)
                                                       (true? word-wrap) (-> (assoc :wordWrap true)
                                                                             (assoc :wordWrapWidth width)))))
                      (set-skew skew-x skew-y)
                      (utils/set-position position)
                      (set-scale scale)
                      (set-shadow props)
                      (set-align props))]

    (when-not (nil? on-click) (utils/set-handler text-object "click" on-click))

    text-object))
