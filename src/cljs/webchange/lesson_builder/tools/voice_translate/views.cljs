(ns webchange.lesson-builder.tools.voice-translate.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.components.block.views :refer [block]]
    [webchange.lesson-builder.components.toolbox.views :refer [toolbox]]
    [webchange.lesson-builder.tools.voice-translate.state :as state]
    [webchange.lesson-builder.widgets.action-audio-editor.views :refer [action-audio-editor auto-select-button]]
    [webchange.lesson-builder.widgets.audio-add.views :refer [audio-add]]
    [webchange.lesson-builder.widgets.audio-list.views :refer [audio-list]]
    [webchange.lesson-builder.widgets.welcome-translate.views :refer [welcome-translate]]
    [webchange.utils.uid :refer [get-uid]]))

(defn audio-manager
  []
  (let [action-path @(re-frame/subscribe [::state/selected-action])
        selected-audio @(re-frame/subscribe [::state/selected-audio])
        handle-audio-changed #(re-frame/dispatch [::state/set-selected-audio action-path %])]
    [:div.tool--voice-translate--audio-manager
     [:h1.audio-manager--header
      "Voice & Translate"]
     [block {:title  "Add Audio"
             :border "none"}
      [audio-add]]
     [block {:title      "Select Audio"
             :class-name "audio-list-wrapper"}
      [audio-list {:value     selected-audio
                   :on-change handle-audio-changed}]]]))

(defn audio-editor
  []
  (r/with-let [id (get-uid)]
    (let [file-name @(re-frame/subscribe [::state/file-name])
          show-audio-editor? @(re-frame/subscribe [::state/show-audio-editor?])
          selected-action @(re-frame/subscribe [::state/selected-action])]
      (if show-audio-editor?
        [toolbox {:title   "Audio Editor"
                  :icon    "translate"
                  :text    file-name
                  :actions [auto-select-button {:id          id
                                                :action-path selected-action}]}
         [action-audio-editor {:id          id
                               :action-path selected-action}]]
        [welcome-translate]))))
