(ns webchange.editor-v2.graph-builder.utils.node-children-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]))

(deftest test-get-children--defined-parent
  (let [node-name :a
        node-data {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b
                                   :sequence :a}
                                  {:previous :a
                                   :name     "next"
                                   :handler  :e
                                   :sequence :a}
                                  {:previous :a
                                   :name     "next"
                                   :handler  :c
                                   :sequence :b}}}
        prev-node :a]
    (let [actual-result (get-children node-name node-data prev-node)
          expected-result #{{:previous :a
                             :name     "next"
                             :handler  :e
                             :sequence :a}
                            {:previous :a
                             :name     "next"
                             :handler  :c
                             :sequence :b}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-children--not-defined-parent
  (let [node-name :a
        node-data {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b
                                   :sequence :a}
                                  {:previous :a
                                   :name     "next"
                                   :handler  :e
                                   :sequence :a}
                                  {:previous :a
                                   :name     "next"
                                   :handler  :c
                                   :sequence :b}}}]
    (let [actual-result (get-children node-name node-data)
          expected-result #{{:previous :root
                             :name     "next"
                             :handler  :b
                             :sequence :a}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-children--nil-parent
  (let [node-name :a
        node-data {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b
                                   :sequence :a}
                                  {:previous :a
                                   :name     "next"
                                   :handler  :e
                                   :sequence :a}
                                  {:previous :a
                                   :name     "next"
                                   :handler  :c
                                   :sequence :b}}}
        previous-node nil]
    (let [actual-result (get-children node-name node-data previous-node)
          expected-result #{{:previous :root
                             :name     "next"
                             :handler  :b
                             :sequence :a}
                            {:previous :a
                             :name     "next"
                             :handler  :e
                             :sequence :a}
                            {:previous :a
                             :name     "next"
                             :handler  :c
                             :sequence :b}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-children--get-handlers-of-last-seq
  (let [node-name :e
        node-data {:connections #{{:previous :f
                                   :name     "next"
                                   :handler  :d
                                   :sequence :b}
                                  {:previous :d
                                   :name     "next"
                                   :handler  :c
                                   :sequence :a}
                                  {:previous :d
                                   :name     "next"
                                   :handler  :g
                                   :sequence :b}}}
        previous-node :d
        sequence-path [:a :b]]
    (let [actual-result (get-children node-name node-data previous-node sequence-path)
          expected-result #{{:previous :d
                             :name     "next"
                             :handler  :g
                             :sequence :b}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-get-children--get-handlers-of-prev-seq-if-no-last
  (let [node-name :e
        node-data {:connections #{{:previous :f
                                   :name     "next"
                                   :handler  :d
                                   :sequence :b}
                                  {:previous :d
                                   :name     "next"
                                   :handler  :c
                                   :sequence :a}}}
        previous-node :d
        sequence-path [:a :b]]
    (let [actual-result (get-children node-name node-data previous-node sequence-path)
          expected-result #{{:previous :d
                             :name     "next"
                             :handler  :c
                             :sequence :a}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
