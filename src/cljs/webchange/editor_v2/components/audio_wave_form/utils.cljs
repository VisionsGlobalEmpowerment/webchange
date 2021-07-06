(ns webchange.editor-v2.components.audio-wave-form.utils)

(defn- get-drawer
  [ws]
  (.-drawer ws))

(defn get-duration
  [ws]
  (.getDuration ws))

(defn set-center
  [ws value]
  (-> (get-drawer ws)
      (.recenter value)))

(defn center-to-time
  [ws time]
  (let [duration (get-duration ws)
        center-value (/ time duration)]
    (set-center ws center-value)))
