(ns webchange.interpreter.renderer.scene.components.painting-area.graphics
  (:require
    [webchange.interpreter.pixi :refer [Graphics blend-mode-erase string2hex line-cap line-join]]))

(defn- create-brush
  [color]
  (let [line-style (clj->js {:width 70
                             :color color
                             :cap   (.-ROUND line-cap)
                             :join  (.-ROUND line-join)})]
    (fn [current-pos last-pos]
      (doto (Graphics.)
        (.lineStyle line-style)
        (.moveTo (.-x last-pos) (.-y last-pos))
        (.lineTo (.-x current-pos) (.-y current-pos))))))

(defn- create-pencil
  [color]
  (let [line-style (clj->js {:width 10
                             :color color
                             :cap   (.-ROUND line-cap)
                             :join  (.-MITER line-join)})]
    (fn [current-pos last-pos]
      (doto (Graphics.)
        (.lineStyle line-style)
        (.moveTo (.-x last-pos) (.-y last-pos))
        (.lineTo (.-x current-pos) (.-y current-pos))))))

(defn- create-felt-tip
  [color]
  (let [line-style (clj->js {:width 25
                             :color color
                             :cap   (.-ROUND line-cap)
                             :join  (.-ROUND line-join)})]
    (fn [current-pos last-pos]
      (doto (Graphics.)
        (.lineStyle line-style)
        (.moveTo (.-x last-pos) (.-y last-pos))
        (.lineTo (.-x current-pos) (.-y current-pos))))))

(defn- create-eraser
  []
  (let [line-style (clj->js {:width 80
                             :color 0xff0000
                             :cap   (.-ROUND line-cap)
                             :join  (.-ROUND line-join)})]
    (fn [current-pos last-pos]
      (doto (Graphics.)
        (set! -blendMode blend-mode-erase)
        (.lineStyle line-style)
        (.moveTo (.-x last-pos) (.-y last-pos))
        (.lineTo (.-x current-pos) (.-y current-pos))))))

(defn create-tool
  [tool-name color]
  (let [color (if (number? color) color (string2hex color))]
    (case (keyword tool-name)
      :brush (create-brush color)
      :felt-tip (create-felt-tip color)
      :pencil (create-pencil color)
      :eraser (create-eraser))))
