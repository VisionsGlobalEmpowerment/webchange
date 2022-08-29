(ns webchange.ui.components.audio-wave.wave-core)

(defn add-region
  [ws region-data]
  (.addRegion ^js ws region-data))

(defn destroy
  [ws]
  (.destroy ^js ws))

(defn destroy-all-plugins
  [ws]
  (.destroyAllPlugins ^js ws))

(defn get-drawer
  [ws]
  (.-drawer ^js ws))

(defn- get-drawer-width
  [drawer]
  (.-width ^js drawer))

(defn get-duration
  [ws]
  (.getDuration ^js ws))

(defn get-params
  [ws]
  (.-params ^js ws))

(defn is-ready?
  [ws]
  (.-isReady ^js ws))

(defn load-blob
  [ws blob]
  (.loadBlob ^js ws blob))

(defn play
  [ws]
  (.play ^js ws))

(defn recenter-drawer
  [drawer progress]
  (.recenter ^js drawer progress))

(defn seek-to
  [ws progress]
  (.seekTo ^js ws progress))

(defn set-audio-script
  [ws value]
  (.setAudioScript ^js ws value))

(defn stop
  [ws]
  (.stop ^js ws))

(defn subscribe
  [ws event handler]
  (.on ^js ws event handler))

(defn zoom
  [ws value]
  (.zoom ^js ws value))
