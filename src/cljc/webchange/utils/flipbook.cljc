(ns webchange.utils.flipbook)

(defn get-book-object-name
  [activity-data]
  (-> activity-data
      (get-in [:metadata :flipbook-name])
      (keyword)))

(defn get-pages-data
  [activity-data]
  (let [flipbook-name (get-book-object-name activity-data)]
    (get-in activity-data [:objects flipbook-name :pages])))

(defn- get-stages-data
  [activity-data]
  (get-in activity-data [:metadata :stages]))

(defn get-stage-data
  [activity-data stage-idx]
  (-> (get-stages-data activity-data)
      (nth stage-idx)))
