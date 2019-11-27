(ns webchange.editor.form-elements.wavesurfer.wave-core
  (:require
    ["wavesurfer.js/dist/plugin/wavesurfer.regions.js" :as RegionsPlugin]
    ["wavesurfer.js/dist/plugin/wavesurfer.timeline.js" :as TimelinePlugin]
    [wavesurfer.js :as WaveSurfer]
    [webchange.interpreter.core :refer [get-data-as-blob]]))

(def audio-color "rgba(0, 0, 0, 0.1)")
(def additional-color "rgba(0, 0, 100, 0.3)")

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
   (let [ws-div (.insertBefore element (js/document.createElement "div") nil)
         timeline-div (.insertBefore element (js/document.createElement "div") nil)
         wavesurfer (.create WaveSurfer (clj->js {:container    ws-div
                                                  :height       (or height 256)
                                                  :minPxPerSec  75
                                                  :scrollParent true
                                                  :plugins      [(.create RegionsPlugin (clj->js {:dragSelection false
                                                                                                  :slop          5
                                                                                                  :color         "hsla(400, 100%, 30%, 0.5)"}))
                                                                 (.create TimelinePlugin (clj->js {:container timeline-div}))]}))]
     (.loadBlob wavesurfer (get-data-as-blob key))
     wavesurfer)))

(defn init-audio-region!
  [wavesurfer region-atom edit key]
  (let [region-data {:id     "audio"
                     :color  audio-color
                     :start  (:start @region-atom)
                     :end    (:end @region-atom)
                     :drag   edit
                     :resize edit}]
    (.on wavesurfer "ready" #(when (and (> (:start region-data) 0) (> (:end region-data) 0))
                               (.seekAndCenter wavesurfer (/ (last-position key (+ (:start region-data) 5)) (.getDuration wavesurfer)))
                               (.addRegion wavesurfer (clj->js region-data))
                               ))))

(defn init-additional-regions!
  [wavesurfer regions-atom sequence-data]
  (if @regions-atom
    (doseq [[id {:keys [start end]}] @regions-atom]
      (.on wavesurfer "ready" #(.addRegion wavesurfer (clj->js {:id    id :start start :end end
                                                                :color additional-color :drag true :resize true}))))
    (doseq [{:keys [start end]} sequence-data]
      (reset! regions-atom {})
      (.on wavesurfer "ready" #(.addRegion wavesurfer (clj->js {:start start :end end
                                                                :color additional-color :drag true :resize true}))))))

(defn handle-audio-region!
  ([wavesurfer region-atom key]
   (handle-audio-region! wavesurfer region-atom key #()))
  ([wavesurfer region-atom key on-change]
   (.enableDragSelection wavesurfer (clj->js {:color audio-color}))
   (.on wavesurfer "region-created" (fn [e]
                                      (when (:region @region-atom) (-> @region-atom :region .remove))
                                      (swap! last-positions assoc key (-> e region->data :start))
                                      (reset! region-atom (-> (region->data e) (assoc :region e)))
                                      (on-change @region-atom)))
   (.on wavesurfer "region-update-end" (fn [e]
                                         (swap! last-positions assoc key (-> e region->data :start))
                                         (reset! region-atom (-> (region->data e) (assoc :region e)))
                                         (on-change @region-atom)))))

(defn handle-additional-regions!
  [wavesurfer regions-atom]
  (.enableDragSelection wavesurfer (clj->js {:color additional-color}))
  (.on wavesurfer "region-created" (fn [e]
                                     (when (not= "audio" (.-id e))
                                       (swap! regions-atom assoc (.-id e) (region->data e)))))
  (.on wavesurfer "region-update-end" (fn [e]
                                        (when (not= "audio" (.-id e))
                                          (swap! regions-atom assoc (.-id e) (region->data e))))))
