(ns webchange.editor-v2.graph-builder.utils.insert-sub-graph-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.graph-builder.utils.insert-sub-graph :refer [insert-sub-graph]]))

(deftest insert-sub-graph--insert-single-node
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b}}}
               :b {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}}}
               :c {:connections #{}}}
        start-node-name :a
        start-node-connections [{:previous :root
                                 :name     "next"
                                 :handler  :b}]
        sub-graph {:d {:connections #{{:previous :prev-action
                                       :name     "next"
                                       :handler  :next-action}}}}]
    (let [actual-result (insert-sub-graph graph start-node-name start-node-connections sub-graph)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :d}}}
                           :d {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :b}}}
                           :b {:connections #{{:previous :d
                                               :name     "next"
                                               :handler  :c}}}
                           :c {:connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest insert-sub-graph--insert-single-node-as-last-node
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b}}}
               :b {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}}}
               :c {:connections #{}}}
        start-node-name :c
        start-node-connections [{:previous :b}]
        sub-graph {:d {:connections #{{:previous :prev-action
                                       :name     "next"
                                       :handler  :next-action}}}}]
    (let [actual-result (insert-sub-graph graph start-node-name start-node-connections sub-graph)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :b}}}
                           :b {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :c}}}
                           :c {:connections #{{:previous :b
                                               :name     "next"
                                               :handler  :d}}}
                           :d {:connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest insert-sub-graph--insert-single-node-as-pre-last-node
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b}}}
               :b {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}}}
               :c {:connections #{}}}
        start-node-name :b
        start-node-connections [{:previous :a
                                 :name     "next"
                                 :handler  :c}]
        sub-graph {:d {:connections #{{:previous :prev-action
                                       :name     "next"
                                       :handler  :next-action}}}}]
    (let [actual-result (insert-sub-graph graph start-node-name start-node-connections sub-graph)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :b}}}
                           :b {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :d}}}
                           :d {:connections #{{:previous :b
                                               :name     "next"
                                               :handler  :c}}}
                           :c {:connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest insert-sub-graph--insert-graph
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b}}}
               :b {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}}}
               :c {:connections #{}}}
        start-node-name :a
        start-node-connections [{:previous :root
                                 :name     "next"
                                 :handler  :b}]
        sub-graph {:d   {:connections #{{:previous :prev-action
                                         :name     "next"
                                         :handler  :d-0
                                         :sequence :d}
                                        {:previous :prev-action
                                         :name     "next"
                                         :handler  :d-1
                                         :sequence :d}}}
                   :d-0 {:connections #{{:previous :d
                                         :name     "next"
                                         :handler  :next-action}}}
                   :d-1 {:connections #{{:previous :d
                                         :name     "next"
                                         :handler  :next-action}}}}]
    (let [actual-result (insert-sub-graph graph start-node-name start-node-connections sub-graph)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :d}}}
                           :d   {:connections #{{:previous :a
                                                 :name     "next"
                                                 :handler  :d-0
                                                 :sequence :d}
                                                {:previous :a
                                                 :name     "next"
                                                 :handler  :d-1
                                                 :sequence :d}}}
                           :d-0 {:connections #{{:previous :d
                                                 :name     "next"
                                                 :handler  :b}}}
                           :d-1 {:connections #{{:previous :d
                                                 :name     "next"
                                                 :handler  :b}}}
                           :b {:connections #{{:previous :d-0
                                               :name     "next"
                                               :handler  :c}
                                              {:previous :d-1
                                               :name     "next"
                                               :handler  :c}}}
                           :c {:connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest insert-sub-graph--insert-graph-as-last-nodes
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b}}}
               :b {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}}}
               :c {:connections #{}}}
        start-node-name :c
        start-node-connections [{:previous :b}]
        sub-graph {:d   {:connections #{{:previous :prev-action
                                         :name     "next"
                                         :handler  :d-0
                                         :sequence :d}
                                        {:previous :prev-action
                                         :name     "next"
                                         :handler  :d-1
                                         :sequence :d}}}
                   :d-0 {:connections #{{:previous :d
                                         :name     "next"
                                         :handler  :next-action}}}
                   :d-1 {:connections #{{:previous :d
                                         :name     "next"
                                         :handler  :next-action}}}}]
    (let [actual-result (insert-sub-graph graph start-node-name start-node-connections sub-graph)
          expected-result {:a   {:connections #{{:previous :root
                                                 :name     "next"
                                                 :handler  :b}}}
                           :b   {:connections #{{:previous :a
                                                 :name     "next"
                                                 :handler  :c}}}
                           :c   {:connections #{{:previous :b
                                                 :name     "next"
                                                 :handler  :d}}}
                           :d   {:connections #{{:previous :c
                                                 :name     "next"
                                                 :handler  :d-0
                                                 :sequence :d}
                                                {:previous :c
                                                 :name     "next"
                                                 :handler  :d-1
                                                 :sequence :d}}}
                           :d-0 {:connections #{}}
                           :d-1 {:connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest insert-sub-graph--insert-graph-as-pre-last-nodes
  (let [graph {:a {:connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b}}}
               :b {:connections #{{:previous :a
                                   :name     "next"
                                   :handler  :c}}}
               :c {:connections #{}}}
        start-node-name :b
        start-node-connections [{:previous :a
                                 :name     "next"
                                 :handler  :c}]
        sub-graph {:d   {:connections #{{:previous :prev-action
                                         :name     "next"
                                         :handler  :d-0
                                         :sequence :d}
                                        {:previous :prev-action
                                         :name     "next"
                                         :handler  :d-1
                                         :sequence :d}}}
                   :d-0 {:connections #{{:previous :d
                                         :name     "next"
                                         :handler  :next-action}}}
                   :d-1 {:connections #{{:previous :d
                                         :name     "next"
                                         :handler  :next-action}}}}]
    (let [actual-result (insert-sub-graph graph start-node-name start-node-connections sub-graph)
          expected-result {:a {:connections #{{:previous :root
                                               :name     "next"
                                               :handler  :b}}}
                           :b {:connections #{{:previous :a
                                               :name     "next"
                                               :handler  :d}}}
                           :d   {:connections #{{:previous :b
                                                 :name     "next"
                                                 :handler  :d-0
                                                 :sequence :d}
                                                {:previous :b
                                                 :name     "next"
                                                 :handler  :d-1
                                                 :sequence :d}}}
                           :d-0 {:connections #{{:previous :d
                                                 :name     "next"
                                                 :handler  :c}}}
                           :d-1 {:connections #{{:previous :d
                                                 :name     "next"
                                                 :handler  :c}}}
                           :c {:connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
