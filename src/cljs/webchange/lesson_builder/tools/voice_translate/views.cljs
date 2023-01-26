(ns webchange.lesson-builder.tools.voice-translate.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.components.block.views :refer [block]]
    [webchange.lesson-builder.components.toolbox.views :refer [toolbox]]
    [webchange.lesson-builder.tools.voice-translate.state :as state]
    [webchange.lesson-builder.tools.voice-translate.transcription.views :as transcription]
    [webchange.lesson-builder.tools.voice-translate.transcription.state :as transcription-state]
    [webchange.lesson-builder.widgets.action-audio-editor.views :refer [action-audio-editor auto-select-button]]
    [webchange.lesson-builder.widgets.audio-add.views :refer [audio-add]]
    [webchange.lesson-builder.widgets.audio-list.views :refer [audio-list]]
    [webchange.lesson-builder.widgets.welcome-translate.views :refer [welcome-translate]]
    [webchange.utils.uid :refer [get-uid]]
    [webchange.ui.index :as ui]))

(defn audio-manager
  []
  (let [action-path @(re-frame/subscribe [::state/selected-action])
        selected-audio @(re-frame/subscribe [::state/selected-audio])
        handle-audio-changed #(re-frame/dispatch [::state/set-selected-audio action-path %])
        handle-audio-item-mode-changed #(re-frame/dispatch [::state/set-apply-disabled %])
        handle-apply #(re-frame/dispatch [::state/apply])
        apply-disabled? @(re-frame/subscribe [::state/apply-disabled?])]
    [:div.tool--voice-translate--audio-manager
     [:h1.audio-manager--header
      "Voice & Translate"]
     [block {:title  "Add Audio"
             :border "none"}
      [audio-add]]
     [block {:title      "Select Audio"
             :class-name "audio-list-wrapper"}
      [audio-list {:value                     selected-audio
                   :on-change                 handle-audio-changed
                   :on-audio-item-mode-change handle-audio-item-mode-changed}]]
     [ui/button {:class-name "audio-manager-apply"
                 :disabled?  apply-disabled?
                 :on-click   handle-apply}
      "Apply"]]))



(defn audio-editor
  []
  (r/with-let [id (r/atom (get-uid))]
    (let [file-name @(re-frame/subscribe [::state/file-name])
          show-audio-editor? @(re-frame/subscribe [::state/show-audio-editor?])
          selected-action @(re-frame/subscribe [::state/selected-action])
          audio-url @(re-frame/subscribe [::state/selected-audio])
          edit-transcription #(re-frame/dispatch [::transcription-state/open-edit-window audio-url])]
      (if show-audio-editor?
        [toolbox {:title   "Audio Editor"
                  :icon    "translate"
                  :text    file-name
                  :class-name "tool--voice-translate--audio-editor"
                  :actions [:<>
                            [ui/button {:class-name "edit-transcription-button"
                                        :shape "rounded"
                                        :on-click edit-transcription} "Edit"]
                            [auto-select-button {:id          @id
                                                 :action-path selected-action}]]}
         ^{:key @id}
         [action-audio-editor {:id          @id
                               :action-path selected-action}]
         [transcription/edit-transcription-window {:on-save #(reset! id new-id)}]]
        [welcome-translate]))))
