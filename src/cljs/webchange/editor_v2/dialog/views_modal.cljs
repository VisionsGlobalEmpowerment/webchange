(ns webchange.editor-v2.dialog.views-modal
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.confirm-dialog.views :refer [confirm-dialog]]
    [webchange.editor-v2.dialog.state.window :as translator.window]
    [webchange.editor-v2.history.views :as history]
    [webchange.editor-v2.translator.text.views-text-animation-editor :as animation-editor]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form.form]
    [webchange.editor-v2.dialog.dialog-form.views-form :refer [translator-form]]))

(defn- get-styles
  []
  {:save-button-wrapper {:margin-left "16px"
                         :position    "relative"}})

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

(def save-edited-data! #(re-frame/dispatch [::translator-form.form/save-changes]))
(def close-window! #(re-frame/dispatch [::translator.window/close]))

(defn dialog-modal
  []
  (r/with-let [confirm-close? (r/atom false)
               confirm-save? (r/atom false)]
    (let [open? @(re-frame/subscribe [::translator.window/modal-state])
          {:keys [single-phrase?]} @(re-frame/subscribe [::translator.window/modal-params])
          has-changes? @(re-frame/subscribe [::translator-form.form/has-changes])
          handle-save #(do (save-edited-data!)
                           (reset! confirm-save? true))
          handle-configure #(re-frame/dispatch [::animation-editor/open])
          handle-close #(if-not has-changes?
                          (close-window!)
                          (reset! confirm-close? true))
          styles (get-styles)]
      [ui/dialog
       {:open       open?
        :on-close   handle-close
        :on-enter   #(enable-hot-keys)
        :on-exit    #(disable-hot-keys)
        :full-width true
        :max-width  "xl"}
       [ui/dialog-title
        "Dialogue"]
       [ui/dialog-content {:class-name "translation-form"}
        (when open?
          [translator-form])
        [confirm-dialog {:open?       confirm-close?
                         :on-confirm  handle-save
                         :on-cancel   #(close-window!)
                         :title       "Save changes?"
                         :description "You are going to close translation window without changes saving."
                         :save-text   "Save"
                         :cancel-text "Discard"}]
        [confirm-dialog {:open?       confirm-save?
                         :on-confirm  #(reset! confirm-save? false)
                         :on-cancel   #(close-window!)
                         :title       "Changes Saved"
                         :description "Do you want to continue work or close dialogue window?"
                         :save-text   "Continue"
                         :cancel-text "Close"}]]
       [ui/dialog-actions {:style {:justify-content "space-between"}}
        [history/controls]
        [:div {:style {:display "flex"}}
         [ui/button {:on-click handle-close}
          "Cancel"]
         (when single-phrase?
           [:div {:style (:save-button-wrapper styles)}
            [ui/button {:color    "primary"
                        :on-click handle-configure}
             "Configure"]])
         [:div {:style (:save-button-wrapper styles)}
          [ui/button {:color    "secondary"
                      :variant  "contained"
                      :on-click handle-save}
           "Save"]]]]])))
