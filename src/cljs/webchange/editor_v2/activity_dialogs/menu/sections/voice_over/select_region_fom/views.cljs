(ns webchange.editor-v2.activity-dialogs.menu.sections.voice-over.select-region-fom.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.menu.sections.voice-over.select-region-fom.state :as state]
    [webchange.editor-v2.components.audio-wave-form.views :refer [audio-wave-form]]
    [webchange.ui-framework.components.index :refer [button dialog icon-button label select]]))

(defn- toolbar
  [{:keys [ws show-expand? show-auto-select? show-scroll?]
    :or   {show-expand?      true
           show-auto-select? true
           show-scroll?      true}}]
  (let [handle-expand #(re-frame/dispatch [::state/open-window])
        handle-scroll-start #(re-frame/dispatch [::state/scroll-to-start @ws])
        handle-scroll-end #(re-frame/dispatch [::state/scroll-to-end @ws])
        handle-auto-select-retry #(re-frame/dispatch [::state/auto-select @ws])]
    [:div.toolbar
     (when show-scroll?
       [:div
        [label {:class-name "label"} "Scroll:"]
        [icon-button {:icon     "arrow-first"
                      :title    "Scroll to region start"
                      :size     "small"
                      :on-click handle-scroll-start}]
        [icon-button {:icon     "arrow-last"
                      :title    "Scroll to region end"
                      :size     "small"
                      :on-click handle-scroll-end}]])
     (when show-auto-select?
       [:div
        [icon-button {:icon     "restart"
                      :title    "Recognition auto select retry"
                      :size     "small"
                      :on-click handle-auto-select-retry}
         "Auto select"]])
     (when show-expand?
       [:div
        [icon-button {:icon     "expand"
                      :size     "small"
                      :on-click handle-expand}
         "Expand"]])]))

(defn- region-options
  []
  (let [value @(re-frame/subscribe [::state/selected-option])
        options @(re-frame/subscribe [::state/region-options])
        handle-change #(re-frame/dispatch [::state/set-selected-option %])]
    [:div.region-options
     [label {:class-name "label"} "Selection options:"]
     [select {:type       "int"
              :class-name "select"
              :value      value
              :options    options
              :on-change  handle-change}]]))

(defn select-region-form
  []
  (let [ws (atom nil)
        handle-ref #(reset! ws %)]
    (re-frame/dispatch [::state/init])
    (fn [{:keys [show-options? toolbar-props]
          :or   {show-options? true}}]
      (let [wave-form-data @(re-frame/subscribe [::state/wave-form-data])
            handle-change-region #(re-frame/dispatch [::state/change-region %])]
        [:div.select-region-form
         [toolbar (merge toolbar-props
                         {:ws ws})]
         [audio-wave-form (merge wave-form-data
                                 {:on-change      handle-change-region
                                  :show-controls? true
                                  :ref            handle-ref})]
         (when show-options?
           [region-options])]))))

(defn select-region
  []
  (let [open? @(re-frame/subscribe [::state/window-open?])
        handle-close #(re-frame/dispatch [::state/close-window])]
    (if open?
      [dialog
       {:title    "Edit current selection"
        :on-close handle-close
        :actions  [[button {:on-click handle-close
                            :variant  "outlined"
                            :size     "big"}
                    "Close"]]}
       [select-region-form {:toolbar-props {:show-expand? false}}]]
      [select-region-form {:toolbar-props {:show-scroll? false}
                           :show-options? false}])))
