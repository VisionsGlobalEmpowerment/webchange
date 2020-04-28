(ns webchange.editor-v2.translator.translator-form.views-form-description
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.translator.translator-form.events :as translator-form-events]
    [webchange.editor-v2.translator.translator-form.subs :as translator-form-subs]))

(def text-input-params {:placeholder "Enter description text"
                        :variant     "outlined"
                        :margin      "normal"
                        :multiline   true
                        :full-width  true})

(defn- get-current-root-data
  [selected-phrase-node data-store]
  (let [name (first (:path selected-phrase-node))
        origin-data (:data selected-phrase-node)
        new-data (get-in data-store [[name nil] :data])]
    (merge origin-data new-data)))

(defn description-block
  []
  (let [selected-phrase-node @(re-frame/subscribe [::editor-subs/current-action])
        data-store @(re-frame/subscribe [::translator-form-subs/edited-actions-data])
        prepared-root-action-data (get-current-root-data selected-phrase-node data-store)
        origin-text (:phrase-description prepared-root-action-data)
        translated-text (:phrase-description-translated prepared-root-action-data)
        handle-change (fn [event]
                        (let [value (.. event -target -value)]
                          (re-frame/dispatch [::translator-form-events/set-dialog-action-description-translated value])))]
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
                             :default-value   (or translated-text "")
                             :on-change       handle-change
                             :InputLabelProps {:shrink  true
                                               :focused true}})]]]))

