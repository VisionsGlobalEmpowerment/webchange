(ns webchange.test.fixtures.resources
  (:require [clojure.test :refer :all]
            [config.core :refer [env]]
            ))

(defn secondary-asset-difference-resource
  [id]
  (let [host-url (:host-url (env :secondary))]
    (str host-url "api/school/asset/difference/")))

