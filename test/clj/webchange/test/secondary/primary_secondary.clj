(ns webchange.test.secondary.primary-secondary
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [webchange.db.core :refer [*db*] :as db]
            [webchange.common.date-time :as dt]
            [java-time :as jt]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [webchange.secondary.core :as core]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest can-create-update-course
  (let [course-created (f/course-created {:status "published"})
        scene-created (f/scene-created course-created)
        stat (core/get-course-update (str f/default-school-id) [])

        school (:school stat)
        course (first (:courses stat))
        course-versions (first (:course-versions stat))
        scene (first (:scenes stat))
        scene-version (first (:scene-versions stat))]
    (is (= (:id school) f/default-school-id))
    (is (= (:id course) (:course-id scene-created)))
    (is (= (:id course-versions) (:version-id course-created)))
    (is (= (:id scene) (:id scene-created)))
    (is (= (:id scene-version) (:version-id scene-created)))))

(deftest can-update-school
  (let  [name "NewName"
         update (as-> (core/get-course-update (str f/default-school-id) []) stat
                      (assoc stat :school {:id f/default-school-id :name name :location "test location" :about "test about"}))]
    (core/import-primary-data! update)
    (let [school (db/get-school {:id f/default-school-id})]
      (assert (= name (:name school))))))

(deftest can-update-course-and-version
  (let  [name "NewName"
         data {:initial-scene "helloKitty"}
         course-created (f/course-created)
         course-old (db/get-course {:slug (:slug course-created)})
         course-version-old (db/get-course-version {:id (:version-id course-created)})
         update (as-> (core/get-course-update (str f/default-school-id) []) stat
                      (assoc stat :courses [(-> course-old
                                                (assoc :name name))])
                      (assoc stat :course-versions [(-> course-version-old
                                                        (assoc :data data)
                                                        (assoc :created-at (dt/date-time2iso-str (jt/local-date-time)))
                                                        )])
                      )
         ]
    (core/import-primary-data! update)
    (let [course-updated (db/get-course {:slug (:slug course-created)})
          course-version-updated (db/get-latest-course-version {:course_id (:id course-created)})]
      (is (= name (:name course-updated)))
      (is (= data (:data course-version-updated))))))

(deftest can-update-scenes-and-version
  (let  [name "NewName"
         data {:initial-scene "helloKitty"}
         course (f/course-created {:status "published"})
         scene-created (f/scene-created course)
         scene-old (db/get-scene {:course_id (:course-id scene-created) :name (:name scene-created)})
         scene-version-old (db/get-scene-version {:id (:version-id scene-created)})
         update (as-> (core/get-course-update (str f/default-school-id) []) stat
                      (assoc stat :scenes [(-> scene-old
                                               (assoc :name name))])
                      (assoc stat :scene-versions  [(-> scene-version-old
                                                        (assoc :data data)
                                                        (assoc :created-at (dt/date-time2iso-str (jt/local-date-time))))]))]
    (core/import-primary-data! update)
    (let [scene-updated (db/get-scene {:course_id (:course-id scene-created) :name name})
          scene-version-updated (db/get-latest-scene-version {:scene_id (:id scene-created)})]
      (is (= name (:name scene-updated)))
      (is (= data (:data scene-version-updated))))))

(deftest can-remove-course-and-version
  (let  [course-created (f/course-created)
         _course-old (db/get-course {:slug (:slug course-created)})
         _course-version-old (db/get-course-version {:id (:version-id course-created)})
         update (as-> (core/get-course-update (str f/default-school-id) []) stat
                      (assoc stat :courses [])
                      (assoc stat :course-versions []))]
    (core/import-primary-data! update)
    (let [course-updated (db/get-course {:slug (:slug course-created)})
          course-version-updated (db/get-course-version {:id (:version-id course-created)})]
      (is (= nil course-updated))
      (is (= nil course-version-updated)))))

(deftest can-add-scene-skills
  (let  [skill-id 1
         course (f/course-created {:status "published"})
         scene-created (f/scene-created course)
         update (as-> (core/get-course-update (str f/default-school-id) []) stat
                      (assoc stat :scene-skills [{:scene-id (:id scene-created) :skill-id skill-id}]))]
    (core/import-primary-data! update)
    (let [[scene-skill] (db/get-scene-skills-by-scene {:scene_id (:id scene-created)})]
      (assert (not (nil? scene-skill)))
      (assert (= skill-id (:skill-id scene-skill))))))

(deftest can-remove-scene-skills
  (let  [skill-id 1
         scene-created (f/scene-created)
         scene-created (f/scene-skills-created (:id scene-created) skill-id)
         update (as-> (core/get-course-update (str f/default-school-id) []) stat
                      (assoc stat :scene-skills []))]
    (core/import-primary-data! update)
    (let [scene-skills (db/get-scene-skills-by-scene {:scene_id (:id scene-created)})]
      (assert (= 0 (count scene-skills))))))
