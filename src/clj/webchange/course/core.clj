(ns webchange.course.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [java-time :as jt]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [webchange.scene :as scene]))

(def hardcoded {"test" true
                "english" true})

(defn get-course-templates
  [course-name]
  (if (contains? hardcoded course-name)
    (scene/get-templates course-name)
    {} ;; "Getting templates for not-hardcoded courses is not implemented"
    ))

(defn get-course-data
  [course-name]
  (let [course (if (contains? hardcoded course-name)
                 (scene/get-course course-name)
                 (let [{course-id :id} (db/get-course {:name course-name})
                       latest-version (db/get-latest-course-version {:course_id course-id})]
                   (:data latest-version)))
        templates (get-course-templates course-name)]
    (merge course {:templates templates})))

(defn get-scene-data
  [course-name scene-name]
  (if (contains? hardcoded course-name)
    (scene/get-scene course-name scene-name)
    (let [{course-id :id} (db/get-course {:name course-name})
          {scene-id :id} (db/get-scene {:course_id course-id :name scene-name})
          latest-version (db/get-latest-scene-version {:scene_id scene-id})]
      (:data latest-version))))

(defn get-or-create-scene! [course-id scene-name]
  (if-let [{scene-id :id} (db/get-scene {:course_id course-id :name scene-name})]
    scene-id
    (let [[{scene-id :id}] (db/create-scene! {:course_id course-id :name scene-name})]
      scene-id)))

(defn save-scene!
  [course-name scene-name scene-data owner-id]
  (let [{course-id :id} (db/get-course {:name course-name})
        scene-id (get-or-create-scene! course-id scene-name)
        created-at (jt/local-date-time)]
    (db/save-scene! {:scene_id scene-id
                     :data scene-data
                     :owner_id owner-id
                     :created_at created-at})
    [true {:id scene-id
           :name scene-name
           :created-at (str created-at)}]))

(defn save-course!
  [course-name data owner-id]
  (let [{course-id :id} (db/get-course {:name course-name})
        created-at (jt/local-date-time)]
    (db/save-course! {:course_id course-id
                     :data data
                     :owner_id owner-id
                     :created_at created-at})
    [true {:created-at (str created-at)}]))

(defn restore-course-version!
  [version-id owner-id]
  (let [{data :data course-id :course-id} (db/get-course-version {:id version-id})
        created-at (jt/local-date-time)]
    (db/save-course! {:course_id course-id
                      :data data
                      :owner_id owner-id
                      :created_at created-at})
    [true {:created-at (str created-at)}]))

(defn restore-scene-version!
  [version-id owner-id]
  (let [{data :data scene-id :scene-id} (db/get-scene-version {:id version-id})
        created-at (jt/local-date-time)]
    (db/save-scene! {:scene_id scene-id
                     :data data
                     :owner_id owner-id
                     :created_at created-at})
    [true {:created-at (str created-at)}]))

(defn get-course-versions
  [course-name]
  (let [{course-id :id} (db/get-course {:name course-name})
        versions (db/get-course-versions {:course_id course-id})]
    {:versions (->> versions
                    (map #(dissoc % :data))
                    (map #(assoc % :created-at (-> % :created-at str)))
                    (map #(assoc % :owner-name "todo")))}))

(defn get-scene-versions
  [course-name scene-name]
  (let [{course-id :id} (db/get-course {:name course-name})
        {scene-id :id} (db/get-scene {:course_id course-id :name scene-name})
        versions (db/get-scene-versions {:scene_id scene-id})]
    {:versions (->> versions
                    (map #(dissoc % :data))
                    (map #(assoc % :created-at (-> % :created-at str)))
                    (map #(assoc % :owner-name "todo")))}))