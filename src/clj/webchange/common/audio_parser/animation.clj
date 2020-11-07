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

(defn join-same-animations
  [animations]
  (->> animations
       (reduce
         (fn [[first-r & rest-r] item]
           (if (nil? first-r)
             [item]
             (if (= (:end first-r) (:start item))
               (concat [(assoc first-r :end (:end item))] rest-r)
               (concat [item first-r] rest-r))))
         [])
       (reverse)))

(defn phonemes->animations
  [phonemes start duration]
  (-> phonemes
      (cut-extra-data start duration)
      (map-animations phonetic-animation-map)
      (filter-empty-animations)
      (join-same-animations)))
