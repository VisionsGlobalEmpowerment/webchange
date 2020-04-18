(ns webchange.assets.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [clojure.java.io :as io]
            [webchange.common.files :as f]
            [clojure.string :refer [join]]))

(import 'java.security.MessageDigest
        'java.math.BigInteger)

(defn directories
  [config]
  (let [raw (f/relative->absolute-path "/raw/" config)
        public (f/relative->absolute-path "/upload/" config)]
    [raw public]))

(defn files
  [dir]
  (let [f (io/file dir)]
    (file-seq f)))


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
    (str (Long/toHexString (. crc getValue))))
  )

(defn update-asset-hash!
  [path]
  (let [
        file-crc (crc32 (slurp path))
        path-md5 (md5 path)
        ]
    (db/update-asset-hash! {:path_hash path-md5
                            :file_hash file-crc
                            })
    )
  )

(defn create-asset-hash!
  [path]
  (let [path-md5 (md5 path)
        file-crc (crc32 (slurp path))]
    (db/create-asset-hash! {:path_hash path-md5
                            :path      path
                            :file_hash file-crc
                            })
    )
  )

(defn clear-asset-hash-table!
  []
  (db/clear-asset-hash!))

(defn store-asset-hash!
  [path]
  (let [path-md5 (md5 path)
        asset-hash (db/get-asset-hash {:path_hash path-md5})]
    (if (= (count asset-hash) 0) (create-asset-hash! path) (update-asset-hash! path))
    )
  )