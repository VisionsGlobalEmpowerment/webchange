(ns webchange.common.audio-parser.animation)

(def phonetic-animation-map
  {"A" "talk"
   "B" "talk"
   "C" "talk"
   "D" "talk"
   "E" "talk"
   "F" "talk"
   "G" "talk"
   "H" "talk"
   "X" nil})

(defn map-animations
  [data animation-names]
  (map
    #(-> %
         (assoc :anim (->> (:value %)
                           (get animation-names)))
         (dissoc :value))
    data))

(defn cut-extra-data
  [data start duration]
  (let [end (+ start duration)]
    (filter
      #(and (> (:start %) start)
            (< (:start %) end))
      data)))

(defn filter-empty-animations
  [animations]
  (filter #(not (nil? (:anim %))) animations))

(defn phonemes->animations
  [phonemes start duration]
  (-> phonemes
      (cut-extra-data start duration)
      (map-animations phonetic-animation-map)
      (filter-empty-animations)))
