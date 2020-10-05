(ns webchange.interpreter.renderer.scene.full-screen)

(defn- fullscreen?
  []
  (some? (.-fullscreenElement js/document)))

(defn request-fullscreen
  [callback]
  (let [document-element (.-documentElement js/document)]
    (when (some? (.-requestFullscreen document-element))
      (-> (.requestFullscreen document-element)
          (.then callback)))))

(defn lock-screen
  []
  (when (fullscreen?)
    (let [orientation (.-orientation js/screen)]
      (when (some? (.-lock orientation))
        (.lock orientation "landscape-primary")))))
