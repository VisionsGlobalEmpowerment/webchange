(ns webchange.editor-v2.diagram.graph-builder.utils.node-children-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.editor-v2.diagram.graph-builder.utils.node-children :refer [get-children]]))

(deftest test-get-children
  (testing "getting children for not defined parent"
    (let [node-data {:connections {:root {:handlers {:next [:x]}}
                                   :a    {:handlers {:next [:y]}}
                                   :b    {:handlers {:next [:z]}}}}]
      (let [actual-result (get-children node-data)
            expected-result [:x]]
        (is (= actual-result expected-result)))))
  (testing "getting children for defined parent"
    (let [node-data {:connections {:root {:handlers {:next [:x]}}
                                   :a    {:handlers {:next [:y]}}
                                   :b    {:handlers {:next [:z]}}}}
          prev-node :a]
      (let [actual-result (get-children node-data prev-node)
            expected-result [:y]]
        (is (= actual-result expected-result)))))
  (testing "getting children for nil parent"
    (let [node-data {:connections {:root {:handlers {:next [:x]}}
                                   :a    {:handlers {:next [:y]}}
                                   :b    {:handlers {:next [:z]}}}}
          prev-node nil]
      (let [actual-result (get-children node-data prev-node)
            expected-result [:x :y :z]]
        (is (= actual-result expected-result))))))
