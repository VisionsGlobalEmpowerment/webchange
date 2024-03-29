(ns webchange.interpreter.renderer.scene.components.svg-path.utils
  (:require
    [webchange.interpreter.renderer.scene.components.svg-path :refer [get-svg-path]]))

(defn set-svg-path
  [texture ctx {:keys [data dash dash-offset fill width height]
                :or   {fill false}}]
  (.clearRect ctx 0 0 width height)

  (let [path (-> (get-svg-path data)
                 (js/Path2D.))]
    (when dash
      (.setLineDash ctx (clj->js dash)))

    (when dash-offset
      (set! ctx -lineDashOffset dash-offset))

    (if fill
      (.fill ctx path)
      (.stroke ctx path)))

  (.update texture))

(defn re-draw
  [ctx texture {:keys [data stroke stroke-width line-cap dash dash-offset fill scale]}]
  (doto ctx
    (set! -strokeStyle stroke)
    (set! -lineWidth stroke-width)
    (set! -lineCap line-cap)
    (.scale (:x scale) (:y scale)))

  (when (and fill (not (boolean? fill)))
    (set! (.-fillStyle ctx) fill))

  (set-svg-path texture ctx {:data data
                             :fill fill
                             :dash dash
                             :dash-offset dash-offset}))
