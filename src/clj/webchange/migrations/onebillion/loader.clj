(ns webchange.migrations.onebillion.loader
  (:require
    [webchange.common.files :as f]
    [clojure.java.io :as io]
    [webchange.migrations.onebillion.book-import :as book-import]
    [clojure.string :refer [join]]))

(defn get-book-name [import-dir]
    (->> (.listFiles (io/file import-dir))
         (filter #(.isDirectory %))
         (filter (fn [dir] (.exists (io/file (str (.getAbsolutePath dir) "/local/en_GB/book.xml")) )))))

(defn load-book
  ([config import-dir owner-id]
   (doseq [dir (get-book-name import-dir)]
     (load-book config import-dir owner-id (.getName dir))))
  ([config import-dir owner-id book-name]
   (let [owner-id (Integer. owner-id)
         source-dir  (str import-dir book-name "/")
         public-dir  (str "/raw/book-onebillion/" book-name "/")
         target-dir  (f/relative->absolute-path public-dir)]
     (println (book-import/import-book owner-id source-dir public-dir target-dir)))))

(def commands
  {"load-onebillion-book"
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
