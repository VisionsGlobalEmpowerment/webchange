(ns webchange.interpreter.executor
  (:require
    [webchange.interpreter.core :refer [get-data create-tagged-key has-data put-data]]))

(defonce audio-ctx (atom nil))
(defonce music-gain (atom nil))
(defonce effects-gain (atom nil))

(defn music-volume
  [volume]
  (set! (.-value (.-gain @music-gain)) volume))

(defn effects-volume
  [volume]
  (set! (.-value (.-gain @effects-gain)) volume))

(defn init
  []
  (when-not @audio-ctx
    (do
      (reset! audio-ctx (if js/window.AudioContext.
                          (js/window.AudioContext.)
                          (js/window.webkitAudioContext.)))
      (reset! music-gain (.createGain @audio-ctx))
      (music-volume 0.5)
      (.connect @music-gain (.-destination @audio-ctx))

      (reset! effects-gain (.createGain @audio-ctx))
      (effects-volume 0.5)
      (.connect @effects-gain (.-destination @audio-ctx))
      )))

(defn create-buffered-source
  [data]
  (let [buffer-source (.createBufferSource @audio-ctx)]
    (set! (.-buffer buffer-source) data)
    buffer-source))

(defn connect
  ([destination]
   (fn [audio-node]
     (connect audio-node destination)))
  ([audio-node destination]
    (.connect audio-node destination)
    audio-node))

(defn get-audio
  [key destination]
  (let [buffer-key (create-tagged-key key "audioBuffer")]
    (when-not (has-data buffer-key)
      (let [data (get-data key)
            promise (.decodeAudioData @audio-ctx data)]
        (put-data promise buffer-key)))
    (let [buffer-data (get-data buffer-key)]
      (-> buffer-data
          (.then create-buffered-source)
          (.then (connect destination))))
    ))

(defn with-on-ended
  [cb]
  (fn [audio]
    (when cb
      (set! (.-onended audio) cb))
    audio))

(defn with-loop
  [loop]
  (fn [audio]
    (when loop
      (set! (.-loop audio) true))
    audio))

(defn start-audio
  ([offset start duration]
    (fn [audio]
      (.start audio (+ (.-currentTime @audio-ctx) offset) start duration)
      audio))
  ([offset start]
   (fn [audio]
     (.start audio (+ (.-currentTime @audio-ctx) offset) start)
     audio)))

(defn execute-audio-fx
  [{:keys [key start duration offset on-ended]}]
  (init)
  (let [audio (get-audio key @effects-gain)]
    (-> audio
        (.then (with-on-ended on-ended))
        (.then (start-audio offset start duration)))))

(defn execute-audio-music
  [{:keys [key start offset on-ended]}]
  (init)
  (let [audio (get-audio key @music-gain)]
    (-> audio
        (.then (with-on-ended on-ended))
        (.then (with-loop true))
        (.then (start-audio offset start)))))

(defn execute-audio
  [{:keys [loop] :as audio}]
  (if loop
    (execute-audio-music audio)
    (execute-audio-fx audio)))