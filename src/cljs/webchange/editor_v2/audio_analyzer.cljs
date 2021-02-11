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

(defn get-start-end-for-text
  [text data]
  (let [data-text (clojure.string/join " " (map (fn [item] (:word item)) data))
        text (-> (clojure.string/replace text #"[^A-Za-z ]" "")
                 (clojure.string/replace #" +" " ")
                 (clojure.string/lower-case))
        text-length (count text)
        text-to-search-length 30
        text-to-search (subs text 0 text-to-search-length)
        result-items (reduce (fn [result item]
                               (let [word-len (count (:word item))
                                     start (:counter result)
                                     end (+ word-len start)]
                                 (-> result
                                     (update :items conj (-> item
                                                             (assoc :start-text start)
                                                             (assoc :end-text end)))
                                     (assoc :counter (+ end 1))))
                               ) {:counter 0 :items []} data)
        data-text-length (count data-text)
        candidates (doall (map (fn [i]
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
        best-candidate (reduce (fn [result candidate]
                                 (if (and (> (:conf candidate) confidence-treshhold)
                                          (> (:conf candidate) (:conf result)))
                                   candidate result)
                                 ) {:conf 0} candidates)
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



(defn get-region-data-if-possible
  [text url]
  (let [script-data @(re-frame/subscribe [::state/audio-script-data url])
        region (get-start-end-for-text text script-data)]
    region))