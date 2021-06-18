(ns webchange.editor-v2.dialog.dialog-text-form.menu.audios.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.dialog.components.record-audio.views :refer [record-audio]]
    [webchange.editor-v2.dialog.dialog-form.views-upload-audio :refer [upload-audio]]
    [webchange.editor-v2.dialog.dialog-text-form.menu.audios.state :as state]
    [webchange.ui-framework.components.index :refer [circular-progress icon icon-button]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- audios-list-item
  [{:keys [action-data alias match-status selected? url]}]
  (let [handle-click #(re-frame/dispatch [::state/audio-selected {:url url :action-data action-data}])
        handle-settings-click #(re-frame/dispatch [::state/open-voice-over-modal action-data])]
    [:div {:on-click   handle-click
           :class-name (get-class-name {"audios-list-item" true
                                        "selected"         selected?})}
     [:div.selected-icon-wrapper
      (when selected?
        [icon {:icon       "check"
               :class-name "selected-icon"}])]
     [:div.alias alias]
     [:div.match-status
      (when selected?
        (case match-status
          :loading [circular-progress {:class-name "loading-status"}]
          :matched [icon {:icon "match" :class-name "match-icon matched"}]
          :mismatched [icon {:icon "mismatch" :class-name "match-icon mismatched"}]
          nil))]
     (when selected?
       [icon-button {:icon       "settings"
                     :title      "Manual voice-over settings"
                     :class-name "settings-button"
                     :on-click   handle-settings-click}])]))

(defn- audios-list
  [{:keys [action-data]}]
  (let [audios @(re-frame/subscribe [::state/audios-list action-data])]
    [:div.side-menu-audios-list
     (for [{:keys [url] :as audio} audios]
       ^{:key url}
       [audios-list-item (merge audio
                                {:action-data action-data})])]))

(defn- add-audio-block
  []
  (r/with-let [show-upload-form? (r/atom true)
               handle-start-record #(reset! show-upload-form? false)
               handle-stop-record #(reset! show-upload-form? true)]
    [:div.add-audio-block
     [record-audio {:on-start-record handle-start-record
                    :on-stop-record  handle-stop-record}]
     (when @show-upload-form?
       [upload-audio {:input-props {:show-icon?   false
                                    :show-input?  false
                                    :button-text  "Upload file"
                                    :button-props {:color   "default"
                                                   :variant "outlined"}}}])]))

(defn audios-menu
  [{:keys [action-data]}]
  [:div.audios-side-menu
   [add-audio-block]
   [audios-list {:action-data action-data}]])
