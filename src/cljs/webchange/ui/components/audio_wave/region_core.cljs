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

(defn set-prop
  [region prop-name prop-value]
  (aset region prop-name prop-value))

(defn set-props
  [region props]
  (doseq [[prop-name prop-value] props]
    (set-prop region prop-name (clj->js prop-value))))
