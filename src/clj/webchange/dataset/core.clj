(ns webchange.dataset.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [java-time :as jt]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :refer [->snake_case_keyword]]))

(defn get-course-datasets
  [course-name]
  (let [{course-id :id} (db/get-course {:name course-name})
        datasets (db/get-datasets-by-course {:course_id course-id})]
    {:datasets datasets}))

(defn get-dataset
  [dataset-id]
  (let [dataset (db/get-dataset {:id dataset-id})]
    {:dataset dataset}))

(defn create-dataset!
  [data]
  (let [{course-id :id} (db/get-course {:name (:course-id data)})
        prepared-data (-> data
                          (assoc :course-id course-id)
                          (#(transform-keys ->snake_case_keyword %)))
        [{id :id}] (db/create-dataset! prepared-data)]
    [true {:id id}]))

(defn update-dataset!
  [dataset-id data]
  (let [prepared-data (assoc data :id dataset-id)]
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

(defn create-dataset-item!
  [data]
  (let [prepared-data (transform-keys ->snake_case_keyword data)
        [{id :id}] (db/create-dataset-item! prepared-data)]
    [true {:id id}]))

(defn update-dataset-item!
  [id data]
  (let [prepared-data (assoc data :id id)]
    (db/update-dataset-item! prepared-data)
    [true {:id id}]))

(defn delete-dataset-item!
  [id]
  (db/delete-dataset-item! {:id id})
  [true {:id id}])

(defn get-lesson-set-by-name
  [name]
  (let [item (db/get-lesson-set-by-name {:name name})]
    item))

(defn create-lesson-set!
  [data]
  (let [prepared-data (transform-keys ->snake_case_keyword data)
        [{id :id}] (db/create-lesson-set! prepared-data)]
    [true {:id id}]))

(defn update-lesson-set!
  [id data]
  (let [prepared-data (assoc data :id id)]
    (db/update-lesson-set! prepared-data)
    [true {:id id}]))

(defn delete-lesson-set!
  [id]
  (db/delete-lesson-set! {:id id})
  [true {:id id}])