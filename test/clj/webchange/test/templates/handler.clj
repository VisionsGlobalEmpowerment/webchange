(ns webchange.test.templates.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest templates-library-can-be-retrieved
  (let [retrieved (-> (f/get-template-library) :body slurp (json/read-str :key-fn keyword))]
    (is (= 1 (count retrieved)))
    (is (some #(= "casa" (:name %)) retrieved))))
