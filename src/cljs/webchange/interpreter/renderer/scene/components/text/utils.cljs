(ns webchange.interpreter.renderer.scene.components.text.utils
  (:require
    [webchange.interpreter.pixi :refer [Text]]))

(defn- text?
  [display-object]
  (instance? Text display-object))

(defn- get-style-property
  [text-object property]
  (if (text? text-object)
    (aget (.-style text-object) property)
    (-> (str "Object is not a PIXI.Text") js/Error. throw)))

(defn- set-style-property
  [text-object property value]
  (if (text? text-object)
    (aset (.-style text-object) property value)
    (-> (str "Object is not a PIXI.Text") js/Error. throw)))

(defn get-fill
  [text-object]
  (get-style-property text-object "fill"))

(defn set-fill
  [text-object fill]
  (set-style-property text-object "fill" fill))

(defn set-font-size
  [text-object font-size]
  (set-style-property text-object "fontSize" font-size))

(defn set-font-family
  [text-object font-family]
  (set-style-property text-object "fontFamily" font-family))
