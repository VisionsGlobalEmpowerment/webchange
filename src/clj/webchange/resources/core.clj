(ns webchange.resources.core
  (:require
    [clojure.string :refer [starts-with?]]
    [webchange.course.core :as course]
    [webchange.dataset.core :as dataset]
    [webchange.interpreter.defaults :as defaults]))

(def public-fold "./resources/public/")
(def js-fold (str public-fold "js/compiled/"))
(def css-fold (str public-fold "css/"))
(def img-fold (str public-fold "images/"))

(defn get-files-list
  [dir]
  (->> (clojure.java.io/file dir)
       (file-seq)
       (filter #(.isFile %))
       (mapv str)
       (map #(str "./" (subs % (count public-fold))))))

(defn get-app-resources
  []
  [true {:data (flatten ["./manifest.json"
                         "./page-skeleton"
                         (get-files-list (str js-fold "app.js"))
                         (get-files-list (str js-fold "out/"))
                         (get-files-list css-fold)
                         (get-files-list img-fold)
                         (->> defaults/default-assets (map #(str "." (:url %))))
                         "https://fonts.googleapis.com/css?family=Luckiest+Guy"
                         "https://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic&subset=latin"
                         "https://fonts.gstatic.com/s/lato/v15/S6u9w4BMUTPHh6UVSwiPGQ.woff2"
                         "//cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css"
                         ])}])

(defn get-values-list
  [data]
  (cond
    (sequential? data) (flatten (map get-values-list data))
    (map? data) (get-values-list (vals data))
    :else data))

(defn find-resources
  [data]
  (filter #(and (string? %)
                (starts-with? % "/raw/")
                (not (starts-with? % "/raw/video/"))) (get-values-list data)))

(defn get-level-resources
  []
  (let [course-name "test"
        scenes (->> course-name
                    course/get-course-data
                    :scenes)
        scenes-resources (->> scenes
                              (map #(->> %
                                         (course/get-scene-data course-name)
                                         (find-resources))))
        api-data (->> scenes
                      (map #(str "/api/courses/" course-name "/scenes/" %))
                      (into [(str "/api/courses/" course-name)
                             (str "/api/courses/" course-name "/lesson-sets")
                             "/api/schools/current"]))
        lessons-resources (->> course-name
                               dataset/get-course-lessons
                               find-resources)]
    [true {:resources   (->> [scenes-resources
                              lessons-resources]
                             flatten
                             distinct)
           :scenes-data api-data}]))
