(ns webchange.utils.audio-analyzer.talk-data
  (:require
    [webchange.utils.audio-analyzer.config :refer [confidence-chunk-threshold]]
    [webchange.utils.audio-analyzer.utils :refer [levenshtein-distance prepare-text]]
    [clojure.string :as str]
    [clojure.set :as set]))

(defn- find-word-matches
  "Finds all word matches in transcription"
  [word transcription-items]
  (let [word-len (count word)]
    (->> transcription-items
         (keep-indexed (fn [idx rec-item]
                         (let [distance (levenshtein-distance word (:word rec-item))
                               conf (/ (- word-len distance) word-len)]
                           (when (> conf confidence-chunk-threshold)
                             idx)))))))

(defn- has-conflicts?
  [items]
  (->> items
       (reduce (fn [{:keys [max]} {:keys [match]}]
                 (cond
                   (nil? match) {:max max}
                   (>= match max) {:max match}
                   :else (reduced {:conflict true})))
               {:max 0})
       (:conflict)))

(defn- eliminate-worst
  [items]
  (let [match-to-remove (->> items
                             (reduce (fn [{:keys [max conflicts] :as result} {:keys [match]}]
                                       (cond
                                         (nil? match)
                                         result
                                         (< match max)
                                         (let [conflicting (->> conflicts
                                                                keys
                                                                (filter #(< match %))
                                                                (concat [match]))]
                                           (reduce (fn [result match]
                                                     (update-in result [:conflicts match] inc))
                                                   result
                                                   conflicting))
                                         :else
                                         (-> result
                                             (assoc :max match)
                                             (assoc-in [:conflicts match] 0))))
                                     {:max 0 :conflicts {}})
                             :conflicts
                             (sort-by second)
                             last
                             first)]
    (map (fn [item] (if (= (:match item) match-to-remove)
                      (dissoc item :match)
                      item))
         items)))


(defn fix-order
  "Recursevely eliminate most conflicting matches. 
  e.g. 1 2 3 6 4 5 - should eliminate 6
       1 2 3 5 6 4 - should eliminate 4

  item {:text str 
        :match int}"
  [items]
  (loop [items items]
    (let [continue? (has-conflicts? items)]
      (if continue?
        (recur (eliminate-worst items))
        items))))

(defn- place-items-between
  [start end items]
  (let [duration (- end start)
        item-duration (/ duration (count items))]
    (map-indexed (fn [idx item]
                   (-> item
                       (assoc :start (+ start (* idx item-duration)))
                       (assoc :end (+ start (* (inc idx) item-duration))))
                   ) items)))

(defn- get-chunks
  "Get chunks with start and end marks for each chunk."
  [words transcription-items]
  (if (= (count words) (count transcription-items))
    transcription-items
    (let [orig-items (->> words
                          (map (fn [item]
                                 {:text item
                                  :matches (find-word-matches item transcription-items)}))
                          (reduce (fn [{:keys [used] :as result} {:keys [text matches]}]
                                    (let [idx (apply min (set/difference (set matches) (set used)))]
                                      (-> result
                                          (update :items conj {:text text :match-idx idx})
                                          (update :used conj idx))))
                                  {:items [] :used []})
                          (:items)
                          (fix-order))
          {:keys [items stack last-idx]} (reduce (fn [{:keys [stack last-idx] :as result} {:keys [match-idx] :as item}]
                                                   (if match-idx
                                                     (let [stacked-items (if (seq stack)
                                                                           (let [start (:end (get transcription-items last-idx))
                                                                                 end (:start (get transcription-items match-idx))]
                                                                             (place-items-between start end stack))
                                                                           [])]
                                                       (-> result
                                                           (update :items concat stacked-items)
                                                           (update :items concat [(get transcription-items match-idx)])
                                                           (assoc :last-idx match-idx)
                                                           (assoc :stack [])))
                                                     (-> result
                                                         (update :stack conj item))))
                                                 {:items [] :stack [] :last-idx 0} orig-items)
          start (or (:start (get transcription-items (inc last-idx))) (:start (get transcription-items last-idx)))
          end (:end (last transcription-items))]
      (-> items (concat (place-items-between start end stack))))))

(defn- pack-talk-data
  [items]
  (map-indexed (fn [_idx item]
                 {:end   (:end item),
                  :start (:start item),
                  :anim  "talk"})
               items))

(defn- pack-chunks
  [items]
  (map-indexed (fn [idx item]
                 {:at       (:start item),
                  :end      (:end item),
                  :chunk    idx,
                  :start    (:start item),
                  :duration (- (:end item) (:start item))})
               items))

(defn- get-talk-data-for-text
  [text data region]
  (let [items (->> data
                   (filter (fn [item]
                             (and (<= (:start region) (:start item)) (>= (:end region) (:end item)))))
                   (vec))
        prepared-text (-> text (prepare-text) (str/split " "))]
    (-> (get-chunks prepared-text items)
        (pack-talk-data))))

(defn get-chunks-for-text
  [text data region]
  (if (some? region)
    (let [items (->> data
                     (filter (fn [item]
                               (and (<= (:start region) (:start item)) (>= (:end region) (:end item)))))
                     (vec))
          prepared-text (-> text (prepare-text) (str/trim) (str/split " "))]
      (-> (get-chunks prepared-text items)
          (pack-chunks)))
    []))

(defn get-chunks-data-if-possible
  [{:keys [script text region]}]
  (get-chunks-for-text text script region))

(defn get-talk-data-if-possible
  [{:keys [script text region]}]
  (get-talk-data-for-text text script region))
