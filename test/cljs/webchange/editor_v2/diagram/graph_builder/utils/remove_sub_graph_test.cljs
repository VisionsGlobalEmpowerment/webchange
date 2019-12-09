(ns webchange.editor-v2.diagram.graph-builder.utils.remove-sub-graph-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.editor-v2.diagram.graph-builder.utils.remove-sub-graph :refer [remove-sub-graph]]))

;             +-> C -> F
;    +---> B -|
; A -|        +-> D
;    +-> E

(deftest test-remove-sub-graph
  (testing "remove last node"
    (let [graph {:a {:connections {:root {:handlers {:next [:b :e]}}}}
                 :b {:connections {:a {:handlers {:next [:c :d]}}}}
                 :c {:connections {:b {:handlers {:next [:f]}}}}
                 :d {:connections {:b {:handlers {}}}}
                 :e {:connections {:a {:handlers {}}}}
                 :f {:connections {:c {:handlers {}}}}}
          start-node :f]
      (let [actual-result (remove-sub-graph graph start-node)
            expected-result {:a {:connections {:root {:handlers {:next [:b :e]}}}}
                             :b {:connections {:a {:handlers {:next [:c :d]}}}}
                             :c {:connections {:b {:handlers {}}}}
                             :d {:connections {:b {:handlers {}}}}
                             :e {:connections {:a {:handlers {}}}}}]
        (is (= actual-result expected-result)))))
  (testing "remove plain sub-tree"
    (let [graph {:a {:connections {:root {:handlers {:next [:b :e]}}}}
                 :b {:connections {:a {:handlers {:next [:c :d]}}}}
                 :c {:connections {:b {:handlers {:next [:f]}}}}
                 :d {:connections {:b {:handlers {}}}}
                 :e {:connections {:a {:handlers {}}}}
                 :f {:connections {:c {:handlers {}}}}}
          start-node :c]
      (let [actual-result (remove-sub-graph graph start-node)
            expected-result {:a {:connections {:root {:handlers {:next [:b :e]}}}}
                             :b {:connections {:a {:handlers {:next [:d]}}}}
                             :d {:connections {:b {:handlers {}}}}
                             :e {:connections {:a {:handlers {}}}}}]
        (is (= actual-result expected-result)))))
  (testing "remove sub-tree with forks"
    (let [graph {:a {:connections {:root {:handlers {:next [:b :e]}}}}
                 :b {:connections {:a {:handlers {:next [:c :d]}}}}
                 :c {:connections {:b {:handlers {:next [:f]}}}}
                 :d {:connections {:b {:handlers {}}}}
                 :e {:connections {:a {:handlers {}}}}
                 :f {:connections {:c {:handlers {}}}}}
          start-node :b]
      (let [actual-result (remove-sub-graph graph start-node)
            expected-result {:a {:connections {:root {:handlers {:next [:e]}}}}
                             :e {:connections {:a {:handlers {}}}}}]
        (is (= actual-result expected-result)))))
  (testing "remove root"
    (let [graph {:a {:connections {:root {:handlers {:next [:b :e]}}}}
                 :b {:connections {:a {:handlers {:next [:c :d]}}}}
                 :c {:connections {:b {:handlers {:next [:f]}}}}
                 :d {:connections {:b {:handlers {}}}}
                 :e {:connections {:a {:handlers {}}}}
                 :f {:connections {:c {:handlers {}}}}}
          start-node :a]
      (let [actual-result (remove-sub-graph graph start-node)
            expected-result {}]
        (is (= actual-result expected-result))))))
