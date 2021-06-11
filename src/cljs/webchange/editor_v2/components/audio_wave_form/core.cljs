(ns webchange.editor-v2.components.audio-wave-form.core
  (:require
    ["wavesurfer.js/dist/plugin/wavesurfer.regions.js" :as RegionsPlugin]
    ["wavesurfer.js/dist/plugin/wavesurfer.timeline.js" :as TimelinePlugin]
    ["wavesurfer.js/dist/plugin/wavesurfer.timeline.js" :as TimelinePlugin]
    [audio-script :as AudioScriptPlugin]
    [wavesurfer.js :as WaveSurfer]
    [webchange.editor-v2.components.audio-wave-form.audio-loader :as loader]
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

(defn- disable-default-scroll-handler
  [parent]
  (let [wave (.querySelector parent "wave")]
    (.addEventListener wave "scroll" (fn [event] (.preventDefault event)))))

(defn create-wavesurfer
  [element key {:keys [height zoom]
                :or   {zoom 300}}]
  (while (.-firstChild element) (-> element .-firstChild .remove))
  (let [font-color (get-in-theme [:palette :text :primary])
        script-div (.insertBefore element (js/document.createElement "div") nil)
        ws-div (.insertBefore element (js/document.createElement "div") nil)
        timeline-div (.insertBefore element (js/document.createElement "div") nil)

        wavesurfer (.create WaveSurfer (clj->js {:container    ws-div
                                                 :height       height
                                                 :minPxPerSec  75
                                                 :scrollParent true
                                                 :plugins      [(.create RegionsPlugin (clj->js {:dragSelection false
                                                                                                 :slop          5
                                                                                                 :color         "hsla(400, 100%, 30%, 0.5)"}))
                                                                (.create AudioScriptPlugin (clj->js {:container        script-div
                                                                                                     :primaryColor     "#979797"
                                                                                                     :secondaryColor   "#979797"
                                                                                                     :primaryFontColor "#979797"
                                                                                                     :timing           []}))
                                                                (.create TimelinePlugin (clj->js {:container          timeline-div
                                                                                                  :primaryFontColor   font-color
                                                                                                  :secondaryFontColor font-color}))]}))]
    (disable-default-scroll-handler ws-div)
    (loader/get-audio-blob key #(.loadBlob wavesurfer %))
    (.zoom wavesurfer zoom)
    wavesurfer))

(defn- set-audio-region!
  [wave-surfer region-atom edit key]
  (let [region-data {:id     "audio"
                     :color  audio-color
                     :start  (:start @region-atom)
                     :end    (:end @region-atom)
                     :drag   edit
                     :resize edit}]
    (when (and (= (:start region-data) 0) (= (:end region-data) 0))
      (.stop wave-surfer)
      (.clearRegions wave-surfer))
    (when (and (> (:start region-data) 0) (> (:end region-data) 0))
      (.stop wave-surfer)
      (.clearRegions wave-surfer)
      (let [progress (/ (last-position key (:start region-data))
                        (.getDuration wave-surfer))

            drawer-wrapper (.. wave-surfer -drawer -wrapper)
            recenter-shift (if (some? drawer-wrapper)
                             (let [full-width (.-scrollWidth drawer-wrapper)
                                   client-width (.-offsetWidth drawer-wrapper)]
                               (-> (/ client-width full-width) (/ 2) (- 0.01)))
                             0)]
        (.seekTo wave-surfer progress)
        (.recenter (.-drawer wave-surfer) (+ progress recenter-shift)))
      (.addRegion wave-surfer (clj->js region-data)))))

(defn init-audio-region!
  [wave-surfer region-atom edit key]
  (if (.-isReady wave-surfer)
    (set-audio-region! wave-surfer region-atom edit key)
    (.on wave-surfer "ready" #(set-audio-region! wave-surfer region-atom edit key))))

(defn update-script!
  [wave-surfer data]
  (if (.-isReady wave-surfer)
    (.setAudioScript wave-surfer (clj->js (if (some? data) data [])))
    (.on wave-surfer "ready" #(.setAudioScript wave-surfer (clj->js (if (some? data) data []))))))

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
