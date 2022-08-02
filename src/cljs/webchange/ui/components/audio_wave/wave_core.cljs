(ns webchange.ui.components.audio-wave.wave-core)

(defn add-region
  [ws region-data]
  (.addRegion ws region-data))

(defn destroy
  [ws]
  (.destroy ws))

(defn destroy-all-plugins
  [ws]
  (.destroyAllPlugins ws))

(defn get-drawer
  [ws]
  (.-drawer ws))

(defn- get-drawer-width
  [drawer]
  (.-width drawer))

(defn get-duration
  [ws]
  (.getDuration ws))

(defn get-params
  [ws]
  (.-params ws))

(defn load-blob
  [ws blob]
  (.loadBlob ws blob))

(defn play
  [ws]
  (.play ws))

(defn recenter-drawer
  [drawer progress]
  (.recenter drawer progress))

(defn seek-to
  [ws progress]
  (.seekTo ws progress))

(defn stop
  [ws]
  (.stop ws))

(defn subscribe
  [ws event handler]
  (.on ws event handler))

(defn zoom
  [ws value]
  (.zoom ws value))
