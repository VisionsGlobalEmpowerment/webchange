(ns webchange.templates.library.flipbook.utils
  (:require
    [webchange.utils.flipbook :as utils]))

(def get-book-object-name utils/get-book-object-name)
(def get-pages-data utils/get-pages-data)

(defn get-text-name
  [template]
  (some->> template
           (filter (fn [[_ {:keys [type]}]] (= type "text")))
           (map first)
           (first)
           (clojure.core/name)))

;; Flipbook data

(defn stage-number->page-number
  [activity-data stage-number page-side]
  (let [stages (get-in activity-data [:metadata :stages])
        {:keys [pages-idx]} (nth stages stage-number)]
    (case page-side
      "left" (nth pages-idx 0)
      "right" (nth pages-idx 1))))

(defn get-pages-count
  [activity-data]
  (let [pages-data (get-pages-data activity-data)]
    (count pages-data)))

(defn get-page-data
  [activity-data page-number]
  (let [pages-data (get-pages-data activity-data)]
    (nth pages-data page-number)))
