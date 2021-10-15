(ns webchange.assets.core
  (:require [webchange.db.core :as db]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [webchange.common.files :as f]
            [config.core :refer [env]]
            [mikera.image.core :as imagez]
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
  (let [crc (java.util.zip.CRC32.)
        _ (. crc update (. string getBytes))
        result (str (Long/toHexString (. crc getValue)))
        pad-length (- 8 (count result))]
    (apply str (concat (take pad-length (repeat "0")) result))))

(defn update-asset-hash!
  [path full-path]
  (let [file-crc (crc32 (slurp full-path))
        path-md5 (md5 path)]
    (db/update-asset-hash! {:path_hash path-md5
                            :file_hash file-crc})))

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
  (let [path (str/replace-first full-path (:public-dir env) "")
        path-md5 (md5 path)
        asset-hash (db/get-asset-hash {:path_hash path-md5})]
    (if (= (count asset-hash) 0)
      (create-asset-hash! path full-path)
      (update-asset-hash! path full-path))
    {:path path
     :full-path full-path}))

(defn remove-file-with-hash! [asset-hash]
  (io/delete-file (f/relative->absolute-path (:path asset-hash)) true)
  (db/remove-asset-hash! {:path_hash (:path-hash asset-hash)}))

(defn update-file-from-primary [path]
  (let [uri (f/relative->absolute-primary-uri path)
        file (f/relative->absolute-path path)]
    (try
      (io/make-parents file)
      (f/save-file-from-uri uri file)
      (store-asset-hash! file)
      (log/debug (str "Stored " uri " to " file))
      (catch Exception e
        (log/error (str "Can not download " uri ", because " (:cause (Throwable->map e))))))))

(defn- thumbnail-path
  "Get path for specified type of thumbnail"
  [source-file type]
  (let [parent-name (-> source-file f/get-directory f/get-file-name)]
    (str "/upload/thumbnails/" (name type) "/" parent-name "/" (f/get-file-name-without-extension source-file) ".png")))

(defn- width-from-type
  "Get width for specified type of thumbnail"
  [type]
  (get {:course 328} type))

(defn make-thumbnail
  ([source-file type]
   (let [thumbnail-file (thumbnail-path source-file type)
         thumbnail-path (f/relative->absolute-path thumbnail-file)
         source-path (f/relative->absolute-path source-file)
         width (width-from-type type)]
     (io/make-parents thumbnail-path)
     (make-thumbnail source-path thumbnail-path width)
     thumbnail-file))
  ([source-file target-file width]
   (let [image (imagez/load-image source-file)
         image (imagez/resize image width)]
     (imagez/save image target-file)
     (store-asset-hash! target-file))))
