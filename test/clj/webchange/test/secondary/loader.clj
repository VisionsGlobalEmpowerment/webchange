(ns webchange.test.secondary.loader
  (:require
    [clojure.test :refer :all]
    [webchange.test.fixtures.core :as f]
    [webchange.db.core :refer [*db*] :as db]
    [webchange.secondary.loader :as loader]
    ))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture)


(deftest could-init-secondary
  (let [email "1@2.ru"
        school-id "1"
        school-id-int (Integer/parseInt school-id)
        ]
    (loader/execute ["init-secondary" school-id email, "123"] {})
    (let [user (db/find-user-by-email {:email email})
          school (db/get-school {:id school-id-int})
          ]
      (is (= (:email user) email))
      (is (= (:active user) true))
      (is (= (:id school) school-id-int))
      )))