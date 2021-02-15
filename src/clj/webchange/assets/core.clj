(ns webchange.assets.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [clojure.java.io :as io]
            [webchange.common.files :as f]
            [config.core :refer [env]]
            [mikera.image.core :as imagez]
            [clojure.string :refer [join]]
            [clojure.tools.logging :as log]))

(import 'java.security.MessageDigest
        'java.math.BigInteger)



(defn directories
  [config]
  (let [raw (f/relative->absolute-path "/raw/" config)
        public (f/relative->absolute-path "/upload/" config)]
    [raw public]))

(defn files
  [dir]
  (let [f (io/file dir)] (file-seq f)))


(defn md5
  [path]
  (->> path
       .getBytes
       (.digest (MessageDigest/getInstance "MD5"))
       (BigInteger. 1)
       (format "%032x")))

(defn crc32
  [^String string]
  (let [crc (java.util.zip.CRC32.)]
     (. crc update (. string getBytes))
    (str (Long/toHexString (. crc getValue)))))

(defn update-asset-hash!
  [path full-path]
  (let [file-crc (crc32 (slurp full-path))
        path-md5 (md5 path)]
    (db/update-asset-hash! {:path_hash path-md5
                            :file_hash file-crc
                            })))

(defn create-asset-hash!
  [path full-path]
  (let [path-md5 (md5 path)
        file-crc (crc32 (slurp full-path))]
    (db/create-asset-hash! {:path_hash path-md5
                            :path      path
                            :file_hash file-crc})))

(defn clear-asset-hash-table! []
  (db/clear-asset-hash!))

(defn store-asset-hash!
  [full-path]
  (let [path (clojure.string/replace-first full-path (:public-dir env) "")
        path-md5 (md5 path)
        asset-hash (db/get-asset-hash {:path_hash path-md5})]
    (if (= (count asset-hash) 0)
      (create-asset-hash! path full-path)
      (update-asset-hash! path full-path))
    {:path path
     :full-path full-path}
    )
  )

(defn remove-file-with-hash! [asset-hash]
  (io/delete-file (f/relative->absolute-path (:path asset-hash)))
  (db/remove-asset-hash! {:path_hash (:path-hash asset-hash)}))

(defn update-file-from-primary [path]
  (let [uri (f/relative->absolute-primary-uri path)
        file (f/relative->absolute-path path)]
    (try
      (clojure.java.io/make-parents file)
      (f/save-file-from-uri uri file)
      (store-asset-hash! file)
      (log/debug (str "Stored " uri " to " file))
      (catch Exception e
        (log/error (str "Can not download " uri ", because " (:cause (Throwable->map e))))))))

(defn make-thumbnail [source-file target-file width]
  (let [image (imagez/load-image source-file)
        image (imagez/resize image width)]
    (imagez/save image target-file)
    (store-asset-hash! target-file)))

;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;
;(defn- next-row
;  [previous current other-seq]
;  (reduce
;    (fn [row [diagonal above other]]
;      (let [update-val (if (= other current)
;                         diagonal
;                         (inc (min diagonal above (peek row))))]
;        (conj row update-val)))
;    [(inc (first previous))]
;    (map vector previous (next previous) other-seq)))
;
;(defn levenshtein-distance
;  "Compute the levenshtein distance between two [sequences]."
;  [sequence1 sequence2]
;  (cond
;    (and (empty? sequence1) (empty? sequence2)) 0
;    (empty? sequence1) (count sequence2)
;    (empty? sequence2) (count sequence1)
;    :else (peek
;            (reduce (fn [previous current] (next-row previous current sequence2))
;                    (map #(identity %2) (cons nil sequence2) (range))
;                    sequence1))))
;
;(def levenshtein-distance-mem (memoize levenshtein-distance))
;
;(def confidence-treshhold 0.5)
;
;(defn get-start-end-for-text
;  [text data]
;  (let [data-text (clojure.string/join " " (map (fn [item] (:word item)) data))
;        _ (println (clojure.string/split data-text " "))
;        text (-> (clojure.string/replace text #"[^A-Za-z ]" "")
;                 (clojure.string/replace #" +" " ")
;                 (clojure.string/lower-case))
;        text-length (count text)
;        text-to-search-length 30
;        text-to-search (subs text 0 text-to-search-length)
;        result-items (reduce (fn [result item]
;                               (let [word-len (count (:word item))
;                                     start (:counter result)
;                                     end (+ word-len start)]
;                                 (-> result
;                                     (update :items conj (-> item
;                                                             (assoc :start-text start)
;                                                             (assoc :end-text end)))
;                                     (assoc :counter (+ end 1))))
;                               ) {:counter 0 :items []} data)
;        data-text-length (count data-text)
;        candidates (doall (map (fn [i]
;                                 (let [end-subs (+ i text-to-search-length)
;                                       subs-to-compare (subs data-text i end-subs)
;                                       distance (levenshtein-distance-mem text-to-search subs-to-compare)]
;                                   {:distance distance
;                                    :conf     (/ (- text-to-search-length distance) text-to-search-length)
;                                    :text     subs-to-compare
;                                    :start    i
;                                    :end      end-subs
;                                    })
;                                 ) (reduce (fn [result item]
;                                             (let [pos (+ (last result) 1 (count item))]
;                                               (if (< pos (- data-text-length text-to-search-length))
;                                                 (conj result pos)
;                                                 result))
;                                             ) [0] (clojure.string/split data-text " "))))
;        best-candidate (reduce (fn [result candidate]
;                                 (if (and (> (:conf candidate) confidence-treshhold)
;                                          (> (:conf candidate) (:conf result)))
;                                   candidate result)
;                                 ) {:conf 0} candidates)
;        final-result (reduce (fn [result item]
;                               (if (and (contains? best-candidate :start)
;                                        (contains? best-candidate :end)
;                                        (>= (:end-text item) (:start best-candidate))
;                                        (<= (:start-text item) (+ (:start best-candidate) text-length)))
;                                 (cond-> result
;                                         (> (:end item) (:end result)) (assoc :end (:end item))
;                                         (< (:start item) (:start result)) (assoc :start (:start item)))
;                                 result))
;                             {:start 1000000} (:items result-items))]
;    final-result))
;
;
;(defn get-chunks-for-text
;  [text data]
;  (let [data-text (clojure.string/join " " (map (fn [item] (:word item)) data))
;        text (-> (clojure.string/replace text #"[^A-Za-z ]" "")
;                 (clojure.string/replace #" +" " ")
;                 (clojure.string/lower-case))
;        text-length (count text)
;        text-to-search-length 30
;        text-to-search (subs text 0 text-to-search-length)
;        result-items (reduce (fn [result item]
;                               (let [word-len (count (:word item))
;                                     start (:counter result)
;                                     end (+ word-len start)]
;                                 (-> result
;                                     (update :items conj (-> item
;                                                             (assoc :start-text start)
;                                                             (assoc :end-text end)))
;                                     (assoc :counter (+ end 1))))
;                               ) {:counter 0 :items []} data)
;        data-text-length (count data-text)
;        candidates (doall (map (fn [i]
;                                 (let [end-subs (+ i text-to-search-length)
;                                       subs-to-compare (subs data-text i end-subs)
;                                       distance (levenshtein-distance-mem text-to-search subs-to-compare)]
;                                   {:distance distance
;                                    :conf     (/ (- text-to-search-length distance) text-to-search-length)
;                                    :text     subs-to-compare
;                                    :start    i
;                                    :end      end-subs
;                                    })
;                                 ) (reduce (fn [result item]
;                                             (let [pos (+ (last result) 1 (count item))]
;                                               (if (< pos (- data-text-length text-to-search-length))
;                                                 (conj result pos)
;                                                 result))
;                                             ) [0] (clojure.string/split data-text " "))))
;        best-candidate (reduce (fn [result candidate]
;                                 (if (and (> (:conf candidate) confidence-treshhold)
;                                          (> (:conf candidate) (:conf result)))
;                                   candidate result)
;                                 ) {:conf 0} candidates)
;        chunks (reduce (fn [result item]
;                         (if (and (contains? best-candidate :start)
;                                  (contains? best-candidate :end)
;                                  (>= (:end-text item) (:start best-candidate))
;                                  (<= (:start-text item) (+ (:start best-candidate) text-length)))
;                           (conj result item)
;                           result)
;                         )
;                       [] (:items result-items))]
;    chunks))