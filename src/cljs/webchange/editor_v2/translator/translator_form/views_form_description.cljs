(ns webchange.editor-v2.translator.translator-form.views-form-description
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]))

(def text-input-params {:placeholder "Enter description text"
                        :variant     "outlined"
                        :margin      "normal"
                        :multiline   true
                        :full-width  true})

(defn description-block
  []
  (let [current-dialog-action @(re-frame/subscribe [::translator-form.actions/current-dialog-action])
        origin-text (:phrase-description current-dialog-action)
        translated-text (:phrase-description-translated current-dialog-action)
        handle-change (fn [event]
                        (let [value (.. event -target -value)]
                          (re-frame/dispatch [::translator-form.actions/set-dialog-action-description-translated value])))]
    [ui/grid {:container true
              :spacing   16
              :justify   "space-between"}
     [ui/grid {:item true :xs 6}
      [ui/text-field (merge text-input-params
                            {:label    "Origin Description"
                             :value    (or origin-text "")
                             :disabled true})]]
     [ui/grid {:item true :xs 6}
      [ui/text-field (merge text-input-params
                            {:label           "Translated Description"
                             :value   (or translated-text "")
                             :on-change       handle-change
                             :InputLabelProps {:shrink  true
                                               :focused true}})]]]))
