(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.views-current-audio
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.components.audio-wave-form.views :refer [audio-wave-form]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.state :as state]))

(defn current-audio
  []
  (let [{:keys [audio] :as audio-data} @(re-frame/subscribe [::state/current-audio])
        handle-change-region #(re-frame/dispatch [::state/set-current-audio-region %])]
    (when (some? audio)
      [:div.current-audio
       ^{:key audio}
       [audio-wave-form (merge {:url audio}
                               (select-keys audio-data [:start :end :duration])
                               {:on-change      handle-change-region
                                :show-controls? true})]])))
