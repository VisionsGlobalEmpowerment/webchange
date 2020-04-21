(ns webchange.editor-v2.components.audio-wave-form.core
  (:require
    ["wavesurfer.js/dist/plugin/wavesurfer.regions.js" :as RegionsPlugin]
    ["wavesurfer.js/dist/plugin/wavesurfer.timeline.js" :as TimelinePlugin]
    [wavesurfer.js :as WaveSurfer]
    [webchange.interpreter.core :refer [wait-data-as-blob]]
    [webchange.ui.theme :refer [get-in-theme]]))

(def audio-color "rgba(0, 0, 0, 0.1)")

(defonce last-positions (atom {}))

(defn last-position
  [key default]
  (get @last-positions key default))

(defn round [f]
  (/ (.round js/Math (* 1000 f)) 1000))

(defn region->data [region]
  (let [start (-> region .-start round)
        end (-> region .-end round)]
    {:start    start
     :end      end
     :duration (round (- end start))}))

(defn create-wavesurfer
  ([element key]
   (create-wavesurfer element key {:height 256}))
  ([element key {:keys [height]}]
   (while (.-firstChild element) (-> element .-firstChild .remove))
   (let [font-color (get-in-theme [:palette :text :primary])
         ws-div (.insertBefore element (js/document.createElement "div") nil)
         timeline-div (.insertBefore element (js/document.createElement "div") nil)
         wavesurfer (.create WaveSurfer (clj->js {:container    ws-div
                                                  :height       (or height 256)
                                                  :minPxPerSec  75
                                                  :scrollParent true
                                                  :plugins      [(.create RegionsPlugin (clj->js {:dragSelection false
                                                                                                  :slop          5
                                                                                                  :color         "hsla(400, 100%, 30%, 0.5)"}))
                                                                 (.create TimelinePlugin (clj->js {:container          timeline-div
                                                                                                   :primaryFontColor   font-color
                                                                                                   :secondaryFontColor font-color}))]}))]
     (wait-data-as-blob key (fn [blob] (.loadBlob wavesurfer blob)))
     wavesurfer)))

(defn- set-audio-region!
  [wave-surfer region-atom edit key]
  (let [region-data {:id     "audio"
                     :color  audio-color
                     :start  (:start @region-atom)
                     :end    (:end @region-atom)
                     :drag   edit
                     :resize edit}]
    (when (and (> (:start region-data) 0) (> (:end region-data) 0))
      (.stop wave-surfer)
      (.clearRegions wave-surfer)
      (.seekAndCenter wave-surfer (/ (last-position key (+ (:start region-data) 5))
                                     (.getDuration wave-surfer)))
      (.addRegion wave-surfer (clj->js region-data)))))

(defn init-audio-region!
  [wave-surfer region-atom edit key]
  (if (.-isReady wave-surfer)
    (set-audio-region! wave-surfer region-atom edit key)
    (.on wave-surfer "ready" #(set-audio-region! wave-surfer region-atom edit key))))

(defn- is-default-created?
  [{:keys [start end]}]
  (and (= start 0) (< end 0.1)))

(defn handle-audio-region!
  ([wavesurfer region-atom key]
   (handle-audio-region! wavesurfer region-atom key #()))
  ([wavesurfer region-atom key on-change]
   (let [handle-event (fn [e]
                        (let [original (select-keys @region-atom [:start :end])
                              data (region->data e)]
                          (when (and (not= original (select-keys data [:start :end])) (not (is-default-created? data)))
                            (on-change data))
                          (swap! last-positions assoc key (:start data))
                          (reset! region-atom (assoc data :region e))))
         remove-region #(when (:region @region-atom) (-> @region-atom :region .remove))]
     (.enableDragSelection wavesurfer (clj->js {:color audio-color}))
     (.on wavesurfer "region-created" (fn [e] (remove-region) (handle-event e)))
     (.on wavesurfer "region-update-end" handle-event))))
