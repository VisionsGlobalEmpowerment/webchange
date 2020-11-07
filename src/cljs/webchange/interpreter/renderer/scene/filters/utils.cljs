(ns webchange.interpreter.renderer.scene.filters.utils)

(defn get-pivot
  [display-object]
  {:x (.. display-object -pivot -x)
   :y (.. display-object -pivot -y)})

(defn set-pivot
  [display-object {:keys [x y]}]
  (aset (.-pivot display-object) "x" x)
  (aset (.-pivot display-object) "y" y))

(defn get-position
  [display-object]
  {:x (aget display-object "x")
   :y (aget display-object "y")})

(defn set-position
  [display-object {:keys [x y]}]
  (aset display-object "x" x)
  (aset display-object "y" y))