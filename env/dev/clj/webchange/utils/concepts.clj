(ns webchange.utils.concepts
  (:require
    [webchange.dataset.core :as dataset]))

(defn get-dataset-items
  [course-slug]
  (->> (dataset/get-course-lessons course-slug)
       (:items)))

(defn find-dataset-item
  [dataset-items {:keys [id]}]
  (some #(and (= (:id %) id) %) dataset-items))

(defn find-dataset-by-data
  [dataset-items predicate]
  (some #(and (predicate (:data %)) %) dataset-items))

(defn get-item-fields
  [dataset-item fields]
  (->> (get dataset-item :data [])
       (filter (fn [[field-name _]]
                 (some #{field-name} fields)))
       (into {})))

(defn update-dataset-item-data
  [id data-patch]
  (let [dataset-item (dataset/get-item id)
        updated-dataset-item (-> dataset-item
                                 (select-keys [:data :name :version])
                                 (update :data merge data-patch))]
    (dataset/update-dataset-item-with-version! id updated-dataset-item)))

(comment
  "Get course dataset items"
  (->> (get-dataset-items "spanish")
       (map #(select-keys % [:id :name]))))

(comment
  "Get concept fields for test"

  (-> (get-dataset-items "spanish")
      (find-dataset-item {:id 5358})
      (get-item-fields [:dialog-field-696fbc25-ff33-4a12-afcb-0b3dac637881])))
