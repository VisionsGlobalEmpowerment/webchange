(ns webchange.test.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture)

(deftest user-can-log-in
  (let [user (f/user-created)
        credentials {:user (select-keys user [:email :password])}
        request (-> (mock/request :post "/api/users/login")
                    (mock/header :content-type "application/json")
                    (mock/body (json/write-str credentials)))
        response (handler/dev-handler request)
        body (json/read-str (:body response))]
    (is (= 200 (:status response)))
    (is (= (:email user) (get-in body ["email"])))))

(deftest unauthorized-cannot-access-editor
  (let [request (mock/request :get "/editor")
        response (handler/dev-handler request)]
    (is (= 302 (:status response)))))

(deftest logged-in-user-can-access-editor
  (let [request (-> (mock/request :get "/editor")
                    f/user-logged-in)
        response (handler/dev-handler request)]
    (is (= 200 (:status response)))))

(deftest course-can-be-retrieved
  (let [course (f/course-created)
        response (f/get-course (:name course))
        body (json/read-str (:body response))]
    (is (= 200 (:status response)))
    (is (= (get-in course [:data :initial-scene]) (get body "initial-scene")))))

(deftest course-can-be-saved
  (let [course (f/course-created)
        edited-value "test-scene-edited"
        _ (f/save-course! (:name course) {:course {:initial-scene edited-value}})
        retrieved-value (-> (:name course) f/get-course :body json/read-str (get "initial-scene"))]
    (is (= edited-value retrieved-value))))

(deftest scene-can-be-retrieved
  (let [scene (f/scene-created)
        response (f/get-scene (:course-name scene) (:name scene))]
    (is (= 200 (:status response)))
    (is (= (get-in scene [:data :test]) (-> response :body json/read-str (get "test"))))))

(deftest scene-can-be-saved
  (let [scene (f/scene-created)
        edited-value "test-edited"
        _ (f/save-scene! (:course-name scene) (:name scene) {:scene {:test edited-value}})
        retrieved-value (-> (f/get-scene (:course-name scene) (:name scene))
                            :body
                            json/read-str
                            (get "test"))]
    (is (= edited-value retrieved-value))))

(deftest course-versions-can-be-retrieved
  (let [course (f/course-created)
        _ (f/save-course! (:name course) {:course {:initial-scene "edited-value"}})
        versions (-> (:name course) f/get-course-versions :body json/read-str (get "versions"))]
    (is (= 2 (count versions)))))

(deftest course-version-can-be-restored
  (let [course (f/course-created)
        original-value (-> course :data :initial-scene)
        _ (f/save-course! (:name course) {:course {:initial-scene "edited-value"}})
        _ (f/restore-course-version! (:version-id course))
        retrieved-value (-> (:name course) f/get-course :body json/read-str (get "initial-scene"))]
    (is (= original-value retrieved-value))))

(deftest scene-versions-can-be-retrieved
  (let [scene (f/scene-created)
        _ (f/save-scene! (:course-name scene) (:name scene) {:scene {:test "edited-value"}})
        versions (-> (f/get-scene-versions (:course-name scene) (:name scene))
                     :body
                     json/read-str
                     (get "versions"))]
    (is (= 2 (count versions)))))

(deftest scene-version-can-be-restored
  (let [scene (f/scene-created)
        original-value (-> scene :data :test)
        _ (f/save-scene! (:course-name scene) (:name scene) {:scene {:test "edited-value"}})
        _ (f/restore-scene-version! (:version-id scene))
        retrieved-value (-> (f/get-scene (:course-name scene) (:name scene))
                            :body
                            json/read-str
                            (get "test"))]
    (is (= original-value retrieved-value))))