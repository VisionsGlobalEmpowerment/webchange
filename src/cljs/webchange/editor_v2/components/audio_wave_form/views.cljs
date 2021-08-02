(ns webchange.editor-v2.components.audio-wave-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.audio-wave-form.core :refer [create-wavesurfer
                                                                 handle-audio-region!
                                                                 init-audio-region!
                                                                 update-script!]]
    [webchange.editor-v2.components.audio-wave-form.utils :refer [inc-zoom]]
    [webchange.editor-v2.components.audio-wave-form.views-controls :refer [float-control]]
    [webchange.editor-v2.components.audio-wave-form.state :as state]))

(def delta-zoom 15)

(defn- audio-wave-form-core
  [{:keys [start end]}]
  (let [ws (r/atom nil)
        region (r/atom {:start start :end end})
        element (r/atom nil)
        handle-wheel (fn [e]
                       (when (some? @ws)
                         (let [client-delta (.-deltaY e)
                               client-delta-sign (/ client-delta (Math/abs client-delta))]
                           (inc-zoom @ws (* client-delta-sign delta-zoom)))))]
    (r/create-class
      {:display-name "audio-wave-form"

       :component-did-mount
                     (fn [this]
                       (let [{:keys [url start end on-change height script ref] :or {height 64}} (r/props this)]
                         (reset! ws (create-wavesurfer @element url {:height height}))
                         (reset! region {:start start :end end})
                         (handle-audio-region! @ws region url on-change)
                         (init-audio-region! @ws region true url)

                         (when (some? script)
                           (update-script! @ws script))

                         (when (fn? ref)
                           (ref @ws))

                         (re-frame/dispatch [::state/init-audio-script url])))

       :component-did-update
                     (fn [this]
                       (let [{:keys [url start end script]} (r/props this)]
                         (reset! region {:start start :end end})
                         (init-audio-region! @ws region true url)
                         (update-script! @ws script)))

       :component-will-unmount
                     (fn []
                       (when-not (nil? @ws)
                         (.destroyAllPlugins @ws)
                         (.destroy @ws)))

       :reagent-render
                     (fn [{:keys [file-name right-side-controls show-controls?]
                           :or   {right-side-controls []}}]
                       [:div.audio-wave-form
                        [:div.header
                         [:div
                          (when show-controls?
                            [float-control ws region])
                          (when (some? file-name)
                            [:span.file-name file-name])]
                         (into [:div]
                               right-side-controls)]
                        [:div.body {:on-wheel handle-wheel
                                    :ref      #(when (and % (nil? @element))
                                                 (reset! element %))}]])})))

(defn audio-wave-form
  [_]
  (let [data (r/atom {})]
    (fn [{:keys [url on-audio-data-change] :as props}]
      (let [script-data @(re-frame/subscribe [::state/audio-script-data url])]
        (when (and on-audio-data-change (not (= script-data @data)))
          (reset! data script-data)
          (on-audio-data-change))
        [audio-wave-form-core (merge props {:script script-data})]))))
