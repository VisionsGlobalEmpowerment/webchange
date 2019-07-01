(ns webchange.resources.core)

(def public-fold "./resources/public/")
(def js-fold (str public-fold "js/compiled/"))
(def css-fold (str public-fold "css/"))

(defn get-files-list
  [dir]
  (->> (clojure.java.io/file dir)
       (file-seq)
       (filter #(.isFile %))
       (mapv str)
       (map #(str "./" (subs % (count public-fold))))))

(defn get-app-resources
  []
  [true {:data (flatten ["./page-skeleton"
                         (get-files-list (str js-fold "app.js"))
                         (get-files-list (str js-fold "out/"))
                         (get-files-list css-fold)])}])
