(ns webchange.interpreter.executor
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.core :refer [get-data create-tagged-key has-data put-data]]
    [cljs.core.async :as a :refer [>! chan]]))

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
    (.connect buffer-source @effects-gain)
    buffer-source))

(defn get-audio
  [key]
  (let [buffer-key (create-tagged-key key "audioBuffer")]
    (when-not (has-data buffer-key)
      (let [data (get-data key)
            promise (.decodeAudioData @audio-ctx data)]
        (put-data promise buffer-key)))
    (let [buffer-data (get-data buffer-key)]
      (-> buffer-data
          (.then create-buffered-source)))
    ))

(defn with-on-ended
  [cb]
  (fn [audio]
    (when cb
      (set! (.-onended audio) cb))
    audio))

(defn start-audio
  [offset start duration]
  (fn [audio]
    (.start audio (+ (.-currentTime @audio-ctx) offset) start duration)
    audio))

(defn execute-audio
  [{:keys [key start duration offset on-ended]}]
  (init)
  (let [audio (get-audio key)]
    (-> audio
        (.then (with-on-ended on-ended))
        (.then (start-audio offset start duration)))))