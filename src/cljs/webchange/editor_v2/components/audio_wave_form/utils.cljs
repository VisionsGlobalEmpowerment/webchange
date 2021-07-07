(ns webchange.editor-v2.components.audio-wave-form.utils)

(defn- get-drawer
  [ws]
  (.-drawer ws))

(defn- get-drawer-client-width
  [ws]
  (-> (get-drawer ws)
      (.getWidth)))

(defn- get-drawer-full-width
  [ws]
  (-> (get-drawer ws)
      (.-width)))

(defn get-duration
  [ws]
  (.getDuration ws))

(defn- get-params
  [ws]
  (.-params ws))

(defn- get-pixel-ratio
  [ws]
  (-> (get-params ws)
      (.-pixelRatio)))

(defn- get-current-zoom
  [ws]
  (/ (get-drawer-full-width ws)
     (get-duration ws)
     (get-pixel-ratio ws)))

(defn set-center
  [ws value]
  (-> (get-drawer ws)
      (.recenter value)))

(defn center-to-time
  [ws time]
  (let [duration (get-duration ws)
        center-value (/ time duration)]
    (set-center ws center-value)))

(defn zoom
  [ws value]
  (.zoom ws value))

(defn inc-zoom
  [ws delta]
  (->> (get-current-zoom ws)
       (+ delta)
       (zoom ws)))
