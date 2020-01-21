(ns webchange.editor-v2.graph-builder.utils.change-node-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.graph-builder.utils.change-node :refer [remove-connection
                                                                 change-parent-node-connections
                                                                 change-child-node-connections
                                                                 remove-node
                                                                 rename-node
                                                                 remove-handler]]))

(deftest test-remove-connection
  (let [graph {:b {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :e
                                   :sequence :a}
                                  {:previous :a
                                   :name     "next"
                                   :handler  :c
                                   :sequence :b}}}}
        node-name :b
        connection-data {:previous :a
                         :sequence :b}]
    (let [actual-result (remove-connection graph node-name connection-data)
          expected-result {:b {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :e
                                               :sequence :a}}}}]
      (is (= actual-result expected-result)))))

(deftest test-change-parent-node-connections--single-connection
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :x}}}
               :b {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :x}}}
               :x {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}
                                  {:previous :b
                                   :name     "next"
                                   :handler  :d}}}
               :c {:connections #{{:previous :x
                                   :name     "next"
                                   :handler  :e}}}
               :d {:connections #{{:previous :x
                                   :name     "next"
                                   :handler  :f}}}}
        parent-node-name :a
        removing-connection-name :x
        new-connection-names [:c]]
    (let [actual-result (change-parent-node-connections graph parent-node-name removing-connection-name new-connection-names)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :c}}}
                           :b {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :x}}}
                           :x {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :c}
                                              {:previous :b
                                               :name     "next"
                                               :handler  :d}}}
                           :c {:connections #{{:previous :x
                                               :name     "next"
                                               :handler  :e}}}
                           :d {:connections #{{:previous :x
                                               :name     "next"
                                               :handler  :f}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-change-parent-node-connections--multiple-connections
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :x}}}
               :x {:connections #{{:previous :a
                                   :name     "true"
                                   :handler  :c}
                                  {:previous :a
                                   :name     "false"
                                   :handler  :d}}}
               :c {:connections #{{:previous :x
                                   :name     "next"
                                   :handler  :e}}}
               :d {:connections #{{:previous :x
                                   :name     "next"
                                   :handler  :f}}}}
        parent-node-name :a
        removing-connection-name :x
        new-connection-names [:c :d]]
    (let [actual-result (change-parent-node-connections graph parent-node-name removing-connection-name new-connection-names)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :c}
                                              {:previous :root
                                               :name     "next"
                                               :handler  :d}}}
                           :x {:connections #{{:previous :a
                                               :name     "true"
                                               :handler  :c}
                                              {:previous :a
                                               :name     "false"
                                               :handler  :d}}}
                           :c {:connections #{{:previous :x
                                               :name     "next"
                                               :handler  :e}}}
                           :d {:connections #{{:previous :x
                                               :name     "next"
                                               :handler  :f}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-change-child-node-connections--single-connection-change
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :x}}}
               :x {:connections #{{:previous :a
                                   :name     "true"
                                   :handler  :c}
                                  {:previous :a
                                   :name     "false"
                                   :handler  :d}}}
               :c {:connections #{{:previous :x
                                   :name     "next"
                                   :handler  :e}}}
               :d {:connections #{{:previous :x
                                   :name     "next"
                                   :handler  :f}}}}
        child-node-name :c
        removing-connection-name :x
        new-connection-names [:a]]
    (let [actual-result (change-child-node-connections graph child-node-name removing-connection-name new-connection-names)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :x}}}
                           :x {:connections #{{:previous :a
                                               :name     "true"
                                               :handler  :c}
                                              {:previous :a
                                               :name     "false"
                                               :handler  :d}}}
                           :c {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :e}}}
                           :d {:connections #{{:previous :x
                                               :name     "next"
                                               :handler  :f}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-change-child-node-connections--multiple-connection-change
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :x}}}
               :b {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :x}}}
               :x {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}
                                  {:previous :b
                                   :name     "next"
                                   :handler  :c}}}
               :c {:connections #{{:previous :x
                                   :name     "next"
                                   :handler  :e}}}}
        child-node-name :c
        removing-connection-name :x
        new-connection-names [:a :b]]
    (let [actual-result (change-child-node-connections graph child-node-name removing-connection-name new-connection-names)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :x}}}
                           :b {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :x}}}
                           :x {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :c}
                                              {:previous :b
                                               :name     "next"
                                               :handler  :c}}}
                           :c {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :e}
                                              {:previous :b
                                               :name     "next"
                                               :handler  :e}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-change-child-node-connections--remove-connection-if-new-one-is-nil
  (let [graph {:c {:connections #{{:previous :x
                                   :name     "next"
                                   :handler  :e}
                                  {:previous :y
                                   :name     "next"
                                   :handler  :f}}}}
        child-node-name :c
        removing-connection-name :y
        new-connection-names nil]
    (let [actual-result (change-child-node-connections graph child-node-name removing-connection-name new-connection-names)
          expected-result {:c {:connections #{{:previous :x
                                               :name     "next"
                                               :handler  :e}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-remove-node--simple-remove
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :x}}}
               :x {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}}}
               :c {:connections #{{:previous :x
                                   :name     "next"
                                   :handler  :e}}}}
        node-name :x]
    (let [actual-result (remove-node graph node-name)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :c}}}
                           :c {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :e}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-remove-node--branches-merger-deleting
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :x}}}
               :b {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :x}}}
               :x {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}
                                  {:previous :b
                                   :name     "next"
                                   :handler  :c}}}
               :c {:connections #{{:previous :x
                                   :name     "next"
                                   :handler  :e}}}}
        node-name :x]
    (let [actual-result (remove-node graph node-name)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :c}}}
                           :b {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :c}}}
                           :c {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :e}
                                              {:previous :b
                                               :name     "next"
                                               :handler  :e}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-remove-node--two-branches-connector-deleting
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :x}}}
               :b {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :x}}}
               :x {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}
                                  {:previous :b
                                   :name     "next"
                                   :handler  :d}}}
               :c {:connections #{{:previous :x
                                   :name     "next"
                                   :handler  :e}}}
               :d {:connections #{{:previous :x
                                   :name     "next"
                                   :handler  :f}}}}
        node-name :x]
    (let [actual-result (remove-node graph node-name)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :c}}}
                           :b {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :d}}}
                           :c {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :e}}}
                           :d {:connections #{{:previous :b
                                               :name     "next"
                                               :handler  :f}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-remove-node--condition-node-deleting
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :x}}}
               :x {:connections #{{:previous :a
                                   :name     "true"
                                   :handler  :c}
                                  {:previous :a
                                   :name     "false"
                                   :handler  :d}}}
               :c {:connections #{{:previous :x
                                   :name     "next"
                                   :handler  :e}}}
               :d {:connections #{{:previous :x
                                   :name     "next"
                                   :handler  :f}}}}
        node-name :x]
    (let [actual-result (remove-node graph node-name)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :c}
                                              {:previous :root
                                               :name     "next"
                                               :handler  :d}}}
                           :c {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :e}}}
                           :d {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :f}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-remove-node--condition-node-with-multiple-entries-deleting
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :x}}}
               :b {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :x}}}
               :x {:connections #{{:previous :a
                                   :name     "true"
                                   :handler  :c}
                                  {:previous :a
                                   :name     "false"
                                   :handler  :d}
                                  {:previous :b
                                   :name     "true"
                                   :handler  :c}
                                  {:previous :b
                                   :name     "false"
                                   :handler  :d}}}
               :c {:connections #{{:previous :x
                                   :name     "next"
                                   :handler  :e}}}
               :d {:connections #{{:previous :x
                                   :name     "next"
                                   :handler  :f}}}}
        node-name :x]
    (let [actual-result (remove-node graph node-name)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :c}
                                              {:previous :root
                                               :name     "next"
                                               :handler  :d}}}
                           :b {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :c}
                                              {:previous :root
                                               :name     "next"
                                               :handler  :d}}}
                           :c {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :e}
                                              {:previous :b
                                               :name     "next"
                                               :handler  :e}}}
                           :d {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :f}
                                              {:previous :b
                                               :name     "next"
                                               :handler  :f}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-rename-node
  (testing "rename single node"
    (let [graph {:a {:connections #{{:previous :root
                                     :name     "next"
                                     :handler  :x}}}
                 :x {:connections #{{:previous :a
                                     :name     "next"
                                     :handler  :c}}}
                 :c {:connections #{{:previous :x
                                     :name     "next"
                                     :handler  :e}}}}
          old-name :x
          new-name :y]
      (let [actual-result (rename-node graph old-name new-name)
            expected-result {:a {:connections #{{:previous :root
                                                 :name     "next"
                                                 :handler  :y}}}
                             :y {:connections #{{:previous :a
                                                 :name     "next"
                                                 :handler  :c}}}
                             :c {:connections #{{:previous :y
                                                 :name     "next"
                                                 :handler  :e}}}}]
        (when-not (= actual-result expected-result)
          (print-maps-comparison actual-result expected-result))
        (is (= actual-result expected-result))))))

(deftest test-remove-handler--one-of-many
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b}
                                  {:previous :root
                                   :name     "next"
                                   :handler  :e}}}}
        node-name :a
        removing-connection-name :b]
    (let [actual-result (remove-handler graph node-name removing-connection-name)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :e}}}}]
      (is (= actual-result expected-result)))))

(deftest test-remove-handler--last
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b}}}}
        node-name :a
        removing-connection-name :b]
    (let [actual-result (remove-handler graph node-name removing-connection-name)
          expected-result {:a {:connections #{}}}]
      (is (= actual-result expected-result)))))
