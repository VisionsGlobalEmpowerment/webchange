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
  [{:keys [wave-surfer]} start end]
  (some? @wave-surfer) (w/play @wave-surfer start end))

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

(defn- remove-region
  [{:keys [regions]} region-id]
  (let [region (->> @regions
                    (filter #(= region-id (r/get-id %)))
                    (first))]
    (r/remove region)
    (swap! regions (fn [r]
                     (->> r
                          (remove #(= region-id (r/get-id %)))
                          (into []))))))

(defn- handle-event
  [{:keys [wave-surfer]} event]
  (w/handle-event @wave-surfer event))

(defn get-controls
  [instances]
  {:play            (partial play instances)
   :play-first      (partial play-first instances)
   :stop            (partial stop instances)
   :rewind-to-start (partial rewind instances :start)
   :rewind-to-end   (partial rewind instances :end)
   :zoom-in         (partial zoom instances 1)
   :zoom-out        (partial zoom instances -1)
   :remove-region   (partial remove-region instances)
   :handle-event    (partial handle-event instances)})
