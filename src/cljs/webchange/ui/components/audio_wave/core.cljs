(ns webchange.ui.components.audio-wave.core
  (:require
    [webchange.logger.index :as logger]
    [webchange.ui.components.audio-wave.config :refer [get-config]]
    [webchange.ui.components.audio-wave.region-utils :as r]
    [webchange.ui.components.audio-wave.wave-utils :as w]
    [webchange.utils.numbers :refer [to-precision]]))

(defn create-region
  [{:keys [start end data]}]
  (->> {:start start
        :end   end
        :data  data}
       (merge (get-config :region))))

(defn scroll-to
  [{:keys [wave-surfer]} {:keys [start]}]
  (cond
    (nil? @wave-surfer) (logger/error "Scroll to region: wavesurfer is not defined")
    :else (w/scroll-to-time @wave-surfer start)))

(defn add-region
  [wave-surfer {:keys [start end data]}]
  (try
    (if (some? wave-surfer)
      (->> (create-region {:start start
                           :end   end
                           :data  data})
           (w/add-region wave-surfer))
      (logger/error "Add region: wavesurfer is not defined"))    
    (catch js/Error e
      (logger/error e))))

(defn remove-current-regions
  [{:keys [regions]}]
  (when (seq @regions)
    (doseq [region @regions]
      (r/remove region))
    (reset! regions [])))

(defn region->data
  [region]
  (let [round #(to-precision % 2)
        start (-> (r/get-start region) (round))
        end (-> (r/get-end region) (round))]
    {:id (r/get-id region)
     :start    start
     :end      end
     :duration (-> (- end start) (round))
     :data     (r/get-data region)}))

(defn update-audio-script
  [{:keys [wave-surfer]} script-data]
  (if (some? @wave-surfer)
    (let [fixed-script-data (->> script-data
                                 (remove #(= "[unk]" (:word %)))
                                 (map #(if (nil? (:word %))
                                         (assoc % :word "")
                                         %)))]
      (->> #(w/set-audio-script @wave-surfer fixed-script-data)
           (w/when-ready @wave-surfer)))
    (logger/error "Update audio script: wavesurfer is not defined")))

(defn update-regions
  [{:keys [wave-surfer initial-regions] :as instances} regions-data]
  (reset! initial-regions regions-data)
  (remove-current-regions instances)
  (doseq [region regions-data]
    (add-region @wave-surfer region))
  (when (= 1 (count regions-data))
    (scroll-to instances (first regions-data))))

(defn handle-paused
  [on-pause]
  (when (fn? on-pause)
    (on-pause)))

(defn handle-region-created
  [{:keys [regions] :as instances} on-create single-region? new-region]
  (when single-region?
    (remove-current-regions instances))
  (r/set-default-style new-region)
  (swap! regions conj new-region)
  (when (fn? on-create)
    (-> (region->data new-region)
        (on-create))))

(defn handle-region-updated
  [{:keys [regions]} _region]
  (when true
    (->> @regions
         (sort r/compare-regions)
         (reduce (fn [pos current-region]
                   (when (> pos (r/get-start current-region))
                     (r/set-bounds current-region {:start pos
                                                   :end (+ pos (r/get-duration current-region))})
                     (r/set-changed current-region true))
                   (r/get-end current-region))
                 0))))

(defn handle-region-update-end
  [{:keys [regions]} on-change region]
  (when (fn? on-change)
    (-> (region->data region)
        (on-change))
    (doseq [region @regions]
      (when (r/is-changed? region)
        (-> (region->data region)
            (on-change))
        (r/set-changed region false)))))

(defn handle-region-click
  [_ on-select region]
  (when (fn? on-select)
    (-> (region->data region)
        (on-select))))

(defn subscribe-to-events
  [{:keys [wave-surfer] :as instances} {:keys [on-create on-change on-pause on-select single-region?]}]
  (if (some? @wave-surfer)
    (do (w/subscribe @wave-surfer "pause" (partial handle-paused on-pause))
        (w/subscribe @wave-surfer "region-created" (partial handle-region-created instances on-create single-region?))
        (w/subscribe @wave-surfer "region-updated" (partial handle-region-updated instances on-change))
        (w/subscribe @wave-surfer "region-update-end" (partial handle-region-update-end instances on-change))
        (w/subscribe @wave-surfer "region-click" (partial handle-region-click instances on-select)))
    (logger/error "Subscribe to events: wavesurfer is not defined")))

(defn destroy
  [{:keys [wave-surfer]}]
  (if (some? @wave-surfer)
    (w/destroy @wave-surfer)
    (logger/error "Destroy: wavesurfer is not defined")))
