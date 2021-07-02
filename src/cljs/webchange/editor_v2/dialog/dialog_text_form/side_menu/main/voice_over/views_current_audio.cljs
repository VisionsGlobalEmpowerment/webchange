(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.views-current-audio
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.components.audio-wave-form.views :refer [audio-wave-form]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.state :as state]))

(defn- no-audio-placeholder
  [{:keys [on-add-click on-select-click]}]
  [:div.no-audio-placeholder
   [:span "No audio selected"]
   [:span
    [:span.clickable {:on-click on-add-click} "Add new"]
    " audio or "
    [:span.clickable {:on-click on-select-click} "select from available"]]])

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
