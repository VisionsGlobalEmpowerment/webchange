(ns webchange.dataset.loader
  (:require [java-time :as jt]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [webchange.dataset.core :as core]
            [clojure.pprint :as p]
            [mount.core :as mount]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :refer [join]]))

(defn items-path
  [config course-slug dataset-name]
  (let [dir (or (:dataset-dir config) "resources/datasets/")]
    (str dir course-slug "-" dataset-name ".edn")))

(defn prepare-item
  [item]
  (-> item
      (update-in [:data] #(into (sorted-map) %))
      (dissoc :dataset-id)
      (dissoc :id)))

(defn name-in-list? [{name :name} names]
  (let [s (into #{} names)]
    (contains? s name)))

(defn item-name->item-id
  [dataset-id item]
  (let [{item-id :id} (core/get-item-by-name dataset-id (:name item))]
    {:id item-id}))

(defn item-id->item-name
  [item]
  (let [{name :name} (core/get-item (:id item))]
    {:name name}))

(defn prepare-items
  [items]
  (->> items
       (map item-id->item-name)
       (into [])))

(defn prepare-lesson
  [lesson]
  (-> lesson
      (update-in [:data :items] prepare-items)
      (dissoc :dataset-id)
      (dissoc :id)))

(defn merge-fields
  [original new]
  (let [new-names (->> (map :name new) (into #{}))]
    (->> original
         (remove #(name-in-list? % new-names))
         (concat new)
         (sort-by :name)
         (into []))))

(defn save
  [config course-slug new-name dataset-name]
  (let [dataset (core/get-dataset-by-name course-slug dataset-name)
        fields (->> (get-in dataset [:scheme :fields])
                    (sort-by :name)
                    (into []))
        items (->> (core/get-dataset-items (:id dataset))
                   :items
                   (map prepare-item)
                   (sort-by :name)
                   (into []))

        lessons (->> (core/get-dataset-lessons (:id dataset))
                     :lesson-sets
                     (map prepare-lesson)
                     (sort-by :name)
                     (into []))
        path (items-path config new-name dataset-name)]
    (p/pprint
      {:fields fields
       :items items
       :lessons lessons}
      (clojure.java.io/writer path))))

(defn load-lessons
  [dataset-id lessons]
  (doseq [{name :name :as lesson} lessons]
    (let [items (->> lesson
                     :data
                     :items
                     (map #(item-name->item-id dataset-id %)))]
      (if-let [db-lesson (core/get-lesson-set-by-name dataset-id name)]
        (core/update-lesson-set! (:id db-lesson) {:data {:items items}})
        (core/create-lesson-set! {:dataset-id dataset-id
                                  :name name
                                  :data {:items items}})))))

(defn load-info
  [config course-slug saved-name dataset-name]
  (let [path (items-path config saved-name dataset-name)
        data (-> path io/reader java.io.PushbackReader. edn/read)
        dataset (core/get-dataset-by-name course-slug dataset-name)]

    (core/update-dataset! (:id dataset) (assoc-in dataset [:scheme :fields] (:fields data)))))

(defn load-force
  [config course-slug saved-name dataset-name]
  (let [path (items-path config saved-name dataset-name)
        data (-> path io/reader java.io.PushbackReader. edn/read)
        dataset (core/get-dataset-by-name course-slug dataset-name)]

    (core/update-dataset! (:id dataset) (assoc-in dataset [:scheme :fields] (:fields data)))

    (doseq [item (:items data)]
      (if-let [db-item (core/get-item-by-name (:id dataset) (:name item))]
        (core/update-dataset-item! (:id db-item) item)
        (core/create-dataset-item! (-> item (assoc :dataset-id (:id dataset))))))

    (load-lessons (:id dataset) (:lessons data))))

(defn load-merge
  [config course-slug saved-name dataset-name & field-names]
  (let [path (items-path config saved-name dataset-name)
        data (-> path io/reader java.io.PushbackReader. edn/read)
        field-names (or field-names (->> (:fields data) (map :name)))
        fields (->> (:fields data)
                    (filter #(name-in-list? % field-names)))
        dataset (core/get-dataset-by-name course-slug dataset-name)]
    (core/update-dataset! (:id dataset) (update-in dataset [:scheme :fields] merge-fields fields))

    (doseq [item (:items data)]
      (if-let [db-item (core/get-item-by-name (:id dataset) (:name item))]
        (let [data (merge (:data db-item) (-> item :data (select-keys (map keyword field-names))))]
          (core/update-dataset-item! (:id db-item) {:name (:name item) :data data}))
        (core/create-dataset-item! (-> item (assoc :dataset-id (:id dataset))))))

    (load-lessons (:id dataset) (:lessons data))))

(def commands
  {"save-dataset"
   (fn [config args]
     (apply save config args))

   "load-dataset-force"
   (fn [config args]
     (apply load-force config args))

   "load-dataset-merge"
   (fn [config args]
     (apply load-merge config args))

   "load-dataset-info"
   (fn [config args]
     (apply load-info config args))})

(defn command? [[arg]]
  (contains? (set (keys commands)) arg))

(defn execute
  "args - vector of arguments, e.g: [\"save-dataset\" \"test\" \"course\"]"
  [args opts]
  (when-not (command? args)
    (throw
      (IllegalArgumentException.
        (str "unrecognized option: " (first args)
             ", valid options are:" (join ", " (keys commands))))))
  ((get commands (first args)) opts (rest args)))
