(ns webchange.lesson-builder.tools.script.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.state :as state]
    [webchange.lesson-builder.tools.script.dialog.views :refer [dialog]]
    [webchange.lesson-builder.tools.script.track-selector.views :refer [track-selector]]
    [webchange.ui.index :as ui]))

(defn- header
  []
  [:div.script--header
   [ui/icon {:icon "edit-boxed"}]
   [:div.script--header--text "Script"]])

(defn script
  []
  (let [dialogs @(re-frame/subscribe [::state/track-dialogs])
        {:keys [open? text]} @(re-frame/subscribe [::state/confirm-window])
        handle-confirm #(re-frame/dispatch [::state/handle-confirm-window])
        handle-cancel #(re-frame/dispatch [::state/close-confirm-window])]
    [:div.widget--script
     [header]
     [track-selector]
     [:div.widget--script--content
      (for [{:keys [id] :as dialog-data} dialogs]
        ^{:key id}
        [dialog dialog-data])]
     [ui/confirm {:open?      open?
                  :on-confirm handle-confirm
                  :on-cancel  handle-cancel}
      text]]))