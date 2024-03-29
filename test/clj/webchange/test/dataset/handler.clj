(ns webchange.test.dataset.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest dataset-list-can-be-retrieved
  (let [{course-id :id course-slug :slug} (f/course-created)
        _ (f/dataset-created {:name "dataset1" :course-id course-id})
        _ (f/dataset-created {:name "dataset2" :course-id course-id})
        datasets (-> course-slug f/get-course-datasets :body slurp (json/read-str :key-fn keyword) :datasets)]
    (is (= 2 (count datasets)))))

(deftest dataset-can-be-retrieved
  (let [dataset (f/dataset-created)
        retrieved-dataset (-> dataset :id f/get-dataset :body slurp (json/read-str :key-fn keyword) :dataset)]
    (is (= (:id dataset) (:id retrieved-dataset)))
    (is (= (:name dataset) (:name retrieved-dataset)))))

(deftest dataset-can-be-created
  (let [{course-slug :slug} (f/course-created)
        data {:course-slug course-slug :name "dataset" :scheme {:fields [{:name "src" :type "string"}]}}
        dataset (f/create-dataset! data)
        retrieved-dataset (-> course-slug f/get-course-datasets :body slurp (json/read-str :key-fn keyword) :datasets first)]
    (is (= (:name data) (:name retrieved-dataset)))
    (is (= (:scheme data) (:scheme retrieved-dataset)))))

(deftest dataset-can-be-updated
  (let [{dataset-id :id version :version :as dataset} (f/dataset-created)
        updated-scheme {:fields [{:name "src" :type "string"}
                                 {:name "width" :type "number"}]}
        _ (f/update-dataset! dataset-id {:scheme updated-scheme :version version})
        retrieved-dataset (-> dataset-id f/get-dataset :body slurp (json/read-str :key-fn keyword) :dataset)]
    (is (= updated-scheme (:scheme retrieved-dataset)))
    (testing "Dataset can not be updated with stale version"
      (let [response (f/update-dataset! dataset-id {:scheme updated-scheme :version version})]
        (is (= 409 (:status response)))))))

(deftest item-can-be-retrieved
  (let [item (f/dataset-item-created)
        retrieved-item (-> item :id f/get-dataset-item :body slurp (json/read-str :key-fn keyword) :item)]
    (is (= (:id item) (:id retrieved-item)))
    (is (= (:dataset-id item) (:dataset-id retrieved-item)))))

(deftest item-can-be-created
  (let [{dataset-id :id} (f/dataset-created)
        data {:dataset-id dataset-id :name "test-item" :data {:src "some-test-src"}}
        item (f/create-dataset-item! data)
        retrieved-item (-> item :id f/get-dataset-item :body slurp (json/read-str :key-fn keyword) :item)]
    (is (= (:id item) (:id retrieved-item)))))

(deftest item-can-be-updated
  (let [{item-id :id version :version} (f/dataset-item-created)
        updated-data {:src "test-edited-value"}
        updated-name "edited-name"]
    (testing "Dataset item can be updated"
      (let [response (f/update-dataset-item! item-id {:data updated-data :name updated-name :version version})
            retrieved-item (-> item-id f/get-dataset-item :body slurp (json/read-str :key-fn keyword) :item)]
        (is (= 200 (:status response)))
        (is (= updated-data (:data retrieved-item)))
        (is (= updated-name (:name retrieved-item)))
        (is (< version (-> response :body slurp (json/read-str :key-fn keyword) :version)))))
    (testing "Dataset item can not be updated with stale version"
      (let [response (f/update-dataset-item! item-id {:data updated-data :name updated-name :version version})]
        (is (= 409 (:status response)))))))

(deftest item-can-be-deleted
  (let [{item-id :id} (f/dataset-item-created)
        _ (f/delete-dataset-item! item-id)
        status (-> item-id f/get-dataset-item :status)]
    (is (= 404 status))))

(deftest lesson-set-can-be-retrieved
  (let [{dataset-id :dataset-id name :name} (f/lesson-set-created)
        retrieved (-> (f/get-lesson-set dataset-id name) :body slurp (json/read-str :key-fn keyword) :lesson-set)]
    (is (= name (:name retrieved)))))

(deftest lesson-set-can-be-created
  (let [{item-id :id dataset-id :dataset-id} (f/dataset-item-created)
        lesson-name "test-lesson"
        data {:dataset-id dataset-id :name lesson-name :data {:items [{:id item-id}]}}
        _ (f/create-lesson-set! data)
        retrieved (-> (f/get-lesson-set dataset-id lesson-name) :body slurp (json/read-str :key-fn keyword) :lesson-set)]
    (is (= item-id (-> retrieved :data :items first :id)))))

(deftest lesson-set-can-be-updated
  (let [{lesson-set-id :id name :name dataset-id :dataset-id} (f/lesson-set-created)
        updated-data {:items [{:id 1} {:id 2}]}
        _ (f/update-lesson-set! lesson-set-id {:data updated-data})
        retrieved (-> (f/get-lesson-set dataset-id name) :body slurp (json/read-str :key-fn keyword) :lesson-set)]
    (is (= updated-data (:data retrieved)))))

(deftest lesson-set-can-be-deleted
  (let [{id :id name :name dataset-id :dataset-id} (f/lesson-set-created)
        _ (f/delete-lesson-set! id)
        status (-> (f/get-lesson-set dataset-id name) :status)]
    (is (= 404 status))))

(deftest course-lesson-sets-can-be-retrieved
  (let [{course-id :id course-slug :slug} (f/course-created)
        {dataset-id :id} (f/dataset-created {:course-id course-id})
        {item-id :id} (f/dataset-item-created {:dataset-id dataset-id})
        _ (f/lesson-set-created {:dataset-id dataset-id :data {:items [{:id item-id}]}})
        retrieved (-> course-slug f/get-course-lessons :body slurp (json/read-str :key-fn keyword))]
    (is (= 1 (count (:items retrieved))))
    (is (= 1 (count (:lesson-sets retrieved))))))

(deftest assets-for-items-can-be-retrieved
  (let [{course-id :id course-slug :slug} (f/course-created)
        {dataset-id :id} (f/dataset-created {:course-id course-id})
        {item-id :id} (f/dataset-item-created {:dataset-id dataset-id})
        _ (f/lesson-set-created {:dataset-id dataset-id :data {:items [{:id item-id}]}})
        retrieved (-> course-slug f/get-course-lessons :body slurp (json/read-str :key-fn keyword))
        asset (-> retrieved :assets first)]
    (is (not (nil? (:url asset))))
    (is (not (nil? (:type asset))))))

(deftest dataset-library-can-be-retrieved
  (let [retrieved (-> (f/get-dataset-library) :body slurp (json/read-str :key-fn keyword))]
    (is (= 2 (count retrieved)))))
