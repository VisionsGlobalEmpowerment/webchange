(ns webchange.editor.form-elements.wavesurfer.wave-form
  (:require
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]
    [webchange.editor.form-elements.wavesurfer.wave-core :refer [create-wavesurfer
                                                                 handle-audio-region!
                                                                 init-audio-region!]]))

(def fab (r/adapt-react-class (aget js/MaterialUI "Fab")))

(defn float-control
  [ws region]
  (r/with-let [state (r/atom "pause")]
              (let [button-params {:color "default"
                                   :size  "small"
                                   :style {:position "absolute"
                                           :bottom   -8
                                           :right    0}}
                    _ (when-not (nil? @ws) (.on @ws "pause" #(reset! state "pause")))]
                [:div {:style {:position "relative"
                               :z-index  10}}
                 (when (= @state "pause")
                   [fab (merge button-params
                               {:on-click (fn []
                                            (when (:region @region) (-> @region :region .play))
                                            (reset! state "play"))})
                    [ic/play-arrow]])
                 (when (= @state "play")
                   [fab (merge button-params
                               {:on-click (fn []
                                            (.stop @ws)
                                            (reset! state "pause"))})
                    [ic/pause]])
                 ])))

(defn audio-wave-form
  [{:keys [key start end]} {:keys [on-change show-controls?] :as options}]
  (r/with-let [ws (r/atom nil)]
              (let [region (r/atom {:start start :end end})]
                [:div
                 [:div {:ref #(when %
                                (let [wave-surfer (create-wavesurfer % key options)]
                                  (handle-audio-region! wave-surfer region key on-change)
                                  (init-audio-region! wave-surfer region true key)
                                  (reset! ws wave-surfer)))}]
                 (when show-controls?
                   [float-control ws region])])))
