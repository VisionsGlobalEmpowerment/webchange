(ns webchange.editor-v2.components.audio-wave-form.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.components.audio-wave-form.core :refer [create-wavesurfer
                                                                 handle-audio-region!
                                                                 init-audio-region!]]
    [webchange.editor-v2.components.audio-wave-form.views-controls :refer [float-control]]))

(defn audio-wave-form
  [{:keys [start end]}]
  (let [ws (r/atom nil)
        region (r/atom {:start start :end end})
        element (r/atom nil)]
    (r/create-class
      {:display-name "audio-wave-form"

       :component-did-mount
                     (fn [this]
                       (let [{:keys [url start end on-change height]} (r/props this)]
                         (reset! ws (create-wavesurfer @element url {:height height}))
                         (reset! region {:start start :end end})
                         (handle-audio-region! @ws region url on-change)
                         (init-audio-region! @ws region true url)))

       :component-did-update
                     (fn [this]
                       (let [{:keys [url start end]} (r/props this)]
                         (reset! region {:start start :end end})
                         (init-audio-region! @ws region true url)))

       :component-will-unmount
                     (fn []
                       (when-not (nil? @ws)
                         (.destroyAllPlugins @ws)
                         (.destroy @ws)))

       :reagent-render
                     (fn [{:keys [show-controls?]}]
                       [:div
                        [:div {:ref #(when (and % (nil? @element))
                                       (reset! element %))}]
                        (when show-controls?
                          [float-control ws region])])})))
