(ns webchange.test.course.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [webchange.auth.core :as auth])
  (:use clj-http.fake))

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
  (let [keys [:id :slug :name :lang :image-src :status :website-user-id :owner-id]
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

(def website-user-id 123)
(def website-user {:id website-user-id :email "email@example.com" :first_name "First" :last_name "Last"})

(deftest course-can-be-localized
  (let [course (f/course-created)
        new-language "new-language"]
    (with-global-fake-routes-in-isolation
      {(auth/website-user-resource website-user-id) (fn [request] {:status 200 :headers {} :body (json/write-str website-user)})}
      (let [new-course (-> (f/localize-course! (:id course) {:language new-language :user-id website-user-id}) :body (json/read-str :key-fn keyword))]
        (is (= new-language (:lang new-course)))))))

(deftest localized-course-can-be-retrieved
  (let [course (f/course-created)
        new-language "new-language"]
    (with-global-fake-routes-in-isolation
      {(auth/website-user-resource website-user-id) (fn [request] {:status 200 :headers {} :body (json/write-str website-user)})}
      (let [_ (f/localize-course! (:id course) {:language new-language :user-id website-user-id})
            my-courses (-> (f/get-courses-by-website-user website-user-id) :body (json/read-str :key-fn keyword))]
        (is (= 1 (count my-courses)))))))

(deftest available-courses-can-be-retrieved
  (let [course-name "available course"
        _ (f/course-created {:name course-name :status "published"})
        response (f/get-available-courses)
        courses (-> response :body (json/read-str :key-fn keyword))]
    (is (= 200 (:status response)))
    (is (= 1 (count courses)))
    (is (= course-name (-> courses first :name)))))
