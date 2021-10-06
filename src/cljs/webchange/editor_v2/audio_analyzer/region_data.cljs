(ns webchange.editor-v2.audio-analyzer.region-data
  (:require
    [webchange.logger.index :as logger]
    [webchange.editor-v2.audio-analyzer.config :refer [analyze-string-length confidence-threshold end-search-distance]]
    [webchange.utils.audio-analyzer :refer [levenshtein-distance prepare-text]]))

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
  ([data-text text-to-search]
   (get-candidates data-text text-to-search 0))
  ([data-text text-to-search start-from]
   (let [text-to-search-length (count text-to-search)
         data-text-length (count data-text)]
     (->> (clojure.string/split data-text " ")
          (reduce (fn [result item]
                    (let [pos (+ (last result) 1 (count item))]
                      (conj result pos))) [0])
          (filter #(or (= % 0) (< % start-from) (<= % (- data-text-length text-to-search-length))))
          (mapcat (fn [i]
                    [(let [supposed-end (+ i text-to-search-length)
                           end-subs (or (clojure.string/index-of data-text " " supposed-end)
                                        supposed-end)
                           subs-to-compare (subs data-text i end-subs)
                           text-to-search-length (count subs-to-compare)
                           distance (levenshtein-distance text-to-search subs-to-compare)]
                       {:distance distance
                        :conf     (/ (- text-to-search-length distance) text-to-search-length)
                        :text     subs-to-compare
                        :start    i
                        :end      (+ i text-to-search-length)})
                     (let [supposed-end (+ i text-to-search-length)
                           end-subs (or (clojure.string/index-of data-text " " supposed-end)
                                        supposed-end)
                           subs-to-compare (->> (clojure.string/split
                                                 (subs data-text i end-subs) #"\s")
                                                (butlast)
                                                (clojure.string/join " "))
                           text-to-search-length (count subs-to-compare)
                           distance (levenshtein-distance text-to-search subs-to-compare)]
                       {:distance distance
                        :conf     (/ (- text-to-search-length distance) text-to-search-length)
                        :text     subs-to-compare
                        :start    i
                        :end      (+ i text-to-search-length)})]))))))

(defn- select-best-candidate
  [candidates]
  (reduce (fn [result candidate]
            (if (and (> (:conf candidate) confidence-threshold)
                     (> (:conf candidate) (:conf result)))
              candidate result)
            ) {:conf 0} candidates))


(defn- text-to-search-start
  [text]
  (let [text-length (count text)
        text-to-search (subs text 0 analyze-string-length)]
    (if (> text-length (count text-to-search))
      (->> (clojure.string/split text-to-search #"\s")
           (butlast)
           (clojure.string/join " "))
      text-to-search)))

(defn- text-to-search-end
  [text]
  (let [text-length (count text)
        text-to-search (subs text (- text-length analyze-string-length) text-length)]
    (if (> text-length (count text-to-search))
      (->> (clojure.string/split text-to-search #"\s")
           (drop 1)
           (clojure.string/join " "))
      text-to-search)))

(defn- compute-duration
  [region]
  (assoc region :duration (- (:end region) (:start region))))

(defn- get-script-region-text
  [script region]
  (->> script
       (filter #(>= (:start %) (:start region)))
       (filter #(<= (:end %) (:end region)))
       (map :word)
       (clojure.string/join " ")))

(defn- prepare-candidate
  [text text-length data-text max-distance result-items candidate-start]
  (let [supposed-end (+ (:start candidate-start) text-length)
        max-supposed-end (+ (:start candidate-start) text-length max-distance)
        ;Adjust end of fragment to make sure that end fragment correctly selected
        candidate-end (if (<= text-length analyze-string-length)
                        candidate-start
                        (let [text-to-search (text-to-search-end text)
                              text-to-search-length (count text-to-search)
                              candidates-end (get-candidates data-text text-to-search (:start candidate-start))
                              ; End should not be far from start
                              candidates-end (filter (fn [candidate] (and (>= max-supposed-end (:end candidate))
                                                                          (>= (:start candidate) (:start candidate-start)))) candidates-end)
                              best-candidate (select-best-candidate candidates-end)]
                            ;Check that end fragment found and looks good, if not fallback to default logic
                          (if (and (contains? best-candidate :start)
                                   (contains? best-candidate :end)
                                   (>= (:end best-candidate) (:end candidate-start)))
                            best-candidate)))
        final-result (reduce (fn [result item]
                               (if (and (contains? candidate-start :start)
                                        (contains? candidate-end :end)
                                        (> (:end-text item) (:start candidate-start))
                                        (< (:start-text item) (:end candidate-end)))
                                 (cond-> result
                                   (>= (:end item) (:end result)) (assoc :end (:end item))
                                   (<= (:start item) (:start result)) (assoc :start (:start item)))
                                 result))
                             {:start 1000000} (:items result-items))]
    final-result))

(defn distinct-by
  "Returns a stateful transducer that removes elements by calling f on each step as a uniqueness key.
   Returns a lazy sequence when provided with a collection.
   see https://gist.github.com/thenonameguy/714b4a4aa5dacc204af60ca0cb15db43"
  ([f]
   (fn [rf]
     (let [seen (volatile! #{})]
       (fn
         ([] (rf))
         ([result] (rf result))
         ([result input]
          (let [v (f input)]
            (if (contains? @seen v)
              result
              (do (vswap! seen conj v)
                  (rf result input)))))))))
  ([f xs]
   (sequence (distinct-by f) xs)))

(defn get-start-end-options-for-text
  [text data]
  (let [data-text (clojure.string/join " " (map (fn [item] (:word item)) data))
        text (prepare-text text)
        text-length (count text)
        max-distance (* end-search-distance text-length)
        text-to-search (text-to-search-start text)
        result-items (prepare-result-items data)
        candidates-start (get-candidates data-text text-to-search)]
    (->> candidates-start
         (filter #(< confidence-threshold (:conf %)))
         (distinct-by :start)
         (sort-by :conf >)
         (map #(prepare-candidate text text-length data-text max-distance result-items %))
         (remove #(nil? (:end %)))
         (map compute-duration)
         (map #(assoc % :region-text (get-script-region-text data %))))))

(defn get-start-end-for-text
  [text data]
  (-> (get-start-end-options-for-text text data)
      first))

(defn get-region-data-if-possible
  [{:keys [text script]}]
  (if (= 0 (count text))
    {}
    (let [region (get-start-end-for-text text script)
          matched? (contains? region :end)]
      (logger/trace "region-data" region text)
      (cond-> {:matched? matched?}
              matched? (assoc :region-data region)))))

(defn get-region-options
  [{:keys [text script]}]
  (when (count text)
    (let [regions (get-start-end-options-for-text text script)]
      (logger/trace "region-options" regions text)
      {:matched? (not-empty regions)
       :regions regions})))
