(ns webchange.editor-v2.dialog.views-modal
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.state.window :as translator.window]
    [webchange.editor-v2.history.views :as history]
    [webchange.editor-v2.dialog.dialog-form.views-form :refer [dialog-form]]
    [webchange.editor-v2.dialog.dialog-form.views-form-actions :refer [form-actions]]
    [webchange.editor-v2.dialog.dialog-text-form.views :as text-form]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form.form]
    [webchange.ui-framework.components.index :refer [dialog icon-button]]))

(defn- event-handler
  [event]
  (let [z-key 90]
    (when (and (= (.-which event) z-key)
               (.-ctrlKey event))
      (history/undo))))

(defn- enable-hot-keys
  []
  (.addEventListener js/document "keyup" event-handler))

(defn- disable-hot-keys
  []
  (.removeEventListener js/document "keyup" event-handler))

(def close-window! #(re-frame/dispatch [::translator.window/close]))

(defn- change-mode-control
  []
  (let [mode @(re-frame/subscribe [::translator.window/mode])
        handle-click #(re-frame/dispatch [::translator.window/set-mode (if (= mode :dialog-text) :classic :dialog-text)])]
    [icon-button {:icon     "settings"
                  :on-click handle-click}
     "Change mode"]))

(defn dialog-modal
  []
  (let [open? @(re-frame/subscribe [::translator.window/modal-state])
        has-changes? @(re-frame/subscribe [::translator-form.form/has-changes])
        handle-close #(if has-changes?
                        (when (js/confirm "Close window without saving?")
                          (close-window!))
                        (close-window!))
        mode @(re-frame/subscribe [::translator.window/mode])]
    [dialog
     {:title         "Dialogue"
      :open?         open?
      :on-close      handle-close
      :on-enter      enable-hot-keys
      :on-exit       disable-hot-keys
      :actions       [[form-actions]]
      :title-actions [[change-mode-control]]
      :full-screen?  true}
     (case mode
       :dialog-text [text-form/dialog-form]
       [dialog-form])]))
