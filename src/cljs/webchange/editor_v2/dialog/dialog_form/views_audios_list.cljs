(ns webchange.editor-v2.dialog.dialog-form.views-audios-list
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-form.audio-assets.views-filter :as filter]
    [webchange.editor-v2.dialog.dialog-form.state.audios :as dialog-form.audios]
    [webchange.editor-v2.dialog.dialog-form.views-audios-list-item :refer [audios-list-item]]))

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

(defn- set-default-props
  [{:keys [alias] :as props}]
  (cond-> props
          (nil? alias) (assoc :alias "Name is not defined")))

(defn audios-list
  []
  (let [current-filter @(re-frame/subscribe [::filter/current-audios-filter])
        audios (->> @(re-frame/subscribe [::dialog-form.audios/audios-list])
                    (filter-audios current-filter)
                    (map set-default-props)
                    (sort-by :date >))]
    [:div.audios-list
     (for [audio-data audios]
       ^{:key (:url audio-data)}
       [audios-list-item audio-data])]))
