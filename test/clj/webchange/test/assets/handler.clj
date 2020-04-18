(ns webchange.test.assets.handler
  (:require [clojure.test :refer :all]
            [webchange.test.fixtures.core :as f]
            [webchange.assets.loader :as loader]
            [webchange.assets.core :as core]
            [clojure.data.json :as json]
            [webchange.db.core :refer [*db*] :as db]
            ))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest could-create-asset-hash
  (loader/execute ["calc-asset-hash" ] {:public-dir "test/clj/webchange/resources"})
  (is  (f/is-created-asset-hash? "3cebc3b9d34eab167aabb44b48028592"))
  )

(deftest could-update-hash-on-upload-asset
  (let [file-path "test/clj/webchange/resources/raw/background.png"]
      (f/upload-file! file-path)
      (let [row (first (db/get-all-asset-hash))]
        (is (= (core/crc32 (slurp (:path row))) (:file-hash row)))
        )
  ))

(deftest could-upload-asset
  (let [file-path "test/clj/webchange/resources/raw/background.png"
        result (f/upload-file! file-path)]
      (is (= (get (json/read-str (:body result)) "size") 21))
    ))
