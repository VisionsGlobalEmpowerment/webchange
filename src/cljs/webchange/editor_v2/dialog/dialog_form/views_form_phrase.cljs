(ns webchange.editor-v2.dialog.dialog-form.views-form-phrase
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.creation-progress.translation-progress.validate-action :as validate]
    [webchange.editor-v2.creation-progress.warning-icon :refer [warning-icon]]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.dialog.dialog-form.state.actions-defaults :refer [get-inner-action get-empty-action]]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as dialog-form.actions]
    [webchange.editor-v2.translator.translator-form.utils :refer [trim-text]]
    [webchange.editor-v2.translator.translator-form.common.views-text-field :refer [text-field]]))


(defn node-options
  []
  (let [text-input-params {:placeholder "Enter options"
                           :variant     "outlined"
                           :margin      "normal"
                           :multiline   true
                           :full-width  true}
        {:keys [path]} @(re-frame/subscribe [::translator-form.actions/current-phrase-action-info])
        paralel? (= 3 (count path))
        main-paralel? (= 0 (last path))
        current-phrase-action @(re-frame/subscribe [::translator-form.actions/current-phrase-action])
        volume-text (-> current-phrase-action get-inner-action :volume str trim-text)
        offset-text (-> current-phrase-action get-empty-action :duration str trim-text)
        handle-volume-change (fn [event] (let [new-value (.. event -target -value)]
                                           (re-frame/dispatch [::dialog-form.actions/set-phrase-action-volume
                                                               new-value])))
        handle-offset-change (fn [event]
                               (let [new-value (.. event -target -value)]
                                 (re-frame/dispatch [::dialog-form.actions/set-phrase-action-offset
                                                     new-value])))]
    [ui/grid {:container true
              :spacing   16
              :justify   "space-between"}
     [ui/grid {:item true :xs 6}
      [text-field (merge text-input-params
                         {:label           "Volume"
                          :value           (or volume-text "")
                          :on-change       handle-volume-change
                          :InputLabelProps {:shrink true}})]]
     (if (and paralel? (not main-paralel?))
       [ui/grid {:item true :xs 6}
        [text-field (merge text-input-params
                           {:label           "Delay"
                            :value           (or offset-text "")
                            :on-change       handle-offset-change
                            :InputLabelProps {:shrink  true
                                              :focused true}})]])]))

(defn- label-with-warning
  [{:keys [label show-warning?]}]
  (if show-warning?
    [:div {:style {:display     "flex"
                   :align-items "center"
                   :position    "relative"
                   :top         "-5px"}}
     [warning-icon {:styles {:main {:margin-right "8px"}}}]
     label]
    label))

(defn phrase-block
  []
  (let [text-input-params {:placeholder "Enter phrase text"
                           :variant     "outlined"
                           :margin      "normal"
                           :multiline   true
                           :full-width  true}
        current-phrase-action @(re-frame/subscribe [::translator-form.actions/current-phrase-action])
        origin-text (-> current-phrase-action get-inner-action :phrase-text)
        translated-text (-> current-phrase-action get-inner-action :phrase-text-translated)]
    [ui/grid {:container true
              :spacing   16
              :justify   "space-between"}
     [ui/grid {:item true :xs 6}
      [text-field (merge text-input-params
                         {:label           (r/as-element [label-with-warning {:label         "Origin Text"
                                                                              :show-warning? (-> current-phrase-action validate/phrase-origin-text-defined? not)}])
                          :value           (or origin-text "")
                          :on-change       #(re-frame/dispatch [::dialog-form.actions/set-phrase-action-phrase (.. % -target -value)])
                          :InputLabelProps {:shrink true}})]]
     [ui/grid {:item true :xs 6}
      [text-field (merge text-input-params
                         {:label           "Translated Text"
                          :value           (or translated-text "")
                          :on-change       #(re-frame/dispatch [::dialog-form.actions/set-phrase-action-phrase-translated (.. % -target -value)])
                          :InputLabelProps {:shrink  true
                                            :focused true}})]]]))
