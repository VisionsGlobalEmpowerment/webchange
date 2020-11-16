(ns webchange.editor-v2.dialog.views-modal
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.confirm-dialog.views :refer [confirm-dialog]]
    [webchange.editor-v2.dialog.state.window :as translator.window]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form.form]
    [webchange.editor-v2.dialog.dialog-form.views-form :refer [translator-form]]))

(defn- get-styles
  []
  {:save-button-wrapper {:position "relative"}})

(def save-edited-data! #(re-frame/dispatch [::translator-form.form/save-changes]))
(def close-window! #(re-frame/dispatch [::translator.window/close]))

(defn dialog-modal
  []
  (r/with-let [confirm-open? (r/atom false)]
    (let [open? @(re-frame/subscribe [::translator.window/modal-state])
          has-changes? @(re-frame/subscribe [::translator-form.form/has-changes])
          handle-save #(do (save-edited-data!)
                           (close-window!))
          handle-close #(if-not has-changes?
                          (close-window!)
                          (reset! confirm-open? true))
          styles (get-styles)]
      [ui/dialog
       {:open       open?
        :on-close   handle-close
        :full-width true
        :max-width  "xl"}
       [ui/dialog-title
        "Dialogue"]
       [ui/dialog-content {:class-name "translation-form"}
        (when open?
          [translator-form])
        [confirm-dialog {:open?       confirm-open?
                         :on-confirm  handle-save
                         :on-cancel   #(close-window!)
                         :title       "Save changes?"
                         :description "You are going to close translation window without changes saving."
                         :save-text   "Save"
                         :cancel-text "Discard"}]]
       [ui/dialog-actions
        [ui/button {:on-click handle-close}
         "Cancel"]
        [:div {:style (:save-button-wrapper styles)}
         [ui/button {:color    "secondary"
                     :variant  "contained"
                     :on-click handle-save}
          "Save"]]]])))
