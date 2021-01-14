(ns webchange.interpreter.renderer.scene.components.svg-path.utils)

(defn set-svg-path
  [texture ctx {:keys [data dash fill]
                :or   {fill false}}]
  (let [path (js/Path2D. data)]
    (when dash
      (.setLineDash ctx (clj->js dash)))
    (if fill
      (.fill ctx path)
      (.stroke ctx path))
    (.update texture)))
