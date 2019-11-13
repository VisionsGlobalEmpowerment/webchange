(ns webchange.editor-v2.diagram.graph-builder.utils.remove-node-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.diagram.graph-builder.utils.remove-node :refer [change-parent-node-connections
                                                                              change-child-node-connections
                                                                              remove-node]]))

(deftest test-change-parent-node-connections
  (testing "single connection change"
    (let [graph {:a {:connections {:root {:handlers {:next [:x]}}}}
                 :b {:connections {:root {:handlers {:next [:x]}}}}
                 :x {:connections {:a {:handlers {:next [:c]}}
                                   :b {:handlers {:next [:d]}}}}
                 :c {:connections {:x {:handlers {:next [:e]}}}}
                 :d {:connections {:x {:handlers {:next [:f]}}}}}
          parent-node-name :a
          removing-connection-name :x
          new-connection-names [:c]]
      (let [actual-result (change-parent-node-connections graph parent-node-name removing-connection-name new-connection-names)
            expected-result {:a {:connections {:root {:handlers {:next [:c]}}}}
                             :b {:connections {:root {:handlers {:next [:x]}}}}
                             :x {:connections {:a {:handlers {:next [:c]}}
                                               :b {:handlers {:next [:d]}}}}
                             :c {:connections {:x {:handlers {:next [:e]}}}}
                             :d {:connections {:x {:handlers {:next [:f]}}}}}]
        (is (= actual-result expected-result)))))
  (testing "multiple connection change"
    (let [graph {:a {:connections {:root {:handlers {:next [:x]}}}}
                 :x {:connections {:a {:handlers {:true  [:c]
                                                  :false [:d]}}}}
                 :c {:connections {:x {:handlers {:next [:e]}}}}
                 :d {:connections {:x {:handlers {:next [:f]}}}}}
          parent-node-name :a
          removing-connection-name :x
          new-connection-names [:c :d]]
      (let [actual-result (change-parent-node-connections graph parent-node-name removing-connection-name new-connection-names)
            expected-result {:a {:connections {:root {:handlers {:next [:c :d]}}}}
                             :x {:connections {:a {:handlers {:true  [:c]
                                                              :false [:d]}}}}
                             :c {:connections {:x {:handlers {:next [:e]}}}}
                             :d {:connections {:x {:handlers {:next [:f]}}}}}]
        (is (= actual-result expected-result))))))

(deftest test-change-child-node-connections
  (testing "single connection change"
    (let [graph {:a {:connections {:root {:handlers {:next [:x]}}}}
                 :x {:connections {:a {:handlers {:true  [:c]
                                                  :false [:d]}}}}
                 :c {:connections {:x {:handlers {:next [:e]}}}}
                 :d {:connections {:x {:handlers {:next [:f]}}}}}
          child-node-name :c
          removing-connection-name :x
          new-connection-names [:a]]
      (let [actual-result (change-child-node-connections graph child-node-name removing-connection-name new-connection-names)
            expected-result {:a {:connections {:root {:handlers {:next [:x]}}}}
                             :x {:connections {:a {:handlers {:true  [:c]
                                                              :false [:d]}}}}
                             :c {:connections {:a {:handlers {:next [:e]}}}}
                             :d {:connections {:x {:handlers {:next [:f]}}}}}]
        (is (= actual-result expected-result)))))
  (testing "multiple connection change"
    (let [graph {:a {:connections {:root {:handlers {:next [:x]}}}}
                 :b {:connections {:root {:handlers {:next [:x]}}}}
                 :x {:connections {:a {:handlers {:next [:c]}}
                                   :b {:handlers {:next [:c]}}}}
                 :c {:connections {:x {:handlers {:next [:e]}}}}}
          child-node-name :c
          removing-connection-name :x
          new-connection-names [:a :b]]
      (let [actual-result (change-child-node-connections graph child-node-name removing-connection-name new-connection-names)
            expected-result {:a {:connections {:root {:handlers {:next [:x]}}}}
                             :b {:connections {:root {:handlers {:next [:x]}}}}
                             :x {:connections {:a {:handlers {:next [:c]}}
                                               :b {:handlers {:next [:c]}}}}
                             :c {:connections {:a {:handlers {:next [:e]}}
                                               :b {:handlers {:next [:e]}}}}}]
        (is (= actual-result expected-result))))))

