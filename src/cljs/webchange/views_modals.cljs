(ns webchange.views-modals
  (:require
    [webchange.editor-v2.concepts.views :refer [delete-dataset-item-modal]]
    [webchange.editor-v2.translator.views-modal :refer [translator-modal]]
    [webchange.editor-v2.dialog.views-modal :refer [dialog-modal]]
    [webchange.editor-v2.question.views-modal :refer [question-modal]]
    [webchange.editor-v2.text-animation-editor.chunks-editor.views :refer [configuration-modal]]))

(defn modal-windows
  []
  [:div
   [question-modal]
   [dialog-modal]
   [translator-modal]
   [configuration-modal]
   [delete-dataset-item-modal]])
