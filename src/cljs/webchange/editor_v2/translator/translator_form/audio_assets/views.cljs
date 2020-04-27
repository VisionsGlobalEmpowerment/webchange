(ns webchange.editor-v2.translator.translator-form.audio-assets.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.audio-assets.subs :as subs]
    [webchange.editor-v2.translator.translator-form.audio-assets.add-audio.views :refer [add-audio-form]]
    [webchange.editor-v2.translator.translator-form.audio-assets.audios-filter.views :refer [audios-filter]]
    [webchange.editor-v2.translator.translator-form.audio-assets.audios-list.views :refer [audios-list]]))

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
  [{:keys []}]
  (r/with-let [current-filter (r/atom nil)]
              (let [audios (->> @(re-frame/subscribe [::subs/audios-list])
                                (filter-audios @current-filter))
                    handle-filter-change (fn [filter] (reset! current-filter filter))]
                [:div
                 [audios-filter {:on-change handle-filter-change}]
                 [audios-list {:audios audios}]
                 [add-audio-form]])))
