(ns webchange.migrations.kitkit.loader
  (:require
    [webchange.common.files :as f]
    [clojure.java.io :as io]
  [webchange.migrations.kitkit.book-import :as book-import]
  [clojure.string :refer [join]]))

(defn get-book-name [import-dir]
    (->> (.listFiles (io/file import-dir))
         (filter #(.isDirectory %))
         (filter (fn [dir] (.exists (io/file (str (.getAbsolutePath dir) "/bookinfo.csv")) )))))

(defn load-book
  ([config import-dir owner-id]
   (doseq [dir (get-book-name import-dir)]
     (load-book config import-dir owner-id (.getName dir))
   ))
  ([config import-dir owner-id name]
   (let [owner-id (Integer. owner-id)
         source-dir  (str import-dir name "/")
         public-dir  (str "/raw/book/" name "/")
         target-dir  (f/relative->absolute-path public-dir)]
     (-> source-dir
         (book-import/read-book-info)
         (book-import/import-assets-book-info source-dir target-dir public-dir)
         (book-import/prepare-book-info-by-sentence)
         (book-import/import-book-info-by-sentence owner-id name))))
  )


(def commands
  {"load-book"
   (fn [config args]
     (apply load-book config args))
   })

(defn command? [[arg]]
  (contains? (set (keys commands)) arg))

(defn execute
  [args opts]
  (when-not (command? args)
    (throw
      (IllegalArgumentException.
        (str "unrecognized option: " (first args)
             ", valid options are:" (join ", " (keys commands))))))
  ((get commands (first args)) opts (rest args)))
