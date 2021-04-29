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
    (is (= {:test "test" :test-dash "test-dash-value" :test3 "test-3-value" :skills []} (course/get-scene-data course-slug scene-name)))))

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

(deftest can-update-course-background-thumbnails
  (let [_ (course/update-editor-assets {:public-dir "test/clj/webchange/resources"})]
    (is (.exists (clojure.java.io/as-file "test/clj/webchange/resources/raw/clipart-thumbs/casa_cut/casa_background.png")))
    (let [editor-asset (db/find-editor-assets-by-path {:path "/raw/clipart/casa_cut/casa_decoration.png"})
          editor-tag (db/find-editor-tag-by-name {:name "Casa"})
          editor-assets (db/find-editor-assets {:tag (:id editor-tag)})]
      (is (= (:thumbnail-path editor-asset) "/raw/clipart-thumbs/casa_cut/casa_decoration.png"))
      (is (= (:type editor-asset) "decoration"))
      (is editor-tag)
      (is (some #{(:id editor-asset)} (map :id editor-assets))))))

(deftest merge-fields
  (testing "Fields are not duplicated on merge"
    (let [original-fields [{:name "test-field",
                            :type "string",
                            :scenes ["test-scene"]}]
          new-fields [{:name "test-field"
                       :type "string"}]
          scene-slug "new-scene"
          merged (course/merge-fields original-fields new-fields scene-slug)]
      (is (= 1 (count merged)))
      (is (= "test-field" (-> merged first :name))))))
