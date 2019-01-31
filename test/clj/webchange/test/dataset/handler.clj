(ns webchange.test.dataset.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture)

(deftest dataset-list-can-be-retrieved
  (let [{course-id :id course-name :name} (f/course-created)
        _ (f/dataset-created {:name "dataset1" :course-id course-id})
        _ (f/dataset-created {:name "dataset2" :course-id course-id})
        datasets (-> course-name f/get-course-datasets :body (json/read-str :key-fn keyword) :datasets)]
    (is (= 2 (count datasets)))))

(deftest dataset-can-be-retrieved
  (let [dataset (f/dataset-created)
        retrieved-dataset (-> dataset :id f/get-dataset :body (json/read-str :key-fn keyword) :dataset)]
    (is (= (:id dataset) (:id retrieved-dataset)))
    (is (= (:name dataset) (:name retrieved-dataset)))))

(deftest dataset-can-be-created
  (let [{course-id :id course-name :name} (f/course-created)
        data {:course-id course-id :name "dataset" :scheme {:fields [{:name "src" :type "string"}]}}
        dataset (f/create-dataset! data)
        retrieved-dataset (-> course-name f/get-course-datasets :body (json/read-str :key-fn keyword) :datasets first)]
    (is (= (:name data) (:name retrieved-dataset)))
    (is (= (:scheme data) (:scheme retrieved-dataset)))))

(deftest dataset-can-be-updated
  (let [{dataset-id :id :as dataset} (f/dataset-created)
        updated-scheme {:fields [{:name "src" :type "string"}
                                 {:name "width" :type "number"}]}
        _ (f/update-dataset! dataset-id {:scheme updated-scheme})
        retrieved-dataset (-> dataset-id f/get-dataset :body (json/read-str :key-fn keyword) :dataset)]
    (is (= updated-scheme (:scheme retrieved-dataset)))))

(deftest item-can-be-retrieved
  (let [item (f/dataset-item-created)
        retrieved-item (-> item :id f/get-dataset-item :body (json/read-str :key-fn keyword) :item)]
    (is (= (:id item) (:id retrieved-item)))
    (is (= (:dataset-id item) (:dataset-id retrieved-item)))))

(deftest item-can-be-created
  (let [{dataset-id :id} (f/dataset-created)
        data {:dataset-id dataset-id :data {:src "some-test-src"}}
        item (f/create-dataset-item! data)
        retrieved-item (-> item :id f/get-dataset-item :body (json/read-str :key-fn keyword) :item)]
    (is (= (:id item) (:id retrieved-item)))))

(deftest item-can-be-updated
  (let [{item-id :id} (f/dataset-item-created)
        updated-data {:src "test-edited-value"}
        _ (f/update-dataset-item! item-id {:data updated-data})
        retrieved-item (-> item-id f/get-dataset-item :body (json/read-str :key-fn keyword) :item)]
    (is (= updated-data (:data retrieved-item)))))

(deftest item-can-be-deleted
  (let [{item-id :id} (f/dataset-item-created)
        _ (f/delete-dataset-item! item-id)
        status (-> item-id f/get-dataset-item :status)]
    (is (= 404 status))))

(deftest lesson-set-can-be-retrieved
  (let [lesson-set (f/lesson-set-created)
        retrieved (-> lesson-set :name f/get-lesson-set :body (json/read-str :key-fn keyword) :lesson-set)]
    (is (= (:name lesson-set) (:name retrieved)))))

(deftest lesson-set-can-be-created
  (let [{item-id :id dataset-id :dataset-id} (f/dataset-item-created)
        lesson-name "test-lesson"
        data {:dataset-id dataset-id :name lesson-name :data {:items [{:id item-id}]}}
        _ (f/create-lesson-set! data)
        retrieved (-> lesson-name f/get-lesson-set :body (json/read-str :key-fn keyword) :lesson-set)]
    (log/warn retrieved)
    (is (= item-id (-> retrieved :data :items first :id)))))

(deftest lesson-set-can-be-updated
  (let [{lesson-set-id :id name :name} (f/lesson-set-created)
        updated-data {:items [{:id 1} {:id 2}]}
        _ (f/update-lesson-set! lesson-set-id {:data updated-data})
        retrieved (-> name f/get-lesson-set :body (json/read-str :key-fn keyword) :lesson-set)]
    (is (= updated-data (:data retrieved)))))

(deftest leson-set-can-be-deleted
  (let [{id :id name :name} (f/lesson-set-created)
        _ (f/delete-lesson-set! id)
        status (-> name f/get-lesson-set :status)]
    (is (= 404 status))))