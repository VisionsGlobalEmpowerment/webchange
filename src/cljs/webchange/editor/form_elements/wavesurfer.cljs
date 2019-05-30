(ns webchange.editor.form-elements.wavesurfer
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.common.kimage :refer [kimage]]
    [webchange.common.anim :refer [anim animations init-spine-player]]
    [webchange.common.events :as ce]
    [webchange.interpreter.variables.subs :as vars.subs]
    [webchange.interpreter.core :refer [get-data-as-blob get-data]]
    [webchange.interpreter.events :as ie]
    [webchange.editor.events :as events]
    [webchange.editor.subs :as es]
    [konva :refer [Transformer]]
    [react-konva :refer [Stage Layer Group Rect Text Custom]]
    [sodium.core :as na]
    [sodium.extensions :as nax]
    [soda-ash.core :as sa]
    [wavesurfer.js :as WaveSurfer]
    ["wavesurfer.js/dist/plugin/wavesurfer.regions.js" :as RegionsPlugin]
    ["wavesurfer.js/dist/plugin/wavesurfer.timeline.js" :as TimelinePlugin]))

(def audio-color "rgba(0, 0, 0, 0.1)")
(def additional-color "rgba(0, 0, 100, 0.3)")

(defn create-wavesurfer [element key]
  (while (.-firstChild element) (-> element .-firstChild .remove))
  (let [ws-div (.insertBefore element (js/document.createElement "div") nil)
        timeline-div (.insertBefore element (js/document.createElement "div") nil)
        wavesurfer (.create WaveSurfer (clj->js {:container ws-div
                                                 :height 256
                                                 :minPxPerSec 75
                                                 :scrollParent true
                                                 :plugins [(.create RegionsPlugin (clj->js {:dragSelection false
                                                                                            :slop 5
                                                                                            :color "hsla(400, 100%, 30%, 0.5)"}))
                                                           (.create TimelinePlugin (clj->js {:container timeline-div}))]}))]
    (.loadBlob wavesurfer (get-data-as-blob key))
    wavesurfer))

(defn round [f]
  (/ (.round js/Math (* 1000 f)) 1000))

(defn region->data [region]
  (let [start (-> region .-start round)
        end (-> region .-end round)]
    {:start start
     :end end
     :duration (round (- end start))}))

(defn handle-audio-region! [wavesurfer region-atom]
  (.enableDragSelection wavesurfer (clj->js {:color audio-color}))
  (.on wavesurfer "region-created" (fn [e]
                                     (when (:region @region-atom) (-> @region-atom :region .remove))
                                     (reset! region-atom (-> (region->data e) (assoc :region e)))))
  (.on wavesurfer "region-update-end" (fn [e]
                                        (reset! region-atom (-> (region->data e) (assoc :region e))))))

(defn handle-additional-regions! [wavesurfer regions-atom]
  (.enableDragSelection wavesurfer (clj->js {:color additional-color}))
  (.on wavesurfer "region-created" (fn [e]
                                     (when (not= "audio" (.-id e))
                                      (swap! regions-atom assoc (.-id e) (region->data e)))))
  (.on wavesurfer "region-update-end" (fn [e]
                                        (when (not= "audio" (.-id e))
                                          (swap! regions-atom assoc (.-id e) (region->data e))))))

