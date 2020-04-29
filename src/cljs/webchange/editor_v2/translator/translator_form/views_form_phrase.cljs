(ns webchange.editor-v2.translator.translator-form.views-form-phrase
  (:require
    [re-frame.core :as re-frame]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.translator.translator-form.events :as translator-form-events]
    [webchange.editor-v2.translator.translator-form.subs :as translator-form-subs]
    [webchange.editor-v2.translator.translator-form.utils :refer [trim-text]]))

(defn phrase-block
  []
  (let [text-input-params {:placeholder "Enter phrase text"
                           :variant     "outlined"
                           :margin      "normal"
                           :multiline   true
                           :full-width  true}
        current-action-data @(re-frame/subscribe [::translator-form-subs/current-action-data])
        origin-text     (-> current-action-data :data :phrase-text trim-text)
        translated-text (-> current-action-data :data :phrase-text-translated trim-text)
        handle-text-change (fn [event] (let [new-value (.. event -target -value)]
                                         (re-frame/dispatch [::translator-form-events/set-current-action-phrase-translated-text
                                                             new-value])))]
    [ui/grid {:container true
              :spacing   16
              :justify   "space-between"}
     [ui/grid {:item true :xs 6}
      [ui/text-field (merge text-input-params
                            {:label    "Origin Text"
                             :value    origin-text
                             :disabled true})]]
     [ui/grid {:item true :xs 6}
      [ui/text-field (merge text-input-params
                            {:label           "Translated Text"
                             :default-value   (or translated-text "")
                             :on-change       handle-text-change
                             :InputLabelProps {:shrink  true
                                               :focused true}})]]]))
