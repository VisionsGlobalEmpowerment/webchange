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
     :full-path full-path}))

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
