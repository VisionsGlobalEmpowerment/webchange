(ns webchange.editor-v2.audio-analyzer
  (:require
    [re-frame.core :as re-frame]
    [webchange.logger.index :as logger]
    [clojure.string :as s]
    [clojure.edn :as edn]
    [webchange.utils.forty-two :as forty-two]
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
(def analize-string-length 30)

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
  (let [numbers (sort-by #(- 0 (count %)) (remove empty? (clojure.string/split text #"[^\d]+")))
        text-numbers (into {} (map (fn [number] [number (forty-two/words (edn/read-string number))]) numbers))
        numbers-to-search (reduce (fn [result number] (str result "|" number)) numbers)
        text (cond-> (or text "")
                 true (clojure.string/replace #"[\s]" " ")
                     true (clojure.string/replace #"[^A-Za-z 0-9]" "")
                     true (clojure.string/replace #" +" " ")
                     true (clojure.string/lower-case)
                     (not (empty? text-numbers)) (clojure.string/replace (re-pattern numbers-to-search) text-numbers))]
    text
    ))

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
                ) (filter #(or (= % 0) (<= % (- data-text-length text-to-search-length)))
                          (reduce (fn [result item]
                                    (let [pos (+ (last result) 1 (count item))]
                                      (conj result pos))
                                    ) [0] (clojure.string/split data-text " ")))))
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
  (let [data-text (clojure.string/join " " (map (fn [item] (:word item)) data))
        data-text-length (count data-text)
        text (prepare-text text)
        text-length (count text)
        text-to-search-length (min analize-string-length text-length)
        text-to-search (subs text 0 text-to-search-length)
        result-items (prepare-result-items data)
        candidates-start (get-candidates text-to-search-length data-text text-to-search data-text-length)
        best-candidate-start (select-best-candidate candidates-start)
        ;Adjust end of fragment to make sure that end fragment correctly selected
        best-candidate-end (if (< text-length analize-string-length)
                             best-candidate-start
                             (let [text-to-search (subs text (- text-length analize-string-length) text-length)
                                   candidates-end (get-candidates analize-string-length data-text text-to-search text-length)
                                   best-candidate (select-best-candidate candidates-end)]
                               ;Check that end fragment found and looks good, if not fallback to default logic
                               (if (and (contains? best-candidate :start)
                                        (contains? best-candidate :end)
                                        (>= (:end best-candidate) (:end best-candidate-start)))
                                        best-candidate
                                        (assoc best-candidate-start :end (+ (:start best-candidate-start) text-length)))))
        final-result (reduce (fn [result item]
                               (if (and (contains? best-candidate-start :start)
                                        (contains? best-candidate-start :end)
                                        (>= (:end-text item) (:start best-candidate-start))
                                        (<= (:start-text item) (:end best-candidate-end)))
                                 (cond-> result
                                         (> (:end item) (:end result)) (assoc :end (:end item))
                                         (< (:start item) (:start result)) (assoc :start (:start item)))
                                 result))
                             {:start 1000000} (:items result-items))]
    final-result))

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
          stack-len (count (:stack processed-items))
          end-fragment (:end (last rec-text))
          next-unprocessed (get rec-text (inc (:last processed-items)))
          start-stack (if next-unprocessed (:start next-unprocessed) (:end (last (:items processed-items))))
          time-duration (max 0.5 (- end-fragment start-stack))
          step-duration (if (> stack-len 0) (/ time-duration stack-len) 0)
          processed-items (-> processed-items
                              (update :items concat (map-indexed (fn [idx item]
                                                                   (-> item
                                                                       (assoc :start (+ (* step-duration idx) start-stack))
                                                                       (assoc :end (+ start-stack (* (+ 1 idx) step-duration))))
                                                                   ) (:stack processed-items))))
          result-items (pack-chunks (:items processed-items))]
      result-items)))

(defn get-chunks-for-text
  [text data region]
  (let [chunks (filter (fn [item]
                         (and (<= (:start region) (:start item)) (>= (:end region) (:end item)))) data)]
    (get-chunks (-> text (prepare-text) (clojure.string/split " ")) (vec chunks))))

(defn get-chunks-data-if-possible
  [text url region]
  (let [script-data @(re-frame/subscribe [::state/audio-script-data url])]
    (get-chunks-for-text text script-data region)))

(defn get-region-data-if-possible
  [text url]
  (if (= 0 (count text))
    {}
    (let [script-data @(re-frame/subscribe [::state/audio-script-data url])
          region (get-start-end-for-text text script-data)]
      (logger/trace "region-data" region text)
      region)))
