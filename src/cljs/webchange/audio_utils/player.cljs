(ns webchange.audio-utils.player)

(defn create
  [audio-blob]
  (when-not (nil? audio-blob)
    (->> (.createObjectURL js/URL audio-blob)
         (js/Audio.))))

(defn play
  [audio-object]
  (when-not (nil? audio-object)
    (.play audio-object)))

(defn stop
  [audio-object]
  (when-not (nil? audio-object)
    (.pause audio-object)
    (aset audio-object "currentTime" 0)))

(defn on-ended
  [audio-object callback]
  (when-not (nil? audio-object)
    (.addEventListener audio-object "ended" callback)))
