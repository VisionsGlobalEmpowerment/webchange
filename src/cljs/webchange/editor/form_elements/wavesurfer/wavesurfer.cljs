(ns webchange.editor.form-elements.wavesurfer.wavesurfer
  (:require
    [reagent.core :as r]
    [sodium.core :as na]
    [soda-ash.core :as sa]
    [webchange.interpreter.core :refer [get-data-as-blob]]
    [webchange.editor.form-elements.wavesurfer.wave-core :refer [create-wavesurfer
                                                                 handle-audio-region!
                                                                 handle-additional-regions!
                                                                 init-audio-region!
                                                                 init-additional-regions!]]))

(defn audio-waveform-modal
  [{:keys [key start end]} on-save]
  (r/with-let [ws (r/atom nil)
               region (r/atom {:start start :end end})
               modal-open (r/atom false)]
              [sa/Modal {:trigger (r/as-element [sa/Button {:on-click #(reset! modal-open true)} "Show Waveform"]) :open @modal-open}
               [sa/ModalHeader "Pick a region"]
               [sa/ModalContent {}
                [sa/ModalDescription
                 [:div {:ref #(when %
                                (let [wavesurfer (create-wavesurfer % key)]
                                  (handle-audio-region! wavesurfer region key)
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
                          (when (= :audio @mode) (handle-audio-region! wavesurfer region key))
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
                          (when (= :audio @mode) (handle-audio-region! wavesurfer region key))
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