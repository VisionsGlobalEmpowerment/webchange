(ns webchange.editor-v2.dialog.dialog-form.views-audio-actions
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.history.state :as state]
    [webchange.editor-v2.history.state-undo :as undo]
    [webchange.editor-v2.translator.text.views-text-animation-editor :as animation-editor]
    [webchange.ui-framework.components.index :refer [button icon]]))

(defn- edit-chunks-button
  []
  (let [handle-click #(re-frame/dispatch [::animation-editor/open])]
    [button {:color    "default"
             :variant  "rectangle"
             :on-click handle-click}
     [icon {:icon "settings"}]
     "Configure"]))

(defn- undo-button
  []
  (let [has-history? @(re-frame/subscribe [::state/has-history])
        handle-click #(re-frame/dispatch [::undo/apply-undo])]
    (when has-history?
      [button {:color    "default"
               :variant  "rectangle"
               :on-click handle-click}
       [icon {:icon "undo"}]
       "Undo"])))

(defn audio-actions
  []
  [:div.audio-actions
   [edit-chunks-button]
   [undo-button]])
