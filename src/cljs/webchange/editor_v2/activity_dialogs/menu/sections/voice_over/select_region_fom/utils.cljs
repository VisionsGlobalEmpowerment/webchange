(ns webchange.editor-v2.activity-dialogs.menu.sections.voice-over.select-region-fom.utils
  (:require
    [webchange.editor-v2.audio-analyzer.index :as audio-analyzer]))

(defn get-region-data
  ([text audio-script]
   (audio-analyzer/get-region-data text audio-script))
  ([text audio-script restriction]
   (audio-analyzer/get-region-data text audio-script restriction)))

(defn get-animation-sequence-data
  [{:keys [form-data text audio-script]}]
  (cond
    (some? audio-script) (-> (get-region-data text audio-script (select-keys form-data [:start :end]))
                             (audio-analyzer/region-data->animation-sequence-data audio-script))
    :else [{:start (:start form-data)
            :end   (:end form-data)
            :anim  "talk"}]))

(defn get-text-animation-data
  [{:keys [text audio-script]}]
  (cond
    (some? audio-script) (-> (get-region-data text audio-script)
                             (audio-analyzer/region->text-animation text audio-script))
    :else []))