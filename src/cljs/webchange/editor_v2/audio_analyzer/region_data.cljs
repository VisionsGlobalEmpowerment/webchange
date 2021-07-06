(ns webchange.editor-v2.audio-analyzer.region-data
  (:require
    [webchange.logger.index :as logger]
    [webchange.editor-v2.audio-analyzer.config :refer [analyze-string-length confidence-threshold end-search-distance]]
    [webchange.editor-v2.audio-analyzer.utils :refer [levenshtein-distance prepare-text]]))

(defn- prepare-result-items
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

(defn- get-candidates
  [text-to-search-length data-text text-to-search data-text-length]
  (doall (map (fn [i]
                (let [end-subs (+ i text-to-search-length)
                      subs-to-compare (subs data-text i end-subs)
                      distance (levenshtein-distance text-to-search subs-to-compare)]
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

(defn- select-best-candidate
  [candidates]
  (reduce (fn [result candidate]
            (if (and (> (:conf candidate) confidence-threshold)
                     (> (:conf candidate) (:conf result)))
              candidate result)
            ) {:conf 0} candidates))

(defn get-start-end-for-text
  [text data]
  (let [data-text (clojure.string/join " " (map (fn [item] (:word item)) data))
        data-text-length (count data-text)
        text (prepare-text text)
        text-length (count text)
        text-to-search-length (min analyze-string-length text-length)
        max-distance (* end-search-distance text-length)
        text-to-search (subs text 0 text-to-search-length)
        result-items (prepare-result-items data)
        candidates-start (get-candidates text-to-search-length data-text text-to-search data-text-length)
        best-candidate-start (select-best-candidate candidates-start)
        supposed-end (+ (:start best-candidate-start) text-length)
        max-supposed-end (+ (:start best-candidate-start) text-length max-distance)
        ;Adjust end of fragment to make sure that end fragment correctly selected
        best-candidate-end (if (< text-length analyze-string-length)
                             best-candidate-start
                             (let [text-to-search (subs text (- text-length analyze-string-length) text-length)
                                   candidates-end (get-candidates text-to-search-length data-text text-to-search data-text-length)
                                   ; End should not be far from start
                                   candidates-end (filter (fn [candidate] (> max-supposed-end (:end candidate))) candidates-end)
                                   best-candidate (select-best-candidate candidates-end)]
                               ;Check that end fragment found and looks good, if not fallback to default logic
                               (if (and (contains? best-candidate :start)
                                        (contains? best-candidate :end)
                                        (>= (:end best-candidate) (:end best-candidate-start)))
                                 best-candidate
                                 (assoc best-candidate-start :end supposed-end))))
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

(defn get-region-data-if-possible
  [{:keys [text script]}]
  (if (= 0 (count text))
    {}
    (let [region (get-start-end-for-text text script)
          matched? (contains? region :end)]
      (logger/trace "region-data" region text)
      (cond-> {:matched? matched?}
              matched? (assoc :region-data (merge region
                                                  {:duration (- (:end region) (:start region))}))))))
