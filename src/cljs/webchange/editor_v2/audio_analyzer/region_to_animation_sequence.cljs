(ns webchange.editor-v2.audio-analyzer.region-to-animation-sequence)

(defn- region-data->words-sequence
  [region-data audio-script]
  (if (some? region-data)
    (filter (fn [{:keys [start end]}]
              (and (>= start (:start region-data))
                   (<= end (:end region-data))))
            audio-script)
    []))

(defn- words-sequence->animation-sequence-data
  [words-sequence]
  (map (fn [{:keys [start end]}]
         {:start start
          :end   end
          :anim  "talk"})
       words-sequence))

(defn region-data->animation-sequence-data
  [region-data audio-script]
  (-> (region-data->words-sequence region-data audio-script)
      (words-sequence->animation-sequence-data)))
