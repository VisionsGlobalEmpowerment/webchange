(ns webchange.editor-v2.translator.translator-form.views-form-target
  (:require
    [re-frame.core :as re-frame]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.translator.translator-form.common.views-target-selector :refer [target-selector]]))

(defn- get-styles
  []
  {:wrapper {:style {:margin-bottom "15px"}}
   :title   {:display "inline-block"
             :margin  "5px 0"}})

(defn target-block
  []
  (let [current-phrase-action @(re-frame/subscribe [::translator-form.actions/current-phrase-action])
        current-target (:target current-phrase-action)
        handle-target-change #(re-frame/dispatch [::translator-form.actions/set-phrase-action-target %])
        styles (get-styles)]
    [:div (:wrapper styles)
     [ui/typography {:variant "h6"
                     :style   (:title styles)}
      "Target:"]
     [target-selector {:value      (or current-target "")
                       :on-change  handle-target-change}]]))
