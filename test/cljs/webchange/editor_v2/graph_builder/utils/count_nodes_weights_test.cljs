(ns webchange.editor-v2.graph-builder.utils.count-nodes-weights-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.graph-builder.utils.count-nodes-weights :refer [weight-changer?]]))

(deftest test-remove-connection--last-empty
  (let [node-weights [0]]
    (let [actual-result (weight-changer? node-weights)
          expected-result false]
      (is (= actual-result expected-result)))))

(deftest test-remove-connection--last-not-empty
  (let [node-weights [1]]
    (let [actual-result (weight-changer? node-weights)
          expected-result true]
      (is (= actual-result expected-result)))))

(deftest test-remove-connection--middle-one-child-empty
  (let [node-weights [1 1]]
    (let [actual-result (weight-changer? node-weights)
          expected-result false]
      (is (= actual-result expected-result)))))

(deftest test-remove-connection--middle-one-child-not-empty
  (let [node-weights [2 1]]
    (let [actual-result (weight-changer? node-weights)
          expected-result true]
      (is (= actual-result expected-result)))))

(deftest test-remove-connection--middle-two-children-empty
  (let [node-weights [2 2 1]]
    (let [actual-result (weight-changer? node-weights)
          expected-result false]
      (is (= actual-result expected-result)))))

(deftest test-remove-connection--middle-two-children-not-empty
  (let [node-weights [3 2 1]]
    (let [actual-result (weight-changer? node-weights)
          expected-result true]
      (is (= actual-result expected-result)))))

;(deftest test-count-subtree-phrases
;  (let [graph {:root {:connections #{{:previous :root
;                                      :name     "next"
;                                      :handler  :01}
;                                     {:previous :root
;                                      :name     "next"
;                                      :handler  :02}}}
;               :01   {:connections #{{:previous :root
;                                      :name     "next"
;                                      :handler  :03}}}
;               :02   {:connections #{{:previous :root
;                                      :name     "next"
;                                      :handler  :04}}}
;               :03   {:connections #{{:previous :01
;                                      :name     "true"
;                                      :handler  :05}
;                                     {:previous :01
;                                      :name     "false"
;                                      :handler  :07}}}
;               :04   {:connections #{{:previous :02
;                                      :name     "true"
;                                      :handler  :06}
;                                     {:previous :02
;                                      :name     "false"
;                                      :handler  :07}}}
;               :05   {:connections #{{:previous :03
;                                      :name     "next"
;                                      :handler  :08}}}
;               :06   {:connections #{{:previous :04
;                                      :name     "next"
;                                      :handler  :08}}}
;               :07   {:connections #{{:previous :03
;                                      :name     "next"
;                                      :handler  :09}
;                                     {:previous :04
;                                      :name     "next"
;                                      :handler  :09}}
;                      :data        {:phrase true}}
;               :08   {:connections #{{:previous :05
;                                      :name     "next"
;                                      :handler  :10}
;                                     {:previous :06
;                                      :name     "next"
;                                      :handler  :10}}}
;               :09   {:connections #{}}
;               :10   {:connections #{{:previous :08
;                                      :name     "next"
;                                      :handler  :11}}
;                      :data        {:phrase true}}
;               :11   {:connections #{{:previous :10
;                                      :name     "next"
;                                      :handler  :12}}}
;               :12   {:connections #{{:previous :11
;                                      :name     "next"
;                                      :handler  :13}}
;                      :data        {:phrase true}}
;               :13   {:connections #{}}}]
;    (let [actual-result (count-subtree-phrases graph)
;          expected-result {:root [6 3 3]
;                           :01   [3 3]
;                           :02   [3 3]
;                           :03   [3 2 1]
;                           :04   [3 1 2]
;                           :05   [2 2]
;                           :06   [2 2]
;                           :07   [1 0]
;                           :08   [2 2]
;                           :09   [0]
;                           :10   [2 1]
;                           :11   [1 1]
;                           :12   [1 0]
;                           :13   [0]}]
;      (when-not (= actual-result expected-result)
;        (print-maps-comparison actual-result expected-result))
;      (is (= actual-result expected-result)))))
