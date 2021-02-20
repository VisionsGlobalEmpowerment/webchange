(ns webchange.editor-v2.translator.views-modal
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.components.confirm-dialog.views :refer [confirm-dialog]]
    [webchange.editor-v2.dialog-form.components.views-actions-bar :refer [actions-bar]]
    [webchange.editor-v2.translator.state.window :as translator.window]
    [webchange.editor-v2.translator.translator-form.views-form :refer [translator-form]]))

(def close-window! #(re-frame/dispatch [::translator.window/close]))

(defn translator-modal
  []
  (let [open? @(re-frame/subscribe [::translator.window/modal-state])
        handle-close #(close-window!)]
    [ui/dialog
     {:open       open?
      :on-close   handle-close
      :full-width true
      :max-width  "xl"}
     [ui/dialog-title
      "Dialog Translation"]
     [ui/dialog-content {:class-name "translation-form"}
      (when open?
        [translator-form])]
     [ui/dialog-actions
      [actions-bar {:on-close close-window!}]]]))
