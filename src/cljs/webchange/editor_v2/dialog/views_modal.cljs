(ns webchange.editor-v2.dialog.views-modal
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.state.window :as translator.window]
    [webchange.editor-v2.history.views :as history]
    [webchange.editor-v2.dialog-form.components.views-actions-bar :refer [actions-bar]]
    [webchange.editor-v2.dialog.dialog-form.views-form :refer [translator-form]]))

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
        handle-close #(close-window!)]
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
        [translator-form])]
     [ui/dialog-actions {:style {:justify-content "space-between"}}
      [actions-bar {:on-close close-window!}]]]))
