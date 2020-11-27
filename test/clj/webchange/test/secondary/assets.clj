(ns webchange.test.secondary.assets
  (:require [clojure.test :refer :all]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [webchange.db.core :refer [*db*] :as db]
            [webchange.common.date-time :as dt]
            [webchange.test.fixtures.resources :as resources]
            [mount.core :as mount]
            [mockery.core :as mockery]
            [clojure.data.json :as json]
            [clojure.java.io :as io]
            [webchange.common.files :as files]
            [webchange.secondary.core :as core]
            [webchange.assets.core :as assets]
            [clojure.tools.logging :as log])
  (:use clj-http.fake)
  )

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest can-ask-to-remove
  (let [asset (f/asset-hash-created {})
        difference (f/get-difference [{
                                       :path-hash "123"
                                       :path "123"
                                       :file-hash "123"}])]
    (let [remove (:remove difference)
          first-remove (first remove)]
    (assert (= "123" (:path-hash first-remove)))
    (assert (= (count remove) 1))
    )))

(deftest can-ask-to-create
  (let [asset (f/asset-hash-created {})
        difference (f/get-difference [])]
    (let [update (:update difference)
          first-update (first update)]
      (assert (= (:path-hash first-update) (:path-hash asset)))
      (assert (= (count update) 1))
      )))

(deftest can-not-ask-to-update
  (let [asset (f/asset-hash-created {})
        difference (f/get-difference [asset])]
    (let [update (:update difference)]
      (assert (= (count update) 0))
      )))

(deftest can-ask-to-update
  (let [asset (f/asset-hash-created {})
        difference (f/get-difference [(-> asset (assoc :file-hash "123"))])]
    (let [update (:update difference)
          first-update (first update)]
      (assert (= (count update) 1))
      (assert (= (:path-hash first-update) (:path-hash asset)))
      (assert (not= (:file-hash first-update) "123"))
      )))

(deftest can-delete-file
  (let [test-file (f/create-test-file!)
        file-data (assets/store-asset-hash! test-file)]
    (with-global-fake-routes-in-isolation
      {(resources/secondary-asset-difference-resource f/default-school-id)
      (fn [request] {:status 200 :headers {} :body (json/write-str {:update [] :remove [{
                                                                                        :path test-file
                                                                                        :path-hash (assets/md5 (:path file-data))
                                                                                        }
                                                                                       ]})})}

      (core/update-assets!)
      (assert (not (.exists (io/file test-file))))
    )))

(deftest can-update-file
  (let [filename "test-file"]
  (mockery/with-mocks
    [io-copy {:target :webchange.common.files/save-file-from-uri}]
      (with-global-fake-routes-in-isolation
        {(resources/secondary-asset-difference-resource f/default-school-id)
        (fn [request] {:status 200 :headers {} :body (json/write-str {:update [{:path filename
                                                                                :path-hash (assets/md5 filename)}]
                                                                      :remove []})})}

        (core/update-assets!)
        (is @io-copy {:called? true
                      :call-count 1
                      :call-args [(files/relative->absolute-primary-uri filename)
                                  (files/relative->absolute-path filename)]})
      ))))
