(ns webchange.editor-v2.translator.views-modal
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor.events :as events]
    [webchange.editor-v2.translator.events :as translator-events]
    [webchange.editor-v2.translator.subs :as translator-subs]
    [webchange.editor-v2.translator.translator-form.views-form :refer [translator-form]]
    [webchange.subs :as subs]))

(defn save-actions-data!
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        data-store @(re-frame/subscribe [::translator-subs/phrase-translation-data])
        save-scene-action (fn [action-name action-data scene-id]
                            (re-frame/dispatch [::events/update-scene-action scene-id action-name action-data]))
        save-concept-action (fn [concept-id field-name field-data]
                              (re-frame/dispatch [::translator-events/update-current-concept-field concept-id field-name field-data]))]
    (doseq [[name {:keys [id type data]}] data-store]
      (case type
        :scene (save-scene-action name data scene-id)
        :concept (save-concept-action id name data)))
    (re-frame/dispatch [::events/save-current-scene scene-id])
    (re-frame/dispatch [::translator-events/save-current-concept])))

(def close-window! #(re-frame/dispatch [::translator-events/close-translator-modal]))

(defn translator-modal
  []
  (let [open? @(re-frame/subscribe [::translator-subs/translator-modal-state])
        handle-save #(do (save-actions-data!)
                         (close-window!))
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
      [ui/button {:on-click handle-close}
       "Cancel"]
      [ui/button {:color    "secondary"
                  :variant  "contained"
                  :on-click handle-save}
       "Save"]]]))
