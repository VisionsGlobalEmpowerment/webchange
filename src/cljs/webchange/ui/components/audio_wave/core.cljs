(ns webchange.ui.components.audio-wave.core
  (:require
    [webchange.logger.index :as logger]
    [webchange.ui.components.audio-wave.config :refer [get-config]]
    [webchange.ui.components.audio-wave.region-utils :as r]
    [webchange.ui.components.audio-wave.wave-utils :as w]
    [webchange.utils.numbers :refer [to-precision]]))

(defn create-region
  [{:keys [start end]}]
  (->> {:start start
        :end   end}
       (merge (get-config :region))))

(defn scroll-to-region
  [{:keys [region wave-surfer]}]
  (->> (r/get-start @region)
       (w/scroll-to-time @wave-surfer)))

(defn add-region
  [{:keys [wave-surfer] :as instances} {:keys [start end]}]
  (if (some? wave-surfer)
    (do (->> (create-region {:start start
                             :end   end})
             (w/add-region @wave-surfer))
        (scroll-to-region instances))
    (logger/error "Add region: wavesurfer is not defined")))

(defn remove-current-region
  [{:keys [region]}]
  (when (some? @region)
    (r/remove @region)))

(defn region->data
  [region]
  (let [round #(to-precision % 2)
        start (-> (r/get-start region) (round))
        end (-> (r/get-end region) (round))]
    {:start    start
     :end      end
     :duration (-> (- end start) (round))}))

(defn update-audio-script
  [{:keys [wave-surfer]} script-data]
  (let [fixed-script-data (remove #(= "[unk]" (:word %)) script-data)]
    (->> #(w/set-audio-script @wave-surfer fixed-script-data)
         (w/when-ready @wave-surfer))))

(defn update-region
  [{:keys [region] :as instances} region-data]
  (when (some? @region)
    (r/set-bounds @region region-data)
    (scroll-to-region instances)))

(defn handle-paused
  [on-pause]
  (when (fn? on-pause)
    (on-pause)))

(defn handle-region-created
  [{:keys [region] :as instances} new-region]
  (remove-current-region instances)
  (r/set-default-style new-region)
  (reset! region new-region))

(defn handle-region-updated
  [_ on-change region]
  (when (fn? on-change)
    (-> (region->data region)
        (on-change))))

(defn subscribe-to-events
  [{:keys [wave-surfer] :as instances} {:keys [on-change on-pause]}]
  (if (some? wave-surfer)
    (do (w/subscribe @wave-surfer "pause" (partial handle-paused on-pause))
        (w/subscribe @wave-surfer "region-created" (partial handle-region-created instances))
        (w/subscribe @wave-surfer "region-update-end" (partial handle-region-updated instances on-change)))
    (logger/error "Subscribe to events: wavesurfer is not defined")))

(defn destroy
  [{:keys [wave-surfer]}]
  (w/destroy @wave-surfer))
