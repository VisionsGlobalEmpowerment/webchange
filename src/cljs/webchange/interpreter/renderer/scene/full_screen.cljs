(ns webchange.interpreter.renderer.scene.full-screen)

(defn- fullscreen?
  []
  (or (-> js/document .-fullscreenElement some?)
      (-> js/document .-webkitIsFullScreen some?)
      (-> js/document .-mozFullScreen some?)))

(defn request-fullscreen
  ([]
   (request-fullscreen (.-documentElement js/document)))
  ([element]
   (cond
     (-> element .-requestFullscreen some?) (.requestFullscreen element)
     (-> element .-webkitrequestFullscreen some?) (.webkitrequestFullscreen element)
     (-> element .-mozRequestFullscreen some?) (.mozRequestFullscreen element)
     :else (js/Promise.reject))))

(defn exit-fullscreen
  []
  (cond
     (-> js/document .-exitFullscreen some?) (.exitFullscreen js/document)
     (-> js/document .-webkitExitFullscreen some?) (.webkitExitFullscreen js/document)
     (-> js/document .-mozCancelFullScreen some?) (.mozCancelFullScreen js/document)
     :else (js/Promise.reject)))

(defn lock-screen-orientation
  []
  (when (fullscreen?)
    (let [orientation (.-orientation js/screen)]
      (when (some? orientation)
        (-> (.lock orientation "landscape-primary")
            (.catch #()))))))
