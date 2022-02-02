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

(defn- set-volume
  [video volume]
  (-> video
      (.-volume)
      (set! volume)))

(defn stop-video
  [sprite]
  (-> (->video sprite) (utils/stop)))

(defn play-video
  [sprite volume]
  (let [video (->video sprite)]
    (when (number? volume)
      (set-volume video volume))
    (utils/play video)))

(defn set-src
  [sprite resource {:keys [play start end on-end volume]
                    :or   {on-end #()}}]
  (let [src (.-url resource)
        texture (.from Texture src)
        _ (aset sprite "texture" texture)
        video (->video sprite)]
    (when (number? volume)
      (set-volume video volume))
    (if play
      (if (and (some? start) (some? end))
        (utils/play-range video start end on-end)
        (set-handler video "end" on-end))
      (utils/stop video))))
