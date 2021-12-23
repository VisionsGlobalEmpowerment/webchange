(ns webchange.utils.flipbook)

(defn get-book-object-name
  [activity-data]
  (-> activity-data
      (get-in [:metadata :flipbook-name])
      (keyword)))

(defn flipbook-activity?
  [activity-data]
  (-> (get-book-object-name activity-data)
      (some?)))

(defn get-pages-data
  [activity-data]
  (let [flipbook-name (get-book-object-name activity-data)]
    (get-in activity-data [:objects flipbook-name :pages])))

(defn get-stages-data
  [activity-data]
  (get-in activity-data [:metadata :stages] []))

(defn get-stage-data
  [activity-data stage-idx]
  (when (number? stage-idx)
    (let [stages-data (get-stages-data activity-data)]
      (nth stages-data stage-idx))))

(defn stage-idx->page-idx
  [activity-data stage-idx page-side]
  (let [stage-pages (-> (get-stages-data activity-data)
                        (nth stage-idx)
                        (get :pages-idx))]
    (case page-side
      "left" (first stage-pages)
      "right" (second stage-pages))))

(defn get-page-data
  [activity-data page-idx]
  (when (some? page-idx)
    (let [page-data (-> (get-pages-data activity-data)
                        (nth page-idx))
          page-object (->> (:object page-data)
                           (keyword)
                           (conj [:objects])
                           (get-in activity-data))]
      (-> page-data
          (assoc :generated? (:generated? page-object))))))

(defn page-object-name->page-number
  [activity-data page-object-name]
  (->> (get-pages-data activity-data)
       (map-indexed vector)
       (some (fn [[idx {:keys [object]}]]
               (and (= object page-object-name) idx)))))

(defn page-number->stage-number
  [activity-data page-number]
  (when (number? page-number)
    (->> (get-stages-data activity-data)
         (map :pages-idx)
         (map-indexed vector)
         (some (fn [[stage-idx pages-numbers]]
                 (and (some #{page-number} pages-numbers) stage-idx))))))
