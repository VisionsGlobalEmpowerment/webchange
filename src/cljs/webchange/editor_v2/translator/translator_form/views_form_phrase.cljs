(ns webchange.editor-v2.translator.translator-form.views-form-phrase
  (:require
    [re-frame.core :as re-frame]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.utils :refer [trim-text]]
    [webchange.editor-v2.translator.translator-form.common.views-text-field :refer [text-field]]))

(defn phrase-block
  []
  (let [text-input-params {:placeholder "Enter phrase text"
                           :variant     "outlined"
                           :margin      "normal"
                           :multiline   true
                           :full-width  true}
        current-phrase-action @(re-frame/subscribe [::translator-form.actions/current-phrase-action])
        origin-text (-> current-phrase-action :phrase-text trim-text)
        translated-text (-> current-phrase-action :phrase-text-translated trim-text)
        handle-text-change (fn [event] (let [new-value (.. event -target -value)]
                                         (re-frame/dispatch [::translator-form.actions/set-phrase-action-phrase
                                                             new-value])))
        handle-text-translated-change (fn [event]
                                        (let [new-value (.. event -target -value)]
                                          (re-frame/dispatch [::translator-form.actions/set-phrase-action-phrase-translated
                                                              new-value])))]
    [ui/grid {:container true
              :spacing   16
              :justify   "space-between"}
     [ui/grid {:item true :xs 6}
      [text-field (merge text-input-params
                         {:label           "Origin Text"
                          :value           (or origin-text "")
                          :on-change       handle-text-change
                          :InputLabelProps {:shrink true}})]]
     [ui/grid {:item true :xs 6}
      [text-field (merge text-input-params
                         {:label           "Translated Text"
                          :value           (or translated-text "")
                          :on-change       handle-text-translated-change
                          :InputLabelProps {:shrink  true
                                            :focused true}})]]]))
