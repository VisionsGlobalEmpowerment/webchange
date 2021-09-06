(ns webchange.editor-v2.dialog.views-modal
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.history.views :as history]
    [webchange.editor-v2.dialog.dialog-form.views-form :refer [dialog-form]]
    [webchange.editor-v2.dialog.dialog-form.views-form-actions :refer [form-actions]]
    [webchange.editor-v2.dialog.state.window :as translator.window]
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

(defn dialog-modal
  []
  (let [open? @(re-frame/subscribe [::translator.window/modal-state])
        has-changes? @(re-frame/subscribe [::translator-form.form/has-changes])
        handle-close #(if has-changes?
                        (when (js/confirm "Close window without saving?")
                          (close-window!))
                        (close-window!))]
    [dialog
     {:title        "Dialogue"
      :open?        open?
      :on-close     handle-close
      :on-enter     enable-hot-keys
      :on-exit      disable-hot-keys
      :actions      [[form-actions]]
      :full-screen? true}
     [dialog-form]]))
