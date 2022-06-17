(ns webchange.editor-v2.translator.translator-form.views-form-description
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.ui-deprecated.theme :refer [get-in-theme]]))

(defn description-block
  []
  (let [settings @(re-frame/subscribe [::translator-form/components-settings :description])
        current-dialog-action @(re-frame/subscribe [::translator-form.actions/current-dialog-action])
        origin-text (:phrase-description current-dialog-action)
        translated-text (:phrase-description-translated current-dialog-action)
        handle-change (fn [event]
                        (let [value (.. event -target -value)]
                          (re-frame/dispatch [::translator-form.actions/set-dialog-action-description-translated value])))]
    (when-not (:hide? settings)
      [ui/grid {:container true
                :spacing   16
                :justify   "space-between"}
       [ui/grid {:item true :xs 6}
        [ui/text-field {:label           "Name of Script Segment"
                        :value           (or translated-text "")
                        :on-change       handle-change
                        :placeholder     "Enter translated text"
                        :variant         "outlined"
                        :margin          "normal"
                        :multiline       true
                        :full-width      true
                        :InputLabelProps {:shrink true}}]]
       [ui/grid {:item  true :xs 6
                 :style {:display      "flex"
                         :align-items  "center"
                         :padding-left "16px"
                         :color        (get-in-theme [:palette :text :secondary])}}
        [ui/typography {:variant "body2"
                        :style   {:margin-right "8px"}} "Previous:"]
        [ui/typography {:variant "body1"} origin-text]]])))
