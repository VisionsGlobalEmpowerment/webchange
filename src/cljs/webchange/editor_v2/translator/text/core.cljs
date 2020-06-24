(ns webchange.editor-v2.translator.text.core
  (:require
    [clojure.string :refer [index-of]]))

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
        (recur (:end chunk)
               (rest tail)
               (conj chunks chunk))))))

(defn chunks->parts
  [phrase chunks]
  (map #(subs phrase (:start %) (:end %)) chunks))
