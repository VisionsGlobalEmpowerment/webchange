(ns webchange.editor-v2.dialog.dialog-form.views-audio-warning
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.creation-progress.translation-progress.validate-action :as validate]
    [webchange.editor-v2.creation-progress.warning-icon :refer [warning-icon]]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]))

(defn audio-warning
  []
  (let [current-phrase-action @(re-frame/subscribe [::translator-form.actions/current-phrase-action])]
    (when-not (validate/phrase-audio-defined? current-phrase-action)
      [:div {:style {:display     "flex"
                     :align-items "center"}}
       [warning-icon]
       [:span {:style {:font-size   "14px"
                       :margin-left "8px"}}
        "Audio region is not selected"]])))
