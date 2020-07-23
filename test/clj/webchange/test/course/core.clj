(ns webchange.test.course.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [webchange.course.core :as course]
            [webchange.test.fixtures.core :as f]
            [clojure.test :refer :all]
            [config.core :refer [env]]
            [ring.mock.request :as mock]
            [webchange.handler :as handler]
            [clojure.data.json :as json]
            [java-time :as jt]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest course-can-be-retrieved
    (let [{course-slug :slug data :data} (f/course-created)]
      (is (= data (course/get-course-data course-slug)))))

(deftest scene-can-be-retrieved
  (let [{course-slug :course-slug scene-name :name} (f/scene-created)]
    (is (= {:test "test" :test-dash "test-dash-value" :test3 "test-3-value"} (course/get-scene-data course-slug scene-name)))))

(defn retrieve-editor-tags []
  (let [url (str "/api/courses/editor/tags")
        request (-> (mock/request :get url)
                    (mock/header :body-encoding "UTF-8")
                    (mock/header :accept "application/json")
                    f/teacher-logged-in)
        response (handler/dev-handler request)]
    (json/read-str (slurp (:body response)) :key-fn keyword)))


(defn retrieve-editor-assets [tag_id type]
  (let [url (f/build-url "/api/courses/editor/assets" {:tag tag_id :type type})
        request (-> (mock/request :get url)
                    (mock/header :body-encoding "UTF-8")
                    (mock/header :accept "application/json")
                    f/teacher-logged-in)
        response (handler/dev-handler request)]
    (json/read-str (slurp (:body response)) :key-fn keyword)))

(defn retrieve-editor-character-skin []
  (let [url "/api/courses/editor/character-skin"
        request (-> (mock/request :get url)
                    (mock/header :body-encoding "UTF-8")
                    (mock/header :accept "application/json")
                    f/teacher-logged-in)
        response (handler/dev-handler request)]
    (json/read-str (slurp (:body response)) :key-fn keyword)))