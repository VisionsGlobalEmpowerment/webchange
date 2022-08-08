(ns webchange.ui.components.audio-wave.region-core)

(defn get-end
  [region]
  (.-end region))

(defn get-start
  [region]
  (.-start region))

(defn play
  [region]
  (.play region))

(defn remove
  [region]
  (.remove region))

(defn set-end
  [region value]
  (aset region "end" value))

(defn set-start
  [region value]
  (aset region "start" value))

(defn update-render
  [region]
  (.updateRender region))
