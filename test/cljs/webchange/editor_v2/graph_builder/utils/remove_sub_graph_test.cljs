(ns webchange.editor-v2.graph-builder.utils.remove-sub-graph-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.graph-builder.utils.remove-sub-graph :refer [remove-sub-graph]]))

;             +-> C -> F
;    +---> B -|
; A -|        +-> D
;    +-> E

(deftest test-remove-sub-graph--remove-last-node
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b}
                                  {:previous :root
                                   :name     "next"
                                   :handler  :e}}}
               :b {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}
                                  {:previous :a
                                   :name     "next"
                                   :handler  :d}}}
               :c {:connections #{{:previous :b
                                   :name     "next"
                                   :handler  :f}}}
               :d {:connections #{}}
               :e {:connections #{}}
               :f {:connections #{}}}
        start-node :f]
    (let [actual-result (remove-sub-graph graph start-node)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :b}
                                              {:previous :root
                                               :name     "next"
                                               :handler  :e}}}
                           :b {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :c}
                                              {:previous :a
                                               :name     "next"
                                               :handler  :d}}}
                           :c {:connections #{}}
                           :d {:connections #{}}
                           :e {:connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-remove-sub-graph--remove-plain-sub-tree
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b}
                                  {:previous :root
                                   :name     "next"
                                   :handler  :e}}}
               :b {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}
                                  {:previous :a
                                   :name     "next"
                                   :handler  :d}}}
               :c {:connections #{{:previous :b
                                   :name     "next"
                                   :handler  :f}}}
               :d {:connections #{}}
               :e {:connections #{}}
               :f {:connections #{}}}
        start-node :c]
    (let [actual-result (remove-sub-graph graph start-node)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :b}
                                              {:previous :root
                                               :name     "next"
                                               :handler  :e}}}
                           :b {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :d}}}
                           :d {:connections #{}}
                           :e {:connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-remove-sub-graph--remove-sub-tree-with-forks
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b}
                                  {:previous :root
                                   :name     "next"
                                   :handler  :e}}}
               :b {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}
                                  {:previous :a
                                   :name     "next"
                                   :handler  :d}}}
               :c {:connections #{{:previous :b
                                   :name     "next"
                                   :handler  :f}}}
               :d {:connections #{}}
               :e {:connections #{}}
               :f {:connections #{}}}
        start-node :b]
    (let [actual-result (remove-sub-graph graph start-node)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :e}}}
                           :e {:connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-remove-sub-graph--remove-root
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b}
                                  {:previous :root
                                   :name     "next"
                                   :handler  :e}}}
               :b {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}
                                  {:previous :a
                                   :name     "next"
                                   :handler  :d}}}
               :c {:connections #{{:previous :b
                                   :name     "next"
                                   :handler  :f}}}
               :d {:connections #{}}
               :e {:connections #{}}
               :f {:connections #{}}}
        start-node :a]
    (let [actual-result (remove-sub-graph graph start-node)
          expected-result {}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
