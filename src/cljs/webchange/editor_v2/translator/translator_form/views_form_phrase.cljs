(ns webchange.editor-v2.translator.translator-form.views-form-phrase
  (:require
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]))

(defn phrase-block
  [{:keys [origin-text translated-text on-change]}]
  (let [text-input-params {:placeholder "Enter phrase text"
                           :variant     "outlined"
                           :margin          "normal"
                           :multiline   true
                           :full-width  true}]
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
                             :default-value           (or translated-text "")
                             :on-change (fn [event] (let [new-value (.. event -target -value)]
                                                      (on-change new-value)))
                             :InputLabelProps {:shrink  true
                                               :focused true}})]]]))
