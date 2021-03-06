(ns webchange.interpreter.renderer.scene.components.video.utils
  (:require
    [webchange.interpreter.pixi :refer [Texture]]
    [webchange.interpreter.utils.video :as utils]))

(defn- ->video
  [sprite]
  (.. sprite -texture -baseTexture -resource -source))

(defn- set-handler
  [video event handler]
  (case event
    "end" (aset video "onended" handler)))

(defn stop-video
  [sprite]
  (-> (->video sprite) (utils/stop)))

(defn play-video
  [sprite]
  (stop-video sprite)
  (-> (->video sprite) (utils/play)))

(defn set-src
  [sprite resource {:keys [play start end on-end]
                    :or   {on-end #()}}]
  (let [video (.-data resource)
        texture (.from Texture video)]
    (aset sprite "texture" texture)
    (if play
      (if (and (some? start) (some? end))
        (utils/play-range video start end on-end)
        (do (set-handler video "end" on-end)
            (play-video sprite)))
      (stop-video sprite))))
