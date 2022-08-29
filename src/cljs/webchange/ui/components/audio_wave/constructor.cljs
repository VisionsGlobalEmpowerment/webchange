(ns webchange.ui.components.audio-wave.constructor
  (:require
    ["wavesurfer.js/dist/plugin/wavesurfer.regions.js" :as Regions]
    ["wavesurfer.js/dist/plugin/wavesurfer.timeline.js" :as Timeline]
    ["/audio-script" :as AudioScript]
    [wavesurfer.js :as WaveSurferConstructor]
    [webchange.ui.components.audio-wave.config :refer [get-config]]
    [webchange.ui.components.audio-wave.audio-loader :as loader]
    [webchange.ui.components.audio-wave.wave-utils :as w]
    [webchange.utils.element :as el]))

(def AudioScriptPlugin AudioScript)
(def RegionsPlugin Regions)
(def TimelinePlugin Timeline)
(def WaveSurfer WaveSurferConstructor)

(defn- create
  [constructor params]
  (->> (clj->js params)
       (.create constructor)))

(defn create-wavesurfer
  [element audio-url {:keys [on-ready script-class-name timeline-class-name wave-class-name]}]
  (el/remove-children element)
  (let [timeline-div (->> (el/create {:class-name timeline-class-name}) (el/insert-before element))
        script-div (->> (el/create {:class-name script-class-name}) (el/insert-before element))
        ws-div (->> (el/create {:class-name wave-class-name}) (el/insert-before element))
        wavesurfer (create WaveSurfer (merge (get-config :wave-surfer)
                                             {:container    ws-div
                                              :height       64
                                              :minPxPerSec  75
                                              :scrollParent true
                                              :plugins      [(create RegionsPlugin
                                                                     (get-config :region-plugin))
                                                             (create AudioScriptPlugin
                                                                     (merge (get-config :audio-script)
                                                                            {:container script-div
                                                                             :timing    []}))
                                                             (create TimelinePlugin
                                                                     (merge (get-config :time-line)
                                                                            {:container timeline-div}))]}))]
    (when (fn? on-ready)
      (w/subscribe wavesurfer "ready" #(on-ready wavesurfer)))

    (loader/get-audio-blob audio-url #(w/load-blob wavesurfer %))

    wavesurfer))
