(ns webchange.editor-v2.graph-builder.graph-normalizer.graph-normalizer-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.graph-builder.graph-normalizer.graph-normalizer :refer [normalize-graph]]))

(deftest test-graph-normalizer--copy-repetitive-actions
  (let [graph {:a {:data        {:type "sequence"
                                 :data ["b" "c"]}
                   :path        [:a]
                   :connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b
                                   :sequence :a}}}
               :b {:data        {:type "sequence"
                                 :data ["d" "e" "d" "e"]}
                   :path        [:b]
                   :connections #{{:previous :a
                                   :name     "next"
                                   :handler  :d
                                   :sequence :b}}}
               :d {:data        {:type "empty"}
                   :path        [:d]
                   :connections #{{:previous :b
                                   :name     "next"
                                   :handler  :e
                                   :sequence :b}
                                  {:previous :e
                                   :name     "next"
                                   :handler  :e
                                   :sequence :b}}}
               :e {:data        {:type "empty"}
                   :path        [:e]
                   :connections #{{:previous :d
                                   :name     "next"
                                   :handler  :d
                                   :sequence :b}
                                  {:previous :d
                                   :name     "next"
                                   :handler  :c
                                   :sequence :a}}}
               :c {:data        {:type "empty"}
                   :path        [:c]
                   :connections #{}}}]
    (let [actual-result (normalize-graph graph)
          expected-result {:a        {:data        {:type "sequence"
                                                    :data ["b" "c"]}
                                      :path        [:a]
                                      :connections #{{:previous :root
                                                      :name     "next"
                                                      :handler  :b
                                                      :sequence :a}}}
                           :b        {:data        {:type "sequence"
                                                    :data ["d" "e" "d" "e"]}
                                      :path        [:b]
                                      :connections #{{:previous :a
                                                      :name     "next"
                                                      :handler  :d-copy-1
                                                      :sequence :b}}}
                           :d-copy-1 {:data        {:type "empty"}
                                      :path        [:d]
                                      :connections #{{:previous :b
                                                      :name     "next"
                                                      :handler  :e-copy-1
                                                      :sequence :b}}}
                           :d-copy-2 {:data        {:type "empty"}
                                      :path        [:d]
                                      :connections #{{:previous :e-copy-1
                                                      :name     "next"
                                                      :handler  :e-copy-2
                                                      :sequence :b}}}
                           :e-copy-1 {:data        {:type "empty"}
                                      :path        [:e]
                                      :connections #{{:previous :d-copy-1
                                                      :name     "next"
                                                      :handler  :d-copy-2
                                                      :sequence :b}}}
                           :e-copy-2 {:data        {:type "empty"}
                                      :path        [:e]
                                      :connections #{{:previous :d-copy-2
                                                      :name     "next"
                                                      :handler  :c
                                                      :sequence :a}}}
                           :c        {:data        {:type "empty"}
                                      :path        [:c]
                                      :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-graph-normalizer--copy-repetitive-actions-in-row
  (let [graph {:a {:data        {:type "sequence"
                                 :data ["b" "b" "c" "b"]}
                   :path        [:a]
                   :connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b
                                   :sequence :a}}}
               :b {:data        {:type "empty"}
                   :path        [:b]
                   :connections #{{:previous :a
                                   :name     "next"
                                   :handler  :b
                                   :sequence :a}
                                  {:previous :b
                                   :name     "next"
                                   :handler  :c
                                   :sequence :a}}}
               :c {:data        {:type "action"}
                   :path        [:c]
                   :connections #{{:previous :b
                                   :name     "next"
                                   :handler  :b
                                   :sequence :a}}}}]
    (let [actual-result (normalize-graph graph)
          expected-result {:a        {:data        {:type "sequence"
                                                    :data ["b" "b" "c" "b"]}
                                      :path        [:a]
                                      :connections #{{:previous :root
                                                      :name     "next"
                                                      :handler  :b-copy-1
                                                      :sequence :a}}}
                           :b-copy-1 {:data        {:type "empty"}
                                      :path        [:b]
                                      :connections #{{:previous :a
                                                      :name     "next"
                                                      :handler  :b-copy-2
                                                      :sequence :a}}}
                           :b-copy-2 {:data        {:type "empty"}
                                      :path        [:b]
                                      :connections #{{:previous :b-copy-1
                                                      :name     "next"
                                                      :handler  :c
                                                      :sequence :a}}}
                           :b-copy-3 {:data        {:type "empty"}
                                      :path        [:b]
                                      :connections #{}}
                           :c        {:data        {:type "action"}
                                      :path        [:c]
                                      :connections #{{:previous :b-copy-2
                                                      :name     "next"
                                                      :handler  :b-copy-3
                                                      :sequence :a}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-graph-normalizer--open-cycle
  (let [graph {:a {:data        {:type "sequence"
                                 :data ["b" "e"]}
                   :path        [:a]
                   :connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b
                                   :sequence :a}}}
               :b {:data        {:type "sequence"
                                 :data ["c" "d" "b"]}
                   :path        [:b]
                   :connections #{{:previous :a
                                   :name     "next"
                                   :handler  :e
                                   :sequence :a}
                                  {:previous :a
                                   :name     "next"
                                   :handler  :c
                                   :sequence :b}
                                  {:previous :d
                                   :name     "next"
                                   :handler  :c
                                   :sequence :b}}}
               :c {:data        {:type "empty"}
                   :path        [:c]
                   :connections #{{:previous :b
                                   :name     "next"
                                   :handler  :d
                                   :sequence :b}}}
               :d {:data        {:type "empty"}
                   :path        [:d]
                   :connections #{{:previous :c
                                   :name     "next"
                                   :handler  :b
                                   :sequence :b}}}
               :e {:data        {:type "empty"}
                   :path        [:e]
                   :connections #{}}}]
    (let [actual-result (normalize-graph graph)
          expected-result {:a {:data        {:type "sequence"
                                             :data ["b" "e"]}
                               :path        [:a]
                               :connections #{{:previous :root
                                               :name     "next"
                                               :handler  :b
                                               :sequence :a}}}
                           :b {:data        {:type "sequence"
                                             :data ["c" "d" "b"]}
                               :path        [:b]
                               :connections #{{:previous :a
                                               :name     "next"
                                               :handler  :e
                                               :sequence :a}
                                              {:previous :a
                                               :name     "next"
                                               :handler  :c
                                               :sequence :b}}}
                           :c {:data        {:type "empty"}
                               :path        [:c]
                               :connections #{{:previous :b
                                               :name     "next"
                                               :handler  :d
                                               :sequence :b}}}
                           :d {:data        {:type "empty"}
                               :path        [:d]
                               :connections #{{:previous :c
                                               :name     "next"
                                               :cycle-to :b
                                               :sequence :b}}}
                           :e {:data        {:type "empty"}
                               :path        [:e]
                               :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

(deftest test-graph-normalizer--open-2-step-cycle-with-repetitive-actions
  (let [graph {:a {:data        {:type "sequence"
                                 :data ["b" "e"]}
                   :path        [:a]
                   :connections #{{:previous :root
                                   :name     "next"
                                   :handler  :b
                                   :sequence :a}}}
               :b {:data        {:type "sequence"
                                 :data ["c" "d" "f"]}
                   :path        [:b]
                   :connections #{{:previous :a
                                   :name     "next"
                                   :handler  :e
                                   :sequence :a}
                                  {:previous :a
                                   :name     "next"
                                   :handler  :c
                                   :sequence :b}
                                  {:previous :d
                                   :name     "next"
                                   :handler  :c
                                   :sequence :b}}}
               :f {:data        {:type "sequence"
                                 :data ["c" "d" "b"]}
                   :path        [:f]
                   :connections #{{:previous :d
                                   :name     "next"
                                   :handler  :c
                                   :sequence :f}}}
               :c {:data        {:type "empty"}
                   :path        [:c]
                   :connections #{{:previous :b
                                   :name     "next"
                                   :handler  :d
                                   :sequence :b}
                                  {:previous :f
                                   :name     "next"
                                   :handler  :d
                                   :sequence :f}}}
               :d {:data        {:type "empty"}
                   :path        [:d]
                   :connections #{{:previous :c
                                   :name     "next"
                                   :handler  :f
                                   :sequence :b}
                                  {:previous :c
                                   :name     "next"
                                   :handler  :b
                                   :sequence :f}}}
               :e {:data        {:type "empty"}
                   :path        [:e]
                   :connections #{}}}]
    (let [actual-result (normalize-graph graph)
          expected-result {:a        {:data        {:type "sequence"
                                                    :data ["b" "e"]}
                                      :path        [:a]
                                      :connections #{{:previous :root
                                                      :name     "next"
                                                      :handler  :b
                                                      :sequence :a}}}
                           :b        {:data        {:type "sequence"
                                                    :data ["c" "d" "f"]}
                                      :path        [:b]
                                      :connections #{{:previous :a
                                                      :name     "next"
                                                      :handler  :e
                                                      :sequence :a}
                                                     {:previous :a
                                                      :name     "next"
                                                      :handler  :c-copy-1
                                                      :sequence :b}}}
                           :f        {:data        {:type "sequence"
                                                    :data ["c" "d" "b"]}
                                      :path        [:f]
                                      :connections #{{:previous :d-copy-1
                                                      :name     "next"
                                                      :handler  :c-copy-2
                                                      :sequence :f}}}
                           :c-copy-1 {:data        {:type "empty"}
                                      :path        [:c]
                                      :connections #{{:previous :b
                                                      :name     "next"
                                                      :handler  :d-copy-1
                                                      :sequence :b}}}
                           :c-copy-2 {:data        {:type "empty"}
                                      :path        [:c]
                                      :connections #{{:previous :f
                                                      :name     "next"
                                                      :handler  :d-copy-2
                                                      :sequence :f}}}
                           :d-copy-1 {:data        {:type "empty"}
                                      :path        [:d]
                                      :connections #{{:previous :c-copy-1
                                                      :name     "next"
                                                      :handler  :f
                                                      :sequence :b}}}
                           :d-copy-2 {:data        {:type "empty"}
                                      :path        [:d]
                                      :connections #{{:previous :c-copy-2
                                                      :name     "next"
                                                      :cycle-to :b
                                                      :sequence :f}}}
                           :e        {:data        {:type "empty"}
                                      :path        [:e]
                                      :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
