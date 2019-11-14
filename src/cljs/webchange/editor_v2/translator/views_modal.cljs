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
  [data-store scene-id]
  (println ">>> save-actions-data!")
  (let [save-scene-action (fn [action-name action-data scene-id]
                            (println "> save-scene-action")
                            (println "scene-id" scene-id)
                            (println "action-name" action-name)
                            (println "action-data" action-data)
                            ;(re-frame/dispatch [::events/edit-scene-action scene-id action-name action-data])
                            )
        save-concept-action (fn [concept-id concept-data]
                              (println "> save-concept-action")
                              (println "concept-id" concept-id)
                              (println "concept-data" concept-data)
                              ;(re-frame/dispatch [::events/edit-sce
                              ;(re-frame/dispatch [::events/edit-dataset-item concept-id concept-data])
                              )]
    (doseq [[name {:keys [type data]}] @data-store]
      (case type
        :scene (save-scene-action name data scene-id)
        :concept (save-concept-action name data)))))

(def close-window! #(re-frame/dispatch [::translator-events/close-translator-modal]))

(defn translator-modal
  []
  (let [actions-data (atom {})
        scene-id (re-frame/subscribe [::subs/current-scene])
        translator-modal-state @(re-frame/subscribe [::translator-subs/translator-modal-state])
        handle-save #(do (save-actions-data! actions-data @scene-id)
                         (close-window!))
        handle-close #(close-window!)]
    [ui/dialog
     {:open       (boolean translator-modal-state)
      :on-close   handle-close
      :full-width true
      :max-width  "xl"}
     [ui/dialog-title
      "Phrase Translation"]
     [ui/dialog-content {:class-name "translation-form"}
      (when (boolean translator-modal-state)
        [translator-form actions-data])]
     [ui/dialog-actions
      [ui/button {:on-click handle-close}
       "Cancel"]
      [ui/button {:color    "secondary"
                  :variant  "contained"
                  :on-click handle-save}
       "Save"]]]))
