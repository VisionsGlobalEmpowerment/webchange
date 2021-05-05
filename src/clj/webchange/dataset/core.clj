(ns webchange.dataset.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [java-time :as jt]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :refer [->snake_case_keyword]]))

(defn get-course-datasets
  [course-slug]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        datasets (db/get-datasets-by-course {:course_id course-id})]
    {:datasets datasets}))

(defn get-dataset
  [dataset-id]
  (let [dataset (db/get-dataset {:id dataset-id})]
    {:dataset dataset}))

(defn get-dataset-by-name [course-slug dataset-name]
  (->> (get-course-datasets course-slug)
       :datasets
       (filter #(= dataset-name (:name %)))
       first))

(defn create-dataset!
  [data]
  (let [{course-id :id} (db/get-course {:slug (:course-slug data)})
        prepared-data (-> data
                          (assoc :course-id course-id)
                          (#(transform-keys ->snake_case_keyword %)))
        [{id :id}] (db/create-dataset! prepared-data)]
    {:id id}))

(defn update-dataset!
  [dataset-id data]
  (let [prepared-data (-> data
                          (assoc :id dataset-id)
                          (update-in [:scheme :fields] #(sort-by :name %)))]
    (db/update-dataset! prepared-data)
    [true {:id dataset-id}]))

(defn get-dataset-items
  [dataset-id]
  (let [items (db/get-dataset-items {:dataset_id dataset-id})]
    {:items items}))

(defn get-item
  [item-id]
  (let [item (db/get-dataset-item {:id item-id})]
    item))

(defn get-item-by-name
  [dataset-id name]
  (let [item (db/get-dataset-item-by-name {:dataset_id dataset-id :name name})]
    item))

(defn create-dataset-item!
  [data]
  (let [prepared-data (transform-keys ->snake_case_keyword data)
        [{id :id}] (db/create-dataset-item! prepared-data)]
    [true {:id id}]))

(defn create-dataset-item-with-id!
  [data]
  (let [prepared-data (transform-keys ->snake_case_keyword data)]
    (db/create-dataset-item-with-id! prepared-data)
    [true data]))

(defn update-dataset-item!
  [id data]
  (let [prepared-data (assoc data :id id)]
    (db/update-dataset-item! prepared-data)
    [true {:id   id
           :data prepared-data}]))

(defn update-dataset-item-with-version!
  [id data]
  (let [prepared-data (assoc data :id id)
        result (db/update-dataset-item-with-version! prepared-data)
        latest-version (db/get-dataset-item {:id id})]
    (if (< 0 result)
      [true latest-version]
      [false latest-version])))

(defn delete-dataset-item!
  [id]
  (db/delete-dataset-item! {:id id})
  [true {:id id}])

(defn get-dataset-lessons
  [dataset-id]
  (let [items (db/get-dataset-lessons {:dataset_id dataset-id})]
    {:lesson-sets items}))

(defn get-lesson-set-by-name
  [dataset-id name]
  (let [item (db/get-lesson-set-by-name {:dataset_id dataset-id :name name})]
    item))

(defn create-lesson-set!
  [data]
  (let [prepared-data (transform-keys ->snake_case_keyword data)
        [{id :id}] (db/create-lesson-set! prepared-data)]
    [true {:id     id
           :lesson (assoc prepared-data :id id)}]))

(defn update-lesson-set!
  [id data]
  (let [prepared-data (assoc data :id id)]
    (db/update-lesson-set! prepared-data)
    [true {:id     id
           :lesson prepared-data}]))

(defn delete-lesson-set!
  [id]
  (db/delete-lesson-set! {:id id})
  [true {:id id}])

(defn assets-from-item [asset-fields item]
  (map #(identity {:url (get-in item [:data (-> % :name keyword)]) :size 1 :type (:type %)}) asset-fields))

(defn is-asset? [field]
  (some #(= (:type field) %) ["audio" "image"]))

(defn asset-fields [dataset]
  (let [fields (get-in dataset [:scheme :fields])]
    (filter is-asset? fields)))

(defn assets-from-item-list [datasets items]
  (let [asset-fields (into {} (map #(identity [(:id %) (asset-fields %)])) datasets)]
    (mapcat #(assets-from-item (get asset-fields (:dataset-id %)) %) items)))

(defn get-course-lessons [course-slug]
  (let [{course-id :id} (db/get-course {:slug course-slug})
        datasets (db/get-datasets-by-course {:course_id course-id})
        items (db/get-course-items {:course_id course-id})
        lesson-sets (db/get-course-lessons {:course_id course-id})]
    {:datasets    datasets
     :items       items
     :lesson-sets lesson-sets
     :assets      (assets-from-item-list datasets items)}))
