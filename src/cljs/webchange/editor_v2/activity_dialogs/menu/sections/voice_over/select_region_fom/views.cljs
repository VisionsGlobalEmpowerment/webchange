(ns webchange.editor-v2.activity-dialogs.menu.sections.voice-over.select-region-fom.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.menu.sections.voice-over.select-region-fom.state :as state]
    [webchange.editor-v2.components.audio-wave-form.views :refer [audio-wave-form]]
    [webchange.ui-framework.components.index :refer [button dialog icon-button label]]))

(defn- toolbar
  [{:keys [ws]}]
  (let [handle-scroll-start #(re-frame/dispatch [::state/scroll-to-start @ws])
        handle-scroll-end #(re-frame/dispatch [::state/scroll-to-end @ws])
        handle-auto-select-retry #(re-frame/dispatch [::state/auto-select])]
    [:div
     [:div
      [label "Scroll:"]
      [icon-button {:icon     "arrow-first"
                    :title    "Scroll to region start"
                    :size     "small"
                    :on-click handle-scroll-start}]
      [icon-button {:icon     "arrow-last"
                    :title    "Scroll to region end"
                    :size     "small"
                    :on-click handle-scroll-end}]]
     [:div
      [icon-button {:icon     "restart"
                    :title    "Recognition auto select retry"
                    :size     "small"
                    :on-click handle-auto-select-retry}
       "Auto select"]]]))

(defn select-region-fom
  []
  (let [ws (atom nil)
        handle-ref #(reset! ws %)]
    (re-frame/dispatch [::state/init])
    (fn []
      (let [wave-form-data @(re-frame/subscribe [::state/wave-form-data])
            handle-change-region #(re-frame/dispatch [::state/change-region %])]
        [:div
         [toolbar {:ws ws}]
         [audio-wave-form (merge wave-form-data
                                 {:on-change      handle-change-region
                                  :show-controls? true
                                  :ref            handle-ref})]]))))

(defn select-region-window
  []
  (let [open? @(re-frame/subscribe [::state/window-open?])
        handle-close #(re-frame/dispatch [::state/close-window])]
    [dialog
     {:title    "Edit current selection"
      :open?    open?
      :on-close handle-close
      :actions  [[button {:on-click handle-close
                          :variant  "outlined"
                          :size     "big"}
                  "Close"]]}
     [select-region-fom]]))

(defn select-region-button
  []
  (let [handle-click #(re-frame/dispatch [::state/open-window])]
    [:div.settings
     [icon-button {:icon     "expand"
                   :size     "small"
                   :on-click handle-click}
      "Select Region"]
     [select-region-window]]))
