(ns webchange.editor-v2.audio-analyzer
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.components.audio-wave-form.state :as state]
    ))

(defn- next-row
  [previous current other-seq]
  (reduce
    (fn [row [diagonal above other]]
      (let [update-val (if (= other current)
                         diagonal
                         (inc (min diagonal above (peek row))))]
        (conj row update-val)))
    [(inc (first previous))]
    (map vector previous (next previous) other-seq)))

(defn levenshtein-distance
  "Compute the levenshtein distance between two [sequences]."
  [sequence1 sequence2]
  (cond
    (and (empty? sequence1) (empty? sequence2)) 0
    (empty? sequence1) (count sequence2)
    (empty? sequence2) (count sequence1)
    :else (peek
            (reduce (fn [previous current] (next-row previous current sequence2))
                    (map #(identity %2) (cons nil sequence2) (range))
                    sequence1))))

(def levenshtein-distance-mem (memoize levenshtein-distance))

(def confidence-treshhold 0.5)
(def confidence-chunk-treshhold 0.8)

(defn prepare-result-items
  [data]
  (reduce (fn [result item]
            (let [word-len (count (:word item))
                  start (:counter result)
                  end (+ word-len start)]
              (-> result
                  (update :items conj (-> item
                                          (assoc :start-text start)
                                          (assoc :end-text end)))
                  (assoc :counter (+ end 1))))
            ) {:counter 0 :items []} data))

(defn prepare-text
  [text]
  (-> (clojure.string/replace text #"[^A-Za-z ]" "")
      (clojure.string/replace #" +" " ")
      (clojure.string/lower-case)))

(defn get-candidates
  [text-to-search-length data-text text-to-search data-text-length]
  (doall (map (fn [i]
                (let [end-subs (+ i text-to-search-length)
                      subs-to-compare (subs data-text i end-subs)
                      distance (levenshtein-distance-mem text-to-search subs-to-compare)]
                  {:distance distance
                   :conf     (/ (- text-to-search-length distance) text-to-search-length)
                   :text     subs-to-compare
                   :start    i
                   :end      end-subs
                   })
                ) (reduce (fn [result item]
                            (let [pos (+ (last result) 1 (count item))]
                              (if (< pos (- data-text-length text-to-search-length))
                                (conj result pos)
                                result))
                            ) [0] (clojure.string/split data-text " "))))
  )

(defn select-best-candidate
  [candidates]
  (reduce (fn [result candidate]
            (if (and (> (:conf candidate) confidence-treshhold)
                     (> (:conf candidate) (:conf result)))
              candidate result)
            ) {:conf 0} candidates))

(defn get-start-end-for-text
  [text data]
  (println "get-start-end-for-text" text data)
  (let [data-text (clojure.string/join " " (map (fn [item] (:word item)) data))
        data-text-length (count data-text)

        text (prepare-text text)
        text-length (count text)
        _ (println "prepare-text" text)

        text-to-search-length (min 30 text-length)
        text-to-search (subs text 0 text-to-search-length)

        result-items (prepare-result-items data)
        candidates (get-candidates text-to-search-length data-text text-to-search data-text-length)
        _ (println "candidates" candidates)
        best-candidate (select-best-candidate candidates)
        _ (println "best-candidate" best-candidate)

        final-result (reduce (fn [result item]
                               (if (and (contains? best-candidate :start)
                                        (contains? best-candidate :end)
                                        (>= (:end-text item) (:start best-candidate))
                                        (<= (:start-text item) (+ (:start best-candidate) text-length)))
                                 (cond-> result
                                         (> (:end item) (:end result)) (assoc :end (:end item))
                                         (< (:start item) (:start result)) (assoc :start (:start item)))
                                 result))
                             {:start 1000000} (:items result-items))]
    final-result))



(def ot ["game" "li" "in" "in" "for" "the" "syllables" "lets"])
(def rt [{:conf       0.996899,
          :end        20.399894,
          :start      19.92,
          :word       "game",
          :start-text 177,
          :end-text   181}
         {:conf       0.531265,
          :end        20.970088,
          :start      20.61,
          :word       "live",
          :start-text 182,
          :end-text   186}
         {:conf       0.517923,
          :end        21.15,
          :start      20.970088,
          :word       "in",
          :start-text 187,
          :end-text   189}
         {:conf       0.975353,
          :end        21.45,
          :start      21.15,
          :word       "for",
          :start-text 190,
          :end-text   193}
         {:conf       0.960079,
          :end        21.6,
          :start      21.45,
          :word       "the",
          :start-text 194,
          :end-text   197}
         {:conf       1,
          :end        22.44,
          :start      21.601198,
          :word       "syllables",
          :start-text 198,
          :end-text   207}])

(defn- pack-chunks
  [items]
  (map-indexed (fn [idx item]
                 {:at       (:start item),
                  :end      (:end item),
                  :chunk    idx,
                  :start    (:start item),
                  :duration (- (:end item) (:start item))}
                 ) items)
  )

(defn get-chunks
  [orig-text rec-text]
  (if (= (count orig-text) (count rec-text))
    (pack-chunks rec-text)
    (let [orig-items (doall (map (fn [item]
                                   {:text  item
                                    :match (remove nil? (map-indexed (fn [idx rec-item]
                                                                       (let [distance (levenshtein-distance-mem item (:word rec-item))
                                                                             len (count item)
                                                                             conf (/ (- len distance) len)]
                                                                         (if (> conf confidence-chunk-treshhold)
                                                                           idx
                                                                           nil)))
                                                                     rec-text))}
                                   ) orig-text))
          orig-items (reduce (fn [result item]
                               (let [used (:used result)
                                     match (:match item)
                                     num (apply min (vec (clojure.set/difference (set match) (set used))))]
                                 (-> result
                                     (update :items conj (-> item
                                                             (assoc :match num)))
                                     (update :used conj num)))
                               ) {:items [] :used []} orig-items)
          processed-items (reduce (fn [result item]
                                    (if (:match item)
                                      (do
                                        (let
                                          [stack-len (count (:stack result))
                                           stacked-items (if (< 0 stack-len)
                                                           (let
                                                             [margins (cond
                                                                        (> 0 (- (:match item) (:last result))) {:end   (:end (get rec-text (:last result)))
                                                                                                                :start (:start (get rec-text (:last result)))}
                                                                        (= 1 (- (:match item) (:last result))) {:end   (:end (get rec-text (:last result)))
                                                                                                                :start (:start (get rec-text (:match item)))}
                                                                        (< 1 (- (:match item) (:last result))) {:end   (:end (get rec-text (inc (:last result))))
                                                                                                                :start (:start (get rec-text (dec (:match item))))})
                                                              duration (- (:end margins) (:start margins))
                                                              item-duration (/ duration stack-len)]
                                                             (vec (doall (map-indexed (fn [idx item]
                                                                                        (-> item
                                                                                            (assoc :start (* (+ 1 idx) (:start margins)))
                                                                                            (assoc :end (+ item-duration (* (+ 1 idx) (:start margins)))))
                                                                                        ) (:stack result)))))
                                                           [])]
                                          (-> result
                                              (update :items vec)
                                              (update :items concat stacked-items)
                                              (update :items vec)
                                              (update :items conj (get rec-text (:match item)))
                                              (assoc :last (:match item))
                                              (update :idx inc)
                                              (assoc :stack []))))
                                      (do
                                        (-> result
                                            (update :idx inc)
                                            (update :stack conj item)))))
                                  {:items [] :stack [] :last 0 :idx 0} (:items orig-items))
          stack-len (count processed-items)
          step-duration (if (> stack-len 0) (/ 0.5 stack-len) 0)
          start-stack (:end (last (:items processed-items)))
          processed-items (-> processed-items
                              (update :items concat (map-indexed (fn [idx item]
                                                                   (-> item
                                                                       (assoc :start (* (+ 1 idx) start-stack))
                                                                       (assoc :end (+ step-duration (* (+ 1 idx) start-stack))))
                                                                   ) (:stack processed-items))))
          result-items (pack-chunks (:items processed-items))]
      result-items)))

(defn get-chunks-for-text
  [text data region]
  (let [chunks (filter (fn [item]
                         (and (<= (:start region) (:start item)) (>= (:end region) (:end item)))) data)]
    (println "get-chunks-for-text" (clojure.string/split text " ") chunks)
    (get-chunks (clojure.string/split text " ") chunks)))

(defn get-chunks-data-if-possible
  [text url region]
  (let [script-data @(re-frame/subscribe [::state/audio-script-data url])]
    (get-chunks-for-text text script-data region)))

(defn get-region-data-if-possible
  [text url]
  (let [script-data @(re-frame/subscribe [::state/audio-script-data url])
        region (get-start-end-for-text text script-data)]
    region))