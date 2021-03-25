(ns webchange.editor-v2.dialog.dialog-form.audio-assets.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.creation-progress.translation-progress.validate-action :as validate]
    [webchange.editor-v2.creation-progress.warning-icon :refer [warning-icon]]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.views :refer [add-audio-form]]
    [webchange.editor-v2.dialog.dialog-form.audio-assets.views-filter :as filter]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.dialog.dialog-form.audio-assets.views-audios-list :refer [audios-list]]
    [webchange.editor-v2.dialog.dialog-form.state.audios :as dialog-form.audios]))

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
       [:span {:style {:font-size   "14px"
                       :margin-left "8px"}}
        "Audio region is not selected"]])))

(defn audios-block
  []
  (let [current-filter @(re-frame/subscribe [::filter/current-audios-filter])
        audios (->> @(re-frame/subscribe [::dialog-form.audios/audios-list])
                    (filter-audios current-filter)
                    (sort-by :date >))]
    [:div
     [warning-block]
     [audios-list {:audios audios}]]))
