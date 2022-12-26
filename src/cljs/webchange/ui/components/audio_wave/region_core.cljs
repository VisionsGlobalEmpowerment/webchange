(ns webchange.ui.components.audio-wave.region-core)

(defn get-end
  [region]
  (.-end ^js region))

(defn get-start
  [region]
  (.-start ^js region))

(defn play
  [region]
  (.play ^js region))

(defn remove
  [region]
  (.remove ^js region))

(defn set-end
  [region value]
  (aset region "end" value))

(defn set-start
  [region value]
  (aset region "start" value))

(defn update-render
  [region]
  (.updateRender ^js region))

(defn get-id
  [region]
  (.-id ^js region))

(defn get-data
  [region]
  (-> (.-data ^js region)
      (js->clj)))

(defn set-data
  [region value]
  (aset region "data" value))
