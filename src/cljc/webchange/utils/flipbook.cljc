(ns webchange.utils.flipbook
  (:require
    [webchange.utils.log :refer [log]]
    [webchange.utils.list :refer [remove-by-value]]
    [webchange.utils.scene-data :refer [get-scene-object update-action]]
    [webchange.utils.scene-object-data :refer [get-children]]))

(defn get-book-object-name
  [activity-data]
  (get-in activity-data [:metadata :flipbook-name]))

(defn flipbook-activity?
  [activity-data]
  (-> (get-book-object-name activity-data)
      (some?)))

(defn get-pages-data
  [activity-data]
  (let [flipbook-name (get-book-object-name activity-data)]
    (get-in activity-data [:objects (keyword flipbook-name) :pages])))

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
      (cond-> page-data
              (:generated? page-object) (assoc :generated? true)))))

(defn page-object-name->page-number
  [activity-data page-object-name]
  (->> (get-pages-data activity-data)
       (map-indexed vector)
       (some (fn [[idx {:keys [object]}]]
               (and (= object page-object-name) idx)))))

(defn page-number->page-data
  [activity-data page-number]
  (-> (get-pages-data activity-data)
      (nth page-number nil)))

(defn page-number->stage-number
  [activity-data page-number]
  (when (number? page-number)
    (->> (get-stages-data activity-data)
         (map :pages-idx)
         (map-indexed vector)
         (some (fn [[stage-idx pages-numbers]]
                 (and (some #{page-number} pages-numbers) stage-idx))))))

(defn find-object-in-parent
  [activity-data current-object-name predicate result-data]
  (let [current-object-data (get-scene-object activity-data current-object-name)]
    (if (predicate {:name current-object-name
                    :data current-object-data})
      result-data
      (->> (get-children current-object-data)
           (map keyword)
           (some (fn [child-name]
                   (->> (-> result-data
                            (update :path conj child-name)
                            (assoc :parent current-object-name))
                        (find-object-in-parent activity-data child-name predicate))))))))

(defn find-object
  [activity-data predicate]
  (->> (get-pages-data activity-data)
       (map :object)
       (map keyword)
       (some (fn [page-object-name]
               (->> {:path [page-object-name]
                     :page page-object-name}
                    (find-object-in-parent activity-data page-object-name predicate))))))

(defn remove-text-object
  [activity-data object-name]
  (let [object-name (if (keyword? object-name) (clojure.core/name object-name) object-name)
        object-name-keyword (keyword object-name)
        {:keys [parent]} (find-object activity-data (fn [{:keys [name]}] (= name object-name-keyword)))]
    (-> activity-data
        (update :objects dissoc object-name-keyword)
        (update-in [:objects parent :children] remove-by-value object-name)
        (update-action (fn [{:keys [data]}]
                         (= (:target data) object-name))
                       {:target nil}))))
