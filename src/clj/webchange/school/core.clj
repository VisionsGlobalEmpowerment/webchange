(ns webchange.school.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [clojure.tools.logging :as log]
            [java-time :as jt]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :refer [->snake_case_keyword]]
            [webchange.auth.core :as auth]))

(defn get-current-school [] (db/get-first-school))

(defn create-school!
  [data]
  (let [prepared-data (transform-keys ->snake_case_keyword data)
        [{id :id}] (db/create-new-school! prepared-data)]
    [true {:id id}]))

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
    [true {:id id}]))

(defn delete-school!
  [id]
  (db/delete-school! {:id id})
  [true {:id id}])
