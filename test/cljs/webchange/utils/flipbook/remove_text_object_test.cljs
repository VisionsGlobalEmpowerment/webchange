(ns webchange.utils.flipbook.remove-text-object-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.utils.flipbook :refer [remove-text-object]]
    [webchange.utils.flipbook.activity-data-mock :as activity-data]
    [webchange.utils.scene-action-data :refer [get-inner-action]]
    [webchange.utils.scene-object-data :refer [get-children]]))

(deftest test-remove-added-text-object
  (let [text-object-name :page-5-text-4
        actual-result (remove-text-object activity-data/data text-object-name)]
    (testing "text object should be removed from objects"
      (is (-> (get actual-result :objects)
              (contains? text-object-name)
              (not))))
    (testing "text object should be removed from parent children"
      (let [parent (get-in actual-result [:objects :page-5])]
        (is (->> (get-children parent)
                 (some #{(clojure.core/name text-object-name)})
                 (not)))))
    (testing "text object should be removed from text-animation actions"
      (let [text-animation-action (-> actual-result
                                      (get-in [:actions :page-5-action :data])
                                      (nth 1)
                                      (get-inner-action))]
        (is (-> (get text-animation-action :target)
                (nil?)))))))
