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
  [element audio-url {:keys [on-ready]}]
  (el/remove-children element)
  (let [timeline-div (->> (el/create)
                          (el/insert-before element))
        ;script-div (->> (el/create)
        ;                (el/insert-before element))
        ws-div (->> (el/create)
                    (el/insert-before element))
        wavesurfer (create WaveSurfer (merge (get-config :wave-surfer)
                                             {:container    ws-div
                                              :height       64
                                              :minPxPerSec  75
                                              :scrollParent true
                                              :plugins      [(create RegionsPlugin
                                                                     (get-config :region-plugin))
                                                             ;(create AudioScriptPlugin {:container        script-div
                                                             ;                           :primaryColor     "#979797"
                                                             ;                           :secondaryColor   "#979797"
                                                             ;                           :primaryFontColor "#979797"
                                                             ;                           :timing           []})
                                                             (create TimelinePlugin
                                                                     (merge (get-config :time-line)
                                                                            {:container timeline-div}))]}))]
    (w/subscribe wavesurfer "ready" on-ready)

    (->> #(w/load-blob wavesurfer %)
         (loader/get-audio-blob audio-url))

    wavesurfer))
