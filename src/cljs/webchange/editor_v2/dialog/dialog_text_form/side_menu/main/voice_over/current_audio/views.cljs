(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.current-audio.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.components.audio-wave-form.views :refer [audio-wave-form]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.state :as state]))

(defn- no-audio-placeholder
  []
  [:div.no-audio-placeholder
   [:span "No audio selected"]
   [:span
    [:span.clickable "Add new"]
    " audio or "
    [:span.clickable "select from available"]]])

(defn current-audio
  [props]
  (let [{:keys [audio] :as audio-data} @(re-frame/subscribe [::state/current-audio])
        handle-change-region #(re-frame/dispatch [::state/set-current-audio-region %])]
    [:div.current-audio
     (if (some? audio)
       ^{:key audio}
       [audio-wave-form (merge {:url audio}
                               (select-keys audio-data [:start :end :duration])
                               {:on-change      handle-change-region
                                :show-controls? true})]
       [no-audio-placeholder props])]))
