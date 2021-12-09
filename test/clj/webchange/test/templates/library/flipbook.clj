(ns webchange.test.templates.library.flipbook
  (:require [clojure.test :refer :all]
            [webchange.test.fixtures.core :as f]
            [clojure.tools.logging :as log]
            [webchange.templates.library.flipbook.template :as t]))

(def defaults {:activity-name "Book",
               :cover-title "Test title",
               :name "Book",
               :cover-layout "title-top",
               :lang "English",
               :skills [],
               :illustrators ["empty"],
               :course-name "Test course",
               :cover-image {:src "test.png"},
               :authors ["TabSchool"],
               :template-id 24})

(deftest flipbook-defaults
  (testing "First content page index should be 4 for consistency"
    (let [created (t/create-activity defaults)]
      (is (= 4 (get-in created [:metadata :next-page-id]))))))

