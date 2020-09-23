(ns webchange.test.templates.handler
  (:require [clojure.test :refer :all]
            [webchange.test.fixtures.core :as f]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest templates-library-can-be-retrieved
  (let [retrieved (-> (f/get-template-library) :body slurp (json/read-str :key-fn keyword))]
    (is (>= 4 (count retrieved)))
    (is (some #(= "casa" (:name %)) retrieved))))
