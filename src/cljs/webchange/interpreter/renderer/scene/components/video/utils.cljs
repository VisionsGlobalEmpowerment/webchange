(ns webchange.interpreter.renderer.scene.components.video.utils
  (:require
    [webchange.interpreter.renderer.pixi :refer [Texture]]))

(defn- ->video
  [sprite]
  (.. sprite -texture -baseTexture -resource -source))

(defn- set-handler
  [video event handler]
  (case event
    "end" (aset video "onended" handler)))

(defn stop-video
  [sprite]
  (doto (->video sprite)
    (.pause)
    (aset "currentTime" 0)))

(defn play-video
  [sprite]
  (stop-video sprite)
  (-> (->video sprite)
      (.play)))

(defn set-src
  [sprite resource {:keys [play on-end]}]
  (let [video (.-data resource)
        texture (.fromVideo Texture video)]
    (aset sprite "texture" texture)
    (when-not (nil? on-end)
      (set-handler video "end" on-end))
    (if play
      (play-video sprite)
      (stop-video sprite))))
