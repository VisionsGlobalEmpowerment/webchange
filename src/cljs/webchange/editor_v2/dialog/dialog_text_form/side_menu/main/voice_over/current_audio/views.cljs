(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.current-audio.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.audio-wave-form.utils :as ws-utils]
    [webchange.editor-v2.components.audio-wave-form.views :refer [audio-wave-form]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.voice-over.state :as state]
    [webchange.ui-framework.components.index :refer [icon-button]]))

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
  (r/with-let [ws (atom nil)
               handle-ref #(reset! ws %)]
    (let [{:keys [audio] :as audio-data} @(re-frame/subscribe [::state/current-audio])
          handle-change-region #(re-frame/dispatch [::state/set-current-audio-region %])
          handle-recognition-retry #(re-frame/dispatch [::state/recognition-retry %])
          handle-scroll-start (fn []
                                (let [{:keys [start]} audio-data]
                                  (ws-utils/center-to-time @ws start)))
          handle-scroll-finish (fn []
                                 (let [{:keys [start end duration]} audio-data
                                       end-time (or end (+ start duration))]
                                   (ws-utils/center-to-time @ws end-time)))]
      (into [:div.current-audio]
            (if (some? audio)
              [[:div.actions
                [icon-button {:icon     "restart"
                              :title    "Recognition retry"
                              :size     "small"
                              :on-click handle-recognition-retry}
                 "Recognition"]
                [icon-button {:icon     "arrow-first"
                              :title    "Scroll to region start"
                              :size     "small"
                              :on-click handle-scroll-start}]
                [icon-button {:icon     "arrow-last"
                              :title    "Scroll to region end"
                              :size     "small"
                              :on-click handle-scroll-finish}]]
               ^{:key audio}
               [audio-wave-form (merge {:url audio}
                                       (select-keys audio-data [:start :end :duration])
                                       {:on-change      handle-change-region
                                        :show-controls? true
                                        :ref            handle-ref})]]
              [[no-audio-placeholder props]])))))