(defn init-audio-region! [wavesurfer region-atom edit]
  (let [region-data {:id "audio"
                     :color audio-color
                     :start (:start @region-atom)
                     :end (:end @region-atom)
                     :drag edit
                     :resize edit}]
    (.on wavesurfer "ready" #(.addRegion wavesurfer (clj->js region-data)))))


(defn audio-waveform-modal [{:keys [key start end]} on-save]
  (r/with-let [ws (r/atom nil)
               region (r/atom {:start start :end end})
               modal-open (r/atom false)]
              [sa/Modal {:trigger (r/as-element [sa/Button {:on-click #(reset! modal-open true)} "Show Waveform"]) :open @modal-open}
               [sa/ModalHeader "Pick a region"]
               [sa/ModalContent {}
                [sa/ModalDescription
                 [:div {:ref #(when %
                                (let [wavesurfer (create-wavesurfer % key)]
                                  (handle-audio-region! wavesurfer region)
                                  (init-audio-region! wavesurfer region true)
                                  (reset! ws wavesurfer)))}]
                 [:div
                  [na/button {:content "Play" :on-click #(when (:region @region) (-> @region :region .play))}]
                  [na/button {:content "Stop" :on-click #(.stop @ws)}]
                  ]
                 ]]
               [sa/ModalActions {}
                [na/button {:content "Cancel" :on-click #(reset! modal-open false)}]
                [na/button {:content "Save" :on-click #(do
                                                         (reset! modal-open false)
                                                         (on-save @region))}]]]))

(defn init-additional-regions! [wavesurfer regions-atom sequence-data]
  (if @regions-atom
    (doseq [[id {:keys [start end]}] @regions-atom]
      (.on wavesurfer "ready" #(.addRegion wavesurfer (clj->js {:id    id :start start :end end
                                                                :color additional-color :drag true :resize true}))))
    (doseq [{:keys [start end]} sequence-data]
      (reset! regions-atom {})
      (.on wavesurfer "ready" #(.addRegion wavesurfer (clj->js {:start start :end end
                                                                :color additional-color :drag true :resize true}))))))

(defn animation-sequence-waveform-modal [{:keys [key start end sequence-data]} on-save]
  (let [ws (r/atom nil)
        region (r/atom {:start start :end end})
        regions (r/atom nil)
        modal-open (r/atom false)
        mode (r/atom :audio)]
    (fn [{:keys [key start end sequence-data]} on-save]
      [sa/Modal {:trigger (r/as-element [sa/Button {:on-click #(do
                                                                 (reset! region {:start start :end end})
                                                                 (reset! regions nil)
                                                                 (reset! mode :audio)
                                                                 (reset! modal-open true))} "Show Waveform"]) :open @modal-open}
       [sa/ModalHeader "Pick a region"]
       [sa/ModalContent {}
        [sa/ModalDescription
         [sa/Radio {:toggle true :label "Animations" :checked (= :animations @mode) :on-change #(if (-> %2 .-checked)
                                                                                                  (reset! mode :animations)
                                                                                                  (reset! mode :audio))}]
         [:div {:ref #(when %
                        (let [wavesurfer (create-wavesurfer % key)]
                          (when (= :audio @mode) (handle-audio-region! wavesurfer region))
                          (init-audio-region! wavesurfer region (= :audio @mode))

                          (when (= :animations @mode)
                            (handle-additional-regions! wavesurfer regions)
                            (init-additional-regions! wavesurfer regions sequence-data))
                          (reset! ws wavesurfer)))}]
         [:div
          [na/button {:content "Play" :on-click #(when (:region @region) (-> @region :region .play))}]
          [na/button {:content "Stop" :on-click #(.stop @ws)}]
          ]
         ]]
       [sa/ModalActions {}
        [na/button {:content "Cancel" :on-click #(reset! modal-open false)}]
        [na/button {:content "Save" :on-click #(do
                                                 (reset! modal-open false)
                                                 (on-save (assoc @region :regions (vals @regions))))}]]])))

(defn text-animation-waveform-modal [{:keys [key start end sequence-data]} on-save]
  (let [ws (r/atom nil)
        region (r/atom {:start start :end end})
        regions (r/atom nil)
        modal-open (r/atom false)
        mode (r/atom :audio)]
    (fn [{:keys [key start end sequence-data]} on-save]
      [sa/Modal {:trigger (r/as-element [sa/Button {:on-click #(do
                                                                 (reset! region {:start start :end end})
                                                                 (reset! regions nil)
                                                                 (reset! mode :audio)
                                                                 (reset! modal-open true))} "Show Waveform"]) :open @modal-open}
       [sa/ModalHeader "Pick a region"]
       [sa/ModalContent {}
        [sa/ModalDescription
         [sa/Radio {:toggle true :label "Chunks" :checked (= :chunks @mode) :on-change #(if (-> %2 .-checked)
                                                                                                  (reset! mode :chunks)
                                                                                                  (reset! mode :audio))}]
         [:div {:ref #(when %
                        (let [wavesurfer (create-wavesurfer % key)]
                          (when (= :audio @mode) (handle-audio-region! wavesurfer region))
                          (init-audio-region! wavesurfer region (= :audio @mode))

                          (when (= :chunks @mode)
                            (handle-additional-regions! wavesurfer regions)
                            (init-additional-regions! wavesurfer regions sequence-data))
                          (reset! ws wavesurfer)))}]
         [:div
          [na/button {:content "Play" :on-click #(when (:region @region) (-> @region :region .play))}]
          [na/button {:content "Stop" :on-click #(.stop @ws)}]
          ]
         ]]
       [sa/ModalActions {}
        [na/button {:content "Cancel" :on-click #(reset! modal-open false)}]
        [na/button {:content "Save" :on-click #(do
                                                 (reset! modal-open false)
                                                 (on-save (assoc @region :regions (vals @regions))))}]]])))