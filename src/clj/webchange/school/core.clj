(ns webchange.school.core
  (:require [webchange.db.core :as db]
            [webchange.events :as e]
            [clojure.tools.logging :as log]
            [camel-snake-kebab.core :refer [->snake_case_keyword]]))

(defn get-current-school [] (db/get-first-school))

(defn create-school!
  [data]
  (let [prepared-data (db/transform-keys-one-level ->snake_case_keyword data)
        [{id :id}] (db/create-new-school! prepared-data)]
    (e/dispatch {:type :schools/created :school-id id})
    [true (-> data
              (assoc :id id)
              (assoc :stats {}))]))

(defn get-school [id]
  (let [school (db/get-school {:id id})]
    school))

(defn get-schools []
  (let [schools (db/get-schools)]
    {:schools schools}))

(defn update-school!
  [id data]
  (let [prepared-data (assoc data :id id)]
    (db/update-school! prepared-data)
    (let [retrieved-school (db/get-school {:id id})]
      [true retrieved-school])))

(defn delete-school!
  [id]
  (db/delete-school! {:id id})
  [true {:id id}])

(defn school-teacher?
  [school-id user-id]
  (let [{teacher? :result} (db/is-school-teacher? {:school-id school-id :user-id user-id})]
    teacher?))

(defn school-admin?
  [school-id user-id]
  (let [{admin? :result} (db/is-school-admin? {:school-id school-id :user-id user-id})]
    admin?))
