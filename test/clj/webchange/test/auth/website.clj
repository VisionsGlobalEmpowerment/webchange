(ns webchange.test.auth.website
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [webchange.auth.website :as website])
  (:use clj-http.fake))

(deftest user-data-can-be-coerced
  (let [data (assoc f/website-user :id "123")
        coerced (website/coerce-user-types data)]
    (is (= 123 (:id coerced)))))
