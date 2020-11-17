(ns webchange.editor-v2.translator.translator-form.audio-assets.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.views :refer [add-audio-form]]
    [webchange.editor-v2.dialog.dialog-form.audio-assets.views-filter :refer [audios-filter] :as filter]
    [webchange.editor-v2.translator.translator-form.audio-assets.audios-list.views :refer [audios-list]]
    [webchange.editor-v2.translator.translator-form.state.audios :as translator-form.audios]))

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

(defn audios-block
  []
  (let [current-filter @(re-frame/subscribe [::filter/current-audios-filter])
        audios (->> @(re-frame/subscribe [::translator-form.audios/audios-list])
                    (filter-audios current-filter)
                    (sort-by :date >))
        handle-filter-change (fn [filter] (reset! current-filter filter))]
    [:div
     [audios-filter {:on-change handle-filter-change}]
     [audios-list {:audios audios}]
     [add-audio-form]]))
