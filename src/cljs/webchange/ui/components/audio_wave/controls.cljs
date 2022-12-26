(ns webchange.ui.components.audio-wave.controls
  (:require
    [webchange.ui.components.audio-wave.config :refer [delta-zoom]]
    [webchange.ui.components.audio-wave.region-utils :as r]
    [webchange.ui.components.audio-wave.wave-utils :as w]))

(defn- play-first
  [{:keys [regions wave-surfer]}]
  (let [first-region (-> @regions first)]
    (cond
      (some? first-region) (r/play first-region)
      (some? @wave-surfer) (w/play @wave-surfer))))

(defn- play
  [{:keys [wave-surfer]}]
  (some? @wave-surfer) (w/play @wave-surfer))

(defn- stop
  [{:keys [wave-surfer]}]
  (w/stop @wave-surfer))

(defn- rewind
  [{:keys [regions wave-surfer]} direction]
  (let [region (-> @regions first)
        time (case direction
               :start (r/get-start region)
               :end (r/get-end region))]
    (w/scroll-to-time @wave-surfer time)))

(defn- zoom
  [{:keys [wave-surfer]} direction]
  (->> (* direction delta-zoom)
       (w/inc-zoom @wave-surfer)))

(defn get-controls
  [instances]
  {:play            (partial play instances)
   :play-first      (partial play-first instances)
   :stop            (partial stop instances)
   :rewind-to-start (partial rewind instances :start)
   :rewind-to-end   (partial rewind instances :end)
   :zoom-in         (partial zoom instances 1)
   :zoom-out        (partial zoom instances -1)})
