(ns webchange.interpreter.renderer.scene.components.svg-path.utils)

(defn set-svg-path
  [texture ctx {:keys [data dash]}]
  (let [path (js/Path2D. data)]
    (when dash
      (.setLineDash ctx (clj->js dash)))
    (.stroke ctx path)
    (.update texture)))
