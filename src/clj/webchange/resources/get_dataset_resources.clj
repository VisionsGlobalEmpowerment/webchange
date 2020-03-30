(ns webchange.resources.get-dataset-resources
  (:require
    [webchange.dataset.core :as dataset]
    [webchange.resources.utils :refer [find-resources]]))

(defn- get-datasets-ids
  [lesson-sets]
  (->> lesson-sets
       (map :dataset-id)
       (distinct)))

(defn- get-dataset-by-id
  [datasets id]
  (some
    (fn [dataset]
      (and (= id (:id dataset))
           dataset))
    datasets))

(defn- get-dataset-fields-names
  [scene-name dataset]
  (->> (get-in dataset [:scheme :fields])
       (filter (fn [field]
                 (and (contains? field :scenes)
                      (some #{scene-name} (:scenes field)))))
       (map :name)))

(defn- get-datasets-fields-names
  [lesson-sets datasets scenes-names]
  (let [datasets-ids (get-datasets-ids lesson-sets)]
    (->> scenes-names
         (map (fn [scene-name]
                (->> datasets-ids
                     (map (fn [datasets-id]
                            (->> datasets-id
                                 (get-dataset-by-id datasets)
                                 (get-dataset-fields-names scene-name)))))))
         (flatten)
         (map keyword))))

(defn- get-items-ids
  [lesson-sets]
  (->> lesson-sets
       (map (fn [lesson-set]
              (map :id (get-in lesson-set [:data :items]))))
       (flatten)
       (distinct)))

(defn- get-current-items
  [lesson-sets items]
  (let [items-ids (get-items-ids lesson-sets)]
    (filter (fn [{:keys [id]}] (some #{id} items-ids)) items)))

(defn- get-actions-resources
  [items fields-names]
  (->> items
       (map (fn [{:keys [data]}]
              (select-keys data fields-names)))
       (map find-resources)
       (flatten)
       (distinct)))

(defn get-dataset-resources
  [course-slug scenes-names]
  (let [{:keys [datasets items lesson-sets]} (dataset/get-course-lessons course-slug)
        current-items (get-current-items lesson-sets items)
        dataset-fields-names (get-datasets-fields-names lesson-sets datasets scenes-names)]
    (get-actions-resources current-items dataset-fields-names)))
