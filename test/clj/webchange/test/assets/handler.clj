(ns webchange.test.assets.handler
  (:require [clojure.test :refer :all]
            [webchange.test.fixtures.core :as f]
            [webchange.assets.loader :as loader]
            [webchange.assets.core :as core]
            [clojure.data.json :as json]
            [webchange.common.files :as files]
            [webchange.db.core :refer [*db*] :as db]
            ))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest could-create-asset-hash
  (loader/execute ["calc-asset-hash" ] {:public-dir "test/clj/webchange/resources"})
  (is  (f/is-created-asset-hash? "3cebc3b9d34eab167aabb44b48028592")))

(deftest could-update-hash-on-upload-asset
  (let [file-path "test/clj/webchange/resources/raw/background.png"]
      (f/upload-file! file-path)
      (let [row (first (db/get-all-asset-hash))]
        (is (= (core/crc32 (slurp (files/relative->absolute-path (:path row)))) (:file-hash row))))))

(deftest could-upload-asset
  (let [file-path "test/clj/webchange/resources/raw/background.png"
        response (f/upload-file! file-path)
        body (-> response :body slurp (json/read-str :key-fn keyword))]
      (is (= (:size body) 21))))
