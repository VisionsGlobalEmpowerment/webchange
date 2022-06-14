(ns webchange.editor-v2.question.question-form.views-form-description
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.question.text.views-text-animation-editor :as text-animation]
    [webchange.editor-v2.translator.translator-form.common.views-text-field :refer [text-field]]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.question.question-form.state.actions :as question-form.actions]
    [webchange.ui-deprecated.theme :refer [get-in-theme]]))

(defn- label-with-warning
  [{:keys [label show-warning?]}]
  (if show-warning?
    [:div {:style {:display     "flex"
                   :align-items "center"
                   :position    "relative"
                   :top         "-5px"}}
     label]
    label))

(defn text-block
  []
  (let [current-dialog-action @(re-frame/subscribe [::question-form.actions/current-question-text-action])
        translated-text (str (:text current-dialog-action))
        handle-change (fn [event]
                        (let [text (subs (.. event -target -value) 0 145)]
                          (re-frame/dispatch [::text-animation/set-text text])
                          (re-frame/dispatch [::text-animation/set-parts text])
                          (re-frame/dispatch [::text-animation/apply])
                          (re-frame/dispatch [::question-form.actions/set-current-question-text-action text])))]
    [ui/grid {:container true
              :spacing   16
              :justify   "space-between"}
     [ui/grid {:item true :xs 6}
      [text-field {:label           "Text"
                   :value           translated-text
                   :on-change       handle-change
                   :variant         "outlined"
                   :margin          "normal"
                   :multiline       true
                   :rows            2
                   :full-width      true
                   :InputLabelProps {:shrink true}}]]]))
