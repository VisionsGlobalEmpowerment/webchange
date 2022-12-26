(ns webchange.ui.components.audio-wave.config
  (:require
    [camel-snake-kebab.core :refer [->camelCase]]
    [camel-snake-kebab.extras :refer [transform-keys]]))

(def blue-1 "#3453A1")
(def grey-3 "#C7D1EB")
(def yellow-1 "#F5A36C")
(def opacity-03 "4d")

(def delta-zoom 15)

(def region-handle-style {:background-color blue-1
                          :width            "1px"})

(defn format-time
  [value]
  (let [m (quot value 60)
        s (mod value 60)]
    (cond->> (str s "s")
             (> m 0) (str m "m "))))

(def config {:audio-script  {:primary-color      yellow-1
                             :secondary-color    grey-3
                             :primary-font-color grey-3}
             ;; https://github.com/katspaugh/wavesurfer.js/blob/master/src/plugin/regions/index.js#L20
             :region        {:loop                 false    ;; Whether to loop the region when played back.
                             :drag                 true     ;; Allow/disallow dragging the region.
                             :resize               true     ;; Allow/disallow resizing the region.
                             :color                (str grey-3 opacity-03) ;; HTML color code.
                             :handle-style         {:left  region-handle-style
                                                    :right region-handle-style} ;; A set of CSS properties used to style the left and right handle.
                             :prevent-context-menu false    ;; Determines whether the context menu is prevented from being opened.
                             :show-tooltip         true     ;; Enable/disable tooltip displaying start and end times when hovering over region.
                                        ;:channelIdx                    ;; Select channel to draw the region on (if there are multiple channel waveforms).
                             }

             ;; https://github.com/katspaugh/wavesurfer.js/blob/master/src/plugin/regions/index.js#L4
             ;; https://wavesurfer-js.org/plugins/regions.html
             :region-plugin {:drag-selection true           ;; Enable creating regions by dragging with the mouse
                             :slop           5              ;; The sensitivity of the mouse dragging
                             :max-regions    nil            ;; Maximum number of regions that may be created by the user at one time.
                             }
             ;; https://github.com/katspaugh/wavesurfer.js/blob/master/src/plugin/timeline/index.js#L2
             :time-line     {:height                   10
                             :format-time-callback     format-time
                             :font-family              "Noto Sans"
                             :font-size                12

                             :primary-font-color       blue-1 ;; The colour of the labels next to the main notches
                             :secondary-font-color     grey-3 ;; The colour of the labels next to the secondary notches
                             :label-padding            5    ;; The padding between the label and the notch

                             :time-interval            1    ;; (pxPerSec) -> seconds between notches
                             :primary-label-interval   5    ;; (pxPerSec) -> cadence between labels in primary color
                             :secondary-label-interval 1    ;; (pxPerSec) -> cadence between labels in secondary color
                             }
             ;; https://github.com/katspaugh/wavesurfer.js/blob/master/src/wavesurfer.js#L21
             :wave-surfer   {:cursor-color   blue-1         ;; The fill color of the cursor indicating the playhead position.
                             :cursor-width   1              ;; Measured in pixels.
                             :progress-color blue-1         ;; The fill color of the part of the waveform behind the cursor.
                             ;; When `progressColor` and `waveColor` are the same the progress wave is not rendered at all.
                             :wave-color     blue-1         ;; The fill color of the waveform after the cursor
                             }})

(defn get-config
  ([config-name]
   (get-config config-name false))
  ([config-name ->js?]
   (cond-> (->> (get config config-name)
                (transform-keys #(-> % ->camelCase clojure.core/name)))
           ->js? (clj->js))))
