(ns webchange.lesson-builder.widgets.action-audio-editor.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.widgets.action-audio-editor.state :as state]
    [webchange.ui.index :as ui]))

(defn action-audio-editor
  [{:keys [action-path]}]
  (r/with-let [audio-wave-control (atom nil)
               handle-ref #(reset! audio-wave-control %)]
    (let [audio-url @(re-frame/subscribe [::state/audio-url action-path])
          handle-play-click (fn []
                              (let [{:keys [play]} @audio-wave-control]
                                (play)))]

      [:div {:class-name (ui/get-class-name {"widget--action-audio-editor" true})}
       (when (some? audio-url)
         ^{:key (str action-path "--" audio-url)}
         [ui/audio-wave {:url       audio-url
                         :on-change print
                         :ref       handle-ref}])
       [ui/button {:icon     "play"
                   :shape    "rounded"
                   :on-click handle-play-click}]])))
