(ns webchange.editor-v2.translator.views-modal
  (:require
    ["@projectstorm/react-diagrams" :refer [DiagramWidget]]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.translator.events :as translator-events]
    [webchange.editor-v2.translator.subs :as translator-subs]))

(defn translator-modal
  []
  (let [translator-modal-state @(re-frame/subscribe [::translator-subs/translator-modal-state])
        handle-close #(re-frame/dispatch [::translator-events/close-translator-modal])
        action-data @(re-frame/subscribe [::editor-subs/current-action])
        phrase-text (get-in action-data [:data :phrase-text])]
    [ui/dialog
     {:open       (boolean translator-modal-state)
      :on-close   handle-close
      :full-width true
      :max-width  "md"}
     [ui/dialog-title
      "Phrase Translation"]
     [ui/dialog-content
      {:class-name "translation-form"}
      [ui/text-field
       {:label           "Phrase Text"
        :placeholder     "Enter phrase text"
        :variant         "outlined"
        :full-width      true
        :value           phrase-text
        :margin          "normal"
        :disabled true
        :InputLabelProps {:shrink true}}]
      [:div.diagram-container
       ;[:> DiagramWidget {:diagramEngine engine}]
       ]]
     [ui/dialog-actions
      [ui/button
       {:on-click handle-close}
       "Cancel"]]]))
