(ns webchange.interpreter.renderer.scene.components.painting-area.graphics
  (:require
    [webchange.interpreter.pixi :refer [Graphics blend-mode-erase string2hex]]))

(defn- create-brush
  [color]
  (doto (Graphics.)
    (.beginFill color)
    (.drawCircle 0 0 70)
    (.endFill)))

(defn- create-pencil
  [color]
  (doto (Graphics.)
    (.beginFill color)
    (.drawCircle 0 0 10)
    (.endFill)))

(defn- create-felt-tip
  [color]
  (doto (Graphics.)
    (.beginFill color)
    (.drawCircle 0 0 25)
    (.endFill)))

(defn- create-eraser
  []
  (doto (Graphics.)
    (set! -blendMode blend-mode-erase)
    (.beginFill 0xff0000)
    (.drawCircle 0 0 80)
    (.endFill)))

(defn create-tool
  [tool-name color]
  (let [color (if (number? color) color (string2hex color))]
    (case (keyword tool-name)
      :brush (create-brush color)
      :felt-tip (create-felt-tip color)
      :pencil (create-pencil color)
      :eraser (create-eraser))))
