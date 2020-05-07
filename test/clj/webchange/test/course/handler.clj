(ns webchange.test.course.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest course-can-be-retrieved
         (let [course (f/course-created)
               response (f/get-course (:slug course))
               body (json/read-str (:body response))]
           (is (= 200 (:status response)))
           (is (= (get-in course [:data :initial-scene]) (get body "initial-scene")))))

(deftest course-can-be-saved
         (let [course (f/course-created)
               edited-value "test-scene-edited"
               _ (f/save-course! (:slug course) {:course {:initial-scene edited-value}})
               retrieved-value (-> (:slug course) f/get-course :body json/read-str (get "initial-scene"))]
           (is (= edited-value retrieved-value))))

(deftest scene-can-be-retrieved
         (let [scene (f/scene-created)
               response (f/get-scene (:course-slug scene) (:name scene))]
           (is (= 200 (:status response)))
           (is (= (get-in scene [:data :test]) (-> response :body json/read-str (get "test"))))))

(deftest scene-can-be-saved
         (let [scene (f/scene-created)
               edited-value "test-edited"
               _ (f/save-scene! (:course-slug scene) (:name scene) {:scene {:test edited-value}})
               retrieved-value (-> (f/get-scene (:course-slug scene) (:name scene))
                                   :body
                                   json/read-str
                                   (get "test"))]
           (is (= edited-value retrieved-value))))

(deftest course-versions-can-be-retrieved
         (let [course (f/course-created)
               _ (f/save-course! (:slug course) {:course {:initial-scene "edited-value"}})
               versions (-> (:slug course) f/get-course-versions :body json/read-str (get "versions"))]
           (is (= 2 (count versions)))))

(deftest course-version-can-be-restored
         (let [course (f/course-created)
               original-value (-> course :data :initial-scene)
               _ (f/save-course! (:slug course) {:course {:initial-scene "edited-value"}})
               _ (f/restore-course-version! (:version-id course))
               retrieved-value (-> (:slug course) f/get-course :body json/read-str (get "initial-scene"))]
           (is (= original-value retrieved-value))))

(deftest scene-versions-can-be-retrieved
         (let [scene (f/scene-created)
               _ (f/save-scene! (:course-slug scene) (:name scene) {:scene {:test "edited-value"}})
               versions (-> (f/get-scene-versions (:course-slug scene) (:name scene))
                            :body
                            json/read-str
                            (get "versions"))]
           (is (= 2 (count versions)))))

(deftest scene-version-can-be-restored
         (let [scene (f/scene-created)
               original-value (-> scene :data :test)
               _ (f/save-scene! (:course-slug scene) (:name scene) {:scene {:test "edited-value"}})
               _ (f/restore-scene-version! (:version-id scene))
               retrieved-value (-> (f/get-scene (:course-slug scene) (:name scene))
                                   :body
                                   json/read-str
                                   (get "test"))]
           (is (= original-value retrieved-value))))

(deftest course-info-can-be-retrieved
  (let [keys [:id :slug :name :lang :image-src]
        course (f/course-created)
        response (f/get-course-info (:slug course))
        body (json/read-str (:body response) :key-fn keyword)]
    (is (= 200 (:status response)))
    (is (= (select-keys course keys) (select-keys body keys)))))

(deftest course-info-can-be-saved
  (let [course (f/course-created)
        name "name-edited"
        slug "slug-edited"
        lang "lang-edited"
        image-src "image-src-edited"
        _ (f/save-course-info! (:id course) {:name name :slug slug :lang lang :image-src image-src})
        retrieved (-> slug f/get-course-info :body (json/read-str :key-fn keyword))]
    (is (= name (:name retrieved)))
    (is (= slug (:slug retrieved)))
    (is (= lang (:lang retrieved)))
    (is (= image-src (:image-src retrieved)))))
