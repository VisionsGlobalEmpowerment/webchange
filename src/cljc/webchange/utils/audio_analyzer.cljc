(ns webchange.utils.audio-analyzer
  (:require
    [clojure.edn :as edn]
    [clojure.string :as str]
    [webchange.utils.forty-two :as forty-two]))

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

(defn- levenshtein-distance-core
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

(def levenshtein-distance (memoize levenshtein-distance-core))

(defn prepare-text
  [text]
  (let [text (or text "")
        numbers (sort-by #(- 0 (count %)) (remove empty? (str/split text #"[^\d]+")))
        text-numbers (into {} (map (fn [number] [number (forty-two/words (edn/read-string number))]) numbers))
        numbers-to-search (reduce (fn [result number] (str result "|" number)) "" numbers)
        text (-> text
                 (str/replace #"[\s]" " ")
                 (str/replace #"[_~.<>{}()!№%:,;#$%^&*+='`]" "")
                 (str/replace #" +" " ")
                 (str/lower-case)
                 (str/trim))]
    (if (seq text-numbers)
      (str/replace text (re-pattern numbers-to-search) text-numbers)
      text)))

(comment
  (prepare-text "")
  (prepare-text nil)
  (prepare-text "Hello, World!")
  )
