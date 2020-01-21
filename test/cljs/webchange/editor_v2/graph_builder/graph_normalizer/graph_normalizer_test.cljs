(ns webchange.editor-v2.graph-builder.graph-normalizer.graph-normalizer-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.graph-builder.graph-normalizer.graph-normalizer :refer [normalize-graph]]))

(deftest test-graph-normalizer--copy-repetitive-actions
  (let [graph {:a {:data        {:type "sequence"
                                 :data ["b" "c" "f"]}
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
                   :connections #{{:previous :e
                                   :name     "next"
                                   :handler  :f
                                   :sequence :a}}}
               :f {:data        {:type "empty"}
                   :path        [:f]
                   :connections #{}}}]
    (let [actual-result (normalize-graph graph)
          expected-result {:a        {:data        {:type "sequence"
                                                    :data ["b" "c" "f"]}
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
                                                      :handler  :d-copy-2
                                                      :sequence :b}}}
                           :d-copy-2 {:data         {:type "empty"}
                                      :path         [:d]
                                      :origin-name  :d
                                      :copy-counter 2
                                      :connections  #{{:previous :b
                                                       :name     "next"
                                                       :handler  :e-copy-2
                                                       :sequence :b}}}
                           :d-copy-1 {:data         {:type "empty"}
                                      :path         [:d]
                                      :origin-name  :d
                                      :copy-counter 1
                                      :connections  #{{:previous :e-copy-2
                                                       :name     "next"
                                                       :handler  :e-copy-1
                                                       :sequence :b}}}
                           :e-copy-2 {:data         {:type "empty"}
                                      :path         [:e]
                                      :origin-name  :e
                                      :copy-counter 2
                                      :connections  #{{:previous :d-copy-2
                                                       :name     "next"
                                                       :handler  :d-copy-1
                                                       :sequence :b}}}
                           :e-copy-1 {:data         {:type "empty"}
                                      :path         [:e]
                                      :origin-name  :e
                                      :copy-counter 1
                                      :connections  #{{:previous :d-copy-1
                                                       :name     "next"
                                                       :handler  :c
                                                       :sequence :a}}}
                           :c        {:data        {:type "empty"}
                                      :path        [:c]
                                      :connections #{{:previous :e-copy-1
                                                      :name     "next"
                                                      :handler  :f
                                                      :sequence :a}}}
                           :f        {:data        {:type "empty"}
                                      :path        [:f]
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
                                                      :handler  :b-copy-3
                                                      :sequence :a}}}
                           :b-copy-3 {:data         {:type "empty"}
                                      :path         [:b]
                                      :origin-name  :b
                                      :copy-counter 3
                                      :connections  #{{:previous :a
                                                       :name     "next"
                                                       :handler  :b-copy-2
                                                       :sequence :a}}}
                           :b-copy-2 {:data         {:type "empty"}
                                      :path         [:b]
                                      :origin-name  :b
                                      :copy-counter 2
                                      :connections  #{{:previous :b-copy-3
                                                       :name     "next"
                                                       :handler  :c
                                                       :sequence :a}}}
                           :b-copy-1 {:data         {:type "empty"}
                                      :path         [:b]
                                      :origin-name  :b
                                      :copy-counter 1
                                      :connections  #{}}
                           :c        {:data        {:type "action"}
                                      :path        [:c]
                                      :connections #{{:previous :b-copy-2
                                                      :name     "next"
                                                      :handler  :b-copy-1
                                                      :sequence :a}}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))

