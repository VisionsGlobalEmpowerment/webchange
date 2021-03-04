(ns webchange.templates.library.flipbook.utils)

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

(defn get-book-object-name
  [activity-data]
  (-> activity-data
      (get-in [:metadata :flipbook-name])
      (keyword)))

(defn get-pages-data
  [activity-data]
  (let [flipbook-name (get-book-object-name activity-data)]
    (get-in activity-data [:objects flipbook-name :pages])))

(defn get-pages-count
  [activity-data]
  (let [pages-data (get-pages-data activity-data)]
    (count pages-data)))

(defn get-page-data
  [activity-data page-number]
  (let [pages-data (get-pages-data activity-data)]
    (nth pages-data page-number)))
