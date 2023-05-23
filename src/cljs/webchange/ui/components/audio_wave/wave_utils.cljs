(ns webchange.ui.components.audio-wave.wave-utils
  (:require
    [webchange.ui.components.audio-wave.wave-core :as core]))

(def get-duration core/get-duration)
(def load-blob core/load-blob)
(def play core/play)
(def stop core/stop)


(defn add-region
  [wave-surfer region-data]
  (->> (clj->js region-data)
       (core/add-region wave-surfer)))

(defn destroy
  [ws]
  (when (some? ws)
    (core/destroy-all-plugins ws)
    (core/destroy ws)))

(defn get-param
  [ws param-name]
  (-> (core/get-params ws)
      (aget param-name)))

(defn get-zoom
  [ws]
  (let [drawer-width (-> (core/get-drawer ws)
                         (core/get-drawer-width))
        duration (get-duration ws)
        pixel-ratio (get-param ws "pixelRatio")]
    (/ drawer-width duration pixel-ratio)))

(defn inc-zoom
  [ws delta]
  (->> (get-zoom ws)
       (+ delta)
       (core/zoom ws)))

(defn set-center
  [ws progress]
  (-> (core/get-drawer ws)
      (core/recenter-drawer progress)))

(defn handle-event
  [ws event]
  (let [duration (get-duration ws)]
    (-> (core/get-drawer ws)
        (core/handle-drawer-event event)
        (* duration))))

(defn scroll-to-time
  [ws time]
  (let [duration (get-duration ws)
        progress (/ time duration)]
    (when (and (<= 0 progress) (>= 1 progress))
      (core/seek-to ws progress)
      (set-center ws progress))))

(defn set-audio-script
  [ws script-data]
  (->> (if (some? script-data)
         script-data
         [])
       (clj->js)
       (core/set-audio-script ws)))

(defn subscribe
  [ws event handler]
  (when (fn? handler)
    (core/subscribe ws event handler)))

(defn when-ready
  [ws callback]
  (if (core/is-ready? ws)
    (callback)
    (subscribe ws "ready" callback)))
