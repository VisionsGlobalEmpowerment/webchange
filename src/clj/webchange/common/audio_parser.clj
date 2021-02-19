(ns webchange.common.audio-parser
  (:require
    [webchange.common.audio-parser.animation :refer [phonemes->animations]]
    [webchange.common.audio-parser.recognizer :refer [get-phonemes]]))

(defn get-talking-animation
  [file-path start duration]
  (-> file-path
      (get-phonemes)
      (phonemes->animations start duration)))