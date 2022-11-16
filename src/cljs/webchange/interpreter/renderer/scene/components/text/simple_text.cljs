(ns webchange.interpreter.renderer.scene.components.text.simple-text
  (:require
    [webchange.interpreter.pixi :refer [Text]]
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

(defn create-simple-text
  [{:keys [align text font-family font-size font-weight fill skew-x skew-y width word-wrap on-click line-height] :as props}]
  (let [position (text-utils/calculate-position props)
        text-object (doto (Text. text (clj->js (cond-> {:align         align
                                                        :fontFamily    font-family
                                                        :fontWeight    font-weight
                                                        :fill          fill
                                                        :wordWrapWidth width
                                                        :padding       30}
                                                       (some? line-height) (assoc :lineHeight line-height)
                                                       (some? font-size) (assoc :fontSize font-size)
                                                       (true? word-wrap) (assoc :wordWrap true))))
                      (set-skew skew-x skew-y)
                      (utils/set-position position)
                      (set-shadow props)
                      (set-align props))]

    (text-utils/check-text-placeholder text-object props)

    (when-not (nil? on-click) (utils/set-handler text-object "click" on-click))

    text-object))
