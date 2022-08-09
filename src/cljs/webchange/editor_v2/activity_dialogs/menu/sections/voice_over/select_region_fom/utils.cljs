(ns webchange.editor-v2.activity-dialogs.menu.sections.voice-over.select-region-fom.utils
  (:require
    [webchange.utils.audio-analyzer.index :as audio-analyzer]))

(defn get-region-data
  ([text audio-script]
   (audio-analyzer/get-region-data text audio-script))
  ([text audio-script restriction]
   (audio-analyzer/get-region-data text audio-script restriction)))

(defn get-available-regions
  [text audio-script]
  (audio-analyzer/get-available-regions text audio-script))

(defn get-animation-sequence-data
  [{:keys [text audio-script selection-data]}]
  (if (some? audio-script)
    (let [text-recognition-result (-> (get-region-data text audio-script (select-keys selection-data [:start :end]))
                                      (audio-analyzer/region-data->animation-sequence-data audio-script))]
      (if-not (empty? text-recognition-result)
        text-recognition-result
        (audio-analyzer/region-data->animation-sequence-data selection-data audio-script)))
    [{:start (:start selection-data)
      :end   (:end selection-data)
      :anim  "talk"}]))

(defn get-text-animation-data
  [{:keys [text audio-script selection-data]}]
  (cond
    (some? audio-script) (-> (get-region-data text audio-script (select-keys selection-data [:start :end]))
                             (audio-analyzer/region->text-animation text audio-script))
    :else []))
