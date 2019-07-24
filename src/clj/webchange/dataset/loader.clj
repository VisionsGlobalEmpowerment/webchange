(ns webchange.dataset.loader
  (:require [java-time :as jt]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [webchange.dataset.core :as core]
            [clojure.pprint :as p]
            [mount.core :as mount]
            [clojure.edn :as edn]
            [clojure.java.io :as io]))

(defn items-path
  [course-name dataset-name]
  (str "resources/datasets/" course-name "-" dataset-name ".edn"))

(defn sort-data
  [item]
  (update-in item [:data] #(into (sorted-map) %)))

(defn name-in-list? [{name :name} names]
  (let [s (into #{} names)]
    (contains? s name)))

(defn merge-fields
  [original new]
  (let [new-names (->> (map :name new) (into #{}))]
    (->> original
         (remove #(name-in-list? % new-names))
         (concat new)
         (sort-by :name)
         (into []))))

(defn save [course-name dataset-name]
  (mount/start)
  (let [dataset (core/get-dataset-by-name course-name dataset-name)
        fields (->> (get-in dataset [:scheme :fields])
                    (sort-by :name)
                    (into []))
        items (->> (core/get-dataset-items (:id dataset))
                   :items
                   (map sort-data)
                   (sort-by :id)
                   (into []))
        path (items-path course-name dataset-name)]
    (p/pprint
      {:fields fields
       :items items}
      (clojure.java.io/writer path))))

(defn load-force [course-name dataset-name]
  (mount/start)
  (let [path (items-path course-name dataset-name)
        data (-> path io/reader java.io.PushbackReader. edn/read)
        dataset (core/get-dataset-by-name course-name dataset-name)]
    (core/update-dataset! (:id dataset) (assoc-in dataset [:scheme :fields] (:fields data)))
    (doseq [item (:items data)]
      (core/delete-dataset-item! (:id item))
      (core/create-dataset-item-with-id! item))))

(defn load-merge [course-name dataset-name & field-names]
  (mount/start)
  (let [path (items-path course-name dataset-name)
        data (-> path io/reader java.io.PushbackReader. edn/read)
        field-names (or field-names (->> (:fields data) (map :name)))
        fields (->> (:fields data)
                    (filter #(name-in-list? % field-names)))
        dataset (core/get-dataset-by-name course-name dataset-name)
        db-items (core/get-dataset-items (:id dataset))
        get-db-item (fn [id] (->> db-items :items (filter #(= id (:id %))) first))]
    (core/update-dataset! (:id dataset) (update-in dataset [:scheme :fields] merge-fields fields))
    (doseq [item (:items data)]
      (let [db-item (get-db-item (:id item))
            data (merge (:data db-item) (-> item :data (select-keys (map keyword field-names))))]
        (core/update-dataset-item! (:id item) {:name (:name item) :data data})))))