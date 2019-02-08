(ns webchange.class.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [clojure.tools.logging :as log]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :refer [->snake_case_keyword]]))

(defn get-classes []
  (log/warn "get-classes")
  (let [classes (db/get-classes)]
    {:classes classes}))

(defn get-class [id]
  (let [class (db/get-class {:id id})]
    class))

(defn get-students-by-class [class-id]
  (let [students (db/get-students-by-class {:class_id class-id})]
    {:students students}))

(defn create-class!
  [data]
  (let [prepared-data (transform-keys ->snake_case_keyword data)
        [{id :id}] (db/create-class! prepared-data)]
    [true {:id id}]))

(defn update-class!
  [id data]
  (let [prepared-data (assoc data :id id)]
    (db/update-class! prepared-data)
    [true {:id id}]))

(defn delete-class!
  [id]
  (db/delete-class! {:id id})
  [true {:id id}])

(defn create-student!
  [data]
  (let [prepared-data (transform-keys ->snake_case_keyword data)
        [{id :id}] (db/create-student! prepared-data)]
    [true {:id id}]))

(defn update-student!
  [id data]
  (let [prepared-data (-> (transform-keys ->snake_case_keyword data)
                          (assoc :id id))]
    (db/update-student! prepared-data)
    [true {:id id}]))

(defn delete-student!
  [id]
  (db/delete-student! {:id id})
  [true {:id id}])