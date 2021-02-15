(ns webchange.utils.text
  (:require
    [clojure.string :refer [index-of]]))

(defn- empty-chunk?
  [{:keys [start end]}]
  (>= start end))

(defn- part->chunk
  [phrase part start]
  (let [start (index-of phrase part start)
        end (+ start (count part))]
    {:start start :end end}))

(defn parts->chunks
  [phrase parts]
  (loop [idx 0
         tail parts
         chunks []]
    (if (empty? tail)
      chunks
      (let [chunk (part->chunk phrase (first tail) idx)]
        (if (empty-chunk? chunk)
          (recur (:end chunk)
                 (rest tail)
                 chunks)
          (recur (:end chunk)
                 (rest tail)
                 (conj chunks chunk)))))))

(defn text->chunks
  ([text]
   (text->chunks text text))
  ([text parts]
   (parts->chunks text (clojure.string/split parts #"[ \n\r]"))))

(defn chunks->parts
  [phrase chunks]
  (map #(subs phrase (:start %) (:end %)) chunks))

(defn text-equals-parts?
  [text parts]
  (let [original-stripped (clojure.string/replace text #"[ \n\r]" "")
        parts-stripped (->> (text->chunks text parts)
                            (chunks->parts text)
                            (clojure.string/join ""))]
    (= original-stripped parts-stripped)))