;(deftest test-graph-normalizer--do-not-copy-after-parallel-action
;  (let [graph {:x   {:connections #{{:previous :root
;                                     :name     "next"
;                                     :handler  :a
;                                     :sequence :x}}}
;               :a   {:connections #{{:previous :x
;                                     :name     "success"
;                                     :handler  :b
;                                     :sequence :x}
;                                    {:previous :x
;                                     :name     "fail"
;                                     :handler  :c
;                                     :sequence :x}}}
;               :b   {:data        {:type "parallel"}
;                     :connections #{{:previous :a
;                                     :name     "next"
;                                     :handler  :b-0
;                                     :sequence :b}
;                                    {:previous :a
;                                     :name     "next"
;                                     :handler  :b-1
;                                     :sequence :b}}}
;               :b-0 {:data        {:type "empty"}
;                     :connections #{{:previous :b
;                                     :name     "next"
;                                     :handler  :d
;                                     :sequence :x}}}
;               :b-1 {:data        {:type "empty"}
;                     :connections #{{:previous :b
;                                     :name     "next"
;                                     :handler  :d
;                                     :sequence :x}}}
;               :c   {:data        {:type "empty"}
;                     :connections #{{:previous :a
;                                     :name     "next"
;                                     :handler  :d
;                                     :sequence :x}}}
;               :d   {:data        {:type "empty"}
;                     :connections #{{:previous :b-0
;                                     :name     "next"
;                                     :handler  :e
;                                     :sequence :x}
;                                    {:previous :b-1
;                                     :name     "next"
;                                     :handler  :e
;                                     :sequence :x}
;                                    {:previous :c
;                                     :name     "next"
;                                     :handler  :e
;                                     :sequence :x}}}
;               :e   {:data        {:type "empty"}
;                     :connections #{{:previous :d
;                                     :name     "next"
;                                     :handler  :f
;                                     :sequence :x}}}
;               :f   {:data        {:type "empty"}
;                     :connections #{}}}]
;    (let [actual-result (normalize-graph graph)
;          expected-result {
;                           :x        {:connections #{{:previous :root
;                                                      :name     "next"
;                                                      :handler  :a
;                                                      :sequence :x}}}
;                           :a        {:connections #{{:previous :x
;                                                      :name     "fail"
;                                                      :handler  :c
;                                                      :sequence :x}
;                                                     {:previous :x
;                                                      :name     "success"
;                                                      :handler  :b
;                                                      :sequence :x}}}
;                           :b        {:data        {:type "parallel"}
;                                      :connections #{{:previous :a
;                                                      :name     "next"
;                                                      :handler  :b-0
;                                                      :sequence :b}
;                                                     {:previous :a
;                                                      :name     "next"
;                                                      :handler  :b-1
;                                                      :sequence :b}}}
;                           :b-0      {:data        {:type "empty"}
;                                      :connections #{{:previous :b
;                                                      :name     "next"
;                                                      :handler  :d-copy-1
;                                                      :sequence :x}}}
;                           :b-1      {:data        {:type "empty"}
;                                      :connections #{{:previous :b
;                                                      :name     "next"
;                                                      :handler  :d-copy-1
;                                                      :sequence :x}}}
;                           :d-copy-1 {:data         {:type "empty"}
;                                      :connections  #{{:previous :b-0
;                                                       :name     "next"
;                                                       :handler  :e-copy-1
;                                                       :sequence :x}
;                                                      {:previous :b-1
;                                                       :name     "next"
;                                                       :handler  :e-copy-1
;                                                       :sequence :x}}
;                                      :origin-name  :d
;                                      :copy-counter 1}
;                           :e-copy-1 {:data         {:type "empty"}
;                                      :connections  #{{:previous :d-copy-1
;                                                       :name     "next"
;                                                       :handler  :f
;                                                       :sequence :x}}
;                                      :origin-name  :e
;                                      :copy-counter 1}
;                           :c        {:data        {:type "empty"}
;                                      :connections #{{:previous :a
;                                                      :name     "next"
;                                                      :handler  :d-copy-2
;                                                      :sequence :x}}}
;                           :d-copy-2 {:data         {:type "empty"}
;                                      :connections  #{{:previous :c
;                                                       :name     "next"
;                                                       :handler  :e-copy-2
;                                                       :sequence :x}}
;                                      :origin-name  :d
;                                      :copy-counter 2}
;
;                           :e-copy-2 {:data         {:type "empty"}
;                                      :connections  #{{:previous :d-copy-2
;                                                       :name     "next"
;                                                       :handler  :f
;                                                       :sequence :x}}
;                                      :origin-name  :e
;                                      :copy-counter 2}
;                           :f        {:data        {:type "empty"}
;                                      :connections #{}}}]
;      (when-not (= actual-result expected-result)
;        (print-maps-comparison actual-result expected-result))
;      (is (= actual-result expected-result)))))

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
                                                      :handler  :c-copy-2
                                                      :sequence :b}}}
                           :f        {:data        {:type "sequence"
                                                    :data ["c" "d" "b"]}
                                      :path        [:f]
                                      :connections #{{:previous :d-copy-2
                                                      :name     "next"
                                                      :handler  :c-copy-1
                                                      :sequence :f}}}
                           :c-copy-2 {:data         {:type "empty"}
                                      :path         [:c]
                                      :origin-name  :c
                                      :copy-counter 2
                                      :connections  #{{:previous :b
                                                       :name     "next"
                                                       :handler  :d-copy-2
                                                       :sequence :b}}}
                           :c-copy-1 {:data         {:type "empty"}
                                      :path         [:c]
                                      :origin-name  :c
                                      :copy-counter 1
                                      :connections  #{{:previous :f
                                                       :name     "next"
                                                       :handler  :d-copy-1
                                                       :sequence :f}}}
                           :d-copy-2 {:data         {:type "empty"}
                                      :path         [:d]
                                      :origin-name  :d
                                      :copy-counter 2
                                      :connections  #{{:previous :c-copy-2
                                                       :name     "next"
                                                       :handler  :f
                                                       :sequence :b}}}
                           :d-copy-1 {:data         {:type "empty"}
                                      :path         [:d]
                                      :origin-name  :d
                                      :copy-counter 1
                                      :connections  #{{:previous :c-copy-1
                                                       :name     "next"
                                                       :cycle-to :b
                                                       :sequence :f}}}
                           :e        {:data        {:type "empty"}
                                      :path        [:e]
                                      :connections #{}}}]
      (when-not (= actual-result expected-result)
        (print-maps-comparison actual-result expected-result))
      (is (= actual-result expected-result)))))