(deftest test-remove-node
  (testing "simple remove"
    (let [graph {:a {:connections {:root {:handlers {:next [:x]}}}}
                 :x {:connections {:a {:handlers {:next [:c]}}}}
                 :c {:connections {:x {:handlers {:next [:e]}}}}}
          node-name :x]
      (let [actual-result (remove-node graph node-name)
            expected-result {:a {:connections {:root {:handlers {:next [:c]}}}}
                             :c {:connections {:a {:handlers {:next [:e]}}}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result)))))
  (testing "branches merger deleting"
    (let [graph {:a {:connections {:root {:handlers {:next [:x]}}}}
                 :b {:connections {:root {:handlers {:next [:x]}}}}
                 :x {:connections {:a {:handlers {:next [:c]}}
                                   :b {:handlers {:next [:c]}}}}
                 :c {:connections {:x {:handlers {:next [:e]}}}}}
          node-name :x]
      (let [actual-result (remove-node graph node-name)
            expected-result {:a {:connections {:root {:handlers {:next [:c]}}}}
                             :b {:connections {:root {:handlers {:next [:c]}}}}
                             :c {:connections {:a {:handlers {:next [:e]}}
                                               :b {:handlers {:next [:e]}}}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result)))))
  (testing "two branches connector deleting"
    (let [graph {:a {:connections {:root {:handlers {:next [:x]}}}}
                 :b {:connections {:root {:handlers {:next [:x]}}}}
                 :x {:connections {:a {:handlers {:next [:c]}}
                                   :b {:handlers {:next [:d]}}}}
                 :c {:connections {:x {:handlers {:next [:e]}}}}
                 :d {:connections {:x {:handlers {:next [:f]}}}}}
          node-name :x]
      (let [actual-result (remove-node graph node-name)
            expected-result {:a {:connections {:root {:handlers {:next [:c]}}}}
                             :b {:connections {:root {:handlers {:next [:d]}}}}
                             :c {:connections {:a {:handlers {:next [:e]}}}}
                             :d {:connections {:b {:handlers {:next [:f]}}}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result)))))
  (testing "'if' node deleting"
    (let [graph {:a {:connections {:root {:handlers {:next [:x]}}}}
                 :x {:connections {:a {:handlers {:true  [:c]
                                                  :false [:d]}}}}
                 :c {:connections {:x {:handlers {:next [:e]}}}}
                 :d {:connections {:x {:handlers {:next [:f]}}}}}
          node-name :x]
      (let [actual-result (remove-node graph node-name)
            expected-result {:a {:connections {:root {:handlers {:next [:c :d]}}}}
                             :c {:connections {:a {:handlers {:next [:e]}}}}
                             :d {:connections {:a {:handlers {:next [:f]}}}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result)))))
  (testing "'if' node with multiple entries deleting"
    (let [graph {:a {:connections {:root {:handlers {:next [:x]}}}}
                 :b {:connections {:root {:handlers {:next [:x]}}}}
                 :x {:connections {:a {:handlers {:true  [:c]
                                                  :false [:d]}}
                                   :b {:handlers {:true  [:c]
                                                  :false [:d]}}}}
                 :c {:connections {:x {:handlers {:next [:e]}}}}
                 :d {:connections {:x {:handlers {:next [:f]}}}}}
          node-name :x]
      (let [actual-result (remove-node graph node-name)
            expected-result {:a {:connections {:root {:handlers {:next [:c :d]}}}}
                             :b {:connections {:root {:handlers {:next [:c :d]}}}}
                             :c {:connections {:a {:handlers {:next [:e]}}
                                               :b {:handlers {:next [:e]}}}}
                             :d {:connections {:a {:handlers {:next [:f]}}
                                               :b {:handlers {:next [:f]}}}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result))))))
