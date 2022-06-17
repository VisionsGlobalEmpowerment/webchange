(ns webchange.editor-v2.question.question-form.audio-assets.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.creation-progress.translation-progress.validate-action :as validate]
    [webchange.editor-v2.creation-progress.warning-icon :refer [warning-icon]]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.views :refer [add-audio-form]]
    [webchange.editor-v2.dialog.dialog-form.audio-assets.views-filter :as filter]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.question.question-form.audio-assets.views-audios-list :refer [audios-list]]
    [webchange.editor-v2.question.question-form.state.audios :as question-form.audios]
    [webchange.ui-deprecated.theme :refer [get-in-theme]]))

(defn- filter-audios
  [filter-params audios-list]
  (filter (fn [audio-data]
            (reduce (fn [result [filter-key filter-value]]
                      (and result
                           (or (empty? filter-value)
                               (= filter-value (get audio-data filter-key)))))
                    true
                    filter-params))
          audios-list))

(defn- warning-block
  []
  (let [current-phrase-action @(re-frame/subscribe [::translator-form.actions/current-phrase-action])]
    (when-not (validate/phrase-audio-defined? current-phrase-action)
      [:div {:style {:display     "flex"
                     :align-items "center"}}
       [warning-icon]
       [ui/typography {:style {:color       (get-in-theme [:palette :warning :default])
                               :font-size   "14px"
                               :margin-left "8px"}}
        "Audio region is not selected"]])))

(defn audios-block
  []
  (let [current-filter @(re-frame/subscribe [::filter/current-audios-filter])
        audios (->> @(re-frame/subscribe [::question-form.audios/audios-list])
                    (filter-audios current-filter)
                    (sort-by :date >))]
    [:div
     [warning-block]
     [add-audio-form]
     [audios-list {:audios audios}]]))
